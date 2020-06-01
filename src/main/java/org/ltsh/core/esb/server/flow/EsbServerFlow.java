package org.ltsh.core.esb.server.flow;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.ltsh.core.core.constants.Config;
import org.ltsh.core.core.util.DateUtil;
import org.ltsh.core.esb.builder.MsgConvert;
import org.ltsh.core.esb.builder.XmlMsgConvert;
import org.ltsh.core.esb.consts.EsbConsts;
import org.ltsh.core.esb.exp.TransException;
import org.ltsh.core.esb.interceptor.EsbInterceptor;
import org.ltsh.core.esb.interceptor.EsbInvocation;
import org.ltsh.core.esb.server.EsbReceiverRequest;
import org.ltsh.core.esb.server.EsbReceiverResponse;
import org.ltsh.core.esb.server.impl.SimpleEsbReceiverRequest;
import org.ltsh.core.esb.server.impl.SimpleEsbReceiverResponse;
import org.ltsh.core.esb.vo.MsgNode;
import org.ltsh.core.socket.TransFlow;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * ESB服务端主要数据传输流程
 * @author Ych
 * 2017年7月24日
 */
public class EsbServerFlow implements TransFlow {
	
	private static Logger logger = LoggerFactory.getLogger(EsbServerFlow.class);
	private String charset = Config.CHARSET;
	private List<EsbInterceptor> interceptorList = new ArrayList<EsbInterceptor>();
	
	private MsgConvert msgConvert = new XmlMsgConvert();

	@Override
	public void process(InputStream input, OutputStream output) throws TransException {
		//读取流数据
		String xmlStr = readData(input);
		logger.info("Receive from ESB message: \n" + xmlStr);
		
		//解析报文大小
		xmlStr = splitSize(xmlStr);
		MsgNode msgNode = msgConvert.toObject(xmlStr);
		
		final EsbReceiverRequest req = esbReceiverRequestFactroy(msgNode);
		final EsbReceiverResponse resp = esbReceiverResponseFactroy();
		buildCommonInfo(req, resp);	//设置通用头信息
		
		//处理数据
		try {
			//启用事务
//			DbPro.use().tx(new IAtom() {
//				public boolean run() throws SQLException {
					new EsbInvocation(req, resp, interceptorList).invoke();
//					return true;
//				}
//			});
			resp.setSuccess(true);
			resp.setMsg(null);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			resp.setSuccess(false);
			resp.setMsg(e.getMessage());
		}
		//组装XML报文数据
		xmlStr = msgConvert.toMessage(resp.getRoot());
		logger.info("Response to ESB message: \n" + xmlStr);
		
		//写流数据
		writeData(output, xmlStr);
	}

	/**
	 * 如果报文最前面有大小信息，则截取
	 * @author Ych
	 */
	private String splitSize(String xmlStr){
		int pos = xmlStr.indexOf(EsbConsts.XML_HEAD);
		
		if(pos != -1){
//			String size = xmlStr.substring(0, pos);
			xmlStr = xmlStr.substring(pos);
		}
		return xmlStr;
	}
	
	/**
	 * 读取数据
	 * @author Ych
	 * 2017年7月24日
	 * @param inputStream
	 * @return
	 */
	protected String readData(InputStream inputStream){
		StringBuilder sb = new StringBuilder();
		
		BufferedReader br;
		try {
			br = new BufferedReader(new InputStreamReader(inputStream, charset));
			int len;
			while((len = br.read()) != -1){
				char c = (char) len;
				sb.append(c);
				if(c == '>' && sb.lastIndexOf(EsbConsts.SDOROOT_TAG) != -1){
					break;
				}
			}
		} catch (SocketTimeoutException e) {
			throw TransException.error("读取报文超时", e);
		} catch (Exception e) {
			throw TransException.error("读取报文IO错误", e);
		}
		return sb.toString();
	}
	
	/**
	 * EsbReceiverRequest工厂
	 * @author Ych
	 * @param xmlStr
	 * @return
	 */
	protected EsbReceiverRequest esbReceiverRequestFactroy(MsgNode msgNode){
		SimpleEsbReceiverRequest req = new SimpleEsbReceiverRequest(msgNode);
		
		/*buildReqData(root, req.getSysHeadMap(), EsbConsts.SYS_HEAD);
		buildReqData(root, req.getAppHeadMap(), EsbConsts.APP_HEAD);
		buildReqData(root, req.getLocalHeadMap(), EsbConsts.LOCAL_HEAD);
		buildReqData(root, req.getParameterMap(), EsbConsts.BODY);*/
		
		return req;
	}
	
	/**
	 * EsbReceiverResponse工厂
	 * @author Ych
	 * @return
	 */
	protected EsbReceiverResponse esbReceiverResponseFactroy(){
		SimpleEsbReceiverResponse resp = new SimpleEsbReceiverResponse();
		return resp;
	}
	
	/**
	 * 设置组装通用头信息
	 * @author Ych
	 * @param req
	 * @param resp
	 */
	protected void buildCommonInfo(EsbReceiverRequest req, EsbReceiverResponse resp){
		resp.setServiceCode(req.getServiceCode());
		resp.setConsumerId(req.getConsumerId());
		resp.setConsumerSeqNo(req.getConsumerSeqNo());
		resp.setServiceScene(req.getServiceScene());
		resp.setEsbSeqNo(req.getEsbSeqNo());
		resp.setTranDate(DateUtil.convertDateToString(new Date(), DateUtil.Pattern.DATE8));
		resp.setTranTimestamp(DateUtil.convertDateToString(new Date(), DateUtil.Pattern.TIME9));
		
		resp.setAppHead(EsbConsts.BRANCH_ID, req.getAppHead(EsbConsts.BRANCH_ID));
		resp.setAppHead(EsbConsts.USER_ID, req.getAppHead(EsbConsts.USER_ID));
	}
	
