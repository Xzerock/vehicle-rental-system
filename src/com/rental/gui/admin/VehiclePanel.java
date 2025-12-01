package com.rental.gui.admin;

import com.rental.dao.VehicleDAO;
import com.rental.model.*;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.SQLException;
import java.util.List;

public class VehiclePanel extends JPanel {

    private final VehicleDAO vehicleDAO = new VehicleDAO();

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
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // ---------- FORM ----------
        JPanel formPanel = new JPanel(new GridLayout(6, 2, 5, 5));

        formPanel.add(new JLabel("Type:"));
        cmbType = new JComboBox<>(new String[]{"CAR", "BIKE", "VAN"});
        formPanel.add(cmbType);

        formPanel.add(new JLabel("Brand:"));
        txtBrand = new JTextField();
        formPanel.add(txtBrand);

        formPanel.add(new JLabel("Model:"));
        txtModel = new JTextField();
        formPanel.add(txtModel);

        formPanel.add(new JLabel("Plate Number:"));
        txtPlate = new JTextField();
        formPanel.add(txtPlate);

        formPanel.add(new JLabel("Price per Day:"));
        txtPrice = new JTextField();
        formPanel.add(txtPrice);

        formPanel.add(new JLabel("Extra (Seats / CC / Capacity):"));
        txtExtra = new JTextField();
        formPanel.add(txtExtra);

        add(formPanel, BorderLayout.NORTH);

        // ---------- TABLE ----------
        tableModel = new DefaultTableModel(
                new Object[]{"ID", "Type", "Brand", "Model", "Plate", "Price", "Available"}, 0
        );
        table = new JTable(tableModel);
        add(new JScrollPane(table), BorderLayout.CENTER);

        // ---------- BUTTONS ----------
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton btnAdd = new JButton("Add Vehicle");
        JButton btnRefresh = new JButton("Refresh");

        buttonPanel.add(btnAdd);
        buttonPanel.add(btnRefresh);
        add(buttonPanel, BorderLayout.SOUTH);

        btnAdd.addActionListener(e -> addVehicle());
        btnRefresh.addActionListener(e -> loadVehicles());
    }

    private void addVehicle() {
        String type = (String) cmbType.getSelectedItem();
        String brand = txtBrand.getText().trim();
        String model = txtModel.getText().trim();
        String plate = txtPlate.getText().trim();
        String priceStr = txtPrice.getText().trim();
        String extraStr = txtExtra.getText().trim();

        if (brand.isEmpty() || model.isEmpty() || plate.isEmpty() || priceStr.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please fill in all required fields.");
            return;
        }

        double price;
        try {
            price = Double.parseDouble(priceStr);
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Price must be a number.");
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
            JOptionPane.showMessageDialog(this, "Extra must be a number.");
            return;
        }

        try {
            vehicleDAO.addVehicle(vehicle);
            JOptionPane.showMessageDialog(this, "Vehicle added.");
            clearForm();
            loadVehicles();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "DB Error: " + e.getMessage());
        }
    }

    private void clearForm() {
        txtBrand.setText("");
        txtModel.setText("");
        txtPlate.setText("");
        txtPrice.setText("");
        txtExtra.setText("");
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
                        v.getPricePerDay(),
                        v.isAvailable()
                });
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error loading vehicles.");
        }
    }
}
