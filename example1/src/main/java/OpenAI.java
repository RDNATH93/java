import com.openai.client.OpenAIClient;
import com.openai.client.okhttp.OpenAIOkHttpClient;
import com.openai.models.chat.completions.ChatCompletion;
import com.openai.models.chat.completions.ChatCompletionCreateParams;

void main() {
    OpenAIClient client = OpenAIOkHttpClient.builder()
            .apiKey(System.getenv("MODELS_GITHUB_TOKEN"))
            .baseUrl("https://models.github.ai/inference")
            .build();

    ChatCompletionCreateParams params = ChatCompletionCreateParams.builder()
            .model("openai/gpt-4o")
            .addSystemMessage("You are a concise assistant.")
            .addUserMessage("Say Hello World!")
            .build();

    ChatCompletion response = client.chat().completions().create(params);
    IO.println(response);
    IO.println("Response: " + response.choices().get(0).message().content().orElse("No response content"));
}
