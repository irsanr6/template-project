package com.irsan.templateproject.utility.util;

import com.irsan.templateproject.exception.AppException;
import org.springframework.stereotype.Component;
import org.springframework.util.ResourceUtils;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.EncodedKeySpec;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

import static com.irsan.templateproject.utility.constant.GlobalConstant.KEYSTORE_PATH;

/**
 * @author : Irsan Ramadhan
 * @email : irsan.ramadhan@iconpln.co.id
 * @date : 24/05/2024
 */
@Component
public class KeyPairUtil {

    public PrivateKey loadPrivateKey() {
        try {
            File privateKeyFile = ResourceUtils.getFile(KEYSTORE_PATH + "private_key_1716557358382_0.6783708362488501.key");
            byte[] privateKeyBytes = Files.readAllBytes(privateKeyFile.toPath());

            KeyFactory privateKeyFactory = KeyFactory.getInstance("RSA");
            EncodedKeySpec privateKeySpec = new PKCS8EncodedKeySpec(privateKeyBytes);
            return privateKeyFactory.generatePrivate(privateKeySpec);
        } catch (IOException | NoSuchAlgorithmException | InvalidKeySpecException e) {
            throw new AppException(e);
        }
    }

    public PublicKey loadPublicKey() {
        try {
            File publicKeyFile = ResourceUtils.getFile(KEYSTORE_PATH + "public_key_1716557358382_0.6783708362488501.key");
            byte[] publicKeyBytes = Files.readAllBytes(publicKeyFile.toPath());

            KeyFactory pulicKeyFactory = KeyFactory.getInstance("RSA");
            EncodedKeySpec publicKeySpec = new X509EncodedKeySpec(publicKeyBytes);
            return pulicKeyFactory.generatePublic(publicKeySpec);
        } catch (IOException | NoSuchAlgorithmException | InvalidKeySpecException e) {
            throw new AppException(e);
        }
    }

}
