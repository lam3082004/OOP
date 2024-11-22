package Common;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {
    private static final String DATABASE_URL = "jdbc:mysql://localhost:3306/librarymanagement";
    private static final String USER = "root";
    private static final String PASSWORD = "";
    // Khởi tạo 1 instance DatabaseConnection
    private static DatabaseConnection instance;
    private Connection connection;

    // Hàm khởi tạo private để tránh việc khởi tạo nhiều instance
    // (Chỉ 1 instance tồn tại cho tới khi giải phóng)
    private DatabaseConnection() throws SQLException {
        try {
            this.connection = DriverManager.getConnection(DATABASE_URL, USER, PASSWORD);
        } catch (SQLException e) {
            System.err.println("Failed to connect to the database.");
            e.printStackTrace();
            throw e;
        }
    }

    public Connection getConnection() {
        return connection;
    }

    // Phương thức public được cung cấp để truy cập instance duy nhất của lớp
    public static DatabaseConnection getInstance() throws SQLException {
        if (instance == null || instance.getConnection().isClosed()) {
            instance = new DatabaseConnection();
        }
        return instance;
    }
}
