import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PiePlot;
import org.jfree.data.general.DefaultPieDataset;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MainWindow extends JFrame {

    private DatabaseHelper dbHelper;

    public MainWindow() {
        // Initialize the database helper
        dbHelper = new DatabaseHelper();

        // Set up the main window
        setTitle("Personal Budget Manager");
        setSize(600, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // Background setup
        JLabel background = new JLabel(new ImageIcon("path/to/your/image.png")); // Update path
        background.setLayout(new GridBagLayout());
        background.setOpaque(true);
        background.setBackground(new Color(46, 65, 83)); // Light color background
        add(background);

        // Layout for buttons
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(10, 10, 10, 10);

        // Add Income Button
        JButton incomeButton = new JButton("Add Income");
        incomeButton.setPreferredSize(new Dimension(150, 40));
        incomeButton.setBackground(new Color(34, 139, 34)); // Dark Green
        incomeButton.setForeground(Color.WHITE);
        gbc.gridx = 0;
        gbc.gridy = 0;
        background.add(incomeButton, gbc);

        // Add Expense Button
        JButton expenseButton = new JButton("Add Expense");
        expenseButton.setPreferredSize(new Dimension(150, 40));
        expenseButton.setBackground(new Color(255, 69, 0)); // Red-Orange
        expenseButton.setForeground(Color.WHITE);
        gbc.gridx = 1;
        gbc.gridy = 0;
        background.add(expenseButton, gbc);

        // View Balance Button
        JButton balanceButton = new JButton("View Balance");
        balanceButton.setPreferredSize(new Dimension(150, 40));
        balanceButton.setBackground(new Color(70, 130, 180)); // Steel Blue
        balanceButton.setForeground(Color.WHITE);
        gbc.gridx = 0;
        gbc.gridy = 1;
        background.add(balanceButton, gbc);

        // View Chart Button
        JButton chartButton = new JButton("View Chart");
        chartButton.setPreferredSize(new Dimension(150, 40));
        chartButton.setBackground(new Color(255, 215, 0)); // Gold
        chartButton.setForeground(Color.BLACK);
        gbc.gridx = 1;
        gbc.gridy = 1;
        background.add(chartButton, gbc);

        // Exit Button
        JButton exitButton = new JButton("Exit");
        exitButton.setPreferredSize(new Dimension(150, 40));
        exitButton.setBackground(new Color(169, 169, 169)); // Dark Gray
        exitButton.setForeground(Color.WHITE);
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 2;
        background.add(exitButton, gbc);

        // Action listeners
        incomeButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String amountStr = JOptionPane.showInputDialog("Enter income amount:");
                String category = JOptionPane.showInputDialog("Enter income category:");
                String date = JOptionPane.showInputDialog("Enter date (YYYY-MM-DD):");
                if (amountStr != null && category != null && date != null) {
                    try {
                        double amount = Double.parseDouble(amountStr);
                        dbHelper.addIncome(amount, category, date);
                        JOptionPane.showMessageDialog(null, "Income added successfully.");
                    } catch (NumberFormatException ex) {
                        JOptionPane.showMessageDialog(null, "Invalid amount entered.");
                    }
                }
            }
        });

        expenseButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String amountStr = JOptionPane.showInputDialog("Enter expense amount:");
                String category = JOptionPane.showInputDialog("Enter expense category:");
                String date = JOptionPane.showInputDialog("Enter date (YYYY-MM-DD):");
                if (amountStr != null && category != null && date != null) {
                    try {
                        double amount = Double.parseDouble(amountStr);
                        dbHelper.addExpense(amount, category, date);
                        JOptionPane.showMessageDialog(null, "Expense added successfully.");
                    } catch (NumberFormatException ex) {
                        JOptionPane.showMessageDialog(null, "Invalid amount entered.");
                    }
                }
            }
        });

        balanceButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                double balance = dbHelper.getCurrentBalance();
                JOptionPane.showMessageDialog(null, "Current Balance: " + balance);
            }
        });

        chartButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                displayChart();
            }
        });

        exitButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });
    }

    private void displayChart() {
        JFrame chartFrame = new JFrame("Income vs Expense Chart");
        chartFrame.setSize(600, 400);
        chartFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        double income = dbHelper.getTotalIncome();
        double expense = dbHelper.getTotalExpenses();
        double total = income + expense;
        double incomePercentage = (income / total) * 100;
        double expensePercentage = (expense / total) * 100;

        DefaultPieDataset dataset = new DefaultPieDataset();
        dataset.setValue("Income", incomePercentage);
        dataset.setValue("Expenses", expensePercentage);

        JFreeChart chart = ChartFactory.createPieChart(
                "Income vs Expense",
                dataset,
                true,
                true,
                false
        );

        PiePlot plot = (PiePlot) chart.getPlot();
        plot.setSectionPaint("Income", new Color(34, 139, 34));  // Dark Green for Income
        plot.setSectionPaint("Expenses", new Color(255, 69, 0)); // Red-Orange for Expenses

        ChartPanel chartPanel = new ChartPanel(chart);
        chartPanel.setPreferredSize(new Dimension(600, 400));
        chartFrame.add(chartPanel);
        chartFrame.pack();
        chartFrame.setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new MainWindow().setVisible(true));
    }
}
