public class Mutation_Displacement extends Mutation{

    Mutation_Displacement(float mutProb) {
        super(mutProb);
    }

    public short[] mutateSpecifically() {
        //create left subArray
        short[] leftSubArray = new short[route.length - subArray.length];

        //fill Left subArray
        int i = 0;
        for(int j = 0; j < startIndex; j++) {
            leftSubArray[i++] = route[j];
        }
        for(int j = stopIndex + 1; j < route.length; j++) {
            leftSubArray[i++] = route[j];
        }

        //choose index to insert
        int indexToInsert = random.nextInt(leftSubArray.length - 1);
        startIndex = indexToInsert;
        stopIndex = startIndex + subArray.length;

        //rewrite left subArray up to chosen index
        for(int j = 0; j < indexToInsert; j++) {
            route[j] = leftSubArray[j];
        }

        //rewrite subArray
        for(int j = 0; j < subArray.length; j++) {
            route[startIndex++] = subArray[j];
        }

        //rewrite last part of left subArray
        for(int j = stopIndex; j < route.length; j++) {
            route[j] = leftSubArray[indexToInsert++];
        }
        route[route.length - 1] = route[0];
        return route;
    }

    private void fillSubArray(short[] subArray){
        int j = 0;
        for(int i = startIndex; i <= stopIndex; i++) {
            subArray[j++] = route[i];
        }
    }
}
