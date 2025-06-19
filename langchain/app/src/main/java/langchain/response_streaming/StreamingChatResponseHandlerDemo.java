package langchain.response_streaming;

import java.util.List;
import java.util.concurrent.CompletableFuture;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import dev.langchain4j.data.message.ChatMessage;
import dev.langchain4j.data.message.SystemMessage;
import dev.langchain4j.data.message.UserMessage;
import dev.langchain4j.model.chat.response.ChatResponse;
import dev.langchain4j.model.chat.response.StreamingChatResponseHandler;
import dev.langchain4j.model.openai.OpenAiStreamingChatModel;

public class StreamingChatResponseHandlerDemo {

    private static final Logger logger = LoggerFactory.getLogger(StreamingChatResponseHandlerDemo.class);

    public static void main(String[] args) {
        OpenAiStreamingChatModel model = OpenAiStreamingChatModel.builder()
                .baseUrl("https://openrouter.ai/api/v1")
                .apiKey(System.getenv("OPEN_ROUTER_GEMMA_3_27B"))
                .modelName("google/gemma-3-27b-it:free")
                .build();

        CompletableFuture<ChatResponse> futureResponse = new CompletableFuture<>();

        List<ChatMessage> messages = List.of(
                SystemMessage.from("You are a very sarcastic assistant"),
                UserMessage.from("Tell me a joke"));

        // model.chat("Tell me a joke", new StreamingChatResponseHandler() {
        model.chat(messages, new StreamingChatResponseHandler() {

            @Override
            public void onPartialResponse(String partialResponse) {
                // logger.info("onPartialResponse: {}", partialResponse);
                System.out.print(partialResponse + "");
            }

            @Override
            public void onCompleteResponse(ChatResponse completeResponse) {
                logger.info("onCompleteResponse: {}", completeResponse);
                futureResponse.complete(completeResponse);
            }

            @Override
            public void onError(Throwable error) {
                futureResponse.completeExceptionally(error);
                // error.printStackTrace();
            }
        });
        futureResponse.join();
    }
}
