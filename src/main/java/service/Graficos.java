package service;

import conexao.Conexao;
import controller.BairroController;
import controller.CidadaoController;
import model.Bairro;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.general.DefaultPieDataset;

import javax.swing.*;
import java.awt.*;
import java.sql.*;
import java.util.List;
import java.util.Map;

public class Graficos {
    private BairroController bairroController;
    private Connection connection;

    public Graficos(Connection connection) {
        this.connection = connection;
        this.bairroController = new BairroController(connection);
    }

    public Graficos() {
    }

    public JPanel createGraficoProfissoes() {
        DefaultPieDataset<String> dataset = new DefaultPieDataset<>();

        String sql = """
            SELECT
                ocupacao,
                COUNT(*) AS total
            FROM
                cidadao
            GROUP BY
                ocupacao
            ORDER BY
                total DESC
        """;

        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                String profissao = rs.getString("ocupacao");
                int total = rs.getInt("total");

                // Evita valores nulos no gráfico
                if (profissao == null || profissao.isBlank()) {
                    profissao = "Não especificada";
                }

                dataset.setValue(profissao, total);
            }

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null,
                    "Erro ao gerar gráfico de profissões: " + e.getMessage(),
                    "Erro",
                    JOptionPane.ERROR_MESSAGE);
        }

        JFreeChart chart = ChartFactory.createPieChart(
                "Distribuição de Profissões dos Cidadãos",
                dataset,
                false, // legenda
                true, // tooltips
                false // URLs
        );
        ChartPanel chartPanel = new ChartPanel(chart);

        // 🔹 Define o tamanho do painel (largura x altura)
        chartPanel.setPreferredSize(new java.awt.Dimension(100, 100));

        // 🔹 Opcional: remove bordas extras
        chartPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        chartPanel.setBackground(Color.DARK_GRAY);

        return chartPanel;
    }

    public JPanel familiasPorBairro(){
        bairroController = new BairroController(connection);
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();

        try {
            // Exemplo: buscar dados do DAO

            List<Bairro> bairros = bairroController.listarTodos();
            for (Bairro b : bairros) {
                dataset.addValue(b.getNumeroFamilias(), "Famílias", b.getNome());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        JFreeChart chart = ChartFactory.createBarChart(
                "Distribuição de Famílias por Bairro", // título do gráfico
                "Bairro",                              // eixo X
                "Nº de Famílias",                       // eixo Y
                dataset
        );

        return new ChartPanel(chart);
    }


    public JPanel createGraficoFaixaEtaria() {
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        CidadaoController cidadaoController = new CidadaoController(Conexao.getConexao());

        try {
            Map<String, Integer> dados = cidadaoController.contarCidadaosPorFaixaEtaria();

            for (Map.Entry<String, Integer> entry : dados.entrySet()) {
                dataset.addValue(entry.getValue(), "Cidadãos",entry.getKey());
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null,
                    "Erro ao gerar gráfico de faixa etária: " + e.getMessage(),
                    "Erro",
                    JOptionPane.ERROR_MESSAGE);
        }

        JFreeChart chart = ChartFactory.createBarChart(
                "Distribuição de Cidadãos por Faixa Etária", // título
                "Faixa Etária",                              // eixo X
                "Número de Cidadãos",                        // eixo Y
                dataset,                                     // dados
                PlotOrientation.VERTICAL,                    // orientação
                false,                                        // legenda
                true,                                        // tooltips
                false                                        // URLs
        );

        chart.setBackgroundPaint(new Color(0x1E1E1E));
        chart.getTitle().setPaint(Color.WHITE);

        var plot = chart.getCategoryPlot();
        plot.setBackgroundPaint(new Color(0x2E2E2E));
        plot.setDomainGridlinePaint(Color.GRAY);
        plot.setRangeGridlinePaint(Color.GRAY);
        plot.getRenderer().setSeriesPaint(0, new Color(0x4A88C7));

        ChartPanel panel = new ChartPanel(chart);
        panel.setPreferredSize(new Dimension(600, 400));
        panel.setMouseWheelEnabled(true);

        return panel;
    }

}

