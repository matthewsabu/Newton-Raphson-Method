import java.util.*;

public interface NewtonRaphsonInterface {
    public Vector<Double> stringToDblVector(String coefficients);
    public void printIterationData(double[] iterData);
    public double createFunction(Vector<Double> coefficients, int vectorSize, double value);
    public double deriveFunction(Vector<Double> coefficients, int vectorSize, double value);
    public double getXnext(double xCurr, double fxCurr, double dfxCurr);
    public double getE(double xNext, double xCurr);
}
