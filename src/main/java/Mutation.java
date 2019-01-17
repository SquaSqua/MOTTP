import java.util.Random;

public abstract class Mutation {
    float mutProb;
    Random random = new Random();
    int startIndex, stopIndex;
    short[] route;
    short[] subArray;

    Mutation(float mutProb) {
        this.mutProb = mutProb;
    }

    void mutate(Individual individual){
        mutate(individual, mutProb);
    }

    void mutate(Individual individual, float mutProb) {
        if(Math.random() < mutProb) {
            short[] route = individual.getRoute();
            startIndex = random.nextInt(route.length - 1);
            stopIndex = random.nextInt(route.length - 1);
            setInOrder();
            subArray = new short[stopIndex - (startIndex - 1)];
            individual.setRoute(mutateSpecifically());
            individual.setPackingPlanAndFitness();
        }
    }
    protected abstract short[] mutateSpecifically();

    private void setInOrder(){
        if (startIndex > stopIndex) {
            int indexTemp = startIndex;
            startIndex = stopIndex;
            stopIndex = indexTemp;
        }
    }
}
