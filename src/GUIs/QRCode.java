package GUIs;

import javax.swing.*;
import java.awt.*;

public class QRCode extends JDialog{

    // đường dẫn tới QR code
    private String qrCodePath;

    // components
    private JPanel qrCodePanel;
    private JLabel lblQRCode;

    public QRCode(JFrame parent, String qrCodePath) {
        setTitle("QR Code");
        this.qrCodePath = qrCodePath;
        setContentPane(qrCodePanel);
        setMinimumSize(new Dimension(500, 500));
        setModal(true);
        setLocationRelativeTo(parent);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        lblQRCode.setIcon(new ImageIcon(qrCodePath));
        setVisible(false);
    }
}
