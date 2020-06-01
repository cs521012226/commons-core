package org.ltsh.core.core.cipher.impl;

import java.io.InputStream;
import java.io.OutputStream;
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

import javax.crypto.Cipher;

import org.apache.commons.codec.binary.Base64;

import org.ltsh.core.core.cipher.CipherHelper;
import org.ltsh.core.core.cipher.base.BaseCipher;

/**
 * RSA非对称密钥加密、解密
 * @author Ych
 * 2018年6月21日
 */
public class RSACrypt extends BaseCipher{

	private static final String ALG = "RSA";
	
	private int keySize = 1024;
	private String signAlg = "MD5withRSA";	//签名算法
	
	private PrivateKey priKey;
	private PublicKey pubKey;
	
	private String privateKey;
	private String publicKey;
	
	
	public RSACrypt(){
		this(null);
	}
	
	public RSACrypt(CipherHelper cipherHelper) {
		super(cipherHelper);
		KeyPair kp = generateKey();
		priKey = kp.getPrivate();
		pubKey = kp.getPublic();
		
		privateKey = Base64.encodeBase64String(priKey.getEncoded());
		publicKey = Base64.encodeBase64String(pubKey.getEncoded());
	}
	
	/**
	 * 生成RSA公要与私钥对 
	 * @Author: Charles
	 * @return
	 * @throws NoSuchAlgorithmException
	 */
	private KeyPair generateKey() {
		try {
			KeyPairGenerator kg = KeyPairGenerator.getInstance(ALG);
			SecureRandom sr = new SecureRandom();
			kg.initialize(keySize, sr);
			return kg.generateKeyPair();
			
		} catch (NoSuchAlgorithmException e) {
			throw new RuntimeException(e);
		}
	}
	@Override
	public String encrypt(String data) {
		try {
			data = encryptInternal(data);
			byte[] d = convertStrToByte(data, charset);
			Cipher c = Cipher.getInstance(ALG);
			c.init(Cipher.ENCRYPT_MODE, pubKey);
			d = c.doFinal(d);
			return convertByteToHexStr(d);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}


	@Override
	public void encrypt(InputStream inputStream, OutputStream outputStream) {
		throw new UnsupportedOperationException();
	}


	@Override
	public String decrypt(String data) {
		try {
			byte[] d = convertHexStrToByte(data);
			Cipher c = Cipher.getInstance(ALG);
			c.init(Cipher.DECRYPT_MODE, priKey);
			d = c.doFinal(d);
			data = convertByteToStr(d, charset);
			return decryptInternal(data);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}


	@Override
	public void decrypt(InputStream inputStream, OutputStream outputStream) {
		throw new UnsupportedOperationException();
	}
	
	/**
	 * 使用私钥签名（用私钥加密数据）
	 * @author Ych
	 * @param data	要签名的数据，一般应是数字摘要
	 * @return	格式为base64编码过签名后的数据
	 */
	public String sign(String data) {
		try {
			byte[] d = convertStrToByte(data, charset);
			Signature sig = Signature.getInstance(signAlg);
			sig.initSign(priKey);
			sig.update(d);
			return Base64.encodeBase64String(sig.sign());
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	/**
	 * 验证数字签名
	 * @param data 未签名数据
	 * @param sign 签名后的数据
	 * @return 是否证实 boolean
	 */
	public boolean verify(String data, String sign) {
		try {
			byte[] s = Base64.decodeBase64(sign);
			byte[] d = convertStrToByte(data, charset);
			Signature sig = Signature.getInstance(signAlg);
			sig.initVerify(pubKey);
			sig.update(d);
			return sig.verify(s);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * 取得私钥
	 * @author Ych
	 * @return	格式为base64编码过的私钥钥字符串
	 */
	public String getPrivateKey() {
		return privateKey;
	}

	/**
	 * 设置私钥
	 * @author Ych
	 * @param privateKey 格式为base64编码过的私钥字符串
	 */
	public void setPrivateKey(String privateKey) {
		try {
			this.privateKey = privateKey;
			byte[] priKeySpecByte = Base64.decodeBase64(privateKey);
			PKCS8EncodedKeySpec privateKeyspec = new PKCS8EncodedKeySpec(priKeySpecByte);
			KeyFactory keyFactory = KeyFactory.getInstance(ALG);
			this.priKey = keyFactory.generatePrivate(privateKeyspec);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * 取得公钥
	 * @author Ych
	 * @return	格式为base64编码过的公钥字符串
	 */
	public String getPublicKey() {
		return publicKey;
	}

	/**
	 * 设置公钥
	 * @author Ych
	 * @param publicKey	格式为base64编码过的公钥字符串
	 */
	public void setPublicKey(String publicKey) {
		this.publicKey = publicKey;
		byte[] pubKeySpecByte = Base64.decodeBase64(publicKey);
		try {
			KeyFactory keyFactory = KeyFactory.getInstance(ALG);
			X509EncodedKeySpec pubKeySpec = new X509EncodedKeySpec(pubKeySpecByte);
			this.pubKey = keyFactory.generatePublic(pubKeySpec);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
}
