import java.util.Date;

public class Individual extends Customer {
    private String firstName;
    private String surname;
    private String idNumber;
    private Date dateOfBirth;
    private NextOfKin nextOfKin;
    private IncomeSource incomeSource;

    public Individual(String firstName, String surname, String address,
                      String idNumber, Date dateOfBirth, String email,
                      String phone, NextOfKin nextOfKin) {
        super(address, email, phone);
        this.firstName = firstName;
        this.surname = surname;
        this.idNumber = idNumber;
        this.dateOfBirth = dateOfBirth;
        this.nextOfKin = nextOfKin;
    }

    // Individual-specific methods
    public String getFirstName() { return firstName; }
    public String getSurname() { return surname; }
    public String getIdNumber() { return idNumber; }
    public Date getDateOfBirth() { return dateOfBirth; }
    public NextOfKin getNextOfKin() { return nextOfKin; }
    public IncomeSource getIncomeSource() { return incomeSource; }

    public void setIncomeSource(IncomeSource incomeSource) {
        this.incomeSource = incomeSource;
    }

    // Implement abstract methods
    @Override
    public String getDisplayName() {
        return firstName + " " + surname;
    }

    @Override
    public String getIdentificationNumber() {
        return idNumber;
    }

    @Override
    public boolean isValidCustomer() {
        return firstName != null && !firstName.trim().isEmpty() &&
                surname != null && !surname.trim().isEmpty() &&
                idNumber != null && !idNumber.trim().isEmpty() &&
                getAddress() != null && !getAddress().trim().isEmpty() &&
                nextOfKin != null;
    }

    // Individual-specific business logic
    public boolean isEmployed() {
        return incomeSource != null && incomeSource.isEmployment() && incomeSource.isValid();
    }

    public boolean hasVerifiedIncome() {
        return incomeSource != null && incomeSource.isValid();
    }

    public double getMonthlyIncome() {
        return (incomeSource != null && incomeSource.isValid()) ? incomeSource.getMonthlyAmount() : 0.0;
    }

    public String getFullName() {
        return getDisplayName();
    }
}