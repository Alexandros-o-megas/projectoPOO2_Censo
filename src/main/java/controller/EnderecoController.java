package controller;

import DAO.EnderecoDAO;
import model.Endereco;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public class EnderecoController {
    private EnderecoDAO enderecoDAO;

    public EnderecoController(Connection connection) {
        this.enderecoDAO = new EnderecoDAO(connection);
    }

    public void adicionarEndereco(String provincia, String municipioCidade, String bairroLocalidade,
                                  String ruaAvenida, String quarteirao, String numeroCasa) {
        try {
            Endereco endereco = new Endereco(provincia, municipioCidade, bairroLocalidade, ruaAvenida, quarteirao, numeroCasa);
            enderecoDAO.inserir(endereco);
            System.out.println("✅ Endereço adicionado com sucesso: " + endereco);
        } catch (SQLException e) {
            System.err.println("❌ Erro ao adicionar endereço: " + e.getMessage());
        }
    }

    public void atualizarEndereco(Endereco antigo, Endereco novo) {
        try {
            enderecoDAO.atualizar(antigo, novo);
            System.out.println("✅ Endereço atualizado com sucesso.");
        } catch (SQLException e) {
            System.err.println("❌ Erro ao atualizar endereço: " + e.getMessage());
        }
    }

    public void removerEndereco(Endereco endereco) {
        try {
            enderecoDAO.excluir(endereco);
            System.out.println("✅ Endereço removido: " + endereco);
        } catch (SQLException e) {
            System.err.println("❌ Erro ao remover endereço: " + e.getMessage());
        }
    }

    public List<Endereco> listarTodos() {
        try {
            return enderecoDAO.listarTodos();
        } catch (SQLException e) {
            System.err.println("❌ Erro ao listar endereços: " + e.getMessage());
            return List.of();
        }
    }
}
