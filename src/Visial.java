import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Visial {
    private Pole gamePole;
    private boolean isXTurn = true; // Флаг для чередования игроков

    public Visial(Pole gamePole) {
        this.gamePole = gamePole;
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
                        // Определяем текущего игрока и передаем ход
                        int player = isXTurn ? 1 : 2;
                        int result = gamePole.Turn(row, col, player);


                        //Проверка на вывод функции статуса сетки

                        System.out.println("То что получает человек ...");

                        for(int[] i : gamePole.getPole()){
                            for(int g : i){
                                System.out.print(g);
                            }
                            System.out.println();
                        }

                        System.out.println("То что получает сеть ...");
                        double[] masDlNeuronNetwork = gamePole.getPoleDlNeuron(2);
                        for(double i : masDlNeuronNetwork){
                            System.out.print(i + " ");
                        }
                        System.out.println();

                        // Обновляем отображение клетки
                        buttons[row][col].setText(isXTurn ? "X" : "O");
                        buttons[row][col].setEnabled(false); // Отключаем кнопку после нажатия

                        // Меняем игрока
                        isXTurn = !isXTurn;

                        // Проверяем результат
                        if (result == 1 || result == 2) {
                            JOptionPane.showMessageDialog(frame, "Игрок " + (isXTurn ? "O" : "X") + " выиграл!");
                            frame.dispose();
                        }
                        else if(result == -1){
                            JOptionPane.showMessageDialog(frame, "   Ничья  ");
                            frame.dispose();
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
}

