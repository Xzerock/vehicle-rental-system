package com.rental.gui.rental;

import com.rental.dao.RentalDAO;
import com.rental.model.Rental;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

public class ReturnVehiclePanel extends JPanel {

    private final RentalDAO rentalDAO = new RentalDAO();

    private JTable table;
    private DefaultTableModel tableModel;

    public ReturnVehiclePanel() {
        initUI();
        loadActiveRentals();
    }

    private void initUI() {
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JLabel title = new JLabel("Return Vehicle", SwingConstants.CENTER);
        title.setFont(new Font("SansSerif", Font.BOLD, 16));
        add(title, BorderLayout.NORTH);

        // -------- TABLE --------
        tableModel = new DefaultTableModel(
                new Object[]{"Rental ID", "Customer", "Vehicle", "Start Date", "Total Price"}, 0
        );
        table = new JTable(tableModel);
        add(new JScrollPane(table), BorderLayout.CENTER);

        // -------- BUTTON --------
        JButton btnReturn = new JButton("Return Selected Vehicle");
        add(btnReturn, BorderLayout.SOUTH);

        btnReturn.addActionListener(e -> returnVehicle());
    }

    private void loadActiveRentals() {
        try {
            List<Rental> rentals = rentalDAO.getActiveRentals();
            tableModel.setRowCount(0);

            for (Rental r : rentals) {
                tableModel.addRow(new Object[]{
                        r.getRentalId(),
                        r.getCustomer().getName(),
                        r.getVehicle().getBrand() + " " + r.getVehicle().getModel(),
                        r.getRentalDate(),
                        r.getTotalPrice()
                });
            }
        } catch (SQLException e) {
            showError("Error loading rentals.");
        }
    }

    private void returnVehicle() {
        int row = table.getSelectedRow();

        if (row < 0) {
            showError("Please select a rental.");
            return;
        }

        int rentalId = (int) tableModel.getValueAt(row, 0);

        try {
            rentalDAO.returnVehicle(rentalId, LocalDate.now());
            JOptionPane.showMessageDialog(this, "Vehicle returned successfully!");
            loadActiveRentals();
        } catch (SQLException e) {
            showError("Error returning vehicle: " + e.getMessage());
        }
    }

    private void showError(String msg) {
        JOptionPane.showMessageDialog(this, msg,
                "Error", JOptionPane.ERROR_MESSAGE);
    }
}
