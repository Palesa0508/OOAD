import java.util.Date;

public class NextOfKin {
    private String firstName;
    private String surname;
    private String relationship;
    private String phoneNumber;
    private String email;
    private String address;
    private Date dateAdded;

    public NextOfKin(String firstName, String surname, String relationship,
                     String phoneNumber, String email, String address) {
        this.firstName = firstName;
        this.surname = surname;
        this.relationship = relationship;
        this.phoneNumber = phoneNumber;
        this.email = email;
        this.address = address;
        this.dateAdded = new Date();
    }

    // Getters and Setters
    public String getFirstName() { return firstName; }
    public void setFirstName(String firstName) { this.firstName = firstName; }

    public String getSurname() { return surname; }
    public void setSurname(String surname) { this.surname = surname; }

    public String getRelationship() { return relationship; }
    public void setRelationship(String relationship) { this.relationship = relationship; }

    public String getPhoneNumber() { return phoneNumber; }
    public void setPhoneNumber(String phoneNumber) { this.phoneNumber = phoneNumber; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }

    public Date getDateAdded() { return dateAdded; }

    public String getFullName() {
        return firstName + " " + surname;
    }

    @Override
    public String toString() {
        return "NextOfKin{" +
                "name='" + getFullName() + '\'' +
                ", relationship='" + relationship + '\'' +
                ", phone='" + phoneNumber + '\'' +
                '}';
    }
}