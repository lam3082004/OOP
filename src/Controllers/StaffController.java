package Controllers;

import Common.DatabaseConnection;
import Common.Model.ComboboxItem;
import Models.Staff;
import Models.User;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class StaffController {
    public Staff getStaffByID(int id) {
        Staff staff = null;
        try {
            Connection connection = DatabaseConnection.getInstance().getConnection();
            String selectQuery = "SELECT * FROM staff WHERE ID = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(selectQuery);
            preparedStatement.setInt(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                staff = new Staff();
                staff.setId(resultSet.getInt("ID"));
                staff.setFirstName(resultSet.getString("FirstName"));
                staff.setLastName(resultSet.getString("LastName"));
                staff.setAddress(resultSet.getString("Address"));
                staff.setPhoneNumber(resultSet.getString("PhoneNumber"));
                staff.setUsername(resultSet.getString("Username"));
                staff.setPassword(resultSet.getString("Password"));
            }
            connection.close();
        } catch (SQLException e) {
            // Thông báo lỗi kết nối
            throw new RuntimeException(e);
        }
        return staff;
    }

    public Staff getAuthenticateUser(String username, String password) {
        Staff staff = null;
        try {
            Connection connection = DatabaseConnection.getInstance().getConnection();
            String loginQuery = "SELECT * FROM staff WHERE Username = ? AND Password = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(loginQuery);
            preparedStatement.setString(1, username);
            preparedStatement.setString(2, password);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                staff = new Staff();
                staff.setId(resultSet.getInt("ID"));
                staff.setFirstName(resultSet.getString("FirstName"));
                staff.setLastName(resultSet.getString("LastName"));
                staff.setAddress(resultSet.getString("Address"));
                staff.setPhoneNumber(resultSet.getString("PhoneNumber"));
                staff.setUsername(resultSet.getString("Username"));
                staff.setPassword(resultSet.getString("Password"));
            }
            connection.close();
        } catch (SQLException e) {
            // Thông báo lỗi kết nối
            throw new RuntimeException(e);
        }
        return staff;
    }

    public List<ComboboxItem> loadComboboxData() {
        List<ComboboxItem> comboboxItems = new ArrayList<>();
        try {
            Connection connection = DatabaseConnection.getInstance().getConnection();
            String selectQuery = "SELECT ID, CONCAT(Firstname, ' ', Lastname) as Fullname FROM staff";
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
