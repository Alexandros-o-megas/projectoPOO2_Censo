package controller;

import DAO.RecenseadorDAO;
import model.*;
import java.sql.*;
import java.util.*;

public class RecenseadorController {
    private RecenseadorDAO recenseadorDAO;

    public RecenseadorController(Connection connection) {
        this.recenseadorDAO = new RecenseadorDAO(connection);
    }

    public void adicionarRecenseador(Recenseador recenseador) {
        try {
            recenseadorDAO.inserir(recenseador);
            System.out.println("✅ Recenseador adicionado com sucesso!");
        } catch (SQLException e) {
            System.err.println("Erro ao adicionar recenseador: " + e.getMessage());
        }
    }

    public void atualizarRecenseador(Recenseador recenseador) {
        try {
            recenseadorDAO.atualizar(recenseador);
            System.out.println("✅ Recenseador atualizado com sucesso!");
        } catch (SQLException e) {
            System.err.println("Erro ao atualizar recenseador: " + e.getMessage());
        }
    }

    public void removerRecenseador(int idRecenseador) {
        try {
            recenseadorDAO.deletar(idRecenseador);
            System.out.println("✅ Recenseador removido com sucesso!");
        } catch (SQLException e) {
            System.err.println("Erro ao remover recenseador: " + e.getMessage());
        }
    }

    public Recenseador buscarRecenseadorPorId(int idRecenseador) {
        try {
            return recenseadorDAO.buscarPorId(idRecenseador);
        } catch (SQLException e) {
            System.err.println("Erro ao buscar recenseador: " + e.getMessage());
            return null;
        }
    }

    public List<Recenseador> listarRecenseadores() {
        try {
            return recenseadorDAO.listarTodos();
        } catch (SQLException e) {
            System.err.println("Erro ao listar recenseadores: " + e.getMessage());
            return new ArrayList<>();
        }
    }
}
