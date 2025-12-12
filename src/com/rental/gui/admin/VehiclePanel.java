package com.rental.gui.admin;

import com.rental.dao.VehicleDAO;
import com.rental.model.*;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.io.File;
import java.sql.SQLException;
import java.util.List;

public class VehiclePanel extends JPanel {

    private final VehicleDAO vehicleDAO = new VehicleDAO();

    // Modern colors
    private static final Color PRIMARY_COLOR = new Color(41, 128, 185);
    private static final Color SUCCESS_COLOR = new Color(46, 204, 113);
    private static final Color DANGER_COLOR = new Color(231, 76, 60);
    private static final Color WARNING_COLOR = new Color(243, 156, 18);
    private static final Color CONTENT_BG = new Color(236, 240, 241);
    private static final Color CARD_BG = Color.WHITE;
    private static final Color TEXT_DARK = new Color(44, 62, 80);
    private static final Color TEXT_LIGHT = new Color(127, 140, 141);
    private static final Color BORDER_COLOR = new Color(189, 195, 199);

    private JComboBox<String> cmbType;
    private JTextField txtBrand;
    private JTextField txtModel;
    private JTextField txtPlate;
    private JTextField txtPrice;
    private JTextField txtExtra;
    private JLabel lblImageFile;
    private String selectedImagePath = null;

    private JPanel vehicleListPanel;
    private JComboBox<String> cmbFilter;

    public VehiclePanel() {
        initUI();
        loadVehicles();
    }
    
    private void chooseImage() {
        JFileChooser fc = new JFileChooser();
        fc.setFileSelectionMode(JFileChooser.FILES_ONLY);

        int option = fc.showOpenDialog(this);

        if (option == JFileChooser.APPROVE_OPTION) {
            selectedImagePath = fc.getSelectedFile().getAbsolutePath();
            lblImageFile.setText(fc.getSelectedFile().getName());
        }
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

        JLabel titleLabel = new JLabel("Vehicle Management");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
        titleLabel.setForeground(TEXT_DARK);
        titleLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel subtitleLabel = new JLabel("Add, view, and manage your vehicle inventory");
        subtitleLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        subtitleLabel.setForeground(TEXT_LIGHT);
        subtitleLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        textPanel.add(titleLabel);
        textPanel.add(Box.createRigidArea(new Dimension(0, 5)));
        textPanel.add(subtitleLabel);

        // Filter panel
        JPanel filterPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        filterPanel.setBackground(CONTENT_BG);

        JLabel lblFilter = new JLabel("Filter:");
        lblFilter.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        lblFilter.setForeground(TEXT_DARK);

        cmbFilter = new JComboBox<>(new String[]{"All", "Available", "Rented"});
        cmbFilter.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        cmbFilter.addActionListener(e -> loadVehicles());

        filterPanel.add(lblFilter);
        filterPanel.add(cmbFilter);

        headerPanel.add(btnBack, BorderLayout.WEST);
        headerPanel.add(textPanel, BorderLayout.CENTER);
        headerPanel.add(filterPanel, BorderLayout.EAST);

        return headerPanel;
    }

