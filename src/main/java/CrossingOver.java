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
        p1Route = parent1.getRoute();
        p2Route = parent2.getRoute();
        ch1 = new short[p1Route.length];
        ch2 = new short[p1Route.length];
        if(Math.random() < crossProb){
            short[][] children = crossOverSpecifically(p1Route, p2Route);
            ch1 = children[0];
            ch2 = children[1];
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
}
