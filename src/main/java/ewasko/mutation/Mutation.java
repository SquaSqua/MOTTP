package ewasko.mutation;

import ewasko.individual.Individual;

import java.util.Random;

public abstract class Mutation {
    float mutProb;
    Random random = new Random();
    int startIndex, stopIndex;
    short[] route;


    /**
     * main parent method of mutation
     * @param individual to be mutated. Will be modified
     */
    public void mutate(Individual individual){
        mutate(individual, mutProb);
    }

    /**
     * mutation for changing probability e.g. avoiding clones, tests of normal mutation
     * @param individual to be mutated. Will be modified
     * @param mutProb float number, probability of mutation
     * @param seed only for tests - change random method into deterministic one
     */
    public void mutate(Individual individual, float mutProb, long... seed) {
        if(seed.length != 0) {//if test, set the seed
            random.setSeed(seed[0]);
        }
        route = individual.getRoute();
        //choose subarray
        startIndex = random.nextInt(route.length - 1);
        stopIndex = random.nextInt(route.length - 1);
        setInOrder();

        individual.setRoute(mutateAccordingToType(mutProb));
        if(seed.length == 0) {
            individual.setPackingPlanAndFitness();
        }
    }


    protected abstract short[] mutateAccordingToType(float mutProb);

    private void setInOrder(){
        if (startIndex > stopIndex) {
            int indexTemp = startIndex;
            startIndex = stopIndex;
            stopIndex = indexTemp;
        }
    }

    void fillTheRest(int subarrayLength, short[] mutated, int nextIndex) {
        //fill the rest
        int numberOfLeftElements = route.length - subarrayLength;
        for(int indexOfNew = 0; indexOfNew < numberOfLeftElements;) {
            //fill left part
            if(indexOfNew == nextIndex) {
                indexOfNew += subarrayLength;
            }
            for(int indexOfOld = 0; indexOfOld < startIndex; indexOfOld++) {
                mutated[indexOfNew] = route[indexOfOld];
                indexOfNew++;
                if(indexOfNew == nextIndex) {
                    indexOfNew += subarrayLength;
                }
            }
            //fill right part
            for(int indexOfOld = stopIndex; indexOfOld < route.length; indexOfOld++) {
                mutated[indexOfNew] = route[indexOfOld];
                indexOfNew++;
                if(indexOfNew == nextIndex) {
                    indexOfNew += subarrayLength;
                }
            }
        }
    }

    public float getMutProb() {
        return mutProb;
    }

    public void setMutProb(float mutProb) {
        this.mutProb = mutProb;
    }
}
