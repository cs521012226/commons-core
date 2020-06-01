package org.ltsh.core.codeutil.loaddata;

import java.io.File;
import java.io.FileFilter;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.ltsh.core.codeutil.mvc.TemplateBuilder;
import org.ltsh.core.core.db.jdbc.table.OracleTableInfo;

/**
 * 代码生成器
 * @author Ych
 * 2018年5月25日
 */
public class CodeGenarate {

	public static void main(String[] args) throws SQLException {
		
		//源码生成位置的根路径
		final String SRC_ROOT_PATH = "F:/Accumulation/YingTong/HePing/新风险预警/远程服务器";

		//Mysql表信息实现
//		MysqlTableInfo tableInfo = new MysqlTableInfo("11.8.130.229", 3306, "bs", "cm", "abc123");
//		tableInfo.setSchemaPattern("cm");	//设置要生成的库名
//		tableInfo.setTableNamePattern("t_operation_bind");	//设置要生成的表实体、支持正则表达式
		
		OracleTableInfo tableInfo = new OracleTableInfo(" 11.8.131.219", 1521, "ADMSDB", "cm", "abc123");
		tableInfo.setSchemaPattern("HX");	//设置要生成的库名
		tableInfo.setTableNamePattern("CADB_ACCT_BIND,COMC_FXQ_RISK,COMR_CIFADDINFO,SAVR_AGENTBOOK,SAVR_OPENBOOK");	//设置要生成的表实体、支持正则表达式
		
		List<TemplateBuilder> builder = new ArrayList<TemplateBuilder>();
		
		
		//添加到生成列表
		builder.add(new CtrlFilelTemplate(SRC_ROOT_PATH));
		builder.add(new SqlldrFilelTemplate(SRC_ROOT_PATH, "cm/abc123@RIK_SIT_219"));
		
		//执行生成列表
		for(TemplateBuilder b : builder){
			//设置是否覆盖文件
			b.setFileFilter(new FileFilter() {
				@Override
				public boolean accept(File pathname) {
					return true;
				}
			});
			//设置表信息实体（必须）
			b.setTableInfo(tableInfo);
			//生成源码
			b.create();
		}
	}

}
