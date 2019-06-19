package ewasko.crossingOver;

import java.util.Random;

public class OX extends CrossingOver {


    /**
     * crossover routes with Ordered Crossing
     * @param route1 route of first parent without last city (doubled with first one)
     * @param route2 route of first parent without last city (doubled with first one)
     * @return crossed routes without doubled first city
     */
    protected short[][] crossOverAccordingToType(short[] route1, short[] route2, long... seed) {
        short[] child1 = new short[route1.length];
        short[] child2 = new short[route1.length];
        fillWithInitialValues(child1);
        fillWithInitialValues(child2);
        Random random = new Random();
        if(seed != null) {
            random.setSeed(seed[0]);
        }
        indexOfSplit1 = random.nextInt(route1.length);
        indexOfSplit2 = random.nextInt(route1.length);
        setInOrder();
        //rewrite subArrays
        for(int i = indexOfSplit1; i <= indexOfSplit2; i++){
            child1[i] = route1[i];
            child2[i] = route2[i];
        }
        //create subarrays for order of elements
        short[] orderOfChild1 = new short[route1.length - (indexOfSplit2 - indexOfSplit1 + 1)];
        short[] orderOfChild2 = new short[route1.length - (indexOfSplit2 - indexOfSplit1 + 1)];

        //fill subarrays with specific order
        orderOfChild1 = fillOrderSubarray(child1, route2, orderOfChild1.length);
        orderOfChild2 = fillOrderSubarray(child2, route1, orderOfChild2.length);

        //fill right part of a child
        fillRightPart(child1, orderOfChild1);
        fillRightPart(child2, orderOfChild2);

        //fill left part of a child
        fillLeftPart(child1, orderOfChild1);
        fillLeftPart(child2, orderOfChild2);

        return new short[][] {child1, child2};
    }

    private void fillLeftPart(short[] child1, short[] orderOfChild1) {
        for(int i = 0; i < indexOfSplit1; ) {
            for(int j = child1.length - indexOfSplit2 - 1; j < orderOfChild1.length; j++) {
                child1[i++] = orderOfChild1[j];
            }
        }
    }

    private void fillRightPart(short[] child1, short[] orderOfChild1) {
        for(int i = indexOfSplit2 + 1; i < child1.length; ) {
            for(int j = 0; j < child1.length - indexOfSplit2 - 1; j++) {
                child1[i++] = orderOfChild1[j];
            }
        }
    }

    private short[] fillOrderSubarray(short[] child1, short[] route2, int orderLength) {
        short[] order = new short[orderLength];
        for(int i = 0; i < orderLength; ) {
            for(int j = indexOfSplit2 + 1; j < route2.length; j++){
                if(findIndexOfaValue(route2[j], child1) == -1) {
                    order[i++] = route2[j];
                }
            }
            for(int j = 0; j <= indexOfSplit2; j++) {
                if(findIndexOfaValue(route2[j], child1) == -1) {
                    order[i++] = route2[j];
                }
            }
        }
        return order;
    }
}
