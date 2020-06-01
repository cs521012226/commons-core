package org.ltsh.core.esb.client.base;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

import org.ltsh.core.core.constants.Config;
import org.ltsh.core.core.util.FileUtil;
import org.ltsh.core.esb.consts.EsbConsts;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * ESB报文请求客户端（HTTP协议）
 * @author Ych
 * 2017年11月20日
 */
public class HttpEsbComm implements EsbComm{
	protected Logger logger = LoggerFactory.getLogger(getClass());
	
	private String url;
	private int timeout;
	private String charset = Config.CHARSET;
	
	public HttpEsbComm(String url, int timeout){
		this.url = url;
		this.timeout = timeout;
	}
	
	/**
	 * 发送请求数据接口
	 * @author Ych
	 * @param request
	 * @return
	 */
	@Override
	public String send(String msg) throws IOException{
		BufferedReader br = null;
		BufferedWriter bw = null;
		
		try {
			URL u = new URL(url);
			
			HttpURLConnection netCon = (HttpURLConnection) u.openConnection();
			
			netCon.setConnectTimeout(timeout);
			netCon.setDoOutput(true);
			netCon.setRequestMethod("POST");
//			netCon.setRequestProperty("Content-Length", String.valueOf(data.length()));
			netCon.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
			
			netCon.connect();
			
			String xml = msg;
			
			bw = new BufferedWriter(new OutputStreamWriter(netCon.getOutputStream(), charset));
			bw.write(xml);
			bw.flush();
			logger.info("ESB request message data: \n" + xml);
			
			br = new BufferedReader(new InputStreamReader(netCon.getInputStream(), charset));
			StringBuilder sb = new StringBuilder();
			int len;
			while((len = br.read()) != -1){
				char c = (char) len;
				sb.append(c);
				if(c == '>' && sb.lastIndexOf(EsbConsts.SDOROOT_TAG) != -1){
					break;
				}
			}
			
			xml = sb.toString();
			logger.info("ESB response message data: \n" + xml);
			
			return xml;
		} finally {
			FileUtil.close(br, bw);
		}
	}
}
