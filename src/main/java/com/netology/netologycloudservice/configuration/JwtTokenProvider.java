package com.netology.netologycloudservice.configuration;

import com.netology.netologycloudservice.dao.Role;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;

import javax.crypto.SecretKey;
import java.security.Key;
import java.util.Date;
import java.util.List;

@Component
public class JwtTokenProvider {
    private final SecretKey secretKey;

    // Чтение секретного ключа из конфигурации
    public JwtTokenProvider(@Value("${jwt.secret}") String secret) {
        this.secretKey = Keys.hmacShaKeyFor(secret.getBytes());
    }
    public String generateToken(String email, Role role) {
        return Jwts.builder()
                .setSubject(email)
                .claim("role", role.name())
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + 3600000))  // Токен действителен 1 час
                .signWith(secretKey, SignatureAlgorithm.HS256)
                .compact();
    }
    // Получение имени пользователя из токена
    public String getEmail(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(secretKey)
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getSubject();  // Получаем email
    }

    // Получение роли из токена
    public Role getRole(String token) {
        String roleName = Jwts.parserBuilder()
                .setSigningKey(secretKey)
                .build()
                .parseClaimsJws(token)
                .getBody()
                .get("role", String.class);
        return Role.valueOf(roleName);  // Преобразуем строку обратно в enum
    }

    // Проверка валидности токена
    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder()
                    .setSigningKey(secretKey)  // Устанавливаем ключ для проверки подписи
                    .build()
                    .parseClaimsJws(token);
            return true;
        } catch (JwtException | IllegalArgumentException e) {
            // В случае ошибки токен недействителен
            return false;
        }
    }

    public String resolveToken(HttpServletRequest request){
        // Получение токена из заголовка Authorization
        String authorizationHeader = request.getHeader("Authorization");
        String token = null;

        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            // Убираем префикс "Bearer " для получения чистого токена
            token = authorizationHeader.substring(7);
        }
        return token;
    }
}
/*
Обычно секретный ключ хранится в конфигурации приложения.

Пример application.yml:
jwt:
  secret: your-256-bit-secret-your-256-bit-secret
 */
