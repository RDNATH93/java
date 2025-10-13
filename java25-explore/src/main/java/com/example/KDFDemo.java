package com.example;

import java.security.SecureRandom;

import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;

//Key Derivation Function API
/**
 * Encrypting file with password
 */
public class KDFDemo {

    public static void main(String[] args) throws Exception {
        // password provided by user
        char[] password = "password".toCharArray();

        // generate random salt for extra security
        byte[] salt = new byte[16];
        new SecureRandom().nextBytes(salt);

        // PBKDF2 - Password Based Key Derivation Function
        PBEKeySpec spec = new PBEKeySpec(password, salt, 65536, 256);

        // create ket factory and generate the derived Key
        SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");
        SecretKey derivedKey = factory.generateSecret(spec);

        IO.println("Derived Key (hex)" + bytesToHex(derivedKey.getEncoded()));
        IO.println("Salt used (hex)" + bytesToHex(salt));

    }

    private static String bytesToHex(byte[] bytes) {
        StringBuilder sb = new StringBuilder();

        for (byte b : bytes) {
            sb.append(String.format("%02x", b));
        }
        return sb.toString();
    }
}
