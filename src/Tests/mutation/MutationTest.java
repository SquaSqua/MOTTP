package mutation;

import ewasko.mutation.Mutation;
import ewasko.mutation.MutationDisplacement;
import ewasko.mutation.MutationInversion;
import ewasko.mutation.MutationSwap;
import ewasko.individual.Individual_NSGAII;
import org.junit.Test;

import static org.junit.Assert.*;

public class MutationTest {
    private Mutation md = new MutationDisplacement();
    private Mutation mi = new MutationInversion();
    private Mutation ms = new MutationSwap();
    private short[] route1 = new short[] {0,1,2,3,4,5,6,7,8,9,10,11,12,13,0};
    private Individual_NSGAII ind1 = new Individual_NSGAII(route1, 1);
    private long seed = 941426258L;
    private short[] mdChild1 = new short[] {0,1,2,3,4,8,9,10,11,5,6,7,12,13,0};
    private short[] miChild1 = new short[] {0,1,2,3,4,11,10,9,8,5,6,7,12,13,0};
    private short[] msChild1 = new short[] {0,1,2,3,4,5,6,7,12,9,10,11,8,13,0};


    @Test
    public void mutateDisplacement() {
        md.setMutProb(1f);
        md.mutate(ind1, 1, seed);
        assertArrayEquals(mdChild1, ind1.route);
    }

    @Test
    public void mutateInversion() {
        mi.setMutProb(1f);
        mi.mutate(ind1, 1, seed);
        assertArrayEquals(miChild1, ind1.route);
    }

    @Test
    public void mutateSwap() {
        ms.setMutProb(1f);
        ms.mutate(ind1, 1, seed);
        assertArrayEquals(msChild1, ind1.route);
    }
}