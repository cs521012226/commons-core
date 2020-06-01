package org.ltsh.core.core.cipher.impl;

import java.io.UnsupportedEncodingException;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.security.Signature;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.HashMap;
import java.util.Map;

import javax.crypto.Cipher;

import org.apache.commons.codec.binary.Base64;

import org.ltsh.core.core.constants.Config;

/**
 * RSA非对称密钥加密、解密
 * @author 邓端宁
 * @date 2018年6月24日
 */
public class RSAExtendCrypt {
	private static final String ALG = "RSA";
	private static final String SIG_ALG = "MD5withRSA";
	private static final String charset = Config.CHARSET;
	private int keySize = 1024;
	private PrivateKey privateKey;
	private PublicKey publicKey;
	
	public RSAExtendCrypt() {
		KeyPair kp = generateKey();
		privateKey = kp.getPrivate();
		publicKey = kp.getPublic();
	}
	
	
	/**
	 * 建立新的密钥对，返回打包的Map形式私钥和公钥
	 * key=privateKey 私钥
	 * key=publicKey 公钥
	 * @return
	 */
	public Map<String, String> getRSAKey() {
		Map<String, String> keyMap = new HashMap<>();
		try {
			String pubKeyStr = new String(Base64.encodeBase64(publicKey.getEncoded()),charset);
	        String priKeyStr = new String(Base64.encodeBase64(privateKey.getEncoded()),charset);
			keyMap.put("publicKey", pubKeyStr);
			keyMap.put("privateKey", priKeyStr);
		} catch (UnsupportedEncodingException e) {
			throw new RuntimeException(e);
		}
		return keyMap;
	}

