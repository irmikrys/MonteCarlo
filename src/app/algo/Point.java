package app.algo;

import java.util.ArrayList;

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
}
