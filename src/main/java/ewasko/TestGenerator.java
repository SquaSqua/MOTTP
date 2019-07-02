package ewasko;

import ewasko.crossingOver.CrossingOver;
import ewasko.infoToRun.Configuration;
import ewasko.metaheuristic.IMetaheuristics;
import ewasko.metaheuristic.NSGAII;
import ewasko.mutation.Mutation;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.stream.IntStream;

public class TestGenerator {
    private static final int POP_SIZE = 1;
    private static final int NUM_OF_GENERATIONS = 2;
    private static final int TOURNAMENT_SIZE = 3;
    private static final int AVOID_CLONES = 4;
    private static final int CROSSING_OVER = 5;
    private static final int CROSS_PROB = 6;
    private static final int MUTATION = 7;
    private static final int MUT_PROB = 8;
    private static final int[] CHART_CREATABLE = new int[] {1,2,3,6,8};


    void performTest(int numberOfCalls, Object[] changingParams, int paramChanged, Configuration configuration) {

        ArrayList<Configuration> allConfigs = setAllConfigurations(changingParams, paramChanged, configuration);

        for(int i = 0; i < changingParams.length - 1; i++) {
            NSGAII test1 = new NSGAII(allConfigs.get(i));
            NSGAII test2 = new NSGAII(allConfigs.get(++i));
            compareTwoConfigurations(test1, test2, numberOfCalls);
        }

        if (IntStream.of(CHART_CREATABLE).anyMatch(x -> x == paramChanged)) {
            try {
                TestResults.setXValues(changingParams);
                TestResults.getMatrixChart(getChartName(paramChanged));
            }
            catch (Exception e) {
                System.out.println(e.toString());
            }
        }

    }

    static void compareTwoConfigurations(NSGAII test1, NSGAII test2, int numberOfCalls) {
        for (int i = 0; i < numberOfCalls - 1; i++) {
            System.out.println("Zacząłem " + i);
            searchForPareto(test1, 1);
            searchForPareto(test2, 2);
            System.out.println("Skończyłem " + i);
        }
        String[] configComparison = compareConfig(test1.getConfiguration(), test2.getConfiguration());

        System.out.println("Ostatnie");
        searchForPareto(test1, 1, configComparison[1]);
        searchForPareto(test2, 2, configComparison[2]);

        System.out.println(configComparison[0]);
        System.out.println(TestResults.statistics());
    }

    /**
     * performs test for chosen metaheuristic
     *
     * @param algorithm name of chosen metaheuristic
     */
    private static void searchForPareto(IMetaheuristics algorithm, int testNumber, String... chart) {
        String chartName;
        if (chart.length != 0) {
            chartName = chart[0];
            TestResults.addMeasures(algorithm.run(chartName), testNumber);
        } else {
            TestResults.addMeasures(algorithm.run(), testNumber);
        }
    }

