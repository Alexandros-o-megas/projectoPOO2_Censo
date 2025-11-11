package view;

import controller.LoginController;
import model.Login;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.geom.RoundRectangle2D;

public class LoginPanel extends JPanel {
    private Image backgroundImage;
    private LoginController loginController;
    private Login login;

    public LoginPanel() {
        setLayout(new GridBagLayout());
        loginController = new LoginController(); // instância do controller

       backgroundImage = new ImageIcon(getClass().getResource("/logiFund.png")).getImage();

        // Painel translúcido e arredondado
        RoundedPanel loginBox = new RoundedPanel(30);
        loginBox.setPreferredSize(new Dimension(300, 300));
        loginBox.setOpaque(false);
        loginBox.setLayout(new GridLayout(5, 1, 10, 10));
        loginBox.setBackground(new Color(255, 255, 255, 30));
        loginBox.setBorder(BorderFactory.createEmptyBorder(25, 50, 25, 50));

        JLabel lblLogin = new JLabel("Autenticação", SwingConstants.CENTER);
        lblLogin.setFont(new Font("SansSerif", Font.BOLD, 20));

        JTextField txtUser = new JTextField();
        txtUser.setBorder(BorderFactory.createTitledBorder("Username"));
        txtUser.setFont(new Font("SansSerif", Font.PLAIN, 14));

        JPasswordField txtPass = new JPasswordField();
        txtPass.setBorder(BorderFactory.createTitledBorder("Password"));
        txtPass.setFont(new Font("SansSerif", Font.PLAIN, 14));

        JButton btnPublic = criarBotaoArredondado("Dados Públicos");
        JButton btnLogin = criarBotaoArredondado("Login");
        txtUser.addActionListener(e -> btnLogin.doClick());
        txtPass.addActionListener(e -> btnLogin.doClick());

        btnLogin.addActionListener((ActionEvent e) -> {
            String username = txtUser.getText().trim();
            String password = new String(txtPass.getPassword());

            if (username.isEmpty() || password.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Preencha todos os campos!", "Aviso", JOptionPane.WARNING_MESSAGE);
                return;
            }

            // Usa o controller para autenticar
            login = loginController.autenticar(username, password);

            if (login != null) {
                String tipo = login.getUserType();
                if (tipo == null) tipo = "";

                switch (tipo.toLowerCase()) {
                    case "admin":
                        abrirJanela(new AdminPanel(), "Administrador");
                        break;
                    case "rec":
                        abrirJanela(new RecenseadorPanel(login), "Recenseador");
                        break;
                    case "pub":
                        abrirJanela(new PublicDataPortal(), "Portal Público");
                        break;
                    default:
                        JOptionPane.showMessageDialog(this, "Tipo de usuário desconhecido!", "Erro", JOptionPane.ERROR_MESSAGE);
                        break;
                }
            } else {
                JOptionPane.showMessageDialog(this, "Credenciais inválidas!", "Erro", JOptionPane.ERROR_MESSAGE);
            }

            txtUser.setText("");
            txtPass.setText("");
        });

        btnPublic.addActionListener(e -> abrirJanela(new PublicDataPortal(), "Portal Público"));

        loginBox.add(lblLogin);
        loginBox.add(txtUser);
        loginBox.add(txtPass);
        loginBox.add(btnPublic);
        loginBox.add(btnLogin);

        add(loginBox);
    }

    private void abrirJanela(JPanel painel, String titulo) {
        JFrame frame = new JFrame("Sistema de Censo de Moçambique - " + titulo);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(1280, 800);
        frame.setLocationRelativeTo(null);
        frame.add(painel);
        frame.setVisible(true);
        frame.setExtendedState(JFrame.MAXIMIZED_BOTH);

        SwingUtilities.getWindowAncestor(this).dispose();
    }

    private JButton criarBotaoArredondado(String texto) {
        JButton botao = new JButton(texto) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                // Fundo
                g2.setColor(getModel().isArmed() ? new Color(60, 120, 200) : new Color(70, 130, 230));
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 20, 20);

                // Texto
                g2.setColor(Color.WHITE);
                g2.setFont(getFont());
                FontMetrics fm = g2.getFontMetrics();
                int x = (getWidth() - fm.stringWidth(getText())) / 2;
                int y = (getHeight() + fm.getAscent()) / 2 - 3;
                g2.drawString(getText(), x, y);
                g2.dispose();
            }

            @Override
            protected void paintBorder(Graphics g) {
                // Sem borda
            }
        };
        botao.setPreferredSize(new Dimension(140, 35));
        botao.setFocusPainted(false);
        botao.setContentAreaFilled(false);
        botao.setFont(new Font("SansSerif", Font.BOLD, 14));
        return botao;
    }

    private static class RoundedPanel extends JPanel {
        private final int cornerRadius;

        public RoundedPanel(int radius) {
            super();
            this.cornerRadius = radius;
            setOpaque(false);
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            Shape clip = new RoundRectangle2D.Double(0, 0, getWidth(), getHeight(), cornerRadius, cornerRadius);
            g2.setColor(getBackground());
            g2.fill(clip);
            g2.dispose();
            super.paintComponent(g);
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (backgroundImage != null) {
            g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
        }
    }

}
