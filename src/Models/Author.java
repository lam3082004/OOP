package Models;

public class Author extends Person {
    private String description;

    public Author() {
    }

    public Author(String firstName, String lastName, String description) {
        super(firstName, lastName);
        this.description = description;
    }

    public Author(int id, String firstName, String lastName, String description) {
        super(id, firstName, lastName);
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    // Override abstract method in Person class
    // Polymorphism in OOP
    @Override
    public void showInfo() {

    }

    @Override
    public String getFullname() {
        return "TG:" + this.getFirstName() + " " + this.getLastName();
    }
}
