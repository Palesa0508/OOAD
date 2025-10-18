package boundaries;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class LoginController {

    @FXML private TextField customerIdField;
    @FXML private PasswordField passwordField;
    @FXML private Button loginButton;
    @FXML private Button registerButton;

    @FXML
    private void handleLogin() {
        String customerId = customerIdField.getText();
        String password = passwordField.getText();
        
        // Only UI logic - validation of input format
        if (customerId.isEmpty() || password.isEmpty()) {
            showAlert("Error", "Please enter both Customer ID and Password");
            return;
        }
        
        // Delegate authentication to control class (would be called here)
        // boolean isValid = authenticationControl.authenticate(customerId, password);
        
        // For now, just navigate to dashboard
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/account_dashboard.fxml"));
            Parent root = loader.load();
            
            Stage stage = (Stage) loginButton.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Account Dashboard - Palesa Britany Leeto");
        } catch (Exception e) {
            showAlert("Error", "Cannot load dashboard: " + e.getMessage());
        }
    }

    @FXML
    private void handleRegister() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/customer_registration.fxml"));
            Parent root = loader.load();
            
            Stage stage = (Stage) registerButton.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Customer Registration");
        } catch (Exception e) {
            showAlert("Error", "Cannot load registration form: " + e.getMessage());
        }
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}