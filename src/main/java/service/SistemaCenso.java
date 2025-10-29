/*package service;

import model.*;
import java.io.*;
import java.util.*;
import java.nio.file.Files;
import java.nio.file.Paths;

public class SistemaCenso {
    private List<Bairro> bairros;
    private List<Familia> familias;
    private List<Recenseador> recenseadores;
    private final String ID_ADMIN = "admin";
    private String passwordAdmin = "admin123";
    private static final String DATA_DIR = "data";
    private static final String BAIRROS_FILE = DATA_DIR + "/bairros.dat";
    private static final String FAMILIAS_FILE = DATA_DIR + "/familias.dat";
    private static final String RECENSEADORES_FILE = DATA_DIR + "/recenseadores.dat";
    private static final String CONFIG_FILE = DATA_DIR + "/config.dat";

    public SistemaCenso() {
        this.bairros = new ArrayList<>();
        this.familias = new ArrayList<>();
        this.recenseadores = new ArrayList<>();
        criarDirectorioData();
        carregarDados();
    }

    private void criarDirectorioData() {
        File dir = new File(DATA_DIR);
        if (!dir.exists()) {
            if (dir.mkdirs()) {
                System.out.println(AnsiColors.GREEN + "Directorio 'data' criado com sucesso." + AnsiColors.RESET);
            } else {
                System.out.println(AnsiColors.RED_BOLD + "Falha ao criar directorio 'data'." + AnsiColors.RESET);
            }
        }
    }

    // --- Método de Login ---
    public boolean login(String adminId, String password) {
        ConsoleUtils.animarTexto(AnsiColors.YELLOW + "A verificar credenciais...", 50);
        ConsoleUtils.aguardar(500);
        boolean success = ID_ADMIN.equals(adminId) && this.passwordAdmin.equals(password);
        if (success) {
            System.out.println(AnsiColors.GREEN_BOLD + "Login bem-sucedido!" + AnsiColors.RESET);
        } else {
            System.out.println(AnsiColors.RED_BOLD + "ID de Administrador ou Password incorrectos." + AnsiColors.RESET);
        }
        ConsoleUtils.aguardar(1000);
        return success;
    }

    public void setPasswordAdmin(String newPassword) {
        this.passwordAdmin = newPassword;
        salvarConfig();
        System.out.println(AnsiColors.GREEN + "Password do administrador alterada com sucesso." + AnsiColors.RESET);
    }

    public String getAdminId() {
        return ID_ADMIN;
    }

    // --- Métodos CRUD(C-create R-read U-update D-delete) e de Negócio (Regras de Negócio) ---

    public boolean cadastrarBairro(Bairro bairro) {
        boolean encontrado = false;
        if (bairro == null) {
            encontrado = true;
        } else {
            for (Bairro b : bairros) {
                if (b.getIdBairro().equalsIgnoreCase(bairro.getIdBairro())) {
                    encontrado = true;
                    break;
                }
            }
        }
        if (encontrado) {
            System.out.println(AnsiColors.RED + "Bairro ja existe ou dados invalidos." + AnsiColors.RESET);
            return false;
        }
        bairros.add(bairro);
        System.out.println(
                AnsiColors.GREEN + "Bairro '" + bairro.getNome() + "' cadastrado com sucesso." + AnsiColors.RESET);
        return true;
    }

    public Optional<Bairro> encontrarBairroPorId(String idBairro) {
        return bairros.stream().filter(b -> b.getIdBairro().equalsIgnoreCase(idBairro)).findFirst();
    }

    public List<Bairro> getTodosBairros() {
        return new ArrayList<>(bairros);
    }

    public boolean cadastrarRecenseador(Recenseador recenseador) {
        boolean encontrado = false;
        if (recenseador == null) {
            encontrado = true;
        } else {
            for (Recenseador r : recenseadores) {
                if (r.getIdRecenseador().equalsIgnoreCase(recenseador.getIdRecenseador())) {
                    encontrado = true;
                    break;
                }
            }
        }
        if (encontrado) {
            System.out.println(AnsiColors.RED + "Recenseador ja existe ou dados inválidos." + AnsiColors.RESET);
            return false;
        }
        recenseadores.add(recenseador);
        System.out.println(AnsiColors.GREEN + "Recenseador '" + recenseador.getNome() + "' cadastrado com sucesso."
                + AnsiColors.RESET);
        return true;
    }

    public Optional<Recenseador> encontrarRecenseadorPorId(String idRecenseador) {
        return recenseadores.stream().filter(r -> r.getIdRecenseador().equalsIgnoreCase(idRecenseador)).findFirst();
    }

    public List<Recenseador> getTodosRecenseadores() {
        return new ArrayList<>(recenseadores);
    }

    public boolean cadastrarFamilia(Familia familia, Bairro bairro, Recenseador recenseador) {
        boolean encontrado = false;
        if (familia == null || bairro == null || recenseador == null) {
            encontrado = true;
        } else {
            for (Familia f : familias) {
                if (f.getIdFamilia().equalsIgnoreCase(familia.getIdFamilia())) {
                    encontrado = true;
                }
            }
        }
        if (encontrado) {
            System.out.println(AnsiColors.RED
                    + "Familia ja existe, bairro/recenseador nao encontrado ou dados invalidos." + AnsiColors.RESET);
            return false;
        }
        if (!bairros.contains(bairro)) {
            System.out.println(AnsiColors.RED + "Bairro especificado nao existe no sistema." + AnsiColors.RESET);
            return false;
        }
        if (!recenseadores.contains(recenseador)) {
            System.out.println(AnsiColors.RED + "Recenseador especificado nao existe no sistema." + AnsiColors.RESET);
            return false;
        }

        familia.setBairro(bairro);
        familia.setRecenseador(recenseador);
        familias.add(familia);

        bairro.adicionarFamilia(familia);
        recenseador.registrarFamilia(familia); // Este método pode precisar ser melhorado no Recenseador

        System.out.println(AnsiColors.GREEN + "Familia '" + familia.getNome() + "' cadastrada com sucesso no bairro '"
                + bairro.getNome() +
                "' pelo recenseador '" + recenseador.getNome() + "'." + AnsiColors.RESET);
        return true;
    }

    public Optional<Familia> encontrarFamiliaPorId(String idFamilia) {
        return familias.stream().filter(f -> f.getIdFamilia().equalsIgnoreCase(idFamilia)).findFirst();
    }

    public List<Familia> getTodasFamilias() {
        return new ArrayList<>(familias);
    }

    // --- Métodos para Geração de Relatórios ---

    public Relatorio gerarRelatorioPopulacaoTotal() {
        ConsoleUtils.mostrarProgresso(AnsiColors.YELLOW + "Calculando populacao total...", 20, 500);
        long totalCidadaos = 0;
        for (Familia f : familias) {
            int qtd = f.getMembros().size();
            totalCidadaos += qtd;
        }
        String conteudo = AnsiColors.GREEN_BOLD + "Populacao Total Registada: " + AnsiColors.WHITE_BOLD + totalCidadaos
                + " cidadaos.\n" + AnsiColors.RESET;
        conteudo += AnsiColors.CYAN + "Total de Familias: " + AnsiColors.WHITE_BOLD + familias.size() + "\n"
                + AnsiColors.RESET;
        conteudo += AnsiColors.CYAN + "Total de Bairros: " + AnsiColors.WHITE_BOLD + bairros.size() + "\n"
                + AnsiColors.RESET;
        return new Relatorio("Relatorio de Populacao Total", conteudo);
    }

    public Relatorio gerarDistribuicaoPorGenero() {
        ConsoleUtils.mostrarProgresso(AnsiColors.YELLOW + "Calculando distribuição por género...", 20, 500);
        Map<String, Long> contagemPorGenero = new HashMap<>();
        for (Familia familia : familias) {
            for (Cidadao cidadao : familia.getMembros()) {
                String genero = cidadao.getGenero();
                if (contagemPorGenero.containsKey(genero)) {
                    contagemPorGenero.put(genero, contagemPorGenero.get(genero) + 1);
                } else {
                    contagemPorGenero.put(genero, 1L);
                }
            }
        }

        StringBuilder sb = new StringBuilder();
        sb.append(AnsiColors.BLUE_BOLD).append("Distribuição de Cidadãos por Género:\n").append(AnsiColors.RESET);

        contagemPorGenero
                .forEach((genero, count) -> sb.append(AnsiColors.GREEN).append(" - ").append(genero).append(": ")
                        .append(AnsiColors.WHITE_BOLD).append(count).append(" cidadãos\n").append(AnsiColors.RESET));

        if (contagemPorGenero.isEmpty()) {
            sb.append(AnsiColors.YELLOW).append("Nenhum cidadão registado para análise de género.\n")
                    .append(AnsiColors.RESET);
        }

        return new Relatorio("Distribuição Populacional por Género", sb.toString());
    }

    public Relatorio gerarDistribuicaoPorEstadoCivil() {
        ConsoleUtils.mostrarProgresso(AnsiColors.YELLOW + "Calculando distribuição por estado civil...", 20, 500);
        Map<String, Long> contagemPorEstadoCivil = new HashMap<>();
        for (Familia f : familias) {
            for (Cidadao c : f.getMembros()) {
                String estadoCivil = c.getEstadoCivil();
                if(contagemPorEstadoCivil.containsKey(estadoCivil)){
                    contagemPorEstadoCivil.put(estadoCivil, contagemPorEstadoCivil.get(estadoCivil) + 1);
                }else{
                    contagemPorEstadoCivil.put(estadoCivil, 1L);
                }
            }
        }

        StringBuilder sb = new StringBuilder();
        sb.append(AnsiColors.BLUE_BOLD).append("Distribuição de Cidadãos por Estado Civil:\n").append(AnsiColors.RESET);
        contagemPorEstadoCivil
                .forEach((estado, count) -> sb.append(AnsiColors.GREEN).append(" - ").append(estado).append(": ")
                        .append(AnsiColors.WHITE_BOLD).append(count).append(" cidadãos\n").append(AnsiColors.RESET));
        if (contagemPorEstadoCivil.isEmpty()) {
            sb.append(AnsiColors.YELLOW).append("Nenhum cidadão registado para análise de estado civil.\n")
                    .append(AnsiColors.RESET);
        }
        return new Relatorio("Distribuição Populacional por Estado Civil", sb.toString());
    }

    public Relatorio gerarDistribuicaoPorProfissao() {
        ConsoleUtils.mostrarProgresso(AnsiColors.YELLOW + "Calculando distribuição por profissão...", 20, 500);
        Map<String, Long> contagemPorProfissao = new HashMap<>();
        for (Familia f : familias) {
            for (Cidadao c : f.getMembros()) {
                String prof = c.getOcupacao();
                if(contagemPorProfissao.containsKey(prof)){
                    contagemPorProfissao.put(prof, contagemPorProfissao.get(prof) + 1);
                }else{
                    contagemPorProfissao.put(prof, 1L);
                }
            }
        }

        StringBuilder sb = new StringBuilder();
        sb.append(AnsiColors.BLUE_BOLD).append("Distribuição de Cidadãos por Profissão:\n").append(AnsiColors.RESET);
        contagemPorProfissao.entrySet().stream()
                .sorted(Map.Entry.<String, Long>comparingByValue().reversed()) // Ordenar por contagem decrescente
                .forEach(entry -> sb.append(AnsiColors.GREEN).append(" - ").append(entry.getKey()).append(": ")
                        .append(AnsiColors.WHITE_BOLD).append(entry.getValue()).append(" cidadãos\n")
                        .append(AnsiColors.RESET));
        if (contagemPorProfissao.isEmpty()) {
            sb.append(AnsiColors.YELLOW).append("Nenhum cidadão registado para análise de profissão.\n")
                    .append(AnsiColors.RESET);
        }
        return new Relatorio("Distribuição Populacional por Profissão", sb.toString());
    }

    public Relatorio gerarDistribuicaoPorIdade(int idadeEspecifica) {
        ConsoleUtils.mostrarProgresso(
                AnsiColors.YELLOW + "Calculando distribuicao por idade (" + idadeEspecifica + ")...", 20, 500);
        long count = 0;
        for (Familia familia : familias) {
            for (Cidadao cidadao : familia.getMembros()) {
                if (cidadao.getIdade() == idadeEspecifica) {
                    count++;
                }
            }
        }
        String conteudo = AnsiColors.BLUE_BOLD + "Cidadaos com " + idadeEspecifica + " anos de idade: " +
                AnsiColors.WHITE_BOLD + count + " cidadaos.\n" + AnsiColors.RESET;
        return new Relatorio("Distribuicao por Idade Especifica (" + idadeEspecifica + " anos)", conteudo);
    }

    public Relatorio gerarDistribuicaoPorIntervaloDeIdades(int minIdade, int maxIdade) {
        if (minIdade > maxIdade) {
            return new Relatorio("Erro de Parametros",
                    AnsiColors.RED_BOLD + "Idade minima nao pode ser maior que a idade maxima." + AnsiColors.RESET);
        }
        ConsoleUtils.mostrarProgresso(AnsiColors.YELLOW + "Calculando distribuicao por intervalo de idades [" + minIdade
                + "-" + maxIdade + "]...", 20, 500);
        long count = 0;
        for (Familia f : familias) {
            for (Cidadao c : f.getMembros()) {
                int id = c.getIdade();
                if(id >= minIdade && id <= maxIdade ){
                    count++;
                }
            }
        }
        String conteudo = AnsiColors.BLUE_BOLD + "Cidadaos entre " + minIdade + " e " + maxIdade + " anos: " +
                AnsiColors.WHITE_BOLD + count + " cidadaos.\n" + AnsiColors.RESET;
        return new Relatorio("Distribuicao por Intervalo de Idades (" + minIdade + "-" + maxIdade + " anos)", conteudo);
    }

    // --- Persistência de Dados ---
    @SuppressWarnings("unchecked")
    public void carregarDados() {
        ConsoleUtils.mostrarProgresso(AnsiColors.YELLOW + "A carregar dados do sistema...", 30, 1000);
        bairros = (List<Bairro>) carregarObjetoDeFicheiro(BAIRROS_FILE);
        if (bairros == null)
            bairros = new ArrayList<>();

        recenseadores = (List<Recenseador>) carregarObjetoDeFicheiro(RECENSEADORES_FILE);
        if (recenseadores == null) {
            recenseadores = new ArrayList<>();
        } else {
            for (Recenseador rec : recenseadores) {
                if (rec.getFamiliasRegistadas() == null) {
                    rec.setFamiliasRegistadas(new ArrayList<>());
                }
            }
        }
        familias = (List<Familia>) carregarObjetoDeFicheiro(FAMILIAS_FILE);
        if (familias == null)
            familias = new ArrayList<>();
        else {
            for (Familia fam : familias) {
                if (fam.getBairro() != null) {
                    Optional<Bairro> bairroOpt = encontrarBairroPorId(fam.getBairro().getIdBairro());
                    bairroOpt.ifPresent(bairro -> {
                        fam.setBairro(bairro); 
                        bairro.adicionarFamilia(fam); 
                    });
                }
                if (fam.getRecenseador() != null) {
                    Optional<Recenseador> recOpt = encontrarRecenseadorPorId(fam.getRecenseador().getIdRecenseador());
                    recOpt.ifPresent(rec -> {
                        fam.setRecenseador(rec); 
                        rec.registrarFamilia(fam);
                    });
                }
            }
        }
        carregarConfig();
        System.out.println(AnsiColors.GREEN + "Dados carregados com sucesso." + AnsiColors.RESET);
    }

    public void salvarDados() {
        ConsoleUtils.mostrarProgresso(AnsiColors.YELLOW + "A salvar dados do sistema...", 30, 1000);
        salvarObjetoEmFicheiro(bairros, BAIRROS_FILE);
        salvarObjetoEmFicheiro(recenseadores, RECENSEADORES_FILE);
        salvarObjetoEmFicheiro(familias, FAMILIAS_FILE); 
        salvarConfig();
        System.out.println(AnsiColors.GREEN_BOLD + "Todos os dados foram salvos com sucesso!" + AnsiColors.RESET);
    }

    private void carregarConfig() {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(CONFIG_FILE))) {
            this.passwordAdmin = (String) ois.readObject();
        } catch (FileNotFoundException e) {
            System.out.println(AnsiColors.YELLOW + "Ficheiro de configuração (" + CONFIG_FILE
                    + ") não encontrado. Usando password padrão para admin." + AnsiColors.RESET);
        } catch (IOException | ClassNotFoundException e) {
            System.err.println(
                    AnsiColors.RED_BOLD + "Erro ao carregar configuração: " + e.getMessage() + AnsiColors.RESET);
        }
    }

    private void salvarConfig() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(CONFIG_FILE))) {
            oos.writeObject(this.passwordAdmin);
        } catch (IOException e) {
            System.err
                    .println(AnsiColors.RED_BOLD + "Erro ao salvar configuração: " + e.getMessage() + AnsiColors.RESET);
        }
    }

    private Object carregarObjetoDeFicheiro(String caminhoFicheiro) {
        try {
            if (!Files.exists(Paths.get(caminhoFicheiro))) {
                System.out.println(AnsiColors.YELLOW + "Ficheiro " + caminhoFicheiro
                        + " nao encontrado. Sera criado ao salvar." + AnsiColors.RESET);
                return null;
            }
            try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(caminhoFicheiro))) {
                return ois.readObject();
            }
        } catch (EOFException e) { // Ficheiro vazio
            System.out.println(AnsiColors.YELLOW + "Ficheiro " + caminhoFicheiro + " esta vazio." + AnsiColors.RESET);
            return null;
        } catch (IOException | ClassNotFoundException e) {
            System.err.println(AnsiColors.RED_BOLD + "Erro ao carregar de " + caminhoFicheiro + ": " + e.getMessage()
                    + AnsiColors.RESET);
            return null;
        }
    }

    private void salvarObjetoEmFicheiro(Object obj, String caminhoFicheiro) {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(caminhoFicheiro))) {
            oos.writeObject(obj);
        } catch (IOException e) {
            System.err.println(AnsiColors.RED_BOLD + "Erro ao salvar em " + caminhoFicheiro + ": " + e.getMessage()
                    + AnsiColors.RESET);
        }
    }
}*/