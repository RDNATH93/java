package langchain.agentic.basic_agent;

import java.io.IOException;

import dev.langchain4j.agentic.AgenticServices;
import dev.langchain4j.model.openai.OpenAiChatModel;
import langchain.agentic.domain.Cv;
import langchain.agentic.util.StringLoader;

public class _1b_Basic_Agent_Example_Structured {

    public static final String GOOGLE_OPENAI_URL = "https://generativelanguage.googleapis.com/v1beta/openai";

    /**
     * This example implements the same CvGenerator agent as in 1a,
     * but this version will return a custom Java objects, Cv, as defined in
     * domain/Cv.java
     */
    void main() throws IOException {

        // 1. Define the model that will power the agent
        OpenAiChatModel chatModel = OpenAiChatModel.builder()
                .apiKey(System.getenv("GOOGLE_API_KEY"))
                .baseUrl(GOOGLE_OPENAI_URL)
                .modelName("gemini-2.5-flash")
                .logRequests(true)
                .logResponses(true)
                .build();

        // 2. Define the agent behavior in agent_interfaces/CvGenerator.java

        // 3. Create the agent using AgenticServices
        CvGeneratorStructuredOutput cvGenerator = AgenticServices
                .agentBuilder(CvGeneratorStructuredOutput.class)
                .chatModel(chatModel)
                .outputName("masterCV")
                .build();

        // 4. Load text file from resources/documents/user_life_story.txt
        String lifeStory = StringLoader.loadFromResource("/documents/user_life_story.txt");

        // 5. We call the agent to generate the CV
        Cv cv = cvGenerator.generateCv(lifeStory);

        // 6. and print the generated CV
        IO.println("=== CV ===");
        IO.println(cv);
    }
}
