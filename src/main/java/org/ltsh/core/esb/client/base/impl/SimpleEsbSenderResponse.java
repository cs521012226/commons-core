package org.ltsh.core.esb.client.base.impl;

import com.alibaba.fastjson.JSONObject;
import org.ltsh.core.esb.client.base.EsbSenderResponse;
import org.ltsh.core.esb.consts.EsbConsts;
import org.ltsh.core.esb.vo.MsgNode;
import org.ltsh.core.esb.vo.MsgNodeInfo;

/**
 * 简单传输数据实现
 * @author Ych
 * 2017年7月25日
 */
public class SimpleEsbSenderResponse implements EsbSenderResponse {
	
	private MsgNode rootNode;
	
	private MsgNode sdoRoot;
	private MsgNode sysHead;
	private MsgNode appHead;
	private MsgNode localHead;
	private MsgNode body;
	
	private boolean success = false;
	private String msg;
	private String code;
	
	
	public SimpleEsbSenderResponse(MsgNode rootNode){
		this.rootNode = rootNode;
		
		sdoRoot = rootNode.getChild(EsbConsts.SDOROOT);
		sysHead = sdoRoot.getChild(EsbConsts.SYS_HEAD);
		appHead = sdoRoot.getChild(EsbConsts.APP_HEAD);
		localHead = sdoRoot.getChild(EsbConsts.LOCAL_HEAD);
		body = sdoRoot.getChild(EsbConsts.BODY);
		
		checkStatus(sysHead);
	}
	
	
	/**
	 * 检查响应状态
	 * @author Ych
	 */
	private void checkStatus(MsgNode node){
		if(node == null || node.getFields().isEmpty()){
			return ;
		}
		
		for(MsgNodeInfo info : node.getFields()){
			String name = info.getKey();
			String value = info.getValue();
			MsgNode subNode = info.getOneValue();
			
			if(EsbConsts.RET_STATUS.equals(name)){
				if(EsbConsts.RET_STATUS_SUCCESS.equals(value)){
					this.success = true;
				}else{
					this.success = false;
				}
			}
			if(EsbConsts.RET_MSG.equals(name)){
				this.msg = value;
			}
			if(EsbConsts.RET_CODE.equals(name)){
				this.code = value;
			}
			checkStatus(subNode);
		}
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
	public boolean isEmpty() {
		return body == null || body.isEmpty();
	}
	@Override
	public void setSuccess(boolean success) {
		this.success = success;
	}
	@Override
	public boolean isSuccess() {
		return success;
	}
	@Override
	public void setMsg(String msg) {
		this.msg = msg;
	}
	@Override
	public String getMsg() {
		return msg;
	}
	
	@Override
	public void setCode(String code) {
		this.code = code;
	}

	@Override
	public String getCode() {
		return code;
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
