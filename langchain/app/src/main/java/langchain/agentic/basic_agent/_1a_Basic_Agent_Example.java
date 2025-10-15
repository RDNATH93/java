package langchain.agentic.basic_agent;

import java.io.IOException;

import dev.langchain4j.agentic.AgenticServices;
import dev.langchain4j.model.chat.ChatModel;
import dev.langchain4j.model.openai.OpenAiChatModel;
import langchain.agentic.util.StringLoader;

public class _1a_Basic_Agent_Example {

    public static final String GOOGLE_OPENAI_URL = "https://generativelanguage.googleapis.com/v1beta/openai";

    void main() throws IOException {
        // 1. Define the model that will power the agent
        ChatModel chatModel = OpenAiChatModel.builder()
                .baseUrl(GOOGLE_OPENAI_URL)
                .apiKey(System.getenv("GOOGLE_API_KEY"))
                .modelName("gemini-2.5-flash")
                .logRequests(true)
                .logResponses(true)
                .build();

        // 2. Define the agent behavior in agent_interfaces/CvGenerator.java

        // 3. Create the agent using AgenticServices
        CvGenerator cvGenerator = AgenticServices
                .agentBuilder(CvGenerator.class)
                .chatModel(chatModel)
                .outputName("masterCV")
                .build();

        // 4. Load text file from resources/documents/user_life_story.txt
        String lifeStory = StringLoader.loadFromResource("/documents/user_life_story.txt");

        // 5. We call the agent to generate the CV
        String cv = cvGenerator.generateCv(lifeStory);

        // 6. and print the generated CV
        System.out.println("=== CV ===");
        System.out.println(cv);

        // In example 1b we'll build the same agent but with structured output

    }
}
