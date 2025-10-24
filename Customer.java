public class Customer extends User {
    private String customerId;
    private String phone;
    private String email;
    private String address;
    private String dateOfBirth;
    private String nationality;
    private String idNumber;
    private String kinName;
    private String kinRelationship;
    private String kinPhone;
    private String kinAddress;
    
    public Customer(String username, String password, String firstName, String lastName,
                   String phone, String email, String address, String dateOfBirth, String nationality) {
        super(username, password, firstName, lastName);
        this.phone = phone;
        this.email = email;
        this.address = address;
        this.dateOfBirth = dateOfBirth;
        this.nationality = nationality;
    }
    
    public String getCustomerId() { return customerId; }
    public void setCustomerId(String customerId) { this.customerId = customerId; }
    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }
    public String getDateOfBirth() { return dateOfBirth; }
    public void setDateOfBirth(String dateOfBirth) { this.dateOfBirth = dateOfBirth; }
    public String getNationality() { return nationality; }
    public void setNationality(String nationality) { this.nationality = nationality; }
    public String getIdNumber() { return idNumber; }
    public void setIdNumber(String idNumber) { this.idNumber = idNumber; }
    public String getKinName() { return kinName; }
    public void setKinName(String kinName) { this.kinName = kinName; }
    public String getKinRelationship() { return kinRelationship; }
    public void setKinRelationship(String kinRelationship) { this.kinRelationship = kinRelationship; }
    public String getKinPhone() { return kinPhone; }
    public void setKinPhone(String kinPhone) { this.kinPhone = kinPhone; }
    public String getKinAddress() { return kinAddress; }
    public void setKinAddress(String kinAddress) { this.kinAddress = kinAddress; }
}