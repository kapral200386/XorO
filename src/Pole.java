public class Pole {
    private int poleSize;
    private int[][] pole;
    private int sizeWin;
    private int ColvoTurn;

    public Pole(int size, int sizeWin) {
        this.poleSize = size;
        this.pole = new int[size][size];
        this.sizeWin = sizeWin;
        this.ColvoTurn = 0;
    }

    public int getPoleSize() {
        return poleSize;
    }

    public double[] getPoleDlNeuron(int player) {
        double[] getPoleDlNeuron = new double[poleSize*poleSize + 1];
        getPoleDlNeuron[0] = player;
        int counter = 1;
        for(int i[] : pole){
            for(int g: i){
                getPoleDlNeuron[counter] = g;
                counter++;
            }
        }
        return getPoleDlNeuron;
    }

    public int Turn(int XPozision, int YPozision, int Player) {
        ColvoTurn++;
        pole[XPozision][YPozision] = -10 + (Player%2)*20 ;
        return StatusWin(Player);
    }

    public int[][] getPole() {
        return pole;
    }

    public int StatusWin(int Player) {
        for (int i = 0; i < poleSize; i++) {
            for (int g = 0; g < poleSize; g++) {
                if (pole[i][g] != 0) {
                    if (Proverka(i, g, pole[i][g]) == 1) {
                        return Player;
                    }
                }
            }
        }
        if(ColvoTurn == poleSize*poleSize){
             return -1;
        }
        return 0;
    }

    public int Proverka(int XPozision, int YPozision, int Player) {
        // Проверка вертикали
        if (XPozision <= poleSize - sizeWin) {
            int counter = 1;
            for (int i = XPozision + 1; i < XPozision + sizeWin; i++) {
                if (pole[i][YPozision] == Player) {
                    counter++;
                } else {
                    break;
                }
            }
            if (counter == sizeWin) {
                return 1;
            }
        }

        // Проверка горизонтали
        if (YPozision <= poleSize - sizeWin) {
            int counter = 1;
            for (int i = YPozision + 1; i < YPozision + sizeWin; i++) {
                if (pole[XPozision][i] == Player) {
                    counter++;
                } else {
                    break;
                }
            }
            if (counter == sizeWin) {
                return 1;
            }
        }

        // Проверка главной диагонали
        if (XPozision <= poleSize - sizeWin && YPozision <= poleSize - sizeWin) {
            int counter = 1;
            for (int i = 1; i < sizeWin; i++) {
                if (pole[XPozision + i][YPozision + i] == Player) {
                    counter++;
                } else {
                    break;
                }
            }
            if (counter == sizeWin) {
                return 1;
            }
        }

        // Проверка побочной диагонали
        if (XPozision <= poleSize - sizeWin && YPozision >= sizeWin - 1) {
            int counter = 1;
            for (int i = 1; i < sizeWin; i++) {
                if (pole[XPozision + i][YPozision - i] == Player) {
                    counter++;
                } else {
                    break;
                }
            }
            if (counter == sizeWin) {
                return 1;
            }
        }

        return 0;
    }
}
