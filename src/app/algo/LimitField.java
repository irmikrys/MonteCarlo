package app.algo;

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

    public boolean checkConstraint(ArrayList<DecisionVar> decVars) {
        System.out.println("\t" + this.toString());

        int sizeTmp = this.vars.size();
        double[] argsTmp = new double[sizeTmp];

        //znajdz argumenty po nazwach w decisionVars i stworz tablice z kolejnymi wartosciami
        for(int arg = 0; arg < sizeTmp; arg++) {
            for (DecisionVar decisionVar : decVars) {
                if (decisionVar.name.equals(this.vars.get(arg))) {
                    argsTmp[arg] = decisionVar.value;
                    System.out.println("\t\tValue of " + decisionVar.name + " set to: " + argsTmp[arg]);
                }
            }
        }

        //oblicz wartosc funkcji dla danego limitu
        double v = this.function.calculate(argsTmp);
        System.out.println("\tCalculated value: " + v);

        //sprawdz jaki znak jest w tym polu i porownaj wartosc funkcji z wartoscia tfVal
        switch(this.sign){
            case "<":
                System.out.println("\t\tSign: <");
                if(!(v < this.value)){
                    System.out.println("\t\tExpression: " + v + " < " + this.value + " is false." );
                    return false;
                }
                break;
            case "<=":
                System.out.println("\t\tSign: <=");
                if(!(v <= this.value)){
                    System.out.println("\t\tExpression: " + v + " <= " + this.value + " is false." );
                    return false;
                }
                break;
            case ">":
                System.out.println("\t\tSign: >");
                if(!(v > this.value)){
                    System.out.println("\t\tExpression: " + v + " > " + this.value + " is false." );
                    return false;
                }
                break;
            case ">=":
                System.out.println("\t\tSign: >=");
                if(!(v >= this.value)){
                    System.out.println("\t\tExpression: " + v + " >= " + this.value + " is false." );
                    return false;
                }
                break;
        }
        return true;
    }
}
