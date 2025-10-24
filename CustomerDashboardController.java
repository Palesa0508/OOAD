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

public class CustomerDashboardController {
    
    @FXML private Label welcomeLabel;
    @FXML private Label customerIdLabel;
    
    // Profile Tab
    @FXML private TextField profileFirstName, profileLastName, profilePhone, profileEmail;
    @FXML private TextField profileAddress, profileNationality, profileDob;
    @FXML private TextField kinNameField, kinRelationshipField, kinPhoneField, kinAddressField;
    
    // Accounts Tab
    @FXML private TableView<Account> accountsTable;
    @FXML private TableColumn<Account, String> accNumberCol, accTypeCol, balanceCol;
    
    // Transactions Tab - Banking Operations
    @FXML private ComboBox<String> operationAccountSelector;
    @FXML private ComboBox<String> operationTypeComboBox;
    @FXML private TextField operationAmountField;
    
    // Transactions Tab - History
    @FXML private ComboBox<String> accountSelector;
    @FXML private TableView<Transaction> transactionsTable;
    @FXML private TableColumn<Transaction, String> transDateCol, transTypeCol, transAmountCol, transDescCol;
    
    // Loans Tab
    @FXML private TextField loanAmountField, loanPurposeField;
    @FXML private TableView<Loan> loansTable;
    @FXML private TableColumn<Loan, String> loanIdCol, loanAmountCol, loanPurposeCol, loanDateCol, loanStatusCol;
    
    private Customer currentCustomer;
    private List<Account> accounts = new ArrayList<>();
    private List<Transaction> transactions = new ArrayList<>();
    private List<Loan> loans = new ArrayList<>();
    private int transactionIdCounter = 1;
    private int loanIdCounter = 1;
    
    public void setCurrentCustomer(Customer customer) {
        this.currentCustomer = customer;
        welcomeLabel.setText("Welcome, " + customer.getFirstName() + " " + customer.getLastName() + "!");
        customerIdLabel.setText("Customer ID: " + customer.getCustomerId() + " • Pale's Bank");
        loadCustomerData();
        initializeTables();
        initializeBankingOperations();
    }
    
    private void loadCustomerData() {
        // Load profile data
        profileFirstName.setText(currentCustomer.getFirstName());
        profileLastName.setText(currentCustomer.getLastName());
        profilePhone.setText(currentCustomer.getPhone());
        profileEmail.setText(currentCustomer.getEmail());
        profileAddress.setText(currentCustomer.getAddress());
        profileNationality.setText(currentCustomer.getNationality());
        profileDob.setText(currentCustomer.getDateOfBirth());
        
        // Load demo accounts
        if (accounts.isEmpty()) {
            accounts.add(new SavingsAccount("SAV001", 1500.00, currentCustomer));
            accounts.add(new InvestmentAccount("INV001", 2500.00, currentCustomer));
            accounts.add(new ChequeAccount("CHQ001", 5000.00, currentCustomer, "Palesa Corp", "Gaborone"));
        }
        
        // Load demo transactions
        if (transactions.isEmpty()) {
            transactions.add(new Transaction("TXN001", LocalDateTime.now().minusDays(2), "DEPOSIT", 1000.00, "Initial deposit", "SAV001"));
            transactions.add(new Transaction("TXN002", LocalDateTime.now().minusDays(1), "DEPOSIT", 500.00, "Salary", "CHQ001"));
            transactions.add(new Transaction("TXN003", LocalDateTime.now(), "INTEREST", 7.50, "Monthly interest", "INV001"));
            transactions.add(new Transaction("TXN004", LocalDateTime.now().minusHours(5), "DEPOSIT", 200.00, "Cash deposit", "SAV001"));
        }
        
        // Populate account selector for transaction history
        accountSelector.getItems().clear();
        for (Account account : accounts) {
            accountSelector.getItems().add(account.getAccountNumber() + " - " + account.getAccountType());
        }
        if (!accountSelector.getItems().isEmpty()) {
            accountSelector.setValue(accountSelector.getItems().get(0));
        }
    }
    
