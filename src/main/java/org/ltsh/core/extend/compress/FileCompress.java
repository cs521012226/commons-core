package org.ltsh.core.extend.compress;

import java.io.File;
import java.io.IOException;

/**
 * 文件压缩、解压
 * @author Ych
 * 2019年4月29日
 */
public interface FileCompress {
	/**
	 * 压缩
	 * @author Ych
	 * @return	返回压缩后的文件
	 */
	public File compress() throws IOException;
	
	/**
	 * 解压
	 * @author Ych
	 * @return	返回解压后的目录
	 */
	public File decompress() throws IOException;
}
