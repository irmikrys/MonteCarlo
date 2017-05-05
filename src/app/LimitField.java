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

    LimitField(){

    }

    LimitField(Function fcn, String sign, Double val, ArrayList<String> vars) {
        this.fcn = fcn;
        this.sign = sign;
        this.val = val;
        this.vars = vars;
    }
}
