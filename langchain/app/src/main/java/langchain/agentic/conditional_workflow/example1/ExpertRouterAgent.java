package langchain.agentic.conditional_workflow.example1;

import dev.langchain4j.agentic.Agent;
import dev.langchain4j.service.V;

public interface ExpertRouterAgent {

    @Agent
    String ask(@V("request")String request);

}
