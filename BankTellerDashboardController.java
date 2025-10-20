import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import java.util.Date;

public class BankTellerDashboardController {

    @FXML private Label welcomeLabel;

    // Customer type selection
    @FXML private ComboBox<String> customerTypeComboBox;

    // Individual Customer fields
    @FXML private VBox individualFields;
    @FXML private TextField customerFirstNameField;
    @FXML private TextField customerSurnameField;
    @FXML private TextField customerIdField;
    @FXML private TextField customerDobField;

    // Company Customer fields
    @FXML private VBox companyFields;
    @FXML private TextField companyNameField;
    @FXML private TextField registrationNumberField;
    @FXML private TextField businessTypeField;
    @FXML private TextField contactPersonField;
    @FXML private TextField annualRevenueField;

    // Common fields for all customers
    @FXML private TextField customerAddressField;
    @FXML private TextField customerEmailField;
    @FXML private TextField customerPhoneField;

    // Next of Kin fields (for individuals)
    @FXML private VBox nextOfKinFields;
    @FXML private TextField nokFirstNameField;
    @FXML private TextField nokSurnameField;
    @FXML private ComboBox<String> nokRelationshipComboBox;
    @FXML private TextField nokPhoneField;
    @FXML private TextField nokEmailField;
    @FXML private TextField nokAddressField;

    // Account fields
    @FXML private ComboBox<String> accountTypeComboBox;
    @FXML private TextField initialDepositField;
    @FXML private Label minimumDepositLabel;
    @FXML private Label requirementsLabel;
    @FXML private TextArea outputArea;

    private BankTeller currentTeller;
    private TellerLandingController landingController;

    // Add this method to fix the error
    public void setLandingController(TellerLandingController landingController) {
        this.landingController = landingController;
    }

    public void setBankTeller(BankTeller teller) {
        this.currentTeller = teller;
        updateDashboard();
    }

    @FXML
    private void initialize() {
        // Set up customer type options
        customerTypeComboBox.getItems().addAll("Individual", "Company");
        customerTypeComboBox.setValue("Individual");

        // Add listener to toggle between individual and company fields
        customerTypeComboBox.valueProperty().addListener((observable, oldValue, newValue) -> {
            toggleCustomerFields(newValue);
        });

        // Set up account type options
        accountTypeComboBox.getItems().addAll("Savings", "Cheque", "Investment");
        accountTypeComboBox.setValue("Savings");

        // Set up next of kin relationship options
        nokRelationshipComboBox.getItems().addAll("Parent", "Spouse", "Sibling", "Child", "Relative", "Friend", "Other");
        nokRelationshipComboBox.setValue("Parent");

        // Add listener to update requirements when account type changes
        accountTypeComboBox.valueProperty().addListener((observable, oldValue, newValue) -> {
            updateAccountRequirements(newValue);
        });

        // Initialize UI state
        toggleCustomerFields("Individual");
        updateAccountRequirements("Savings");

        // Clear output area
        outputArea.setText("Bank Teller System Ready.\nSelect customer type and fill in details to begin.\n");

        // Set up input validation
        setupInputValidation();
    }

    private void toggleCustomerFields(String customerType) {
        if ("Individual".equals(customerType)) {
            individualFields.setVisible(true);
            individualFields.setManaged(true);
            companyFields.setVisible(false);
            companyFields.setManaged(false);
            nextOfKinFields.setVisible(true);
            nextOfKinFields.setManaged(true);
        } else {
            individualFields.setVisible(false);
            individualFields.setManaged(false);
            companyFields.setVisible(true);
            companyFields.setManaged(true);
            nextOfKinFields.setVisible(false);
            nextOfKinFields.setManaged(false);
        }
    }

    private void updateDashboard() {
        if (currentTeller != null) {
            welcomeLabel.setText("Bank Teller: " + currentTeller.getFullName() + " | " + currentTeller.getEmployeeId());
        }
    }

