package langchain.aiservice;

import dev.langchain4j.model.chat.ChatModel;
import dev.langchain4j.model.openai.OpenAiChatModel;
import dev.langchain4j.model.openai.OpenAiModerationModel;
import dev.langchain4j.service.AiServices;
import dev.langchain4j.service.Moderate;
import dev.langchain4j.service.ModerationException;

import static dev.langchain4j.model.openai.OpenAiChatModelName.GPT_4_O_MINI;
import static dev.langchain4j.model.openai.OpenAiModerationModelName.TEXT_MODERATION_LATEST;

public class ServiceWithAutoModerationDemo {
    interface Chat {

        @Moderate
        String chat(String text);
    }

    public static void main(String[] args) {

        OpenAiModerationModel moderationModel = OpenAiModerationModel.builder()
                .baseUrl("http://langchain4j.dev/demo/openai/v1")
                .apiKey("demo")
                .modelName(TEXT_MODERATION_LATEST)
                .logRequests(true)
                .logResponses(true)
                .build();


        OpenAiChatModel chatModel = OpenAiChatModel.builder()
                .baseUrl("http://langchain4j.dev/demo/openai/v1")
                .apiKey("demo")
                .modelName(GPT_4_O_MINI)
                .logRequests(true)
               // .logResponses(true)
                .build();

        Chat chat = AiServices.builder(Chat.class)
                .chatModel(chatModel)
                .moderationModel(moderationModel)
                .build();

        try {
            chat.chat("I WILL KILL YOU!!!");
        } catch (ModerationException e) {
            System.out.println(e.getMessage());
            // Text "I WILL KILL YOU!!!" violates content policy
        }
    }
}