package helper;

import java.io.InputStream;
import java.util.List;

import manager.SqlVersionManager;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {

	private static final String DB_NAME = "mydata.db"; // 数据库名称
	private static final int version = 4; // 数据库版本
	private Context context;
	private SqlVersionManager sql_manager;
	public DatabaseHelper(Context context) {
		super(context, DB_NAME, null, version);
		this.context = context;
	}


	@Override
	public void onCreate(SQLiteDatabase db) { 
		initDataBase(db);
	}
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		initDataBase(db);
	}
	
    void initDataBase(SQLiteDatabase db){
    	List<String> sqls=getSqlData();
    	if(sqls!=null){
    		for(String sql:sqls){
    			db.execSQL(sql);
    		}
    		SPHelper.setBaseMsg(context, "sql_version", version);
    	}
    }
    
	List<String> getSqlData() {
		try {
			int sql_version = SPHelper.getBaseMsg(context, "sql_version", 0);
			if (sql_version < version) {
				//InputStream fileStream = context.getResources().getAssets().open("sqlversions.xml");
				InputStream fileStream = context.getClass().getClassLoader().getResourceAsStream("assets/"+"sqlversions.xml");
				sql_manager=new SqlVersionManager(fileStream);
				return sql_manager.getSqlsByVersion(sql_version);
			}
		} catch (Exception ex) {
			
		}
		return null;
	}
}