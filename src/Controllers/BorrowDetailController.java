package Controllers;

import Common.DatabaseConnection;
import Common.Constants.BorrowStatusConstants;
import Models.BorrowDetail;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class BorrowDetailController {

    // Thêm mới bản ghi mượn sách chi tiết dựa theo id của bản ghi mượn sách và tài liệu
    public void addBorrowDetail(List<BorrowDetail> borrowDetails) {
        try {
            Connection connection = DatabaseConnection.getInstance().getConnection();
            String insertQuery = "INSERT INTO borrowdetail VALUES (?, ?, ?, ?)";
            PreparedStatement preparedStatement = connection.prepareStatement(insertQuery);
            for (BorrowDetail borrowDetail: borrowDetails) {
                preparedStatement.setInt(1, borrowDetail.getId());
                preparedStatement.setInt(2, borrowDetail.getBorrowId());
                preparedStatement.setInt(3, borrowDetail.getDocumentId());
                preparedStatement.setInt(4, BorrowStatusConstants.NOT_RETURNED);
                preparedStatement.addBatch();
            }
            preparedStatement.executeBatch();
            connection.close();
        } catch (SQLException e) {
            // Thông báo lỗi kết nối
            throw new RuntimeException(e);
        }
    }

    public void updateBorrowDetailsStatus(int borrowId, int status) {
        try {
            Connection connection = DatabaseConnection.getInstance().getConnection();
            String updateQuery = "UPDATE borrowdetail SET Status = ? WHERE BorrowID = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(updateQuery);
            preparedStatement.setInt(1, status);
            preparedStatement.setInt(2, borrowId);
            preparedStatement.executeUpdate();
            connection.close();
        } catch (SQLException e) {
            // Thông báo lỗi kết nối
            throw new RuntimeException(e);
        }
    }

    public int updateBorrowDetailStatus(BorrowDetail borrowDetail) {
        int result = 0;
        try {
            Connection connection = DatabaseConnection.getInstance().getConnection();
            String updateQuery = "UPDATE borrowdetail SET Status = ? WHERE ID = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(updateQuery);
            preparedStatement.setInt(1, borrowDetail.getStatus());
            preparedStatement.setInt(2, borrowDetail.getId());
            result = preparedStatement.executeUpdate();
            connection.close();
        } catch (SQLException e) {
            // Thông báo lỗi kết nối
            throw new RuntimeException(e);
        }
        return result;
    }

    public int deleteBorrowDetail(int id) {
        int result = 0;
        try {
            Connection connection = DatabaseConnection.getInstance().getConnection();
            String deleteQuery = "DELETE borrowdetail WHERE ID = ?";
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

    public List<BorrowDetail> getDetailOfBorrowByID(int id) {
        List<BorrowDetail> borrowDetails = new ArrayList<>();
        try {
            Connection connection = DatabaseConnection.getInstance().getConnection();
            String selectQuery = "SELECT * FROM borrowdetail WHERE BorrowID = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(selectQuery);
            preparedStatement.setInt(1, id);
            ResultSet resultSet = preparedStatement.executeQuery(selectQuery);
            while (resultSet.next()) {
                BorrowDetail borrowDetail = new BorrowDetail();
                borrowDetail.setId(resultSet.getInt("ID"));
                borrowDetail.setBorrowId(resultSet.getInt("BorrowID"));
                borrowDetail.setStatus(resultSet.getInt("Status"));
                borrowDetails.add(borrowDetail);
            }
            connection.close();
        } catch (SQLException e) {
            // Thông báo lỗi kết nối
            throw new RuntimeException(e);
        }
        return borrowDetails;
    }

    public List<BorrowDetail> getBorrowDetailByDocument(int documentId) {
        List<BorrowDetail> borrowDetails = new ArrayList<>();
        try {
            Connection connection = DatabaseConnection.getInstance().getConnection();
            String selectQuery = "SELECT * FROM borrowdetail WHERE DocumentID = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(selectQuery);
            preparedStatement.setInt(1, documentId);
            ResultSet resultSet = preparedStatement.executeQuery(selectQuery);
            while (resultSet.next()) {
                BorrowDetail borrowDetail = new BorrowDetail();
                borrowDetail.setId(resultSet.getInt("ID"));
                borrowDetail.setBorrowId(resultSet.getInt("BorrowID"));
                borrowDetail.setStatus(resultSet.getInt("Status"));
                borrowDetails.add(borrowDetail);
            }
            connection.close();
        } catch (SQLException e) {
            // Thông báo lỗi kết nối
            throw new RuntimeException(e);
        }
        return borrowDetails;
    }
}
