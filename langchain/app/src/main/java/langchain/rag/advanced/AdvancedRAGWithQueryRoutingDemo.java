package langchain.rag.advanced;

import static langchain.rag.shared.Utils.startConversationWith;
import static langchain.rag.shared.Utils.toPath;

import java.nio.file.Path;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import dev.langchain4j.data.document.Document;
import dev.langchain4j.data.document.DocumentSplitter;
import dev.langchain4j.data.document.loader.FileSystemDocumentLoader;
import dev.langchain4j.data.document.parser.TextDocumentParser;
import dev.langchain4j.data.document.splitter.DocumentSplitters;
import dev.langchain4j.data.embedding.Embedding;
import dev.langchain4j.data.segment.TextSegment;
import dev.langchain4j.memory.chat.MessageWindowChatMemory;
import dev.langchain4j.model.chat.ChatModel;
import dev.langchain4j.model.embedding.EmbeddingModel;
import dev.langchain4j.model.embedding.onnx.bgesmallenv15q.BgeSmallEnV15QuantizedEmbeddingModel;
import dev.langchain4j.model.openai.OpenAiChatModel;
import dev.langchain4j.rag.DefaultRetrievalAugmentor;
import dev.langchain4j.rag.RetrievalAugmentor;
import dev.langchain4j.rag.content.retriever.ContentRetriever;
import dev.langchain4j.rag.content.retriever.EmbeddingStoreContentRetriever;
import dev.langchain4j.rag.query.router.LanguageModelQueryRouter;
import dev.langchain4j.rag.query.router.QueryRouter;
import dev.langchain4j.service.AiServices;
import dev.langchain4j.store.embedding.EmbeddingStore;
import dev.langchain4j.store.embedding.inmemory.InMemoryEmbeddingStore;
import langchain.rag.shared.Assistant;


/**
     * Please refer to {@link Naive_RAG_Example} for a basic context.
     * <p>
     * Advanced RAG in LangChain4j is described here: https://github.com/langchain4j/langchain4j/pull/538
     * <p>
     * This example showcases the implementation of a more advanced RAG application
     * using a technique known as "query routing".
     * <p>
     * Often, private data is spread across multiple sources and formats.
     * This might include internal company documentation on Confluence, your project's code in a Git repository,
     * a relational database with user data, or a search engine with the products you sell, among others.
     * In a RAG flow that utilizes data from multiple sources, you will likely have multiple
     * {@link EmbeddingStore}s or {@link ContentRetriever}s.
     * While you could route each user query to all available {@link ContentRetriever}s,
     * this approach might be inefficient and counterproductive.
     * <p>
     * "Query routing" is the solution to this challenge. It involves directing a query to the most appropriate
     * {@link ContentRetriever} (or several). Routing can be implemented in various ways:
     * - Using rules (e.g., depending on the user's privileges, location, etc.).
     * - Using keywords (e.g., if a query contains words X1, X2, X3, route it to {@link ContentRetriever} X, etc.).
     * - Using semantic similarity (see EmbeddingModelTextClassifierExample in this repository).
     * - Using an LLM to make a routing decision.
     * <p>
     * For scenarios 1, 2, and 3, you can implement a custom {@link QueryRouter}.
     * For scenario 4, this example will demonstrate how to use a {@link LanguageModelQueryRouter}.
     */

public class AdvancedRAGWithQueryRoutingDemo {
    private static final String GOOGLE_OPENAI_URL = "https://generativelanguage.googleapis.com/v1beta/openai";
    private static final String OPENROUTER_URL = "https://openrouter.ai/api/v1";

    public static void main(String[] args) {
        Assistant assistant = createAssistant();

        // First, ask "What is the legacy of John Doe?"
        // Then, ask "Can I cancel my reservation?"
        // Now, see the logs to observe how the queries are routed to different retrievers.
        startConversationWith(assistant);
    }

    private static Assistant createAssistant() {

        ChatModel model = OpenAiChatModel.builder()
                .baseUrl(GOOGLE_OPENAI_URL)
                .apiKey(System.getenv("GOOGLE_API_KEY"))
                .modelName("gemini-2.5-flash")
                .logRequests(true)
                .logResponses(true)
                .build();

        ChatModel queryRoutingmodel = OpenAiChatModel.builder()
                .baseUrl(OPENROUTER_URL)
                .apiKey(System.getenv("OPENROUTER_API_KEY"))
                .modelName("google/gemma-3-27b-it:free")
                .logRequests(true)
                .logResponses(true)
                .build();

        EmbeddingModel embeddingModel = new BgeSmallEnV15QuantizedEmbeddingModel();

        // Let's create a separate embedding store specifically for biographies.
        EmbeddingStore<TextSegment> biographyEmbeddingStore =
        embed(toPath("documents/biography-of-john-doe.txt"),embeddingModel);

        EmbeddingStoreContentRetriever biographyContentRetriever = EmbeddingStoreContentRetriever.builder()
                .displayName("BiographyContentRetriver")
                .embeddingModel(embeddingModel)
                .embeddingStore(biographyEmbeddingStore)
                .maxResults(2)
                .minScore(0.5)
                .build();

        // Additionally, let's create a separate embedding store dedicated to terms of use.                
        EmbeddingStore<TextSegment> termsAndConditionsEmbeddingStore = 
        embed(toPath("documents/miles-of-smiles-terms-of-use.txt"), embeddingModel);

        EmbeddingStoreContentRetriever termsAndConditionsContentRetriever = EmbeddingStoreContentRetriever.builder()
                .displayName("TermsOfUseContentRetriver")
                .embeddingModel(embeddingModel)
                .embeddingStore(termsAndConditionsEmbeddingStore)
                .maxResults(2)
                .minScore(0.5)
                .build();

        // Let's create a query router.
        Map<ContentRetriever, String> retrieverToDescription = new HashMap<>();
        retrieverToDescription.put(biographyContentRetriever, "biography of John Doe");
        retrieverToDescription.put(termsAndConditionsContentRetriever, "terms of use of car rental company");

        QueryRouter languageModelQueryRouter = LanguageModelQueryRouter.builder()
            .chatModel(queryRoutingmodel)
            .retrieverToDescription(retrieverToDescription)
            .build();
  
        LoggingQueryRouter loggingQueryRouter = new LoggingQueryRouter(languageModelQueryRouter);    

        RetrievalAugmentor augmentor = DefaultRetrievalAugmentor.builder()
            .queryRouter(loggingQueryRouter)
            .build();

        return AiServices.builder(Assistant.class)
                .chatModel(model)
                .chatMemory(MessageWindowChatMemory.withMaxMessages(10))
                .retrievalAugmentor(augmentor)
                .build();
    }

    private static EmbeddingStore<TextSegment> embed(Path documentPath, EmbeddingModel embeddingModel) {
        Document document = FileSystemDocumentLoader.loadDocument(documentPath, new TextDocumentParser());
        DocumentSplitter splitter = DocumentSplitters.recursive(300, 0);
        List<TextSegment> textSegments = splitter.split(document);

        List<Embedding> embeddings = embeddingModel.embedAll(textSegments).content();

        EmbeddingStore<TextSegment> embeddingStore = new InMemoryEmbeddingStore<>();
        embeddingStore.addAll(embeddings, textSegments);

        return embeddingStore;
    }
}
