package app;

import org.mariuszgromada.math.mxparser.Function;

import java.util.ArrayList;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Created on 29.04.2017.
 */
public class Algo {

    private static final double MIN = Double.MIN_VALUE;
    private static final double MAX = Double.MAX_VALUE;
    private static final double POINTS_NUM = 1000;

    public static double epsilon;
    public static Function targetFcn;
    public static String maxMinTarget;
    public static ArrayList<LimitField> limits;
    public static ArrayList<String> decisionVars = new ArrayList<>();

    private static ArrayList<ArrayList<Double>> randomArrs = new ArrayList<>();

    /**
     * Generate as many coordinates as number of decision variables
     * @param n - number of coordinates
     * @return - new array filled with random coordinates
     */
    private static ArrayList<Double> genRandomXs(int n) {
        ArrayList<Double> randomArray = new ArrayList<>(n);
        for(int i = 0; i < n; i++){
            randomArray.set(i, ThreadLocalRandom.current().nextDouble(MIN, MAX));
        }
        return randomArray;
    }

    /**
     * Fill randomArrs class variable array with random points coordinates
     * @param pointsNum - number of points to generate
     * @param n - number of coordinates (here: decision variables)
     */
    private static void fillWithRandomArrs(int pointsNum, int n) {
        randomArrs.clear();
        for(int i = 0; i < pointsNum; i++){
            randomArrs.add(genRandomXs(n));
        }
    }


    /**
     * Checks if all limits are satisfied
     * @param limitsNum - number of limits (limits.size)
     * @return - true if all limits are satisfied, false if any is not
     */
    private static boolean checkLimits(int limitsNum) {
        for(int i = 0; i < limitsNum; i++){
            //check if condition is ok
            //if not ok any, false
        }
        return true;
    }
}
