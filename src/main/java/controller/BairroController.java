package controller;

import DAO.BairroDAO;
import model.Bairro;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public class BairroController {
    private BairroDAO bairroDAO;

    public BairroController(Connection connection) {
        this.bairroDAO = new BairroDAO(connection);
    }

    public boolean adicionarBairro(Bairro bairro){
        try {
            bairroDAO.inserir(bairro);
            return true;
        } catch (SQLException e) {
            return false;
        }
    }

    public boolean adicionarBairro(String nome) {
        try {
            Bairro bairro = new Bairro(nome);
            bairroDAO.inserir(bairro);
            return true;
        } catch (SQLException e) {
            return false;
        }
    }

    public boolean atualizarBairro(int id, String novoNome) {
        try {
            Bairro bairro = bairroDAO.buscarPorId(id);
            if (bairro != null) {
                bairro.setNome(novoNome);
                bairroDAO.atualizar(bairro);
                return true;
            }
        } catch (SQLException e) {
            System.err.println("❌ Erro ao atualizar bairro: " + e.getMessage());
            return false;
        }
        return false;
    }

    public boolean removerBairro(int id) {
        try {
            bairroDAO.excluir(id);
            return true;
        } catch (SQLException e) {
            return false;
        }
    }

    public Optional<Bairro> buscarPorId(int id) {
        try {
            Bairro bairro = bairroDAO.buscarPorId(id);
            return Optional.of(bairro);
        } catch (SQLException e) {
            return Optional.empty();
        }
    }

    public List<Bairro> listarTodos() {
        try {
            return bairroDAO.listarTodos();
        } catch (SQLException e) {
            return List.of();
        }
    }
}
