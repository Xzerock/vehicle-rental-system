package com.rental.gui.admin;

import com.rental.dao.CustomerDAO;
import com.rental.dao.RentalDAO;
import com.rental.model.Customer;
import com.rental.model.Rental;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.sql.SQLException;
import java.util.List;

public class CustomerPanel extends JPanel {

    private final CustomerDAO customerDAO = new CustomerDAO();
    private final RentalDAO rentalDAO = new RentalDAO();

    // Modern colors
    private static final Color PRIMARY_COLOR = new Color(41, 128, 185);
    private static final Color SUCCESS_COLOR = new Color(46, 204, 113);
    private static final Color DANGER_COLOR = new Color(231, 76, 60);
    private static final Color WARNING_COLOR = new Color(243, 156, 18);
    private static final Color PREMIUM_COLOR = new Color(241, 196, 15);
    private static final Color CONTENT_BG = new Color(236, 240, 241);
    private static final Color CARD_BG = Color.WHITE;
    private static final Color TEXT_DARK = new Color(44, 62, 80);
    private static final Color TEXT_LIGHT = new Color(127, 140, 141);
    private static final Color BORDER_COLOR = new Color(189, 195, 199);

    private JComboBox<String> cmbType;
    private JTextField txtName;
    private JTextField txtPhone;
    private JTextField txtEmail;

    private JPanel customerListPanel;

    public CustomerPanel() {
        initUI();
        loadCustomers();
    }

    private void initUI() {
        setLayout(new BorderLayout(15, 15));
        setBackground(CONTENT_BG);
        setBorder(new EmptyBorder(20, 20, 20, 20));

        add(createHeaderPanel(), BorderLayout.NORTH);
        add(createFormPanel(), BorderLayout.SOUTH);

        // Scrollable customer list
        customerListPanel = new JPanel();
        customerListPanel.setLayout(new BoxLayout(customerListPanel, BoxLayout.Y_AXIS));
        customerListPanel.setBackground(CONTENT_BG);

        JScrollPane scrollPane = new JScrollPane(customerListPanel);
        scrollPane.setBorder(null);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);

