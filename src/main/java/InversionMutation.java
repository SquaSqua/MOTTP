public class InversionMutation extends Mutation {

    InversionMutation(float mutProb){
        super(mutProb);
    }

    public short[] mutateSpecifically() {
        fillInReversedOrder(subArray);
        int j = 0;
        for (int i = startIndex; i <= stopIndex; i++, j++) {
            route[i] = subArray[j];
        }
        route[route.length - 1] = route[0];
        return route;
    }

    private void fillInReversedOrder(short[] subArray){
        int j = stopIndex;
        for (int i = 0; i < subArray.length; i++, j--) {
            subArray[i] = route[j];
        }
    }
}
