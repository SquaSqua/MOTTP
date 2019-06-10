import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.time.LocalDate;
import java.time.LocalTime;

public class Runner {
    public static void main(String[] args) {
        long start = System.currentTimeMillis();
        String definitionFile = "src/main/resources/definitionFiles/hard_1.ttp";
        DataProvider configProvider = new DataProvider();
        configProvider.readFile(definitionFile);

        Evolution populationWithoutClones = new Evolution(
                new Configuration(100, 100, 0.6f, true,
                        new Mutation_Swap(0.5f), new CrossingOver_CX(0.02f))
        );
        Evolution population = new Evolution(
                new Configuration(100, 100, 0.6f,false,
                        new Mutation_Swap(0.5f), new CrossingOver_CX(0.02f))
        );
        Multiobjective_Tabu_Search mots = new Multiobjective_Tabu_Search(
                4, 250, 25, 1000);
        searchForPareto(populationWithoutClones);
        searchForPareto(population);
        searchForPareto(mots);
        countTimeUpHere(start);

        /**
         * Niezobowiązujące testy jednostkowe XD
         */
//        Individual_NSGA_II ind1 = new Individual_NSGA_II(new short[] {1,2,3,4,5,6,7,8,9,1}, 1);
//        Individual_NSGA_II ind2 = new Individual_NSGA_II(new short[] {9,8,7,6,5,4,3,2,1,9}, 1);
//        CrossingOver cx = new CrossingOver_CX(1);
//        Mutation_Swap mut = new Mutation_Swap(1);
//        Individual_NSGA_II[] children = cx.crossOver(ind1, ind2, 1);
//        System.out.println(Arrays.toString(children[0].getRoute()));
//        System.out.println(Arrays.toString(children[1].getRoute()));
//        mut.mutate(ind1);
//        mut.mutate(ind2);
//        System.out.println(Arrays.toString(ind1.getRoute()));
//        System.out.println(Arrays.toString(ind2.getRoute()));
    }

    private static void searchForPareto(IMetaheuristics algorithm) {
        try {
            PrintWriter out = new PrintWriter(giveName(algorithm));
            out.println(algorithm.run());
            out.close();
        } catch (FileNotFoundException ex) {
            System.out.println("Nie da się utworzyć pliku!");
        }
    }

    private static String giveName(IMetaheuristics algorithm) {
        LocalDate date = LocalDate.now();
        LocalTime time = LocalTime.now();
        String dateAndTime = date.getMonth() + "_" + date.getDayOfMonth()
                + "__" + time.getHour() + "_" + time.getMinute() + "_" + time.getSecond();
        return dateAndTime + "_" + algorithm.getClass().getName() + ".csv";
    }

    private static void countTimeUpHere(long start) {
        long milliseconds = System.currentTimeMillis() - start;
            System.out.println((int)Math.floor((float)milliseconds / 60000) + " min "
                    + (milliseconds % 60000) / 1000 + "." + milliseconds % 1000 + "s");
    }
}