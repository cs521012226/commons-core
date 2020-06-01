package org.ltsh.core.codeutil.mvc;

import java.io.File;
import java.io.FileFilter;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.List;

import org.ltsh.core.core.db.jdbc.bean.DBTable;
import org.ltsh.core.core.db.jdbc.table.DBTableInfo;
import org.ltsh.core.core.util.FileUtil;

import freemarker.template.Configuration;
import freemarker.template.Template;

/**
 * 模版生成器
 * @author Ych
 * 2018年4月28日
 */
public abstract class TemplateBuilder {

	private String rootPath;
	private String subPath;
	private DBTableInfo tableInfo;
	private String templateFileName;
	
	private FileFilter fileFilter;
	private Configuration conf = new Configuration();
	
	public TemplateBuilder(String rootPath, String subPath){
		this(rootPath, subPath, null);
	}
	
	public TemplateBuilder(String rootPath, String subPath, File templateFile){
		this.rootPath = FileUtil.buildPath(rootPath);
		this.subPath = subPath == null ? null :  packageToPath(subPath);
		try {
			
			if(templateFile != null){
				if(templateFile.isFile()){
					File tmplDir = new File(templateFile.getPath());
					conf.setDirectoryForTemplateLoading(tmplDir);
				}else{
					throw new IllegalArgumentException("template file is not found");
				}
				templateFileName = templateFile.getName();
			}else{
				conf.setClassForTemplateLoading(getClass(), "/" + packageToPath(getClass().getPackage().getName()));
				templateFileName = getClass().getSimpleName() + ".tpl";
			}
			
			
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
		
	}
	/**
	 * 路径转为包名
	 * @author YeChao
	 * @param targetSubDir
	 * @return
	 */
	protected String pathToPackage(String path){
		String packageName = path.replaceAll("\\.|/", ".");
		packageName = packageName.endsWith(".") ? packageName.substring(0, packageName.length() - 1) : packageName;
		return packageName;
	}
	
	/**
	 * 包名转为路径名称
	 * @author Ych
	 * @param subPath
	 * @return
	 */
	protected String packageToPath(String path){
		return path.replaceAll("\\.|/", "/");
	}
	
	/**
	 * 检查目录是否存在
	 * @author YeChao
	 * @param targetRootDir
	 * @param targetSubDir
	 */
	protected File checkDirExist(String targetFullPath){
		File targetDir = new File(targetFullPath);
		if(!targetDir.exists()){
			targetDir.mkdirs();
		}
		
		if(!targetDir.isDirectory()){
			throw new IllegalArgumentException("targetDir is not directory");
		}
		return targetDir;
	}
	
	/**
	 * 生成模版
	 * @author Ych
	 */
	public void create(){
		List<DBTable> tables = tableInfo.getTableInfo(false);
		String fullPath = subPath == null ? rootPath : FileUtil.buildPath(rootPath, subPath);
		checkDirExist(fullPath);
		
		for(DBTable table : tables){
			Writer fos = null;
			try {
				File targetFile = new File(fullPath + getTargetFileName(table));
				if(targetFile.isFile()){
					//如果fileFilter为空或者返回false， 不覆盖已有文件
					if(fileFilter == null || !fileFilter.accept(targetFile)){	
						continue;
					}
				}
				Template tpl = conf.getTemplate(templateFileName);
				fos = new OutputStreamWriter(new FileOutputStream(targetFile));
				tpl.process(getModel(table), fos);
				System.out.println("已生成文件: " + targetFile.getName());
				
			} catch (Exception e) {
				throw new RuntimeException(e);
			} finally {
				FileUtil.close(fos);
			}
		}
	}
	
	/**
	 * 获取将要生成模版的变量
	 * @author Ych
	 */
	public abstract Object getModel(DBTable table);
	
	/**
	 * 获取将要生成的文件名
	 * @author Ych
	 * @param table
	 */
	public abstract String getTargetFileName(DBTable table);
	
	public String getRootPath() {
		return rootPath;
	}
	public void setRootPath(String rootPath) {
		this.rootPath = rootPath;
	}
	
	public String getSubPath() {
		return subPath;
	}

	public void setSubPath(String subPath) {
		this.subPath = subPath;
	}

	public DBTableInfo getTableInfo() {
		return tableInfo;
	}
	public void setTableInfo(DBTableInfo tableInfo) {
		this.tableInfo = tableInfo;
	}
	public FileFilter getFileFilter() {
		return fileFilter;
	}
	public void setFileFilter(FileFilter fileFilter) {
		this.fileFilter = fileFilter;
	}
	
}
