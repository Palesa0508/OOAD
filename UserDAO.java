import java.sql.*;

public class UserDAO {
    
    public User authenticateUser(String username, String password) {
        Connection conn = DatabaseConnection.getConnection();
        if (conn == null) {
            System.out.println("⚠️ Database not available - returning null");
            return null;
        }
        
        String sql = "SELECT u.username, u.password, u.first_name, u.last_name, u.user_type, " +
                    "c.customer_id, c.phone, c.email, c.address, c.date_of_birth, c.nationality, " +
                    "c.id_number, c.kin_name, c.kin_relationship, c.kin_phone, c.kin_address " +
                    "FROM users u LEFT JOIN customers c ON u.username = c.username " +
                    "WHERE u.username = ? AND u.password = ?";
        
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, username);
            stmt.setString(2, password);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                String userType = rs.getString("user_type");
                if ("TELLER".equals(userType)) {
                    return new Teller(
                        rs.getString("username"),
                        rs.getString("password"),
                        rs.getString("first_name"),
                        rs.getString("last_name")
                    );
                } else if ("CUSTOMER".equals(userType)) {
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
            }
        } catch (SQLException e) {
            System.err.println("Error authenticating user: " + e.getMessage());
        }
        return null;
    }
}