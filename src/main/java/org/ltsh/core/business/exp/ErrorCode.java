package org.ltsh.core.business.exp;

/**
 * 系统错误码
 * @author Ych
 * 2018年3月5日
 */
public enum ErrorCode {
	/**
	 * 0000: 操作成功
	 */
	SUCCESS("0000", "操作成功"),
	/**
	 * 9999: 操作失败
	 */
	FAIL("9999", "操作失败"),
	
	/**
	 * 9999: 系统错误
	 */
	ERROR("9999", "系统错误"),
	/**
	 * "0000", "正常响应"
	 */
	INFO("0000", "正常响应"),
	/**
	 * "9992", "请求资源已被拦截, 拒绝连接"
	 */
	INTERCEPTOR("9992", "请求资源已被拦截, 拒绝连接"),
	/**
	 * "9995", "无权限操作"
	 */
	NO_PERMISSION("9995", "无权限操作"),
	/**
	 * "9997", "登录超时"
	 */
	SESSION_TIME_OUT("9997", "登录超时"),
	/**
	 * "9998", "数据库操作异常"
	 */
	SQL_ERROR("9998", "数据库操作异常");
	
	private String code;
	private String message;
	
	ErrorCode(String code, String message){
		this.code = code;
		this.message = message;
	}
	
	/**
	 * 错误码是否相等
	 * @author Ych
	 * @param code
	 * @return
	 */
	public boolean eq(String code){
		return this.code.equals(code);
	}

	public String getCode() {
		return code;
	}

	public String getMessage() {
		return message;
	}
	
}
