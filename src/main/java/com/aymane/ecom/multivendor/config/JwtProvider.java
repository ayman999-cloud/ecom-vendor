package com.aymane.ecom.multivendor.config;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;

import java.util.Collection;
import java.util.Date;
import java.util.stream.Collectors;

import static com.aymane.ecom.multivendor.config.JWT_CONSTANT.SECRET_KEY;

@Service
public class JwtProvider {
    final SecretKey key = Keys.hmacShaKeyFor(SECRET_KEY.getBytes());

    public String generateToken(final Authentication authentication) {
        final Collection<? extends GrantedAuthority> authorities
                = authentication.getAuthorities();
        final String roles =
                authorities
                        .stream()
                        .map(GrantedAuthority::getAuthority)
                        .collect(Collectors.joining(","));

        return Jwts
                .builder()
                .setIssuedAt(new Date())
                .setExpiration(new Date(new Date().getTime() + 86400000))
                .claim("email", authentication.getName())
                .claim("authorities", roles)
                .signWith(key)
                .compact();
    }

    public String getEmailFromToken(String token) {
        token = token.substring(7);

        final Claims claims = Jwts.parser().setSigningKey(key).build()
                .parseClaimsJws(token).getBody();

        return String.valueOf(claims.get("email"));
    }
}
