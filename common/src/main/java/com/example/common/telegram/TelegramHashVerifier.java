package com.example.common.telegram;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.security.MessageDigest;
import java.util.*;

public class TelegramHashVerifier {

    private final String botToken;

    public TelegramHashVerifier(String botToken) {
        this.botToken = botToken;
    }

    public boolean isHashValid(TelegramAuthRequest request) {
        Map<String, String> data = new HashMap<>();
        if (request.getTelegramUserId() != null) data.put("id", request.getTelegramUserId());
        if (request.getUsername() != null) data.put("username", request.getUsername());
        if (request.getFirstName() != null) data.put("first_name", request.getFirstName());
        if (request.getLastName() != null) data.put("last_name", request.getLastName());
        if (request.getPhotoUrl() != null) data.put("photo_url", request.getPhotoUrl());
        if (request.getAuthDate() != null) data.put("auth_date", request.getAuthDate());
        if (request.getHash() != null) data.put("hash", request.getHash());

        return isHashValid(data);
    }

    public boolean isHashValid(Map<String, String> data) {
        String receivedHash = data.get("hash");

        Map<String, String> sortedData = new TreeMap<>(data);
        sortedData.remove("hash");

        StringBuilder dataCheckString = new StringBuilder();
        for (Map.Entry<String, String> entry : sortedData.entrySet()) {
            dataCheckString.append(entry.getKey()).append("=").append(entry.getValue()).append("\n");
        }

        if (!dataCheckString.isEmpty()) {
            dataCheckString.setLength(dataCheckString.length() - 1);
        }

        String secretKey = sha256(botToken);
        String computedHash = hmacSha256(dataCheckString.toString(), secretKey);

        return computedHash.equals(receivedHash);
    }

    private String sha256(String input) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] bytes = digest.digest(input.getBytes());
            return bytesToHex(bytes);
        } catch (Exception e) {
            throw new RuntimeException("SHA-256 error", e);
        }
    }

    private String hmacSha256(String data, String key) {
        try {
            Mac hmac = Mac.getInstance("HmacSHA256");
            SecretKeySpec secretKey = new SecretKeySpec(key.getBytes(), "HmacSHA256");
            hmac.init(secretKey);
            byte[] hash = hmac.doFinal(data.getBytes());
            return bytesToHex(hash);
        } catch (Exception e) {
            throw new RuntimeException("HMAC error", e);
        }
    }

    private String bytesToHex(byte[] bytes) {
        StringBuilder hex = new StringBuilder();
        for (byte b : bytes) {
            hex.append(String.format("%02x", b));
        }
        return hex.toString();
    }
}
