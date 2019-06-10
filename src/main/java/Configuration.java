//import lombok.AllArgsConstructor;
//import lombok.Data;

/**
 * Contains configuration needed to run ONE test
 */
//@AllArgsConstructor
//@Data
class Configuration {

    private int popSize;
    private int numOfGenerations;
    private float tournamentSize;
    private boolean avoidClones;
    private Mutation mutation;
    private CrossingOver crossingOver;

    Configuration(int popSize, int numOfGenerations, float tournamentSize, boolean avoidClones, Mutation mutation, CrossingOver crossingOver) {
        this.popSize = popSize;
        this.numOfGenerations = numOfGenerations;
        this.tournamentSize = tournamentSize;
        this.avoidClones = avoidClones;
        this.mutation = mutation;
        this.crossingOver = crossingOver;
    }

    int getPopSize() {
        return popSize;
    }

    int getNumOfGenerations() {
        return numOfGenerations;
    }

    float getTournamentSize() {
        return tournamentSize;
    }
    boolean isAvoidClones() {
        return avoidClones;
    }

    Mutation getMutation() {
        return mutation;
    }

    CrossingOver getCrossingOver() {
        return crossingOver;
    }
}
