package GUIs;

import Common.Helpers.DateTimeHelper;
import Common.Model.ComboboxItem;
import Controllers.BorrowController;
import Controllers.StaffController;
import Controllers.UserController;
import Models.Borrow;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class ManageBorrowForm extends JDialog {

    // Biến dùng để lưu giá trị Borrow id để xử lý
    int selectedId = 0;
    int seletedUserId = 0;
    int selectedStaffId = 0;
    int selectedStatus = 0;

    // biến staffId để lưu phiên đăng nhập
    private int staffId;

    // Components
    private JPanel manageBorrowPanel;
    private JTextField txtStaffname;
    private JTextField txtBorrowDate;
    private JTextField txtReturnDate;
    private JTextField txtUsername;
    private JComboBox cbUser;
    private JComboBox cbStatus;
    private JTextField txtSearch;
    private JTable tblBorrow;
    private JLabel lblBorrowStatus;
    private JComboBox cbStaff;
    private JPanel borrowInforPanel;
    private JPanel borrowDataPanel;
    private JButton btnUpdateDetail;
    private JButton btnClear;
    private JButton btnBack;
    private JScrollPane scrollPanel;
    private JButton btnFilter;
    private JButton btnAdd;
    private DefaultTableModel tableModel;

    public ManageBorrowForm(JFrame parent, int staffId) {
        super(parent);
        this.staffId = staffId;
        setTitle("Manage Borrow");
        setContentPane(manageBorrowPanel);
        setMinimumSize(new Dimension(700, 590));
        setPreferredSize(new Dimension(700, 590));
        borrowInforPanel.setMinimumSize(new Dimension(330, 320));
        borrowDataPanel.setMinimumSize(new Dimension(330, 430));
        setModal(true);
        setLocationRelativeTo(parent);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        // Disable các trường để không cho người dùng nhập.
        // Dữ liệu chỉ được tự động điền từ database
        disableTextField();

        // Đổ dữ liệu vào trong các combobox phần bộ lọc
        loadStaffCombobox();
        loadUserCombobox();
        loadStatusCombobox();

        // khởi tạo giá trị ban đầu cho các combobox
        cbStaff.setSelectedIndex(0);
        cbStatus.setSelectedIndex(0);
        cbUser.setSelectedIndex(0);

        // khởi tạo datatable
        tableModel = new DefaultTableModel();
        tblBorrow.setModel(tableModel);
        tableModel.setColumnCount(0);
        loadTableHeader();
        BorrowController borrowController = new BorrowController();
        List<Borrow> borrows = borrowController.getBorrowsByStaff(staffId);
        loadDataInBackground();
        tblBorrow.setEnabled(false);

        // khởi tạo trạng thái cho các button (Chưa chọn gì thì disable)
        updateButtonBasedOnSelectedItem(selectedId);

        btnFilter.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ComboboxItem temp = (ComboboxItem) cbUser.getSelectedItem();
                int selectedUserId = temp.getId();
                temp = (ComboboxItem) cbStaff.getSelectedItem();
                int selectedStaffId = temp.getId();
                temp = (ComboboxItem) cbStatus.getSelectedItem();
                int selectedStatus = temp.getId();
                loadDataInBackground();
            }
        });
        btnAdd.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ManageBorrowDetailForm manageBorrowDetailForm = new ManageBorrowDetailForm(null, 0, staffId);
                dispose();
                manageBorrowDetailForm.setVisible(true);
            }
        });
        btnUpdateDetail.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (selectedId > 0) {
                    ManageBorrowDetailForm manageBorrowDetailForm = new ManageBorrowDetailForm(null, selectedId, staffId);
                    dispose();
                    manageBorrowDetailForm.setVisible(true);
                }
                else {
                    JOptionPane.showMessageDialog(ManageBorrowForm.this,
                            "Không có phiếu mượn nào được chọn",
                            "Cảnh báo",
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
        tblBorrow.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int row = tblBorrow.rowAtPoint(e.getPoint());
                BorrowController borrowController = new BorrowController();
                int borrowId = (int) tblBorrow.getModel().getValueAt(row, 0);
                Borrow borrow = borrowController.getBorrowByID(borrowId);
                UserController userController = new UserController();
                txtUsername.setText(userController.getUserByID(borrow.getUserId()).getFullname());
                StaffController staffController = new StaffController();
                txtStaffname.setText(staffController.getStaffByID(borrow.getStaffId()).getFullname());
                txtBorrowDate.setText(DateTimeHelper.getDate(borrow.getBorrowDate()));
                txtReturnDate.setText(DateTimeHelper.getDate(borrow.getReturnDate()));
                selectedId = borrowId;
                updateButtonBasedOnSelectedItem(selectedId);
            }
        });
        txtSearch.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                super.keyReleased(e);
            }
        });
    }

    private void disableTextField() {
        txtUsername.setEditable(false);
        txtUsername.setFocusable(false);
        txtStaffname.setEditable(false);
        txtStaffname.setFocusable(false);
        txtBorrowDate.setEditable(false);
        txtBorrowDate.setFocusable(false);
        txtReturnDate.setEditable(false);
        txtReturnDate.setFocusable(false);
    }

    private void loadUserCombobox() {
        cbUser.removeAllItems();
        UserController userController = new UserController();
        List<ComboboxItem> comboboxItemList = new ArrayList<>();
        comboboxItemList.add(new ComboboxItem(0, ""));
        List<ComboboxItem> temp = userController.loadComboboxData();
        comboboxItemList.addAll(temp);
        for (ComboboxItem item: comboboxItemList) {
            cbUser.addItem(item);
        }
    }

    private void loadStaffCombobox() {
        cbStaff.removeAllItems();
        StaffController staffController = new StaffController();
        List<ComboboxItem> comboboxItemList = new ArrayList<>();
        comboboxItemList.add(new ComboboxItem(0, ""));
        List<ComboboxItem> temp = staffController.loadComboboxData();
        comboboxItemList.addAll(temp);
        for (ComboboxItem item: comboboxItemList) {
            cbStaff.addItem(item);
        }
    }

    private void loadStatusCombobox() {
        cbStatus.removeAllItems();
        cbStatus.addItem(new ComboboxItem(-1, ""));
        cbStatus.addItem(new ComboboxItem(0, "Chưa trả"));
        cbStatus.addItem(new ComboboxItem(1, "Đã trả"));
    }

    private void updateButtonBasedOnSelectedItem(int selectedId)
    {
        if (selectedId > 0) {
            btnAdd.setEnabled(false);
            btnUpdateDetail.setEnabled(true);
            btnClear.setEnabled(true);
        }
        else {
            btnAdd.setEnabled(true);
            btnUpdateDetail.setEnabled(false);
            btnClear.setEnabled(false);
        }
    }

    private void flushData()
    {
        selectedId = 0;
        selectedStaffId = 0;
        seletedUserId = 0;
        selectedStatus = -1;
        updateButtonBasedOnSelectedItem(selectedId);
    }

    private void loadTableHeader() {
        tableModel.addColumn("ID");
        tableModel.addColumn("Người mượn");
        tableModel.addColumn("Ngày mượn");
        tableModel.addColumn("Ngày trả");
        tableModel.addColumn("Trạng thái");
    }

    private void clearData() {
        txtUsername.setText("");
        txtStaffname.setText("");
        txtBorrowDate.setText("");
        txtReturnDate.setText("");
        cbStatus.setSelectedIndex(-1);
        cbUser.setSelectedIndex(0);
        cbStaff.setSelectedIndex(0);
        txtSearch.setText("");
        flushData();
    }

    private void loadDataInBackground() {
        BorrowController borrowController = new BorrowController();
        SwingWorker<List<Borrow>, Void> dataLoader = new SwingWorker<List<Borrow>, Void>() {
            @Override
            protected List<Borrow> doInBackground() throws Exception {
                return borrowController.getBorrows();
            }

            @Override
            protected void done() {
                List<Borrow> borrows = new ArrayList<>();
                try {
                    borrows = get();
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                } catch (ExecutionException e) {
                    throw new RuntimeException(e);
                }
                loadDataToTable(borrows);
            }
        };
        dataLoader.execute();
    }

    private void loadDataToTable(List<Borrow> borrows)
    {
        tableModel.setRowCount(0);
        BorrowController borrowController = new BorrowController();
        UserController userController = new UserController();
        for (Borrow borrow: borrows) {
            tableModel.addRow(new Object[] {
                    borrow.getId(),
                    userController.getUserByID(borrow.getStaffId()).getFullname(),
                    DateTimeHelper.getDate(borrow.getBorrowDate()),
                    DateTimeHelper.getDate(borrow.getReturnDate()),
                    Borrow.getReadableStatus(borrow.getStatus())
            });
        }
    }
}
