public class Configuration {

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


    //getters
    int getPopSize() {
        return popSize;
    }
    int getNumOfGeners() {
        return numOfGeners;
    }
    float getCrossProb() {
        return crossProb;
    }
    float getMutProb() {
        return mutProb;
    }
    Integer getTournamentSize() {
        return tournamentSize;
    }
    boolean isAvoidClones() {
        return avoidClones;
    }
    Mutation getMutationType() {
        return mutation;
    }
    CrossingOver getCrossingOver() {
        return crossingOver;
    }
    Selection getSelection() {
        return selection;
    }
    PackingPlanSetter getPackingPlanSetter() {
        return packingPlanSetter;
    }

    //setters

    public void setPopSize(int popSize) {
        this.popSize = popSize;
    }
    public void setNumOfGeners(int numOfGeners) {
        this.numOfGeners = numOfGeners;
    }
    public void setCrossProb(float crossProb) {
        this.crossProb = crossProb;
    }
    public void setMutProb(float mutProb) {
        this.mutProb = mutProb;
    }
    public void setTournamentSize(Integer tournamentSize) {
        this.tournamentSize = tournamentSize;
    }
    public void setAvoidClones(boolean avoidClones) {
        this.avoidClones = avoidClones;
    }
    public void setMutation(Mutation mutation) {
        this.mutation = mutation;
    }
    public void setCrossingOver(CrossingOver crossingOver) {
        this.crossingOver = crossingOver;
    }
    public void setSelection(Selection selection) {
        this.selection = selection;
    }
    public void setPackingPlanSetter(PackingPlanSetter packingPlanSetter) {
        this.packingPlanSetter = packingPlanSetter;
    }
}
