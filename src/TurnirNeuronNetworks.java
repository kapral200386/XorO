import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;


public class TurnirNeuronNetworks {
    private NeuronNetwork neuronNetworkWin;
    private int colvoNeuronNetworks;
    private int[] networkSize;
    private int sizePole;
    private int sizeWin;   // Чисто для справки, чтобы можно было узнать на что она обучалась
    private final int numThreads = 28;
    private int lastScore = 0;
    private NeuronNetwork[] neuronNetworksWinsTurn;


    public TurnirNeuronNetworks(){
        int[] NetworkSize = {sizePole*sizePole + 1, 50, 100, 100, 50, sizePole*sizePole};
        this.networkSize = NetworkSize;
        this.colvoNeuronNetworks = 1000;
        this.sizePole = 3;
        this.sizeWin = 3;
        this.neuronNetworkWin = new NeuronNetwork(sizePole*sizePole + 1, networkSize);
        NeuronNetwork[] neuronNetworks = new NeuronNetwork[10];
        neuronNetworks[0] = neuronNetworkWin;
        for(int i = 1; i < 10; i++){
            neuronNetworks[i] = new NeuronNetwork(sizePole*sizePole + 1, networkSize);
        }
        this.neuronNetworksWinsTurn = neuronNetworks;
    }


    public TurnirNeuronNetworks(int[] NetworkSize, int ColvoNeuronNetworks, int SizePole, NeuronNetwork NeuronNetworkWin, int SizeWin){
        this.networkSize = NetworkSize;
        this.colvoNeuronNetworks = ColvoNeuronNetworks;
        this.sizePole = SizePole;
        this.neuronNetworkWin = NeuronNetworkWin;
        this.sizeWin = SizeWin;
        NeuronNetwork[] neuronNetworks = new NeuronNetwork[10];
        neuronNetworks[0] = neuronNetworkWin;
        for(int i = 1; i < 10; i++){
            neuronNetworks[i] = new NeuronNetwork(sizePole*sizePole + 1, networkSize);
        }
        this.neuronNetworksWinsTurn = neuronNetworks;
    }


    public void UPTurnirNeuronNetworks(){
        int[] NetworkSize = {sizePole*sizePole + 1, 50, 100, 100, 50, 50, sizePole*sizePole};
        this.networkSize = NetworkSize;
        this.neuronNetworkWin = new NeuronNetwork(sizePole*sizePole, networkSize);
    }

    public void setNetworkSize(int[] NetworkSize){
        this.networkSize = NetworkSize;
    }

    public void setNeuronNetworkWin(NeuronNetwork NeuronNetworkWin){
        this.neuronNetworkWin = NeuronNetworkWin;
    }

    public void SetColvoNeuronNetworks(int ColvoNeuronNetworks){
        this.colvoNeuronNetworks = ColvoNeuronNetworks;
    }

    public void SetSizePole(int SizePole){
        this.sizePole = SizePole;
    }

    public void setSizeWin(int SizeWin){
        this.sizeWin = SizeWin;
    }

    public int getSizeWin(){
        return sizeWin;
    }

    public int[] getNetworkSize(){
        return networkSize;
    }

    public NeuronNetwork getNeuronNetworkWin(){
        return neuronNetworkWin;
    }




