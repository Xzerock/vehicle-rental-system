package com.rental.gui.rental;

import com.rental.gui.MainFrame;

import javax.swing.*;
import java.awt.*;

public class RentalPanel extends JPanel {

    private final MainFrame mainFrame;

    public RentalPanel(MainFrame mainFrame) {
        this.mainFrame = mainFrame;
        initUI();
    }

    private void initUI() {
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JLabel title = new JLabel("Rental Panel", SwingConstants.CENTER);
        title.setFont(new Font("SansSerif", Font.BOLD, 18));
        add(title, BorderLayout.NORTH);

        JPanel menu = new JPanel(new GridLayout(3, 1, 10, 10));

        JButton btnRent = new JButton("Rent Vehicle");
        JButton btnReturn = new JButton("Return Vehicle");
        JButton btnBack = new JButton("Back");

        menu.add(btnRent);
        menu.add(btnReturn);
        menu.add(btnBack);

        add(menu, BorderLayout.CENTER);

        btnRent.addActionListener(e -> showRentPanel());
        btnReturn.addActionListener(e -> showReturnPanel());
        btnBack.addActionListener(e -> mainFrame.showHome());
    }
    private void showRentPanel() {
        removeAll();
        setLayout(new BorderLayout());
        add(new RentVehiclePanel(), BorderLayout.CENTER);
        revalidate();
        repaint();
    }
    private void showReturnPanel() {
        removeAll();
        setLayout(new BorderLayout());
        add(new ReturnVehiclePanel(), BorderLayout.CENTER);
        revalidate();
        repaint();
    }
}
