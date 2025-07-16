package langchain.rag.advanced;

import static langchain.rag.shared.Utils.startConversationWith;
import static langchain.rag.shared.Utils.toPath;

import dev.langchain4j.data.document.Document;
import dev.langchain4j.data.document.loader.FileSystemDocumentLoader;
import dev.langchain4j.data.document.parser.TextDocumentParser;
import dev.langchain4j.data.document.splitter.DocumentSplitters;
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
import dev.langchain4j.rag.query.transformer.CompressingQueryTransformer;
import dev.langchain4j.rag.query.transformer.QueryTransformer;
import dev.langchain4j.service.AiServices;
import dev.langchain4j.store.embedding.EmbeddingStore;
import dev.langchain4j.store.embedding.EmbeddingStoreIngestor;
import dev.langchain4j.store.embedding.IngestionResult;
import dev.langchain4j.store.embedding.inmemory.InMemoryEmbeddingStore;
import langchain.rag.shared.AdvancedAssistant;

/**
     * Please refer to {@link Naive_RAG_Example} for a basic context.
     * <p>
     * Advanced RAG in LangChain4j is described here: https://github.com/langchain4j/langchain4j/pull/538
     * <p>
     * This example illustrates the implementation of a more sophisticated RAG application
     * using a technique known as "query compression".
     * Often, a query from a user is a follow-up question that refers back to earlier parts of the conversation
     * and lacks all the necessary details for effective retrieval.
     * For example, consider this conversation:
     * User: What is the legacy of John Doe?
     * AI: John Doe was a...
     * User: When was he born?
     * <p>
     * In such scenarios, using a basic RAG approach with a query like "When was he born?"
     * would likely fail to find articles about John Doe, as it doesn't contain "John Doe" in the query.
     * Query compression involves taking the user's query and the preceding conversation, then asking the LLM
     * to "compress" this into a single, self-contained query.
     * The LLM should generate a query like "When was John Doe born?".
     * This method adds a bit of latency and cost but significantly enhances the quality of the RAG process.
     * It's worth noting that the LLM used for compression doesn't have to be the same as the one
     * used for conversation. For instance, you might use a smaller local model trained for summarization.
     */

public class AdvancedRAGWithQueryCompressionDemo {
    private static final String GOOGLE_OPENAI_URL = "https://generativelanguage.googleapis.com/v1beta/openai";
    private static final String OPENROUTER_URL = "https://openrouter.ai/api/v1";

    public static void main(String[] args) {

        AdvancedAssistant assistant = createAssistant("documents/biography-of-john-doe.txt");

        // First, ask "What is the legacy of John Doe?"
        // Then, ask "When was he born?"
        // Now, review the logs:
        // The first query was not compressed as there was no preceding context to compress.
        // The second query, however, was compressed into something like "When was John Doe born?"
        startConversationWith(assistant);

    }

    private static AdvancedAssistant createAssistant(String documentPath) {

        ChatModel chatModel = OpenAiChatModel.builder()
        .baseUrl(GOOGLE_OPENAI_URL)
        .apiKey(System.getenv("GOOGLE_API_KEY"))
        .modelName("gemini-2.5-flash")
        .logRequests(true)
        .logResponses(true)
        .build();

        ChatModel queryCompessionModel = OpenAiChatModel.builder()
        .baseUrl(OPENROUTER_URL)
        .apiKey(System.getenv("OPENROUTER_API_KEY"))
        .modelName("google/gemma-3-27b-it:free")
        .logRequests(true)
        .logResponses(true)
        .build();
   
        Document document = FileSystemDocumentLoader.loadDocument(toPath(documentPath),new TextDocumentParser());

        EmbeddingModel embeddingModel = new BgeSmallEnV15QuantizedEmbeddingModel();
        EmbeddingStore<TextSegment> embeddingStore = new InMemoryEmbeddingStore<>();


        EmbeddingStoreIngestor ingestor = EmbeddingStoreIngestor.builder()
        .documentSplitter(DocumentSplitters.recursive(300,0))
        .embeddingModel(embeddingModel)
        .embeddingStore(embeddingStore)
        .build();        

        IngestionResult result = ingestor.ingest(document);

        // We will create a CompressingQueryTransformer, which is responsible for compressing
        // the user's query and the preceding conversation into a single, stand-alone query.
        // This should significantly improve the quality of the retrieval process.
        QueryTransformer compressingQueryTransformer = CompressingQueryTransformer.builder()
        .chatModel(queryCompessionModel)
        .build();

        LoggingQueryTransformer loggingQueryTransformer = new LoggingQueryTransformer(compressingQueryTransformer);

        ContentRetriever embeddingStoreContentRetriever = EmbeddingStoreContentRetriever.builder()
        .embeddingModel(embeddingModel)
        .embeddingStore(embeddingStore)
        .maxResults(2)
        .minScore(0.5)
        .build();


        // The RetrievalAugmentor serves as the entry point into the RAG flow in LangChain4j.
        // It can be configured to customize the RAG behavior according to your requirements.
        // In subsequent examples, we will explore more customizations.
        RetrievalAugmentor augmentor = DefaultRetrievalAugmentor.builder()
        .queryTransformer(loggingQueryTransformer)
        .contentRetriever(embeddingStoreContentRetriever)
        .build();

        AdvancedAssistant assistant = AiServices.builder(AdvancedAssistant.class)
        .chatModel(chatModel)
        .chatMemory(MessageWindowChatMemory.withMaxMessages(10))
        .retrievalAugmentor(augmentor)
        .build();

        return assistant;

    }
}
