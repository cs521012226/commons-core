package org.ltsh.core.test.cipher;

import java.util.Map;

import org.ltsh.core.core.cipher.impl.RSAExtendCrypt;


/**
 * 非对称加密算法DEMO
 * @author 邓端宁
 * @date 2018年6月24日
 */
public class RSAExtendCryptDemo {
	static String privateKey = "MIICdwIBADANBgkqhkiG9w0BAQEFAASCAmEwggJdAgEAAoGBAKxH8iHnF85JAuQoW4PQlRVI4PFNHNDsCWsXRpxTrhNK/9dzVxfhjM8wLsotnUT++IFyy0gpZq3Dt0Czsr1IyiQujoFsedKRl55vQCpXZ4JrqQa+y059+1A9lYZ/yH9NSdidjkTTc9oTjeXTi6BVKBha2Rc7NR6lFGjv7c4tK+kFAgMBAAECgYB9wzNlPk4pfzDGHxPXII+D7ySN4Y3UkUm6zE32Vjuvx8MFoyQDwdnJcVHmsu+oazw7OvJcJiTXWhTO/Z7QAG/WTtDn4qzQr7pEAEYmzjzLgG6Ze+XhYmBQRjlGCvTmB15rAg/ACdPKioEEUQ+gE5CmKmHVdh7YVqDMSHuofwt3zQJBAO2+L9UrtXlt1g/yi1jz0ggMcG3ZJlnw7j/xQIRvNAlg6e9g7CnRjkyUWSZZthqZxwpH0qXQOXa6xN0s5jA9/isCQQC5gtZEdi4xhwl8hkcUTsodpAPP72/cEggD5ObrRkgiK0iQnSPfELEU+AaCT9kZVojHPOYKk0f25BxY9bHLSE2PAkEAlA3Fof5HVGiXT3UfXHTyznpYpb/+hzJoXL+0CwVppZGJ/b4hNGXBNWWJIz3pDyXG81sJDqPetVbYdFiavV5N4wJBAJBUwDohrIzyICpXGoQtTMlE64ZjJqeVC9FjzVmto7+Z52pEKD2L8gp7ciz+zM0O63n/4obF0g6/qWJ5PewIA/cCQFOdz5r6qSFZrmIE4ZgeTW7zi6sizTiWWgpsecoqj+zx6aRjiZrp/Mhjg/HhPsmgeWF2sOwmoU0ws8IalPWfD84=";
	static String publicKey = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCsR/Ih5xfOSQLkKFuD0JUVSODxTRzQ7AlrF0acU64TSv/Xc1cX4YzPMC7KLZ1E/viBcstIKWatw7dAs7K9SMokLo6BbHnSkZeeb0AqV2eCa6kGvstOfftQPZWGf8h/TUnYnY5E03PaE43l04ugVSgYWtkXOzUepRRo7+3OLSvpBQIDAQAB";

	/**
	 * RSA加密
	 * @param data 需要加密的明文数据
	 * @return
	 */
	public static String encrypt(String data) {
		RSAExtendCrypt rsaCrypt = new RSAExtendCrypt(); 
		return rsaCrypt.encrypt(data, publicKey);
	}
	/**
	 * RSA解密
	 * @param data 需要解密的密文数据
	 * @return
	 */
	public static String decrypt(String data) {
		RSAExtendCrypt rsaCrypt = new RSAExtendCrypt(); 
		return rsaCrypt.decrypt(data, privateKey);
	}
	/**
	 * 是否签名通过
	 * @param data
	 * @param privateKey
	 * @param publicKey
	 * @return
	 */
	public static boolean isSign(String data,String privateKey,String publicKey) {
		RSAExtendCrypt rsaCrypt = new RSAExtendCrypt(); 
		return rsaCrypt.isSign(data, privateKey, publicKey);
	}
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		RSAExtendCrypt rsaCrypt = new RSAExtendCrypt();
		Map<String, String> keyMap = rsaCrypt.getRSAKey();
		System.out.println(keyMap);
		
		String data = "广东南粤银行股份有限公司";
		String encoderData = RSAExtendCryptDemo.encrypt(data);
		System.out.println(encoderData);
		
		System.out.println(RSAExtendCryptDemo.isSign(encoderData, privateKey, publicKey));
		
		String decoderData = RSAExtendCryptDemo.decrypt(encoderData);
		System.out.println(decoderData);
	}

}
