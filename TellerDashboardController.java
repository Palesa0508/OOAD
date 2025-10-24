import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class TellerDashboardController {
    
    @FXML private Label welcomeLabel;
    @FXML private TabPane mainTabPane;
    
    // Customer Registration Tab
    @FXML private TextField firstNameField, lastNameField, idNumberField, phoneField;
    @FXML private TextField emailField, addressField, nationalityField, dobField;
    @FXML private TextField kinNameField, kinRelationshipField, kinPhoneField, kinAddressField;
    
    // Account Management Tab
    @FXML private ComboBox<String> customerComboBox;
    @FXML private ComboBox<String> accountTypeComboBox;
    @FXML private TextField initialDepositField;
    @FXML private TextField companyField, companyAddressField;
    @FXML private TableView<Account> accountsTable;
    @FXML private TableColumn<Account, String> accNumberCol, accTypeCol, customerCol, balanceCol;
    
    // Customer Search Tab
    @FXML private TextField searchField;
    @FXML private TableView<Customer> customerTable;
    @FXML private TableColumn<Customer, String> custIdCol, fullNameCol, idNumberCol, phoneCol;
    
    // Loan Management Tab
    @FXML private TableView<Loan> loanTable;
    @FXML private TableColumn<Loan, String> loanIdCol, loanCustomerCol, amountCol, purposeCol, dateCol, statusCol;
    
    private Teller currentTeller;
    private List<Customer> customers = new ArrayList<>();
    private List<Account> accounts = new ArrayList<>();
    private List<Loan> loans = new ArrayList<>();
    private int customerIdCounter = 1;
    private int accountNumberCounter = 1001;
    private int loanIdCounter = 1;
    
    public void setCurrentTeller(Teller teller) {
        this.currentTeller = teller;
        welcomeLabel.setText("Welcome, " + teller.getFirstName() + " " + teller.getLastName() + "!");
        initializeData();
    }
    
    private void initializeData() {
        // Initialize demo customer
        Customer demoCustomer = new Customer("palesa.leeto", "customer123", "Palesa", "Leeto", 
                "+267 123 4567", "palesa.customer@palesbank.com", "Gaborone, Botswana", 
                "1990-01-01", "Botswana");
        demoCustomer.setCustomerId("CUST001");
        demoCustomer.setIdNumber("ID001");
        customers.add(demoCustomer);
        
        // Initialize tables
        initializeCustomerTable();
        initializeAccountsTable();
        initializeLoanTable();
        populateCustomerComboBox();
        
        accountTypeComboBox.getItems().addAll("Savings", "Investment", "Cheque");
        
        // Add demo loans
        loans.add(new Loan("LN001", 5000.00, "Car loan", demoCustomer));
        loans.add(new Loan("LN002", 10000.00, "Home renovation", demoCustomer));
        loans.add(new Loan("LN003", 7500.00, "Education", demoCustomer));
        loanTable.setItems(FXCollections.observableArrayList(loans));
    }
    
    private void initializeCustomerTable() {
        custIdCol.setCellValueFactory(new PropertyValueFactory<>("customerId"));
        fullNameCol.setCellValueFactory(new PropertyValueFactory<>("fullName"));
        idNumberCol.setCellValueFactory(new PropertyValueFactory<>("idNumber"));
        phoneCol.setCellValueFactory(new PropertyValueFactory<>("phone"));
        customerTable.setItems(FXCollections.observableArrayList(customers));
    }
    
    private void initializeAccountsTable() {
        accNumberCol.setCellValueFactory(new PropertyValueFactory<>("accountNumber"));
        accTypeCol.setCellValueFactory(new PropertyValueFactory<>("accountType"));
        customerCol.setCellValueFactory(new PropertyValueFactory<>("customerName"));
        balanceCol.setCellValueFactory(new PropertyValueFactory<>("balanceFormatted"));
        accountsTable.setItems(FXCollections.observableArrayList(accounts));
    }
    
    private void initializeLoanTable() {
        loanIdCol.setCellValueFactory(new PropertyValueFactory<>("loanId"));
        loanCustomerCol.setCellValueFactory(new PropertyValueFactory<>("customerName"));
        amountCol.setCellValueFactory(new PropertyValueFactory<>("amountFormatted"));
        purposeCol.setCellValueFactory(new PropertyValueFactory<>("purpose"));
        dateCol.setCellValueFactory(new PropertyValueFactory<>("applicationDate"));
        statusCol.setCellValueFactory(new PropertyValueFactory<>("status"));
        loanTable.setItems(FXCollections.observableArrayList(loans));
    }
    
    private void populateCustomerComboBox() {
        customerComboBox.getItems().clear();
        for (Customer customer : customers) {
            customerComboBox.getItems().add(customer.getCustomerId() + " - " + customer.getFullName());
        }
    }
    
    @FXML
    private void handleRegisterCustomer() {
        if (validateCustomerRegistration()) {
            String customerId = "CUST" + String.format("%03d", customerIdCounter++);
            Customer newCustomer = new Customer(
                    generateUsername(firstNameField.getText(), lastNameField.getText()),
                    "default123",
                    firstNameField.getText(),
                    lastNameField.getText(),
                    phoneField.getText(),
                    emailField.getText(),
                    addressField.getText(),
                    dobField.getText(),
                    nationalityField.getText()
            );
            newCustomer.setCustomerId(customerId);
            newCustomer.setIdNumber(idNumberField.getText());
            newCustomer.setKinName(kinNameField.getText());
            newCustomer.setKinRelationship(kinRelationshipField.getText());
            newCustomer.setKinPhone(kinPhoneField.getText());
            newCustomer.setKinAddress(kinAddressField.getText());
            
            customers.add(newCustomer);
            clearCustomerForm();
            showAlert("Success", "Customer registered successfully!\nCustomer ID: " + customerId);
            populateCustomerComboBox();
            customerTable.setItems(FXCollections.observableArrayList(customers));
        }
    }
    
    @FXML
    private void handleClearForm() {
        clearCustomerForm();
    }
    
    private void clearCustomerForm() {
        firstNameField.clear();
        lastNameField.clear();
        idNumberField.clear();
        phoneField.clear();
        emailField.clear();
        addressField.clear();
        nationalityField.clear();
        dobField.clear();
        kinNameField.clear();
        kinRelationshipField.clear();
        kinPhoneField.clear();
        kinAddressField.clear();
    }
    
    @FXML
    private void handleCreateAccount() {
        if (validateAccountCreation()) {
            String selectedCustomer = customerComboBox.getValue();
            String customerId = selectedCustomer.split(" - ")[0];
            Customer customer = findCustomerById(customerId);
            
            if (customer != null) {
                String accountType = accountTypeComboBox.getValue();
                double initialDeposit = Double.parseDouble(initialDepositField.getText());
                String accountNumber = "ACC" + accountNumberCounter++;
                
                Account newAccount = null;
                
                switch (accountType) {
                    case "Savings":
                        newAccount = new SavingsAccount(accountNumber, initialDeposit, customer);
                        break;
                    case "Investment":
                        if (initialDeposit >= 500) {
                            newAccount = new InvestmentAccount(accountNumber, initialDeposit, customer);
                        } else {
                            showAlert("Error", "Investment account requires minimum deposit of BWP 500.00");
                            return;
                        }
                        break;
                    case "Cheque":
                        if (!companyField.getText().isEmpty() && !companyAddressField.getText().isEmpty()) {
                            newAccount = new ChequeAccount(accountNumber, initialDeposit, customer, 
                                    companyField.getText(), companyAddressField.getText());
                        } else {
                            showAlert("Error", "Cheque account requires employment information");
                            return;
                        }
                        break;
                }
                
                if (newAccount != null) {
                    accounts.add(newAccount);
                    accountsTable.setItems(FXCollections.observableArrayList(accounts));
                    clearAccountForm();
                    showAlert("Success", "Account created successfully!\nAccount Number: " + accountNumber);
                }
            }
        }
    }
    
    @FXML
    private void handleSearchCustomer() {
        String searchTerm = searchField.getText().toLowerCase();
        if (searchTerm.isEmpty()) {
            customerTable.setItems(FXCollections.observableArrayList(customers));
        } else {
            ObservableList<Customer> filteredCustomers = FXCollections.observableArrayList();
            for (Customer customer : customers) {
                if (customer.getFullName().toLowerCase().contains(searchTerm) ||
                    customer.getIdNumber().toLowerCase().contains(searchTerm) ||
                    customer.getPhone().contains(searchTerm)) {
                    filteredCustomers.add(customer);
                }
            }
            customerTable.setItems(filteredCustomers);
        }
    }
    
    @FXML
    private void handleViewAllCustomers() {
        searchField.clear();
        customerTable.setItems(FXCollections.observableArrayList(customers));
    }
    
    @FXML
    private void handleApproveLoan() {
        Loan selectedLoan = loanTable.getSelectionModel().getSelectedItem();
        if (selectedLoan != null) {
            selectedLoan.setStatus("APPROVED");
            loanTable.refresh();
            showAlert("Success", "Loan approved successfully!");
        } else {
            showAlert("Error", "Please select a loan to approve");
        }
    }
    
    @FXML
    private void handleRejectLoan() {
        Loan selectedLoan = loanTable.getSelectionModel().getSelectedItem();
        if (selectedLoan != null) {
            selectedLoan.setStatus("REJECTED");
            loanTable.refresh();
            showAlert("Success", "Loan rejected successfully!");
        } else {
            showAlert("Error", "Please select a loan to reject");
        }
    }
    
    @FXML
    private void handleRefreshLoans() {
        loanTable.setItems(FXCollections.observableArrayList(loans));
    }
    
    @FXML
    private void handleLogout() {
        try {
            System.out.println("Logging out from Teller Dashboard...");
            Stage stage = (Stage) welcomeLabel.getScene().getWindow();
            
            // Load the login screen
            Parent root = FXMLLoader.load(getClass().getResource("Login.fxml"));
            
            Scene scene = new Scene(root, 900, 600);
            
            // Try to load CSS
            try {
                scene.getStylesheets().add(getClass().getResource("styles.css").toExternalForm());
            } catch (Exception e) {
                System.out.println("CSS not loaded during logout: " + e.getMessage());
            }
            
            stage.setScene(scene);
            stage.centerOnScreen();
            System.out.println("Logout successful - returned to login screen");
            
        } catch (Exception e) {
            System.out.println("Error during logout: " + e.getMessage());
            e.printStackTrace();
            showAlert("Logout Error", "Failed to logout: " + e.getMessage());
        }
    }
    
    private boolean validateCustomerRegistration() {
        if (firstNameField.getText().isEmpty() || lastNameField.getText().isEmpty() ||
            idNumberField.getText().isEmpty() || phoneField.getText().isEmpty()) {
            showAlert("Error", "Please fill in all required fields");
            return false;
        }
        return true;
    }
    
    private boolean validateAccountCreation() {
        if (customerComboBox.getValue() == null || accountTypeComboBox.getValue() == null ||
            initialDepositField.getText().isEmpty()) {
            showAlert("Error", "Please fill in all required fields");
            return false;
        }
        
        try {
            double deposit = Double.parseDouble(initialDepositField.getText());
            if (deposit <= 0) {
                showAlert("Error", "Initial deposit must be positive");
                return false;
            }
        } catch (NumberFormatException e) {
            showAlert("Error", "Please enter a valid amount");
            return false;
        }
        
        return true;
    }
    
    private Customer findCustomerById(String customerId) {
        for (Customer customer : customers) {
            if (customer.getCustomerId().equals(customerId)) {
                return customer;
            }
        }
        return null;
    }
    
    private String generateUsername(String firstName, String lastName) {
        return firstName.toLowerCase() + "." + lastName.toLowerCase();
    }
    
    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
    
    private void clearAccountForm() {
        customerComboBox.setValue(null);
        accountTypeComboBox.setValue(null);
        initialDepositField.clear();
        companyField.clear();
        companyAddressField.clear();
    }
    
    // Method to simulate customer loan application
    public void addLoanApplication(Loan loan) {
        loans.add(loan);
        loanTable.setItems(FXCollections.observableArrayList(loans));
    }
}