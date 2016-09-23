package com.example.renrenstep;

import java.util.List;

import manager.MyLog;
import manager.MyLog.LogLevel;
import tools.TimeFormat;
import db.SysLog;
import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

public class LogInfoActivity extends Activity {
	private ListView log_list;
	private List<SysLog> list;
	private LogAdapter adapter;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_log_info);
		initView();
		initData();
	}
	
	void initView(){
		log_list=(ListView)findViewById(R.id.log_list);
	}
	
	void initData(){
		initLogData(LogLevel.DEBUG);
		adapter=new LogAdapter();
		log_list.setAdapter(adapter);
	}
	
	void initLogData(LogLevel plevel){
		list=MyLog.getLogs(plevel);
	}
	
	class LogAdapter extends BaseAdapter{
		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return list.size() ;
		}

		@Override
		public Object getItem(int arg0) {
			// TODO Auto-generated method stub
			return list.get(arg0);
		}

		@Override
		public long getItemId(int arg0) {
			// TODO Auto-generated method stub
			return 0;
		}
		@Override
		public View getView(int arg0, View arg1, ViewGroup arg2) {
			// TODO Auto-generated method stub
			ViewHolder holder;
			if(arg1==null){
				holder=new ViewHolder();
				arg1 = LayoutInflater.from(LogInfoActivity.this).inflate(R.layout.log_item, null);
				holder.txt_key=(TextView)arg1.findViewById(R.id.txt_key);
				holder.txt_value=(TextView)arg1.findViewById(R.id.txt_val);
				holder.txt_timer=(TextView)arg1.findViewById(R.id.txt_timer);
				holder.txt_typ=(TextView)arg1.findViewById(R.id.txt_typ);
				arg1.setTag(holder);
			}else{
				holder=(ViewHolder)arg1.getTag();
			}
			holder.txt_key.setText(list.get(arg0).getKey());
			holder.txt_value.setText(list.get(arg0).getValue());
			holder.txt_typ.setText(list.get(arg0).getTyp()+"");
			holder.txt_timer.setText(TimeFormat.LongToDate(list.get(arg0).getTimer(),"yyyy/MM/dd"));
			return arg1;
		}
	}
	
	class ViewHolder{
		TextView txt_key,txt_value,txt_timer,txt_typ;
	}
}
