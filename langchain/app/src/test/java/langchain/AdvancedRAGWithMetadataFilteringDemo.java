package langchain;

import static dev.langchain4j.data.document.Metadata.metadata;
import static dev.langchain4j.store.embedding.filter.MetadataFilterBuilder.metadataKey;
import static org.assertj.core.api.Assertions.assertThat;

import java.util.function.Function;

import org.junit.jupiter.api.Test;


import dev.langchain4j.data.document.Metadata;
import dev.langchain4j.data.segment.TextSegment;
import dev.langchain4j.memory.chat.MessageWindowChatMemory;
import dev.langchain4j.model.chat.ChatModel;
import dev.langchain4j.model.embedding.EmbeddingModel;
import dev.langchain4j.model.embedding.onnx.bgesmallenv15q.BgeSmallEnV15QuantizedEmbeddingModel;
import dev.langchain4j.store.embedding.filter.builder.sql.LanguageModelSqlFilterBuilder;
import dev.langchain4j.store.embedding.filter.builder.sql.TableDefinition;
import dev.langchain4j.model.openai.OpenAiChatModel;
import dev.langchain4j.rag.content.retriever.ContentRetriever;
import dev.langchain4j.rag.content.retriever.EmbeddingStoreContentRetriever;
import dev.langchain4j.rag.query.Query;
import dev.langchain4j.service.AiServices;
import dev.langchain4j.service.MemoryId;
import dev.langchain4j.store.embedding.EmbeddingStore;
import dev.langchain4j.store.embedding.filter.Filter;
import dev.langchain4j.store.embedding.inmemory.InMemoryEmbeddingStore;
import langchain.rag.shared.Assistant;

class AdvancedRAGWithMetadataFilteringDemo {

    public static final String GOOGLE_OPENAI_URL = "https://generativelanguage.googleapis.com/v1beta/openai";

    ChatModel model = OpenAiChatModel.builder()
            .baseUrl(GOOGLE_OPENAI_URL)
            .apiKey(System.getenv("GOOGLE_API_KEY"))
            .modelName("gemini-2.5-flash")
            .logRequests(true)
            .logResponses(true)
            .build();

    EmbeddingModel embeddingModel = new BgeSmallEnV15QuantizedEmbeddingModel();

    @Test
    void Static_Metadata_Filter_Example() {
        TextSegment catSegment = TextSegment.from("Article about cats ...", Metadata.metadata("animal", "cat"));
        TextSegment dogSegment = TextSegment.from("Article about dogs ...", Metadata.metadata("animal", "dog"));

        // embeddingStore contains segments about both dogs and birds
        EmbeddingStore<TextSegment> embeddingStore = new InMemoryEmbeddingStore<>();
        embeddingStore.add(embeddingModel.embed(catSegment).content(), catSegment);
        embeddingStore.add(embeddingModel.embed(dogSegment).content(), dogSegment);

        Filter onlyDogs = metadataKey("animal").isEqualTo("dog");

        ContentRetriever contentRetriever = EmbeddingStoreContentRetriever.builder()
                .embeddingModel(embeddingModel)
                .embeddingStore(embeddingStore)
                .filter(onlyDogs)
                .build();

        Assistant assistant = AiServices.builder(Assistant.class)
                .chatModel(model)
                .contentRetriever(contentRetriever)
                .build();

        // when
        String answer = assistant.answer("Which animal?");

        assertThat(answer).containsIgnoringCase("dog").doesNotContainIgnoringCase("cat");
    }

    interface PersonalizedAssistant {
        String chat(@MemoryId String userId, @dev.langchain4j.service.UserMessage String userMessage);
    }

    @Test
    void Dynamic_Metadata_Filter_Example() {

        // given
        TextSegment user1Info = TextSegment.from("My favorite color is green", metadata("userId", "1"));
        TextSegment user2Info = TextSegment.from("My favorite color is red", metadata("userId", "2"));

        // embeddingStore contains information about both first and second user
        EmbeddingStore<TextSegment> embeddingStore = new InMemoryEmbeddingStore<>();
        embeddingStore.add(embeddingModel.embed(user1Info).content(), user1Info);
        embeddingStore.add(embeddingModel.embed(user2Info).content(), user2Info);

        Function<Query,Filter> filterByUserId=
        query-> metadataKey("userId").isEqualTo(query.metadata().chatMemoryId().toString());

        ContentRetriever contentRetriever = EmbeddingStoreContentRetriever.builder()
        .embeddingModel(embeddingModel)
        .embeddingStore(embeddingStore)
        .dynamicFilter(filterByUserId)
        .build();


        var personalizedAssistant = AiServices.builder(PersonalizedAssistant.class)
        .chatModel(model)
        .chatMemoryProvider(memoryId->MessageWindowChatMemory.withMaxMessages(3))
        .contentRetriever(contentRetriever)
        .build();

       // when
        String answer1 = personalizedAssistant.chat("1", "Which color would be best for a dress?");
        
        // then
        assertThat(answer1)
                .containsIgnoringCase("green")
                .doesNotContainIgnoringCase("red");

       // when
        String answer2 = personalizedAssistant.chat("2", "Which color would be best for a dress?");
        
        // then
        assertThat(answer2)
                .containsIgnoringCase("red")
                .doesNotContainIgnoringCase("green");                
    }   



     @Test
    void LLM_generated_Metadata_Filter_Example() {

        // given
        TextSegment forrestGump = TextSegment.from("Forrest Gump", metadata("genre", "drama").put("year", 1994));
        TextSegment groundhogDay = TextSegment.from("Groundhog Day", metadata("genre", "comedy").put("year", 1993));
        TextSegment dieHard = TextSegment.from("Die Hard", metadata("genre", "action").put("year", 1998));

        // describe metadata keys as if they were columns in the SQL table
        TableDefinition tableDefinition = TableDefinition.builder()
                .name("movies")
                .addColumn("genre", "VARCHAR", "one of: [comedy, drama, action]")
                .addColumn("year", "INT")
                .build();

        LanguageModelSqlFilterBuilder sqlFilterBuilder = new LanguageModelSqlFilterBuilder(model, tableDefinition);

        EmbeddingStore<TextSegment> embeddingStore = new InMemoryEmbeddingStore<>();
        embeddingStore.add(embeddingModel.embed(forrestGump).content(), forrestGump);
        embeddingStore.add(embeddingModel.embed(groundhogDay).content(), groundhogDay);
        embeddingStore.add(embeddingModel.embed(dieHard).content(), dieHard);

        ContentRetriever contentRetriever = EmbeddingStoreContentRetriever.builder()
                .embeddingStore(embeddingStore)
                .embeddingModel(embeddingModel)
                .dynamicFilter(query -> sqlFilterBuilder.build(query)) // LLM will generate the filter dynamically
                .build();

        Assistant assistant = AiServices.builder(Assistant.class)
                .chatModel(model)
                .contentRetriever(contentRetriever)
                .build();

        // when
        String answer = assistant.answer("Recommend me a good drama from 90s");

        // then
        assertThat(answer)
                .containsIgnoringCase("Forrest Gump")
                .doesNotContainIgnoringCase("Groundhog Day")
                .doesNotContainIgnoringCase("Die Hard");
    }
        
}
