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
import langchain.rag.shared.Assistant;

public class AdvancedRAGWithQueryCompressionDemo {
    private static final String GOOGLE_OPENAI_URL = "https://generativelanguage.googleapis.com/v1beta/openai";
    private static final String OPENROUTER_URL = "https://openrouter.ai/api/v1";

    public static void main(String[] args) {

        AdvancedAssistant assistant = createAssistant("documents/biography-of-john-doe.txt");

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
