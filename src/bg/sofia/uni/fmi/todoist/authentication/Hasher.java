package bg.sofia.uni.fmi.todoist.authentication;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import java.util.Base64;

public class Hasher {
    private static final int SALT_LENGTH = 16;
    private static final int ITERATION_COUNT = 65536;
    private static final int KEY_LENGTH = 128;
    private static final String COLON = ":";

    private Hasher() {
    }

    public static String hashPassword(String password) {
        byte[] salt = generateSalt();

        KeySpec spec = new PBEKeySpec(password.toCharArray(), salt, ITERATION_COUNT, KEY_LENGTH);

        SecretKeyFactory factory = getFactoryInstance();

        byte[] hash = generateHash(factory, spec);

        String encodedSalt = Base64.getEncoder().encodeToString(salt);
        String encodedHashedPassword = Base64.getEncoder().encodeToString(hash);

        return encodedSalt + COLON + encodedHashedPassword;
    }

    public static boolean checkPassword(String inputPassword, String storedHash) {
        String[] tokens = storedHash.split(COLON);
        String encodedSalt = tokens[0];
        String encodedPasswordHash = tokens[1];

        byte[] salt = Base64.getDecoder().decode(encodedSalt);
        byte[] storedPasswordHash = Base64.getDecoder().decode(encodedPasswordHash);

        KeySpec spec = new PBEKeySpec(inputPassword.toCharArray(), salt, ITERATION_COUNT, KEY_LENGTH);
        SecretKeyFactory factory = getFactoryInstance();

        byte[] hash = generateHash(factory, spec);

        return MessageDigest.isEqual(hash, storedPasswordHash);
    }

    private static byte[] generateSalt() {
        SecureRandom random = new SecureRandom();
        byte[] salt = new byte[SALT_LENGTH];

        random.nextBytes(salt);

        return salt;
    }

    private static SecretKeyFactory getFactoryInstance() {
        try {
            return SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    private static byte[] generateHash(SecretKeyFactory factory, KeySpec spec) {
        try {
            return factory.generateSecret(spec).getEncoded();
        } catch (InvalidKeySpecException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }
}
