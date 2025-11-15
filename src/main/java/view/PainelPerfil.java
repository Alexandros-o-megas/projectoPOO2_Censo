package view;

import controller.*;
import model.Login;
import model.Recenseador;
import service.Utilitarios;

import javax.swing.*;
import java.awt.*;

public class PainelPerfil extends JPanel {
    private FamiliaController familiaController = new FamiliaController();
    private LoginController loginController = new LoginController();
    Login loginActual;

    public PainelPerfil(Recenseador r) {
        loginActual = loginController.buscarPorNome(Utilitarios.desnormalizarNome(r.getNome()));
        setLayout(new BorderLayout());
        setBackground(new Color(0x2B2B2B));
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        //-----------------------------------------
        // TÍTULO
        //-----------------------------------------
        JLabel titulo = new JLabel("Meu Perfil");
        titulo.setFont(new Font("SansSerif", Font.BOLD, 22));
        titulo.setForeground(new Color(0x333333));
        add(titulo, BorderLayout.NORTH);

        //-----------------------------------------
        // PAINEL CENTRAL (FOTO + INFO)
        //-----------------------------------------
        JPanel centro = new JPanel(new BorderLayout(15, 0));
        centro.setBackground(Color.WHITE);
        centro.setBorder(BorderFactory.createTitledBorder("Minhas Informações"));

        //-----------------------------------------
        // FOTO DO RECENSEADOR
        //-----------------------------------------
        JPanel fotoPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        fotoPanel.setBackground(Color.WHITE);

        ImageIcon icon = carregarFoto(r.getIdRecenseador());
        JLabel fotoLabel = new JLabel(icon);
        fotoLabel.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY, 1));
        fotoPanel.add(fotoLabel);

        centro.add(fotoPanel, BorderLayout.WEST);

        //-----------------------------------------
        // INFORMAÇÕES DO RECENSEADOR
        //-----------------------------------------
        JPanel infoPanel = new JPanel(new GridLayout(5, 1, 5, 5));
        infoPanel.setBackground(Color.WHITE);

        JLabel lblNome = new JLabel("Nome: " + r.getNome());
        JLabel lblId = new JLabel("ID de Recenseador: " + r.getIdRecenseador());
        JLabel lblBairro = new JLabel("Bairro de Atuação: Maputo"); //+ r.getBairro().getNome());
        JLabel lblTotal = new JLabel("Famílias Registadas: " + familiaController.numeroFamiliaPorRecenseador(r.getIdRecenseador()));

        lblNome.setFont(new Font("SansSerif", Font.PLAIN, 16));
        lblId.setFont(new Font("SansSerif", Font.PLAIN, 16));
        lblBairro.setFont(new Font("SansSerif", Font.PLAIN, 16));
        lblTotal.setFont(new Font("SansSerif", Font.PLAIN, 16));

        infoPanel.add(lblNome);
        infoPanel.add(lblId);
        infoPanel.add(lblBairro);
        infoPanel.add(lblTotal);

        centro.add(infoPanel, BorderLayout.CENTER);

        add(centro, BorderLayout.CENTER);

        //-----------------------------------------
        // PAINEL DE ALTERAR PASSWORD
        //-----------------------------------------
        add(criarPainelAlterarPassword(r), BorderLayout.SOUTH);
    }


    // ===========================================================
    //  MÉTODO PARA CARREGAR A FOTO DO RECENSEADOR
    // ===========================================================
    private ImageIcon carregarFoto(int id) {
        String path = "fotos/recenseadores/" + id + ".png";

        try {
            ImageIcon ic = new ImageIcon(path);
            Image img = ic.getImage().getScaledInstance(130, 130, Image.SCALE_SMOOTH);
            return new ImageIcon(img);
        } catch (Exception e) {
            ImageIcon ic = new ImageIcon("assets/avatar_default.png");
            Image img = ic.getImage().getScaledInstance(130, 130, Image.SCALE_SMOOTH);
            return new ImageIcon(img);
        }
    }


    // ===========================================================
    //  PAINEL DE ALTERAR PASSWORD
    // ===========================================================
    private JPanel criarPainelAlterarPassword(Recenseador r) {

        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(4, 2, 10, 10));
        panel.setBorder(BorderFactory.createTitledBorder("Alterar Minha Password"));
        panel.setBackground(Color.WHITE);

        JLabel lblAtual = new JLabel("Password Atual:");
        JLabel lblNova = new JLabel("Nova Password:");
        JLabel lblConfirma = new JLabel("Confirmar Nova:");

        JPasswordField txtAtual = new JPasswordField();
        JPasswordField txtNova = new JPasswordField();
        JPasswordField txtConfirma = new JPasswordField();

        JButton btnSalvar = new JButton("Salvar Nova Password");
        btnSalvar.setBackground(new Color(0x28A745));
        btnSalvar.setForeground(Color.WHITE);

        btnSalvar.addActionListener(e -> {

            String atual = new String(txtAtual.getPassword()).trim();
            String nova = new String(txtNova.getPassword()).trim();
            String conf = new String(txtConfirma.getPassword()).trim();

            if (atual.isEmpty() || nova.isEmpty() || conf.isEmpty()) {
                JOptionPane.showMessageDialog(null, "Preencha todos os campos!", "Erro", JOptionPane.ERROR_MESSAGE);
                return;
            }

            if (loginController.autenticar(Utilitarios.desnormalizarNome(r.getNome()), atual) == null) {
                JOptionPane.showMessageDialog(null, "Password atual incorreta!", "Erro", JOptionPane.ERROR_MESSAGE);
                return;
            }

            if (!nova.equals(conf)) {
                JOptionPane.showMessageDialog(null, "As passwords não coincidem!", "Erro", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Aqui actualiza no BD
            loginController.atualizarUsuario(loginActual.getUserId(), Utilitarios.getHash(nova), loginActual.getUserType());

            JOptionPane.showMessageDialog(null, "Password alterada com sucesso!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
        });

        panel.add(lblAtual);
        panel.add(txtAtual);

        panel.add(lblNova);
        panel.add(txtNova);

        panel.add(lblConfirma);
        panel.add(txtConfirma);

        panel.add(new JLabel()); // espaço
        panel.add(btnSalvar);

        return panel;
    }
}

