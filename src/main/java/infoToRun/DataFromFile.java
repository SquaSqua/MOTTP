package infoToRun;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
/**
 * contains all data from given file
 * dimension, capacity, min/max speed, renting ratio
 * calculates nadir and ideal point, adjusts items and distances as an array
 */
public class DataFromFile {

    private static int dimension;
    private static int capacity;
    private static double minSpeed;
    private static double maxSpeed;
    private static double rentingRatio;

    private static double[][] distances;
    private static int[][] items;
    private static Point ideal, nadir;

//    getters
    public static int getCapacity() {
        return capacity;
    }
    public static int getDimension() {
        return dimension;
    }
    public static int[][] getItems() {
        return items;
    }

    public static double getMinSpeed() {
        return minSpeed;
    }
    public static double getMaxSpeed() {
        return maxSpeed;
    }

    public static double[][] getDistances() {
        return distances;
    }

    public static Point getIdeal() {
        return ideal;
    }
    public static Point getNadir() {
        return nadir;
    }


    //setters
    public static void setDimension(int dimension) { DataFromFile.dimension = dimension; }
    public static void setCapacity(int capacity) {
        DataFromFile.capacity = capacity;
    }
    public static void setItems(int[][] items) {
        DataFromFile.items = items;
    }

    static void setMinSpeed(double minSpeed) {
        DataFromFile.minSpeed = minSpeed;
    }
    static void setMaxSpeed(double maxSpeed) {
        DataFromFile.maxSpeed = maxSpeed;
    }
    static void setRentingRatio(double rentingRatio) {
        DataFromFile.rentingRatio = rentingRatio;
    }
    static void setDistances(double[][] distances) {
        DataFromFile.distances = distances;
    }
    static void setIdeal(Point ideal) {
        DataFromFile.ideal = ideal;
    }
    static void setNadir(Point nadir) {
        DataFromFile.nadir = nadir;
    }

}
