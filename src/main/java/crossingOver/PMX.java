package crossingOver;

import java.util.ArrayList;
import java.util.Random;

public class PMX extends CrossingOver {
    private static final int FirstElement = 0;
    private static final int SecondElement = 1;
    private short[] route1;
    private short[] route2;
    private short[] child1;
    private short[] child2;
    private int indexOfSplit1, indexOfSplit2;

    public PMX(float crossProb) {
        super(crossProb);
    }

    /**
     * crossover routes with Partially Mapped Crossing
     * @param route1 route of first parent without last city (doubled with first one)
     * @param route2 route of first parent without last city (doubled with first one)
     * @return crossed routes without doubled first city
     */
    protected short[][] crossOverAccordingToType(short[] route1, short[] route2, long... seed) {
        this.route1 = route1;
        this.route2 = route2;

        Random random = new Random();
        if(seed.length != 0) {
            random.setSeed(seed[0]);
        }
        indexOfSplit1 = random.nextInt(route1.length);
        indexOfSplit2 = random.nextInt(route2.length);//element of this index is one after rewritten
        setInOrder();

        child1 = new short[route1.length];
        child2 = new short[route1.length];

        //find all cycles
        ArrayList<short[]> cycles = new ArrayList<>();
        for(int i = indexOfSplit1; i < indexOfSplit2; i++) {
            short[] cycle = new short[2];
            cycle[FirstElement] = route1[i];
            cycle[SecondElement] = route2[i];
            addNewCycle(cycles, cycle);
        }
        fixCycles(cycles);

        fillSubarray();

        fillWithTheMap(cycles);

        fillTheRest();

        return new short[][] {child1, child2};
    }

    private void addNewCycle(ArrayList<short[]> cycles, short[] cycle) {
        boolean connected = false;
        for(short[] currentCycle : cycles) {
            if(currentCycle[SecondElement] == cycle[FirstElement]) {
                currentCycle[SecondElement] = cycle[SecondElement];
                connected = true;
                break;
            }
            else if(cycle[SecondElement] == currentCycle[FirstElement]) {
                currentCycle[FirstElement] = cycle[FirstElement];
                connected = true;
                break;
            }
        }
        if(!connected) {
            cycles.add(cycle);
        }
    }

    private void fixCycles(ArrayList<short[]> cycles) {
        boolean isAnyFixed;
        do{
            isAnyFixed = false;
            ArrayList<short[]> elementsToRemove;
            for(int i = 0; i < cycles.size(); i++) {
                elementsToRemove = new ArrayList<>();
                short[] firstCycle = cycles.get(i);
                if(firstCycle[FirstElement] == firstCycle[SecondElement]) {
                    continue;
                }
                for (short[] secondCycle : cycles) {
                    if (firstCycle[FirstElement] == secondCycle[SecondElement]) {
                        secondCycle[SecondElement] = firstCycle[SecondElement];
                        cycles.get(i)[FirstElement] = -1;
                        cycles.get(i)[SecondElement] = -1;
                        elementsToRemove.add(cycles.get(i));
                        isAnyFixed = true;
                        break;
                    }
                }
                i -= elementsToRemove.size();
                cycles.removeAll(elementsToRemove);
            }
        }while (isAnyFixed);
    }

    /**
     * copies subarray of given parents switching first array with second
     */
    private void fillSubarray() {
        for(int i = indexOfSplit1; i < indexOfSplit2; i++) {
            child1[i] = route2[i];
            child2[i] = route1[i];
        }
    }

    private void fillWithTheMap(ArrayList<short[]> cycles) {
        fillHalfPart(cycles, 0, indexOfSplit1);
        fillHalfPart(cycles, indexOfSplit2, route1.length);
    }

    private void fillHalfPart(ArrayList<short[]> cycles, int from, int to) {
        for(int i = from; i < to; i++) {
            int valueToChangeParent1 = route1[i];
            int valueToChangeParent2 = route2[i];

            for(short[] cycle : cycles) {
                if(cycle[FirstElement] == valueToChangeParent1) {
                    child1[i] = cycle[SecondElement];
                }
                else if(cycle[SecondElement] == valueToChangeParent1) {
                    child1[i] = cycle[FirstElement];
                }
                if(cycle[SecondElement] == valueToChangeParent2) {
                    child2[i] = cycle[FirstElement];
                }
                else if(cycle[FirstElement] == valueToChangeParent2) {
                    child2[i] = cycle[SecondElement];
                }
            }
        }
    }

    private void fillTheRest() {
        for(int i = 0; i < child1.length; i++) {
            if(child1[i] == 0) {
                child1[i] = route1[i];
            }
            if(child2[i] == 0) {
                child2[i] = route2[i];
            }
        }
    }
}
