package org.ltsh.core.core.cipher.impl;

import java.io.InputStream;
import java.io.OutputStream;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

import javax.crypto.Cipher;
import javax.crypto.CipherInputStream;
import javax.crypto.CipherOutputStream;
import javax.crypto.KeyGenerator;
import javax.crypto.spec.SecretKeySpec;

import org.ltsh.core.core.cipher.CipherHelper;
import org.ltsh.core.core.cipher.base.BaseCipher;
import org.ltsh.core.core.util.FileUtil;

/**
 * AES对称加密、解密
 * @author Ych
 * 2017年9月21日
 */
public class AESCrypt extends BaseCipher{
	
	private static final String ALG = "AES";
	
	private Key key;		//密钥对象
	private String custemKey;	// 自定义密钥， AES固定格式为128/192/256 bits.即：16/24/32bytes
	
	public AESCrypt(String custemKey){
		this(custemKey, null);
	}
	
	public AESCrypt(String custemKey, CipherHelper cipherHelper){
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
			d = c.doFinal(d);
			return convertByteToHexStr(d);
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
			d = c.doFinal(d);
			data = convertByteToStr(d, charset);
			return decryptInternal(data);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	private byte[] buildCustemKey(String key){
		byte[] keyByte = convertStrToByte(key, charset);
		byte[] rs = null;
		byte padding = '0';
		if(keyByte.length < 16){
			rs = new byte[16];
		}else if(keyByte.length < 24){
			rs = new byte[24];
		}else{
			rs = new byte[32];
		}
		for(int i=0; i<rs.length; i++){
			rs[i] = (i < keyByte.length) ? keyByte[i] : padding;
		}
		return rs;
	}
	
	@Override
	public void encrypt(InputStream inputStream, OutputStream outputStream) {
		CipherOutputStream cos = null;
		try {
			Cipher cipher = Cipher.getInstance(ALG);
			cipher.init(Cipher.ENCRYPT_MODE, key);
			
			int blockSize = cipher.getBlockSize();
			byte[] bufferBytes = new byte[blockSize];
			
			cos = new CipherOutputStream(outputStream, cipher);
			
			int readLength = 0;
			while((readLength = inputStream.read(bufferBytes)) != -1){
				outputStream.write(bufferBytes, 0, readLength);
			}
		} catch (Exception e) {
			throw new RuntimeException(e);
		} finally {
			FileUtil.close(cos);
			FileUtil.close(outputStream);
			FileUtil.close(inputStream);
		}
	}

	@Override
	public void decrypt(InputStream inputStream, OutputStream outputStream) {
		CipherInputStream cis = null;
		try {
			Cipher cipher = Cipher.getInstance(ALG);
			cipher.init(Cipher.DECRYPT_MODE, key);
			
			int blockSize = cipher.getBlockSize();
			byte[] bufferBytes = new byte[blockSize];
			
			cis = new CipherInputStream(inputStream, cipher);
			
			int readLength = 0;
			while((readLength = inputStream.read(bufferBytes)) != -1){
				outputStream.write(bufferBytes, 0, readLength);
			}
			
		} catch (Exception e) {
			throw new RuntimeException(e);
		} finally {
			FileUtil.close(cis);
			FileUtil.close(outputStream);
			FileUtil.close(inputStream);
		}
	}
	
}
