package generator;

import comparator.ObjectiveFrontComp;
import individual.Individual;
import infoToRun.DataFromFile;
import infoToRun.Point;

import java.util.ArrayList;

public class ParetoFrontsGenerator {

    private static Point ideal = DataFromFile.getIdeal();
    private static Point nadir = DataFromFile.getNadir();


    //each calling overrides last set paretoFronts
    private static ArrayList<ArrayList<Individual>> generateFronts(ArrayList<? extends Individual> population) {
        ArrayList<ArrayList<Individual>> paretoFronts = new ArrayList<>();
        paretoFronts.add(new ArrayList<>());
        for(int individualIndex = 0; individualIndex < population.size(); individualIndex++) {
            for (int frontIndex = 0; frontIndex < paretoFronts.size(); frontIndex++) {
                ArrayList<Individual> currentFront = paretoFronts.get(frontIndex);
                if (currentFront.size() == 0) {
                    currentFront.add(population.get(individualIndex));
                    break;
                } else {
                    for (int comparedIndividualIndex = 0; comparedIndividualIndex < currentFront.size(); comparedIndividualIndex++) {
                        int compared = population.get(individualIndex).compareTo(currentFront.get(comparedIndividualIndex));
                        if ((compared == 0) && (comparedIndividualIndex == currentFront.size() - 1)) {
                            currentFront.add(population.get(individualIndex));
                            frontIndex = paretoFronts.size();
                            break;
                        } else if (compared == 1) {//1 to idivFromPop wygrał z comparedInd
                            //zamiana miejsc
                            ArrayList<Individual> betterFront = new ArrayList<>();
                            betterFront.add(population.get(individualIndex));
                            for(int z = 0; z < comparedIndividualIndex; ) {
                                betterFront.add(currentFront.get(z));
                                currentFront.remove(z);
                                comparedIndividualIndex--;
                            }
                            for(int z = 1; z < currentFront.size(); z++) {
                                if(population.get(individualIndex).compareTo(currentFront.get(z)) == 0) {
                                    betterFront.add(currentFront.get(z));
                                    currentFront.remove(z);
                                    z--;
                                }
                            }
                            paretoFronts.add(frontIndex, betterFront);
                            ArrayList<ArrayList<Individual>> fixedPareto = new ArrayList<>();
                            for(int correctParetoIndex = 0; correctParetoIndex < frontIndex + 1; correctParetoIndex++) {
                                fixedPareto.add(paretoFronts.get(correctParetoIndex));
                            }
                            fixedPareto.addAll(fixFronts(paretoFronts, frontIndex));
                            paretoFronts = fixedPareto;
                            frontIndex = paretoFronts.size();
                            break;
                        } else if (compared == -1) {//jeśli przegrałem
                            //nowy gorszy front
                            if (paretoFronts.size() == frontIndex + 1) { //jeśli sprawdzany front jest ostatnim
                                paretoFronts.add(new ArrayList<>());
                                paretoFronts.get(frontIndex + 1).add(population.get(individualIndex));
                                frontIndex = paretoFronts.size();
                                break;
                            }
                            else {
                                break;
                            }
                        }
                    }
                }
            }
        }
        return paretoFronts;
    }

    private static ArrayList<ArrayList<Individual>> fixFronts(ArrayList<ArrayList<Individual>> paretoFronts, int j) {
        ArrayList<Individual> tempPopulation = new ArrayList<>();
        for(int i = j + 1; i < paretoFronts.size(); i++) {
            tempPopulation.addAll(paretoFronts.get(i));
        }
        return generateFronts(tempPopulation);
    }

    public static ArrayList<ArrayList<Individual>> generateFrontsWithAssignments(ArrayList<Individual> population) {
        ArrayList<ArrayList<Individual>> paretoFronts;
        paretoFronts = generateFronts(population);
        assignRank(paretoFronts);
        crowdingDistanceSetter(paretoFronts);

        return paretoFronts;
    }

    private static void assignRank(ArrayList<ArrayList<Individual>> paretoFronts) {
        for(int i = 0; i < paretoFronts.size(); i++) {
            for(int j = 0; j < paretoFronts.get(i).size(); j++) {
                paretoFronts.get(i).get(j).setRank(i);
            }
        }
    }

    private static void objectiveSorting(ArrayList<ArrayList<Individual>> paretoFronts) {
        for (ArrayList<Individual> paretoFront : paretoFronts) {
            paretoFront.sort(new ObjectiveFrontComp());
        }
    }

    private static void crowdingDistanceSetter(ArrayList<ArrayList<Individual>> paretoFronts) {
        objectiveSorting(paretoFronts);
        for (ArrayList<Individual> paretoFront : paretoFronts) {
            paretoFront.get(0).setCrowdingDistance(Double.POSITIVE_INFINITY);
            paretoFront.get(paretoFront.size() - 1).setCrowdingDistance(Double.POSITIVE_INFINITY);

            for (int j = 1; j < paretoFront.size() - 1; j++) {
                Individual currentInd = paretoFront.get(j);
                double a = Math.abs(paretoFront.get(j + 1).getFitnessTime()
                        - paretoFront.get(j - 1).getFitnessTime());
                double b = Math.abs(paretoFront.get(j + 1).getFitnessWage()
                        - paretoFront.get(j - 1).getFitnessWage());
                currentInd.setCrowdingDistance(a * b);
            }
        }
    }

    public static String ED_measure(ArrayList<ArrayList<Individual>> paretoFronts) {
        double sumED = 0;
        ArrayList<Individual> paretoFront = paretoFronts.get(0);
        paretoFront.sort(new ObjectiveFrontComp());
        for (Individual ind : paretoFront) {
            sumED += Math.sqrt((long)((ind.getFitnessTime() - ideal.x)
                    * (ind.getFitnessTime() - ideal.x) + (ind.getFitnessWage() - ideal.y)
                    * (ind.getFitnessWage() - ideal.y)));
        }

        return (sumED / paretoFront.size()) + "";
    }

    public static String PFS_measure(ArrayList<ArrayList<Individual>> paretoFronts) {
        return paretoFronts.get(0).size() + "";
    }

    public static double HV_measure(ArrayList<ArrayList<Individual>> paretoFronts) {
        ArrayList<Individual> paretoFront = paretoFronts.get(0);
        paretoFront.sort(new ObjectiveFrontComp());
        long hyperVolume = 0L;
        double lastY = nadir.y;
        for (Individual ind : paretoFront) {
            hyperVolume += ((int) ((nadir.x - ind.getFitnessTime())
                    * (lastY - ind.getFitnessWage())));
            lastY = ind.getFitnessWage();
        }
        return hyperVolume;
    }
}
