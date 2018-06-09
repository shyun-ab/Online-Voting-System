package com.dms.blockchainvote;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Hash {
    /**
     * Hash function with SHA256
     * this method doesn't hash empty string.
     * @param str string to hash
     * @return hash value
     */
    public static String hashSHA256(String str){
        if(str.equals("")){
            return "";
        }
        String SHA;
        try{
            MessageDigest sh = MessageDigest.getInstance("SHA-256");
            sh.update(str.getBytes());
            byte byteData[] = sh.digest();
            StringBuilder sb = new StringBuilder();
            for(byte data : byteData){
                sb.append(Integer.toString((data&0xff) + 0x100, 16).substring(1));
            }
            SHA = sb.toString();
        }catch(NoSuchAlgorithmException e){
            SHA = null;
        }
        return SHA;
    }

    /**
     * Hash function with SHA256, with two string
     * this method doesn't hash empty string.
     * @param str1 string to hash
     * @param str2 string to hash
     * @return hash value
     */
    public static String hashSHA256(String str1, String str2){
        String SHA;
        String str = str1 + str2;
        if(str.equals("")){
            return "";
        }
        try{
            MessageDigest sh = MessageDigest.getInstance("SHA-256");
            sh.update(str.getBytes());
            byte byteData[] = sh.digest();
            StringBuilder sb = new StringBuilder();
            for(byte data : byteData){
                sb.append(Integer.toString((data&0xff) + 0x100, 16).substring(1));
            }
            SHA = sb.toString();
        }catch(NoSuchAlgorithmException e){
            SHA = null;
        }
        return SHA;
    }
}
