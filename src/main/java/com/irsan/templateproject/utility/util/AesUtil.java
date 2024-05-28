package com.irsan.templateproject.utility.util;

import com.irsan.templateproject.exception.AppException;
import org.springframework.beans.factory.annotation.Value;
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

/**
 * @author : Irsan Ramadhan
 * @email : irsan.ramadhan@iconpln.co.id
 * @date : 28/05/2024
 */
@Component
public class AesUtil {

    @Value("${aes.secret.key}")
    private String aesMimsSecretKey;

    public String decrypt(String encryptedText) {
        try {
            byte[] decodedEncryptedText = Base64.getDecoder().decode(encryptedText);

            byte[] decodedSecretKey = Base64.getDecoder().decode(aesMimsSecretKey);
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

}
