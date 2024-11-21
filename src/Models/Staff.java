package Models;

public class Staff extends User {
    private String username;
    private String password;

    public Staff() {
    }

    public Staff(int id,
                 String firstName,
                 String lastName,
                 String address,
                 String phoneNumber,
                 String username,
                 String password) {
        super(id, firstName, lastName, address, phoneNumber);
        this.username = username;
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public void showInfo() {

    }

    @Override
    public String getFullname() {
        return "NV:" + this.getFirstName() + " " + this.getLastName();
    }
}
