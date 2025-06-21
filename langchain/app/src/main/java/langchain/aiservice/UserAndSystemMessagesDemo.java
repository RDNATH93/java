package langchain.aiservice;

import dev.langchain4j.model.ollama.OllamaChatModel;
import dev.langchain4j.service.AiServices;
import dev.langchain4j.service.SystemMessage;
import dev.langchain4j.service.UserMessage;
import dev.langchain4j.service.V;

interface Assistant {
    
    // @SystemMessage("Given a name of a country, answer with a name of it's capital")
    // String chat(String userMessage);

    // @SystemMessage("Given a name of a country, answer with a name of it's capital")
    // String chat(@UserMessage String userMessage);

    // @SystemMessage("Given a name of a country, {{answerInstructions}}")
    // String chat(@V("answerInstructions") String answerInstructions, @UserMessage String userMessage);

    // @SystemMessage("Given a name of a country, answer with a name of it's capital")
    // String chat(@UserMessage String userMessage, @V("country") String country); // userMessage contains "{{country}}" template variable

    // @SystemMessage("Given a name of a country, {{answerInstructions}}")
    // String chat(@V("answerInstructions") String answerInstructions, @UserMessage String userMessage, @V("country") String country); // userMessage contains "{{country}}" template variable

    // @SystemMessage("Given a name of a country, answer with a name of it's capital")
    // @UserMessage("Germany")
    // String chat();

    // @SystemMessage("Given a name of a country, {{answerInstructions}}")
    // @UserMessage("Germany")
    // String chat(@V("answerInstructions") String answerInstructions);

    @SystemMessage("""
                      Given a name of a country, answer with a name of it's capital. 
                      If it is not a country, respond as It's not a Country.
                      If it's a valid city name, reposnd with coutry name in which city is
                      Question: Sydney
                      Answer: Sydney is not a Country , It is a city in Australia
                      Question: Rome
                      Answer: Rome is not a Country , It is a city and also Capital of Italy""")
    @UserMessage("{{it}}")
    String chat(String country);

    // @SystemMessage("Given a name of a country, answer with a name of it's capital")
    // @UserMessage("{{country}}")
    // String chat(@V("country") String country);

    // @SystemMessage("Given a name of a country, {{answerInstructions}}")
    // @UserMessage("{{country}}")
    // String chat(@V("answerInstructions") String answerInstructions, @V("country") String country);

}

public class UserAndSystemMessagesDemo {
    /**
     * @param args
     */
    public static void main(String[] args) {
        OllamaChatModel model = OllamaChatModel.builder()
                .baseUrl("http://localhost:11434")
                .modelName("gemma3:1b")
                .logRequests(true)
                .build();
        
        Assistant assistant = AiServices.create(Assistant.class, model);
        //String answer = assistant.chat("tell me top 5 cities as per economic condition","India");
        //String answer = assistant.chat("tell me top 5 cities as per economic condition","{{country}}","America");
        //String answer = assistant.chat();
        //String answer = assistant.chat("tell me top 5 cities as per economic condition");
        String answer = assistant.chat("paulo alto");
        System.out.println(answer);

    }
}
