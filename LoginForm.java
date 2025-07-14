package loginapp;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class LoginForm extends JFrame {

    private JTextField usernameField;
    private JPasswordField passwordField;
    private float opacity = 0.0f;
    private boolean maximized = false;
    private Point clickPoint = new Point();

    public LoginForm() {
        setTitle("Veterinary Pharmacy - Login");
        setSize(800, 500);
        setUndecorated(true);
        setLocationRelativeTo(null);
        setLayout(null);
        setOpacity(opacity);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        // 🔷 Custom Title Bar
        JPanel titleBar = new JPanel(null);
        titleBar.setBounds(0, 0, 800, 30);
        titleBar.setBackground(new Color(0, 0, 0, 100));
        add(titleBar);

//        JButton homeBtn = createTitleButton("🏠");
//        homeBtn.setBounds(700, 0, 30, 30);
//        homeBtn.addActionListener(e -> {
//            new RoleAccess("admin", "admin");
//            dispose();
//        });
//        titleBar.add(homeBtn);

        JButton minBtn = createTitleButton("—");
        minBtn.setBounds(730, 0, 30, 30);
        minBtn.addActionListener(e -> setState(JFrame.ICONIFIED));
        titleBar.add(minBtn);

        JButton maxBtn = createTitleButton("▢");
        maxBtn.setBounds(760, 0, 30, 30);
        maxBtn.addActionListener(e -> {
            if (maximized) {
                setExtendedState(JFrame.NORMAL);
                maximized = false;
            } else {
                setExtendedState(JFrame.MAXIMIZED_BOTH);
                maximized = true;
            }
        });
        titleBar.add(maxBtn);

        JButton closeBtn = createTitleButton("X");
        closeBtn.setBounds(790, 0, 30, 30);
        closeBtn.setForeground(Color.RED);
        closeBtn.addActionListener(e -> System.exit(0));
        titleBar.add(closeBtn);

        // 🔁 Make title bar draggable
        titleBar.addMouseListener(new MouseAdapter() {
            public void mousePressed(MouseEvent e) {
                clickPoint = e.getPoint();
            }
        });
        titleBar.addMouseMotionListener(new MouseMotionAdapter() {
            public void mouseDragged(MouseEvent e) {
                Point location = getLocation();
                int x = location.x + e.getX() - clickPoint.x;
                int y = location.y + e.getY() - clickPoint.y;
                setLocation(x, y);
            }
        });

        // 🔷 Background Panel
        AnimatedBackgroundPanel background = new AnimatedBackgroundPanel();
        background.setLayout(null);
        background.setBounds(0, 0, 800, 500);
        add(background);

        // 🔷 Glass Login Panel
        JPanel loginPanel = new JPanel(null) {
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g;
                g2.setColor(new Color(255, 255, 255, 90));
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 30, 30);
                super.paintComponent(g);
            }
        };
        loginPanel.setBounds(230, 130, 340, 250);
        loginPanel.setOpaque(false);
        background.add(loginPanel);

        JLabel title = new JLabel("Login", SwingConstants.CENTER);
        title.setFont(new Font("Segoe UI", Font.BOLD, 24));
        title.setForeground(Color.BLACK);
        title.setBounds(0, 10, 340, 40);
        loginPanel.add(title);

        usernameField = new JTextField();
        usernameField.setBounds(40, 70, 260, 40);
        usernameField.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        usernameField.setBorder(BorderFactory.createTitledBorder("Username"));
        loginPanel.add(usernameField);

        passwordField = new JPasswordField();
        passwordField.setBounds(40, 130, 260, 40);
        passwordField.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        passwordField.setBorder(BorderFactory.createTitledBorder("Password"));
        loginPanel.add(passwordField);

        JButton loginBtn = new JButton("Login");
        loginBtn.setBounds(100, 190, 140, 40);
        styleButton(loginBtn);
        loginBtn.addActionListener(e -> login());
        loginPanel.add(loginBtn);

        fadeIn();
        setVisible(true);
    }

    private void login() {
        String username = usernameField.getText().trim();
        String password = new String(passwordField.getPassword());

        if (username.equals("admin") && password.equals("admin123")) {
            new RoleAccess(username, "admin");
            dispose();
        } else if (username.equals("staff") && password.equals("user123")) {
            new RoleAccess(username, "user");
            dispose();
        } else {
            JOptionPane.showMessageDialog(this, "Invalid credentials", "Login Failed", JOptionPane.ERROR_MESSAGE);
        }
    }

    private JButton createTitleButton(String text) {
        JButton btn = new JButton(text);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btn.setForeground(Color.WHITE);
        btn.setBackground(Color.BLACK);
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setContentAreaFilled(true);
        btn.setOpaque(true);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setBorder(BorderFactory.createEmptyBorder());
        btn.setFocusable(false); // no dotted focus

        btn.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                if (btn.getText().equals("X")) {
                    btn.setBackground(new Color(180, 0, 0));
                } else {
                    btn.setBackground(new Color(30, 30, 30));
                }
            }

            public void mouseExited(MouseEvent e) {
                btn.setBackground(Color.BLACK);
            }
        });

        return btn;
    }

    private void styleButton(JButton button) {
        button.setBackground(new Color(0, 123, 255));
        button.setForeground(Color.WHITE);
        button.setFont(new Font("Segoe UI", Font.BOLD, 15));
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createEmptyBorder());
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));

        button.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                button.setBackground(new Color(0, 100, 220));
            }

            public void mouseExited(MouseEvent e) {
                button.setBackground(new Color(0, 123, 255));
            }
        });
    }

    private void fadeIn() {
        Timer timer = new Timer(20, null);
        timer.addActionListener(e -> {
            opacity += 0.05f;
            if (opacity >= 1.0f) {
                setOpacity(1.0f);
                ((Timer) e.getSource()).stop();
            } else {
                setOpacity(opacity);
            }
        });
        timer.start();
    }

    // 🔷 Background Gradient Animation
    class AnimatedBackgroundPanel extends JPanel {
        private float hue = 0.0f;

        public AnimatedBackgroundPanel() {
            Timer timer = new Timer(40, e -> {
                hue += 0.005f;
                if (hue > 1.0f) hue = 0.0f;
                repaint();
            });
            timer.start();
        }

        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Color c1 = Color.getHSBColor(hue, 0.6f, 1f);
            Color c2 = Color.getHSBColor(hue + 0.2f, 0.6f, 1f);
            Graphics2D g2d = (Graphics2D) g;
            GradientPaint gp = new GradientPaint(0, 0, c1, getWidth(), getHeight(), c2);
            g2d.setPaint(gp);
            g2d.fillRect(0, 0, getWidth(), getHeight());
        }
    }

    public static void main(String[] args) {
        new LoginForm();
    }
}
