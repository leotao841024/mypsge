package com.example.renrenstep;

import helper.BGHelper;
import helper.SPHelper;

import java.util.ArrayList;
import java.util.List;

import comm.CommHelper;

import constant.Cons;
import bean.Recom;
import adapter.RecomAdapter;
import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;

public class RecommendActivity extends Activity {
	private LinearLayout iv_back,empty;
	private ListView lv_recommed;
	private RelativeLayout recombg;
	private Dialog dialog;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getActionBar().hide();
		setContentView(R.layout.activity_recommend);
		initView();
		initEvent(); 
	}
	void initEvent(){ 
		recombg.setBackgroundResource(BGHelper.setBackground(this,  SPHelper.getDetailMsg(this, Cons.APP_SEX, "M")));
		iv_back.setOnClickListener(new OnClickListener() { 
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
			}
		});
		RecomAdapter adapter=new RecomAdapter(this);
		List<Recom> list=getdata();
		if(list.size()!=0){
			empty.setVisibility(View.GONE);
		}
		adapter.setList(list);
		adapter.notifyDataSetChanged();
		lv_recommed.setAdapter(adapter); 
	}
	void initView(){
		iv_back=(LinearLayout)findViewById(R.id.iv_back);
		empty=(LinearLayout)findViewById(R.id.empty);
		recombg=(RelativeLayout)findViewById(R.id.recombg);  
		lv_recommed=(ListView)findViewById(R.id.lv_recommed);
		dialog=CommHelper.createLoadingDialog(this, "",SPHelper.getDetailMsg(RecommendActivity.this, Cons.APP_SEX,"M"));
	}
	List<Recom> getdata(){
		List<Recom> list=new ArrayList<Recom>();
		for(int i=0;i<3;i++){
			Recom rec=new Recom();
			rec.setImgid(R.drawable.regist_man);
			rec.setBrief("简介简介简介简介简介简介简介简介简介简介简介简介简介简介简介");
			rec.setId(i);
			rec.setTitle("标题");
			list.add(rec);
		}
		return list;
	}
}
