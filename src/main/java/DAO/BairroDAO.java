package DAO;

import model.Bairro;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class BairroDAO {

    private Connection connection;

    // Construtor recebe a conexão já aberta
    public BairroDAO(Connection connection) {
        this.connection = connection;
    }

    // CREATE
    public void inserir(Bairro bairro) throws SQLException {
        String sql = "INSERT INTO bairro (nome) VALUES (?)";
        try (PreparedStatement stmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, bairro.getNome());
            stmt.executeUpdate();

            try (ResultSet rs = stmt.getGeneratedKeys()) {
                if (rs.next()) {
                    bairro.setIdBairro(rs.getInt(1));
                }
            }
        }
    }

    // READ by ID
    public Bairro buscarPorId(int id) throws SQLException {
        String sql = "SELECT id_bairro, nome FROM bairro WHERE id_bairro = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return new Bairro(rs.getInt("id_bairro"), rs.getString("nome"));
                }
            }
        }
        return null;
    }

    // READ all
    public List<Bairro> listarTodos() throws SQLException {
        List<Bairro> bairros = new ArrayList<>();
        String sql = "SELECT id_bairro, nome FROM bairro ORDER BY nome";
        try (PreparedStatement stmt = connection.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                Bairro bairro = new Bairro(rs.getInt("id_bairro"), rs.getString("nome"));
                bairros.add(bairro);
            }
        }
        return bairros;
    }

    // UPDATE
    public void atualizar(Bairro bairro) throws SQLException {
        String sql = "UPDATE bairro SET nome = ? WHERE id_bairro = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, bairro.getNome());
            stmt.setInt(2, bairro.getIdBairro());
            stmt.executeUpdate();
        }
    }

    // DELETE
    public void excluir(int id) throws SQLException {
        String sql = "DELETE FROM bairro WHERE id_bairro = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
        }
    }
}

