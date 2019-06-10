import org.knowm.xchart.SwingWrapper;
import org.knowm.xchart.XYChart;
import org.knowm.xchart.XYChartBuilder;
import org.knowm.xchart.XYSeries;
import org.knowm.xchart.style.Styler;
import org.knowm.xchart.style.markers.SeriesMarkers;

import java.awt.*;
import java.util.ArrayList;
import java.util.Random;

/**
 * performs a whole test of NSGA-II and saves
 */
class Evolution implements IMetaheuristics {

    private static final int attemptsToAvoidClones = 3;

    private static int howManyClones = 0;
    private static int howManyClonesHopeless = 0;

    private Configuration configuration;

    private ArrayList<Individual> population = new ArrayList<>();
    private ArrayList<Individual> archive = new ArrayList<>();

    private String measures = "", firstPopFront = "\nFirst population front\n",
            middlePopFront = "\nMiddle population front\n", lastPopFront = "\nLast population front\n";
    private StringBuilder sBMeasures = new StringBuilder(measures);
    private StringBuilder sBFirstPopFront = new StringBuilder(firstPopFront);
    private StringBuilder sBMiddlePopFront = new StringBuilder(middlePopFront);
    private StringBuilder sBLastPopFront = new StringBuilder(lastPopFront);

    private ArrayList<Integer> wageDataAllPrevious = new ArrayList<>();
    private ArrayList<Double> timeDataAllPrevious = new ArrayList<>();


    Evolution(Configuration configuration) {
        this.configuration = configuration;
    }

    /**
     * cos robi
     * @return zwraca...
     */
    public String run() {
        ArrayList<ArrayList<Individual>> paretoFronts;
        initialize();
        for (int generation = 1; generation < configuration.getNumOfGenerations(); generation++) {
            ParetoFrontsGenerator.generateFrontsWithAssignments(population);
            population.addAll(generateOffspring(generation));
            paretoFronts = ParetoFrontsGenerator.generateFrontsWithAssignments(population);
            population = chooseNextGeneration(paretoFronts);
            if(generation == 1) {
                appendParetoFrontToStringBuilder(sBFirstPopFront, population);
//                appendPopulationToStringBuilder(sBFirstPopFront);
                statistics(paretoFronts, generation);
            }
            addParetoToChartSeries(population);
        }
        paretoFronts = ParetoFrontsGenerator.generateFrontsWithAssignments(population);
        statistics(paretoFronts, configuration.getNumOfGenerations());
        sBMeasures.append("Nadir:,").append(DataFromFile.getNadir().x).append(", ").append(DataFromFile.getNadir().y).append("\n");
        sBMeasures.append("Ideal:,").append(DataFromFile.getIdeal().x).append(", ").append(DataFromFile.getIdeal().y).append("\n");
        appendParetoFrontToStringBuilder(sBLastPopFront, population);
        sBMiddlePopFront.append(sBLastPopFront);
        sBFirstPopFront.append(sBMiddlePopFront);
        sBMeasures.append(sBFirstPopFront);
        measures = sBMeasures.toString();

        XYChart chart = getChart();
//        XYSeries idealAndNadir = chart.addSeries(
//                "Ideal and Nadir point",
//                new double[] {DataFromFile.getIdeal().x, DataFromFile.getNadir().x},
//                new double[] {DataFromFile.getIdeal().y, DataFromFile.getNadir().y}
//                );
//        idealAndNadir.setMarkerColor(Color.BLACK);
        new SwingWrapper<>(chart).displayChart();

        return measures;
    }

    private void initialize() {
        for (int i = 0; i < configuration.getPopSize(); i++) {
            population.add(new Individual_NSGA_II(DataFromFile.getDimension()));
            population.get(population.size() - 1).setPackingPlanAndFitness();
        }
    }

    private ArrayList<Individual_NSGA_II> generateOffspring(int generation) {
        ArrayList<Individual_NSGA_II> offspring = new ArrayList<>();
        while (offspring.size() < configuration.getPopSize()) {
            Individual_NSGA_II[] children;
            if(configuration.isAvoidClones()) {
                children = wardOffClones(generation);
            }
            else {
                children = matingPool(generation);
            }
            offspring.add(children[0]);
            if (offspring.size() < configuration.getPopSize()) {
                offspring.add(children[1]);
            }
        }
        return offspring;
    }

