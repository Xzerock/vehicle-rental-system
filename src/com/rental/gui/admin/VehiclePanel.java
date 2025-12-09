package com.rental.gui.admin;

import com.rental.dao.VehicleDAO;
import com.rental.model.*;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;
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

    private JComboBox<String> cmbType;
    private JTextField txtBrand;
    private JTextField txtModel;
    private JTextField txtPlate;
    private JTextField txtPrice;
    private JTextField txtExtra;

    private JTable table;
    private DefaultTableModel tableModel;

    public VehiclePanel() {
        initUI();
        loadVehicles();
    }

    private void initUI() {
        setLayout(new BorderLayout(15, 15));
        setBackground(CONTENT_BG);
        setBorder(new EmptyBorder(20, 20, 20, 20));

        add(createHeaderPanel(), BorderLayout.NORTH);

        JPanel mainPanel = new JPanel(new BorderLayout(15, 15));
        mainPanel.setBackground(CONTENT_BG);
        mainPanel.add(createFormPanel(), BorderLayout.NORTH);
        mainPanel.add(createTablePanel(), BorderLayout.CENTER);

        add(mainPanel, BorderLayout.CENTER);
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
            // Navigate back to admin menu
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

        headerPanel.add(btnBack, BorderLayout.WEST);
        headerPanel.add(textPanel, BorderLayout.CENTER);

        return headerPanel;
    }

    private JPanel createFormPanel() {
        JPanel formContainer = new JPanel(new BorderLayout());
        formContainer.setBackground(CARD_BG);
        formContainer.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(189, 195, 199), 1),
            new EmptyBorder(20, 20, 20, 20)
        ));

        JLabel formTitle = new JLabel("Add New Vehicle");
        formTitle.setFont(new Font("Segoe UI", Font.BOLD, 16));
        formTitle.setForeground(TEXT_DARK);
        formTitle.setBorder(new EmptyBorder(0, 0, 15, 0));

        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBackground(CARD_BG);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 5, 5, 5);

        // Row 1
        gbc.gridx = 0; gbc.gridy = 0; gbc.weightx = 0;
        formPanel.add(createLabel("Vehicle Type:"), gbc);
        
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
        formPanel.add(createLabel("Plate Number:"), gbc);
        
        gbc.gridx = 3; gbc.weightx = 1;
        txtPlate = createStyledTextField();
        formPanel.add(txtPlate, gbc);

        // Row 3
        gbc.gridx = 0; gbc.gridy = 2; gbc.weightx = 0;
        formPanel.add(createLabel("Price/Day (0.00):"), gbc);
        
        gbc.gridx = 1; gbc.weightx = 1;
        txtPrice = createStyledTextField();
        formPanel.add(txtPrice, gbc);

        gbc.gridx = 2; gbc.weightx = 0;
        formPanel.add(createLabel("Extra (Seats/CC/Capacity):"), gbc);
        
        gbc.gridx = 3; gbc.weightx = 1;
        txtExtra = createStyledTextField();
        formPanel.add(txtExtra, gbc);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 10));
        buttonPanel.setBackground(CARD_BG);

        JButton btnClear = createStyledButton("Clear", WARNING_COLOR);
        JButton btnAdd = createStyledButton("Add Vehicle", SUCCESS_COLOR);

        btnClear.addActionListener(e -> clearForm());
        btnAdd.addActionListener(e -> addVehicle());

        buttonPanel.add(btnClear);
        buttonPanel.add(btnAdd);

        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setBackground(CARD_BG);
        topPanel.add(formTitle, BorderLayout.NORTH);
        topPanel.add(formPanel, BorderLayout.CENTER);

        formContainer.add(topPanel, BorderLayout.CENTER);
        formContainer.add(buttonPanel, BorderLayout.SOUTH);

        return formContainer;
    }

    private JPanel createTablePanel() {
        JPanel tableContainer = new JPanel(new BorderLayout());
        tableContainer.setBackground(CARD_BG);
        tableContainer.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(189, 195, 199), 1),
            new EmptyBorder(20, 20, 20, 20)
        ));

        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(CARD_BG);
        headerPanel.setBorder(new EmptyBorder(0, 0, 15, 0));

        JLabel tableTitle = new JLabel("Vehicle Inventory");
        tableTitle.setFont(new Font("Segoe UI", Font.BOLD, 16));
        tableTitle.setForeground(TEXT_DARK);

        JButton btnRefresh = createStyledButton("Refresh", PRIMARY_COLOR);
        btnRefresh.addActionListener(e -> loadVehicles());

        headerPanel.add(tableTitle, BorderLayout.WEST);
        headerPanel.add(btnRefresh, BorderLayout.EAST);

        tableModel = new DefaultTableModel(
            new Object[]{"ID", "Type", "Brand", "Model", "Plate", "Price/Day", "Status"}, 0
        ) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        table = new JTable(tableModel);
        table.getTableHeader().setReorderingAllowed(false); // ✅ Prevent column dragging
        table.setFillsViewportHeight(true); // ✅ Fill viewport
        table.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS); // ✅ Auto-resize
        table.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        table.setRowHeight(35);
        table.setGridColor(new Color(189, 195, 199));
        table.setSelectionBackground(new Color(174, 214, 241));
        table.setSelectionForeground(TEXT_DARK);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        JTableHeader header = table.getTableHeader();
        header.setFont(new Font("Segoe UI", Font.BOLD, 13));
        header.setBackground(PRIMARY_COLOR);
        header.setForeground(Color.WHITE);
        header.setPreferredSize(new Dimension(0, 40));

        // ✅ Custom header renderer with bold, centered text
        DefaultTableCellRenderer headerRenderer = new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value,
                    boolean isSelected, boolean hasFocus, int row, int column) {
                super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                setFont(new Font("Segoe UI", Font.BOLD, 13));
                setBackground(PRIMARY_COLOR);
                setForeground(Color.WHITE);
                setHorizontalAlignment(JLabel.CENTER);
                setBorder(UIManager.getBorder("TableHeader.cellBorder"));
                return this;
            }
        };
        header.setDefaultRenderer(headerRenderer);

        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
        for (int i = 0; i < table.getColumnCount(); i++) {
            table.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
        }

        table.getColumnModel().getColumn(6).setCellRenderer(new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(
                    JTable table, Object value, boolean isSelected,
                    boolean hasFocus, int row, int column) {

                super.getTableCellRendererComponent(
                        table, value, isSelected, hasFocus, row, column);

                setHorizontalAlignment(JLabel.CENTER);

                boolean available = Boolean.TRUE.equals(value);

                if (available) {
                    setText("Available");
                    setForeground(SUCCESS_COLOR);
                } else {
                    setText("Rented");
                    setForeground(DANGER_COLOR);
                }

                if (isSelected) {
                    setForeground(Color.WHITE); // keep readable when selected
                }

                return this;
            }
        });
        
        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBorder(BorderFactory.createLineBorder(new Color(189, 195, 199)));

        tableContainer.add(headerPanel, BorderLayout.NORTH);
        tableContainer.add(scrollPane, BorderLayout.CENTER);

        return tableContainer;
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
            BorderFactory.createLineBorder(new Color(189, 195, 199)),
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
                    vehicle = new Car(0, brand, model, plate, price, true, seats);
                }
                case "BIKE" -> {
                    int cc = extraStr.isEmpty() ? 100 : Integer.parseInt(extraStr);
                    vehicle = new Bike(0, brand, model, plate, price, true, cc);
                }
                case "VAN" -> {
                    double capacity = extraStr.isEmpty() ? 1000 : Double.parseDouble(extraStr);
                    vehicle = new Van(0, brand, model, plate, price, true, capacity);
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
    }

    private void loadVehicles() {
        try {
            List<Vehicle> vehicles = vehicleDAO.getAllVehicles();
            tableModel.setRowCount(0);

            for (Vehicle v : vehicles) {
                tableModel.addRow(new Object[]{
                    v.getId(),
                    v.getType(),
                    v.getBrand(),
                    v.getModel(),
                    v.getPlateNumber(),
                    String.format("$%.2f", v.getPricePerDay()),
                    v.isAvailable()
                });
            }
        } catch (SQLException e) {
            showError("Error loading vehicles: " + e.getMessage());
        }
    }

    private void showError(String message) {
        JOptionPane.showMessageDialog(this, message, "Error", JOptionPane.ERROR_MESSAGE);
    }

    private void showSuccess(String message) {
        JOptionPane.showMessageDialog(this, message, "Success", JOptionPane.INFORMATION_MESSAGE);
    }
}