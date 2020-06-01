package org.ltsh.core.business.files;

import java.io.File;
import java.io.OutputStream;


/**
 * 导出接口
 * @author Ych
 * 2017年5月17日
 */
public interface Export {
	
	/**
	 * 写数据
	 * @author Ych
	 * 2017年5月17日
	 */
	public File export();
	
	/**
	 * 写数据
	 * @author YeChao
	 * 2017年5月17日
	 */
	public void exportToStream(OutputStream output);
	
	/**
	 * 获取文件名称
	 * @author Ych
	 * 2017年5月17日
	 * @return
	 */
	public String getFileName();
	
}
