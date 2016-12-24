package com.patchwork.db.security;

import java.nio.charset.StandardCharsets;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.SecureRandom;

import org.apache.tomcat.util.buf.HexUtils;
import org.apache.tomcat.util.security.ConcurrentMessageDigest;

public class PasswordDigester {
	public static byte[] salt = null;
	public static String saltString = null;
	public static String securePassword = null;
	
    public static void digest(String passwordToHash) throws NoSuchAlgorithmException, NoSuchProviderException 
    {
        salt = getSalt();         
        securePassword = getSecurePassword(passwordToHash, salt);
        saltString = HexUtils.toHexString(salt);
    }
     
    private static String getSecurePassword(String passwordToHash, byte[] salt)
    {
        byte[] generatedPassword = ConcurrentMessageDigest.digest("MD5", 1,
                salt, passwordToHash.getBytes(StandardCharsets.UTF_8));
        return HexUtils.toHexString(generatedPassword);
    }
     
    //Add salt
    private static byte[] getSalt() throws NoSuchAlgorithmException, NoSuchProviderException
    {
        //Always use a SecureRandom generator
        SecureRandom sr = SecureRandom.getInstance("SHA1PRNG", "SUN");
        //Create array for salt
        byte[] salt = new byte[16];
        //Get a random salt
        sr.nextBytes(salt);
        //return salt
        return salt;
    }
}
