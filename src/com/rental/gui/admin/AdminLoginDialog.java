package com.rental.gui.admin;

import javax.swing.*;
import java.awt.*;

public class AdminLoginDialog extends JDialog {

    private static final String ADMIN_PASSWORD = "admin123"; 
    // ✅ change if needed

    private boolean authenticated = false;
    private JPasswordField passwordField;

    public AdminLoginDialog(JFrame parent) {
        super(parent, "Admin Login", true);
        initUI();
    }

    private void initUI() {
        setLayout(new BorderLayout(10, 10));
        setSize(300, 150);
        setLocationRelativeTo(getParent());

        JPanel center = new JPanel(new GridLayout(2, 2, 5, 5));
        center.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        center.add(new JLabel("Admin Password:"));
        passwordField = new JPasswordField();
        center.add(passwordField);

        add(center, BorderLayout.CENTER);

        JPanel buttons = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton btnLogin = new JButton("Login");
        JButton btnCancel = new JButton("Cancel");

        buttons.add(btnLogin);
        buttons.add(btnCancel);
        add(buttons, BorderLayout.SOUTH);

        btnLogin.addActionListener(e -> authenticate());
        btnCancel.addActionListener(e -> dispose());
    }

    private void authenticate() {
        String input = new String(passwordField.getPassword());

        if (ADMIN_PASSWORD.equals(input)) {
            authenticated = true;
            dispose();
        } else {
            JOptionPane.showMessageDialog(this,
                    "Incorrect password",
                    "Access Denied",
                    JOptionPane.ERROR_MESSAGE);
            passwordField.setText("");
        }
    }

    public boolean isAuthenticated() {
        return authenticated;
    }
}
