package langchain.aiservice;

import dev.langchain4j.model.openai.OpenAiChatModel;
import dev.langchain4j.service.AiServices;
import dev.langchain4j.service.UserMessage;
import dev.langchain4j.service.V;

interface CountryCapital {
    // String find(String userMessage);

    // String find(@UserMessage String userMessage);

    // userMessage contains "{{country}}" template variable
    //String find(@UserMessage String userMessage, @V("country") String country);
    
    // @UserMessage("What is the capital of Germany?")
    // String find();

    //  @UserMessage("What is the capital of {{it}}?")
    //  String find(String country);

    // @UserMessage("What is the capital of {{country}}?")
    // String find(@V("country") String country);

    @UserMessage("What is the {{something}} of {{country}}?")
    String find(@V("something") String something, @V("country") String country);

    // @UserMessage("What is the capital of {{country}}?")
    // String chat(String country); // this works only in Quarkus and Spring Boot
    // applications

}

public class UserMessageDemo {
    public static void main(String[] args) {
        OpenAiChatModel model = OpenAiChatModel.builder()
                .baseUrl("https://openrouter.ai/api/v1")
                .apiKey(System.getenv("OPEN_ROUTER_LLAMA_4_SCOUT"))
                .logRequests(true)
                // .logResponses(true)
                .modelName("meta-llama/llama-4-scout:free")
                .build();

        CountryCapital countryCapital = AiServices.builder(CountryCapital.class)
                .chatModel(model)
                .systemMessageProvider(memoryId -> """
                        Be specific and to the point what is asked.
                        Example:
                        Question: What is the capital of India?
                        Answer: The capital of India is New Delhi """)
                .build();

       // String answer = countryCapital.find("What is the capital of {{country}}","SriLanka");

       //String answer = countryCapital.find("Germany");
       String answer = countryCapital.find("city","Germany");
        System.out.println(answer);
    }

}
