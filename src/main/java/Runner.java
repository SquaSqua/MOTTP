import crossingOver.PMX;
import infoToRun.Configuration;
import infoToRun.DataProvider;
import metaheuristic.IMetaheuristics;
import metaheuristic.NSGAII;
import mutation.MutationDisplacement;
import mutation.MutationInversion;
import mutation.MutationSwap;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.time.LocalDate;
import java.time.LocalTime;

/**
 * main class performing tests
 */
public class Runner {
    public static void main(String[] args) {
        long start = System.currentTimeMillis();
        String definitionFile = "src/main/resources/definitionFiles/easy_1.ttp";
        DataProvider dataProvider = new DataProvider();
        dataProvider.readFile(definitionFile);

        NSGAII populationWithoutClones = new NSGAII(
                new Configuration(100, 100, 0.6f, true,
                        new MutationSwap(0.3f), new PMX(0.02f))
        );
        searchForPareto(populationWithoutClones);
        countTimeUpHere(start);
    }

    /**
     * performs test for chosen metaheuristic
     * @param algorithm name of chosen metaheuristic
     */
    private static void searchForPareto(IMetaheuristics algorithm) {
        try {
            String fileName = giveName(algorithm);
            String chartName = "chart_" + fileName;
            PrintWriter out = new PrintWriter(new File("src/Results", fileName));
            out.println(algorithm.run(chartName));
            out.close();
        } catch (FileNotFoundException ex) {
            System.out.println("Nie da się utworzyć pliku!");
        }
    }

    /**
     * creates string with unique name by date and name of metaheuristic
     * @param algorithm name of chosen metaheuristic
     * @return unique string name
     */
    private static String giveName(IMetaheuristics algorithm) {
        LocalDate date = LocalDate.now();
        LocalTime time = LocalTime.now();
        String dateAndTime = date.getMonth() + "_" + date.getDayOfMonth()
                + "__" + time.getHour() + "_" + time.getMinute() + "_" + time.getSecond();
        return dateAndTime + "_" + algorithm.getClass().getName() + ".csv";
    }

    /**
     * prints formated total time from given start time
     * @param start beginning time in milliseconds
     */
    private static void countTimeUpHere(long start) {
        long milliseconds = System.currentTimeMillis() - start;
            System.out.println((int)Math.floor((float)milliseconds / 60000) + " min "
                    + (milliseconds % 60000) / 1000 + "." + milliseconds % 1000 + "s");
    }
}