package GUIs;

import Common.Constants.DocumentType;
import Common.Helpers.QRCodeHelper;
import Common.Model.ComboboxItem;
import Controllers.AuthorController;
import Controllers.BorrowDetailController;
import Controllers.DocumentController;
import Models.Author;
import Models.BorrowDetail;
import Models.DocumentFactory.Document;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.regex.Pattern;

public class ManageDocumentForm extends JDialog {

    // Biến dùng để lưu giá trị Document id để xử lý
    int selectedId = 0;

    // biến staffId để lưu phiên đăng nhập
    private int staffId;

    // Components
    private JTextField txtDocumentName;
    private JTextField txtDocumentPrice;
    private DefaultTableModel tableModel;
    private JTable tblDocument;
    private JButton btnAdd;
    private JButton btnDelete;
    private JButton btnUpdate;
    private JButton btnClear;
    private JTextArea txtDocumentDescription;
    private JLabel lblDocumentName;
    private JLabel lblDocumentPrice;
    private JLabel lblDocumentDescription;
    private JComboBox cbDocumentAuthor;
    private JLabel lblDocumentAuthor;
    private JComboBox cbDocumentType;
    private JLabel lblDocumentType;
    private JButton btnBack;
    private JTextField txtSearch;
    private JPanel ManageDocumentPanel;
    private JTextField txtQuantity;
    private JLabel lblQuantity;
    private JPanel documentInforPanel;
    private JPanel documentDataPanel;
    private JButton btnQR;


