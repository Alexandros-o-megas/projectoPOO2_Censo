package service;

import com.itextpdf.text.Document;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;

import javax.swing.*;
import java.io.FileOutputStream;
import java.io.FileWriter;

public class Relatorios {

    public static void exportarPDF(String titulo, String conteudo) {
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
