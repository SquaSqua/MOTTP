package mutation;

public class MutationInversion extends Mutation {

    public MutationInversion(float mutProb){
        super(mutProb);
    }

    /**
     * performs mutation with inversion PER INDIVIDUAL
     * @param mutProb probability of mutation
     * @return mutated route
     */
    public short[] mutateAccordingToType(float mutProb) {
        short[] mutated;
        int subarrayLength = stopIndex - startIndex;
        if(Math.random() < mutProb) {
            mutated = new short[route.length];
            int nextIndex = random.nextInt(route.length - subarrayLength);
            fillInReversedOrder(mutated, nextIndex, subarrayLength);
            fillTheRest(subarrayLength, mutated, nextIndex);
            mutated[route.length - 1] = route[0];
            route = mutated;
        }
        return route;
    }

    private void fillInReversedOrder(short[] subArray, int nextIndex, int subarrayLength){
        int j = stopIndex - 1;
        for (int i = nextIndex; i < nextIndex + subarrayLength; i++, j--) {
            subArray[i] = route[j];
        }
    }
}
