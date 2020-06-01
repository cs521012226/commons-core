package org.ltsh.core.core.tree;

import com.alibaba.fastjson.JSON;

public class Test implements TreeVo {
	private String id;
	private String name;
	private String parentId;
	
	public Test(String id, String parentId, String name) {
		super();
		this.id = id;
		this.name = name;
		this.parentId = parentId;
	}
	

	@Override
	public String toString() {
		return JSON.toJSONString(this);
	}


	@Override
	public String getId() {
		// TODO Auto-generated method stub
		return id;
	}

	@Override
	public String getParentId() {
		// TODO Auto-generated method stub
		return parentId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	

}
