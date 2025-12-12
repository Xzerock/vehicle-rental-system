package com.rental.gui.rental;

import com.rental.dao.VehicleDAO;
import com.rental.model.Customer;
import com.rental.model.Vehicle;
import com.rental.service.RentalService;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.io.File;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

public class RentVehiclePanel extends JPanel {

    private final VehicleDAO vehicleDAO = new VehicleDAO();
    private final RentalService rentalService = new RentalService();
    private final Customer customer;

    // Modern colors
    private static final Color PRIMARY_COLOR = new Color(46, 204, 113);
    private static final Color SUCCESS_COLOR = new Color(39, 174, 96);
    private static final Color WARNING_COLOR = new Color(243, 156, 18);
    private static final Color CONTENT_BG = new Color(236, 240, 241);
    private static final Color CARD_BG = Color.WHITE;
    private static final Color TEXT_DARK = new Color(44, 62, 80);
    private static final Color TEXT_LIGHT = new Color(127, 140, 141);
    private static final Color BORDER_COLOR = new Color(189, 195, 199);

    private JPanel vehicleListPanel;
    private JTextField txtStartDate;
    private JTextField txtEndDate;
    private JLabel lblCalculatedPrice;
    private Vehicle selectedVehicle;

    public RentVehiclePanel(Customer customer) {
        this.customer = customer;
        initUI();
        loadVehicles();
    }

    private void initUI() {
        setLayout(new BorderLayout(15, 15));
        setBackground(CONTENT_BG);
        setBorder(new EmptyBorder(20, 20, 20, 20));

        add(createHeaderPanel(), BorderLayout.NORTH);
        add(createFormPanel(), BorderLayout.SOUTH);

        // Scrollable vehicle list
        vehicleListPanel = new JPanel();
        vehicleListPanel.setLayout(new BoxLayout(vehicleListPanel, BoxLayout.Y_AXIS));
        vehicleListPanel.setBackground(CONTENT_BG);

        JScrollPane scrollPane = new JScrollPane(vehicleListPanel);
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

        JPanel textPanel = new JPanel();
        textPanel.setLayout(new BoxLayout(textPanel, BoxLayout.Y_AXIS));
        textPanel.setBackground(CONTENT_BG);

        JLabel titleLabel = new JLabel("Rent a Vehicle");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
        titleLabel.setForeground(TEXT_DARK);
        titleLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel subtitleLabel = new JLabel("Select an available vehicle to create a rental");
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

        JLabel formTitle = new JLabel("Rental Details");
        formTitle.setFont(new Font("Segoe UI", Font.BOLD, 16));
        formTitle.setForeground(TEXT_DARK);

        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBackground(CARD_BG);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(8, 8, 8, 8);
        
        // Customer info
        gbc.gridx = 0; gbc.gridy = 0; gbc.weightx = 0;
        formPanel.add(createLabel("Customer:"), gbc);

        gbc.gridx = 1; gbc.weightx = 1;
        JLabel lblCustomer = new JLabel(customer.getName() + " (" + customer.getType() + ")");
        lblCustomer.setFont(new Font("Segoe UI", Font.BOLD, 14));
        lblCustomer.setForeground(TEXT_DARK);
        formPanel.add(lblCustomer, gbc);

        gbc.gridx = 2; gbc.weightx = 0;
        formPanel.add(createLabel("Start Date:"), gbc);
        
        gbc.gridx = 3; gbc.weightx = 1;
        txtStartDate = createStyledTextField();
        txtStartDate.setText(LocalDate.now().toString());
        txtStartDate.addCaretListener(e -> calculatePrice());
        formPanel.add(txtStartDate, gbc);

        // End date and price
        gbc.gridx = 0; gbc.gridy = 1; gbc.weightx = 0;
        formPanel.add(createLabel("End Date:"), gbc);
        
        gbc.gridx = 1; gbc.weightx = 1;
        txtEndDate = createStyledTextField();
        txtEndDate.setText(LocalDate.now().plusDays(1).toString());
        txtEndDate.addCaretListener(e -> calculatePrice());
        formPanel.add(txtEndDate, gbc);

        gbc.gridx = 2; gbc.weightx = 0;
        formPanel.add(createLabel("Estimated Total:"), gbc);
        
        gbc.gridx = 3; gbc.weightx = 1;
        lblCalculatedPrice = new JLabel("Select a vehicle");
        lblCalculatedPrice.setFont(new Font("Segoe UI", Font.BOLD, 16));
        lblCalculatedPrice.setForeground(PRIMARY_COLOR);
        formPanel.add(lblCalculatedPrice, gbc);

        // Button panel
        gbc.gridx = 0; gbc.gridy = 2; gbc.gridwidth = 4;
        gbc.insets = new Insets(15, 8, 0, 8);
        
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        buttonPanel.setBackground(CARD_BG);
        
        JButton btnRefresh = createStyledButton("Refresh List", WARNING_COLOR);
        JButton btnRent = createStyledButton("Confirm Rental", SUCCESS_COLOR);

        btnRefresh.addActionListener(e -> loadVehicles());
        btnRent.addActionListener(e -> rentVehicle());

        buttonPanel.add(btnRefresh);
        buttonPanel.add(btnRent);
        formPanel.add(buttonPanel, gbc);

        JPanel topPanel = new JPanel(new BorderLayout(0, 15));
        topPanel.setBackground(CARD_BG);
        topPanel.add(formTitle, BorderLayout.NORTH);
        topPanel.add(formPanel, BorderLayout.CENTER);

        formContainer.add(topPanel);
        return formContainer;
    }

