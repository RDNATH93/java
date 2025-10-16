package langchain.agentic.parallel_workflow.example1;

import java.util.List;

import dev.langchain4j.agentic.Agent;
import dev.langchain4j.service.V;

public interface EveningPlannerAgent {
    
    @Agent
    List<EveningPlan> plan(@V("mood") String mood);
}
