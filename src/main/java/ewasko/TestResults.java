package ewasko;

import java.text.DecimalFormat;
import java.util.ArrayList;

public class TestResults {
    public static final int NUMBER_OF_MEASURES = 3;
    public static final int PARETO_FRONT_SIZE = 0;
    public static final int EUCLIDEAN_DISTANCE = 1;
    public static final int HYPER_VOLUME = 2;

    private static ArrayList<Integer> pfs1 = new ArrayList<>();
    private static ArrayList<Integer> pfs2 = new ArrayList<>();
    private static ArrayList<Double> ed1 = new ArrayList<>();
    private static ArrayList<Double> ed2 = new ArrayList<>();
    private static ArrayList<Double> hv1 = new ArrayList<>();
    private static ArrayList<Double> hv2 = new ArrayList<>();


    static void addMeasures(Object[] measures, int callNumber) {
        if(callNumber == 1) {
            pfs1.add((int)measures[PARETO_FRONT_SIZE]);
            ed1.add((Double)measures[EUCLIDEAN_DISTANCE]);
            hv1.add((Double)measures[HYPER_VOLUME]);
        }
        else {
            pfs2.add((int)measures[PARETO_FRONT_SIZE]);
            ed2.add((Double)measures[EUCLIDEAN_DISTANCE]);
            hv2.add((Double)measures[HYPER_VOLUME]);
        }
    }

    static String  statistics() {

        String result = "Miary:\tŚrednia arytmetyczna\tOdchylenie standardowe\tŚrednia arytmetyczna\tOdchylenie standardowe" +
                "PFS\t" + countStandardDeviation(pfs1) + "\t" + countStandardDeviation(pfs2) +
                "\nED\t" + countStandardDeviation(ed1) + "\t" + countStandardDeviation(ed2) +
                "\nHV\t" + countStandardDeviation(hv1) + "\t" + countStandardDeviation(hv2);
        return result;
    }

    private static String countStandardDeviation(ArrayList<? extends Number> measures) {
        double standardDeviation = 0.0;
        double average = countArithmeticAverage(measures);
        for(Number i : measures) {
            if(i instanceof Integer) {
                i = Double.valueOf((Integer)i);
            }
            double difference = average - (double)i;
            standardDeviation += Math.pow(difference, 2);
        }
        standardDeviation /= measures.size();

        DecimalFormat df = new DecimalFormat("#.###");

        return df.format(average) + "\t" + df.format(Math.sqrt(standardDeviation));
    }

    private static double countArithmeticAverage(ArrayList<? extends Number> measures) {
        double average = 0;
        for(Object i : measures) {
            if(i instanceof Integer) {
                i = Double.valueOf((Integer)i);
            }
            average += (double)i;
        }
        return average/measures.size();
    }
}
