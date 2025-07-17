package langchain.rag.advanced;

import static langchain.rag.shared.Utils.startConversationWith;
import static langchain.rag.shared.Utils.toPath;

import java.util.List;

import dev.langchain4j.data.document.Document;
import dev.langchain4j.data.document.DocumentSplitter;
import dev.langchain4j.data.document.loader.FileSystemDocumentLoader;
import dev.langchain4j.data.document.parser.TextDocumentParser;
import dev.langchain4j.data.document.splitter.DocumentSplitters;
import dev.langchain4j.data.segment.TextSegment;
import dev.langchain4j.model.chat.ChatModel;
import dev.langchain4j.model.embedding.EmbeddingModel;
import dev.langchain4j.model.embedding.onnx.bgesmallenv15q.BgeSmallEnV15QuantizedEmbeddingModel;
import dev.langchain4j.model.openai.OpenAiChatModel;
import dev.langchain4j.rag.DefaultRetrievalAugmentor;
import dev.langchain4j.rag.RetrievalAugmentor;
import dev.langchain4j.rag.content.injector.ContentInjector;
import dev.langchain4j.rag.content.injector.DefaultContentInjector;
import dev.langchain4j.rag.content.retriever.ContentRetriever;
import dev.langchain4j.rag.content.retriever.EmbeddingStoreContentRetriever;
import dev.langchain4j.service.AiServices;
import dev.langchain4j.store.embedding.EmbeddingStore;
import dev.langchain4j.store.embedding.EmbeddingStoreIngestor;
import dev.langchain4j.store.embedding.IngestionResult;
import dev.langchain4j.store.embedding.inmemory.InMemoryEmbeddingStore;
import langchain.rag.shared.Assistant;

public class AdvancedRAGWithMetadata {

    public static final String GOOGLE_OPENAI_URL = "https://generativelanguage.googleapis.com/v1beta/openai";

    public static void main(String[] args) {
        Assistant assistant = createAsssitant("documents/miles-of-smiles-terms-of-use.txt");

        startConversationWith(assistant);
    }

    private static Assistant createAsssitant(String documentPath) {

        ChatModel model = OpenAiChatModel.builder()
                .baseUrl(GOOGLE_OPENAI_URL)
                .apiKey(System.getenv("GOOGLE_API_KEY"))
                .modelName("gemini-2.5-flash")
                .logRequests(true)
                .logResponses(true)
                .build();

        Document document = FileSystemDocumentLoader.loadDocument(toPath(documentPath), new TextDocumentParser());
        // System.out.println("Document: "+ document);

        DocumentSplitter splitter = DocumentSplitters.recursive(300, 0);
        // List<TextSegment> textSegments = splitter.split(document);
        // System.out.println(textSegments);

        EmbeddingModel embeddingModel = new BgeSmallEnV15QuantizedEmbeddingModel();
        EmbeddingStore<TextSegment> embeddingStore = new InMemoryEmbeddingStore<>();

        EmbeddingStoreIngestor ingestor = EmbeddingStoreIngestor.builder()
                .documentSplitter(splitter)
                .embeddingModel(embeddingModel)
                .embeddingStore(embeddingStore)
                .build();

        IngestionResult ingestResult = ingestor.ingest(document);
        System.out.println(ingestResult.tokenUsage());

        ContentRetriever contentRetriever = EmbeddingStoreContentRetriever.builder()
                .embeddingModel(embeddingModel)
                .embeddingStore(embeddingStore)
                .build();

        ContentInjector contentInjector = DefaultContentInjector.builder()
                .metadataKeysToInclude(List.of("file_name", "index"))
                .build();

        RetrievalAugmentor augmentor = DefaultRetrievalAugmentor.builder()
                .contentRetriever(contentRetriever)
                .contentInjector(contentInjector)
                .build();

        return AiServices.builder(Assistant.class)
                .chatModel(model)
                .retrievalAugmentor(augmentor)
                .build();
    }
}