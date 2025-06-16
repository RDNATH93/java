package jagent;

import static java.nio.charset.StandardCharsets.UTF_8;

import java.util.Map;
import java.util.Scanner;

import com.google.adk.agents.BaseAgent;
import com.google.adk.agents.LlmAgent;
import com.google.adk.events.Event;
import com.google.adk.runner.InMemoryRunner;
import com.google.adk.sessions.Session;
import com.google.adk.tools.FunctionTool;
import com.google.genai.types.Content;
import com.google.genai.types.Part;

import io.reactivex.rxjava3.core.Flowable;

import com.google.adk.tools.Annotations.Schema;

public class CustomerAgent {

    public static BaseAgent ROOT_AGENT = initAgent();

    public static BaseAgent initAgent() {
        return LlmAgent.builder()
                .name("order-status-agent")
                .model("gemini-2.0-flash")
                .description("query the status of customer order")
                .instruction("""
                        You are a helpful order service agent who can answer questions
                        about customer orders.When a user ask for the status of an
                        order,call the `retrieveOrderById` function """)
                .tools(FunctionTool.create(CustomerOrderTools.class, "retrieveOrderById"))
                .build();
    }

    public static class CustomerOrderTools {
        @Schema(description = "Retrieve the status of an order by its ID")
        public static Map<String, String> retrieveOrderById(
                @Schema(name = "orderId", description = "The ID of the order to retrieve") String orderId) {

            return Map.of("orderId", orderId,
                          "status", "DELIVERED");
        }
    }

    public static void main(String[] args) {
        InMemoryRunner runner = new InMemoryRunner(ROOT_AGENT);
        Session session = runner
                .sessionService()
                .createSession(runner.appName(), "customer")
                .blockingGet();

        try (Scanner scanner = new Scanner(System.in, UTF_8)) {
            while (true) {
                System.out.print("\nYou > ");
                String userInput = scanner.nextLine();
                if ("quit".equalsIgnoreCase(userInput)) {
                    break;
                }
                Content userMsg = Content.fromParts(Part.fromText(userInput));
                Flowable<Event> events =
                    runner.runAsync(session.userId(), session.id(), userMsg);

                System.out.print("\nAgent > ");
                events.blockingForEach(event -> {
                    System.out.println(event.stringifyContent());
                });
            }
        }        
    }
}
