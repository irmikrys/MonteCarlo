package app;

import org.mariuszgromada.math.mxparser.Function;

import java.util.ArrayList;

/**
 * Created on 05.05.2017.
 */
public class LimitField {
    public Function function;
    public String sign;
    public Double value;
    public ArrayList<String> vars = new ArrayList<>();

    public LimitField(){

    }

    public LimitField(Function function, String sign, Double value, ArrayList<String> vars) {
        this.function = function;
        this.sign = sign;
        this.value = value;
        this.vars = vars;
    }

    public String toString(){
        return "Expression: " + this.function.getFunctionExpressionString() +
                ", Sign : " + this.sign + ", Value: " +
                this.value + ", DecVars: "+ vars.toString();
    }
}
