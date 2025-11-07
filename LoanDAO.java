import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class LoanDAO {
    
    // Read all loans for a customer
    public List<Loan> getLoansByCustomer(String customerUsername, CustomerDAO customerDAO) {
        List<Loan> loans = new ArrayList<>();
        Connection conn = DatabaseConnection.getConnection();
        if (conn == null) {
            System.out.println("⚠️ Database not available - returning empty loan list");
            return loans;
        }
        
        String sql = "SELECT loan_id, amount, purpose, application_date, status, customer_username " +
                    "FROM loans WHERE customer_username = ? ORDER BY application_date DESC";
        
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, customerUsername);
            ResultSet rs = stmt.executeQuery();
            
            Customer customer = customerDAO.getCustomerByUsername(customerUsername);
            if (customer == null) return loans;
            
            while (rs.next()) {
                // Use the constructor that matches your Loan class
                Loan loan = new Loan(
                    rs.getString("loan_id"),
                    rs.getDouble("amount"),
                    rs.getString("purpose"),
                    customer
                );
                // If your Loan class has status field in constructor, add it there
                // Otherwise, use setStatus if available
                
                loans.add(loan);
            }
        } catch (SQLException e) {
            System.err.println("Error retrieving customer loans: " + e.getMessage());
        }
        return loans;
    }
    
    // Get all loans (for admin purposes)
    public List<Loan> getAllLoans(CustomerDAO customerDAO) {
        List<Loan> loans = new ArrayList<>();
        Connection conn = DatabaseConnection.getConnection();
        if (conn == null) {
            System.out.println("⚠️ Database not available - returning empty loan list");
            return loans;
        }
        
        String sql = "SELECT loan_id, amount, purpose, application_date, status, customer_username " +
                    "FROM loans ORDER BY application_date DESC";
        
        try (PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            
            while (rs.next()) {
                String customerUsername = rs.getString("customer_username");
                Customer customer = customerDAO.getCustomerByUsername(customerUsername);
                
                if (customer == null) continue;
                
                Loan loan = new Loan(
                    rs.getString("loan_id"),
                    rs.getDouble("amount"),
                    rs.getString("purpose"),
                    customer
                );
                
                loans.add(loan);
            }
        } catch (SQLException e) {
            System.err.println("Error retrieving all loans: " + e.getMessage());
        }
        return loans;
    }
    
    // Get loan by ID
    public Loan getLoanById(String loanId, CustomerDAO customerDAO) {
        Connection conn = DatabaseConnection.getConnection();
        if (conn == null) {
            System.out.println("⚠️ Database not available - cannot retrieve loan");
            return null;
        }
        
        String sql = "SELECT loan_id, amount, purpose, application_date, status, customer_username " +
                    "FROM loans WHERE loan_id = ?";
        
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, loanId);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                String customerUsername = rs.getString("customer_username");
                Customer customer = customerDAO.getCustomerByUsername(customerUsername);
                
                if (customer == null) return null;
                
                Loan loan = new Loan(
                    rs.getString("loan_id"),
                    rs.getDouble("amount"),
                    rs.getString("purpose"),
                    customer
                );
                
                return loan;
            }
        } catch (SQLException e) {
            System.err.println("Error retrieving loan by ID: " + e.getMessage());
        }
        return null;
    }
    
    // Create new loan
    public boolean createLoan(Loan loan) {
        Connection conn = DatabaseConnection.getConnection();
        if (conn == null) {
            System.out.println("⚠️ Database not available - cannot create loan");
            return false;
        }
        
        String sql = "INSERT INTO loans (loan_id, amount, purpose, application_date, status, customer_username) " +
                    "VALUES (?, ?, ?, ?, ?, ?)";
        
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, loan.getLoanId());
            stmt.setDouble(2, loan.getAmount());
            stmt.setString(3, loan.getPurpose());
            stmt.setTimestamp(4, Timestamp.valueOf(java.time.LocalDateTime.now())); // Use current date
            stmt.setString(5, "PENDING"); // Default status
            stmt.setString(6, loan.getCustomer().getUsername());
            
            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;
            
        } catch (SQLException e) {
            System.err.println("Error creating loan: " + e.getMessage());
            return false;
        }
    }
    
    // Update loan status
    public boolean updateLoanStatus(String loanId, String newStatus) {
        Connection conn = DatabaseConnection.getConnection();
        if (conn == null) {
            System.out.println("⚠️ Database not available - cannot update loan status");
            return false;
        }
        
        String sql = "UPDATE loans SET status = ? WHERE loan_id = ?";
        
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, newStatus);
            stmt.setString(2, loanId);
            
            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;
            
        } catch (SQLException e) {
            System.err.println("Error updating loan status: " + e.getMessage());
            return false;
        }
    }
    
    // Update loan details
    public boolean updateLoan(Loan loan) {
        Connection conn = DatabaseConnection.getConnection();
        if (conn == null) {
            System.out.println("⚠️ Database not available - cannot update loan");
            return false;
        }
        
        String sql = "UPDATE loans SET amount = ?, purpose = ?, status = ? WHERE loan_id = ?";
        
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setDouble(1, loan.getAmount());
            stmt.setString(2, loan.getPurpose());
            stmt.setString(3, loan.getStatus());
            stmt.setString(4, loan.getLoanId());
            
            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;
            
        } catch (SQLException e) {
            System.err.println("Error updating loan: " + e.getMessage());
            return false;
        }
    }
    
    // Delete loan
    public boolean deleteLoan(String loanId) {
        Connection conn = DatabaseConnection.getConnection();
        if (conn == null) {
            System.out.println("⚠️ Database not available - cannot delete loan");
            return false;
        }
        
        String sql = "DELETE FROM loans WHERE loan_id = ?";
        
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, loanId);
            
            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;
            
        } catch (SQLException e) {
            System.err.println("Error deleting loan: " + e.getMessage());
            return false;
        }
    }
    
    // Get loans by status
    public List<Loan> getLoansByStatus(String status, CustomerDAO customerDAO) {
        List<Loan> loans = new ArrayList<>();
        Connection conn = DatabaseConnection.getConnection();
        if (conn == null) {
            System.out.println("⚠️ Database not available - returning empty loan list");
            return loans;
        }
        
        String sql = "SELECT loan_id, amount, purpose, application_date, status, customer_username " +
                    "FROM loans WHERE status = ? ORDER BY application_date DESC";
        
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, status);
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
                String customerUsername = rs.getString("customer_username");
                Customer customer = customerDAO.getCustomerByUsername(customerUsername);
                
                if (customer == null) continue;
                
                Loan loan = new Loan(
                    rs.getString("loan_id"),
                    rs.getDouble("amount"),
                    rs.getString("purpose"),
                    customer
                );
                
                loans.add(loan);
            }
        } catch (SQLException e) {
            System.err.println("Error retrieving loans by status: " + e.getMessage());
        }
        return loans;
    }
    
    // Get next loan ID
    public String getNextLoanId() {
        Connection conn = DatabaseConnection.getConnection();
        if (conn == null) {
            System.out.println("⚠️ Database not available - generating fallback loan ID");
            return "LN-" + System.currentTimeMillis();
        }
        
        String sql = "SELECT MAX(CAST(SUBSTRING(loan_id, 4) AS UNSIGNED)) as max_id FROM loans WHERE loan_id LIKE 'LN-%'";
        
        try (PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            
            if (rs.next()) {
                int maxId = rs.getInt("max_id");
                if (rs.wasNull()) {
                    return "LN-10001";
                }
                return "LN-" + (maxId + 1);
            }
        } catch (SQLException e) {
            System.err.println("Error generating next loan ID: " + e.getMessage());
        }
        
        // Fallback if anything goes wrong
        return "LN-" + System.currentTimeMillis();
    }
    
    // Check if loan exists
    public boolean loanExists(String loanId) {
        Connection conn = DatabaseConnection.getConnection();
        if (conn == null) {
            System.out.println("⚠️ Database not available - cannot check loan existence");
            return false;
        }
        
        String sql = "SELECT 1 FROM loans WHERE loan_id = ?";
        
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, loanId);
            ResultSet rs = stmt.executeQuery();
            return rs.next();
            
        } catch (SQLException e) {
            System.err.println("Error checking loan existence: " + e.getMessage());
            return false;
        }
    }
    
    // Get total loan amount for a customer
    public double getTotalLoanAmountForCustomer(String customerUsername) {
        Connection conn = DatabaseConnection.getConnection();
        if (conn == null) {
            System.out.println("⚠️ Database not available - cannot calculate total loan amount");
            return 0.0;
        }
        
        String sql = "SELECT SUM(amount) as total_amount FROM loans WHERE customer_username = ? AND status = 'APPROVED'";
        
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, customerUsername);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                return rs.getDouble("total_amount");
            }
        } catch (SQLException e) {
            System.err.println("Error calculating total loan amount: " + e.getMessage());
        }
        return 0.0;
    }
    
    // Get pending loans count (for admin dashboard)
    public int getPendingLoansCount() {
        Connection conn = DatabaseConnection.getConnection();
        if (conn == null) {
            System.out.println("⚠️ Database not available - returning 0");
            return 0;
        }
        
        String sql = "SELECT COUNT(*) as pending_count FROM loans WHERE status = 'PENDING'";
        
        try (PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            
            if (rs.next()) {
                return rs.getInt("pending_count");
            }
        } catch (SQLException e) {
            System.err.println("Error getting pending loans count: " + e.getMessage());
        }
        return 0;
    }
    
    // Get total loans count
    public int getTotalLoansCount() {
        Connection conn = DatabaseConnection.getConnection();
        if (conn == null) {
            System.out.println("⚠️ Database not available - returning 0");
            return 0;
        }
        
        String sql = "SELECT COUNT(*) as total_count FROM loans";
        
        try (PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            
            if (rs.next()) {
                return rs.getInt("total_count");
            }
        } catch (SQLException e) {
            System.err.println("Error getting total loans count: " + e.getMessage());
        }
        return 0;
    }
    
    // Get loans with pagination (for admin interface)
    public List<Loan> getLoansWithPagination(int offset, int limit, CustomerDAO customerDAO) {
        List<Loan> loans = new ArrayList<>();
        Connection conn = DatabaseConnection.getConnection();
        if (conn == null) {
            System.out.println("⚠️ Database not available - returning empty loan list");
            return loans;
        }
        
        String sql = "SELECT loan_id, amount, purpose, application_date, status, customer_username " +
                    "FROM loans ORDER BY application_date DESC LIMIT ? OFFSET ?";
        
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, limit);
            stmt.setInt(2, offset);
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
                String customerUsername = rs.getString("customer_username");
                Customer customer = customerDAO.getCustomerByUsername(customerUsername);
                
                if (customer == null) continue;
                
                Loan loan = new Loan(
                    rs.getString("loan_id"),
                    rs.getDouble("amount"),
                    rs.getString("purpose"),
                    customer
                );
                
                loans.add(loan);
            }
        } catch (SQLException e) {
            System.err.println("Error retrieving loans with pagination: " + e.getMessage());
        }
        return loans;
    }
    
    // Get loan status
    public String getLoanStatus(String loanId) {
        Connection conn = DatabaseConnection.getConnection();
        if (conn == null) {
            System.out.println("⚠️ Database not available - cannot retrieve loan status");
            return "UNKNOWN";
        }
        
        String sql = "SELECT status FROM loans WHERE loan_id = ?";
        
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, loanId);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                return rs.getString("status");
            }
        } catch (SQLException e) {
            System.err.println("Error retrieving loan status: " + e.getMessage());
        }
        return "UNKNOWN";
    }
}