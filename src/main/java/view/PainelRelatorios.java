package view;

import controller.BairroController;
import controller.CidadaoController;
import controller.FamiliaController;
import service.RelatorioService;
import service.Relatorios;

import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.List;
import java.util.Map;

public class PainelRelatorios extends JPanel {
    private JPanel painelVisualizacao;
    private JTree treeRelatorios;
    private JButton btnExportarPDF;
    private JButton btnExportarTXT;

    public PainelRelatorios() {
        setLayout(new BorderLayout());

        // Painel de seleção à esquerda
        DefaultMutableTreeNode root = new DefaultMutableTreeNode("Relatórios");
        DefaultMutableTreeNode demo = new DefaultMutableTreeNode("Demográficos");
        demo.add(new DefaultMutableTreeNode("População Total"));
        demo.add(new DefaultMutableTreeNode("Distribuição por Género"));
        demo.add(new DefaultMutableTreeNode("Distribuição por Faixa Etária"));
        demo.add(new DefaultMutableTreeNode("Densidade Populacional"));

        DefaultMutableTreeNode social = new DefaultMutableTreeNode("Sociais/Económicos");
        social.add(new DefaultMutableTreeNode("Estado Civil"));
        social.add(new DefaultMutableTreeNode("Tamanho das Famílias"));
        social.add(new DefaultMutableTreeNode("Distribuição por Ocupação"));

        DefaultMutableTreeNode operacional = new DefaultMutableTreeNode("Operacionais");
        operacional.add(new DefaultMutableTreeNode("Produtividade"));
        operacional.add(new DefaultMutableTreeNode("Atividade de Registo"));

        root.add(demo);
        root.add(social);
        root.add(operacional);

        treeRelatorios = new JTree(root);
        treeRelatorios.setRootVisible(true);
        JScrollPane treeScroll = new JScrollPane(treeRelatorios);
        treeScroll.setPreferredSize(new Dimension(200, 0));

        // Área de visualização à direita
        painelVisualizacao = new JPanel();
        painelVisualizacao.setLayout(new BorderLayout());
        painelVisualizacao.setBorder(BorderFactory.createTitledBorder("Visualização do Relatório"));

        // Botões de exportação
        JPanel painelBotoes = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        btnExportarPDF = new JButton("Exportar PDF");
        btnExportarTXT = new JButton("Exportar TXT");
        painelBotoes.add(btnExportarPDF);
        painelBotoes.add(btnExportarTXT);

        add(treeScroll, BorderLayout.WEST);
        add(painelVisualizacao, BorderLayout.CENTER);
        add(painelBotoes, BorderLayout.SOUTH);

        // Eventos
        treeRelatorios.addTreeSelectionListener(e -> mostrarRelatorio(treeRelatorios.getLastSelectedPathComponent()));
        btnExportarPDF.addActionListener(this::exportarPDF);
        btnExportarTXT.addActionListener(this::exportarTXT);
    }

