import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Visial3 {
    private Pole gamePole;
    private boolean isXTurn = true; // Флаг для чередования игроков (нейросетей)
    private NeuronNetwork neuralNetworkX; // Нейронная сеть для X
    private NeuronNetwork neuralNetworkY; // Нейронная сеть для O

    public Visial3(Pole gamePole, NeuronNetwork neuralNetworkX, NeuronNetwork neuralNetworkY) {
        this.gamePole = gamePole;
        this.neuralNetworkX = neuralNetworkX;
        this.neuralNetworkY = neuralNetworkY;

        JFrame frame = new JFrame();
        JPanel panel = new JPanel();
        panel.setLayout(new java.awt.GridLayout(gamePole.getPoleSize(), gamePole.getPoleSize()));
        frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);

        // Создаем сетку кнопок
        JButton[][] buttons = new JButton[gamePole.getPoleSize()][gamePole.getPoleSize()];

        // Заполняем сетку кнопок и добавляем ActionListener
        for (int i = 0; i < gamePole.getPoleSize(); i++) {
            for (int j = 0; j < gamePole.getPoleSize(); j++) {
                buttons[i][j] = new JButton();
                int row = i;
                int col = j;

                // Добавляем слушатель на каждую кнопку
                buttons[i][j].addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        if (isXTurn) {  // Ход нейросети X
                            if (!isGameOver(frame)) {
                                neuralMove(buttons, frame, neuralNetworkX, 1); // Ход нейросети Y
                            }
                            isXTurn = false; // Передаем ход нейросети Y
                            if (!isGameOver(frame)) {
                                neuralMove(buttons, frame, neuralNetworkY, 2); // Ход нейросети Y
                            }
                        }
                    }
                });

                panel.add(buttons[i][j]);
            }
        }

        frame.add(panel);
        frame.setSize(400, 400);
        frame.setVisible(true);
    }

    private void makeMove(int row, int col, JButton[][] buttons, int player) {
        int result = gamePole.Turn(row, col, player);

        // Обновляем отображение клетки
        buttons[row][col].setText(player == 1 ? "X" : "O");
        buttons[row][col].setEnabled(false); // Отключаем кнопку после нажатия

        // Проверяем результат после хода
        if (result == 1 || result == 2) {
            JOptionPane.showMessageDialog(null, "Игрок " + (player == 1 ? "X" : "O") + " выиграл!");
        } else if (result == -1) {
            JOptionPane.showMessageDialog(null, "Ничья");
        }
    }

    private void neuralMove(JButton[][] buttons, JFrame frame, NeuronNetwork currentNetwork, int player) {
        double[] inputs = gamePole.getPoleDlNeuron(player);
        GameNeuron gameNeuron = new GameNeuron(currentNetwork, gamePole);
        int[] cell = gameNeuron.TurnNeuron(inputs); // Получаем координаты из нейронной сети

        // Проверяем, что клетка свободна и нейросеть выбрала корректные координаты
        if (cell[0] >= 0 && cell[0] < gamePole.getPoleSize() &&
            cell[1] >= 0 && cell[1] < gamePole.getPoleSize() &&
            gamePole.getPole()[cell[0]][cell[1]] == 0) {

            System.out.println(cell[0] + " " + cell[1]);

            makeMove(cell[0], cell[1], buttons, player); // Ход нейросети
            isXTurn = !isXTurn; // Передаем ход обратно другой нейросети
            isGameOver(frame); // Проверяем, окончена ли игра
        } else {
            neuralMove(buttons, frame, currentNetwork, player); // Попытка нового хода, если ячейка занята
        }
    }

    private boolean isGameOver(JFrame frame) {
        int result = gamePole.StatusWin(isXTurn ? 1 : 2);
        if (result == 1 || result == 2) {
            JOptionPane.showMessageDialog(frame, "Игрок " + (isXTurn ? "O" : "X") + " выиграл!");
            frame.dispose();
            return true;
        } else if (result == -1) {
            JOptionPane.showMessageDialog(frame, "Ничья");
            frame.dispose();
            return true;
        }
        return false;
    }
}
