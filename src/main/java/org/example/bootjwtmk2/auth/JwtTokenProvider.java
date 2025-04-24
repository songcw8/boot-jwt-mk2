package org.example.bootjwtmk2.auth;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.NotWritablePropertyException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.time.Instant;
import java.util.Date;
import java.util.List;

@Component
public class JwtTokenProvider {
    @Value("${jwt.secret}")
    private String secretKey;
    @Value("${jwt.expiration-ms}")
    private long expirationMs;

    // HMAC이라는 알고리즘에 사용할 수 있게 SecretKey를 SecretKey 객체로 만들어주기
    public SecretKey getSecretKey() {
        return Keys.hmacShaKeyFor(secretKey.getBytes());
    }

    // TOKEN 발급
    public String generateToken(Authentication authentication, List<String> roles) {
        String username = authentication.getName();
        Instant now = Instant.now(); //UTC
        Date expiration = new Date(now.toEpochMilli() + expirationMs);
        Claims claims = Jwts.claims()
                .subject(username)
                .add("roles", roles)
                .build();

        return Jwts.builder()
                .subject(username)
                .issuedAt(Date.from(now))
                .expiration(expiration)
                .signWith(getSecretKey(), Jwts.SIG.HS256)
                .compact();
    }

    // TOKEN을 바탕으로 유저 이름을 추출
    public String getUsername(String token) {
        return Jwts.parser()
                .verifyWith(getSecretKey())
                .build() // 1. 양식을 지켰나 2. 키와 대응 3.만료가 되었나
                .parseSignedClaims(token)
                .getPayload() // 유저 관련 데이터
                .getSubject(); // username or userid
    }

    public boolean validateToken(String token) {
        try {
            Jwts.parser()
                    .verifyWith(getSecretKey())
                    .build() // 1. 양식을 지켰나 2. 키와 대응 3.만료가 되었나
                    .parseSignedClaims(token);
            return true;
        } catch (JwtException | IllegalArgumentException e) {
            return false;
        }
    }

    @SuppressWarnings("unchecked")
    public List<String> getRoles(String token) {
        return (List<String>) Jwts.parser()
                .verifyWith(getSecretKey())
                .build()
                .parseSignedClaims(token)
                .getPayload()
                .get("roles");
    }

    public Authentication getAuthentication(String token) {
        UserDetails user = new User(getUsername(token), "",
                getRoles(token).stream()
                        .map(SimpleGrantedAuthority::new).toList());
        // 문자열 -> 권한 클래스 객체로
        return new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());
    }
}
