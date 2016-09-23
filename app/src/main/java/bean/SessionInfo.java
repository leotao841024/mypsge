package bean;

import Interface.Functional.IConversationHandler;

public class SessionInfo {
	private String conversationid;
	private String icon;
	private String title;
	private String lastmsg;
	private long peerid;
	private long toplevel;
	private int typ;
	private int totalmember;
	private int status;
	private int noreadnum; 
	private IConversationHandler conversation;
	private MessageInfo.MssageType messageType;
	public IConversationHandler getConversation() {
		return conversation;
	}
	public void setConversation(IConversationHandler conversation) {
		this.conversation = conversation;
	}
	public int getNoreadnum() {
		return noreadnum;
	}
	public void setNoreadnum(int noreadnum) {
		this.noreadnum = noreadnum;
	}
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}
	public String getConversationid() {
		return conversationid;
	}
	public void setConversationid(String conversationid) {
		this.conversationid = conversationid;
	} 
	public String getIcon() {
		return icon;
	} 
	public void setIcon(String icon) {
		this.icon = icon;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getLastmsg() {
		return lastmsg;
	}
	public void setLastmsg(String lastmsg) {
		this.lastmsg = lastmsg;
	} 
	public long getPeerid() {
		return peerid;
	}
	public void setPeerid(long peerid) {
		this.peerid = peerid;
	} 
	public long getToplevel() {
		return toplevel;
	}
	public void setToplevel(long toplevel) {
		this.toplevel = toplevel;
	}
	public int getTyp() {
		return typ;
	}
	public void setTyp(int typ) {
		this.typ = typ;
	}
	public int getTotalmember() {
		return totalmember;
	}
	public void setTotalmember(int totalmember) {
		this.totalmember = totalmember;
	}

	public MessageInfo.MssageType getMessageType() {
		return messageType;
	}

	public void setMessageType(MessageInfo.MssageType messageType) {
		this.messageType = messageType;
	}

	public enum SessionType{SINGLE,GROUP}
}
