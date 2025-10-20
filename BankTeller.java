public class BankTeller {
    private String tellerId;
    private String firstName;
    private String lastName;
    private String employeeId;
    private String branchCode;

    public BankTeller(String firstName, String lastName, String employeeId, String branchCode) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.employeeId = employeeId;
        this.branchCode = branchCode;
        this.tellerId = "TEL_" + employeeId;
    }

    // Getters
    public String getTellerId() { return tellerId; }
    public String getFirstName() { return firstName; }
    public String getLastName() { return lastName; }
    public String getFullName() { return firstName + " " + lastName; }
    public String getEmployeeId() { return employeeId; }
    public String getBranchCode() { return branchCode; }

    public Account openAccount(Customer customer, String accountType, String branch) {
        Account newAccount = null;

        if (accountType.equalsIgnoreCase("savings")) {
            newAccount = new SavingsAccount(branch, customer);

        } else if (accountType.equalsIgnoreCase("cheque")) {
            if (customer instanceof Individual) {
                Individual individual = (Individual) customer;
                IncomeSource income = individual.getIncomeSource();

                if (income != null && income.isEmployment() && income.isValid()) {
                    newAccount = new ChequeAccount(branch, customer, income);
                } else {
                    System.out.println("Cannot open cheque account - no verified employment");
                    return null;
                }
            } else {
                System.out.println("Cheque accounts only available for individual customers");
                return null;
            }

        } else if (accountType.equalsIgnoreCase("investment")) {
            if (customer instanceof Individual) {
                Individual individual = (Individual) customer;
                if (individual.getMonthlyIncome() >= 500.0) {
                    newAccount = new InvestmentAccount(branch, customer, 500.0);
                } else {
                    System.out.println("Insufficient income for investment account");
                    return null;
                }
            } else if (customer instanceof Company) {
                // For companies, use a higher minimum deposit
                newAccount = new InvestmentAccount(branch, customer, 1000.0);
            }
        }

        if (newAccount != null) {
            // Use the addAccount method from Customer class - this is the correct way
            customer.addAccount(newAccount);
            System.out.println("Bank Teller " + getFullName() + " opened new " + accountType +
                    " account (#" + newAccount.getAccountNumber() + ") for " + customer.getDisplayName());
        }
        return newAccount;
    }

    // Helper method to check if a customer can open a specific account type
    public boolean canOpenAccountType(Customer customer, String accountType) {
        if (accountType.equalsIgnoreCase("cheque")) {
            return customer instanceof Individual;
        }
        return true; // Savings and Investment are available for both
    }
}