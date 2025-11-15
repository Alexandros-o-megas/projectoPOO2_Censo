package DAO;

import conexao.Conexao;
import model.Bairro;

import javax.swing.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class BairroDAO {

    private Connection connection;

    public BairroDAO(Connection connection) {
        this.connection = connection;
    }

    // Criar novo bairro
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

    public int buscarId(String nome){
        int id = 0;
        String sql = """
                SELECT id_bairro FROM bairro
                WHERE nome = ?
                """;
        try(Connection conn = Conexao.getConexao();
            PreparedStatement statement = conn.prepareStatement(sql)) {
            statement.setString(1, nome);
            try(ResultSet rs = statement.executeQuery()){
                if(rs.next())
                    id = rs.getInt("id_bairro");
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e.getMessage(), "ERROR", JOptionPane.ERROR_MESSAGE);
        }
        return id;
    }

    // READ all
    public List<Bairro> listarTodos() throws SQLException {
        List<Bairro> bairros = new ArrayList<>();
        String sql = """
                SELECT
                    b.id_bairro,
                    b.nome,
                    COUNT(f.id_familia) AS total
                FROM
                    bairro b
                LEFT JOIN
                    familia f ON b.id_bairro = f.id_bairro
                GROUP BY
                    b.id_bairro, b.nome
                ORDER BY
                    b.nome;
                
                """;
        PreparedStatement stmt = connection.prepareStatement(sql);
        ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                Bairro bairro = new Bairro(rs.getInt("id_bairro"), rs.getString("nome"), rs.getInt("total"));
                bairros.add(bairro);
            }
        return bairros;
    }

    public List<String> getNomes(){
        List<String> nomes = new ArrayList<>();
        try(Connection connection = Conexao.getConexao();
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT nome FROM bairro")){

            while (resultSet.next()){
                nomes.add(resultSet.getString("nome"));
            }

        }catch (Exception ee){
            JOptionPane.showMessageDialog(null, "Erro: "+ ee.getMessage(), "ERROR", JOptionPane.ERROR_MESSAGE);
        }
        return nomes;
    }

    public int total() throws SQLException{
        String sql = "SELECT COUNT(*) FROM bairro";
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery(sql);
            if (resultSet.next())
                return resultSet.getInt("COUNT");

        return 0;
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

    public int count(){
        String sql = "SELECT COUNT(*) as total FROM bairro";
        int total = 0;
        try(Connection connection = Conexao.getConexao();
            Statement statement = connection.createStatement();
            ResultSet rs = statement.executeQuery(sql)) {
            if(rs.next())
                total = rs.getInt("total");
        }catch (SQLException ee){
            JOptionPane.showMessageDialog(null,"Erro: "+ee.getMessage(), "ERROR",JOptionPane.ERROR_MESSAGE);
        }
        return total;
    }
}

