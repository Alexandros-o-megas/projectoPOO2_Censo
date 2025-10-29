package service;

import model.*;
import controller.*;
import conexao.Conexao;

import javax.swing.*;
import java.sql.Connection;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class SistemaCenso {

    private BairroController bairroController;
    private FamiliaController familiaController;
    private RecenseadorController recenseadorController;

    private final String ID_ADMIN = "admin";
    private String passwordAdmin = "admin123";

    public SistemaCenso() {
        Connection conn = Conexao.getConexao();
        this.bairroController = new BairroController(conn);
        this.familiaController = new FamiliaController(conn);
        this.recenseadorController = new RecenseadorController(conn);
    }

    // --- LOGIN ---
    public boolean login(String adminId, String password) {
        boolean success = ID_ADMIN.equals(adminId) && passwordAdmin.equals(password);
        if (success) {
            System.out.println("Login bem-sucedido!");
        } else {
            System.out.println("ID de Administrador ou Password incorretos.");
        }
        return success;
    }

    public void setPasswordAdmin(String newPassword) {
        this.passwordAdmin = newPassword;
        System.out.println("Password do administrador alterada com sucesso.");
    }

    public String getAdminId() {
        return ID_ADMIN;
    }

    // --- CRUD de Bairro ---
    public boolean cadastrarBairro(Bairro bairro) {
        return bairroController.adicionarBairro(bairro);
    }

    public Optional<Bairro> encontrarBairroPorId(int idBairro) {
        return bairroController.buscarPorId(idBairro);
    }

    public List<Bairro> getTodosBairros() {
        return bairroController.listarTodos();
    }

    // --- CRUD de Recenseador ---
    public boolean cadastrarRecenseador(Recenseador recenseador) {
        return recenseadorController.adicionarRecenseador(recenseador);
    }

    public Optional<Recenseador> encontrarRecenseadorPorId(int idRecenseador) {
        return recenseadorController.buscarRecenseadorPorId(idRecenseador);
    }

    public List<Recenseador> getTodosRecenseadores() {
        return recenseadorController.listarRecenseadores();
    }

    // --- CRUD de Família ---
    public boolean cadastrarFamilia(Familia familia) {
        if (familia.getBairro() == null || familia.getRecenseador() == null) {
            JOptionPane.showMessageDialog(null,"Bairro ou Recenseador não definidos para a família.", "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        Optional<Bairro> bairroOpt = bairroController.buscarPorId(familia.getBairro().getIdBairro());
        Optional<Recenseador> recOpt = recenseadorController.buscarRecenseadorPorId(familia.getRecenseador().getIdRecenseador());
        if (bairroOpt.isEmpty()) {
            JOptionPane.showMessageDialog (null, "Bairro não encontrado no sistema.", "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        if (recOpt.isEmpty()) {
            JOptionPane.showMessageDialog (null, "Recenseador não encontrado no sistema.", "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        familia.setBairro(bairroOpt.get());
        familia.setRecenseador(recOpt.get());

        return familiaController.adicionarFamilia(familia);

    }

    public Optional<Familia> encontrarFamiliaPorId(int idFamilia) {
        return familiaController.buscarFamiliaPorId(idFamilia);
    }

    public List<Familia> getTodasFamilias() {
        return familiaController.listarFamilias();
    }

    // --- Relatórios ---
    public Relatorio gerarRelatorioPopulacaoTotal() {
        List<Familia> familias = familiaController.listarFamilias();
        long totalCidadaos = familias.stream().mapToLong(f -> f.getMembros().size()).sum();

        String conteudo = "População total registrada: " + totalCidadaos + " cidadãos\n";
        conteudo += "Total de famílias: " + familias.size() + "\n";
        conteudo += "Total de bairros: " + bairroController.listarTodos().size() + "\n";

        return new Relatorio("Relatório de População Total", conteudo);
    }

    public Relatorio gerarDistribuicaoPorGenero() {
        List<Familia> familias = familiaController.listarFamilias();
        Map<String, Long> contagemPorGenero = new java.util.HashMap<>();

        for (Familia f : familias) {
            for (Cidadao c : f.getMembros()) {
                contagemPorGenero.put(c.getGenero(),
                        contagemPorGenero.getOrDefault(c.getGenero(), 0L) + 1);
            }
        }

        StringBuilder sb = new StringBuilder();
        sb.append("Distribuição de Cidadãos por Género:\n");
        contagemPorGenero.forEach((genero, count) -> sb.append(" - ").append(genero).append(": ").append(count).append("\n"));

        return new Relatorio("Distribuição Populacional por Género", sb.toString());
    }

    // Métodos semelhantes podem ser implementados para Estado Civil, Profissão e Idade
}