	/**
	 * 设置文档结构
	 * @author Ych
	 * @param response
	 * @param isSuccess
	 * @return
	 */
	/*protected Document setDocument(EsbReceiverRequest request, EsbReceiverResponse response){
		Document doc = DocumentHelper.createDocument();
		Element root = doc.addElement(EsbConsts.SDOROOT);
		
		Element sysHeadEl = buildRespElement(root, response.getSysHeadMap(), EsbConsts.SYS_HEAD);
		
		//设置成功/失败信息节点
		setRetMsg(sysHeadEl, request, response);
		
		buildRespElement(root, response.getAppHeadMap(), EsbConsts.APP_HEAD);
		buildRespElement(root, response.getLocalHeadMap(), EsbConsts.LOCAL_HEAD);
		
		if(response.isSuccess()){
			buildRespElement(root, response.getParameterMap(), EsbConsts.BODY);
		}else{	//如果有错误，则body不添加内容
			root.addElement(EsbConsts.BODY).addText("");
		}
		return doc;
	}*/
	
	/**
	 * 设置成功/失败信息节点
	 * @author Ych
	 * @param sysHeadEl
	 * @param isSuccess
	 */
	/*private void setRetMsg(Element sysHeadEl, EsbReceiverRequest request, EsbReceiverResponse response){
		String status;
		String retCode;
		String retMsg;
		String consumerId = request.getConsumerId();
		String msg = response.getMsg();
		
		//处理返回信息
		if(response.isSuccess()){
			status = EsbConsts.RET_STATUS_SUCCESS;
			retCode = "I" + consumerId + "000001";
			retMsg = StringUtil.nvl(msg, EsbConsts.RET_MSG_SUCCESS);
		}else{
			status = EsbConsts.RET_STATUS_FAIL;
			retCode = "E" + consumerId + "000001";
			retMsg = StringUtil.nvl(msg, EsbConsts.RET_MSG_FAIL);
		}
		sysHeadEl.addElement(EsbConsts.RET_STATUS).addText(status);
		Element sdoEl = sysHeadEl.addElement(EsbConsts.RET).addElement(EsbConsts.SDO);
		sdoEl.addElement(EsbConsts.RET_CODE).addText(retCode);
		sdoEl.addElement(EsbConsts.RET_MSG).addText(retMsg);
	}*/
	
	/**
	 * 构建响应dom
	 * @author Ych
	 * @param root
	 * @param head
	 * @param node
	 */
	/*private Element buildRespElement(Element root, KeyMap head, String node){
		Element headEl = root.addElement(node);
		if(head == null || head.isEmpty()){
			headEl.addText("");
			return null;
		}
		
		for(Map.Entry<String, ?> m : head.entrySet()){
			String key = m.getKey();
			Object value = m.getValue();
			
			Element el = headEl.addElement(key);
			el.addText(value == null ? "" : value.toString());
		}
		return headEl;
	}*/
	
	/**
	 * 设置请求dom
	 * @author Ych
	 * @param root
	 * @param head
	 * @param node
	 */
	/*private void buildReqData(Element root, KeyMap head, String node){
		//系统头
		Element headEl = root.element(node);
		if(headEl == null){
			return ;
		}
		for(Object child : headEl.elements()){
			Element childEl = (Element) child;
			head.put(childEl.getName(), childEl.getTextTrim());
		}
	}*/
	
	/**
	 * 构建xml格式的报文
	 * @author Ych
	 * @param response
	 * @return
	 */
	/*protected String buildXml(EsbReceiverRequest req, EsbReceiverResponse resp){
		
		Document doc = setDocument(req, resp);
		
		ByteArrayOutputStream bao = new ByteArrayOutputStream();
		
		OutputFormat fmt = OutputFormat.createPrettyPrint();
		//设置文件编码
		fmt.setEncoding(charset);
		// 设置换行
		fmt.setNewlines(true);
		
		XMLWriter writer = null;
		try {
			writer = new XMLWriter(bao, fmt);
			writer.write(doc);
		} catch(Exception e){
			logger.error(e.getMessage(), e);
		} finally {
			try {
				if(writer != null){
					writer.flush();
					writer.close();
				}
			} catch (IOException e) {
				logger.error(e.getMessage(), e);
			}
		}
		
		String result = null;
		try {
			result = bao.toString(charset);
			result = StringUtil.leftPad(String.valueOf(result.getBytes(charset).length), '0', 6) + result;
		} catch (UnsupportedEncodingException e) {
			logger.error(e.getMessage(), e);
		}
		
		return result;
	}*/
	
	/**
	 * 写数据
	 * @author Ych
	 * 2017年7月24日
	 * @param output
	 * @param data
	 */
	protected void writeData(OutputStream output, String data){
		BufferedWriter writer = null;
		try {
			writer = new BufferedWriter(new OutputStreamWriter(output, charset));
			writer.write(data);
			writer.flush();
			
		} catch (SocketTimeoutException e) {
			throw TransException.error("写出报文超时", e);
		} catch (IOException e) {
			throw TransException.error("写出报文数据IO错误", e);
		}
	}

	public void addInterceptorList(EsbInterceptor interceptor) {
		interceptorList.add(interceptor);
	}
	
}
