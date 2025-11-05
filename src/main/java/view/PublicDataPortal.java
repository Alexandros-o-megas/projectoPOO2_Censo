package view;

import service.Graficos;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.geom.RoundRectangle2D;
import java.util.HashMap;
import java.util.Map;

public class PublicDataPortal extends JPanel {
    private final Color BACKGROUND_COLOR = new Color(0x1E1E1E);
    private final Color CARD_BACKGROUND = new Color(0x2E2E2E);
    private final Color HEADER_BACKGROUND = new Color(0x1E2A3A);
    private final Color TEXT_COLOR = new Color(0x7F7F7F);

    public PublicDataPortal() {
        setLayout(new BorderLayout());
        setBackground(BACKGROUND_COLOR);
        createUI();
    }

    private void createUI() {
        // Cabeçalho
        add(createHeader(), BorderLayout.NORTH);

        // Painel Principal
        JPanel mainContent = new JPanel(new BorderLayout(20, 20));
        mainContent.setBorder(new EmptyBorder(20, 20, 20, 20));
        mainContent.setBackground(BACKGROUND_COLOR);

        // Indicadores (KPI)
        JPanel kpiPanel = new JPanel(new GridLayout(1, 4, 20, 20));
        kpiPanel.setBackground(BACKGROUND_COLOR);
        kpiPanel.add(createKpiCard("População Total", "15,482", new Color(0x007BFF)));
        kpiPanel.add(createKpiCard("Total de Famílias", "3,120", new Color(0x28A745)));
        kpiPanel.add(createKpiCard("Média por Família", "4.9", new Color(0xFFC107)));
        kpiPanel.add(createKpiCard("Bairros Cadastrados", "22", new Color(0x17A2B8)));

        mainContent.add(kpiPanel, BorderLayout.NORTH);

        // Painel de Gráficos
        JPanel chartsPanel = new JPanel(new GridLayout(2, 2, 20, 20));
        chartsPanel.setBackground(BACKGROUND_COLOR);

        Map<String, Integer> genderData = new HashMap<>();
        genderData.put("Feminino", 51);
        genderData.put("Masculino", 49);

        chartsPanel.add(createRoundedPanel(new PieChartPanel("Distribuição por Género", genderData)));
        chartsPanel.add(createRoundedPanel(createMapPlaceholder()));

        Map<String, Integer> ageData = new HashMap<>();
        ageData.put("0-18", 4500);
        ageData.put("19-35", 5500);
        ageData.put("36-60", 4000);
        ageData.put("60+", 1482);

        chartsPanel.add(createRoundedPanel(new Graficos().createGraficoFaixaEtaria()));
        chartsPanel.add(createRoundedPanel(createDataExportPanel()));

        mainContent.add(chartsPanel, BorderLayout.CENTER);
        add(mainContent, BorderLayout.CENTER);
        add(createFooter(), BorderLayout.SOUTH);
    }

    private JPanel createHeader() {
        JPanel header = new JPanel(new FlowLayout(FlowLayout.LEFT));
        header.setBackground(HEADER_BACKGROUND);
        JLabel title = new JLabel("Portal de Dados do Censo de Moçambique");
        title.setFont(new Font("Segoe UI", Font.BOLD, 24));
        title.setForeground(Color.WHITE);
        header.add(title);
        return header;
    }

