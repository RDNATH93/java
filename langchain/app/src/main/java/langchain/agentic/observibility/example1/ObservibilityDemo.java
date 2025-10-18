package langchain.agentic.observibility.example1;

import java.util.Map;

import dev.langchain4j.agentic.AgenticServices;
import dev.langchain4j.agentic.UntypedAgent;
import dev.langchain4j.model.openai.OpenAiChatModel;

public class ObservibilityDemo {

        public static final String GOOGLE_OPENAI_URL = "https://generativelanguage.googleapis.com/v1beta/openai";

        void main() {
                OpenAiChatModel model = OpenAiChatModel.builder()
                                .modelName("gemini-2.5-flash")
                                .apiKey(System.getenv("GOOGLE_API_KEY"))
                                .baseUrl(GOOGLE_OPENAI_URL)
                                .logRequests(true)
                                .logResponses(true)
                                .build();

                // creative writer agent
                CreativeWriter creativeWriter = AgenticServices
                        .agentBuilder(CreativeWriter.class)
                        .chatModel(model)
                        .outputName("story")
                        .beforeAgentInvocation(request -> IO.println("Invoking CreativeWriter with topic: " + request))
                        .afterAgentInvocation(response -> IO.println("CreativeWriter generated this story: " + response))
                        .build();

                UntypedAgent novelAgent = AgenticServices
                                .sequenceBuilder()
                                .subAgents(creativeWriter)
                                .outputName("story")
                                .build();

                Map<String, Object> input = Map.of(
                                "topic", "dragons and wizards");

                String story = (String) novelAgent.invoke(input);

                IO.println("*********************STORY*********************");
                IO.println(story);

        }
}
