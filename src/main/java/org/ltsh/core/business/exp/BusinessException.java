package org.ltsh.core.business.exp;

/**
 * 业务类型异常
 * @author Ych
 */
public class BusinessException extends RuntimeException{
	private static final long serialVersionUID = 1L;
	private String code;
	
	public BusinessException(String code, String message) {
		super(message);
		this.code = code;
	}
	
	public BusinessException(String code, String message, Throwable cause) {
		super(message, cause);
		this.code = code;
	}

	public String getCode() {
		return code;
	}
	
	/**
	 * 错误类型异常
	 * @author Ych
	 * @param message
	 */
	public static void err(String message){
		throw new BusinessException(ErrorCode.ERROR.getCode(), message);
	}
	/**
	 * 错误类型异常
	 * @author Ych
	 * @param message
	 * @param cause
	 */
	public static void err(String message, Throwable cause){
		throw new BusinessException(ErrorCode.ERROR.getCode(), message, cause);
	}
	
	/**
	 * 提示消息异常
	 * @author Ych
	 * @param message
	 */
	public static void info(String message){
		throw new BusinessException(ErrorCode.INFO.getCode(), message);
	}
	
}
