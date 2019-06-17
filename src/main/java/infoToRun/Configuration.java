package infoToRun;

import crossingOver.CrossingOver;
import mutation.Mutation;
import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * Contains configuration needed to run ONE test
 */
@AllArgsConstructor
@Data
public class Configuration {

    private int popSize;
    private int numOfGenerations;
    private float tournamentSize;
    private boolean avoidClones;
    private Mutation mutation;
    private CrossingOver crossingOver;
}
