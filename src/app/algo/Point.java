package app.algo;

import java.util.ArrayList;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Created on 13.05.2017.
 */
public class Point {
    ArrayList<Double> coordinates;

    public Point(int size) {
        this.coordinates = new ArrayList<>(size);
    }

    public Point(ArrayList<Double> coordinates) {
        this. coordinates = coordinates;
    }

    //zwraca neighborsNum sasiadow w promieniu radius od punktu
    public ArrayList<Point> getNeighbors(int neighborsNum, double radius) {
        ArrayList<Point> neighbors = new ArrayList<>(neighborsNum);
        for(int i = 0; i < neighborsNum; i++) {
            Point point = new Point(coordinates.size());
            for(int j = 0; j < coordinates.size(); j++) {
                double minTmp = coordinates.get(j) - radius;
                double maxTmp = coordinates.get(j) + radius;
                point.coordinates.set(j, ThreadLocalRandom.current().nextDouble(minTmp, maxTmp));
            }
            neighbors.add(point);
        }
        return neighbors;
    }
}
