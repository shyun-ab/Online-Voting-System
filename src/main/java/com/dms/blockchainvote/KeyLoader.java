package com.dms.blockchainvote;

import java.io.*;
import java.security.*;
import java.security.spec.*;
import javax.crypto.*;
import org.apache.commons.codec.binary.Base64;

public class KeyLoader {
	private PublicKey publicKey;
	
	public KeyLoader() {
		this.publicKey = loadKey();
	}
	
	public PublicKey loadKey() {
		PublicKey key;
		String sPublicKey = null;
		BufferedReader br = null;
		try {
			br = new BufferedReader(new FileReader("PublicKeys.txt"));
			sPublicKey = br.readLine();
		} catch (IOException e) {
			sPublicKey = null;
		} finally {
			try {
				if(br != null) br.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
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
	
	//블록에서 가져온 암호화된 후보자 정보를 받아 복호화하는 메소드
	public String decode(String sCipher) {
		String candidate;
		try {
			Cipher cipher = Cipher.getInstance("RSA");
			byte[] bCipher = Base64.decodeBase64(sCipher.getBytes());
			
			cipher.init(Cipher.DECRYPT_MODE, publicKey);
			byte[] bCandidate = cipher.doFinal(bCipher);
			candidate = new String(bCandidate);
		} catch (NoSuchAlgorithmException e) {
			candidate = null;
		} catch (InvalidKeyException e) {
			candidate = null;
		}
		
		return candidate;
	}
}
