package com.example.renrenstep;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.widget.RelativeLayout;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.HashMap;
import java.util.Map;
import helper.BaseAsyncTask;
import helper.DatabaseHelper;
import db.DbSqlLite;
import comm.SqlLiteManager;
import constant.Cons;
import helper.SPHelper;
import helper.UpdateManager;
import tools.ToastManager;
public class AnimActivity extends LoginActivity {
	private UpdateManager mUpdateManager;
	View view;
	int status;
	private String version;
	RelativeLayout linear_anim;
	private SqlLiteManager stepManger; 
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		view = View.inflate(getApplicationContext(), R.layout.activity_anim,null);
		setContentView(view);
		mUpdateManager = new UpdateManager(this);
		try {
			PackageManager manager = this.getPackageManager();
			PackageInfo info = manager.getPackageInfo(this.getPackageName(), 0);
			version = info.versionName;
		} catch (Exception ex) {
			version = "";
		}
		stepManger = new SqlLiteManager(new DbSqlLite(AnimActivity.this,new DatabaseHelper(AnimActivity.this)));
		getVersion();
	}
	
	private void initAnim() {
		linear_anim = (RelativeLayout) view.findViewById(R.id.linear_anim);
		AlphaAnimation anim = new AlphaAnimation(1.0f, 1.0f);
		anim.setDuration(3000);
		linear_anim.startAnimation(anim);
		anim.setAnimationListener(new AnimationListener() {
			@Override
			public void onAnimationStart(Animation animation) {
				// TODO Auto-generated method stub
			}
			
			@Override
			public void onAnimationRepeat(Animation animation) {
				// TODO Auto-generated method stub 
			}

			@Override
			public void onAnimationEnd(Animation animation) {
				noUpdata();
			}
		});
	}
	
	public void noUpdata() {
		String token = SPHelper.getBaseMsg(AnimActivity.this, "mtoken", "");
		String mid = SPHelper.getBaseMsg(AnimActivity.this, "mid", "");
		String ischecked = SPHelper.getDetailMsg(AnimActivity.this,"ischecked", "");
		if (token != "" && mid != "" && ischecked != "") {
			String uid = SPHelper.getBaseMsg(AnimActivity.this, "uid", "");
			String pwd = SPHelper.getBaseMsg(AnimActivity.this, "pwd", "");
			checkLoginMsg(uid, pwd);
		} else {
			toLogin();
		}
	}
	
	void toLogin() {
		Intent intent = new Intent(AnimActivity.this, AppActivity.class);
		startActivity(intent);
		AnimActivity.this.finish();
	} 
	private void getVersion() {
		if (!version.equals("")) {
			Map<String, Object> maps = new HashMap<String, Object>();
			String type = getResources().getString(R.string.type); 
			maps.put("typ", type);
			maps.put("vsn", version);
			new BaseAsyncTask(Cons.GET_VERSION, maps, BaseAsyncTask.HttpType.Get, "", this) {
				@Override
				public void handler(String param) {
					Log.i("GET_VERSION", param);
					if (param != null) {
						try {
							JSONObject jsonObject = new JSONObject(param);
							status = jsonObject.getInt("status");
							SPHelper.setBaseMsg(getApplicationContext(),"versionStatus", status);
							switch (status) {
							case 0:// 无更新
								initAnim();
								break;
							case 1:// 主版本更新
								mUpdateManager.checkUpdateInfo(status);
								break;
							case 2:// 子版本更新
								mUpdateManager.checkUpdateInfo(AnimActivity.this);
								break;
							case 3:// 修正本更新
								initAnim();
								break;
							default:
								initAnim();
								break;
							}
						} catch (JSONException e) {
							e.printStackTrace();
							initAnim();
						}
					}
				}
			}.execute("");
		}
	}
	@Override
	protected void login_Success() {
		login_sys();
	}
	
	@Override
	protected void login_Fail() {
		toLogin(); 
		ToastManager.show(AnimActivity.this,getResources().getString(R.string.userpwderror), 2000); 
	}
	
	@Override
	protected void login_Exception() {
		login_sys();
	}
	
	void login_sys(){
		Intent main = new Intent(AnimActivity.this, MainActivity.class);
		startActivity(main);
		finish();
	}
}
