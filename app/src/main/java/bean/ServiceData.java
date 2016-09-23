package bean;

import java.util.List;

public class ServiceData {
	private long collect_date;
	private List<ServiceStepData> smds; 
	public long getCollect_date() {
		return collect_date;
	}

	public void setCollect_date(long collect_date) {
		this.collect_date = collect_date;
	}

	public List<ServiceStepData> getSmds() {
		return smds;
	}
	
	public void setSmds(List<ServiceStepData> smds) {
		this.smds = smds;
	}
}
