package controller;

import DAO.RelatorioDAO;
import model.Relatorio;
import java.sql.*;
import java.util.*;

public class RelatorioController {
    private RelatorioDAO relatorioDAO;

    public RelatorioController(Connection connection) {
        this.relatorioDAO = new RelatorioDAO(connection);
    }

    public void gerarRelatorio(Relatorio relatorio) {
        try {
            relatorioDAO.inserir(relatorio);
            System.out.println("✅ Relatório '" + relatorio.getTitulo() + "' gerado e salvo com sucesso!");
        } catch (SQLException e) {
            System.err.println("Erro ao gerar relatório: " + e.getMessage());
        }
    }

    public Relatorio buscarRelatorio(String titulo) {
        try {
            return relatorioDAO.buscarPorTitulo(titulo);
        } catch (SQLException e) {
            System.err.println("Erro ao buscar relatório: " + e.getMessage());
            return null;
        }
    }

    public List<Relatorio> listarRelatorios() {
        try {
            return relatorioDAO.listarTodos();
        } catch (SQLException e) {
            System.err.println("Erro ao listar relatórios: " + e.getMessage());
            return new ArrayList<>();
        }
    }

    public void removerRelatorio(String titulo) {
        try {
            relatorioDAO.deletarPorTitulo(titulo);
            System.out.println("✅ Relatório '" + titulo + "' removido com sucesso!");
        } catch (SQLException e) {
            System.err.println("Erro ao remover relatório: " + e.getMessage());
        }
    }

    public void limparTodosRelatorios() {
        try {
            relatorioDAO.deletarTodos();
            System.out.println("✅ Todos os relatórios foram removidos do histórico!");
        } catch (SQLException e) {
            System.err.println("Erro ao limpar relatórios: " + e.getMessage());
        }
    }
}