    private void updateAccountRequirements(String accountType) {
        switch (accountType) {
            case "Savings":
                minimumDepositLabel.setText("P50.00");
                requirementsLabel.setText("• Minimum opening deposit: P50.00\n• No monthly fees\n• Interest earning\n• Easy access to funds");
                break;
            case "Cheque":
                minimumDepositLabel.setText("P100.00");
                requirementsLabel.setText("• Minimum opening deposit: P100.00\n• Monthly maintenance fee: P25.00\n• Cheque book included\n• Overdraft protection available");
                break;
            case "Investment":
                minimumDepositLabel.setText("P500.00");
                requirementsLabel.setText("• Minimum opening deposit: P500.00\n• Higher interest rates\n• Minimum monthly income: P5,000.00\n• 30-day notice for withdrawals");
                break;
        }
    }

    @FXML
    private void handleOpenAccount() {
        try {
            String customerType = customerTypeComboBox.getValue();

            if ("Individual".equals(customerType)) {
                openIndividualAccount();
            } else {
                openCompanyAccount();
            }

        } catch (Exception e) {
            showAlert("Error", "Error opening account: " + e.getMessage());
            outputArea.appendText("\n❌ ERROR: " + e.getMessage() + "\n");
        }
    }

    private void openIndividualAccount() {
        // Validate individual customer information
        if (!validateIndividualInformation()) {
            return;
        }

        // Validate next of kin information
        if (!validateNextOfKinInformation()) {
            return;
        }

        // Validate deposit amount
        if (!validateDepositAmount()) {
            return;
        }

        double initialDeposit = Double.parseDouble(initialDepositField.getText());
        String accountType = accountTypeComboBox.getValue();

        if (!validateMinimumDeposit(accountType, initialDeposit)) {
            return;
        }

        // Create next of kin
        NextOfKin nextOfKin = createNextOfKinFromForm();

        // Create individual customer
        Individual customer = createIndividualFromForm(nextOfKin);

        // Open account
        Account newAccount = currentTeller.openAccount(customer, accountType.toLowerCase(), currentTeller.getBranchCode());

        if (newAccount != null) {
            // Process initial deposit
            newAccount.deposit(initialDeposit);

            // Display success message
            outputArea.appendText("\n✅ INDIVIDUAL ACCOUNT OPENED SUCCESSFULLY!\n");
            outputArea.appendText("   Customer: " + customer.getFullName() + "\n");
            outputArea.appendText("   ID Number: " + customer.getIdNumber() + "\n");
            outputArea.appendText("   Account Type: " + accountType + " Account\n");
            outputArea.appendText("   Account Number: #" + newAccount.getAccountNumber() + "\n");
            outputArea.appendText("   Initial Deposit: P" + String.format("%.2f", initialDeposit) + "\n");
            outputArea.appendText("   Current Balance: P" + String.format("%.2f", newAccount.getBalance()) + "\n");
            outputArea.appendText("   Next of Kin: " + nextOfKin.getFullName() + " (" + nextOfKin.getRelationship() + ")\n");
            outputArea.appendText("   Opened by: " + currentTeller.getFullName() + "\n");
            outputArea.appendText("   Branch: " + currentTeller.getBranchCode() + "\n");
            outputArea.appendText("   Date: " + new Date() + "\n\n");

            // Clear form for next customer
            clearForm();
        } else {
            outputArea.appendText("\n❌ FAILED TO OPEN INDIVIDUAL ACCOUNT\n");
            outputArea.appendText("   Please check the account requirements and try again.\n");
        }
    }

