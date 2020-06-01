package org.ltsh.core.esb.message;

import org.ltsh.core.esb.vo.MsgNode;

/**
 * ESB报文数据设置接口
 * @author Ych
 * 2017年11月20日
 */
public interface EsbMsgSetter {

	/**
	 * 服务代码	服务唯一标识  （见下面SERVICE_CODE详细说明）
	 * @author Ych
	 * @return
	 */
	void setServiceCode(String serviceCode);
	/**
	 * 服务应用场景	描述每个服务的应用场景，具体在每个服务中说明
	 * @author Ych
	 * @return
	 */
	void setServiceScene(String serviceScene);
	/**
	 * 请求系统编号	消费系统编号 类型见：应用系统编码列表
	 * @author Ych
	 * @return
	 */
	void setConsumerId(String consumerId);
	
	/**
	 * 服务请求发送方流水号	请求方发起每一笔报文时的流水号，每笔报文的唯一标识 需要CONSUMER上送，以便跟踪  建议请求系统编号规则：流水号编码规则
	 * @author Ych
	 * @return
	 */
	void setConsumerSeqNo(String consumerSeqNo);
	
	/**
	 * 发送方交易日期	交易发起的日期
	 * @author Ych
	 * @param tranDate	格式：YYYYMMDD
	 */
	void setTranDate(String tranDate);
	/**
	 * 发送方交易时间	交易发起的时间
	 * @author Ych
	 * @param tranTimestamp	格式：HHmmddSSS
	 */
	void setTranTimestamp(String tranTimestamp);
	
	/**
	 * ESB产生的流水号	由ESB产生的流水号，请求系统无需上送该字段
	 * @author Ych
	 * @return
	 */
	void setEsbSeqNo(String esbSeqNo);
	/**
	 * 发起方系统编号	服务原始发起方的系统编号
	 * @author Ych
	 * @param orgSysId
	 */
	void setOrgSysId(String orgSysId);
	
	/**
	 * 发送方机构ID	服务请求者的机构编号
	 * @author Ych
	 * @param branchId
	 */
	void setBranchId(String branchId);
	
	/**
	 * 服务请求者身份	服务请求者的身份
	 * @author Ych
	 * @param userId
	 */
	void setUserId(String userId);
	/**
	 * 设置请求参数
	 * @author Ych
	 * @param key
	 * @param value
	 */
	void setBodyHead(String key, String value);
	
	/**
	 * 获取系统头数据
	 * @author Ych
	 * @param key
	 * @return
	 */
	void setSysHead(String key, String value);
	
	/**
	 * 获取应用头数据
	 * @author Ych
	 * @param key
	 * @return
	 */
	void setAppHead(String key, String value);
	
	/**
	 * 获取本地头数据
	 * @author Ych
	 * @param key
	 * @return
	 */
	void setLocalHead(String key, String value);
	
	/**
	 * 获取body节点
	 * @author Ych
	 * @param body
	 */
	MsgNode getBody();
	
	/**
	 * 获取根节点
	 * @author Ych
	 * @return
	 */
	MsgNode getRoot();
	
	/**
	 * 获取参数数据
	 * @author Ych
	 * @return
	 *//*
	public KeyMap getParameterMap();

	*//**
	 * 获取系统头数据
	 * @author Ych
	 * @return
	 *//*
	public KeyMap getSysHeadMap();

	*//**
	 * 获取应用头数据
	 * @author Ych
	 * @return
	 *//*
	public KeyMap getAppHeadMap() ;

	*//**
	 * 获取本地头数据
	 * @author Ych
	 * @return
	 *//*
	public KeyMap getLocalHeadMap() ;*/
}
