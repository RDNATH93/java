package langchain;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import dev.langchain4j.data.message.ChatMessage;
import dev.langchain4j.data.message.UserMessage;
import dev.langchain4j.model.chat.request.ChatRequest;
import dev.langchain4j.model.chat.response.ChatResponse;
import dev.langchain4j.model.ollama.OllamaChatModel;

public class ChatModelDemo {
  
  private final static Logger logger = LoggerFactory.getLogger(ChatModelDemo.class);

  static String MODEL_NAME = "gemma3:1b"; // try other local ollama model names
  static String BASE_URL = "http://localhost:11434"; // local ollama base url



  public static void main(String[] args) {
    OllamaChatModel chatModel = OllamaChatModel.builder()
        .baseUrl(BASE_URL)
        .modelName(MODEL_NAME)
        .logRequests(true)
        .build();

      String aiMessage= chatModel.chat("Tell me top 10 cities in the world by population");
      logger.info("AI Response: {}", aiMessage);

     
     ChatRequest chatRequest = ChatRequest.builder()
        .messages(UserMessage.from("What is the capital of France?"))
        .build();

      ChatResponse chatResponse = chatModel.chat(chatRequest);

       logger.info("Chat Response: {}", chatResponse);

  }

}
