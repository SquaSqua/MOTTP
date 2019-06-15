package infoToRun;//import lombok.AllArgsConstructor;
//import lombok.Data;

import crossingOver.CrossingOver;
import mutation.Mutation;

/**
 * Contains configuration needed to run ONE test
 */
//@AllArgsConstructor
//@Data
public class Configuration {

    private int popSize;
    private int numOfGenerations;
    private float tournamentSize;
    private boolean avoidClones;
    private Mutation mutation;
    private CrossingOver crossingOver;

    public Configuration(int popSize, int numOfGenerations, float tournamentSize, boolean avoidClones, Mutation mutation, CrossingOver crossingOver) {
        this.popSize = popSize;
        this.numOfGenerations = numOfGenerations;
        this.tournamentSize = tournamentSize;
        this.avoidClones = avoidClones;
        this.mutation = mutation;
        this.crossingOver = crossingOver;
    }

    public int getPopSize() {
        return popSize;
    }

    public int getNumOfGenerations() {
        return numOfGenerations;
    }

    public float getTournamentSize() {
        return tournamentSize;
    }
    public boolean isAvoidClones() {
        return avoidClones;
    }

    public Mutation getMutation() {
        return mutation;
    }

    public CrossingOver getCrossingOver() {
        return crossingOver;
    }
}
