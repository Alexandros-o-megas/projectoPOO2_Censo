package DAO;

import model.Endereco;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class EnderecoDAO {

    private Connection connection;

    public EnderecoDAO(Connection connection) {
        this.connection = connection;
    }

    // CREATE
    public void inserir(Endereco endereco) throws SQLException {
        String sql = """
                INSERT INTO endereco (provincia, municipio_cidade, bairro_localidade, 
                                      rua_avenida, quarteirao, numero_casa)
                VALUES (?, ?, ?, ?, ?, ?)
                """;
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, endereco.getProvincia());
            stmt.setString(2, endereco.getMunicipioCidade());
            stmt.setString(3, endereco.getBairroLocalidade());
            stmt.setString(4, endereco.getRuaAvenida());
            stmt.setString(5, endereco.getQuarteirao());
            stmt.setString(6, endereco.getNumeroCasa());
            stmt.executeUpdate();
        }
    }

    // READ ALL
    public List<Endereco> listarTodos() throws SQLException {
        List<Endereco> lista = new ArrayList<>();
        String sql = "SELECT * FROM endereco ORDER BY provincia, municipio_cidade";
        try (PreparedStatement stmt = connection.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                Endereco e = new Endereco(
                        rs.getString("provincia"),
                        rs.getString("municipio_cidade"),
                        rs.getString("bairro_localidade"),
                        rs.getString("rua_avenida"),
                        rs.getString("quarteirao"),
                        rs.getString("numero_casa")
                );
                lista.add(e);
            }
        }
        return lista;
    }

    // UPDATE (usando todos os campos como referência)
    public void atualizar(Endereco antigo, Endereco novo) throws SQLException {
        String sql = """
                UPDATE endereco SET provincia=?, municipio_cidade=?, bairro_localidade=?, 
                                     rua_avenida=?, quarteirao=?, numero_casa=?
                WHERE provincia=? AND municipio_cidade=? AND bairro_localidade=? 
                      AND rua_avenida=? AND quarteirao=? AND numero_casa=?
                """;
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, novo.getProvincia());
            stmt.setString(2, novo.getMunicipioCidade());
            stmt.setString(3, novo.getBairroLocalidade());
            stmt.setString(4, novo.getRuaAvenida());
            stmt.setString(5, novo.getQuarteirao());
            stmt.setString(6, novo.getNumeroCasa());

            stmt.setString(7, antigo.getProvincia());
            stmt.setString(8, antigo.getMunicipioCidade());
            stmt.setString(9, antigo.getBairroLocalidade());
            stmt.setString(10, antigo.getRuaAvenida());
            stmt.setString(11, antigo.getQuarteirao());
            stmt.setString(12, antigo.getNumeroCasa());
            stmt.executeUpdate();
        }
    }

    // DELETE (baseado em todos os campos)
    public void excluir(Endereco endereco) throws SQLException {
        String sql = """
                DELETE FROM endereco
                WHERE provincia=? AND municipio_cidade=? AND bairro_localidade=? 
                      AND rua_avenida=? AND quarteirao=? AND numero_casa=?
                """;
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, endereco.getProvincia());
            stmt.setString(2, endereco.getMunicipioCidade());
            stmt.setString(3, endereco.getBairroLocalidade());
            stmt.setString(4, endereco.getRuaAvenida());
            stmt.setString(5, endereco.getQuarteirao());
            stmt.setString(6, endereco.getNumeroCasa());
            stmt.executeUpdate();
        }
    }
}

