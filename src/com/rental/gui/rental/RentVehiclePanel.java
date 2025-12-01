package com.rental.gui.rental;

import com.rental.dao.CustomerDAO;
import com.rental.dao.VehicleDAO;
import com.rental.model.Customer;
import com.rental.model.Vehicle;
import com.rental.service.RentalService;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

public class RentVehiclePanel extends JPanel {

    private final CustomerDAO customerDAO = new CustomerDAO();
    private final VehicleDAO vehicleDAO = new VehicleDAO();
    private final RentalService rentalService = new RentalService();

    private JComboBox<Customer> cmbCustomer;
    private JTable vehicleTable;
    private DefaultTableModel tableModel;

    private JTextField txtStartDate;
    private JTextField txtEndDate;

    public RentVehiclePanel() {
        initUI();
        loadCustomers();
        loadVehicles();
    }

    private void initUI() {
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // -------- TOP: CUSTOMER + DATE --------
        JPanel topPanel = new JPanel(new GridLayout(3, 2, 5, 5));

        topPanel.add(new JLabel("Customer:"));
        cmbCustomer = new JComboBox<>();
        topPanel.add(cmbCustomer);

        topPanel.add(new JLabel("Start Date (YYYY-MM-DD):"));
        txtStartDate = new JTextField(LocalDate.now().toString());
        topPanel.add(txtStartDate);

        topPanel.add(new JLabel("End Date (YYYY-MM-DD):"));
        txtEndDate = new JTextField();
        topPanel.add(txtEndDate);

        add(topPanel, BorderLayout.NORTH);

        // -------- CENTER: VEHICLE TABLE --------
        tableModel = new DefaultTableModel(
                new Object[]{"ID", "Type", "Brand", "Model", "Plate", "Price"}, 0
        );
        vehicleTable = new JTable(tableModel);
        add(new JScrollPane(vehicleTable), BorderLayout.CENTER);

        // -------- BOTTOM: BUTTON --------
        JButton btnRent = new JButton("Rent Vehicle");
        add(btnRent, BorderLayout.SOUTH);

        btnRent.addActionListener(e -> rentVehicle());
    }

    private void loadCustomers() {
        try {
            List<Customer> customers = customerDAO.getAllCustomers();
            cmbCustomer.removeAllItems();
            for (Customer c : customers) {
                cmbCustomer.addItem(c);
            }
        } catch (SQLException e) {
            showError("Error loading customers.");
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
                        v.getPricePerDay()
                });
            }
        } catch (SQLException e) {
            showError("Error loading vehicles.");
        }
    }

    private void rentVehicle() {
        int row = vehicleTable.getSelectedRow();
        Customer customer = (Customer) cmbCustomer.getSelectedItem();

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

            JOptionPane.showMessageDialog(this, "Vehicle rented successfully!");
            loadVehicles();

        } catch (Exception e) {
            showError(e.getMessage());
        }
    }

    private void showError(String msg) {
        JOptionPane.showMessageDialog(this, msg,
                "Error", JOptionPane.ERROR_MESSAGE);
    }
}
