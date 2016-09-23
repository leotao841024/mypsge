package bean;

import com.alibaba.wukong.im.Message;

public class Talked {
	private long mid;
	private String timer;
	private String picnm;
	private String cont;
	private String typ;
	private long messageid;
	private boolean isgood;


	private String msgtyp;

	private String nc;
	public String getNc() {
		return nc;
	}
	public void setNc(String nc) {
		this.nc = nc;
	}
	public String getTimer() {
		return timer;
	}
	public void setTimer(String timer) {
		this.timer = timer;
	}
	public String getPicnm() {
		return picnm;
	}
	public void setPicnm(String picnm) {
		this.picnm = picnm;
	}
	public String getCont() {
		return cont;
	}
	public void setCont(String cont) {
		this.cont = cont;
	}
	public String getTyp() {
		return typ;
	}
	public void setTyp(String typ) {
		this.typ = typ;
	}
	public long getMessageid() {
		return messageid;
	}
	public void setMessageid(long messageid) {
		this.messageid = messageid;
	}
	public boolean isIsgood() {
		return isgood;
	}
	public void setIsgood(boolean isgood) {
		this.isgood = isgood;
	}
	public long getMid() {
		return mid;
	}
	public void setMid(long mid) {
		this.mid = mid;
	}
	public String getMsgtyp() {
		return msgtyp;
	}

	public void setMsgtyp(String msgtyp) {
		this.msgtyp = msgtyp;
	}
	public int getChatTyp() {
		return chatTyp;
	}

	public void setChatTyp(int chatTyp) {
		this.chatTyp = chatTyp;
	}

	private int chatTyp;

	public Message getMessage() {
		return message;
	}

	public void setMessage(Message message) {
		this.message = message;
	}

	private Message message;

}
