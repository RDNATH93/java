package langchain.rag.advanced;

import static langchain.rag.shared.Utils.startConversationWith;
import static langchain.rag.shared.Utils.toPath;

import java.nio.file.Path;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jspecify.annotations.NonNull;

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
import langchain.chat_language_model.OpenAIModelDemo;
import langchain.rag.shared.Assistant;

public class AdvancedRAGWithQueryRoutingDemo {
    private static final String GOOGLE_OPENAI_URL = "https://generativelanguage.googleapis.com/v1beta/openai";
    private static final String OPENROUTER_URL = "https://openrouter.ai/api/v1";

    public static void main(String[] args) {
        Assistant assistant = createAssistant();
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

        EmbeddingStore<TextSegment> biographyEmbeddingStore =
        embed(toPath("documents/biography-of-john-doe.txt"),embeddingModel);

        EmbeddingStoreContentRetriever biographyContentRetriever = EmbeddingStoreContentRetriever.builder()
                .displayName("BiographyContentRetriver")
                .embeddingModel(embeddingModel)
                .embeddingStore(biographyEmbeddingStore)
                .maxResults(2)
                .minScore(0.5)
                .build();

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
