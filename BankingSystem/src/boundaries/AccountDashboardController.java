package boundaries;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

public class AccountDashboardController {

    @FXML private TableView<?> accountsTable;
    @FXML private TableColumn<?, ?> accountNumberColumn;
    @FXML private TableColumn<?, ?> accountTypeColumn;
    @FXML private TableColumn<?, ?> balanceColumn;
    @FXML private TableColumn<?, ?> branchColumn;
    @FXML private Label totalBalanceLabel;
    @FXML private Button logoutButton;
    @FXML private Button viewAccountsButton;
    @FXML private Button openAccountButton;
    @FXML private Button makeTransactionButton;
    @FXML private Button viewProfileButton;

    @FXML
    public void initialize() {
        
        // Display sample data for demonstration
        totalBalanceLabel.setText("Total Balance: BWP 1,500.00");
    }

    @FXML
    private void handleViewAccounts() {
        // Refresh accounts display - would call control class to get data
        showAlert(Alert.AlertType.INFORMATION, "Accounts", "Displaying all accounts for Palesa Britany Leeto");
    }

    @FXML
    private void handleOpenAccount() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/account_opening.fxml"));
            Parent root = loader.load();
            
            Stage stage = (Stage) openAccountButton.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Open New Account");
        } catch (Exception e) {
            showAlert(Alert.AlertType.ERROR, "Navigation Error", "Cannot load account opening form: " + e.getMessage());
        }
    }

    @FXML
    private void handleMakeTransaction() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/transaction.fxml"));
            Parent root = loader.load();
            
            Stage stage = (Stage) makeTransactionButton.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Make Transaction");
        } catch (Exception e) {
            showAlert(Alert.AlertType.ERROR, "Navigation Error", "Cannot load transaction form: " + e.getMessage());
        }
    }

    @FXML
    private void handleViewProfile() {
        // Display customer profile information
        String profileInfo = """
            Customer Profile:
            Name: Palesa Britany Leeto
            ID Number: 123456789
            Date of Birth: 15/03/1990
            Gender: Female
            Address: 123 Main Street, Gaborone
            Phone: 71234567
            Next of Kin: Britany Leeto (Mother)
            """;
        
        TextArea textArea = new TextArea(profileInfo);
        textArea.setEditable(false);
        textArea.setWrapText(true);
        
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Customer Profile");
        alert.setHeaderText("Profile Information for Palesa Britany Leeto");
        alert.getDialogPane().setContent(textArea);
        alert.showAndWait();
    }

    @FXML
    private void handleLogout() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/login.fxml"));
            Parent root = loader.load();
            
            Stage stage = (Stage) logoutButton.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Banking System Login");
        } catch (Exception e) {
            showAlert(Alert.AlertType.ERROR, "Navigation Error", "Cannot logout: " + e.getMessage());
        }
    }

    private void showAlert(Alert.AlertType type, String title, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}