    //Сделать новый механизм отбора, добавить кроссовер, и систему "Швецарку" сменить систему оценки поражений и побед
    public NeuronNetwork MutateAndStartGameTurnirNeiuronNetwork(double MutationRate, int ColvoProc) {
        int numThreads = ColvoProc;
        ExecutorService executorService = Executors.newFixedThreadPool(numThreads);

        NeuronNetwork[] neuronNetworks = new NeuronNetwork[colvoNeuronNetworks];
        double[] neuronNetworksScore = new double[colvoNeuronNetworks];
        for(int i = 0; i < 10; i++){
            neuronNetworks[i] = neuronNetworksWinsTurn[i];
        }
        int counter = 0;
        for(NeuronNetwork neuronNetworkParent1: neuronNetworksWinsTurn){
            for(NeuronNetwork neuronNetworkParent2: neuronNetworksWinsTurn){
                neuronNetworks[10+counter] = crossover(neuronNetworkParent1, neuronNetworkParent2);
                counter++;
            }
        }
        for(int i = 10 + counter; i < colvoNeuronNetworks / 5; i++){
            neuronNetworks[i] = new NeuronNetwork(sizePole*sizePole + 1, networkSize);
             counter++;
        }
        // Мутация остальных нейронных сетей
        for (int i = 10 + counter; i < colvoNeuronNetworks; i++) {
            neuronNetworks[i] = mutate(MutationRate);
        }

        List<Callable<Void>> tasks = new ArrayList<>();
        final AtomicInteger completedGames = new AtomicInteger(0);

        for (int i = 0; i < colvoNeuronNetworks; i++) {
            for (int j = 0; j < colvoNeuronNetworks; j++) {
                if (i != j) {  // Исключаем игры одной сети с самой собой
                    final int x = i;
                    final int y = j;
                    tasks.add(() -> {
                        Game NeuronGame = new Game(sizePole, sizeWin);
                        int res = NeuronGame.StartGameVsNeuralNetwork(neuronNetworks[x], neuronNetworks[y]);

                        completedGames.incrementAndGet(); // Увеличиваем завершенные игры

                        if (x >= 0 && x < neuronNetworksScore.length && y >= 0 && y < neuronNetworksScore.length) {
                            if (res == 1) {
                                neuronNetworksScore[x] += 1;
                                neuronNetworksScore[y] -= 0;
                            } else if (res == 2) {
                                neuronNetworksScore[y] += 1;
                                neuronNetworksScore[x] -= 0;
                            } else if(res == -1){
                                neuronNetworksScore[y] += 0.5;
                                neuronNetworksScore[x] += 0.5;
                            }
                        }

                        return null;
                    });
                }
            }
        }

        ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
        Runnable statusUpdateTask = () -> {
            System.out.println("Завершено игр: " + completedGames.get() + " из " + (colvoNeuronNetworks * colvoNeuronNetworks));
        };
        scheduler.scheduleAtFixedRate(statusUpdateTask, 0, 2, TimeUnit.SECONDS);

        try {
            List<Future<Void>> futures = executorService.invokeAll(tasks);

            for (Future<Void> future : futures) {
                future.get(); // Ожидаем завершения всех задач
            }
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        } finally {
            executorService.shutdown();
            scheduler.shutdown();
        }

        System.out.println("Завершено игр: " + completedGames.get() + " из " + (colvoNeuronNetworks * colvoNeuronNetworks));


        sortArrays(neuronNetworksScore, neuronNetworks);
        for(int i = 0; i < 10; i++){
            this.neuronNetworksWinsTurn[i] = neuronNetworks[i];
        }

        neuronNetworkWin = neuronNetworks[0];
        System.out.println("Счёт победителя: " + neuronNetworksScore[0]);
        return neuronNetworkWin;
    }



    public NeuronNetwork StartGameTurnirNeiuronNetwork(){
        NeuronNetwork[] neuronNetworks = new NeuronNetwork[colvoNeuronNetworks];
        int[] neuronNetworksScore = new int[colvoNeuronNetworks];
        for(int i = 0; i < colvoNeuronNetworks; i++){
            neuronNetworks[i] = new NeuronNetwork(sizePole*sizePole, networkSize);
        }
        int counterX = 0;
        int counterY = 0;
        for(NeuronNetwork neuronNetworkX : neuronNetworks){
            counterX++;
            System.out.println(counterX - 1 + " из " + colvoNeuronNetworks);
            for(NeuronNetwork neuronNetworkY : neuronNetworks){
                Game NeuronGame = new Game(sizePole, sizeWin);
                counterY++;
                int res = NeuronGame.StartGameVsNeuralNetwork(neuronNetworkX, neuronNetworkY);
                if(res == 1){
                    neuronNetworksScore[counterX]++;
                    neuronNetworksScore[counterY] = neuronNetworksScore[counterY] - 5;
                }
                else if(res == 2){
                    neuronNetworksScore[counterY]++;
                    neuronNetworksScore[counterX] = neuronNetworksScore[counterX] - 5;
                }
            }
        }
        //По идее можно реализовать блок поединков между одинаковыми по счёту нейронками, но пока просто последняя с макс счётом
        int counterMinScore = 10000;
        int counterMaxScore = -100000;
        NeuronNetwork neurolNetworksWin = neuronNetworks[0];
        for(int i = 0; i < neuronNetworksScore.length; i++){
            if(neuronNetworksScore[i] >= counterMaxScore){
                counterMaxScore = neuronNetworksScore[i];
                neurolNetworksWin = neuronNetworks[i];
            }
        }
        this.lastScore = counterMaxScore;
        System.out.println("Счёт победителя: " + counterMaxScore);
        return neurolNetworksWin;
    }


