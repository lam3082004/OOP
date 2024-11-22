package GUIs;

import Controllers.AuthController;
import Models.Staff;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Login extends JDialog{
    private JTextField txtUsername;
    private JLabel lblUsername;
    private JLabel lblPassword;
    private JButton btnLogin;
    private JButton btnClear;
    private JPanel loginPanel;
    private JPasswordField txtPassword;

    public Login(JFrame parent) {
        super(parent);
        setTitle("Login");
        setContentPane(loginPanel);
        setMinimumSize(new Dimension(500, 500));
        setModal(true);
        setLocationRelativeTo(parent);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        btnLogin.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String username = txtUsername.getText().trim();
                String password = txtPassword.getText().trim();
                boolean isValidate = validateData(username, password);
                if (isValidate) {
                    AuthController authController = new AuthController();
                    Staff staff = authController.Login(username, password);
                    if (staff != null)
                    {
                        dispose();
                        MenuForm.getInstance(null, staff.getId()).setVisible(true);
                    }
                    else {
                        JOptionPane.showMessageDialog(Login.this,
                                "Thông tin đăng nhập sai",
                                "Cảnh báo",
                                JOptionPane.ERROR_MESSAGE);
                    }
                }
            }
        });
        btnClear.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                txtUsername.setText("");
                txtPassword.setText("");
            }
        });
        setVisible(false);
    }

    private boolean validateData(String username, String password) {
        if (username != null && !username.isEmpty() && password != null && !password.isEmpty()) {
            return true;
        }
        if (password != null && !password.isEmpty()) {
            JOptionPane.showMessageDialog(Login.this,
                    "Username không được để trống",
                    "Cảnh báo",
                    JOptionPane.ERROR_MESSAGE);
            return false;
        }
        if (username != null && !username.isEmpty())
        {
            JOptionPane.showMessageDialog(Login.this,
                    "Password không được để trống",
                    "Cảnh báo",
                    JOptionPane.ERROR_MESSAGE);
            return false;
        }
        JOptionPane.showMessageDialog(Login.this,
                "Username và password không được để trống",
                "Cảnh báo",
                JOptionPane.ERROR_MESSAGE);
        return false;
    }
}
