package langchain.rag.advanced;

import static langchain.rag.shared.Utils.*;

import java.nio.file.FileSystem;
import java.nio.file.Path;
import java.util.List;

import org.jspecify.annotations.NonNull;

import dev.langchain4j.data.document.Document;
import dev.langchain4j.data.document.DocumentSplitter;
import dev.langchain4j.data.document.loader.FileSystemDocumentLoader;
import dev.langchain4j.data.document.parser.TextDocumentParser;
import dev.langchain4j.data.document.splitter.DocumentSplitters;
import dev.langchain4j.data.embedding.Embedding;
import dev.langchain4j.data.segment.TextSegment;
import dev.langchain4j.model.chat.ChatModel;
import dev.langchain4j.model.embedding.EmbeddingModel;
import dev.langchain4j.model.embedding.onnx.bgesmallenv15q.BgeSmallEnV15QuantizedEmbeddingModel;
import dev.langchain4j.model.openai.OpenAiChatModel;
import dev.langchain4j.rag.DefaultRetrievalAugmentor;
import dev.langchain4j.rag.RetrievalAugmentor;
import dev.langchain4j.rag.content.retriever.ContentRetriever;
import dev.langchain4j.rag.content.retriever.EmbeddingStoreContentRetriever;
import dev.langchain4j.rag.query.router.DefaultQueryRouter;
import dev.langchain4j.rag.query.router.QueryRouter;
import dev.langchain4j.service.AiServices;
import dev.langchain4j.store.embedding.EmbeddingStore;
import dev.langchain4j.store.embedding.inmemory.InMemoryEmbeddingStore;
import langchain.rag.shared.Assistant;

public class AdvancedRAGWithDefaultQueryRouting {

    public static final String GOOGLE_OPENAI_URL = "https://generativelanguage.googleapis.com/v1beta/openai";
    static EmbeddingModel embeddingModel = new BgeSmallEnV15QuantizedEmbeddingModel();

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

        EmbeddingStore<TextSegment> personEmbeddingStore = embed(toPath("documents/biography-of-john-doe.txt"),
                embeddingModel);

        ContentRetriever personContentRetriever = EmbeddingStoreContentRetriever.builder()
                .embeddingModel(embeddingModel)
                .embeddingStore(personEmbeddingStore)
                .maxResults(2)
                .minScore(0.8)
                .build();

        EmbeddingStore<TextSegment> companyEmbeddingStore = embed(toPath("documents/miles-of-smiles-terms-of-use.txt"),
                embeddingModel);

        ContentRetriever companyContentRetriever = EmbeddingStoreContentRetriever.builder()
                .embeddingModel(embeddingModel)
                .embeddingStore(companyEmbeddingStore)
                .maxResults(2)
                .minScore(0.8)
                .build();

        QueryRouter queryRouter = new DefaultQueryRouter(personContentRetriever, companyContentRetriever);

        RetrievalAugmentor retrievalAugmentor = DefaultRetrievalAugmentor.builder()
                .queryRouter(queryRouter)
                .build();

        return AiServices.builder(Assistant.class)
                .chatModel(model)
                .retrievalAugmentor(retrievalAugmentor)
                .build();
    }

    private static EmbeddingStore<TextSegment> embed(Path path, EmbeddingModel embeddingModel2) {
        Document document = FileSystemDocumentLoader.loadDocument(path, new TextDocumentParser());
        DocumentSplitter splitter = DocumentSplitters.recursive(300, 0);
        List<TextSegment> textSegments = splitter.split(document);

        List<Embedding> embeddings = embeddingModel.embedAll(textSegments).content();
        EmbeddingStore<TextSegment> embeddingStore = new InMemoryEmbeddingStore<>();
        embeddingStore.addAll(embeddings, textSegments);

        return embeddingStore;
    }
}
