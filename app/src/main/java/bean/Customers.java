package bean;

import java.util.List;

public class Customers {
	private int status;
	private List<Customer> items;
	private int count;
	
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}
	public List<Customer> getItems() {
		return items;
	}
	public void setItems(List<Customer> items) {
		this.items = items;
	}
	public int getCount() {
		return count;
	} 
	public void setCount(int count) {
		this.count = count;
	}
}
