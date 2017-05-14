package app.algo;

/**
 * Created on 13.05.2017.
 */
public class DecisionVar {
    public String name;
    public double value;

    public DecisionVar(String name) {
        this.name = name;
    }

    public DecisionVar(String  name, int value) {
        this.name = name;
        this.value = value;
    }

    @Override
    public String toString() {
        return "Decision Variable: " + name + ", value: " + value;
    }

}
