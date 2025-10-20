package langchain.pure_agentic_ai.example1;

import dev.langchain4j.agent.tool.P;
import dev.langchain4j.agent.tool.Tool;

public class ExchangeTool {

    @Tool("Exchange the given amount of money from the original to the target currency")
    Double exchange(@P("originalCurrency") String originalCurrency, @P("amount") Double amount,
            @P("targetCurrency") String targetCurrency) {
        // Invoke a REST service to get the exchange rate
        return amount *1.17;
    }
}