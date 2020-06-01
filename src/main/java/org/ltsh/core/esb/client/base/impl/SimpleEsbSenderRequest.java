package org.ltsh.core.esb.client.base.impl;

import com.alibaba.fastjson.JSONObject;
import org.ltsh.core.esb.client.base.EsbSenderRequest;
import org.ltsh.core.esb.consts.EsbConsts;
import org.ltsh.core.esb.vo.MsgNode;

/**
 * 简单传输数据实现
 * @author Ych
 * 2017年7月25日
 */
public class SimpleEsbSenderRequest implements EsbSenderRequest {
	
	private MsgNode rootNode;
	
	private MsgNode root;
	private MsgNode sysHead;
	private MsgNode appHead;
	private MsgNode localHead;
	private MsgNode body;
	
	public SimpleEsbSenderRequest(){
		rootNode =  new MsgNode();
		root = rootNode.addChild(EsbConsts.SDOROOT);
		
		sysHead = root.addChild(EsbConsts.SYS_HEAD);
//		appHead = root.addChild(EsbConsts.APP_HEAD);
//		localHead = root.addChild(EsbConsts.LOCAL_HEAD);
		body = root.addChild(EsbConsts.BODY);
	}
	
	private void setAppHeadValue(String key, String value){
		if(appHead == null){
			appHead = root.addChild(EsbConsts.APP_HEAD);
		}
		appHead.set(key, value);
	}
	private String getAppHeadValue(String key){
		return appHead == null ? "" : appHead.get(key);
	}
	private void setLocalHeadValue(String key, String value){
		if(localHead == null){
			localHead = root.addChild(EsbConsts.LOCAL_HEAD);
		}
		localHead.set(key, value);
	}
	private String getLocalHeadValue(String key){
		return localHead == null ? "" : localHead.get(key);
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
		return getAppHeadValue(EsbConsts.BRANCH_ID);
	}
	@Override
	public String getUserId() {
		return getAppHeadValue(EsbConsts.USER_ID);
	}
	
	@Override
	public String getSysHead(String key) {
		return sysHead.get(key);
	}
	@Override
	public String getAppHead(String key) {
		return getAppHeadValue(key);
	}
	@Override
	public String getLocalHead(String key) {
		return getLocalHeadValue(key);
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
		setAppHeadValue(EsbConsts.BRANCH_ID, branchId);
	}
	@Override
	public void setUserId(String userId) {
		setAppHeadValue(EsbConsts.USER_ID, userId);
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
		setAppHeadValue(key, value);
	}

	@Override
	public void setBodyHead(String key, String value) {
		body.set(key, value);
	}


	@Override
	public void setLocalHead(String key, String value) {
		setLocalHeadValue(key, value);
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
