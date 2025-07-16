package langchain.rag.advanced;

import java.util.Collection;

import dev.langchain4j.rag.content.retriever.ContentRetriever;
import dev.langchain4j.rag.query.Query;
import dev.langchain4j.rag.query.router.QueryRouter;

public class LoggingQueryRouter implements QueryRouter {

    private QueryRouter router;
    LoggingQueryRouter(QueryRouter router){
        this.router=router;
    }

    @Override
    public Collection<ContentRetriever> route(Query query) {
        Collection<ContentRetriever> contentRetrievers = router.route(query);
         System.out.println("ContentRetrivers: " +contentRetrievers);
        return contentRetrievers;
    }
    
}
