package com.truecodes.utilities.auth;

import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Encoders;
import io.jsonwebtoken.security.Keys;

import java.security.Key;

public class JwtKeyGenerator {
    public static void main(String[] args) {
        Key key = Keys.secretKeyFor(SignatureAlgorithm.HS256); // generates 256-bit key
        String base64Key = Encoders.BASE64.encode(key.getEncoded());
        System.out.println("Generated SECRET_KEY: " + base64Key);
    }
}
