package org.ltsh.core.core.util;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.Flushable;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipOutputStream;

import org.ltsh.core.business.exp.BusinessException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * 文件管理操作工具类
 * @author Ych
 */
public class FileUtil {
	
	private static final String FILE_SEP = "/";
	
	/**通用日志对象**/
	private static Logger logger = LoggerFactory.getLogger(FileUtil.class);
	/** 
	 * @Author: Ych
	 * @Description: 删除指定文件
	 * @param fileFullName	文件名称
	 * @return boolean: 返回是否删除成功
	 */
	public static boolean delFile(String fileFullName){
		File file = null;
		if(!StringUtil.isBlank(fileFullName)){
			file = new File(fileFullName);
			if(file.exists() && file.isFile()){
				return file.delete();
			}
		}
		return false;
	}
	
	/** 
	 * @Author: Ych
	 * @Description: 删除当前目录及目录内所有目录文件，或只删除该目录下的所有目录及文件
	 * @param dirPath	目录路径
	 * @param isDelThisFloder	是否删除当前文件夹
	 * @return boolean: 	是否删除成功
	 */
	public static boolean delFloders(String dirPath, boolean isDelThisFloder){
		File dir = null;
		
		try{
			if(!StringUtil.isBlank(dirPath)){
				dir = new File(dirPath);
				if(dir.exists() && dir.isDirectory()){
					File[] listFile = dir.listFiles();	//当前目录下的所有文件
					for(File subFileOrDir : listFile){
						if(subFileOrDir.isFile()){
							if(!delFile(subFileOrDir.getCanonicalPath())){
								return false;
							}	//删除文件
						}
						if(subFileOrDir.isDirectory()){
							if(!delFloders(subFileOrDir.getCanonicalPath(), true)){
								return false;
							}
						}
					}
					if(isDelThisFloder){	//是否删除当前文件夹
						dir.delete();
					}
				}
			}
		}catch(IOException e){
			logger.error("删除目录时发生异常", e);
			return false;
		}
		return true;
	}
	
	/**
	 * 创建文件
	 * @author Ych
	 * @param fullPath	目录或文件全路径
	 * @return
	 */
	public static File createFile(String fullPath){
		String path = null;
		String fileName = null;
		
		fullPath = buildPath(fullPath);
		
		int lastSeparator = fullPath.lastIndexOf(FILE_SEP);
		if(lastSeparator != -1){
			fileName = fullPath.substring(lastSeparator + 1);
			path = fullPath.substring(0, lastSeparator + 1);
		}else{
			path = fullPath;
		}
		
		File dir = new File(path);
		if(!dir.exists()){
			dir.mkdirs();
		}
		if(fileName == null){
			return dir;
		}
		
		File file = new File(path + fileName);
		try {
			if (!file.exists()) {
				file.createNewFile();
			}
		} catch (IOException e) {
			logger.error(e.getMessage(), e);
		}
		return file;
	}
	
	/**
	 * 删除文件
	 * @author Ych
	 * @param file
	 */
	public static void deleteFile(File file){
		if(file != null && file.exists()){
			file.delete();
		}
	}
	
	/**
	 * 删除文件
	 * @author Ych
	 * @param filePath
	 */
	public static void deleteFile(String filePath){
		deleteFile(new File(filePath));
	}
	
	/**
	 * 构建目标目录
	 * @author Ych
	 * 2017年5月18日
	 * @param targetDir		
	 * @param isCreateDir	是否创建目录
	 * @return
	 */
	public static String buildPath(String targetDir, boolean isCreateDir){
		return buildPath(targetDir, isCreateDir, false);
	}
	
	/**
	 * 构建目标目录
	 * @author Ych
	 * @param targetDir	目录路径
	 * @param isCreateDir	若没该路径目录，是否创建目录
	 * @param isRemoveRootSymbol	是否移除第一位路径（/）符号
	 * @return
	 */
	public static String buildPath(String targetDir, boolean isCreateDir, boolean isRemoveRootSymbol){
		targetDir = buildPath(targetDir);
		
		File dir = new File(targetDir);
		if(!dir.exists() && isCreateDir){
			dir.mkdirs();
		}
		if(isRemoveRootSymbol && targetDir.startsWith(FILE_SEP)){
			targetDir = targetDir.substring(targetDir.indexOf(FILE_SEP) + 1);
		}
		return targetDir;
	}
	
