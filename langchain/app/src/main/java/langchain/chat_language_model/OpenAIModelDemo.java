package langchain.chat_language_model;

import dev.langchain4j.data.image.Image;
import dev.langchain4j.data.message.ImageContent;
import dev.langchain4j.data.message.TextContent;
import dev.langchain4j.data.message.UserMessage;
import dev.langchain4j.model.chat.response.ChatResponse;
import dev.langchain4j.model.image.ImageModel;
import dev.langchain4j.model.openai.OpenAiChatModel;
import dev.langchain4j.model.openai.OpenAiImageModel;
import dev.langchain4j.model.output.Response;

import static dev.langchain4j.model.openai.OpenAiImageModelName.DALL_E_3;

/**
 * added langchain4j-open-ai dependency
 * using OpenAI DALL_E_3 model for Image Generation
 * 
 * using Google: Gemma 3 27B (free) model hosted in OpenRouter platform for image to text generation
 */

public class OpenAIModelDemo {

        public static void main(String[] args) {

                // OpenAiChatModel model = OpenAiChatModel.builder()
                //                 .baseUrl("http://langchain4j.dev/demo/openai/v1")
                //                 .apiKey("demo")
                //                 .modelName("gpt-4o-mini")
                //                 .build();

                // String response= chatModel.chat("Tell me a joke");
                // System.out.println(response);

                OpenAiChatModel model = OpenAiChatModel.builder()
                                .baseUrl("https://openrouter.ai/api/v1")
                                .apiKey("API_KEY")
                                .modelName("google/gemma-3-27b-it:free")
                                .build();

                UserMessage userMessage = UserMessage.from(
                                TextContent.from("Describe the following image"),
                                ImageContent.from("https://gratisography.com/wp-content/uploads/2024/10/gratisography-birthday-dog-sunglasses-800x525.jpg"));

                ChatResponse response = model.chat(userMessage);

                System.out.println(response);

                // ImageModel model = OpenAiImageModel.builder()
                // .baseUrl("http://langchain4j.dev/demo/openai/v1")
                // .apiKey("demo")
                // .modelName(DALL_E_3)
                // .build();

                // Response<Image> imgResponse = model.generate(
                // "Swiss software developers with cheese fondue, a parrot and a cup of
                // coffee");

                // System.out.println(imgResponse.content().url());

        }

}
