package db;

import db.DBbase;
import helper.SPHelper;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import helper.DatabaseHelper;
import constant.Cons;
import bean.DayStep;
import bean.HisSteps;
import bean.MinuteStep;
import bean.StepDatas;
import bean.UploadDataBase;

public class DbSqlLite extends DBbase {
	private SQLiteDatabase db;
	private Context context;
	public DbSqlLite(Context context, DatabaseHelper database) {
		this.context = context;
		db = database.getReadableDatabase();
	}

	@Override
	public void Insert(String sql) {
		db.execSQL(sql);
	}
	
	@Override
	public StepDatas UploadData() {
		long lastUpdateVal = SPHelper.getDetailMsg(context, "lastval",(long)0);
		StepDatas stepdata = new StepDatas();
		stepdata.setBaseData(getBaseData(lastUpdateVal));
		stepdata.setMinStep(getBaseDataDetail(lastUpdateVal));//CloseDb();
		return stepdata;
	}
	
	List<MinuteStep> getBaseDataDetail(long lastval) {
		String sql = "select * from apm_steps where timer>? and mtoken=?";
		List<MinuteStep> list = new ArrayList<MinuteStep>();
		Cursor cusor = db.rawQuery(sql, new String[] { lastval + "",SPHelper.getBaseMsg(context, "mid","0")});
		while (cusor.moveToNext()) {
			MinuteStep baseobj = new MinuteStep();
			baseobj.setYear(cusor.getInt(cusor.getColumnIndex("year")));
			baseobj.setMonth(cusor.getInt(cusor.getColumnIndex("month")));
			baseobj.setDay(cusor.getInt(cusor.getColumnIndex("day")));
			baseobj.setHour(cusor.getInt(cusor.getColumnIndex("hour")));
			baseobj.setMinute(cusor.getInt(cusor.getColumnIndex("minute")));
			baseobj.setTimer(cusor.getLong(cusor.getColumnIndex("timer")));
			baseobj.setSteps(cusor.getInt(cusor.getColumnIndex("steps")));
			list.add(baseobj);
		}
		cusor.close();
		return list;
	}

	List<UploadDataBase> getBaseData(long lastval) {
		List<UploadDataBase> list = new ArrayList<UploadDataBase>();
		String mid=SPHelper.getBaseMsg(context, "mid","0");
		String sql = "select apm_steps.year,apm_steps.month,apm_steps.day,sum(apm_steps.steps) as step,count(timer) as num,min(apm_steps.timer) as mintimer,max(apm_steps.timer) as maxtimer from apm_steps,(select year,month,day from apm_steps where timer>? and mtoken=? GROUP BY year,month,day) as b  WHERE apm_steps.year=b.year and apm_steps.month=b.month and apm_steps.day=b.day and apm_steps.mtoken=? GROUP BY apm_steps.year,apm_steps.month,apm_steps.day";
		Cursor cusor = db.rawQuery(sql, new String[] { lastval + "",mid,mid });
		while (cusor.moveToNext()) {
			UploadDataBase baseobj = new UploadDataBase();
			baseobj.setApptype("StepDataV2");
			baseobj.setYear(cusor.getInt(cusor.getColumnIndex("year")));
			baseobj.setMonth(cusor.getInt(cusor.getColumnIndex("month")));
			baseobj.setDay(cusor.getInt(cusor.getColumnIndex("day")));
			baseobj.setCollectdate(cusor.getInt(cusor.getColumnIndex("year"))
					+ "-" + cusor.getInt(cusor.getColumnIndex("month")) + "-"
					+ cusor.getInt(cusor.getColumnIndex("day")));
			baseobj.setMinutes(cusor.getInt(cusor.getColumnIndex("num")));
			baseobj.setBegintime(GetStringFromLong(cusor.getLong(cusor
					.getColumnIndex("mintimer"))));
			baseobj.setEndtime(GetStringFromLong(cusor.getLong(cusor
					.getColumnIndex("maxtimer"))));
			baseobj.setSteps(cusor.getInt(cusor.getColumnIndex("step")));
			baseobj.setTimer(cusor.getLong(cusor.getColumnIndex("maxtimer")));
			baseobj.setDatakey(Integer.parseInt(SPHelper.getBaseMsg(context,"mid","0")));
			list.add(baseobj);
		}
		cusor.close(); 
		return list;
	}