    private void initializeTables() {
        // Initialize accounts table
        accNumberCol.setCellValueFactory(new PropertyValueFactory<>("accountNumber"));
        accTypeCol.setCellValueFactory(new PropertyValueFactory<>("accountType"));
        balanceCol.setCellValueFactory(new PropertyValueFactory<>("balanceFormatted"));
        accountsTable.setItems(FXCollections.observableArrayList(accounts));
        
        // Initialize transactions table
        transDateCol.setCellValueFactory(new PropertyValueFactory<>("dateFormatted"));
        transTypeCol.setCellValueFactory(new PropertyValueFactory<>("type"));
        transAmountCol.setCellValueFactory(new PropertyValueFactory<>("amountFormatted"));
        transDescCol.setCellValueFactory(new PropertyValueFactory<>("description"));
        
        // Initialize loans table
        loanIdCol.setCellValueFactory(new PropertyValueFactory<>("loanId"));
        loanAmountCol.setCellValueFactory(new PropertyValueFactory<>("amountFormatted"));
        loanPurposeCol.setCellValueFactory(new PropertyValueFactory<>("purpose"));
        loanDateCol.setCellValueFactory(new PropertyValueFactory<>("applicationDate"));
        loanStatusCol.setCellValueFactory(new PropertyValueFactory<>("status"));
        loansTable.setItems(FXCollections.observableArrayList(loans));
    }
    
    private void initializeBankingOperations() {
        // Populate account selector for operations
        operationAccountSelector.getItems().clear();
        for (Account account : accounts) {
            operationAccountSelector.getItems().add(account.getAccountNumber() + " - " + account.getAccountType());
        }
        
        // Populate operation types
        operationTypeComboBox.getItems().addAll("DEPOSIT", "WITHDRAW");
        
        if (!operationAccountSelector.getItems().isEmpty()) {
            operationAccountSelector.setValue(operationAccountSelector.getItems().get(0));
        }
        if (!operationTypeComboBox.getItems().isEmpty()) {
            operationTypeComboBox.setValue(operationTypeComboBox.getItems().get(0));
        }
    }
    
    @FXML
    private void handleTransaction() {
        if (validateTransaction()) {
            String selectedAccount = operationAccountSelector.getValue();
            String operationType = operationTypeComboBox.getValue();
            double amount = Double.parseDouble(operationAmountField.getText());
            
            String accountNumber = selectedAccount.split(" - ")[0];
            Account account = findAccountByNumber(accountNumber);
            
            if (account != null) {
                boolean success = false;
                String transactionType = "";
                
                if ("DEPOSIT".equals(operationType)) {
                    success = account.deposit(amount);
                    transactionType = "DEPOSIT";
                } else if ("WITHDRAW".equals(operationType)) {
                    success = account.withdraw(amount);
                    transactionType = "WITHDRAW";
                }
                
                if (success) {
                    // Create and add transaction record
                    String transactionId = "TXN" + String.format("%03d", transactionIdCounter++);
                    Transaction transaction = new Transaction(transactionId, LocalDateTime.now(), 
                        transactionType, amount, "Customer " + operationType.toLowerCase(), accountNumber);
                    transactions.add(transaction);
                    
                    showAlert("Success", operationType + " completed successfully!\nNew Balance: " + account.getBalanceFormatted());
                    operationAmountField.clear();
                    
                    // Refresh accounts table to show updated balance
                    accountsTable.refresh();
                } else {
                    showAlert("Error", operationType + " failed. Please check the amount and account restrictions.");
                }
            }
        }
    }
    
