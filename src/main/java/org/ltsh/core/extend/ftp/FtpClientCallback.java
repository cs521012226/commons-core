package org.ltsh.core.extend.ftp;

import java.io.IOException;

import org.apache.commons.net.ftp.FTPClient;

/**
 * Ftp操作执行回调接口
 * @author Ych
 * 2018年7月12日
 * @param <T>
 */
public interface FtpClientCallback<T> {

	/**
	 * 执行回调
	 * @author Ych
	 * @param ftpClient
	 * @return
	 */
	T execute(FTPClient ftpClient) throws IOException;
}
