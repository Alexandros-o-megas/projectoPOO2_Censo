package service;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.*;
import com.itextpdf.text.pdf.draw.LineSeparator;
import controller.CidadaoController;

import javax.swing.*;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.util.List;

public class Relatorios {

    public static void exportarPDF(String titulo, String conteudo) {

        try{
            Document document = new Document(PageSize.A4, 40, 40, 60, 40);
            String path = titulo.replaceAll("\\s+", "_") + ".pdf";
            PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(path));

            // ------- Cabeçalho e Rodapé Personalizados -------
            writer.setPageEvent(new PdfPageEventHelper() {

                Font headerFont = new Font(Font.FontFamily.HELVETICA, 10, Font.BOLD, BaseColor.GRAY);
                Font footerFont = new Font(Font.FontFamily.HELVETICA, 9, Font.NORMAL, BaseColor.GRAY);

                @Override
                public void onEndPage(PdfWriter writer, Document document) {
                    PdfPTable header = new PdfPTable(1);
                    header.setTotalWidth(520);
                    header.setHorizontalAlignment(Element.ALIGN_CENTER);

                    PdfPCell hCell = new PdfPCell(new Phrase("Sistema de Gestão Demográfico – Relatório Estatístico", headerFont));
                    hCell.setBorder(Rectangle.BOTTOM);
                    hCell.setHorizontalAlignment(Element.ALIGN_CENTER);
                    hCell.setPaddingBottom(5);
                    header.addCell(hCell);
                    header.writeSelectedRows(0, -1, 40, 825, writer.getDirectContent());

                    PdfPTable footer = new PdfPTable(1);
                    footer.setTotalWidth(520);
                    footer.setHorizontalAlignment(Element.ALIGN_CENTER);

                    PdfPCell fCell = new PdfPCell(new Phrase("Página " + writer.getPageNumber(), footerFont));
                    fCell.setBorder(Rectangle.TOP);
                    fCell.setHorizontalAlignment(Element.ALIGN_CENTER);
                    fCell.setPaddingTop(5);
                    footer.addCell(fCell);
                    footer.writeSelectedRows(0, -1, 40, 40, writer.getDirectContent());
                }
            });

            document.open();

            // ------- Título Principal -------
            Font tituloFont = new Font(Font.FontFamily.HELVETICA, 18, Font.BOLD, BaseColor.BLACK);
            Paragraph tituloParagrafo = new Paragraph(titulo, tituloFont);
            tituloParagrafo.setAlignment(Element.ALIGN_CENTER);
            tituloParagrafo.setSpacingAfter(20);
            document.add(tituloParagrafo);

            // ------- Linha Divisória -------
            LineSeparator ls = new LineSeparator();
            ls.setLineColor(BaseColor.DARK_GRAY);
            document.add(new Chunk(ls));
            document.add(new Paragraph("\n"));

            // ------- Conteúdo Formatado -------
            Font conteudoFont = new Font(Font.FontFamily.HELVETICA, 12, Font.NORMAL, BaseColor.BLACK);

            for (String linha : conteudo.split("\n")) {
                Paragraph p = new Paragraph(linha, conteudoFont);
                p.setSpacingAfter(5);
                document.add(p);
            }

            document.close();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static void exportar(String titulo, String conteudo) {
        try {
            Document document = new Document();
            String path = titulo.replaceAll("\\s+", "_") + ".pdf";
            PdfWriter.getInstance(document, new FileOutputStream(path));
            document.open();
            document.add(new Paragraph(titulo));
            document.add(new Paragraph("\n"));
            document.add(new Paragraph(conteudo));
            document.close();
            JOptionPane.showMessageDialog(null, "PDF exportado com sucesso: " + path);
        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(null, "Erro ao exportar PDF: " + ex.getMessage());
        }
    }

    public static void exportarTXT(String titulo, String conteudo) {
        try {
            String path = titulo.replaceAll("\\s+", "_") + ".txt";
            FileWriter writer = new FileWriter(path);
            writer.write(titulo + "\n\n" + conteudo);
            writer.close();
            JOptionPane.showMessageDialog(null, "TXT exportado com sucesso: " + path);
        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(null, "Erro ao exportar TXT: " + ex.getMessage());
        }
    }

}
