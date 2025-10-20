public class ChequeAccount extends Account implements Withdrawal {
    private IncomeSource employmentIncome;

    public ChequeAccount(String branch, Customer customer, IncomeSource employmentIncome) {
        super(branch, customer);

        // Validate that we have employment income
        if (employmentIncome == null || !employmentIncome.isEmployment()) {
            throw new IllegalArgumentException("Cheque account requires valid employment income");
        }

        this.employmentIncome = employmentIncome;
    }

    @Override
    public void showAccountType() {
        System.out.println("Cheque Account [" + getAccountNumber() + "] - Employer: " +
                employmentIncome.getSourceName());
    }

    @Override
    public void withdraw(double amount) {
        if (amount <= 0) {
            System.out.println("Withdrawal amount must be positive.");
            return;
        }

        double currentBalance = getBalance();

        if (currentBalance >= amount) {
            updateBalance(currentBalance - amount);

            System.out.println("Withdrew " + amount + " from cheque account " + getAccountNumber());
            System.out.println("New balance: " + getBalance());

            Transaction transaction = new Transaction(
                    getAccountNumber(),
                    "WITHDRAWAL",
                    amount,
                    getBalance(),
                    "Cheque account withdrawal - Employer: " + employmentIncome.getSourceName()
            );
            getTransactionHistory().add(transaction);
        } else {
            System.out.println("Insufficient funds in cheque account " + getAccountNumber());
            System.out.println("Current balance: " + currentBalance + ", Attempted withdrawal: " + amount);
        }
    }

    @Override
    public double getBalance() {
        return super.getBalance();
    }

    public IncomeSource getEmploymentIncome() {
        return employmentIncome;
    }

    public String getEmployerName() {
        return employmentIncome.getSourceName();
    }

    public String getEmployerAddress() {
        return employmentIncome.getEmployerAddress();
    }

    public void displayAccountDetails() {
        showAccountType();
        System.out.println("Employer: " + employmentIncome.getSourceName());
        System.out.println("Employer Address: " + employmentIncome.getEmployerAddress());
        System.out.println("Current Balance: " + getBalance());
        System.out.println("Monthly Salary: " + employmentIncome.getMonthlyAmount());
    }
}
