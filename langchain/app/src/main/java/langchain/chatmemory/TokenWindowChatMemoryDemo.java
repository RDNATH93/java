package langchain.chatmemory;

import static dev.langchain4j.model.openai.OpenAiChatModelName.GPT_4_O_MINI;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import dev.langchain4j.data.message.AiMessage;
import dev.langchain4j.data.message.ChatMessage;
import dev.langchain4j.data.message.UserMessage;
import dev.langchain4j.memory.ChatMemory;
import dev.langchain4j.memory.chat.TokenWindowChatMemory;
import dev.langchain4j.model.chat.response.ChatResponse;
import dev.langchain4j.model.openai.OpenAiChatModel;
import dev.langchain4j.model.openai.OpenAiTokenCountEstimator;

public class TokenWindowChatMemoryDemo {

    private static final Logger logger = LoggerFactory.getLogger(TokenWindowChatMemoryDemo.class);
    private static OpenAiTokenCountEstimator openAiTokenCountEstimator;
    public static void main(String[] args) {
        OpenAiChatModel model = OpenAiChatModel.builder()
                .baseUrl("https://openrouter.ai/api/v1")
                .apiKey(System.getenv("OPEN_ROUTER_GEMMA_3_27B"))
                .maxCompletionTokens(50)
                .modelName("google/gemma-3-27b-it:free")
                .build();
                
        openAiTokenCountEstimator = new OpenAiTokenCountEstimator(GPT_4_O_MINI);
        TokenWindowChatMemory chatMemory = TokenWindowChatMemory.withMaxTokens(150, openAiTokenCountEstimator);

        // first usermessage added to chat memory
        chatMemory.add(UserMessage.from("Hello, My Name is Bond, James Bond"));
        getChatMemoryInsites(chatMemory);

        ChatResponse response = model.chat(chatMemory.messages());
        //logger.info("Response: {}",response);
        logger.info("Token Usage: {}", response.tokenUsage()); 

        //adding aimessage to chat memory
        chatMemory.add(AiMessage.from(response.aiMessage().text()));
        //adding next user message in chat memory
        chatMemory.add(UserMessage.from("What is my name"));
        getChatMemoryInsites(chatMemory);

        response = model.chat(chatMemory.messages());
        // logger.info("Response: {}",response);
        logger.info("Token Usage: {}", response.tokenUsage());
        
        chatMemory.add(AiMessage.from(response.aiMessage().text()));
        chatMemory.add(UserMessage.from("Tell me about all my missions"));
        getChatMemoryInsites(chatMemory);

        response = model.chat(chatMemory.messages());
       // logger.info("AiMessage: {}", response);
        logger.info("Token Usage: {}", response.tokenUsage());


        chatMemory.add(AiMessage.from(response.aiMessage().text()));
        chatMemory.add(UserMessage.from("Which missions do you think was toughest"));
        getChatMemoryInsites(chatMemory);

        response = model.chat(chatMemory.messages());
       // logger.info("AiMessage: {}", response);
        logger.info("Token Usage: {}", response.tokenUsage());

        chatMemory.add(AiMessage.from(response.aiMessage().text()));
        getChatMemoryInsites(chatMemory);

    }


    public static void getChatMemoryInsites(ChatMemory chatMemory){
        logger.info("################################################################################################");
           for(ChatMessage message:chatMemory.messages()){
            if(message instanceof UserMessage){
                logger.info("UserMessage: {}",((UserMessage)(message)).singleText());
                logger.info("TokenCountEstimator: {}", openAiTokenCountEstimator.estimateTokenCountInMessage(message));
            }else if(message instanceof AiMessage){
                logger.info("AiMessage: {}", ((AiMessage)(message)).text());
                logger.info("TokenCountEstimator: {}", openAiTokenCountEstimator.estimateTokenCountInMessage(message));
            }
        }

        logger.info("Estimate Token Count: {}", openAiTokenCountEstimator.estimateTokenCountInMessages(chatMemory.messages()));
        logger.info("################################################################################################\n");
    }
}