package com.arra.test.security.jwt;

import com.arra.test.config.ApplicationConfiguration;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.security.Principal;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.stream.Collectors;

@ApplicationScoped
public class TokenProvider {
    private final Logger log = LoggerFactory.getLogger(TokenProvider.class);

    @Inject
    private ApplicationConfiguration applicationConfiguration;

    private static final String AUTHORITIES_KEY = "auth";
    private Key key;
    private long tokenValidityInMilliseconds;
    private long tokenValidityInMillisecondsForRememberMe;

    @PostConstruct
    public void init() {
        byte[] keyBytes;
        String secret = applicationConfiguration.jwtConfiguration.base64Secret;
        if(secret == null || secret.trim().equals("")) {
            log.warn("Warning: the JWT key used is empty. " +
                    "We recommend using the `application.jwtConfiguration.base64-secret` key for optimum security.");
            keyBytes = "".getBytes(StandardCharsets.UTF_8);
        } else {
            log.debug("Using a Base64-encoded JWT secret key");
            keyBytes = Decoders.BASE64.decode(secret);
        }

        this.key = Keys.hmacShaKeyFor(keyBytes);
        this.tokenValidityInMilliseconds = 1000 * applicationConfiguration.jwtConfiguration.tokenValidityInSeconds;
        this.tokenValidityInMillisecondsForRememberMe = 1000 * applicationConfiguration.jwtConfiguration.tokenValidityInSecondsForRememberMe;
    }

    public String createToken(JwtPrincipal principal, boolean rememberMe) {
        long now = (new Date()).getTime();
        Date validity;
        if (rememberMe) {
            validity = new Date(now + this.tokenValidityInMillisecondsForRememberMe);
        } else {
            validity = new Date(now + this.tokenValidityInMilliseconds);
        }

        return Jwts.builder()
                .setSubject(principal.getName())
                .claim(AUTHORITIES_KEY, principal.getAuthorities())
                .signWith(this.key, SignatureAlgorithm.HS512)
                .setExpiration(validity)
                .compact();
    }

    public Principal getPrincipal(String token) {
        Claims claims = Jwts.parser()
                .setSigningKey(key)
                .parseClaimsJws(token)
                .getBody();



         String[] roles = null;
        if(claims.get(AUTHORITIES_KEY) != null) {
            Collection<String> authorities = Arrays.stream(claims.get(AUTHORITIES_KEY).toString().split(","))
                    .collect(Collectors.toList());
            roles = (String[]) authorities.toArray();
        }

        return new JwtPrincipal(claims.getSubject(), roles);
    }

    public boolean validateToken(String authToken) {
        try {
            Jwts.parser().setSigningKey(key).parseClaimsJws(authToken);
            return true;
        } catch (io.jsonwebtoken.security.SecurityException | MalformedJwtException e) {
            log.info("Invalid JWT signature.");
            log.trace("Invalid JWT signature trace: {}", e);
        } catch (ExpiredJwtException e) {
            log.info("Expired JWT token.");
            log.trace("Expired JWT token trace: {}", e);
        } catch (UnsupportedJwtException e) {
            log.info("Unsupported JWT token.");
            log.trace("Unsupported JWT token trace: {}", e);
        } catch (IllegalArgumentException e) {
            log.info("JWT token compact of handler are invalid.");
            log.trace("JWT token compact of handler are invalid trace: {}", e);
        }
        return false;
    }
}