    private void openCompanyAccount() {
        // Validate company information
        if (!validateCompanyInformation()) {
            return;
        }

        // Validate deposit amount
        if (!validateDepositAmount()) {
            return;
        }

        double initialDeposit = Double.parseDouble(initialDepositField.getText());
        String accountType = accountTypeComboBox.getValue();

        if (!validateMinimumDeposit(accountType, initialDeposit)) {
            return;
        }

        // Create company customer
        Company customer = createCompanyFromForm();

        // Open account
        Account newAccount = currentTeller.openAccount(customer, accountType.toLowerCase(), currentTeller.getBranchCode());

        if (newAccount != null) {
            // Process initial deposit
            newAccount.deposit(initialDeposit);

            // Display success message
            outputArea.appendText("\n✅ COMPANY ACCOUNT OPENED SUCCESSFULLY!\n");
            outputArea.appendText("   Company: " + customer.getDisplayName() + "\n");
            outputArea.appendText("   Registration: " + customer.getIdentificationNumber() + "\n");
            outputArea.appendText("   Business Type: " + customer.getBusinessType() + "\n");
            outputArea.appendText("   Contact Person: " + customer.getContactPersonName() + "\n");
            outputArea.appendText("   Annual Revenue: P" + String.format("%.2f", customer.getAnnualRevenue()) + "\n");
            outputArea.appendText("   Account Type: " + accountType + " Account\n");
            outputArea.appendText("   Account Number: #" + newAccount.getAccountNumber() + "\n");
            outputArea.appendText("   Initial Deposit: P" + String.format("%.2f", initialDeposit) + "\n");
            outputArea.appendText("   Current Balance: P" + String.format("%.2f", newAccount.getBalance()) + "\n");
            outputArea.appendText("   Opened by: " + currentTeller.getFullName() + "\n");
            outputArea.appendText("   Branch: " + currentTeller.getBranchCode() + "\n");
            outputArea.appendText("   Date: " + new Date() + "\n\n");

            // Clear form for next customer
            clearForm();
        } else {
            outputArea.appendText("\n❌ FAILED TO OPEN COMPANY ACCOUNT\n");
            outputArea.appendText("   Please check the account requirements and try again.\n");
        }
    }

    private boolean validateIndividualInformation() {
        if (customerFirstNameField.getText().trim().isEmpty()) {
            showAlert("Validation Error", "Please enter customer's first name");
            customerFirstNameField.requestFocus();
            return false;
        }

        if (customerSurnameField.getText().trim().isEmpty()) {
            showAlert("Validation Error", "Please enter customer's surname");
            customerSurnameField.requestFocus();
            return false;
        }

        if (customerAddressField.getText().trim().isEmpty()) {
            showAlert("Validation Error", "Please enter customer's address");
            customerAddressField.requestFocus();
            return false;
        }

        if (customerIdField.getText().trim().isEmpty()) {
            showAlert("Validation Error", "Please enter customer's ID number");
            customerIdField.requestFocus();
            return false;
        }

        // Validate email format if provided
        if (!customerEmailField.getText().trim().isEmpty() && !isValidEmail(customerEmailField.getText().trim())) {
            showAlert("Validation Error", "Please enter a valid email address or leave the field empty");
            customerEmailField.requestFocus();
            return false;
        }

        return true;
    }

    private boolean validateCompanyInformation() {
        if (companyNameField.getText().trim().isEmpty()) {
            showAlert("Validation Error", "Please enter company name");
            companyNameField.requestFocus();
            return false;
        }

        if (registrationNumberField.getText().trim().isEmpty()) {
            showAlert("Validation Error", "Please enter company registration number");
            registrationNumberField.requestFocus();
            return false;
        }

        if (businessTypeField.getText().trim().isEmpty()) {
            showAlert("Validation Error", "Please enter business type");
            businessTypeField.requestFocus();
            return false;
        }

        if (contactPersonField.getText().trim().isEmpty()) {
            showAlert("Validation Error", "Please enter contact person name");
            contactPersonField.requestFocus();
            return false;
        }

        if (customerAddressField.getText().trim().isEmpty()) {
            showAlert("Validation Error", "Please enter company address");
            customerAddressField.requestFocus();
            return false;
        }

        // Validate annual revenue
        if (annualRevenueField.getText().trim().isEmpty()) {
            showAlert("Validation Error", "Please enter annual revenue");
            annualRevenueField.requestFocus();
            return false;
        }

        try {
            double revenue = Double.parseDouble(annualRevenueField.getText());
            if (revenue < 0) {
                showAlert("Validation Error", "Annual revenue cannot be negative");
                annualRevenueField.requestFocus();
                return false;
            }
        } catch (NumberFormatException e) {
            showAlert("Validation Error", "Please enter a valid numeric value for annual revenue");
            annualRevenueField.requestFocus();
            return false;
        }

        // Validate email format if provided
        if (!customerEmailField.getText().trim().isEmpty() && !isValidEmail(customerEmailField.getText().trim())) {
            showAlert("Validation Error", "Please enter a valid email address or leave the field empty");
            customerEmailField.requestFocus();
            return false;
        }

        return true;
    }

