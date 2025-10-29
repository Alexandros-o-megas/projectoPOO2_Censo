package model;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class Relatorio {
    private String titulo;
    private String conteudo;
    private LocalDate dataGeracao;

    public Relatorio(String titulo, String conteudo) {
        this.titulo = titulo;
        this.conteudo = conteudo;
        this.dataGeracao = LocalDate.now();
    }

    public String getTitulo() {
        return titulo;
    }

    public String getConteudo() {
        return conteudo;
    }

    public LocalDate getDataGeracao() {
        return dataGeracao;
    }

    public void exibir() {
        System.out.println("--- RELATÓRIO ---");
        System.out.println("Título: " + titulo);
        System.out.println("Data de Geração: " + dataGeracao.format(DateTimeFormatter.ISO_LOCAL_DATE));
        System.out.println("Conteúdo:\n" + conteudo);
        System.out.println("--- FIM DO RELATÓRIO ---");
    }

    public void exportarPDF() {
        // Implementação da exportação para PDF (requer bibliotecas externas como iText ou Apache PDFBox)
        System.out.println("Funcionalidade de exportar para PDF para '" + titulo + "' ainda não implementada.");
    }
}
