package edu.uwp.appfactory.tow.securityconfig.security.jwt;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.*;
import com.auth0.jwt.interfaces.DecodedJWT;
import edu.uwp.appfactory.tow.securityconfig.security.services.UserDetailsImpl;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;
import java.security.NoSuchAlgorithmException;
import java.util.Date;

/**
 * Java Web Tokens Utilities class, housing mostly all methods relating to JWT
 */
@Component
public class JwtUtils {
    private final String jwtSecret;

    @Value("${tow.app.jwtExpirationMs}")
    private int jwtExpirationMs;

    private final EncryptionUtility encryptionUtility;
    public JwtUtils(EncryptionUtility encryptionUtility) throws NoSuchAlgorithmException {
        this.encryptionUtility = encryptionUtility;
        jwtSecret = encryptionUtility.getStrongSecret();
    }
    /**
     * method that generates a JWT token
     */
    public String generateJwtToken(Authentication authentication){
        Algorithm algorithm = Algorithm.HMAC256(jwtSecret);
        UserDetailsImpl userPrincipal = (UserDetailsImpl) authentication.getPrincipal();
        String jwt = JWT.create()
                .withSubject(userPrincipal.getId().toString())
                .withIssuedAt(new Date(System.currentTimeMillis()))
                .withExpiresAt(new Date(System.currentTimeMillis() + jwtExpirationMs))
                .sign(algorithm);
        return encryptionUtility.encrypt(jwt.strip());
    }

    /**
     * method to refresh a users JWT token
     */
    public String refreshJwtToken(String userUUID) {
        Algorithm algorithm = Algorithm.HMAC256(jwtSecret);
        String token = JWT.create()
                .withSubject(userUUID)
                .withIssuedAt(new Date(System.currentTimeMillis()))
                .withExpiresAt(new Date(System.currentTimeMillis() + jwtExpirationMs))
                .sign(algorithm);
        return encryptionUtility.encrypt(token.strip());
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
        token = encryptionUtility.decrypt(token.strip());
        DecodedJWT jwt = JWT.require(algorithm)
                .build().verify(token.strip());
        return jwt.getSubject();
    }

    public DecodedJWT getDecodedJwt(String jwtToken) {
        try{
            return JWT.decode(jwtToken);
        } catch (JWTDecodeException e){
            return null;
        }
    }
    public boolean isTokenValid(String jwtToken) {
        DecodedJWT decodedJWT = getDecodedJwt(encryptionUtility.decrypt(jwtToken));
        if(decodedJWT == null){
            return false;
        }
        return !isTokenExpired(decodedJWT);
    }

    public boolean isTokenExpired(DecodedJWT token) {
        return token.getExpiresAt().before(new Date());
    }
}
