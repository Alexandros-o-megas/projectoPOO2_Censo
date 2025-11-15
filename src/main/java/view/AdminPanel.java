package view;

import conexao.Conexao;
import controller.*;
import model.*;
import service.Graficos;

import javax.swing.*;
import javax.swing.border.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.RoundRectangle2D;
import java.util.List;
import java.util.Map;

public class AdminPanel extends JPanel {

    private final CardLayout contentCardLayout = new CardLayout();
    private final JPanel contentPanel = new JPanel(contentCardLayout);
    private final Color SIDEBAR_BACKGROUND = new Color(0x2E2E2E);
    private final Color CONTENT_BACKGROUND = new Color(0x1E1E1E);
    private final Color ACCENT_COLOR = new Color(0x4A88C7);

    private final CidadaoController cidadaoController;
    private final RecenseadorController recenseadorController;
    private final BairroController bairroController;
    private final FamiliaController familiaController;

    public AdminPanel() {
        cidadaoController = new CidadaoController(Conexao.getConexao());
        familiaController = new FamiliaController(Conexao.getConexao());
        bairroController = new BairroController(Conexao.getConexao());
        recenseadorController = new RecenseadorController(Conexao.getConexao());

        setLayout(new BorderLayout());
        setBackground(CONTENT_BACKGROUND);
        createUI();

    }

    private void createUI() {
        JPanel sidebar = createSidebar();
        setupContentPanel();
        add(sidebar, BorderLayout.WEST);
        add(contentPanel, BorderLayout.CENTER);
    }

    private JPanel createSidebar() {
        JPanel sidebar = new JPanel();
        sidebar.setLayout(new BoxLayout(sidebar, BoxLayout.Y_AXIS));
        sidebar.setBackground(SIDEBAR_BACKGROUND);
        sidebar.setPreferredSize(new Dimension(240, 0));
        sidebar.setBorder(new MatteBorder(0, 0, 0, 1, new Color(0x444444)));

        JLabel title = new JLabel("PAINEL ADMIN");
        title.setFont(new Font("Segoe UI", Font.BOLD, 22));
        title.setForeground(Color.WHITE);
        title.setAlignmentX(Component.CENTER_ALIGNMENT);
        title.setBorder(new EmptyBorder(20, 10, 20, 10));

        sidebar.add(title);
        sidebar.add(Box.createRigidArea(new Dimension(0, 10)));

        adicionarButoesAoSideBar("Dashboard", "DASHBOARD", sidebar);
        adicionarButoesAoSideBar("Bairros", "BAIRROS", sidebar);
        adicionarButoesAoSideBar("Recenseadores", "RECENSEADORES", sidebar);
        adicionarButoesAoSideBar("Famílias", "FAMILIAS", sidebar);
        adicionarButoesAoSideBar("Relatórios", "RELATORIOS", sidebar);
        adicionarButoesAoSideBar("Configurações", "CONFIGURACOES", sidebar);

        sidebar.add(Box.createVerticalGlue());
        adicionarButoesAoSideBar("Mudar Usuário", "MUDAR_USUARIO", sidebar);
        adicionarButoesAoSideBar("Sair", "SAIR", sidebar);

        return sidebar;
    }

    private void adicionarButoesAoSideBar(String text, String actionCommand, JPanel container) {
        RoundedButton button = new RoundedButton(text);
        button.setAlignmentX(Component.CENTER_ALIGNMENT);
        button.setMaximumSize(new Dimension(Integer.MAX_VALUE, 45));
        button.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        button.setForeground(Color.LIGHT_GRAY);
        button.setBackground(new Color(0x3C3F41));
        button.setHorizontalAlignment(SwingConstants.LEFT);
        button.setBorder(new EmptyBorder(10, 20, 10, 20));
        button.setFocusPainted(false);
        button.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        // Efeito hover
        button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                button.setBackground(ACCENT_COLOR.darker());
                button.setForeground(Color.WHITE);
            }

