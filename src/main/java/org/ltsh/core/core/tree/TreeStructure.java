package org.ltsh.core.core.tree;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 树型数据结构辅助类
 * @author Ych
 * 2017年12月15日
 */
public class TreeStructure <E extends TreeVo>{

	private List<TreeNode<E>> nodeList;
	private Map<String, TreeNode<E>> mapContainer;
	
	public TreeStructure(List<E> dataSource){
		if(dataSource == null){
			throw new IllegalArgumentException("参数list不能为空");
		}
		nodeList = new ArrayList<TreeNode<E>>(dataSource.size());
		for(E tr : dataSource){
			nodeList.add(new TreeNode<E>(tr));
		}
	}
	
	/**
	 * 构建树形结构容器
	 * @author Ych
	 * @return
	 */
	private Map<String, TreeNode<E>> buildTreeStructure(){
		
		Map<String, TreeNode<E>> container = new HashMap<String, TreeNode<E>>();
		
		//把表每一条记录都存进container里待处理
		for(TreeNode<E> m : nodeList){
			String nodeId = m.getData().getId();
			container.put(nodeId, m);
		}
		//父、子关系关联操作
		for(TreeNode<E> m : nodeList){
			E node = m.getData();
			
			String parentNodeId = node.getParentId();
			//从容器里取出父节点
			TreeNode<E> parentNode = container.get(parentNodeId);
			
			//引用关联
			if(parentNode != null){
				
				List<TreeNode<E>> children = parentNode.getChildren();
				if(children == null){
					children = new ArrayList<TreeNode<E>>();
					parentNode.setChildren(children);
				}
				children.add(m);
			}
		}
		return container;
	}
	
	/**
	 * 递归提取节点里的数据放到result里
	 * @author Ych
	 * @param node
	 * @param result
	 */
	private void recursionExtract(TreeNode<E> node, List<E> result, String rootNodeId, boolean includeCurrentNode){
		E data = node.getData();
		
		//如果指定不包括rootNodeId节点，就不把它添加入result列表
		if(!(!includeCurrentNode && data.getId().equals(rootNodeId))){
			result.add(data);
		}
		
		List<TreeNode<E>> children = node.getChildren();
		if(children != null && !children.isEmpty()){
			for(TreeNode<E> subNode : children){
				recursionExtract(subNode, result, rootNodeId, includeCurrentNode);
			}
		}
		
	}
	/**
	 * 获取节点容器
	 * @author Ych
	 * @return
	 */
	private Map<String, TreeNode<E>> getMapContainner(){
		if(mapContainer == null){
			mapContainer = buildTreeStructure();
		}
		return mapContainer;
	}
	
	/**
	 * 通过某节点ID获取节点树形数据结构
	 * @author Ych
	 * @param rootNodeId
	 * @return
	 */
	public TreeNode<E> getTreeNode(String rootNodeId){
		return getMapContainner().get(rootNodeId);
	}
	
	/**
	 * 通过某父节点ID获取节点树形数据结构
	 * @author Ych
	 * @param parentNodeId
	 * @return
	 */
	public TreeNode<E> getTreeNodeByParentId(String parentNodeId){
		if (parentNodeId == null) {
			return null;
		}
		for(TreeNode<E> node : nodeList){
			E date = node.getData();
			if(parentNodeId.equals(date.getParentId())){
				return getMapContainner().get(date.getId());
			}
		}
		return null;
	}
	
	/**
	 * 清空结构数据
	 * @author Ych
	 */
	public void clean(){
		mapContainer = null;
	}
	
	public E getData(String nodeId){
		return getTreeNode(nodeId).getData();
	}
	
	/**
	 * 通过某节点ID获取该节点下所有子节点列表（包括当前节点）
	 * @author Ych
	 * @param rootNodeId
	 * @return
	 */
	public List<E> getDataListForLower(String rootNodeId){
		return getDataListForLower(rootNodeId, true);
	}
	
	/**
	 * 通过某节点ID获取该节点下所有子节点列表
	 * @author Ych
	 * @param rootNodeId	节点ID
	 * @param includeCurrentNode	是否包括rootNodeId的节点
	 * @return
	 */
	public List<E> getDataListForLower(String rootNodeId, boolean includeCurrentNode){
		TreeNode<E> node = getTreeNode(rootNodeId);
		List<E> result = new ArrayList<E>();
		if(node == null){
			return result;
		}
		recursionExtract(node, result, rootNodeId, includeCurrentNode);
		return result;
	}
	
	/**
	 * 通过某节点的parentId，获取该节点下所有子节点列表（包括当前节点）
	 * @author Ych
	 * @param parentNodeId
	 * @return
	 */
	public List<E> getDataListForLowerByParentId(String parentNodeId){
		List<E> result = new ArrayList<E>();
		for(TreeNode<E> node : nodeList){
			E data = node.getData();
			String id = data.getId();
			String parentId = data.getParentId();
			
			if(parentId.equals(parentNodeId)){
				recursionExtract(getTreeNode(id), result, id , true);
			}
		}
		return result;
	}
	
	/**
	 * 通过某节点ID获取该节点上所有父节点列表（包括当前节点）
	 * @author Ych
	 * @param rootNodeId
	 * @return
	 */
	public List<E> getDataListForUpper(String rootNodeId){
		return getDataListForUpper(rootNodeId, true);
	}
	/**
	 * 通过某节点ID获取该节点上所有父节点列表
	 * @author Ych
	 * @param rootNodeId	根节点ID
	 * @param includeCurrentNode	是否包括rootNodeId该节点
	 * @return
	 */
	public List<E> getDataListForUpper(String rootNodeId, boolean includeCurrentNode){
		TreeNode<E> node = getTreeNode(rootNodeId);
		List<E> result = new ArrayList<E>();
		if(node == null){
			return result;
		}
		Map<String, TreeNode<E>> map = getMapContainner();
		while(true){
			E data = node.getData();
			//如果指定不包括rootNodeId节点，就不把它添加入result列表
			if(!(!includeCurrentNode && data.getId().equals(rootNodeId))){
				result.add(data);
			}
			String parentId = data.getParentId();
			TreeNode<E> parentNode = map.get(parentId);
			
			if(parentNode == null){
				break;
			}
			node = parentNode;
		}
		return result;
	}
	
	@Override
	public String toString() {
		return nodeList.toString();
	}
}
