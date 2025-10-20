import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import javafx.scene.layout.VBox;
import javafx.geometry.Insets;
import javafx.util.Callback;
import javafx.scene.layout.HBox;
import javafx.scene.Node;

public class CustomerDashboardController {

    // Fields from FXML
    @FXML private Label welcomeLabel;
    @FXML private Label balanceLabel;
    @FXML private ListView<Account> accountsListView;
    @FXML private ListView<String> transactionsListView;
    @FXML private ComboBox<Account> transferFromCombo;
    @FXML private TextField targetAccountField;
    @FXML private TextField transferAmountField;
    @FXML private TextField transferDescription;

    private Customer currentCustomer;

    public void setCustomer(Customer customer) {
        this.currentCustomer = customer;
        updateDashboard();
    }

    private void updateDashboard() {
        if (currentCustomer != null) {
            welcomeLabel.setText("Welcome, " + currentCustomer.getDisplayName());
            updateAccountDisplay();
            updateBalanceSummary();
            updateTransferComboBox();
        }
    }

    private void updateAccountDisplay() {
        accountsListView.getItems().clear();

        if (currentCustomer.getAccounts().isEmpty()) {
            // Create a custom cell factory to display message when no accounts
            accountsListView.setCellFactory(lv -> new ListCell<Account>() {
                @Override
                protected void updateItem(Account item, boolean empty) {
                    super.updateItem(item, empty);
                    if (empty) {
                        setText("No accounts available");
                    } else {
                        setText(null);
                    }
                }
            });
            return;
        }

        accountsListView.setCellFactory(lv -> new ListCell<Account>() {
            @Override
            protected void updateItem(Account account, boolean empty) {
                super.updateItem(account, empty);
                if (empty || account == null) {
                    setText(null);
                } else {
                    setText(String.format("%s - %s\nBalance: P%.2f",
                            account.getAccountNumber(),
                            getAccountTypeDisplay(account),
                            account.getBalance()));
                }
            }
        });

        accountsListView.getItems().addAll(currentCustomer.getAccounts());
    }

    private void updateBalanceSummary() {
        double totalBalance = currentCustomer.getAccounts().stream()
                .mapToDouble(Account::getBalance)
                .sum();
        balanceLabel.setText("Current Balance: P" + String.format("%.2f", totalBalance));
    }

    private void updateTransferComboBox() {
        transferFromCombo.getItems().clear();


        transferFromCombo.setCellFactory(lv -> new ListCell<Account>() {
            @Override
            protected void updateItem(Account account, boolean empty) {
                super.updateItem(account, empty);
                if (empty || account == null) {
                    setText(null);
                } else {
                    setText(String.format("%s - %s (P%.2f)",
                            account.getAccountNumber(),
                            getAccountTypeDisplay(account),
                            account.getBalance()));
                }
            }
        });


        transferFromCombo.setButtonCell(new ListCell<Account>() {
            @Override
            protected void updateItem(Account account, boolean empty) {
                super.updateItem(account, empty);
                if (empty || account == null) {
                    setText("Select Account");
                } else {
                    setText(String.format("%s - %s",
                            account.getAccountNumber(),
                            getAccountTypeDisplay(account)));
                }
            }
        });

        transferFromCombo.getItems().addAll(currentCustomer.getAccounts());

        if (!transferFromCombo.getItems().isEmpty()) {
            transferFromCombo.setValue(transferFromCombo.getItems().get(0));
        }
    }

    private String getAccountTypeDisplay(Account account) {
        if (account instanceof SavingsAccount) {
            return "Savings";
        } else if (account instanceof ChequeAccount) {
            return "Cheque";
        } else if (account instanceof InvestmentAccount) {
            return "Investment";
        } else {
            return "Account";
        }
    }

    @FXML
    private void handleDeposit() {
        // Get selected account from ListView
        Account selectedAccount = accountsListView.getSelectionModel().getSelectedItem();
        if (selectedAccount == null) {
            showAlert("Error", "Please select an account from the list");
            return;
        }

        // Create deposit dialog
        Dialog<Double> depositDialog = new Dialog<>();
        depositDialog.setTitle("Deposit Funds");
        depositDialog.setHeaderText("Deposit to " + selectedAccount.getAccountNumber());

        // Set the button types
        ButtonType depositButtonType = new ButtonType("Deposit", ButtonBar.ButtonData.OK_DONE);
        depositDialog.getDialogPane().getButtonTypes().addAll(depositButtonType, ButtonType.CANCEL);

        // Create the amount input
        VBox content = new VBox(10);
        content.setPadding(new Insets(20));
        Label amountLabel = new Label("Amount:");
        TextField amountField = new TextField();
        amountField.setPromptText("Enter amount");
        content.getChildren().addAll(amountLabel, amountField);

        depositDialog.getDialogPane().setContent(content);

        // Convert result to amount
        depositDialog.setResultConverter(dialogButton -> {
            if (dialogButton == depositButtonType) {
                try {
                    return Double.parseDouble(amountField.getText());
                } catch (NumberFormatException e) {
                    return null;
                }
            }
            return null;
        });

        // Show dialog and process result
        depositDialog.showAndWait().ifPresent(amount -> {
            if (amount > 0) {
                selectedAccount.deposit(amount);
                updateDashboard();
                showAlert("Success", String.format("Deposited P%.2f to account %s",
                        amount, selectedAccount.getAccountNumber()));
            } else {
                showAlert("Error", "Amount must be positive");
            }
        });
    }

