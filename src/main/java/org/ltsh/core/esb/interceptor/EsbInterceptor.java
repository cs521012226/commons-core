package org.ltsh.core.esb.interceptor;

/**
 * Esb报文拦截器
 * @author YeChao
 * 2017年7月26日
 */
public interface EsbInterceptor {

	/**
	 * 处理拦截
	 * @author YeChao
	 * @param inv
	 */
	void intercept(EsbInvocation inv);
}
