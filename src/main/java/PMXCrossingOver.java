import java.util.Random;

public class PMXCrossingOver extends CrossingOver{

    PMXCrossingOver(float crossProb) {
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
        // na tym etapie mamy w routach zamienione podstringi np. {1,2,3,6,5,4,3,8,9} i {9,8,7,4,5,6,7,2,1}

        return new short[0][];
    }
}
