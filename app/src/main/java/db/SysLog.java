package db; 
import java.util.Map; 
public class SysLog extends DBEntity<SysLog> {
	public SysLog(){}
	public SysLog(String key,String value,long timer,int typ){
		this.key=key;
		this.value=value;
		this.timer=timer;
		this.typ=typ;
	}
	private String key;
	private String value;
	private long timer;
	private int typ;
	public String getKey() {
		return key;
	}
	public void setKey(String key) {
		this.key = key;
	}
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
	public long getTimer() {
		return timer;
	} 
	public void setTimer(long timer) {
		this.timer = timer;
	} 
	public int getTyp() {
		return typ;
	}
	public void setTyp(int typ) {
		this.typ = typ;
	}
	@Override
	protected String getTableNm() { 
		return "sys_log";
	}
	@Override
	protected String[] getTableColumn() {
		return new String[]{"key","value","timer","typ"};
	}
	
	@Override
	protected Object[] getTableColumnValues() {
		return new Object[]{key,value,timer,typ};
	}
	
	@Override
	protected Class<?>[] getTableColumnTypes() { 
		return new Class<?>[]{String.class,String.class,Long.class,Integer.class};
	}
	
	@Override
	protected void build(SysLog entry, Map<String, Object> values) {
		// TODO Auto-generated method stub
		entry.setKey((String)values.get("key"));
		entry.setTimer((Long)values.get("timer"));
		entry.setValue((String)values.get("value"));
		entry.setTyp((Integer)values.get("typ"));
	}
}
