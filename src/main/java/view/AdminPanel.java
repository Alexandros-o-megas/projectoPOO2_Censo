package view;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.MatteBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class AdminPanel extends JPanel {

    private final CardLayout contentCardLayout = new CardLayout();
    private final JPanel contentPanel = new JPanel(contentCardLayout);
    private final Color SIDEBAR_BACKGROUND = new Color(0x3C3F41);
    private final Color CONTENT_BACKGROUND = new Color(0x2B2B2B);
    private final Color ACCENT_COLOR = new Color(0x4A88C7);

    public AdminPanel() {
        setLayout(new BorderLayout());
        setBackground(CONTENT_BACKGROUND);
        createUI();
    }

    private void createUI() {
        // 1. Sidebar de Navegação
        JPanel sidebar = createSidebar();

        // 2. Painel de Conteúdo (que alterna as telas)
        setupContentPanel();

        add(sidebar, BorderLayout.WEST);
        add(contentPanel, BorderLayout.CENTER);
    }

    private JPanel createSidebar() {
        JPanel sidebar = new JPanel();
        sidebar.setLayout(new BoxLayout(sidebar, BoxLayout.Y_AXIS));
        sidebar.setBackground(SIDEBAR_BACKGROUND);
        sidebar.setPreferredSize(new Dimension(240, 0));
        sidebar.setBorder(new MatteBorder(0, 0, 0, 1, new Color(0x555555)));

        JLabel title = new JLabel("PAINEL ADMIN");
        title.setFont(new Font("Segoe UI", Font.BOLD, 22));
        title.setForeground(Color.WHITE);
        title.setAlignmentX(Component.CENTER_ALIGNMENT);
        title.setBorder(new EmptyBorder(20, 10, 20, 10));

        sidebar.add(title);
        sidebar.add(Box.createRigidArea(new Dimension(0, 10)));

        // Botões de Navegação
        addSidebarButton("Dashboard", "DASHBOARD", sidebar);
        addSidebarButton("Bairros", "BAIRROS", sidebar);
        addSidebarButton("Recenseadores", "RECENSEADORES", sidebar);
        addSidebarButton("Famílias", "FAMILIAS", sidebar);
        addSidebarButton("Relatórios", "RELATORIOS", sidebar);
        addSidebarButton("Configurações", "CONFIGURACOES", sidebar);

        sidebar.add(Box.createVerticalGlue()); // Empurra o botão Sair para baixo

        addSidebarButton("Sair", "SAIR", sidebar);
        return sidebar;
    }

    private void addSidebarButton(String text, String actionCommand, JPanel container) {
        JButton button = new JButton(text);
        button.setAlignmentX(Component.CENTER_ALIGNMENT);
        button.setMaximumSize(new Dimension(Integer.MAX_VALUE, 50));
        button.setBackground(SIDEBAR_BACKGROUND);
        button.setForeground(Color.LIGHT_GRAY);
        button.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        button.setBorder(new EmptyBorder(10, 20, 10, 20));
        button.setFocusPainted(false);
        button.setHorizontalAlignment(SwingConstants.LEFT);

        button.addActionListener(e -> {
            if ("SAIR".equals(actionCommand)) {
                System.exit(0); // Ação de Sair
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
        contentPanel.add(createManagementPanel("Gestão de Bairros", new String[]{"ID", "Nome Bairro", "Nº Famílias"}), "BAIRROS");
        contentPanel.add(createManagementPanel("Gestão de Recenseadores", new String[]{"ID Funcional", "Nome", "Contacto", "Famílias Registadas"}), "RECENSEADORES");
        contentPanel.add(createManagementPanel("Gestão de Famílias", new String[]{"ID Família", "Nome", "Bairro", "Recenseador", "Nº Membros"}), "FAMILIAS");
        contentPanel.add(new JLabel("Painel de Relatórios em construção...", SwingConstants.CENTER), "RELATORIOS");
        contentPanel.add(new JLabel("Painel de Configurações em construção...", SwingConstants.CENTER), "CONFIGURACOES");
    }

    private JPanel createDashboardPanel() {
        JPanel dashboard = new JPanel(new BorderLayout(20, 20));
        dashboard.setBorder(new EmptyBorder(20, 20, 20, 20));
        dashboard.setBackground(CONTENT_BACKGROUND);

        JLabel title = new JLabel("Dashboard - Visão Geral do Censo");
        title.setFont(new Font("Segoe UI", Font.BOLD, 28));
        title.setForeground(Color.WHITE);
        dashboard.add(title, BorderLayout.NORTH);

        // Painel de Estatísticas
        JPanel statsPanel = new JPanel(new GridLayout(1, 4, 20, 20));
        statsPanel.setBackground(CONTENT_BACKGROUND);
        statsPanel.add(createStatCard("Total de Cidadãos", "15,482", new Color(0x28A745)));
        statsPanel.add(createStatCard("Total de Famílias", "3,120", new Color(0x007BFF)));
        statsPanel.add(createStatCard("Total de Bairros", "8", new Color(0xFFC107)));
        statsPanel.add(createStatCard("Recenseadores Ativos", "25", new Color(0x17A2B8)));

        dashboard.add(statsPanel, BorderLayout.CENTER);

        // Placeholder para um gráfico
        JPanel chartPlaceholder = new JPanel();
        chartPlaceholder.setBorder(BorderFactory.createTitledBorder("Distribuição de Famílias por Bairro"));
        chartPlaceholder.setBackground(SIDEBAR_BACKGROUND);
        dashboard.add(chartPlaceholder, BorderLayout.SOUTH);

        return dashboard;
    }

    private JPanel createStatCard(String title, String value, Color accentColor) {
        JPanel card = new JPanel(new BorderLayout());
        card.setBackground(SIDEBAR_BACKGROUND);
        card.setBorder(new MatteBorder(5, 0, 0, 0, accentColor));

        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(new Font("Segoe UI", Font.PLAIN, 18));
        titleLabel.setForeground(Color.LIGHT_GRAY);
        titleLabel.setBorder(new EmptyBorder(15, 15, 10, 15));

        JLabel valueLabel = new JLabel(value);
        valueLabel.setFont(new Font("Segoe UI Semibold", Font.BOLD, 42));
        valueLabel.setForeground(Color.WHITE);
        valueLabel.setBorder(new EmptyBorder(0, 15, 15, 15));

        card.add(titleLabel, BorderLayout.NORTH);
        card.add(valueLabel, BorderLayout.CENTER);

        return card;
    }

    private JPanel createManagementPanel(String titleText, String[] columnNames) {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(new EmptyBorder(20, 20, 20, 20));
        panel.setBackground(CONTENT_BACKGROUND);

        JLabel title = new JLabel(titleText);
        title.setFont(new Font("Segoe UI", Font.BOLD, 28));
        title.setForeground(Color.WHITE);
        panel.add(title, BorderLayout.NORTH);

        // Toolbar
        JToolBar toolBar = new JToolBar();
        toolBar.setBackground(CONTENT_BACKGROUND);
        toolBar.setFloatable(false);
        toolBar.add(new JButton("Adicionar Novo"));
        toolBar.add(new JButton("Editar Selecionado"));
        toolBar.add(new JButton("Remover Selecionado"));
        toolBar.add(Box.createHorizontalGlue());
        toolBar.add(new JTextField(20));
        toolBar.add(new JButton("Pesquisar"));
        panel.add(toolBar, BorderLayout.CENTER);

        // Tabela
        DefaultTableModel tableModel = new DefaultTableModel(columnNames, 0);
        // TODO: Popular a tabela com dados reais
        for(int i = 0; i < 20; i++) { tableModel.addRow(new Object[]{"Dado "+i+"-1", "Dado "+i+"-2", "Dado "+i+"-3", "Dado "+i+"-4"}); }

        JTable table = new JTable(tableModel);
        table.setFillsViewportHeight(true);
        table.setRowHeight(30);
        JScrollPane scrollPane = new JScrollPane(table);
        panel.add(scrollPane, BorderLayout.SOUTH);

        return panel;
    }
}