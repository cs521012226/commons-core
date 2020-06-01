package org.ltsh.core.codeutil.mvc;

import java.util.HashMap;
import java.util.Map;

import org.ltsh.core.core.db.jdbc.bean.DBTable;
import org.ltsh.core.core.util.StringUtil;

/**
 *	Service 实现类生成器
 * @author Ych
 * 2018年4月28日
 */
public class ServiceImplTemplate extends TemplateBuilder{
	private TemplateBuilder serviceInterface;
	private TemplateBuilder daoInterface;
	
	public ServiceImplTemplate(String rootPath, String packagePath) {
		super(rootPath, packagePath);
	}
	
	@Override
	public Object getModel(DBTable table) {
		String entityName = getEntityName(table);
		Map<String, Object> cur = new HashMap<String, Object>();
		cur.put("package", pathToPackage(getSubPath()));
		cur.put("entityName", entityName);
		cur.put("entityNameLower", StringUtil.firstCharToLowerCase(entityName));
		cur.put("superPackage", pathToPackage(serviceInterface.getSubPath()));
		cur.put("daoPackage", pathToPackage(daoInterface.getSubPath()));
		return cur;
	}

	@Override
	public String getTargetFileName(DBTable table) {
		return getEntityName(table) + "ServiceImpl.java";
	}
	
	protected String getEntityName(DBTable table){
		String entityName = table.getTableName().toLowerCase();		//先统一转为小写
		entityName = StringUtil.underlineToCamelCase(entityName);	//先转驼峰写法
		entityName = StringUtil.firstCharToUpperCase(entityName);		//首字母转大写
		return entityName;
	}

	public TemplateBuilder getServiceInterface() {
		return serviceInterface;
	}

	public void setServiceInterface(TemplateBuilder serviceInterface) {
		this.serviceInterface = serviceInterface;
	}

	public TemplateBuilder getDaoInterface() {
		return daoInterface;
	}

	public void setDaoInterface(TemplateBuilder daoInterface) {
		this.daoInterface = daoInterface;
	}
}
