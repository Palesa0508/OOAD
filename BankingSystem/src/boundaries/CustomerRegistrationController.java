package boundaries;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

public class CustomerRegistrationController {

    @FXML private TextField firstNameField;
    @FXML private TextField surnameField;
    @FXML private DatePicker dobPicker;
    @FXML private ComboBox<String> genderComboBox;
    @FXML private TextField idNumberField;
    @FXML private TextField nationalityField;
    @FXML private TextField addressField;
    @FXML private TextField emailField;
    @FXML private TextField phoneField;
    @FXML private TextField nextOfKinNameField;
    @FXML private TextField nextOfKinRelationshipField;
    @FXML private TextField nextOfKinPhoneField;
    @FXML private TextField nextOfKinAddressField;
    @FXML private TextField employerField;
    @FXML private TextField companyAddressField;
    @FXML private Button submitButton;
    @FXML private Button cancelButton;

    @FXML
    public void initialize() {
        // Initialize gender options - only UI logic
        genderComboBox.getItems().addAll("Male", "Female", "Other", "Prefer not to say");
        
        // Set default nationality
        nationalityField.setText("Motswana");
    }

    @FXML
    private void handleSubmit() {
        // Only UI validation - no business logic
        if (!validateForm()) {
            return;
        }
        
        // Collect data for control class (would be passed to control class)
        String firstName = firstNameField.getText();
        String surname = surnameField.getText();
        
    
        // Show success message
        showAlert(Alert.AlertType.INFORMATION, "Registration Successful", 
                 "Customer " + firstName + " " + surname + " has been registered successfully!");
        
        // Navigate back to login
        navigateToLogin();
    }

    @FXML
    private void handleCancel() {
        navigateToLogin();
    }

    private boolean validateForm() {
        // Only UI validation - checking if fields are filled
        if (firstNameField.getText().isEmpty() || surnameField.getText().isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Validation Error", "First name and surname are required");
            return false;
        }
        
        if (dobPicker.getValue() == null) {
            showAlert(Alert.AlertType.ERROR, "Validation Error", "Date of birth is required");
            return false;
        }
        
        if (idNumberField.getText().isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Validation Error", "ID number is required");
            return false;
        }
        
        if (addressField.getText().isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Validation Error", "Physical address is required");
            return false;
        }
        
        return true;
    }

    private void navigateToLogin() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/login.fxml"));
            Parent root = loader.load();
            
            Stage stage = (Stage) submitButton.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Banking System Login");
        } catch (Exception e) {
            showAlert(Alert.AlertType.ERROR, "Navigation Error", "Cannot load login screen: " + e.getMessage());
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
