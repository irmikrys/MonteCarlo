package app;

import javafx.scene.control.Button;

/**
 * Created on 04.05.2017.
 */
public class ButtonFields extends Button {
    private int counter = 0;

    public int getCounter(){
        return counter;
    }
    public int setCounter(int val) {
        if(val >= 4) {
            val = 0;
        }
        counter = val;
        return counter;
    }
}
