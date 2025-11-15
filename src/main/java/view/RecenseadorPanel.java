package view;

import controller.BairroController;
import controller.CidadaoController;
import controller.FamiliaController;
import controller.RecenseadorController;
import model.*;
import service.Graficos;
import service.Utilitarios;

import javax.swing.*;
import javax.swing.border.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.*;
import java.util.List;

public class RecenseadorPanel extends JPanel {
    private JLabel lblFamiliasHoje;
    private JLabel lblTotalSector;
    private final CardLayout contentCardLayout = new CardLayout();
    private final JPanel contentPanel = new JPanel(contentCardLayout);
    private final Color SIDEBAR_BACKGROUND = new Color(0x3A3A3A); // Um pouco diferente do admin
    private final Color CONTENT_BACKGROUND = new Color(0x2B2B2B);
    private final Color ACCENT_COLOR = new Color(0x4A88C7);
    private final Login login;
    private final BairroController bairroController = new BairroController();
    private final FamiliaController familiaController= new FamiliaController();
    private final RecenseadorController recenseadorController = new RecenseadorController();
    private final CidadaoController cidadaoController = new CidadaoController();
    private final int idFamiliaActual = familiaController.proximoID();
    private final String rec;
    private final List<Familia> familiaList;

    public RecenseadorPanel(Login login) {
        this.login = login;
        rec = Utilitarios.normalizarNome(login.getUsername());
        familiaList = familiaController.familiasPorRecenseador(recenseadorController.buscarIdRecenseador(Utilitarios.normalizarNome(login.getUsername())));
        setLayout(new BorderLayout());
        setBackground(CONTENT_BACKGROUND);
        criarUI();
    }

    private void criarUI() {
        JPanel sidebar = criarSidebar();
        setupContentPanel();
        add(sidebar, BorderLayout.WEST);
        add(contentPanel, BorderLayout.CENTER);
    }

    private JPanel criarSidebar() {
        JPanel sidebar = new JPanel();
        sidebar.setLayout(new BoxLayout(sidebar, BoxLayout.Y_AXIS));
        sidebar.setBackground(SIDEBAR_BACKGROUND);
        sidebar.setPreferredSize(new Dimension(220, 0));

        JLabel title = new JLabel("RECENSEADOR");
        title.setFont(new Font("Segoe UI", Font.BOLD, 20));
        title.setForeground(Color.WHITE);
        title.setAlignmentX(Component.CENTER_ALIGNMENT);
        title.setBorder(new EmptyBorder(20, 10, 20, 10));

        JLabel userInfo = new JLabel("Bem-vindo, "+ Utilitarios.normalizarNome(login.getUsername()));
        userInfo.setFont(new Font("Algerian", Font.ITALIC, 16));
        userInfo.setForeground(Color.LIGHT_GRAY);
        userInfo.setAlignmentX(Component.CENTER_ALIGNMENT);

        sidebar.add(title);
        sidebar.add(userInfo);
        sidebar.add(Box.createRigidArea(new Dimension(0, 20)));

        adicionarButoesAoSideBar("Início", "INICIO", sidebar);
        adicionarButoesAoSideBar("Registar Família", "REGISTAR", sidebar);
        adicionarButoesAoSideBar("Meus Registos", "REGISTOS", sidebar);
        adicionarButoesAoSideBar("Meu Perfil", "PERFIL", sidebar);
        sidebar.add(Box.createVerticalGlue());
        adicionarButoesAoSideBar("Mudar Usuário", "MUDAR_USUARIO", sidebar);
        adicionarButoesAoSideBar("Sair", "Sair", sidebar);

        return sidebar;
    }

