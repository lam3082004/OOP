package GUIs;

import Common.Constants.BorrowStatusConstants;
import Common.Constants.DocumentType;
import Common.Helpers.DateTimeHelper;
import Common.Model.ComboboxItem;
import Controllers.*;
import Models.Borrow;
import Models.BorrowDetail;
import Models.DocumentFactory.Document;
import org.jdatepicker.impl.DateComponentFormatter;
import org.jdatepicker.impl.JDatePickerImpl;
import org.jdatepicker.impl.JDatePanelImpl;
import org.jdatepicker.impl.SqlDateModel;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.List;

public class ManageBorrowDetailForm extends JDialog {
    // Biến dùng để lưu giá trị Borrow id để xử lý
    int selectedId = 0;

    // biến staffId để lưu phiên đăng nhập
    private int staffId;

    // biến để lưu hàng của các JTable
    int remainTableIndex = -1;
    int borrowTableIndex = -1;

    //components
    private JPanel manageBorrowDetailPanel;
    private JTextField txtStaffName;
    private JComboBox cbUser;
    private JButton btnReturnDat;
    private JButton btnAddDocument;
    private JButton btnRemoveDocument;
    private JLabel selectedReturnDateField;
    private JButton btnBorrowDat;
    private JLabel selectedBorrowDateField;
    private JTable tblBorrowBook;
    private JTable tblRemainBook;
    private JButton btnBack;
    private JButton btnBorrow;
    private JButton btnClear;
    private JButton btnReturn;
    private JTextField txtBookId;
    private JTextField txtBookName;
    private JTextField txtAuthorName;
    private JTextField txtStatus;
    private JLabel lblBookId;
    private JLabel lblBookName;
    private JLabel lblBookAuthor;
    private JLabel txtBookType;
    private JLabel lblOverallStatus;
    private DefaultTableModel desireDocumentTableModel;
    private DefaultTableModel borrowDocumentTableModel;

    // Components

