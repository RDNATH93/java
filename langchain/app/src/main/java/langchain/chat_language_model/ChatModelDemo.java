package langchain.chat_language_model;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import dev.langchain4j.data.message.AiMessage;
import dev.langchain4j.data.message.SystemMessage;
import dev.langchain4j.data.message.TextContent;
import dev.langchain4j.data.message.UserMessage;
import dev.langchain4j.model.chat.request.ChatRequest;
import dev.langchain4j.model.chat.request.ResponseFormat;
import dev.langchain4j.model.chat.request.ResponseFormatType;
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

    // *****************Multiple ways to form ChatMessage*****************

     //String aiMessage = chatModel.chat("Tell me top 10 cities in the world by population");
    // logger.info("AI Response: {}", aiMessage);

     UserMessage message = new UserMessage("Tell me top 10 cities in the world by population");
    // ChatResponse response = chatModel.chat(message);
    // logger.info("Response: {}", response);

     message = UserMessage.from("population", "Tell me top 10 cities in the world by population");
    // ChatResponse response = chatModel.chat(message);
    // logger.info("Response: {}", response);

     UserMessage childMessage = UserMessage.builder().name("Human Body")
    .addContent( TextContent.from("How many bones does a child body has")).build();
     UserMessage adultMessage= UserMessage.userMessage(TextContent.from("How many bones does an adult human body has"));
    // ChatResponse response = chatModel.chat(List.of(childMessage,adultMessage));
    // logger.info("Response: {}", response);

    ChatRequest chatRequest = ChatRequest.builder()
    .messages(UserMessage.from("capital of NewZeeland"))
    .temperature(0.1)
    .topP(0.8)
    .maxOutputTokens(10)
    .responseFormat(ResponseFormat.TEXT)
    .build();
    // ChatResponse chatResponse = chatModel.chat(chatRequest);
    // logger.info("Chat Response: {}", chatResponse);

    // Types of ChatMessage
    UserMessage pritateJoke = UserMessage.from("Tell me a joke on pirates");
    UserMessage coderJoke = UserMessage.from("Tell me a joke on programmer");
    //ChatResponse multipleChatResponse = chatModel.chat(pritateJoke, coderJoke);
    // logger.info("Multiple ChatResponse {}", multipleChatResponse);


    // System message to instruct LLM to reply like Amitabh Bachchan
    SystemMessage amitabhSystemMsg = SystemMessage.from("""
    Reply in the style of Kevin Hart, an American comedian and actor.
    Use his tone, mannerisms, and signature style in your responses.
    """);
    UserMessage userMsg = UserMessage.from("Tell me a joke");
   // ChatResponse hartResponse = chatModel.chat(amitabhSystemMsg, userMsg);
    //logger.info("Response: {}", hartResponse.aiMessage().text());

    // Multiple ChatMessages
    UserMessage firstUserMessage = UserMessage.from("Hello, my name is Bond, James Bond.");
    // AiMessage firstAiMessage = chatModel.chat(firstUserMessage).aiMessage();
    // logger.info("firstAiMessage: {}", firstAiMessage.text());
    // UserMessage secondUserMessage = UserMessage.from("What is my name?");
    // AiMessage secondAiMessage = chatModel.chat(firstUserMessage, firstAiMessage,secondUserMessage).aiMessage();
    // logger.info("secondAiMessage: {}", secondAiMessage.text());

    // //Multimodality - Text Content
    UserMessage userMessage = UserMessage.from(List.of(TextContent.from("Hello!"),TextContent.from("How are you?")));
    ChatResponse textContentChatResponse= chatModel.chat(userMessage);
    logger.info("Text Content Chat Response {}",textContentChatResponse);

  }

}
