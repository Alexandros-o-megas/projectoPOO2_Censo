package service;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Utilitarios {
    public static byte[] getHash(String senha){
        try {
            MessageDigest messageDigest = MessageDigest.getInstance("SHA-256");
            return messageDigest.digest(senha.getBytes());
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }
}