	String GetStringFromLong(long millis) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Calendar cal = Calendar.getInstance();
		Date dt = new Date(millis);
		cal.setTime(dt);
		return cal.get(Calendar.HOUR) + ":" + cal.get(Calendar.MINUTE) + ":00";
	}

	@Override
	public List<HisSteps> GetAllStep() {
		// TODO Auto-generated method stub
		List<HisSteps> bases = new ArrayList<HisSteps>();
		String mid=SPHelper.getBaseMsg(context, "mid","0");
		String sql = "select year,month,day,sum(steps) as step,max(timer) as maxtimer,(select sum(steps) from apm_steps t where t.year=apm_steps.year and t.month=apm_steps.month and t.day=apm_steps.day and t.steps>120  and t.mtoken=?) as faststep from apm_steps where mtoken=? GROUP BY year,month,day ORDER BY timer desc";
		Cursor cursor = db.rawQuery(sql, new String[]{mid,mid});
		long lstval = SPHelper.getDetailMsg(context, "lastval",0); // ConfigHelper1.getLastValue(context);
		//long lastUpdateVal =SPHelper.getDetailMsg(context, "lastval",0);
		while (cursor.moveToNext()) {
			HisSteps baseobj = new HisSteps();
			baseobj.setYear(cursor.getInt(cursor.getColumnIndex("year")));
			baseobj.setMonth(cursor.getInt(cursor.getColumnIndex("month")));
			baseobj.setDay(cursor.getInt(cursor.getColumnIndex("day")));
			long timer = cursor.getLong(cursor.getColumnIndex("maxtimer"));
			baseobj.setTimer(timer);
			int total = cursor.getInt(cursor.getColumnIndex("step"));
			baseobj.setSteps(total);
			String faststep = cursor.getString(cursor
					.getColumnIndex("faststep"));
			int fasts = faststep == null ? 0 : Integer.parseInt(faststep);
			baseobj.setFastStep(fasts);
			int per = total==0?0:fasts * 100 / total;
			baseobj.setPercent(per);
			baseobj.setStatus(lstval >= timer);
			bases.add(baseobj);
		}
		cursor.close();
		return bases;
	}

	@Override
	public DayStep GetOneDay(int year, int month, int day) {
		String mid=SPHelper.getBaseMsg(context, "mid","0");
		String sql = " select sum(steps) as step ,count(steps) as num from apm_steps where year=? and month=?  and day=? and mtoken=?";
		Cursor cusr = db.rawQuery(sql, new String[] { year + "", month + "",
				day + "",mid });
		int total = 0;
		int mins = 0;
		while (cusr.moveToNext()) {
			total = cusr.getInt(cusr.getColumnIndex("step"));
			mins = cusr.getInt(cusr.getColumnIndex("num"));
		}
		DayStep step = new DayStep();
		step.setFast_step(GetFastStep(year, month, day));
		step.setMinutes(mins);
		step.setTotal_step(total);
		cusr.close();
		return step;
	}

	int GetFastStep(int year, int month, int day) {
		int faststep = 0;
		String mid=SPHelper.getBaseMsg(context, "mid","0");
		String sql = "select sum(steps) as step ,count(steps) as num from apm_steps where year=? and month=?  and day=? and steps>="+Cons.FIRSTSTEP+" and mtoken=?";
		Cursor cusr = db.rawQuery(sql, new String[] { year + "", month + "",
				day + "",mid});
		while (cusr.moveToNext()) {
			faststep = cusr.getInt(cusr.getColumnIndex("step"));
		}
		cusr.close();
		return faststep;
	}
	
	@Override
	protected void finalize() throws Throwable {
		CloseDb();
		super.finalize();
	}
	private void CloseDb(){
		if(db!=null)
		{
			db.close();
		}
		//if(dbhelper!=null)
		//{
		//	dbhelper.close();
	//	}
	}

	@Override
	public List<MinuteStep> GetOneDaySteps(int year, int month, int day) {
		String mid=SPHelper.getBaseMsg(context, "mid","0");
		String sql = " select * from apm_steps where year=? and month=?  and day=? and mtoken=?";
		Cursor cusr = db.rawQuery(sql, new String[] { year + "", month + "",
				day + "",mid}); 
		List<MinuteStep> mlist=new ArrayList<MinuteStep>();
		while (cusr.moveToNext()) {
			MinuteStep minstep=new MinuteStep();
			minstep.setYear(year);
			minstep.setMonth(month);
			minstep.setDay(day);
			minstep.setHour(cusr.getInt(cusr.getColumnIndex("hour")));
			minstep.setMinute(cusr.getInt(cusr.getColumnIndex("minute")));
			minstep.setTimer(cusr.getInt(cusr.getColumnIndex("timer")));
			minstep.setSteps(cusr.getInt(cusr.getColumnIndex("steps")));
			mlist.add(minstep);
		}
		return mlist;
	}

	@Override
	public void DelStepHisData(String sql) {
		// TODO Auto-generated method stub
		db.execSQL(sql);
	}

	
}
