package comparator;

import individual.Individual;

import java.util.Comparator;

public class GenerationComp implements Comparator<Individual> {

    @Override
    public int compare(Individual o1, Individual o2) {
        return Integer.compare(o1.getBirthday(), o2.getBirthday());
    }
}
