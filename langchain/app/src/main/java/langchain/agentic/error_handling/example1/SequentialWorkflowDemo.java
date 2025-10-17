package langchain.agentic.error_handling.example1;

import java.util.Map;

import dev.langchain4j.agentic.AgenticServices;
import dev.langchain4j.agentic.UntypedAgent;
import dev.langchain4j.agentic.agent.ErrorRecoveryResult;
import dev.langchain4j.agentic.agent.MissingArgumentException;
import dev.langchain4j.model.openai.OpenAiChatModel;

public class SequentialWorkflowDemo {

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
                                .outputName("story").build();

                // audience editor agent
                AudienceEditor audienceEditor = AgenticServices
                                .agentBuilder(AudienceEditor.class)
                                .chatModel(model)
                                .outputName("story").build();

                // style editor agent
                StyleEditor styleEditor = AgenticServices
                                .agentBuilder(StyleEditor.class)
                                .chatModel(model)
                                .outputName("story").build();

                // agent orchestrator
                UntypedAgent novelAgent = AgenticServices.sequenceBuilder()
                                .subAgents(creativeWriter, audienceEditor, styleEditor)
                                .errorHandler(errorContext->{
                                        if(errorContext.agentName().equals("generateStory") &&
                                        errorContext.exception() instanceof MissingArgumentException mae
                                        && mae.argumentName().equals("topic")){
                                                errorContext.agenticScope().writeState("topic", "dragons and wizards");
                                             //errorRecoveryCalled.set(true);
                                             return ErrorRecoveryResult.retry();  
                                        }
                                        return ErrorRecoveryResult.throwException();
                                })
                                .outputName("story")
                                .build();

                Map<String, Object> input = Map.of(
                                //"topic", "dragons and wizards",
                                "style", "fantasy",
                                "audience", "young adults");

                String story = (String) novelAgent.invoke(input);

                IO.println("*********************STORY*********************");
                IO.println(story);

        }
}
