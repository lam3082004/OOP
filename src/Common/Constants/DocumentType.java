package Common.Constants;

public enum DocumentType {
    BOOK,
    MAGAZINE,
    THESIS,
    NEWSPAPER;

    // Phương thức để lấy DocumentType dựa theo chuỗi
    public static DocumentType fromString(String value) {
        for (DocumentType type : DocumentType.values()) {
            if (type.name().equalsIgnoreCase(value)) {
                return type;
            }
        }
        throw new IllegalArgumentException("Unknown DocumentType: " + value);
    }

    public static String toString(String type) {
        switch (type) {
            case "BOOK" -> {
                return "Sách";
            }
            case "MAGAZINE" -> {
                return "Tạp chí";
            }
            case "THESIS" -> {
                return "Luận văn";
            }
            case "NEWSPAPER" -> {
                return "Báo";
            }
            default -> {
                return "";
            }
        }
    }
}
