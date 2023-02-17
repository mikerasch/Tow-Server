package edu.uwp.appfactory.tow.webSecurityConfig.security.jwt;

import org.springframework.stereotype.Component;

import javax.crypto.*;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Base64;

@Component
public class EncryptionUtility {
    private final SecretKey secret;
    private final Cipher cipher;

    public EncryptionUtility() throws NoSuchAlgorithmException, NoSuchPaddingException {
        KeyGenerator keyGenerator = KeyGenerator.getInstance("AES");
        keyGenerator.init(128);
        secret = keyGenerator.generateKey();
        cipher = Cipher.getInstance("AES");
    }
    public String encrypt(final String plainMessage) {
        try{
            byte[] plainText = plainMessage.getBytes();
            cipher.init(Cipher.ENCRYPT_MODE,secret);
            byte[] encryptedByte = cipher.doFinal(plainText);
            Base64.Encoder encoder = Base64.getEncoder();
            return encoder.encodeToString(encryptedByte);
        } catch (Exception e){
            return "encrypt failed";
        }
    }
    public String decrypt(final String encryptedMessage) {
        try{
            Base64.Decoder decoder = Base64.getDecoder();
            byte[] encryptedByte = decoder.decode(encryptedMessage);
            cipher.init(Cipher.DECRYPT_MODE,secret);
            byte[] decryptedByte = cipher.doFinal(encryptedByte);
            return new String(decryptedByte);
        } catch(Exception e){
            return "decrypt failed";
        }
    }
    public String getStrongSecret() throws NoSuchAlgorithmException {
        SecureRandom secureRandom = SecureRandom.getInstanceStrong();
        byte[] bytes = new byte[128];
        secureRandom.nextBytes(bytes);
        StringBuilder stringBuilder = new StringBuilder();
        for(byte i: bytes){
            stringBuilder.append(String.format("%02x",i));
        }
        return stringBuilder.toString();
    }
}
