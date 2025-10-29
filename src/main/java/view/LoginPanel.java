package view;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

public class LoginPanel extends JPanel {
    private Image backgroundImage;

    public LoginPanel(CardLayout cardLayout, JPanel mainPanel) {
        setLayout(new GridBagLayout());

        // Carregar a imagem de fundo (substitua pelo nome exato do arquivo)
        backgroundImage = new ImageIcon("58204b00-4049-4544-8940-f4a454257fae.png").getImage();

        // Painel translúcido central
        JPanel loginBox = new JPanel();
        loginBox.setLayout(new GridLayout(5, 1, 10, 10));
        loginBox.setBackground(new Color(255, 255, 255, 45)); // levemente transparente
        loginBox.setBorder(BorderFactory.createEmptyBorder(20, 40, 20, 40));

        JLabel lblLogin = new JLabel("Login", SwingConstants.CENTER);
        lblLogin.setFont(new Font("SansSerif", Font.BOLD, 18));

        JTextField txtUser = new JTextField();
        txtUser.setBorder(BorderFactory.createTitledBorder("User Id"));

        JPasswordField txtPass = new JPasswordField();
        txtPass.setBorder(BorderFactory.createTitledBorder("Password"));

        JButton btnPublic = new JButton("Dados Públicos");
        JButton btnLogin = new JButton("Login");

        // Botão de login
        btnLogin.addActionListener((ActionEvent e) -> {
            String user = txtUser.getText().trim();
            String pass = new String(txtPass.getPassword());

            // --- Verificação simples de credenciais ---
            if (user.equals("admin") && pass.equals("123")) {
                abrirJanela(new AdminPanel(), "Administrador");
            } else if (user.equals("rec") && pass.equals("123")) {
                abrirJanela(new RecenseadorPanel(), "Recenseador");
            } else {
                JOptionPane.showMessageDialog(this, "Credenciais inválidas!", "Erro", JOptionPane.ERROR_MESSAGE);
            }

            txtUser.setText("");
            txtPass.setText("");
        });

        // Botão Dados Públicos
        btnPublic.addActionListener(e -> abrirJanela(new PublicDataPortal(), "Portal Público"));

        // Adicionar componentes
        loginBox.add(lblLogin);
        loginBox.add(txtUser);
        loginBox.add(txtPass);
        loginBox.add(btnPublic);
        loginBox.add(btnLogin);

        add(loginBox);
    }

    /** Método que abre a janela correspondente e fecha o login */
    private void abrirJanela(JPanel painel, String titulo) {
        JFrame frame = new JFrame("Sistema de Censo de Moçambique - " + titulo);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(1280, 800);
        frame.setLocationRelativeTo(null);
        frame.add(painel);
        frame.setVisible(true);

        // Fechar a janela de login
        SwingUtilities.getWindowAncestor(this).dispose();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (backgroundImage != null) {
            g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
        }
    }
}

