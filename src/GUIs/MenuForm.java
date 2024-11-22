package GUIs;


import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MenuForm extends JDialog {

    // biến staffId để lưu phiên đăng nhập
    private int staffId;

    // components
    private JButton btnManageBorrow;
    private JButton btnManageDocument;
    private JButton btnManageAuthor;
    private JButton btnExit;
    private JButton btnManageUser;
    private JPanel menuPanel;

    private static MenuForm instance;
    private MenuForm(JFrame parent, int userId) {
        super(parent);
        staffId = userId;
        setTitle("Menu");
        setContentPane(menuPanel);
        setMinimumSize(new Dimension(500, 500));
        setModal(true);
        setLocationRelativeTo(parent);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        btnManageDocument.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ManageDocumentForm manageDocumentForm = new ManageDocumentForm(null, staffId);
                dispose();
                manageDocumentForm.setVisible(true);
            }
        });
        btnManageAuthor.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ManageAuthorForm manageAuthorForm = new ManageAuthorForm(null, staffId);
                dispose();
                manageAuthorForm.setVisible(true);
            }
        });
        btnManageUser.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ManageUserForm manageUserForm = new ManageUserForm(null, staffId);
                dispose();
                manageUserForm.setVisible(true);
            }
        });
        btnManageBorrow.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ManageBorrowForm manageBorrowForm = new ManageBorrowForm(parent, staffId);
                dispose();
                manageBorrowForm.setVisible(true);
            }
        });
        btnExit.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });
    }

    public static MenuForm getInstance(JFrame parent, int userId) {
        if (instance == null) {
            instance = new MenuForm(parent, userId);
        }
        return instance;
    }
}
