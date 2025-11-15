package controller;

import DAO.RecenseadorDAO;
import conexao.Conexao;
import model.*;

import javax.swing.*;
import java.sql.*;
import java.util.*;

public class RecenseadorController {
    private RecenseadorDAO recenseadorDAO;

    public RecenseadorController(Connection connection) {
        this.recenseadorDAO = new RecenseadorDAO(connection);
    }

    public RecenseadorController() {
        this.recenseadorDAO = new RecenseadorDAO(Conexao.getConexao());
    }

    public boolean adicionarRecenseador(Recenseador recenseador) {
        try {
            recenseadorDAO.inserir(recenseador);
            return true;
        } catch (SQLException e) {
            return false;
        }
    }

    public boolean atualizarRecenseador(Recenseador recenseador) {
        try {
            recenseadorDAO.atualizar(recenseador);
            return true;
        } catch (SQLException e) {
            return false;
        }
    }

    public boolean removerRecenseador(int idRecenseador) {
        try {
            recenseadorDAO.deletar(idRecenseador);
            return true;
        } catch (SQLException e) {
            return false;
        }
    }

    public Recenseador buscarRecenseadorPorId(int idRecenseador) {
        try {
            Optional<Recenseador> rec = recenseadorDAO.buscarPorId(idRecenseador);
            return rec.get();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog (null,"Erro ao buscar recenseador: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            return null;
        }
    }

    public int contarTodos(){
        try {
            return recenseadorDAO.contarTodos();
        } catch (SQLException e) {
            throw new RuntimeException(e);
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

    public int buscarIdRecenseador(String nome){
        return recenseadorDAO.buscarIdRecenseador(nome);
    }
}
