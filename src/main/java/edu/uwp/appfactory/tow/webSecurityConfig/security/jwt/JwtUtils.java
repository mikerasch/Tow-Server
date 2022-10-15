package edu.uwp.appfactory.tow.webSecurityConfig.security.jwt;

import edu.uwp.appfactory.tow.webSecurityConfig.security.services.UserDetailsImpl;
import io.jsonwebtoken.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.function.Function;

import static io.jsonwebtoken.SignatureAlgorithm.HS256;

/**
 * Java Web Tokens Utilities class, housing mostly all methods relating to JWT
 */
@Component
public class JwtUtils {

    private static final Logger logger = LoggerFactory.getLogger(JwtUtils.class);

    @Value("${tow.app.jwtSecret}")
    private String jwtSecret;

    @Value("${tow.app.jwtExpirationMs}")
    private int jwtExpirationMs;

    /**
     * method that generates a JWT token
     */
    public String generateJwtToken(Authentication authentication) {

        UserDetailsImpl userPrincipal = (UserDetailsImpl) authentication.getPrincipal();

        return Jwts.builder()
                .setSubject(userPrincipal.getId().toString())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + jwtExpirationMs))
                .signWith(HS256, jwtSecret)
                .compact();
    }

    /**
     * method to refresh a users JWT token
     */
    public String refreshJwtToken(String userUUID) {

        return Jwts.builder()
                .setSubject(userUUID)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + jwtExpirationMs))
                .signWith(HS256, jwtSecret)
                .compact();
    }

    /**
     * method to get UUID from a JWT token
     */
    public String getUUIDFromJwtToken(String token) {
        token = truncateBearerTag(token);
        Function<Claims, String> claimsResolver = Claims::getSubject;
        Claims parsedToken = Jwts
                .parser()
                .setSigningKey(jwtSecret)
                .parseClaimsJws(token)
                .getBody();
        return claimsResolver.apply(parsedToken);
    }

    /**
     * Replaces the JWT token if it contains Bearer heading.
     * @param token - JWT token
     * @return - JWT token with no funny business
     */
    private String truncateBearerTag(String token) {
        return token.replace("Bearer","");
    }

    /**
     * method to validate a JWT token
     */
    public boolean validateJwtToken(String authToken) {
        try {
            Jwts.parser()
                    .setSigningKey(jwtSecret)
                    .parseClaimsJws(authToken);
            return true;
        } catch (MalformedJwtException e) {
            logger.error("Invalid JWT token: {}", e.getMessage());
        } catch (ExpiredJwtException e) {
            logger.error("JWT token is expired: {}", e.getMessage());
        } catch (UnsupportedJwtException e) {
            logger.error("JWT token is unsupported: {}", e.getMessage());
        } catch (IllegalArgumentException e) {
            logger.error("JWT claims string is empty: {}", e.getMessage());
        } catch (Exception e) {
            logger.error("JWT exception: {}", e.getMessage());
        }
        return false;
    }
}
