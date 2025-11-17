package org.project.service;
import java.security.MessageDigest;

public class PasswordHasher {


    public static String hashPassword(String password) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] hash = md.digest(password.getBytes());

            StringBuilder hexString = new StringBuilder();
            for (byte b : hash) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) {
                    hexString.append('0');
                }
                hexString.append(hex);
            }
            return hexString.toString();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }


    public static boolean checkPassword(String password, String hash) {
        String hashedPassword = hashPassword(password);
        return hashedPassword != null && hashedPassword.equals(hash);
    }
}