package org.ltsh.core.core.tree;

/**
 * 树形结构关系接口
 * @author Ych
 * 2017年12月15日
 */
public interface TreeVo {
	/**
	 * 唯一实体标识ID
	 * @author Ych
	 * @return
	 */
	String getId();
	
	/**
	 * 唯一实体标识上级ID
	 * @author Ych
	 * @return
	 */
	String getParentId();
}
