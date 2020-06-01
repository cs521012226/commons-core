package org.ltsh.core.core.util;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.ConnectException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.HashMap;
import java.util.Map;

import org.ltsh.core.business.exp.BusinessException;
import org.ltsh.core.core.constants.Config;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 用来发送HTTP请求
 * @date 2015年5月12日
 * @author Ych
 */
public class HttpRequestor {
	private static Logger logger = LoggerFactory.getLogger(HttpRequestor.class); 
	
	private static final String SCHEME = "http://";
	private static final int TIMEOUT = 10000;	//默认超时时间10秒
	private static final String CHARSET = Config.CHARSET;	//默认字符集
	
	/**
	 * 序列化连接
	 * @author	Ych
	 * @param url
	 * @param params
	 * @return
	 */
	private static String serializeUrl(String url, Map<String, Object> params){
		if(!url.startsWith(SCHEME)){
			url = SCHEME + url;
		}
		String paramStr = serializeParams(params);
		if(StringUtil.isBlank(paramStr)){
			return url;
		}
		if(url.indexOf("?") != -1){
			url = url + "&" + paramStr;
		}else{
			url = url + "?" + paramStr;
		}
		return url;
	}
	
	/**
	 * 序列化参数
	 * @author	Ych
	 * @param params
	 * @return
	 */
	private static String serializeParams(Map<String, Object> params){
		StringBuilder sb = new StringBuilder("");
		if(params != null){
			for(Map.Entry<String, Object> p : params.entrySet()){
				String paramKey = p.getKey();
				String paramValue = p.getValue().toString();
				sb.append("&" + paramKey + "=" + paramValue);
			}
		}
		String result = sb.toString();
		return result.length() > 0 ? result.substring(1) : result;
	}
	
	/**
	 * http协议发送get请求
	 * @author Ych
	 * @param url		连接
	 * @param params	参数
	 * @param timeout	响应超时
	 * @param charset	响应字符集合
	 * @return
	 * @throws ConnectException, IOException
	 */
	public static String sendGet(String url, Map<String, Object> params, int timeout, String charset) 
			throws ConnectException, IOException{
		StringBuilder result = new StringBuilder();
		
		BufferedReader br = null;
		try {
			URL u = new URL(serializeUrl(url, params));
			URLConnection netCon = u.openConnection();
			
			netCon.setConnectTimeout(timeout);
			
			netCon.connect();
			
			br = new BufferedReader(new InputStreamReader(netCon.getInputStream(), charset));
			
			int len;
			while((len = br.read()) != -1){
				char chars = (char) len;
				result.append(chars);
			}
			
		} finally {
			if(br != null){
				try {
					br.close();
				} catch (IOException e) {
					logger.error(e.getMessage(), e);
				}
			}
		}
		
		return result.toString();
	}
	/**
	 * http协议发送get请求
	 * @author Ych
	 * @param url		连接
	 * @param paramName	参数KEY
	 * @param paramValue	参数value
	 * @param timeout	响应超时
	 * @param charset	响应字符集合
	 * @return
	 * @throws ConnectException, IOException
	 */
	public static String sendGet(String url, String paramName, Object paramValue, int timeout, String charset) 
			throws ConnectException, IOException{
		Map<String, Object> params = new HashMap<String, Object>();
		params.put(paramName, paramValue);
		return sendGet(url, params, timeout, charset);
	}
	
	/**
	 * http协议发送get请求
	 * @author Ych
	 * @param url		连接
	 * @param paramName	参数KEY
	 * @param paramValue	参数value
	 * @return
	 * @throws BusinessException
	 */
	public static String sendGet(String url, String paramName, Object paramValue) 
			throws ConnectException, IOException{
		return sendGet(url, paramName, paramValue, TIMEOUT, CHARSET);
	}
	
	/**
	 * http协议发送post请求
	 * @author Ych
	 * @param url		连接
	 * @param params	参数
	 * @param timeout	响应超时
	 * @param charset	响应字符集合
	 * @return
	 * @throws ConnectException, IOException
	 */
	public static String sendPost(String url, Map<String, Object> params, int timeout, String charset) 
			throws ConnectException, IOException{
		StringBuilder result = new StringBuilder();
		
		BufferedReader br = null;
		BufferedWriter pw = null;
		try {
			URL u = new URL(serializeUrl(url, null));
			
			HttpURLConnection netCon = (HttpURLConnection) u.openConnection();
			String paramsData = serializeParams(params);
			
			netCon.setConnectTimeout(timeout);
			netCon.setDoOutput(true);
			netCon.setRequestMethod("POST");
			netCon.setRequestProperty("Content-Length", String.valueOf(paramsData.length()));
			netCon.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
			
			pw = new BufferedWriter(new OutputStreamWriter(netCon.getOutputStream(), charset));
			pw.write(paramsData);
			pw.flush();
			
			netCon.connect();
			
			br = new BufferedReader(new InputStreamReader(netCon.getInputStream(), charset));
			
			int len;
			while((len = br.read()) != -1){
				char chars = (char) len;
				result.append(chars);
			}
			
		} finally {
			if(br != null){
				try {
					br.close();
				} catch (IOException e) {
					logger.error(e.getMessage(), e);
				}
			}
			if(pw != null){
				try {
					pw.close();
				} catch (IOException e) {
					logger.error(e.getMessage(), e);
				}
			}
		}
		
		return result.toString();
	}
	
	
	/**
	 * http协议发送post请求
	 * @author Ych
	 * @param url		连接
	 * @param paramName	参数KEY
	 * @param paramValue	参数value
	 * @param timeout	响应超时
	 * @param charset	响应字符集合
	 * @return		响应字符串
	 * @throws ConnectException, IOException
	 */
	public static String sendPost(String url, String paramName, Object paramValue, 
			int timeout, String charset) throws ConnectException, IOException{
		Map<String, Object> params = new HashMap<String, Object>();
		params.put(paramName, paramValue);
		return sendPost(url, params, timeout, charset);
	}
	
	/**
	 * http协议发送post请求
	 * @author Ych
	 * @param url
	 * @param paramName	参数KEY
	 * @param paramValue	参数value
	 * @return
	 * @throws ConnectException, IOException
	 */
	public static String sendPost(String url, String paramName, Object paramValue) 
			throws ConnectException, IOException{
		return sendPost(url, paramName, paramValue, TIMEOUT, CHARSET);
	}
}
