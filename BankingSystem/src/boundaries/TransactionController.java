package boundaries;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class TransactionController {

    @FXML private TextField accountNumberField;
    @FXML private ComboBox<String> transactionTypeComboBox;
    @FXML private TextField amountField;
    @FXML private TextField descriptionField;
    @FXML private Button submitButton;
    @FXML private Button cancelButton;

    @FXML
    public void initialize() {
        transactionTypeComboBox.getItems().addAll("Deposit", "Withdrawal", "Transfer");
    }

    @FXML
    private void handleSubmit() {
        if (accountNumberField.getText().isEmpty() || amountField.getText().isEmpty()) {
            showAlert("Error", "Please fill all required fields");
            return;
        }
        
        showAlert("Success", "Transaction processed for account: " + accountNumberField.getText());
        handleCancel();
    }

    @FXML
    private void handleCancel() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("../fxml/account_dashboard.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) cancelButton.getScene().getWindow();
            stage.setScene(new Scene(root, 800, 500));
        } catch (Exception e) {
            showAlert("Error", "Cannot return to dashboard: " + e.getMessage());
        }
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}