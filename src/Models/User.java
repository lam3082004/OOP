package Models;

public class User extends Person {
    private String address;
    private String phoneNumber;

    public User() {
    }

    public User(String firstName, String lastName, String address, String phoneNumber) {
        super(firstName, lastName);
        this.address = address;
        this.phoneNumber = phoneNumber;
    }

    public User(int id, String firstName, String lastName, String address, String phoneNumber) {
        super(id, firstName, lastName);
        this.address = address;
        this.phoneNumber = phoneNumber;
    }

    @Override
    public void showInfo() {

    }

    @Override
    public String getFullname() {
        return "ĐG: " + this.getFirstName() + " " + this.getLastName();
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }
}
