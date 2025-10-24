public class ChequeAccount extends Account {
    private String companyName;
    private String companyAddress;
    
    public ChequeAccount(String accountNumber, double balance, Customer customer, 
                        String companyName, String companyAddress) {
        super(accountNumber, balance, customer);
        this.companyName = companyName;
        this.companyAddress = companyAddress;
        this.accountType = "Cheque";
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
        if (amount > 0 && balance >= amount) {
            balance -= amount;
            return true;
        }
        return false;
    }
    
    public String getCompanyName() { return companyName; }
    public String getCompanyAddress() { return companyAddress; }
}