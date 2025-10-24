public abstract class Account {
    protected String accountNumber;
    protected double balance;
    protected Customer customer;
    protected String accountType;
    
    public Account(String accountNumber, double balance, Customer customer) {
        this.accountNumber = accountNumber;
        this.balance = balance;
        this.customer = customer;
    }
    
    public String getAccountNumber() { return accountNumber; }
    public double getBalance() { return balance; }
    public Customer getCustomer() { return customer; }
    public String getAccountType() { return accountType; }
    public String getCustomerName() { return customer.getFullName(); }
    public String getBalanceFormatted() { return String.format("BWP %.2f", balance); }
    
    public abstract boolean deposit(double amount);
    public abstract boolean withdraw(double amount);
}