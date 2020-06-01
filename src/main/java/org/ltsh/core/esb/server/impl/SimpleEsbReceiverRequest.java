package org.ltsh.core.esb.server.impl;

import com.alibaba.fastjson.JSONObject;
import org.ltsh.core.esb.consts.EsbConsts;
import org.ltsh.core.esb.server.EsbReceiverRequest;
import org.ltsh.core.esb.vo.MsgNode;

/**
 * 简单传输数据实现
 * @author YeChao
 * 2017年7月25日
 */
public class SimpleEsbReceiverRequest implements EsbReceiverRequest {
	
	private MsgNode rootNode;
	
	private MsgNode sdoRoot;
	private MsgNode sysHead;
	private MsgNode appHead;
	private MsgNode localHead;
	private MsgNode body;
	
	
	public SimpleEsbReceiverRequest(MsgNode rootNode){
		this.rootNode = rootNode;
		
		sdoRoot = rootNode.getChild(EsbConsts.SDOROOT);
		sysHead = sdoRoot.getChild(EsbConsts.SYS_HEAD);
		appHead = sdoRoot.getChild(EsbConsts.APP_HEAD);
		localHead = sdoRoot.getChild(EsbConsts.LOCAL_HEAD);
		body = sdoRoot.getChild(EsbConsts.BODY);
	}
	
	@Override
	public String toString() {
		return JSONObject.toJSONString(this);
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
	
}
