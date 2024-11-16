public class ModelComparer {

    // Функция для сравнения двух моделей
    public static boolean compareModels(NeuronNetwork model1, NeuronNetwork model2) {
        // Сравниваем количество слоев
        if (model1.getLayers().length != model2.getLayers().length) {
            System.out.println("Количество слоев не совпадает");
            return false;
        }

        // Проходим по каждому слою
        for (int layerIndex = 0; layerIndex < model1.getLayers().length; layerIndex++) {
            Layer layer1 = model1.getLayers()[layerIndex];
            Layer layer2 = model2.getLayers()[layerIndex];

            // Сравниваем количество нейронов в слое
            if (layer1.getNeurons().length != layer2.getNeurons().length) {
                System.out.println("Количество нейронов в слое " + layerIndex + " не совпадает");
                return false;
            }

            // Проходим по каждому нейрону в слое
            for (int neuronIndex = 0; neuronIndex < layer1.getNeurons().length; neuronIndex++) {
                Neuron neuron1 = layer1.getNeurons()[neuronIndex];
                Neuron neuron2 = layer2.getNeurons()[neuronIndex];

                // Сравниваем веса нейронов
                if (!compareWeights(neuron1.getWeights(), neuron2.getWeights())) {
                    System.out.println("Веса нейронов на слое " + layerIndex + " в нейроне " + neuronIndex + " не совпадают");
                    return false;
                }

                // Сравниваем bias (смещения) нейронов
                if (neuron1.getBias() != neuron2.getBias()) {
                    System.out.println("Bias нейронов на слое " + layerIndex + " в нейроне " + neuronIndex + " не совпадает");
                    return false;
                }
            }
        }

        // Если все проверки прошли успешно, модели одинаковы
        System.out.println("Модели идентичны");
        return true;
    }

    // Функция для сравнения весов двух нейронов
    public static boolean compareWeights(double[] weights1, double[] weights2) {
        // Если длины массивов не совпадают, возвращаем false
        if (weights1.length != weights2.length) {
            System.out.println("Размеры массивов весов не совпадают");
            return false;
        }

        // Сравниваем каждый вес в массиве
        for (int i = 0; i < weights1.length; i++) {
            if (weights1[i] != weights2[i]) {
                System.out.println("Вес на позиции " + i + " не совпадает: " + weights1[i] + " != " + weights2[i]);
                return false;
            }
        }

        // Все веса совпадают
        return true;
    }
}

