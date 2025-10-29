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

    public boolean adicionarFamilia(Familia familia) {
        try {
            familiaDAO.inserir(familia);
            return true;
        } catch (SQLException e) {
            return false;
        }
    }

    public boolean atualizarFamilia(Familia familia) {
        try {
            familiaDAO.atualizar(familia);
            return true;
        } catch (SQLException e) {
            return false;
        }
    }

    public boolean removerFamilia(int idFamilia) {
        try {
            familiaDAO.deletar(idFamilia);
            return true;
        } catch (SQLException e) {
            //System.err.println("Erro ao remover família: " + e.getMessage());
            return false;
        }
    }

    public Optional<Familia> buscarFamiliaPorId(int idFamilia) {
        try {
            Familia fam = familiaDAO.buscarPorId(idFamilia);
            return Optional.of(fam);
        } catch (SQLException e) {
            return Optional.empty();
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
