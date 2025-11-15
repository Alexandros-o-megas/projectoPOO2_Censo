package controller;

import DAO.FamiliaDAO;
import conexao.Conexao;
import model.*;

import javax.swing.*;
import java.sql.*;
import java.util.*;

public class FamiliaController {
    private FamiliaDAO familiaDAO;

    public FamiliaController(Connection connection) {
        this.familiaDAO = new FamiliaDAO(connection);
    }

    public FamiliaController(){
        this.familiaDAO = new FamiliaDAO(Conexao.getConexao());
    }

    public int adicionarFamilia(Familia familia) {
        try {
            return familiaDAO.inserir(familia);
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, e.getMessage(), "ERROR", JOptionPane.ERROR_MESSAGE);
            return -1;
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

    public Map<Integer, Integer> contarFamiliasPorRecenseador(){
        return familiaDAO.contarFamiliasPorRecenseador();
    }

    public int contarTotal(){
        try{
            return familiaDAO.contarTodos();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public int ultimoID(){
        return familiaDAO.lastId();
    }

    public int proximoID(){
        return familiaDAO.lastId()+1;
    }

    public List<Familia> familiasPorRecenseador(int nome){
        return familiaDAO.registosPorRecenseador(nome);
    }

    public boolean exist(int id_familia){
        return familiaDAO.familiaExiste(id_familia);
    }

    public int numeroFamiliaPorRecenseador(int idR){
        return familiaDAO.familiasPorRecenseador(idR);
    }
}
