package GUIs;

import Controllers.AuthorController;
import Controllers.BorrowController;
import Controllers.UserController;
import Models.Author;
import Models.Borrow;
import Models.DocumentFactory.Document;
import Models.User;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.ExecutionException;
import java.util.regex.Pattern;

public class ManageUserForm extends JDialog {

    // Biến dùng để lưu giá trị User id để xử lý
    private int selectedId;

    // biến staffId để lưu phiên đăng nhập
    private int staffId;

    // Components
    private JPanel manageUserPanel;
    private JTextField txtSearch;
    private JTable tblUser;
    private JTextField txtFirstname;
    private JTextField txtLastname;
    private JTextField txtPhonenumber;
    private JTextArea txtAddress;
    private JButton btnAdd;
    private JButton btnUpdate;
    private JButton btnDelete;
    private JButton btnClear;
    private JButton btnBack;
    private JLabel lblFirstname;
    private JLabel lblLastname;
    private JLabel lblPhonenumber;
    private JLabel lblAddress;
    private JPanel userInfoPanel;
    private JPanel userDataPanel;
    private DefaultTableModel tableModel;

    public ManageUserForm(JFrame parent, int staffId) {
        super(parent);
        this.staffId = staffId;
        setTitle("Manage Document");
        setContentPane(manageUserPanel);
        setMinimumSize(new Dimension(700, 500));
        setPreferredSize(new Dimension(700, 500));
        userInfoPanel.setMinimumSize(new Dimension(330, 430));
        userInfoPanel.setMinimumSize(new Dimension(330, 320));
        setModal(true);
        setLocationRelativeTo(parent);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        // Quản lý JTable sử dụng DefaultTableModel object
        tableModel = new DefaultTableModel();
        tblUser.setModel(tableModel);
        // Khởi tạo header và dữ liệu cho JTable
        tableModel.setColumnCount(0);
        loadTableHeader();
        loadTableDataInBackground();
        // Cập nhật trạng thái cho các button khi mới khởi tạo Form
        // Add - Enable
        // Update và Delete - Disable (Do chưa có bản ghi nào được chọn)
        updateButtonBasedOnSelectedItem(selectedId);
        btnAdd.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String firstName = txtFirstname.getText();
                String lastName = txtLastname.getText();
                String phoneNumber = txtPhonenumber.getText();
                String address = txtAddress.getText();
                boolean isValid = validateData(firstName, lastName, phoneNumber, address);
                if (isValid) {
                    User user = new User(firstName, lastName, address, phoneNumber);
                    UserController userController = new UserController();
                    int result = userController.createUser(user);
                    if (result > 0) {
                        JOptionPane.showMessageDialog(ManageUserForm.this,
                                "Thêm người đọc thành công",
                                "Thành công",
                                JOptionPane.INFORMATION_MESSAGE);
                        clearData();
                        loadTableDataInBackground();
                    }
                    else {
                        JOptionPane.showMessageDialog(ManageUserForm.this,
                                "Có lỗi xảy ra\nVui lòng thử lại",
                                "Thất bại",
                                JOptionPane.ERROR_MESSAGE);
                    }
                }
            }
        });
        btnUpdate.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String firstName = txtFirstname.getText();
                String lastName = txtLastname.getText();
                String phoneNumber = txtPhonenumber.getText();
                String address = txtAddress.getText();
                boolean isValid = validateData(firstName, lastName, phoneNumber, address);
                if (isValid && selectedId > 0) {
                    User user = new User(selectedId, firstName, lastName, address, phoneNumber);
                    UserController userController = new UserController();
                    int result = userController.updateUser(user);
                    if (result > 0) {
                        JOptionPane.showMessageDialog(ManageUserForm.this,
                                "Cập nhật người đọc thành công",
                                "Thành công",
                                JOptionPane.INFORMATION_MESSAGE);
                        clearData();
                        loadTableDataInBackground();
                    }
                    else {
                        JOptionPane.showMessageDialog(ManageUserForm.this,
                                "Có lỗi xảy ra\nVui lòng thử lại",
                                "Thất bại",
                                JOptionPane.ERROR_MESSAGE);
                    }
                }
            }
        });
        btnDelete.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int choice = JOptionPane.showConfirmDialog(ManageUserForm.this,
                        "Bạn chắc chắn muốn xoá người đọc này?",
                        "Warning",
                        JOptionPane.YES_NO_OPTION);
                if (choice == JOptionPane.YES_OPTION && selectedId > 0) {
                    // Kiểm tra số lượng phiếu mượn của độc giả (Nếu có thì không cho phép xoá
                    // tránh lỗi có bản ghi tham chiếu trong SQL)
                    BorrowController borrowController = new BorrowController();
                    List<Borrow> borrows = borrowController.getBorrowsByUser(selectedId);
                    if (borrows.size() == 0) {
                        UserController userController = new UserController();
                        int result = userController.deleteUser(selectedId);
                        if (result > 0) {
                            JOptionPane.showMessageDialog(ManageUserForm.this,
                                    "Xoá người đọc thành công",
                                    "Thành công",
                                    JOptionPane.INFORMATION_MESSAGE);
                            clearData();
                            loadTableDataInBackground();
                        }
                        else {
                            JOptionPane.showMessageDialog(ManageUserForm.this,
                                    "Có lỗi xảy ra\nVui lòng thử lại",
                                    "Thất bại",
                                    JOptionPane.ERROR_MESSAGE);
                        }
                    }
                    else {
                        JOptionPane.showMessageDialog(ManageUserForm.this,
                                "Không thể xoá người đọc do có phiếu mượn tồn tại trong hệ thống\n" +
                                        "Vui lòng xoá tất cả phiếu mượn của người đọc trước khi thao tác",
                                "Thất bại",
                                JOptionPane.ERROR_MESSAGE);
                    }
                }
            }
        });
        btnClear.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                clearData();
            }
        });
        btnBack.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
                MenuForm.getInstance(null, staffId).setVisible(true);
            }
        });
        tblUser.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int row = tblUser.rowAtPoint(e.getPoint());
                UserController userController = new UserController();
                int userId = (int) tblUser.getModel().getValueAt(row, 0);
                User user = userController.getUserByID(userId);
                txtFirstname.setText(user.getFirstName());
                txtLastname.setText(user.getLastName());
                txtAddress.setText(user.getAddress());
                txtPhonenumber.setText(user.getPhoneNumber());
                selectedId = userId;
                updateButtonBasedOnSelectedItem(selectedId);
            }
        });
        txtSearch.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                String search = txtSearch.getText();
                UserController userController = new UserController();
                List<User> users = new ArrayList<>();
                if (!search.isEmpty()) {
                    users = userController.searchUsers(search);
                }
                else {
                    users = userController.getUsers();
                }
                loadDataToTable(users);
            }
        });
    }

    private void updateButtonBasedOnSelectedItem(int selectedId)
    {
        if (selectedId > 0) {
            btnAdd.setEnabled(false);
            btnUpdate.setEnabled(true);
            btnDelete.setEnabled(true);
        }
        else {
            btnAdd.setEnabled(true);
            btnUpdate.setEnabled(false);
            btnDelete.setEnabled(false);
        }
    }

    private void flushData()
    {
        selectedId = 0;
        updateButtonBasedOnSelectedItem(selectedId);
    }

    private void loadTableHeader() {
        tableModel.addColumn("ID");
        tableModel.addColumn("Họ");
        tableModel.addColumn("Tên");
        tableModel.addColumn("Địa chỉ");
        tableModel.addColumn("SĐT");
    }

    private void loadTableDataInBackground() {
        UserController userController = new UserController();
        SwingWorker<List<User>, Void> dataLoader = new SwingWorker<List<User>, Void>() {
            @Override
            protected List<User> doInBackground() throws Exception {
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                return userController.getUsers();
            }

            @Override
            protected void done() {
                List<User> users = new ArrayList<>();
                try {
                     users = get();
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                } catch (ExecutionException e) {
                    throw new RuntimeException(e);
                }
                loadDataToTable(users);
            }
        };
        // Chạy swing worker
        dataLoader.execute();
    }

    private void loadDataToTable(List<User> users)
    {
        tableModel.setRowCount(0);
        for (User user: users) {
            tableModel.addRow(new Object[] {
                    user.getId(),
                    user.getFirstName(),
                    user.getLastName(),
                    user.getAddress(),
                    user.getPhoneNumber()
            });
        }
    }

    private void clearData() {
        txtFirstname.setText("");
        txtLastname.setText("");
        txtPhonenumber.setText("");
        txtAddress.setText("");
        txtSearch.setText("");
        flushData();
    }

    private boolean validateData(String firstName, String lastName, String phoneNumber, String address) {
        String phoneNumberPattern = "^0\\d{9}$";
        boolean match = Pattern.matches(phoneNumberPattern, phoneNumber);
        if (firstName != "" && !firstName.isEmpty()
                && lastName != "" && !lastName.isEmpty()
                && phoneNumber != "" && !phoneNumber.isEmpty()
                && address != "" && !address.isEmpty() && match) {
            return true;
        }
        if (firstName != "" && !firstName.isEmpty()
                && lastName != "" && !lastName.isEmpty()
                && phoneNumber != "" && !phoneNumber.isEmpty()
                && address != "" && !address.isEmpty()) {
            JOptionPane.showMessageDialog(ManageUserForm.this,
                    "Số điện thoại người đọc không chính xác",
                    "Cảnh báo",
                    JOptionPane.ERROR_MESSAGE);
            return false;
        }
        if (firstName != "" && !firstName.isEmpty()
                && lastName != "" && !lastName.isEmpty()
                && phoneNumber != "" && !phoneNumber.isEmpty()) {
            JOptionPane.showMessageDialog(ManageUserForm.this,
                    "Địa chỉ người đọc không được để trống",
                    "Cảnh báo",
                    JOptionPane.ERROR_MESSAGE);
            return false;
        }
        if (firstName != "" && !firstName.isEmpty()
                && lastName != "" && !lastName.isEmpty()
                && address != "" && !address.isEmpty()) {
            JOptionPane.showMessageDialog(ManageUserForm.this,
                    "Số điện thoại người đọc không được để trống",
                    "Cảnh báo",
                    JOptionPane.ERROR_MESSAGE);
            return false;
        }
        if (firstName != "" && !firstName.isEmpty()
                && phoneNumber != "" && !phoneNumber.isEmpty()
                && address != "" && !address.isEmpty()) {
            JOptionPane.showMessageDialog(ManageUserForm.this,
                    "Tên người đọc không được để trống",
                    "Cảnh báo",
                    JOptionPane.ERROR_MESSAGE);
            return false;
        }
        if (lastName != "" && !lastName.isEmpty()
                && phoneNumber != "" && !phoneNumber.isEmpty()
                && address != "" && !address.isEmpty()) {
            JOptionPane.showMessageDialog(ManageUserForm.this,
                    "Họ người đọc không được để trống",
                    "Cảnh báo",
                    JOptionPane.ERROR_MESSAGE);
            return false;
        }
        JOptionPane.showMessageDialog(ManageUserForm.this,
                "Thông tin liên lạc người đọc không được để trống",
                "Cảnh báo",
                JOptionPane.ERROR_MESSAGE);
        return false;
    }
}
