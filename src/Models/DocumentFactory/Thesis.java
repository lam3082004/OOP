package Models.DocumentFactory;

import Common.Constants.DocumentType;
import Models.Author;

public class Thesis extends Document {
    public Thesis(int id,
                  String name,
                  int authorId,
                  int quantity,
                  double price,
                  String description) {
        super(id, name, authorId, quantity, price, description, DocumentType.THESIS.toString());
    }
}
