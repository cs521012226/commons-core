package org.ltsh.core.core.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.UUID;

/**
 * 常用公共方法
 * @Description
 * @author 邓端宁
 * @2015-8-3 下午3:29:38
 * @Copyright
 */
public class MethodUtil {
	/**
	 * 获取32位UUID
	 * @return
	 */
	public static String UUID() {
		return UUID.randomUUID().toString().replace("-", "");
	}
	/**
	 * 对字符串解码两次
	 * @param content
	 * @return
	 */
	public static String decoderTwo(String content) {
		try {
			return URLDecoder.decode(URLDecoder.decode(content,"utf-8"),"utf-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return "";
	}
	/**
	 * 对字符串解码两次
	 * @param content
	 * @return
	 */
	public static String decoderTwo(String content,String encoding) {
		try {
			return URLDecoder.decode(URLDecoder.decode(content,encoding),encoding);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return "";
	}
	/**
	 * 对字符串加码两次
	 * @param content
	 * @return
	 */
	public static String encoderTwo(String content) {
		try {
			return URLEncoder.encode(URLEncoder.encode(content,"utf-8"),"utf-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return "";
	}
	public static void main(String[] args) {
		try {
			System.out.println(URLEncoder.encode("运营检查系统操作手册1.0.doc","utf-8"));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
	}
	/**
	 * 对字符串加码两次
	 * @param content
	 * @return
	 */
	public static String encoderTwo(String content,String encoding) {
		try {
			return URLEncoder.encode(URLEncoder.encode(content,encoding),encoding);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return "";
	}
	/**
	 * 转化字符串
	 * @param sourceStr 源字符串
	 * @param charStr 分割字符串
	 * @return 返回如'1','2',..,'n'
	 */
	public static String converStr(String sourceStr,String charStr) {
		StringBuffer converStr = new StringBuffer("");
		String [] str = sourceStr.split(charStr);
		for (int i = 0; i < str.length; i++) {
			converStr.append("'").append(str[i]).append("'");
			if(i != str.length-1) converStr.append(",");
		}
		return converStr.toString();
	}
	
	/**
	 * 得到指定长度的字符串
	 * @param sourceStr 源字符串
	 * @param length 得到的字符串长度，如果源字符串长度大于指定长度则返回源字符串
	 * @param replaceStr 前补字符串，默认为空，则前补“0” 
	 * @return
	 * @throws Exception 
	 */
	public static String getLengthStr(String sourceStr,int length,String replaceStr) throws Exception {
		int len = sourceStr.length();
		if(len > length) return sourceStr;
		if(!StringUtil.isBlank(replaceStr) && replaceStr.length() > 1) throw new Exception("要补的字符串【"+replaceStr+"】长度不能大于1！");
		replaceStr = !StringUtil.isBlank(replaceStr) ? replaceStr : "0";
		String tmp = "";
		for (int i = 0; i < (length-len); i++) {
			tmp += replaceStr;
		}
		return tmp+sourceStr;
	}
	
	/**
	 * 文件复制，并删除源文件
	 * @param resourePath
	 * @param toPath
	 * @throws Exception
	 */
	public static void copyFile(String resourePath,String toPath) throws Exception{
		File fromFile = new File(resourePath);
		if(!fromFile.exists()) {
			throw new Exception("不存在路径："+resourePath);
		}
		File toFile = new File(toPath);
		if(!toFile.getParentFile().exists()) {
			toFile.getParentFile().mkdirs();
		}
		if(!toFile.exists()) {
			toFile.createNewFile();
		}
		OutputStream os = null;
		InputStream in = null;
		try {
			os = new FileOutputStream(toFile);
			in = new FileInputStream(fromFile);
			byte [] b = new byte[1024];
			int len;
	        while ((len = in.read(b)) != -1) {
	             os.write(b, 0, len);
	         }
	        os.flush();
		} finally {
			if(os!=null) {
				os.close();
			}
			if(in!=null) {
				in.close();
			}
			//关闭IO流才能删除文件
	        fromFile.delete();
		}
	}
}
