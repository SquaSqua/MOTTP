import java.util.Random;

public class OXCrossingOver extends CrossingOver {
    int indexOfSplit1;
    int indexOfSplit2;

    OXCrossingOver(float crossProb) {
        super(crossProb);
    }

    protected short[][] crossOverSpecifically(short[] route1, short[] route2) {
        short[] child1 = new short[route1.length];
        short[] child2 = new short[route1.length];
        fillWithInitialValues(child1);
        fillWithInitialValues(child2);
        Random random = new Random();
        indexOfSplit1 = random.nextInt(route1.length);
        indexOfSplit2 = random.nextInt(route1.length);
        setInOrder();
        //rewrite subArrays
        for(int i = indexOfSplit1; i <= indexOfSplit2; i++){
            child1[i] = route1[i];
            child2[i] = route2[i];
        }

        fillLastPartOfChild(indexOfSplit2 + 1, child1, route2);
        fillLastPartOfChild(indexOfSplit2 + 1, child2, route1);

        fillFirstPartOfChild(indexOfSplit1, child1, route2);
        fillFirstPartOfChild(indexOfSplit1, child2, route1);

        return new short[][] {child1, child2};
    }

    private void fillLastPartOfChild(int startIndex, short[] child1, short[] parent2) {
        for(int i = startIndex; i < child1.length; i++) {
            for(int j = startIndex; j < parent2.length; j++) {
                if(findIndexOfaValue(parent2[j], child1) == -1) {
                    child1[i] = parent2[j];
                    break;
                }
                if(j == parent2.length - 1 && i < child1.length - 1) {
                    j = -1;
                }
            }
        }
    }

    private void fillFirstPartOfChild(int endIndex, short[] child1, short[] parent2) {
        for(int i = 0; i < endIndex; i++) {//tu szukać błędu jak coś
            for(int j = 0; j < parent2.length; j++) {
                if(findIndexOfaValue(parent2[j], child1) == -1) {
                    child1[i] = parent2[j];
                    break;
                }
            }
        }
    }

    private void setInOrder(){
        if (indexOfSplit1 > indexOfSplit2) {
            int indexTemp = indexOfSplit1;
            indexOfSplit1 = indexOfSplit2;
            indexOfSplit2 = indexTemp;
        }
    }
}
