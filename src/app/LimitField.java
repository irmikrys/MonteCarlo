package app;

import org.mariuszgromada.math.mxparser.Function;

import java.util.ArrayList;

/**
 * Created on 05.05.2017.
 */
public class LimitField {
    public Function fcn;
    public String sign;
    public Double val;
    public ArrayList<String> vars = new ArrayList<>();

    public LimitField(){

    }

    public LimitField(Function fcn, String sign, Double val, ArrayList<String> vars) {
        this.fcn = fcn;
        this.sign = sign;
        this.val = val;
        this.vars = vars;
    }

    public String toString(){
        return "\nFunction: " + this.fcn.getFunctionExpressionString() +
                ", Sign : " + this.sign + ", Value: " +
                this.val + ", DecVars: "+ vars.toString();
    }
}
