package langchain.rag.advanced;

import static langchain.rag.shared.Utils.startConversationWith;
import static langchain.rag.shared.Utils.toPath;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

import dev.langchain4j.data.document.Document;
import dev.langchain4j.data.document.loader.FileSystemDocumentLoader;
import dev.langchain4j.data.document.splitter.DocumentSplitters;
import dev.langchain4j.data.message.AiMessage;
import dev.langchain4j.data.segment.TextSegment;
import dev.langchain4j.memory.chat.MessageWindowChatMemory;
import dev.langchain4j.model.chat.ChatModel;
import dev.langchain4j.model.embedding.EmbeddingModel;
import dev.langchain4j.model.embedding.onnx.bgesmallenv15q.BgeSmallEnV15QuantizedEmbeddingModel;
import dev.langchain4j.model.input.Prompt;
import dev.langchain4j.model.input.PromptTemplate;
import dev.langchain4j.model.openai.OpenAiChatModel;
import dev.langchain4j.rag.DefaultRetrievalAugmentor;
import dev.langchain4j.rag.RetrievalAugmentor;
import dev.langchain4j.rag.content.retriever.ContentRetriever;
import dev.langchain4j.rag.content.retriever.EmbeddingStoreContentRetriever;
import dev.langchain4j.rag.query.Query;
import dev.langchain4j.rag.query.router.QueryRouter;
import dev.langchain4j.service.AiServices;
import dev.langchain4j.store.embedding.EmbeddingStore;
import dev.langchain4j.store.embedding.EmbeddingStoreIngestor;
import dev.langchain4j.store.embedding.inmemory.InMemoryEmbeddingStore;
import langchain.rag.shared.Assistant;


 /**
     * Please refer to {@link Naive_RAG_Example} for a basic context.
     * <p>
     * Advanced RAG in LangChain4j is described here: https://github.com/langchain4j/langchain4j/pull/538
     * <p>
     * This example demonstrates how to conditionally skip retrieval.
     * Sometimes, retrieval is unnecessary, for instance, when a user simply says "Hi".
     * <p>
     * There are multiple ways to implement this, but the simplest one is to use a custom {@link QueryRouter}.
     * When retrieval should be skipped, QueryRouter will return an empty list,
     * meaning that the query will not be routed to any {@link ContentRetriever}.
     * <p>
     * Decision-making can be implemented in various ways:
     * - Using rules (e.g., depending on the user's privileges, location, etc.).
     * - Using keywords (e.g., if a query contains specific words).
     * - Using semantic similarity (see EmbeddingModelTextClassifierExample in this repository).
     * - Using an LLM to make a decision.
     * <p>
     * In this example, we will use an LLM to decide whether a user query should do retrieval or not.
     */
public class AdvancedRAGSkipRetrievalDemo {
    public static final String GOOGLE_OPENAI_URL = "https://generativelanguage.googleapis.com/v1beta/openai";

    public static void main(String[] args) {
        Assistant assistant = createAssistant();
        
        // First, say "Hi"
        // Notice how this query is not routed to any retrievers.

        // Now, ask "Can I cancel my reservation?"
        // This query has been routed to our retriever.
        startConversationWith(assistant);
    }

    private static Assistant createAssistant() {

        ChatModel chatModel = OpenAiChatModel.builder()
                .baseUrl(GOOGLE_OPENAI_URL)
                .apiKey(System.getenv("GOOGLE_API_KEY"))
                .modelName("gemini-2.5-flash")
                .logRequests(true)
                .logResponses(true)
                .build();

        EmbeddingModel embeddingModel = new BgeSmallEnV15QuantizedEmbeddingModel();
        EmbeddingStore<TextSegment> embeddingStore = embed(toPath("documents/miles-of-smiles-terms-of-use.txt"),
                embeddingModel);

        ContentRetriever contentRetriever = EmbeddingStoreContentRetriever.builder()
                .embeddingModel(embeddingModel)
                .embeddingStore(embeddingStore)
                .maxResults(2)
                .minScore(0.6)
                .build();

                
        // Let's create a query router.
        QueryRouter queryRouter = new QueryRouter() {

            PromptTemplate PROMPT_TEMPLATE = PromptTemplate.from("""
                    Is the following query related to the business of the car rental company?
                    Answer only 'yes', 'no' or 'maybe'.
                    Query: {{it}}
                    """);

            @Override
            public Collection<ContentRetriever> route(Query query) {
                Prompt prompt = PROMPT_TEMPLATE.apply(query.text());
                AiMessage aiMessage = chatModel.chat(prompt.toUserMessage()).aiMessage();
                System.out.println("LLM decided: " + aiMessage.text());

                if (aiMessage.text().toLowerCase().contains("no")) {
                    return new ArrayList<>();
                }
                return Collections.singletonList(contentRetriever);
            }
        };

        RetrievalAugmentor retrievalAugmentor = DefaultRetrievalAugmentor.builder()
                .queryRouter(queryRouter)
                .build();

        return AiServices.builder(Assistant.class)
                .chatModel(chatModel)
                .chatMemory(MessageWindowChatMemory.withMaxMessages(10))
                .retrievalAugmentor(retrievalAugmentor)
                .build();

    }

    private static EmbeddingStore<TextSegment> embed(Path path, EmbeddingModel embeddingModel) {
        EmbeddingStore<TextSegment> embeddingStore = new InMemoryEmbeddingStore<>();
        Document document = FileSystemDocumentLoader.loadDocument(path);

        EmbeddingStoreIngestor embeddingStoreIngestor = EmbeddingStoreIngestor.builder()
                .documentSplitter(DocumentSplitters.recursive(300, 0))
                .embeddingModel(embeddingModel)
                .embeddingStore(embeddingStore)
                .build();

        embeddingStoreIngestor.ingest(document);

        return embeddingStore;

    }
}
