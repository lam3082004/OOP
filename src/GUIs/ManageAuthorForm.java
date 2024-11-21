package GUIs;

import Controllers.AuthorController;
import Controllers.DocumentController;
import Models.Author;
import Models.DocumentFactory.Document;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.List;

public class ManageAuthorForm extends JDialog {

    // Biến dùng để lưu giá trị Author id để xử lý
    private int selectedId;

    // biến staffId để lưu phiên đăng nhập
    private int staffId;

    // Components
    private JPanel manageAuthorPanel;
    private JTextField txtFirstname;
    private JTextField txtLastname;
    private JButton btnAdd;
    private JButton btnDelete;
    private JButton btnUpdate;
    private JButton btnClear;
    private JButton btnBack;
    private JTextField txtSearch;
    private JLabel lblFirstname;
    private JLabel lblLastname;
    private JLabel lblDescription;
    private JTextArea txtDescription;
    private JTable tblAuthor;
    private JScrollPane scrollPanel;
    private JPanel authorInfoPanel;
    private DefaultTableModel tableModel;
    private JPanel authorDataPanel;

    public ManageAuthorForm(JFrame parent, int staffId) {
        super(parent);
        this.staffId = staffId;
        setTitle("Manage Author");
        setContentPane(manageAuthorPanel);
        setMinimumSize(new Dimension(700, 450));
        setPreferredSize(new Dimension(700, 450));
        authorInfoPanel.setMinimumSize(new Dimension(330, 430));
        authorDataPanel.setMinimumSize(new Dimension(330, 320));
        setModal(true);
        setLocationRelativeTo(parent);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        tableModel = new DefaultTableModel();
        tblAuthor.setModel(tableModel);
        // Load dữ liệu vào bảng
        tableModel.setColumnCount(0);
        loadTableHeader();
        AuthorController authorController = new AuthorController();
        List<Author> authors = authorController.getAuthors();
        loadDataToTable(authors);
        tblAuthor.setEnabled(false);
        // Cập nhật trạng thái cho các button khi mới khởi tạo Form
        // Add - Enable
        // Update và Delete - Disable (Do chưa có bản ghi nào được chọn)
        updateButtonBasedOnSelectedItem(selectedId);
        btnAdd.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String firstName = txtFirstname.getText();
                String lastName = txtLastname.getText();
                String description = txtDescription.getText();
                boolean isValid = validateData(firstName, lastName);
                if (isValid) {
                    Author author = new Author(firstName, lastName, description);
                    AuthorController authorController = new AuthorController();
                    int result = authorController.createAuthor(author);
                    if (result > 0) {
                        JOptionPane.showMessageDialog(ManageAuthorForm.this,
                                "Tạo mới thành công",
                                "Thành công",
                                JOptionPane.INFORMATION_MESSAGE);
                        clearData();
                        List<Author> authors = authorController.getAuthors();
                        loadDataToTable(authors);
                    }
                    else {
                        JOptionPane.showMessageDialog(ManageAuthorForm.this,
                                "Có lỗi xảy ra\nVui lòng thử lại",
                                "Thất bại",
                                JOptionPane.ERROR_MESSAGE);
                    }
                }
                else {
                    JOptionPane.showMessageDialog(ManageAuthorForm.this,
                            "Dữ liệu nhập vào không chính xác\nVui lòng thử lại",
                            "Thất bại",
                            JOptionPane.ERROR_MESSAGE);
                }
            }
        });
        btnUpdate.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (selectedId > 0) {
                    String firstName = txtFirstname.getText();
                    String lastName = txtLastname.getText();
                    String description = txtDescription.getText();
                    boolean isValid = validateData(firstName, lastName);
                    if (isValid) {
                        Author author = new Author(selectedId, firstName, lastName, description);
                        AuthorController authorController = new AuthorController();
                        int result = authorController.updateAuthor(author);
                        if (result > 0) {
                            JOptionPane.showMessageDialog(ManageAuthorForm.this,
                                    "Cập nhật thành công",
                                    "Thành công",
                                    JOptionPane.INFORMATION_MESSAGE);
                            clearData();
                            List<Author> authors = authorController.getAuthors();
                            loadDataToTable(authors);
                        }
                        else {
                            JOptionPane.showMessageDialog(ManageAuthorForm.this,
                                    "Có lỗi xảy ra\nVui lòng thử lại",
                                    "Thất bại",
                                    JOptionPane.ERROR_MESSAGE);
                        }
                    }
                    else {
                        JOptionPane.showMessageDialog(ManageAuthorForm.this,
                                "Dữ liệu nhập vào không chính xác\nVui lòng thử lại",
                                "Thất bại",
                                JOptionPane.ERROR_MESSAGE);
                    }
                }
                else {
                    JOptionPane.showMessageDialog(ManageAuthorForm.this,
                            "Không có bản ghi nào được chon\nVui lòng thử lại",
                            "Thất bại",
                            JOptionPane.ERROR_MESSAGE);
                }
            }
        });
        btnDelete.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int choice = JOptionPane.showConfirmDialog(ManageAuthorForm.this,
                        "Bạn chắc chắn muốn xoá tác giả này?",
                        "Warning",
                        JOptionPane.YES_NO_OPTION);
                if (selectedId > 0) {
                    if (choice == JOptionPane.YES_OPTION) {
                        // Kiểm tra số lượng tác phẩm của tác giả (Nếu có thì không cho phép xoá
                        // tránh lỗi có bản ghi tham chiếu trong SQL)
                        DocumentController documentController = new DocumentController();
                        List<Document> documents = documentController.getDocumentByAuthor(selectedId);
                        if (documents.isEmpty()) {
                            AuthorController authorController = new AuthorController();
                            int result = authorController.deleteAuthor(selectedId);
                            if (result > 0) {
                                JOptionPane.showMessageDialog(ManageAuthorForm.this,
                                        "Xoá tác giả thành công",
                                        "Thành công",
                                        JOptionPane.INFORMATION_MESSAGE);
                                clearData();
                                List<Author> authors = authorController.getAuthors();
                                loadDataToTable(authors);
                            }
                            else {
                                JOptionPane.showMessageDialog(ManageAuthorForm.this,
                                        "Có lỗi xảy ra\nVui lòng thử lại",
                                        "Thất bại",
                                        JOptionPane.ERROR_MESSAGE);
                            }
                        }
                        else {
                            JOptionPane.showMessageDialog(ManageAuthorForm.this,
                                    "Không thể xoá tác giả do có tác phẩm tồn tại trong hệ thống\n" +
                                            "Vui lòng xoá tất cả tác phẩm của tác giả trước khi thao tác",
                                    "Thất bại",
                                    JOptionPane.ERROR_MESSAGE);
                        }
                    }
                }
                else {
                    JOptionPane.showMessageDialog(ManageAuthorForm.this,
                            "Không có bản ghi nào được chon\nVui lòng thử lại",
                            "Thất bại",
                            JOptionPane.ERROR_MESSAGE);
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
        txtSearch.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                String search = txtSearch.getText();
                AuthorController authorController = new AuthorController();
                List<Author> authors = new ArrayList<>();
                if (!search.isEmpty()) {
                    authors = authorController.searchAuthors(search);
                }
                else {
                    authors = authorController.getAuthors();
                }
                loadDataToTable(authors);
            }
        });
        tblAuthor.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int row = tblAuthor.rowAtPoint(e.getPoint());
                AuthorController authorController = new AuthorController();
                int authorId = (int) tblAuthor.getModel().getValueAt(row, 0);
                Author author = authorController.getAuthorByID(authorId);
                txtFirstname.setText(author.getFirstName());
                txtLastname.setText(author.getLastName());
                txtDescription.setText(author.getDescription());
                selectedId = authorId;
                updateButtonBasedOnSelectedItem(selectedId);
            }
        });
        setVisible(false);
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
    }

    private void loadDataToTable(List<Author> authors)
    {
        tableModel.setRowCount(0);
        for (Author author: authors) {
            tableModel.addRow(new Object[] {
                    author.getId(),
                    author.getFirstName(),
                    author.getLastName()
            });
        }
    }

    private void clearData() {
        txtFirstname.setText("");
        txtLastname.setText("");
        txtDescription.setText("");
        txtSearch.setText("");
        flushData();
    }

    private boolean validateData(String firstName, String lastName)
    {
        if (firstName == null || firstName.isEmpty()) {
            return false;
        }
        return lastName != null && !lastName.isEmpty();
    }
}
