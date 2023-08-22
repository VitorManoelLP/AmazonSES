package com.moneyflow.flow.configuration;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwsHeader;
import io.jsonwebtoken.SigningKeyResolverAdapter;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Base64;

@Component
public class KeyResolver extends SigningKeyResolverAdapter {

    @Override
    public Key resolveSigningKey(JwsHeader header, Claims claims) {
        return Keys.hmacShaKeyFor(generateSafeSecret());
    }

    private static byte[] generateSafeSecret() {
        byte[] key = JwtTokenValidator.JWT_KEY.getBytes();
        var encoder = Base64.getUrlEncoder().withoutPadding();
        return encoder.encode(key);
    }

}