	/**
	 * 构建目标目录路径
	 * @author Ych
	 * 2017年5月18日
	 * @param rootDir
	 * @param dirs
	 * @return
	 */
	public static String buildPath(String rootDir, String... dirs){
		StringBuilder rs = new StringBuilder(rootDir);
		rs.append(FILE_SEP);
		for(String d : dirs){
			rs.append(d);
			rs.append(FILE_SEP);
		}
		final String reg = "[\\\\|/]+";
		rootDir = rs.toString().replaceAll(reg, FILE_SEP);
		rootDir = rootDir.endsWith(FILE_SEP) ? rootDir : rootDir + FILE_SEP;
		return rootDir;
	}
	
	/**
	 * 边读边写
	 * @author Ych
	 * @param input
	 * @param output
	 */
	public static void readAndWrite(InputStream input, OutputStream output){
		final int bufferSize = 30 * 1024;		//缓冲设置为30KB
		BufferedInputStream bis = null;
		BufferedOutputStream bos = null;
		try {
        	
			bis = new BufferedInputStream(input, bufferSize); // 读入原文件  
			bos = new BufferedOutputStream(output, bufferSize); // 写入目标文件  
        	
			int by;
            while((by = bis.read()) != -1){
            	bos.write(by);
            }
		} catch (IOException e) {
			logger.error(e.getMessage(), e);
		} finally {
			close(bos, bis);
		}
	}
	
	/**
	 * 读取文件内容返回字符串
	 * @author Ych
	 * @param file
	 * @return
	 * @throws IOException
	 */
	public static String readAsString(File file, String charset) throws IOException{
		StringBuffer b = null;
		BufferedReader br = null;
		try {
			br = new BufferedReader(new InputStreamReader(new FileInputStream(file), charset));
			b = new StringBuffer();
			while (true) {
				int ch = br.read();
				if (ch == -1)
					break;
				b.append((char) ch);
			}
		} catch (IOException e) {
			throw e;
		} finally {
			close(br);
		}
		return b.toString();
	}
	
	/**
	 * 读取文件返回字节数组<br/>
	 * 注意：文件多大就会占有多大内存，这个方法只适合小文件，否则内存会爆掉
	 * @author Ych
	 * @param file
	 * @return
	 * @throws IOException
	 */
	public static byte[] readAsByte(File file) throws IOException{
		byte[] a = null;
		FileInputStream fis = null;
		try {
			fis = new FileInputStream(file);
			a = new byte[fis.available()];
			fis.read(a);
		} catch (Exception e) {
			throw e;
		} finally {
			close(fis);
		}
		return a;
	}
	
	/**
	 * 字节数组写入文件
	 * @author Ych
	 * @param bs
	 * @param file
	 * @throws IOException
	 */
	public static void writeAsByte(byte[] bs, File file) throws IOException{
		FileOutputStream fis = null;
		try {
			fis = new FileOutputStream(file);
			fis.write(bs);
		} catch (Exception e) {
			throw e;
		} finally {
			close(fis);
		}
	}
	
