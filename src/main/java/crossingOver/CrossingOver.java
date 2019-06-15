package crossingOver;

import individual.Individual_NSGAII;

import java.util.Arrays;

public abstract class CrossingOver {
    private float crossProb;
    int indexOfSplit1;
    int indexOfSplit2;

    CrossingOver(float crossProb) {
        this.crossProb = crossProb;
    }

    /**
     * parent class of various kinds of crossover
     * @param parent1 First of two individuals to be crossed. Won't be modified
     * @param parent2 Second of two individuals to be crossed. Won't be modified
     * @param generation indicates number of population
     * @return array of two new individuals which are offspring of given individuals
     */
    public Individual_NSGAII[] crossOver(Individual_NSGAII parent1, Individual_NSGAII parent2, int generation) {
        return crossOver(parent1, parent2, crossProb, generation);
    }

    /**
     * inside method which shells route from individual, cuts last city and calls specific method of chosen
     * crossing, then adds last city
     */
    private Individual_NSGAII[] crossOver(Individual_NSGAII parent1, Individual_NSGAII parent2, float crossProb, int generation) {
        short[] p1Route, p2Route, ch1, ch2;
        p1Route = Arrays.copyOf(parent1.getRoute(), parent1.getRoute().length - 1);
        p2Route = Arrays.copyOf(parent2.getRoute(), parent1.getRoute().length - 1);
        ch1 = new short[p1Route.length];
        ch2 = new short[p1Route.length];
        if(Math.random() < crossProb){
            short[] route1 = copyParentRoute(p1Route);
            short[] route2 = copyParentRoute(p2Route);
            short[][] children = crossOverSpecifically(route1, route2);
            ch1 = addLastCity(children[0]);
            ch2 = addLastCity(children[1]);
        } else {
            for (int i = 0; i < ch1.length; i++) {
                ch1[i] = p1Route[i];
                ch2[i] = p2Route[i];
            }
            ch1 = addLastCity(ch1);
            ch2 = addLastCity(ch2);
        }
        return new Individual_NSGAII[]{
                new Individual_NSGAII(ch1, generation),
                new Individual_NSGAII(ch2, generation)
        };
    }

    protected abstract short[][] crossOverSpecifically(short[] route1, short[] route2);


    private short[] addLastCity(short[] child) {
        short[] ch = new short[child.length + 1];
        System.arraycopy(child, 0, ch, 0, child.length);
        ch[ch.length - 1] = ch[0];
        return ch;
    }

    short findFirstEmpty(short[] route) {
        short firstEmpty = -1;
        for (int i = 0; i < route.length; i++) {
            if (route[i] == -1) {
                firstEmpty = (short)i;
                break;
            }
        }
        return firstEmpty;
    }

    int findIndexOfaValue(int value, short[] route) {
        int index = -1;
        for (int i = 0; i < route.length; i++) {
            if (route[i] == value) {
                index = i;
                break;
            }
        }
        return index;
    }

    void fillWithInitialValues(short[] array) {
        for (int i = 0; i < array.length; i++) {
            array[i] = -1;
        }
    }

    private short[] copyParentRoute(short[] pRoute) {
        short[] route1 = new short[pRoute.length];
        System.arraycopy(pRoute, 0, route1, 0, route1.length);
        return route1;
    }

    void setInOrder(){
        if (indexOfSplit1 > indexOfSplit2) {
            int indexTemp = indexOfSplit1;
            indexOfSplit1 = indexOfSplit2;
            indexOfSplit2 = indexTemp;
        }
    }
}