    public ManageBorrowDetailForm(JFrame parent, int borrowId, int staffId) {
        super(parent);
        this.staffId = staffId;
        this.selectedId = borrowId;
        if (borrowId > 0) {
            setTitle("Phiếu mượn chi tiết");
        }
        else {
            setTitle("Tạo mới phiếu mượn");
        }
        setContentPane(manageBorrowDetailPanel);
        setMinimumSize(new Dimension(700, 590));
        setPreferredSize(new Dimension(700, 590));
        setModal(true);
        setLocationRelativeTo(parent);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        // đặt giá trị chiều rộng-cao cho các component
        txtStaffName.setMaximumSize(new Dimension(140, 30));
        txtStaffName.setPreferredSize(new Dimension(140, 30));
        cbUser.setMaximumSize(new Dimension(140, 30));
        cbUser.setPreferredSize(new Dimension(140, 30));
        selectedReturnDateField.setMaximumSize(new Dimension(100, 30));
        selectedReturnDateField.setPreferredSize(new Dimension(100, 30));
        selectedBorrowDateField.setMaximumSize(new Dimension(100, 30));
        selectedBorrowDateField.setPreferredSize(new Dimension(100, 30));

        // khởi tạo các JTable
        desireDocumentTableModel = new DefaultTableModel();
        tblRemainBook.setModel(desireDocumentTableModel);

        borrowDocumentTableModel = new DefaultTableModel();
        tblBorrowBook.setModel(borrowDocumentTableModel);
        desireDocumentTableModel.setRowCount(0);
        borrowDocumentTableModel.setRowCount(0);
        loadTableHeaders();

        // load combobox
        loadUserCombobox();

        // cài đặt trạng thái cho các nút
        txtStaffName.setEnabled(false);
        txtStaffName.setEditable(false);
        StaffController staffController = new StaffController();
        txtStaffName.setText(staffController.getStaffByID(staffId).getFullname());
        initializeComponentStatus();

        // Load dữ liệu vào các bảng (bảng đăng kí mượn - tương tự giỏ hàng) sẽ không có dữ liệu
        // ban đầu
        loadDesireDocuments();

        // Load dữ liệu vào các field
        initializeData();

        btnReturnDat.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                SqlDateModel model = new SqlDateModel();
                Properties properties = new Properties();
                properties.put("text.today", "Today");
                properties.put("text.month", "Month");
                properties.put("text.year", "Year");

                JDatePanelImpl datePanel = new JDatePanelImpl(model, properties);
                JDatePickerImpl datePicker = new JDatePickerImpl(datePanel, new DateComponentFormatter());

                // Show the date picker in a dialog
                int result = JOptionPane.showConfirmDialog(
                        ManageBorrowDetailForm.this,
                        datePicker,
                        "Chọn ngày trả (Nếu không chọn thì mặc định sẽ là 7 ngày tới)",
                        JOptionPane.OK_CANCEL_OPTION,
                        JOptionPane.PLAIN_MESSAGE
                );

                // If user clicks OK, set the selected date in the text field
                if (result == JOptionPane.OK_OPTION) {
                    java.sql.Date selectedDate = (java.sql.Date) datePicker.getModel().getValue();
                    if (selectedDate != null) {
                        selectedReturnDateField.setText(selectedDate.toString());
                    }
                }
            }
        });
        btnBorrowDat.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                SqlDateModel model = new SqlDateModel();
                Properties properties = new Properties();
                properties.put("text.today", "Today");
                properties.put("text.month", "Month");
                properties.put("text.year", "Year");

                JDatePanelImpl datePanel = new JDatePanelImpl(model, properties);
                JDatePickerImpl datePicker = new JDatePickerImpl(datePanel, new DateComponentFormatter());

                // Show the date picker in a dialog
                int result = JOptionPane.showConfirmDialog(
                        ManageBorrowDetailForm.this,
                        datePicker,
                        "Chọn ngày mượn (Nếu không chọn thì mặc định sẽ là ngày hiện tại)",
                        JOptionPane.OK_CANCEL_OPTION,
                        JOptionPane.PLAIN_MESSAGE
                );

                // If user clicks OK, set the selected date in the text field
                if (result == JOptionPane.OK_OPTION) {
                    java.sql.Date selectedDate = (java.sql.Date) datePicker.getModel().getValue();
                    if (selectedDate != null) {
                        selectedBorrowDateField.setText(selectedDate.toString());
                    }
                }
            }
        });
        btnBack.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                backToManageBorrow();
            }
        });
        btnBorrow.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int rowCount = borrowDocumentTableModel.getRowCount();
                if (rowCount > 0) {
                    String borrowDate = selectedBorrowDateField.getText();
                    String returnDate = selectedReturnDateField.getText();
                    ComboboxItem item = (ComboboxItem) cbUser.getSelectedItem();
                    int userId = item.getId();
                    try {
                        boolean isValid = validateData(userId, borrowDate, returnDate);
                        if (isValid) {
                            Borrow borrow = new Borrow(userId, staffId, borrowDate, returnDate);
                            BorrowController borrowController = new BorrowController();
                            int borrowId = borrowController.createBorrow(borrow);
                            if (borrowId > 0) {
                                List<BorrowDetail> borrowDetails = new ArrayList<>();
                                for (int row = 0; row < rowCount; row++) {
                                    int documentId = (int) borrowDocumentTableModel.getValueAt(row, 0);
                                    BorrowDetail borrowDetail = new BorrowDetail(documentId, borrowId, BorrowStatusConstants.NOT_RETURNED);
                                    borrowDetails.add(borrowDetail);
                                }
                                BorrowDetailController borrowDetailController = new BorrowDetailController();
                                borrowDetailController.addBorrowDetail(borrowDetails);
                                JOptionPane.showMessageDialog(ManageBorrowDetailForm.this,
                                        "Tạo phiếu mượn thành công",
                                        "Thành công",
                                        JOptionPane.INFORMATION_MESSAGE);
                                backToManageBorrow();
                            }
                            else {
                                JOptionPane.showMessageDialog(ManageBorrowDetailForm.this,
                                        "Có lỗi xảy ra\nVui lòng thử lại",
                                        "Thất bại",
                                        JOptionPane.ERROR_MESSAGE);
                            }
                        }
                        else {
                            JOptionPane.showMessageDialog(ManageBorrowDetailForm.this,
                                    "Dữ liệu nhập vào không chính xác\nVui lòng thử lại",
                                    "Thất bại",
                                    JOptionPane.ERROR_MESSAGE);
                        }
                    } catch (ParseException ex) {
                        throw new RuntimeException(ex);
                    }
                }
                else {
                    JOptionPane.showMessageDialog(ManageBorrowDetailForm.this,
                            "Chưa chọn tài liệu",
                            "Cảnh báo",
                            JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        tblRemainBook.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (selectedId == 0) {
                    int row = tblRemainBook.rowAtPoint(e.getPoint());
                    remainTableIndex = row;
                    borrowTableIndex = -1;
                    if (row >= 0) {
                        txtBookId.setText(tblRemainBook.getModel().getValueAt(row, 0).toString());
                        txtBookName.setText(tblRemainBook.getModel().getValueAt(row, 1).toString());
                        txtAuthorName.setText(tblRemainBook.getModel().getValueAt(row, 2).toString());
                        txtStatus.setText(tblRemainBook.getModel().getValueAt(row, 3).toString());
                    }
                }
            }
        });
        tblBorrowBook.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (selectedId == 0) {
                    int row = tblBorrowBook.rowAtPoint(e.getPoint());
                    remainTableIndex = -1;
                    borrowTableIndex = row;
                    if (row >= 0) {
                        txtBookId.setText(tblBorrowBook.getModel().getValueAt(row, 0).toString());
                        txtBookName.setText(tblBorrowBook.getModel().getValueAt(row, 1).toString());
                        txtAuthorName.setText(tblBorrowBook.getModel().getValueAt(row, 2).toString());
                        txtStatus.setText(tblBorrowBook.getModel().getValueAt(row, 3).toString());
                    }
                }
            }
        });
        btnAddDocument.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (remainTableIndex > -1) {
                    borrowDocumentTableModel.addRow(new Object[] {
                            desireDocumentTableModel.getValueAt(remainTableIndex, 0),
                            desireDocumentTableModel.getValueAt(remainTableIndex, 1),
                            desireDocumentTableModel.getValueAt(remainTableIndex, 2),
                            desireDocumentTableModel.getValueAt(remainTableIndex, 3),
                    });
                    desireDocumentTableModel.removeRow(remainTableIndex);
                    remainTableIndex = -1;
                    flushDocumentInfo();
                }
                else {
                    JOptionPane.showMessageDialog(ManageBorrowDetailForm.this,
                            "Chưa chọn tài liệu mượn",
                            "Cảnh báo",
                            JOptionPane.ERROR_MESSAGE);
                }
            }
        });
        btnRemoveDocument.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (borrowTableIndex > -1) {
                    desireDocumentTableModel.addRow(new Object[] {
                            borrowDocumentTableModel.getValueAt(borrowTableIndex, 0),
                            borrowDocumentTableModel.getValueAt(borrowTableIndex, 1),
                            borrowDocumentTableModel.getValueAt(borrowTableIndex, 2),
                            borrowDocumentTableModel.getValueAt(borrowTableIndex, 3),
                    });
                    borrowDocumentTableModel.removeRow(borrowTableIndex);
                    borrowTableIndex = -1;
                    flushDocumentInfo();
                }
                else {
                    JOptionPane.showMessageDialog(ManageBorrowDetailForm.this,
                            "Chưa chọn tài liệu huỷ mượn",
                            "Cảnh báo",
                            JOptionPane.ERROR_MESSAGE);
                }
            }
        });
        btnClear.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                flushDocumentInfo();
                remainTableIndex = -1;
                borrowTableIndex = -1;
                initializeComponentStatus();
                borrowDocumentTableModel.setRowCount(0);
                desireDocumentTableModel.setRowCount(0);
                loadDesireDocuments();
                loadDefaultDate();
            }
        });
        btnReturn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int rowCount = borrowDocumentTableModel.getRowCount();
                if (rowCount > 0) {
                    int choice = JOptionPane.showConfirmDialog(ManageBorrowDetailForm.this,
                            "Bạn muốn cập nhật trạng thái đã trả cho phiếu mượn này?",
                            "Warning",
                            JOptionPane.YES_NO_OPTION);
                    if (choice == JOptionPane.YES_OPTION && selectedId > 0) {
                        BorrowDetailController borrowDetailController = new BorrowDetailController();
                        borrowDetailController.updateBorrowDetailsStatus(selectedId, BorrowStatusConstants.RETURNED);
                        JOptionPane.showMessageDialog(ManageBorrowDetailForm.this,
                                "Trả phiếu mượn thành công",
                                "Thành công",
                                JOptionPane.INFORMATION_MESSAGE);
                        backToManageBorrow();
                    }
                }
                else {
                    JOptionPane.showMessageDialog(ManageBorrowDetailForm.this,
                            "Chưa chọn tài liệu",
                            "Cảnh báo",
                            JOptionPane.ERROR_MESSAGE);
                }
            }
        });
    }

    private void backToManageBorrow() {
        ManageBorrowForm manageBorrowForm = new ManageBorrowForm(null, staffId);
        dispose();
        manageBorrowForm.setVisible(true);
    }

    private void flushDocumentInfo() {
        txtBookId.setText("");
        txtStatus.setText("");
        txtBookName.setText("");
        txtAuthorName.setText("");
    }

    private void initializeData() {
        if (selectedId > 0) {
            BorrowController borrowController = new BorrowController();
            Borrow borrow = borrowController.getBorrowByID(selectedId);
            if (borrow != null) {
                for (int i = 0; i < cbUser.getItemCount(); i++) {
                    ComboboxItem item = (ComboboxItem) cbUser.getItemAt(i);
                    if (item.getId() == borrow.getUserId()) {
                        cbUser.setSelectedIndex(i);
                        break;
                    }
                }
                selectedBorrowDateField.setText(DateTimeHelper.getDate(borrow.getBorrowDate()));
                selectedReturnDateField.setText(DateTimeHelper.getDate(borrow.getReturnDate()));
                DocumentController documentController = new DocumentController();
                List<Document> documents = documentController.getDocumentInBorrow(borrow.getId());
                borrowDocumentTableModel.setRowCount(0);
                int status = borrow.getStatus();
                lblOverallStatus.setText(Borrow.getReadableStatus(status));
                if (status == BorrowStatusConstants.NOT_RETURNED) {
                    btnReturn.setEnabled(true);
                }
                else {
                    btnReturn.setEnabled(false);
                }
                for (Document document: documents) {
                    AuthorController authorController = new AuthorController();
                    borrowDocumentTableModel.addRow(new Object[] {
                            document.getId(),
                            document.getName(),
                            authorController.getAuthorByID(document.getAuthorId()).getFullname(),
                            DocumentType.toString(document.getType())
                    });
                }
            }
            else {
                JOptionPane.showMessageDialog(ManageBorrowDetailForm.this,
                        "Phiếu mượn không tồn tại",
                        "Cảnh báo",
                        JOptionPane.ERROR_MESSAGE);
                ManageBorrowForm manageBorrowForm = new ManageBorrowForm(null, staffId);
                dispose();
                manageBorrowForm.setVisible(true);
            }
        }
        else {
            loadDefaultDate();
        }
    }

    private void loadDefaultDate() {
        selectedBorrowDateField.setText(new SimpleDateFormat("yyyy-MM-dd").format(new Date()));
        // Lấy ngày hiện tại và cộng thêm 7 ngày
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_YEAR, 7);

        // Chuyển đổi sang chuỗi ngày
        selectedReturnDateField.setText(new SimpleDateFormat("yyyy-MM-dd").format(calendar.getTime()));
    }

    private void initializeComponentStatus() {
        // Nếu ID truyền sang > 0 (Xem chi tiết và thanh toán)
        // Thì chỉ hiển thị nút back và nút trả sách
        // Không cho phép thao tác thêm
        if (selectedId > 0) {
            updateComponentStatus(false);
        }
        else {
            updateComponentStatus(true);
        }
    }

    private void updateComponentStatus(boolean isNewRecord) {
        cbUser.setEditable(isNewRecord);
        cbUser.setEnabled(isNewRecord);
        btnBorrow.setEnabled(isNewRecord);
        btnReturnDat.setEnabled(isNewRecord);
        btnBorrowDat.setEnabled(isNewRecord);
        btnClear.setEnabled(isNewRecord);
        btnAddDocument.setEnabled(isNewRecord);
        btnRemoveDocument.setEnabled(isNewRecord);
        btnReturn.setEnabled(!isNewRecord);
        tblRemainBook.setEnabled(isNewRecord);
        tblBorrowBook.setEnabled(isNewRecord);
        txtStatus.setEnabled(isNewRecord);
        txtStatus.setEditable(isNewRecord);
        txtBookId.setEnabled(isNewRecord);
        txtBookId.setEditable(isNewRecord);
        txtBookName.setEnabled(isNewRecord);
        txtBookName.setEditable(isNewRecord);
        txtAuthorName.setEnabled(isNewRecord);
        txtAuthorName.setEditable(isNewRecord);
    }

    private void loadTableHeaders() {
        // Tạo header cho bảng sách chưa đăng kí mượn
        desireDocumentTableModel.addColumn("ID");
        desireDocumentTableModel.addColumn("Tên tài liệu");
        desireDocumentTableModel.addColumn("Tác giả");
        desireDocumentTableModel.addColumn("Loại");

        // Tạo header cho bảng sách đã đăng kí mượn
        borrowDocumentTableModel.addColumn("ID");
        borrowDocumentTableModel.addColumn("Tên tài liệu");
        borrowDocumentTableModel.addColumn("Tác giả");
        borrowDocumentTableModel.addColumn("Loại");
    }

    private void loadUserCombobox() {
        cbUser.removeAllItems();
        UserController userController = new UserController();
        java.util.List<ComboboxItem> comboboxItemList = new ArrayList<>();
        comboboxItemList.add(new ComboboxItem(0, ""));
        List<ComboboxItem> temp = userController.loadComboboxData();
        comboboxItemList.addAll(temp);
        for (ComboboxItem item: comboboxItemList) {
            cbUser.addItem(item);
        }
    }

    private void loadDesireDocuments() {
        desireDocumentTableModel.setRowCount(0);
        DocumentController documentController = new DocumentController();
        List<Document> documents = documentController.getDocumentNotInBorrow(selectedId);
        for (Document document: documents) {
            AuthorController authorController = new AuthorController();
            desireDocumentTableModel.addRow(new Object[] {
                    document.getId(),
                    document.getName(),
                    authorController.getAuthorByID(document.getAuthorId()).getFullname(),
                    DocumentType.toString(document.getType())
            });
        }
    }


    private boolean validateData(int userId, String borrowDate, String returnDate) throws ParseException {
        if (userId == 0) {
            return false;
        }
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        return sdf.parse(borrowDate).before(sdf.parse(returnDate));
    }
}
