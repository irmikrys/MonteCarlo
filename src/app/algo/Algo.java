package app.algo;

import org.mariuszgromada.math.mxparser.Function;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import static java.lang.Math.abs;
import static java.util.Collections.sort;

/**
 * Created on 29.04.2017.
 */
public class Algo {

    public static final int POINTS_NUM = 1000;
    public static final double SCALE = 0.8;
    private static final int STARTING_POINTS = 16;

    public static double epsilon;
    public static Function targetFcn;
    public static String maxMinTarget;
    public static ArrayList<LimitField> limits = new ArrayList<>();
    public static ArrayList<DecisionVar> decisionVars = new ArrayList<>();
    public static ArrayList<MonteCarloBranch> threadPool = new ArrayList<>(STARTING_POINTS);
    public static List<Point> bestPoints = Collections.synchronizedList(new ArrayList<Point>());

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
        //System.out.println("\nChecking constraints...");

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
    public static Point monteCarlo(int pointsNum) {
        System.out.println("\n=========================\nStarting Monte Carlo counting...\n");

        double MIN, MAX;
        ArrayList<Point> setOfPoints = new ArrayList<>(pointsNum);
        ArrayList<Double> coordinates = new ArrayList<>();

        //znajdz pointsNum punktow na poczatek spelniajacych ograniczenia i dodaj do zbioru wspolrzednych
        boolean max = false;
        max = maxMinTarget.equals("maximize");
        int magnitude = getStartingMagnitude(max);
        for(int i = 0; i < pointsNum; i++) {
            Point satisfyingPoint = getSatisfyingPoint(decisionVars.size(), magnitude);
            coordinates.addAll(satisfyingPoint.coordinates);
            setOfPoints.add(i, satisfyingPoint);
            //System.out.println("\t\t" + setOfPoints.get(i).toString());
        }

        //ustal poczatkowy promien
        sort(coordinates);
        MIN = coordinates.get(0);
        MAX = coordinates.get(coordinates.size() - 1);
        double radius = abs((MAX - MIN) * 0.5);
        //System.out.println(">>>>>>> RADIUS: "+radius);

        for(int i = 0; i < STARTING_POINTS; ++i) {
            threadPool.add(new MonteCarloBranch(pointsNum, radius, epsilon, bestPoints, setOfPoints.get(i), targetFcn));
            threadPool.get(i).start();
        }

        for(int i = 0; i < STARTING_POINTS; ++i) {
            try {
                threadPool.get(i).join();
                System.out.println("thread " + i + " joined");
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        /*Point bestPoint;
        //szukaj kolejnych punktów, dopoki promien kostki wiekszy od zadanego epsilon
         do {
             bestPoint = new Point();
            //wybierz pewna ilosc punktow, ktore maja odpowiednio najmniejsza/najwieksza wartosc funkcji celu
            for (Point tmpPoint : setOfPoints) {
                double[] coords = new double[tmpPoint.coordinates.size()];
                for (int j = 0; j < tmpPoint.coordinates.size(); j++) {
                    coords[j] = tmpPoint.coordinates.get(j);
                }
                tmpPoint.objFunctionValue = targetFcn.calculate(coords);
            }
            sort(setOfPoints);
            //System.out.println(setOfPoints.toString());
            if(Algo.maxMinTarget.equals("maximize")) {
                //System.out.println("======MAXIMIZE=======");
                Collections.reverse(setOfPoints);
            }
            else {
                //System.out.println("======MINIMIZE=======");
            }
            bestPoint = setOfPoints.get(0);

            //dla kazdego z tych punktow znajdz pointsNum sasiadow
            setOfPoints = bestPoint.getNeighbors(POINTS_NUM, radius);

            radius = radius * SCALE;
             //System.out.println(">>>>>>> RADIUS: "+RADIUS);

        } while(radius > epsilon); */

        double bestVal;
        int bestIndex;
        if(maxMinTarget.equals("maximize")) {
            bestVal = 0;
            bestIndex = 0;
            for (int i = 0; i < STARTING_POINTS; ++i) {
                if (bestPoints.get(i).objFunctionValue > bestVal) {
                    bestVal = bestPoints.get(i).objFunctionValue;
                    bestIndex = i;
                }
            }
        }
        else{
            bestVal = Double.MAX_VALUE;
            bestIndex = 0;
            for (int i = 0; i < STARTING_POINTS; ++i) {
                if (bestPoints.get(i).objFunctionValue > bestVal) {
                    bestVal = bestPoints.get(i).objFunctionValue;
                    bestIndex = i;
                }
            }
        }
        setDecVarVals(bestPoints.get(bestIndex).coordinates);
        System.out.println("\n================ RESULT ================\n");
        for (DecisionVar decisionVar : decisionVars) {
            System.out.println(decisionVar.toString());
        }
        return bestPoints.get(bestIndex);
    }

    //////////////////////////////////////

    private static Point getSatisfyingPoint(int dimension, int magnitude){
        //System.out.println("\n\n======TRYING TO GET SATISFYING POINT...");
        Point point = new Point();
        point.coordinates = new ArrayList<>(dimension);
        for(int i = 0; i < dimension; i++){
            point.coordinates.add(i, 0.0);
        }
        Random random = new Random();
        double[] tmpCoords = new double[dimension];
        while(true){
            for(int iter = magnitude; iter >= 1; --iter) {
                for (int i = 0; i < dimension; i++) {
                    tmpCoords[i] = random.nextDouble() * Math.pow(10, iter);
                    point.coordinates.set(i, tmpCoords[i]);
                }
                if (checkConstraints(limits.size(), point)) {
                    //System.out.println("====Satisfying " + point.toString());
                    return point;
                }
            }
        }
    }

    private static int getStartingMagnitude(boolean maximize) {
        int val = 1;
        for(LimitField lf : limits) {
            if (lf.value > val) val = (int) ((double) lf.value);
        }
        int magnitude = 2;
        while(val > 1) {
            val /= 10;
            ++magnitude;
        }
        return magnitude;
    }

    private static void clearDecVarVals() {
        //System.out.println("\tClearing decision variables values...");
        for (DecisionVar dv : decisionVars) {
            dv.value = 0;
        }
    }

    private static void setDecVarVals(ArrayList<Double> coordinates) {
        //System.out.println("\tSetting coordinates to decision variables...");
        for(int i = 0; i < decisionVars.size(); i++) {
            decisionVars.get(i).value = coordinates.get(i);
        }
    }
}

///////////////////////////////////////////////

class MonteCarloBranch extends Thread {
    ArrayList<Point> setOfPoints;
    List<Point> bestPoints;
    Function targetFcn;
    double radius;
    double epsilon;

    MonteCarloBranch(int pointsNum, double radius, double epsilon, List<Point> bestPoints, Point startingPoint, Function targetFcn) {
        this.setOfPoints = new ArrayList<>(pointsNum);
        Point cpPoint = new Point();
        for(int j = 0; j < startingPoint.coordinates.size(); ++j) {
            cpPoint.coordinates.add(startingPoint.coordinates.get(j));
        }
        this.targetFcn = new Function(targetFcn);
        this.setOfPoints.add(cpPoint);
        this.bestPoints = bestPoints;
        this.radius = radius;
        this.epsilon = epsilon;
    }

    @Override
    public void run() {
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
            //System.out.println(setOfPoints.toString());
            if(Algo.maxMinTarget.equals("maximize")) {
                //System.out.println("======MAXIMIZE=======");
                Collections.reverse(setOfPoints);
            }
            else {
                //System.out.println("======MINIMIZE=======");
            }
            bestPoint = setOfPoints.get(0);

            //dla kazdego z tych punktow znajdz pointsNum sasiadow
            synchronized (Algo.class) {
                setOfPoints = bestPoint.getNeighbors(Algo.POINTS_NUM, radius);
            }

            radius = radius * Algo.SCALE;
            //System.out.println(">>>>>>> RADIUS: "+RADIUS);

        } while(radius > epsilon);

        System.out.println("returning from thread");
        bestPoints.add(bestPoint);
    }
}