package individual;

import generator.PackingPlanGenerator;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

/**
 * abstract class for all (two) types of implemented metaheuristics for Travelling Thief Problem
 * It has all needed fields to represent individual, but also fields for grouping results in fronts (rank) or crowding distance
 */
public abstract class Individual {

    public short[] route;
    private boolean[] packingPlan;
    private double fitnessTime;
    private int fitnessWage;
    private int birthday;

    //fields accessed only through methods in ParetoFrontGenerator
    private double crowdingDistance;
    private int rank;

    /**
     * constructor for new individual being offspring of another individual
     * @param route order of visited cities with last city equal to the first one
     * @param birthday number of generation when this individual was created
     */
    Individual(short[] route, int birthday) {
        this.route = route;
        packingPlan = null;
        this.birthday = birthday;
    }

    /**
     * constructor for initialization of population. Only number of cities is needed.
     * IMPORTANT! DataFromFile needs to be initialized
     * @param dimension number of cities to visit
     */
    Individual(int dimension) {
        short[] route = new short[dimension + 1];
        ArrayList<Integer> routeList = new ArrayList<>();
        for (int i = 0; i < dimension; i++) {
            routeList.add(i);
        }
        Collections.shuffle(routeList);
        for (int i = 0; i < dimension; i++) {
            route[i] = routeList.get(i).shortValue();
        }
        route[dimension] = route[0];
        this.route = route;
        this.birthday = 0;
    }

    /**
     * compares two individual in pareto-optimal sense
     * @param o individual
     * @return 1 if "this" individual is better, -1 if given is better, 0 if they are equal
     */
    public int compareTo(Individual o) {
        return (int) Math.signum((Math.signum(fitnessTime - o.fitnessTime) * -1)+ Math.signum((fitnessWage - o.fitnessWage)));
    }

    //getters
    public short[] getRoute() {
        return route;
    }
    public boolean[] getPackingPlan() {
        return packingPlan;
    }
    public double getFitnessTime() {
        return fitnessTime;
    }
    public int getFitnessWage() {
        return fitnessWage;
    }
    public int getBirthday() { return birthday; }
    public double getCrowdingDistance() {
        return crowdingDistance;
    }
    public int getRank() {
        return rank;
    }

    //setters
    public void setRoute(short[] route) {
        this.route = route;
    }
    public void setPackingPlan(boolean[] packingPlan) {
        this.packingPlan = packingPlan;
    }
    public void setPackingPlanAndFitness() {
        PackingPlanGenerator.settlePackingPlan(this);
        PackingPlanGenerator.setFitnessForIndividual(this);
    }
    public void setFitnessTime(double fitnessTime) {
        this.fitnessTime = fitnessTime;
    }
    public void setFitnessWage(int fitnessWage) {
        this.fitnessWage = fitnessWage;
    }
    public void setCrowdingDistance(double crowdingDistance) {
        this.crowdingDistance = crowdingDistance;
    }
    public void setRank(int rank) {
        this.rank = rank;
    }

    /** checks if genotypes (routes) are are equal
     * @param individual individual to compare to
     * @return if routes are equal
     */
    public boolean equals(Object individual) {
        if(individual instanceof Individual_NSGAII) {
            Individual_NSGAII ind = (Individual_NSGAII) individual;
            return Arrays.equals(this.route, ind.getRoute());
        }
        return false;
    }
}