package bean;

import java.io.Serializable;


public class MinuteStep  implements Cloneable,Serializable {
	private int year;
	private int month;
	private int day;
	private int hour;
	private int minute;
	private int steps;
	private long timer;
	public MinuteStep()
	{
		this.year=0;
		this.month=0;
		this.day=0;
		this.hour=0;
		this.minute=0;
		this.steps=0;
		this.timer=0;
	}
	public int getYear() {
		return year;
	}
	public void setYear(int year) {
		this.year = year;
	}
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
	public int getHour() {
		return hour;
	}
	public void setHour(int hour) {
		this.hour = hour;
	}
	public int getMinute() {
		return minute;
	}
	public void setMinute(int minute) {
		this.minute = minute;
	}
	public int getSteps() {
		return steps;
	}
	public void setSteps(int steps) {
		this.steps = steps;
	}
	public long getTimer() {
		return timer;
	}
	public void setTimer(long timer) {
		this.timer = timer;
	} 
    public Object clone() throws CloneNotSupportedException { 
        return super.clone(); 
    }
}