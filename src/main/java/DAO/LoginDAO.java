package DAO;

import java.sql.*;
import java.util.*;
import model.Login;
import conexao.Conexao;

public class LoginDAO {

    // Inserir novo login
    public boolean inserir(Login login) {
        String sql = "INSERT INTO login (username, userpassword, usertype) VALUES (?, ?, ?)";
        try (Connection conn = Conexao.getConexao();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, login.getUsername());
            ps.setBytes(2, login.getUserPassword());
            ps.setString(3, login.getUserType());
            ps.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.err.println("Erro ao inserir login: " + e.getMessage());
            return false;
        }
    }

    // Buscar login por username
    public Login buscarPorUsername(String username) {
        String sql = "SELECT * FROM login WHERE username = ?";
        try (Connection conn = Conexao.getConexao();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, username);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return new Login(
                        rs.getInt("userid"),
                        rs.getString("username"),
                        rs.getBytes("userpassword"),
                        rs.getString("usertype")
                );
            }
        } catch (SQLException e) {
            System.err.println("Erro ao buscar login: " + e.getMessage());
        }
        return null;
    }

    // Listar todos os logins
    public List<Login> listarTodos() {
        List<Login> lista = new ArrayList<>();
        String sql = "SELECT * FROM login ORDER BY userid";
        try (Connection conn = Conexao.getConexao();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                lista.add(new Login(
                        rs.getInt("userid"),
                        rs.getString("username"),
                        rs.getBytes("userpassword"),
                        rs.getString("usertype")
                ));
            }
        } catch (SQLException e) {
            System.err.println("Erro ao listar logins: " + e.getMessage());
        }
        return lista;
    }

    // Atualizar senha e/ou tipo de usuário
    public boolean atualizar(int userId, byte[] novaSenha, String novoTipo) {
        String sql = "UPDATE login SET userpassword = ?, usertype = ? WHERE userid = ?";
        try (Connection conn = Conexao.getConexao();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setBytes(1, novaSenha);
            ps.setString(2, novoTipo);
            ps.setInt(3, userId);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Erro ao atualizar login: " + e.getMessage());
            return false;
        }
    }

    // Remover login
    public boolean remover(int userId) {
        String sql = "DELETE FROM login WHERE userid = ?";
        try (Connection conn = Conexao.getConexao();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, userId);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Erro ao remover login: " + e.getMessage());
            return false;
        }
    }
}
