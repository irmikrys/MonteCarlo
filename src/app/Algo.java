package app;

import org.mariuszgromada.math.mxparser.Function;

import java.util.ArrayList;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Created on 29.04.2017.
 */
public class Algo {
    public static double epsilon;
    public static Function targetFcn;
    public static String maxMinTarget;
    public static ArrayList<LimitField> limits;

    private static ArrayList<ArrayList<Double>> randomArrs = new ArrayList<>();

    private static final double MIN = Double.MIN_VALUE;
    private static final double MAX = Double.MAX_VALUE;
    private static final double POINTS_NUM = 1000;

    //n = limits.size
    private static ArrayList<Double> genRandomXs(int n) {
        ArrayList<Double> randomArray = new ArrayList<>(n);
        for(int i = 0; i < n; i++){
            randomArray.set(i, ThreadLocalRandom.current().nextDouble(MIN, MAX));
        }
        return randomArray;
    }

    //randomArrsNum = POINTS_NUM, n = limits.size
    private static void fillWithRandomArrs(int pointsNum, int n) {
        for(int i = 0; i < pointsNum; i++){
            randomArrs.add(genRandomXs(n));
        }
    }

    //dla kazdego punktu tak bedzie sprawdzane
    private static boolean checkLimits(int limitsNum, int pointsNum) {
        for(int i = 0; i < limitsNum; i++){
            //check if condition is ok
            //if not ok any, false
        }
        return true;
    }
}
