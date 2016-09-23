package bean;

import java.io.Serializable;

public class DayStep implements Serializable {
	private int total_step;
	private int fast_step;
	private int minutes;
	public int getTotal_step() {
		return total_step;
	}
	public void setTotal_step(int total_step) {
		this.total_step = total_step;
	}
	public int getFast_step() {
		return fast_step;
	}
	public void setFast_step(int fast_step) {
		this.fast_step = fast_step;
	}
	public int getMinutes() {
		return minutes;
	}
	public void setMinutes(int minutes) {
		this.minutes = minutes;
	}
	@Override
	public String toString() {
		return "DayStep [total_step=" + total_step + ", fast_step=" + fast_step
				+ ", minutes=" + minutes + "]";
	}
	
}
