public class LoginService {

    public AuthContext login(String username, String password) {
        System.out.println("Attempting login for: " + username);

        if ("user1".equals(username) && "pass1".equals(password)) {
            NextOfKin nextOfKin1 = new NextOfKin("Morwa", "North", "brother", "555-1234", "morwa@gmail.com", "456 Oak Ave");
            Individual customer = new Individual("Sunflower", "Lesedi", "456 Oak Ave", "ID456", new java.util.Date(), "lesedi@gmail.com", "555-0001", nextOfKin1);
            System.out.println("✅ Found customer: " + customer.getFirstName());
            return new AuthContext(username, "CUSTOMER", customer);
        }
        else if ("teller1".equals(username) && "pass1".equals(password)) {
            BankTeller teller = new BankTeller("May", "Sab", "EMP123", "BRANCH01");
            System.out.println("✅ Found bank teller: " + teller.getFullName());
            return new AuthContext(username, "BANK_TELLER", teller);
        }
        else if ("customer1".equals(username) && "pass123".equals(password)) {
            NextOfKin nextOfKin2 = new NextOfKin("Mary", "Doe", "sister", "555-5678", "mary.doe@email.com", "123 Main St");
            Individual customer = new Individual("Leatile", "Morerwa", "123 Main St", "ID123", new java.util.Date(), "leatilemorerwa@gmail.com", "555-0002", nextOfKin2);
            System.out.println("✅ Found customer: " + customer.getFirstName());
            return new AuthContext(username, "CUSTOMER", customer);
        }

        System.out.println("❌ Login failed: Invalid credentials");
        return null;
    }
}