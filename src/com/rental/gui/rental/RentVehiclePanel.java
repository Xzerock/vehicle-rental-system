package com.rental.gui.rental;

import com.rental.dao.CustomerDAO;
import com.rental.dao.VehicleDAO;
import com.rental.model.Customer;
import com.rental.model.Vehicle;
import com.rental.service.RentalService;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;
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

    private JTable vehicleTable;
    private DefaultTableModel tableModel;
    private JTextField txtStartDate;
    private JTextField txtEndDate;
    private JLabel lblCalculatedPrice;

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
        JButton btnBack = new JButton("Back");
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
            BorderFactory.createLineBorder(new Color(189, 195, 199), 1),
            new EmptyBorder(20, 20, 20, 20)
        ));

        JLabel formTitle = new JLabel("Rental Information");
        formTitle.setFont(new Font("Segoe UI", Font.BOLD, 16));
        formTitle.setForeground(TEXT_DARK);
        formTitle.setBorder(new EmptyBorder(0, 0, 15, 0));

        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBackground(CARD_BG);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 5, 5, 5);
        
        // Row 1 – Logged-in Customer (READ ONLY)
        gbc.gridx = 0; gbc.gridy = 0; gbc.weightx = 0;
        formPanel.add(createLabel("Customer:"), gbc);

        gbc.gridx = 1; gbc.weightx = 1;
        JLabel lblCustomer = new JLabel(
                customer.getName() + " (" + customer.getPhone() + ")"
        );
        lblCustomer.setFont(new Font("Segoe UI", Font.BOLD, 14));
        lblCustomer.setForeground(TEXT_DARK);
        formPanel.add(lblCustomer, gbc);

        gbc.gridx = 2; gbc.weightx = 0;
        formPanel.add(createLabel("Start Date (YYYY-MM-DD):"), gbc);
        
        gbc.gridx = 3; gbc.weightx = 1;
        txtStartDate = createStyledTextField();
        txtStartDate.setText(LocalDate.now().toString());
        formPanel.add(txtStartDate, gbc);

        // Row 2
        gbc.gridx = 0; gbc.gridy = 1; gbc.weightx = 0;
        formPanel.add(createLabel("End Date (YYYY-MM-DD):"), gbc);
        
        gbc.gridx = 1; gbc.weightx = 1;
        txtEndDate = createStyledTextField();
        txtEndDate.setText(LocalDate.now().plusDays(1).toString());
        formPanel.add(txtEndDate, gbc);

        gbc.gridx = 2; gbc.weightx = 0;
        formPanel.add(createLabel("Estimated Total:"), gbc);
        
        gbc.gridx = 3; gbc.weightx = 1;
        lblCalculatedPrice = new JLabel("$0.00");
        lblCalculatedPrice.setFont(new Font("Segoe UI", Font.BOLD, 14));
        lblCalculatedPrice.setForeground(PRIMARY_COLOR);
        formPanel.add(lblCalculatedPrice, gbc);

        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setBackground(CARD_BG);
        topPanel.add(formTitle, BorderLayout.NORTH);
        topPanel.add(formPanel, BorderLayout.CENTER);

        formContainer.add(topPanel, BorderLayout.CENTER);

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

        JLabel tableTitle = new JLabel("Available Vehicles");
        tableTitle.setFont(new Font("Segoe UI", Font.BOLD, 16));
        tableTitle.setForeground(TEXT_DARK);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        buttonPanel.setBackground(CARD_BG);

        JButton btnRefresh = createStyledButton("Refresh", WARNING_COLOR);
        JButton btnRent = createStyledButton("Rent Selected", SUCCESS_COLOR);

        btnRefresh.addActionListener(e -> loadVehicles());
        btnRent.addActionListener(e -> rentVehicle());

        buttonPanel.add(btnRefresh);
        buttonPanel.add(btnRent);

        headerPanel.add(tableTitle, BorderLayout.WEST);
        headerPanel.add(buttonPanel, BorderLayout.EAST);

        tableModel = new DefaultTableModel(
            new Object[]{"ID", "Type", "Brand", "Model", "Plate", "Price/Day"}, 0
        ) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        
        vehicleTable = new JTable(tableModel);
        vehicleTable.getTableHeader().setReorderingAllowed(false); // ✅ Prevent column dragging
        vehicleTable.setFillsViewportHeight(true); // ✅ Fill viewport
        vehicleTable.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS); // ✅ Auto-resize
        vehicleTable.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        vehicleTable.setRowHeight(35);
        vehicleTable.setGridColor(new Color(189, 195, 199));
        vehicleTable.setSelectionBackground(new Color(174, 214, 241));
        vehicleTable.setSelectionForeground(TEXT_DARK);
        vehicleTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        // Add selection listener to calculate price
        vehicleTable.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                calculatePrice();
            }
        });

        JTableHeader header = vehicleTable.getTableHeader();
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
        for (int i = 0; i < vehicleTable.getColumnCount(); i++) {
            vehicleTable.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
        }
        JScrollPane scrollPane = new JScrollPane(vehicleTable);
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

    private void calculatePrice() {
        int row = vehicleTable.getSelectedRow();
        if (row < 0) {
            lblCalculatedPrice.setText("$0.00");
            return;
        }

        try {
            String priceStr = tableModel.getValueAt(row, 5).toString();
            double pricePerDay = Double.parseDouble(priceStr.replace("$", ""));
            
            LocalDate start = LocalDate.parse(txtStartDate.getText().trim());
            LocalDate end = LocalDate.parse(txtEndDate.getText().trim());
            
            long days = java.time.temporal.ChronoUnit.DAYS.between(start, end);
            if (days <= 0) days = 1;
            
            double total = pricePerDay * days;
            lblCalculatedPrice.setText(String.format("$%.2f (%d days)", total, days));
        } catch (Exception e) {
            lblCalculatedPrice.setText("Invalid dates");
        }
    }

    private void loadVehicles() {
        try {
            List<Vehicle> vehicles = vehicleDAO.getAvailableVehicles();
            tableModel.setRowCount(0);

            for (Vehicle v : vehicles) {
                tableModel.addRow(new Object[]{
                    v.getId(),
                    v.getType(),
                    v.getBrand(),
                    v.getModel(),
                    v.getPlateNumber(),
                    String.format("$%.2f", v.getPricePerDay())
                });
            }
        } catch (SQLException e) {
            showError("Error loading vehicles: " + e.getMessage());
        }
    }

    private void rentVehicle() {
        int row = vehicleTable.getSelectedRow();
        Customer customer = this.customer;

        if (customer == null || row < 0) {
            showError("Please select a customer and a vehicle.");
            return;
        }

        try {
            int vehicleId = (int) tableModel.getValueAt(row, 0);
            Vehicle vehicle = vehicleDAO.getAllVehicles()
                    .stream()
                    .filter(v -> v.getId() == vehicleId)
                    .findFirst()
                    .orElse(null);

            if (vehicle == null) {
                showError("Vehicle not found.");
                return;
            }

            LocalDate start = LocalDate.parse(txtStartDate.getText().trim());
            LocalDate end = LocalDate.parse(txtEndDate.getText().trim());

            rentalService.rentVehicle(customer, vehicle, start, end);

            showSuccess("Vehicle rented successfully!");
            loadVehicles();
            vehicleTable.clearSelection();
            lblCalculatedPrice.setText("$0.00");

        } catch (Exception e) {
            showError(e.getMessage());
        }
    }

    private void showError(String msg) {
        JOptionPane.showMessageDialog(this, msg, "Error", JOptionPane.ERROR_MESSAGE);
    }

    private void showSuccess(String msg) {
        JOptionPane.showMessageDialog(this, msg, "Success", JOptionPane.INFORMATION_MESSAGE);
    }
}