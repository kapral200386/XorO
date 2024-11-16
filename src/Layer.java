public class Layer {
    private Neuron[] neurons;

    public Layer(int neuronCount, int inputCount) {
        neurons = new Neuron[neuronCount];
        for (int i = 0; i < neuronCount; i++) {
            neurons[i] = new Neuron(inputCount);
        }
    }

    public int LayerSize(){
        return neurons.length;
    }

    public double[] forwardPass(double[] inputs) {
        double[] outputs = new double[neurons.length];
        for (int i = 0; i < neurons.length; i++) {
            outputs[i] = neurons[i].activate(inputs);
        }
        return outputs;
    }

    public Neuron[] getNeurons() {
        return neurons;
    }
}