    @FXML
    private void handleUpdateProfile() {
        if (validateProfileUpdate()) {
            currentCustomer.setFirstName(profileFirstName.getText());
            currentCustomer.setLastName(profileLastName.getText());
            currentCustomer.setPhone(profilePhone.getText());
            currentCustomer.setEmail(profileEmail.getText());
            currentCustomer.setAddress(profileAddress.getText());
            currentCustomer.setNationality(profileNationality.getText());
            currentCustomer.setDateOfBirth(profileDob.getText());
            currentCustomer.setKinName(kinNameField.getText());
            currentCustomer.setKinRelationship(kinRelationshipField.getText());
            currentCustomer.setKinPhone(kinPhoneField.getText());
            currentCustomer.setKinAddress(kinAddressField.getText());
            
            showAlert("Success", "Profile updated successfully!");
        }
    }
    
    @FXML
    private void handleResetChanges() {
        loadCustomerData();
    }
    
    @FXML
    private void handleApplyForLoan() {
        if (validateLoanApplication()) {
            double amount = Double.parseDouble(loanAmountField.getText());
            String purpose = loanPurposeField.getText();
            String loanId = "LN" + String.format("%03d", loanIdCounter++);
            
            Loan newLoan = new Loan(loanId, amount, purpose, currentCustomer);
            loans.add(newLoan);
            loansTable.setItems(FXCollections.observableArrayList(loans));
            
            showAlert("Success", "Loan application submitted successfully!\nLoan ID: " + loanId);
            
            loanAmountField.clear();
            loanPurposeField.clear();
        }
    }
    
    @FXML
    private void handleViewTransactions() {
        String selectedAccount = accountSelector.getValue();
        if (selectedAccount != null) {
            String accountNumber = selectedAccount.split(" - ")[0];
            ObservableList<Transaction> accountTransactions = FXCollections.observableArrayList();
            
            for (Transaction transaction : transactions) {
                if (transaction.getAccountNumber().equals(accountNumber)) {
                    accountTransactions.add(transaction);
                }
            }
            
            transactionsTable.setItems(accountTransactions);
        }
    }
    
    @FXML
    private void handleViewAllTransactions() {
        transactionsTable.setItems(FXCollections.observableArrayList(transactions));
    }
    
    @FXML
    private void handleLogout() {
        try {
            System.out.println("Logging out from Customer Dashboard...");
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
    
    private boolean validateTransaction() {
        if (operationAccountSelector.getValue() == null || 
            operationTypeComboBox.getValue() == null || 
            operationAmountField.getText().isEmpty()) {
            showAlert("Error", "Please fill in all fields");
            return false;
        }
        
        try {
            double amount = Double.parseDouble(operationAmountField.getText());
            if (amount <= 0) {
                showAlert("Error", "Amount must be positive");
                return false;
            }
        } catch (NumberFormatException e) {
            showAlert("Error", "Please enter a valid amount");
            return false;
        }
        
        return true;
    }
    
    private boolean validateProfileUpdate() {
        if (profileFirstName.getText().isEmpty() || profileLastName.getText().isEmpty() ||
            profilePhone.getText().isEmpty() || profileEmail.getText().isEmpty()) {
            showAlert("Error", "Please fill in all required fields");
            return false;
        }
        return true;
    }
    
    private boolean validateLoanApplication() {
        if (loanAmountField.getText().isEmpty() || loanPurposeField.getText().isEmpty()) {
            showAlert("Error", "Please fill in all required fields");
            return false;
        }
        
        try {
            double amount = Double.parseDouble(loanAmountField.getText());
            if (amount <= 0) {
                showAlert("Error", "Loan amount must be positive");
                return false;
            }
        } catch (NumberFormatException e) {
            showAlert("Error", "Please enter a valid loan amount");
            return false;
        }
        
        return true;
    }
    
    private Account findAccountByNumber(String accountNumber) {
        for (Account account : accounts) {
            if (account.getAccountNumber().equals(accountNumber)) {
                return account;
            }
        }
        return null;
    }
    
    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}