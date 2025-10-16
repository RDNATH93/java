package langchain.agentic.loop_workflow.example1;

import dev.langchain4j.agentic.AgenticServices;
import dev.langchain4j.agentic.UntypedAgent;
import dev.langchain4j.model.openai.OpenAiChatModel;
import langchain.agentic.sequential_workflow.example1.NovelAgent;

public class LoopWorkflowDemo {

    public static final String GOOGLE_OPENAI_URL = "https://generativelanguage.googleapis.com/v1beta/openai";
    private static final String OPENROUTER_URL = "https://openrouter.ai/api/v1";

    void main() {

        OpenAiChatModel scoringModel = OpenAiChatModel.builder()
                .modelName("openai/gpt-oss-20b:free")
                .apiKey(System.getenv("OPENROUTER_API_KEY"))
                .baseUrl(OPENROUTER_URL)
                .logRequests(true)
                .logResponses(true)
                .build();

        OpenAiChatModel chatModel = OpenAiChatModel.builder()
                .modelName("gemini-2.5-flash")
                .apiKey(System.getenv("GOOGLE_API_KEY"))
                .baseUrl(GOOGLE_OPENAI_URL)
                .build();

        // style editor agent
        StyleEditor styleEditor = AgenticServices.agentBuilder(StyleEditor.class)
                .chatModel(scoringModel)
                .outputName("story")
                .build();

        // style score agent
        StyleScorer styleScorer = AgenticServices.agentBuilder(StyleScorer.class)
                .chatModel(scoringModel)
                .outputName("score")
                .build();

        // agent orchestrator
        UntypedAgent scoreReviewLoop = AgenticServices
                .loopBuilder()
                .subAgents(styleEditor, styleScorer)
                .maxIterations(5)
                .testExitAtLoopEnd(true)
                .exitCondition(agenticScope -> agenticScope.readState("score", 0.0) >= 0.8)
                .build();

        // ceative write agent
        CreativeWriter creativeWriter = AgenticServices.agentBuilder(CreativeWriter.class)
                .chatModel(chatModel)
                .outputName("story")
                .build();

         
        // agent orchestrator
        NovelAgent novelAgent = AgenticServices
                .sequenceBuilder(NovelAgent.class)
                .subAgents(creativeWriter, scoreReviewLoop)
                .outputName("story")
                .build();   
        
        String story= novelAgent.createNovel("murder mystery party", "", "horror");
        IO.println("++++++++++++++++++++++ STORY ++++++++++++++++++++++");
        IO.println(story);        
                
    }

}
