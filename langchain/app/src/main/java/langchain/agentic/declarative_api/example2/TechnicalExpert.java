package langchain.agentic.declarative_api.example2;

import dev.langchain4j.agentic.Agent;
import dev.langchain4j.service.UserMessage;
import dev.langchain4j.service.V;

public interface TechnicalExpert {
        @UserMessage("""
        You are a technical expert.
        Analyze the following user request under a technical point of view and provide the best possible answer.
        The user request is {{request}}.
        """)
    @Agent("A technical expert")
    String technical(@V("request") String request);
}
