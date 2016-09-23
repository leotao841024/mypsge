package bean;

public class StepHisData {
	private int year;
	private int month;
	public int getMonth() {
		return month;
	}
	public void setMonth(int month) {
		this.month = month;
	}
	public int getDay() {
		return day;
	}
	public void setDay(int day) {
		this.day = day;
	}
	public int getSteps() {
		return steps;
	}
	public void setSteps(int steps) {
		this.steps = steps;
	}
	private int day;
	private int steps;
	public int getYear() {
		return year;
	}
	public void setYear(int year) {
		this.year = year;
	}
	private int faststep;
	public int getFaststep() {
		return faststep;
	}
	public void setFaststep(int faststep) {
		this.faststep = faststep;
	}
}
