package view;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.MatteBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionListener;

public class RecenseadorPanel extends JPanel {
    private final CardLayout contentCardLayout = new CardLayout();
    private final JPanel contentPanel = new JPanel(contentCardLayout);
    private final Color SIDEBAR_BACKGROUND = new Color(0x3A3A3A); // Um pouco diferente do admin
    private final Color CONTENT_BACKGROUND = new Color(0x2B2B2B);

    public RecenseadorPanel() {
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
        sidebar.setPreferredSize(new Dimension(220, 0));

        JLabel title = new JLabel("RECENSEADOR");
        title.setFont(new Font("Segoe UI", Font.BOLD, 20));
        title.setForeground(Color.WHITE);
        title.setAlignmentX(Component.CENTER_ALIGNMENT);
        title.setBorder(new EmptyBorder(20, 10, 20, 10));

        JLabel userInfo = new JLabel("Bem-vindo, João Mário");
        userInfo.setFont(new Font("Segoe UI", Font.ITALIC, 14));
        userInfo.setForeground(Color.LIGHT_GRAY);
        userInfo.setAlignmentX(Component.CENTER_ALIGNMENT);

        sidebar.add(title);
        sidebar.add(userInfo);
        sidebar.add(Box.createRigidArea(new Dimension(0, 20)));

        addSidebarButton("Início", "INICIO", sidebar);
        addSidebarButton("Registar Família", "REGISTAR", sidebar);
        addSidebarButton("Meus Registos", "REGISTOS", sidebar);
        addSidebarButton("Meu Perfil", "PERFIL", sidebar);
        sidebar.add(Box.createVerticalGlue());
        addSidebarButton("Sair", "Sair", sidebar);

        return sidebar;
    }

    private void addSidebarButton(String text, String actionCommand, JPanel container) {
        JButton button = new JButton(text);
        button.setAlignmentX(Component.CENTER_ALIGNMENT);
        button.setMaximumSize(new Dimension(Integer.MAX_VALUE, 45));
        button.setBackground(SIDEBAR_BACKGROUND);
        button.setForeground(Color.LIGHT_GRAY);
        button.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        button.setBorder(new EmptyBorder(10, 20, 10, 20));
        button.setFocusPainted(false);
        button.setHorizontalAlignment(SwingConstants.LEFT);

        button.addActionListener(e -> contentCardLayout.show(contentPanel, actionCommand));
        container.add(button);
        container.add(Box.createRigidArea(new Dimension(0, 5)));
    }

    private void setupContentPanel() {
        contentPanel.add(createDashboardPanel(), "INICIO");
        contentPanel.add(createRegistrationWizardPanel(), "REGISTAR");
        contentPanel.add(createMyRecordsPanel(), "REGISTOS");
        contentPanel.add(new JLabel("Painel de Perfil em construção...", SwingConstants.CENTER), "PERFIL");
    }

    private JPanel createDashboardPanel() {
        JPanel panel = new JPanel(new BorderLayout(20,20));
        panel.setBorder(new EmptyBorder(20,20,20,20));
        panel.setBackground(CONTENT_BACKGROUND);

        JLabel title = new JLabel("Painel do Recenseador");
        title.setFont(new Font("Segoe UI", Font.BOLD, 28));
        title.setForeground(Color.WHITE);
        panel.add(title, BorderLayout.NORTH);

        JPanel centerPanel = new JPanel(new GridLayout(2, 2, 20, 20));
        centerPanel.setBackground(CONTENT_BACKGROUND);
        centerPanel.add(createStatCard("Famílias Registadas Hoje", "7", new Color(0x28A745)));
        centerPanel.add(createStatCard("Total no Seu Setor", "124", new Color(0x007BFF)));

        panel.add(centerPanel, BorderLayout.CENTER);

        JButton bigButton = new JButton("Iniciar Registo de Nova Família");
        bigButton.setFont(new Font("Segoe UI", Font.BOLD, 22));
        bigButton.setPreferredSize(new Dimension(100, 80));
        bigButton.addActionListener(e -> contentCardLayout.show(contentPanel, "REGISTAR"));
        panel.add(bigButton, BorderLayout.SOUTH);

        return panel;
    }

