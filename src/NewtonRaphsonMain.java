import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.*;

public class NewtonRaphsonMain {
    private static NewtonRaphsonMethods methods = new NewtonRaphsonMethods();
    private static Scanner coefficients = new Scanner(System.in);
    private static Scanner initialConds = new Scanner(System.in);
    private static Scanner stoppingCrit = new Scanner(System.in);
    private static Vector<Double> coeffVec = new Vector<Double>();          //[C,B,A]
    private static ArrayList<Double> stopCrit = new ArrayList<Double>();    //[i,e,fxi]

    public static void main(String[] args) {
        //initial conditions
        double iterNo=0,x,xi,fx,fxD,fxi,e;
        x=xi=fx=fxD=fxi=e=0;

        //set function assumptions
        setCoeff();
        x = setInitCond();
        setStopCrit();

        //set initial iteration row (Row #1)
        double[] iterData = {iterNo,x,xi,fx,fxD,fxi,e};

        //perform algo and display table
        System.out.printf("\n%-8s%-12s%-12s%-12s%-12s%-12s\n","iter #","x","f(x)","f'(x)","f(xi)","e %");
        runNewtonRaphsonAlgo(coeffVec,stopCrit,iterData);

        coefficients.close();
        initialConds.close();
        stoppingCrit.close();
    }

    //user input
    public static void setCoeff(){
        //define function
        System.out.println("Newton Raphson Method: Find your equation's root!");
        System.out.print("Equation's coefficients ([C B A] Format): ");
        coeffVec = methods.stringToDblVector(coefficients.nextLine());
        return;
    }

    public static double setInitCond() {
        //set initial condition
        System.out.println("\nInitial Condition:");
        System.out.print("x0 = ");
        double init = initialConds.nextDouble();

        System.out.println("[" + init + "]");
        return init;
    }

    public static void setStopCrit(){
        //set stopping criteria
        System.out.println("\nStopping Criterion:");
        System.out.print("# of Iterations = ");
        stopCrit.add(stoppingCrit.nextDouble());

        System.out.print("|Error| % = ");
        stopCrit.add(stoppingCrit.nextDouble());

        System.out.print("|f(xi)| = ");
        stopCrit.add(stoppingCrit.nextDouble());

        System.out.println(stopCrit);
        return;
    }

    //everything with an "i" at the end indicates the value/s for the next (succeeding) iteration 
    //0=iteration,1=x,2=xi,3=fx,4=fxD,5=fxi,6=e (iterData index references)
    public static void runNewtonRaphsonAlgo(Vector<Double> coefficients, ArrayList<Double> stopCrit, double[] iterData) {
        double approxRoot = 0,xCurr = iterData[1],xNew = iterData[2];

        methods.printIterationData(iterData);   //print iteration row

        iterData[3] = methods.createFunction(coefficients,coefficients.size(),xCurr); //f(x)
        iterData[4] = methods.deriveFunction(coefficients,coefficients.size(),xCurr); //f(xD)
        iterData[2] = methods.getXnext(xCurr,iterData[3],iterData[4]); //xi
        iterData[5] = methods.createFunction(coefficients,coefficients.size(),iterData[2]); //f(xi)
        iterData[6] = methods.getE(iterData[2],xCurr); //e

        iterData[0]++;  //increase iteration counter

        xCurr = iterData[1];
        xNew = iterData[2];

        //stopping criterion:
        // iteration <= stopping Iteration && error <= stopping Error% && f(xi+1) <= stopping f(xi)
        if(!(iterData[0]>stopCrit.get(0)) && !(iterData[6]<=stopCrit.get(1)) && !(Math.abs(iterData[5])<=stopCrit.get(2))){
            //update iteration row (Row #i)
            double[] iterDatai = {iterData[0],xNew,xCurr,iterData[3],iterData[4],iterData[5],iterData[6]};

            runNewtonRaphsonAlgo(coefficients,stopCrit,iterDatai);
        } else {
            iterData[1] = xNew;
            methods.printIterationData(iterData);   //print trailing iteration row

            System.out.println("\nA stopping criteria has been met--stopped iterating.");
            DecimalFormat df = new DecimalFormat("0.00");
            if(xNew > 0) df.setRoundingMode(RoundingMode.FLOOR);
            else df.setRoundingMode(RoundingMode.CEILING);
            approxRoot = Double.parseDouble(df.format(xNew));   //rootAsymp
            System.out.println("\nThe equation's root is approximately " + approxRoot);
        } 
        return;
    }
}
