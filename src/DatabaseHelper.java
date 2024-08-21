import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper {
    private static final String DB_URL = "jdbc:mysql://localhost:3306/budget_manager";
    private static final String USER = "root"; // Replace with your MySQL username
    private static final String PASS = "Radhika@1980"; // Replace with your MySQL password

    private Connection connection;

    public DatabaseHelper() {
        try {
            connection = DriverManager.getConnection(DB_URL, USER, PASS);
            initializeDatabase();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Initialize the database with necessary tables
    private void initializeDatabase() {
        try (Statement stmt = connection.createStatement()) {
            // Create Income and Expense table
            String createTransactionsTable = "CREATE TABLE IF NOT EXISTS transactions (" +
                    "id INT AUTO_INCREMENT PRIMARY KEY, " +
                    "type VARCHAR(10), " +
                    "amount DOUBLE, " +
                    "category VARCHAR(50), " +
                    "date DATE" +
                    ")";
            stmt.executeUpdate(createTransactionsTable);

            // Create Category table
            String createCategoryTable = "CREATE TABLE IF NOT EXISTS categories (" +
                    "id INT AUTO_INCREMENT PRIMARY KEY, " +
                    "name VARCHAR(50) UNIQUE" +
                    ")";
            stmt.executeUpdate(createCategoryTable);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Add income entry
    public void addIncome(double amount, String category, String date) {
        addTransaction("Income", amount, category, date);
    }

    // Add expense entry
    public void addExpense(double amount, String category, String date) {
        addTransaction("Expense", amount, category, date);
    }

    // Add a transaction (common method for income and expense)
    private void addTransaction(String type, double amount, String category, String date) {
        try (PreparedStatement pstmt = connection.prepareStatement(
                "INSERT INTO transactions (type, amount, category, date) VALUES (?, ?, ?, ?)")) {
            pstmt.setString(1, type);
            pstmt.setDouble(2, amount);
            pstmt.setString(3, category);
            pstmt.setDate(4, Date.valueOf(date));
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Get the current balance (total income - total expenses)
    public double getCurrentBalance() {
        double totalIncome = getTotalIncome();
        double totalExpense = getTotalExpenses();
        return totalIncome - totalExpense;
    }

    // Get total income
    public double getTotalIncome() {
        return getTotalAmount("Income");
    }

    // Get total expenses
    public double getTotalExpenses() {
        return getTotalAmount("Expense");
    }

    // Get the total amount for a given transaction type
    private double getTotalAmount(String type) {
        double total = 0.0;
        try (PreparedStatement pstmt = connection.prepareStatement(
                "SELECT SUM(amount) FROM transactions WHERE type = ?")) {
            pstmt.setString(1, type);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                total = rs.getDouble(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return total;
    }

    // Get the transaction history
    public List<Transaction> getTransactionHistory() {
        List<Transaction> transactions = new ArrayList<>();
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT * FROM transactions ORDER BY date DESC")) {
            while (rs.next()) {
                int id = rs.getInt("id");
                String type = rs.getString("type");
                double amount = rs.getDouble("amount");
                String category = rs.getString("category");
                String date = rs.getDate("date").toString();
                transactions.add(new Transaction(id, type, amount, category, date));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return transactions;
    }

    // Add a new category
    public void addCategory(String category) {
        try (PreparedStatement pstmt = connection.prepareStatement(
                "INSERT INTO categories (name) VALUES (?)")) {
            pstmt.setString(1, category);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Delete a category
    public void deleteCategory(String category) {
        try (PreparedStatement pstmt = connection.prepareStatement(
                "DELETE FROM categories WHERE name = ?")) {
            pstmt.setString(1, category);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
