package service;

import conexao.Conexao;
import controller.BairroController;
import controller.CidadaoController;
import model.Bairro;
import model.Familia;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PiePlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.general.DefaultPieDataset;

import javax.swing.*;
import java.awt.*;
import java.sql.*;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.stream.Collectors;

public class Graficos {
    private BairroController bairroController;
    private CidadaoController cidadaoController;
    private Connection connection;

    public Graficos(Connection connection) {
        this.connection = connection;
        this.bairroController = new BairroController(connection);
        this.cidadaoController = new CidadaoController();

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
                true, // legenda
                false, // tooltips
                false // URLs
        );

        chart.setBackgroundPaint(new Color(0x1E1E1E));
        chart.getTitle().setPaint(Color.WHITE);

        PiePlot plot = (PiePlot) chart.getPlot();
        plot.setLabelGenerator(null);
        plot.setCircular(true);
        plot.setSimpleLabels(true);
        plot.setBackgroundPaint(new Color(0x2E2E2E));

        ChartPanel panel = new ChartPanel(chart);
        panel.setMouseWheelEnabled(true);
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        panel.setBackground(Color.DARK_GRAY);

        return panel;
    }

    public JPanel familiasPorBairro(){
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();

        try {

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


        chart.setBackgroundPaint(new Color(0x1E1E1E));
        chart.getTitle().setPaint(Color.WHITE);

        var plot = chart.getCategoryPlot();
        plot.setBackgroundPaint(new Color(0x2E2E2E));
        plot.setDomainGridlinePaint(Color.GRAY);
        plot.setRangeGridlinePaint(Color.GRAY);
        plot.getRenderer().setSeriesPaint(0, new Color(0x4A88C7));

        ChartPanel panel = new ChartPanel(chart);
        panel.setMouseWheelEnabled(true);
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        panel.setBackground(Color.DARK_GRAY);

        return panel;
    }

    public JPanel createGraficoFaixaEtaria() {
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        CidadaoController cidadaoController = new CidadaoController();

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
        panel.setMouseWheelEnabled(true);
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        panel.setBackground(Color.DARK_GRAY);

        return panel;
    }

    public JPanel createGraficoGenero() {
        Map<String, Integer> lista = cidadaoController.contarGenero();
        DefaultPieDataset<String> dataset = new DefaultPieDataset<>();
        for (Map.Entry<String, Integer> entry : lista.entrySet()) {
            String genero = entry.getKey();
            int total = entry.getValue();
            dataset.setValue(genero, total);
        }

        JFreeChart chart = ChartFactory.createPieChart(
                "Distribuição de Cidadãos por Género",
                dataset,
                true,   // legenda
                true,   // tooltips
                false   // URLs
        );

        chart.setBackgroundPaint(new Color(0x1E1E1E));
        chart.getTitle().setPaint(Color.WHITE);

        PiePlot plot = (PiePlot) chart.getPlot();
        plot.setLabelGenerator(null);
        plot.setCircular(true);
        plot.setSimpleLabels(true);
        plot.setBackgroundPaint(new Color(0x2E2E2E));

        ChartPanel panel = new ChartPanel(chart);
        panel.setMouseWheelEnabled(true);
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        panel.setBackground(Color.DARK_GRAY);

        return panel;
    }

    public static ChartPanel gerarGraficoProgresso(List<Familia> familias) {
        if (familias == null || familias.isEmpty()) {
            JOptionPane.showMessageDialog(null, "Nenhum registro encontrado para este recenseador.");
            return new ChartPanel(null);
        }

        Map<LocalDate, Long> familiasPorData = familias.stream()
                .collect(Collectors.groupingBy(
                        f -> {
                            java.util.Date data = f.getData();
                            if (data instanceof java.sql.Date) {
                                return ((Date) data).toLocalDate();
                            } else {
                                return new java.sql.Date(data.getTime()).toLocalDate();
                            }
                        },
                        TreeMap::new,
                        Collectors.counting()
                ));

        // Criar dataset
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        for (Map.Entry<LocalDate, Long> entry : familiasPorData.entrySet()) {
            dataset.addValue(entry.getValue(), "Famílias", entry.getKey().toString());
        }

        // Criar o gráfico
        JFreeChart chart = ChartFactory.createLineChart(
                "Progresso de Registos do Recenseador",
                "Data de Cadastro",
                "Famílias Registradas",
                dataset,
                PlotOrientation.VERTICAL,
                false,
                false,
                false
        );

        chart.setBackgroundPaint(new Color(0x1E1E1E));
        chart.getTitle().setPaint(Color.WHITE);

        var plot = chart.getCategoryPlot();
        plot.setBackgroundPaint(new Color(0x2E2E2E));
        plot.setDomainGridlinePaint(Color.GRAY);
        plot.setRangeGridlinePaint(Color.GRAY);
        plot.getRenderer().setSeriesPaint(0, new Color(0x4A88C7));

        ChartPanel panel = new ChartPanel(chart);
        panel.setMouseWheelEnabled(true);
        panel.setPreferredSize(new java.awt.Dimension(700, 300));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        panel.setBackground(Color.DARK_GRAY);

        return panel;
    }
}

