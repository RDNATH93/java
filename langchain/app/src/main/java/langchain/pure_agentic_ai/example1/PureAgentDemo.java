package langchain.pure_agentic_ai.example1;

import dev.langchain4j.agentic.AgenticServices;
import dev.langchain4j.agentic.supervisor.SupervisorAgent;
import dev.langchain4j.agentic.supervisor.SupervisorResponseStrategy;
import dev.langchain4j.model.openai.OpenAiChatModel;

public class PureAgentDemo {
    public static final String GOOGLE_OPENAI_URL = "https://generativelanguage.googleapis.com/v1beta/openai";

    void main() {
        
        OpenAiChatModel BASE_MODEL = OpenAiChatModel.builder()
                .modelName("gemini-2.5-flash")
                .apiKey(System.getenv("GOOGLE_API_KEY"))
                .baseUrl(GOOGLE_OPENAI_URL)
                .logRequests(true)
                .logResponses(true)
                .build();

        OpenAiChatModel PLANNER_MODEL = OpenAiChatModel.builder()
                .modelName("gemini-2.5-pro")
                .apiKey(System.getenv("GOOGLE_API_KEY"))
                .baseUrl(GOOGLE_OPENAI_URL)
                .logRequests(true)
                .logResponses(true)
                .build();                

        BankTool bankTool = new BankTool();
        bankTool.createAccount("Mario", 1000.0);
        bankTool.createAccount("Georgios", 1000.0);

        WithdrawAgent withdrawAgent = AgenticServices
                .agentBuilder(WithdrawAgent.class)
                .chatModel(BASE_MODEL)
                .tools(bankTool)
                .build();
        CreditAgent creditAgent = AgenticServices
                .agentBuilder(CreditAgent.class)
                .chatModel(BASE_MODEL)
                .tools(bankTool)
                .build();

        ExchangeAgent exchangeAgent = AgenticServices
                .agentBuilder(ExchangeAgent.class)
                .chatModel(BASE_MODEL)
                .tools(new ExchangeTool())
                .build();

        SupervisorAgent bankSupervisor = AgenticServices
                .supervisorBuilder()
                .chatModel(PLANNER_MODEL)
                .subAgents(withdrawAgent, creditAgent, exchangeAgent)
                .responseStrategy(SupervisorResponseStrategy.SUMMARY)
                .build();

      String result = bankSupervisor.invoke("Transfer 100 EUR from Mario's account to Georgios' one");       
      
      IO.println(result);
    }
}
