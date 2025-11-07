import java.sql.*;
import java.util.Properties;

public class DatabaseConnection {
    private static final String URL = "jdbc:oracle:thin:@livesql.oracle.com:1521:ORCL";
    private static final String USER = "cse24-084@thuto.bac.ac.bw";
    private static final String PASSWORD = "LetangPalesa1808!&";
    
    private static Connection connection = null;
    private static boolean useDatabase = true; // Flag to track if database is available
    
    public static Connection getConnection() {
        if (!useDatabase) {
            return null; // Return null if database is not available
        }
        
        try {
            if (connection == null || connection.isClosed()) {
                // Load Oracle JDBC driver
                Class.forName("oracle.jdbc.driver.OracleDriver");
                
                // Set connection properties
                Properties props = new Properties();
                props.setProperty("user", USER);
                props.setProperty("password", PASSWORD);
                props.setProperty("oracle.net.CONNECT_TIMEOUT", "5000"); // Reduced timeout
                
                connection = DriverManager.getConnection(URL, props);
                System.out.println("✅ Connected to Oracle Live SQL successfully!");
            }
        } catch (ClassNotFoundException e) {
            System.err.println("❌ Oracle JDBC Driver not found!");
            useDatabase = false;
        } catch (SQLException e) {
            System.err.println("❌ Database connection failed: " + e.getMessage());
            System.err.println("🔄 Falling back to in-memory data storage");
            useDatabase = false;
        }
        return connection;
    }
    
    public static boolean isDatabaseAvailable() {
        return useDatabase;
    }
    
    public static void closeConnection() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
                System.out.println("Database connection closed.");
            }
        } catch (SQLException e) {
            System.err.println("Error closing connection: " + e.getMessage());
        }
    }
}