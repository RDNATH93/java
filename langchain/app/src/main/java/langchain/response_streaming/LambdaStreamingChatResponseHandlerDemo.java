package langchain.response_streaming;

import static dev.langchain4j.model.LambdaStreamingResponseHandler.onPartialResponse;
import static dev.langchain4j.model.LambdaStreamingResponseHandler.onPartialResponseAndError;

import java.util.concurrent.CompletableFuture;

import dev.langchain4j.model.openai.OpenAiStreamingChatModel;

public class LambdaStreamingChatResponseHandlerDemo {
    public static void main(String[] args) {
        OpenAiStreamingChatModel model = OpenAiStreamingChatModel.builder()
                .baseUrl("https://openrouter.ai/api/v1")
                .apiKey(System.getenv("OPEN_ROUTER_GEMMA_3_27B"))
                .modelName("google/gemma-3-27b-it:free")
                .build();

        CompletableFuture<String>futureResponse= new CompletableFuture<>();        
       // model.chat("Tell me a new joke",onPartialResponse(System.out::print)); 
        model.chat("Tell me a new joke",onPartialResponseAndError(System.out::print,System.out::print)); 
        futureResponse.join();       
    }
}
