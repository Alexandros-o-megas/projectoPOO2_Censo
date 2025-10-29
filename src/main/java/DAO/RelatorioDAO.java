package DAO;

import model.Relatorio;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class RelatorioDAO {
    private Connection connection;

    public RelatorioDAO(Connection connection) {
        this.connection = connection;
    }

    // Inserir um novo relatório
    public void inserir(Relatorio relatorio) throws SQLException {
        String sql = "INSERT INTO relatorio (titulo, conteudo, data_geracao) VALUES (?, ?, ?)";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, relatorio.getTitulo());
            ps.setString(2, relatorio.getConteudo());
            ps.setDate(3, Date.valueOf(relatorio.getDataGeracao()));
            ps.executeUpdate();
        }
    }

    // Buscar relatório por título
    public Relatorio buscarPorTitulo(String titulo) throws SQLException {
        String sql = "SELECT * FROM relatorio WHERE titulo = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, titulo);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new Relatorio(
                            rs.getString("titulo"),
                            rs.getString("conteudo")
                    );
                }
            }
        }
        return null;
    }

    // Listar todos os relatórios
    public List<Relatorio> listarTodos() throws SQLException {
        List<Relatorio> lista = new ArrayList<>();
        String sql = "SELECT * FROM relatorio ORDER BY data_geracao DESC";
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Relatorio relatorio = new Relatorio(
                        rs.getString("titulo"),
                        rs.getString("conteudo")
                );
                lista.add(relatorio);
            }
        }
        return lista;
    }

    // Deletar relatório por título
    public void deletarPorTitulo(String titulo) throws SQLException {
        String sql = "DELETE FROM relatorio WHERE titulo = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, titulo);
            ps.executeUpdate();
        }
    }

    // Deletar todos os relatórios (por exemplo, para limpar histórico)
    public void deletarTodos() throws SQLException {
        try (Statement stmt = connection.createStatement()) {
            stmt.executeUpdate("DELETE FROM relatorio");
        }
    }
}
