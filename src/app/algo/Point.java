package app.algo;

import java.util.ArrayList;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Created on 13.05.2017.
 */
public class Point implements Comparable<Point> {

    ArrayList<Double> coordinates;
    double objFunctionValue;

    public Point(int size) {
        super();
        this.coordinates = new ArrayList<>(size);
    }

    public Point(ArrayList<Double> coordinates) {
        super();
        this.coordinates = coordinates;
    }

    //zwraca neighborsNum sasiadow w promieniu radius od punktu razem z tym punktem
    public ArrayList<Point> getNeighbors(int neighborsNum, double radius) {
        ArrayList<Point> neighbors = new ArrayList<>(neighborsNum + 1);
        for(int i = 0; i < neighborsNum; i++) {
            Point point = new Point(coordinates.size());
            for(int j = 0; j < coordinates.size(); j++) {
                double minTmp = coordinates.get(j) - radius;
                double maxTmp = coordinates.get(j) + radius;
                point.coordinates.set(j, ThreadLocalRandom.current().nextDouble(minTmp, maxTmp));
            }
            neighbors.add(point);
        }
        neighbors.add(this);
        return neighbors;
    }

    /////////////////////////////////////////////

    @Override
    public int compareTo(Point comparePoint) {
        return new Double(this.objFunctionValue).compareTo(comparePoint.objFunctionValue);
    }

}
