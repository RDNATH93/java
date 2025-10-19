package langchain.agentic.declarative_api.example1;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import dev.langchain4j.agentic.declarative.Output;
import dev.langchain4j.agentic.declarative.ParallelAgent;
import dev.langchain4j.agentic.declarative.ParallelExecutor;
import dev.langchain4j.agentic.declarative.SubAgent;
import dev.langchain4j.service.V;

public interface EveningPlannerAgent {
    
    @ParallelAgent(outputName = "plans", subAgents = {
        @SubAgent(type = FoodExpert.class,outputName = "meals"),
        @SubAgent(type = MovieExpert.class,outputName = "movies")
    })
    List<EveningPlan> plan(@V("mood") String mood);


    @ParallelExecutor
    static Executor executor(){
        return Executors.newFixedThreadPool(2);
    }

    @Output
    static List<EveningPlan> createPlans(@V("meals")List<String>meals,@V("movies")List<String>movies){
        List<EveningPlan> mealsAndMovies= new ArrayList<>();
        for(int i=0;i<meals.size();i++){
            if(i>=movies.size()){
                break;
            }
            mealsAndMovies.add(new EveningPlan(movies.get(i),meals.get(i)));
        }
        return mealsAndMovies;
    }
 }
