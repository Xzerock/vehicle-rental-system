package com.rental.gui;

import com.rental.gui.admin.AdminPanel;
import com.rental.gui.rental.RentalPanel;
import com.rental.gui.admin.AdminLoginDialog;

import javax.swing.*;
import java.awt.*;

public class MainFrame extends JFrame {

    private JPanel contentPanel;

    public MainFrame() {
        initUI();
    }

    private void initUI() {
        setTitle("Vehicle Rental System");
        setSize(800, 500);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        JLabel titleLabel = new JLabel("Vehicle Rental System", SwingConstants.CENTER);
        titleLabel.setFont(new Font("SansSerif", Font.BOLD, 22));
        add(titleLabel, BorderLayout.NORTH);

        // role buttons
        JPanel rolePanel = new JPanel(new GridLayout(2, 1, 20, 20));
        rolePanel.setBorder(BorderFactory.createEmptyBorder(80, 200, 80, 200));

        JButton btnAdmin = new JButton("Admin");
        JButton btnRental = new JButton("Rental");

        rolePanel.add(btnAdmin);
        rolePanel.add(btnRental);

        contentPanel = new JPanel(new BorderLayout());
        contentPanel.add(rolePanel, BorderLayout.CENTER);
        add(contentPanel, BorderLayout.CENTER);

        btnAdmin.addActionListener(e -> {
            AdminLoginDialog dialog = new AdminLoginDialog(this);
            dialog.setVisible(true);

            if (dialog.isAuthenticated()) {
                showAdminPanel();
            }
        });
        btnRental.addActionListener(e -> showRentalPanel());
    }

    private void showAdminPanel() {
        contentPanel.removeAll();
        contentPanel.add(new AdminPanel(this), BorderLayout.CENTER);
        refresh();
    }

    private void showRentalPanel() {
        contentPanel.removeAll();
        contentPanel.add(new RentalPanel(this), BorderLayout.CENTER);
        refresh();
    }

    public void showHome() {
        dispose();
        new MainFrame().setVisible(true);
    }

    private void refresh() {
        contentPanel.revalidate();
        contentPanel.repaint();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new MainFrame().setVisible(true));
    }
}
