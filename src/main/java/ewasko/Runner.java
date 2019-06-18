package ewasko;

import ewasko.crossingOver.PMX;
import ewasko.infoToRun.Configuration;
import ewasko.infoToRun.DataProvider;
import ewasko.metaheuristic.IMetaheuristics;
import ewasko.metaheuristic.NSGAII;
import ewasko.mutation.MutationDisplacement;
import ewasko.mutation.MutationInversion;
import ewasko.mutation.MutationSwap;

import java.time.LocalDate;
import java.time.LocalTime;

/**
 * main class performing tests
 */
public class Runner {
    public static void main(String[] args) {
        String definitionFile = "src/main/resources/definitionFiles/hard_1.ttp";
        DataProvider dataProvider = new DataProvider();
        dataProvider.readFile(definitionFile);


        Configuration config1 = new Configuration(10, 100, 0.6f, false,
                new MutationDisplacement(0.3f), new PMX(0.02f));
        Configuration config2 = new Configuration(50, 100, 0.6f, false,
                new MutationDisplacement(0.3f), new PMX(0.02f));

        Configuration config3 = new Configuration(70, 100, 0.6f, false,
                new MutationDisplacement(0.3f), new PMX(0.02f));
        Configuration config4 = new Configuration(100, 100, 0.6f, false,
                new MutationDisplacement(0.3f), new PMX(0.02f));

        Configuration config5 = new Configuration(300, 100, 0.6f, false,
                new MutationDisplacement(0.3f), new PMX(0.02f));
        Configuration config6 = new Configuration(1000, 100, 0.6f, false,
                new MutationDisplacement(0.3f), new PMX(0.02f));



        NSGAII test1 = new NSGAII(config1);
        NSGAII test2 = new NSGAII(config2);
        compareTwoConfigurations(test1, test2, 1);

//        NSGAII test3 = new NSGAII(config3);
//        NSGAII test4 = new NSGAII(config4);
//        System.out.println();
//        compareTwoConfigurations(test3, test4, 10);
//
//        NSGAII test5 = new NSGAII(config5);
//        NSGAII test6 = new NSGAII(config6);
//        System.out.println();
//        compareTwoConfigurations(test5, test6, 10);
    }

    private static void compareTwoConfigurations(NSGAII test1, NSGAII test2, int numberOfCalls) {
        for(int i = 0; i < numberOfCalls - 1; i++) {
            searchForPareto(test1, 1);
            searchForPareto(test2, 2);
        }
        String[] configComparison = compareConfig(test1.getConfiguration(), test2.getConfiguration());

        searchForPareto(test1, 1,configComparison[1]);
        searchForPareto(test2, 2,configComparison[2]);

        System.out.println(configComparison[0]);
        System.out.println(TestResults.statistics());
    }

    /**
     * performs test for chosen metaheuristic
     * @param algorithm name of chosen metaheuristic
     */
    private static void searchForPareto(IMetaheuristics algorithm, int testNumber, String... chart) {
        String chartName;
        if(chart.length != 0) {
            chartName = chart[0];
            TestResults.addMeasures(algorithm.run(chartName), testNumber);
        }
        else {
            TestResults.addMeasures(algorithm.run(), testNumber);
        }
    }

    /**
     * creates string with unique name by date and name of metaheuristic
     * @param algorithm name of chosen metaheuristic
     * @return unique string name
     */
    public static String giveName(IMetaheuristics algorithm) {
        LocalDate date = LocalDate.now();
        LocalTime time = LocalTime.now();
        String dateAndTime = "__" + date.getDayOfMonth()
                + "__" + time.getHour() + "_" + time.getMinute() + "_" + time.getSecond();
        return dateAndTime + "_" + algorithm.getClass().getName() + ".csv";
    }

