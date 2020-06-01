package org.ltsh.core.esb.interceptor;

import java.util.List;

import org.ltsh.core.esb.exp.TransException;
import org.ltsh.core.esb.server.EsbReceiverRequest;
import org.ltsh.core.esb.server.EsbReceiverResponse;

/**
 * ESB拦截器调用逻辑
 * @author YeChao
 * 2017年7月26日
 */
public class EsbInvocation {

	private List<EsbInterceptor> interceptorList;
	private int index = 0;
	
	private EsbReceiverRequest request;
	private EsbReceiverResponse response;
	
	public EsbInvocation(EsbReceiverRequest request, EsbReceiverResponse response, List<EsbInterceptor> interceptorList){
		this.request = request;
		this.response = response;
		this.interceptorList = interceptorList;
	}
	
	/**
	 * 拦截器调用人口
	 * @author YeChao
	 * @throws TransException
	 */
	public void invoke() throws TransException{
		if (index < interceptorList.size()) {
			interceptorList.get(index++).intercept(this);
		}
	}
	
	public List<EsbInterceptor> getInterceptorList() {
		return interceptorList;
	}

	public EsbReceiverRequest getRequest() {
		return request;
	}

	public EsbReceiverResponse getResponse() {
		return response;
	}

}
