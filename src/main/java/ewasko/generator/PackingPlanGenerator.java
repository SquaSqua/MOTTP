package ewasko.generator;

import ewasko.individual.Individual;
import ewasko.infoToRun.DataFromFile;
import ewasko.infoToRun.DataProvider;

import java.util.ArrayList;

public class PackingPlanGenerator {

    private static double maxSpeed = DataFromFile.getMaxSpeed();
    private static int capacity = DataFromFile.getCapacity();
    private static int dimension = DataFromFile.getDimension();
    private static double[][] distances = DataFromFile.getDistances();
    private static int[][] items = DataFromFile.getItems();
    private static Integer[][] groupedItems = createGroupedItemsArray();
    private static double coefficient = (maxSpeed - DataFromFile.getMinSpeed()) / capacity;

    public static void setAllData() {
        maxSpeed = DataFromFile.getMaxSpeed();
        capacity = DataFromFile.getCapacity();
        dimension = DataFromFile.getDimension();
        distances = DataFromFile.getDistances();
        items = DataFromFile.getItems();
        groupedItems = createGroupedItemsArray();
        coefficient = (maxSpeed - DataFromFile.getMinSpeed()) / capacity;
    }

    /**
     * finds and sets packing plan for one individual
      * @param individual will be modified
     */
    public static void settlePackingPlan(Individual individual) {
        short[] route = individual.getRoute();
        ArrayList<double[]> gainOfItems = countGain(route);
        boolean[] packingPlan = new boolean[items.length];
        int ind = 0;
        int currentWeight = 0;
        while(currentWeight < capacity && ind < gainOfItems.size()) {
            int rowNumber = (int)gainOfItems.get(ind)[0];
            if(items[rowNumber][2] + currentWeight <= capacity) {
                packingPlan[rowNumber] = true;
                currentWeight += items[rowNumber][2];
            }
            ind++;
        }
        individual.setPackingPlan(packingPlan);
    }

    /**
     * finds and sets both values of fitness for all objectives
     * @param individual will be modified
     */
    public static void setFitnessForIndividual(Individual individual) {
        countFitnessTime(individual);
        countFitnessWage(individual);
    }

    /**
     * counts gain ratio for each item for give route
     * @param route array of cities set in order
     * @return sorted descending array od pairs (index of item, gain ratio)
     */
    private static ArrayList<double[]> countGain(short[] route) {
        ArrayList<double[]> gainOfItems = new ArrayList<>();
        for(int i = 0; i < items.length; i++) {
            int[] currentRow = items[i];
            gainOfItems.add(new double[] {i, currentRow[DataProvider.PROFIT_FROM_ITEM] /
                    (currentRow[DataProvider.WEIGHT_OF_ITEM]
                            * countTime(route, currentRow[DataProvider.CITY_OF_ITEM],
                            countSpeed(currentRow[DataProvider.WEIGHT_OF_ITEM])))});
        }
        gainOfItems.sort((double[] o1, double[] o2) ->
                o2[1] - o1[1] < 0 ? -1 : o2[1] > 0 ? 1 : 0);

        return gainOfItems;
    }

    private static void countFitnessWage(Individual individual) {
        boolean[] packingPlan = individual.getPackingPlan();
        int totalWage = 0;
        for(int i = 0; i < packingPlan.length; i++) {
            if(packingPlan[i]) {
                totalWage += items[i][DataProvider.PROFIT_FROM_ITEM];
            }
        }
        individual.setFitnessWage(totalWage);
    }

    private static void countFitnessTime(Individual individual) {
        short[] route = individual.getRoute();
        boolean[] packingPlan = individual.getPackingPlan();
        double weight = 0;
        double time = 0;
        for(int currentPosition = 0; currentPosition < route.length - 1; ) {
            Integer[] currentCity = groupedItems[currentPosition];
            for (Integer item : currentCity) {
                if (packingPlan[item]) {//taken
                    weight += items[item][DataProvider.WEIGHT_OF_ITEM];
                }
            }
            time += countTime(route, currentPosition, ++currentPosition, countSpeed(weight));
        }
        individual.setFitnessTime(time);
    }

    /**
     * counts time from given city to the end assuming that there are no other things picked
     * @param route order of cities
     * @param startIndex index from which time is counted
     * @param currentSpeed speed of thief in current point of packed backpack
     * @return time from given point to the end of given route
     */
    private static double countTime(short[] route, int startIndex, double currentSpeed) {
        int endIndex = route.length - 1;
        return countTime(route, startIndex, endIndex, currentSpeed);
    }

    /**
     * counts time from one to another given city assuming that there are no other things picked
     * @param route order of cities
     * @param startIndex index from which time is counted
     * @param currentSpeed speed of thief in current point of packed backpack
     * @return time from given point to another
     */
    private static double countTime(short[] route, int startIndex, int endIndex, double currentSpeed) {
        return countRoad(route, startIndex, endIndex) / currentSpeed;
    }

    /**
     * counts distance from one point to another
     * @param route order of visited cities
     * @param startIndex index of start city
     * @param endIndex index of end city
     * @return sum of distances between visited city on route
     */
    private static double countRoad(short[] route, int startIndex, int endIndex) {
        double completeDistance = 0;
        if(endIndex == route.length - 1) {
            for(int i = startIndex; i < endIndex - 1; ) {
                int cityA = route[i];
                int cityB = route[++i];
                completeDistance += distances[cityA][cityB];
            }
            completeDistance += distances[route[route.length - 2]][route[0]];
        }
        else {
            for(int i = startIndex; i < endIndex; i++) {
                completeDistance += distances[route[i]][route[i + 1]];
            }
        }

        return completeDistance;
    }

    /**
     * coefficient is a constant value for the whole problem
     * This function counts current value of speed with given weight of backpack
     * @param currentWeight sum of thing's weight packed currently in backpack
     * @return current speed
     */
    private static double countSpeed(double currentWeight) {
        return maxSpeed - (currentWeight * coefficient);
    }

    /**
     * creates array of arrays where first index is an index of city, and an array inside is a group of items in this city
     * @return grouped items in cities
     */
    private static Integer[][] createGroupedItemsArray() {
        groupedItems = new Integer[dimension][];
        ArrayList<Integer>[] groupedItemsList = new ArrayList[dimension];
        for(int i = 0; i < dimension; i++) {
            groupedItemsList[i] = new ArrayList<>();
        }
        for (int[] item : items) {
            groupedItemsList[item[DataProvider.CITY_OF_ITEM] - 1].add(item[DataProvider.INDEX_OF_ITEM] - 1);
        }
        for(int i = 0; i < dimension; i++) {
            groupedItems[i] = new Integer[groupedItemsList[i].size()];
            for (int j = 0; j < groupedItemsList[i].size(); j++) {
                groupedItems[i][j] = groupedItemsList[i].get(j);
            }
        }
        return groupedItems;
    }
}
