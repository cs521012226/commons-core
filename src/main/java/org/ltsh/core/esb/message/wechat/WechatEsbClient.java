package org.ltsh.core.esb.message.wechat;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.ltsh.core.core.cipher.impl.MD5Crypt;
import org.ltsh.core.core.util.StringUtil;
import org.ltsh.core.esb.builder.XmlMsgConvert;
import org.ltsh.core.esb.client.base.EsbClientTemplate;
import org.ltsh.core.esb.client.base.EsbSenderRequest;
import org.ltsh.core.esb.client.base.EsbSenderResponse;
import org.ltsh.core.esb.client.base.SocketEsbComm;
import org.ltsh.core.esb.exp.TransException;
import org.ltsh.core.esb.message.wechat.vo.MsgInfoStatus;
import org.ltsh.core.esb.vo.MsgNode;

import com.alibaba.fastjson.JSON;

/**
 * ESB微信消息报文接口
 * @author Ych
 * 2018年4月13日
 */
public class WechatEsbClient extends EsbClientTemplate {
	private List<MsgInfoStatus> msgStatus = new ArrayList<MsgInfoStatus>();
	private String msgType;		//	STRING(20)	消息类型
	private String agentId;		//	STRING(20)	企业应用ID
	private String isSave;		//	Boolean	是否永久保存
	private String safe;		//	Boolean	是否是保密消息
	private String title;		//	STRING(200)	标题
	private String content;		//	STRING(500)	内容
	private String url;		//	STRING(200)	文件或图片回调下载地址
	private String seqNo;			//唯一参数
	private String channelcode;			//平台提供(渠道编码、密码)
	private String password;		//密码

	
	private StringBuilder userMailList;
	private StringBuilder userMobileList;
	private final String SEP = "|";
	
	private int startIndex = 0;	//当前开始索引
	private int endIndex = 0;	//当前结束索引
	
	public WechatEsbClient(String host, int port, int timeout){
		super(new SocketEsbComm(host, port, timeout), new XmlMsgConvert());
	}
	
	
	@Override
	protected void validParam() throws TransException {
		checkRequiredParam(msgType, "msgType");
		checkRequiredParam(agentId, "agentId");
		checkRequiredParam(isSave, "isSave");
		checkRequiredParam(safe, "safe");
		checkRequiredParam(title, "title");
		checkRequiredParam(content, "content");
		checkRequiredParam(password, "password");
	}


	@Override
	protected void sendBefore(EsbSenderRequest request) {
		String channelCode = getConsumerId();
		String consumerSeqNo = request.getConsumerSeqNo();
		
		request.setSysHead("USER_LANG", "CHINESE");
		
		request.setAppHead("BIZ_SEQ_NO", consumerSeqNo);
//		
		request.setLocalHead("CHANNEL_CODE", channelCode);
		//BODY节点
		request.setBodyHead("USERMAIL", userMailList.toString());
		request.setBodyHead("USERTELE", userMobileList.toString());
		request.setBodyHead("MSGTYPE", msgType);
		request.setBodyHead("AGENTID", agentId);
		request.setBodyHead("ISSAVE", isSave);
		request.setBodyHead("SAFE", safe);
		request.setBodyHead("TITLE", title);
		request.setBodyHead("URL", url);
		request.setBodyHead("SEQNO", StringUtil.isBlank(seqNo) ? consumerSeqNo : seqNo);
		
		
		//verifyValue生成规则：渠道编码+密码+流水号进行MD5，取后16位
		String encrypt = new MD5Crypt().encrypt(channelCode + password);
		String verifyValue = encrypt.substring(encrypt.length() - 16); 
				
		request.setBodyHead("CHANNELCODE", channelCode);
		request.setBodyHead("VERIFYVALUE", verifyValue);
		
		
		MsgNode body = request.getBody();
		MsgNode text = body.addChild("TEXT");
		text.set("CONTENT", content);
		
//		MsgNode body = request.getBody();
//		MsgNode newsArray = body.addChild("NEWS_ARRAY");
//		newsArray.set("TITLE", title); // STRING(500) 图文内容
//		newsArray.set("CONTENT", content); // STRING(200) 图文标题
//		newsArray.set("SUMMARY", ""); // STRING(500) 图文描述
//		newsArray.set("ORIURL", ""); // STRING(200) 原文链接
//		newsArray.set("THUMB", ""); // STRING(200) 图片链接
//		newsArray.set("AUTHOR", ""); // STRING(50) 作者
//		newsArray.set("ADDIMG", "");			//	BOOLEAN	是否添加到正文
	}


	@Override
	protected void sendAfter(EsbSenderResponse response) {
		String notExitsUsers = response.getBodyHead("NOTEXITSUSERS");	//不存在用户
//		String invalidUser = response.getBodyHead("NOTEXITSUSERS");	//无效用户
		
		for(int i = startIndex; i < endIndex; i++){
			MsgInfoStatus info = msgStatus.get(i);
			String mail = info.getMail() == null ? "" : info.getMail();
			String mobile = info.getMobile() == null ? "" : info.getMobile();
			
			if(StringUtil.isBlank(mobile) && StringUtil.isBlank(mail)){
				info.setStatus(false, "", "缺少电话或邮箱");
				continue;
			}
			
			//检查返回的信息里是否有不存在的用户
			if(notExitsUsers != null && (notExitsUsers.indexOf(mail) > 0 || notExitsUsers.indexOf(mobile) > 0)){
				info.setStatus(false, response.getCode(), "微信配置用户不存在");
			}else{
				info.setStatus(response.isSuccess(), response.getCode(), response.getMsg());
			}
		}
	}

