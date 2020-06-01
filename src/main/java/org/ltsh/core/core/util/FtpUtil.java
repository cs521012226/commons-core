package org.ltsh.core.core.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;
import org.apache.commons.net.ftp.FTPReply;
import org.ltsh.core.extend.ftp.FtpClientCallback;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * FTP工具类
 * @author Ych
 * 2018年7月9日
 */
public class FtpUtil {
	private static Logger logger = LoggerFactory.getLogger(FileUtil.class);
	
	/**
	 * 下载文件
	 * @author Ych
	 * @param host		FTP地址
	 * @param username	用户名
	 * @param password	密码
	 * @param remotePath	远程FTP目录
	 * @param remoteFileName		远程文件名称
	 * @param localPath		本地目录
	 * @param localFileName	本地文件名称
	 * @return
	 * @throws IOException
	 */
	public static File downloadFile(String host, String username,
			String password, final String remotePath, final String remoteFileName, final String localPath, final String localFileName) throws IOException{
		
		return execute(host, username, password, new FtpClientCallback<File>() {
			@Override
			public File execute(FTPClient ftpClient) throws IOException{
				FileOutputStream os = null;
				ftpClient.changeWorkingDirectory(remotePath);// 转移到FTP服务器目录
				FTPFile[] fs = ftpClient.listFiles();		//获取FTP上remotePath目录下的文件列表
				File localFile = null;
				boolean isFind = false;	//是否找到相关文件
				for (FTPFile ff : fs) {
					if (ff.getName().equals(remoteFileName)) {
						isFind = true;
					}
				}
				try {
					if(isFind){
						File dir = new File(localPath);
						if(!dir.exists()){							
							dir.mkdirs();
						}
						localFile = new File(localPath, localFileName);
						os = new FileOutputStream(localFile);
						ftpClient.retrieveFile(remoteFileName, os);
					}
				} finally {
					FileUtil.close(os);
				}
				return localFile;
			}
		});
	}
	
	/**
	 * FTP上是否有某文件
	 * @author Ych
	 * @param host		FTP地址
	 * @param username	用户名
	 * @param password	密码
	 * @param remotePath	远程FTP目录
	 * @param remoteFileNamePattern	文件名称,支持正则表达式
	 * @return
	 * @throws IOException
	 */
	public static boolean hasFile(String host, String username,
			String password, final String remotePath, final String remoteFileNameRegex) throws IOException{
		
		return execute(host, username, password, new FtpClientCallback<Boolean>() {

			@Override
			public Boolean execute(FTPClient ftpClient) throws IOException{
				boolean isFind = false;
				ftpClient.changeWorkingDirectory(remotePath);// 转移到FTP服务器目录
				FTPFile[] fs = ftpClient.listFiles();		//获取FTP上remotePath目录下的文件列表
				for (FTPFile ff : fs) {
					if (ff.getName().matches(remoteFileNameRegex)) {
						isFind = true;
					}
				}
				return isFind;
			}
		});
	}
	
	/**
	 * 删除文件
	 * @author Ych
	 * @param host		FTP地址
	 * @param username	用户名
	 * @param password	密码
	 * @param remotePath	远程FTP目录
	 * @param remoteFileNamePattern	文件名称,支持正则表达式
	 * @return
	 * @throws IOException
	 */
	public static boolean deleteFile(String host, String username,
			String password, final String remotePath, final String remoteFileNameRegex) throws IOException{
		
		return execute(host, username, password, new FtpClientCallback<Boolean>() {

			@Override
			public Boolean execute(FTPClient ftpClient) throws IOException{
				boolean success = false;
				ftpClient.changeWorkingDirectory(remotePath);// 转移到FTP服务器目录
				FTPFile[] fs = ftpClient.listFiles();		//获取FTP上remotePath目录下的文件列表
				for (FTPFile ff : fs) {
					if (ff.getName().matches(remoteFileNameRegex)) {
						ftpClient.deleteFile(remotePath + File.separator + ff.getName());
						success = true;
					}
				}
				return success;
			}
		});
	}
	
	/**
	 * 上传文件到FTP服务器
	 * @author Ych
	 * @param host		FTP地址
	 * @param username	用户名
	 * @param password	密码
	 * @param remotePath	远程FTP目录
	 * @param remoteFileName		远程文件名称
	 * @param localPath		本地目录
	 * @param localFileName	本地文件名称
	 * @throws IOException
	 */
	public static boolean uploadFile(String host, String username,
			String password, String remotePath, String remoteFileName, String localPath, String localFileName) throws IOException{
		boolean success = false;
		File localFile = new File(localPath + File.separator + localFileName);
		FileInputStream is = new FileInputStream(localFile);
		try {
			success = uploadFileByStream(host, username, password, remotePath, remoteFileName, is);
		} finally {
			FileUtil.close(is);
		}
		return success;
	}
	
	/**
	 * 上传文件到FTP服务器
	 * 注意：该方法不负责关闭inputStream流，需要客户根据需要手动关闭
	 * @author Ych
	 * @param host		FTP地址
	 * @param username	用户名
	 * @param password	密码
	 * @param remotePath	远程FTP目录
	 * @param remoteFileName		远程文件名称
	 * @param inputStream	输出流
	 * @throws IOException
	 */
	public static boolean uploadFileByStream(String host, String username,
			String password, final String remotePath, final String remoteFileName, final InputStream inputStream) throws IOException {
		boolean success = execute(host, username, password, new FtpClientCallback<Boolean>() {

				@Override
				public Boolean execute(FTPClient ftpClient) throws IOException{
					
					ftpClient.changeWorkingDirectory(remotePath);
//				ftpClient.enterLocalActiveMode();
					ftpClient.storeFile(remoteFileName, inputStream);
					
					return true;
				}
				
			});
		return success;
	}
	
	/**
	 * 执行FTP业务逻辑
	 * @author Ych
	 * @param host
	 * @param username
	 * @param password
	 * @param executor
	 * @return
	 * @throws IOException
	 */
	public static <T> T execute(String host, String username, String password, FtpClientCallback<T> executor) throws IOException {
		FTPClient ftpClient = new FTPClient();
		String serverName = username + "@" + host;
		try {
			int timeout = 10 * 1000;
			logger.info("连接超时时间设置为" + timeout + "毫秒");
			ftpClient.setConnectTimeout(timeout);
			ftpClient.enterLocalPassiveMode();	//设置被动模式
			logger.info("准备连接" + serverName);
			ftpClient.connect(host);// 连接FTP服务器
			logger.info("已连接" + serverName);
			ftpClient.login(username, password);// 登录
			logger.info("登录" + serverName + "成功");
			// 如果采用默认端口，可以使用ftp.connect(url)的方式直接连接FTP服务器
			ftpClient.setFileTransferMode(FTPClient.STREAM_TRANSFER_MODE);
            ftpClient.setFileType(FTPClient.BINARY_FILE_TYPE);
			int reply = ftpClient.getReplyCode();
			logger.info("reply = " + reply);
			if (!FTPReply.isPositiveCompletion(reply)) {
				logger.info(serverName + "拒绝连接");
				ftpClient.disconnect();
				throw new RuntimeException(serverName + "拒绝连接");
			}
			logger.info("start executor.execute");
			T result = executor.execute(ftpClient);
			logger.info("end executor.execute");
			ftpClient.logout();
			logger.info("已注销" + serverName);
			return result;
		} finally {
			logger.info("准备断开连接" + serverName);
			ftpClient.disconnect();
			logger.info("已断开连接" + serverName);
		}
	}
}
