package org.ltsh.core.core.cipher.base;

import java.io.UnsupportedEncodingException;

import org.ltsh.core.core.cipher.CipherHelper;
import org.ltsh.core.core.constants.Config;


/**
 * 装饰者模式基础加解密类
 * @author Ych
 * 2018年3月1日
 */
public abstract class BaseCipher implements CipherHelper {

	protected String charset = Config.CHARSET;
	protected CipherHelper cipherHelper;
	
	public BaseCipher(){
		
	}
	
	public BaseCipher(CipherHelper cipherHelper){
		this.cipherHelper = cipherHelper;
	}
	
	/**
	 * 字节数组转为普通字符串
	 * @author Ych
	 * @param data
	 * @return
	 */
	public static String convertByteToStr(byte[] data, String charset){
		String rs = null;
		try {
			rs = new String(data, charset);
		} catch (UnsupportedEncodingException e) {
			throw new RuntimeException(e);
		}
		return rs;
	}
	/**
	 * 字节数组转16进制字符串
	 * @author Ych
	 * @param data
	 * @return
	 */
	public static String convertByteToHexStr(byte[] data){
		StringBuffer hexstr = new StringBuffer();
		String shaHex = "";
		for (int i = 0; i < data.length; i++) {
			shaHex = Integer.toHexString(data[i] & 0xFF);
			if (shaHex.length() < 2) {
				hexstr.append(0);
			}
			hexstr.append(shaHex);
		}
		return hexstr.toString();
	}
	
	/**
	 * 字符串转字节数组
	 * @author Ych
	 * @param data
	 * @return
	 */
	public static byte[] convertStrToByte(String data, String charset){
		try {
			return data.getBytes(charset);
		} catch (UnsupportedEncodingException e) {
			throw new RuntimeException(e);
		}
	}
	
	/**
	 * 16进制字符串转字节数组
	 * @author Ych
	 * @param data
	 * @return
	 */
	public static byte[] convertHexStrToByte(String data){
		int len = data.length();
        byte[] b = new byte[len / 2];
        for (int i = 0; i < len; i += 2) {
            b[i / 2] = (byte) ((Character.digit(data.charAt(i), 16) << 4) + Character.digit(data.charAt(i+1), 16));
        }
        return b;
	}
	
	/**
	 * 内部调用需要装饰的类的加密
	 * @author Ych
	 * @param data
	 * @return
	 */
	protected String encryptInternal(String data){
		return cipherHelper == null ? data : cipherHelper.encrypt(data);
	}
	
	/**
	 * 内部调用需要装饰的类的解密
	 * @author Ych
	 * @param data
	 * @return
	 */
	protected String decryptInternal(String data){
		return cipherHelper == null ? data : cipherHelper.decrypt(data);
	}
	
	public String getCharset() {
		return charset;
	}

	public void setCharset(String charset) {
		this.charset = charset;
	}
	
}
