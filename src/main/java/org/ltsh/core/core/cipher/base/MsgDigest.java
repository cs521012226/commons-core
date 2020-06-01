package org.ltsh.core.core.cipher.base;

import java.io.InputStream;
import java.io.OutputStream;
import java.security.MessageDigest;

import org.ltsh.core.core.cipher.CipherHelper;

/**
 * 数字指纹散列加密基类
 * @author Ych
 * 2017年10月9日
 */
public abstract class MsgDigest extends BaseCipher{
	
	private String alg;		//算法名称
	private String salt;	//盐
	
	public MsgDigest(String alg, CipherHelper cipherHelper){
		super(cipherHelper);
		this.alg = alg;
	}
	
	/**
	 * 散列加密
	 * @author Ych
	 * @param alg	算法
	 * @param data	明文数据
	 * @return
	 */
	protected String messageDigest(String data){
		try {
			data = encryptInternal(data);
			
			MessageDigest md = MessageDigest.getInstance(alg);
			md.update(data.getBytes(charset));
			byte[] digest = md.digest();
			return convertByteToHexStr(digest);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	@Override
	public String encrypt(String data) {
		return messageDigest(data);
	}

	@Override
	public void encrypt(InputStream inputStream, OutputStream outputStream) {
		throw new UnsupportedOperationException();
	}

	@Override
	public String decrypt(String data) {
		throw new UnsupportedOperationException();
	}

	@Override
	public void decrypt(InputStream inputStream, OutputStream outputStream) {
		throw new UnsupportedOperationException();
	}

	public String getSalt() {
		return salt;
	}

	public void setSalt(String salt) {
		this.salt = salt;
	}
}