	/**
	 * 字符串内存写入文件
	 * @author Ych
	 * @param text
	 * @param file
	 * @param charset
	 * @throws IOException
	 */
	public static void writeAsString(String text, File file, String charset) throws IOException{
		BufferedWriter bw = null;
		try {
			bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file), charset));
			bw.write(text);
			close(bw);
		} catch (IOException e) {
			throw e;
		} finally {
			close(bw);
		}
	}
	
	public static void recursionCopy(File srcFile, String srcCharset, File targetFile, String targetCharset) throws IOException{
		if(srcFile.isDirectory()){
			if(!targetFile.exists()){
				targetFile.mkdirs();
			}
			for(File subFile : srcFile.listFiles()){
				recursionCopy(subFile, srcCharset, new File(targetFile.getCanonicalPath() + File.separator + subFile.getName()), targetCharset);
			}
		}
		if(srcFile.isFile()){
			targetFile = new File(targetFile.getCanonicalPath());
//			String code = FileUtil.readAsString(srcFile, srcCharset);
			byte[] bs = FileUtil.readAsByte(srcFile);
			byte[] tbs = null;
			if(bs.length > 3){
				if(bs[0] == -17 && bs[1] == -69 && bs[2] == -65){
					tbs = new byte[bs.length - 3];
					System.arraycopy(bs, 3, tbs, 0, bs.length - 3);
					bs = tbs;
				}
			}
			System.out.println(bs);
//			FileUtil.writeAsString(code, targetFile, targetCharset);
			FileUtil.writeAsByte(bs, targetFile);
			
		}
	}
	
	/**
	 * 序列化对象
	 * @author Ych
	 * 2017年5月17日
	 * @param output
	 * @param bean
	 */
	public static void serializeObject(OutputStream output, Object bean){
		ObjectOutputStream oos = null;
		try {
			oos = new ObjectOutputStream(output);
			oos.writeObject(bean);
			
		} catch (IOException e) {
			logger.error(e.getMessage(), e);
		} finally {
			close(oos);
		}
	}
	
	/**
	 * 序列化对象
	 * @author Ych
	 * 2017年5月17日
	 * @param file
	 * @param bean
	 */
	public static void serializeObject(File file, Object bean){
		try {
			serializeObject(new FileOutputStream(file), bean);
		} catch (FileNotFoundException e) {
			logger.error(e.getMessage(), e);
		}
	}
	
	/**
	 * 反序列化
	 * @author Ych
	 * 2017年5月17日
	 * @param input
	 * @return
	 */
	public static Object unserializeObject(InputStream input){
		Object bean = null;
		ObjectInputStream oos = null;
		try {
			oos = new ObjectInputStream(input);
			bean = oos.readObject();
			
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		} finally {
			close(oos);
		}
		return bean;
	}
	
	/**
	 * 反序列化
	 * @author Ych
	 * 2017年5月17日
	 * @param file
	 * @return
	 */
	public static Object unserializeObject(File file){
		try {
			return unserializeObject(new FileInputStream(file));
		} catch (FileNotFoundException e) {
			logger.error(e.getMessage(), e);
		}
		return null;
	}
	
	/**
	 * 递归获取所需要的文件列表
	 * @author YeChao
	 * 2017年6月19日
	 * @param file		文件/目录
	 * @param filter	文件过滤业务
	 * @return
	 */
	public static List<File> getFiles(File file, FileFilter filter){
		List<File> rs = null;
		
		if(file.isFile()){
			if(filter != null && filter.accept(file)){
				rs = new ArrayList<File>(1);
				rs.add(file);
			}
		}else if(file.isDirectory()){
			File[] subFiles = file.listFiles();
			rs = new ArrayList<File>(subFiles.length);
			
			for(File subFile : subFiles){
				List<File> subFileList = getFiles(subFile, filter);
				if(subFileList != null && !subFileList.isEmpty()){
					rs.addAll(getFiles(subFile, filter));
				}
			}
		}
		return rs;
	}
	
	/**
	 * URL下载文件
	 * @author Ych
	 * @param url		url
	 * @param fileFullPath	生成的文件路径
	 * @return
	 */
	public static File downloadFileByUrl(String url, String fileFullPath) throws IOException{
		File file = new File(fileFullPath);
		if(file.exists() && file.isFile()){
			return file;
		}
		
		if(FileUtil.isFile(file) && !file.getParentFile().exists()){
			file.getParentFile().mkdirs();
		}
		
		InputStream is = null;
		OutputStream os = null;
		try{
			URL u = new URL(url);
			URLConnection netCon = u.openConnection();		
			is = netCon.getInputStream();
			os = new FileOutputStream(file);
			
			readAndWrite(is, os);
		} catch(IOException e){
			throw e;
		} finally {
			close(is, os);
		}
		return file;
	}
	
	/**
	 * 压缩文件，默认是包含父文件夹名称
	 * @author Ych
	 * @param zipFilePathList	需要压缩的文件、目录列表
	 * @param targetFilePath	指定生成的压缩文件
	 * @return
	 */
	public static File zipFile(List<String> zipFilePathList, String targetFilePath) {
		FileOutputStream fos = null;
		ZipOutputStream zos = null;
		
		File targetFile = new File(targetFilePath);
		if(targetFile.exists()){
			targetFile.delete();
		}
		
		try {
			fos = new FileOutputStream(targetFile);
			zos = new ZipOutputStream(fos);
			
			for(String zipFilePath : zipFilePathList){
				addEntry(new File(zipFilePath), "", zos);
			}
			return targetFile;
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} finally {
			close(zos, fos);
		}
		return null;
	}
	
	/**
	 * 解压缩文件
	 * @author Ych
	 * @param sourceZipFilePath 压缩源文件全路径
	 * @param targetFilePath	解压缩后放置的目录路径
	 * @return
	 */
	public static File unzipFile(String sourceZipFilePath, String targetFilePath){
		File targetFileDir = new File(targetFilePath);
		if(!targetFileDir.exists()){
			targetFileDir.mkdirs();
		}
		try {
			ZipFile zipFile =  new ZipFile(sourceZipFilePath);
			Enumeration<? extends ZipEntry> entries = zipFile.entries();
			while(entries.hasMoreElements()){
				ZipEntry ze = entries.nextElement();
				File targetFile = new File(targetFilePath, ze.getName());
				
				if(ze.isDirectory()){
					if(!targetFile.exists()){
						targetFile.mkdirs();
					}
				}else{
					if(!targetFile.getParentFile().exists()){
						targetFile.getParentFile().mkdirs();
					}
					InputStream is = null;
					FileOutputStream fos = null;
			        try {
			        	is = zipFile.getInputStream(ze);
			        	
			        	int len;
			            byte[] buffer = new byte[1024 * 30];
			            fos = new FileOutputStream(targetFile);
				        while ((len = is.read(buffer)) != -1) {
				        	fos.write(buffer, 0, len);
				        }
			        } catch(Exception e){
			        	e.printStackTrace();
			        } finally {
			        	close(is, fos);
			        }
				}
			}
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		return targetFileDir;
	}
	
	/**
	 * 压缩文件
	 * @author Ych
	 * @param zipFilePath	需要压缩的文件、目录全路径，如目录路径：/home/test/；或文件路径：/home/test/abx.txt
	 * @param targetFilePath	指定生成的压缩文件全路径，如/home/zhangshan/abc.zip
	 * @return
	 */
	public static File zipFile(String zipFilePath, String targetFilePath){
		List<String> zipFilePathList = new ArrayList<String>(1);
		zipFilePathList.add(zipFilePath);
		return zipFile(zipFilePathList, targetFilePath);
	}
	
	/**
	 * 添加压缩文件
	 * @author Ych
	 * @param file
	 * @param parentPath
	 * @param zos
	 */
	private static void addEntry(File file, String parentPath, ZipOutputStream zos){
		if(file.isDirectory()){
			for(File subFile : file.listFiles()){
				addEntry(subFile, parentPath + file.getName() + File.separator, zos);
			}
		}else{
	        FileInputStream fis = null;
	        try {
	        	ZipEntry zipEntry = new ZipEntry(parentPath + file.getName());
	        	zos.putNextEntry(zipEntry);
	        	int len;
	            byte[] buffer = new byte[1024 * 30];
	            fis = new FileInputStream(file.getCanonicalPath());
		        while ((len = fis.read(buffer)) != -1) {
		            zos.write(buffer, 0, len);
		        }
		        zos.closeEntry();
	        } catch(Exception e){
	        	e.printStackTrace();
	        } finally {
	        	close(fis);
	        }
		}
	}
	
	/**
	 * 拷贝目录或文件，例如 source = /home/user/test/， target = /home/user/testTarget/
	 * 最终会把test目录下所有文件拷贝到testTarget目录下
	 * @author 谢明才
	 * @param source	需要拷贝的目录/文件
	 * @param dest	拷贝到指定目录
	 * @param isOverride 是否覆盖目标文件
	 * @throws IOException
	 */
	public static void copyFile(String source, String dest, boolean isOverride) throws IOException{
		File srcFile = new File(source);
		File destFile = new File(dest);
		
		if(!srcFile.exists()){
			throw new NullPointerException("不存在目标路径: " + source);
		}
		
		deepCopyFile(srcFile, destFile, isOverride);
	}
	
	
	/**
	 * 拷贝目录或文件，例如 source = /home/user/test/， target = /home/user/testTarget/
	 * 最终会把test目录下所有文件拷贝到testTarget目录下
	 * @author Ych
	 * @param source	需要拷贝的目录/文件
	 * @param dest	拷贝到指定目录
	 * @throws IOException
	 */
	public static void copyFile(String source, String dest) throws IOException{
		File srcFile = new File(source);
		File destFile = new File(dest);
		
		if(!srcFile.exists()){
			throw new NullPointerException("不存在目标路径: " + source);
		}
		
		deepCopyFile(srcFile, destFile);
	}
	
	/**
	 * 深拷贝文件/目录
	 * @author Ych
	 * @param source	需要拷贝的目录/文件
	 * @param target	拷贝到指定目录
	 * @throws IOException
	 */
	private static void deepCopyFile(File source, File target) throws IOException{
		if(source.isDirectory()){
			if(isFile(target)){
				throw new IllegalArgumentException("目录路径" + source + "无法复制给文件路径" + target);
			}
			target = new File(target.getCanonicalPath() + File.separator + source.getName());
			
			if(!target.exists()){
				target.mkdirs();
			}
			
			for(File subFile : source.listFiles()){
				deepCopyFile(subFile, target);
			}
		}
		if(source.isFile()){
			
			if(target.isFile()){
				String path = target.getCanonicalPath();
				int index = path.lastIndexOf(".");
				String prefix = path.substring(0, index);
				String suffix = path.substring(index);
				int len = 1;
				//如果文件已经存在，则需要从命名防止文件被覆盖 的情况
				while(target.isFile()){
					target = new File(prefix + "_" + len++ + suffix);
				}
			}
			
			if(isFile(target)){
				target.getParentFile().mkdirs();
			}else{
				target = new File(target.getCanonicalPath() + File.separator + source.getName());
			}
			
			FileInputStream fis = new FileInputStream(source);
			FileOutputStream fos = new FileOutputStream(target);
			readAndWrite(fis, fos);
			close(fis, fos);
		}
	}
	
	/**
	 *  深拷贝文件/目录
	 * @author 谢明才
	 * @param source
	 * @param target
	 * @param isOverride 是否覆盖目标文件，true表示覆盖目标文件，false表示不覆盖目标
	 * @throws IOException
	 */
	private static void deepCopyFile(File source, File target, boolean isOverride) throws IOException{
		if(source.isDirectory()){
			if(isFile(target)){
				throw new IllegalArgumentException("目录路径" + source + "无法复制给文件路径" + target);
			}
			target = new File(target.getCanonicalPath() + File.separator + source.getName());
			
			if(!target.exists()){
				target.mkdirs();
			}
			
			for(File subFile : source.listFiles()){
				deepCopyFile(subFile, target);
			}
		}
		if(source.isFile()){
			
			if(target.isFile() && !isOverride){
				String path = target.getCanonicalPath();
				int index = path.lastIndexOf(".");
				String prefix = path.substring(0, index);
				String suffix = path.substring(index);
				int len = 1;
				//如果文件已经存在，则需要从命名防止文件被覆盖 的情况
				while(target.isFile()){
					target = new File(prefix + "_" + len++ + suffix);
				}
			}
			
			if(isFile(target)){
				target.getParentFile().mkdirs();
			}else{
				target = new File(target.getCanonicalPath() + File.separator + source.getName());
			}
			
			FileInputStream fis = new FileInputStream(source);
			FileOutputStream fos = new FileOutputStream(target);
			readAndWrite(fis, fos);
			close(fis, fos);
		}
	}
	/**
	 * 判断是否是文件
	 * @author Ych
	 * @param file
	 * @return
	 */
	private static boolean isFile(File file){
		if(!file.exists()){
			String name = file.getName();
			//如果有文件后缀，则大众认为是文件
			return name.contains(".") ? true : false;
		}
		return file.isFile();
	}
	
	/**
	 * 关闭流
	 * @author Ych
	 * @param closeable
	 */
	public static void close(AutoCloseable... closeable){
		for(AutoCloseable c : closeable){
			try {
				if (c != null) {
					if(c instanceof Flushable){
						((Flushable) c).flush();
					}
					c.close();
				}
			} catch (Exception e) {
				logger.error(e.getMessage(), e);
			}
		}
	}
	
	/**
	 * 将文件重命名为UUID格式的名称
	 * @author 邓端宁
	 */
	public static File rename(File file, String newFileName) throws BusinessException{
		String fileName = file.getName();
		newFileName = newFileName + (fileName.lastIndexOf(".") != -1 ? fileName.substring(fileName.lastIndexOf(".")) : "");
		File newFile = new File(file.getParent() + "/" + newFileName);
		file.renameTo(newFile);
		return newFile;
	}

}