    public NeuronNetwork mutate(double mutationRate) {
        if(lastScore < 0){
            mutationRate = mutationRate*3;
        }
        else if(lastScore == colvoNeuronNetworks-1){
            mutationRate = mutationRate/3;
        }
        NeuronNetwork dopNeuroNetwork = neuronNetworkWin;
        Random rand = new Random();
        for (Layer layer : dopNeuroNetwork.getLayers()) {
            for (Neuron neuron : layer.getNeurons()) {
                // Мутация весов
                for (int i = 0; i < neuron.weights.length; i++) {
                    if (rand.nextDouble() < mutationRate) {
                        neuron.weights[i] += (rand.nextDouble() - 0.5) * 0.1; // от 0.01 до 0.2
                    }
                }
                // Мутация bias
                if (rand.nextDouble() < mutationRate) {
                    neuron.bias += (rand.nextDouble() - 0.5) * 0.1;
                }
            }
        }
        return dopNeuroNetwork;
    }


    public static void sortArrays(double[] arr1, NeuronNetwork[] arr2) {
        // Создание массива индексов для сортировки
        Integer[] indices = new Integer[arr1.length];
        for (int i = 0; i < arr1.length; i++) {
            indices[i] = i;
        }

        // Сортировка индексов в соответствии с значениями arr1 в порядке убывания
        Arrays.sort(indices, (i1, i2) -> Double.compare(arr1[i2], arr1[i1]));

        // Создание временных массивов для отсортированных значений
        double[] sortedArr1 = new double[arr1.length];
        NeuronNetwork[] sortedArr2 = new NeuronNetwork[arr2.length];

        // Перемещение значений по индексам
        for (int i = 0; i < arr1.length; i++) {
            sortedArr1[i] = arr1[indices[i]];
            sortedArr2[i] = arr2[indices[i]];
        }

        // Копирование отсортированных значений обратно в исходные массивы
        System.arraycopy(sortedArr1, 0, arr1, 0, arr1.length);
        System.arraycopy(sortedArr2, 0, arr2, 0, arr2.length);
    }

    public NeuronNetwork crossover(NeuronNetwork parent1, NeuronNetwork parent2) {
        Random rand = new Random();
        NeuronNetwork offspring = new NeuronNetwork(sizePole * sizePole + 1, networkSize);  // создаем новую нейронную сеть для потомка



        //System.out.println(parent1.getLayersSize() + " " + parent2.getLayersSize());

//        for(int i = 0; i < parent1.getLayersSize().length; i++){
//            //System.out.println(parent1.getLayersSize()[i] + " " + parent2.getLayersSize()[i]);
//        }

        // Проходим по всем слоям сети
        for (int layerIndex = 0; layerIndex < parent1.getLayers().length; layerIndex++) {
            Layer layer1 = parent1.getLayers()[layerIndex];
            Layer layer2 = parent2.getLayers()[layerIndex];
            Layer offspringLayer = offspring.getLayers()[layerIndex];

            // Для каждого нейрона в слое
            for (int neuronIndex = 0; neuronIndex < layer1.getNeurons().length; neuronIndex++) {
    //          System.out.println(layer1.getNeurons().length + " " + layer2.getNeurons().length);
                Neuron neuron1 = layer1.getNeurons()[neuronIndex];
                Neuron neuron2 = layer2.getNeurons()[neuronIndex];
                Neuron offspringNeuron = offspringLayer.getNeurons()[neuronIndex];
                //System.out.println(neuron1.getWeights().length);
                // Разделяем веса между двумя нейронами случайным образом
                for (int weightIndex = 0; weightIndex < neuron1.getWeights().length; weightIndex++) {
                    // Кроссовер весов: случайный выбор из двух родительских нейронов
                    if (rand.nextBoolean()) {
                        offspringNeuron.getWeights()[weightIndex] = neuron1.getWeights()[weightIndex];
                    } else {
                        offspringNeuron.getWeights()[weightIndex] = neuron2.getWeights()[weightIndex];
                    }
                }

                // Кроссовер bias (сравниваем с помощью random)
                if (rand.nextBoolean()) {
                    offspringNeuron.setBias(neuron1.getBias());
                } else {
                    offspringNeuron.setBias(neuron2.getBias());
                }
            }
        }

        return offspring;
    }







}
