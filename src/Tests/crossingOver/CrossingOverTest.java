package crossingOver;

import ewasko.crossingOver.CX;
import ewasko.crossingOver.CrossingOver;
import ewasko.crossingOver.OX;
import ewasko.crossingOver.PMX;
import ewasko.individual.Individual_NSGAII;
import org.junit.Test;

import static org.junit.Assert.*;

public class CrossingOverTest {

    private CrossingOver cx = new CX(1);
    private CrossingOver pmx = new PMX(1);
    private CrossingOver ox = new OX(1);
    private short[] route1 = new short[] {0,1,2,3,4,5,6,7,8,9,10,11,12,13,0};
    private short[] route2 = new short[] {4,0,13,5,10,2,6,11,1,8,9,12,3,7,4};
    private Individual_NSGAII parent1 = new Individual_NSGAII(route1, 1);
    private Individual_NSGAII parent2 = new Individual_NSGAII(route2, 1);
    private long seed = 96426558L;
    private short[] cxChild1 = new short[] {0,1,13,5,4,2,6,11,8,9,10,12,3,7,0};
    private short[] cxChild2 = new short[] {4,0,2,3,10,5,6,7,1,8,9,11,12,13,4};

    private short[] pmxChild1 = new short[] {0,9,13,5,10,2,6,11,1,8,4,7,12,3,0};
    private short[] pmxChild2 = new short[] {10,0,2,3,4,5,6,7,8,9,1,12,13,11,10};

    private short[] oxChild1 = new short[] {11,1,2,3,4,5,6,7,8,9,10,12,0,13,11};
    private short[] oxChild2 = new short[] {4,7,13,5,10,2,6,11,1,8,9,12,0,3,4};

    @Test
    public void crossOverCX() {
        assertArrayEquals(
                new Individual_NSGAII[] {
                        new Individual_NSGAII(cxChild1, 1), new Individual_NSGAII(cxChild2, 1)
                },
                cx.crossOver(parent1, parent2, 1));
    }

    @Test
    public void crossOverPMX() {
        assertArrayEquals(
                new Individual_NSGAII[] {
                        new Individual_NSGAII(pmxChild1, 1), new Individual_NSGAII(pmxChild2, 1)
                },
                pmx.crossOver(parent1, parent2, 1, seed));
    }

    @Test
    public void crossOverOX() {
        assertArrayEquals(
                    new Individual_NSGAII[] {
                        new Individual_NSGAII(oxChild1, 1), new Individual_NSGAII(oxChild2, 1)
                },
                ox.crossOver(parent1, parent2, 1, seed));
    }
}