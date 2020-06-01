package org.ltsh.core.core.cipher;

import java.io.InputStream;
import java.io.OutputStream;

/**
 * 加解密接口
 * @author Ych
 * 2017年9月21日
 */
public interface CipherHelper {
	
	/**
	 * 加密
	 * @author Ych
	 * @param data	明文
	 * @return	密文
	 */
	String encrypt(String data);
	
	/**
	 * 加密数据流
	 * @author Ych
	 * @param inputStream	输入流
	 * @param outputStream	输出流
	 * @return
	 */
	void encrypt(InputStream inputStream, OutputStream outputStream);
	
	/**
	 * 解密
	 * @author Ych
	 * @param data	密文
	 * @return	明文
	 */
	String decrypt(String data);
	
	/**
	 * 解密数据流
	 * @author Ych
	 * @param inputStream	输入流
	 * @param outputStream	输出流
	 * @return
	 */
	void decrypt(InputStream inputStream, OutputStream outputStream);
	
}
