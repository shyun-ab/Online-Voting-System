package com.dms.blockchainvote;

import java.io.*;
import java.security.*;
import java.security.spec.*;
import javax.crypto.*;
import org.apache.commons.codec.binary.Base64;

public class KeyLoader {
	private PublicKey publicKey;
	
	public KeyLoader(String sPublicKey) {
		this.publicKey = loadKey(sPublicKey);
	}
	
	public PublicKey loadKey(String sPublicKey) {
		PublicKey key;
		BufferedReader br = null;
		
		byte[] bPublicKey = Base64.decodeBase64(sPublicKey.getBytes());
		
		try {
			KeyFactory keyFactory = KeyFactory.getInstance("RSA");
			X509EncodedKeySpec publicKeySpec = new X509EncodedKeySpec(bPublicKey);
			key = keyFactory.generatePublic(publicKeySpec);
		} catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
			key = null;
		}
		
		return key;
	}
	
	public String decode(String sCipher) {
		String candidate;
		try {
			Cipher cipher = Cipher.getInstance("RSA");
			byte[] bCipher = Base64.decodeBase64(sCipher.getBytes());
			
			cipher.init(Cipher.DECRYPT_MODE, publicKey);
			byte[] bCandidate = cipher.doFinal(bCipher);
			candidate = new String(bCandidate);
		} catch (Exception e) {
			candidate = null;
		}
		
		return candidate;
	}
}
