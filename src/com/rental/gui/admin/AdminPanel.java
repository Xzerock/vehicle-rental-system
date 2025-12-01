package com.rental.gui.admin;

import com.rental.gui.MainFrame;
import com.rental.gui.admin.VehiclePanel;

import javax.swing.*;
import java.awt.*;

public class AdminPanel extends JPanel {

    private final MainFrame mainFrame;

    public AdminPanel(MainFrame mainFrame) {
        this.mainFrame = mainFrame;
        initUI();
    }

    private void initUI() {
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JLabel title = new JLabel("Admin Panel", SwingConstants.CENTER);
        title.setFont(new Font("SansSerif", Font.BOLD, 18));
        add(title, BorderLayout.NORTH);

        JPanel menu = new JPanel(new GridLayout(3, 1, 10, 10));

        JButton btnVehicles = new JButton("Manage Vehicles");
        JButton btnCustomers = new JButton("Manage Customers");
        JButton btnBack = new JButton("Back");

        menu.add(btnVehicles);
        menu.add(btnCustomers);
        menu.add(btnBack);

        add(menu, BorderLayout.CENTER);

        btnVehicles.addActionListener(e -> showVehiclePanel());
        btnCustomers.addActionListener(e -> showCustomerPanel());
        btnBack.addActionListener(e -> mainFrame.showHome());
    }

    private void showVehiclePanel() {
        removeAll();
        setLayout(new BorderLayout());
        add(new VehiclePanel(), BorderLayout.CENTER);
        revalidate();
        repaint();
    }
    private void showCustomerPanel() {
        removeAll();
        setLayout(new BorderLayout());
        add(new CustomerPanel(), BorderLayout.CENTER);
        revalidate();
        repaint();
    }
}
