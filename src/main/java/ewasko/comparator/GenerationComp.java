package ewasko.comparator;

import ewasko.individual.Individual;

import java.util.Comparator;

/**
 * compares two individuals by their number of generation (birth)
 */
public class GenerationComp implements Comparator<Individual> {

    @Override
    public int compare(Individual o1, Individual o2) {
        return Integer.compare(o1.getBirthday(), o2.getBirthday());
    }
}
