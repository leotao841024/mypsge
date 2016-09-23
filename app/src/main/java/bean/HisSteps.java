package bean;

public class HisSteps extends UploadDataBase {
	private int fastStep;
	public int getPercent() {
		return percent;
	}
	public void setPercent(int percent) {
		this.percent = percent;
	}
	private int percent;
	private boolean status;
	public int getFastStep() {
		return fastStep;
	}
	public void setFastStep(int fastStep) {
		this.fastStep = fastStep;
	}
	public boolean isStatus() {
		return status;
	}
	public void setStatus(boolean status) {
		this.status = status;
	}
	@Override
	public String toString() {
		return "HisSteps [fastStep=" + fastStep + ", percent=" + percent
				+ ", status=" + status + "]";
	} 
	
}
