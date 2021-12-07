package com.javamentor.qa.platform.security.JWT;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.javamentor.qa.platform.models.entity.user.User;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.stream.Collectors;

@Service
@PropertySource("classpath:application.properties")
public class JWTUtil {

    @Value("${jwt.secret}")
    private String SECRET_KEY;

    public Algorithm getAlgorithm() {
        return  Algorithm.HMAC256(SECRET_KEY.getBytes(StandardCharsets.UTF_8));
    }

    public String generateAccessToken(User user) {

        Algorithm algorithm = getAlgorithm();

        String token = JWT.create()
                .withSubject(user.getUsername())
                .withExpiresAt(new Date(System.currentTimeMillis() + 24 * 60 * 60 * 1000))
                .withIssuedAt(new Date(System.currentTimeMillis()))
                .withClaim("roles", user.getAuthorities().stream().map(GrantedAuthority::getAuthority).collect(Collectors.toList()))
                .sign(algorithm);
        return token;
    }

    public UsernamePasswordAuthenticationToken checkToken(String token) {

            Algorithm algorithm = getAlgorithm();
            JWTVerifier verifier = JWT.require(algorithm).build();
            DecodedJWT decodedJWT = verifier.verify(token);
            String username = decodedJWT.getSubject();
            String[] roles = decodedJWT.getClaim("roles").asArray(String.class);

            Collection<SimpleGrantedAuthority> authorities = new ArrayList<>();
            Arrays.stream(roles).forEach(role -> {
                authorities.add(new SimpleGrantedAuthority(role));
            });

            UsernamePasswordAuthenticationToken authenticationToken =
                    new UsernamePasswordAuthenticationToken(username, null, authorities);
            return authenticationToken;
    }
}
