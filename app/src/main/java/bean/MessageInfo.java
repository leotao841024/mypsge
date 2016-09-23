package bean;

import com.alibaba.wukong.im.Message;

import Interface.Functional.IConversationHandler;

public class MessageInfo {
	private long msgId;
	private String content;
	private String order;
	private long sendId;
	private int conversationtype;
	private long timer;
	private int msgConversationType;
	private String extendmsg;
	private String conversationId;

	public MssageType getMessageType() {
		return messageType;
	}

	public void setMessageType(MssageType messageType) {
		this.messageType = messageType;
	}
	private MssageType messageType;
	private IConversationHandler conversation;
	public IConversationHandler getConversation() {
		return conversation;
	}
	public void setConversation(IConversationHandler conversation) {
		this.conversation = conversation;
	}
	public String getConversationId() {
		return conversationId;
	}
	public void setConversationId(String conversationId) {
		this.conversationId = conversationId;
	}
	public String getExtendmsg() {
		return extendmsg;
	}
	public void setExtendmsg(String extendmsg) {
		this.extendmsg = extendmsg;
	}
	public long getMsgId() {
		return msgId;
	}
	public void setMsgId(long msgId) {
		this.msgId = msgId;
	}
	public long getSendId() {
		return sendId;
	}
	public void setSendId(long sendId) {
		this.sendId = sendId;
	}
	public int getConversationtype() {
		return conversationtype;
	}
	public void setConversationtype(int conversationtype) {
		this.conversationtype = conversationtype;
	}
	public long getTimer() {
		return timer;
	}
	public void setTimer(long timer) {
		this.timer = timer;
	}
	public int getMsgConversationType() {
		return msgConversationType;
	}
	public void setMsgConversationType(int msgConversationType) {
		this.msgConversationType = msgConversationType;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public String getOrder() {
		return order;
	}
	public void setOrder(String order) {
		this.order = order;
	}
	public enum MssageType{
		TXT,
		IMAGE
	}

	public Message getMessage() {
		return message;
	}

	public void setMessage(Message message) {
		this.message = message;
	}

	private Message message;

}
