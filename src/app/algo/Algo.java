package app.algo;

import org.mariuszgromada.math.mxparser.Function;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

import static java.util.Collections.sort;

/**
 * Created on 29.04.2017.
 */
public class Algo {

    public static final int POINTS_NUM = 10;
    private static final double SCALE = 0.8;
    private static final int SATISFYING_POINTS_NUM = 5;

    public static double epsilon;
    public static Function targetFcn;
    public static String maxMinTarget;
    public static ArrayList<LimitField> limits = new ArrayList<>();
    public static ArrayList<DecisionVar> decisionVars = new ArrayList<>();

    /////////////////////////////////////////////////

    public static boolean containsDecVar(String name) {
        for(DecisionVar dv : decisionVars) {
            if(dv.name.equals(name)){
                return true;
            }
        }
        return false;
    }

    /////////////////////////////////////////////////

    /**
     * Checks if all limits are satisfied by one range of coordinates
     * @param limitsNum - number of limits (limits.size)
     * @return - true if all limits are satisfied, false if any is not
     */
    public static boolean checkConstraints(int limitsNum, Point point) {
        System.out.println("\nChecking constraints...");

        boolean satisfies;

        clearDecVarVals();
        setDecVarVals(point.coordinates);

        for(int i = 0; i < limitsNum; i++) {
            LimitField lfTmp = limits.get(i);
            satisfies = lfTmp.checkConstraint(decisionVars);
            if(!satisfies) return false;
        }
        return true;
    }

    /**
     * Generate pointsNum points each time loop runs and check if they satisfy all conditions,
     * if they are - generate same number of points around the points that satisfy,
     * if not - do nothing about them
     * @param pointsNum - number of points to random generation each time
     */
    public static void monteCarlo(int pointsNum) {
        System.out.println("\n=========================\nStarting Monte Carlo counting...\n");

        double MIN, MAX;
        RandomArray randomArr = new RandomArray();
        ArrayList<Point> setOfPoints = new ArrayList<>(pointsNum);
        ArrayList<Double> coordinates = new ArrayList<>();

        //znajdz pointsNum punktow na poczatek spelniajacych ograniczenia i dodaj do zbioru wspolrzednych
        for(int i = 0; i < pointsNum; i++) {
            Point satisfyingPoint = getSatisfyingPoint(decisionVars.size());
            coordinates.addAll(satisfyingPoint.coordinates);
            setOfPoints.set(i, satisfyingPoint);
            System.out.println("\t\t" + setOfPoints.get(i).toString());
        }

        //ustal poczatkowy promien
        sort(coordinates);
        MIN = coordinates.get(0);
        MAX = coordinates.get(coordinates.size() - 1);
        double RADIUS = (MAX - MIN) * 0.5;


        Point bestPoint;
        //szukaj kolejnych punktów, dopoki promien kostki wiekszy od zadanego epsilon
         do {
            //wybierz pewna ilosc punktow, ktore maja odpowiednio najmniejsza/najwieksza wartosc funkcji celu
            for (Point tmpPoint : setOfPoints) {
                double[] coords = new double[tmpPoint.coordinates.size()];
                for (int j = 0; j < tmpPoint.coordinates.size(); j++) {
                    coords[j] = tmpPoint.coordinates.get(j);
                }
                tmpPoint.objFunctionValue = targetFcn.calculate(coords);
            }
            sort(setOfPoints);
            System.out.println(setOfPoints.toString());
            if(Algo.maxMinTarget.equals("maximize")) { //FIXME
                Collections.reverse(setOfPoints);
            }
            bestPoint = setOfPoints.get(0);

            //dla kazdego z tych punktow znajdz pointsNum sasiadow
            setOfPoints = bestPoint.getNeighbors(POINTS_NUM, RADIUS);

            RADIUS = RADIUS * SCALE;

        } while(RADIUS > epsilon);

        setDecVarVals(bestPoint.coordinates);
        System.out.println("\n================ RESULT ================\n");
        for(int i = 0; i < decisionVars.size(); i++) {
            System.out.println(decisionVars.toString());
        }
    }

    //////////////////////////////////////

    private static Point getSatisfyingPoint(int dimension){
        Point point = new Point(dimension);
        Random random = new Random();
        while(true){
            for(int i = 0; i < dimension; i++){
                point.coordinates.set(i, random.nextDouble());
            }
            if(checkConstraints(limits.size(), point)){
                return point;
            }
        }
    }

    private static Point getSatisfyingPoint(int dimension, final double MIN, final double MAX){
        Point point = new Point(dimension);
        while(true){
            for(int i = 0; i < dimension; i++){
                point.coordinates.set(i, ThreadLocalRandom.current().nextDouble(MIN, MAX));
            }
            if(checkConstraints(limits.size(), point)){
                return point;
            }
        }
    }

    private static void clearDecVarVals() {
        System.out.println("\tClearing decision variables values...");
        for (DecisionVar dv : decisionVars) {
            dv.value = 0;
        }
    }

    private static void setDecVarVals(ArrayList<Double> coordinates) {
        System.out.println("\tSetting random coordinates...");
        for(int i = 0; i < decisionVars.size(); i++) {
            decisionVars.get(i).value = coordinates.get(i);
        }
    }
}
