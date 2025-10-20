import java.util.Date;

public class Company extends Customer {
    private String companyName;
    private String registrationNumber;
    private String businessType;
    private String contactPersonName;
    private double annualRevenue;

    public Company(String companyName, String registrationNumber, String businessType,
                   String address, String contactEmail, String contactPhone,
                   String contactPersonName, double annualRevenue) {
        super(address, contactEmail, contactPhone);
        this.companyName = companyName;
        this.registrationNumber = registrationNumber;
        this.businessType = businessType;
        this.contactPersonName = contactPersonName;
        this.annualRevenue = annualRevenue;
    }

    // Company-specific methods
    public String getCompanyName() { return companyName; }
    public String getRegistrationNumber() { return registrationNumber; }
    public String getBusinessType() { return businessType; }
    public String getContactPersonName() { return contactPersonName; }
    public double getAnnualRevenue() { return annualRevenue; }

    // Implement abstract methods
    @Override
    public String getDisplayName() {
        return companyName;
    }

    @Override
    public String getIdentificationNumber() {
        return registrationNumber;
    }

    @Override
    public boolean isValidCustomer() {
        return companyName != null && !companyName.trim().isEmpty() &&
                registrationNumber != null && !registrationNumber.trim().isEmpty() &&
                address != null && !address.trim().isEmpty() &&
                contactPersonName != null && !contactPersonName.trim().isEmpty();
    }

    // Company-specific business logic
    public boolean isLargeBusiness() {
        return annualRevenue > 1000000.00; // Over 1 million annual revenue
    }

    public boolean isSmallBusiness() {
        return annualRevenue <= 500000.00; // Up to 500k annual revenue
    }
}