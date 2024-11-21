package Controllers;

import Common.DatabaseConnection;
import Common.Model.ComboboxItem;
import Models.User;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UserController {
    // Thêm mới author vào database
    public int createUser(User user) {
        int result = 0;
        try {
            Connection connection = DatabaseConnection.getInstance().getConnection();
            String insertQuery = "INSERT INTO users VALUES (?, ?, ?, ?, ?)";
            PreparedStatement preparedStatement = connection.prepareStatement(insertQuery);
            preparedStatement.setInt(1, user.getId());
            preparedStatement.setString(2, user.getFirstName());
            preparedStatement.setString(3, user.getLastName());
            preparedStatement.setString(4, user.getAddress());
            preparedStatement.setString(5, user.getPhoneNumber());
            result = preparedStatement.executeUpdate();
            connection.close();
        } catch (SQLException e) {
            // Thông báo lỗi kết nối
            throw new RuntimeException(e);
        }
        return result;
    }
    // Cập nhật bản ghi author trong database
    public int updateUser(User user) {
        int result = 0;
        try {
            Connection connection = DatabaseConnection.getInstance().getConnection();
            String updateQuery = "UPDATE users SET Firstname = ?, " +
                    "Lastname = ?, " +
                    "Address = ?," +
                    "Phonenumber = ? " +
                    "WHERE ID = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(updateQuery);
            preparedStatement.setString(1, user.getFirstName());
            preparedStatement.setString(2, user.getLastName());
            preparedStatement.setString(3, user.getAddress());
            preparedStatement.setString(4, user.getPhoneNumber());
            preparedStatement.setInt(5, user.getId());
            result = preparedStatement.executeUpdate();
            connection.close();
        } catch (SQLException e) {
            // Thông báo lỗi kết nối
            throw new RuntimeException(e);
        }
        return result;
    }
    // Xoá bản ghi author trong database
    public int deleteUser(int userId) {
        int result = 0;
        try {
            Connection connection = DatabaseConnection.getInstance().getConnection();
            String deleteQuery = "DELETE FROM users WHERE ID = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(deleteQuery);
            preparedStatement.setInt(1, userId);
            result = preparedStatement.executeUpdate();
            connection.close();
        } catch (SQLException e) {
            // Thông báo lỗi kết nối
            throw new RuntimeException(e);
        }
        return result;
    }
    // Lấy danh sách authors
    public List<User> getUsers() {
        List<User> users = new ArrayList<>();
        try {
            Connection connection = DatabaseConnection.getInstance().getConnection();
            String selectQuery = "SELECT * FROM users";
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(selectQuery);
            while (resultSet.next()) {
                User user = new User();
                user.setId(resultSet.getInt("ID"));
                user.setFirstName(resultSet.getString("Firstname"));
                user.setLastName(resultSet.getString("Lastname"));
                user.setAddress(resultSet.getString("Address"));
                user.setPhoneNumber(resultSet.getString("PhoneNumber"));
                users.add(user);
            }
            connection.close();
        } catch (SQLException e) {
            // Thông báo lỗi kết nối
            throw new RuntimeException(e);
        }
        return users;
    }

    // Tìm kiếm theo ID
    public User getUserByID(int id) {
        User user = null;
        try {
            Connection connection = DatabaseConnection.getInstance().getConnection();
            String selectQuery = "SELECT * FROM users WHERE ID = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(selectQuery);
            preparedStatement.setInt(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                user = new User();
                user.setId(resultSet.getInt("ID"));
                user.setFirstName(resultSet.getString("Firstname"));
                user.setLastName(resultSet.getString("Lastname"));
                user.setAddress(resultSet.getString("Address"));
                user.setPhoneNumber(resultSet.getString("PhoneNumber"));
            }
            connection.close();
        } catch (SQLException e) {
            // Thông báo lỗi kết nối
            throw new RuntimeException(e);
        }
        return user;
    }
    // Tìm kiếm theo các tiêu chí

    public List<User> searchUsers(String search) {
        List<User> users = new ArrayList<>();
        try {
            Connection connection = DatabaseConnection.getInstance().getConnection();
            String searchQuery = "SELECT * FROM users " +
                    "WHERE CONCAT(Firstname, ' ', Lastname) LIKE ?" +
                    "OR Address LIKE ?" +
                    "OR Phonenumber LIKE ?";
            String searchPattern = "%" + search + "%";
            PreparedStatement preparedStatement = connection.prepareStatement(searchQuery);
            preparedStatement.setString(1, searchPattern);
            preparedStatement.setString(2, searchPattern);
            preparedStatement.setString(3, searchPattern);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                User user = new User();
                user.setId(resultSet.getInt("ID"));
                user.setFirstName(resultSet.getString("Firstname"));
                user.setLastName(resultSet.getString("Lastname"));
                user.setAddress(resultSet.getString("Address"));
                user.setPhoneNumber(resultSet.getString("PhoneNumber"));
                users.add(user);
            }
            connection.close();
        } catch (SQLException e) {
            // Thông báo lỗi kết nối
            throw new RuntimeException(e);
        }
        return users;
    }

    public List<ComboboxItem> loadComboboxData() {
        List<ComboboxItem> comboboxItems = new ArrayList<>();
        try {
            Connection connection = DatabaseConnection.getInstance().getConnection();
            String selectQuery = "SELECT ID, CONCAT(Firstname, ' ', Lastname) as Fullname FROM users";
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
