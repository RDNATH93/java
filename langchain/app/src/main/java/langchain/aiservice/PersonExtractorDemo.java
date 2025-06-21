package langchain.aiservice;

import java.time.LocalDate;

import dev.langchain4j.model.ollama.OllamaChatModel;
import dev.langchain4j.model.output.structured.Description;
import dev.langchain4j.service.AiServices;
import dev.langchain4j.service.UserMessage;

record Person (

    @Description("first name of a person") // you can add an optional description to help an LLM have a better understanding
    String firstName,
    String lastName,
    LocalDate birthDate,
    Address address
){}

@Description("an address") // you can add an optional description to help an LLM have a better understanding
record Address(
    String street,
    Integer streetNumber,
    String city
){}

interface PersonExtractor {

    @UserMessage("Extract information about a person from {{it}}")
    Person extractPersonFrom(String text);
}

public class PersonExtractorDemo {
    public static void main(String[] args) {

        OllamaChatModel model = OllamaChatModel.builder()
                .baseUrl("http://localhost:11434")
                .modelName("gemma3:1b")
                .logRequests(true)
                .build();

        PersonExtractor personExtractor = AiServices.create(PersonExtractor.class, model);

        String text = """
                In 1968, amidst the fading echoes of Independence Day,
                a child named John arrived under the calm evening sky.
                This newborn, bearing the surname Doe, marked the start of a new journey.
                He was welcomed into the world at 345 Whispering Pines Avenue
                a quaint street nestled in the heart of Springfield
                an abode that echoed with the gentle hum of suburban dreams and aspirations.
                """;

        Person person = personExtractor.extractPersonFrom(text);

        System.out.println(person);
    }
}
