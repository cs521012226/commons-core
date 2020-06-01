package org.ltsh.core.esb.client.base;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;

import org.ltsh.core.core.constants.Config;
import org.ltsh.core.core.util.FileUtil;
import org.ltsh.core.esb.consts.EsbConsts;
import org.ltsh.core.esb.exp.TransException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * ESB报文请求客户端（TCP/IP协议）
 * @author Ych
 * 2017年11月20日
 */
public class SocketEsbComm implements EsbComm{
	protected Logger logger = LoggerFactory.getLogger(getClass());
	
	private String host;
	private int port;
	private int timeout;
	private String charset = Config.CHARSET;
	
	public SocketEsbComm(String host, int port, int timeout){
		this.host = host;
		this.port = port;
		this.timeout = timeout;
	}
	
	/**
	 * 发送请求数据接口
	 * @author Ych
	 * @param request
	 * @return
	 */
	@Override
	public String send(String message) throws IOException{
		Socket socket = null;
		BufferedReader br = null;
		BufferedWriter bw = null;
		
		try {
			socket = new Socket(host, port);
			socket.setKeepAlive(true);
			socket.setSoTimeout(timeout);
			
			bw = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream(), charset));
			
			String xml = message;
			
			bw.write(xml);
			bw.flush();
			logger.info("ESB request message data: \n" + xml);
			
			br = new BufferedReader(new InputStreamReader(socket.getInputStream(), charset));
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
		} catch (UnknownHostException e) {
			throw TransException.error("服务器地址错误: " + host);
		} catch (SocketTimeoutException e) {
			throw TransException.error("连接超时: " + timeout + "毫秒");
		} catch (IOException e) {
			throw TransException.error("IO连接错误", e);
		} finally {
			FileUtil.close(br, bw, socket);
		}
	}
}
