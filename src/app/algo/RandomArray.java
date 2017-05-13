package app.algo;

import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Created on 13.05.2017.
 */
public class RandomArray extends ArrayList<Point> {

    /**
     * Generate as many coordinates as number of decision variables
     * @param dimension - number of coordinates
     * @return - new array filled with random coordinates
     */
    public Point getRandomCoordinates(int dimension, final double MIN, final double MAX) {
        //System.out.println("Generating random coordinates...");
        ArrayList<Double> randomArray = new ArrayList<>();
        for(int i = 0; i < dimension; i++){
            randomArray.add(i, ThreadLocalRandom.current().nextDouble(MIN, MAX));
        }
        return new Point(randomArray);
    }

    public Point getRandomCoordinates(int dimension) {
        //System.out.println("Generating random coordinates...");
        ArrayList<Double> randomArray = new ArrayList<>();
        Random rand = new Random();
        for(int i = 0; i < dimension; i++){
            randomArray.add(i, rand.nextDouble());
        }
        return new Point(randomArray);
    }

    /**
     * Fill array with random points coordinates
     * @param pointsNum - number of points to generate
     * @param dimension - number of coordinates (here: decision variables)
     */
    public void fillWithRandomPoints(int pointsNum, int dimension, final double MIN, final double MAX) {
        //System.out.println("Filling with random arrays...");
        this.clear();
        for(int i = 0; i < pointsNum; i++){
            this.add(getRandomCoordinates(dimension, MIN, MAX));
        }
    }

    public void fillWithRandomPoints(int pointsNum, int dimension) {
        //System.out.println("Filling with random arrays...");
        this.clear();
        for(int i = 0; i < pointsNum; i++){
            this.add(getRandomCoordinates(dimension));
        }
    }
}