    private void adicionarButoesAoSideBar(String text, String actionCommand, JPanel container) {
        JButton button = new JButton(text);
        button.setAlignmentX(Component.CENTER_ALIGNMENT);
        button.setMaximumSize(new Dimension(Integer.MAX_VALUE, 45));
        button.setBackground(SIDEBAR_BACKGROUND);
        button.setForeground(Color.LIGHT_GRAY);
        button.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        button.setBorder(new EmptyBorder(10, 20, 10, 20));
        button.setFocusPainted(false);
        button.setHorizontalAlignment(SwingConstants.LEFT);

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
            if ("Sair".equals(actionCommand)) {
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

        button.addActionListener(e -> contentCardLayout.show(contentPanel, actionCommand));
        container.add(button);
        container.add(Box.createRigidArea(new Dimension(0, 5)));
    }

    private void setupContentPanel() {
        contentPanel.add(criarPainelDashboard(), "INICIO");
        contentPanel.add(criarPainelRegistar(), "REGISTAR");
        contentPanel.add(criarPainelMeusRegistos(), "REGISTOS");
        contentPanel.add(new PainelPerfil(recenseadorController.buscarRecenseadorPorId
                (recenseadorController.buscarIdRecenseador(rec))), "PERFIL");
    }

    private JPanel criarPainelDashboard() {
        JPanel panel = new JPanel(new BorderLayout(20,20));
        panel.setBorder(new EmptyBorder(20,20,20,20));
        panel.setBackground(CONTENT_BACKGROUND);

        JLabel title = new JLabel("Painel do Recenseador");
        title.setFont(new Font("Segoe UI", Font.BOLD, 28));
        title.setForeground(Color.WHITE);
        panel.add(title, BorderLayout.NORTH);

        JPanel centerPanel = new JPanel(new GridLayout(2, 2, 20, 20));
        centerPanel.setBackground(CONTENT_BACKGROUND);
        lblFamiliasHoje = new JLabel("" + Utilitarios.contarFamiliasCadastradasHoje(familiaList));
        lblTotalSector = new JLabel("" + familiaController.numeroFamiliaPorRecenseador(
                recenseadorController.buscarIdRecenseador(Utilitarios.normalizarNome(login.getUsername()))
        ));
        centerPanel.add(createStatCard("Famílias Registadas Hoje", lblFamiliasHoje, new Color(0x28A745)));
        centerPanel.add(createStatCard("Total no Seu Sector", lblTotalSector, new Color(0x007BFF)));
        panel.add(centerPanel, BorderLayout.CENTER);

        panel.add(new Graficos().gerarGraficoProgresso(familiaList), BorderLayout.EAST);

        JButton bigButton = new JButton("Iniciar Registo de Nova Família");
        bigButton.setFont(new Font("Segoe UI", Font.BOLD, 22));
        bigButton.setPreferredSize(new Dimension(100, 80));
        bigButton.addActionListener(e -> contentCardLayout.show(contentPanel, "REGISTAR"));
        panel.add(bigButton, BorderLayout.SOUTH);

        return panel;
    }

    private JPanel createStatCard(String title, JLabel valueLabel, Color accentColor) {
        JPanel card = new JPanel(new BorderLayout());
        card.setBackground(SIDEBAR_BACKGROUND);
        card.setBorder(new MatteBorder(5, 0, 0, 0, accentColor));

        JLabel titleLabel = new JLabel(" " + title);
        titleLabel.setForeground(Color.LIGHT_GRAY);

        valueLabel.setFont(new Font("Arial Black", Font.BOLD, 40));
        valueLabel.setForeground(Color.WHITE);
        valueLabel.setHorizontalAlignment(SwingConstants.CENTER);
        valueLabel.setVerticalAlignment(SwingConstants.CENTER);

        card.add(titleLabel, BorderLayout.NORTH);
        card.add(valueLabel, BorderLayout.CENTER);

        return card;
    }

    private JPanel criarPainelRegistar() {
        JPanel wizardPanel = new JPanel(new BorderLayout(10,10));
        wizardPanel.setBorder(new EmptyBorder(20,20,20,20));
        wizardPanel.setBackground(CONTENT_BACKGROUND);

        CardLayout wizardLayout = new CardLayout();
        JPanel stepsPanel = new JPanel(wizardLayout);

        // Passo 1: Dados da Família
        JPanel step1 = new JPanel();
        step1.setLayout(new BoxLayout(step1, BoxLayout.Y_AXIS));
        step1.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(Color.GRAY),
                "Passo 1 de 2: Dados da Família",
                TitledBorder.LEFT, TitledBorder.TOP,
                new Font("Segoe UI", Font.BOLD, 14), Color.WHITE
        ));
        step1.setBackground(CONTENT_BACKGROUND);
        step1.setForeground(Color.WHITE);

