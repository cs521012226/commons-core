package org.ltsh.core.core.cipher.impl;

import java.io.InputStream;
import java.io.OutputStream;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.spec.SecretKeySpec;

import org.ltsh.core.core.cipher.CipherHelper;
import org.ltsh.core.core.cipher.base.BaseCipher;

/**
 * DES对称加密、解密
 * @author Ych
 * 2017年9月21日
 */
public class DESCrypt extends BaseCipher{
	
	private static final String ALG = "DES";
	
	private Key key;		//密钥对象
	private String custemKey;	// 自定义密钥，DES固定格式为128bits，即8bytes
	
	public DESCrypt(String custemKey){
		this(custemKey, null);
	}
	
	public DESCrypt(String custemKey, CipherHelper cipherHelper){
		super(cipherHelper);
		byte[] bt = buildCustemKey(custemKey);
		this.custemKey = convertByteToStr(bt, charset);
		this.key = new SecretKeySpec(bt, ALG);
	}
	
	/**
	 * 随机生成对称AES算法密钥（16进制字符串）
	 * @author Ych
	 * @return
	 */
	public static String generateKey(){
		try {
			KeyGenerator kg = KeyGenerator.getInstance(ALG);
			SecureRandom sr = new SecureRandom();
			kg.init(sr);
			Key key = kg.generateKey();
			return convertByteToHexStr(key.getEncoded());
		} catch (NoSuchAlgorithmException e) {
			throw new RuntimeException(e);
		}
	}
	
	/**
	 * 获取自定义密钥
	 * @author Ych
	 * @return
	 */
	public String getKey(){
		return custemKey;
	}
	
	@Override
	public String encrypt(String data) {
		try {
			data = encryptInternal(data);
			byte[] d = convertStrToByte(data, charset);
			
			Cipher c = Cipher.getInstance(ALG);
			c.init(Cipher.ENCRYPT_MODE, key);
			byte[] rs = c.doFinal(d);
			return convertByteToHexStr(rs);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public String decrypt(String data) {
		try {
			byte[] d = convertHexStrToByte(data);
			Cipher c = Cipher.getInstance(ALG);
			c.init(Cipher.DECRYPT_MODE, key);
			data = convertByteToStr(c.doFinal(d), charset);
			return decryptInternal(data);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	private byte[] buildCustemKey(String key){
		byte[] keyByte = convertStrToByte(key, charset);
		byte[] rs = new byte[8];
		byte padding = '0';
		for(int i=0; i<rs.length; i++){
			rs[i] = (i < keyByte.length) ? keyByte[i] : padding;
		}
		return rs;
	}
	
	@Override
	public void encrypt(InputStream inputStream, OutputStream outputStream) {
		throw new UnsupportedOperationException();
	}

	@Override
	public void decrypt(InputStream inputStream, OutputStream outputStream) {
		throw new UnsupportedOperationException();
	}
	
}
