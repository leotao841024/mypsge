package bean;

import java.util.List;


public class DeviseData {
	private int status;
	private List<Device> data;
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}
	public List<Device> getData() {
		return data;
	}
	public void setData(List<Device> data) {
		this.data = data;
	}
}
