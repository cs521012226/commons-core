package org.ltsh.core.codeutil.loaddata;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.ltsh.core.codeutil.mvc.TemplateBuilder;
import org.ltsh.core.core.db.jdbc.bean.DBTable;
import org.ltsh.core.core.db.jdbc.bean.DBTableColumn;
import org.ltsh.core.core.util.DateUtil;

/**
 * oracle数据导入ctrl文件生成器
 * @author Ych
 * 2018年4月28日
 */
public class CtrlFilelTemplate extends TemplateBuilder{
	
	private String appendColumnName = "loaddata_date";
	private String appendColumnValue = DateUtil.convertDateToString(new Date());
	
	public CtrlFilelTemplate(String rootPath) {
		super(rootPath, null);
	}
	
	@Override
	public Object getModel(DBTable table) {
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		
		List<DBTableColumn> cols = table.getColumns();
		
		for(int i=0; i<cols.size(); i++){
			DBTableColumn f = cols.get(i);
			String fieldName = f.getColumnName().toUpperCase();	
			String dataType = f.getDataType();
			String fieldType = null;
			
			if(fieldName.equalsIgnoreCase(appendColumnName)){
				fieldType = "constant \"" + appendColumnValue + "\"";
			}else if("DATE TIMESTAMP(6)".contains(dataType)){
				fieldType = "Date 'yyyy/MM/dd'";
			}else if("NUMBER".contains(dataType)){
				fieldType = "DECIMAL EXTERNAL";
			}else{
				fieldType = "CHAR";
			}
			if(i != cols.size() - 1){
				fieldType += ",";
			}
			Map<String, Object> curField = new HashMap<String, Object>();
			curField.put("fieldName", fieldName);
			curField.put("fieldType", fieldType);
			
			list.add(curField);
		}
		Map<String, Object> cur = new HashMap<String, Object>();
		cur.put("columnField", list);
		cur.put("loadType", "truncate");
		cur.put("tableSchema", table.getTableSchema());
		cur.put("tableName", table.getTableName());
		cur.put("separate", "[#|#]");
		cur.put("dataFilePath", "");
		cur.put("dataFileName", "T_OIS_" + table.getTableName() + ".dat");
		
		return cur;
	}

	@Override
	public String getTargetFileName(DBTable table) {
		return table.getTableName() + ".ctrl";
	}

	public String getAppendColumnName() {
		return appendColumnName;
	}

	public void setAppendColumnName(String appendColumnName) {
		this.appendColumnName = appendColumnName;
	}

	public String getAppendColumnValue() {
		return appendColumnValue;
	}

	public void setAppendColumnValue(String appendColumnValue) {
		this.appendColumnValue = appendColumnValue;
	}
	
}