        JPanel aux = new JPanel(new FlowLayout()), aux0 = new JPanel(new FlowLayout()), aux1 = new JPanel(new FlowLayout()), aux3 = new JPanel(new FlowLayout());
        JTextField nomeFamiliaTxt = new JTextField();
        JTextField idField = new JTextField();
        String[] arrayBairro = bairroController.listarTodosNomes().toArray(new String[0]);

        Font labelFont = new Font("Segoe UI", Font.PLAIN, 14);
        Font fieldFont = new Font("Segoe UI", Font.PLAIN, 14);

        Dimension fieldSize = new Dimension(250, 32); // largura x altura

        idField.setFont(fieldFont);
        idField.setEditable(false);
        idField.setPreferredSize(new Dimension(80, 32));
        idField.setMinimumSize(new Dimension(80, 32));
        idField.setMaximumSize(new Dimension(100, 32));
        idField.setText("" + familiaController.proximoID());

// Nome da Família — aumentado
        nomeFamiliaTxt.setFont(fieldFont);
        nomeFamiliaTxt.setPreferredSize(new Dimension(300, 32));
        nomeFamiliaTxt.setMinimumSize(new Dimension(250, 32));
        nomeFamiliaTxt.setText("Familia ");

        JLabel labelContext = new JLabel("INFORMAÇÃO DE CONTEXTO");
        labelContext.setFont(new Font("Algerian", Font.BOLD, 20));
        labelContext.setForeground(Color.WHITE);
        JPanel panelCentro = new JPanel(new FlowLayout(FlowLayout.CENTER));
        panelCentro.add(labelContext);
        step1.add(panelCentro);

        JLabel labelRecenseador = new JLabel("Recenseador: " + rec);
        labelRecenseador.setFont(new Font("Arial", Font.BOLD, 18));
        labelRecenseador.setForeground(Color.WHITE);
        JPanel panelCentro2 = new JPanel(new FlowLayout(FlowLayout.CENTER));
        panelCentro2.add(labelRecenseador);
        step1.add(panelCentro2);

        //Separador
        JSeparator sep = new JSeparator();
        sep.setForeground(Color.GRAY);
        step1.add(sep);
        step1.add(Box.createRigidArea(new Dimension(0, 15)));

        JLabel labelDados = new JLabel("DADOS A PREENCHER");
        labelDados.setFont(new Font("Algerian", Font.BOLD, 20));
        labelDados.setForeground(Color.WHITE);
        JPanel panelCentro3 = new JPanel(new FlowLayout(FlowLayout.CENTER));
        panelCentro3.add(labelDados);
        step1.add(panelCentro3);

//data registo
        JSpinner txtData = new JSpinner(new SpinnerDateModel());
        txtData.setFont(fieldFont);
        txtData.setPreferredSize(fieldSize);
        txtData.setMinimumSize(fieldSize);
        txtData.setMaximumSize(fieldSize);

        JSpinner.DateEditor editor = new JSpinner.DateEditor(txtData, "dd/MM/yyyy");
        txtData.setEditor(editor);

        JLabel lblData = new JLabel("Data de Registro: ");
        lblData.setFont(labelFont);
        lblData.setForeground(Color.WHITE);
        aux3.add(lblData);
        aux3.add(txtData);

// Bairro escolha
        JLabel lblBairro = new JLabel("Bairro: ");
        lblBairro.setFont(labelFont);
        lblBairro.setForeground(Color.WHITE);
        aux.add(lblBairro);

        JComboBox<String> comboBairro = new JComboBox<>(arrayBairro);
        comboBairro.setFont(fieldFont);
        comboBairro.setPreferredSize(fieldSize);
        comboBairro.setMinimumSize(fieldSize);
        comboBairro.setMaximumSize(fieldSize);
        aux.add(comboBairro);

