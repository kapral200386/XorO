import java.util.Random;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        Random random = new Random();
        System.out.println("Hello world!");
        int[] NetworkSize = {10, 50, 100, 100, 50, 9};
        NeuronNetwork neuronNetworkWin = new NeuronNetwork(10, NetworkSize);


        while (true){
            System.out.println("Выберите действие");
            System.out.println("Битва между игроками - 1");
//          System.out.println("Битва между нейронками - 2");
            System.out.println("Битва между нейронными сетями - 2");
            System.out.println("Битва между человеком и нейронной сетью - 3");
            System.out.println("Начало эволюции - 4");
            System.out.println("Сохранение модели - 5");
            System.out.println("Загрузка модели - 6");
            System.out.println("Сравнение двух моделей(Для тестов) - 7");
            System.out.println("Прекращение работы - 0");
            String strZapros = scanner.nextLine();


            if(strZapros.equals("1")){
                System.out.print("Выберите размер поля:  ");
                String sizeGame = scanner.nextLine();
                System.out.println();
                System.out.print("Выберите сложность:  ");
                String sizeWin = scanner.nextLine();
                Game game = new Game(Integer.parseInt(sizeGame), Integer.parseInt(sizeWin));
                int g = game.StartGameVsEachOther();
            }


            if(strZapros.equals("2")){
                System.out.print("Выберите размер поля:  ");
                String sizeGame = scanner.nextLine();
                System.out.println();
                System.out.print("Выберите сложность:  ");
                String sizeWin = scanner.nextLine();
                System.out.println();
                System.out.print("Выберите путь загрузки до нужной нейронной сети 1:  ");
                String path1 = scanner.nextLine();
                System.out.println();
                System.out.print("Выберите путь загрузки до нужной нейронной сети 2:  ");
                String path2 = scanner.nextLine();
                System.out.println();
                Game game = new Game(Integer.parseInt(sizeGame), Integer.parseInt(sizeWin));
                LoadAndSave loadAndSave = new LoadAndSave();
                int g = game.StartGameVsNeuralNetwork(loadAndSave.LoadModel(path1), loadAndSave.LoadModel(path2));
            }


            if(strZapros.equals("3")){
                System.out.print("Выберите размер поля:  ");
                String sizeGame = scanner.nextLine();
                System.out.println();
                System.out.print("Выберите сложность:  ");
                String sizeWin = scanner.nextLine();
                System.out.println();
                System.out.print("Выберите путь загрузки до нужной нейронной сети:  ");
                String path1 = scanner.nextLine();
                System.out.println();
                Game game = new Game(Integer.parseInt(sizeGame), Integer.parseInt(sizeWin));
                LoadAndSave loadAndSave = new LoadAndSave();
                int g = game.StartGameVsPeopleAndNeuron(loadAndSave.LoadModel(path1));
            }


            if(strZapros.equals("4")) {
                System.out.print("Сколько поколений вы хотете обучить: ");
                String ColvoPoc = scanner.nextLine();
                System.out.print("Шанс мутации: ");
                String MutationRate = scanner.nextLine();
                System.out.print("Сколько потоков задействовать: ");
                String ColvoProc = scanner.nextLine();
                System.out.print("Сколько поколений этой модели вы уже обучили: ");
                int ColvoPocR = Integer.parseInt(scanner.nextLine());
                double MutationRate1 = (double) (Integer.parseInt(ColvoPoc) / 100);
                int sizePole = 3;
                TurnirNeuronNetworks turnirNeuronNetworks = new TurnirNeuronNetworks(new int[]{sizePole*sizePole + 1, 50, 100, 100, 50, sizePole*sizePole}, 1000, 3, neuronNetworkWin, 3);
                turnirNeuronNetworks.UPTurnirNeuronNetworks();
                System.out.print("Укажите путь нейронки: ");
                String strPathLoad = scanner.nextLine();
                LoadAndSave loadAndSave = new LoadAndSave();
                neuronNetworkWin = loadAndSave.LoadModel(strPathLoad);
                neuronNetworkWin = turnirNeuronNetworks.MutateAndStartGameTurnirNeiuronNetwork(MutationRate1, Integer.parseInt(ColvoProc));
                int i = 2;
                while (i < Integer.parseInt(ColvoPoc)) {
                    i++;
                    neuronNetworkWin = turnirNeuronNetworks.MutateAndStartGameTurnirNeiuronNetwork(MutationRate1, Integer.parseInt(ColvoProc));
                    System.out.print("----------------- " + i + " из " + Integer.parseInt(ColvoPoc) + "---------------------");
                    if((ColvoPocR + i) % 100 == 0){

                        String strNameSavetxt = neuronNetworkWin + " " + (ColvoPocR + i) + " поколение_(АвтоСейф).txt";
                        loadAndSave.SaveModel(neuronNetworkWin, "F:\\Dev\\weght\\" + strNameSavetxt);
                    }
                    System.out.println();
                }
            }


            if(strZapros.equals("5")) {
                System.out.println("Укажите путь (F:\\Dev\\weght\\3x3v1.txt)");
                strZapros = scanner.nextLine();
                LoadAndSave loadAndSave = new LoadAndSave();
                loadAndSave.SaveModel(neuronNetworkWin, strZapros);
            }


            if(strZapros.equals("6")) {
                System.out.println("Укажите путь (F:\\Dev\\weght\\3x3v1.txt)");
                strZapros = scanner.nextLine();
                LoadAndSave loadAndSave = new LoadAndSave();
                neuronNetworkWin = loadAndSave.LoadModel(strZapros);
            }

            if(strZapros.equals("7")) {
                System.out.println("Укажите путь (F:\\Dev\\weght\\3x3v1.txt)");
                strZapros = scanner.nextLine();
                LoadAndSave loadAndSave = new LoadAndSave();
                ModelComparer modelComparer = new ModelComparer();
                System.out.println(modelComparer.compareModels(loadAndSave.LoadModel(strZapros), neuronNetworkWin));
            }


            if(strZapros.equals("0")) {
                break;
            }


            else {
                System.out.println("Запрос не ясен, повторите запрос");
            }
//                System.out.println("Сохранить ли веса?  Y/N");
//                strZapros = scanner.nextLine();
//                if(strZapros.equals("Y")){
//                    System.out.println("Укажите путь (F:\\Dev\\weght\\3x3v2.txt)");
//                    strZapros = scanner.nextLine();
//                    LoadAndSave loadAndSave = new LoadAndSave();
//                    loadAndSave.SaveModel(neuronNetworkWin, strZapros);
//                }
//
//
//                System.out.println("Выберите размер поля:  ");
//                String sizeGame = scanner.nextLine();
//                System.out.println();
//                System.out.print("Выберите сложность:  ");
//                String sizeWin = scanner.nextLine();
//                Game game = new Game(Integer.parseInt(sizeGame), Integer.parseInt(sizeWin));
//                int g = game.StartGameVsPeopleAndNeuron(neuronNetworkWin);
//
//            if(strZapros.equals("2")){
//                TurnirNeuronNetworks turnirNeuronNetworks = new TurnirNeuronNetworks();
//                NeuronNetwork neuronNetworkWin = turnirNeuronNetworks.StartGameTurnirNeiuronNetwork();
//                System.out.println("Победила модель " + neuronNetworkWin);
//                System.out.println("Выберите дальнейшее действие");
//                System.out.println("Сохранение - 0");
//                System.out.println("Мутация - 1");
//                System.out.println("Загрузка - 2");
//                System.out.println("Игра двуй сетей - 3");
//                System.out.println("Сыграть с нейронной сетью - 4");
//                String strZapros1 = scanner.nextLine();
//
//                if(strZapros1.equals("1")){
//                    neuronNetworkWin = turnirNeuronNetworks.MutateAndStartGameTurnirNeiuronNetwork(0.15);
//                    System.out.println("Победила модель " + neuronNetworkWin);
//                    System.out.println("Сыграть с нейронной сетью - 4");
//                    System.out.print("Выберите размер поля:  ");
//                    String sizeGame = scanner.nextLine();
//                    System.out.println();
//                    System.out.print("Выберите сложность:  ");
//                    String sizeWin = scanner.nextLine();
//                    Game game = new Game(Integer.parseInt(sizeGame), Integer.parseInt(sizeWin));
//                    int g = game.StartGameVsPeopleAndNeuron(neuronNetworkWin);
//                }
//
//                if(strZapros1.equals("3")){
//                    System.out.print("Выберите размер поля:  ");
//                    String sizeGame = scanner.nextLine();
//                    System.out.println();
//                    System.out.print("Выберите сложность:  ");
//                    String sizeWin = scanner.nextLine();
//                    Game game = new Game(Integer.parseInt(sizeGame), Integer.parseInt(sizeWin));
//                    int g = game.StartGameVsNeuronAndNeuron(neuronNetworkWin, neuronNetworkWin);
//                }
//
//                if(strZapros1.equals("4")){
//                    System.out.print("Выберите размер поля:  ");
//                    String sizeGame = scanner.nextLine();
//                    System.out.println();
//                    System.out.print("Выберите сложность:  ");
//                    String sizeWin = scanner.nextLine();
//                    Game game = new Game(Integer.parseInt(sizeGame), Integer.parseInt(sizeWin));
//                    int g = game.StartGameVsPeopleAndNeuron(neuronNetworkWin);
//                }
//            }


        }
    }

}