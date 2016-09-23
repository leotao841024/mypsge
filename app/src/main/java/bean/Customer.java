package bean;

import java.io.Serializable;

public class Customer implements Serializable {
	private String nc;
	private String email;
	private String city_alia;
	private int id;
	private String avatar;
	private String state;
	private String words;
	private String remark; 
	private boolean isgroupmem;
	public boolean isIsgroupmem() {
		return isgroupmem;
	}
	public void setIsgroupmem(boolean isgroupmem) {
		this.isgroupmem = isgroupmem;
	}
	public String getNc() {
		return nc;
	}
	public void setNc(String nc) {
		this.nc = nc;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	 
	public String getCity_alia() {
		return city_alia;
	}
	public void setCity_alia(String city_alia) {
		this.city_alia = city_alia;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getAvatar() {
		return avatar;
	}
	public void setAvatar(String avatar) {
		this.avatar = avatar;
	}
	public String getState() {
		return state;
	}
	public void setState(String state) {
		this.state = state;
	}
	public String getWords() {
		return words;
	}
	public void setWords(String words) {
		this.words = words;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	} 
}
