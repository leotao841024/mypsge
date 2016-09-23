package bean;

public class Notify {
	private String title;
	private int msgid;
	private String time;
	private String src;
	public String getSrc() {
		return src;
	}
	public void setSrc(String src) {
		this.src = src;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public int getMsgid() {
		return msgid;
	}
	public void setMsgid(int msgid) {
		this.msgid = msgid;
	}
	public String getTime() {
		return time;
	}
	public void setTime(String time) {
		this.time = time;
	}
	
	public Notify() {
		super();
	}
	public Notify(String title, int msgid, String time,String src) {
		super();
		this.title = title;
		this.msgid = msgid;
		this.time = time;
		this.src=src;
	}
	@Override
	public String toString() {
		return "Notify [title=" + title + ", msgid=" + msgid + ", time=" + time
				+ "]";
	}
	
	
}