    private JPanel createKpiCard(String title, String value, Color accentColor) {
        RoundedPanel card = new RoundedPanel(20);
        card.setLayout(new BorderLayout());
        card.setBackground(CARD_BACKGROUND);
        card.setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 15));

        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));
        titleLabel.setForeground(TEXT_COLOR);

        JLabel valueLabel = new JLabel(value);
        valueLabel.setFont(new Font("Segoe UI", Font.BOLD, 32));
        valueLabel.setForeground(accentColor);

        card.add(titleLabel, BorderLayout.NORTH);
        card.add(valueLabel, BorderLayout.CENTER);
        return card;
    }

    private JPanel createMapPlaceholder() {
        RoundedPanel mapPanel = new RoundedPanel(20);
        mapPanel.setLayout(new BorderLayout());
        mapPanel.setBackground(CARD_BACKGROUND);
        mapPanel.setBorder(BorderFactory.createTitledBorder("Mapa Interativo do Bairro"));
        JLabel placeholder = new JLabel("🗺️ Mapa não disponível no modo público.", SwingConstants.CENTER);
        placeholder.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        mapPanel.add(placeholder, BorderLayout.CENTER);
        return mapPanel;
    }

    private JPanel createDataExportPanel() {
        RoundedPanel panel = new RoundedPanel(20);
        panel.setBackground(CARD_BACKGROUND);
        panel.setBorder(BorderFactory.createTitledBorder("Acesso aos Dados"));
        panel.setLayout(new GridBagLayout());

        JButton exportButton = new JButton("Exportar Dados Agregados (CSV)");
        exportButton.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        exportButton.setBackground(new Color(0x1E88E5));
        exportButton.setForeground(Color.WHITE);
        exportButton.setFocusPainted(false);
        exportButton.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        exportButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        panel.add(exportButton);
        return panel;
    }

    private JPanel createFooter() {
        JPanel footer = new JPanel(new FlowLayout(FlowLayout.CENTER));
        footer.setBackground(HEADER_BACKGROUND);
        JLabel footerText = new JLabel("© 2024 Instituto Nacional de Estatística | Todos os direitos reservados.");
        footerText.setForeground(Color.LIGHT_GRAY);
        footer.setBorder(new EmptyBorder(10, 0, 10, 0));
        footer.add(footerText);
        return footer;
    }

    private RoundedPanel createRoundedPanel(JPanel innerPanel) {
        RoundedPanel wrapper = new RoundedPanel(20);
        wrapper.setLayout(new BorderLayout());
        wrapper.setBackground(CARD_BACKGROUND);
        wrapper.add(innerPanel, BorderLayout.CENTER);
        return wrapper;
    }

    // --- Classes Internas para Simulação de Gráficos ---

    private class PieChartPanel extends JPanel {
        private final Map<String, Integer> data;

        public PieChartPanel(String title, Map<String, Integer> data) {
            this.data = data;
            setBorder(BorderFactory.createTitledBorder(title));
            setBackground(CARD_BACKGROUND);
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            int total = data.values().stream().mapToInt(Integer::intValue).sum();
            if (total == 0) return;

            int width = getWidth();
            int height = getHeight();
            int diameter = Math.min(width, height) - 60;
            int x = (width - diameter) / 2;
            int y = (height - diameter) / 2;

            int startAngle = 0;
            Color[] colors = {new Color(0x4A88C7), new Color(0x28A745)};
            int colorIndex = 0;

            for (Map.Entry<String, Integer> entry : data.entrySet()) {
                int arcAngle = (int) Math.round((double) entry.getValue() * 360 / total);
                g.setColor(colors[colorIndex++ % colors.length]);
                g.fillArc(x, y, diameter, diameter, startAngle, arcAngle);
                startAngle += arcAngle;
            }
        }
    }

    private class BarChartPanel extends JPanel {
        private final Map<String, Integer> data;

        public BarChartPanel(String title, Map<String, Integer> data) {
            this.data = data;
            setBorder(BorderFactory.createTitledBorder(title));
            setBackground(CARD_BACKGROUND);
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            if (data == null || data.isEmpty()) return;

            int maxVal = data.values().stream().mapToInt(v -> v).max().orElse(0);
            int width = getWidth();
            int height = getHeight();
            int barWidth = (width - 60) / data.size();

            int x = 30;
            for (Map.Entry<String, Integer> entry : data.entrySet()) {
                int barHeight = (int) (((double) entry.getValue() / maxVal) * (height - 80));
                g.setColor(new Color(0xFFC107));
                g.fillRect(x, height - barHeight - 40, barWidth - 10, barHeight);
                g.setColor(TEXT_COLOR);
                g.drawString(entry.getKey(), x + 5, height - 20);
                x += barWidth;
            }
        }
    }

    // --- Painel Arredondado Reutilizável ---
    private static class RoundedPanel extends JPanel {
        private final int radius;

        public RoundedPanel(int radius) {
            this.radius = radius;
            setOpaque(false);
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(getBackground());
            g2.fill(new RoundRectangle2D.Double(0, 0, getWidth(), getHeight(), radius, radius));
            g2.dispose();
            super.paintComponent(g);
        }
    }
}
