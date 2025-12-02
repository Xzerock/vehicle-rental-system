package com.rental.gui.admin;

import com.rental.gui.MainFrame;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class AdminPanel extends JPanel {
    
    private final MainFrame mainFrame;
    private JPanel contentArea;
    private JButton selectedButton;
    
    // Modern colors
    private static final Color PRIMARY_COLOR = new Color(41, 128, 185);
    private static final Color PRIMARY_DARK = new Color(31, 97, 141);
    private static final Color SIDEBAR_BG = new Color(44, 62, 80);
    private static final Color CONTENT_BG = new Color(236, 240, 241);
    private static final Color BUTTON_HOVER = new Color(52, 73, 94);
    private static final Color TEXT_WHITE = Color.WHITE;
    private static final Color TEXT_DARK = new Color(44, 62, 80);
    private static final Color DANGER_COLOR = new Color(231, 76, 60);
    
    public AdminPanel(MainFrame mainFrame) {
        this.mainFrame = mainFrame;
        initUI();
    }
    
    private void initUI() {
        setLayout(new BorderLayout());
        setBackground(CONTENT_BG);
        
        // Header
        add(createHeaderPanel(), BorderLayout.NORTH);
        
        // Sidebar
        add(createSidebarPanel(), BorderLayout.WEST);
        
        // Content area
        contentArea = new JPanel(new BorderLayout());
        contentArea.setBackground(CONTENT_BG);
        contentArea.add(createWelcomePanel(), BorderLayout.CENTER);
        add(contentArea, BorderLayout.CENTER);
    }
    
    private JPanel createHeaderPanel() {
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(PRIMARY_COLOR);
        headerPanel.setPreferredSize(new Dimension(0, 70));
        headerPanel.setBorder(new EmptyBorder(15, 25, 15, 25));
        
        JLabel titleLabel = new JLabel("Administration Dashboard");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
        titleLabel.setForeground(TEXT_WHITE);
        
        JPanel userPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        userPanel.setOpaque(false);
        JLabel userLabel = new JLabel("Administrator");
        userLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        userLabel.setForeground(TEXT_WHITE);
        userLabel.setIcon(new ImageIcon()); // Add icon if needed
        userPanel.add(userLabel);
        
        headerPanel.add(titleLabel, BorderLayout.WEST);
        headerPanel.add(userPanel, BorderLayout.EAST);
        
        return headerPanel;
    }
    
    private JPanel createSidebarPanel() {
        JPanel sidebar = new JPanel();
        sidebar.setLayout(new BoxLayout(sidebar, BoxLayout.Y_AXIS));
        sidebar.setBackground(SIDEBAR_BG);
        sidebar.setPreferredSize(new Dimension(250, 0));
        sidebar.setBorder(new EmptyBorder(20, 0, 20, 0));
        
        // Navigation buttons
        addNavigationButton(sidebar, "Manage Vehicles", this::showVehiclePanel);
        addNavigationButton(sidebar, "Manage Customers", this::showCustomerPanel);
        
        // Spacer
        sidebar.add(Box.createVerticalGlue());
        
        // Back button
        JButton btnBack = createSidebarButton("← Back to Home");
        btnBack.setBackground(DANGER_COLOR);
        btnBack.addActionListener(e -> {
            int confirm = JOptionPane.showConfirmDialog(
                this,
                "Are you sure you want to exit admin panel?",
                "Confirm Exit",
                JOptionPane.YES_NO_OPTION
            );
            if (confirm == JOptionPane.YES_OPTION) {
                mainFrame.showHome();
            }
        });
        sidebar.add(btnBack);
        
        return sidebar;
    }
    
    private void addNavigationButton(JPanel panel, String text, Runnable action) {
        JButton button = createSidebarButton(text);
        button.addActionListener(e -> {
            setSelectedButton(button);
            action.run();
        });
        panel.add(button);
        panel.add(Box.createRigidArea(new Dimension(0, 10)));
    }
    
    private JButton createSidebarButton(String text) {
        JButton button = new JButton(text);
        button.setAlignmentX(Component.CENTER_ALIGNMENT);
        button.setMaximumSize(new Dimension(230, 50));
        button.setPreferredSize(new Dimension(230, 50));
        button.setFont(new Font("Segoe UI", Font.PLAIN, 15));
        button.setForeground(TEXT_WHITE);
        button.setBackground(SIDEBAR_BG);
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setHorizontalAlignment(SwingConstants.LEFT);
        button.setBorder(new EmptyBorder(10, 20, 10, 20));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        button.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                if (button != selectedButton) {
                    button.setBackground(BUTTON_HOVER);
                }
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                if (button != selectedButton) {
                    button.setBackground(SIDEBAR_BG);
                }
            }
        });
        
        return button;
    }
    
    private void setSelectedButton(JButton button) {
        if (selectedButton != null) {
            selectedButton.setBackground(SIDEBAR_BG);
        }
        selectedButton = button;
        button.setBackground(PRIMARY_DARK);
    }
    
    private JPanel createWelcomePanel() {
        JPanel welcomePanel = new JPanel(new GridBagLayout());
        welcomePanel.setBackground(CONTENT_BG);
        
        JPanel centerPanel = new JPanel();
        centerPanel.setLayout(new BoxLayout(centerPanel, BoxLayout.Y_AXIS));
        centerPanel.setBackground(CONTENT_BG);
        
        JLabel welcomeLabel = new JLabel("Welcome to Admin Dashboard");
        welcomeLabel.setFont(new Font("Segoe UI", Font.BOLD, 32));
        welcomeLabel.setForeground(TEXT_DARK);
        welcomeLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        JLabel subtitleLabel = new JLabel("Select an option from the sidebar to manage your system");
        subtitleLabel.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        subtitleLabel.setForeground(new Color(127, 140, 141));
        subtitleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        centerPanel.add(welcomeLabel);
        centerPanel.add(Box.createRigidArea(new Dimension(0, 15)));
        centerPanel.add(subtitleLabel);
        
        welcomePanel.add(centerPanel);
        return welcomePanel;
    }
    
    private void showVehiclePanel() {
        VehiclePanel vehiclePanel = new VehiclePanel();
        addBackButtonSupport(vehiclePanel);
        switchPanel(vehiclePanel);
    }
    
    private void showCustomerPanel() {
        CustomerPanel customerPanel = new CustomerPanel();
        addBackButtonSupport(customerPanel);
        switchPanel(customerPanel);
    }
    
    private void addBackButtonSupport(JPanel panel) {
        // Find and configure back button in the panel
        Component[] components = panel.getComponents();
        for (Component comp : components) {
            if (comp instanceof JPanel) {
                JPanel innerPanel = (JPanel) comp;
                configureBackButton(innerPanel);
            }
        }
    }
    
    private void configureBackButton(JPanel panel) {
        Component[] components = panel.getComponents();
        for (Component comp : components) {
            if (comp instanceof JButton) {
                JButton btn = (JButton) comp;
                if (btn.getText().contains("Back")) {
                    // Remove existing listeners
                    for (var listener : btn.getActionListeners()) {
                        btn.removeActionListener(listener);
                    }
                    // Add new listener to show welcome panel
                    btn.addActionListener(e -> {
                        contentArea.removeAll();
                        contentArea.add(createWelcomePanel(), BorderLayout.CENTER);
                        contentArea.revalidate();
                        contentArea.repaint();
                        if (selectedButton != null) {
                            selectedButton.setBackground(SIDEBAR_BG);
                            selectedButton = null;
                        }
                    });
                }
            } else if (comp instanceof JPanel) {
                configureBackButton((JPanel) comp);
            }
        }
    }
    
    private void switchPanel(JPanel panel) {
        contentArea.removeAll();
        contentArea.add(panel, BorderLayout.CENTER);
        contentArea.revalidate();
        contentArea.repaint();
    }
}