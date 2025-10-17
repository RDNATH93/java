package langchain.agentic.error_handling.example1;

import dev.langchain4j.agentic.Agent;
import dev.langchain4j.service.UserMessage;
import dev.langchain4j.service.V;

public interface CreativeWriter {

    @UserMessage("""
                You are a creative writer.
                Generate a draft of a story no more than
                3 sentences long around the give topic.
                Return only the story and nothing else.
                The topic is {{topic}}.
            """)
    @Agent("Generates a story based on the given topic")
    String generateStory(@V("topic") String topic);
}
