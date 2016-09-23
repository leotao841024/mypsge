package bean;

public class UploadDataStail {
	private String apptype;
	private String[] collectdate;
	private Integer[] steps;
	private int datakey;
	
	public int getDatakey() {
		return datakey;
	}
	public void setDatakey(int datakey) {
		this.datakey = datakey;
	}
	public String getApptype() {
		return apptype;
	}
	public void setApptype(String apptype) {
		this.apptype = apptype;
	}
	public String[] getCollectdate() {
		return collectdate;
	}
	public void setCollectdate(String[] collectdate) {
		this.collectdate = collectdate;
	}
	public Integer[] getSteps() {
		return steps;
	}
	public void setSteps(Integer[] steps) {
		this.steps = steps;
	}
}