        JLabel lblId = new JLabel("ID da Família:");
        lblId.setFont(labelFont);
        lblId.setForeground(Color.WHITE);
        aux0.add(lblId);
        aux0.add(idField);

        JLabel lblNome = new JLabel("Nome da Família:");
        lblNome.setFont(labelFont);
        lblNome.setForeground(Color.WHITE);
        aux1.add(lblNome);
        aux1.add(nomeFamiliaTxt);

        step1.add(aux);
        step1.add(aux0);
        step1.add(aux3);
        step1.add(aux1);

        // Passo 2: Adicionar Membros
        JPanel step2 = new JPanel(new BorderLayout(10,10));
        step2.setBorder(BorderFactory.createTitledBorder("Passo 2: Adicionar Membros"));
        JToolBar memberToolbar = new JToolBar();
        JButton addMemberBtn = new JButton("+ Adicionar Novo Membro");
        addMemberBtn.setFont(new Font("Segoe UI", Font.BOLD, 14));
        addMemberBtn.setForeground(Color.WHITE);
        addMemberBtn.setBackground(new Color(0x28A745));
        addMemberBtn.setFocusPainted(false);
        addMemberBtn.setBorderPainted(false);

        DefaultTableModel membersModel = new DefaultTableModel(
                new String[]{"Nome", "Idade", "Género", "Contacto"}, 0
        ) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        JTable membersTable = new JTable(membersModel);
        JScrollPane tableScroll = new JScrollPane(membersTable);
        step2.add(tableScroll, BorderLayout.CENTER);

        JPanel navPanel = new JPanel();
        navPanel.setLayout(new BoxLayout(navPanel, BoxLayout.X_AXIS));

        JButton cancelButton = new JButton("Cancelar");
        JButton backButton = new JButton("< Voltar");
        JButton nextButton = new JButton("Continuar >");
        JButton finishButton = new JButton("Concluir Registo");
        finishButton.setEnabled(false);

        List<Cidadao> membrosAdicionados = new ArrayList<>();

        finishButton.addActionListener(e -> {
            String nomeFamilia = nomeFamiliaTxt.getText();
            Date data = (Date) txtData.getValue();
            Optional<Bairro> bairro = bairroController.buscarPorId(bairroController.buscarId(comboBairro.getSelectedItem().toString()));
            Recenseador recenseador = recenseadorController.buscarRecenseadorPorId(recenseadorController.buscarIdRecenseador(rec));
            if(recenseador == null) {
                JOptionPane.showMessageDialog(null, "Recenseador" + rec + " nao encontrado", "WARNING", JOptionPane.ERROR_MESSAGE);
                return;
            }
            if(bairro == null){
                JOptionPane.showMessageDialog(null, "Bairro" + bairro.get().getNome() + " nao encontrado", "ERROR", JOptionPane.ERROR_MESSAGE);
                return;
            }

            int rowCount = membersModel.getRowCount();

            if(nomeFamiliaTxt.getText().trim().equals("Familia")){
                JOptionPane.showMessageDialog(null, "Coloque o nome da Familia!", "Erro", JOptionPane.WARNING_MESSAGE);
                return;
            }else {
                Familia familia = new Familia(nomeFamilia,  bairro.get(), recenseador, data);
                int idFamilia = familiaController.adicionarFamilia(familia);
                for(Cidadao c: membrosAdicionados) {
                    c.setId_familia(idFamilia);
                    cidadaoController.add(c);
                }
                atualizarDash();
            }

            if (familiaController.exist(idFamiliaActual))
            JOptionPane.showMessageDialog(RecenseadorPanel.this, "Família '" + nomeFamilia + "' registada com sucesso!\n" + rowCount + " membro(s) adicionado(s).", "Sucesso", JOptionPane.INFORMATION_MESSAGE);

            contentCardLayout.show(contentPanel, "INICIO");
        });

