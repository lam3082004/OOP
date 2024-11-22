package Models.DocumentFactory;

import Common.Constants.DocumentType;
import Models.Author;

public class Magazine extends Document{
    public Magazine(int id,
                  String name,
                  int authorId,
                  int quantity,
                  double price,
                  String description) {
        super(id, name, authorId, quantity, price, description, DocumentType.MAGAZINE.toString());
    }
}
