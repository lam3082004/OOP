package Models;

import Common.Constants.BorrowStatusConstants;

import java.util.List;

public class Borrow {
    private int id;
    private int userId;
    private int staffId;
    private String borrowDate;
    private String returnDate;
    private int status;

    public Borrow() {
    }

    public Borrow(int id,
                  int userId,
                  int staffId,
                  String borrowDate,
                  String returnDate) {
        this.id = id;
        this.userId = userId;
        this.staffId = staffId;
        this.borrowDate = borrowDate;
        this.returnDate = returnDate;
    }

    public Borrow(int userId,
                  int staffId,
                  String borrowDate,
                  String returnDate) {
        this.userId = userId;
        this.staffId = staffId;
        this.borrowDate = borrowDate;
        this.returnDate = returnDate;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getStaffId() {
        return staffId;
    }

    public void setStaffId(int staffId) {
        this.staffId = staffId;
    }

    public String getBorrowDate() {
        return borrowDate;
    }

    public void setBorrowDate(String borrowDate) {
        this.borrowDate = borrowDate;
    }

    public String getReturnDate() {
        return returnDate;
    }

    public void setReturnDate(String returnDate) {
        this.returnDate = returnDate;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public static String getReadableStatus(int status) {
        if (status == BorrowStatusConstants.NOT_RETURNED) {
            return "Chưa hoàn thành";
        }
        return "Đã hoàn thành";
    }
}
