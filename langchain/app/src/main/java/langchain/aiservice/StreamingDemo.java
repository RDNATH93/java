package langchain.aiservice;

import static java.util.concurrent.TimeUnit.SECONDS;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;

import dev.langchain4j.model.chat.StreamingChatModel;
import dev.langchain4j.model.chat.response.ChatResponse;
import dev.langchain4j.model.openai.OpenAiStreamingChatModel;
import dev.langchain4j.service.AiServices;
import dev.langchain4j.service.SystemMessage;
import dev.langchain4j.service.TokenStream;

interface QueryAssistant {

    @SystemMessage("You are a helpful assistant. Answer user question pricisely and politely")
    TokenStream chat(String userMessage);
}

public class StreamingDemo {

    public static void main(String[] args) throws Exception {
        StreamingChatModel model = OpenAiStreamingChatModel.builder()
                .baseUrl("https://openrouter.ai/api/v1")
                .apiKey(System.getenv("OPEN_ROUTER_GEMMA_3_27B"))
                .modelName("google/gemma-3-27b-it:free")
                .build(); 
           
        //CompletableFuture<String>future= new CompletableFuture<>();
        QueryAssistant assistant = AiServices.create(QueryAssistant.class, model);        
        TokenStream tokenStream = assistant.chat("How to prepare Chicken Biryani");

       
        // tokenStream.onPartialResponse(System.out::print)
        // .onCompleteResponse((reponse)->System.out.println("I hope this answer your query"))
        // .onError(Throwable::printStackTrace)
        // .start();

        // future.join();

        CompletableFuture<ChatResponse> futureResponse = new CompletableFuture<>();

        tokenStream.onPartialResponse(System.out::print)
                .onCompleteResponse(futureResponse::complete)
                .onError(futureResponse::completeExceptionally)
                .start();

        ChatResponse chatResponse = futureResponse.get(60, SECONDS);
        System.out.println("\n" + chatResponse);
    }
}
