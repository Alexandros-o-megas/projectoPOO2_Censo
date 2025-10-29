package DAO;

import model.*;
import java.sql.*;
import java.util.*;

public class RecenseadorDAO {
    private Connection connection;

    public RecenseadorDAO(Connection connection) {
        this.connection = connection;
    }

    // Inserir novo recenseador
    public void inserir(Recenseador recenseador) throws SQLException {
        String sql = "INSERT INTO recenseador (nome, contacto, id_admin, provincia, municipio_cidade, bairro_localidade, rua_avenida, quarteirao, numero_casa) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, recenseador.getNome());
            ps.setString(2, recenseador.getContacto());
            ps.setInt(3, recenseador.getIdAdmin());
            ps.setString(4, recenseador.getEndereco().getProvincia());
            ps.setString(5, recenseador.getEndereco().getMunicipioCidade());
            ps.setString(6, recenseador.getEndereco().getBairroLocalidade());
            ps.setString(7, recenseador.getEndereco().getRuaAvenida());
            ps.setString(8, recenseador.getEndereco().getQuarteirao());
            ps.setString(9, recenseador.getEndereco().getNumeroCasa());
            ps.executeUpdate();

            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    recenseador.setIdRecenseador(rs.getInt(1));
                }
            }
        }
    }

    // Atualizar dados do recenseador
    public void atualizar(Recenseador recenseador) throws SQLException {
        String sql = "UPDATE recenseador SET nome = ?, contacto = ?, id_admin = ?, provincia = ?, municipio_cidade = ?, bairro_localidade = ?, rua_avenida = ?, quarteirao = ?, numero_casa = ? "
                + "WHERE id_recenseador = ?";

        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, recenseador.getNome());
            ps.setString(2, recenseador.getContacto());
            ps.setInt(3, recenseador.getIdAdmin());
            ps.setString(4, recenseador.getEndereco().getProvincia());
            ps.setString(5, recenseador.getEndereco().getMunicipioCidade());
            ps.setString(6, recenseador.getEndereco().getBairroLocalidade());
            ps.setString(7, recenseador.getEndereco().getRuaAvenida());
            ps.setString(8, recenseador.getEndereco().getQuarteirao());
            ps.setString(9, recenseador.getEndereco().getNumeroCasa());
            ps.setInt(10, recenseador.getIdRecenseador());
            ps.executeUpdate();
        }
    }

    // Remover recenseador
    public void deletar(int idRecenseador) throws SQLException {
        String sql = "DELETE FROM recenseador WHERE id_recenseador = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, idRecenseador);
            ps.executeUpdate();
        }
    }

    // Buscar recenseador por ID
    public Recenseador buscarPorId(int idRecenseador) throws SQLException {
        String sql = "SELECT * FROM recenseador WHERE id_recenseador = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, idRecenseador);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    Endereco endereco = new Endereco(
                            rs.getString("provincia"),
                            rs.getString("municipio_cidade"),
                            rs.getString("bairro_localidade"),
                            rs.getString("rua_avenida"),
                            rs.getString("quarteirao"),
                            rs.getString("numero_casa")
                    );

                    return new Recenseador(
                            rs.getInt("id_recenseador"),
                            rs.getString("nome"),
                            rs.getString("contacto"),
                            endereco
                    );
                }
            }
        }
        return null;
    }

    // Listar todos os recenseadores
    public List<Recenseador> listarTodos() throws SQLException {
        List<Recenseador> lista = new ArrayList<>();
        String sql = "SELECT * FROM recenseador";
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Endereco endereco = new Endereco(
                        rs.getString("provincia"),
                        rs.getString("municipio_cidade"),
                        rs.getString("bairro_localidade"),
                        rs.getString("rua_avenida"),
                        rs.getString("quarteirao"),
                        rs.getString("numero_casa")
                );

                Recenseador recenseador = new Recenseador(
                        rs.getInt("id_recenseador"),
                        rs.getString("nome"),
                        rs.getString("contacto"),
                        endereco
                );
                recenseador.setIdAdmin(rs.getInt("id_admin"));
                lista.add(recenseador);
            }
        }
        return lista;
    }
}