        addMemberBtn.addActionListener(e -> {
            Window ownerWindow = SwingUtilities.getWindowAncestor(RecenseadorPanel.this);
            if (ownerWindow == null) {
                JOptionPane.showMessageDialog(this, "Erro: Janela principal não encontrada.", "Erro", JOptionPane.ERROR_MESSAGE);
                return;
            }
            AddMember dialog = new AddMember((Frame) SwingUtilities.getWindowAncestor(ownerWindow));
            dialog.setVisible(true);

            if (dialog.wasSaved()) {
                int idade = calcularIdade(dialog.getDataNasc());

                // Adicionar linha à tabela
                membersModel.addRow(new Object[]{
                        dialog.getNome(),
                        idade == -1 ? "?" : idade,
                        dialog.getGenero(),
                        dialog.getContactoField()
                });

                membrosAdicionados.add(new Cidadao(familiaController.proximoID(), dialog.getNome(),dialog.getDataNasc(),
                        dialog.getGenero(), dialog.getEstadoCivilCombo(), dialog.getOcupacaoField(),
                        dialog.getContactoField(), dialog.getNacionalidadeField()));

                finishButton.setEnabled(membersModel.getRowCount() > 0);
            }
        });

        memberToolbar.add(addMemberBtn);
        step2.add(memberToolbar, BorderLayout.NORTH);
        step2.add(new JScrollPane(new JTable(membersModel)), BorderLayout.CENTER);
        stepsPanel.add(step1, "STEP1");
        stepsPanel.add(step2, "STEP2");

        wizardPanel.add(stepsPanel, BorderLayout.CENTER);

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

        cancelButton.addActionListener(e -> {
            int result = JOptionPane.showConfirmDialog(
                    wizardPanel,
                    "Deseja realmente cancelar o registo?",
                    "Confirmar",
                    JOptionPane.YES_NO_OPTION
            );
            if (result == JOptionPane.YES_OPTION) {
                contentCardLayout.show(contentPanel, "INICIO");
            }
        });

        navPanel.add(cancelButton);

        navPanel.add(Box.createHorizontalGlue());

        navPanel.add(backButton);
        navPanel.add(nextButton);
        navPanel.add(finishButton);
        wizardPanel.add(navPanel, BorderLayout.SOUTH);

        return wizardPanel;
    }

    private JPanel criarPainelMeusRegistos() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(new EmptyBorder(20, 20, 20, 20));
        panel.setBackground(CONTENT_BACKGROUND);
        JLabel title = new JLabel("Meus Registos de Famílias");
        title.setFont(new Font("Segue UI", Font.BOLD, 28));
        title.setForeground(Color.WHITE);
        panel.add(title, BorderLayout.NORTH);

        DefaultTableModel tableModel = new DefaultTableModel(new String[]{"ID Família", "Nome", "Data Registo", "Nº Membros"}, 0);
        JTable table = new JTable(tableModel);
        panel.add(new JScrollPane(table), BorderLayout.CENTER);
        DefaultTableCellRenderer centralizar = new DefaultTableCellRenderer();
        centralizar.setHorizontalAlignment(SwingConstants.CENTER);
        for (int i = 0; i < table.getColumnCount(); i++) {
            table.getColumnModel().getColumn(i).setCellRenderer(centralizar);
        }

        for(Familia f: familiaList) {
            tableModel.addRow(new String[]{f.getIdFamilia()+"", f.getNome(), f.getData() + "", f.getNumeroMembros() + ""});
        }
        return panel;
    }


    private int calcularIdade(LocalDate dataNascimento) {
        if (dataNascimento == null) {
            return -1;
        }

        LocalDate hoje = LocalDate.now();

        // Calcula idade básica
        int idade = hoje.getYear() - dataNascimento.getYear();

        // Ajusta se o aniversário ainda não chegou este ano
        if (hoje.getDayOfYear() < dataNascimento.getDayOfYear()) {
            idade--;
        }

        return idade;
    }

    private void atualizarDash() {
        lblFamiliasHoje.setText("" + Utilitarios.contarFamiliasCadastradasHoje(familiaList));
        lblTotalSector.setText("" + familiaController.numeroFamiliaPorRecenseador(
                recenseadorController.buscarIdRecenseador(Utilitarios.normalizarNome(login.getUsername()))
        ));
    }


}
