package org.ltsh.core.esb.exp;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 传输服务异常
 * @author Ych
 * 2017年7月24日
 */
public class TransException extends RuntimeException {
	public static Logger logger = LoggerFactory.getLogger(TransException.class);

	private static final long serialVersionUID = 1L;

	public TransException(String message){
		super(message);
	}

	public TransException(String message, Throwable cause) {
		super(message, cause);
	}
	
	public static TransException error(String msg){
		logger.error(msg);
		return new TransException(msg);
	}
	
	public static TransException error(String msg, Throwable cause){
		logger.error(msg, cause);
		return new TransException(msg, cause);
	}
}
