package com.rental.gui.admin;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class AdminLoginDialog extends JDialog {
    
    private static final String ADMIN_PASSWORD = "admin123";
    
    // Modern colors
    private static final Color PRIMARY_COLOR = new Color(41, 128, 185);
    private static final Color DANGER_COLOR = new Color(231, 76, 60);
    private static final Color SUCCESS_COLOR = new Color(46, 204, 113);
    private static final Color BACKGROUND = new Color(236, 240, 241);
    private static final Color CARD_BG = Color.WHITE;
    private static final Color TEXT_DARK = new Color(44, 62, 80);
    private static final Color TEXT_LIGHT = new Color(127, 140, 141);
    
    private boolean authenticated = false;
    private JPasswordField passwordField;
    private JLabel errorLabel;
    
    public AdminLoginDialog(JFrame parent) {
        super(parent, "Administrator Login", true);
        initUI();
    }
    
    private void initUI() {
        setLayout(new BorderLayout());
        setSize(450, 320);
        setLocationRelativeTo(getParent());
        setResizable(false);
        
        // Main container
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(BACKGROUND);
        
        // Header
        JPanel headerPanel = new JPanel();
        headerPanel.setBackground(PRIMARY_COLOR);
        headerPanel.setPreferredSize(new Dimension(0, 80));
        headerPanel.setLayout(new BoxLayout(headerPanel, BoxLayout.Y_AXIS));
        headerPanel.setBorder(new EmptyBorder(20, 30, 20, 30));
        
        JLabel titleLabel = new JLabel("Admin Access");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        JLabel subtitleLabel = new JLabel("Enter your administrator password");
        subtitleLabel.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        subtitleLabel.setForeground(new Color(255, 255, 255, 200));
        subtitleLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        headerPanel.add(titleLabel);
        headerPanel.add(Box.createRigidArea(new Dimension(0, 5)));
        headerPanel.add(subtitleLabel);
        
        // Content panel
        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
        contentPanel.setBackground(CARD_BG);
        contentPanel.setBorder(new EmptyBorder(30, 40, 30, 40));
        
        // Password field
        JLabel passwordLabel = new JLabel("Password");
        passwordLabel.setFont(new Font("Segoe UI", Font.BOLD, 13));
        passwordLabel.setForeground(TEXT_DARK);
        passwordLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        passwordField = new JPasswordField();
        passwordField.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        passwordField.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(189, 195, 199)),
            new EmptyBorder(10, 12, 10, 12)
        ));
        passwordField.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        passwordField.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        // Enter key support
        passwordField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    authenticate();
                }
            }
        });
        
        // Error label
        errorLabel = new JLabel(" ");
        errorLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        errorLabel.setForeground(DANGER_COLOR);
        errorLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        // Info label
        JLabel infoLabel = new JLabel("Default password: admin123");
        infoLabel.setFont(new Font("Segoe UI", Font.ITALIC, 11));
        infoLabel.setForeground(TEXT_LIGHT);
        infoLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        contentPanel.add(passwordLabel);
        contentPanel.add(Box.createRigidArea(new Dimension(0, 8)));
        contentPanel.add(passwordField);
        contentPanel.add(Box.createRigidArea(new Dimension(0, 8)));
        contentPanel.add(errorLabel);
        contentPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        contentPanel.add(infoLabel);
        
        // Button panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 15));
        buttonPanel.setBackground(CARD_BG);
        
        JButton btnCancel = createStyledButton("Cancel", new Color(149, 165, 166));
        JButton btnLogin = createStyledButton("Login", SUCCESS_COLOR);
        
        btnCancel.addActionListener(e -> dispose());
        btnLogin.addActionListener(e -> authenticate());
        
        buttonPanel.add(btnCancel);
        buttonPanel.add(btnLogin);
        
        // Assemble
        mainPanel.add(headerPanel, BorderLayout.NORTH);
        mainPanel.add(contentPanel, BorderLayout.CENTER);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);
        
        add(mainPanel);
        
        // Focus password field
        passwordField.requestFocusInWindow();
    }
    
    private JButton createStyledButton(String text, Color bgColor) {
        JButton button = new JButton(text);
        button.setFont(new Font("Segoe UI", Font.BOLD, 13));
        button.setForeground(Color.WHITE);
        button.setBackground(bgColor);
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setPreferredSize(new Dimension(100, 35));
        
        button.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setBackground(bgColor.darker());
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setBackground(bgColor);
            }
        });
        
        return button;
    }
    
    private void authenticate() {
        String input = new String(passwordField.getPassword());
        if (ADMIN_PASSWORD.equals(input)) {
            authenticated = true;
            dispose();
        } else {
            errorLabel.setText("Incorrect password. Please try again.");
            passwordField.setText("");
            passwordField.requestFocusInWindow();
            
            // Shake animation effect
            Point originalLocation = getLocation();
            try {
                for (int i = 0; i < 3; i++) {
                    setLocation(originalLocation.x - 10, originalLocation.y);
                    Thread.sleep(50);
                    setLocation(originalLocation.x + 10, originalLocation.y);
                    Thread.sleep(50);
                }
                setLocation(originalLocation);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
    
    public boolean isAuthenticated() {
        return authenticated;
    }
}