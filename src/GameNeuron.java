public class GameNeuron {
    private NeuronNetwork neuronNetwork;
    private int poleSize;
    public GameNeuron(NeuronNetwork neuronNetwork, Pole pole){
        this.neuronNetwork = neuronNetwork;
        this.poleSize = pole.getPoleSize();
    }

    public int[] TurnNeuron(double[] inputs){
        // Получаем выходы сети
        double[] outputs = neuronNetwork.forwardPass(inputs);
        int y = 0;

        outputs = softmax(outputs);
        // Обнуляем вероятности для занятых клеток
        for(int i = 1; i < outputs.length; i++) {
            if (inputs[i] == 10 || inputs[i] == -10) {
                outputs[i -1 ] = -1000; // уменьшение вероятности занятых клеток
            }
        }

        // Применяем softmax для нормализации вероятностей


        // Находим клетку с максимальным значением
        double maxValue = -1000;
        int maxIndex = 0;
        for(int i = 0; i < outputs.length; i++) {
            if(outputs[i] > maxValue) {
                maxValue = outputs[i];
                maxIndex = i;
            }
        }

        // Преобразуем индекс в координаты клетки
        int CellY = maxIndex / poleSize;
        int CellX = maxIndex % poleSize;

        return new int[]{CellY, CellX};
    }



    private double[] softmax(double[] z) {
        double sum = 0;
        double[] result = new double[z.length];
        for (double value : z) {
            sum += Math.exp(value);
        }
        for (int i = 0; i < z.length; i++) {
            result[i] = Math.exp(z[i]) / sum;
        }
        return result;
    }


}
