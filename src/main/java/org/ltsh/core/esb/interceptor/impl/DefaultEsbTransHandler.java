package org.ltsh.core.esb.interceptor.impl;

import org.ltsh.core.esb.exp.TransException;
import org.ltsh.core.esb.interceptor.EsbInterceptor;
import org.ltsh.core.esb.interceptor.EsbInvocation;

/**
 * 默认报文处理程序
 * @author YeChao
 * 2017年7月26日
 */
public class DefaultEsbTransHandler implements EsbInterceptor {
	
	@Override
	public void intercept(EsbInvocation inv) {
		throw TransException.error("无程序处理报文");
	}

}
