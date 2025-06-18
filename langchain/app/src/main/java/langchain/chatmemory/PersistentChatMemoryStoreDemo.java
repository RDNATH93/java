package langchain.chatmemory;

import static org.mapdb.Serializer.STRING;
import static dev.langchain4j.data.message.ChatMessageDeserializer.messagesFromJson;
import static dev.langchain4j.data.message.ChatMessageSerializer.messagesToJson;

import java.util.List;
import java.util.Map;

import org.mapdb.DB;
import org.mapdb.DBMaker;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import dev.langchain4j.data.message.AiMessage;
import dev.langchain4j.data.message.ChatMessage;
import dev.langchain4j.data.message.UserMessage;
import dev.langchain4j.memory.chat.MessageWindowChatMemory;
import dev.langchain4j.model.chat.response.ChatResponse;
import dev.langchain4j.model.openai.OpenAiChatModel;
import dev.langchain4j.store.memory.chat.ChatMemoryStore;

public class PersistentChatMemoryStoreDemo {

    private static final Logger logger = LoggerFactory.getLogger(PersistentChatMemoryStoreDemo.class);

    public static void main(String[] args) {
        OpenAiChatModel model = OpenAiChatModel.builder()
                .baseUrl("https://openrouter.ai/api/v1")
                .apiKey(System.getenv("OPEN_ROUTER_GEMMA_3_27B"))
                .modelName("google/gemma-3-27b-it:free")
                .build();

        MessageWindowChatMemory chatMemory = MessageWindowChatMemory.builder()
                .id("12345")
                .maxMessages(3)
                .chatMemoryStore(new PersistentChatMemoryStore())
                .build();

        // chatMemory.add(UserMessage.from("My name is Bond, James Bond"));    
        // ChatResponse response = model.chat(chatMemory.messages());   
        // String aiMessage = response.aiMessage().text();
        // logger.info("AiMessage: {}",aiMessage);
        // chatMemory.add(AiMessage.from(aiMessage));
        
        chatMemory.add(UserMessage.from("What is my name"));
        ChatResponse response = model.chat(chatMemory.messages());  
        String aiMessage = response.aiMessage().text();
        logger.info("AiMessage: {}",aiMessage);
        chatMemory.add(AiMessage.from(aiMessage));

    }

    static class PersistentChatMemoryStore implements ChatMemoryStore {

        private final DB db = DBMaker.fileDB("chat-memory.db").transactionEnable().make();
        private final Map<String, String> map = db.hashMap("messages", STRING, STRING).createOrOpen();

        @Override
        public List<ChatMessage> getMessages(Object memoryId) {
            String json = map.get((String) memoryId);
            logger.info("Get Message memoryId: {} message: {}",memoryId,json);
            return messagesFromJson(json);
        }
        
        @Override
        public void updateMessages(Object memoryId, List<ChatMessage> messages) {
            String json = messagesToJson(messages);
            logger.info("Update Message memoryId: {} message: {}",memoryId,json);
            map.put((String) memoryId, json);
            db.commit();
        }
        
        @Override
        public void deleteMessages(Object memoryId) {
            String removedMessage = map.remove((String) memoryId);
            logger.info("Delete Message memoryId: {} message: {}",memoryId,removedMessage);
            db.commit();
        }
    }
}