        add(scrollPane, BorderLayout.CENTER);
    }

    private JPanel createHeaderPanel() {
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(CONTENT_BG);
        headerPanel.setBorder(new EmptyBorder(0, 0, 15, 0));

        // Back button
        JButton btnBack = new JButton("← Back");
        btnBack.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        btnBack.setForeground(PRIMARY_COLOR);
        btnBack.setBackground(CONTENT_BG);
        btnBack.setFocusPainted(false);
        btnBack.setBorderPainted(false);
        btnBack.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnBack.addActionListener(e -> {
            Container parent = getParent();
            if (parent != null) {
                parent.removeAll();
                parent.revalidate();
                parent.repaint();
            }
        });

        JPanel textPanel = new JPanel();
        textPanel.setLayout(new BoxLayout(textPanel, BoxLayout.Y_AXIS));
        textPanel.setBackground(CONTENT_BG);

        JLabel titleLabel = new JLabel("Customer Management");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
        titleLabel.setForeground(TEXT_DARK);
        titleLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel subtitleLabel = new JLabel("Add, view, and manage customer information");
        subtitleLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        subtitleLabel.setForeground(TEXT_LIGHT);
        subtitleLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        textPanel.add(titleLabel);
        textPanel.add(Box.createRigidArea(new Dimension(0, 5)));
        textPanel.add(subtitleLabel);

        headerPanel.add(btnBack, BorderLayout.WEST);
        headerPanel.add(textPanel, BorderLayout.CENTER);

        return headerPanel;
    }

    private JPanel createFormPanel() {
        JPanel formContainer = new JPanel(new BorderLayout());
        formContainer.setBackground(CARD_BG);
        formContainer.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(BORDER_COLOR, 1),
            new EmptyBorder(20, 20, 20, 20)
        ));

        JLabel formTitle = new JLabel("Add New Customer");
        formTitle.setFont(new Font("Segoe UI", Font.BOLD, 16));
        formTitle.setForeground(TEXT_DARK);

        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBackground(CARD_BG);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 5, 5, 5);

        // Row 1
        gbc.gridx = 0; gbc.gridy = 0; gbc.weightx = 0;
        formPanel.add(createLabel("Type:"), gbc);
        
        gbc.gridx = 1; gbc.weightx = 1;
        cmbType = new JComboBox<>(new String[]{"REGULAR", "PREMIUM"});
        cmbType.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        formPanel.add(cmbType, gbc);

        gbc.gridx = 2; gbc.weightx = 0;
        formPanel.add(createLabel("Full Name:"), gbc);
        
        gbc.gridx = 3; gbc.weightx = 1;
        txtName = createStyledTextField();
        formPanel.add(txtName, gbc);

        // Row 2
        gbc.gridx = 0; gbc.gridy = 1; gbc.weightx = 0;
        formPanel.add(createLabel("Phone:"), gbc);
        
        gbc.gridx = 1; gbc.weightx = 1;
        txtPhone = createStyledTextField();
        formPanel.add(txtPhone, gbc);

        gbc.gridx = 2; gbc.weightx = 0;
        formPanel.add(createLabel("Email:"), gbc);
        
        gbc.gridx = 3; gbc.weightx = 1;
        txtEmail = createStyledTextField();
        formPanel.add(txtEmail, gbc);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 10));
        buttonPanel.setBackground(CARD_BG);

        JButton btnClear = createStyledButton("Clear", WARNING_COLOR);
        JButton btnAdd = createStyledButton("Add Customer", SUCCESS_COLOR);

        btnClear.addActionListener(e -> clearForm());
        btnAdd.addActionListener(e -> addCustomer());

        buttonPanel.add(btnClear);
        buttonPanel.add(btnAdd);

        JPanel topPanel = new JPanel(new BorderLayout(0, 15));
        topPanel.setBackground(CARD_BG);
        topPanel.add(formTitle, BorderLayout.NORTH);
        topPanel.add(formPanel, BorderLayout.CENTER);

        formContainer.add(topPanel, BorderLayout.CENTER);
        formContainer.add(buttonPanel, BorderLayout.SOUTH);

        return formContainer;
    }

    private JPanel createCustomerCard(Customer customer) {
        JPanel card = new JPanel(new BorderLayout(15, 0));
        card.setBackground(CARD_BG);
        card.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(BORDER_COLOR, 1),
            new EmptyBorder(15, 15, 15, 15)
        ));
        card.setMaximumSize(new Dimension(Integer.MAX_VALUE, 110));

        // Info panel
        JPanel infoPanel = new JPanel();
        infoPanel.setLayout(new BoxLayout(infoPanel, BoxLayout.Y_AXIS));
        infoPanel.setBackground(CARD_BG);

        JLabel lblName = new JLabel(customer.getName());
        lblName.setFont(new Font("Segoe UI", Font.BOLD, 18));
        lblName.setForeground(TEXT_DARK);
        lblName.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel lblType = new JLabel(customer.getType() + " Customer");
        lblType.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        lblType.setForeground("PREMIUM".equals(customer.getType()) ? PREMIUM_COLOR : TEXT_LIGHT);
        lblType.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel lblPhone = new JLabel(customer.getPhone());
        lblPhone.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        lblPhone.setForeground(TEXT_DARK);
        lblPhone.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel lblEmail = new JLabel((customer.getEmail() != null ? customer.getEmail() : "N/A"));
        lblEmail.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        lblEmail.setForeground(TEXT_LIGHT);
        lblEmail.setAlignmentX(Component.LEFT_ALIGNMENT);

        infoPanel.add(lblName);
        infoPanel.add(Box.createRigidArea(new Dimension(0, 5)));
        infoPanel.add(lblType);
        infoPanel.add(Box.createRigidArea(new Dimension(0, 8)));
        infoPanel.add(lblPhone);
        infoPanel.add(Box.createRigidArea(new Dimension(0, 3)));
        infoPanel.add(lblEmail);

        // Rental info panel (RIGHT side)
        JPanel rentalPanel = new JPanel();
        rentalPanel.setLayout(new BoxLayout(rentalPanel, BoxLayout.Y_AXIS));
        rentalPanel.setBackground(CARD_BG);
        rentalPanel.setPreferredSize(new Dimension(350, 0));

        try {
            List<Rental> activeRentals = rentalDAO.getActiveRentalsByCustomer(customer.getId());
            
            if (activeRentals.isEmpty()) {
                JLabel lblNoRentals = new JLabel("No active rentals");
                lblNoRentals.setFont(new Font("Segoe UI", Font.ITALIC, 13));
                lblNoRentals.setForeground(TEXT_LIGHT);
                lblNoRentals.setAlignmentX(Component.LEFT_ALIGNMENT);
                rentalPanel.add(lblNoRentals);
            } else {
                JLabel lblRentalTitle = new JLabel("Active Rentals:");
                lblRentalTitle.setFont(new Font("Segoe UI", Font.BOLD, 13));
                lblRentalTitle.setForeground(TEXT_DARK);
                lblRentalTitle.setAlignmentX(Component.LEFT_ALIGNMENT);
                rentalPanel.add(lblRentalTitle);
                rentalPanel.add(Box.createRigidArea(new Dimension(0, 5)));

                for (Rental rental : activeRentals) {
                    JLabel lblRental = new JLabel(rental.getVehicle().getBrand() + " " + 
                                                  rental.getVehicle().getModel() + 
                                                  " (" + rental.getVehicle().getPlateNumber() + ")");
                    lblRental.setFont(new Font("Segoe UI", Font.PLAIN, 12));
                    lblRental.setForeground(WARNING_COLOR);
                    lblRental.setAlignmentX(Component.LEFT_ALIGNMENT);
                    rentalPanel.add(lblRental);
                    rentalPanel.add(Box.createRigidArea(new Dimension(0, 3)));
                }
            }
        } catch (SQLException e) {
            JLabel lblError = new JLabel("Error loading rentals");
            lblError.setFont(new Font("Segoe UI", Font.ITALIC, 12));
            lblError.setForeground(DANGER_COLOR);
            lblError.setAlignmentX(Component.LEFT_ALIGNMENT);
            rentalPanel.add(lblError);
        }

        card.add(infoPanel, BorderLayout.WEST);
        card.add(rentalPanel, BorderLayout.EAST);

        return card;
    }

    private void loadCustomers() {
        try {
            List<Customer> customers = customerDAO.getAllCustomers();
            customerListPanel.removeAll();

            if (customers.isEmpty()) {
                JLabel noCustomersLabel = new JLabel("No customers found");
                noCustomersLabel.setFont(new Font("Segoe UI", Font.ITALIC, 16));
                noCustomersLabel.setForeground(TEXT_LIGHT);
                noCustomersLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
                customerListPanel.add(Box.createVerticalGlue());
                customerListPanel.add(noCustomersLabel);
                customerListPanel.add(Box.createVerticalGlue());
            } else {
                for (Customer customer : customers) {
                    customerListPanel.add(createCustomerCard(customer));
                    customerListPanel.add(Box.createRigidArea(new Dimension(0, 10)));
                }
            }

            customerListPanel.revalidate();
            customerListPanel.repaint();

        } catch (SQLException e) {
            showError("Error loading customers: " + e.getMessage());
        }
    }

    private JLabel createLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        label.setForeground(TEXT_DARK);
        return label;
    }

    private JTextField createStyledTextField() {
        JTextField textField = new JTextField();
        textField.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        textField.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(BORDER_COLOR),
            new EmptyBorder(5, 10, 5, 10)
        ));
        return textField;
    }

    private JButton createStyledButton(String text, Color bgColor) {
        JButton button = new JButton(text);
        button.setFont(new Font("Segoe UI", Font.BOLD, 13));
        button.setForeground(Color.WHITE);
        button.setBackground(bgColor);
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setPreferredSize(new Dimension(130, 35));

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

    private void addCustomer() {
        String type = (String) cmbType.getSelectedItem();
        String name = txtName.getText().trim();
        String phone = txtPhone.getText().trim();
        String email = txtEmail.getText().trim();

        if (name.isEmpty() || phone.isEmpty()) {
            showError("Name and phone number are required.");
            return;
        }

        Customer customer = new Customer(0, type, name, phone, email);

        try {
            customerDAO.addCustomer(customer);
            showSuccess("Customer added successfully!");
            clearForm();
            loadCustomers();
        } catch (SQLException e) {
            showError("Database error: " + e.getMessage());
        }
    }

    private void clearForm() {
        txtName.setText("");
        txtPhone.setText("");
        txtEmail.setText("");
        cmbType.setSelectedIndex(0);
    }

    private void showError(String message) {
        JOptionPane.showMessageDialog(this, message, "Error", JOptionPane.ERROR_MESSAGE);
    }

    private void showSuccess(String message) {
        JOptionPane.showMessageDialog(this, message, "Success", JOptionPane.INFORMATION_MESSAGE);
    }
}