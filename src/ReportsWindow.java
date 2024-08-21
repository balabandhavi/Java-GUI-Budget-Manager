import javax.swing.*;
import java.awt.*;

public class ReportsWindow extends JFrame {
    private DatabaseHelper dbHelper;

    public ReportsWindow(DatabaseHelper dbHelper) {
        this.dbHelper = dbHelper;

        setTitle("Reports");
        setSize(400, 300);
        setLayout(new GridLayout(2, 1));

        JLabel incomeLabel = new JLabel("Total Income: $" + dbHelper.getTotalIncome());
        JLabel expenseLabel = new JLabel("Total Expenses: $" + dbHelper.getTotalExpenses());

        add(incomeLabel);
        add(expenseLabel);
    }
}
