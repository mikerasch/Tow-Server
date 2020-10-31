package edu.uwp.appfactory.tow.WebSecurityConfig.security.jwt;

import io.jsonwebtoken.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;
import edu.uwp.appfactory.tow.WebSecurityConfig.security.services.UserDetailsImpl;
import java.util.Date;
import java.util.function.Function;

/**
 * Java Web Tokens Utilities class, housing mostly all methods relating to JWT
 */
@Component
public class JwtUtils {
    /**
     * logger to log items in console
     */
    private static final Logger logger = LoggerFactory.getLogger(JwtUtils.class);

    /**
     * JWT secret used to encode/decode
     */
    @Value("${tow.app.jwtSecret}")
    private String jwtSecret;

    /**
     * JWT expiration time in milliseconds
     */
    @Value("${tow.app.jwtExpirationMs}")
    private int jwtExpirationMs;

    /**
     * JWT reset expiration time in milliseconds
     */
    @Value("${tow.app.jwtResetExpirationMs}")
    private int jwtResetExpirationMs;

    /**
     * method that generates a JWT token
     * @param authentication authentication object
     * @return JWT built token
     */
    public String generateJwtToken(Authentication authentication) {

        UserDetailsImpl userPrincipal = (UserDetailsImpl) authentication.getPrincipal();

        return Jwts.builder()
                .setSubject((userPrincipal.getUUID()))
                .setIssuedAt(new Date())
                .setExpiration(new Date((new Date()).getTime() + jwtExpirationMs))
                .signWith(SignatureAlgorithm.HS512, jwtSecret)
                .compact();
    }

    /**
     * method to refresh a users JWT token
     * @param UUID of user
     * @return JWT built token
     */
    public String refreshJwtToken(String UUID) {

        return Jwts.builder()
                .setSubject((UUID))
                .setIssuedAt(new Date())
                .setExpiration(new Date((new Date()).getTime() + jwtExpirationMs))
                .signWith(SignatureAlgorithm.HS512, jwtSecret)
                .compact();
    }

    /**
     * method to get UUID from a JWT token
     * @param token from JWT for a user
     * @return UUID of the JWT token user
     */
    public String getUUIDFromJwtToken(String token) {

        Function<Claims, String> claimsResolver = Claims::getSubject;
        Claims parsedToken = Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(token).getBody();
        return claimsResolver.apply(parsedToken);
    }

    /**
     * not used, may use later
     */
    public String generateResetJwtToken(String UUID) {

        return Jwts.builder()
                .setSubject(UUID)
                .setIssuedAt(new Date())
                .setExpiration(new Date((new Date()).getTime() + jwtResetExpirationMs))
                .signWith(SignatureAlgorithm.HS512, jwtSecret)
                .compact();
    }
    public String generateResetJwtDigit(String resetDigits) { // pull digits to see if digits match provided email

        return Jwts.builder()
                .setSubject(resetDigits)
                .setIssuedAt(new Date())
                .setExpiration(new Date((new Date()).getTime() + jwtResetExpirationMs))
                .signWith(SignatureAlgorithm.HS512, jwtSecret)
                .compact();
    }

    /**
     * method to validate a JWT token
     * @param authToken of user
     * @return boolean if it was valid or not
     */
    public boolean validateJwtToken(String authToken) {
        try {
            Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(authToken);
            return true;
        } catch (SignatureException e) {
            logger.error("Invalid JWT signature: {}", e.getMessage());
        } catch (MalformedJwtException e) {
            logger.error("Invalid JWT token: {}", e.getMessage());
        } catch (ExpiredJwtException e) {
            logger.error("JWT token is expired: {}", e.getMessage());
        } catch (UnsupportedJwtException e) {
            logger.error("JWT token is unsupported: {}", e.getMessage());
        } catch (IllegalArgumentException e) {
            logger.error("JWT claims string is empty: {}", e.getMessage());
        }
        return false;
    }
}
