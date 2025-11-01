package controller;

import DAO.LoginDAO;
import model.Login;
import service.Utilitarios;

import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Arrays;

public class LoginController {
    private LoginDAO dao = new LoginDAO();

    public boolean registrarUsuario(String username, byte[] senha, String tipo) {
        if (username == null || username.isBlank() || senha == null) {
            System.err.println("Username, senha ou tipo inválidos!");
            return false;
        }

        Login login = new Login(username, senha, tipo);
        return dao.inserir(login);
    }

    public Login autenticar(String username, String senha) {
        Login login = dao.buscarPorUsername(username);
        if (login != null && Arrays.equals(login.getUserPassword(), Utilitarios.getHash(senha))) {
            return login; // sucesso
        }
        return null;
    }

    public List<Login> listarUsuarios() {
        return dao.listarTodos();
    }

    public boolean atualizarUsuario(int userId, byte[] novaSenha, String novoTipo) {
        return dao.atualizar(userId, novaSenha, novoTipo);
    }

    public boolean removerUsuario(int userId) {
        return dao.remover(userId);
    }
}
