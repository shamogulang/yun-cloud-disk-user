package com.example.cloudiskuser.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.time.Instant;

@Component
@Slf4j
public class ThirdAuthHelper {

    @Value("${third.auth.secret:}")
    private String secret;

    public  String generateSignature(String serviceName,  String timestamp) throws Exception {
        String message = serviceName + timestamp;
        Mac hmacSha256 = Mac.getInstance("HmacSHA256");
        SecretKeySpec secretKey = new SecretKeySpec(secret.getBytes(StandardCharsets.UTF_8), "HmacSHA256");
        hmacSha256.init(secretKey);
        byte[] hash = hmacSha256.doFinal(message.getBytes(StandardCharsets.UTF_8));
        return bytesToHex(hash);
    }

    private  String bytesToHex(byte[] bytes) {
        StringBuilder hex = new StringBuilder();
        for (byte b : bytes) {
            hex.append(String.format("%02x", b));
        }
        return hex.toString();
    }

    public  boolean verify(String serviceName, String timestamp, String receivedSignature) throws Exception {
        // time check
        long now = Instant.now().getEpochSecond();
        long requestTime = Long.parseLong(timestamp);
        if (Math.abs(now - requestTime) > 300) { // exceeding five minutes
            log.error("the time of service exceed 5 minutes");
            return false;
        }
        // re-signature
        String expectedSignature = this.generateSignature(serviceName,  timestamp);
        return expectedSignature.equalsIgnoreCase(receivedSignature);
    }
}
