package com.rental.gui.rental;

import com.rental.dao.RentalDAO;
import com.rental.model.Rental;
import com.rental.model.Customer;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.io.File;

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
    private static final Color BORDER_COLOR = new Color(189, 195, 199);
    
    private JPanel rentalListPanel;
    private Rental selectedRental;
    private JLabel lblSelectedInfo;
    
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
        add(createActionPanel(), BorderLayout.SOUTH);
        
        // Scrollable rental list
        rentalListPanel = new JPanel();
        rentalListPanel.setLayout(new BoxLayout(rentalListPanel, BoxLayout.Y_AXIS));
        rentalListPanel.setBackground(CONTENT_BG);
        
        JScrollPane scrollPane = new JScrollPane(rentalListPanel);
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
        
        JLabel titleLabel = new JLabel("Return Vehicle");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
        titleLabel.setForeground(TEXT_DARK);
        titleLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        JLabel subtitleLabel = new JLabel("Select an active rental to process return");
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
    
    private JPanel createActionPanel() {
        JPanel actionContainer = new JPanel(new BorderLayout());
        actionContainer.setBackground(CARD_BG);
        actionContainer.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(BORDER_COLOR, 1),
            new EmptyBorder(20, 20, 20, 20)
        ));
        
        JLabel actionTitle = new JLabel("Return Action");
        actionTitle.setFont(new Font("Segoe UI", Font.BOLD, 16));
        actionTitle.setForeground(TEXT_DARK);
        
        JPanel contentPanel = new JPanel(new BorderLayout(15, 15));
        contentPanel.setBackground(CARD_BG);
        
        lblSelectedInfo = new JLabel("No rental selected");
        lblSelectedInfo.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        lblSelectedInfo.setForeground(TEXT_LIGHT);
        
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        buttonPanel.setBackground(CARD_BG);
        
        JButton btnRefresh = createStyledButton("Refresh List", WARNING_COLOR);
        JButton btnReturn = createStyledButton("Process Return", DANGER_COLOR);
        
        btnRefresh.addActionListener(e -> loadActiveRentals());
        btnReturn.addActionListener(e -> returnVehicle());
        
        buttonPanel.add(btnRefresh);
        buttonPanel.add(btnReturn);
        
        contentPanel.add(lblSelectedInfo, BorderLayout.CENTER);
        contentPanel.add(buttonPanel, BorderLayout.EAST);
        
        JPanel topPanel = new JPanel(new BorderLayout(0, 15));
        topPanel.setBackground(CARD_BG);
        topPanel.add(actionTitle, BorderLayout.NORTH);
        topPanel.add(contentPanel, BorderLayout.CENTER);
        
        actionContainer.add(topPanel);
        return actionContainer;
    }
    
    private JPanel createRentalCard(Rental rental) {

        JPanel card = new JPanel(new BorderLayout(15, 0));
        card.setBackground(CARD_BG);
        card.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(BORDER_COLOR, 1),
            new EmptyBorder(15, 15, 15, 15)
        ));
        card.setMaximumSize(new Dimension(Integer.MAX_VALUE, 180));
        card.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        // Vehicle image panel
        JPanel imagePanel = new JPanel(new BorderLayout());
        imagePanel.setPreferredSize(new Dimension(240, 150));
        imagePanel.setBackground(new Color(245, 245, 245));
        imagePanel.setBorder(BorderFactory.createLineBorder(BORDER_COLOR));
        
        JLabel imageLabel = new JLabel();
        imageLabel.setHorizontalAlignment(JLabel.CENTER);
        imageLabel.setVerticalAlignment(JLabel.CENTER);
        
        // Load vehicle image
        ImageIcon vehicleImage = loadVehicleImage(rental.getVehicle().getImagePath());
        if (vehicleImage != null) {
            imageLabel.setIcon(vehicleImage);
        } else {
            // Fallback icon
            imageLabel.setText("🚗");
            imageLabel.setFont(new Font("Segoe UI", Font.PLAIN, 64));
            imageLabel.setForeground(TEXT_LIGHT);
        }
        
        imagePanel.add(imageLabel, BorderLayout.CENTER);
        
        // Info panel
        JPanel infoPanel = new JPanel();
        infoPanel.setLayout(new BoxLayout(infoPanel, BoxLayout.Y_AXIS));
        infoPanel.setBackground(CARD_BG);
        
        JLabel lblVehicle = new JLabel(rental.getVehicle().getBrand() + " " + rental.getVehicle().getModel());
        lblVehicle.setFont(new Font("Segoe UI", Font.BOLD, 18));
        lblVehicle.setForeground(TEXT_DARK);
        lblVehicle.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        JLabel lblCustomer = new JLabel("Customer: " + rental.getCustomer().getName());
        lblCustomer.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        lblCustomer.setForeground(TEXT_DARK);
        lblCustomer.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        JLabel lblRentalDate = new JLabel("Rented: " + rental.getRentalDate());
        lblRentalDate.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        lblRentalDate.setForeground(TEXT_LIGHT);
        lblRentalDate.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        // FIX: Calculate days rented correctly based on expected return date
        LocalDate rentalDate = rental.getRentalDate();
        LocalDate expectedReturnDate = rental.getExpectedReturnDate();
        
        long daysRented;
        if (expectedReturnDate != null) {
            // Calculate based on the rental period (start to expected end)
            daysRented = ChronoUnit.DAYS.between(rentalDate, expectedReturnDate);
            if (daysRented == 0) {
                daysRented = 1; // Minimum 1 day
            }
        } else {
            // Fallback: calculate from rental date to today
            LocalDate today = LocalDate.now();
            daysRented = ChronoUnit.DAYS.between(rentalDate, today);
            if (daysRented == 0) {
                daysRented = 1;
            }
        }
        
        JLabel lblDuration = new JLabel(String.format("Duration: %d day%s", daysRented, daysRented > 1 ? "s" : ""));
        lblDuration.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        lblDuration.setForeground(TEXT_LIGHT);
        lblDuration.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        // NEW: Add expected return date
        JLabel lblReturnDate = null;
        if (rental.getExpectedReturnDate() != null) {
            lblReturnDate = new JLabel("Due: " + rental.getExpectedReturnDate());
            lblReturnDate.setFont(new Font("Segoe UI", Font.PLAIN, 13));
            
            // Check if overdue
            LocalDate today = LocalDate.now();
            LocalDate returnDate = rental.getExpectedReturnDate();
            if (today.isAfter(returnDate)) {
                long daysOverdue = ChronoUnit.DAYS.between(returnDate, today);
                lblReturnDate.setText("Due: " + rental.getExpectedReturnDate() + " (Overdue by " + daysOverdue + " day" + (daysOverdue > 1 ? "s" : "") + ")");
                lblReturnDate.setForeground(DANGER_COLOR);
            } else if (today.isEqual(returnDate)) {
                lblReturnDate.setText("Due: " + rental.getExpectedReturnDate() + " (Today)");
                lblReturnDate.setForeground(WARNING_COLOR);
            } else {
                lblReturnDate.setForeground(INFO_COLOR);
            }
            
            lblReturnDate.setAlignmentX(Component.LEFT_ALIGNMENT);
        }
        
        JLabel lblPrice = new JLabel(String.format("Total: RM%.2f", rental.getTotalPrice()));
        lblPrice.setFont(new Font("Segoe UI", Font.BOLD, 16));
        lblPrice.setForeground(PRIMARY_COLOR);
        lblPrice.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        infoPanel.add(lblVehicle);
        infoPanel.add(Box.createRigidArea(new Dimension(0, 5)));
        infoPanel.add(lblCustomer);
        infoPanel.add(Box.createRigidArea(new Dimension(0, 3)));
        infoPanel.add(lblRentalDate);
        infoPanel.add(Box.createRigidArea(new Dimension(0, 3)));
        infoPanel.add(lblDuration);
        
        // Add return date if available
        if (lblReturnDate != null) {
            infoPanel.add(Box.createRigidArea(new Dimension(0, 3)));
            infoPanel.add(lblReturnDate);
        }
        
        infoPanel.add(Box.createVerticalGlue());
        infoPanel.add(lblPrice);
        
        // Status panel
        JPanel statusPanel = new JPanel(new BorderLayout());
        statusPanel.setBackground(CARD_BG);
        statusPanel.setPreferredSize(new Dimension(120, 0));

        JLabel lblStatus = new JLabel("Active");
        lblStatus.setFont(new Font("Segoe UI", Font.BOLD, 12));
        lblStatus.setForeground(WARNING_COLOR);
        lblStatus.setHorizontalAlignment(JLabel.CENTER);

        // Rental ID
        JLabel lblRentalIdStatus = new JLabel("Rental #" + rental.getRentalId());
        lblRentalIdStatus.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        lblRentalIdStatus.setForeground(TEXT_LIGHT);
        lblRentalIdStatus.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Vehicle ID
        JLabel lblVehicleIdStatus = new JLabel("Vehicle #" + rental.getVehicle().getId());
        lblVehicleIdStatus.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        lblVehicleIdStatus.setForeground(TEXT_LIGHT);
        lblVehicleIdStatus.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Add to status panel
        JPanel statusContent = new JPanel();
        statusContent.setLayout(new BoxLayout(statusContent, BoxLayout.Y_AXIS));
        statusContent.setBackground(CARD_BG);

        statusContent.add(lblStatus);
        statusContent.add(Box.createRigidArea(new Dimension(0, 5)));
        statusContent.add(lblRentalIdStatus);
        statusContent.add(Box.createRigidArea(new Dimension(0, 5)));
        statusContent.add(lblVehicleIdStatus);

        statusPanel.add(statusContent, BorderLayout.NORTH);
        
        card.add(imagePanel, BorderLayout.WEST);
        card.add(infoPanel, BorderLayout.CENTER);
        card.add(statusPanel, BorderLayout.EAST);
        
        // Click to select
        card.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                selectRental(rental, card);
            }
            
            @Override
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                if (selectedRental != rental) {
                    card.setBorder(BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(DANGER_COLOR, 2),
                        new EmptyBorder(14, 14, 14, 14)
                    ));
                }
            }
            
            @Override
            public void mouseExited(java.awt.event.MouseEvent evt) {
                if (selectedRental != rental) {
                    card.setBorder(BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(BORDER_COLOR, 1),
                        new EmptyBorder(15, 15, 15, 15)
                    ));
                }
            }
        });
        
        return card;
    }
    
    private void selectRental(Rental rental, JPanel card) {
        // Deselect previous
        for (Component comp : rentalListPanel.getComponents()) {
            if (comp instanceof JPanel && comp != card) {
                JPanel panel = (JPanel) comp;
                panel.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(BORDER_COLOR, 1),
                    new EmptyBorder(15, 15, 15, 15)
                ));
                panel.setBackground(CARD_BG);
            }
        }
        
        // Select current
        selectedRental = rental;
        card.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(DANGER_COLOR, 3),
            new EmptyBorder(14, 14, 14, 14)
        ));
        card.setBackground(new Color(255, 235, 235));
        
        lblSelectedInfo.setText(String.format("Selected: %s %s (Rental #%d)",
            rental.getVehicle().getBrand(),
            rental.getVehicle().getModel(),
            rental.getRentalId()));
        lblSelectedInfo.setForeground(TEXT_DARK);
    }
    
    private void loadActiveRentals() {
        try {
            List<Rental> rentals = rentalDAO.getActiveRentalsByCustomer(customer.getId());
            rentalListPanel.removeAll();
            selectedRental = null;
            lblSelectedInfo.setText("No rental selected");
            lblSelectedInfo.setForeground(TEXT_LIGHT);
            
            if (rentals.isEmpty()) {
                JLabel noRentalsLabel = new JLabel("No active rentals found");
                noRentalsLabel.setFont(new Font("Segoe UI", Font.ITALIC, 16));
                noRentalsLabel.setForeground(TEXT_LIGHT);
                noRentalsLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
                rentalListPanel.add(Box.createVerticalGlue());
                rentalListPanel.add(noRentalsLabel);
                rentalListPanel.add(Box.createVerticalGlue());
            } else {
                for (Rental rental : rentals) {
                    rentalListPanel.add(createRentalCard(rental));
                    rentalListPanel.add(Box.createRigidArea(new Dimension(0, 10)));
                }
            }
            
            rentalListPanel.revalidate();
            rentalListPanel.repaint();
            
        } catch (SQLException e) {
            showError("Error loading rentals: " + e.getMessage());
        }
    }
    
    private void returnVehicle() {
        if (selectedRental == null) {
            showError("Please select a rental to return.");
            return;
        }
        
        int confirm = JOptionPane.showConfirmDialog(
            this,
            String.format("Process return for:\n%s %s\nCustomer: %s\nRental ID: %d\n\nContinue?",
                selectedRental.getVehicle().getBrand(),
                selectedRental.getVehicle().getModel(),
                selectedRental.getCustomer().getName(),
                selectedRental.getRentalId()
            ),
            "Confirm Return",
            JOptionPane.YES_NO_OPTION,
            JOptionPane.QUESTION_MESSAGE
        );
        
        if (confirm != JOptionPane.YES_OPTION) {
            return;
        }
        
        try {
            rentalDAO.returnVehicle(selectedRental.getRentalId(), LocalDate.now());
            showSuccess("Vehicle returned successfully!");
            loadActiveRentals();
        } catch (SQLException e) {
            showError("Error returning vehicle: " + e.getMessage());
        }
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
    
    private ImageIcon loadVehicleImage(String imagePath) {
        if (imagePath == null || imagePath.isEmpty()) {
            return null;
        }

        try {
            // 1️⃣ Load from images folder (correct for your setup)
            File imageFile = new File("images/" + imagePath);
            if (imageFile.exists()) {
                ImageIcon icon = new ImageIcon(imageFile.getAbsolutePath());
                Image scaledImage = icon.getImage()
                        .getScaledInstance(240, 150, Image.SCALE_SMOOTH);
                return new ImageIcon(scaledImage);
            }

            // 2️⃣ Optional future-proof: classpath resource
            java.net.URL imgURL = getClass().getResource("/images/" + imagePath);
            if (imgURL != null) {
                ImageIcon icon = new ImageIcon(imgURL);
                Image scaledImage = icon.getImage()
                        .getScaledInstance(240, 150, Image.SCALE_SMOOTH);
                return new ImageIcon(scaledImage);
            }

            System.err.println("Image not found: " + imagePath);

        } catch (Exception e) {
            System.err.println("Failed to load image: " + imagePath);
            e.printStackTrace();
        }

        return null;
    }
}