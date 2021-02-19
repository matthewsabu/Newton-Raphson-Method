import java.math.*;
import java.util.*;
//Created by: Matthew Simon M. Sabularse | ID 11813652
public class NewtonRaphson {

    public static void main(String[] args) {
        Scanner coefficients = new Scanner(System.in);
        Vector<Double> coeffVec = new Vector<Double>(); //[C,B,A]
        //initial conditions
        double iterNo=0,xi,fx,fxD,fxi,e;
        xi=fx=fxD=fxi=e=0;

        //set given assumptions
        double initx = 1;   //initial x0
        double[] stopCrit = {10,0.002,0.0001}; //[iteration,|error%|,|f(xi)|] *if error or f(xi) is not given, use -1.
        coeffVec = setVector(coefficients,coeffVec);    
        printGiven(coeffVec,initx,stopCrit);

        //set initial iteration row (Row #1)
        double[] iterData = {iterNo,initx,xi,fx,fxD,fxi,e};

        //perform algo and display table
        System.out.format("\n%-8s%-12s%-12s%-12s%-12s%-12s\n","iter #","x","f(x)","f'(x)","f(xi)","e %");
        runNewtonRaphsonAlgo(coeffVec,stopCrit,iterData);

        coefficients.close();
    }

    //user input
    public static Vector<Double> setVector(Scanner coefficients, Vector<Double> coeffVec){
        //define function
        System.out.println("Newton Raphson Method: Find your equation's root!");
        System.out.print("Polynomial Equation's coefficients ([C B A] Format): ");

        String[] abc = coefficients.nextLine().split(" "); //whitespaces can be denoted by \\s+        
        for(String strCoeff : abc){
            double intCoeff = Double.parseDouble(strCoeff);
            coeffVec.addElement(intCoeff);
        }
        return coeffVec;
    }

    public static void printGiven(Vector<Double> coeffVec, double initx, double[] stopCrit){        
        //display assumptions
        System.out.println("Inputted Vector: " + coeffVec);
        System.out.println("\nInitial Conditions:");
        System.out.println("x0 = " + initx);
        System.out.println("\nStopping Criteria:");
        System.out.println("Iterations = " + stopCrit[0]);
        System.out.println("Error % = " + new BigDecimal(Double.toString(stopCrit[1])));
        System.out.println("|f(xi)| = " + new BigDecimal(Double.toString(stopCrit[2])));
    }

    //everything with an "i" at the end indicates the value/s for the next (succeeding) iteration 
    //0=iteration,1=x,2=xi,3=fx,4=fxD,5=fxi,6=e (iterData index references)
    public static void runNewtonRaphsonAlgo(Vector<Double> coefficients, double[] stopCrit, double[] iterData) {
        printIterationData(iterData);   //print current iteration row
        
        double xCurr = iterData[1];
        double fx = createFunction(coefficients,coefficients.size(),xCurr); //f(x)
        double fxD = deriveFunction(coefficients,coefficients.size(),xCurr); //f(xD)
        double xNew = getXnext(xCurr,fx,fxD); //xi
        double fxi = createFunction(coefficients,coefficients.size(),xNew); //f(xi)
        double e = getE(xNew,xCurr); //e

        iterData[0]++;  //increase iteration counter

        //stopping criteria:
        // iteration !> stopping Iteration && |error%| !<= stopping Error% && |f(xi+1)| !<= stopping f(xi)
        if(!(iterData[0]>=stopCrit[0]) && !(Math.abs(e)<=stopCrit[1]) && !(Math.abs(fxi)<=stopCrit[2])){
            //update iteration row (Row #i)
            double[] newIterData = {iterData[0],xNew,xCurr,fx,fxD,fxi,e};

            runNewtonRaphsonAlgo(coefficients,stopCrit,newIterData);
        } else {
            double[] iterDatai = {iterData[0],xNew,xCurr,fx,fxD,fxi,e};
            printIterationData(iterDatai);   //print trailing iteration row
            
            System.out.println("\nA stopping criteria has been met--stopped iterating.");
            double approxRoot = xNew;
            System.out.println("\nThe equation's root is approximately " + approxRoot);
        }
        return;
    }

    //algorithm functions:
    public static void printIterationData(double[] iterData) {
        int index = 0;
        for(double column : iterData){
            if(index != 2) {
                if(iterData[0] == 0) {
                    switch(index){
                        case 0:
                            System.out.format("%-8.0f",column);
                            break;
                        case 1:
                            System.out.format("%-12s",column);
                            break;
                        default:
                            System.out.format("%-12s","-");
                            break;
                    } 
                } else {
                    if(index == 0) System.out.format("%-8.0f",column);
                    else System.out.format("%-12s",column);
                }
            }
            index++;
        }
        System.out.println();
        return;
    }

    //BigDecimal is more precise than double
    public static double createFunction(Vector<Double> coefficients, int vectorSize, double value) {    //coeff*(value^exponent)
        BigDecimal sum = new BigDecimal("0");
        for(int x=vectorSize-1;x>=0;x--){
            BigDecimal term = new BigDecimal(Double.toString(coefficients.get(x)*Math.pow(value,x)));
            sum = sum.add(term);
        }
        return truncate(sum,5,false);
    }

    public static double deriveFunction(Vector<Double> coefficients, int vectorSize, double value) {    //exponent*coeff*(value^exponent)
        BigDecimal sum = new BigDecimal("0");
        BigDecimal term = new BigDecimal("0");
        for(int x=vectorSize-1;x>=0;x--){
            if(x==0) term = new BigDecimal("0");
            else term = new BigDecimal(Double.toString(x*coefficients.get(x)*Math.pow(value,(x-1))));
            sum = sum.add(term);
        }
        return truncate(sum,5,false);
    }

    public static double getXnext(double xCurr, double fxCurr, double dfxCurr) {    //xi+1 = xi - f(xi)/df(xi)
        BigDecimal xNext;
        if(fxCurr == 0 || dfxCurr == 0) xNext = new BigDecimal(Double.toString(xCurr));   //to avoid infinity
        else {
            BigDecimal xCurrBd = new BigDecimal(Double.toString(xCurr));
            BigDecimal quotient = new BigDecimal(Double.toString(fxCurr/dfxCurr));
            xNext = xCurrBd.subtract(quotient);
        }
            return truncate(xNext,5,false);
    }

    public static double getE(double xNew, double xOld) {   //error = |(xNew - xOld) / xNew| * 100
        BigDecimal e;
        if(xNew !=0) {  //to avoid infinity
            BigDecimal error = new BigDecimal(Double.toString((xNew-xOld)/xNew));
            e = ((error.multiply(new BigDecimal("100")))).abs();   
        } else e = new BigDecimal("0");
        return truncate(e,5,false);
    }

    public static double truncate(BigDecimal value, int trunLimit, boolean reverse){
        if(value.doubleValue() != 0) {
            if(!reverse){
                if(value.doubleValue() > 0) value = value.setScale(trunLimit,RoundingMode.FLOOR);   //FLOOR rounds to negative infinity
                else value = value.setScale(trunLimit,RoundingMode.CEILING);    //CEILING rounds to positive infinity
            } else {
                if(value.doubleValue() > 0) value = value.setScale(trunLimit,RoundingMode.CEILING);
                else value = value.setScale(trunLimit,RoundingMode.FLOOR);
            }
        }
        return value.doubleValue();
    }
}
