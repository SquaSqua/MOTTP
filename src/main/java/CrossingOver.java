import java.util.Arrays;

public abstract class CrossingOver {
    private float crossProb;

    CrossingOver(float crossProb) {
        this.crossProb = crossProb;
    }

    Individual_NSGA_II[] crossOver(Individual_NSGA_II parent1, Individual_NSGA_II parent2, int generation) {
        return crossOver(parent1, parent2, crossProb, generation);
    }

    Individual_NSGA_II[] crossOver(Individual_NSGA_II parent1, Individual_NSGA_II parent2, float crossProb, int generation) {
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
        }
        return new Individual_NSGA_II[]{
                new Individual_NSGA_II(ch1, generation),
                new Individual_NSGA_II(ch2, generation)
        };
    }

    protected abstract short[][] crossOverSpecifically(short[] p1Route, short[] p2Route);


    private short[] addLastCity(short[] child) {
        short[] ch = new short[child.length + 1];
        System.arraycopy(child, 0, ch, 0, child.length);
        ch[ch.length - 1] = ch[0];
        return ch;
    }

    protected short findFirstEmpty(short[] route) {
        short firstEmpty = -1;
        for (int i = 0; i < route.length; i++) {
            if (route[i] == -1) {
                firstEmpty = (short)i;
                break;
            }
        }
        return firstEmpty;
    }

    protected int findIndexOfaValue(int value, short[] route) {
        int index = -1;
        for (int i = 0; i < route.length; i++) {
            if (route[i] == value) {
                index = i;
                break;
            }
        }
        return index;
    }

    protected void fillWithInitialValues(short[] array) {
        for (int i = 0; i < array.length; i++) {
            array[i] = -1;
        }
    }

    protected short[] copyParentRoute(short[] pRoute) {
        short[] route1 = new short[pRoute.length];
        for (int i = 0; i < route1.length; i++) {
            route1[i] = pRoute[i];
        }
        return route1;
    }
}
