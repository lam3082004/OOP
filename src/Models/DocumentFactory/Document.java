package Models.DocumentFactory;

public class Document {
    private int id;
    private String name;
    private int authorId;
    private int quantity;
    private double price;
    private String description;
    private String type;

    public Document() {
    }

    public Document(int id,
                    String name,
                    int authorId,
                    int quantity,
                    double price,
                    String description,
                    String type) {
        this.id = id;
        this.name = name;
        this.authorId = authorId;
        this.quantity = quantity;
        this.price = price;
        this.description = description;
        this.type = type;
    }

    public Document(String name,
                    int authorId,
                    int quantity,
                    double price,
                    String description,
                    String type) {
        this.name = name;
        this.authorId = authorId;
        this.quantity = quantity;
        this.price = price;
        this.description = description;
        this.type = type;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAuthorId() {
        return authorId;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public void setAuthorId(int authorId) {
        this.authorId = authorId;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getType() {
        return type;
    }
}
