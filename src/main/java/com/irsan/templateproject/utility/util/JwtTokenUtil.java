package com.irsan.templateproject.utility.util;

import io.jsonwebtoken.*;
import lombok.RequiredArgsConstructor;
import org.slf4j.MDC;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.UUID;

import static com.irsan.templateproject.utility.constant.GlobalConstant.EXPIRE_DURATION;

/**
 * @author : Irsan Ramadhan
 * @email : irsan.ramadhan@iconpln.co.id
 * @date : 23/05/2024
 */
@Component
@RequiredArgsConstructor
public class JwtTokenUtil {

    private final LoggerUtil log = new LoggerUtil(JwtTokenUtil.class);
    private final KeyPairUtil keyPairUtil;
    private final FileUtil fileUtil;

    public String generateAccessToken(String username) {
        return Jwts.builder()
                .setSubject(username)
                .setIssuer("Template Project 2024")
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRE_DURATION))
                .signWith(SignatureAlgorithm.RS256, keyPairUtil.loadPrivateKey())
//                .compressWith(CompressionCodecs.GZIP)
                .setId(UUID.randomUUID().toString())
                .compact();

    }

    public boolean validateAccessToken(String token) {
        String requestId = MDC.get("requestId");
        try {
            Jwts.parser().setSigningKey(keyPairUtil.loadPublicKey()).parseClaimsJws(token);
            return true;
        } catch (ExpiredJwtException ex) {
            log.error("JWT expired: {}", ex.getMessage());
            fileUtil.printStackTrace(ex, String.format("log/prinstacktrace/logging_%s.log", requestId));
        } catch (IllegalArgumentException ex) {
            log.error("Token is null, empty or only whitespace: {}", ex.getMessage());
            fileUtil.printStackTrace(ex, String.format("log/prinstacktrace/logging_%s.log", requestId));
        } catch (MalformedJwtException ex) {
            log.error("JWT is invalid: {}", ex.getMessage());
            fileUtil.printStackTrace(ex, String.format("log/prinstacktrace/logging_%s.log", requestId));
        } catch (UnsupportedJwtException ex) {
            log.error("JWT is not supported: {}", ex.getMessage());
            fileUtil.printStackTrace(ex, String.format("log/prinstacktrace/logging_%s.log", requestId));
        } catch (SignatureException ex) {
            log.error("Signature validation failed: {}", ex.getMessage());
            fileUtil.printStackTrace(ex, String.format("log/prinstacktrace/logging_%s.log", requestId));
        }

        return false;
    }

    public String getSubject(String token) {
        return parseClaims(token).getSubject();
    }

    public LocalDateTime getExpiration(String token) {
        long timeMillis = parseClaims(token).getExpiration().getTime();
        return Instant.ofEpochMilli(timeMillis)
                .atZone(ZoneId.systemDefault())
                .toLocalDateTime();
    }

    public String getJwtId(String token) {
        return parseClaims(token).getId();
    }

    private Claims parseClaims(String token) {
        return Jwts.parser()
                .setSigningKey(keyPairUtil.loadPublicKey())
                .parseClaimsJws(token)
                .getBody();
    }

}
