package com.dms.blockchainvote;

import java.io.*;
import java.security.*;
import java.security.spec.*;
import javax.crypto.*;
import org.apache.commons.codec.binary.Base64;

public class KeyMaker {
	
	private PublicKey publicKey;
	private PrivateKey privateKey;
	private KeyPair keypair;
	
	public KeyMaker() {
		this.keypair = makeKeyPair();
		this.publicKey = getPublicKey();
		this.privateKey = getPrivateKey();
	}
	
	public KeyPair makeKeyPair() {
		KeyPair pair;
		SecureRandom secureRandom = new SecureRandom();
		KeyPairGenerator keyPairGenerator;
	      try {
	    	  keyPairGenerator = KeyPairGenerator.getInstance("RSA");
	    	  keyPairGenerator.initialize(1024, secureRandom);
	    	  pair = keyPairGenerator.genKeyPair();
	      } catch (NoSuchAlgorithmException e) {
	    	  pair = null;
	      } 
	      return pair;
	}
	
	public PublicKey getPublicKey() {
		return keypair.getPublic();
	}
	
	public PrivateKey getPrivateKey() {
		return keypair.getPrivate();
	}
	
	public String getSPublicKey() {
		byte[] bPublicKey = publicKey.getEncoded();
		String sPublicKey = Base64.encodeBase64String(bPublicKey);
		
		return sPublicKey;
	}
	
	public String encode(String candidate) {
		String sCipherBase64;
		try {
			Cipher cipher = Cipher.getInstance("RSA");
			
			cipher.init(Cipher.ENCRYPT_MODE, privateKey);
			byte[] bCipher = cipher.doFinal(candidate.getBytes());
			
			sCipherBase64 = Base64.encodeBase64String(bCipher);
		}catch (Exception e) {
			sCipherBase64 = null;
		}
		return sCipherBase64;
	}
}

