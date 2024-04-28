package com.azat4dev.demobooking.users.data.services;

import io.jsonwebtoken.Jwts;

public class JwtSecret {
    private String secret;

    private JwtSecret(String secret) {
        this.secret = secret;
    }

    public String getSecret() {
        return secret;
    }

    public static JwtSecret random() {
        return new JwtSecret(
            Jwts.SIG.HS256.key().build().toString()
        );
    }
}
