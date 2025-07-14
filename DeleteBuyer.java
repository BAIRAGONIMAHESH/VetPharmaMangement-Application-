package loginapp;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class DeleteBuyer extends JFrame {

    private JTextField buyerIdField;
    private float opacity = 0.0f;

    public DeleteBuyer() {
        setTitle("Delete Buyer");
        setSize(700, 450);
        setUndecorated(true);
        setLocationRelativeTo(null);
        setLayout(null);
        setOpacity(opacity);

        // Top Title Bar with controls
        JPanel titleBar = new JPanel(null);
        titleBar.setBounds(0, 0, 700, 40);
        titleBar.setBackground(Color.DARK_GRAY);
        add(titleBar);

        JButton homeBtn = createTitleButton("Home");
        homeBtn.setBounds(5, 5, 60, 30);
        titleBar.add(homeBtn);
        homeBtn.addActionListener(e -> {
            new RoleAccess("admin", "admin");
            dispose();
        });

        JButton minBtn = createTitleButton("—");
        minBtn.setBounds(600, 5, 30, 30);
        titleBar.add(minBtn);
        minBtn.addActionListener(e -> setState(JFrame.ICONIFIED));

        JButton maxBtn = createTitleButton("☐");
        maxBtn.setBounds(630, 5, 30, 30);
        titleBar.add(maxBtn);
        maxBtn.addActionListener(e -> setExtendedState(getExtendedState() == JFrame.MAXIMIZED_BOTH ? JFrame.NORMAL : JFrame.MAXIMIZED_BOTH));

        JButton closeBtn = createTitleButton("X");
        closeBtn.setBounds(660, 5, 30, 30);
        closeBtn.setForeground(Color.RED);
        titleBar.add(closeBtn);
        closeBtn.addActionListener(e -> System.exit(0));

        // Animated Gradient Background
        AnimatedBackground bg = new AnimatedBackground();
        bg.setBounds(0, 0, 700, 450);
        bg.setLayout(null);
        add(bg);

        // Form panel
        JPanel panel = new JPanel(null);
        panel.setBounds(150, 100, 400, 200);
        panel.setOpaque(false);
        bg.add(panel);

        JLabel title = new JLabel("Delete Buyer", SwingConstants.CENTER);
        title.setFont(new Font("Segoe UI", Font.BOLD, 22));
        title.setForeground(Color.WHITE);
        title.setBounds(0, 40, 700, 30);
        bg.add(title);

        buyerIdField = new JTextField();
        buyerIdField.setBounds(50, 40, 300, 35);
        buyerIdField.setBorder(BorderFactory.createTitledBorder("Buyer ID"));
        panel.add(buyerIdField);

        JButton deleteBtn = createActionButton("Delete");
        deleteBtn.setBounds(50, 100, 90, 35);
        panel.add(deleteBtn);
        deleteBtn.addActionListener(e -> {
            // TODO: Delete from database using buyerIdField.getText()
            JOptionPane.showMessageDialog(this, "Buyer deleted successfully!");
        });

        JButton clearBtn = createActionButton("Clear");
        clearBtn.setBounds(155, 100, 90, 35);
        panel.add(clearBtn);
        clearBtn.addActionListener(e -> buyerIdField.setText(""));

        JButton resetBtn = createActionButton("Reset");
        resetBtn.setBounds(260, 100, 90, 35);
        panel.add(resetBtn);
        resetBtn.addActionListener(clearBtn.getActionListeners()[0]);

        fadeIn();
        setVisible(true);
    }

    private JButton createTitleButton(String text) {
        JButton btn = new JButton(text);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btn.setForeground(Color.WHITE);
        btn.setBackground(Color.BLACK);
        btn.setOpaque(true);

        btn.setBorder(BorderFactory.createEmptyBorder());
        btn.setContentAreaFilled(true);
        btn.setFocusPainted(false);
        btn.setFocusable(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return btn;
    }

    private JButton createActionButton(String text) {
        JButton btn = new JButton(text);
        btn.setBackground(new Color(0, 123, 255));
        btn.setForeground(Color.WHITE);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btn.setOpaque(true);

        btn.setBorder(BorderFactory.createEmptyBorder());
        btn.setContentAreaFilled(true);
        btn.setFocusPainted(false);
        btn.setFocusable(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
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

    class AnimatedBackground extends JPanel {
        private float hue = 0f;

        public AnimatedBackground() {
            Timer timer = new Timer(50, e -> {
                hue += 0.005f;
                if (hue >= 1.0f) hue = 0.0f;
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
        new DeleteBuyer();
    }
}
