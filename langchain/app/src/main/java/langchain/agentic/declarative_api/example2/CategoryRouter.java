package langchain.agentic.declarative_api.example2;

import dev.langchain4j.agentic.Agent;
import dev.langchain4j.agentic.declarative.ChatModelSupplier;
import dev.langchain4j.model.chat.ChatModel;
import dev.langchain4j.model.openai.OpenAiChatModel;
import dev.langchain4j.service.UserMessage;
import dev.langchain4j.service.V;

public interface CategoryRouter {

    public static final String GOOGLE_OPENAI_URL = "https://generativelanguage.googleapis.com/v1beta/openai";

    @UserMessage("""
        Analyze the following user request and categorize it as 'legal', 'medical' or 'technical'.
        In case the request doesn't belong to any of those categories categorize it as 'unknown'.
        Reply with only one of those words and nothing else.
        The user request is: '{{request}}'.
        """)
    @Agent("Categorizes a user request")
    RequestCategory classify(@V("request") String request);


    @ChatModelSupplier
    static ChatModel model(){
        return OpenAiChatModel.builder()
                .modelName("gemini-2.5-flash")
                .apiKey(System.getenv("GOOGLE_API_KEY"))
                .baseUrl(GOOGLE_OPENAI_URL)
                .logRequests(true)
                .logResponses(true)
                .build();      
    }
}
