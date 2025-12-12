    package com.rental.gui;

    import javax.swing.*;
    import java.awt.*;
    import java.awt.geom.RoundRectangle2D;
    import java.io.File;

    public class SplashScreen extends JWindow {

        private static final Color PRIMARY_COLOR = new Color(41, 128, 185);
        private static final Color BACKGROUND = new Color(236, 240, 241);
        private static final Color TEXT_DARK = new Color(44, 62, 80);

        private JLabel lblLogo;
        private JLabel lblTitle;
        private JLabel lblSubtitle;
        private JLabel lblLoading;
        private JProgressBar progressBar;
        private float opacity = 0.0f;

        public SplashScreen() {
            initUI();
        }

        private void initUI() {
            setSize(600, 400);
            setLocationRelativeTo(null);

            JPanel mainPanel = new JPanel() {
                @Override
                protected void paintComponent(Graphics g) {
                    super.paintComponent(g);
                    Graphics2D g2d = (Graphics2D) g;
                    g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                    // Gradient background
                    GradientPaint gradient = new GradientPaint(
                        0, 0, PRIMARY_COLOR,
                        0, getHeight(), PRIMARY_COLOR.darker()
                    );
                    g2d.setPaint(gradient);
                    g2d.fillRect(0, 0, getWidth(), getHeight());
                }
            };
            mainPanel.setLayout(new GridBagLayout());

            GridBagConstraints gbc = new GridBagConstraints();
            gbc.gridx = 0;
            gbc.gridy = GridBagConstraints.RELATIVE;
            gbc.insets = new Insets(10, 0, 10, 0);

            // Try to load logo image, fallback to text
            lblLogo = new JLabel();
            lblLogo.setHorizontalAlignment(SwingConstants.CENTER);

            // Try loading image from resources or file
            try {
                ImageIcon logoIcon = loadLogoImage();
                if (logoIcon != null) {
                    lblLogo.setIcon(logoIcon);
                } else {
                    // Fallback to text logo
                    lblLogo.setText("AXON");
                    lblLogo.setFont(new Font("Segoe UI", Font.BOLD, 72));
                    lblLogo.setForeground(Color.WHITE);
                }
            } catch (Exception e) {
                // Fallback to text logo
                lblLogo.setText("AXON");
                lblLogo.setFont(new Font("Segoe UI", Font.BOLD, 72));
                lblLogo.setForeground(Color.WHITE);
            }

            mainPanel.add(lblLogo, gbc);

            // Title
            lblTitle = new JLabel("Vehicle Rental System");
            lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 28));
            lblTitle.setForeground(Color.WHITE);
            lblTitle.setHorizontalAlignment(SwingConstants.CENTER);
            gbc.insets = new Insets(20, 0, 5, 0);
            mainPanel.add(lblTitle, gbc);

            // Subtitle
            lblSubtitle = new JLabel("Professional Vehicle Management Solution");
            lblSubtitle.setFont(new Font("Segoe UI", Font.PLAIN, 14));
            lblSubtitle.setForeground(new Color(255, 255, 255, 200));
            lblSubtitle.setHorizontalAlignment(SwingConstants.CENTER);
            gbc.insets = new Insets(0, 0, 30, 0);
            mainPanel.add(lblSubtitle, gbc);

            // Progress bar
            progressBar = new JProgressBar();
            progressBar.setPreferredSize(new Dimension(400, 6));
            progressBar.setIndeterminate(true);
            progressBar.setForeground(Color.WHITE);
            progressBar.setBackground(new Color(255, 255, 255, 100));
            progressBar.setBorderPainted(false);
            gbc.insets = new Insets(20, 0, 5, 0);
            mainPanel.add(progressBar, gbc);

            // Loading text
            lblLoading = new JLabel("Loading...");
            lblLoading.setFont(new Font("Segoe UI", Font.PLAIN, 12));
            lblLoading.setForeground(new Color(255, 255, 255, 180));
            lblLoading.setHorizontalAlignment(SwingConstants.CENTER);
            gbc.insets = new Insets(5, 0, 20, 0);
            mainPanel.add(lblLoading, gbc);
            
            setContentPane(mainPanel);

            // Make window rounded (optional - works on some systems)
            try {
                setShape(new RoundRectangle2D.Double(0, 0, getWidth(), getHeight(), 20, 20));
            } catch (Exception e) {
                // Ignore if not supported
            }
        }

        private ImageIcon loadLogoImage() {
            // Try multiple paths for logo
            String[] possiblePaths = {
                "resources/logo.png",
                "logo.png",
                "images/logo.png",
                "assets/logo.png"
            };

            for (String path : possiblePaths) {
                File file = new File(path);
                if (file.exists()) {
                    ImageIcon icon = new ImageIcon(path);
                    // Scale to appropriate size
                    Image scaledImage = icon.getImage().getScaledInstance(150, 150, Image.SCALE_SMOOTH);
                    return new ImageIcon(scaledImage);
                }
            }

            // Try loading from resources
            try {
                java.net.URL imgURL = getClass().getResource("/logo.png");
                if (imgURL != null) {
                    ImageIcon icon = new ImageIcon(imgURL);
                    Image scaledImage = icon.getImage().getScaledInstance(150, 150, Image.SCALE_SMOOTH);
                    return new ImageIcon(scaledImage);
                }
            } catch (Exception e) {
                // Resource not found
            }

            return null;
        }

        public void showSplash() {
            setVisible(true);

            // Fade in animation
            Timer fadeInTimer = new Timer(20, null);
            fadeInTimer.addActionListener(e -> {
                opacity += 0.05f;
                if (opacity >= 1.0f) {
                    opacity = 1.0f;
                    fadeInTimer.stop();
                    // Start loading simulation after fade in
                    simulateLoading();
                }
                setOpacity(opacity);
            });
            fadeInTimer.start();
        }

        private void simulateLoading() {
            // Animate loading text
            Timer textTimer = new Timer(500, null);
            final int[] dotCount = {0};
            textTimer.addActionListener(e -> {
                dotCount[0] = (dotCount[0] + 1) % 4;
                String dots = ".".repeat(dotCount[0]);
                lblLoading.setText("Loading" + dots);
            });
            textTimer.start();

            // Wait 2.5 seconds then fade out
            Timer loadingTimer = new Timer(2500, e -> {
                textTimer.stop();
                // Fade out
                Timer fadeOutTimer = new Timer(20, null);
                fadeOutTimer.addActionListener(evt -> {
                    opacity -= 0.05f;
                    if (opacity <= 0.0f) {
                        opacity = 0.0f;
                        fadeOutTimer.stop();
                        dispose();
                        // Show main frame
                        SwingUtilities.invokeLater(() -> {
                            MainFrame mainFrame = new MainFrame();
                            mainFrame.setVisible(true);
                        });
                    }
                    setOpacity(opacity);
                });
                fadeOutTimer.start();
            });
            loadingTimer.setRepeats(false);
            loadingTimer.start();
        }
    }