package langchain.rag.advanced;

import static langchain.rag.shared.Utils.startConversationWith;
import static langchain.rag.shared.Utils.toPath;

import java.io.IOException;
import java.nio.file.Files;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

import javax.sql.DataSource;

import org.h2.jdbcx.JdbcDataSource;

import dev.langchain4j.experimental.rag.content.retriever.sql.SqlDatabaseContentRetriever;
import dev.langchain4j.model.chat.ChatModel;
import dev.langchain4j.model.openai.OpenAiChatModel;
import dev.langchain4j.rag.content.retriever.ContentRetriever;
import dev.langchain4j.service.AiServices;
import langchain.rag.shared.Assistant;

/**
     * Please refer to {@link Naive_RAG_Example} for a basic context.
     * <p>
     * Advanced RAG in LangChain4j is described here: https://github.com/langchain4j/langchain4j/pull/538
     * <p>
     * This example demonstrates how to use SQL database content retriever.
     * <p>
     * WARNING! Although fun and exciting, {@link SqlDatabaseContentRetriever} is dangerous to use!
     * Do not ever use it in production! The database user must have very limited READ-ONLY permissions!
     * Although the generated SQL is somewhat validated (to ensure that the SQL is a SELECT statement),
     * there is no guarantee that it is harmless. Use it at your own risk!
     * <p>
     * In this example we will use an in-memory H2 database with 3 tables: customers, products and orders.
     * See "resources/sql" directory for more details.
     * <p>
     * This example requires "langchain4j-experimental-sql" dependency.
     */
public class AdvancedRAGSQLDatabaseRetreiverDemo {
    public static final String GOOGLE_OPENAI_URL = "https://generativelanguage.googleapis.com/v1beta/openai";

    public static void main(String[] args) {
        Assistant assistant = createAssistant();
        
        // You can ask questions such as "How many customers do we have?" and "What is our top seller?".
        startConversationWith(assistant);
    }

    private static Assistant createAssistant() {

        ChatModel model = OpenAiChatModel.builder()
                .baseUrl(GOOGLE_OPENAI_URL)
                .apiKey(System.getenv("GOOGLE_API_KEY"))
                .modelName("gemini-2.5-flash")
                .logRequests(true)
                .logResponses(true)
                .build();

        DataSource dataSource = createDataSource();

        ContentRetriever sqlDatabaseContentRetriever = SqlDatabaseContentRetriever.builder()
                .chatModel(model)
                .dataSource(dataSource)
                .build();

        return AiServices.builder(Assistant.class)
                .chatModel(model)
                .contentRetriever(sqlDatabaseContentRetriever)
                .build();
    }

    private static DataSource createDataSource() {
        JdbcDataSource dataSource = new JdbcDataSource();
        dataSource.setURL("jdbc:h2:mem:test;DB_CLOSE_DELAY=-1");
        dataSource.setUser("sa");
        dataSource.setPassword("sa");

        String createTableScript = read("sql/create_tables.sql");
        execute(createTableScript, dataSource);

        String prefillTablesScript = read("sql/prefill_tables.sql");
        execute(prefillTablesScript, dataSource);

        return dataSource;
    }

    private static void execute(String createTableScript, DataSource dataSource) {
        try (Connection connection = dataSource.getConnection(); Statement statement = connection.createStatement()) {
            for (String sql : createTableScript.split(";")) {
                statement.execute(sql.trim());
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private static String read(String path) {
        try {
            return new String(Files.readAllBytes(toPath(path)));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
