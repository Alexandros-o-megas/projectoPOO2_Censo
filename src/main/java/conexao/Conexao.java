package conexao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Conexao {

    private static final String URL = "jdbc:postgresql://localhost:5432/censo_database";
    private static final String USUARIO = "censouser";
    private static final String SENHA = "poooo2";

    private static Connection conexao = null;

    public static Connection getConexao() {
        if (conexao == null) {
            try {
                Class.forName("org.postgresql.Driver");
                conexao = DriverManager.getConnection(URL, USUARIO, SENHA);
            } catch (ClassNotFoundException e) {
                System.err.println("Driver PostgreSQL não encontrado: " + e.getMessage());
            } catch (SQLException e) {
                System.err.println("Erro ao conectar ao banco: " + e.getMessage());
            }
        }
        return conexao;
    }

    public static void fecharConexao() {
        if (conexao != null) {
            try {
                conexao.close();
                conexao = null;
            } catch (SQLException e) {
                System.err.println("Erro ao fechar a conexão: " + e.getMessage());
            }
        }
    }

}
