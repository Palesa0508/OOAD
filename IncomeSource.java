import java.util.Date;

public class IncomeSource {
    private String sourceType; // "EMPLOYMENT", "ALLOWANCE", "PENSION", "INVESTMENT", "OTHER"
    private String sourceName;
    private double monthlyAmount;
    private String employerAddress; // Only used for employment
    private String description;

    public IncomeSource(String sourceType, String sourceName, double monthlyAmount,
                        String employerAddress, String description) {
        this.sourceType = sourceType;
        this.sourceName = sourceName;
        this.monthlyAmount = monthlyAmount;
        this.employerAddress = employerAddress;
        this.description = description;
    }

    // Simple validation
    public boolean isValid() {
        if (monthlyAmount <= 0) {
            return false;
        }

        // For employment, check address too
        if ("EMPLOYMENT".equals(sourceType)) {
            return employerAddress != null && !employerAddress.trim().isEmpty();
        }

        return true;
    }

    public boolean isEmployment() {
        return "EMPLOYMENT".equals(sourceType);
    }

    // Simple factory methods
    public static IncomeSource createEmployment(String companyName, double monthlyAmount,
                                                String employerAddress, String description) {
        return new IncomeSource("EMPLOYMENT", companyName, monthlyAmount,
                employerAddress, description);
    }

    public static IncomeSource createAllowance(String provider, double monthlyAmount,
                                               String description) {
        return new IncomeSource("ALLOWANCE", provider + " Allowance", monthlyAmount,
                null, description);
    }

    public static IncomeSource createPension(String provider, double monthlyAmount,
                                             String description) {
        return new IncomeSource("PENSION", provider + " Pension", monthlyAmount,
                null, description);
    }

    // Getters
    public String getSourceType() { return sourceType; }
    public String getSourceName() { return sourceName; }
    public double getMonthlyAmount() { return monthlyAmount; }
    public String getEmployerAddress() { return employerAddress; }
    public String getDescription() { return description; }
}





