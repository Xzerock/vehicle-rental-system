package com.rental.gui.customer;

import com.rental.dao.CustomerDAO;
import com.rental.model.Customer;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.sql.SQLException;

public class CustomerSignupDialog extends JDialog {
    
    private final CustomerDAO customerDAO = new CustomerDAO();
    
    // Modern colors - Green theme for customer portal
    private static final Color PRIMARY_COLOR = new Color(46, 204, 113);
    private static final Color DANGER_COLOR = new Color(231, 76, 60);
    private static final Color SUCCESS_COLOR = new Color(39, 174, 96);
    private static final Color BACKGROUND = new Color(236, 240, 241);
    private static final Color CARD_BG = Color.WHITE;
    private static final Color TEXT_DARK = new Color(44, 62, 80);
    private static final Color TEXT_LIGHT = new Color(127, 140, 141);
    
    private Customer createdCustomer;
    private JTextField txtName;
    private JTextField txtPhone;
    private JTextField txtEmail;
    private JLabel errorLabel;
    
    public CustomerSignupDialog(JFrame parent) {
        super(parent, "Customer Sign Up", true);
        initUI();
    }
    
    private void initUI() {
        setLayout(new BorderLayout());
        setSize(500, 450);
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
        
        JLabel titleLabel = new JLabel("Create Account");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        JLabel subtitleLabel = new JLabel("Fill in your details to create a new account");
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
        
        // Full Name field
        JLabel nameLabel = new JLabel("Full Name *");
        nameLabel.setFont(new Font("Segoe UI", Font.BOLD, 13));
        nameLabel.setForeground(TEXT_DARK);
        nameLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        txtName = new JTextField();
        txtName.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        txtName.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(189, 195, 199)),
            new EmptyBorder(10, 12, 10, 12)
        ));
        txtName.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        txtName.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        // Phone Number field
        JLabel phoneLabel = new JLabel("Phone Number *");
        phoneLabel.setFont(new Font("Segoe UI", Font.BOLD, 13));
        phoneLabel.setForeground(TEXT_DARK);
        phoneLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        txtPhone = new JTextField();
        txtPhone.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        txtPhone.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(189, 195, 199)),
            new EmptyBorder(10, 12, 10, 12)
        ));
        txtPhone.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        txtPhone.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        // Email field
        JLabel emailLabel = new JLabel("Email Address (Optional)");
        emailLabel.setFont(new Font("Segoe UI", Font.BOLD, 13));
        emailLabel.setForeground(TEXT_DARK);
        emailLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        txtEmail = new JTextField();
        txtEmail.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        txtEmail.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(189, 195, 199)),
            new EmptyBorder(10, 12, 10, 12)
        ));
        txtEmail.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        txtEmail.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        // Enter key support on last field
        txtEmail.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    signup();
                }
            }
        });
        
        // Error label
        errorLabel = new JLabel(" ");
        errorLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        errorLabel.setForeground(DANGER_COLOR);
        errorLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        // Info label
        JLabel infoLabel = new JLabel("* Required fields");
        infoLabel.setFont(new Font("Segoe UI", Font.ITALIC, 11));
        infoLabel.setForeground(TEXT_LIGHT);
        infoLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        contentPanel.add(nameLabel);
        contentPanel.add(Box.createRigidArea(new Dimension(0, 8)));
        contentPanel.add(txtName);
        contentPanel.add(Box.createRigidArea(new Dimension(0, 15)));
        
        contentPanel.add(phoneLabel);
        contentPanel.add(Box.createRigidArea(new Dimension(0, 8)));
        contentPanel.add(txtPhone);
        contentPanel.add(Box.createRigidArea(new Dimension(0, 15)));
        
        contentPanel.add(emailLabel);
        contentPanel.add(Box.createRigidArea(new Dimension(0, 8)));
        contentPanel.add(txtEmail);
        contentPanel.add(Box.createRigidArea(new Dimension(0, 8)));
        
        contentPanel.add(errorLabel);
        contentPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        contentPanel.add(infoLabel);
        
        // Button panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 15));
        buttonPanel.setBackground(CARD_BG);
        
        JButton btnCancel = createStyledButton("Cancel", new Color(149, 165, 166));
        JButton btnSignup = createStyledButton("Sign Up", SUCCESS_COLOR);
        
        btnCancel.addActionListener(e -> dispose());
        btnSignup.addActionListener(e -> signup());
        
        buttonPanel.add(btnCancel);
        buttonPanel.add(btnSignup);
        
        // Assemble
        mainPanel.add(headerPanel, BorderLayout.NORTH);
        mainPanel.add(contentPanel, BorderLayout.CENTER);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);
        
        add(mainPanel);
        
        // Focus first field
        txtName.requestFocusInWindow();
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
    
    private void signup() {
        String name = txtName.getText().trim();
        String phone = txtPhone.getText().trim();
        String email = txtEmail.getText().trim();
        
        if (name.isEmpty() || phone.isEmpty()) {
            errorLabel.setText("Name and phone number are required.");
            if (name.isEmpty()) {
                txtName.requestFocusInWindow();
            } else {
                txtPhone.requestFocusInWindow();
            }
            shakeDialog();
            return;
        }
        
        try {
            Customer c = new Customer(0, "REGULAR", name, phone, email);
            customerDAO.addCustomer(c);
            
            // Retrieve created customer for auto-login
            createdCustomer = customerDAO.getCustomerByIdentifier(phone);
            
            // Show success message
            JOptionPane.showMessageDialog(
                this,
                "Account created successfully!\nYou will be logged in automatically.",
                "Success",
                JOptionPane.INFORMATION_MESSAGE
            );
            
            dispose();
            
        } catch (SQLException ex) {
            errorLabel.setText("Error: Phone/email may already exist or database error.");
            ex.printStackTrace();
            shakeDialog();
        }
    }
    
    private void shakeDialog() {
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
    
    public Customer getCreatedCustomer() {
        return createdCustomer;
    }
}