	@Override
	public void send() throws IOException {
		if(msgStatus.isEmpty()){
			throw TransException.error("缺少电话或邮件列表");
		}
		
		int maxLength = 450;
		startIndex = 0;
		endIndex = 0;
		userMailList = new StringBuilder();
		userMobileList = new StringBuilder();
		
		while(endIndex < msgStatus.size()){
			MsgInfoStatus info = msgStatus.get(endIndex);
			
			String mobile = info.getMobile();
			String email = info.getMail();
			
			if(StringUtil.isBlank(mobile) && StringUtil.isBlank(email)){
				endIndex++;
				continue;
			}
			
			/**
			 * 分批发送，因为ESB接口手机与邮箱接收人字段不能超过500长度
			 */
			if(userMailList.length() > maxLength || userMobileList.length() > maxLength){
				super.send();
				
				userMailList = new StringBuilder();
				userMobileList = new StringBuilder();
				startIndex = endIndex;
			}
			
			userMailList.append(email).append(SEP);
			userMobileList.append(mobile).append(SEP);
			endIndex++;
		}
		
		if(userMailList.length() > SEP.length() || userMobileList.length() > SEP.length()){
			super.send();
			userMailList = new StringBuilder();
			userMobileList = new StringBuilder();
		}
	}
	
	/**
	 * 添加发送信息参数
	 * @author Ych
	 * @param status
	 */
	public void addMsgInfoStatus(MsgInfoStatus status){
		msgStatus.add(status);
	}
	
	@Override
	public String toString() {
		return JSON.toJSONString(this);
	}
	
	
	/**
	 * 清空用户数据
	 * @author Ych
	 */
	public void clearMsgInfoStatus(){
		msgStatus.clear();
	}

	public List<MsgInfoStatus> getMsgStatus() {
		return msgStatus;
	}

	public String getMsgType() {
		return msgType;
	}


	public void setMsgType(String msgType) {
		this.msgType = msgType;
	}


	public String getAgentId() {
		return agentId;
	}


	public void setAgentId(String agentId) {
		this.agentId = agentId;
	}


	public String getIsSave() {
		return isSave;
	}


	public void setIsSave(String isSave) {
		this.isSave = isSave;
	}


	public String getSafe() {
		return safe;
	}


	public void setSafe(String safe) {
		this.safe = safe;
	}


	public String getTitle() {
		return title;
	}


	public void setTitle(String title) {
		this.title = title;
	}


	public String getContent() {
		return content;
	}


	public void setContent(String content) {
		this.content = content;
	}


	public String getUrl() {
		return url;
	}


	public void setUrl(String url) {
		this.url = url;
	}
	
	public String getSeqNo() {
		return seqNo;
	}


	public void setSeqNo(String seqNo) {
		this.seqNo = seqNo;
	}
	public String getChannelcode() {
		return channelcode;
	}
	public void setChannelcode(String channelcode) {
		this.channelcode = channelcode;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}


	public static void main(String[] args) throws Exception{
		WechatEsbClient wec = new WechatEsbClient("11.8.129.95", 30073, 10 * 1000);
		
		wec.setServiceCode("11005000029");
		wec.setServiceScene("01");
		wec.setConsumerId("040404");
		wec.setOrgSysId("040404");
		wec.setBranchId("9900");
		wec.setUserId("99004");
		
		wec.setMsgType("text");
		wec.setAgentId("1");
		wec.setTitle("测试标题");
		
		final String BR = "\\r\\n";
		
//		String content = "ESB微信测试content" + DateUtil.convertDateToString(new Date(), DateUtil.Pattern.DATE_TIME) + "<br/>" + "第二行";
		String content = "尊敬的：XXX，您好；";
		content += BR + "本月工资条概览信息如下，请查阅！";
		content += BR;
		content += BR + "底薪：100";
		content += BR + "绩效工资：200";
		content += BR + "月绩效奖金：300";
		content += BR + "防暑降温费：400";
		content += BR + "个人养老保险：500";
		content += BR + "个人医疗保险：600";
		content += BR + "个人失业保险：700";
		content += BR + "个人住房公积金：800";
		content += BR + "个人工资所得税：900";
		content += BR + "实发工资：50";
		content += BR + "";
		content += BR + "注：如需查询更多详细信息，请使用NYTALK APP或登录PS系统进行工资单查询，感谢您的支持。";
		
		
		System.out.println(content);
		wec.setContent(content);
		wec.setIsSave("false");
		wec.setSafe("false");
		wec.setPassword("111111");
		
		MsgInfoStatus status = new MsgInfoStatus();
//		status.setMobile("18390930918");
//		status.setMail("chenqh@3gmsc.com");
		status.setMail("dengdn@gdnybank.com");
//		status.setMail("linwq@gdnybank.com");
		
		wec.addMsgInfoStatus(status);
		
		wec.send();
		
		List<MsgInfoStatus> list = wec.getMsgStatus();
		System.out.println(list);
	}
}
