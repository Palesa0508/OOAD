public class InvestmentAccount extends Account implements InterestBearing {
    private static final double INTEREST_RATE = 0.05; // 5% monthly
    private static final double MINIMUM_BALANCE = 500.00;
    
    public InvestmentAccount(String accountNumber, double balance, Customer customer) {
        super(accountNumber, balance, customer);
        this.accountType = "Investment";
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
        if (amount > 0 && (balance - amount) >= MINIMUM_BALANCE) {
            balance -= amount;
            return true;
        }
        return false;
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