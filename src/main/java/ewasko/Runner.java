package ewasko;

import ewasko.crossingOver.CrossingOver;
import ewasko.infoToRun.Configuration;
import ewasko.infoToRun.DataProvider;
import ewasko.metaheuristic.NSGAII;
import ewasko.mutation.Mutation;
import ewasko.simpleFactory.CrossingOverFactory;
import ewasko.simpleFactory.MutationFactory;

import java.io.FileNotFoundException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.InputMismatchException;
import java.util.Scanner;

/**
 * main class performing tests
 */
public class Runner {
    public static void main(String[] args) {
        runCLI();
    }

    private static void runCLI() {
        Scanner sc = new Scanner(System.in);
        boolean isStopped = false;
        boolean wantToChangeConfig = true;
        Configuration configuration1 = null;
        Configuration configuration2 = null;
        String hello = "Wielokryterialny Problem Mobilnego złodzieja\n********************************************";
        String chooseOptionMessage = "Wybierz opcję:\n" +
                "0 - zakończ\n" +
                "1 - wyświetl wykres dla jednego wywołania\n" +
                "2 - przeprowadź porównanie dla dwóch konfiguracji\n" +
                "3 - przeprowadź badanie dla zmiennych parametrów\n" +
                "4 - zmień ścieżkę pliku konfiguracyjnego";

        String configMessage = "Podaj parametry wywołania:";

        String paramChangedMessage = "Który parametr ma się zmieniać podczas testu?\n"
                                    + "1 - POP_SIZE\n"
                                    + "2 - NUM_OF_GENERATIONS\n"
                                    + "3 - TOURNAMENT_SIZE\n"
                                    + "4 - AVOID_CLONES\n"
                                    + "5 - CROSSING_OVER\n"
                                    + "6 - CROSS_PROB\n"
                                    + "7 - MUTATION\n"
                                    + "8 - MUT_PROB\n";
        String appRunningMessage = "Trwa liczenie, poczekaj na wynik...";
        String warningMessage = "Pamiętaj aby nie zamykać okna wykresu, ponieważ zakończy to działanie aplikacji";

        System.out.println(hello);
        getDefinitionFileFromUser();
        do {
            System.out.println(chooseOptionMessage);
            int typeOfRun;
            do{
                typeOfRun = -1;
                try{
                   typeOfRun = sc.nextInt();
                   if(typeOfRun < 0 || typeOfRun > 4) {
                       typeOfRun = -1;
                       throw new InputMismatchException();
                   }
                }
                catch (InputMismatchException e) {
                    System.out.println("Wprowadź cyfrę z zakresu 0-4");
                    sc.nextLine();
                }
            }while (typeOfRun == -1);

            if(typeOfRun != 0 && typeOfRun != 4 && configuration1 != null) {
                wantToChangeConfig = checkIfChangeConfig(sc);
            }
            switch (typeOfRun) {
                case 0: {
                    isStopped = true;
                    break;
                }
                case 1: {
                    if(wantToChangeConfig) {
                        System.out.println(configMessage);
                        configuration1 = getConfigurationFromUser();
                    }
                    NSGAII test1 = new NSGAII(configuration1);
                    System.out.println(appRunningMessage);
                    test1.run(giveName());
                    System.out.println(warningMessage);
                    break;
                }
                case 2: {
                    if(wantToChangeConfig || configuration2 == null) {
                        System.out.println(configMessage + " dla pierwszej konfiguracji");
                        configuration1 = getConfigurationFromUser();
                        System.out.println(configMessage + " dla drugiej konfiguracji");
                        configuration2 = getConfigurationFromUser();
                    }

                    NSGAII test1 = new NSGAII(configuration1);
                    NSGAII test2 = new NSGAII(configuration2);
                    System.out.println("Dla ilu powtórzeń ma się wywołać test?");
                    int numberOfCalls = sc.nextInt();
                    System.out.println(appRunningMessage);
                    TestGenerator.compareTwoConfigurations(test1, test2, numberOfCalls);
                    System.out.println(warningMessage);
                    break;
                }
                case 3: {
                    if (wantToChangeConfig) {
                        System.out.println("Podaj konfigurację wspólną dla obu testów");
                        configuration1 = getConfigurationFromUser();
                    }
                    TestGenerator testGenerator = new TestGenerator();
                    System.out.println("Dla ilu powtórzeń ma się wywołać test?");
                    int numberOfCalls = sc.nextInt();
                    System.out.println(paramChangedMessage);
                    int paramChanged = sc.nextInt();
                    int type;
                    if (paramChanged == 1 || paramChanged == 2) {
                        type = 2;
                    }
                    else {
                        type = 1;
                    }
                    Object[] changingParams = getChangingParamsFromUser(sc, type);
                    System.out.println(appRunningMessage);
                    testGenerator.performTest(numberOfCalls, changingParams, paramChanged, configuration1);
                    System.out.println(warningMessage);
                    break;
                }
                case 4: {
                    getDefinitionFileFromUser();
                    break;
                }
                default: {
                    System.out.println("Podaj jedną z opcji 0-4");
                    break;
                }
            }
        }while (!isStopped);
        sc.close();
    }

    private static boolean checkIfChangeConfig(Scanner sc) {
        System.out.println("Czy chcesz zmienić konfigurację? true/false");
        return sc.nextBoolean();
    }

