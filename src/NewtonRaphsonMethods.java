import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.*;

public class NewtonRaphsonMethods implements NewtonRaphsonInterface{

    @Override
    public Vector<Double> stringToDblVector(String coefficients) {
        String[] abc = coefficients.split(" "); //whitespaces can be denoted by \\s+

        Vector<Double> coeffVec = new Vector<Double>();

        for(String strCoeff : abc){
            double intCoeff = Double.parseDouble(strCoeff);
            coeffVec.addElement(intCoeff);
        }

        System.out.println("Inputted Vector: " + coeffVec);
        return coeffVec;
    }

    @Override
    public void printIterationData(double[] iterData) {
        int index=0;
        for(double column : iterData){
            if(index != 2) {
                if(iterData[0] == 0) {
                    switch(index){
                        case 0:
                            System.out.printf("%-8.0f",column);
                            break;
                        case 1:
                            System.out.printf("%-12s",column);
                            break;
                        default:
                            System.out.printf("%-12s","-");
                            break;
                    } 
                } else {
                    if(index == 0) System.out.printf("%-8.0f",column);
                    else System.out.printf("%-12s",column);
                }
            }
            index++;
        }
        System.out.println();
        return;
    }

    @Override   //returns: [truncated function sum,truncated derived function sum]
    public double createFunction(Vector<Double> coefficients, int vectorSize, double value) {
        double sum = 0;
        for(int x=vectorSize-1;x>=0;x--){
            double term = coefficients.get(x)*Math.pow(value,x);
            sum = sum + term;
        }
        DecimalFormat df = new DecimalFormat("0.00000");
        if(sum > 0) df.setRoundingMode(RoundingMode.FLOOR);
        else df.setRoundingMode(RoundingMode.CEILING);
        double truncatedSum = Double.parseDouble(df.format(sum));
        return truncatedSum;
    }

    @Override
    public double deriveFunction(Vector<Double> coefficients, int vectorSize, double value) {
        double sum = 0, term = 0;
        for(int x=vectorSize-1;x>=0;x--){
            if(x==0) term = 0;
            else term = x*coefficients.get(x)*Math.pow(value,(x-1));
            sum = sum + term;
        }
        // System.out.println("DERIVATIVE = " + sum);
        // System.out.println();
        DecimalFormat df = new DecimalFormat("0.00000");
        if(sum > 0) df.setRoundingMode(RoundingMode.FLOOR);
        else df.setRoundingMode(RoundingMode.CEILING);
        double truncatedSum = Double.parseDouble(df.format(sum));
        return truncatedSum;
    }

    @Override
    public double getXnext(double xCurr, double fxCurr, double dfxCurr) {
        double xNext;
        if(fxCurr == 0 || dfxCurr == 0) xNext = xCurr;
        else xNext = xCurr - (fxCurr/dfxCurr);
        DecimalFormat df = new DecimalFormat("0.00000");
        if(xNext > 0) df.setRoundingMode(RoundingMode.FLOOR);
        else df.setRoundingMode(RoundingMode.CEILING);
        double truncatedXnext = Double.parseDouble(df.format(xNext));
        return truncatedXnext;
    }

    @Override
    public double getE(double xNext, double xCurr) {
        double e = Math.abs(((xNext - xCurr)/xNext) * 100);
        DecimalFormat df = new DecimalFormat("0.00000");
        df.setRoundingMode(RoundingMode.FLOOR);
        double truncatedE = Double.parseDouble(df.format(e));
        return truncatedE;
    }
}
