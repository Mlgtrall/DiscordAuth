package ru.mlgtrall.discordauth.security;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

//TODO: add more algorithms / Use BCrypt API?
public class Hash {
    private static final String algorithm = "SHA-256";
    private static final short saltLength = 32;

    public static @NotNull String generateHash(@NotNull String data, @NotNull String salt) {
        try {
            MessageDigest md = MessageDigest.getInstance(algorithm);
            md.reset();
            md.update(salt.getBytes());
            byte[] hash = md.digest(data.getBytes());
            return bytesToStringHex(hash);
        }catch (NoSuchAlgorithmException e){
            e.printStackTrace();
        }
        return "";
    }

    public static @NotNull String generateHash(@NotNull String data) throws NoSuchAlgorithmException {
        MessageDigest md = MessageDigest.getInstance(algorithm);
        md.reset();
        byte[] hash = md.digest(data.getBytes());
        return bytesToStringHex(hash);
    }

    public static @NotNull String generateHash(@NotNull String data, byte[] salt) throws NoSuchAlgorithmException {
        MessageDigest md = MessageDigest.getInstance(algorithm);
        md.reset();
        md.update(salt);
        byte[] hash = md.digest(data.getBytes());
        return bytesToStringHex(hash);
    }

    public static @NotNull String generateHash(@NotNull String data, String algorithm, byte[] salt) throws NoSuchAlgorithmException {
        MessageDigest md = MessageDigest.getInstance(algorithm);
        md.reset();
        md.update(salt);
        byte[] hash = md.digest(data.getBytes());
        return bytesToStringHex(hash);
    }


    private final static char[] hexArray = "0123456789ABCDEF".toCharArray();

    @Contract(value = "_ -> new", pure = true)
    public static @NotNull String bytesToStringHex(byte @NotNull [] bytes){
        char[] hexChars = new char[bytes.length * 2];
        for (int i = 0; i < bytes.length ; i++) {
            int v  = bytes[i] & 0xFF;
            hexChars[i * 2] = hexArray[v >> 4];
            hexChars[i * 2 + 1] = hexArray[v & 0x0F];
        }
        return new String(hexChars);
    }

    public static byte @NotNull [] createSalt(){
        byte[] bytes = new byte[saltLength];
        SecureRandom random = new SecureRandom();
        random.nextBytes(bytes);
        return bytes;
    }

    public static @NotNull String createSaltStr(){
        byte[] bytes = new byte[saltLength];
        SecureRandom random = new SecureRandom();
        random.nextBytes(bytes);
        return bytesToStringHex(bytes);
    }
}
