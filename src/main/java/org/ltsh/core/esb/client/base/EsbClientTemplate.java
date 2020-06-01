package org.ltsh.core.esb.client.base;

import java.io.IOException;
import java.util.Date;
import java.util.Random;

import org.ltsh.core.core.constants.Config;
import org.ltsh.core.core.util.DateUtil;
import org.ltsh.core.core.util.StringUtil;
import org.ltsh.core.esb.builder.MsgConvert;
import org.ltsh.core.esb.client.base.impl.SimpleEsbSenderRequest;
import org.ltsh.core.esb.client.base.impl.SimpleEsbSenderResponse;
import org.ltsh.core.esb.exp.TransException;
import org.ltsh.core.esb.vo.MsgNode;

import com.alibaba.fastjson.JSON;

/**
 * ESB请求客户端模版类
 * @author Ych
 * 2017年11月22日
 */
public abstract class EsbClientTemplate {
	private String charset = Config.CHARSET;
	private String serviceCode;
	private String serviceScene;
	private String consumerId;
	private String orgSysId;
	private String tranDate;
	private String tranTimestamp;
	
	private String branchId;
	private String userId;
	
	private EsbComm esbCommu;
	private MsgConvert msgConvert;
	
	public EsbClientTemplate(EsbComm esbCommu, MsgConvert msgConvert){
		Date now = new Date();
		this.esbCommu = esbCommu;
		this.msgConvert = msgConvert;
		tranDate = DateUtil.convertDateToString(now, DateUtil.Pattern.DATE8);
		tranTimestamp = DateUtil.convertDateToString(now, DateUtil.Pattern.TIME9);
	}
	
	/**
	 * 组装通用请求头信息
	 * @author Ych
	 * @return
	 */
	protected EsbSenderRequest buildCommonRequest(EsbSenderRequest request){
		
		Random r = new Random();
		String consumerSeqNo = orgSysId + tranDate + StringUtil.leftPad(String.valueOf(r.nextInt(99999999)), '0', 8);
		
		request.setServiceCode(serviceCode);
		request.setServiceScene(serviceScene);
		request.setConsumerId(consumerId);
		request.setConsumerSeqNo(consumerSeqNo);
		request.setTranDate(tranDate);
		request.setTranTimestamp(tranTimestamp);
		request.setOrgSysId(orgSysId);
		
		request.setBranchId(branchId);
		request.setUserId(userId);
		
		return request;
	}
	
	/**
	 * EsbSenderRequest工厂
	 * @author Ych
	 * @return
	 */
	protected EsbSenderRequest esbSenderRequestFactroy(){
		return new SimpleEsbSenderRequest();
	};
	
	/**
	 * EsbSenderResponse工厂
	 * @author Ych
	 * @return
	 */
	protected EsbSenderResponse esbSenderResponseFactroy(MsgNode msgNode){
		return new SimpleEsbSenderResponse(msgNode);
	};
	
	/**
	 * 发送入口
	 * @author Ych
	 * @throws TransException
	 */
	public void send() throws IOException {
		MsgNode msgNode = null;
		
		//先校验参数是否合法
		checkSendParamInternal();
		//生成请求对象
		EsbSenderRequest request = esbSenderRequestFactroy();
		//构建通用头字段信息
		buildCommonRequest(request);
		//构建自定义字段信息
		sendBefore(request);		
		
		//转换器转换对象为报文格式
		String msg = msgConvert.toMessage(request.getRoot());	
		//消息发送与接收
		msg = esbCommu.send(msg);	
		//转换器转换报文格式为对象
		msgNode = msgConvert.toObject(msg);
		
		//响应报文解析后组装生成响应对象
		EsbSenderResponse response = esbSenderResponseFactroy(msgNode);	
		//处理子类业务逻辑
		sendAfter(response);
	}
	
	/**
	 * 内部检查参数是否合法
	 * @author Ych
	 */
	private void checkSendParamInternal() throws TransException{
		//检查必填参数
		checkRequiredParam(serviceCode, "serviceCode");
		checkRequiredParam(serviceScene, "serviceScene");
		checkRequiredParam(consumerId, "consumerId");
		checkRequiredParam(tranDate, "tranDate");
		checkRequiredParam(tranTimestamp, "tranTimestamp");
		checkRequiredParam(orgSysId, "orgSysId");
		checkRequiredParam(branchId, "branchId");
		checkRequiredParam(userId, "userId");
		//检查子类参数逻辑
		validParam();
	}
	
	/**
	 * 检查必填参数通用方法
	 * @author Ych
	 * @param paramKey
	 * @param paramName
	 * @throws TransException
	 */
	protected void checkRequiredParam(String paramKey, String paramName) throws TransException{
		if(StringUtil.isBlank(paramKey)){
			throw TransException.error("缺少必填参数: " + paramName);
		}
	}
	
	/**
	 * 发送之前校验参数
	 * @author Ych
	 */
	protected abstract void validParam() throws TransException;
	
	/**
	 * 发送之前要处理的业务逻辑
	 * @author Ych
	 * @param request
	 */
	protected abstract void sendBefore(EsbSenderRequest req);
	
	/**
	 * 发送之后要处理的业务逻辑
	 * @author Ych
	 * @param response
	 */
	protected abstract void sendAfter(EsbSenderResponse response);
	
	@Override
	public String toString() {
		return JSON.toJSONString(this);
	}

	public String getCharset() {
		return charset;
	}
	public void setCharset(String charset) {
		this.charset = charset;
	}
	public String getServiceCode() {
		return serviceCode;
	}
	public void setServiceCode(String serviceCode) {
		this.serviceCode = serviceCode;
	}
	public String getServiceScene() {
		return serviceScene;
	}
	public void setServiceScene(String serviceScene) {
		this.serviceScene = serviceScene;
	}
	public String getConsumerId() {
		return consumerId;
	}
	public void setConsumerId(String consumerId) {
		this.consumerId = consumerId;
	}
	public String getOrgSysId() {
		return orgSysId;
	}
	public void setOrgSysId(String orgSysId) {
		this.orgSysId = orgSysId;
	}
	public String getTranDate() {
		return tranDate;
	}
	public void setTranDate(String tranDate) {
		this.tranDate = tranDate;
	}
	public String getTranTimestamp() {
		return tranTimestamp;
	}
	public void setTranTimestamp(String tranTimestamp) {
		this.tranTimestamp = tranTimestamp;
	}
	public String getBranchId() {
		return branchId;
	}
	public void setBranchId(String branchId) {
		this.branchId = branchId;
	}
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public EsbComm getEsbCommu() {
		return esbCommu;
	}
	public void setEsbCommu(EsbComm esbCommu) {
		this.esbCommu = esbCommu;
	}
	public MsgConvert getMsgConvert() {
		return msgConvert;
	}
	public void setMsgConvert(MsgConvert msgConvert) {
		this.msgConvert = msgConvert;
	}
}
