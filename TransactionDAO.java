import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class TransactionDAO {
    
    // Read all transactions for an account
    public List<Transaction> getTransactionsByAccount(String accountNumber) {
        List<Transaction> transactions = new ArrayList<>();
        Connection conn = DatabaseConnection.getConnection();
        if (conn == null) {
            System.out.println("⚠️ Database not available - returning empty transaction list");
            return transactions;
        }
        
        String sql = "SELECT transaction_id, account_number, transaction_date, transaction_type, amount, description " +
                    "FROM transactions WHERE account_number = ? ORDER BY transaction_date DESC";
        
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, accountNumber);
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
                Transaction transaction = new Transaction(
                    rs.getString("transaction_id"),
                    rs.getTimestamp("transaction_date").toLocalDateTime(),
                    rs.getString("transaction_type"),
                    rs.getDouble("amount"),
                    rs.getString("description"),
                    rs.getString("account_number")
                );
                transactions.add(transaction);
            }
        } catch (SQLException e) {
            System.err.println("Error retrieving account transactions: " + e.getMessage());
        }
        return transactions;
    }
    
    // Create a new transaction
    public boolean createTransaction(Transaction transaction) {
        Connection conn = DatabaseConnection.getConnection();
        if (conn == null) {
            System.out.println("⚠️ Database not available - cannot create transaction");
            return false;
        }
        
        String sql = "INSERT INTO transactions (transaction_id, account_number, transaction_date, transaction_type, amount, description) " +
                    "VALUES (?, ?, ?, ?, ?, ?)";
        
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, transaction.getTransactionId());
            stmt.setString(2, transaction.getAccountNumber());
            stmt.setTimestamp(3, Timestamp.valueOf(transaction.getDate()));
            stmt.setString(4, transaction.getType());
            stmt.setDouble(5, transaction.getAmount());
            stmt.setString(6, transaction.getDescription());
            
            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;
            
        } catch (SQLException e) {
            System.err.println("Error creating transaction: " + e.getMessage());
            return false;
        }
    }
    
    // Get all transactions (for admin purposes)
    public List<Transaction> getAllTransactions() {
        List<Transaction> transactions = new ArrayList<>();
        Connection conn = DatabaseConnection.getConnection();
        if (conn == null) {
            System.out.println("⚠️ Database not available - returning empty transaction list");
            return transactions;
        }
        
        String sql = "SELECT transaction_id, account_number, transaction_date, transaction_type, amount, description " +
                    "FROM transactions ORDER BY transaction_date DESC";
        
        try (PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            
            while (rs.next()) {
                Transaction transaction = new Transaction(
                    rs.getString("transaction_id"),
                    rs.getTimestamp("transaction_date").toLocalDateTime(),
                    rs.getString("transaction_type"),
                    rs.getDouble("amount"),
                    rs.getString("description"),
                    rs.getString("account_number")
                );
                transactions.add(transaction);
            }
        } catch (SQLException e) {
            System.err.println("Error retrieving all transactions: " + e.getMessage());
        }
        return transactions;
    }
    
    // Get transactions by type
    public List<Transaction> getTransactionsByType(String transactionType) {
        List<Transaction> transactions = new ArrayList<>();
        Connection conn = DatabaseConnection.getConnection();
        if (conn == null) {
            System.out.println("⚠️ Database not available - returning empty transaction list");
            return transactions;
        }
        
        String sql = "SELECT transaction_id, account_number, transaction_date, transaction_type, amount, description " +
                    "FROM transactions WHERE transaction_type = ? ORDER BY transaction_date DESC";
        
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, transactionType);
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
                Transaction transaction = new Transaction(
                    rs.getString("transaction_id"),
                    rs.getTimestamp("transaction_date").toLocalDateTime(),
                    rs.getString("transaction_type"),
                    rs.getDouble("amount"),
                    rs.getString("description"),
                    rs.getString("account_number")
                );
                transactions.add(transaction);
            }
        } catch (SQLException e) {
            System.err.println("Error retrieving transactions by type: " + e.getMessage());
        }
        return transactions;
    }
    
    // Get transactions within a date range
    public List<Transaction> getTransactionsByDateRange(LocalDateTime startDate, LocalDateTime endDate) {
        List<Transaction> transactions = new ArrayList<>();
        Connection conn = DatabaseConnection.getConnection();
        if (conn == null) {
            System.out.println("⚠️ Database not available - returning empty transaction list");
            return transactions;
        }
        
        String sql = "SELECT transaction_id, account_number, transaction_date, transaction_type, amount, description " +
                    "FROM transactions WHERE transaction_date BETWEEN ? AND ? ORDER BY transaction_date DESC";
        
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setTimestamp(1, Timestamp.valueOf(startDate));
            stmt.setTimestamp(2, Timestamp.valueOf(endDate));
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
                Transaction transaction = new Transaction(
                    rs.getString("transaction_id"),
                    rs.getTimestamp("transaction_date").toLocalDateTime(),
                    rs.getString("transaction_type"),
                    rs.getDouble("amount"),
                    rs.getString("description"),
                    rs.getString("account_number")
                );
                transactions.add(transaction);
            }
        } catch (SQLException e) {
            System.err.println("Error retrieving transactions by date range: " + e.getMessage());
        }
        return transactions;
    }
    
    // Get transaction by ID
    public Transaction getTransactionById(String transactionId) {
        Connection conn = DatabaseConnection.getConnection();
        if (conn == null) {
            System.out.println("⚠️ Database not available - cannot retrieve transaction");
            return null;
        }
        
        String sql = "SELECT transaction_id, account_number, transaction_date, transaction_type, amount, description " +
                    "FROM transactions WHERE transaction_id = ?";
        
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, transactionId);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                return new Transaction(
                    rs.getString("transaction_id"),
                    rs.getTimestamp("transaction_date").toLocalDateTime(),
                    rs.getString("transaction_type"),
                    rs.getDouble("amount"),
                    rs.getString("description"),
                    rs.getString("account_number")
                );
            }
        } catch (SQLException e) {
            System.err.println("Error retrieving transaction by ID: " + e.getMessage());
        }
        return null;
    }
    
    // Get transactions for multiple accounts (for customer dashboard)
    public List<Transaction> getTransactionsForAccounts(List<String> accountNumbers) {
        List<Transaction> transactions = new ArrayList<>();
        Connection conn = DatabaseConnection.getConnection();
        if (conn == null) {
            System.out.println("⚠️ Database not available - returning empty transaction list");
            return transactions;
        }
        
        if (accountNumbers.isEmpty()) {
            return transactions;
        }
        
        // Build the IN clause dynamically
        StringBuilder sqlBuilder = new StringBuilder(
            "SELECT transaction_id, account_number, transaction_date, transaction_type, amount, description " +
            "FROM transactions WHERE account_number IN ("
        );
        
        for (int i = 0; i < accountNumbers.size(); i++) {
            sqlBuilder.append("?");
            if (i < accountNumbers.size() - 1) {
                sqlBuilder.append(",");
            }
        }
        sqlBuilder.append(") ORDER BY transaction_date DESC");
        
        try (PreparedStatement stmt = conn.prepareStatement(sqlBuilder.toString())) {
            for (int i = 0; i < accountNumbers.size(); i++) {
                stmt.setString(i + 1, accountNumbers.get(i));
            }
            
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
                Transaction transaction = new Transaction(
                    rs.getString("transaction_id"),
                    rs.getTimestamp("transaction_date").toLocalDateTime(),
                    rs.getString("transaction_type"),
                    rs.getDouble("amount"),
                    rs.getString("description"),
                    rs.getString("account_number")
                );
                transactions.add(transaction);
            }
        } catch (SQLException e) {
            System.err.println("Error retrieving transactions for multiple accounts: " + e.getMessage());
        }
        return transactions;
    }
    
    // Get next transaction ID
    public String getNextTransactionId() {
        Connection conn = DatabaseConnection.getConnection();
        if (conn == null) {
            System.out.println("⚠️ Database not available - generating fallback transaction ID");
            return "TXN-" + System.currentTimeMillis();
        }
        
        String sql = "SELECT MAX(CAST(SUBSTRING(transaction_id, 5) AS UNSIGNED)) as max_id FROM transactions WHERE transaction_id LIKE 'TXN-%'";
        
        try (PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            
            if (rs.next()) {
                int maxId = rs.getInt("max_id");
                if (rs.wasNull()) {
                    return "TXN-10001";
                }
                return "TXN-" + (maxId + 1);
            }
        } catch (SQLException e) {
            System.err.println("Error generating next transaction ID: " + e.getMessage());
        }
        
        // Fallback if anything goes wrong
        return "TXN-" + System.currentTimeMillis();
    }
    
    // Get total transaction amount by type and account
    public double getTotalAmountByTypeAndAccount(String accountNumber, String transactionType) {
        Connection conn = DatabaseConnection.getConnection();
        if (conn == null) {
            System.out.println("⚠️ Database not available - cannot calculate total amount");
            return 0.0;
        }
        
        String sql = "SELECT SUM(amount) as total_amount FROM transactions WHERE account_number = ? AND transaction_type = ?";
        
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, accountNumber);
            stmt.setString(2, transactionType);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                return rs.getDouble("total_amount");
            }
        } catch (SQLException e) {
            System.err.println("Error calculating total amount by type: " + e.getMessage());
        }
        return 0.0;
    }
    
    // Get recent transactions (last N transactions)
    public List<Transaction> getRecentTransactions(int limit) {
        List<Transaction> transactions = new ArrayList<>();
        Connection conn = DatabaseConnection.getConnection();
        if (conn == null) {
            System.out.println("⚠️ Database not available - returning empty transaction list");
            return transactions;
        }
        
        String sql = "SELECT transaction_id, account_number, transaction_date, transaction_type, amount, description " +
                    "FROM transactions ORDER BY transaction_date DESC LIMIT ?";
        
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, limit);
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
                Transaction transaction = new Transaction(
                    rs.getString("transaction_id"),
                    rs.getTimestamp("transaction_date").toLocalDateTime(),
                    rs.getString("transaction_type"),
                    rs.getDouble("amount"),
                    rs.getString("description"),
                    rs.getString("account_number")
                );
                transactions.add(transaction);
            }
        } catch (SQLException e) {
            System.err.println("Error retrieving recent transactions: " + e.getMessage());
        }
        return transactions;
    }
    
    // Delete transaction (admin function)
    public boolean deleteTransaction(String transactionId) {
        Connection conn = DatabaseConnection.getConnection();
        if (conn == null) {
            System.out.println("⚠️ Database not available - cannot delete transaction");
            return false;
        }
        
        String sql = "DELETE FROM transactions WHERE transaction_id = ?";
        
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, transactionId);
            
            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;
            
        } catch (SQLException e) {
            System.err.println("Error deleting transaction: " + e.getMessage());
            return false;
        }
    }
    
    // Check if transaction exists
    public boolean transactionExists(String transactionId) {
        Connection conn = DatabaseConnection.getConnection();
        if (conn == null) {
            System.out.println("⚠️ Database not available - cannot check transaction existence");
            return false;
        }
        
        String sql = "SELECT 1 FROM transactions WHERE transaction_id = ?";
        
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, transactionId);
            ResultSet rs = stmt.executeQuery();
            return rs.next();
            
        } catch (SQLException e) {
            System.err.println("Error checking transaction existence: " + e.getMessage());
            return false;
        }
    }
}