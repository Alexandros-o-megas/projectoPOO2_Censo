package conexao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Conexao {

    private static final String URL = "jdbc:postgresql://localhost:5432/censo_database";
    private static final String USUARIO = "censouser";
    private static final String SENHA = "poooo2";

    // Mantém a referência da conexão (para controle opcional)
    private static Connection conexao = null;

    /** Obtém uma nova conexão ou reabre se estiver fechada */
    public static Connection getConexao() {
        try {
            // Garante que o driver do PostgreSQL esteja carregado
            Class.forName("org.postgresql.Driver");

            // Se a conexão ainda não existe ou foi fechada, cria uma nova
            if (conexao == null || conexao.isClosed()) {
                conexao = DriverManager.getConnection(URL, USUARIO, SENHA);
            }
            return conexao;

        } catch (ClassNotFoundException e) {
            System.err.println("⚠️ Driver PostgreSQL não encontrado: " + e.getMessage());
        } catch (SQLException e) {
            System.err.println("⚠️ Erro ao conectar ao banco: " + e.getMessage());
        }

        return null; // Retorna null se der erro
    }

    /** Fecha a conexão atual, se estiver aberta */
    public static void fecharConexao() {
        if (conexao != null) {
            try {
                if (!conexao.isClosed()) {
                    conexao.close();
                    System.out.println("✅ Conexão encerrada com sucesso.");
                }
                conexao = null;
            } catch (SQLException e) {
                System.err.println("⚠️ Erro ao fechar a conexão: " + e.getMessage());
            }
        }
    }
}