    private JPanel createFormPanel() {
        JPanel formContainer = new JPanel(new BorderLayout());
        formContainer.setBackground(CARD_BG);
        formContainer.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(BORDER_COLOR, 1),
            new EmptyBorder(20, 20, 20, 20)
        ));

        JLabel formTitle = new JLabel("Add New Vehicle");
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
        cmbType = new JComboBox<>(new String[]{"CAR", "BIKE", "VAN"});
        cmbType.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        formPanel.add(cmbType, gbc);

        gbc.gridx = 2; gbc.weightx = 0;
        formPanel.add(createLabel("Brand:"), gbc);
        
        gbc.gridx = 3; gbc.weightx = 1;
        txtBrand = createStyledTextField();
        formPanel.add(txtBrand, gbc);

        // Row 2
        gbc.gridx = 0; gbc.gridy = 1; gbc.weightx = 0;
        formPanel.add(createLabel("Model:"), gbc);
        
        gbc.gridx = 1; gbc.weightx = 1;
        txtModel = createStyledTextField();
        formPanel.add(txtModel, gbc);

        gbc.gridx = 2; gbc.weightx = 0;
        formPanel.add(createLabel("Plate:"), gbc);
        
        gbc.gridx = 3; gbc.weightx = 1;
        txtPlate = createStyledTextField();
        formPanel.add(txtPlate, gbc);

        // Row 3
        gbc.gridx = 0; gbc.gridy = 2; gbc.weightx = 0;
        formPanel.add(createLabel("Price/Day:"), gbc);
        
        gbc.gridx = 1; gbc.weightx = 1;
        txtPrice = createStyledTextField();
        formPanel.add(txtPrice, gbc);

        gbc.gridx = 2; gbc.weightx = 0;
        formPanel.add(createLabel("Extra (Seats, cc, Cargo Capacity):"), gbc);
        
        gbc.gridx = 3; gbc.weightx = 1;
        txtExtra = createStyledTextField();
        formPanel.add(txtExtra, gbc);
        
        // Row 4
        gbc.gridx = 0; gbc.gridy = 3; gbc.weightx = 0;
        formPanel.add(createLabel("Image:"), gbc);

        gbc.gridx = 1; gbc.weightx = 1;
        JButton btnChooseImage = createStyledButton("Choose", PRIMARY_COLOR);
        btnChooseImage.setPreferredSize(new Dimension(100, 35));
        btnChooseImage.addActionListener(e -> chooseImage());
        formPanel.add(btnChooseImage, gbc);

        gbc.gridx = 2; gbc.gridwidth = 2;
        lblImageFile = new JLabel("No file chosen");
        lblImageFile.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        lblImageFile.setForeground(TEXT_LIGHT);
        formPanel.add(lblImageFile, gbc);

        gbc.gridwidth = 1;

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 10));
        buttonPanel.setBackground(CARD_BG);

        JButton btnClear = createStyledButton("Clear", WARNING_COLOR);
        JButton btnAdd = createStyledButton("Add Vehicle", SUCCESS_COLOR);

        btnClear.addActionListener(e -> clearForm());
        btnAdd.addActionListener(e -> addVehicle());

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

    private JPanel createVehicleCard(Vehicle vehicle) {
        JPanel card = new JPanel(new BorderLayout(15, 0));
        card.setBackground(CARD_BG);
        card.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(BORDER_COLOR, 1),
            new EmptyBorder(15, 15, 15, 15)
        ));
        card.setMaximumSize(new Dimension(Integer.MAX_VALUE, 180));

        // Image panel
        JPanel imagePanel = new JPanel(new BorderLayout());
        imagePanel.setPreferredSize(new Dimension(240, 150));
        imagePanel.setBackground(new Color(245, 245, 245));
        imagePanel.setBorder(BorderFactory.createLineBorder(BORDER_COLOR));

        JLabel imageLabel = new JLabel();
        imageLabel.setHorizontalAlignment(JLabel.CENTER);
        imageLabel.setVerticalAlignment(JLabel.CENTER);

        ImageIcon vehicleImage = loadVehicleImage(vehicle.getImagePath());
        if (vehicleImage != null) {
            imageLabel.setIcon(vehicleImage);
        } else {
            imageLabel.setText("🚗");
            imageLabel.setFont(new Font("Segoe UI", Font.PLAIN, 64));
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

        // ✅ NEW: Add extra info (seats/cc/capacity)
        JLabel lblExtra = new JLabel(vehicle.getExtra());
        lblExtra.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        lblExtra.setForeground(TEXT_DARK);
        lblExtra.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel lblPrice = new JLabel(String.format("RM%.2f per day", vehicle.getPricePerDay()));
        lblPrice.setFont(new Font("Segoe UI", Font.BOLD, 16));
        lblPrice.setForeground(PRIMARY_COLOR);
        lblPrice.setAlignmentX(Component.LEFT_ALIGNMENT);

        infoPanel.add(lblBrand);
        infoPanel.add(Box.createRigidArea(new Dimension(0, 5)));
        infoPanel.add(lblType);
        infoPanel.add(Box.createRigidArea(new Dimension(0, 3)));
        infoPanel.add(lblPlate);
        infoPanel.add(Box.createRigidArea(new Dimension(0, 3)));
        infoPanel.add(lblExtra);
        infoPanel.add(Box.createVerticalGlue());
        infoPanel.add(lblPrice);

        // Status panel
        JPanel statusPanel = new JPanel(new BorderLayout());
        statusPanel.setBackground(CARD_BG);
        statusPanel.setPreferredSize(new Dimension(120, 0));
        
        JLabel lblStatus = new JLabel(vehicle.isAvailable() ? "Available" : "Rented");
        lblStatus.setFont(new Font("Segoe UI", Font.BOLD, 12));
        lblStatus.setForeground(vehicle.isAvailable() ? SUCCESS_COLOR : DANGER_COLOR);
        lblStatus.setHorizontalAlignment(JLabel.CENTER);
        lblStatus.setVerticalAlignment(JLabel.TOP);
        
        JLabel lblId = new JLabel("#" + vehicle.getId());
        lblId.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        lblId.setForeground(TEXT_LIGHT);
        lblId.setHorizontalAlignment(JLabel.CENTER);

        JPanel statusContent = new JPanel();
        statusContent.setLayout(new BoxLayout(statusContent, BoxLayout.Y_AXIS));
        statusContent.setBackground(CARD_BG);
        statusContent.add(lblStatus);
        statusContent.add(Box.createRigidArea(new Dimension(0, 5)));
        statusContent.add(lblId);

        statusPanel.add(statusContent, BorderLayout.NORTH);

        card.add(imagePanel, BorderLayout.WEST);
        card.add(infoPanel, BorderLayout.CENTER);
        card.add(statusPanel, BorderLayout.EAST);

        return card;
    }

    private ImageIcon loadVehicleImage(String imagePath) {
        if (imagePath == null || imagePath.isEmpty()) {
            return null;
        }

        try {
            File imageFile = new File(imagePath);
            if (imageFile.exists()) {
                ImageIcon icon = new ImageIcon(imagePath);
                Image scaledImage = icon.getImage().getScaledInstance(240, 150, Image.SCALE_SMOOTH);
                return new ImageIcon(scaledImage);
            }

            java.net.URL imgURL = getClass().getResource("/" + imagePath);
            if (imgURL != null) {
                ImageIcon icon = new ImageIcon(imgURL);
                Image scaledImage = icon.getImage().getScaledInstance(240, 150, Image.SCALE_SMOOTH);
                return new ImageIcon(scaledImage);
            }
        } catch (Exception e) {
            System.err.println("Failed to load image: " + imagePath);
        }

        return null;
    }

    private void loadVehicles() {
        try {
            List<Vehicle> vehicles = vehicleDAO.getAllVehicles();
            vehicleListPanel.removeAll();

            String filter = (String) cmbFilter.getSelectedItem();
            
            List<Vehicle> filteredVehicles = vehicles.stream()
                .filter(v -> {
                    if ("Available".equals(filter)) return v.isAvailable();
                    if ("Rented".equals(filter)) return !v.isAvailable();
                    return true;
                })
                .toList();

            if (filteredVehicles.isEmpty()) {
                JLabel noVehiclesLabel = new JLabel("No vehicles found");
                noVehiclesLabel.setFont(new Font("Segoe UI", Font.ITALIC, 16));
                noVehiclesLabel.setForeground(TEXT_LIGHT);
                noVehiclesLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
                vehicleListPanel.add(Box.createVerticalGlue());
                vehicleListPanel.add(noVehiclesLabel);
                vehicleListPanel.add(Box.createVerticalGlue());
            } else {
                for (Vehicle vehicle : filteredVehicles) {
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

    private void addVehicle() {
        String type = (String) cmbType.getSelectedItem();
        String brand = txtBrand.getText().trim();
        String model = txtModel.getText().trim();
        String plate = txtPlate.getText().trim();
        String priceStr = txtPrice.getText().trim();
        String extraStr = txtExtra.getText().trim();

        if (brand.isEmpty() || model.isEmpty() || plate.isEmpty() || priceStr.isEmpty()) {
            showError("Please fill in all required fields.");
            return;
        }

        double price;
        try {
            price = Double.parseDouble(priceStr);
        } catch (NumberFormatException e) {
            showError("Price must be a valid number.");
            return;
        }

        Vehicle vehicle;
        try {
            switch (type) {
                case "CAR" -> {
                    int seats = extraStr.isEmpty() ? 4 : Integer.parseInt(extraStr);
                    vehicle = new Car(0, brand, model, plate, price, true, seats, selectedImagePath);
                }
                case "BIKE" -> {
                    int cc = extraStr.isEmpty() ? 100 : Integer.parseInt(extraStr);
                    vehicle = new Bike(0, brand, model, plate, price, true, cc, selectedImagePath);
                }
                case "VAN" -> {
                    double capacity = extraStr.isEmpty() ? 1000 : Double.parseDouble(extraStr);
                    vehicle = new Van(0, brand, model, plate, price, true, capacity, selectedImagePath);
                }
                default -> throw new IllegalArgumentException();
            }
        } catch (NumberFormatException e) {
            showError("Extra field must be a valid number.");
            return;
        }

        try {
            vehicleDAO.addVehicle(vehicle);
            showSuccess("Vehicle added successfully!");
            clearForm();
            loadVehicles();
        } catch (SQLException e) {
            showError("Database error: " + e.getMessage());
        }
    }

    private void clearForm() {
        txtBrand.setText("");
        txtModel.setText("");
        txtPlate.setText("");
        txtPrice.setText("");
        txtExtra.setText("");
        cmbType.setSelectedIndex(0);
        selectedImagePath = null;
        if (lblImageFile != null) {
            lblImageFile.setText("No file chosen");
        }
    }

    private void showError(String message) {
        JOptionPane.showMessageDialog(this, message, "Error", JOptionPane.ERROR_MESSAGE);
    }

    private void showSuccess(String message) {
        JOptionPane.showMessageDialog(this, message, "Success", JOptionPane.INFORMATION_MESSAGE);
    }
}