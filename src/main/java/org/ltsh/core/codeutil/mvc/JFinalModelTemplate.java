package org.ltsh.core.codeutil.mvc;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.ltsh.core.core.db.jdbc.bean.DBTable;
import org.ltsh.core.core.db.jdbc.bean.DBTableColumn;
import org.ltsh.core.core.util.StringUtil;

/**
 * JFinal数据库实体model类模版生成器
 * @author Ych
 * 2018年4月28日
 */
public class JFinalModelTemplate extends TemplateBuilder{
	
	public JFinalModelTemplate(String rootPath, String packagePath) {
		super(rootPath, packagePath);
	}
	
	@Override
	public Object getModel(DBTable table) {
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		for(DBTableColumn f : table.getColumns()){
			String fieldName = f.getColumnName().toLowerCase();		//先转驼峰写法
			String dbFieldName = fieldName.toUpperCase();
			fieldName = StringUtil.underlineToCamelCase(fieldName);
			String capFieldName = StringUtil.firstCharToUpperCase(fieldName);
			
			Map<String, Object> curField = new HashMap<String, Object>();
			curField.put("fieldName", fieldName);
			curField.put("fieldType", getTableInfo().dataTypeMapper(f));
			curField.put("dbFieldName", dbFieldName);
			curField.put("capFieldName", capFieldName);
			
			list.add(curField);
		}
		Map<String, Object> cur = new HashMap<String, Object>();
		cur.put("package", pathToPackage(getSubPath()));
		cur.put("entityName", getEntityName(table));
		cur.put("entityField", list);
		
		return cur;
	}



	@Override
	public String getTargetFileName(DBTable table) {
		return getEntityName(table) + ".java";
	}

	protected String getEntityName(DBTable table){
		String entityName = table.getTableName().toLowerCase();		//先统一转为小写
		entityName = StringUtil.underlineToCamelCase(entityName);	//先转驼峰写法
		entityName = StringUtil.firstCharToUpperCase(entityName);		//首字母转大写
		return entityName;
	}
}
