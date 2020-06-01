package org.ltsh.core.core.cipher.impl;

import org.ltsh.core.core.cipher.CipherHelper;
import org.ltsh.core.core.cipher.base.MsgDigest;


/**
 * MD5加密
 * @author Ych
 * 2017年10月9日
 */
public class MD5Crypt extends MsgDigest{
	
	public MD5Crypt(){
		this(null);
	}

	public MD5Crypt(CipherHelper cipherHelper){
		super("MD5", cipherHelper);
	}
	
}
