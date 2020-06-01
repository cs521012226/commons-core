package org.ltsh.core.core.util;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.ConnectException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

import org.ltsh.core.core.constants.Config;

public class HttpClient {
	private String protocol = "http://";
	private int timeout = 10000;	//默认超时时间10秒
	private String charset = Config.CHARSET;	//默认字符集
	
	private String contentType = "application/x-www-form-urlencoded";
	private Map<String, String> header = new HashMap<String, String>();
	
	private Map<String, Object> params = new HashMap<String, Object>();
	private String url;
	
	
	public HttpClient(String url){
		this.url = url;
	}
	
	/**
	 * 添加参数
	 * @author Ych
	 * @param key
	 * @param value
	 */
	public void setParameter(String key, Object value){
		params.put(key, value);
	}
	public void setParameter(Map<String, ?> param){
		params.putAll(param);
	}
	
	public void clearParameter(){
		params.clear();
	}
	
	public void setHeader(String key, String value){
		header.put(key, value);
	}
	
	public void clearHeader(){
		header.clear();
	}
	
	/**
	 * 序列化URL
	 * @author	Ych
	 * @param url
	 * @param params
	 * @return
	 */
	private String serializeURL(boolean withParam) {
		StringBuilder rsUrl = new StringBuilder();
		if(!url.startsWith(protocol)){
			rsUrl.append(protocol).append(url);
		}else{
			rsUrl.append(url);
		}
		if(url.contains("?")){
			rsUrl.append("&");
		}else{
			rsUrl.append("?");
		}
		if(withParam){
			String paramStr = serializeParams();
			if(StringUtil.isBlank(paramStr)){
				return url;
			}
			rsUrl.append(paramStr);
		}
		return rsUrl.toString();
	}
	
	/**
	 * 序列化参数
	 * @author	Ych
	 * @param params
	 * @return
	 */
	private String serializeParams() {
		StringBuilder sb = new StringBuilder();
		if(params != null){
			for(Map.Entry<String, ?> p : params.entrySet()){
				String paramKey = p.getKey();
				Object v = p.getValue();
				String paramValue;
				try {
					paramValue = v == null ? "" : URLEncoder.encode(v.toString(), charset);
				} catch (UnsupportedEncodingException e) {
					throw new IllegalArgumentException(e);
				}
				sb.append("&").append(paramKey).append("=").append(paramValue);
			}
		}
		return sb.length() > 0 ? sb.substring(1) : sb.toString();
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
	public String sendGet() throws ConnectException, IOException{
		StringBuilder result = new StringBuilder();
		
		BufferedReader br = null;
		try {
			URL u = new URL(serializeURL(true));
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
			FileUtil.close(br);
		}
		
		return result.toString();
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
	public String sendPost() throws ConnectException, IOException{
		StringBuilder result = new StringBuilder();
		
		BufferedReader br = null;
		BufferedWriter pw = null;
		try {
			URL u = new URL(serializeURL(false));
			
			HttpURLConnection netCon = (HttpURLConnection) u.openConnection();
			String paramsData = serializeParams();
			
			netCon.setConnectTimeout(timeout);
			netCon.setDoOutput(true);
			netCon.setRequestMethod("POST");
			netCon.setRequestProperty("Content-Length", String.valueOf(paramsData.length()));
			netCon.setRequestProperty("Content-Type", contentType);
			for(Map.Entry<String, String> m : header.entrySet()){
				netCon.setRequestProperty(m.getKey(), m.getValue());
			}
			
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
			FileUtil.close(br, pw);
		}
		
		return result.toString();
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public void setProtocol(String protocol) {
		this.protocol = protocol;
	}

	public void setTimeout(int timeout) {
		this.timeout = timeout;
	}

	public void setCharset(String charset) {
		this.charset = charset;
	}

	public void setContentType(String contentType) {
		this.contentType = contentType;
	}
}
