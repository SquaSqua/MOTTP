package ewasko.mutation;

public class MutationDisplacement extends Mutation{

    public MutationDisplacement(float mutProb) {
        super(mutProb);
    }

    /**
     * performs mutation with displacement PER INDIVIDUAL
     * @param mutProb probability of mutation
     * @return mutated route
     */
    public short[] mutateAccordingToType(float mutProb) {
        if(Math.random() < mutProb) {
            short[] mutated = new short[route.length];
            mutated = fillWithInitialValues(mutated.length);
            int subarrayLength = stopIndex - startIndex;

            //choose new index for subarray. For tests it's 2
            int nextIndex = random.nextInt(route.length - subarrayLength);

            //place subarray in a new place
            int indexOfNew = nextIndex;
            for(int indexOfOld = startIndex; indexOfOld < stopIndex; indexOfOld++, indexOfNew++) {
                    mutated[indexOfNew] = route[indexOfOld];
            }

            fillTheRest(subarrayLength, mutated, nextIndex);

            mutated[route.length - 1] = route[0];
            route = mutated;
        }
        return route;
    }

    private short[] fillWithInitialValues(int length) {
        short[] result = new short[length];
        for(int i = 0; i < length; i++) {
            result[i] = -1;
        }
        return result;
    }
}