    private void mostrarRelatorio(Object node) {
        painelVisualizacao.removeAll();
        if (node == null) return;

        String nome = node.toString();

        String texto = obterDadosRelatorio(nome);

        JPanel container = new JPanel();
        container.setLayout(new BoxLayout(container, BoxLayout.Y_AXIS));

        JLabel labelTexto = new JLabel("<html><pre>" + texto + "</pre></html>");
        labelTexto.setAlignmentX(Component.LEFT_ALIGNMENT);
        labelTexto.setFont(new Font("Times New Roman", Font.BOLD,16));
        labelTexto.setForeground(Color.WHITE);
        labelTexto.setHorizontalAlignment(SwingConstants.CENTER);

        container.add(labelTexto);

        switch (nome) {

            case "População Total" -> {

                // 1° TABELA: POPULAÇÃO POR BAIRRO (Map<String, Integer>)
                Map<String, Integer> bairros = new BairroController().familiasPorBairro();
                container.add(new JLabel("População por Bairro:"));
                container.add(criarTabela(bairros, "bairro", "total"));

                // 2° TABELA: FAMÍLIAS (List<Familia>)
                List familias = new FamiliaController().listarFamilias();
                container.add(new JLabel("Famílias cadastradas:"));
                container.add(criarTabelaLista(familias, new String[]{"idFamilia", "nome", "NumeroMembros"}));

                // 3° TABELA: CIDADÃOS (List<Cidadao>)
                List cidadaos = new CidadaoController().listarTodos();
                container.add(new JLabel("Cidadãos cadastrados:"));
                container.add(criarTabelaLista(cidadaos, new String[]{"idCidadao", "nome", "anoNascimento", "genero", "ocupacao", "estadoCivil"}));
            }
            case "Distribuição por Género" -> {
                Map<String, Integer> genero = new CidadaoController().contarGenero();
                container.add(new JLabel("Distribuição de Género:"));
                container.add(criarTabela(genero, "Género", "Quantidade"));
            }
            case "Distribuição por Faixa Etária" -> {
                Map<String, Integer> lista = new CidadaoController().contarCidadaosPorFaixaEtaria();
                container.add(new JLabel("População por faixa etária:"));
                container.add(criarTabela(lista, "faixa", "quantidade"));
            }
            case "Distribuição por Ocupação" -> {
                Map<String, Integer> lista = new CidadaoController().distribuicaoPorProfissao();
                container.add(new JLabel("População por ocupação:"));
                container.add(criarTabela(lista, "Ocupação", "Total"));
            }
            case "Estado Civil" -> {
                Map<String, Integer> lista = new CidadaoController().distribuicaoEstadoCivil();
                container.add(new JLabel("População por Estado Civil:"));
                container.add(criarTabela(lista, "Estado", "Total"));
            }


        }
        painelVisualizacao.add(new JScrollPane(container), BorderLayout.CENTER);
        painelVisualizacao.revalidate();
        painelVisualizacao.repaint();
    }

    private void exportarPDF(ActionEvent e) {
        String nomeRelatorio = treeRelatorios.getLastSelectedPathComponent().toString();
        String conteudo = obterDadosRelatorio(nomeRelatorio);
        Relatorios.exportarPDF(nomeRelatorio, conteudo);
    }

    private void exportarTXT(ActionEvent e) {
        String nomeRelatorio = treeRelatorios.getLastSelectedPathComponent().toString();
        String conteudo = obterDadosRelatorio(nomeRelatorio);
        Relatorios.exportarTXT(nomeRelatorio, conteudo);
    }

    private String obterDadosRelatorio(String nome) {
        return switch (nome) {
            case "População Total" -> RelatorioService.getPopulacaoTotal();
            case "Distribuição por Género" -> RelatorioService.getDistribuicaoGenero();
            case "Distribuição por Faixa Etária" -> RelatorioService.getDistribuicaoFaixaEtaria();
            case "Densidade Populacional" -> RelatorioService.getDensidadePopulacional();
            case "Estado Civil" -> RelatorioService.getEstadoCivil();
            case "Tamanho das Famílias" -> RelatorioService.getTamanhoFamilias();
            case "Distribuição por Ocupação" -> RelatorioService.getDistribuicaoOcupacao();
            case "Produtividade" -> RelatorioService.getProdutividade();
            case "Atividade de Registo" -> RelatorioService.getActividadeRegisto();
            default -> "Selecione um relatorio!";
        };
    }

    private JScrollPane criarTabelaLista(List<?> lista, String[] colunas) {

        String[][] linhas = new String[lista.size()][colunas.length];

        try {
            for (int i = 0; i < lista.size(); i++) {
                Object item = lista.get(i);

                for (int j = 0; j < colunas.length; j++) {
                    var campo = item.getClass().getDeclaredField(colunas[j]);
                    campo.setAccessible(true);
                    Object valor = campo.get(item);
                    linhas[i][j] = valor != null ? valor.toString() : "";
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            return new JScrollPane(new JTable()); // fallback
        }

        JTable tabela = new JTable(linhas, colunas);
        tabela.setFillsViewportHeight(true);

        return new JScrollPane(tabela);
    }

    private JScrollPane criarTabela(Map<String, Integer> dados, String colunaChave, String colunaValor) {

        String[] colunas = {colunaChave, colunaValor};
        String[][] linhas = new String[dados.size()][2];

        int i = 0;
        for (var entry : dados.entrySet()) {
            linhas[i][0] = entry.getKey();
            linhas[i][1] = entry.getValue().toString();
            i++;
        }

        JTable tabela = new JTable(linhas, colunas);
        tabela.setFillsViewportHeight(true);
        tabela.setAlignmentX(Component.LEFT_ALIGNMENT);

        return new JScrollPane(tabela);
    }



}
