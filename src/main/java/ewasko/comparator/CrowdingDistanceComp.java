package ewasko.comparator;

import ewasko.individual.Individual;

import java.util.Comparator;

/**
 * compares two individuals by their crowding distance
*/
public class CrowdingDistanceComp implements Comparator<Individual> {
    @Override
    public int compare(Individual o1, Individual o2) {
        return Double.compare(o1.getCrowdingDistance(), o2.getCrowdingDistance());
    }
}
