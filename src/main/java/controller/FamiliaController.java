package controller;

import DAO.FamiliaDAO;
import model.*;
import java.sql.*;
import java.util.*;

public class FamiliaController {
    private FamiliaDAO familiaDAO;

    public FamiliaController(Connection connection) {
        this.familiaDAO = new FamiliaDAO(connection);
    }

    public void adicionarFamilia(Familia familia) {
        try {
            familiaDAO.inserir(familia);
            System.out.println("✅ Família adicionada com sucesso!");
        } catch (SQLException e) {
            System.err.println("Erro ao adicionar família: " + e.getMessage());
        }
    }

    public void atualizarFamilia(Familia familia) {
        try {
            familiaDAO.atualizar(familia);
            System.out.println("✅ Família atualizada com sucesso!");
        } catch (SQLException e) {
            System.err.println("Erro ao atualizar família: " + e.getMessage());
        }
    }

    public void removerFamilia(int idFamilia) {
        try {
            familiaDAO.deletar(idFamilia);
            System.out.println("✅ Família removida com sucesso!");
        } catch (SQLException e) {
            System.err.println("Erro ao remover família: " + e.getMessage());
        }
    }

    public Familia buscarFamiliaPorId(int idFamilia) {
        try {
            return familiaDAO.buscarPorId(idFamilia);
        } catch (SQLException e) {
            System.err.println("Erro ao buscar família: " + e.getMessage());
            return null;
        }
    }

    public List<Familia> listarFamilias() {
        try {
            return familiaDAO.listarTodos();
        } catch (SQLException e) {
            System.err.println("Erro ao listar famílias: " + e.getMessage());
            return new ArrayList<>();
        }
    }
}
