package langchain.agentic.sequential_workflow.example1;

import dev.langchain4j.agentic.Agent;
import dev.langchain4j.service.V;

public interface NovelAgent {
    
    @Agent
    String createNovel(@V("topic")String tpoic,@V("audience")String audience,@V("style")String style);
    
}
