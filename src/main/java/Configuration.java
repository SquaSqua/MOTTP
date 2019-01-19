import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
class Configuration {

    private int popSize;
    private int numOfGeners;
    private float crossProb;
    private float mutProb;
    private Integer tournamentSize;
    private boolean avoidClones;
    private Mutation mutation;
    private CrossingOver crossingOver;
    private Selection selection;
    private PackingPlanSetter packingPlanSetter;
}
