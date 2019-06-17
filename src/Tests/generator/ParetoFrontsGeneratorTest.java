package generator;

import individual.Individual;
import individual.Individual_NSGAII;
import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.*;

public class ParetoFrontsGeneratorTest {
    private ArrayList<Individual> population = new ArrayList<>();

    @Test
    public void generateFrontsWithAssignments() {
        fillPopulation();
        ArrayList<ArrayList<Individual>> fronts = ParetoFrontsGenerator.generateFrontsWithAssignments(population);
        assertEquals(1, fronts.get(3).size());
        assertEquals(5, fronts.get(0).size());
        assertEquals(4, fronts.get(1).size());
        assertEquals(4, fronts.get(2).size());
    }

    private void fillPopulation() {
        Individual_NSGAII indiv;
        indiv = new Individual_NSGAII(1);
        indiv.setFitnessWage(6);
        indiv.setFitnessTime(1);
        population.add(indiv);
        indiv = new Individual_NSGAII(1);
        indiv.setFitnessWage(9);
        indiv.setFitnessTime(2);
        population.add(indiv);
        indiv = new Individual_NSGAII(1);
        indiv.setFitnessWage(13);
        indiv.setFitnessTime(3);
        population.add(indiv);
        indiv = new Individual_NSGAII(1);
        indiv.setFitnessWage(14);
        indiv.setFitnessTime(5);
        population.add(indiv);
        indiv = new Individual_NSGAII(1);
        indiv.setFitnessWage(15);
        indiv.setFitnessTime(7);
        population.add(indiv);
        indiv = new Individual_NSGAII(1);
        indiv.setFitnessWage(5);
        indiv.setFitnessTime(1);
        population.add(indiv);
        indiv = new Individual_NSGAII(1);
        indiv.setFitnessWage(10);
        indiv.setFitnessTime(1);
        population.add(indiv);
        indiv = new Individual_NSGAII(1);
        indiv.setFitnessWage(13);
        indiv.setFitnessTime(2);
        population.add(indiv);
        indiv = new Individual_NSGAII(1);
        indiv.setFitnessWage(10);
        indiv.setFitnessTime(4);
        population.add(indiv);
        indiv = new Individual_NSGAII(1);
        indiv.setFitnessWage(7);
        indiv.setFitnessTime(4);
        population.add(indiv);
        indiv = new Individual_NSGAII(1);
        indiv.setFitnessWage(0);
        indiv.setFitnessTime(0);
        population.add(indiv);
        indiv = new Individual_NSGAII(1);
        indiv.setFitnessWage(14);
        indiv.setFitnessTime(7);
        population.add(indiv);
        indiv = new Individual_NSGAII(1);
        indiv.setFitnessWage(12);
        indiv.setFitnessTime(6);
        population.add(indiv);
        indiv = new Individual_NSGAII(1);
        indiv.setFitnessWage(7);
        indiv.setFitnessTime(3);
        population.add(indiv);
    }
}