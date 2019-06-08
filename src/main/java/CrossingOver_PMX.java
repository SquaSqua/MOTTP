import java.util.Random;

public class CrossingOver_PMX extends CrossingOver{

    CrossingOver_PMX(float crossProb) {
        super(crossProb);
    }

    protected short[][] crossOverSpecifically(short[] route1, short[] route2) {

        Random random = new Random();
        indexOfSplit1 = random.nextInt(route1.length);
        indexOfSplit2 = random.nextInt(route1.length);
        setInOrder();

        for(int i = indexOfSplit1; i < indexOfSplit2 + 1; i++) {
            short temp = route1[i];
            route1[i] = route2[i];
            route2[i] = temp;
        }
    }
}