    private Individual_NSGA_II[] wardOffClones(int generation) {
        Individual_NSGA_II[] children = matingPool(generation);
        for (int i = 0; i < children.length; i++) {
            int attemptsLeft = attemptsToAvoidClones;
            while (attemptsLeft > 0) {
                if (population.contains(children[i])) {
                    configuration.getMutation().mutate(children[i], 1);//przeszukaj lokalnie przestrzeń dookoła
                    attemptsLeft--;
                    howManyClones++;
                } else {
                    break;
                }
            }
            if(population.contains(children[i])) {
                howManyClonesHopeless++;
                children[i] = wardOffClones(generation)[i];
            }
        }
        return children;
    }

    //at this point population is already filled out with rank and crowding distance
    private Individual_NSGA_II[] matingPool(int generation) {
        Individual_NSGA_II[] children = configuration.getCrossingOver()
                .crossOver(
                        select(population), select(population), generation);
        configuration.getMutation().mutate(children[0]);
        configuration.getMutation().mutate(children[1]);
        return children;
    }

    private ArrayList<Individual> chooseNextGeneration(ArrayList<ArrayList<Individual>> pareto) {
        ArrayList<ArrayList<Individual>> temporaryPareto = new ArrayList<>(pareto);
        ArrayList<Individual> nextGeneration = new ArrayList<>();
        updateArchive(pareto.get(0));
        while (nextGeneration.size() < configuration.getPopSize()) {
            if (temporaryPareto.get(0).size() <= configuration.getPopSize() - nextGeneration.size()) {
                nextGeneration.addAll(temporaryPareto.get(0));
                temporaryPareto.remove(0);
            } else {
                ArrayList<Individual> firstFront = temporaryPareto.get(0);
                firstFront.sort(new Comparator_CrowdingDistance());
                for (Individual ind : firstFront) {
                    if (nextGeneration.size() < configuration.getPopSize()) {
                        nextGeneration.add(ind);
                    }
                }
            }
        }
        return nextGeneration;
    }

