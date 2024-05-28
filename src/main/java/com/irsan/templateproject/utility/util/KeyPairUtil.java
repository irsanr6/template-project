package com.irsan.templateproject.utility.util;

import com.irsan.templateproject.exception.AppException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import org.springframework.util.ResourceUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.security.*;
import java.security.spec.EncodedKeySpec;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

import static com.irsan.templateproject.utility.constant.GlobalConstant.KEYSTORE_PATH_1;
import static com.irsan.templateproject.utility.constant.GlobalConstant.KEYSTORE_PATH_2;

/**
 * @author : Irsan Ramadhan
 * @email : irsan.ramadhan@iconpln.co.id
 * @date : 24/05/2024
 */
@Component
public class KeyPairUtil {
    LoggerUtil log = new LoggerUtil(KeyPairUtil.class);

    @Value(value = "${enable-generate-keypair}")
    private boolean enableGenerateKeypair;

    @Bean
    public void generateKeyPair() {
        if (!enableGenerateKeypair) {
            log.warn("KeyPair generation is disabled");
            return;
        }
        try {
            KeyPairGenerator kpg = KeyPairGenerator.getInstance("RSA");
            kpg.initialize(2048);

            KeyPair kp = kpg.generateKeyPair();
            PrivateKey s = kp.getPrivate();
            PublicKey p = kp.getPublic();

            String filePath = "%s/%s";
            String privateKey = "private_keypair.key";
            String publicKey = "public_keypair.key";
            try (FileOutputStream outS = new FileOutputStream(String.format(filePath, KEYSTORE_PATH_2, privateKey))) {
                outS.write(s.getEncoded());
            }

            try (FileOutputStream outP = new FileOutputStream(String.format(filePath, KEYSTORE_PATH_2, publicKey))) {
                outP.write(p.getEncoded());
            }

            log.info("KeyPair success generated");
            log.info("Private key and Public key saved in file with name : {}, {}", privateKey, publicKey);
        } catch (Exception e) {
            throw new AppException(e);
        }
    }

    public PrivateKey loadPrivateKey() {
        try {
            File privateKeyFile = ResourceUtils.getFile(KEYSTORE_PATH_1 + "private_keypair.key");
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
            File publicKeyFile = ResourceUtils.getFile(KEYSTORE_PATH_1 + "public_keypair.key");
            byte[] publicKeyBytes = Files.readAllBytes(publicKeyFile.toPath());

            KeyFactory pulicKeyFactory = KeyFactory.getInstance("RSA");
            EncodedKeySpec publicKeySpec = new X509EncodedKeySpec(publicKeyBytes);
            return pulicKeyFactory.generatePublic(publicKeySpec);
        } catch (IOException | NoSuchAlgorithmException | InvalidKeySpecException e) {
            throw new AppException(e);
        }
    }

}
