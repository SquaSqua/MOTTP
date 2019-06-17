package metaheuristic;

import comparator.CrowdingDistanceComp;
import comparator.GenerationComp;
import generator.ParetoFrontsGenerator;
import individual.Individual;
import individual.Individual_NSGAII;
import infoToRun.Configuration;
import infoToRun.DataFromFile;
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


    public NSGAII(Configuration configuration) {
        this.configuration = configuration;
    }

    /**
     * performs NSGA-II test
     * @param chartName String with name of chart file
     * @return all connected solutions and measures for one run as String
     */
    public String run(String... chartName) {
        ArrayList<ArrayList<Individual>> paretoFronts;
        initialize();
        for (int generation = 1; generation < configuration.getNumOfGenerations(); generation++) {
            ParetoFrontsGenerator.generateFrontsWithAssignments(population);
            population.addAll(generateOffspring(generation));
            paretoFronts = ParetoFrontsGenerator.generateFrontsWithAssignments(population);
            population = chooseNextGeneration(paretoFronts);
            if(generation == 1) {
                appendParetoFrontToStringBuilder(sBFirstPopFront, population);
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

        drawChart(chartName);

        return measures;
    }

    private void drawChart(String chartName) {
        XYChart chart = getChart();
//        XYSeries idealAndNadir = chart.addSeries(
//                "Ideal and Nadir point",
//                new double[] {infoToRun.DataFromFile.getIdeal().x, infoToRun.DataFromFile.getNadir().x},
//                new double[] {infoToRun.DataFromFile.getIdeal().y, infoToRun.DataFromFile.getNadir().y}
//                );
//        idealAndNadir.setMarkerColor(Color.BLACK);
        new SwingWrapper<>(chart).displayChart();
        try {
            BitmapEncoder.saveBitmap(chart, chartName, BitmapEncoder.BitmapFormat.PNG);
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
     * adds to the global variables string builders with all measures and prints on a console how many clones have been found
     * @param pareto population gouped for lists of fronts
     * @param generation number of current generation
     */
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

    /**
     * adds string builder with all population's info to given string builder
     * @param sB String Builder where all info should be saved
     * @param groupOfIndividuals any population or it's part, of which statistic is needed
     */
    private void appendParetoFrontToStringBuilder(StringBuilder sB, ArrayList<Individual> groupOfIndividuals) {
        sB.append("Czas podrozy").append(", ").append("Zarobek").append(", ").append("Stworzony w generacji\n");
        for (Individual i : groupOfIndividuals) {
            if (i.getRank() == 0) {
                sB.append(i.getFitnessTime()).append(", ").append(i.getFitnessWage()).append(", ").append(i.getBirthday());
                sB.append("\n");
            }
        }
    }

    /**
     * draws chart
     * @return nice chart
     */
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
        addColouredParetoToChart(archive, chart, new Color(0, 0, 100));
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
    private void addColouredParetoToChart(ArrayList<? extends Individual> population, XYChart chart, Color... startColor) {
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
        Color currentColor = new Color(100,0,0);
        if(startColor.length != 0) {
            currentColor = startColor[0];
        }
        XYSeries currentSeries;
        for (Individual ind : paretoFront) {
            if (ind.getBirthday() == lastGeneration) {
                wageSeries.add(ind.getFitnessWage());
                timeSeries.add(ind.getFitnessTime());
            } else {
                String name;
                if(startColor.length == 0) {
                    name = lastGeneration + "";
                }
                else {
                    name = "a" + lastGeneration;
                }
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
        if(startColor.length == 0) {
            currentSeries = chart.addSeries("Pareto Front", wageSeries, timeSeries);
        }
        else {
            currentSeries = chart.addSeries("Archive", wageSeries, timeSeries);
        }
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
}
