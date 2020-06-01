package org.ltsh.core.codeutil.loaddata;

import java.util.HashMap;
import java.util.Map;

import org.ltsh.core.codeutil.mvc.TemplateBuilder;
import org.ltsh.core.core.db.jdbc.bean.DBTable;

/**
 * oracle数据导入sqlldr命令生成器
 * @author Ych
 * 2018年4月28日
 */
public class SqlldrFilelTemplate extends TemplateBuilder{
	
	private String tnsname;
	
	public SqlldrFilelTemplate(String rootPath, String tnsname) {
		super(rootPath, null);
		this.tnsname = tnsname;
	}
	
	@Override
	public Object getModel(DBTable table) {
		Map<String, Object> cur = new HashMap<String, Object>();
		cur.put("tnsname", tnsname);
		cur.put("tableName", table.getTableName());
		return cur;
	}

	@Override
	public String getTargetFileName(DBTable table) {
		return table.getTableName() + ".sqlldr";
	}

	
}
