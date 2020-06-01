package org.ltsh.core.esb.message.mobile.impl;

import org.ltsh.core.core.cipher.impl.MD5Crypt;
import org.ltsh.core.esb.builder.XmlMsgConvert;
import org.ltsh.core.esb.client.base.EsbClientTemplate;
import org.ltsh.core.esb.client.base.EsbSenderRequest;
import org.ltsh.core.esb.client.base.EsbSenderResponse;
import org.ltsh.core.esb.client.base.SocketEsbComm;
import org.ltsh.core.esb.exp.TransException;
import org.ltsh.core.esb.message.mobile.MobileMsg;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSON;

/**
 * ESB短信报文接口
 * @author Ych
 * 2017年8月17日
 */
public class EsbMobileMsg extends EsbClientTemplate implements MobileMsg {
	protected Logger logger = LoggerFactory.getLogger(getClass());
	
	private MD5Crypt md5Crypt = new MD5Crypt();
	
	private String channelCode;
	private String password;
	private String flag;
	private String verifyType;
	private String content;
	private String mobile;
	
	private boolean success;
	private String code;
	private String message;
	
	public EsbMobileMsg(String host, int port, int timeout){
		super(new SocketEsbComm(host, port, timeout), new XmlMsgConvert());
	}

	
	@Override
	public String toString() {
		return JSON.toJSONString(this);
	}
	
	@Override
	protected void validParam() throws TransException {
		checkRequiredParam(channelCode, "channelCode");
		checkRequiredParam(flag, "flag");
		checkRequiredParam(mobile, "mobile");
		checkRequiredParam(content, "content");
		checkRequiredParam(verifyType, "verifyType");
	}

	@Override
	protected void sendBefore(EsbSenderRequest request) {
		String verifyValue = md5Crypt.encrypt(channelCode + password + mobile + content);
		request.setBodyHead("CHANNEL_CODE", channelCode);
		request.setBodyHead("FLAG", flag);
		request.setBodyHead("MOBILE", mobile);
		request.setBodyHead("MSG", content);
		request.setBodyHead("VERIFY_TYPE", verifyType);
		request.setBodyHead("PASSWORD", password);
		request.setBodyHead("VERIFY_VALUE", verifyValue);
	}


	@Override
	protected void sendAfter(EsbSenderResponse response) {
		if(response.isSuccess()){
			success = true;
			message = "发送短信成功";
		}else{
			success = false;
			code = response.getCode();
			message = response.getMsg();
		}
	}

	@Override
	public void setCharset(String charset) {
	}
	@Override
	public void setMobile(String mobile) {
		this.mobile = mobile;
	}
	@Override
	public void setContent(String content) {
		this.content = content;
	}

	public String getChannelCode() {
		return channelCode;
	}

	public void setChannelCode(String channelCode) {
		this.channelCode = channelCode;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getFlag() {
		return flag;
	}

	public void setFlag(String flag) {
		this.flag = flag;
	}

	public String getVerifyType() {
		return verifyType;
	}

	public void setVerifyType(String verifyType) {
		this.verifyType = verifyType;
	}
	public boolean isSuccess() {
		return success;
	}
	public String getCode() {
		return code;
	}
	public String getMessage() {
		return message;
	}
	public static void main(String[] args) throws Exception{
		EsbMobileMsg msg = new EsbMobileMsg("11.8.129.121", 30104, 10 * 1000);
		
		msg.setServiceCode("11002000003");
		msg.setServiceScene("04");
		msg.setConsumerId("040401");
		msg.setOrgSysId("040401");
		msg.setBranchId("9900");
		msg.setUserId("99004");
		msg.setChannelCode("EKP");

		msg.setFlag("0");
		msg.setVerifyType("MD5");
		msg.setPassword("ekpekp");
		
		msg.setMobile("18718805335");
		msg.setContent("测试内容");
		
		msg.send();
		
		System.out.println(msg.getMessage());
		System.out.println(msg.getCode());
		System.out.println(msg.isSuccess());
	}
}
