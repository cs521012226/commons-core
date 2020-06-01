package org.ltsh.core.core.tree;

import java.util.ArrayList;
import java.util.List;

/**
 * 树节点容器
 * @author Ych
 * 2017年12月15日
 * @param <E>
 */
public class TreeNode<E>{
	private E data;
	private List<TreeNode<E>> children;
	
	public TreeNode(E data){
		this.data = data;
	}
	
	public E getData(){
		return data;
	}

	public List<TreeNode<E>> getChildren(){
		return children;
	}

	public void setData(E data) {
		this.data = data;
	}

	public void setChildren(List<TreeNode<E>> children) {
		this.children = children;
	}
	
	/**
	 * 添加子节点并返回子节点引用
	 * @author Ych
	 * @param data
	 * @return
	 */
	public TreeNode<E> addChild(E data){
		if(children == null){
			children = new ArrayList<TreeNode<E>>();
		}
		TreeNode<E> node = new TreeNode<E>(data);
		children.add(node);
		return node;
	}

	@Override
	public String toString() {
		return data.toString();
	}
}
