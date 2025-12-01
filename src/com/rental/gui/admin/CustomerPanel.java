package com.rental.gui.admin;

import com.rental.dao.CustomerDAO;
import com.rental.model.Customer;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.SQLException;
import java.util.List;

public class CustomerPanel extends JPanel {

    private final CustomerDAO customerDAO = new CustomerDAO();

    private JComboBox<String> cmbType;
    private JTextField txtName;
    private JTextField txtPhone;
    private JTextField txtEmail;

    private JTable table;
    private DefaultTableModel tableModel;

    public CustomerPanel() {
        initUI();
        loadCustomers();
    }

    private void initUI() {
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // -------- FORM --------
        JPanel formPanel = new JPanel(new GridLayout(4, 2, 5, 5));

        formPanel.add(new JLabel("Customer Type:"));
        cmbType = new JComboBox<>(new String[]{"REGULAR", "PREMIUM"});
        formPanel.add(cmbType);

        formPanel.add(new JLabel("Name:"));
        txtName = new JTextField();
        formPanel.add(txtName);

        formPanel.add(new JLabel("Phone:"));
        txtPhone = new JTextField();
        formPanel.add(txtPhone);

        formPanel.add(new JLabel("Email:"));
        txtEmail = new JTextField();
        formPanel.add(txtEmail);

        add(formPanel, BorderLayout.NORTH);

        // -------- TABLE --------
        tableModel = new DefaultTableModel(
                new Object[]{"ID", "Type", "Name", "Phone", "Email"}, 0
        );
        table = new JTable(tableModel);
        add(new JScrollPane(table), BorderLayout.CENTER);

        // -------- BUTTONS --------
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton btnAdd = new JButton("Add Customer");
        JButton btnRefresh = new JButton("Refresh");

        buttonPanel.add(btnAdd);
        buttonPanel.add(btnRefresh);
        add(buttonPanel, BorderLayout.SOUTH);

        btnAdd.addActionListener(e -> addCustomer());
        btnRefresh.addActionListener(e -> loadCustomers());
    }

    private void addCustomer() {
        String type = (String) cmbType.getSelectedItem();
        String name = txtName.getText().trim();
        String phone = txtPhone.getText().trim();
        String email = txtEmail.getText().trim();

        if (name.isEmpty() || phone.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "Name and phone are required.",
                    "Validation Error",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        Customer customer = new Customer(0, type, name, phone, email);

        try {
            customerDAO.addCustomer(customer);
            JOptionPane.showMessageDialog(this, "Customer added.");
            clearForm();
            loadCustomers();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this,
                    "DB Error: " + e.getMessage(),
                    "Database Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    private void clearForm() {
        txtName.setText("");
        txtPhone.setText("");
        txtEmail.setText("");
    }

    private void loadCustomers() {
        try {
            List<Customer> customers = customerDAO.getAllCustomers();
            tableModel.setRowCount(0);

            for (Customer c : customers) {
                tableModel.addRow(new Object[]{
                        c.getId(),
                        c.getType(),
                        c.getName(),
                        c.getPhone(),
                        c.getEmail()
                });
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this,
                    "Error loading customers.",
                    "Database Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }
}
