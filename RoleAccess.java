package loginapp;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.List;
import java.util.ArrayList;

public class RoleAccess extends JFrame {

    private float opacity = 0.0f;
    private String username;
    private String role;

    public RoleAccess(String username, String role) {
        this.username = username;
        this.role = role;

        setTitle("Dashboard - " + role);
        setSize(900, 500);
        setUndecorated(true);
        setLocationRelativeTo(null);
        setOpacity(opacity);
       

        // Background with animated gradient
        AnimatedBackground bg = new AnimatedBackground();
        bg.setLayout(null);
        bg.setBounds(0, 0, 900, 500);
        add(bg); 
        JPanel topBar = new JPanel(null);
        topBar.setBounds(0, 0, 900, 40);
        topBar.setBackground(Color.DARK_GRAY);
        bg.add(topBar);
        JButton minBtn = createTitleButton("—");
        minBtn.setBounds(800, 5, 30, 30);
        minBtn.addActionListener(e -> setState(JFrame.ICONIFIED));
        topBar.add(minBtn);

        JButton maxBtn = createTitleButton("☐");
        maxBtn.setBounds(830, 5, 30, 30);
        maxBtn.addActionListener(e -> setExtendedState(getExtendedState() == JFrame.MAXIMIZED_BOTH ? JFrame.NORMAL : JFrame.MAXIMIZED_BOTH));
        topBar.add(maxBtn);

        JButton closeBtn = createTitleButton("X");
        closeBtn.setBounds(860, 5, 30, 30);
        closeBtn.setForeground(Color.RED);
        closeBtn.addActionListener(e -> System.exit(0));
        topBar.add(closeBtn);


        // Glass panel with buttons
        JPanel glassPanel = new JPanel(null) {
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g;
                g2.setColor(new Color(255, 255, 255, 120));
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 25, 25);
            }
        };
        glassPanel.setBounds(100, 80, 700, 330);
        glassPanel.setOpaque(false);
        bg.add(glassPanel);

        // Welcome label
        JLabel welcomeLabel = new JLabel("Welcome " + username + " (" + role + ")", SwingConstants.CENTER);
        welcomeLabel.setFont(new Font("Segoe UI", Font.BOLD, 22));
        welcomeLabel.setForeground(Color.BLACK);
        welcomeLabel.setBounds(0, 20, 700, 30);
        glassPanel.add(welcomeLabel);

        // Feature Buttons
        List<JButton> featureButtons = new ArrayList<>();
        featureButtons.add(createFeatureButton("📦 Inventory", e -> new InventoryPage()));
        featureButtons.add(createFeatureButton("📉 Low Stock Alert", e -> new LowStockAlert()));
        featureButtons.add(createFeatureButton("⏰ Expiry Alert", e -> new ExpiryAlert()));

        if (role.equalsIgnoreCase("admin")) {
            featureButtons.add(createFeatureButton("➕ Add Product", e -> new AddProduct()));
            featureButtons.add(createFeatureButton("➖ Delete Product", e -> new DeleteProduct()));
            featureButtons.add(createFeatureButton("➕ Add Buyer", e -> new AddBuyer()));
            featureButtons.add(createFeatureButton("➖ Delete Buyer", e -> new DeleteBuyer()));
            featureButtons.add(createFeatureButton("📤 Export Data", e -> new ExportData()));
            featureButtons.add(createFeatureButton("👤 Manage Users", e -> 
                JOptionPane.showMessageDialog(this, "Manage Users coming soon!")));
        }

        // Add buttons to glassPanel (2-column layout)
        int x = 50, y = 70;
        for (int i = 0; i < featureButtons.size(); i++) {
            JButton btn = featureButtons.get(i);
            btn.setBounds(x, y, 270, 40);
            glassPanel.add(btn);
            y += 50;
            if ((i + 1) % 5 == 0) {
                x += 300;
                y = 70;
            }
        }

        // Logout Button
        JButton logoutBtn = new JButton("Logout");
        logoutBtn.setBounds(580, 270, 100, 30);
        logoutBtn.setBackground(new Color(192, 57, 43));
        logoutBtn.setForeground(Color.WHITE);
        logoutBtn.setFont(new Font("Segoe UI", Font.BOLD, 14));
        logoutBtn.setFocusPainted(false);
        logoutBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        logoutBtn.addActionListener(e -> {
            dispose();
            new LoginForm();
        });
        glassPanel.add(logoutBtn);

        // Show window after components are loaded
        setVisible(true);
        fadeIn();
    }

    private JButton createFeatureButton(String text, ActionListener action) {
        JButton btn = new JButton(text);
        btn.setBackground(new Color(52, 152, 219));
        btn.setForeground(Color.WHITE);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btn.setFocusPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setBorder(BorderFactory.createEmptyBorder());
        btn.addActionListener(action);
        return btn;
    }
     // 🔹 Title Buttons: No focus/hover dot
    private JButton createTitleButton(String text) {
        JButton btn = new JButton(text);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btn.setForeground(Color.WHITE);
        btn.setBackground(Color.DARK_GRAY);
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setContentAreaFilled(true);
        btn.setOpaque(true);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setBorder(BorderFactory.createEmptyBorder());
        btn.setFocusable(false); // 💥 This removes dotted outline
        return btn;
    }


    private void fadeIn() {
        Timer timer = new Timer(20, null);
        timer.addActionListener(e -> {
            opacity += 0.05f;
            if (opacity >= 1.0f) {
                opacity = 1.0f;
                setOpacity(opacity);
                ((Timer) e.getSource()).stop();
            } else {
                setOpacity(opacity);
            }
        });
        timer.start();
    }

    // Background panel with animated gradient
    class AnimatedBackground extends JPanel {
        private float hue = 0.0f;

        public AnimatedBackground() {
            Timer timer = new Timer(40, e -> {
                hue += 0.003f;
                if (hue > 1.0f) hue = 0.0f;
                repaint();
            });
            timer.start();
        }

        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Color c1 = Color.getHSBColor(hue, 0.6f, 1.0f);
            Color c2 = Color.getHSBColor(hue + 0.2f, 0.6f, 1.0f);
            Graphics2D g2 = (Graphics2D) g;
            GradientPaint gp = new GradientPaint(0, 0, c1, getWidth(), getHeight(), c2);
            g2.setPaint(gp);
            g2.fillRect(0, 0, getWidth(), getHeight());
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new RoleAccess("mahesh", "admin"));
    }
}
