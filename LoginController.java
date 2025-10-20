import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class LoginController {

    @FXML private TextField usernameField;
    @FXML private PasswordField passwordField;

    private LoginService loginService;

    public LoginController() {
        this.loginService = new LoginService();
    }

    @FXML
    private void handleLogin() {
        String username = usernameField.getText();
        String password = passwordField.getText();

        if (username.isEmpty() || password.isEmpty()) {
            showAlert("Login Error", "Please enter both username and password");
            return;
        }

        try {
            // Authenticate user using LoginService
            AuthContext authContext = loginService.login(username, password);

            if (authContext != null) {
                // Use the type-safe getters from AuthContext
                if (authContext.isBankTeller()) {
                    BankTeller teller = authContext.getBankTeller();
                    openTellerDashboard(teller);
                } else if (authContext.isCustomer()) {
                    Customer customer = authContext.getCustomer();
                    openCustomerDashboard(customer);
                } else {
                    showAlert("Login Error", "Invalid user type");
                }
            } else {
                showAlert("Login Error", "Invalid username or password");
            }

        } catch (Exception e) {
            showAlert("Login Error", "Error during login: " + e.getMessage());
        }
    }

    private void openTellerDashboard(BankTeller teller) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("tellerLanding.fxml"));
            Parent root = loader.load();

            TellerLandingController controller = loader.getController();
            controller.setBankTeller(teller);

            Stage stage = new Stage();
            stage.setTitle("Bank Teller Portal - " + teller.getFullName());
            stage.setScene(new Scene(root, 800, 600));
            stage.setResizable(false);
            stage.show();

            // Close login window
            Stage loginStage = (Stage) usernameField.getScene().getWindow();
            loginStage.close();

        } catch (Exception e) {
            showAlert("Error", "Failed to open teller dashboard: " + e.getMessage());
        }
    }

    private void openCustomerDashboard(Customer customer) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("customerDashboard.fxml"));
            Parent root = loader.load();

            CustomerDashboardController controller = loader.getController();
            controller.setCustomer(customer);

            Stage stage = new Stage();

            // Use displayName which works for both Individual and Company
            String displayName = customer.getDisplayName();
            stage.setTitle("Customer Portal - " + displayName);
            stage.setScene(new Scene(root, 800, 600));
            stage.setResizable(false);
            stage.show();

            // Close login window
            Stage loginStage = (Stage) usernameField.getScene().getWindow();
            loginStage.close();

        } catch (Exception e) {
            showAlert("Error", "Failed to open customer dashboard: " + e.getMessage());
        }
    }

    @FXML
    private void handleCancel() {
        System.exit(0);
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}