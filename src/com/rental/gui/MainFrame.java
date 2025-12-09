package com.rental.gui;

import com.rental.gui.admin.AdminPanel;
import com.rental.gui.rental.RentalPanel;
import com.rental.gui.admin.AdminLoginDialog;
import com.rental.gui.customer.CustomerLoginDialog;
import com.rental.model.Customer;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class MainFrame extends JFrame {
    
    private JPanel contentPanel;
    
    // Modern color palette
    private static final Color PRIMARY_COLOR = new Color(41, 128, 185);
    private static final Color PRIMARY_DARK = new Color(31, 97, 141);
    private static final Color ACCENT_COLOR = new Color(52, 152, 219);
    private static final Color SUCCESS_COLOR = new Color(46, 204, 113);
    private static final Color BACKGROUND = new Color(236, 240, 241);
    private static final Color CARD_BG = Color.WHITE;
    private static final Color TEXT_DARK = new Color(44, 62, 80);
    private static final Color TEXT_LIGHT = new Color(127, 140, 141);
    
    public MainFrame() {
        initUI();
    }
    
    private void initUI() {
        setTitle("Vehicle Rental System");
        setSize(1000, 650);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
        
        // Set system look and feel
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        // Header with gradient background
        JPanel headerPanel = createHeaderPanel();
        add(headerPanel, BorderLayout.NORTH);
        
        // Main content area
        contentPanel = new JPanel(new BorderLayout());
        contentPanel.setBackground(BACKGROUND);
        contentPanel.add(createRoleSelectionPanel(), BorderLayout.CENTER);
        add(contentPanel, BorderLayout.CENTER);
        
        // Footer
        add(createFooterPanel(), BorderLayout.SOUTH);
    }
    
    private JPanel createHeaderPanel() {
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(PRIMARY_COLOR);
        headerPanel.setPreferredSize(new Dimension(0, 100));
        headerPanel.setBorder(new EmptyBorder(20, 40, 20, 40));
        
        // Title section
        JPanel titlePanel = new JPanel();
        titlePanel.setLayout(new BoxLayout(titlePanel, BoxLayout.Y_AXIS));
        titlePanel.setOpaque(false);
        
        JLabel titleLabel = new JLabel("Vehicle Rental System");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 32));
        titleLabel.setForeground(Color.WHITE);
        
        titlePanel.add(titleLabel);
        titlePanel.add(Box.createRigidArea(new Dimension(0, 5)));
        
        headerPanel.add(titlePanel, BorderLayout.WEST);
        
        return headerPanel;
    }
    
    private JPanel createRoleSelectionPanel() {
        JPanel mainPanel = new JPanel(new GridBagLayout());
        mainPanel.setBackground(BACKGROUND);
        
        JPanel centerPanel = new JPanel();
        centerPanel.setLayout(new BoxLayout(centerPanel, BoxLayout.Y_AXIS));
        centerPanel.setOpaque(false);
        
        // Welcome message
        JLabel welcomeLabel = new JLabel("Welcome! Please select your role");
        welcomeLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
        welcomeLabel.setForeground(TEXT_DARK);
        welcomeLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        JLabel instructionLabel = new JLabel("Choose how you want to access the system");
        instructionLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        instructionLabel.setForeground(TEXT_LIGHT);
        instructionLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        centerPanel.add(welcomeLabel);
        centerPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        centerPanel.add(instructionLabel);
        centerPanel.add(Box.createRigidArea(new Dimension(0, 40)));
        
        // Role cards container
        JPanel cardsPanel = new JPanel(new GridLayout(1, 2, 30, 0));
        cardsPanel.setOpaque(false);
        cardsPanel.setMaximumSize(new Dimension(700, 280));
        
        // Admin card
        JPanel adminCard = createRoleCard(
            "Administrator",
            "Manage vehicles, customers, and system settings",
            new String[]{"• Manage vehicle inventory", "• View all rentals", "• Generate reports", "• System administration"},
            PRIMARY_COLOR,
            e -> {
                AdminLoginDialog dialog = new AdminLoginDialog(this);
                dialog.setVisible(true);
                if (dialog.isAuthenticated()) {
                    showAdminPanel();
                }
            }
        );
        
        // Rental card
        JPanel rentalCard = createRoleCard(
            "Customer Portal",
            "Browse and rent vehicles for your needs",
            new String[]{"• Browse available vehicles", "• Make reservations", "• View rental history", "• Manage bookings"},
            SUCCESS_COLOR,
            e -> {
                CustomerLoginDialog dialog = new CustomerLoginDialog(this);
                dialog.setVisible(true);

                Customer customer = dialog.getAuthenticatedCustomer();
                if (customer != null) {
                    showRentalPanel(customer); // ✅ logged-in access
                }
            }
        );
        
        cardsPanel.add(adminCard);
        cardsPanel.add(rentalCard);
        
        centerPanel.add(cardsPanel);
        
        mainPanel.add(centerPanel);
        
        return mainPanel;
    }
    
    private JPanel createRoleCard(String title, String description, String[] features, Color accentColor, java.awt.event.ActionListener action) {
        JPanel card = new JPanel();
        card.setLayout(new BorderLayout(0, 15));
        card.setBackground(CARD_BG);
        card.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(189, 195, 199), 1),
            new EmptyBorder(25, 20, 25, 20)
        ));
        
        // Hover effect
        card.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                card.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(accentColor, 2),
                    new EmptyBorder(24, 19, 24, 19)
                ));
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                card.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(new Color(189, 195, 199), 1),
                    new EmptyBorder(25, 20, 25, 20)
                ));
            }
        });
        
        // Header section
        JPanel headerSection = new JPanel();
        headerSection.setLayout(new BoxLayout(headerSection, BoxLayout.Y_AXIS));
        headerSection.setOpaque(false);
        
        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 20));
        titleLabel.setForeground(TEXT_DARK);
        titleLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        JLabel descLabel = new JLabel(description);
        descLabel.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        descLabel.setForeground(TEXT_LIGHT);
        descLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        headerSection.add(titleLabel);
        headerSection.add(Box.createRigidArea(new Dimension(0, 8)));
        headerSection.add(descLabel);
        
        // Features section
        JPanel featuresPanel = new JPanel();
        featuresPanel.setLayout(new BoxLayout(featuresPanel, BoxLayout.Y_AXIS));
        featuresPanel.setOpaque(false);
        featuresPanel.setBorder(new EmptyBorder(10, 0, 10, 0));
        
        for (String feature : features) {
            JLabel featureLabel = new JLabel(feature);
            featureLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
            featureLabel.setForeground(TEXT_DARK);
            featureLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
            featuresPanel.add(featureLabel);
            featuresPanel.add(Box.createRigidArea(new Dimension(0, 6)));
        }
        
        // Button
        JButton accessButton = new JButton("Access Portal");
        accessButton.setFont(new Font("Segoe UI", Font.BOLD, 14));
        accessButton.setForeground(Color.WHITE);
        accessButton.setBackground(accentColor);
        accessButton.setFocusPainted(false);
        accessButton.setBorderPainted(false);
        accessButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        accessButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        accessButton.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        
        accessButton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                accessButton.setBackground(accentColor.darker());
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                accessButton.setBackground(accentColor);
            }
        });
        
        accessButton.addActionListener(action);
        
        card.add(headerSection, BorderLayout.NORTH);
        card.add(featuresPanel, BorderLayout.CENTER);
        card.add(accessButton, BorderLayout.SOUTH);
        
        return card;
    }
    
    private JPanel createFooterPanel() {
        JPanel footerPanel = new JPanel(new BorderLayout());
        footerPanel.setBackground(new Color(44, 62, 80));
        footerPanel.setPreferredSize(new Dimension(0, 40));
        footerPanel.setBorder(new EmptyBorder(10, 20, 10, 20));
        
        JLabel copyrightLabel = new JLabel("© 2025 Vehicle Rental System. All rights reserved.");
        copyrightLabel.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        copyrightLabel.setForeground(new Color(189, 195, 199));
        
        JLabel versionLabel = new JLabel("v1.0.0");
        versionLabel.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        versionLabel.setForeground(new Color(189, 195, 199));
        
        footerPanel.add(copyrightLabel, BorderLayout.WEST);
        footerPanel.add(versionLabel, BorderLayout.EAST);
        
        return footerPanel;
    }
    
    private void showAdminPanel() {
        contentPanel.removeAll();
        contentPanel.add(new AdminPanel(this), BorderLayout.CENTER);
        refresh();
    }
    
    private void showRentalPanel(Customer customer) {
        contentPanel.removeAll();
        contentPanel.add(new RentalPanel(this, customer), BorderLayout.CENTER);
        refresh();
    }
    
    public void showHome() {
        dispose();
        new MainFrame().setVisible(true);
    }
    
    private void refresh() {
        contentPanel.revalidate();
        contentPanel.repaint();
    }
    
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new MainFrame().setVisible(true));
    }
}