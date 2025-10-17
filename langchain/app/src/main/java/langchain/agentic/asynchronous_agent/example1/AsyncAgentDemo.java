package langchain.agentic.asynchronous_agent.example1;

import module java.base;

import dev.langchain4j.agentic.AgenticServices;
import dev.langchain4j.model.openai.OpenAiChatModel;

public class AsyncAgentDemo {

    public static final String GOOGLE_OPENAI_URL = "https://generativelanguage.googleapis.com/v1beta/openai";

    void main() {

        OpenAiChatModel chatModel = OpenAiChatModel.builder()
                .modelName("gemini-2.5-pro")
                .apiKey(System.getenv("GOOGLE_API_KEY"))
                .baseUrl(GOOGLE_OPENAI_URL)
                .logRequests(true)
                .logResponses(true)
                .build();

        FoodExpert foodExpert = AgenticServices
                .agentBuilder(FoodExpert.class)
                .async(true)
                .chatModel(chatModel)
                .outputName("meals").build();

        MovieExpert movieExpert = AgenticServices
                .agentBuilder(MovieExpert.class)
                .chatModel(chatModel)
                .async(true)
                .outputName("movies")
                .build();

        EveningPlannerAgent eveningPlannerAgent = AgenticServices
                .parallelBuilder(EveningPlannerAgent.class)
                .subAgents(foodExpert, movieExpert)
                .executor(Executors.newFixedThreadPool(2))
                .outputName("plans")
                .output(agenticScope -> {
                    List<String> meals = agenticScope.readState("meals", List.of());
                    List<String> movies = agenticScope.readState("movies", List.of());

                    List<EveningPlan> moviesAndMeals = new ArrayList<>();
                    for (int i = 0; i < movies.size(); i++) {
                        if (i >= meals.size()) {
                            break;
                        }
                        moviesAndMeals.add(new EveningPlan(movies.get(i), meals.get(i)));
                    }
                    return moviesAndMeals;
                })
                .build();

        List<EveningPlan> plans = eveningPlannerAgent.plan("romantic");

        IO.println(plans);

    }
}
