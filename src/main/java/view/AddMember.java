package view;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.text.MaskFormatter;
import java.awt.*;

public class AddMember extends JDialog {

    private JTextField nomeField;
    private JTextField biField;
    private JFormattedTextField dataNascField;
    private JComboBox<String> generoCombo;
    private JComboBox<String> estadoCivilCombo;
    private JTextField ocupacaoField;
    private JTextField contactoField;
    private JTextField nacionalidadeField;

    private boolean saved = false;

    public AddMember(Frame owner) {
        super(owner, "Adicionar Membro", true); // modal
        setLayout(new BorderLayout());

        // Panel principal com formulário
        JPanel formPanel = new JPanel();
        formPanel.setLayout(new BoxLayout(formPanel, BoxLayout.Y_AXIS));
        formPanel.setBorder(new EmptyBorder(15, 15, 15, 15));

        // Helper para criar pares "Label + Campo"
        formPanel.add(createLabeledField("BI / ID do Cidadão:", biField = new JTextField(20)));
        formPanel.add(createLabeledField("Nome Completo:", nomeField = new JTextField(20)));

        // Data de nascimento com máscara (DD/MM/AAAA)
        try {
            MaskFormatter mask = new MaskFormatter("##/##/####");
            mask.setPlaceholderCharacter('_');
            dataNascField = new JFormattedTextField(mask);
        } catch (Exception e) {
            dataNascField = new JFormattedTextField("__/__/____");
        }
        formPanel.add(createLabeledField("Data de Nascimento:", dataNascField));

        generoCombo = new JComboBox<>(new String[]{"Masculino", "Feminino", "Outro"});
        formPanel.add(createLabeledField("Género:", generoCombo));

        estadoCivilCombo = new JComboBox<>(new String[]{"Solteiro", "Casado", "Divorciado", "Viúvo"});
        formPanel.add(createLabeledField("Estado Civil:", estadoCivilCombo));

        formPanel.add(createLabeledField("Ocupação:", ocupacaoField = new JTextField(20)));
        formPanel.add(createLabeledField("Contacto:", contactoField = new JTextField(20)));
        formPanel.add(createLabeledField("Nacionalidade:", nacionalidadeField = new JTextField(20)));

        add(new JScrollPane(formPanel), BorderLayout.CENTER);

        // Botões
        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton cancelBtn = new JButton("Cancelar");
        JButton saveBtn = new JButton("Salvar Membro");

        cancelBtn.addActionListener(e -> dispose());
        saveBtn.addActionListener(e -> {
            if (validateFields()) {
                saved = true;
                dispose();
            } else {
                JOptionPane.showMessageDialog(this, "Preencha pelo menos o Nome e o BI.", "Dados Incompletos", JOptionPane.WARNING_MESSAGE);
            }
        });

        btnPanel.add(cancelBtn);
        btnPanel.add(saveBtn);
        add(btnPanel, BorderLayout.SOUTH);

        pack();
        setLocationRelativeTo(owner);
        setResizable(false);
    }

    private JPanel createLabeledField(String labelText, JComponent field) {
        JPanel panel = new JPanel(new BorderLayout(5, 0));
        panel.setAlignmentX(Component.LEFT_ALIGNMENT);
        JLabel label = new JLabel(labelText);
        label.setPreferredSize(new Dimension(150, 25));
        panel.add(label, BorderLayout.WEST);
        panel.add(field, BorderLayout.CENTER);
        return panel;
    }

    private boolean validateFields() {
        return nomeField.getText().trim().length() > 0 &&
                biField.getText().trim().length() > 0;
    }

    // Getters para os dados salvos
    public boolean wasSaved() { return saved; }
    public String getNome() { return nomeField.getText().trim(); }
    public String getBi() { return biField.getText().trim(); }
    public String getDataNasc() { return dataNascField.getText().trim(); }
    public String getGenero() { return (String) generoCombo.getSelectedItem(); }
    // ... adicione mais getters conforme necessário
}
