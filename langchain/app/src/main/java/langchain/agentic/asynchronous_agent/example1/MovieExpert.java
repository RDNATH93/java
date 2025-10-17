package langchain.agentic.asynchronous_agent.example1;

import java.util.List;

import dev.langchain4j.agentic.Agent;
import dev.langchain4j.service.UserMessage;
import dev.langchain4j.service.V;

public interface MovieExpert {
    @UserMessage("""
            You are a great evening planner.
            Propose a list of 3 movies matching the given mood.
            The mood is {{mood}}.
            Provide a list with the 3 items and nothing else.
            """)
    @Agent
    List<String> findMovie(@V("mood") String mood);
}
