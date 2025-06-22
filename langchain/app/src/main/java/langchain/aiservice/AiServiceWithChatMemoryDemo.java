package langchain.aiservice;

import dev.langchain4j.memory.chat.MessageWindowChatMemory;
import dev.langchain4j.model.openai.OpenAiChatModel;
import dev.langchain4j.service.AiServices;
import dev.langchain4j.service.MemoryId;
import dev.langchain4j.service.UserMessage;
import dev.langchain4j.service.memory.ChatMemoryAccess;

/**
 * When using ChatMemory in this way it's also important to evict the memory of
 * a no longer needed conversations in order to avoid memory leaks. To make the
 * chat memories internally used by an AI service accessible it's enough that
 * the interface defining it extends the ChatMemoryAccess one
 */
// interface AssistantWithMemory {
//     String chat(@MemoryId int memoryId, @UserMessage String message);
// }

/**
 * Please note that if an AI Service method does not have a parameter annotated
 * with @MemoryId, the value of memoryId in ChatMemoryProvider will default to a
 * string "default"
 */
interface AssistantWithMemory extends ChatMemoryAccess {
    String chat(@MemoryId int memoryId, @UserMessage String message);
}

public class AiServiceWithChatMemoryDemo {

    public static void main(String[] args) {
        OpenAiChatModel model = OpenAiChatModel.builder()
                .baseUrl("https://openrouter.ai/api/v1")
                .apiKey(System.getenv("OPEN_ROUTER_GEMMA_3_27B"))
                .modelName("google/gemma-3-27b-it:free")
                .build();

        AssistantWithMemory assistant = AiServices.builder(AssistantWithMemory.class)
                .chatModel(model)
                .chatMemoryProvider(memoryId -> MessageWindowChatMemory.withMaxMessages(10))
                // .chatMemory(MessageWindowChatMemory.withMaxMessages(10))
                .build();

        String wickChat = assistant.chat(1, "Hello , My name is John Wick");
        String cenaChat = assistant.chat(2, "Hello , My name is John Cena");

        System.out.println(wickChat);
        System.out.println(cenaChat);
        wickChat = assistant.chat(1, "Who am I");
        cenaChat = assistant.chat(2, "Who am I");

        System.out.println(wickChat);
        System.out.println(cenaChat);

        System.out.println(assistant.getChatMemory(1));
        System.out.println(assistant.getChatMemory(2));

        System.out.println(assistant.evictChatMemory(1));
        System.out.println(assistant.evictChatMemory(2));

    }
}
