package service;

import controller.BairroController;
import controller.CidadaoController;
import controller.FamiliaController;

import java.util.Map;

public class RelatorioService {

    private static FamiliaController familiaController = new FamiliaController();
    private static CidadaoController cidadaoController = new CidadaoController();
    private static BairroController bairroController = new BairroController();

    // ==========================================
    //        RELATÓRIOS DEMOGRÁFICOS
    // ==========================================

    public static String getPopulacaoTotal() {
        int populacaoTotal = cidadaoController.contarTodos();
        int familiasTotal = familiaController.contarTotal();
        int bairrosTotal = bairroController.contarTodos();
        float mediaCidadaosPorFamilia = (float) populacaoTotal / familiasTotal;

        String relatorio =
                "RELATÓRIO ESTATÍSTICO – POPULAÇÃO E GERAL\n" +
                        "------------------------------------------------------------\n" +
                        "1. População Total\n" +
                        "Total de cidadãos registados: " + populacaoTotal + "\n\n" +
                        "2. Famílias Registadas\n" +
                        "Total de famílias cadastradas: " + familiasTotal + "\n\n" +
                        "3. Bairros Abrangidos\n" +
                        "Total de bairros registados: " + bairrosTotal + "\n\n" +
                        "4. Média de Cidadãos por Família\n" +
                        "Média calculada: " + mediaCidadaosPorFamilia + "\n";

        return relatorio;

    }

    public static String getDistribuicaoGenero() {
        Map<String, Integer> lista = cidadaoController.contarGenero();

        int masculino = lista.getOrDefault("Masculino", 0);
        int feminino  = lista.getOrDefault("Feminino", 0);
        int total = masculino + feminino;

        String relatorio =
                "RELATÓRIO ESTATÍSTICO – DISTRIBUIÇÃO POR GÉNERO\n" +
                        "------------------------------------------------------------\n" +
                        "1. Género Masculino:\n" +
                        "   Total registado: " + masculino + "\n\n" +
                        "2. Género Feminino:\n" +
                        "   Total registado: " + feminino + "\n\n" +
                        "3. Total Geral:\n" +
                        "   Total de cidadãos registados: " + total + "\n";

        return relatorio;

    }

    public static String getDistribuicaoFaixaEtaria() {
        Map<String, Integer> lista = cidadaoController.contarCidadaosPorFaixaEtaria();

        StringBuilder relatorio = new StringBuilder();
        relatorio.append("RELATÓRIO ESTATÍSTICO – DISTRIBUIÇÃO POR FAIXA ETÁRIA\n");
        relatorio.append("------------------------------------------------------------\n");

        for (Map.Entry<String, Integer> entry : lista.entrySet()) {
            relatorio.append(entry.getKey())
                    .append(": ")
                    .append(entry.getValue())
                    .append("\n");
        }

        return relatorio.toString();
    }


    public static String getDensidadePopulacional() {
        double areaKm2 = 150.8;
        int populacao = 12450;
        double densidade = populacao / areaKm2;

        return "DENSIDADE POPULACIONAL\n" +
                "----------------------------------\n" +
                "Área total: " + areaKm2 + " km²\n" +
                "População: " + populacao + "\n" +
                String.format("Densidade: %.2f habitantes por km²", densidade);
    }


    // ==========================================
    //        RELATÓRIOS SOCIAIS/ECONÓMICOS
    // ==========================================

    public static String getEstadoCivil() {
        Map<String, Integer> lista = cidadaoController.distribuicaoEstadoCivil();

        StringBuilder relatorio = new StringBuilder();
        relatorio.append("RELATÓRIO ESTATÍSTICO – ESTADO CIVIL\n");
        relatorio.append("------------------------------------------------------------\n");

        for (Map.Entry<String, Integer> entry : lista.entrySet()) {
            relatorio.append(entry.getKey())
                    .append(": ")
                    .append(entry.getValue())
                    .append("\n");
        }

        return relatorio.toString();

    }

    public static String getTamanhoFamilias() {
        return """
                TAMANHO DAS FAMÍLIAS
                ----------------------------------
                1 membro: 1.200 famílias
                2 membros: 2.000 famílias
                3 membros: 1.900 famílias
                4 membros: 1.600 famílias
                5+ membros: 900 famílias
                """;
    }

    public static String getDistribuicaoOcupacao() {
        Map<String, Integer> lista = cidadaoController.distribuicaoPorProfissao();

        StringBuilder relatorio = new StringBuilder();
        relatorio.append("RELATÓRIO ESTATÍSTICO – DISTRIBUIÇÃO POR OCUPAÇÃO\n");
        relatorio.append("------------------------------------------------------------\n");

        for (Map.Entry<String, Integer> entry : lista.entrySet()) {
            relatorio.append(entry.getKey())
                    .append(": ")
                    .append(entry.getValue())
                    .append("\n");
        }

        return relatorio.toString();

    }


    // ==========================================
    //        RELATÓRIOS OPERACIONAIS
    // ==========================================

    public static String getProdutividade() {
        return """
                PRODUTIVIDADE OPERACIONAL
                ----------------------------------
                Registos por dia: 420
                Registos por semana: 2.900
                Registos por mês: 11.600
                
                Média por operador: 85 registos/dia
                Operadores activos: 12
                """;
    }

    public static String getAtividadeRegisto() {
        return """
                ATIVIDADE DE REGISTO
                ----------------------------------
                Hoje: 30 registos
                Ontem: 395 registos
                Últimos 7 dias: 2.850 registos
                Últimos 30 dias: 11.300 registos
                """;
    }
}
