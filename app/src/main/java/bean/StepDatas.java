package bean;

import java.util.List;

public class StepDatas {
	private List<MinuteStep> minStep;
	private List<UploadDataBase> baseData;
	public List<MinuteStep> getMinStep() {
		return minStep;
	}
	public void setMinStep(List<MinuteStep> minStep) {
		this.minStep = minStep;
	}
	public List<UploadDataBase> getBaseData() {
		return baseData;
	}
	public void setBaseData(List<UploadDataBase> baseData) {
		this.baseData = baseData;
	}
}
