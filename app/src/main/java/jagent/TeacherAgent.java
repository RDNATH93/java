package jagent;

import static java.nio.charset.StandardCharsets.UTF_8;

import java.util.Map;
import java.util.Scanner;

import com.google.adk.agents.BaseAgent;
import com.google.adk.agents.LlmAgent;
import com.google.adk.events.Event;
import com.google.adk.events.EventActions;
import com.google.adk.runner.InMemoryRunner;
import com.google.adk.sessions.Session;
import com.google.genai.types.Content;
import com.google.genai.types.Part;

import io.reactivex.rxjava3.core.Flowable;

public class TeacherAgent {

    public static BaseAgent ROOT_AGENT = initAgent();

    public static BaseAgent initAgent() {
        return LlmAgent.builder()
                .name("teacher-app")
                .description("teacher agent")
                .model("gemini-2.0-flash")
                .instruction("""
                        You are a helpful {topic} teacher that explains
                        {topic} concepts to {audience}.
                        """)
                .build();
    }

    public static void main(String[] args) {
        InMemoryRunner runner = new InMemoryRunner(ROOT_AGENT);

        Session session = runner
                .sessionService()
                .createSession(runner.appName(), "userId",
                    new java.util.concurrent.ConcurrentHashMap<>(
                     Map.of("topic", "science", "audience", "kids")),
                    null)
                .blockingGet();

        // right way to update session state
        runner.sessionService().appendEvent(session, Event.builder().actions(
            EventActions.builder().stateDelta(
                new java.util.concurrent.ConcurrentHashMap<>(
                    Map.of("topic", "Physics"))).build()
            ).build()
        );
        try (Scanner scanner = new Scanner(System.in, UTF_8)) {
            while (true) {
                System.out.print("\nYou > ");
                String userInput = scanner.nextLine();
                if ("quit".equalsIgnoreCase(userInput)) {
                    break;
                }

                Content userMsg = Content.fromParts(Part.fromText(userInput));
                Flowable<Event> events = runner.runAsync(session.userId(), session.id(), userMsg);

                System.out.print("\nAgent > ");
                events.blockingForEach(event -> {
                    System.out.println(event.stringifyContent());
                });
            }
        }
    }
}
