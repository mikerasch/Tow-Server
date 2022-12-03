package edu.uwp.appfactory.tow.webSecurityConfig.security.jwt;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.AlgorithmMismatchException;
import com.auth0.jwt.exceptions.IncorrectClaimException;
import com.auth0.jwt.exceptions.SignatureVerificationException;
import com.auth0.jwt.exceptions.TokenExpiredException;
import com.auth0.jwt.interfaces.DecodedJWT;
import edu.uwp.appfactory.tow.webSecurityConfig.security.services.UserDetailsImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;
import java.util.Date;

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
        Algorithm algorithm = Algorithm.HMAC256(jwtSecret);
        UserDetailsImpl userPrincipal = (UserDetailsImpl) authentication.getPrincipal();
        return JWT.create()
                .withSubject(userPrincipal.getId().toString())
                .withIssuedAt(new Date(System.currentTimeMillis()))
                .withExpiresAt(new Date(System.currentTimeMillis() + jwtExpirationMs))
                .sign(algorithm);
    }

    /**
     * method to refresh a users JWT token
     */
    public String refreshJwtToken(String userUUID) {
        Algorithm algorithm = Algorithm.HMAC256(jwtSecret);
        return JWT.create()
                .withSubject(userUUID)
                .withIssuedAt(new Date(System.currentTimeMillis()))
                .withExpiresAt(new Date(System.currentTimeMillis() + jwtExpirationMs))
                .sign(algorithm);
    }

    /**
     * Replaces the JWT token if it contains Bearer heading.
     * @param token - JWT token
     * @return - JWT token with no funny business
     */
    private String truncateBearerTag(String token) {
        return token.replace("Bearer","");
    }

    public String getUUIDFromJwtToken(String token) {
        Algorithm algorithm = Algorithm.HMAC256(jwtSecret);
        token = truncateBearerTag(token);
        DecodedJWT jwt = JWT.require(algorithm)
                .build().verify(token.strip());
        return jwt.getSubject();
    }

    /**
     * method to validate a JWT token
     */
    public boolean validateJwtToken(String authToken) {
        Algorithm algorithm = Algorithm.HMAC256(jwtSecret);
        try {
            DecodedJWT jwt = JWT.require(algorithm)
                    .build().verify(authToken.strip());
            return true;
        } catch (SignatureVerificationException e) {
            logger.error("Invalid JWT token: {}", e.getMessage());
        } catch (TokenExpiredException e) {
            logger.error("JWT token is expired: {}", e.getMessage());
        } catch (AlgorithmMismatchException e) {
            logger.error("JWT token is unsupported: {}", e.getMessage());
        } catch (IncorrectClaimException e) {
            logger.error("JWT claims string is empty: {}", e.getMessage());
        } catch (Exception e) {
            logger.error("JWT exception: {}", e.getMessage());
        }
        return false;
    }
}
