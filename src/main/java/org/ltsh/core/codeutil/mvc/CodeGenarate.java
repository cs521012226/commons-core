package org.ltsh.core.codeutil.mvc;

import java.io.File;
import java.io.FileFilter;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.ltsh.core.codeutil.mvc.DaoImplTemplate;
import org.ltsh.core.codeutil.mvc.DaoInterfaceTemplate;
import org.ltsh.core.codeutil.mvc.JFinalModelTemplate;
import org.ltsh.core.codeutil.mvc.ServiceImplTemplate;
import org.ltsh.core.codeutil.mvc.ServiceInterfaceTemplate;
import org.ltsh.core.codeutil.mvc.TemplateBuilder;
import org.ltsh.core.core.db.jdbc.table.MysqlTableInfo;

/**
 * 代码生成器
 * @author Ych
 * 2018年5月25日
 */
public class CodeGenarate {

	public static void main(String[] args) throws SQLException {
		
		//源码生成位置的根路径
		final String SRC_ROOT_PATH = "D:\\DEV\\Workspace\\YingTong\\workspace_sts\\erp-parent\\erp-basic\\src\\main\\java\\";

		//Mysql表信息实现
		MysqlTableInfo tableInfo = new MysqlTableInfo("11.8.130.229", 3306, "cm", "cm", "abc123");
		tableInfo.setTableNamePattern("t_%");	//设置要生成的表实体、支持正则表达式
		
//		OracleTableInfo tableInfo = new OracleTableInfo("11.8.129.118", 1521, "PSPROD", "SYSADM", "SYSADM");
//		tableInfo.setSchemaPattern("SYSADM");	//设置要生成的库名
//		tableInfo.setTableNamePattern("ps_gnb_kq_salary_msg");	//设置要生成的表实体、支持正则表达式
		
		List<TemplateBuilder> builder = new ArrayList<TemplateBuilder>();
		
		//生成DAO源码
		DaoInterfaceTemplate daoInterTpl = new DaoInterfaceTemplate(SRC_ROOT_PATH, "com/ny/adms/dao");
		DaoImplTemplate daoImplTpl = new DaoImplTemplate(SRC_ROOT_PATH, "com/ny/adms/dao/impl");
		daoImplTpl.setDaoInterface(daoInterTpl);
		
		//生成Service源码
		ServiceInterfaceTemplate serviceInterTpl = new ServiceInterfaceTemplate(SRC_ROOT_PATH, "com/ny/adms/service");
		ServiceImplTemplate serviceImplTpl = new ServiceImplTemplate(SRC_ROOT_PATH, "com/ny/adms/service/impl");
		
		serviceImplTpl.setServiceInterface(serviceInterTpl);
		serviceImplTpl.setDaoInterface(daoInterTpl);
		
		//添加到生成列表
//		builder.add(new SimpleModelTemplate(SRC_ROOT_PATH, "com/enton/cardtxn/business/entity"));
		builder.add(new PlainEntityTemplate(SRC_ROOT_PATH, "com/ny/basic/entity"));
//		builder.add(daoInterTpl);
//		builder.add(daoImplTpl);
//		builder.add(serviceInterTpl);
//		builder.add(serviceImplTpl);
		
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
