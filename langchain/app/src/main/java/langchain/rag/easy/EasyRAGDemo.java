package langchain.rag.easy;

import java.nio.file.FileSystems;
import java.nio.file.PathMatcher;
import java.util.List;

import dev.langchain4j.data.document.Document;
import dev.langchain4j.data.document.loader.FileSystemDocumentLoader;
import dev.langchain4j.data.segment.TextSegment;
import dev.langchain4j.memory.chat.MessageWindowChatMemory;
import dev.langchain4j.model.chat.ChatModel;
import dev.langchain4j.model.openai.OpenAiChatModel;
import dev.langchain4j.rag.RetrievalAugmentor;
import dev.langchain4j.rag.content.retriever.ContentRetriever;
import dev.langchain4j.rag.content.retriever.EmbeddingStoreContentRetriever;
import dev.langchain4j.service.AiServices;
import dev.langchain4j.store.embedding.EmbeddingStoreIngestor;
import dev.langchain4j.store.embedding.inmemory.InMemoryEmbeddingStore;
import langchain.rag.easy.shared.Assistant;

import static langchain.rag.easy.shared.Utils.*;

/**
 * This example demonstrates how to implement an "Easy RAG" (Retrieval-Augmented
 * Generation) application.
 * By "easy" we mean that we won't dive into all the details about parsing,
 * splitting, embedding, etc.
 * All the "magic" is hidden inside the "langchain4j-easy-rag" module.
 * <p>
 */

public class EasyRAGDemo {
    private static final String GOOGLE_OPENAI_URL = "https://generativelanguage.googleapis.com/v1beta/openai";

    public static void main(String[] args) {
        ChatModel model = OpenAiChatModel.builder()
                .baseUrl(GOOGLE_OPENAI_URL)
                .apiKey(System.getenv("GOOGLE_API_KEY"))
                .modelName("gemini-2.5-flash")
                .build();

        // First, let's load documents that we want to use for RAG
        PathMatcher pathMatcher = FileSystems.getDefault().getPathMatcher("glob:*.txt");
        List<Document> documents = FileSystemDocumentLoader.loadDocuments(toPath("documents/"), pathMatcher);

        // Second, let's create an assistant that will have access to our documents
        Assistant assistant = AiServices.builder(Assistant.class)
                .chatModel(model)
                .chatMemory(MessageWindowChatMemory.withMaxMessages(10)) // it should remember 10 latest messages
                .contentRetriever(createContentRetriever(documents)) // it should have access to our documents
                .build();

        // Lastly, let's start the conversation with the assistant. We can ask questions
        // like:
        // - Can I cancel my reservation?
        // - I had an accident, should I pay extra?
        startConversationWith(assistant);

    }

    private static ContentRetriever createContentRetriever(List<Document> documents) {

        // Here, we create an empty in-memory store for our documents and their
        // embeddings.
        InMemoryEmbeddingStore<TextSegment> embeddingStore = new InMemoryEmbeddingStore<>();

        // Here, we are ingesting our documents into the store.
        // Under the hood, a lot of "magic" is happening, but we can ignore it for now.
        EmbeddingStoreIngestor.ingest(documents, embeddingStore);

        // Lastly, let's create a content retriever from an embedding store.
        return EmbeddingStoreContentRetriever.from(embeddingStore);

    }
}
