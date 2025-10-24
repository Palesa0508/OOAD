import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class LoginController {
    
    @FXML private TextField usernameField;
    @FXML private PasswordField passwordField;
    
    private List<User> users = new ArrayList<>();
    
    @FXML
    public void initialize() {
        // Create demo users
        users.add(new Teller("britany.kgaboetsile", "teller123", "Britany", "Kgaboetsile"));
        
        Customer demoCustomer = new Customer("palesa.leeto", "customer123", "Palesa", "Leeto", 
                "+267 123 4567", "palesa.customer@palesbank.com", "Gaborone, Botswana", 
                "1990-01-01", "Botswana");
        demoCustomer.setCustomerId("CUST001");
        demoCustomer.setIdNumber("ID001");
        users.add(demoCustomer);
        
        System.out.println("LoginController initialized with " + users.size() + " demo users");
    }
    
    @FXML
    private void handleLogin() {
        String username = usernameField.getText();
        String password = passwordField.getText();
        
        if (username.isEmpty() || password.isEmpty()) {
            showAlert("Error", "Please enter both username and password");
            return;
        }
        
        User authenticatedUser = authenticateUser(username, password);
        
        if (authenticatedUser != null) {
            try {
                redirectToDashboard(authenticatedUser);
            } catch (IOException e) {
                e.printStackTrace();
                showAlert("Error", "Failed to load dashboard: " + e.getMessage());
            }
        } else {
            showAlert("Invalid Credentials", "Invalid username or password. Please try again.");
        }
    }
    
    private User authenticateUser(String username, String password) {
        for (User user : users) {
            if (user.getUsername().equals(username) && user.getPassword().equals(password)) {
                return user;
            }
        }
        return null;
    }
    
    private void redirectToDashboard(User user) throws IOException {
        Stage stage = (Stage) usernameField.getScene().getWindow();
        Parent root;
        
        if (user instanceof Teller) {
            System.out.println("Loading Teller Dashboard...");
            FXMLLoader loader = new FXMLLoader(getClass().getResource("TellerDashboard.fxml"));
            root = loader.load();
            
            // Get the controller and set the teller
            TellerDashboardController controller = loader.getController();
            controller.setCurrentTeller((Teller) user);
        } else {
            System.out.println("Loading Customer Dashboard...");
            FXMLLoader loader = new FXMLLoader(getClass().getResource("CustomerDashboard.fxml"));
            root = loader.load();
            
            // Get the controller and set the customer
            CustomerDashboardController controller = loader.getController();
            controller.setCurrentCustomer((Customer) user);
        }
        
        Scene scene = new Scene(root, 1200, 800);
        try {
            scene.getStylesheets().add(getClass().getResource("styles.css").toExternalForm());
        } catch (Exception e) {
            System.out.println("CSS not loaded for dashboard: " + e.getMessage());
        }
        stage.setScene(scene);
        stage.centerOnScreen();
    }
    
    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
    
    @FXML
    private void showDemoCredentials() {
        String demoInfo = "Demo Credentials:\n\n" +
                "Teller Login:\nUsername: britany.kgaboetsile\nPassword: teller123\n\n" +
                "Customer Login:\nUsername: palesa.leeto\nPassword: customer123";
        
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Demo Credentials");
        alert.setHeaderText(null);
        alert.setContentText(demoInfo);
        alert.showAndWait();
    }
}