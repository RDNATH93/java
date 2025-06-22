package langchain.aiservice;

import static dev.langchain4j.model.openai.OpenAiChatModelName.GPT_4_O_MINI;

import java.util.List;

import dev.langchain4j.model.chat.ChatModel;
import dev.langchain4j.model.input.structured.StructuredPrompt;
import dev.langchain4j.model.openai.OpenAiChatModel;
import dev.langchain4j.model.output.structured.Description;
import dev.langchain4j.service.AiServices;

record Recipe(
        @Description("short title, 3 words maximum") String title,

        @Description("short description, 2 sentences maximum") String description,

        @Description("each step should be described in 4 words, steps should rhyme") List<String> steps,

        Integer preparationTimeMinutes) {
}

@StructuredPrompt("Create a recipe of a {{dish}} that can be prepared using only {{ingredients}}")
record CreateRecipePrompt(
        String dish,
        List<String> ingredients) {
}

interface Chef {
    Recipe createRecipeFrom(String... ingredients);

    Recipe createRecipe(CreateRecipePrompt prompt);

}

public class RecipeWithJSONModeDemo {
    /**
     * @param args
     */
    public static void main(String[] args) {
            ChatModel model = OpenAiChatModel.builder()
                    .baseUrl("http://langchain4j.dev/demo/openai/v1")
                    .apiKey("demo")
                    .modelName(GPT_4_O_MINI)
                    // When extracting POJOs with the LLM that supports the "json mode" feature
                    // (e.g., OpenAI, Azure OpenAI, Vertex AI Gemini, Ollama, etc.),
                    // it is advisable to enable it (json mode) to get more reliable results.
                    // When using this feature, LLM will be forced to output a valid JSON.
                    .responseFormat("json_schema")
                    .strictJsonSchema(true) // https://docs.langchain4j.dev/integrations/language-models/open-ai#structured-outputs-for-json-mode
                    .logRequests(true)
                    .logResponses(true)
                    .build();

        Chef chef = AiServices.create(Chef.class, model);
        Recipe recipe = chef.createRecipeFrom(new String[] { "Basmati Rice", "Chicken", "Yogurt",
                "Onions", "Herbs", "Spices", "Oil/Ghee", "Saffron-infused milk" });

        System.out.println(recipe);

        // CreateRecipePrompt prompt = new CreateRecipePrompt("Chicken Biryani",
        //         List.of("Basmati Rice", "Chicken", "Yogurt",
        //                 "Onions", "Herbs", "Spices", "Oil/Ghee", "Saffron-infused milk"));

        // Recipe anotherRecipe = chef.createRecipe(prompt);
        // System.out.println(anotherRecipe);

    }
}
