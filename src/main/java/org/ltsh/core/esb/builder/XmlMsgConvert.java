package org.ltsh.core.esb.builder;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.XMLWriter;
import org.ltsh.core.core.constants.Config;
import org.ltsh.core.core.util.StringUtil;
import org.ltsh.core.esb.consts.EsbConsts;
import org.ltsh.core.esb.exp.TransException;
import org.ltsh.core.esb.vo.MsgNode;
import org.ltsh.core.esb.vo.MsgNodeInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * XML报文转换器
 * @author Ych
 * 2018年4月16日
 */
public class XmlMsgConvert implements MsgConvert {
	protected Logger logger = LoggerFactory.getLogger(getClass());
	
	private String charset = Config.CHARSET;

	@Override
	public String toMessage(MsgNode node) {
		if(node == null || node.getFields() == null || node.getFields().size() != 1){
			throw new IllegalArgumentException("root node must only one");
		}
		
		Document document = DocumentHelper.createDocument();
		
		//根节点
		Element root = document.addElement(EsbConsts.SDOROOT);
		root.addAttribute("package_type", "xml");
		recursionBuildElement(root, node.getFields().get(0).getOneValue());
		
		return buildXml(document);
	}
	
	/**
	 * 递归组装DOM节点
	 * @author Ych
	 * @param root
	 * @param node
	 */
	private void recursionBuildElement(Element root, MsgNode node){
		for(MsgNodeInfo info : node.getFields()){
			Element subEl = root.addElement(info.getKey());
			MsgNode subMsgNode = info.getOneValue();
			String value = info.getValue();
			
			if(subMsgNode != null && !subMsgNode.isEmpty()){
				recursionBuildElement(subEl, subMsgNode);
			}else{
				subEl.addText(StringUtil.isBlank(value) ? "" : value);
			}
		}
	}
	
	/**
	 * 构建xml格式的报文
	 * @author Ych
	 * @param response
	 * @return
	 */
	private String buildXml(Document doc){
		
		ByteArrayOutputStream bao = new ByteArrayOutputStream();
		
		OutputFormat fmt = OutputFormat.createPrettyPrint();
		//设置文件编码
		fmt.setEncoding(charset);
		// 设置换行
		fmt.setNewlines(true);
		
		XMLWriter writer = null;
		try {
			writer = new XMLWriter(bao, fmt);
			writer.setEscapeText(false);
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
	}

	@Override
	public MsgNode toObject(String message) {
		int pos = message.indexOf(EsbConsts.XML_HEAD);
		//截取前面的长度值
		if(pos != -1){
			message = message.substring(pos);
		}
		Document doc = null;
		try {
			doc = DocumentHelper.parseText(message);
		} catch (DocumentException e) {
			throw TransException.error("解析Dom报文错误: " + message, e);
		}
		Element root = doc.getRootElement();
		MsgNode rs = new MsgNode();
		
		recursionBuildMsgNode(root, rs);
		return rs;
	}
	
	
	private void recursionBuildMsgNode(Element root, MsgNode node){
		String name = root.getName();
		
		if(!root.elements().isEmpty()){
			MsgNode subNode = node.addChild(name);
			
			for(Object el : root.elements()){
				Element childEl = (Element) el;
				recursionBuildMsgNode(childEl, subNode);
			}
		}else{
			node.set(name, root.getTextTrim());
		}
	}

	public String getCharset() {
		return charset;
	}

	public void setCharset(String charset) {
		this.charset = charset;
	}
}
