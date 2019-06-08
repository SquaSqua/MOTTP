import java.util.Comparator;

/**
 * compares two individuals by their crowding distance
 */
public class Comparator_CrowdingDistance implements Comparator<Individual> {
    @Override
    public int compare(Individual o1, Individual o2) {
        return Double.compare(o1.getCrowdingDistance(), o2.getCrowdingDistance());
    }
}