    public ManageDocumentForm(JFrame parent, int staffId) {
        super(parent);
        this.staffId = staffId;
        setTitle("Manage Document");
        setContentPane(ManageDocumentPanel);
        setMinimumSize(new Dimension(700, 500));
        setPreferredSize(new Dimension(700, 500));
        documentDataPanel.setMinimumSize(new Dimension(330, 430));
        documentInforPanel.setMinimumSize(new Dimension(330, 320));
        setModal(true);
        setLocationRelativeTo(parent);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        DocumentType[] documentTypes = DocumentType.values();
        tableModel = new DefaultTableModel();
        tblDocument.setModel(tableModel);
        // Clear dữ liệu
        cbDocumentAuthor.removeAllItems();
        cbDocumentType.removeAllItems();
        // Cập nhật trạng thái cho các button khi mới khởi tạo Form
        // Add - Enable
        // Update và Delete - Disable (Do chưa có bản ghi nào được chọn)
        updateButtonBasedOnSelectedItem(selectedId);
        // Load dữ liệu vào combobox Document Type
        cbDocumentType.addItem("");
        for (DocumentType documentType: documentTypes) {
            cbDocumentType.addItem(documentType.name());
        }
        // Load dữ liệu vào combobox Author
        cbDocumentAuthor.addItem(new ComboboxItem(0, ""));
        AuthorController authorController = new AuthorController();
        List<ComboboxItem> comboboxItemList = authorController.loadComboboxData();
        for (ComboboxItem item: comboboxItemList) {
            cbDocumentAuthor.addItem(item);
        }
        // Load dữ liệu vào bảng
        tableModel.setColumnCount(0);
        loadTableHeader();
        loadDataInBackground();
        btnAdd.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                DocumentController documentController = new DocumentController();
                String documentName = txtDocumentName.getText();
                ComboboxItem item = (ComboboxItem) cbDocumentAuthor.getSelectedItem();
                int authorId = item.getId();
                String price = txtDocumentPrice.getText();
                String documentDescription = txtDocumentDescription.getText();
                String documentType = cbDocumentType.getSelectedItem().toString();
                String quantity = txtQuantity.getText();
                boolean isValid = validateData(documentName, price, quantity, documentType, authorId);
                if (isValid) {
                    double documentPrice = Double.valueOf(price);
                    int documentQuantity = Integer.valueOf(quantity);
                    Document document = new Document(documentName, authorId, documentQuantity, documentPrice, documentDescription, documentType);
                    int result = documentController.createDocument(document);
                    if (result > 0) {
                        JOptionPane.showMessageDialog(ManageDocumentForm.this,
                                "Tạo mới thành công",
                                "Thành công",
                                JOptionPane.INFORMATION_MESSAGE);
                        clearData();
                        loadDataInBackground();
                    }
                    else {
                        JOptionPane.showMessageDialog(ManageDocumentForm.this,
                                "Có lỗi xảy ra\nVui lòng thử lại",
                                "Thất bại",
                                JOptionPane.ERROR_MESSAGE);
                    }
                }
                else {
                    JOptionPane.showMessageDialog(ManageDocumentForm.this,
                            "Dữ liệu nhập vào không chính xác\nVui lòng thử lại",
                            "Thất bại",
                            JOptionPane.ERROR_MESSAGE);
                }
            }
        });
        btnUpdate.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                DocumentController documentController = new DocumentController();
                String documentName = txtDocumentName.getText();
                ComboboxItem item = (ComboboxItem) cbDocumentAuthor.getSelectedItem();
                int authorId = item.getId();
                String price = txtDocumentPrice.getText();
                String documentDescription = txtDocumentDescription.getText();
                String documentType = cbDocumentType.getSelectedItem().toString();
                String quantity = txtQuantity.getText();
                boolean isValid = validateData(documentName, price, quantity, documentType, authorId);
                if (isValid && selectedId > 0) {
                    double documentPrice = Double.valueOf(price);
                    int documentQuantity = Integer.valueOf(quantity);
                    Document document = new Document(selectedId, documentName, authorId, documentQuantity, documentPrice, documentDescription, documentType);
                    int result = documentController.updateDocument(document);
                    if (result > 0) {
                        JOptionPane.showMessageDialog(ManageDocumentForm.this,
                                "Cập nhật thành công",
                                "Thành công",
                                JOptionPane.INFORMATION_MESSAGE);
                        clearData();
                        loadDataInBackground();
                    }
                    else {
                        JOptionPane.showMessageDialog(ManageDocumentForm.this,
                                "Có lỗi xảy ra\nVui lòng thử lại",
                                "Thất bại",
                                JOptionPane.ERROR_MESSAGE);
                    }
                }
                else {
                    JOptionPane.showMessageDialog(ManageDocumentForm.this,
                            "Dữ liệu nhập vào không chính xác\nVui lòng thử lại",
                            "Thất bại",
                            JOptionPane.ERROR_MESSAGE);
                }
            }
        });
        btnDelete.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int choice = JOptionPane.showConfirmDialog(ManageDocumentForm.this,
                        "Bạn chắc chắn muốn xoá tài liệu này?",
                        "Warning",
                        JOptionPane.YES_NO_OPTION);
                if (choice == JOptionPane.YES_OPTION && selectedId > 0) {
                    BorrowDetailController borrowDetailController = new BorrowDetailController();
                    List<BorrowDetail> borrowDetails = borrowDetailController.getBorrowDetailByDocument(selectedId);
                    if (!borrowDetails.isEmpty())
                    {
                        JOptionPane.showMessageDialog(ManageDocumentForm.this,
                                "Không thể xoá tài liệu do có phiếu mượn liên quan",
                                "Thất bại",
                                JOptionPane.ERROR_MESSAGE);
                    }
                    else {
                        DocumentController documentController = new DocumentController();
                        int result = documentController.deleteDocument(selectedId);
                        if (result > 0) {
                            JOptionPane.showMessageDialog(ManageDocumentForm.this,
                                    "Xoá tài liệu thành công",
                                    "Thành công",
                                    JOptionPane.INFORMATION_MESSAGE);
                            clearData();
                            loadDataInBackground();
                        }
                        else {
                            JOptionPane.showMessageDialog(ManageDocumentForm.this,
                                    "Có lỗi xảy ra\nVui lòng thử lại",
                                    "Thất bại",
                                    JOptionPane.ERROR_MESSAGE);
                        }
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
        txtSearch.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                String search = txtSearch.getText();
                DocumentController documentController = new DocumentController();
                List<Document> documents = new ArrayList<>();
                if (!search.isEmpty()) {
                    documents = documentController.searchDocuments(search);
                }
                else {
                    documents = documentController.getDocuments();
                }
                updateTableModel(documents);
            }
        });
        setVisible(false);
        tblDocument.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int row = tblDocument.rowAtPoint(e.getPoint());
                DocumentController documentController = new DocumentController();
                int documentId = (int) tblDocument.getModel().getValueAt(row, 0);
                Document document = documentController.getDocumentByID(documentId);
                txtDocumentName.setText(document.getName());
                for (int i = 0; i < cbDocumentAuthor.getItemCount(); i++) {
                    ComboboxItem item = (ComboboxItem) cbDocumentAuthor.getItemAt(i);
                    if (item.getId() == document.getAuthorId()) {
                        cbDocumentAuthor.setSelectedIndex(i);
                        break;
                    }
                }
                txtDocumentPrice.setText(Double.toString(document.getPrice()));
                cbDocumentType.setSelectedItem(document.getType());
                txtQuantity.setText(Integer.toString(document.getQuantity()));
                txtDocumentDescription.setText(document.getDescription());
                selectedId = documentId;
                updateButtonBasedOnSelectedItem(selectedId);
            }
        });
        btnQR.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String documentName = txtDocumentName.getText();
                String filePath = QRCodeHelper.generateQRCode(documentName, "png");
                QRCode qrCode = new QRCode(null, filePath);
                qrCode.setVisible(true);
            }
        });
    }

    private void updateButtonBasedOnSelectedItem(int selectedId)
    {
        if (selectedId > 0) {
            btnAdd.setEnabled(false);
            btnUpdate.setEnabled(true);
            btnDelete.setEnabled(true);
            btnQR.setEnabled(true);
        }
        else {
            btnAdd.setEnabled(true);
            btnUpdate.setEnabled(false);
            btnDelete.setEnabled(false);
            btnQR.setEnabled(false);
        }
    }

    private void flushData()
    {
        selectedId = 0;
        updateButtonBasedOnSelectedItem(selectedId);
    }

    private void loadTableHeader() {
        tableModel.addColumn("ID");
        tableModel.addColumn("Tên");
        tableModel.addColumn("Tác giả");
        tableModel.addColumn("Giá");
        tableModel.addColumn("Số lượng");
        tableModel.addColumn("Loại");
    }

    private void loadDataInBackground()
    {
        DocumentController documentController = new DocumentController();
        // create a swing worker thread to load data
        SwingWorker<List<Document>, Void> dataLoader = new SwingWorker<List<Document>, Void>() {
            @Override
            protected List<Document> doInBackground() throws Exception {
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException ex) {
                    ex.printStackTrace();
                }
                return documentController.getDocuments();
            }

            // Phương thức này được gọi khi hoàn thành việc load
            @Override
            protected void done() {
                List<Document> documents = null;
                try {
                    documents = get();
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                } catch (ExecutionException e) {
                    throw new RuntimeException(e);
                }
                updateTableModel(documents);
            }
        };
        // Bắt đầu thực thi SwingWorker
        dataLoader.execute();
    }

    private void updateTableModel(List<Document> documents) {
        tableModel.setRowCount(0);
        for (Document document: documents) {
            AuthorController authorController = new AuthorController();
            Author author = authorController.getAuthorByID(document.getAuthorId());
            tableModel.addRow(new Object[] {
                    document.getId(),
                    document.getName(),
                    author.getFullname(),
                    document.getPrice(),
                    document.getQuantity(),
                    DocumentType.toString(document.getType())
            });
        }
    }

    private void clearData() {
        txtDocumentName.setText("");
        cbDocumentAuthor.setSelectedIndex(0);
        txtDocumentPrice.setText("");
        cbDocumentType.setSelectedIndex(0);
        txtDocumentDescription.setText("");
        txtSearch.setText("");
        flushData();
    }

    private boolean validateData(String documentName,
                                 String documentPrice,
                                 String documentQuantity,
                                 String documentType,
                                 int authorId)
    {
        String decimalPattern = "^[0-9]+([\\\\,\\\\.][0-9]+)?$";
        String integerPattern = "^[1-9]\\d*$";
        boolean matchPrice = Pattern.matches(decimalPattern, documentPrice);
        boolean matchQuantity = Pattern.matches(integerPattern, documentQuantity);
        if (documentName == null || documentName.isEmpty()) {
            return false;
        }
        if (documentPrice == null || documentPrice.isEmpty() || !matchPrice) {
            return false;
        }
        if (documentQuantity == null || documentQuantity.isEmpty() || !matchQuantity) {
            return false;
        }
        if (documentType == null || documentType.isEmpty()) {
            return false;
        }
        return authorId >= 1;
    }
}
