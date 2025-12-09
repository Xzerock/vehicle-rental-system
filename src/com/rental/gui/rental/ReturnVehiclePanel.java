package com.rental.gui.rental;

import com.rental.dao.RentalDAO;
import com.rental.model.Rental;
import com.rental.model.Customer;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

public class ReturnVehiclePanel extends JPanel {
    
    private final RentalDAO rentalDAO = new RentalDAO();
    private final Customer customer;
    
    // Modern colors
    private static final Color PRIMARY_COLOR = new Color(46, 204, 113);
    private static final Color SUCCESS_COLOR = new Color(39, 174, 96);
    private static final Color WARNING_COLOR = new Color(243, 156, 18);
    private static final Color DANGER_COLOR = new Color(231, 76, 60);
    private static final Color INFO_COLOR = new Color(52, 152, 219);
    private static final Color CONTENT_BG = new Color(236, 240, 241);
    private static final Color CARD_BG = Color.WHITE;
    private static final Color TEXT_DARK = new Color(44, 62, 80);
    private static final Color TEXT_LIGHT = new Color(127, 140, 141);
    
    private JTable table;
    private DefaultTableModel tableModel;
    private JLabel lblSelectedRental;
    
    public ReturnVehiclePanel(Customer customer) {
        this.customer = customer;
        initUI();
        loadActiveRentals();
    }
    
    private void initUI() {
        setLayout(new BorderLayout(15, 15));
        setBackground(CONTENT_BG);
        setBorder(new EmptyBorder(20, 20, 20, 20));
        
        add(createHeaderPanel(), BorderLayout.NORTH);
        
        JPanel mainPanel = new JPanel(new BorderLayout(15, 15));
        mainPanel.setBackground(CONTENT_BG);
        mainPanel.add(createInfoPanel(), BorderLayout.NORTH);
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
        
        JLabel titleLabel = new JLabel("Return Vehicle");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
        titleLabel.setForeground(TEXT_DARK);
        titleLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        JLabel subtitleLabel = new JLabel("Process vehicle returns and complete rentals");
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
    
    private JPanel createInfoPanel() {
        JPanel infoContainer = new JPanel(new BorderLayout());
        infoContainer.setBackground(INFO_COLOR);
        infoContainer.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(INFO_COLOR.darker(), 1),
            new EmptyBorder(15, 20, 15, 20)
        ));
        
        JLabel iconLabel = new JLabel("i");
        iconLabel.setFont(new Font("Segoe UI", Font.BOLD, 20));
        iconLabel.setForeground(Color.WHITE);
        iconLabel.setPreferredSize(new Dimension(30, 30));
        iconLabel.setHorizontalAlignment(SwingConstants.CENTER);
        
        JPanel textPanel = new JPanel();
        textPanel.setLayout(new BoxLayout(textPanel, BoxLayout.Y_AXIS));
        textPanel.setOpaque(false);
        
        JLabel infoTitle = new JLabel("Active Rentals");
        infoTitle.setFont(new Font("Segoe UI", Font.BOLD, 14));
        infoTitle.setForeground(Color.WHITE);
        
        lblSelectedRental = new JLabel("Select a rental from the table below to process return");
        lblSelectedRental.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        lblSelectedRental.setForeground(new Color(255, 255, 255, 230));
        
        textPanel.add(infoTitle);
        textPanel.add(Box.createRigidArea(new Dimension(0, 5)));
        textPanel.add(lblSelectedRental);
        
        infoContainer.add(iconLabel, BorderLayout.WEST);
        infoContainer.add(textPanel, BorderLayout.CENTER);
        
        return infoContainer;
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
        
        JLabel tableTitle = new JLabel("Current Active Rentals");
        tableTitle.setFont(new Font("Segoe UI", Font.BOLD, 16));
        tableTitle.setForeground(TEXT_DARK);
        
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        buttonPanel.setBackground(CARD_BG);
        
        JButton btnRefresh = createStyledButton("Refresh", WARNING_COLOR);
        JButton btnReturn = createStyledButton("Process Return", DANGER_COLOR);
        
        btnRefresh.addActionListener(e -> loadActiveRentals());
        btnReturn.addActionListener(e -> returnVehicle());
        
        buttonPanel.add(btnRefresh);
        buttonPanel.add(btnReturn);
        
        headerPanel.add(tableTitle, BorderLayout.WEST);
        headerPanel.add(buttonPanel, BorderLayout.EAST);
        
