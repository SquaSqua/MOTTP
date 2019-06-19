package ewasko.infoToRun;

import ewasko.crossingOver.CrossingOver;
import ewasko.mutation.Mutation;
//import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * Contains configuration needed to run ONE test
 */
//@AllArgsConstructor
@Data
public class Configuration {

    public Configuration(int popSize, int numOfGenerations, float tournamentSize, boolean avoidClones, CrossingOver crossingOver, float crossProb, Mutation mutation, float mutProb) {
        this.popSize = popSize;
        this.numOfGenerations = numOfGenerations;
        this.tournamentSize = tournamentSize;
        this.avoidClones = avoidClones;
        this.crossingOver = crossingOver;
        this.crossProb = crossProb;
        this.crossingOver.setCrossProb(crossProb);
        this.mutation = mutation;
        this.mutProb = mutProb;
        this.mutation.setMutProb(mutProb);
    }

    public Configuration(Configuration configuration) {
        this.popSize = configuration.popSize;
        this.numOfGenerations = configuration.numOfGenerations;
        this.tournamentSize = configuration.tournamentSize;
        this.avoidClones = configuration.avoidClones;
        this.crossingOver = configuration.crossingOver;
        this.crossProb = configuration.crossProb;
        this.crossingOver.setCrossProb(crossProb);
        this.mutation = configuration.mutation;
        this.mutProb = configuration.mutProb;
        this.mutation.setMutProb(mutProb);
    }

    private int popSize;
    private int numOfGenerations;
    private float tournamentSize;
    private boolean avoidClones;
    private CrossingOver crossingOver;
    private float crossProb;
    private Mutation mutation;
    private float mutProb;

}
