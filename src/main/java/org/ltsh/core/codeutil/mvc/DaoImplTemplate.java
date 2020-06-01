package org.ltsh.core.codeutil.mvc;

import java.util.HashMap;
import java.util.Map;

import org.ltsh.core.core.db.jdbc.bean.DBTable;
import org.ltsh.core.core.util.StringUtil;

/**
 *	Dao 实现类生成器
 * @author Ych
 * 2018年4月28日
 */
public class DaoImplTemplate extends TemplateBuilder{
	private TemplateBuilder daoInterface;
	
	public DaoImplTemplate(String rootPath, String packagePath) {
		super(rootPath, packagePath);
	}
	
	@Override
	public Object getModel(DBTable table) {
		Map<String, Object> cur = new HashMap<String, Object>();
		cur.put("package", pathToPackage(getSubPath()));
		cur.put("entityName", getEntityName(table));
		cur.put("superPackage", pathToPackage(daoInterface.getSubPath()));
		
		return cur;
	}

	@Override
	public String getTargetFileName(DBTable table) {
		return getEntityName(table) + "DaoImpl.java";
	}
	
	protected String getEntityName(DBTable table){
		String entityName = table.getTableName().toLowerCase();		//先统一转为小写
		entityName = StringUtil.underlineToCamelCase(entityName);	//先转驼峰写法
		entityName = StringUtil.firstCharToUpperCase(entityName);		//首字母转大写
		return entityName;
	}

	public TemplateBuilder getDaoInterface() {
		return daoInterface;
	}

	public void setDaoInterface(TemplateBuilder daoInterface) {
		this.daoInterface = daoInterface;
	}
}
