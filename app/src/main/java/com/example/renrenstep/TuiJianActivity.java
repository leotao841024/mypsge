package com.example.renrenstep;

import constant.Cons;
import helper.SPHelper;
import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class TuiJianActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.recommend);
		init();
	}
	private void init() {
		// TODO Auto-generated method stub
		String gender = SPHelper.getDetailMsg(this, Cons.APP_SEX, "M");
		RelativeLayout layout_actionbar = (RelativeLayout)findViewById(R.id.layout_actionbar);
		layout_actionbar.setBackgroundColor(gender.equals("M")?getResources().getColor(R.color.appcolor_blue):getResources().getColor(R.color.appcolor_red));
		LinearLayout ll_back = (LinearLayout)findViewById(R.id.ll_back);
		ll_back.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				TuiJianActivity.this.finish();
			}
		});
		TextView tv_rectitle = (TextView)findViewById(R.id.tv_rectitle);
		tv_rectitle.getPaint().setFakeBoldText(true);
	}
}
