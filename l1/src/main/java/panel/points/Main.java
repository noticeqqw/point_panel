package panel.points;

import javax.swing.*;
import java.awt.*;

/**
 * Главный класс приложения.
 * Демонстрирует работу PointsPanel с паттерном Observer/Observable.
 */
public class Main {
    public static void main(String[] args) {
        // Запускаем GUI в потоке событий Swing
        SwingUtilities.invokeLater(() -> {
            createAndShowGUI();
        });
    }

    private static void createAndShowGUI() {
        // Создаем диалог для ввода параметров
        JPanel inputPanel = new JPanel(new GridLayout(7, 2, 5, 5));

        JTextField widthField = new JTextField("600");
        JTextField heightField = new JTextField("400");
        JTextField minXField = new JTextField("0.0");
        JTextField maxXField = new JTextField("100.0");
        JTextField minYField = new JTextField("0.0");
        JTextField maxYField = new JTextField("100.0");
        JTextField maxPointsField = new JTextField("10");

        inputPanel.add(new JLabel("Ширина панели (пикселей):"));
        inputPanel.add(widthField);
        inputPanel.add(new JLabel("Высота панели (пикселей):"));
        inputPanel.add(heightField);
        inputPanel.add(new JLabel("Минимум X:"));
        inputPanel.add(minXField);
        inputPanel.add(new JLabel("Максимум X:"));
        inputPanel.add(maxXField);
        inputPanel.add(new JLabel("Минимум Y:"));
        inputPanel.add(minYField);
        inputPanel.add(new JLabel("Максимум Y:"));
        inputPanel.add(maxYField);
        inputPanel.add(new JLabel("Количество точек N:"));
        inputPanel.add(maxPointsField);

        int result = JOptionPane.showConfirmDialog(null, inputPanel,
                "Введите параметры панели", JOptionPane.OK_CANCEL_OPTION);

        if (result != JOptionPane.OK_OPTION) {
            System.out.println("Создание панели отменено");
            return;
        }

        // Получаем значения из полей
        int width, height, maxPoints;
        double minX, maxX, minY, maxY;

        try {
            width = Integer.parseInt(widthField.getText().trim());
            height = Integer.parseInt(heightField.getText().trim());
            minX = Double.parseDouble(minXField.getText().trim());
            maxX = Double.parseDouble(maxXField.getText().trim());
            minY = Double.parseDouble(minYField.getText().trim());
            maxY = Double.parseDouble(maxYField.getText().trim());
            maxPoints = Integer.parseInt(maxPointsField.getText().trim());

            if (width <= 0 || height <= 0 || maxPoints <= 0) {
                JOptionPane.showMessageDialog(null,
                        "Ширина, высота и количество точек должны быть положительными числами!",
                        "Ошибка ввода", JOptionPane.ERROR_MESSAGE);
                return;
            }

            if (minX >= maxX || minY >= maxY) {
                JOptionPane.showMessageDialog(null,
                        "Минимальные значения должны быть меньше максимальных!",
                        "Ошибка ввода", JOptionPane.ERROR_MESSAGE);
                return;
            }

        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(null,
                    "Неверный формат числа! Проверьте введенные данные.",
                    "Ошибка ввода", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Создаем главное окно
        JFrame frame = new JFrame("📊 Панель Точек");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout());

        // Создаем панель для отображения точек с введенными параметрами
        PointsPanel pointsPanel = new PointsPanel(width, height, minX, maxX, minY, maxY, maxPoints);

        // Создаем генератор данных (Observable) с введенным диапазоном
        Observable generator = new Observable(minX, maxX, minY, maxY);

        // Подписываем панель на генератор
        generator.addObserver(pointsPanel);

        // Добавляем панель на окно
        frame.add(pointsPanel, BorderLayout.CENTER);

        // Создаем панель управления
        JPanel controlPanel = new JPanel();
        controlPanel.setLayout(new FlowLayout());

        // Кнопка для генерации одной точки
        JButton generateButton = new JButton("Сгенерировать точку");
        generateButton.addActionListener(e -> generator.generatePoint());
        controlPanel.add(generateButton);

        // Создаем таймер для автоматической генерации точек
        Timer timer = new Timer(1000, e -> generator.generatePoint());

        // Кнопка для запуска/остановки автоматической генерации
        JToggleButton autoGenerateButton = new JToggleButton("Запустить автогенерацию");
        autoGenerateButton.addActionListener(e -> {
            if (autoGenerateButton.isSelected()) {
                timer.start();
                autoGenerateButton.setText("Остановить автогенерацию");
            } else {
                timer.stop();
                autoGenerateButton.setText("Запустить автогенерацию");
            }
        });
        controlPanel.add(autoGenerateButton);

        frame.add(controlPanel, BorderLayout.SOUTH);

        // Настраиваем и показываем окно
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);

        System.out.println("Панель точек запущена!✅");
    }
}