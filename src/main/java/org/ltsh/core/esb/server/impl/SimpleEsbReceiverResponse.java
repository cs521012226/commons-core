package org.ltsh.core.esb.server.impl;

import com.alibaba.fastjson.JSONObject;
import org.ltsh.core.esb.consts.EsbConsts;
import org.ltsh.core.esb.server.EsbReceiverResponse;
import org.ltsh.core.esb.vo.MsgNode;
import org.ltsh.core.esb.vo.MsgNodeInfo;

/**
 * 简单传输数据实现
 * @author YeChao
 * 2017年7月25日
 */
public class SimpleEsbReceiverResponse implements EsbReceiverResponse {
	
	private MsgNode rootNode;
	
	private MsgNode root;
	private MsgNode sysHead;
	private MsgNode appHead;
	private MsgNode localHead;
	private MsgNode body;
	
	private MsgNodeInfo resStatus;
	private MsgNodeInfo resCode;
	private MsgNodeInfo resMsg;
	
	public SimpleEsbReceiverResponse(){
		rootNode =  new MsgNode();
		root = rootNode.addChild(EsbConsts.SDOROOT);
		
		sysHead = root.addChild(EsbConsts.SYS_HEAD);
		appHead = root.addChild(EsbConsts.APP_HEAD);
		localHead = root.addChild(EsbConsts.LOCAL_HEAD);
		body = root.addChild(EsbConsts.BODY);
		
		buildStatus(sysHead);
	}
	
	private boolean success = true;
	private String msg;
	private String code;
	
	
	private void buildStatus(MsgNode sysHead){
		if(resStatus == null){
			resStatus = sysHead.set(EsbConsts.RET_STATUS, EsbConsts.RET_STATUS_FAIL);
		}
		if(resCode == null){
			MsgNode ret = sysHead.addChild(EsbConsts.RET);
			MsgNode sdo = ret.addChild(EsbConsts.SDO);
			resCode = sdo.set(EsbConsts.RET_CODE, null);
			resMsg = sdo.set(EsbConsts.RET_MSG, null);
		}
	}
	
	@Override
	public void setServiceCode(String serviceCode) {
		sysHead.set(EsbConsts.SERVICE_CODE, serviceCode);
	}

	@Override
	public void setConsumerId(String consumerId) {
		sysHead.set(EsbConsts.CONSUMER_ID, consumerId);
	}

	@Override
	public void setConsumerSeqNo(String consumerSeqNo) {
		sysHead.set(EsbConsts.CONSUMER_SEQ_NO, consumerSeqNo);
	}

	@Override
	public void setServiceScene(String serviceScene) {
		sysHead.set(EsbConsts.SERVICE_SCENE, serviceScene);
	}

	@Override
	public void setTranDate(String tranDate) {
		sysHead.set(EsbConsts.TRAN_DATE, tranDate);
	}

	@Override
	public void setTranTimestamp(String tranTimestamp) {
		sysHead.set(EsbConsts.TRAN_TIMESTAMP, tranTimestamp);
	}

	@Override
	public void setEsbSeqNo(String esbSeqNo) {
		sysHead.set(EsbConsts.ESB_SEQ_NO, esbSeqNo);
	}
	

	@Override
	public void setOrgSysId(String orgSysId) {
		sysHead.set(EsbConsts.ORG_SYS_ID, orgSysId);
	}

	@Override
	public void setBranchId(String branchId) {
		appHead.set(EsbConsts.BRANCH_ID, branchId);
	}

	@Override
	public void setUserId(String userId) {
		appHead.set(EsbConsts.USER_ID, userId);
	}

	@Override
	public String toString() {
		return JSONObject.toJSONString(this);
	}


	@Override
	public void setSysHead(String key, String value) {
		sysHead.set(key, value);
	}

	@Override
	public void setAppHead(String key, String value) {
		appHead.set(key, value);
	}


	@Override
	public void setLocalHead(String key, String value) {
		localHead.set(key, value);
	}

	@Override
	public void setBodyHead(String key, String value) {
		body.set(key, value);
	}

	@Override
	public String getServiceCode() {
		return sysHead.get(EsbConsts.SERVICE_CODE);
	}
	@Override
	public String getConsumerId() {
		return sysHead.get(EsbConsts.CONSUMER_ID);
	}
	
	@Override
	public String getConsumerSeqNo() {
		return sysHead.get(EsbConsts.CONSUMER_SEQ_NO);
	}
	@Override
	public String getServiceScene() {
		return sysHead.get(EsbConsts.SERVICE_SCENE);
	}
	
	@Override
	public String getTranDate() {
		return sysHead.get(EsbConsts.TRAN_DATE);
	}
	@Override
	public String getTranTimestamp() {
		return sysHead.get(EsbConsts.TRAN_TIMESTAMP);
	}
	
	@Override
	public String getEsbSeqNo() {
		return sysHead.get(EsbConsts.ESB_SEQ_NO);
	}
	@Override
	public String getOrgSysId() {
		return sysHead.get(EsbConsts.ORG_SYS_ID);
	}
	@Override
	public String getBranchId() {
		return appHead.get(EsbConsts.BRANCH_ID);
	}
	@Override
	public String getUserId() {
		return appHead.get(EsbConsts.USER_ID);
	}
	
	@Override
	public String getSysHead(String key) {
		return sysHead.get(key);
	}
	@Override
	public String getAppHead(String key) {
		return appHead.get(key);
	}
	@Override
	public String getLocalHead(String key) {
		return localHead.get(key);
	}

	@Override
	public String getBodyHead(String key) {
		return body == null ? null : body.get(key);
	}

	@Override
	public MsgNode getBody() {
		return body;
	}

	@Override
	public MsgNode getRoot() {
		return rootNode;
	}

	@Override
	public void setSuccess(boolean success) {
		this.success = success;
		String status;
		String retCode;
		if(success){
			status = EsbConsts.RET_STATUS_SUCCESS;
			retCode = "I" + getConsumerId() + "000001";
		}else{
			status = EsbConsts.RET_STATUS_FAIL;
			retCode = "E" + getConsumerId() + "000001";
		}
		resStatus.setValue(status);
		resCode.setValue(retCode);
	}
	

	@Override
	public boolean isSuccess() {
		return success;
	}

	@Override
	public void setMsg(String msg) {
		this.msg = msg;
		resMsg.setValue(EsbConsts.RET_STATUS_SUCCESS);
	}
	

	@Override
	public String getMsg() {
		return msg;
	}

	@Override
	public void setCode(String code) {
		this.code = code;
		resCode.setValue(code);
	}

	@Override
	public String getCode() {
		return code;
	}
	
}
