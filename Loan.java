import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Loan {
    private String loanId;
    private double amount;
    private String purpose;
    private Customer customer;
    private LocalDateTime applicationDate;
    private String status;
    
    public Loan(String loanId, double amount, String purpose, Customer customer) {
        this.loanId = loanId;
        this.amount = amount;
        this.purpose = purpose;
        this.customer = customer;
        this.applicationDate = LocalDateTime.now();
        this.status = "PENDING";
    }
    
    public String getLoanId() { return loanId; }
    public double getAmount() { return amount; }
    public String getPurpose() { return purpose; }
    public Customer getCustomer() { return customer; }
    public String getCustomerName() { return customer.getFullName(); }
    public LocalDateTime getApplicationDate() { return applicationDate; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    
    public String getAmountFormatted() { return String.format("BWP %.2f", amount); }
    public String getApplicationDateFormatted() { 
        return applicationDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")); 
    }
}