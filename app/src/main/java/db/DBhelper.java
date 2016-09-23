package db;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteOpenHelper;
import app.MyApplication;
import helper.DatabaseHelper;

public class DBhelper {

	private static DBhelper sDBHelper = null;
	private SQLiteOpenHelper mSqliteHelper = null;

	public  DBhelper() {
	}

	public synchronized static DBhelper instance() {
		if (sDBHelper == null) {
			sDBHelper = new DBhelper();
		}
		return sDBHelper;
	}

	public synchronized void open() {
		close();
		mSqliteHelper = new DatabaseHelper(MyApplication.getInstance());
	}

	public synchronized void insert(String sql) {
		if (mSqliteHelper == null) {
			return;
		}
		mSqliteHelper.getWritableDatabase().execSQL(sql);
	}

	public synchronized long insert(String table, ContentValues values) {
		if (mSqliteHelper == null) {
			return -1;
		}
		return mSqliteHelper.getWritableDatabase().insert(table, null, values);
	}
	
	public synchronized int update(String table, ContentValues values,String whereClause, String[] whereArgs) {
		if (mSqliteHelper == null) {
			return -1;
		}
		return mSqliteHelper.getWritableDatabase().update(table, values,whereClause, whereArgs);
	}

	public synchronized Cursor query(String table, String[] columns,
			String selection, String[] selectionArgs, String groupBy,
			String having, String orderBy) {
		if (mSqliteHelper == null) {
			return null;
		}
		return mSqliteHelper.getReadableDatabase().query(table, columns,
				selection, selectionArgs, groupBy, having, orderBy);
	}

	public synchronized Cursor query(String sql) { 
		return query(sql,null);
	}
	
	public synchronized Cursor query(String sql,String[] selectinArgs) {
		if (mSqliteHelper == null) {
			return null;
		} 
		return mSqliteHelper.getReadableDatabase().rawQuery(sql, selectinArgs);
	}
	
	public synchronized int delete(String table, String whereClause,
			String[] whereArgs) {
		if (mSqliteHelper == null) {
			return -1;
		}
		return mSqliteHelper.getReadableDatabase().delete(table, whereClause,whereArgs);
	}
	
	public synchronized void close() {
		if (mSqliteHelper != null) {
			mSqliteHelper.close();
			mSqliteHelper = null;
		}
	}
}
