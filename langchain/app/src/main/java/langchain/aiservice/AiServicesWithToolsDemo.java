package langchain.aiservice;

import dev.langchain4j.agent.tool.Tool;
import dev.langchain4j.model.openai.OpenAiChatModel;
import dev.langchain4j.service.AiServices;

interface MathAssistant {
    String solve(String userMessage);
}

class Calculator {

    @Tool("add two numbers")
    long add(int num1, int num2) {
        System.out.println("doing addition...");
        return num1 + num2;
    }

    @Tool("substract two numbers")
    int substract(int num1, int num2) {
        System.out.println("doing substraction...");
        return num1 + num2;
    }

    @Tool("multiply two numbers")
    long multiply(int num1, int num2) {
        System.out.println("doing multiplication...");
        return num1 * num2;
    }

    @Tool("divide two numbers")
    float divide(int num1, int num2) {
        System.out.println("doing division...");
        if (num2 == 0) {
            throw new IllegalArgumentException("Cannot divide by zero");

        }
        return (float) num1 / num2;
    }
}

public class AiServicesWithToolsDemo {
    public static void main(String[] args) {

        OpenAiChatModel model = OpenAiChatModel.builder()
                .baseUrl("http://langchain4j.dev/demo/openai/v1")
                .apiKey("demo")
                .modelName("gpt-4o-mini")
                .logRequests(true)
                .logResponses(true)
                .build();

        MathAssistant mathAssistant = AiServices.builder(MathAssistant.class)
                .chatModel(model)
                .systemMessageProvider((s)->"solve problem step by step")
                .tools(new Calculator())
                .build();

        String answer = mathAssistant.solve("""
                Emma had 48 apples. She gave 12 apples to her friend, then bought 20 more apples.
                She divided all her apples equally into 4 baskets.
                Each basket of apples is then multiplied by 3 to prepare apple gift packs.
                How many apples are in total ?
                """);

        System.out.println(answer);
    }
}
