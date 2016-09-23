package com.example.renrenstep;

import helper.SPHelper;

import java.util.ArrayList;
import java.util.List;

import comm.CommHelper;
import view.CustomViewPager;
import adapter.TabAdapter;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBarActivity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;
import fragment.UserAgeFragment;
import fragment.UserHeightFragment;
import fragment.UserInfoFragment;
import fragment.UserSexFragment;
import fragment.UserSportsFragment;
import fragment.UserTroubleFragment;
import fragment.UserWeightFragment;

public class RegisterActivity extends ActionBarActivity implements
		UserAgeFragment.IOnPagerScrollListener {
	private List<Fragment> list;
	public static List<String> ages = new ArrayList<String>();
	public static List<String> heights = new ArrayList<String>();
	public static List<String> weights = new ArrayList<String>();
	private CustomViewPager viewPager;
	private LinearLayout layout_register;
	private View infoFragment;
	public static final int EXIT = -1;
	public UserAgeFragment userAgeFragment;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getSupportActionBar().hide();
		setContentView(R.layout.activity_register);
		infoFragment = LayoutInflater.from(this).inflate(R.layout.fragment_user, null);
		userAgeFragment = new UserAgeFragment(this);
		
		init();
		
		initData();

		Intent intent = getIntent();
		String reg = intent.getStringExtra("reg");
		if(reg!=null&&reg.equals("regist")){
			loadPager(1);
			CommHelper.insert_visit(this,"genderpg");
			Toast.makeText(this, "请完善个人信息", 1).show();
		}else{
			loadPager(0);
		} 
	}

	private void initData() {
		// TODO Auto-generated method stub
		for (int i = 14; i <= 100; i++) {
			ages.add(String.valueOf(i));
		}
		for (int j = 130; j < 230; j++) {
			heights.add(String.valueOf(j));
		}
		for (int j = 20; j < 150; j++) {
			weights.add(String.valueOf(j));
		}
	}

	private void init() {
		layout_register = (LinearLayout) findViewById(R.id.layout_register);
		viewPager = (CustomViewPager) findViewById(R.id.viewPager);
	}
	
	public void loadPager(int position) {
		list = new ArrayList<Fragment>();
		list.add(new UserInfoFragment(this));
		list.add(new UserSexFragment(this, userAgeFragment, this));
		list.add(userAgeFragment);
		list.add(new UserHeightFragment(this));
		list.add(new UserWeightFragment(this));
		list.add(new UserTroubleFragment(this));
		list.add(new UserSportsFragment(this));
		TabAdapter adapter = new TabAdapter(getSupportFragmentManager(), list);
		viewPager.setAdapter(adapter);
		viewPager.setCurrentItem(position);
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK ) {  
			Intent intent=new Intent(RegisterActivity.this,AppActivity.class);
			startActivity(intent);
			finish();
		}
		return false; 
	}
	
	@Override
	public void OnPaterScroll(int position) {
		// TODO Auto-generated method stub
		if (position == EXIT) {
			Intent intent = new Intent(RegisterActivity.this,
					MainActivity.class);
			startActivity(intent);
			SPHelper.setDetailMsg(RegisterActivity.this,"ischecked","true");
			this.finish();
		} else {
			viewPager.setCurrentItem(position); 
		}
	} 
}