	/**
	 * 新建密钥对
	 * @return KeyPair对象
	 */
	public KeyPair generateKey() {
		long mySeed = System.currentTimeMillis();
		try {
			KeyPairGenerator keyGen = KeyPairGenerator.getInstance(ALG);
			SecureRandom random = new SecureRandom();
			random.setSeed(mySeed);
			keyGen.initialize(keySize, random);
			return keyGen.generateKeyPair();
		}catch (NoSuchAlgorithmException e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * 是否通过签名验证
	 * @param data 要进行签名的数据
	 * @param privateKey 私钥
	 * @param publicKeyStr 公钥
	 * @throws Exception
	 */
	public boolean isSign(String data,String privateKey,String publicKey) {
		try {
			//生成要签名的数据 
	        String needSignData = new String(Base64.decodeBase64(data),charset);
	        byte privKeySpecByte[] = Base64.decodeBase64(privateKey);
	        byte[] privKeySign = sign(privKeySpecByte, needSignData.getBytes(charset));
	        //签名后的数据
	        String signData = new String(Base64.encodeBase64(Base64.encodeBase64(privKeySign)));
	        byte publicKeyByte[] = Base64.decodeBase64(publicKey.getBytes());
	        byte decodeSign[] = Base64.decodeBase64(Base64.decodeBase64(signData.getBytes()));
	
	        return verify(publicKeyByte, needSignData.getBytes(charset), decodeSign);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	/**
	 * RSA加密
	 * @param data 需要加密的明文数据
	 * @return
	 */
	public String encrypt(String data,String publicKey) {
		RSAExtendCrypt rsaCrypt = new RSAExtendCrypt(); 
		try {
			byte[] publicKeyByte = Base64.decodeBase64(publicKey.getBytes());
			byte [] encoderData = rsaCrypt.encrypt(publicKeyByte, data.getBytes(charset));
			return Base64.encodeBase64String(encoderData);
			
		} catch (Exception e) {
			throw new RuntimeException("加密失败：",e);
		}
	}
	/**
	 * RSA解密
	 * @param data 需要解密的密文数据
	 * @return
	 */
	public String decrypt(String data, String privateKey) {
		RSAExtendCrypt rsaCrypt = new RSAExtendCrypt(); 
		try {
			byte[] base64DecoderData = Base64.decodeBase64(data.getBytes());
			byte[] privateKeyByte = Base64.decodeBase64(privateKey.getBytes());
			byte [] decoderData = rsaCrypt.decrypt(privateKeyByte, base64DecoderData);
			return new String(decoderData, charset);
		} catch (Exception e) {
			throw new RuntimeException("解密失败：",e);
		}
	}
	/**
	 * 使用RSA公钥加密数据
	 * @param pubKeyInByte 打包的byte[]形式公钥
	 * @param data 要加密的数据
	 * @return 加密数据
	 */
	private byte[] encrypt(byte[] pubKeySpecByte, byte[] data) {
		try {
			KeyFactory keyFactory = KeyFactory.getInstance(ALG);
			X509EncodedKeySpec pubKeySpec = new X509EncodedKeySpec(pubKeySpecByte);
			PublicKey pubKey = keyFactory.generatePublic(pubKeySpec);
			Cipher cipher = Cipher.getInstance(ALG);
			cipher.init(Cipher.ENCRYPT_MODE, pubKey);
			return cipher.doFinal(data);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * 用RSA私钥解密
	 * @param privKeyInByte 私钥打包成byte[]形式
	 * @param data 要解密的数据
	 * @return 解密数据
	 */
	private byte[] decrypt(byte[] privKeyInByte, byte[] data) {
		try {
			PKCS8EncodedKeySpec privateKeyspec = new PKCS8EncodedKeySpec(privKeyInByte);
			KeyFactory keyFactory = KeyFactory.getInstance(ALG);
			PrivateKey privKey = keyFactory.generatePrivate(privateKeyspec);
			Cipher cipher = Cipher.getInstance(ALG);
			cipher.init(Cipher.DECRYPT_MODE, privKey);
			return cipher.doFinal(data);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}

	}
	
	/** 使用私钥加密数据
	  * 用一个已打包成byte[]形式的私钥加密数据，即数字签名
	  * @param privSpecByte 打包成byte[]的私钥
	  * @param source 要签名的数据，一般应是数字摘要
	  * @return 签名 byte[]
	  */
	private byte[] sign(byte[] privKeySpecByte, byte[] source) {
		try {
			PKCS8EncodedKeySpec privKeySpec = new PKCS8EncodedKeySpec(privKeySpecByte);
			KeyFactory mykeyFactory = KeyFactory.getInstance(ALG);
			PrivateKey privKey = mykeyFactory.generatePrivate(privKeySpec);
			Signature sig = Signature.getInstance(SIG_ALG);
			sig.initSign(privKey);
			sig.update(source);
			return sig.sign();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	/**
	 * 验证数字签名
	 * @param pubKeySpecByte 打包成byte[]形式的公钥
	 * @param source 原文的数字摘要
	 * @param sign 签名（对原文的数字摘要的签名）
	 * @return 是否证实 boolean
	 */
	private boolean verify(byte[] pubKeySpecByte, byte[] source, byte[] sign) {
		try {
			KeyFactory mykeyFactory = KeyFactory.getInstance(ALG);
			Signature sig = Signature.getInstance(SIG_ALG);
			X509EncodedKeySpec pubKeySpec = new X509EncodedKeySpec(pubKeySpecByte);
			PublicKey pubKey = mykeyFactory.generatePublic(pubKeySpec);
			sig.initVerify(pubKey);
			sig.update(source);
			return sig.verify(sign);
		} catch (Exception e) {
			return false;
		}
	}
	
	public int getKeySize() {
		return keySize;
	}
	public void setKeySize(int keySize) {
		this.keySize = keySize;
	}
	public PrivateKey getPrivateKey() {
		return privateKey;
	}
	public void setPrivateKey(PrivateKey privateKey) {
		this.privateKey = privateKey;
	}
	public PublicKey getPublicKey() {
		return publicKey;
	}
	public void setPublicKey(PublicKey publicKey) {
		this.publicKey = publicKey;
	}

}
