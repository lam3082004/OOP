package Controllers;

import Common.DatabaseConnection;
import Common.Constants.DocumentType;
import Models.Author;
import Models.DocumentFactory.Document;
import Models.DocumentFactory.DocumentFactory;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DocumentController {
    // Tạo mới 1 bản ghi document và lưu vào mySql
    public int createDocument(Document document) {
        int result = 0;
        try {
            Connection connection = DatabaseConnection.getInstance().getConnection();
            String insertQuery = "INSERT INTO document VALUES (?, ?, ?, ?, ?, ?, ?)";
            PreparedStatement preparedStatement = connection.prepareStatement(insertQuery);
            preparedStatement.setInt(1, document.getId());
            preparedStatement.setString(2, document.getName());
            preparedStatement.setInt(3, document.getAuthorId());
            preparedStatement.setDouble(4, document.getPrice());
            preparedStatement.setString(5, document.getDescription());
            preparedStatement.setString(6, document.getType());
            preparedStatement.setInt(7, document.getQuantity());
            result = preparedStatement.executeUpdate();
            connection.close();
        } catch (SQLException e) {
            // Thông báo lỗi kết nối
            throw new RuntimeException(e);
        }
        return result;
    }

    public int updateDocument(Document document) {
        int result = 0;
        try {
            Connection connection = DatabaseConnection.getInstance().getConnection();
            String updateQuery = "UPDATE document " +
                    "SET Name = ?," +
                    "AuthorID = ?, " +
                    "Price = ?, " +
                    "Description = ?, " +
                    "Type = ?, " +
                    "Quantity = ? " +
                    "WHERE ID = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(updateQuery);
            preparedStatement.setString(1, document.getName());
            preparedStatement.setInt(2, document.getAuthorId());
            preparedStatement.setDouble(3, document.getPrice());
            preparedStatement.setString(4, document.getDescription());
            preparedStatement.setString(5, document.getType());
            preparedStatement.setInt(6, document.getQuantity());
            preparedStatement.setInt(7, document.getId());
            result = preparedStatement.executeUpdate();
            connection.close();
        } catch (SQLException e) {
            // Thông báo lỗi kết nối
            throw new RuntimeException(e);
        }
        return result;
    }

    public int deleteDocument(int id) {
        int result = 0;
        try {
            Connection connection = DatabaseConnection.getInstance().getConnection();
            String deleteQuery = "DELETE FROM document WHERE ID = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(deleteQuery);
            preparedStatement.setInt(1, id);
            result = preparedStatement.executeUpdate();
            connection.close();
        } catch (SQLException e) {
            // Thông báo lỗi kết nối
            throw new RuntimeException(e);
        }
        return result;
    }

    public List<Document> getDocuments() {
        List<Document> documents = new ArrayList<>();
        try {
            Connection connection = DatabaseConnection.getInstance().getConnection();
            String selectQuery = "SELECT * FROM document";
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(selectQuery);
            while (resultSet.next()) {
                Document document =
                        DocumentFactory.createDocument(DocumentType.fromString(resultSet.getString("Type")),
                                resultSet.getInt("ID"),
                                resultSet.getString("Name"),
                                resultSet.getInt("AuthorID"),
                                resultSet.getInt("Quantity"),
                                resultSet.getDouble("Price"),
                                resultSet.getString("Description"));
                documents.add(document);
            }
            connection.close();
        } catch (SQLException e) {
            // Thông báo lỗi kết nối
            throw new RuntimeException(e);
        }
        return documents;
    }

    public Document getDocumentByID(int id) {
        Document document = null;
        try {
            Connection connection = DatabaseConnection.getInstance().getConnection();
            String selectQuery = "SELECT * FROM document WHERE ID = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(selectQuery);
            preparedStatement.setInt(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                document =
                        DocumentFactory.createDocument(DocumentType.fromString(resultSet.getString("Type")),
                                resultSet.getInt("ID"),
                                resultSet.getString("Name"),
                                resultSet.getInt("AuthorID"),
                                resultSet.getInt("Quantity"),
                                resultSet.getDouble("Price"),
                                resultSet.getString("Description"));
            }
            connection.close();
        } catch (SQLException e) {
            // Thông báo lỗi kết nối
            throw new RuntimeException(e);
        }
        return document;
    }

    public List<Document> getDocumentByAuthor(int authorId) {
        List<Document> documents = new ArrayList<>();
        try {
            Connection connection = DatabaseConnection.getInstance().getConnection();
            String selectQuery = "SELECT * FROM document WHERE AuthorID = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(selectQuery);
            preparedStatement.setInt(1, authorId);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                Document document =
                        DocumentFactory.createDocument(DocumentType.fromString(resultSet.getString("Type")),
                                resultSet.getInt("ID"),
                                resultSet.getString("Name"),
                                resultSet.getInt("AuthorID"),
                                resultSet.getInt("Quantity"),
                                resultSet.getDouble("Price"),
                                resultSet.getString("Description"));
                documents.add(document);
            }
            connection.close();
        } catch (SQLException e) {
            // Thông báo lỗi kết nối
            throw new RuntimeException(e);
        }
        return documents;
    }

    public List<Document> searchDocuments(String search) {
        List<Document> documents = new ArrayList<>();
        try {
            Connection connection = DatabaseConnection.getInstance().getConnection();
            String searchQuery = "SELECT d.* " +
                    "FROM document d " +
                    "INNER JOIN author a " +
                    "ON d.AuthorID = a.ID " +
                    "WHERE d.Name LIKE ? " +
                    "OR CONCAT(a.Firstname, ' ', a.Lastname) LIKE ? " +
                    "OR d.Price LIKE ? " +
                    "OR d.Description LIKE ? " +
                    "OR d.Type LIKE ?";
            String searchPattern = "%" + search + "%";
            PreparedStatement preparedStatement = connection.prepareStatement(searchQuery);
            preparedStatement.setString(1, searchPattern);
            preparedStatement.setString(2, searchPattern);
            preparedStatement.setString(3, searchPattern);
            preparedStatement.setString(4, searchPattern);
            preparedStatement.setString(5, searchPattern);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                Document document =
                        DocumentFactory.createDocument(DocumentType.fromString(resultSet.getString("Type")),
                                resultSet.getInt("ID"),
                                resultSet.getString("Name"),
                                resultSet.getInt("AuthorID"),
                                resultSet.getInt("Quantity"),
                                resultSet.getDouble("Price"),
                                resultSet.getString("Description"));
                documents.add(document);
            }
            connection.close();
        } catch (SQLException e) {
            // Thông báo lỗi kết nối
            throw new RuntimeException(e);
        }
        return documents;
    }

    public List<Document> getDocumentInBorrow(int borrowId) {
        List<Document> documents = new ArrayList<>();
        try {
            Connection connection = DatabaseConnection.getInstance().getConnection();
            String searchQuery = "SELECT d.* FROM document d " +
                    "INNER JOIN borrowdetail bd " +
                    "ON d.ID = bd.DocumentID " +
                    "WHERE bd.BorrowID = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(searchQuery);
            preparedStatement.setInt(1, borrowId);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                Document document =
                        DocumentFactory.createDocument(DocumentType.fromString(resultSet.getString("Type")),
                                resultSet.getInt("ID"),
                                resultSet.getString("Name"),
                                resultSet.getInt("AuthorID"),
                                resultSet.getInt("Quantity"),
                                resultSet.getDouble("Price"),
                                resultSet.getString("Description"));
                documents.add(document);
            }
            connection.close();
        } catch (SQLException e) {
            // Thông báo lỗi kết nối
            throw new RuntimeException(e);
        }
        return documents;
    }

    public List<Document> getDocumentNotInBorrow(int borrowId) {
        List<Document> documents = new ArrayList<>();
        try {
            Connection connection = DatabaseConnection.getInstance().getConnection();
            String searchQuery = "SELECT d.* FROM document d " +
                    "LEFT JOIN borrowdetail bd " +
                    "ON d.ID = bd.DocumentID " +
                    "WHERE bd.BorrowID <> ? " +
                    "OR bd.DocumentID IS NULL " +
                    "GROUP BY d.ID, d.Name, d.Quantity " +
                    "HAVING d.Quantity - COALESCE(COUNT(CASE WHEN bd.Status = 0 THEN bd.ID END), 0) > 0";
            PreparedStatement preparedStatement = connection.prepareStatement(searchQuery);
            preparedStatement.setInt(1, borrowId);
            System.out.println(preparedStatement);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                Document document =
                        DocumentFactory.createDocument(DocumentType.fromString(resultSet.getString("Type")),
                                resultSet.getInt("ID"),
                                resultSet.getString("Name"),
                                resultSet.getInt("AuthorID"),
                                resultSet.getInt("Quantity"),
                                resultSet.getDouble("Price"),
                                resultSet.getString("Description"));
                documents.add(document);
            }
            connection.close();
        } catch (SQLException e) {
            // Thông báo lỗi kết nối
            throw new RuntimeException(e);
        }
        return documents;
    }
}
