package com.example.renrenstep;

import helper.SPHelper;

import java.util.ArrayList;
import java.util.List;

import adapter.TabAdapter;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.widget.ImageView;
import fragment.LeaderFragment1;
import fragment.LeaderFragment2;

public class LeaderActivity extends FragmentActivity {
	private ViewPager vp_leader;
	private TabAdapter adapter;
	private List<Fragment> list;
	private ImageView imageView1, imageView2;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//getActionBar().hide();
		setContentView(R.layout.activity_leader);
		SPHelper.setDetailMsg(this, "login", 0);
		vp_leader = (ViewPager) findViewById(R.id.vp_leader);
		imageView1 = (ImageView) findViewById(R.id.imageView1);
		imageView2 = (ImageView) findViewById(R.id.imageView2);
		list = new ArrayList<Fragment>();
		list.add(new LeaderFragment1());
		list.add(new LeaderFragment2());
		adapter = new TabAdapter(getSupportFragmentManager(), list);
		vp_leader.setAdapter(adapter);
		vp_leader.setOnPageChangeListener(new OnPageChangeListener() {
			@Override
			public void onPageSelected(int arg0) {
				// TODO Auto-generated method stub
				switch (arg0) {
				case 0:
					imageView1.setImageResource(R.drawable.icon_point_select);
					imageView2.setImageResource(R.drawable.icon_point_normal);
					break;
				case 1:
					imageView1.setImageResource(R.drawable.icon_point_normal);
					imageView2.setImageResource(R.drawable.icon_point_select);
					break;
				default:
					break;
				}
			} 
			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onPageScrollStateChanged(int arg0) {
				// TODO Auto-generated method stub
				
			}
		});
	}

}
