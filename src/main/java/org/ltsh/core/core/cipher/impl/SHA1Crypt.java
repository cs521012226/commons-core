package org.ltsh.core.core.cipher.impl;

import org.ltsh.core.core.cipher.CipherHelper;
import org.ltsh.core.core.cipher.base.MsgDigest;


/**
 * SHA1加密
 * @author Ych
 * 2017年10月9日
 */
public class SHA1Crypt extends MsgDigest{

	public SHA1Crypt(){
		this(null);
	}
	public SHA1Crypt(CipherHelper cipherHelper){
		super("SHA1", cipherHelper);
	}
	
}