    private static String[] compareConfig(Configuration config1, Configuration config2) {
        StringBuilder result = new StringBuilder();
        StringBuilder difference = new StringBuilder();

        String testName1 = "";
        String testName2 = "";
        if(config1.getPopSize() == config2.getPopSize()) {
            result.append("Rozmiar populacji: ").append(config1.getPopSize()).append(", ");
        }
        else {
            difference.append("\tRozmiar populacji: \n\t").append(config1.getPopSize()).append("\t").append(config2.getPopSize());
            testName1 = "Rozmiar populacji: " + config1.getPopSize();
            testName2 = "Rozmiar populacji: " + config2.getPopSize();
        }

        if(config1.getNumOfGenerations() == config2.getNumOfGenerations()) {
            result.append("liczba generacji: ").append(config1.getNumOfGenerations()).append(", ");
        }
        else {
            difference.append("\tLiczba generacji: \n\t").append(config1.getNumOfGenerations())
                                            .append("\t").append(config2.getNumOfGenerations());
            testName1 = "Liczba generacji: " + config1.getNumOfGenerations();
            testName2 = "Liczba generacji: " + config2.getNumOfGenerations();
        }

        if(config1.getTournamentSize() == config2.getTournamentSize()) {
            result.append("rozmiar turnieju: ").append(config1.getTournamentSize()).append(", ");
        }
        else {
            difference.append("\tRozmiar turnieju: \n\t").append(config1.getTournamentSize())
                                            .append("\t").append(config2.getTournamentSize());
            testName1 = "Rozmiar turnieju: " + config1.getTournamentSize();
            testName2 = "Rozmiar turnieju: " + config2.getTournamentSize();
        }

        if(config1.getCrossingOver().getClass().getSimpleName().equals(config2.getCrossingOver().getClass().getSimpleName())) {
            result.append("krzyżowanie ").append(config1.getCrossingOver().getClass().getSimpleName()).append(", ");
        }
        else {
            difference.append("\tKrzyżowanie \n\t").append(config1.getCrossingOver().getClass().getSimpleName())
                                    .append("\t").append(config2.getCrossingOver().getClass().getSimpleName());
            testName1 = "Krzyżowanie " + config1.getCrossingOver().getClass().getSimpleName();
            testName2 = "Krzyżowanie " + config2.getCrossingOver().getClass().getSimpleName();
        }

        if(config1.getCrossingOver().getCrossProb() == config2.getCrossingOver().getCrossProb()) {
            result.append("Px: ").append(config1.getCrossingOver().getCrossProb()).append(", ");
        }
        else {
            difference.append("\tPx \n\t").append(config1.getCrossingOver().getCrossProb())
                        .append("\t").append(config2.getCrossingOver().getCrossProb());
            testName1 = "Px " + config1.getCrossingOver().getCrossProb();
            testName2 = "Px " + config2.getCrossingOver().getCrossProb();
        }

        if(config1.getMutation().getClass().getSimpleName().equals(config2.getMutation().getClass().getSimpleName())) {
            result.append("mutacja ").append(config1.getMutation().getClass().getSimpleName()).append(", ");
        }
        else {
            difference.append("\tMutacja \n\t").append(config1.getMutation().getClass().getSimpleName())
                                .append("\t").append(config2.getMutation().getClass().getSimpleName());
            testName1 = "Mutacja " + config1.getMutation().getClass().getSimpleName();
            testName2 = "Mutacja " + config2.getMutation().getClass().getSimpleName();
        }

        if(config1.getMutation().getMutProb() == config2.getMutation().getMutProb()) {
            result.append("Pm: ").append(config1.getMutation().getMutProb()).append(", ");
        }
        else {
            difference.append("\tPm \n\t").append(config1.getMutation().getMutProb())
                            .append("\t").append(config2.getMutation().getMutProb());
            testName1 = "Pm " + config1.getMutation().getMutProb();
            testName2 = "Pm " + config2.getMutation().getMutProb();
        }

        if(config1.isAvoidClones() == config2.isAvoidClones()) {
            result.append("unikanie klonów: ").append(config1.isAvoidClones());
        }
        else {
            difference.append("\tUnikanie klonów \n\t").append(config1.isAvoidClones())
                    .append("\t").append(config2.isAvoidClones());
            testName1 = config1.isAvoidClones() ? "Z unikaniem klonów" : "Z dopuszczaniem klonów";
            testName2 = config1.isAvoidClones() ? "Z unikaniem klonów" : "Z dopuszczaniem klonów";
        }
        result.append("\n").append(difference);
        return new String[] {result.toString(), testName1, testName2};
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