package langchain.aiservice;

import dev.langchain4j.model.ollama.OllamaChatModel;
import dev.langchain4j.service.AiServices;
import dev.langchain4j.service.UserMessage;

enum Priority {
    CRITICAL, HIGH, LOW
}

interface PriorityAnalyzer {

    @UserMessage("Analyze the priority of the following issue: {{it}}")
    Priority analyzePriority(String issueDescription);
}

public class PriorityAnalyzerDemo {
    public static void main(String[] args) {
        OllamaChatModel model = OllamaChatModel.builder()
                .baseUrl("http://localhost:11434")
                .modelName("gemma3:1b")
                .logRequests(true)
                .build();
        PriorityAnalyzer priorityAnalyzer = AiServices.create(PriorityAnalyzer.class, model);

        Priority priority = priorityAnalyzer
                .analyzePriority("""
                        The main payment gateway is down, and customers cannot process transactions.""");

        System.out.println("Priority: " + priority);

        priority = priorityAnalyzer
                .analyzePriority("""
                        Write a newslatter on GenAI on this weekend.""");

        System.out.println("Priority: " + priority);

                priority = priorityAnalyzer
                .analyzePriority("""
                        Create a youtube video on latest updates from Google I/O.""");

        System.out.println("Priority: " + priority);
    }
}
