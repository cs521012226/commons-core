package org.ltsh.core.core.cipher.impl;

import java.io.InputStream;
import java.io.OutputStream;

import org.apache.commons.codec.binary.Base64;

import org.ltsh.core.core.cipher.CipherHelper;
import org.ltsh.core.core.cipher.base.BaseCipher;

/**
 * Base64编码、解码
 * @author Ych
 * 2017年9月21日
 */
public class Base64Crypt extends BaseCipher{
	
	public Base64Crypt(){
		this(null);
	}
	
	public Base64Crypt(CipherHelper cipherHelper){
		super(cipherHelper);
	}
	
	@Override
	public String encrypt(String data) {
		data = encryptInternal(data);
		return Base64.encodeBase64String(convertStrToByte(data, charset));
	}
	@Override
	public void encrypt(InputStream inputStream, OutputStream outputStream) {
		throw new UnsupportedOperationException();
	}
	@Override
	public String decrypt(String data) {
		byte[] bt = null;
		bt = Base64.decodeBase64(data);
		data = convertByteToStr(bt, charset);
		return decryptInternal(data);
	}
	@Override
	public void decrypt(InputStream inputStream, OutputStream outputStream) {
		throw new UnsupportedOperationException();
	}
}