    @FXML
    private void handleWithdraw() {
        // Get selected account from ListView
        Account selectedAccount = accountsListView.getSelectionModel().getSelectedItem();
        if (selectedAccount == null) {
            showAlert("Error", "Please select an account from the list");
            return;
        }

        // Create withdrawal dialog
        Dialog<Double> withdrawDialog = new Dialog<>();
        withdrawDialog.setTitle("Withdraw Funds");
        withdrawDialog.setHeaderText("Withdraw from " + selectedAccount.getAccountNumber());

        // Set the button types
        ButtonType withdrawButtonType = new ButtonType("Withdraw", ButtonBar.ButtonData.OK_DONE);
        withdrawDialog.getDialogPane().getButtonTypes().addAll(withdrawButtonType, ButtonType.CANCEL);

        // Create the amount input
        VBox content = new VBox(10);
        content.setPadding(new Insets(20));
        Label amountLabel = new Label("Amount:");
        TextField amountField = new TextField();
        amountField.setPromptText("Enter amount");
        content.getChildren().addAll(amountLabel, amountField);

        withdrawDialog.getDialogPane().setContent(content);

        // Convert result to amount
        withdrawDialog.setResultConverter(dialogButton -> {
            if (dialogButton == withdrawButtonType) {
                try {
                    return Double.parseDouble(amountField.getText());
                } catch (NumberFormatException e) {
                    return null;
                }
            }
            return null;
        });

        // Show dialog and process result
        withdrawDialog.showAndWait().ifPresent(amount -> {
            if (amount > 0) {
                try {
                    selectedAccount.withdraw(amount);
                    updateDashboard();
                    showAlert("Success", String.format("Withdrew P%.2f from account %s",
                            amount, selectedAccount.getAccountNumber()));
                } catch (Exception e) {
                    showAlert("Error", "Withdrawal failed: " + e.getMessage());
                }
            } else {
                showAlert("Error", "Amount must be positive");
            }
        });
    }

    @FXML
    private void handleTransfer() {
        try {
            Account fromAccount = transferFromCombo.getValue();
            if (fromAccount == null) {
                showAlert("Error", "Please select source account");
                return;
            }

            String toAccountNumber = targetAccountField.getText().trim();
            if (toAccountNumber.isEmpty()) {
                showAlert("Error", "Please enter target account number");
                return;
            }

            double amount;
            try {
                amount = Double.parseDouble(transferAmountField.getText());
                if (amount <= 0) {
                    showAlert("Error", "Amount must be positive");
                    return;
                }
            } catch (NumberFormatException e) {
                showAlert("Error", "Please enter a valid amount");
                return;
            }

            // Simulate transfer
            if (fromAccount.getBalance() >= amount) {
                fromAccount.withdraw(amount);
                updateDashboard();

                String description = transferDescription.getText().isEmpty() ?
                        "Transfer to " + toAccountNumber : transferDescription.getText();

                showAlert("Success",
                        String.format("Transferred P%.2f from %s to %s",
                                amount, fromAccount.getAccountNumber(), toAccountNumber));

                // Clear form
                targetAccountField.clear();
                transferAmountField.clear();
                transferDescription.clear();

            } else {
                showAlert("Error", "Insufficient funds for transfer");
            }

        } catch (Exception e) {
            showAlert("Error", "Transfer failed: " + e.getMessage());
        }
    }

    @FXML
    private void handleRefresh() {
        updateDashboard();
        showAlert("Info", "Dashboard refreshed");
    }