    private Individual_NSGA_II select(ArrayList<Individual> population) {
        Individual_NSGA_II bestIndividual = (Individual_NSGA_II)population.get(0);//just any individual to initialize
        int bestRank = Integer.MAX_VALUE;
        Random rand = new Random();
        float tournamentSize = configuration.getTournamentSize() * configuration.getPopSize();
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

    private void updateArchive(ArrayList<Individual> youngPareto) {
        for(Individual individual : youngPareto) {
            if(!archive.contains(individual)) {
                archive.add(individual);
            }
        }
        archive = ParetoFrontsGenerator.generateFrontsWithAssignments(archive).get(0);
    }

    private void statistics(ArrayList<ArrayList<Individual>> pareto, int generation) {
        sBMeasures.append("Generacja ").append(generation).append("\n");
        sBMeasures.append(ParetoFrontsGenerator.ED_measure(pareto)).append(", ")
                .append(ParetoFrontsGenerator.PFS_measure(pareto)).append(", ")
                .append(ParetoFrontsGenerator.HV_measure(pareto));
        sBMeasures.append("\n");
        if(configuration.isAvoidClones() && generation != 1){
            System.out.println("Klonów:" + howManyClones);
            System.out.println("Klonów powyżej 3 prób:" + howManyClonesHopeless);
        }
    }

//    private void appendPopulationToStringBuilder(StringBuilder sB) {
//        int currentRank = 0;
//        sB.append("Czas podrozy").append(", ").append("Zarobek").append(", ").append("Stworzony w generacji\n");
//        for (Individual i : population) {
//            if (i.getRank() != currentRank) {
//                currentRank++;
//                sB.append("\n");
//            }
//            sB.append(i.getFitnessTime()).append(", ").append(i.getFitnessWage()).append(", ").append(i.getBirthday());
////                    .append(Arrays.toString(i.getRoute())).append(", ").append(Arrays.toString(i.getPackingPlan()));
//            sB.append("\n");
//        }
//    }

    private void appendParetoFrontToStringBuilder(StringBuilder sB, ArrayList<Individual> groupOfIndividuals) {
        sB.append("Czas podrozy").append(", ").append("Zarobek").append(", ").append("Stworzony w generacji\n");
        for (Individual i : groupOfIndividuals) {
            if (i.getRank() == 0) {
                sB.append(i.getFitnessTime()).append(", ").append(i.getFitnessWage()).append(", ").append(i.getBirthday());
//                    .append(Arrays.toString(i.getRoute())).append(", ").append(Arrays.toString(i.getPackingPlan()));
                sB.append("\n");
            }
        }
    }

    private XYChart getChart() {
        // Create Chart
        XYChart chart = new XYChartBuilder().width(1000).height(600).build();
        chart.setTitle(getClass().getSimpleName());
        chart.setXAxisTitle("Wartość zebranych przedmiotów");
        chart.setYAxisTitle("Czas podróży");

        //Customize chart
        chart.getStyler().setDefaultSeriesRenderStyle(XYSeries.XYSeriesRenderStyle.Scatter);
        chart.getStyler().setChartTitleVisible(true);
        chart.getStyler().setLegendPosition(Styler.LegendPosition.InsideNW);
        chart.getStyler().setChartPadding(20);
//        chart.getStyler().setMarkerSize(16);

        //Series
        XYSeries previous = chart.addSeries("Wcześniejsze Pareto Fronty", wageDataAllPrevious, timeDataAllPrevious);
        previous.setMarkerColor(Color.LIGHT_GRAY);
        previous.setMarker(SeriesMarkers.CIRCLE);
        addColouredParetoToChart(population, chart);

        return chart;
    }

    private void addParetoToChartSeries(ArrayList<? extends Individual> population) {
        ArrayList<Individual> paretoFront = new ArrayList<>();
        for(Individual individual : population) {
            if(individual.getRank() == 0) {
                paretoFront.add(individual);
            }
        }
        for(Individual individual : paretoFront) {
            wageDataAllPrevious.add(individual.getFitnessWage());
            timeDataAllPrevious.add(individual.getFitnessTime());
        }
    }


    private void addColouredParetoToChart(ArrayList<? extends Individual> population, XYChart chart) {
        ArrayList<Individual> paretoFront = new ArrayList<>();
        for(Individual individual : population) {
            if(individual.getRank() == 0) {
                paretoFront.add(individual);
            }
        }
        paretoFront.sort(new Comparator_Generation());
        int lastGeneration = paretoFront.get(0).getBirthday();
        ArrayList<Integer> wageSeries = new ArrayList<>();
        ArrayList<Double> timeSeries = new ArrayList<>();
        Color currentColor = new Color(100,0,0);
        XYSeries currentSeries;
        for (Individual ind : paretoFront) {
            if (ind.getBirthday() == lastGeneration) {
                wageSeries.add(ind.getFitnessWage());
                timeSeries.add(ind.getFitnessTime());
            } else {
                currentSeries = chart.addSeries(lastGeneration + "", wageSeries, timeSeries);
                currentSeries.setMarkerColor(currentColor);
                currentSeries.setMarker(SeriesMarkers.CIRCLE);
                currentSeries.setShowInLegend(false);
                wageSeries = new ArrayList<>();
                timeSeries = new ArrayList<>();
                currentColor = nextColor(currentColor);
                lastGeneration = ind.getBirthday();

                wageSeries.add(ind.getFitnessWage());
                timeSeries.add(ind.getFitnessTime());
            }
        }
        currentSeries = chart.addSeries("Pareto Front", wageSeries, timeSeries);
        currentSeries.setMarker(SeriesMarkers.CIRCLE);
        currentSeries.setMarkerColor(Color.RED);
        currentSeries.setShowInLegend(true);
    }

    private Color nextColor(Color color) {
        int r = color.getRed();
        int g = color.getGreen();
        int b = color.getBlue();
        if(r < 255) {
            r += 5;
        }
        else if (g < 255) {
            g += 5;
        }
        else {
            b += 5;
        }
        return new Color(r,g,b);
    }
}
