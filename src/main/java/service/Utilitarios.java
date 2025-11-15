package service;

import model.Familia;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class Utilitarios {
    public static byte[] getHash(String senha){
        try {
            MessageDigest messageDigest = MessageDigest.getInstance("SHA-256");
            return messageDigest.digest(senha.getBytes());
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }
    public static String normalizarNome(String input) {
        if (input == null || input.trim().isEmpty()) {
            return "";
        }
        return Arrays.stream(input.split("\\."))
                .map(part -> part.isEmpty() ? "" :
                        part.substring(0, 1).toUpperCase() +
                                part.substring(1).toLowerCase())
                .collect(Collectors.joining(" "));
    }

    public static String desnormalizarNome(String input) {
        if (input == null || input.trim().isEmpty()) {
            return "";
        }

        // Remove acentos (opcional, mas recomendado)
        String semAcentos = java.text.Normalizer.normalize(input, java.text.Normalizer.Form.NFD)
                .replaceAll("\\p{M}", "");

        return Arrays.stream(semAcentos.trim().split("\\s+"))
                .map(String::toLowerCase)
                .collect(Collectors.joining("."));
    }


    public static int contarFamiliasCadastradasHoje(List<Familia> familias) {
        LocalDate hoje = LocalDate.now();
        int contador = 0;

        for (Familia f : familias) {
            if (f.getData() != null &&
                    f.getData().equals(hoje)) {
                contador++;
            }
        }
        return contador;
    }
}