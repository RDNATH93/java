package langchain.tools_function_calling;

import dev.langchain4j.agent.tool.Tool;
import dev.langchain4j.model.openai.OpenAiChatModel;
import dev.langchain4j.service.AiServices;
import dev.langchain4j.service.UserMessage;

interface RouterAgent {

    @UserMessage("""
            Analyze the following user request and categorize it as 'legal', 'medical' or 'technical',
            then forward the request as it is to the corresponding expert provided as a tool.
            Finally return the answer that you received from the expert without any modification.

            The user request is: '{{it}}'.
            """)
    String askToExpert(String request);
}

interface MedicalExpert {

    @UserMessage("""
            You are a medical expert.
            Analyze the following user request under a medical point of view and provide the best possible answer.
            The user request is {{it}}.
            """)
    @Tool("A medical expert")
    String medicalRequest(String request);
}

interface LegalExpert {

    @UserMessage("""
            You are a legal expert.
            Analyze the following user request under a legal point of view and provide the best possible answer.
            The user request is {{it}}.
            """)
    @Tool("A legal expert")
    String legalRequest(String request);
}

interface TechnicalExpert {

    @UserMessage("""
            You are a technical expert.
            Analyze the following user request under a technical point of view and provide the best possible answer.
            The user request is {{it}}.
            """)
    @Tool("A technical expert")
    String technicalRequest(String request);
}

public class AiServicesAsToolExample {
    public static void main(String[] args) {

        OpenAiChatModel model = OpenAiChatModel.builder()
                .baseUrl("http://langchain4j.dev/demo/openai/v1")
                .apiKey("demo")
                .modelName("gpt-4o-mini")
                .logRequests(true)
                .logResponses(true)
                .build();

        MedicalExpert medicalExpert = AiServices.builder(MedicalExpert.class)
                .chatModel(model)
                .build();
        LegalExpert legalExpert = AiServices.builder(LegalExpert.class)
                .chatModel(model)
                .build();
        TechnicalExpert technicalExpert = AiServices.builder(TechnicalExpert.class)
                .chatModel(model)
                .build();

        RouterAgent routerAgent = AiServices.builder(RouterAgent.class)
                .chatModel(model)
                .tools(medicalExpert, legalExpert, technicalExpert)
                .build();

        String advice = routerAgent.askToExpert("I have a land dispute what should I do");

        System.out.println("Expert Advice: "+advice);

    }
}
