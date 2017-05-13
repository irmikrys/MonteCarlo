package app.algo;

import org.mariuszgromada.math.mxparser.Function;

import java.util.ArrayList;
import java.util.Random;

/**
 * Created on 29.04.2017.
 */
public class Algo {

    private static double MIN = Double.MIN_VALUE;
    private static double MAX = Double.MAX_VALUE;
    public static final int POINTS_NUM = 1000;

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
    private static boolean checkConstraints(int limitsNum, ArrayList<Double> coordinates) {
        System.out.println("\nChecking limits...");

        clearDecVarVals();
        setDecVarVals(coordinates);

        for(int i = 0; i < limitsNum; i++) {
            LimitField lfTmp = limits.get(i);
            System.out.println("\t" + lfTmp.toString());
            int sizeTmp = lfTmp.vars.size();
            double[] argsTmp = new double[sizeTmp];
            //znajdz argumenty po nazwach w decisionVars i stworz tablice z kolejnymi wartosciami
            for(int arg = 0; arg < sizeTmp; arg++) {
                for (DecisionVar decisionVar : decisionVars) {
                    if (decisionVar.name.equals(lfTmp.vars.get(arg))) {
                        argsTmp[arg] = decisionVar.value;
                        System.out.println("\t\tValue of " + decisionVar.name + " set to: " + argsTmp[arg]);
                    }
                }
            }
            //oblicz wartosc funkcji dla danego limitu
            double v = lfTmp.function.calculate(argsTmp);
            System.out.println("\tCalculated value: " + v);
            //sprawdz jaki znak jest w tym polu i porownaj wartosc funkcji z wartoscia tfVal
            switch(lfTmp.sign){
                case "<":
                    System.out.println("\t\tSign: <");
                    if(!(v < lfTmp.value)){
                        System.out.println("\t\tExpression: " + v + " < " + lfTmp.value + " is false." );
                        return false;
                    }
                    break;
                case "<=":
                    System.out.println("\t\tSign: <=");
                    if(!(v <= lfTmp.value)){
                        System.out.println("\t\tExpression: " + v + " <= " + lfTmp.value + " is false." );
                        return false;
                    }
                    break;
                case ">":
                    System.out.println("\t\tSign: >");
                    if(!(v > lfTmp.value)){
                        System.out.println("\t\tExpression: " + v + " > " + lfTmp.value + " is false." );
                        return false;
                    }
                    break;
                case ">=":
                    System.out.println("\t\tSign: >=");
                    if(!(v >= lfTmp.value)){
                        System.out.println("\t\tExpression: " + v + " >= " + lfTmp.value + " is false." );
                        return false;
                    }
                    break;
            }
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

        RandomArray randomArr = new RandomArray();
        ArrayList<ArrayList<Double>> firstSetOfPoints = new ArrayList<>(POINTS_NUM);
        ArrayList<ArrayList<Double>> setOfPoints = new ArrayList<>(POINTS_NUM);


        //MIN = najmniejsza wspolrzedna sposrod spelniajacyh punktow
        //MAX = najwieksza wspolrzedna sposrod spelniajacych punktow

        randomArr.fillWithRandomArrs(pointsNum, decisionVars.size(), MIN, MAX);
        for(int i = 0; i < pointsNum; i++) {
            checkConstraints(limits.size(), randomArr.get(i));
        }
    }

    //////////////////////////////////////
    private ArrayList<Double> getSatisfyingPoint(int dimension){
        ArrayList<Double> coordinates = new ArrayList<>(dimension);
        Random random = new Random();
        while(true){
            for(int i = 0; i < dimension; i++){
                coordinates.set(i, random.nextDouble());
            }
            if(checkConstraints(limits.size(), coordinates)){
                return coordinates;
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
