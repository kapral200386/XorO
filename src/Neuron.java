import java.util.Random;

public class Neuron {
    public double[] weights;
    public double bias;
    public double output;
    Random rand = new Random();


    public Neuron(int inputCount) {
        weights = new double[inputCount];
        for (int i = 0; i < inputCount; i++) {
            weights[i] = rand.nextDouble() * 1.0 - 0.5; // от -0.5 до 0.5


        }
        bias = rand.nextDouble() - 0.5;
    }

    private double sigmoid(double x) {
        return 1 / (1 + Math.exp(-x));
    }

    public double activate(double[] inputs) {
        double sum = bias;
        for (int i = 0; i < weights.length; i++) {
            sum += inputs[i] * weights[i];
        }
        output = sigmoid(sum);
        return output;
    }

    public double getOutput() {
        return output;
    }

    public void setWeights(double[] Weights){
        this.weights = Weights;
    }

    public double[] getWeights(){
        return weights;
    }

    public double getBias() {
        return bias;
    }

    public void setBias(double bias) {
        this.bias = bias;
    }

    //    public void updateWeights(double[] inputs, double delta, double learningRate) {
//        for (int i = 0; i < weights.length; i++) {
//            weights[i] += learningRate * delta * inputs[i];
//        }
//        bias += learningRate * delta;
//    }

}
