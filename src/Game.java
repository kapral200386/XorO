public class Game {

    private int IdPlayerY;
    private int IdPlayerX;
    private Pole GamePole;

    public Game(int SizeGamePole, int SizeWin){
        this.GamePole = new Pole(SizeGamePole, SizeWin);
    }

    public int StartGameVsEachOther(){
        Visial visial = new Visial(GamePole);
        return 0;
    }
    public int StartGameVsPeopleAndNeuron(NeuronNetwork neuronNetwork){
        Visial2 visial = new Visial2(GamePole, neuronNetwork);
        return 0;
    }

    public int StartGameVsNeuronAndNeuron(NeuronNetwork neuronNetworkX, NeuronNetwork NeuronNetworkY){
        Visial3 visial = new Visial3(GamePole, neuronNetworkX, NeuronNetworkY );
        return 0;
    }

    public int StartGameVsNeuralNetwork(NeuronNetwork neuronNetworkX, NeuronNetwork neuronNetworkY){
        while(true){
            GameNeuron gameNeuronX = new GameNeuron(neuronNetworkX, GamePole);
            GameNeuron gameNeuronY = new GameNeuron(neuronNetworkY, GamePole);
            double[] test1 = GamePole.getPoleDlNeuron(1);
            int[] TurnX = gameNeuronX.TurnNeuron(test1);
            int ResTurnX = GamePole.Turn(TurnX[0], TurnX[1], 1);
            if(ResTurnX != 0){
                return ResTurnX;
            }
            double[] test2 = GamePole.getPoleDlNeuron(2);
            int[] TurnY = gameNeuronY.TurnNeuron(test2);
            int ResTurnY = GamePole.Turn(TurnY[0], TurnY[1], 2);
            if(ResTurnY != 0){
                return ResTurnX;
            }
        }
    }
}
