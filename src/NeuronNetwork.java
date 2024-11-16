import java.util.Random;

public class NeuronNetwork {
    private Layer[] layers;
    private int inputSize;
    private int WinSize;

    public Layer[] getLayers(){
        return layers;
    }

    public int[] getLayersSize(){
        int[] layersSize = new int[layers.length];
        int counter = 0;
        for(Layer layer : layers){
            layersSize[counter] = layer.LayerSize();
        }
        return layersSize;
    }

    public NeuronNetwork(Layer[] layers){
        this.layers = layers;
        this.inputSize = layers[0].LayerSize();
    }


    public NeuronNetwork(int inputSize, int[] layerSizes) {
        this.inputSize = inputSize;
        layers = new Layer[layerSizes.length];
        layers[0] = new Layer(layerSizes[0], inputSize);

        for (int i = 1; i < layers.length; i++) {
            layers[i] = new Layer(layerSizes[i], layerSizes[i - 1]);
        }
    }

    public double[] forwardPass(double[] inputs) {
        double[] outputs = inputs;
        int y = 0;
        for (Layer layer : layers) {
            y++;
            if (layer.getNeurons().length > 0) {  // Если нейронов в слое больше 0
                outputs = layer.forwardPass(outputs);
            }
            //System.out.println("Layer " + y + " output length: " + outputs.length);
        }


        //Блок для крестиков ноликов
        //System.out.println(outputs[0]);
//        for(double i : outputs){
//           System.out.println(i);
//        }
        //System.out.println(CellX);
        return outputs;
        //Конец блока


        //return outputs[0];
    }
}
