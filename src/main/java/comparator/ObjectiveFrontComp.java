package comparator;

import individual.Individual;

import java.util.Comparator;

/**
 * preliminarily sort front by one objective to easily set their crowding distances
 */
public class ObjectiveFrontComp implements Comparator<Individual> {

    @Override
    public int compare(Individual o1, Individual o2) {
        return Double.compare(o1.getFitnessWage(), o2.getFitnessWage());
    }
}