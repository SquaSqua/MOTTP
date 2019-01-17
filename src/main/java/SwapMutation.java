import java.util.Random;

public class SwapMutation extends Mutation {

    SwapMutation(float mutProb) {
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