    private boolean validateNextOfKinInformation() {
        if (nokFirstNameField.getText().trim().isEmpty()) {
            showAlert("Validation Error", "Please enter next of kin's first name");
            nokFirstNameField.requestFocus();
            return false;
        }

        if (nokSurnameField.getText().trim().isEmpty()) {
            showAlert("Validation Error", "Please enter next of kin's surname");
            nokSurnameField.requestFocus();
            return false;
        }

        if (nokRelationshipComboBox.getValue() == null) {
            showAlert("Validation Error", "Please select relationship to customer");
            nokRelationshipComboBox.requestFocus();
            return false;
        }

        if (nokPhoneField.getText().trim().isEmpty()) {
            showAlert("Validation Error", "Next of kin phone number is required for emergency contact");
            nokPhoneField.requestFocus();
            return false;
        }

        // Validate email format if provided
        if (!nokEmailField.getText().trim().isEmpty() && !isValidEmail(nokEmailField.getText().trim())) {
            showAlert("Validation Error", "Please enter a valid email address for next of kin or leave the field empty");
            nokEmailField.requestFocus();
            return false;
        }

        return true;
    }

    private boolean validateDepositAmount() {
        if (initialDepositField.getText().isEmpty()) {
            showAlert("Validation Error", "Please enter an initial deposit amount");
            return false;
        }

        try {
            Double.parseDouble(initialDepositField.getText());
        } catch (NumberFormatException e) {
            showAlert("Validation Error", "Please enter a valid numeric deposit amount");
            return false;
        }

        return true;
    }

    private boolean isValidEmail(String email) {
        String emailRegex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$";
        return email.matches(emailRegex);
    }

    private boolean validateMinimumDeposit(String accountType, double deposit) {
        double minimumDeposit = 0.0;

        switch (accountType) {
            case "Savings": minimumDeposit = 50.00; break;
            case "Cheque": minimumDeposit = 100.00; break;
            case "Investment": minimumDeposit = 500.00; break;
        }

        if (deposit < minimumDeposit) {
            showAlert("Deposit Error",
                    accountType + " account requires minimum deposit of P" + String.format("%.2f", minimumDeposit) +
                            "\nYou deposited: P" + String.format("%.2f", deposit));
            initialDepositField.requestFocus();
            return false;
        }

        if (deposit <= 0) {
            showAlert("Deposit Error", "Initial deposit must be positive");
            initialDepositField.requestFocus();
            return false;
        }

        // Check for reasonable maximum deposit (optional)
        if (deposit > 1000000.00) {
            showAlert("Deposit Warning",
                    "Deposit amount seems unusually high. Please verify the amount.\n" +
                            "If correct, proceed with supervisor approval.");
            return true; // Allow but warn
        }

        return true;
    }

    private NextOfKin createNextOfKinFromForm() {
        String email = nokEmailField.getText().trim().isEmpty()
                ? "Not Provided" : nokEmailField.getText().trim();

        String address = nokAddressField.getText().trim().isEmpty()
                ? "Same as customer" : nokAddressField.getText().trim();

        return new NextOfKin(
                nokFirstNameField.getText().trim(),
                nokSurnameField.getText().trim(),
                nokRelationshipComboBox.getValue(),
                nokPhoneField.getText().trim(),
                email,
                address
        );
    }

    private Individual createIndividualFromForm(NextOfKin nextOfKin) {
        String phoneNumber = customerPhoneField.getText().trim().isEmpty()
                ? "Not Provided" : customerPhoneField.getText().trim();

        String email = customerEmailField.getText().trim().isEmpty()
                ? "Not Provided" : customerEmailField.getText().trim();

        // Parse date of birth (you might want to use a DatePicker instead)
        Date dateOfBirth = new Date(); // Using current date as placeholder

        return new Individual(
                customerFirstNameField.getText().trim(),
                customerSurnameField.getText().trim(),
                customerAddressField.getText().trim(),
                customerIdField.getText().trim(),
                dateOfBirth,
                email,
                phoneNumber,
                nextOfKin
        );
    }

    private Company createCompanyFromForm() {
        String phoneNumber = customerPhoneField.getText().trim().isEmpty()
                ? "Not Provided" : customerPhoneField.getText().trim();

        String email = customerEmailField.getText().trim().isEmpty()
                ? "Not Provided" : customerEmailField.getText().trim();

        double annualRevenue = Double.parseDouble(annualRevenueField.getText().trim());

        return new Company(
                companyNameField.getText().trim(),
                registrationNumberField.getText().trim(),
                businessTypeField.getText().trim(),
                customerAddressField.getText().trim(),
                email,
                phoneNumber,
                contactPersonField.getText().trim(),
                annualRevenue
        );
    }

