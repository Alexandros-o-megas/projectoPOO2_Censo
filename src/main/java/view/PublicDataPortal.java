package view;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.util.HashMap;
import java.util.Map;

public class PublicDataPortal extends JPanel {
    private final Color BACKGROUND_COLOR = new Color(0xF2F2F2); // Tema claro para o público
    private final Color TEXT_COLOR = new Color(0x333333);
    private final Color HEADER_BACKGROUND = new Color(0x1E2A3A);

    public PublicDataPortal() {
        setLayout(new BorderLayout());
        setBackground(BACKGROUND_COLOR);
        createUI();
    }

    private void createUI() {
        // Cabeçalho
        add(createHeader(), BorderLayout.NORTH);

        // Painel Principal com dashboard
        JPanel mainContent = new JPanel(new BorderLayout(20, 20));
        mainContent.setBorder(new EmptyBorder(20,20,20,20));
        mainContent.setBackground(BACKGROUND_COLOR);

        // Indicadores Chave (KPIs)
        JPanel kpiPanel = new JPanel(new GridLayout(1,4,20,20));
        kpiPanel.setBackground(BACKGROUND_COLOR);
        kpiPanel.add(createKpiCard("População Total", "15,482", Color.BLUE));
        kpiPanel.add(createKpiCard("Total de Famílias", "3,120", Color.GREEN));
        kpiPanel.add(createKpiCard("Média por Família", "4.9", Color.ORANGE));

        mainContent.add(kpiPanel, BorderLayout.NORTH);

        // Gráficos e Mapa
        JPanel chartsPanel = new JPanel(new GridLayout(2,2,20,20));
        chartsPanel.setBackground(BACKGROUND_COLOR);

        Map<String, Integer> genderData = new HashMap<>();
        genderData.put("Feminino", 51);
        genderData.put("Masculino", 49);

        chartsPanel.add(new PieChartPanel("Distribuição por Género", genderData));
        chartsPanel.add(createMapPlaceholder());

        Map<String, Integer> ageData = new HashMap<>();
        ageData.put("0-18", 4500);
        ageData.put("19-35", 5500);
        ageData.put("36-60", 4000);
        ageData.put("60+", 1482);

        chartsPanel.add(new BarChartPanel("Distribuição por Faixa Etária", ageData));
        chartsPanel.add(createDataExportPanel());

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
        JPanel card = new JPanel(new BorderLayout());
        card.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));
        JLabel titleLabel = new JLabel(" " + title);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));
        JLabel valueLabel = new JLabel(" " + value);
        valueLabel.setFont(new Font("Segoe UI", Font.BOLD, 32));
        valueLabel.setForeground(accentColor);
        card.add(titleLabel, BorderLayout.NORTH);
        card.add(valueLabel, BorderLayout.CENTER);
        return card;
    }

    private JPanel createMapPlaceholder() {
        JPanel mapPanel = new JPanel();
        mapPanel.setBorder(BorderFactory.createTitledBorder("Mapa Interativo do Bairro"));
        mapPanel.add(new JLabel("Placeholder para componente de mapa."));
        return mapPanel;
    }

    private JPanel createDataExportPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(BorderFactory.createTitledBorder("Acesso aos Dados"));
        JButton exportButton = new JButton("Exportar Dados Agregados (CSV)");
        exportButton.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        panel.add(exportButton);
        return panel;
    }

    private JPanel createFooter() {
        JPanel footer = new JPanel(new FlowLayout(FlowLayout.CENTER));
        footer.setBackground(Color.DARK_GRAY);
        JLabel footerText = new JLabel("© 2024 Instituto Nacional de Estatística | Todos os direitos reservados.");
        footerText.setForeground(Color.LIGHT_GRAY);
        footer.add(footerText);
        return footer;
    }

    // --- Classes Internas para Simulação de Gráficos ---

    private class PieChartPanel extends JPanel {
        private final Map<String, Integer> data;

        public PieChartPanel(String title, Map<String, Integer> data) {
            this.data = data;
            setBorder(BorderFactory.createTitledBorder(title));
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            int total = data.values().stream().mapToInt(Integer::intValue).sum();
            if (total == 0) return;

            int width = getWidth();
            int height = getHeight();
            int diameter = Math.min(width, height) - 40;
            int x = (width - diameter) / 2;
            int y = (height - diameter) / 2;

            int startAngle = 0;
            Color[] colors = {new Color(0x4A88C7), new Color(0x28A745), Color.ORANGE, Color.RED};
            int colorIndex = 0;

            for (Map.Entry<String, Integer> entry : data.entrySet()) {
                int arcAngle = (int) Math.round((double) entry.getValue() * 360 / total);
                g.setColor(colors[colorIndex++ % colors.length]);
                g.fillArc(x, y, diameter, diameter, startAngle, arcAngle);
                startAngle += arcAngle;
            }
            // TODO: Adicionar legenda
        }
    }

    private class BarChartPanel extends JPanel {
        private final Map<String, Integer> data;

        public BarChartPanel(String title, Map<String, Integer> data) {
            this.data = data;
            setBorder(BorderFactory.createTitledBorder(title));
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            if (data == null || data.isEmpty()) return;

            int maxVal = data.values().stream().mapToInt(v -> v).max().orElse(0);
            int width = getWidth();
            int height = getHeight();
            int barWidth = (width - 60) / data.size();
            int padding = 20;

            Graphics2D g2 = (Graphics2D) g;

            int x = 30;
            for(Map.Entry<String, Integer> entry : data.entrySet()){
                int barHeight = (int) (((double)entry.getValue() / maxVal) * (height - 60));
                g2.setColor(new Color(0xFFC107));
                g2.fillRect(x, height - barHeight - 30, barWidth - 10, barHeight);
                g2.setColor(TEXT_COLOR);
                g2.drawString(entry.getKey(), x, height - 15);
                x += barWidth;
            }
        }
    }
}
