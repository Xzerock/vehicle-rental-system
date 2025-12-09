package com.rental.gui.customer;

import com.rental.dao.CustomerDAO;
import com.rental.model.Customer;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class CustomerLoginDialog extends JDialog {
    
    private final CustomerDAO customerDAO = new CustomerDAO();
    
    // Modern colors - Green theme for customer portal
    private static final Color PRIMARY_COLOR = new Color(46, 204, 113);
    private static final Color DANGER_COLOR = new Color(231, 76, 60);
    private static final Color SUCCESS_COLOR = new Color(39, 174, 96);
    private static final Color BACKGROUND = new Color(236, 240, 241);
    private static final Color CARD_BG = Color.WHITE;
    private static final Color TEXT_DARK = new Color(44, 62, 80);
    private static final Color TEXT_LIGHT = new Color(127, 140, 141);
    
    private Customer authenticatedCustomer;
    private JTextField txtIdentifier;
    private JLabel errorLabel;
    
    public CustomerLoginDialog(JFrame parent) {
        super(parent, "Customer Login", true);
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
        
        JLabel titleLabel = new JLabel("Customer Login");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        JLabel subtitleLabel = new JLabel("Enter your phone number or email to continue");
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
        
        // Phone/Email field
        JLabel identifierLabel = new JLabel("Phone Number or Email");
        identifierLabel.setFont(new Font("Segoe UI", Font.BOLD, 13));
        identifierLabel.setForeground(TEXT_DARK);
        identifierLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        txtIdentifier = new JTextField();
        txtIdentifier.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        txtIdentifier.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(189, 195, 199)),
            new EmptyBorder(10, 12, 10, 12)
        ));
        txtIdentifier.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        txtIdentifier.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        // Enter key support
        txtIdentifier.addKeyListener(new KeyAdapter() {
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
        JLabel infoLabel = new JLabel("Example: 123-456-7890 or user@email.com");
        infoLabel.setFont(new Font("Segoe UI", Font.ITALIC, 11));
        infoLabel.setForeground(TEXT_LIGHT);
        infoLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        contentPanel.add(identifierLabel);
        contentPanel.add(Box.createRigidArea(new Dimension(0, 8)));
        contentPanel.add(txtIdentifier);
        contentPanel.add(Box.createRigidArea(new Dimension(0, 8)));
        contentPanel.add(errorLabel);
        contentPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        contentPanel.add(infoLabel);
        
        // Button panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 15));
        buttonPanel.setBackground(CARD_BG);
        
        JButton btnSignup = createStyledButton("Sign Up", new Color(52, 152, 219));
        JButton btnCancel = createStyledButton("Cancel", new Color(149, 165, 166));
        JButton btnLogin = createStyledButton("Login", SUCCESS_COLOR);
        
        btnSignup.addActionListener(e -> openSignup());
        btnCancel.addActionListener(e -> dispose());
        btnLogin.addActionListener(e -> authenticate());
        
        buttonPanel.add(btnSignup);
        buttonPanel.add(btnCancel);
        buttonPanel.add(btnLogin);
        
        // Assemble
        mainPanel.add(headerPanel, BorderLayout.NORTH);
        mainPanel.add(contentPanel, BorderLayout.CENTER);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);
        
        add(mainPanel);
        
        // Focus text field
        txtIdentifier.requestFocusInWindow();
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
        String identifier = txtIdentifier.getText().trim();
        
        if (identifier.isEmpty()) {
            errorLabel.setText("Please enter your phone number or email.");
            txtIdentifier.requestFocusInWindow();
            shakeDialog();
            return;
        }
        
        try {
            Customer customer = customerDAO.getCustomerByIdentifier(identifier);
            if (customer == null) {
                errorLabel.setText("Customer not found. Please check your information.");
                txtIdentifier.setText("");
                txtIdentifier.requestFocusInWindow();
                shakeDialog();
                return;
            }
            
            authenticatedCustomer = customer;
            dispose();
            
        } catch (Exception e) {
            errorLabel.setText("Database error: " + e.getMessage());
            e.printStackTrace();
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
    
    private void openSignup() {
        CustomerSignupDialog dialog = new CustomerSignupDialog((JFrame) getParent());
        dialog.setVisible(true);
        Customer c = dialog.getCreatedCustomer();
        if (c != null) {
            authenticatedCustomer = c; // auto login after signup
            dispose();
        }
    }
    
    public Customer getAuthenticatedCustomer() {
        return authenticatedCustomer;
    }
}