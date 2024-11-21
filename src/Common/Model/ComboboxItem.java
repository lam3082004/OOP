package Common.Model;

public class ComboboxItem {
    private int id;
    private String name;

    public ComboboxItem(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return name; // Chỉ hiển thị tên trong JComboBox
    }
}

