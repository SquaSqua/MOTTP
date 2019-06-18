package ewasko.individual;

/**
 * represents individual needed to NSGA-II algorithm
 * It is equal to the parent class
 */
public class Individual_NSGAII extends Individual {

    /**
     * constructor for initialization of population. Only number of cities is needed.
     * IMPORTANT! DataFromFile needs to be initialized
     * @param dimension number of cities to visit
     */
    public Individual_NSGAII(int dimension) {
        super(dimension);
    }

    /**
     * constructor for new individual being offspring of another individual
     * @param route order of visited cities with last city equal to the first one
     * @param generation number of generation when this individual was created
     */
    public Individual_NSGAII(short[] route, int generation) {
        super(route, generation);
    }
}
