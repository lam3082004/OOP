package Controllers;

import Common.Constants.BorrowStatusConstants;
import Common.DatabaseConnection;
import Models.Borrow;
import Models.BorrowDetail;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class BorrowController {
    public int createBorrow(Borrow borrow)
    {
        int borrowId = 0;
        try {
            Connection connection = DatabaseConnection.getInstance().getConnection();
            String insertQuery = "INSERT INTO Borrow VALUES (?, ?, ?, ?, ?)";
            PreparedStatement preparedStatement = connection.prepareStatement(insertQuery, Statement.RETURN_GENERATED_KEYS);
            preparedStatement.setInt(1, borrow.getId());
            preparedStatement.setInt(2, borrow.getUserId());
            preparedStatement.setInt(3, borrow.getStaffId());
            preparedStatement.setString(4, borrow.getBorrowDate());
            preparedStatement.setString(5, borrow.getReturnDate());
            int result = preparedStatement.executeUpdate();
            if (result > 0) {
                ResultSet resultSet = preparedStatement.getGeneratedKeys();
                while (resultSet.next()) {
                    borrowId = resultSet.getInt(1);
                }
            }
            connection.close();
        } catch (SQLException e) {
            // Thông báo lỗi kết nối
            throw new RuntimeException(e);
        }
        return borrowId;
    }

    public List<Borrow> getBorrows() {
        List<Borrow> borrows = new ArrayList<>();
        try {
            Connection connection = DatabaseConnection.getInstance().getConnection();
            String selectQuery = "SELECT b.*, " +
                    "CASE " +
                    "WHEN EXISTS " +
                    "(SELECT 1 FROM borrowdetail bd WHERE b.ID = bd.BorrowID AND bd.Status = 0) " +
                    "THEN 0 " +
                    "WHEN NOT EXISTS " +
                    "(SELECT 1 FROM borrowdetail bd WHERE b.ID = bd.BorrowID AND bd.Status <> 1) " +
                    "THEN 1 " +
                    "ELSE NULL " +
                    "END AS Overall_status " +
                    "FROM borrow b";
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(selectQuery);
            while (resultSet.next()) {
                Borrow borrow = new Borrow();
                int borrowId = resultSet.getInt("ID");
                borrow.setId(borrowId);
                borrow.setUserId(resultSet.getInt("UserID"));
                borrow.setStaffId(resultSet.getInt("StaffID"));
                borrow.setBorrowDate(resultSet.getString("BorrowDate"));
                borrow.setReturnDate(resultSet.getString("ReturnDate"));
                borrow.setStatus(resultSet.getInt("Overall_status"));
                borrows.add(borrow);
            }
            connection.close();
        } catch (SQLException e) {
            // Thông báo lỗi kết nối
            throw new RuntimeException(e);
        }
        return borrows;
    }

    public Borrow getBorrowByID(int id) {
        Borrow borrow = null;
        try {
            Connection connection = DatabaseConnection.getInstance().getConnection();
            String selectQuery = "SELECT b.*, " +
                    "CASE " +
                    "WHEN EXISTS " +
                    "(SELECT 1 FROM borrowdetail bd WHERE b.ID = bd.BorrowID AND bd.Status = 0) " +
                    "THEN 0 " +
                    "WHEN NOT EXISTS " +
                    "(SELECT 1 FROM borrowdetail bd WHERE b.ID = bd.BorrowID AND bd.Status <> 1) " +
                    "THEN 1 " +
                    "ELSE NULL " +
                    "END AS Overall_status " +
                    "FROM borrow b " +
                    "WHERE b.ID = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(selectQuery);
            preparedStatement.setInt(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                borrow = new Borrow();
                int borrowId = resultSet.getInt("ID");
                borrow.setId(borrowId);
                borrow.setUserId(resultSet.getInt("UserID"));
                borrow.setStaffId(resultSet.getInt("StaffID"));
                borrow.setBorrowDate(resultSet.getString("BorrowDate"));
                borrow.setReturnDate(resultSet.getString("ReturnDate"));
                borrow.setStatus(resultSet.getInt("Overall_status"));

            }
            connection.close();
        } catch (SQLException e) {
            // Thông báo lỗi kết nối
            throw new RuntimeException(e);
        }
        return borrow;
    }

    public List<Borrow> getBorrowsByUser(int userId) {
        List<Borrow> borrows = new ArrayList<>();
        try {
            Connection connection = DatabaseConnection.getInstance().getConnection();
            String selectQuery = "SELECT b.*, " +
                    "CASE " +
                    "WHEN EXISTS " +
                    "(SELECT 1 FROM borrowdetail bd WHERE b.ID = bd.BorrowID AND bd.Status = 0) " +
                    "THEN 0 " +
                    "WHEN NOT EXISTS " +
                    "(SELECT 1 FROM borrowdetail bd WHERE b.ID = bd.BorrowID AND bd.Status <> 1) " +
                    "THEN 1 " +
                    "ELSE NULL " +
                    "END AS Overall_status " +
                    "FROM borrow b " +
                    "WHERE b.UserID = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(selectQuery);
            preparedStatement.setInt(1, userId);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                Borrow borrow = new Borrow();
                int borrowId = resultSet.getInt("ID");
                borrow.setId(borrowId);
                borrow.setUserId(resultSet.getInt("UserID"));
                borrow.setStaffId(resultSet.getInt("StaffID"));
                borrow.setBorrowDate(resultSet.getString("BorrowDate"));
                borrow.setReturnDate(resultSet.getString("ReturnDate"));
                borrow.setStatus(resultSet.getInt("Overall_status"));
                borrows.add(borrow);
            }
            connection.close();
        } catch (SQLException e) {
            // Thông báo lỗi kết nối
            throw new RuntimeException(e);
        }
        return borrows;
    }

    public List<Borrow> filterBorrows(int staffId, int userId, int status) {
        List<Borrow> borrows = new ArrayList<>();
        try {
            Connection connection = DatabaseConnection.getInstance().getConnection();
            String selectQuery = "SELECT b.*, " +
                    "CASE " +
                    "WHEN EXISTS " +
                    "(SELECT 1 FROM borrowdetail bd WHERE b.ID = bd.BorrowID AND bd.Status = 0) " +
                    "THEN 0 " +
                    "WHEN NOT EXISTS " +
                    "(SELECT 1 FROM borrowdetail bd WHERE b.ID = bd.BorrowID AND bd.Status <> 1) " +
                    "THEN 1 " +
                    "ELSE NULL " +
                    "END AS Overall_status " +
                    "FROM borrow b " +
                    "WHERE 1 = 1 ";
            if (staffId > 0) {
                selectQuery += "AND b.StaffID = " + staffId + " ";
            }
            if (userId > 0) {
                selectQuery += "AND b.UserID = " + userId + " ";
            }
            if (status > -1) {
                selectQuery += "HAVING overall_status = " + status;
            }
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(selectQuery);
            while (resultSet.next()) {
                Borrow borrow = new Borrow();
                int borrowId = resultSet.getInt("ID");
                borrow.setId(borrowId);
                borrow.setUserId(resultSet.getInt("UserID"));
                borrow.setStaffId(resultSet.getInt("StaffID"));
                borrow.setBorrowDate(resultSet.getString("BorrowDate"));
                borrow.setReturnDate(resultSet.getString("ReturnDate"));
                borrow.setStatus(resultSet.getInt("Overall_status"));
                borrows.add(borrow);
            }
            connection.close();
        } catch (SQLException e) {
            // Thông báo lỗi kết nối
            throw new RuntimeException(e);
        }
        return borrows;
    }

    public List<Borrow> getBorrowsByStaff(int staffId) {
        List<Borrow> borrows = new ArrayList<>();
        try {
            Connection connection = DatabaseConnection.getInstance().getConnection();
            String selectQuery = "SELECT b.*, " +
                    "CASE " +
                    "WHEN EXISTS " +
                    "(SELECT 1 FROM borrowdetail bd WHERE b.ID = bd.BorrowID AND bd.Status = 0) " +
                    "THEN 0 " +
                    "WHEN NOT EXISTS " +
                    "(SELECT 1 FROM borrowdetail bd WHERE b.ID = bd.BorrowID AND bd.Status <> 1) " +
                    "THEN 1 " +
                    "ELSE NULL " +
                    "END AS Overall_status " +
                    "FROM borrow b " +
                    "WHERE b.StaffID = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(selectQuery);
            preparedStatement.setInt(1, staffId);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                Borrow borrow = new Borrow();
                int borrowId = resultSet.getInt("ID");
                borrow.setId(borrowId);
                borrow.setUserId(resultSet.getInt("UserID"));
                borrow.setStaffId(resultSet.getInt("StaffID"));
                borrow.setBorrowDate(resultSet.getString("BorrowDate"));
                borrow.setReturnDate(resultSet.getString("ReturnDate"));
                borrow.setStatus(resultSet.getInt("Overall_status"));
                borrows.add(borrow);
            }
            connection.close();
        } catch (SQLException e) {
            // Thông báo lỗi kết nối
            throw new RuntimeException(e);
        }
        return borrows;
    }

    public List<Borrow> getBorrowsByStatus(int status) {
        List<Borrow> borrows = new ArrayList<>();
        try {
            Connection connection = DatabaseConnection.getInstance().getConnection();
            String selectQuery = "SELECT b.*, " +
                    "CASE " +
                    "WHEN EXISTS " +
                    "(SELECT 1 FROM borrowdetail bd WHERE b.ID = bd.BorrowID AND bd.Status = 0) " +
                    "THEN 0 " +
                    "WHEN NOT EXISTS " +
                    "(SELECT 1 FROM borrowdetail bd WHERE b.ID = bd.BorrowID AND bd.Status <> 1) " +
                    "THEN 1 " +
                    "ELSE NULL " +
                    "END AS Overall_status " +
                    "FROM borrow b " +
                    "WHERE Overall_status = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(selectQuery);
            preparedStatement.setInt(1, status);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                Borrow borrow = new Borrow();
                int borrowId = resultSet.getInt("ID");
                borrow.setId(borrowId);
                borrow.setUserId(resultSet.getInt("UserID"));
                borrow.setStaffId(resultSet.getInt("StaffID"));
                borrow.setBorrowDate(resultSet.getString("BorrowDate"));
                borrow.setReturnDate(resultSet.getString("ReturnDate"));
                borrow.setStatus(resultSet.getInt("Overall_status"));
                borrows.add(borrow);
            }
            connection.close();
        } catch (SQLException e) {
            // Thông báo lỗi kết nối
            throw new RuntimeException(e);
        }
        return borrows;
    }

    public String getBorrowStatus(List<BorrowDetail> borrowDetails) {
        for (BorrowDetail borrowDetail: borrowDetails) {
            if (borrowDetail.getStatus() == BorrowStatusConstants.NOT_RETURNED) {
                return "Chưa hoàn thành";
            }
        }
        return "Đã hoàn thành";
    }
}
