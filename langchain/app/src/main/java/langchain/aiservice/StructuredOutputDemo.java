package langchain.aiservice;

import java.util.List;

import dev.langchain4j.model.ollama.OllamaChatModel;
import dev.langchain4j.model.output.FinishReason;
import dev.langchain4j.model.output.TokenUsage;
import dev.langchain4j.rag.content.Content;
import dev.langchain4j.service.AiServices;
import dev.langchain4j.service.Result;
import dev.langchain4j.service.UserMessage;
import dev.langchain4j.service.tool.ToolExecution;

interface Outliner {

    @UserMessage("Generate an outline for the article on the following topic: {{it}}")
    Result<List<String>> generateOutlineFor(String topic);
}

public class StructuredOutputDemo {
    public static void main(String[] args) {
        OllamaChatModel model = OllamaChatModel.builder()
                .baseUrl("http://localhost:11434")
                .modelName("gemma3:1b")
                .logRequests(true)
                .build();

        Outliner assistant = AiServices.create(Outliner.class, model);
        Result<List<String>> result = assistant.generateOutlineFor("Java");

        List<String> outline = result.content();
        System.out.println("Outline: "+outline);
        System.out.println("---------------------------------------------------------------");
        TokenUsage tokenUsage = result.tokenUsage();
        System.out.println("TokenUsage: "+tokenUsage);
        System.out.println("---------------------------------------------------------------");
        List<Content> sources = result.sources();
        System.out.println("Content: "+sources);
        System.out.println("---------------------------------------------------------------");
        List<ToolExecution> toolExecutions = result.toolExecutions();
        System.out.println("ToolExecutions: "+toolExecutions);
        System.out.println("---------------------------------------------------------------");
        FinishReason finishReason = result.finishReason();
        System.out.println("FinishReason: "+finishReason);
    }
}
