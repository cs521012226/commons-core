package org.ltsh.core.esb.vo;

import java.util.ArrayList;
import java.util.List;

/**
 * 报文节点
 * @author Ych
 * 2018年4月16日
 */
public class MsgNode {
	
	private List<MsgNodeInfo> fields = new ArrayList<MsgNodeInfo>();
	
	/**
	 * 设置字段值
	 * @author Ych
	 * @param key
	 * @param value
	 * @return
	 */
	public MsgNodeInfo set(String key, String value){
		MsgNodeInfo info = new MsgNodeInfo(key, value);
		fields.add(info);
		return info;
	}
	
	@Override
	public String toString() {
		return fields.toString();
	}

	/**
	 * 获取字段值
	 * @author Ych
	 * @param key
	 * @return
	 */
	public String get(String key){
		for(MsgNodeInfo info : fields){
			if(info.getKey().equals(key)){
				return info.getValue();
			}
		}
		return null;
	}
	
	/**
	 * 或者子节点对象
	 * @author Ych
	 * @param key
	 * @return
	 */
	public MsgNode getChild(String key){
		for(MsgNodeInfo info : fields){
			if(info.getKey().equals(key)){
				return info.getOneValue();
			}
		}
		return null;
	}
	
	/**
	 * 添加子节点对象
	 * @author Ych
	 * @param key
	 * @return
	 */
	public MsgNode addChild(String key){
		MsgNode node = new MsgNode();
		MsgNodeInfo kv = new MsgNodeInfo(key, node);
		fields.add(kv);
		return node;
	}
	
	/**
	 * 是否字段列表为空
	 * @author Ych
	 * @return
	 */
	public boolean isEmpty(){
		return fields.isEmpty();
	}

	/**
	 * 获取该节点所有字段
	 * @author Ych
	 * @return
	 */
	public List<MsgNodeInfo> getFields() {
		return fields;
	}
	
}
