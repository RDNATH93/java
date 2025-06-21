package langchain.aiservice;

import dev.langchain4j.model.openai.OpenAiChatModel;
import dev.langchain4j.service.AiServices;
import dev.langchain4j.service.SystemMessage;
import dev.langchain4j.service.UserMessage;
import dev.langchain4j.service.V;

interface Friend {

    /**
     * added the @SystemMessage annotation with a system prompt template we want to use.
     * This will be converted into a SystemMessage behind the scenes and sent to the
     * LLM along with the UserMessage.
     */
    // @SystemMessage("You are a good friend of mine. Answer using slang.")
    // String chat(String userMessage);

    
    // @SystemMessage can also load a prompt template from resources
    // @SystemMessage(fromResource = "my-prompt-template.txt")
    // String chat(String userMessage);

    
    /**
     * assume the model we use does not support system messages,
     * or maybe we just want to use UserMessage for that purpose.
     */
    // @UserMessage("You are a good friend of mine. Answer using slang. {{it}}")
    // String chat(String userMessage);

   
    /**
     * annotate the String userMessage with @V and assign a custom name to the
     * prompt template variable:
     */
    // @UserMessage("You are a good friend of mine. Answer using slang.
    // {{message}}")
    // String chat(@V("message") String userMessage);

    
    // @UserMessage can also load a prompt template from resources
    @UserMessage(fromResource = "my-prompt-template.txt")
    String chat(String userMessage);    

}

public class SystemMessageDemo {
    public static void main(String[] args) {
        OpenAiChatModel model = OpenAiChatModel.builder()
                .baseUrl("https://openrouter.ai/api/v1")
                .apiKey(System.getenv("OPEN_ROUTER_GEMMA_3_27B"))
                .logRequests(true)
                // .logResponses(true)
                .modelName("google/gemma-3-27b-it:free")
                .build();

        Friend friend = AiServices.create(Friend.class, model);

        // System messages can also be defined dynamically with the system message
        // provider
        // Friend friend = AiServices.builder(Friend.class)
        // .chatModel(model)
        // .systemMessageProvider(chatMemoryId -> "You are a good friend of mine. Answer
        // using slang.")
        // .build();

        String chat = friend.chat("Hey buddy");
        System.out.println(chat);
    }

}
