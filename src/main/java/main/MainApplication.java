package main;

import com.formdev.flatlaf.FlatDarculaLaf;
import view.LoginPanel;

import javax.swing.*;
import java.awt.*;

public class MainApplication {

    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(new FlatDarculaLaf());
        } catch (UnsupportedLookAndFeelException e) {
            e.printStackTrace();
        }

        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Sistema de Censo de Moçambique");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setSize(1280, 800);
            frame.setLocationRelativeTo(null);
            frame.setResizable(true);

            // CardLayout para alternar entre painéis
            CardLayout cardLayout = new CardLayout();
            JPanel mainPanel = new JPanel(cardLayout);

            // Criar e adicionar painéis
            LoginPanel loginPanel = new LoginPanel(cardLayout, mainPanel);
            mainPanel.add(loginPanel, "LOGIN");

            // Adicionar ao frame e exibir
            frame.add(mainPanel);
            frame.setVisible(true);

            // Começar com o login
            cardLayout.show(mainPanel, "LOGIN");
        });
    }
}


