import com.azure.ai.inference.ChatCompletionsClient;
import com.azure.ai.inference.ChatCompletionsClientBuilder;
import com.azure.ai.inference.models.ChatCompletions;
import com.azure.ai.inference.models.ChatCompletionsOptions;
import com.azure.ai.inference.models.ChatRequestMessage;
import com.azure.ai.inference.models.ChatRequestSystemMessage;
import com.azure.ai.inference.models.ChatRequestUserMessage;
import com.azure.core.credential.AzureKeyCredential;

void main() {
    String key = System.getenv("MODELS_GITHUB_TOKEN");

    String endpoint = "https://models.github.ai/inference";
    String model = "openai/gpt-5";

    ChatCompletionsClient client = new ChatCompletionsClientBuilder()
            .credential(new AzureKeyCredential(key))
            .endpoint(endpoint)
            .buildClient();

    List<ChatRequestMessage> chatMessages = Arrays.asList(
            new ChatRequestSystemMessage("You are a helpful assistant."),
            new ChatRequestUserMessage("Tell me 3 jokes about trains"));

    ChatCompletionsOptions chatCompletionsOptions = new ChatCompletionsOptions(chatMessages);
    chatCompletionsOptions.setModel(model);

    ChatCompletions completions = client.complete(chatCompletionsOptions);

    System.out.printf("%s.%n", completions.getChoice().getMessage().getContent());
}