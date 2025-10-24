import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Transaction {
    private String transactionId;
    private LocalDateTime date;
    private String type;
    private double amount;
    private String description;
    private String accountNumber;
    
    public Transaction(String transactionId, LocalDateTime date, String type, 
                      double amount, String description, String accountNumber) {
        this.transactionId = transactionId;
        this.date = date;
        this.type = type;
        this.amount = amount;
        this.description = description;
        this.accountNumber = accountNumber;
    }
    
    public String getTransactionId() { return transactionId; }
    public LocalDateTime getDate() { return date; }
    public String getType() { return type; }
    public double getAmount() { return amount; }
    public String getDescription() { return description; }
    public String getAccountNumber() { return accountNumber; }
    
    public String getDateFormatted() { 
        return date.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")); 
    }
    public String getAmountFormatted() { 
        return String.format("BWP %.2f", amount); 
    }
}