    // Método createStatCard pode ser reutilizado ou copiado do AdminPanel
    private JPanel createStatCard(String title, String value, Color accentColor) {
        JPanel card = new JPanel(new BorderLayout());
        card.setBackground(SIDEBAR_BACKGROUND);
        card.setBorder(new MatteBorder(5, 0, 0, 0, accentColor));
        JLabel titleLabel = new JLabel(" " + title);
        titleLabel.setForeground(Color.LIGHT_GRAY);
        JLabel valueLabel = new JLabel(" " + value);
        valueLabel.setFont(new Font("Segoe UI", Font.BOLD, 36));
        valueLabel.setForeground(Color.WHITE);
        card.add(titleLabel, BorderLayout.NORTH);
        card.add(valueLabel, BorderLayout.CENTER);
        return card;
    }

    private JPanel createRegistrationWizardPanel() {
        // Simulação do wizard com CardLayout
        JPanel wizardPanel = new JPanel(new BorderLayout(10,10));
        wizardPanel.setBorder(new EmptyBorder(20,20,20,20));
        wizardPanel.setBackground(CONTENT_BACKGROUND);

        CardLayout wizardLayout = new CardLayout();
        JPanel stepsPanel = new JPanel(wizardLayout);

        // Passo 1: Dados da Família
        JPanel step1 = new JPanel(new GridLayout(4,2, 10, 10));
        step1.setBorder(BorderFactory.createTitledBorder("Passo 1: Dados da Família"));
        step1.add(new JLabel("ID Família:"));
        step1.add(new JTextField("FAM-2024-AUTO") {{ setEditable(false); }});
        step1.add(new JLabel("Nome da Família:"));
        step1.add(new JTextField());
        stepsPanel.add(step1, "STEP1");

        // Passo 2: Adicionar Membros
        JPanel step2 = new JPanel(new BorderLayout(10,10));
        step2.setBorder(BorderFactory.createTitledBorder("Passo 2: Adicionar Membros"));
        JToolBar memberToolbar = new JToolBar();
        memberToolbar.add(new JButton("+ Adicionar Membro"));
        step2.add(memberToolbar, BorderLayout.NORTH);
        DefaultTableModel membersModel = new DefaultTableModel(new String[]{"Nome", "Idade", "Género"}, 0);
        step2.add(new JScrollPane(new JTable(membersModel)), BorderLayout.CENTER);
        stepsPanel.add(step2, "STEP2");

        wizardPanel.add(stepsPanel, BorderLayout.CENTER);

        // Botões de Navegação do Wizard
        JPanel navPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton backButton = new JButton("< Voltar");
        JButton nextButton = new JButton("Continuar >");
        JButton finishButton = new JButton("Concluir Registo");

        backButton.setVisible(false);
        finishButton.setVisible(false);

        backButton.addActionListener(e -> {
            wizardLayout.show(stepsPanel, "STEP1");
            nextButton.setVisible(true);
            finishButton.setVisible(false);
            backButton.setVisible(false);
        });

        nextButton.addActionListener(e -> {
            wizardLayout.show(stepsPanel, "STEP2");
            nextButton.setVisible(false);
            finishButton.setVisible(true);
            backButton.setVisible(true);
        });

        navPanel.add(backButton);
        navPanel.add(nextButton);
        navPanel.add(finishButton);
        wizardPanel.add(navPanel, BorderLayout.SOUTH);

        return wizardPanel;
    }

    private JPanel createMyRecordsPanel() {
        // Reutiliza a estrutura do painel de gestão do admin
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(new EmptyBorder(20, 20, 20, 20));
        panel.setBackground(CONTENT_BACKGROUND);
        JLabel title = new JLabel("Meus Registos de Famílias");
        title.setFont(new Font("Segoe UI", Font.BOLD, 28));
        title.setForeground(Color.WHITE);
        panel.add(title, BorderLayout.NORTH);

        DefaultTableModel tableModel = new DefaultTableModel(new String[]{"ID Família", "Nome", "Data Registo", "Nº Membros"}, 0);
        JTable table = new JTable(tableModel);
        panel.add(new JScrollPane(table), BorderLayout.CENTER);
        return panel;
    }
}
