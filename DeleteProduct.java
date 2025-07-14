package loginapp;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class DeleteProduct extends JFrame {

    private JTextField productIdField;
    private float opacity = 0.0f;

    public DeleteProduct() {
        setTitle("Delete Product");
        setSize(700, 450);
        setUndecorated(true);
        setLocationRelativeTo(null);
        setLayout(null);
        setOpacity(opacity);

        // === Top Bar with Buttons ===
        JPanel topBar = new JPanel(null);
        topBar.setBounds(0, 0, 700, 40);
        topBar.setBackground(Color.WHITE);
        add(topBar);

        // Home Button
        JButton homeBtn = createTopButton("Home", Color.BLACK, Color.WHITE);
        homeBtn.setBounds(5, 5, 60, 30);
        topBar.add(homeBtn);
        homeBtn.addActionListener(e -> {
            new RoleAccess("admin", "admin");
            dispose();
        });

        // Minimize Button
        JButton minBtn = createTopButton("—", Color.GRAY, Color.WHITE);
        minBtn.setBounds(610, 5, 30, 30);
        topBar.add(minBtn);
        minBtn.addActionListener(e -> setState(JFrame.ICONIFIED));

        // Maximize Button
        JButton maxBtn = createTopButton("☐", Color.GRAY, Color.WHITE);
        maxBtn.setBounds(640, 5, 30, 30);
        topBar.add(maxBtn);
        maxBtn.addActionListener(e -> {
            int state = getExtendedState();
            setExtendedState(state == JFrame.MAXIMIZED_BOTH ? JFrame.NORMAL : JFrame.MAXIMIZED_BOTH);
        });

        // Close Button
        JButton closeBtn = createTopButton("X", Color.RED, Color.WHITE);
        closeBtn.setBounds(670, 5, 30, 30);
        topBar.add(closeBtn);
        closeBtn.addActionListener(e -> System.exit(0));

        // === Animated Gradient Background ===
        AnimatedBackground bg = new AnimatedBackground();
        bg.setBounds(0, 0, 700, 450);
        bg.setLayout(null);
        add(bg);

        // === Panel ===
        JPanel panel = new JPanel(null);
        panel.setBounds(150, 100, 400, 200);
        panel.setOpaque(false);
        bg.add(panel);

        // === Title ===
        JLabel title = new JLabel("Delete Product", SwingConstants.CENTER);
        title.setFont(new Font("Segoe UI", Font.BOLD, 24));
        title.setForeground(Color.WHITE);
        title.setBounds(0, 40, 700, 30);
        bg.add(title);

        // === Product ID Field ===
        productIdField = new JTextField();
        productIdField.setBounds(50, 30, 300, 40);
        productIdField.setBorder(BorderFactory.createTitledBorder("Enter Product ID"));
        panel.add(productIdField);

        // === Delete Button ===
        JButton deleteBtn = createActionButton("Delete");
        deleteBtn.setBounds(50, 90, 90, 35);
        panel.add(deleteBtn);
        deleteBtn.addActionListener(e -> {
            // TODO: Delete from database
            JOptionPane.showMessageDialog(this, "Product deleted successfully!");
        });

        // === Clear Button ===
        JButton clearBtn = createActionButton("Clear");
        clearBtn.setBounds(155, 90, 90, 35);
        panel.add(clearBtn);
        clearBtn.addActionListener(e -> productIdField.setText(""));

        // === Reset Button ===
        JButton resetBtn = createActionButton("Reset");
        resetBtn.setBounds(260, 90, 90, 35);
        panel.add(resetBtn);
        resetBtn.addActionListener(clearBtn.getActionListeners()[0]);

        fadeIn();
        setVisible(true);
    }

    private JButton createActionButton(String text) {
        JButton btn = new JButton(text);
        btn.setBackground(new Color(0, 123, 255));
        btn.setForeground(Color.WHITE);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btn.setFocusPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return btn;
    }

    private JButton createTopButton(String text, Color bgColor, Color fgColor) {
    JButton btn = new JButton(text);
    btn.setFocusPainted(false);                  // No focus highlight
    btn.setContentAreaFilled(true);              // Use background color
    btn.setBorder(BorderFactory.createEmptyBorder()); // No border at all
    btn.setFont(new Font("Segoe UI", Font.BOLD, 12));
    btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
    btn.setBackground(bgColor);
    btn.setForeground(fgColor);
    btn.setOpaque(true);
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

    // === Gradient Background Panel ===
    class AnimatedBackground extends JPanel {
        private float hue = 0f;

        public AnimatedBackground() {
            Timer timer = new Timer(50, e -> {
                hue += 0.005f;
                if (hue > 1.0f) hue = 0f;
                repaint();
            });
            timer.start();
        }

        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Color c1 = Color.getHSBColor(hue, 0.6f, 1f);
            Color c2 = Color.getHSBColor(hue + 0.1f, 0.6f, 1f);
            Graphics2D g2d = (Graphics2D) g;
            GradientPaint gp = new GradientPaint(0, 0, c1, getWidth(), getHeight(), c2);
            g2d.setPaint(gp);
            g2d.fillRect(0, 0, getWidth(), getHeight());
        }
    }

    public static void main(String[] args) {
        new DeleteProduct();
    }
}