    @FXML
    private void handleViewProfile() {
        try {
            Dialog<Void> profileDialog = new Dialog<>();
            profileDialog.setTitle("Customer Profile - " + currentCustomer.getDisplayName());
            profileDialog.setHeaderText("Profile Information");

            VBox profileContent = new VBox(10);
            profileContent.setPadding(new Insets(20));

            StringBuilder profileInfo = new StringBuilder();
            profileInfo.append("=== CUSTOMER PROFILE ===\n\n");

            if (currentCustomer instanceof Individual) {
                Individual individual = (Individual) currentCustomer;
                profileInfo.append("INDIVIDUAL CUSTOMER\n");
                profileInfo.append("────────────────────\n");
                profileInfo.append("First Name: ").append(individual.getFirstName()).append("\n");
                profileInfo.append("Surname: ").append(individual.getSurname()).append("\n");
                profileInfo.append("ID Number: ").append(individual.getIdNumber()).append("\n");
                profileInfo.append("Date of Birth: ").append(individual.getDateOfBirth()).append("\n");

                if (individual.getNextOfKin() != null) {
                    profileInfo.append("\nNEXT OF KIN\n");
                    profileInfo.append("───────────\n");
                    profileInfo.append("Name: ").append(individual.getNextOfKin().getFullName()).append("\n");
                    profileInfo.append("Relationship: ").append(individual.getNextOfKin().getRelationship()).append("\n");
                    profileInfo.append("Phone: ").append(individual.getNextOfKin().getPhoneNumber()).append("\n");
                    profileInfo.append("Email: ").append(individual.getNextOfKin().getEmail()).append("\n");
                    profileInfo.append("Address: ").append(individual.getNextOfKin().getAddress()).append("\n");
                }

            } else if (currentCustomer instanceof Company) {
                Company company = (Company) currentCustomer;
                profileInfo.append("COMPANY CUSTOMER\n");
                profileInfo.append("────────────────\n");
                profileInfo.append("Company Name: ").append(company.getCompanyName()).append("\n");
                profileInfo.append("Registration: ").append(company.getRegistrationNumber()).append("\n");
                profileInfo.append("Business Type: ").append(company.getBusinessType()).append("\n");
                profileInfo.append("Contact Person: ").append(company.getContactPersonName()).append("\n");
                profileInfo.append("Annual Revenue: P").append(String.format("%.2f", company.getAnnualRevenue())).append("\n");
            }

            profileInfo.append("\nCONTACT INFORMATION\n");
            profileInfo.append("───────────────────\n");
            profileInfo.append("Email: ").append(currentCustomer.getContactEmail()).append("\n");
            profileInfo.append("Phone: ").append(currentCustomer.getContactPhone()).append("\n");
            profileInfo.append("Address: ").append(currentCustomer.getAddress()).append("\n");
            profileInfo.append("Member Since: ").append(currentCustomer.getRegistrationDate()).append("\n");

            profileInfo.append("\nACCOUNT SUMMARY\n");
            profileInfo.append("───────────────\n");
            profileInfo.append("Total Accounts: ").append(currentCustomer.getAccounts().size()).append("\n");

            double totalBalance = currentCustomer.getAccounts().stream()
                    .mapToDouble(Account::getBalance)
                    .sum();
            profileInfo.append("Total Balance: P").append(String.format("%.2f", totalBalance)).append("\n");

            // List all accounts
            profileInfo.append("\nACCOUNTS:\n");
            profileInfo.append("─────────\n");
            for (Account account : currentCustomer.getAccounts()) {
                profileInfo.append(String.format("- %s (%s): P%.2f\n",
                        account.getAccountNumber(),
                        getAccountTypeDisplay(account),
                        account.getBalance()));
            }

            TextArea profileText = new TextArea(profileInfo.toString());
            profileText.setEditable(false);
            profileText.setWrapText(true);
            profileText.setPrefSize(500, 400);
            profileText.setStyle("-fx-font-family: 'Monospaced'; -fx-font-size: 12px;");

            profileContent.getChildren().add(profileText);
            profileDialog.getDialogPane().getButtonTypes().add(ButtonType.CLOSE);
            profileDialog.getDialogPane().setContent(profileContent);
            profileDialog.showAndWait();

        } catch (Exception e) {
            showAlert("Error", "Failed to display profile: " + e.getMessage());
        }
    }

    @FXML
    private void handleLogout() {
        try {
            Alert confirmAlert = new Alert(Alert.AlertType.CONFIRMATION);
            confirmAlert.setTitle("Confirm Logout");
            confirmAlert.setHeaderText("Logout Confirmation");
            confirmAlert.setContentText("Are you sure you want to logout?");

            if (confirmAlert.showAndWait().orElse(ButtonType.CANCEL) == ButtonType.OK) {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("mainmenu.fxml"));
                Parent root = loader.load();

                Stage mainMenuStage = new Stage();
                mainMenuStage.setTitle("Banking System - Main Menu");
                mainMenuStage.setScene(new Scene(root, 800, 600));
                mainMenuStage.setResizable(false);
                mainMenuStage.show();

                Stage currentStage = (Stage) welcomeLabel.getScene().getWindow();
                currentStage.close();
            }
        } catch (Exception e) {
            showAlert("Error", "Error during logout: " + e.getMessage());
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