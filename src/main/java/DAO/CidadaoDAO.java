package DAO;

import conexao.Conexao;
import model.Cidadao;

import javax.swing.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class CidadaoDAO {

    public CidadaoDAO() {
    }

    public void inserir(Cidadao cidadao) throws SQLException {
        String sql = "INSERT INTO cidadao (nome, data_nascimento, genero, estado_civil, ocupacao, nacionalidade, id_familia) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = Conexao.getConexao();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, cidadao.getNome());
            stmt.setDate(2, Date.valueOf(cidadao.getAnoNascimento()));
            stmt.setString(3, cidadao.getGenero());
            stmt.setString(4, cidadao.getEstadoCivil());
            stmt.setString(5, cidadao.getOcupacao());
            stmt.setString(6, cidadao.getNacionalidade());
            stmt.setInt(7, cidadao.getId_familia());
            stmt.executeUpdate();

            try (ResultSet rs = stmt.getGeneratedKeys()) {
                if (rs.next()) {
                    cidadao.setIdCidadao(rs.getInt(1));
                }
            }
        }
    }

    public Cidadao buscarPorId(int id) throws SQLException {
        String sql = "SELECT * FROM cidadao WHERE id_cidadao = ?";
        try (PreparedStatement stmt = Conexao.getConexao().prepareStatement(sql)) {
            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return new Cidadao(
                            rs.getInt("id_cidadao"),
                            rs.getInt("id_familia"),
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

    public List<Cidadao> listarTodos() throws SQLException {
        List<Cidadao> lista = new ArrayList<>();
        String sql = """
                SELECT
                    c.*,
                    cc.contacto
                FROM cidadao c
                LEFT JOIN contactocidadao cc
                       ON cc.id_cidadao = c.id_cidadao
                ORDER BY c.nome
                """;
        try (PreparedStatement stmt = Conexao.getConexao().prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                Cidadao c = new Cidadao(
                        rs.getInt("id_cidadao"),
                        rs.getInt("id_familia"),
                        rs.getString("nome"),
                        rs.getDate("data_nascimento").toLocalDate(),
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

    public void atualizar(Cidadao cidadao) throws SQLException {
        String sql = "UPDATE cidadao SET nome=?, ano_nascimento=?, genero=?, estado_civil=?, ocupacao=?, contacto=?, nacionalidade=? " +
                "WHERE id_cidadao=?";
        try (PreparedStatement stmt = Conexao.getConexao().prepareStatement(sql)) {
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

    public void excluir(int id) throws SQLException {
        String sql = "DELETE FROM cidadao WHERE id_cidadao = ?";
        try (PreparedStatement stmt = Conexao.getConexao().prepareStatement(sql)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
        }
    }

    public int contarTodos() throws SQLException{
        Connection connection = Conexao.getConexao();
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery("SELECT COUNT(*) FROM cidadao");
            if(resultSet.next())
                return resultSet.getInt("COUNT");

        return 0;
    }

    public Map<String, Integer> obterDistribuicaoPorFaixaEtaria() {
        Map<String, Integer> distribuicao = new LinkedHashMap<>();

        String sql = """
            SELECT
                  CASE
                      WHEN calcular_idade(data_nascimento) BETWEEN 0 AND 12 THEN 'Crianças'
                      WHEN calcular_idade(data_nascimento) BETWEEN 13 AND 19 THEN 'Adolescentes'
                      WHEN calcular_idade(data_nascimento) BETWEEN 20 AND 35 THEN 'Jovens'
                      WHEN calcular_idade(data_nascimento) BETWEEN 36 AND 59 THEN 'Adultos'
                      ELSE 'Idosos'
                  END AS faixa_etaria,
                  COUNT(*) AS total
              FROM cidadao
              GROUP BY faixa_etaria
              ORDER BY total;
        """;

        try (Connection conn = Conexao.getConexao();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                String faixa = rs.getString("faixa_etaria");
                int total = rs.getInt("total");
                distribuicao.put(faixa, total);
            }

        } catch (SQLException e) {
            System.err.println("Erro ao obter distribuição por faixa etária: " + e.getMessage());
        }

        return distribuicao;
    }

    public Map<String, Integer> generoDivision(){
        Map<String, Integer> divisao = new LinkedHashMap<>();
        String sql = """
            SELECT
                genero,
                COUNT(*) AS total
            FROM
                cidadao
            GROUP BY
                genero
            ORDER BY
                total DESC
        """;

        try (Connection connection = Conexao.getConexao();
             Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                String genero = rs.getString("genero");
                int total = rs.getInt("total");

                // Evita valores nulos ou vazios
                if (genero == null || genero.isBlank()) {
                    genero = "Não especificado";
                }

                divisao.put(genero, total);
            }

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null,
                    "Erro ao gerar distribuição por género: " + e.getMessage(),
                    "Erro",
                    JOptionPane.ERROR_MESSAGE);
        }
        return divisao;
    }

    public Map<String, Integer> contarPorOcupacao() throws SQLException {
        String sql = """
            SELECT
                ocupacao,
                COUNT(*) AS total
            FROM
                cidadao
            GROUP BY
                ocupacao
            ORDER BY
                total DESC
            """;

        Map<String, Integer> resultado = new LinkedHashMap<>();

        try (PreparedStatement ps = Conexao.getConexao().prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                String ocupacao = rs.getString("ocupacao");
                int total = rs.getInt("total");
                resultado.put(ocupacao, total);
            }
        }

        return resultado;
    }

    public Map<String, Integer> contarPorEstadoCivil() throws SQLException {
        String sql = """
            SELECT
                estado_civil,
                COUNT(*) AS total
            FROM
                cidadao
            GROUP BY
                estado_civil
            ORDER BY
                total DESC
            """;

        Map<String, Integer> resultado = new LinkedHashMap<>();

        try (PreparedStatement ps = Conexao.getConexao().prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                String estadoCivil = rs.getString("estado_civil");
                int total = rs.getInt("total");
                resultado.put(estadoCivil, total);
            }
        }

        return resultado;
    }
}
