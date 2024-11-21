package Controllers;

import Common.DatabaseConnection;
import Common.Model.ComboboxItem;
import Models.Author;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class AuthorController {

    // Thêm mới author vào database
    public int createAuthor(Author author) {
        int result = 0;
        try {
            Connection connection = DatabaseConnection.getInstance().getConnection();
            String insertQuery = "INSERT INTO author VALUES (?, ?, ?, ?)";
            PreparedStatement preparedStatement = connection.prepareStatement(insertQuery);
            preparedStatement.setInt(1, author.getId());
            preparedStatement.setString(2, author.getFirstName());
            preparedStatement.setString(3, author.getLastName());
            preparedStatement.setString(4, author.getDescription());
            result = preparedStatement.executeUpdate();
            connection.close();
        } catch (SQLException e) {
            // Thông báo lỗi kết nối
            throw new RuntimeException(e);
        }
        return result;
    }
    // Cập nhật bản ghi author trong database
    public int updateAuthor(Author author) {
        int result = 0;
        try {
            Connection connection = DatabaseConnection.getInstance().getConnection();
            String updateQuery = "UPDATE author SET Firstname = ?, " +
                    "Lastname = ?, " +
                    "Description = ? " +
                    "WHERE ID = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(updateQuery);
            preparedStatement.setString(1, author.getFirstName());
            preparedStatement.setString(2, author.getLastName());
            preparedStatement.setString(3, author.getDescription());
            preparedStatement.setInt(4, author.getId());
            result = preparedStatement.executeUpdate();
            connection.close();
        } catch (SQLException e) {
            // Thông báo lỗi kết nối
            throw new RuntimeException(e);
        }
        return result;
    }
    // Xoá bản ghi author trong database
    public int deleteAuthor(int id) {
        int result = 0;
        try {
            Connection connection = DatabaseConnection.getInstance().getConnection();
            String deleteQuery = "DELETE FROM author WHERE ID = ?";
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
    // Lấy danh sách authors
    public List<Author> getAuthors() {
        List<Author> authors = new ArrayList<>();
        try {
            Connection connection = DatabaseConnection.getInstance().getConnection();
            String selectQuery = "SELECT * FROM author";
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(selectQuery);
            while (resultSet.next()) {
                Author author = new Author();
                author.setId(resultSet.getInt("ID"));
                author.setFirstName(resultSet.getString("Firstname"));
                author.setLastName(resultSet.getString("Lastname"));
                author.setDescription(resultSet.getString("Description"));
                authors.add(author);
            }
            connection.close();
        } catch (SQLException e) {
            // Thông báo lỗi kết nối
            throw new RuntimeException(e);
        }
        return authors;
    }

    // Tìm kiếm theo ID
    public Author getAuthorByID(int id) {
        Author author = null;
        try {
            Connection connection = DatabaseConnection.getInstance().getConnection();
            String selectQuery = "SELECT * FROM author WHERE ID = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(selectQuery);
            preparedStatement.setInt(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                author = new Author();
                author.setId(resultSet.getInt("ID"));
                author.setFirstName(resultSet.getString("Firstname"));
                author.setLastName(resultSet.getString("Lastname"));
                author.setDescription(resultSet.getString("Description"));
            }
            connection.close();
        } catch (SQLException e) {
            // Thông báo lỗi kết nối
            throw new RuntimeException(e);
        }
        return author;
    }
    // Tìm kiếm theo các tiêu chí

    public List<Author> searchAuthors(String search) {
        List<Author> authors = new ArrayList<>();
        try {
            Connection connection = DatabaseConnection.getInstance().getConnection();
            String searchQuery = "SELECT * FROM author " +
                    "WHERE CONCAT(Firstname, ' ', Lastname) LIKE ?" +
                    "OR Description LIKE ?";
            PreparedStatement preparedStatement = connection.prepareStatement(searchQuery);
            String searchPattern = "%" + search + "%";
            preparedStatement.setString(1, searchPattern);
            preparedStatement.setString(2, searchPattern);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                Author author = new Author();
                author.setId(resultSet.getInt("ID"));
                author.setFirstName(resultSet.getString("Firstname"));
                author.setLastName(resultSet.getString("Lastname"));
                author.setDescription(resultSet.getString("Description"));
                authors.add(author);
            }
            connection.close();
        } catch (SQLException e) {
            // Thông báo lỗi kết nối
            throw new RuntimeException(e);
        }
        return authors;
    }

    public List<ComboboxItem> loadComboboxData() {
        List<ComboboxItem> comboboxItems = new ArrayList<>();
        try {
            Connection connection = DatabaseConnection.getInstance().getConnection();
            String selectQuery = "SELECT ID, CONCAT(Firstname, ' ', Lastname) as Fullname FROM author";
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(selectQuery);
            while (resultSet.next()) {
                int id = resultSet.getInt("ID");
                String name = resultSet.getString("Fullname");
                ComboboxItem comboboxItem = new ComboboxItem(id, name);
                comboboxItems.add(comboboxItem);
            }
            connection.close();
        } catch (SQLException e) {
            // Thông báo lỗi kết nối
            throw new RuntimeException(e);
        }
        return comboboxItems;
    }
}
