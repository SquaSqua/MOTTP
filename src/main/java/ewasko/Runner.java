package ewasko;

import ewasko.crossingOver.PMX;
import ewasko.infoToRun.Configuration;
import ewasko.infoToRun.DataProvider;
import ewasko.mutation.MutationDisplacement;

/**
 * main class performing tests
 */
public class Runner {
    public static void main(String[] args) {
        String definitionFile = "src/main/resources/definitionFiles/hard_1.ttp";
        DataProvider dataProvider = new DataProvider();
        dataProvider.readFile(definitionFile);

        TestGenerator testGenerator = new TestGenerator();

        Configuration standardConfiguration = new Configuration(150, 300, 0.6f, false,
                new PMX(), 0.4f,new MutationDisplacement(),0.2f);


        Object[] changingPopSize = new Object[] {10, 50, 70, 100, 300, 1000};
        Object[] changingNumberOfGenerations = new Object[] {10, 50, 70, 100, 300, 1000};
        Object[] changingTournamentSize = new Object[] {0.0067f, 0.05f, 0.1f, 0.3f, 0.7f, 1f};
//        Object[] changingCrossProb = new Object[] {0.01f, 0.03f, 0.1f, 0.3f, 0.7f, 1f};

        testGenerator.performTest(10, changingPopSize, TestGenerator.POP_SIZE, standardConfiguration);
        testGenerator.performTest(10, changingNumberOfGenerations, TestGenerator.NUM_OF_GENERATIONS, standardConfiguration);
        testGenerator.performTest(10, changingTournamentSize, TestGenerator.TOURNAMENT_SIZE, standardConfiguration);
//        testGenerator.performTest(, changingCrossProb, TestGenerator.CROSS_PROB, standardConfiguration);
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