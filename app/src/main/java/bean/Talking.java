package bean;

public class Talking {
	private String conversationid;
	private String nc;
	private String avatar;
	private int noreadnum;
	private String timer;
	private String lastWord; 
	private boolean isgroup;
	private long mid;
	private String msgtyp;
	private String completeLastwors;
	public boolean isIsgroup() {
		return isgroup;
	}
	public void setIsgroup(boolean isgroup) {
		this.isgroup = isgroup;
	}
	public long getMid() {
		return mid;
	}
	public void setMid(long mid) {
		this.mid = mid;
	}
	public String getConversationid() {
		return conversationid;
	}
	public void setConversationid(String conversationid) {
		this.conversationid = conversationid;
	}
	public String getNc() {
		return nc;
	}
	public void setNc(String nc) {
		this.nc = nc;
	}
	public String getAvatar() {
		return avatar;
	}
	public void setAvatar(String avatar) {
		this.avatar = avatar;
	}
	public int getNoreadnum() {
		return noreadnum;
	}
	public void setNoreadnum(int noreadnum) {
		this.noreadnum = noreadnum;
	}
	public String getTimer() {
		return timer;
	}
	public void setTimer(String timer) {
		this.timer = timer;
	}
	public String getLastWord() {
		return lastWord;
	}
	public void setLastWord(String lastWord) {
		this.lastWord = lastWord;
	}

	public String getMsgtyp() {
		return msgtyp;
	}

	public void setMsgtyp(String msgtyp) {
		this.msgtyp = msgtyp;
	}

	public String getCompleteLastwors() {
		return completeLastwors;
	}

	public void setCompleteLastwors(String completeLastwors) {
		this.completeLastwors = completeLastwors;
	}
}
