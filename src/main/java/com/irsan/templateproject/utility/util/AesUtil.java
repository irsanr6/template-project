package com.irsan.templateproject.utility.util;

import com.irsan.templateproject.exception.AppException;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.UUID;

/**
 * @author : Irsan Ramadhan
 * @email : irsan.ramadhan@iconpln.co.id
 * @date : 28/05/2024
 */
@Component
public class AesUtil {

    private final LoggerUtil log = new LoggerUtil(AesUtil.class);
    private final Environment env;

    private String aesSecretKey;

    public AesUtil(Environment env) {
        this.env = env;
        initializeSecretKey();
    }

    private void initializeSecretKey() {
        this.aesSecretKey = env.getProperty("aes-secret-key");
        if (aesSecretKey == null || aesSecretKey.isEmpty()) {
            log.warn("AES secret key is empty");
            this.aesSecretKey = UUID.randomUUID().toString().replace("-", "");
            log.warn("Generated new AES secret key : {}", this.aesSecretKey);
        }
    }

    public String decrypt(String encryptedText) {
        try {
            byte[] decodedEncryptedText = Base64.getDecoder().decode(encryptedText);

            byte[] decodedSecretKey = Base64.getDecoder().decode(aesSecretKey);
            SecretKeySpec secretKeySpec = new SecretKeySpec(decodedSecretKey, "AES");

            byte[] iv = new byte[16];
            System.arraycopy(decodedEncryptedText, 0, iv, 0, 16);

            byte[] encryptedBytes = new byte[decodedEncryptedText.length - 16];
            System.arraycopy(decodedEncryptedText, 16, encryptedBytes, 0, encryptedBytes.length);

            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            cipher.init(Cipher.DECRYPT_MODE, secretKeySpec, new IvParameterSpec(iv));

            byte[] decryptedBytes;

            decryptedBytes = cipher.doFinal(encryptedBytes);

            return new String(decryptedBytes, StandardCharsets.UTF_8);
        } catch (InvalidAlgorithmParameterException | NoSuchPaddingException | IllegalBlockSizeException |
                 NoSuchAlgorithmException | BadPaddingException | InvalidKeyException e) {
            throw new AppException(e);
        }
    }

    public String encrypt(String plainText) {
        try {
            byte[] decodedSecretKey = Base64.getDecoder().decode(aesSecretKey);
            SecretKeySpec secretKeySpec = new SecretKeySpec(decodedSecretKey, "AES");

            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec);

            byte[] iv = cipher.getIV();
            byte[] encryptedBytes = cipher.doFinal(plainText.getBytes(StandardCharsets.UTF_8));

            byte[] encryptedText = new byte[iv.length + encryptedBytes.length];
            System.arraycopy(iv, 0, encryptedText, 0, iv.length);
            System.arraycopy(encryptedBytes, 0, encryptedText, iv.length, encryptedBytes.length);

            return Base64.getEncoder().encodeToString(encryptedText);
        } catch (NoSuchPaddingException | NoSuchAlgorithmException | IllegalBlockSizeException |
                 BadPaddingException | InvalidKeyException e) {
            throw new AppException(e);
        }
    }

}
