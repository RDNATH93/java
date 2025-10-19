package langchain.agentic.declarative_api.example1;

import java.util.List;

import dev.langchain4j.agentic.AgenticServices;
import dev.langchain4j.model.openai.OpenAiChatModel;

public class DeclarativeApiDemo {

    public static final String GOOGLE_OPENAI_URL = "https://generativelanguage.googleapis.com/v1beta/openai";

    void main() {

        OpenAiChatModel chatModel = OpenAiChatModel.builder()
                .modelName("gemini-2.5-pro")
                .apiKey(System.getenv("GOOGLE_API_KEY"))
                .baseUrl(GOOGLE_OPENAI_URL)
                .logRequests(true)
                .logResponses(true)
                .build();

        EveningPlannerAgent eveningPlannerAgent = AgenticServices
                .createAgenticSystem(EveningPlannerAgent.class,chatModel);

        List<EveningPlan> plans = eveningPlannerAgent.plan("romantic");

        IO.print(plans);

    }
}
