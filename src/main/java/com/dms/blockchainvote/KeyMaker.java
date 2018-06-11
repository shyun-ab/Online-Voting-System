package com.dms.blockchainvote;

import java.io.*;
import java.security.*;
import java.security.spec.*;
import javax.crypto.*;
import org.apache.commons.codec.binary.Base64;

public class KeyMaker {

  public static void main(String[] args) {
    
	  // Produce private, public keys
      PublicKey publicKey1 = null;
      PrivateKey privateKey1 = null;
      
      SecureRandom secureRandom = new SecureRandom();
      KeyPairGenerator keyPairGenerator;
      try {
        keyPairGenerator = KeyPairGenerator.getInstance("RSA");
        keyPairGenerator.initialize(1024, secureRandom); 
      
        KeyPair keyPair = keyPairGenerator.genKeyPair();
        
        publicKey1 = keyPair.getPublic();
        privateKey1 = keyPair.getPrivate();
      
      } catch (NoSuchAlgorithmException e) {
        e.printStackTrace();
      } 
      
      byte[] bPublicKey1 = publicKey1.getEncoded();
      String sPublicKey1 = Base64.encodeBase64String(bPublicKey1);

      byte[] bPrivateKey1 = privateKey1.getEncoded();
      String sPrivateKey1 = Base64.encodeBase64String(bPrivateKey1);
      
      // save to text file 
      try {
    	  BufferedWriter bw1 = new BufferedWriter(new FileWriter("PublicKey.txt"));
          bw1.write(sPublicKey1);
          bw1.newLine();
          bw1.close();
          BufferedWriter bw2 = new BufferedWriter(new FileWriter("PrivateKey.txt"));
          bw2.write(sPrivateKey1);
          bw2.newLine();
          bw2.close();
      } catch (IOException e) {
        e.printStackTrace();
      }
          
          
      // Bring the key information in the text file
      String sPublicKey2 = null;
      String sPrivateKey2 = null;
          
      BufferedReader brPublicKey = null;
      BufferedReader brPrivateKey = null;
      try {
          brPublicKey = new BufferedReader(new FileReader("PublicKey.txt"));
          sPublicKey2 = brPublicKey.readLine();   // First Line Read
          brPrivateKey = new BufferedReader(new FileReader("PrivateKey.txt"));
          sPrivateKey2 = brPrivateKey.readLine(); // First Line Read
          System.out.println("Pubilc Key & Private Key Read");
      } catch (IOException e) {
          e.printStackTrace();
      } finally {
          try {
              if (brPublicKey != null)
                  brPublicKey.close();
              if (brPrivateKey != null)
                  brPrivateKey.close();
          } catch (IOException e) {
              e.printStackTrace();
          }
      }
      
      //decoding keys 
      byte[] bPublicKey2 = Base64.decodeBase64(sPublicKey2.getBytes());
      PublicKey  publicKey2 = null;
          
      byte[] bPrivateKey2 = Base64.decodeBase64(sPrivateKey2.getBytes());
      PrivateKey privateKey2 = null;
          
      try {
          KeyFactory keyFactory2 = KeyFactory.getInstance("RSA");
              
          X509EncodedKeySpec publicKeySpec
                                          = new X509EncodedKeySpec(bPublicKey2);
          publicKey2 = keyFactory2.generatePublic(publicKeySpec);
              
          PKCS8EncodedKeySpec privateKeySpec
                                          = new PKCS8EncodedKeySpec(bPrivateKey2);
          privateKey2 = keyFactory2.generatePrivate(privateKeySpec);
      
      } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
          e.printStackTrace();
      }

      String sPlain1 = "Welcome to RSA";
      String sPlain2 = null;

      
      
      
       // 요기서부터는 암호화 복호화 잘되는지 확인 
       
      try {
        Cipher cipher = Cipher.getInstance("RSA");

          // 공개키 이용 암호화
          cipher.init(Cipher.ENCRYPT_MODE, publicKey2);
          byte[] bCipher1 = cipher.doFinal(sPlain1.getBytes());
          String sCipherBase64 = Base64.encodeBase64String(bCipher1);
              
          // 개인키 이용 복호화
          byte[] bCipher2 = Base64.decodeBase64(sCipherBase64.getBytes());
          cipher.init(Cipher.DECRYPT_MODE, privateKey2);
          byte[] bPlain2 = cipher.doFinal(bCipher2);
          sPlain2 = new String(bPlain2);
        } catch (NoSuchAlgorithmException e) {
          e.printStackTrace();
        } catch (NoSuchPaddingException e) {
          e.printStackTrace();
        } catch (InvalidKeyException e) {
          e.printStackTrace();
        } catch (IllegalBlockSizeException e) {
          e.printStackTrace();
        } catch (BadPaddingException e) {
          e.printStackTrace();
        }
      
        System.out.println("sPlain1 : " + sPlain1); // 평문(원본)
        System.out.println("sPlain2 : " + sPlain2); // 평문(암호화후 복호화된 평문)
  }
}



