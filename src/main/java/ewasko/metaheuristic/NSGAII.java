package ewasko.metaheuristic;

import ewasko.Runner;
import ewasko.TestGenerator;
import ewasko.TestResults;
import ewasko.comparator.CrowdingDistanceComp;
import ewasko.comparator.GenerationComp;
import ewasko.generator.ParetoFrontsGenerator;
import ewasko.individual.Individual;
import ewasko.individual.Individual_NSGAII;
import ewasko.infoToRun.Configuration;
import ewasko.infoToRun.DataFromFile;
import org.knowm.xchart.*;
import org.knowm.xchart.style.Styler;
import org.knowm.xchart.style.markers.SeriesMarkers;

import java.awt.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Random;


/**
 * performs a whole test of NSGA-II and producing String with whole needed solutions data
 */
public class NSGAII implements IMetaheuristics {

    private static final int ATTEMPTS_TO_AVOID_CLONES = 3;

    private static int howManyClones = 0;
    private static int howManyClonesHopeless = 0;

    private Configuration configuration;

    private ArrayList<Individual> population = new ArrayList<>();
    private ArrayList<Individual> archive = new ArrayList<>();

    private ArrayList<Integer> wageDataAllPrevious = new ArrayList<>();
    private ArrayList<Double> timeDataAllPrevious = new ArrayList<>();


    public NSGAII(Configuration configuration) {
        this.configuration = configuration;
    }

    /**
     * performs NSGA-II test
     * @param chartName String with name of chart file
     * @return all connected solutions and measures for one run as String
     */
    public Object[] run(String... chartName) {
        ArrayList<ArrayList<Individual>> paretoFronts;
        initialize();
        for (int generation = 1; generation < configuration.getNumOfGenerations(); generation++) {
            ParetoFrontsGenerator.generateFrontsWithAssignments(population);
            population.addAll(generateOffspring(generation));
            paretoFronts = ParetoFrontsGenerator.generateFrontsWithAssignments(population);
            population = chooseNextGeneration(paretoFronts);
            addParetoToChartSeries(population);
        }
        if(chartName.length != 0) {
            drawChart(chartName[0]);
        }

        return countMeasures(ParetoFrontsGenerator.generateFrontsWithAssignments(population));
    }

    private Object[] countMeasures(ArrayList<ArrayList<Individual>> paretoFronts) {
        Object[] measures = new Object[TestResults.NUMBER_OF_MEASURES];
        measures[TestResults.PARETO_FRONT_SIZE] = ParetoFrontsGenerator.PFS_measure(paretoFronts);
        measures[TestResults.EUCLIDEAN_DISTANCE] = ParetoFrontsGenerator.ED_measure(paretoFronts);
        measures[TestResults.HYPER_VOLUME] = ParetoFrontsGenerator.HV_measure(paretoFronts);
        return measures;
    }

