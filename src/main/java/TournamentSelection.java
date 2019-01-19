import java.util.ArrayList;
import java.util.Random;

public class TournamentSelection extends Selection {
    int tournamentSize;

    TournamentSelection(int tournamentSize) {
        this.tournamentSize = tournamentSize;
    }

    @Override
    Individual_NSGA_II select(ArrayList<Individual> population) {
        Individual_NSGA_II bestIndividual = (Individual_NSGA_II)population.get(0);//just any individual to initialize
        int bestRank = Integer.MAX_VALUE;
        Random rand = new Random();
        for (int i = 0; i < tournamentSize; i++) {
            Individual_NSGA_II individual = (Individual_NSGA_II)population.get(rand.nextInt(population.size()));
            int rank = individual.getRank();
            if (rank < bestRank) {
                bestRank = rank;
                bestIndividual = individual;
            } else if (rank == bestRank) {
                if (bestIndividual.getCrowdingDistance() < individual.getCrowdingDistance()) {
                    bestIndividual = individual;
                }
            }
        }
        return bestIndividual;
    }
}
