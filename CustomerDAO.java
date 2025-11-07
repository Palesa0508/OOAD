import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CustomerDAO {
    
    // Read all customers
    public List<Customer> getAllCustomers() {
        List<Customer> customers = new ArrayList<>();
        Connection conn = DatabaseConnection.getConnection();
        if (conn == null) {
            System.out.println("⚠️ Database not available - returning empty customer list");
            return customers;
        }
        
        String sql = "SELECT u.username, u.password, u.first_name, u.last_name, " +
                    "c.customer_id, c.phone, c.email, c.address, c.date_of_birth, " +
                    "c.nationality, c.id_number, c.kin_name, c.kin_relationship, " +
                    "c.kin_phone, c.kin_address " +
                    "FROM users u JOIN customers c ON u.username = c.username";
        
        try (PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            
            while (rs.next()) {
                Customer customer = new Customer(
                    rs.getString("username"),
                    rs.getString("password"),
                    rs.getString("first_name"),
                    rs.getString("last_name"),
                    rs.getString("phone"),
                    rs.getString("email"),
                    rs.getString("address"),
                    rs.getString("date_of_birth"),
                    rs.getString("nationality")
                );
                customer.setCustomerId(rs.getString("customer_id"));
                customer.setIdNumber(rs.getString("id_number"));
                customer.setKinName(rs.getString("kin_name"));
                customer.setKinRelationship(rs.getString("kin_relationship"));
                customer.setKinPhone(rs.getString("kin_phone"));
                customer.setKinAddress(rs.getString("kin_address"));
                
                customers.add(customer);
            }
        } catch (SQLException e) {
            System.err.println("Error retrieving customers: " + e.getMessage());
        }
        return customers;
    }
    
    // Get customer by username
    public Customer getCustomerByUsername(String username) {
        Connection conn = DatabaseConnection.getConnection();
        if (conn == null) {
            System.out.println("⚠️ Database not available - returning null");
            return null;
        }
        
        String sql = "SELECT u.username, u.password, u.first_name, u.last_name, " +
                    "c.customer_id, c.phone, c.email, c.address, c.date_of_birth, " +
                    "c.nationality, c.id_number, c.kin_name, c.kin_relationship, " +
                    "c.kin_phone, c.kin_address " +
                    "FROM users u JOIN customers c ON u.username = c.username " +
                    "WHERE u.username = ?";
        
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, username);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                Customer customer = new Customer(
                    rs.getString("username"),
                    rs.getString("password"),
                    rs.getString("first_name"),
                    rs.getString("last_name"),
                    rs.getString("phone"),
                    rs.getString("email"),
                    rs.getString("address"),
                    rs.getString("date_of_birth"),
                    rs.getString("nationality")
                );
                customer.setCustomerId(rs.getString("customer_id"));
                customer.setIdNumber(rs.getString("id_number"));
                customer.setKinName(rs.getString("kin_name"));
                customer.setKinRelationship(rs.getString("kin_relationship"));
                customer.setKinPhone(rs.getString("kin_phone"));
                customer.setKinAddress(rs.getString("kin_address"));
                
                return customer;
            }
        } catch (SQLException e) {
            System.err.println("Error retrieving customer by username: " + e.getMessage());
        }
        return null;
    }
    
    // Get customer by customer ID
    public Customer getCustomerByCustomerId(String customerId) {
        Connection conn = DatabaseConnection.getConnection();
        if (conn == null) {
            System.out.println("⚠️ Database not available - returning null");
            return null;
        }
        
        String sql = "SELECT u.username, u.password, u.first_name, u.last_name, " +
                    "c.customer_id, c.phone, c.email, c.address, c.date_of_birth, " +
                    "c.nationality, c.id_number, c.kin_name, c.kin_relationship, " +
                    "c.kin_phone, c.kin_address " +
                    "FROM users u JOIN customers c ON u.username = c.username " +
                    "WHERE c.customer_id = ?";
        
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, customerId);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                Customer customer = new Customer(
                    rs.getString("username"),
                    rs.getString("password"),
                    rs.getString("first_name"),
                    rs.getString("last_name"),
                    rs.getString("phone"),
                    rs.getString("email"),
                    rs.getString("address"),
                    rs.getString("date_of_birth"),
                    rs.getString("nationality")
                );
                customer.setCustomerId(rs.getString("customer_id"));
                customer.setIdNumber(rs.getString("id_number"));
                customer.setKinName(rs.getString("kin_name"));
                customer.setKinRelationship(rs.getString("kin_relationship"));
                customer.setKinPhone(rs.getString("kin_phone"));
                customer.setKinAddress(rs.getString("kin_address"));
                
                return customer;
            }
        } catch (SQLException e) {
            System.err.println("Error retrieving customer by ID: " + e.getMessage());
        }
        return null;
    }
    
    // Create new customer
    public boolean createCustomer(Customer customer) {
        Connection conn = DatabaseConnection.getConnection();
        if (conn == null) {
            System.out.println("⚠️ Database not available - cannot create customer");
            return false;
        }
        
        try {
            conn.setAutoCommit(false); // Start transaction
            
            // Insert into users table
            String userSql = "INSERT INTO users (username, password, first_name, last_name, role) VALUES (?, ?, ?, ?, ?)";
            try (PreparedStatement userStmt = conn.prepareStatement(userSql)) {
                userStmt.setString(1, customer.getUsername());
                userStmt.setString(2, customer.getPassword());
                userStmt.setString(3, customer.getFirstName());
                userStmt.setString(4, customer.getLastName());
                userStmt.setString(5, "CUSTOMER");
                
                int userRows = userStmt.executeUpdate();
                if (userRows == 0) {
                    conn.rollback();
                    return false;
                }
            }
            
            // Insert into customers table
            String customerSql = "INSERT INTO customers (customer_id, username, phone, email, address, date_of_birth, " +
                                "nationality, id_number, kin_name, kin_relationship, kin_phone, kin_address) " +
                                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
            try (PreparedStatement customerStmt = conn.prepareStatement(customerSql)) {
                customerStmt.setString(1, customer.getCustomerId());
                customerStmt.setString(2, customer.getUsername());
                customerStmt.setString(3, customer.getPhone());
                customerStmt.setString(4, customer.getEmail());
                customerStmt.setString(5, customer.getAddress());
                customerStmt.setString(6, customer.getDateOfBirth());
                customerStmt.setString(7, customer.getNationality());
                customerStmt.setString(8, customer.getIdNumber());
                customerStmt.setString(9, customer.getKinName());
                customerStmt.setString(10, customer.getKinRelationship());
                customerStmt.setString(11, customer.getKinPhone());
                customerStmt.setString(12, customer.getKinAddress());
                
                int customerRows = customerStmt.executeUpdate();
                if (customerRows == 0) {
                    conn.rollback();
                    return false;
                }
            }
            
            conn.commit();
            return true;
            
        } catch (SQLException e) {
            try {
                conn.rollback();
            } catch (SQLException rollbackEx) {
                System.err.println("Error rolling back transaction: " + rollbackEx.getMessage());
            }
            System.err.println("Error creating customer: " + e.getMessage());
            return false;
        } finally {
            try {
                conn.setAutoCommit(true);
            } catch (SQLException e) {
                System.err.println("Error resetting auto-commit: " + e.getMessage());
            }
        }
    }
    
    // Update customer
    public boolean updateCustomer(Customer customer) {
        Connection conn = DatabaseConnection.getConnection();
        if (conn == null) {
            System.out.println("⚠️ Database not available - cannot update customer");
            return false;
        }
        
        try {
            conn.setAutoCommit(false); // Start transaction
            
            // Update users table
            String userSql = "UPDATE users SET first_name = ?, last_name = ?, password = ? WHERE username = ?";
            try (PreparedStatement userStmt = conn.prepareStatement(userSql)) {
                userStmt.setString(1, customer.getFirstName());
                userStmt.setString(2, customer.getLastName());
                userStmt.setString(3, customer.getPassword());
                userStmt.setString(4, customer.getUsername());
                
                int userRows = userStmt.executeUpdate();
                if (userRows == 0) {
                    conn.rollback();
                    return false;
                }
            }
            
            // Update customers table
            String customerSql = "UPDATE customers SET phone = ?, email = ?, address = ?, date_of_birth = ?, " +
                                "nationality = ?, id_number = ?, kin_name = ?, kin_relationship = ?, " +
                                "kin_phone = ?, kin_address = ? WHERE username = ?";
            try (PreparedStatement customerStmt = conn.prepareStatement(customerSql)) {
                customerStmt.setString(1, customer.getPhone());
                customerStmt.setString(2, customer.getEmail());
                customerStmt.setString(3, customer.getAddress());
                customerStmt.setString(4, customer.getDateOfBirth());
                customerStmt.setString(5, customer.getNationality());
                customerStmt.setString(6, customer.getIdNumber());
                customerStmt.setString(7, customer.getKinName());
                customerStmt.setString(8, customer.getKinRelationship());
                customerStmt.setString(9, customer.getKinPhone());
                customerStmt.setString(10, customer.getKinAddress());
                customerStmt.setString(11, customer.getUsername());
                
                int customerRows = customerStmt.executeUpdate();
                if (customerRows == 0) {
                    conn.rollback();
                    return false;
                }
            }
            
            conn.commit();
            return true;
            
        } catch (SQLException e) {
            try {
                conn.rollback();
            } catch (SQLException rollbackEx) {
                System.err.println("Error rolling back transaction: " + rollbackEx.getMessage());
            }
            System.err.println("Error updating customer: " + e.getMessage());
            return false;
        } finally {
            try {
                conn.setAutoCommit(true);
            } catch (SQLException e) {
                System.err.println("Error resetting auto-commit: " + e.getMessage());
            }
        }
    }
    
    // Delete customer
    public boolean deleteCustomer(String username) {
        Connection conn = DatabaseConnection.getConnection();
        if (conn == null) {
            System.out.println("⚠️ Database not available - cannot delete customer");
            return false;
        }
        
        try {
            conn.setAutoCommit(false); // Start transaction
            
            // Delete from customers table first (due to foreign key constraint)
            String customerSql = "DELETE FROM customers WHERE username = ?";
            try (PreparedStatement customerStmt = conn.prepareStatement(customerSql)) {
                customerStmt.setString(1, username);
                int customerRows = customerStmt.executeUpdate();
                // We don't check rows here because customer might not exist in customers table
            }
            
            // Delete from users table
            String userSql = "DELETE FROM users WHERE username = ?";
            try (PreparedStatement userStmt = conn.prepareStatement(userSql)) {
                userStmt.setString(1, username);
                int userRows = userStmt.executeUpdate();
                
                if (userRows > 0) {
                    conn.commit();
                    return true;
                } else {
                    conn.rollback();
                    return false;
                }
            }
            
        } catch (SQLException e) {
            try {
                conn.rollback();
            } catch (SQLException rollbackEx) {
                System.err.println("Error rolling back transaction: " + rollbackEx.getMessage());
            }
            System.err.println("Error deleting customer: " + e.getMessage());
            return false;
        } finally {
            try {
                conn.setAutoCommit(true);
            } catch (SQLException e) {
                System.err.println("Error resetting auto-commit: " + e.getMessage());
            }
        }
    }
    
    // Check if customer exists by username
    public boolean customerExists(String username) {
        Connection conn = DatabaseConnection.getConnection();
        if (conn == null) {
            System.out.println("⚠️ Database not available - cannot check customer existence");
            return false;
        }
        
        String sql = "SELECT 1 FROM customers WHERE username = ?";
        
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, username);
            ResultSet rs = stmt.executeQuery();
            return rs.next();
            
        } catch (SQLException e) {
            System.err.println("Error checking customer existence: " + e.getMessage());
            return false;
        }
    }
    
    // Check if customer exists by customer ID
    public boolean customerExistsByCustomerId(String customerId) {
        Connection conn = DatabaseConnection.getConnection();
        if (conn == null) {
            System.out.println("⚠️ Database not available - cannot check customer existence");
            return false;
        }
        
        String sql = "SELECT 1 FROM customers WHERE customer_id = ?";
        
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, customerId);
            ResultSet rs = stmt.executeQuery();
            return rs.next();
            
        } catch (SQLException e) {
            System.err.println("Error checking customer existence by ID: " + e.getMessage());
            return false;
        }
    }
    
    // Get next customer ID
    public String getNextCustomerId() {
        Connection conn = DatabaseConnection.getConnection();
        if (conn == null) {
            System.out.println("⚠️ Database not available - generating fallback customer ID");
            return "CUST-" + System.currentTimeMillis();
        }
        
        String sql = "SELECT MAX(CAST(SUBSTRING(customer_id, 6) AS UNSIGNED)) as max_id FROM customers WHERE customer_id LIKE 'CUST-%'";
        
        try (PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            
            if (rs.next()) {
                int maxId = rs.getInt("max_id");
                if (rs.wasNull()) {
                    return "CUST-10001";
                }
                return "CUST-" + (maxId + 1);
            }
        } catch (SQLException e) {
            System.err.println("Error generating next customer ID: " + e.getMessage());
        }
        
        // Fallback if anything goes wrong
        return "CUST-" + System.currentTimeMillis();
    }
    
    // Search customers by name
    public List<Customer> searchCustomersByName(String searchTerm) {
        List<Customer> customers = new ArrayList<>();
        Connection conn = DatabaseConnection.getConnection();
        if (conn == null) {
            System.out.println("⚠️ Database not available - returning empty customer list");
            return customers;
        }
        
        String sql = "SELECT u.username, u.password, u.first_name, u.last_name, " +
                    "c.customer_id, c.phone, c.email, c.address, c.date_of_birth, " +
                    "c.nationality, c.id_number, c.kin_name, c.kin_relationship, " +
                    "c.kin_phone, c.kin_address " +
                    "FROM users u JOIN customers c ON u.username = c.username " +
                    "WHERE u.first_name LIKE ? OR u.last_name LIKE ? OR CONCAT(u.first_name, ' ', u.last_name) LIKE ?";
        
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            String likeTerm = "%" + searchTerm + "%";
            stmt.setString(1, likeTerm);
            stmt.setString(2, likeTerm);
            stmt.setString(3, likeTerm);
            
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
                Customer customer = new Customer(
                    rs.getString("username"),
                    rs.getString("password"),
                    rs.getString("first_name"),
                    rs.getString("last_name"),
                    rs.getString("phone"),
                    rs.getString("email"),
                    rs.getString("address"),
                    rs.getString("date_of_birth"),
                    rs.getString("nationality")
                );
                customer.setCustomerId(rs.getString("customer_id"));
                customer.setIdNumber(rs.getString("id_number"));
                customer.setKinName(rs.getString("kin_name"));
                customer.setKinRelationship(rs.getString("kin_relationship"));
                customer.setKinPhone(rs.getString("kin_phone"));
                customer.setKinAddress(rs.getString("kin_address"));
                
                customers.add(customer);
            }
        } catch (SQLException e) {
            System.err.println("Error searching customers by name: " + e.getMessage());
        }
        return customers;
    }
    
    // Get total number of customers
    public int getTotalCustomers() {
        Connection conn = DatabaseConnection.getConnection();
        if (conn == null) {
            System.out.println("⚠️ Database not available - returning 0");
            return 0;
        }
        
        String sql = "SELECT COUNT(*) as total FROM customers";
        
        try (PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            
            if (rs.next()) {
                return rs.getInt("total");
            }
        } catch (SQLException e) {
            System.err.println("Error getting total customers: " + e.getMessage());
        }
        return 0;
    }
}