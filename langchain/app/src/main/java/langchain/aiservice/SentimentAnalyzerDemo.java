package langchain.aiservice;

import dev.langchain4j.model.openai.OpenAiChatModel;
import dev.langchain4j.service.AiServices;
import dev.langchain4j.service.UserMessage;

interface SentimentAnalyzer {

    @UserMessage("Does {{it}} has a positive sentiment?")
    boolean isPositive(String text);

}

public class SentimentAnalyzerDemo {
    public static void main(String[] args) {
        OpenAiChatModel model = OpenAiChatModel.builder()
                .baseUrl("http://langchain4j.dev/demo/openai/v1")
                .logRequests(true)
                .apiKey("demo")
                .modelName("gpt-4o-mini")
                .build();

        SentimentAnalyzer sentimentAnalyzer = AiServices.create(SentimentAnalyzer.class, model);

        boolean result = sentimentAnalyzer.isPositive("It's wonderful!");

        System.out.println("Result: " + result);
    }
}
