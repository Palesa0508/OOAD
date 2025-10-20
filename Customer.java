import java.util.Date;
import java.util.ArrayList;
import java.util.List;

public abstract class Customer {
    protected String address;
    protected String contactEmail;
    protected String contactPhone;
    protected Date registrationDate;
    protected List<Account> accounts; // Add accounts list

    public Customer(String address, String contactEmail, String contactPhone) {
        this.address = address;
        this.contactEmail = contactEmail;
        this.contactPhone = contactPhone;
        this.registrationDate = new Date();
        this.accounts = new ArrayList<>(); // Initialize accounts list
    }

    // Common getters and setters
    public String getAddress() { return address; }
    public String getContactEmail() { return contactEmail; }
    public String getContactPhone() { return contactPhone; }
    public Date getRegistrationDate() { return registrationDate; }
    public List<Account> getAccounts() { return accounts; } // Add this method

    public void setAddress(String address) { this.address = address; }
    public void setContactEmail(String contactEmail) { this.contactEmail = contactEmail; }
    public void setContactPhone(String contactPhone) { this.contactPhone = contactPhone; }

    // Method to add an account
    public void addAccount(Account account) {
        this.accounts.add(account);
    }

    // Abstract methods that all customers must implement
    public abstract String getDisplayName();
    public abstract String getIdentificationNumber();
    public abstract boolean isValidCustomer();
}