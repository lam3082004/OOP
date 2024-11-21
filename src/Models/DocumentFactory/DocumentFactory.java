package Models.DocumentFactory;

import Common.Constants.DocumentType;
import Models.Author;

public class DocumentFactory {
    public static Document createDocument(DocumentType documentType,
                                          int id,
                                          String name,
                                          int authorId,
                                          int quantity,
                                          double price,
                                          String description) {
        return switch (documentType) {
            case BOOK -> new Book(id, name, authorId, quantity, price, description);
            case THESIS -> new Thesis(id, name, authorId, quantity, price, description);
            case MAGAZINE -> new Magazine(id, name, authorId, quantity, price, description);
            case NEWSPAPER -> new Newspaper(id, name, authorId, quantity, price, description);
            default -> throw new IllegalArgumentException("Loại tài liệu không tồn tại");
        };
    }
}
