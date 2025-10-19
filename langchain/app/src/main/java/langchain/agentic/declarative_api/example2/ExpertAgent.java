package langchain.agentic.declarative_api.example2;

import dev.langchain4j.agentic.declarative.ActivationCondition;
import dev.langchain4j.agentic.declarative.ChatModelSupplier;
import dev.langchain4j.agentic.declarative.ConditionalAgent;
import dev.langchain4j.agentic.declarative.SubAgent;
import dev.langchain4j.model.chat.ChatModel;
import dev.langchain4j.model.openai.OpenAiChatModel;
import dev.langchain4j.service.V;

public interface ExpertAgent {

    public static final String GOOGLE_OPENAI_URL = "https://generativelanguage.googleapis.com/v1beta/openai";

    @ConditionalAgent(outputName = "response", subAgents = {
            @SubAgent(type = MedicalExpert.class, outputName = "response"),
            @SubAgent(type = TechnicalExpert.class, outputName = "response"),
            @SubAgent(type = LegalExpert.class, outputName = "response")
    })
    String askExpert(@V("request") String request);

    @ActivationCondition(LegalExpert.class)
    static boolean activateLegal(@V("category") RequestCategory category) {
        return category == RequestCategory.LEGAL;
    }

    @ActivationCondition(MedicalExpert.class)
    static boolean activateMedical(@V("category") RequestCategory category) {
        return category == RequestCategory.MEDICAL;
    }

    @ActivationCondition(TechnicalExpert.class)
    static boolean activateTechnical(@V("category") RequestCategory category) {
        return category == RequestCategory.TECHNICAL;
    }

    @ChatModelSupplier
    static ChatModel model() {
        return OpenAiChatModel.builder()
                .modelName("gemini-2.5-flash")
                .apiKey(System.getenv("GOOGLE_API_KEY"))
                .baseUrl(GOOGLE_OPENAI_URL)
                .logRequests(true)
                .logResponses(true)
                .build();
    }
}
