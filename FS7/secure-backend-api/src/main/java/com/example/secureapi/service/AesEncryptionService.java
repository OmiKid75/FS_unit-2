package com.example.secureapi.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;
import java.util.Base64;

@Service
public class AesEncryptionService {
    private static final int IV_LENGTH=12; private static final int TAG_BITS=128;
    private final SecretKey key; private final SecureRandom random=new SecureRandom();
    public AesEncryptionService(@Value("${app.aes.secret-base64}") String base64){byte[] raw=Base64.getDecoder().decode(base64);if(raw.length!=32)throw new IllegalArgumentException("AES key must decode to exactly 32 bytes (AES-256)");key=new SecretKeySpec(raw,"AES");}
    public String encrypt(String plaintext){try{byte[] iv=new byte[IV_LENGTH];random.nextBytes(iv);Cipher cipher=Cipher.getInstance("AES/GCM/NoPadding");cipher.init(Cipher.ENCRYPT_MODE,key,new GCMParameterSpec(TAG_BITS,iv));byte[] encrypted=cipher.doFinal(plaintext.getBytes(StandardCharsets.UTF_8));byte[] combined=new byte[iv.length+encrypted.length];System.arraycopy(iv,0,combined,0,iv.length);System.arraycopy(encrypted,0,combined,iv.length,encrypted.length);return Base64.getEncoder().encodeToString(combined);}catch(Exception e){throw new IllegalStateException("Encryption failed",e);}}
    public String decrypt(String encoded){try{byte[] combined=Base64.getDecoder().decode(encoded);if(combined.length<=IV_LENGTH)throw new IllegalArgumentException("Invalid ciphertext");byte[] iv=java.util.Arrays.copyOfRange(combined,0,IV_LENGTH);byte[] encrypted=java.util.Arrays.copyOfRange(combined,IV_LENGTH,combined.length);Cipher cipher=Cipher.getInstance("AES/GCM/NoPadding");cipher.init(Cipher.DECRYPT_MODE,key,new GCMParameterSpec(TAG_BITS,iv));return new String(cipher.doFinal(encrypted),StandardCharsets.UTF_8);}catch(Exception e){throw new IllegalArgumentException("Could not decrypt value; ciphertext or key may be invalid",e);}}
}
