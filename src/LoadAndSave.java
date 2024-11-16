import java.io.*;
import java.util.ArrayList;
import java.util.List;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class LoadAndSave {
    public LoadAndSave(){

    }

    // Сохранение модели
    public void SaveModel(NeuronNetwork neuronNetwork, String filePath) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
            int poleSize = neuronNetwork.getLayers()[neuronNetwork.getLayers().length - 1].LayerSize();
            StringBuilder stringBuilder = new StringBuilder();

            stringBuilder.append(poleSize).append("\n");

            // Сохранение размеров слоёв
            int[] sizeLayers = neuronNetwork.getLayersSize();
            StringBuilder strSizeLayers = new StringBuilder();
            for (Layer layer : neuronNetwork.getLayers()) {
                strSizeLayers.append(layer.LayerSize()).append(" ");
            }
            stringBuilder.append(strSizeLayers.toString().trim()).append("\n");

            // Сохранение весов и bias нейронов
            StringBuilder strWeightNeuronInLayers = new StringBuilder();
            for (Layer layer : neuronNetwork.getLayers()) {
                for (Neuron neuron : layer.getNeurons()) {
                    // Сохранение весов
                    for (double weight : neuron.weights) {
                        strWeightNeuronInLayers.append(weight).append(" ");
                    }
                    // Сохранение bias
                    strWeightNeuronInLayers.append(neuron.getBias()).append("\n");
                }
                strWeightNeuronInLayers.append("\n");
            }

            writer.write(stringBuilder.toString());
            writer.write(strWeightNeuronInLayers.toString());

            System.out.println("Модель сохранена в файл: " + filePath);
        } catch (IOException e) {
            System.out.println("Ошибка при сохранении модели: " + e.getMessage());
        }
    }

    // Загрузка модели
    public NeuronNetwork LoadModel(String filePath) {
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            // Считываем первую строку - размер матрицы
            int poleSize = Integer.parseInt(reader.readLine().trim());

            // Считываем вторую строку - размеры слоёв
            String[] layerSizesStr = reader.readLine().trim().split(" ");
            int[] layerSizes = new int[layerSizesStr.length];
            for (int i = 0; i < layerSizesStr.length; i++) {
                layerSizes[i] = Integer.parseInt(layerSizesStr[i]);
            }

            // Создаем структуру для хранения нейронов и весов
            List<Layer> layers = new ArrayList<>();
            int counter = 0;
            for (int layerSize : layerSizes) {
                if(counter == 0){
                    Layer layer = new Layer(layerSize, layerSize);
                    layers.add(layer);
                }
                else{
                    Layer layer = new Layer(layerSize, layerSizes[counter]);
                    layers.add(layer);
                }
                counter++;
            }

            // Считываем веса и bias нейронов для каждого слоя
            for (Layer layer : layers) {
                for (Neuron neuron : layer.getNeurons()) {
                    String[] weightsStr = reader.readLine().trim().split(" ");
                    double[] weights = new double[weightsStr.length - 1]; // все, кроме последнего
                    // Считываем веса
                    for (int i = 0; i < weights.length; i++) {
                        weights[i] = Double.parseDouble(weightsStr[i]);
                    }
                    neuron.setWeights(weights);
                    // Считываем bias
                    double bias = Double.parseDouble(weightsStr[weightsStr.length - 1]);
                    neuron.setBias(bias);
                }
                // Пропускаем пустую строку между слоями
                reader.readLine();
            }

            // Создаем и возвращаем загруженную нейронную сеть
            NeuronNetwork neuronNetwork = new NeuronNetwork(layers.toArray(new Layer[0]));
            System.out.println("Модель загружена из файла: " + filePath);
            return neuronNetwork;
        } catch (IOException e) {
            System.out.println("Ошибка при загрузке модели: " + e.getMessage());
            return null;
        } catch (NumberFormatException e) {
            System.out.println("Ошибка в формате данных: " + e.getMessage());
            return null;
        }
    }
}
