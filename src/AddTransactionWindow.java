import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class AddTransactionWindow extends JFrame {
    private JTextField amountField;
    private JTextField categoryField;
    private JTextField dateField;
    private JButton submitButton;
    private DatabaseHelper dbHelper;
    private JLabel balanceLabel;

    public AddTransactionWindow(String type, DatabaseHelper dbHelper, JLabel balanceLabel) {
        this.dbHelper = dbHelper;
        this.balanceLabel = balanceLabel;

        setTitle("Add " + type);
        setSize(300, 200);
        setLayout(new GridLayout(5, 2));

        JLabel amountLabel = new JLabel("Amount:");
        amountField = new JTextField();

        JLabel categoryLabel = new JLabel("Category:");
        categoryField = new JTextField();

        JLabel dateLabel = new JLabel("Date (YYYY-MM-DD):");
        dateField = new JTextField();

        submitButton = new JButton("Submit");

        add(amountLabel);
        add(amountField);
        add(categoryLabel);
        add(categoryField);
        add(dateLabel);
        add(dateField);
        add(new JLabel());  // Empty cell
        add(submitButton);

        submitButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                double amount = Double.parseDouble(amountField.getText());
                String category = categoryField.getText();
                String date = dateField.getText();

                if (type.equals("Income")) {
                    dbHelper.addIncome(amount, category, date);
                } else {
                    dbHelper.addExpense(amount, category, date);
                }

                balanceLabel.setText("Current Balance: $" + dbHelper.getCurrentBalance());
                dispose();
            }
        });
    }
}
