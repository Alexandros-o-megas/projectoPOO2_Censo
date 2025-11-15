package view;

import service.RelatorioService;
import service.Relatorios;

import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;
import java.awt.*;
import java.awt.event.ActionEvent;

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
        String dados = obterDadosRelatorio(nome);

        JLabel label = new JLabel("<html><pre>" + dados + "</pre></html>");
        label.setHorizontalAlignment(SwingConstants.CENTER);
        label.setForeground(Color.WHITE);
        label.setFont(new Font("Arial", Font.BOLD,18));
        painelVisualizacao.add(label, BorderLayout.CENTER);
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
            case "Atividade de Registo" -> RelatorioService.getAtividadeRegisto();
            default -> "Selecione um relatorio!";
        };
    }

}
