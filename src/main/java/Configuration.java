import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * Contains configuration needed to run ONE test
 */
@AllArgsConstructor
@Data
class Configuration {
    private int popSize;
    private int numOfGenerations;
//    private float crossProb;
//    private float mutProb;
    private float tournamentSize;
    private boolean avoidClones;
    private Mutation mutation;
    private CrossingOver crossingOver;
//    private PackingPlanSetter packingPlanSetter;
}
