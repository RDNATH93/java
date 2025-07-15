package langchain.rag.advanced;

import java.util.Collection;

import dev.langchain4j.rag.query.Query;
import dev.langchain4j.rag.query.transformer.QueryTransformer;

public class LoggingQueryTransformer implements QueryTransformer {
    private final QueryTransformer delegate;

    public LoggingQueryTransformer(QueryTransformer delegate) {
        this.delegate = delegate;
    }

    @Override
    public Collection<Query> transform(Query query) {
        Collection<Query> transformedQueries = delegate.transform(query);
        System.out.println("Original query: " + query.text());
        System.out.println("Compressed queries: " +
                transformedQueries.stream()
                        .map(Query::text)
                        .collect(java.util.stream.Collectors.joining(", ")));
        return transformedQueries;
    }
}