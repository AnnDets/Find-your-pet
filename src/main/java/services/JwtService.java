package services;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import models.User;

import java.security.Key;
import java.util.Date;
import java.util.Map;

public class JwtService {
    static Algorithm algorithm = Algorithm.HMAC256("fqhewoeqrwi[0egeg[0jqib2rj9qw]0qefvw");
    static private final long expirationMillis = 3600000; // Время действия токена: 1 час

    static public String generateToken(User user) {
        return JWT.create()
                .withIssuer("find-your-pet") // Укажите имя вашего приложения
                .withSubject(user.getEmail()) // Укажите идентификатор пользователя
                .withIssuedAt(new Date()) // Время создания токена
                .withPayload(Map.of("id", user.getId()))
                .withExpiresAt(new Date(System.currentTimeMillis() + expirationMillis)) // Токен истекает через 1 час
                .sign(algorithm);
    }

    static public boolean verifyToken(String token) {
        try {
            JWTVerifier verifier = JWT.require(algorithm)
                    .withIssuer("find-your-pet")
                    .build();
            verifier.verify(token); // Если токен валиден, исключение не выбрасывается
            return true;
        } catch (JWTVerificationException e) {
            e.printStackTrace();
            return false;
        }
    }

    static public User getUserFromToken(String token) {
        DecodedJWT decode = JWT.decode(token);
        Integer userId = decode.getClaim("id").asInt();
        User user = new User(userId, "", "", "", "", "");
        return user;
    }
}