    /**
     * creates string with unique name by date and name of metaheuristic
     *
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

        StringBuilder testName1 = new StringBuilder();
        StringBuilder testName2 = new StringBuilder();
        if (config1.getPopSize() == config2.getPopSize()) {
            result.append("Rozmiar populacji: ").append(config1.getPopSize()).append(", ");
        } else {
            difference.append("\tRozmiar populacji: \n\t").append(config1.getPopSize()).append("\t").append(config2.getPopSize());
            testName1.append("Rozmiar populacji: ").append(config1.getPopSize()).append(" ");
            testName2.append("Rozmiar populacji: ").append(config2.getPopSize()).append(" ");
        }

        if (config1.getNumOfGenerations() == config2.getNumOfGenerations()) {
            result.append("liczba generacji: ").append(config1.getNumOfGenerations()).append(", ");
        } else {
            difference.append("\tLiczba generacji: \n\t").append(config1.getNumOfGenerations())
                    .append("\t").append(config2.getNumOfGenerations());
            testName1.append("Liczba generacji: ").append(config1.getNumOfGenerations()).append(" ");
            testName2.append("Liczba generacji: ").append(config2.getNumOfGenerations()).append(" ");
        }

        if (config1.getTournamentSize() == config2.getTournamentSize()) {
            result.append("rozmiar turnieju: ").append(config1.getTournamentSize()).append(", ");
        } else {
            difference.append("\tRozmiar turnieju: \n\t").append(config1.getTournamentSize())
                    .append("\t").append(config2.getTournamentSize());
            testName1.append("Rozmiar turnieju: ").append(config1.getTournamentSize()).append(" ");
            testName2.append("Rozmiar turnieju: ").append(config2.getTournamentSize()).append(" ");
        }

        if (config1.getCrossingOver().getClass().getSimpleName().equals(config2.getCrossingOver().getClass().getSimpleName())) {
            result.append("krzyżowanie ").append(config1.getCrossingOver().getClass().getSimpleName()).append(", ");
        } else {
            difference.append("\tKrzyżowanie \n\t").append(config1.getCrossingOver().getClass().getSimpleName())
                    .append("\t").append(config2.getCrossingOver().getClass().getSimpleName());
            testName1.append("Krzyżowanie ").append(config1.getCrossingOver().getClass().getSimpleName()).append(" ");
            testName2.append("Krzyżowanie ").append(config2.getCrossingOver().getClass().getSimpleName()).append(" ");
        }

        if (config1.getCrossingOver().getCrossProb() == config2.getCrossingOver().getCrossProb()) {
            result.append("Px: ").append(config1.getCrossingOver().getCrossProb()).append(", ");
        } else {
            difference.append("\tPx \n\t").append(config1.getCrossingOver().getCrossProb())
                    .append("\t").append(config2.getCrossingOver().getCrossProb());
            testName1.append("Px ").append(config1.getCrossingOver().getCrossProb()).append(" ");
            testName2.append("Px ").append(config2.getCrossingOver().getCrossProb()).append(" ");
        }

        if (config1.getMutation().getClass().getSimpleName().equals(config2.getMutation().getClass().getSimpleName())) {
            result.append("mutacja ").append(config1.getMutation().getClass().getSimpleName()).append(", ");
        } else {
            difference.append("\tMutacja \n\t").append(config1.getMutation().getClass().getSimpleName())
                    .append("\t").append(config2.getMutation().getClass().getSimpleName());
            testName1.append("Mutacja ").append(config1.getMutation().getClass().getSimpleName()).append(" ");
            testName2.append("Mutacja ").append(config2.getMutation().getClass().getSimpleName()).append(" ");
        }

        if (config1.getMutation().getMutProb() == config2.getMutation().getMutProb()) {
            result.append("Pm: ").append(config1.getMutation().getMutProb()).append(", ");
        } else {
            difference.append("\tPm \n\t").append(config1.getMutation().getMutProb())
                    .append("\t").append(config2.getMutation().getMutProb());
            testName1.append("Pm ").append(config1.getMutation().getMutProb()).append(" ");
            testName2.append("Pm ").append(config2.getMutation().getMutProb()).append(" ");
        }

        if (config1.isAvoidClones() == config2.isAvoidClones()) {
            result.append("unikanie klonów: ").append(config1.isAvoidClones());
        } else {
            difference.append("\tUnikanie klonów \n\t").append(config1.isAvoidClones())
                    .append("\t").append(config2.isAvoidClones());
            testName1.append(config1.isAvoidClones() ? "z unikaniem klonów" : "z dopuszczaniem klonów ");
            testName2.append(config2.isAvoidClones() ? "z unikaniem klonów" : "z dopuszczaniem klonów ");
        }
        result.append("\n").append(difference);
        return new String[]{result.toString(), testName1.toString(), testName2.toString()};
    }

    private ArrayList<Configuration> setAllConfigurations(Object[] changingParams, int paramChanged, Configuration configuration) {

        ArrayList<Configuration> allConfigs = new ArrayList<>();

        for(int i = 0; i < changingParams.length; i++) {
            allConfigs.add(new Configuration(configuration));
        }

        switch (paramChanged) {
            case POP_SIZE: {
                for(int i = 0; i < allConfigs.size(); i++) {
                    allConfigs.get(i).setPopSize((int)changingParams[i]);
                }
                break;
            }
            case NUM_OF_GENERATIONS: {
                for(int i = 0; i < allConfigs.size(); i++) {
                    allConfigs.get(i).setNumOfGenerations((int)changingParams[i]);
                }
                break;
            }
            case TOURNAMENT_SIZE: {
                for(int i = 0; i < allConfigs.size(); i++) {
                    allConfigs.get(i).setTournamentSize((float)changingParams[i]);
                }
                break;
            }
            case AVOID_CLONES: {
                for(int i = 0; i < allConfigs.size(); i++) {
                    allConfigs.get(i).setAvoidClones((boolean)changingParams[i]);
                }
                break;
            }
            case CROSSING_OVER: {
                for(int i = 0; i < allConfigs.size(); i++) {
                    allConfigs.get(i).setCrossingOver((CrossingOver) changingParams[i]);
                }
                break;
            }
            case CROSS_PROB: {
                for(int i = 0; i < allConfigs.size(); i++) {
                    allConfigs.get(i).setCrossProb((float) changingParams[i]);
                }
                break;
            }
            case MUTATION: {
                for(int i = 0; i < allConfigs.size(); i++) {
                    allConfigs.get(i).setMutation((Mutation) changingParams[i]);
                }
                break;
            }
            case MUT_PROB: {
                for(int i = 0; i < allConfigs.size(); i++) {
                    allConfigs.get(i).setMutProb((float) changingParams[i]);
                }
                break;
            }
        }
        return allConfigs;
    }

    private String getChartName(int paramChanged) throws Exception {
        switch (paramChanged) {
            case POP_SIZE: {
                return "Rozmiar populacji";
            }
            case NUM_OF_GENERATIONS: {
                return "Liczba generacji";
            }
            case TOURNAMENT_SIZE: {
                return "Rozmiar turnieju";
            }
            case CROSS_PROB: {
                return "Prawdopodobieństwo krzyżowania";
            }
            case MUT_PROB: {
                return "Prawdopodobieństwo mutacji";
            }
            default: {
                throw new Exception ("Nie można stworzyć takiego wykresu!");
            }
        }
    }
}