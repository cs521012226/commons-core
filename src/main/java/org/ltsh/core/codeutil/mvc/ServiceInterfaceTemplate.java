package org.ltsh.core.codeutil.mvc;

import java.util.HashMap;
import java.util.Map;

import org.ltsh.core.core.db.jdbc.bean.DBTable;
import org.ltsh.core.core.util.StringUtil;

/**
 *	Service 接口生成器
 * @author Ych
 * 2018年4月28日
 */
public class ServiceInterfaceTemplate extends TemplateBuilder{
	
	public ServiceInterfaceTemplate(String rootPath, String packagePath) {
		super(rootPath, packagePath);
	}
	
	@Override
	public Object getModel(DBTable table) {
		Map<String, Object> cur = new HashMap<String, Object>();
		cur.put("package", pathToPackage(getSubPath()));
		cur.put("className", getClassName(table));
		
		return cur;
	}

	@Override
	public String getTargetFileName(DBTable table) {
		return getClassName(table) + ".java";
	}
	
	protected String getClassName(DBTable table){
		String entityName = table.getTableName().toLowerCase();		//先统一转为小写
		entityName = StringUtil.underlineToCamelCase(entityName);	//先转驼峰写法
		entityName = StringUtil.firstCharToUpperCase(entityName);		//首字母转大写
		return entityName + "Service";
	}
}
