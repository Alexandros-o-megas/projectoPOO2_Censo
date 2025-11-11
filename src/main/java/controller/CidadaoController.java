package controller;

import DAO.CidadaoDAO;
import model.Cidadao;

import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

public class CidadaoController {
    private CidadaoDAO cidadaoDAO;

    public CidadaoController(Connection connection) {
        this.cidadaoDAO = new CidadaoDAO(connection);
    }

    public CidadaoController() {
        this.cidadaoDAO = new CidadaoDAO();
    }

    public void adicionarCidadao(String nome, LocalDate anoNascimento, String genero,
                                 String estadoCivil, String ocupacao, String contacto, String nacionalidade) {
        try {
            Cidadao cidadao = new Cidadao(nome, anoNascimento, genero, estadoCivil, ocupacao, contacto, nacionalidade);
            cidadaoDAO.inserir(cidadao);
            System.out.println("✅ Cidadão adicionado com sucesso: " + cidadao);
        } catch (SQLException e) {
            System.err.println("❌ Erro ao adicionar cidadão: " + e.getMessage());
        }
    }

    public void atualizarCidadao(int id, String nome, LocalDate anoNascimento, String genero,
                                 String estadoCivil, String ocupacao, String contacto, String nacionalidade) {
        try {
            Cidadao cidadao = cidadaoDAO.buscarPorId(id);
            if (cidadao != null) {
                cidadao.setNome(nome);
                cidadao.setEstadoCivil(estadoCivil);
                cidadao.setOcupacao(ocupacao);
                cidadao.setContacto(contacto);
                cidadao.setNacionalidade(nacionalidade);
                cidadaoDAO.atualizar(cidadao);
                System.out.println("✅ Cidadão atualizado: " + cidadao);
            } else {
                System.out.println("⚠️ Cidadão com ID " + id + " não encontrado.");
            }
        } catch (SQLException e) {
            System.err.println("❌ Erro ao atualizar cidadão: " + e.getMessage());
        }
    }

    public void removerCidadao(int id) {
        try {
            cidadaoDAO.excluir(id);
            System.out.println("✅ Cidadão removido (ID: " + id + ")");
        } catch (SQLException e) {
            System.err.println("❌ Erro ao remover cidadão: " + e.getMessage());
        }
    }

    public Cidadao buscarPorId(int id) {
        try {
            return cidadaoDAO.buscarPorId(id);
        } catch (SQLException e) {
            System.err.println("❌ Erro ao buscar cidadão: " + e.getMessage());
            return null;
        }
    }

    public List<Cidadao> listarTodos() {
        try {
            return cidadaoDAO.listarTodos();
        } catch (SQLException e) {
            System.err.println("❌ Erro ao listar cidadãos: " + e.getMessage());
            return List.of();
        }
    }

    public int contarTodos(){
        try {
            return cidadaoDAO.contarTodos();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public Map<String, Integer> contarCidadaosPorFaixaEtaria() {
        return cidadaoDAO.obterDistribuicaoPorFaixaEtaria();
    }

    public Map<String, Integer> contarGenero(){
        return cidadaoDAO.generoDivision();
    }

}
