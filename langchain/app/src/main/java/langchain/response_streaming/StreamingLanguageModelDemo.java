package langchain.response_streaming;

import java.util.concurrent.CompletableFuture;

import dev.langchain4j.model.StreamingResponseHandler;
import dev.langchain4j.model.openai.OpenAiStreamingLanguageModel;
import dev.langchain4j.model.output.Response;

public class StreamingLanguageModelDemo {
    public static void main(String[] args) {
        OpenAiStreamingLanguageModel model = OpenAiStreamingLanguageModel.builder()
                .baseUrl("https://openrouter.ai/api/v1")
                .apiKey(System.getenv("OPEN_ROUTER_GEMMA_3_27B"))
                .logRequests(true)
                .logResponses(true)
                .modelName("google/gemma-3-27b-it:free")
                .build();

        CompletableFuture<Response<String>>fetureResponse= new CompletableFuture<>();
         
        model.generate("Tell me intresting fact about universe",new StreamingResponseHandler<>() {

            @Override
            public void onNext(String token) {
                System.out.print(token);
            }

            @Override
            public void onComplete(Response<String> response) {
                fetureResponse.complete(response);
            }

            @Override
            public void onError(Throwable error) {
                fetureResponse.completeExceptionally(error);
            }
        });

        fetureResponse.join();
    }  
}
