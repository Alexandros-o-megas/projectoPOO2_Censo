package DAO;

import conexao.Conexao;
import model.*;

import javax.swing.*;
import java.net.URI;
import java.sql.*;
import java.util.*;

public class FamiliaDAO {
    private Connection connection;

    public FamiliaDAO(Connection connection) {
        this.connection = connection;
    }

    public FamiliaDAO() {
        this.connection = connection;
    }

    // Inserir nova família
    public void inserir(Familia familia) throws SQLException {
        String sql = "INSERT INTO familia (nome, id_bairro, id_recenseador_cadastro) VALUES (?, ?, ?)";
        try (PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, familia.getNome());
            ps.setInt(2, familia.getBairro().getIdBairro());
            ps.setInt(3, familia.getRecenseador().getIdRecenseador());
            ps.executeUpdate();

            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    familia.setIdFamilia(rs.getInt(1));
                }
            }
        }
    }

    // Atualizar dados da família
    public void atualizar(Familia familia) throws SQLException {
        String sql = "UPDATE familia SET nome = ?, id_bairro = ?, id_recenseador_cadastro = ? WHERE id_familia = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, familia.getNome());
            ps.setInt(2, familia.getBairro().getIdBairro());
            ps.setInt(3, familia.getRecenseador().getIdRecenseador());
            ps.setInt(4, familia.getIdFamilia());
            ps.executeUpdate();
        }
    }

    // Remover família
    public void deletar(int idFamilia) throws SQLException {
        String sql = "DELETE FROM familia WHERE id_familia = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, idFamilia);
            ps.executeUpdate();
        }
    }

    // Buscar por ID
    public Familia buscarPorId(int idFamilia) throws SQLException {
        String sql = "SELECT * FROM familia WHERE id_familia = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, idFamilia);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    Bairro bairro = new Bairro(rs.getInt("id_bairro"), buscarNomeBairro(rs.getInt("id_bairro")));
                    Recenseador recenseador = new Recenseador(rs.getInt("id_recenseador_cadastro"), buscarNomeRecenseador(rs.getInt("id_recenseador_cadastro")));
                    return new Familia(
                            rs.getInt("id_familia"),
                            rs.getString("nome"),
                            bairro,
                            recenseador
                    );
                }
            }
        }
        return null;
    }

    // Listar todas as famílias
    public List<Familia> listarTodos() throws SQLException {
        List<Familia> familias = new ArrayList<>();
        String sql = """
                SELECT
                    f.id_familia,
                    f.nome,
                    f.id_bairro,
                    f.id_recenseador_cadastro,
                    COUNT(c.id_cidadao) AS total_membros
                FROM
                    familia f
                LEFT JOIN
                    cidadao c ON f.id_familia = c.id_familia
                GROUP BY
                    f.id_familia, f.nome, f.id_bairro, f.id_recenseador_cadastro
                ORDER BY
                    f.nome;
                """;
        Statement stmt = connection.createStatement();
        ResultSet rs = stmt.executeQuery(sql);

            while (rs.next()) {
                Bairro bairro = new Bairro(rs.getInt("id_bairro"), buscarNomeBairro(rs.getInt("id_bairro")));
                Recenseador recenseador = new Recenseador(rs.getInt("id_recenseador_cadastro"), buscarNomeRecenseador(rs.getInt("id_recenseador_cadastro")));

                familias.add(new Familia(
                        rs.getInt("id_familia"),
                        rs.getString("nome"),
                        bairro,
                        recenseador,
                        rs.getInt("total_membros")
                ));
            }

        return familias;
    }

    // Métodos auxiliares para nomes de FK
    private String buscarNomeBairro(int idBairro) throws SQLException {
        String sql = "SELECT nome FROM bairro WHERE id_bairro = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, idBairro);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return rs.getString("nome");
            }
        }
        return "Desconhecido";
    }

    private String buscarNomeRecenseador(int idRecenseador) throws SQLException {
        String sql = "SELECT nome FROM recenseador WHERE id_recenseador = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, idRecenseador);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return rs.getString("nome");
            }
        }
        return "Desconhecido";
    }

    public Map<Integer, Integer> contarFamiliasPorRecenseador() {
        Map<Integer, Integer> resultado = new HashMap<>();
        String sql = "SELECT id_recenseador_cadastro, COUNT(*) AS total FROM familia GROUP BY id_recenseador_cadastro";

        try {
            Connection conn = Conexao.getConexao();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                int idRecenseador = rs.getInt("id_recenseador_cadastro");
                int total = rs.getInt("total");
                resultado.put(idRecenseador, total);
            }

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Erro ao contar famílias por recenseador: " + e.getMessage());
        }

        return resultado;
    }

    public int contarTodos() throws SQLException {
        Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery("SELECT COUNT(*) AS total FROM familia");
            if (resultSet.next())
                return resultSet.getInt("total");
        return 0;
    }

    public int lastId(){
        int lastId = 0;
        String sql = " SELECT last_value FROM familia_id_familia_seq ";
        try(Connection conn = Conexao.getConexao();
            Statement statement = conn.createStatement();
            ResultSet resultSet = statement.executeQuery(sql)){
            if(resultSet.next())
                lastId = resultSet.getInt("last_value");
        }catch (Exception eee){
            JOptionPane.showMessageDialog(null, "Erro: "+eee.getMessage(), "ERROR", JOptionPane.ERROR_MESSAGE);
        }
        return lastId;
    }

    public List<Familia> registosPorRecenseador(int idR){
        String sql = """
                SELECT
                    f.id_familia,
                    f.nome,
                    f.id_bairro,
                    f.id_recenseador_cadastro,
                    f.datacadastro,
                    COUNT(c.id_cidadao) AS total_membros
                FROM
                    familia f
                LEFT JOIN
                    cidadao c ON f.id_familia = c.id_familia
                WHERE f.id_recenseador_cadastro = '""" + idR + """
                'GROUP BY
                    f.id_familia, f.nome, f.id_bairro, f.id_recenseador_cadastro
                ORDER BY
                    f.nome;
                """;
        List<Familia> list = new ArrayList<>();
        try(Connection conn = Conexao.getConexao();
            Statement statement = conn.createStatement();
            ResultSet resultSet = statement.executeQuery(sql)){
            while (resultSet.next()) {
                Bairro bairro = new Bairro(resultSet.getInt("id_bairro"), buscarNomeBairro(resultSet.getInt("id_bairro")));
                Recenseador recenseador = new Recenseador(resultSet.getInt("id_recenseador_cadastro"), buscarNomeRecenseador(resultSet.getInt("id_recenseador_cadastro")));

                list.add(new Familia(
                        resultSet.getInt("id_familia"),
                        resultSet.getString("nome"),
                        bairro,
                        recenseador,
                        resultSet.getInt("total_membros"),
                        resultSet.getDate("datacadastro")
                ));
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Erro:"+ e.getMessage());
        }
        return list;
    }

    public int familiasPorRecenseador(int idR){
        String sql = "SELECT COUNT(*) AS total FROM FAMILIA WHERE id_recenseador_cadastro = ?";
        int cont = 0;
        try(Connection conn = Conexao.getConexao();
            PreparedStatement preparedStatement = conn.prepareStatement(sql)){
            preparedStatement.setInt(1,idR);

            try(ResultSet rs = preparedStatement.executeQuery()) {
                if (rs.next())
                    cont = rs.getInt("total");
            }

        }catch (SQLException eww){
            JOptionPane.showMessageDialog(null, eww.getMessage(), "ERROR", JOptionPane.ERROR_MESSAGE);
        }
        return cont;
    }

}
