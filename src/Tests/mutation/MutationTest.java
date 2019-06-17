package mutation;

import individual.Individual_NSGAII;
import org.junit.Test;

import static org.junit.Assert.*;

public class MutationTest {
    private Mutation md = new MutationDisplacement(1);
    private Mutation mi = new MutationInversion(1);
    private Mutation ms = new MutationSwap(1);
    private short[] route1 = new short[] {0,1,2,3,4,5,6,7,8,9,10,11,12,13,0};
    private Individual_NSGAII ind1 = new Individual_NSGAII(route1, 1);
    private long seed = 941426258L;
    private short[] mdChild1 = new short[] {0,1,8,9,10,11,2,3,4,5,6,7,12,13,0};
    private short[] miChild1 = new short[] {0,1,11,10,9,8,2,3,4,5,6,7,12,13,0};
    private short[] msChild1 = new short[] {0,1,2,3,4,5,6,7,12,9,10,11,8,13,0};


    @Test
    public void mutateDisplacement() {
        md.mutate(ind1, 1, seed);
        assertArrayEquals(mdChild1, ind1.route);
    }

    @Test
    public void mutateInversion() {
        mi.mutate(ind1, 1, seed);
        assertArrayEquals(miChild1, ind1.route);
    }

    @Test
    public void mutateSwap() {
        ms.mutate(ind1, 1, seed);
        assertArrayEquals(msChild1, ind1.route);
    }
}