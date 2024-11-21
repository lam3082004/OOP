package Models;

import Common.Constants.BorrowStatusConstants;
import Models.DocumentFactory.Document;

public class BorrowDetail {
    private int id;
    private int documentId;
    private int borrowId;
    private int status;

    public BorrowDetail() {
    }

    public BorrowDetail(int id, int documentId, int borrowId, int status) {
        this.id = id;
        this.documentId = documentId;
        this.borrowId = borrowId;
        this.status = status;
    }

    public BorrowDetail(int documentId, int borrowId, int status) {
        this.documentId = documentId;
        this.borrowId = borrowId;
        this.status = status;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getDocumentId() {
        return documentId;
    }

    public void setDocumentId(int documentId) {
        this.documentId = documentId;
    }

    public int getBorrowId() {
        return borrowId;
    }

    public void setBorrowId(int borrowId) {
        this.borrowId = borrowId;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getReadableStatus()
    {
        if (this.status == BorrowStatusConstants.RETURNED) {
            return "Đã trả";
        }
        return "Chưa trả";
    }
}
