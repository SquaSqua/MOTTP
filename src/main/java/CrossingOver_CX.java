public class CrossingOver_CX extends CrossingOver{

    /**
     *
     * @param crossProb given probability of crossing
     */
    CrossingOver_CX(float crossProb) {
        super(crossProb);
    }

    protected short[][] crossOverSpecifically(short[] route1, short[] route2) {
            short[] child1 = new short[route1.length];
            short[] child2 = new short[route2.length];
            fillWithInitialValues(child1);
            fillWithInitialValues(child2);

            int beginningValue = route1[0];
            int currentInd = 0;

            boolean isSwapTurn = false;
            while (true) {
                assignGens(isSwapTurn, currentInd, route1, route2, child1, child2);
                if (route1[currentInd] == route2[currentInd]) {
                    isSwapTurn = !isSwapTurn;
                }
                currentInd = findIndexOfaValue(route2[currentInd], route1);
                if (route2[currentInd] == beginningValue) {
                    assignGens(isSwapTurn, currentInd, route1, route2, child1, child2);
                    currentInd = findFirstEmpty(child1);
                    if (currentInd == -1) {
                        break;
                    }
                    beginningValue = route1[currentInd];
                    isSwapTurn = !isSwapTurn;
                }
            }
            return new short[][]{child1, child2};
    }
    private void assignGens(boolean isSwapTurn, int currentInd, short[] route1, short[] route2, short[] child1, short[] child2) {
        if (!isSwapTurn) {
            child1[currentInd] = route1[currentInd];
            child2[currentInd] = route2[currentInd];
        } else {
            child1[currentInd] = route2[currentInd];
            child2[currentInd] = route1[currentInd];
        }
    }
}
