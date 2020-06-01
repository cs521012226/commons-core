package org.ltsh.core.esb.vo;


/**
 * 报文信息对象
 * @author Ych
 * 2018年4月16日
 */
public class MsgNodeInfo {

	private String key;
	private String value;
	private MsgNode oneValue;
	
	public MsgNodeInfo(String key, String value){
		this.key = key;
		this.value = value;
	}
	
	public MsgNodeInfo(String key, MsgNode value){
		this.key = key;
		this.oneValue = value;
	}

	public String getKey() {
		return key;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public void setOneValue(MsgNode oneValue) {
		this.oneValue = oneValue;
	}

	public MsgNode getOneValue() {
		return oneValue;
	}
}
