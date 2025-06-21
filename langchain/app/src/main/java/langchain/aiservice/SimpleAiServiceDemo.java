package langchain.aiservice;

import dev.langchain4j.model.openai.OpenAiChatModel;
import dev.langchain4j.service.AiServices;

interface Assistance {
    String chat(String userMessage);

}

public class SimpleAiServiceDemo {

    public static void main(String[] args) {
        OpenAiChatModel model = OpenAiChatModel.builder()
                .baseUrl("https://openrouter.ai/api/v1")
                .apiKey(System.getenv("OPEN_ROUTER_GEMMA_3_27B"))
                .modelName("google/gemma-3-27b-it:free")
                .build();
         
        Assistance assistance = AiServices.create(Assistance.class, model);  
        String chat = assistance.chat("Tell me about furtherest blackhole that has been discovered till today");      
        System.out.println(chat);        
    }
}
