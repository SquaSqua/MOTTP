package mutation;

import java.util.Random;

public class MutationSwap extends Mutation {

    public MutationSwap(float mutProb) {
        super(mutProb);
    }

    public short[] mutateSpecifically(float mutProb) {
        Random random = new Random();
        float probPerGen = mutProb == 1 ? 1 : (float)countProbPerIndividual();
        for(int i = 0; i < route.length - 1; i++) {
            if(random.nextFloat() < probPerGen/*mutProb*/) {
                int swapIndex = new Random().nextInt(route.length - 1);
                short temp = route[i];
                route[i] = route[swapIndex];
                route[swapIndex] = temp;
            }
        }
        route[route.length - 1] = route[0];
        return route;
    }

    private double countProbPerIndividual() {

        double fraction = ((float)1/route.length);
        double power = Math.pow((1 - mutProb), fraction);
        return 1 - power;
    }
}
