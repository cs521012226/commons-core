package org.ltsh.core.core.cipher.impl;

import org.ltsh.core.core.cipher.CipherHelper;
import org.ltsh.core.core.cipher.base.MsgDigest;


/**
 * SHA256加密
 * @author Ych
 * 2017年10月9日
 */
public class SHA256Crypt extends MsgDigest{

	public SHA256Crypt(){
		this(null);
	}
	public SHA256Crypt(CipherHelper cipherHelper){
		super("SHA-256", cipherHelper);
	}
	
}