            @Override
            public void mouseExited(MouseEvent e) {
                button.setBackground(new Color(0x3C3F41));
                button.setForeground(Color.LIGHT_GRAY);
            }
        });

        button.addActionListener(e -> {
            if ("SAIR".equals(actionCommand)) {
                System.exit(0);

            } else if ("MUDAR_USUARIO".equals(actionCommand)) {
                JFrame frame = (JFrame) SwingUtilities.getWindowAncestor(this);
                frame.getContentPane().removeAll();
                frame.getContentPane().add(new LoginPanel());
                frame.revalidate();
                frame.repaint();

            } else {
                contentCardLayout.show(contentPanel, actionCommand);
            }

        });

        container.add(button);
        container.add(Box.createRigidArea(new Dimension(0, 5)));
    }

    private void setupContentPanel() {
        contentPanel.setBackground(CONTENT_BACKGROUND);
        contentPanel.add(createDashboardPanel(), "DASHBOARD");
        contentPanel.add(criarPainelDeGestao("Gestão de Bairros", new String[]{"ID", "Nome Bairro", "Nº Famílias"}), "BAIRROS");
        contentPanel.add(criarPainelDeGestao("Gestão de Recenseadores", new String[]{"ID Funcional", "Nome", "Contacto", "Famílias Registadas"}), "RECENSEADORES");
        contentPanel.add(criarPainelDeGestao("Gestão de Famílias", new String[]{"ID Família", "Nome", "Bairro", "Recenseador", "Nº Membros"}), "FAMILIAS");
        contentPanel.add(new PainelRelatorios(), "RELATORIOS");
        contentPanel.add(criarPainelConfiguracoesAdmin(), "CONFIGURACOES");
    }

    private JPanel createDashboardPanel() {
        JPanel dashboard = new JPanel(new BorderLayout(20, 20));
        dashboard.setBorder(new EmptyBorder(20, 20, 20, 20));
        dashboard.setBackground(CONTENT_BACKGROUND);

        JLabel title = new JLabel("Dashboard - Visão Geral do Censo");
        title.setFont(new Font("Segoe UI", Font.BOLD, 28));
        title.setForeground(Color.WHITE);
        dashboard.add(title, BorderLayout.NORTH);

        JPanel dashMain = new JPanel();
        dashMain.setLayout(new BoxLayout(dashMain, BoxLayout.Y_AXIS));

        Graficos graficos = new Graficos(Conexao.getConexao());
        JPanel graphicPanel = new RoundedPanel(30,CONTENT_BACKGROUND);
        graphicPanel.setLayout(new GridLayout(1,2));
        graphicPanel.add(graficos.familiasPorBairro());
        graphicPanel.add(graficos.createGraficoProfissoes());

        JPanel statsPanel = new JPanel(new FlowLayout(FlowLayout.CENTER,20,10));
        statsPanel.setBackground(CONTENT_BACKGROUND);

        statsPanel.add(createStatCard("Total de Cidadãos", cidadaoController.contarTodos()+"", new Color(0x28A745)));
        statsPanel.add(createStatCard("Total de Famílias", familiaController.contarTotal()+"", new Color(0x007BFF)));
        statsPanel.add(createStatCard("Total de Bairros", bairroController.getTotalFamilias()+"", new Color(0xFFC107)));
        statsPanel.add(createStatCard("Recenseadores Activos", recenseadorController.contarTodos()+"", new Color(0x17A2B8)));

        dashMain.add(statsPanel);
        dashMain.add(graphicPanel);
        dashboard.add(dashMain, BorderLayout.CENTER);
        return dashboard;
    }

    private JPanel createStatCard(String title, String value, Color accentColor) {
        RoundedPanel card = new RoundedPanel(20, new Color(0x333333));
        card.setLayout(new BorderLayout());
        card.setBorder(new MatteBorder(4, 0, 0, 0, accentColor));

        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(new Font("Segoe UI", Font.PLAIN, 18));
        titleLabel.setForeground(Color.LIGHT_GRAY);
        titleLabel.setBorder(new EmptyBorder(15, 15, 10, 15));

        JLabel valueLabel = new JLabel();
        animateNumber(valueLabel, Integer.parseInt(value));
        valueLabel.setFont(new Font("Segoe UI Semibold", Font.BOLD, 40));
        valueLabel.setForeground(Color.WHITE);
        valueLabel.setBorder(new EmptyBorder(0, 15, 15, 15));

        card.add(titleLabel, BorderLayout.NORTH);
        card.add(valueLabel, BorderLayout.CENTER);

        return card;
    }

    private JPanel criarPainelDeGestao(String titleText, String[] columnNames) {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(new EmptyBorder(20, 20, 20, 20));
        panel.setBackground(CONTENT_BACKGROUND);

        JLabel title = new JLabel(titleText);
        title.setFont(new Font("Segoe UI", Font.BOLD, 28));
        title.setForeground(Color.WHITE);
        panel.add(title, BorderLayout.NORTH);

        DefaultTableModel tableModel = new DefaultTableModel(columnNames, 0);
        JScrollPane scrollPane = getJScrollPane(tableModel);
        panel.add(scrollPane, BorderLayout.CENTER);

        // === 🔹 Carregar dados do BD conforme o tipo de painel ===
        try {
            if (titleText.contains("Bairros")) {
                List<Bairro> bairros = bairroController.listarTodos();
                for (Bairro b : bairros) {
                    tableModel.addRow(new Object[]{
                            b.getIdBairro(),
                            b.getNome(),
                            b.getNumeroFamilias()
                    });
                }
            } else if (titleText.contains("Famílias")) {
                List<Familia> familias = familiaController.listarFamilias();
                for (Familia f : familias) {
                    tableModel.addRow(new Object[]{
                            f.getIdFamilia(),
                            f.getNome(),
                            f.getBairro().getNome(),
                            f.getRecenseador().getNome(),
                            f.getNumeroMembros()
                    });
                }
            } else if (titleText.contains("Recenseadores")) {
                List<Recenseador> recenseadores = recenseadorController.listarRecenseadores();
                Map<Integer, Integer> contagens = familiaController.contarFamiliasPorRecenseador();
                for (Recenseador r : recenseadores) {
                    int total = contagens.getOrDefault(r.getIdRecenseador(), 0);
                    tableModel.addRow(new Object[]{
                            r.getIdRecenseador(),
                            r.getNome(),
                            r.getContacto(),
                            total
                    });
                }
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(panel, "Erro ao carregar dados: " + e.getMessage(),
                    "Erro", JOptionPane.ERROR_MESSAGE);
        }

        return panel;
    }

    private JScrollPane getJScrollPane(DefaultTableModel tableModel) {
        JTable table = new JTable(tableModel);
        table.setFillsViewportHeight(true);
        table.setRowHeight(28);
        table.setForeground(Color.WHITE);
        table.setBackground(new Color(0x2C2C2C));
        table.setGridColor(new Color(0x444444));
        table.setSelectionBackground(ACCENT_COLOR.darker());
        table.setSelectionForeground(Color.WHITE);

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBorder(BorderFactory.createLineBorder(new Color(0x444444)));
        return scrollPane;
    }

    public JPanel criarPainelConfiguracoesAdmin() {
        JTabbedPane tabbedPane = new JTabbedPane();

        // ----- Aba 1: Segurança -----
        JPanel segurancaPanel = new JPanel(new BorderLayout());
        segurancaPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Seção Alterar Password
        JPanel alterarSenhaPanel = new JPanel(new GridBagLayout());
        alterarSenhaPanel.setBorder(BorderFactory.createTitledBorder("Alterar Minha Password"));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel lblAtual = new JLabel("Password Atual:");
        JPasswordField pfAtual = new JPasswordField(15);
        JLabel lblNova = new JLabel("Nova Password:");
        JPasswordField pfNova = new JPasswordField(15);
        JLabel lblConfirmar = new JLabel("Confirmar Nova Password:");
        JPasswordField pfConfirmar = new JPasswordField(15);
        JButton btnSalvarSenha = new JButton("Salvar Alterações");
        btnSalvarSenha.setBackground(new Color(0x28A745));
        btnSalvarSenha.setForeground(Color.WHITE);

        gbc.gridx = 0; gbc.gridy = 0; alterarSenhaPanel.add(lblAtual, gbc);
        gbc.gridx = 1; alterarSenhaPanel.add(pfAtual, gbc);
        gbc.gridx = 0; gbc.gridy = 1; alterarSenhaPanel.add(lblNova, gbc);
        gbc.gridx = 1; alterarSenhaPanel.add(pfNova, gbc);
        gbc.gridx = 0; gbc.gridy = 2; alterarSenhaPanel.add(lblConfirmar, gbc);
        gbc.gridx = 1; alterarSenhaPanel.add(pfConfirmar, gbc);
        gbc.gridx = 1; gbc.gridy = 3; alterarSenhaPanel.add(btnSalvarSenha, gbc);

        // Seção Sessões Ativas
        JPanel sessoesPanel = new JPanel(new BorderLayout());
        sessoesPanel.setBorder(BorderFactory.createTitledBorder("Sessões Ativas"));

        String[] colunas = {"Utilizador", "IP", "Última Atividade"};
        Object[][] dados = {}; // inicialmente vazio, preencher dinamicamente
        JTable tabelaSessoes = new JTable(dados, colunas);
        JScrollPane scroll = new JScrollPane(tabelaSessoes);
        JButton btnDesconectar = new JButton("Desconectar Todas as Outras Sessões");
        btnDesconectar.setBackground(Color.RED);
        btnDesconectar.setForeground(Color.WHITE);

        sessoesPanel.add(scroll, BorderLayout.CENTER);
        sessoesPanel.add(btnDesconectar, BorderLayout.SOUTH);

        // Adiciona seções ao painel de Segurança
        segurancaPanel.add(alterarSenhaPanel, BorderLayout.NORTH);
        segurancaPanel.add(sessoesPanel, BorderLayout.CENTER);

        tabbedPane.addTab("Segurança", segurancaPanel);

        // ----- Aba 2: Backup e Restauração -----
        JPanel backupPanel = new JPanel();
        backupPanel.setLayout(new BoxLayout(backupPanel, BoxLayout.Y_AXIS));
        backupPanel.setBorder(BorderFactory.createEmptyBorder(10,10,10,10));

        JPanel secaoBackup = new JPanel(new FlowLayout(FlowLayout.LEFT));
        secaoBackup.setBorder(BorderFactory.createTitledBorder("Backup dos Dados"));
        secaoBackup.add(new JLabel("Último backup realizado: 10/11/2025 14:00"));
        secaoBackup.add(new JLabel("Localização do backup: /home/admin/backups"));
        JButton btnBackup = new JButton("Realizar Backup Agora");
        secaoBackup.add(btnBackup);

        JPanel secaoRestauracao = new JPanel(new FlowLayout(FlowLayout.LEFT));
        secaoRestauracao.setBorder(BorderFactory.createTitledBorder("Restauração de Dados"));
        JLabel aviso = new JLabel("Atenção: A restauração irá substituir todos os dados atuais.");
        aviso.setForeground(Color.RED);
        JButton btnRestaurar = new JButton("Restaurar a partir de um Ficheiro");
        secaoRestauracao.add(aviso);
        secaoRestauracao.add(btnRestaurar);

        backupPanel.add(secaoBackup);
        backupPanel.add(secaoRestauracao);

        tabbedPane.addTab("Backup e Restauração", backupPanel);

        // ----- Aba 3: Sobre -----
        JPanel sobrePanel = new JPanel();
        sobrePanel.setLayout(new BoxLayout(sobrePanel, BoxLayout.Y_AXIS));
        sobrePanel.setBorder(BorderFactory.createEmptyBorder(10,10,10,10));

        JLabel lblSistema = new JLabel("Sistema de Censo de Moçambique");
        lblSistema.setFont(new Font("Arial", Font.BOLD, 16));
        JLabel lblVersao = new JLabel("Versão: 1.0.0");
        JLabel lblDev = new JLabel("Desenvolvido por: Sua Equipa");
        JTextArea taInfo = new JTextArea("Licença e agradecimentos...");
        taInfo.setEditable(false);
        taInfo.setBackground(sobrePanel.getBackground());

        sobrePanel.add(lblSistema);
        sobrePanel.add(lblVersao);
        sobrePanel.add(lblDev);
        sobrePanel.add(taInfo);

        tabbedPane.addTab("Sobre", sobrePanel);

        JPanel painelFinal = new JPanel(new BorderLayout());
        painelFinal.add(tabbedPane, BorderLayout.CENTER);
        return painelFinal;
    }


    // ==== CLASSES AUXILIARES ====

    static class RoundedPanel extends JPanel {
        private final int cornerRadius;
        private final Color backgroundColor;

        public RoundedPanel(int radius, Color bgColor) {
            this.cornerRadius = radius;
            this.backgroundColor = bgColor;
            setOpaque(false);
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            Shape round = new RoundRectangle2D.Float(0, 0, getWidth(), getHeight(), cornerRadius, cornerRadius);
            g2.setColor(backgroundColor);
            g2.fill(round);
            g2.dispose();
        }
    }

    private void animateNumber(JLabel label, int targetValue) {
        new Thread(() -> {
            int current = 0;
            while (current <= targetValue) {
                int finalValue = current;
                SwingUtilities.invokeLater(() -> label.setText(String.valueOf(finalValue)));
                try {
                    Thread.sleep(200); // velocidade da animação
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                current += Math.max(1, targetValue / 50); // ajusta incremento
            }
            SwingUtilities.invokeLater(() -> label.setText(String.valueOf(targetValue)));
        }).start();
    }


    static class RoundedButton extends JButton {
        public RoundedButton(String text) {
            super(text);
            setFocusPainted(false);
            setContentAreaFilled(false);
            setOpaque(false);
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            Color fillColor = getModel().isArmed() ? getBackground().darker() : getBackground();
            g2.setColor(fillColor);
            g2.fillRoundRect(0, 0, getWidth(), getHeight(), 15, 15);
            super.paintComponent(g);
            g2.dispose();
        }

        @Override
        public void updateUI() {
            super.updateUI();
            setBorderPainted(false);
            setFocusPainted(false);
            setContentAreaFilled(false);
        }
    }
}
