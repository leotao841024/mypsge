package db;
  
import java.util.ArrayList;
import java.util.HashMap; 
import java.util.List;
import java.util.Map;

import android.content.ContentValues; 
import android.database.Cursor;

public abstract class DBEntity<T> {
	public DBEntity(){} 
	protected DBhelper db = new DBhelper(); 
	protected Long id;
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	private void insert(){
		ContentValues values = getContentValues();
		db.insert(getTableNm(), values);
	}
	private void update(){
		db.update(getTableNm(), getContentValues(), " id=?", new String[]{getId()+""});
	} 
	public void save(){
		db.open();
		if(getId()!=null){
			update();
		}else{
			insert();
		}
		db.close();
	}
	public  T findById(Class<T> typ,long id){ 
		String sql="select "+getColumnNmStr()+" from "+getTableNm()+" where id="+id+"";
		db.open();
		Cursor cursor=db.query(sql);
		List<Map<String,Object>> maps = getCursorData(cursor);
		if(maps.size()==0){return null;}
		cursor.close();
		db.close();
		T t = null;
		try {
			t = typ.newInstance();
			build(t, maps.get(0));
		} catch (Exception e) {
		}
		return t;
	}
	 
	public List<T> find(Class<T> typ, String where, String orderBy, String limit,String offset,String... whereArgs){
		StringBuilder sb= new StringBuilder("select "+getColumnNmStr() +" from "+getTableNm()+"");
		contactSql(sb,where,"where"); 
		contactSql(sb,orderBy,"order by");
		contactSql(sb,limit,"limit");
		contactSql(sb,offset,"offset");
		List<T> mlist=new ArrayList<T>();
		db.open();
		Cursor cursor = db.query(sb.toString(), whereArgs);
		List<Map<String,Object>> maps = getCursorData(cursor);
		for(Map<String,Object> item:maps){
			T t = null;
			try {
				t = typ.newInstance();
				build(t,item);
				mlist.add(t);
			} catch (Exception e) {
			}
		}
		cursor.close();
		db.close();
		return mlist;
	}
	
	void contactSql(StringBuilder psql,String cont,String typ){
		if(!cont.equals("")){
			psql.append(" "+typ+" "+ cont);
		}
	}
	
	private List<Map<String,Object>> getCursorData(Cursor cursor){
		List<Map<String,Object>> mlist=new ArrayList<Map<String,Object>>();
		while(cursor.moveToNext()){
			Map<String,Object> map = new HashMap<String,Object>();
			String[] keys = getTableColumn();
			Class<?>[] types=getTableColumnTypes();
			for(int i=0;i < keys.length;i++){
				if(types[i].equals(String.class)){
					map.put(keys[i],cursor.getString(cursor.getColumnIndex(keys[i])));
				}else if(types[i].equals(Integer.class)){
					map.put(keys[i],cursor.getInt(cursor.getColumnIndex(keys[i])));
				}else if(types[i].equals(Long.class)){
					map.put(keys[i], cursor.getLong(cursor.getColumnIndex(keys[i])));
				}
			}
			mlist.add(map);
		}
		return mlist;
	}
	
	ContentValues getContentValues(){
		Object[] objs = getTableColumnValues();
		String[] keys = getTableColumn();
		Class<?>[] types=getTableColumnTypes();
		ContentValues values = new ContentValues();
		for(int i=0;i< objs.length;i++){
			if(types[i].equals(String.class)){
				values.put(keys[i], (objs[i]==null?"":(String)objs[i]));
			}else if(types[i].equals(Integer.class)){
				values.put(keys[i], (Integer)objs[i]);
			}else if(types[i].equals(Long.class)){
				values.put(keys[i], (Long)objs[i]);
			}
		}
		return values;
	}
	
	private String getColumnNmStr(){
		String[] arr = getTableColumn();
		StringBuilder sb = new StringBuilder();
		for(String item:arr){
			sb.append(item+",");
		}
		return sb.substring(0,sb.length()-1);
	}
	
	protected abstract String getTableNm();
	protected abstract String[] getTableColumn();
	protected abstract Object[] getTableColumnValues();
	protected abstract Class<?>[] getTableColumnTypes();
	protected abstract void build(T entry, Map<String, Object> values);
}