    @FXML
    private void handleClearForm() {
        clearForm();
        outputArea.appendText("Form cleared. Ready for new customer.\n");
    }

    @FXML
    private void handleViewRecentAccounts() {
        // This could be implemented to show recently opened accounts
        outputArea.appendText("\n📋 Recent accounts feature coming soon...\n");
    }

    @FXML
    private void handleLogout() {
        try {
            // Confirmation dialog before logout
            Alert confirmAlert = new Alert(Alert.AlertType.CONFIRMATION);
            confirmAlert.setTitle("Confirm Logout");
            confirmAlert.setHeaderText("Logout Confirmation");
            confirmAlert.setContentText("Are you sure you want to logout?");

            ButtonType result = confirmAlert.showAndWait().orElse(ButtonType.CANCEL);

            if (result != ButtonType.OK) {
                return; // User cancelled logout
            }

            System.out.println("Bank Teller logging out: " + currentTeller.getFullName());

            // Close dashboard
            Stage dashboardStage = (Stage) welcomeLabel.getScene().getWindow();
            dashboardStage.close();

            // Return to main menu
            FXMLLoader loader = new FXMLLoader(getClass().getResource("mainmenu.fxml"));
            Parent root = loader.load();

            Stage mainMenuStage = new Stage();
            mainMenuStage.setTitle("Banking System - Main Menu");
            mainMenuStage.setScene(new Scene(root, 800, 600));
            mainMenuStage.setResizable(false);
            mainMenuStage.show();

        } catch (Exception e) {
            showAlert("Error", "Error during logout: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void clearForm() {
        // Clear individual fields
        customerFirstNameField.clear();
        customerSurnameField.clear();
        customerIdField.clear();
        customerDobField.clear();

        // Clear company fields
        companyNameField.clear();
        registrationNumberField.clear();
        businessTypeField.clear();
        contactPersonField.clear();
        annualRevenueField.clear();

        // Clear common fields
        customerAddressField.clear();
        customerEmailField.clear();
        customerPhoneField.clear();

        // Clear next of kin fields
        nokFirstNameField.clear();
        nokSurnameField.clear();
        nokRelationshipComboBox.setValue("Parent");
        nokPhoneField.clear();
        nokEmailField.clear();
        nokAddressField.clear();

        // Clear account fields
        initialDepositField.clear();
        accountTypeComboBox.setValue("Savings");
        customerTypeComboBox.setValue("Individual");

        // Set focus to first field for better UX
        customerTypeComboBox.requestFocus();
    }

    private void setupInputValidation() {
        // Only allow numeric input for deposit and revenue fields
        initialDepositField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("\\d*(\\.\\d*)?")) {
                initialDepositField.setText(oldValue);
            }
        });

        annualRevenueField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("\\d*(\\.\\d*)?")) {
                annualRevenueField.setText(oldValue);
            }
        });

        // Prevent special characters in name fields
        setupNameFieldValidation(customerFirstNameField);
        setupNameFieldValidation(customerSurnameField);
        setupNameFieldValidation(nokFirstNameField);
        setupNameFieldValidation(nokSurnameField);
        setupNameFieldValidation(contactPersonField);
    }

    private void setupNameFieldValidation(TextField field) {
        field.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("[a-zA-Z ]*")) {
                field.setText(oldValue);
            }
        });
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    @FXML
    private void handleHelp() {
        showAlert("Bank Teller Help",
                "How to open an account:\n\n" +
                        "1. Select customer type (Individual or Company)\n" +
                        "2. Fill in all required customer details\n" +
                        "3. For individuals: fill in next of kin information\n" +
                        "4. Select the account type\n" +
                        "5. Enter initial deposit (must meet minimum requirement)\n" +
                        "6. Click 'Open Account' to complete the process\n\n" +
                        "Minimum Deposits:\n" +
                        "• Savings: P50.00\n" +
                        "• Cheque: P100.00\n" +
                        "• Investment: P500.00\n\n" +
                        "Note: Next of kin phone number is required for individual accounts.");
    }
}