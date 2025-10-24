public class SavingsAccount extends Account implements InterestBearing {
    private static final double INTEREST_RATE = 0.0005; // 0.05% monthly
    
    public SavingsAccount(String accountNumber, double balance, Customer customer) {
        super(accountNumber, balance, customer);
        this.accountType = "Savings";
    }
    
    @Override
    public boolean deposit(double amount) {
        if (amount > 0) {
            balance += amount;
            return true;
        }
        return false;
    }
    
    @Override
    public boolean withdraw(double amount) {
        return false; // No withdrawals allowed for savings account
    }
    
    @Override
    public void applyInterest() {
        balance += balance * INTEREST_RATE;
    }
    
    @Override
    public double getInterestRate() {
        return INTEREST_RATE;
    }
}