    private static void getDefinitionFileFromUser() {
        Scanner sc1 = new Scanner(System.in);
        System.out.println("Podaj pełną ścieżkę pliku zestawu danych");
        String definitionFile = sc1.nextLine();
        DataProvider dataProvider = new DataProvider();
        try{
            dataProvider.readFile(definitionFile);
        }catch (FileNotFoundException fnfe) {
            System.out.println("Plik nie istnieje lub jest otwarty!\n Spróbuj ponownie");
            getDefinitionFileFromUser();
        }catch (Exception e) {
            System.out.println("Podczas odczytu danych pojawił się błąd: " + e);
        }
    }

    private static Object[] getChangingParamsFromUser(Scanner sc, int changingParam) {
        String message = "Podaj kolejno parzystą liczbę parametrów oddzielając je spacją";
        System.out.println(message);
        sc.nextLine();
        String line = sc.nextLine();
        String[] arr = line.split(" ");
        Object[] result = new Object[arr.length];
        if(changingParam == 1) { //float
            for(int i = 0; i < arr.length; i++) {
                result[i] = Float.parseFloat(arr[i] + "f");
            }
        }
        if (changingParam == 2) {//int
            for(int i = 0; i < arr.length; i++) {
                result[i] = Integer.parseInt(arr[i]);
            }
        }


        return result;
    }

    private static Configuration getConfigurationFromUser() {
        String popSizeMessage = "rozmiar populacji";
        String genersMessage = "podaj liczbę generacji";
        String tournamentSizeMessage = "podaj rozmiar turnieju z przedziału <0, 1> np. \"0,5\"";
        String avoidClonesMessage = "Czy unikać klonów? true/false";
        String crossingOverMessage = "Jaki rodzaj krzyżowania? \n1 - Cycle crossover\n2 - Partially Mapped crossover\n3 - Ordered crossover";
        String crossProbMessage = "Podaj prawdopodobieństwo krzyżowania z przedziału <0, 1> np. \"0,5\"";
        String mutationMessage = "Jaki rodzaj mutacji? \n1 - Displacement\n2 - Inversion\n3 - Swap";
        String mutProbMessage = "Podaj prawdopodobieństwo mutacji z przedziału <0, 1> np. \"0,5\"";
        String intMessage = "Wprowadź liczbę całkowitą";
        String floatMessage = "Wprowadź liczbę z przedziału (0,1) w formacie \"0,5\"";
        String booleanMessage = "Wprowadź \"true\" jeśli chcesz uniknąć klonów lub \"false\" jeśli nie.";
        String optionMessage = "Wprowadź liczbę 1-3";

        int popSize, numOfGeners, crossoverNum, mutNum;
        float tournamentSize, crossProb, mutProb;
        Boolean isAvoidClones;

        System.out.println(popSizeMessage);
        popSize = getInt(intMessage);

        System.out.println(genersMessage);
        numOfGeners = getInt(intMessage);

        System.out.println(tournamentSizeMessage);
        tournamentSize = getFloat(floatMessage);

        System.out.println(avoidClonesMessage);
        isAvoidClones = getBoolean(booleanMessage);

        System.out.println(crossingOverMessage);
        crossoverNum = getOption(optionMessage);
        CrossingOverFactory crossFactory = new CrossingOverFactory();
        CrossingOver crossingOver = crossFactory.createCrossingOver(crossoverNum+"");

        System.out.println(crossProbMessage);
        crossProb = getFloat(floatMessage);

        System.out.println(mutationMessage);
        mutNum = getOption(optionMessage);
        MutationFactory mutFactory = new MutationFactory();
        Mutation mutation = mutFactory.createMutation(mutNum+"");

        System.out.println(mutProbMessage);
        mutProb = getFloat(floatMessage);

        return new Configuration(popSize, numOfGeners, tournamentSize, isAvoidClones, crossingOver, crossProb, mutation, mutProb);
    }

    private static int getOption(String message) {
        Scanner scanner = new Scanner(System.in);
        int option = -1;
        do{
            try{
                option = scanner.nextInt();
                if (option != 1 && option != 2 && option != 3) {
                    option = -1;
                    throw new InputMismatchException();
                }
            }
            catch (InputMismatchException e) {
                System.out.println(message);
                scanner.nextLine();
            }
        }while (option == -1);
        return option;
    }

    private static float getFloat(String message) {
        Scanner scanner = new Scanner(System.in);
        float number = -1;
        do{
            try{
                number = scanner.nextFloat();
                if(number < 0 || number > 1) {
                    number = -1;
                    throw new InputMismatchException();
                }
            }
            catch (InputMismatchException e) {
                System.out.println(message);
                scanner.nextLine();
            }
        }while (number == -1);
        return number;
    }

    private static int getInt(String message) {
        Scanner scanner = new Scanner(System.in);
        int number = -1;
        do{
            try{
                number = scanner.nextInt();
                if(number < 0) {
                    number = -1;
                    throw new InputMismatchException();
                }
            }
            catch (InputMismatchException e) {
                System.out.println(message);
                scanner.nextLine();
            }
        }while (number == -1);
        return number;
    }

    private static Boolean getBoolean(String message) {
        Scanner scanner = new Scanner(System.in);
        Boolean decision = null;
        do{
            try{
                decision = scanner.nextBoolean();
            }
            catch (InputMismatchException e) {
                System.out.println(message);
                scanner.nextLine();
            }
        }while (decision == null);
        return decision;
    }

    private static String giveName() {
        LocalDate date = LocalDate.now();
        LocalTime time = LocalTime.now();
        String dateAndTime = " " + date.getDayOfMonth() + "." + date.getMonth()
                + " " + time.getHour() + ":" + time.getMinute() + ":" + time.getSecond();
        return "NSGA-II " + dateAndTime;
    }
}