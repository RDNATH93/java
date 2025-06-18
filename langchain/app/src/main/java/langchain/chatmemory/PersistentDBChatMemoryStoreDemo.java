package langchain.chatmemory;

import static dev.langchain4j.data.message.ChatMessageDeserializer.messagesFromJson;
import static dev.langchain4j.data.message.ChatMessageSerializer.messagesToJson;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import dev.langchain4j.data.message.AiMessage;
import dev.langchain4j.data.message.ChatMessage;
import dev.langchain4j.data.message.UserMessage;
import dev.langchain4j.memory.chat.MessageWindowChatMemory;
import dev.langchain4j.model.chat.response.ChatResponse;
import dev.langchain4j.model.openai.OpenAiChatModel;
import dev.langchain4j.store.memory.chat.ChatMemoryStore;

public class PersistentDBChatMemoryStoreDemo {

    private static final Logger logger = LoggerFactory.getLogger(PersistentDBChatMemoryStoreDemo.class);

    public static void main(String[] args) {
        OpenAiChatModel model = OpenAiChatModel.builder()
                .baseUrl("https://openrouter.ai/api/v1")
                .apiKey(System.getenv("OPEN_ROUTER_GEMMA_3_27B"))
                .modelName("google/gemma-3-27b-it:free")
                .build();

        MessageWindowChatMemory chatMemory = MessageWindowChatMemory.builder()
                .id("12345")
                .maxMessages(3)
                .chatMemoryStore(new PostgresChatMemoryStore())
                .build();

        // chatMemory.add(UserMessage.from("My name is Bond, James Bond"));
        // ChatResponse response = model.chat(chatMemory.messages());
        // String aiMessage = response.aiMessage().text();
        // logger.info("AiMessage: {}",aiMessage);
        // chatMemory.add(AiMessage.from(aiMessage));

        chatMemory.add(UserMessage.from("What is my name"));
        ChatResponse response = model.chat(chatMemory.messages());
        String aiMessage = response.aiMessage().text();
        logger.info("AiMessage: {}", aiMessage);
        chatMemory.add(AiMessage.from(aiMessage));
    }

    static class PostgresChatMemoryStore implements ChatMemoryStore {

        private final HikariDataSource dataSource;

        public PostgresChatMemoryStore() {
            HikariConfig config = new HikariConfig();
            config.setJdbcUrl("jdbc:postgresql://localhost:5432/chatmemory");
            config.setUsername("postgres");
            config.setPassword("postgres");
            config.setMaximumPoolSize(10);

            this.dataSource = new HikariDataSource(config);
            initializeTable();
        }

        private void initializeTable() {
            String sql = """
                        CREATE TABLE IF NOT EXISTS chat_messages (
                            memory_id VARCHAR(255) PRIMARY KEY,
                            messages TEXT NOT NULL
                        )
                    """;

            try (Connection conn = dataSource.getConnection();
                    PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.execute();
            } catch (SQLException e) {
                logger.error("Error initializing table", e);
                throw new RuntimeException(e);
            }
        }

        @Override
        public List<ChatMessage> getMessages(Object memoryId) {
            String sql = "SELECT messages FROM chat_messages WHERE memory_id = ?";

            try (Connection conn = dataSource.getConnection();
                    PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setString(1, (String) memoryId);

                try (ResultSet rs = stmt.executeQuery()) {
                    if (rs.next()) {
                        String json = rs.getString("messages");
                        logger.info("Get Message memoryId: {} message: {}", memoryId, json);
                        return messagesFromJson(json);
                    }
                    return List.of();
                }
            } catch (SQLException e) {
                logger.error("Error getting messages", e);
                throw new RuntimeException(e);
            }
        }

        @Override
        public void updateMessages(Object memoryId, List<ChatMessage> messages) {
            String sql = """
                        INSERT INTO chat_messages (memory_id, messages)
                        VALUES (?, ?)
                        ON CONFLICT (memory_id)
                        DO UPDATE SET messages = EXCLUDED.messages
                    """;

            String json = messagesToJson(messages);

            try (Connection conn = dataSource.getConnection();
                    PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setString(1, (String) memoryId);
                stmt.setString(2, json);
                stmt.executeUpdate();
                logger.info("Update Message memoryId: {} message: {}", memoryId, json);
            } catch (SQLException e) {
                logger.error("Error updating messages", e);
                throw new RuntimeException(e);
            }
        }

        @Override
        public void deleteMessages(Object memoryId) {
            String sql = "DELETE FROM chat_messages WHERE memory_id = ?";

            try (Connection conn = dataSource.getConnection();
                    PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setString(1, (String) memoryId);
                stmt.executeUpdate();
                logger.info("Deleted messages for memoryId: {}", memoryId);
            } catch (SQLException e) {
                logger.error("Error deleting messages", e);
                throw new RuntimeException(e);
            }
        }
    }
}