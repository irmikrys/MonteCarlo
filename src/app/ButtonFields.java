package app;

import javafx.scene.control.Button;

/**
 * Created on 04.05.2017.
 */
public class ButtonFields extends Button {
    private int counter;

    public int setCounter(int val) {
        if(val >= 4) {
            val = 0;
        }
        return val;
    }
}
