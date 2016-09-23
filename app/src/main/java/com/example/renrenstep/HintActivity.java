package com.example.renrenstep;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import comm.CommHelper;

public class HintActivity extends Activity implements CommHelper.IAleterCallback {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getActionBar().hide();
		setContentView(CommHelper.getAlterView(this,this));
		/*
		findViewById(R.id.bt_next).setOnClickListener(this); 
		List<String> forceList = new ArrayList<String>(); 
		List<String> allowList = new ArrayList<String>();
		ListView lv_force = (ListView)findViewById(R.id.lv_force);
		ListView lv_allow = (ListView)findViewById(R.id.lv_allow);
		PermissionHelper per=new PermissionHelper(this);
		List<Permin> list = per.getPermin();
		String[] levels={"must","middlemust","notmust"};
		for(String item:levels){
			 List<String> mystrs=getPerStr(list,item);
			 if(mystrs.size()>0){
				 if(item.equals("must")||(item.equals("middlemust")&&CommHelper.isLockedPhone())){
					 forceList.addAll(mystrs);
				 }else{
					 allowList.addAll(mystrs);
				 }
			 }
		}
		HintAdapter fAdapter = new HintAdapter(this, forceList, HintAdapter.RED);
		lv_force.setAdapter(fAdapter);
		HintAdapter lAdapter = new HintAdapter(this, allowList, HintAdapter.GRAY);
		lv_allow.setAdapter(lAdapter);List<String> getPerStr(List<Permin> list,String level){
		List<String> minlist=new ArrayList<String>();
		for(Permin item:list){
			if(item.getLevel().equals(level)&&!CommHelper.getPermission(item.getName(), this)){
				minlist.add(item.getDesc());
			}
		}
		return minlist;
	}  
		*/
	} 
	@Override
	public void aleterCallback() {
		// TODO Auto-generated method stub
		Intent intent=new Intent(this,AppActivity.class);
		startActivity(intent);
		HintActivity.this.finish();
	}
}
