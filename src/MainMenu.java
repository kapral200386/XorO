import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

public class MainMenu extends JFrame {
    private JLabel infoLabel;

    public MainMenu() {
        setTitle("Основное меню для управления обучением");
        setSize(500, 400);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // Создаем и настраиваем область для информации
        infoLabel = new JLabel("Выберите опцию:");
        infoLabel.setHorizontalAlignment(SwingConstants.CENTER);
        add(infoLabel, BorderLayout.NORTH);

        // Контейнерная панель для всех кнопок
        JPanel buttonPanel = new JPanel(new GridBagLayout());
        add(buttonPanel, BorderLayout.CENTER);

        // Создаем и настраиваем кнопки с уникальными действиями
        JButton button1 = createButton("Загрузить модель", 150, 40, e -> {
            List<JLabel> labels = List.of(new JLabel("Путь к модели:"));
            List<JTextField> textFields = List.of(new JTextField());
            openCustomWindow("Загрузить модель", labels, textFields);
        });

        JButton button2 = createButton("Сохранить модель", 150, 40, e -> {
            List<JLabel> labels = List.of(new JLabel("Имя файла:"), new JLabel("Путь:"));
            List<JTextField> textFields = List.of(new JTextField(), new JTextField());
            openCustomWindow("Сохранить модель", labels, textFields);
        });

        JButton button3 = createButton("Начать/Продолжить обучение", 150, 40, e -> {
            List<JLabel> labels = List.of(new JLabel("Параметры обучения:"));
            List<JTextField> textFields = List.of(new JTextField());
            openCustomWindow("Начать/Продолжить обучение", labels, textFields);
        });

        JButton button4 = createButton("Игра между людьми", 150, 40, e -> {
            List<JLabel> labels = List.of(new JLabel("Игрок 1:"), new JLabel("Игрок 2:"));
            List<JTextField> textFields = List.of(new JTextField(), new JTextField());
            openCustomWindow("Игра между людьми", labels, textFields);
        });

        // Расположение кнопок на панели
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.gridx = 0;
        gbc.gridy = 0;
        buttonPanel.add(button1, gbc);

        gbc.gridx = 1;
        gbc.gridy = 0;
        buttonPanel.add(button2, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        buttonPanel.add(button3, gbc);

        gbc.gridx = 1;
        gbc.gridy = 1;
        buttonPanel.add(button4, gbc);
    }

    // Метод для создания кнопки с заданными размерами и действием
    private JButton createButton(String text, int width, int height, ActionListener action) {
        JButton button = new JButton(text);
        button.setPreferredSize(new Dimension(width, height));
        button.addActionListener(action);
        return button;
    }

    // Метод для создания настраиваемого окна с лейблами и текстовыми полями
    private void openCustomWindow(String title, List<JLabel> labels, List<JTextField> textFields) {
        JDialog customDialog = new JDialog(this, title, true);
        customDialog.setSize(400, 300);
        customDialog.setLayout(new BorderLayout());

        JPanel inputPanel = new JPanel();
        inputPanel.setLayout(new GridLayout(labels.size(), 2, 10, 10));

        // Добавление лейблов и текстовых полей
        for (int i = 0; i < labels.size(); i++) {
            inputPanel.add(labels.get(i));
            inputPanel.add(textFields.get(i));
        }

        customDialog.add(inputPanel, BorderLayout.CENTER);

        JButton submitButton = new JButton("OK");
        submitButton.addActionListener(e -> {
            for (int i = 0; i < textFields.size(); i++) {
                System.out.println("Значение из поля " + (i + 1) + ": " + textFields.get(i).getText());
            }
            customDialog.dispose();
        });

        customDialog.add(submitButton, BorderLayout.SOUTH);
        customDialog.setLocationRelativeTo(this);
        customDialog.setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            MainMenu mainMenu = new MainMenu();
            mainMenu.setVisible(true);
        });
    }
}

