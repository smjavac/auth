package ru.said;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class SecurityUtils {

    private SecurityUtils() {
    }

    public static String hash(String hashpass) throws NoSuchAlgorithmException {
        MessageDigest sha256 = MessageDigest.getInstance("SHA-256");
        byte[] bytes = sha256.digest(hashpass.getBytes());
        StringBuilder strBuilder = new StringBuilder();
        for (byte b : bytes)
            strBuilder.append(b);
        return strBuilder.toString();
    }

}