        tableModel = new DefaultTableModel(
            new Object[]{"Rental ID", "Customer", "Vehicle", "Start Date", "Total Price", "Status"}, 0
        ) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        
        table = new JTable(tableModel);
        table.getTableHeader().setReorderingAllowed(false); // ✅ FIX: Prevents column dragging
        table.setFillsViewportHeight(true); // ✅ FIX: Makes table fill viewport
        table.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS); // ✅ FIX: Auto-resize columns
        table.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        table.setRowHeight(35);
        table.setGridColor(new Color(189, 195, 199));
        table.setSelectionBackground(new Color(174, 214, 241));
        table.setSelectionForeground(TEXT_DARK);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        
        // Add selection listener
        table.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                updateSelectedRentalInfo();
            }
        });
        
        JTableHeader header = table.getTableHeader();
        header.setFont(new Font("Segoe UI", Font.BOLD, 13));
        header.setBackground(Color.CYAN); // ✅ Changed to cyan
        header.setForeground(Color.BLACK);
        header.setPreferredSize(new Dimension(0, 40));
        header.setOpaque(true);

        DefaultTableCellRenderer headerRenderer = new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value,
                    boolean isSelected, boolean hasFocus, int row, int column) {
                super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                setFont(new Font("Segoe UI", Font.BOLD, 13));
                setBackground(Color.CYAN);
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
        
        // Custom renderer for status column
        table.getColumnModel().getColumn(5).setCellRenderer(new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value,
                    boolean isSelected, boolean hasFocus, int row, int column) {
                Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                setHorizontalAlignment(JLabel.CENTER);
                
                if (!isSelected) {
                    c.setForeground(WARNING_COLOR);
                    setFont(new Font("Segoe UI", Font.BOLD, 13));
                    setText("Active");
                }
                return c;
            }
        });
        
        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBorder(BorderFactory.createLineBorder(new Color(189, 195, 199)));
        scrollPane.setPreferredSize(new Dimension(800, 400));
        
        tableContainer.add(headerPanel, BorderLayout.NORTH);
        tableContainer.add(scrollPane, BorderLayout.CENTER);
        
        return tableContainer;
    }
    
    private JButton createStyledButton(String text, Color bgColor) {
        JButton button = new JButton(text);
        button.setFont(new Font("Segoe UI", Font.BOLD, 13));
        button.setForeground(Color.WHITE);
        button.setBackground(bgColor);
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setPreferredSize(new Dimension(140, 35));
        
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
    
    private void updateSelectedRentalInfo() {
        int row = table.getSelectedRow();
        if (row >= 0) {
            String customer = tableModel.getValueAt(row, 1).toString();
            String vehicle = tableModel.getValueAt(row, 2).toString();
            lblSelectedRental.setText("Selected: " + customer + " - " + vehicle);
        } else {
            lblSelectedRental.setText("Select a rental from the table below to process return");
        }
    }
    
    private void loadActiveRentals() {
        try {
            List<Rental> rentals =
                rentalDAO.getActiveRentalsByCustomer(customer.getId());
            tableModel.setRowCount(0);
            
            for (Rental r : rentals) {
                tableModel.addRow(new Object[]{
                    r.getRentalId(),
                    r.getCustomer().getName(),
                    r.getVehicle().getBrand() + " " + r.getVehicle().getModel(),
                    r.getRentalDate(),
                    String.format("$%.2f", r.getTotalPrice()),
                    "Active"
                });
            }
            
            if (rentals.isEmpty()) {
                lblSelectedRental.setText("No active rentals found");
            }
        } catch (SQLException e) {
            showError("Error loading rentals: " + e.getMessage());
        }
    }
    
    private void returnVehicle() {
        int row = table.getSelectedRow();
        if (row < 0) {
            showError("Please select a rental to return.");
            return;
        }
        
        int rentalId = (int) tableModel.getValueAt(row, 0);
        String customer = tableModel.getValueAt(row, 1).toString();
        String vehicle = tableModel.getValueAt(row, 2).toString();
        
        int confirm = JOptionPane.showConfirmDialog(
            this,
            "Process return for:\n" + customer + "\n" + vehicle + "\n\nContinue?",
            "Confirm Return",
            JOptionPane.YES_NO_OPTION,
            JOptionPane.QUESTION_MESSAGE
        );
        
        if (confirm != JOptionPane.YES_OPTION) {
            return;
        }
        
        try {
            rentalDAO.returnVehicle(rentalId, LocalDate.now());
            showSuccess("Vehicle returned successfully!");
            loadActiveRentals();
            table.clearSelection();
        } catch (SQLException e) {
            showError("Error returning vehicle: " + e.getMessage());
        }
    }
    
    private void showError(String msg) {
        JOptionPane.showMessageDialog(this, msg, "Error", JOptionPane.ERROR_MESSAGE);
    }
    
    private void showSuccess(String msg) {
        JOptionPane.showMessageDialog(this, msg, "Success", JOptionPane.INFORMATION_MESSAGE);
    }
}