    private void drawChart(String chartName) {
        XYChart chart = getChart(chartName);
        new SwingWrapper<>(chart).displayChart();
        try {
            String imageName = chartName.replaceAll(" ", "_").replaceAll(":", "")
                    + TestGenerator.giveName(this);
            BitmapEncoder.saveBitmap(chart, imageName , BitmapEncoder.BitmapFormat.PNG);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * initializes population of zero-generation with random individuals and sets packing plan to them
     */
    private void initialize() {
        for (int i = 0; i < configuration.getPopSize(); i++) {
            population.add(new Individual_NSGAII(DataFromFile.getDimension()));
            population.get(population.size() - 1).setPackingPlanAndFitness();
        }
    }

    /**
     * generates as population of offspring and if "avoidClones" is chosen it mutates every found clone
     * @param generation number of iteration
     * @return group of new individuals with the size of parent population
     */
    private Collection<? extends Individual> generateOffspring(int generation) {
        ArrayList<Individual_NSGAII> offspring = new ArrayList<>();
        while (offspring.size() < configuration.getPopSize()) {
            Individual_NSGAII[] children;
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

    /**
     * finds two new individuals. If they are clones it mutates them up to 3 times and if they're still clones it chooses again
     * @param generation number of iteration of NSGA-II
     * @return array of two individuals
     */
    private Individual_NSGAII[] wardOffClones(int generation) {
        Individual_NSGAII[] children = matingPool(generation);
        for (int i = 0; i < children.length; i++) {
            int attemptsLeft = ATTEMPTS_TO_AVOID_CLONES;
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

    /**
     * performs the whole evolution process. Selects 2 individuals from population, crosses them and mutates them
     * at this point population is already filled out with rank and crowding distance
     * @param generation number of generation
     * @return brand new individuals
     */
    private Individual_NSGAII[] matingPool(int generation) {
        Individual_NSGAII[] children = configuration.getCrossingOver()
                .crossOver(
                        select(population), select(population), generation);
        configuration.getMutation().mutate(children[0]);
        configuration.getMutation().mutate(children[1]);
        return children;
    }

    /**
     * from two merged populations it chooses half of individuals by rank and crowding distance
     * @param pareto two merged populations of parents and offspring
     * @return half-size population
     */
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
                firstFront.sort(new CrowdingDistanceComp());
                for (Individual ind : firstFront) {
                    if (nextGeneration.size() < configuration.getPopSize()) {
                        nextGeneration.add(ind);
                    }
                }
            }
        }
        return nextGeneration;
    }

    /**
     * performs tournament selection. Selects best individual from random group from population
     * @param population all individuals of one generation with rank and crowding distance assigned
     * @return individual being a winner of selection
     */
    private Individual_NSGAII select(ArrayList<Individual> population) {
        Individual_NSGAII bestIndividual = (Individual_NSGAII)population.get(0);//just any individual to initialize
        int bestRank = Integer.MAX_VALUE;
        Random rand = new Random();
        float tournamentSize = configuration.getTournamentSize() * configuration.getPopSize();
        for (int i = 0; i < tournamentSize; i++) {
            Individual_NSGAII individual = (Individual_NSGAII)population.get(rand.nextInt(population.size()));
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

    /**
     * adds new pareto-optimal front to the archive and choose only first front from a new group
     * @param youngPareto first front from new population
     */
    private void updateArchive(ArrayList<Individual> youngPareto) {
        for(Individual individual : youngPareto) {
            if(!archive.contains(individual)) {
                archive.add(individual);
            }
        }
        archive = ParetoFrontsGenerator.generateFrontsWithAssignments(archive).get(0);
    }

    /**
     * draws chart
     * @return nice chart
     */
    private XYChart getChart(String title) {
        // Create Chart
        XYChart chart = new XYChartBuilder().width(1000).height(600).title(title).build();
        chart.setXAxisTitle("Wartość zebranych przedmiotów");
        chart.setYAxisTitle("Czas podróży");

        //Customize chart
        chart.getStyler().setDefaultSeriesRenderStyle(XYSeries.XYSeriesRenderStyle.Scatter);
        chart.getStyler().setChartTitleVisible(true);
        chart.getStyler().setLegendPosition(Styler.LegendPosition.InsideNW);
        chart.getStyler().setChartPadding(20);

        //Series
        XYSeries previous = chart.addSeries("Fronty pareto-optymalne wcześniejszych generacji", wageDataAllPrevious, timeDataAllPrevious);
        previous.setMarkerColor(Color.LIGHT_GRAY);
        previous.setMarker(SeriesMarkers.CIRCLE);
        addColouredParetoToChart(archive, chart);
        return chart;
    }

    /**
     * for population takes only first front and adds it to the chart
     * @param population individuals with assigned rank and fitness
     */
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

    /**
     * adds the whole pareto-optimal front to the chart as many new series where the brighter color it shows,
     * the younger individual is
     * @param population individuals of last generation
     * @param chart XYChart
     */
    private void addColouredParetoToChart(ArrayList<? extends Individual> population, XYChart chart) {
        ArrayList<Individual> paretoFront = new ArrayList<>();
        for(Individual individual : population) {
            if(individual.getRank() == 0) {
                paretoFront.add(individual);
            }
        }
        paretoFront.sort(new GenerationComp());
        int lastGeneration = paretoFront.get(0).getBirthday();
        ArrayList<Integer> wageSeries = new ArrayList<>();
        ArrayList<Double> timeSeries = new ArrayList<>();
        Color currentColor = new Color(0,0,100);
        XYSeries currentSeries;
        for (Individual ind : paretoFront) {
            if (ind.getBirthday() == lastGeneration) {
                wageSeries.add(ind.getFitnessWage());
                timeSeries.add(ind.getFitnessTime());
            } else {
                String name;
                name = lastGeneration + "";
                currentSeries = chart.addSeries(name, wageSeries, timeSeries);
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
        currentSeries = chart.addSeries("Archiwum", wageSeries, timeSeries);
        currentSeries.setMarker(SeriesMarkers.CIRCLE);
        currentSeries.setMarkerColor(currentColor);
        currentSeries.setShowInLegend(true);
    }

    /**
     * for given color finds next color increased by 5 in order: Red, Green, Blue if last reached 255
     * @param color last used color
     * @return new color
     */
    private Color nextColor(Color color) {
        int r = color.getRed();
        int g = color.getGreen();
        int b = color.getBlue();
        if(r < 250) {
            r += 5;
        }
        else if (g < 250) {
            g += 5;
        }
        else {
            b += 5;
        }
        return new Color(r,g,b);
    }

    public Configuration getConfiguration() {
        return configuration;
    }
}
