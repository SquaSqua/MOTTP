package ewasko;


import org.knowm.xchart.SwingWrapper;
import org.knowm.xchart.XYChart;
import org.knowm.xchart.XYChartBuilder;
import org.knowm.xchart.XYSeries;
import org.knowm.xchart.style.markers.SeriesMarkers;

import java.awt.*;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("Duplicates")
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

    private static ArrayList<Double> pfsTrend = new ArrayList<>();
    private static ArrayList<Double> pfsDeviation = new ArrayList<>();
    private static ArrayList<Double> edTrend = new ArrayList<>();
    private static ArrayList<Double> edDeviation = new ArrayList<>();
    private static ArrayList<Double> hvTrend = new ArrayList<>();
    private static ArrayList<Double> hvDeviation = new ArrayList<>();
    private static ArrayList<Object> xValues = new ArrayList<>();

    /**
     * incremented every repetition for the same configuration
     * @param measures PFS, ED, HV of one method call
     * @param callNumber number of test (1 or 2)
     */
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

    /**
     * called once for each research (e.g. 15 repetitions of one test).
     * After call it clears measures
     * @return Prints measures in Word table format
     */
    static String  statistics() {
        double[] pfsRes1 = countStandardDeviation(pfs1);
        pfsTrend.add(pfsRes1[0]);
        pfsDeviation.add(pfsRes1[1]);
        double[] pfsRes2 = countStandardDeviation(pfs2);
        pfsTrend.add(pfsRes2[0]);
        pfsDeviation.add(pfsRes2[1]);

        double[] edRes1 = countStandardDeviation(ed1);
        edTrend.add(edRes1[0]);
        edDeviation.add(edRes1[1]);
        double[] edRes2 = countStandardDeviation(ed2);
        edTrend.add(edRes2[0]);
        edDeviation.add(edRes2[1]);

        double[] hvRes1 = countStandardDeviation(hv1);
        hvTrend.add(hvRes1[0]);
        hvDeviation.add(hvRes1[1]);
        double[] hvRes2 = countStandardDeviation(hv2);
        hvTrend.add(hvRes2[0]);
        hvDeviation.add(hvRes2[1]);
        DecimalFormat df = new DecimalFormat("#.###");

        String result = "Miary:\tŚrednia arytmetyczna\tOdchylenie standardowe\tŚrednia arytmetyczna\tOdchylenie standardowe" +

                "\nPFS\t" + df.format(pfsRes1[0]) + "\t" + df.format(pfsRes1[1])
                    + "\t" + df.format(pfsRes2[0]) + "\t" + df.format(pfsRes2[1]) +

                "\nED\t" + df.format(edRes1[0]) + "\t" + df.format(edRes1[1])
                    + "\t" + df.format(edRes2[0]) + "\t" + df.format(edRes2[1]) +

                "\nHV\t" + df.format(hvRes1[0]) + "\t" + df.format(hvRes1[1])
                    + "\t" + df.format(hvRes2[0]) + "\t" + df.format(hvRes2[1]);

        clearMeasures();

        return result;
    }


    static void getMatrixChart(String growingVariable) {
        List<XYChart> charts = new ArrayList<>();

        String [] titles = new String[] {"Trend Pareto Front Size", growingVariable, "PFS"};
        ArrayList<Double>[] data = new ArrayList[] {xValues, pfsTrend, pfsDeviation};
        charts.add(getMeasureChart(titles, data, new Color(255, 80, 80)));

        titles = new String[] {"Trend Euclidean Distance", growingVariable, "ED"};
        data = new ArrayList[] {xValues, edTrend, edDeviation};
        charts.add(getMeasureChart(titles, data, new Color(80, 180, 80)));

        titles = new String[] {"Trend Hyper Volume", growingVariable, "HV"};
        data = new ArrayList[] {xValues, hvTrend, hvDeviation};
        charts.add(getMeasureChart(titles, data, new Color(50, 80, 255)));

        new SwingWrapper<>(charts).displayChartMatrix();

        clearTrends();

//        try {
//            String imageName = "trend_" + growingVariable;
//            BitmapEncoder.saveBitmap(charts, 3, 1, imageName, BitmapEncoder.BitmapFormat.PNG);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
    }

    private static XYChart getMeasureChart(String[] titles, ArrayList<Double>[] data, Color color) {

        // Create Chart
        XYChart chart = new XYChartBuilder().width(800).height(600).title(titles[0]).xAxisTitle(titles[1]).yAxisTitle(titles[2]).build();

        // Customize Chart
        chart.getStyler().setDefaultSeriesRenderStyle(XYSeries.XYSeriesRenderStyle.Scatter);
        chart.getStyler().setChartTitleVisible(true);
        chart.getStyler().setLegendVisible(false);
        chart.getStyler().setAxisTitlesVisible(true);

        // Series
        XYSeries series = chart.addSeries(titles[0], data[0], data[1], data[2]);
        series.setMarkerColor(color);
        series.setMarker(SeriesMarkers.DIAMOND);

        return chart;
    }

    private static double[] countStandardDeviation(ArrayList<? extends Number> measures) {
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
        standardDeviation = Math.sqrt(standardDeviation);

        return new double[] {average, standardDeviation};
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

    private static void clearMeasures() {
        pfs1 = new ArrayList<>();
        pfs2 = new ArrayList<>();
        ed1 = new ArrayList<>();
        ed2 = new ArrayList<>();
        hv1 = new ArrayList<>();
        hv2 = new ArrayList<>();
    }

    private static void clearTrends() {
        pfsTrend = new ArrayList<>();
        pfsDeviation = new ArrayList<>();
        edTrend = new ArrayList<>();
        edDeviation = new ArrayList<>();
        hvTrend = new ArrayList<>();
        hvDeviation = new ArrayList<>();
        xValues = new ArrayList<>();
    }

    static void setXValues(Object[] values) {
        for(Object i : values) {
            if(i instanceof Integer) {
                xValues.add((Integer)i);
            }
            if(i instanceof Float) {
                xValues.add((Float)i);
            }
        }
    }
}
