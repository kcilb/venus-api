package com.neptunesoftware.venusApis.Util;

import org.springframework.stereotype.Component;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;
import java.security.spec.KeySpec;
import java.util.Base64;

@Component
public class LicenseManager {
    /**
     * Reference to the cipher
     */
    private static Cipher cipher;

    /**
     * 8 byte salt to be used is stored in this array
     */
    private final byte[] salt = {-87, -101, -56, 50, 86, 53, -29, 3};

    /**
     * iteration count
     */
    private static final int ITERATION_COUNT = 1024;

    /**
     * Key Strengh
     */
    private static final int KEY_STRENGTH = 128;

    /**
     * iteration count
     */
    private static SecretKey key;

    /**
     * Initialization Vector
     */
    private static final byte[] iv = {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};

    /**
     * Default password phrase
     */
    private static final String DEFAULT_NEW_PASS_PHRASE = "3v6sHu2gb8C6KR6MlWGuNRBHwn9pnzhw";


    public LicenseManager() throws Exception {
        super();
        // Create SecretKeyFactory
        SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA512");
        // Create PBEKeySpec
        KeySpec spec = new PBEKeySpec(DEFAULT_NEW_PASS_PHRASE.toCharArray(), salt, ITERATION_COUNT, KEY_STRENGTH);
        // Create SecretKey
        SecretKey tmp = factory.generateSecret(spec);
        // Create SecretKeySpec
        key = new SecretKeySpec(tmp.getEncoded(), "AES");
        // Get Cipher Instance
        cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
    }


    public String encrypt(final String plainStr) throws Exception {
        if (plainStr == null) {
            throw new IllegalArgumentException("The argument 'plainStr' is null");
        }
        // IvParameterSpec is a wrapper for an initialization vector
        IvParameterSpec ivspec = new IvParameterSpec(iv);
        // Initialize Cipher for ENCRYPT_MODE
        cipher.init(Cipher.ENCRYPT_MODE, key, ivspec);
        // Encrypt
        byte[] utf8EncryptedData = cipher.doFinal(plainStr.getBytes());
        // Encode bytes to base64 to get a string
        String base64EncryptedData = Base64.getEncoder().encodeToString(utf8EncryptedData);
        return base64EncryptedData;
    }

    /**
     * Decrypts a previously encrypted string
     *
     * @param encryptedStr the encrypted string
     * @return the clear text String
     */
    public String decrypt(final String encryptedStr) throws Exception {
        if (encryptedStr == null) {
            throw new IllegalArgumentException("The argument 'encryptedStr' is null");
        }
        // IvParameterSpec is a wrapper for an initialization vector
        IvParameterSpec ivspec = new IvParameterSpec(iv);
        // Initialize Cipher for DECRYPT_MODE
        cipher.init(Cipher.DECRYPT_MODE, key, ivspec);
        // Decode base64 to get bytes
        byte[] decryptedData = Base64.getDecoder().decode(encryptedStr);
        // Decrypt the decoded bytes
        byte[] utf8 = cipher.doFinal(decryptedData);
        // Decode using utf-8 to get the final plain string object
        return new String(utf8, "UTF8");
    }

    public static void main(String args[]) throws Exception {
        LicenseManager encrypter = new LicenseManager();
        //String encrypted = encrypter.encrypt("01-19-2024~FINCA UGANDA LIMITED (MDI)~E_COL");

        //String decrypted = encrypter.encrypt("04-14-2025~FINCA UGANDA LIMITED (MDI)~E_COL");

         String decrypted = encrypter.decrypt("ev+Si7LoS5Fi4h1t3D/70HiIWwY0mYpGIYXw/h6yAzyoVcH/btweV3moxJZ08de6TLnbY66a2rJk7IaJh+4pig==");

        System.out.println(decrypted);

//        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
//        LocalDate expiryDate = LocalDate.parse(expiryDt, formatter);
//        LocalDate coreDate = LocalDate.parse("2025/01/30", formatter);
//        LocalDate currentDate = LocalDate.now();
//        DateTimeFormatter inputFormatter = DateTimeFormatter.ofPattern("MM-dd-yyyy");
//        LocalDate date = LocalDate.parse("2024-12-31", inputFormatter);
//        DateTimeFormatter outputFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
//        String formattedDate = date.format(outputFormatter);
//        LocalDate formatedExpiryDate = LocalDate.parse(formattedDate, formatter);
//        System.out.println(ChronoUnit.DAYS.between(coreDate, formatedExpiryDate));

        // ozRsaYN5xMMR/Yf24JxLTA==,terbBYpcZIcgFRvvTgsP6A==
        //System.out.println("encrypted==>" + encrypted);

//        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
//        LocalDate expiryDate = LocalDate.parse("24/12/2024", formatter);
//        LocalDate currentDate = LocalDate.now();
//        long daysDiff = ChronoUnit.DAYS.between(currentDate, expiryDate);
        System.out.println(decrypted);

    }
}
