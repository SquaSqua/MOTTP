import java.util.Random;

public class Mutation_Swap extends Mutation {

    Mutation_Swap(float mutProb) {
        super(mutProb);
    }

    public short[] mutateSpecifically() {
        for(int i = 0; i < route.length - 1; i++) {
            if(Math.random() < mutProb) {
                int swapIndex = new Random().nextInt(route.length - 1);
                short temp = route[i];
                route[i] = route[swapIndex];
                route[swapIndex] = temp;
            }
        }
        route[route.length - 1] = route[0];
        return route;
    }
}
