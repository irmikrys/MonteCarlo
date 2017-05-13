package app.algo;

import java.util.ArrayList;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Created on 13.05.2017.
 */
public class RandomArray extends ArrayList<ArrayList<Double>> {

    /**
     * Generate as many coordinates as number of decision variables
     * @param n - number of coordinates
     * @return - new array filled with random coordinates
     */
    private ArrayList<Double> genRandomXs(int n, final double MIN, final double MAX) {
        //System.out.println("Generating random coordinates...");
        ArrayList<Double> randomArray = new ArrayList<>();
        for(int i = 0; i < n; i++){
            randomArray.add(i, ThreadLocalRandom.current().nextDouble(MIN, MAX));
        }
        return randomArray;
    }

    /**
     * Fill array with random points coordinates
     * @param pointsNum - number of points to generate
     * @param n - number of coordinates (here: decision variables)
     */
    public void fillWithRandomArrs(int pointsNum, int n, final double MIN, final double MAX) {
        //System.out.println("Filling with random arrays...");
        this.clear();
        for(int i = 0; i < pointsNum; i++){
            this.add(genRandomXs(n, MIN, MAX));
        }
    }
}