    private JPanel createVehicleCard(Vehicle vehicle) {
        JPanel card = new JPanel(new BorderLayout(15, 0));
        card.setBackground(CARD_BG);
        card.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(BORDER_COLOR, 1),
            new EmptyBorder(15, 15, 15, 15)
        ));
        card.setMaximumSize(new Dimension(Integer.MAX_VALUE, 150));
        card.setCursor(new Cursor(Cursor.HAND_CURSOR));

        // Image panel - LARGER SIZE
        JPanel imagePanel = new JPanel(new BorderLayout());
        imagePanel.setPreferredSize(new Dimension(240, 160)); // Increased from 180x120
        imagePanel.setBackground(new Color(245, 245, 245));
        imagePanel.setBorder(BorderFactory.createLineBorder(BORDER_COLOR));

        JLabel imageLabel = new JLabel();
        imageLabel.setHorizontalAlignment(JLabel.CENTER);
        imageLabel.setVerticalAlignment(JLabel.CENTER);

        // Load vehicle image
        ImageIcon vehicleImage = loadVehicleImage(vehicle.getImagePath());
        if (vehicleImage != null) {
            imageLabel.setIcon(vehicleImage);
        } else {
            // Fallback icon
            imageLabel.setText("🚗");
            imageLabel.setFont(new Font("Segoe UI", Font.PLAIN, 64)); // Larger fallback icon
            imageLabel.setForeground(TEXT_LIGHT);
        }

        imagePanel.add(imageLabel, BorderLayout.CENTER);

        // Info panel
        JPanel infoPanel = new JPanel();
        infoPanel.setLayout(new BoxLayout(infoPanel, BoxLayout.Y_AXIS));
        infoPanel.setBackground(CARD_BG);

        JLabel lblBrand = new JLabel(vehicle.getBrand() + " " + vehicle.getModel());
        lblBrand.setFont(new Font("Segoe UI", Font.BOLD, 18));
        lblBrand.setForeground(TEXT_DARK);
        lblBrand.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel lblType = new JLabel(vehicle.getType());
        lblType.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        lblType.setForeground(TEXT_LIGHT);
        lblType.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel lblPlate = new JLabel("Plate: " + vehicle.getPlateNumber());
        lblPlate.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        lblPlate.setForeground(TEXT_DARK);
        lblPlate.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel lblPrice = new JLabel(String.format("$%.2f per day", vehicle.getPricePerDay()));
        lblPrice.setFont(new Font("Segoe UI", Font.BOLD, 16));
        lblPrice.setForeground(PRIMARY_COLOR);
        lblPrice.setAlignmentX(Component.LEFT_ALIGNMENT);

        infoPanel.add(lblBrand);
        infoPanel.add(Box.createRigidArea(new Dimension(0, 5)));
        infoPanel.add(lblType);
        infoPanel.add(Box.createRigidArea(new Dimension(0, 5)));
        infoPanel.add(lblPlate);
        infoPanel.add(Box.createVerticalGlue());
        infoPanel.add(lblPrice);
        
        JLabel lblExtra = new JLabel(vehicle.getExtra());
        lblExtra.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        lblExtra.setForeground(TEXT_DARK);
        lblExtra.setAlignmentX(Component.LEFT_ALIGNMENT);
        infoPanel.add(lblExtra);

        infoPanel.add(Box.createVerticalGlue());
        infoPanel.add(lblPrice);

        // Selection indicator
        JPanel statusPanel = new JPanel(new BorderLayout());
        statusPanel.setBackground(CARD_BG);
        statusPanel.setPreferredSize(new Dimension(120, 0));
        
        JLabel lblStatus = new JLabel("Available");
        lblStatus.setFont(new Font("Segoe UI", Font.BOLD, 12));
        lblStatus.setForeground(SUCCESS_COLOR);
        lblStatus.setHorizontalAlignment(JLabel.CENTER);
        lblStatus.setVerticalAlignment(JLabel.TOP);
        statusPanel.add(lblStatus, BorderLayout.NORTH);

        card.add(imagePanel, BorderLayout.WEST);
        card.add(infoPanel, BorderLayout.CENTER);
        card.add(statusPanel, BorderLayout.EAST);

        // Click to select
        card.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                selectVehicle(vehicle, card);
            }

            @Override
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                if (selectedVehicle != vehicle) {
                    card.setBorder(BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(PRIMARY_COLOR, 2),
                        new EmptyBorder(14, 14, 14, 14)
                    ));
                }
            }

            @Override
            public void mouseExited(java.awt.event.MouseEvent evt) {
                if (selectedVehicle != vehicle) {
                    card.setBorder(BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(BORDER_COLOR, 1),
                        new EmptyBorder(15, 15, 15, 15)
                    ));
                }
            }
        });

        return card;
    }

    private void selectVehicle(Vehicle vehicle, JPanel card) {
        // Deselect previous
        for (Component comp : vehicleListPanel.getComponents()) {
            if (comp instanceof JPanel && comp != card) {
                JPanel panel = (JPanel) comp;  // Cast it!
                panel.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(BORDER_COLOR, 1),
                    new EmptyBorder(15, 15, 15, 15)
                ));
            }
        }
        // Select current
        selectedVehicle = vehicle;
        card.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(PRIMARY_COLOR, 3),
            new EmptyBorder(14, 14, 14, 14)
        ));
        card.setBackground(new Color(232, 248, 240));

        calculatePrice();
    }

    private ImageIcon loadVehicleImage(String imagePath) {
        if (imagePath == null || imagePath.isEmpty()) {
            return null;
        }

        try {
            File imageFile = new File(imagePath);
            if (imageFile.exists()) {
                ImageIcon icon = new ImageIcon(imagePath);
                Image scaledImage = icon.getImage().getScaledInstance(240, 160, Image.SCALE_SMOOTH); // Updated size
                return new ImageIcon(scaledImage);
            }

            // Try as resource
            java.net.URL imgURL = getClass().getResource("/" + imagePath);
            if (imgURL != null) {
                ImageIcon icon = new ImageIcon(imgURL);
                Image scaledImage = icon.getImage().getScaledInstance(240, 160, Image.SCALE_SMOOTH); // Updated size
                return new ImageIcon(scaledImage);
            }
        } catch (Exception e) {
            System.err.println("Failed to load image: " + imagePath);
        }

        return null;
    }

    private void calculatePrice() {
        if (selectedVehicle == null) {
            lblCalculatedPrice.setText("Select a vehicle");
            return;
        }

        try {
            LocalDate start = LocalDate.parse(txtStartDate.getText().trim());
            LocalDate end = LocalDate.parse(txtEndDate.getText().trim());
            
            long days = java.time.temporal.ChronoUnit.DAYS.between(start, end);
            if (days <= 0) days = 1;
            
            double total = selectedVehicle.getPricePerDay() * days;
            lblCalculatedPrice.setText(String.format("$%.2f (%d days)", total, days));
        } catch (Exception e) {
            lblCalculatedPrice.setText("Invalid dates");
        }
    }

    private void loadVehicles() {
        try {
            List<Vehicle> vehicles = vehicleDAO.getAvailableVehicles();
            vehicleListPanel.removeAll();
            selectedVehicle = null;
            lblCalculatedPrice.setText("Select a vehicle");

            if (vehicles.isEmpty()) {
                JLabel noVehiclesLabel = new JLabel("No vehicles available");
                noVehiclesLabel.setFont(new Font("Segoe UI", Font.ITALIC, 16));
                noVehiclesLabel.setForeground(TEXT_LIGHT);
                noVehiclesLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
                vehicleListPanel.add(Box.createVerticalGlue());
                vehicleListPanel.add(noVehiclesLabel);
                vehicleListPanel.add(Box.createVerticalGlue());
            } else {
                for (Vehicle vehicle : vehicles) {
                    vehicleListPanel.add(createVehicleCard(vehicle));
                    vehicleListPanel.add(Box.createRigidArea(new Dimension(0, 10)));
                }
            }

            vehicleListPanel.revalidate();
            vehicleListPanel.repaint();

        } catch (SQLException e) {
            showError("Error loading vehicles: " + e.getMessage());
        }
    }

    private void rentVehicle() {
        if (selectedVehicle == null) {
            showError("Please select a vehicle.");
            return;
        }

        try {
            LocalDate start = LocalDate.parse(txtStartDate.getText().trim());
            LocalDate end = LocalDate.parse(txtEndDate.getText().trim());

            if (end.isBefore(start) || end.isEqual(start)) {
                showError("End date must be after start date.");
                return;
            }

            int confirm = JOptionPane.showConfirmDialog(
                this,
                String.format("Confirm rental of %s %s\nFrom: %s\nTo: %s\nTotal: %s",
                    selectedVehicle.getBrand(),
                    selectedVehicle.getModel(),
                    start,
                    end,
                    lblCalculatedPrice.getText()
                ),
                "Confirm Rental",
                JOptionPane.YES_NO_OPTION
            );

            if (confirm == JOptionPane.YES_OPTION) {
                rentalService.rentVehicle(customer, selectedVehicle, start, end);
                showSuccess("Vehicle rented successfully!");
                loadVehicles();
            }

        } catch (Exception e) {
            showError(e.getMessage());
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
        button.setPreferredSize(new Dimension(140, 38));

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

    private void showError(String msg) {
        JOptionPane.showMessageDialog(this, msg, "Error", JOptionPane.ERROR_MESSAGE);
    }

    private void showSuccess(String msg) {
        JOptionPane.showMessageDialog(this, msg, "Success", JOptionPane.INFORMATION_MESSAGE);
    }
}