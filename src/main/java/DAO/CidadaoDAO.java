package DAO;

import model.Cidadao;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CidadaoDAO {

    private Connection connection;

    public CidadaoDAO(Connection connection) {
        this.connection = connection;
    }

    // CREATE
    public void inserir(Cidadao cidadao) throws SQLException {
        String sql = "INSERT INTO cidadao (nome, ano_nascimento, genero, estado_civil, ocupacao, contacto, nacionalidade) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, cidadao.getNome());
            stmt.setDate(2, Date.valueOf(cidadao.getAnoNascimento()));
            stmt.setString(3, cidadao.getGenero());
            stmt.setString(4, cidadao.getEstadoCivil());
            stmt.setString(5, cidadao.getOcupacao());
            stmt.setString(6, cidadao.getContacto());
            stmt.setString(7, cidadao.getNacionalidade());
            stmt.executeUpdate();

            try (ResultSet rs = stmt.getGeneratedKeys()) {
                if (rs.next()) {
                    cidadao.setIdCidadao(rs.getInt(1));
                }
            }
        }
    }

    // READ by ID
    public Cidadao buscarPorId(int id) throws SQLException {
        String sql = "SELECT * FROM cidadao WHERE id_cidadao = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return new Cidadao(
                            rs.getInt("id_cidadao"),
                            rs.getString("nome"),
                            rs.getDate("ano_nascimento").toLocalDate(),
                            rs.getString("genero"),
                            rs.getString("estado_civil"),
                            rs.getString("ocupacao"),
                            rs.getString("contacto"),
                            rs.getString("nacionalidade")
                    );
                }
            }
        }
        return null;
    }

    // READ all
    public List<Cidadao> listarTodos() throws SQLException {
        List<Cidadao> lista = new ArrayList<>();
        String sql = "SELECT * FROM cidadao ORDER BY nome";
        try (PreparedStatement stmt = connection.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                Cidadao c = new Cidadao(
                        rs.getInt("id_cidadao"),
                        rs.getString("nome"),
                        rs.getDate("ano_nascimento").toLocalDate(),
                        rs.getString("genero"),
                        rs.getString("estado_civil"),
                        rs.getString("ocupacao"),
                        rs.getString("contacto"),
                        rs.getString("nacionalidade")
                );
                lista.add(c);
            }
        }
        return lista;
    }

    // UPDATE
    public void atualizar(Cidadao cidadao) throws SQLException {
        String sql = "UPDATE cidadao SET nome=?, ano_nascimento=?, genero=?, estado_civil=?, ocupacao=?, contacto=?, nacionalidade=? " +
                "WHERE id_cidadao=?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, cidadao.getNome());
            stmt.setDate(2, Date.valueOf(cidadao.getAnoNascimento()));
            stmt.setString(3, cidadao.getGenero());
            stmt.setString(4, cidadao.getEstadoCivil());
            stmt.setString(5, cidadao.getOcupacao());
            stmt.setString(6, cidadao.getContacto());
            stmt.setString(7, cidadao.getNacionalidade());
            stmt.setInt(8, cidadao.getIdCidadao());
            stmt.executeUpdate();
        }
    }

    // DELETE
    public void excluir(int id) throws SQLException {
        String sql = "DELETE FROM cidadao WHERE id_cidadao = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
        }
    }

    public int contarTodos() throws SQLException{
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery("SELECT COUNT(*) FROM cidadao");
            if(resultSet.next())
                return resultSet.getInt("COUNT");

        return 0;
    }
}
