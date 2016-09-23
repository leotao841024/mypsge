package bean;

import java.util.List;

public class ServiceReturnData {
	private int status;
	private List<ServiceData> data;
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}
	public List<ServiceData> getData() {
		return data;
	}
	public void setData(List<ServiceData> data) {
		this.data = data;
	}
}
