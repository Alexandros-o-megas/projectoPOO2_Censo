package controller;

import DAO.BairroDAO;
import model.Bairro;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public class BairroController {
    private BairroDAO bairroDAO;

    public BairroController(Connection connection) {
        this.bairroDAO = new BairroDAO(connection);
    }

    public void adicionarBairro(String nome) {
        try {
            Bairro bairro = new Bairro(nome);
            bairroDAO.inserir(bairro);
            System.out.println("✅ Bairro adicionado com sucesso: " + bairro);
        } catch (SQLException e) {
            System.err.println("❌ Erro ao adicionar bairro: " + e.getMessage());
        }
    }

    public void atualizarBairro(int id, String novoNome) {
        try {
            Bairro bairro = bairroDAO.buscarPorId(id);
            if (bairro != null) {
                bairro.setNome(novoNome);
                bairroDAO.atualizar(bairro);
                System.out.println("✅ Bairro atualizado: " + bairro);
            } else {
                System.out.println("⚠️ Bairro com ID " + id + " não encontrado.");
            }
        } catch (SQLException e) {
            System.err.println("❌ Erro ao atualizar bairro: " + e.getMessage());
        }
    }

    public void removerBairro(int id) {
        try {
            bairroDAO.excluir(id);
            System.out.println("✅ Bairro removido (ID: " + id + ")");
        } catch (SQLException e) {
            System.err.println("❌ Erro ao remover bairro: " + e.getMessage());
        }
    }

    public Bairro buscarPorId(int id) {
        try {
            return bairroDAO.buscarPorId(id);
        } catch (SQLException e) {
            System.err.println("❌ Erro ao buscar bairro: " + e.getMessage());
            return null;
        }
    }

    public List<Bairro> listarTodos() {
        try {
            return bairroDAO.listarTodos();
        } catch (SQLException e) {
            System.err.println("❌ Erro ao listar bairros: " + e.getMessage());
            return List.of();
        }
    }
}
