package mutation;

import java.util.Random;

public class MutationSwap extends Mutation {

    public MutationSwap(float mutProb) {
        super(mutProb);
    }

    /**
     * performs swap mutation PER INDIVIDUAL
     * @param mutProb probability of mutation
     * @return mutated route
     */
    public short[] mutateAccordingToType(float mutProb) {
        if(Math.random() < mutProb) {
            short temp = route[startIndex];
            route[startIndex] = route[stopIndex];
            route[stopIndex] = temp;
        }
        route[route.length - 1] = route[0];
        return route;
    }

//    private double countProbPerIndividual() {
//        double fraction = ((float)1/route.length);
//        double power = Math.pow((1 - mutProb), fraction);
//        return 1 - power;
//    }
}
