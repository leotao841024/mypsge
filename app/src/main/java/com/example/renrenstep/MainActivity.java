package com.example.renrenstep;

import Interface.Functional;
import bean.SessionInfo;
import manager.ConversationServiceHandler;
import fragment.TalkFramgment;
import helper.BGHelper;
import manager.ImageCacheManger;
import helper.SPHelper;
import helper.StepDataHelper;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;
import tools.FragmentControl;
import tools.Toast;
import org.json.JSONObject;
import bean.FoodCal;
import helper.BaseAsyncTask;
import comm.CommHelper;
import comm.FoodXmlHandler;
import receiver.BrocastReviverManager;
import service.MyService;
import service.SimpleStepService;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.PowerManager;
import android.os.PowerManager.WakeLock;
import android.support.v4.content.LocalBroadcastManager;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;
import com.alibaba.wukong.AuthConstants;
import com.umeng.socialize.controller.UMServiceFactory;
import com.umeng.socialize.controller.UMSocialService;
import com.umeng.socialize.sso.UMSsoHandler;
import constant.Cons;
import fragment.GameFragment;
import fragment.HomeFragment;
import fragment.MineFragment;

public class MainActivity extends Activity implements OnClickListener {
	public static UMSocialService mController;
	private long temptime;
	private RadioButton main_tab_home, main_tab_mine,main_tab_talk,main_tab_game;
	private HomeFragment homeFragment;
	private MineFragment mineFragment;
	private TalkFramgment talkFragment;
	private GameFragment gameFragment;
	private TextView noreadnum;
	static MainActivity mainpage;
	private BrocastReviverManager brocastMana;
	private ConversationServiceHandler conversationServiceHandler;
	private FragmentControl fragmentControl;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_main);
		initData();
		init();
		initView();
		initHold();
		uploadData();
		showPoint();
		lockScr();
		initBrocaster();
		initConversationChangeListner();
		uploadStepData();
		registerListner();
	}
	void registerListner(){
		LocalBroadcastManager broadcast = LocalBroadcastManager.getInstance(this);
		IntentFilter intentFilter = new IntentFilter();
		intentFilter.addAction(AuthConstants.Event.EVENT_AUTH_KICKOUT);
		broadcast.registerReceiver(new BroadcastReceiver() {
			@Override
			public void onReceive(Context context, Intent intent) {
				String action = intent.getAction();
				if (AuthConstants.Event.EVENT_AUTH_KICKOUT.equals(action)) {
					kickOut();
				}
			}
		} , intentFilter);
	}
	void kickOut(){
		View view = LayoutInflater.from(this).inflate(R.layout.open_app, null);
		Button bt_finish=(Button)view.findViewById(R.id.bt_finish);
		TextView tv_content=(TextView)view.findViewById(R.id.tv_content);
		TextView tv_title=(TextView)view.findViewById(R.id.tv_title);
		tv_content.setText(getResources().getString(R.string.other_place_login));
		tv_title.setText(getResources().getString(R.string.replace_login_title));
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setCancelable(false);
		builder.setView(view);
		final AlertDialog  dialog= builder.create();
		dialog.getWindow().setType(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);
		dialog.show();
		bt_finish.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				dialog.dismiss();
				Intent intent=new Intent(MainActivity.this,AppActivity.class);
				startActivity(intent);
				finish();
			}
		});
	}

	void uploadStepData(){
		if(SPHelper.getBaseMsg(this, "bindkey", "3").equals("3")){
			new AsyncTask<String, String, Double>(){
				@Override
				protected Double doInBackground(String... arg0) {
					new StepDataHelper(MainActivity.this).UploadData();
					return null;
				}
			}.execute("");
		}
	}

	void initConversationChangeListner(){
		conversationServiceHandler.listnerConversationStatusChange(new Functional.Action<List<SessionInfo>>() {
			@Override
			public void handler(List<SessionInfo> sessionInfos) {
				loadConversations();
			}
		});
	}
	void initBrocaster(){
		noreadnum = (TextView)findViewById(R.id.noreadnum);
		brocastMana.addFilter(Cons.RECIVE_MSG_ACTION);
		brocastMana.setCallback(new BrocastReviverManager.ReciverCallback() {
			@Override
			public void handler(Intent intent) {
				loadConversations();
			}
		});
		brocastMana.begin();
	}
	void lockScr(){
		if(CommHelper.isLockedPhone()&&SPHelper.getBaseMsg(this, "bindkey","3").equals("3")){
			acquireWakeLock();
		}
	}
	private WakeLock wakeLock = null;
	/** * 获取电源锁，保持该服务在屏幕熄灭时仍然获取CPU时，保持运行 */
	@SuppressWarnings("deprecation")
	@SuppressLint("Wakelock")
	private void acquireWakeLock() {
		if (null == wakeLock) {
			PowerManager pm = (PowerManager) getSystemService(Context.POWER_SERVICE);
			wakeLock =pm.newWakeLock(PowerManager.SCREEN_DIM_WAKE_LOCK,"My Tag");//半屏幕休眠
			if (wakeLock != null) {
				wakeLock.acquire();
			}
		}
	}

	boolean judge = false;

	private void showPoint() {
		// TODO Auto-generated method stub
		int point = SPHelper.getDetailMsg(this, "point", 0);
		if (point == 0) {
			final LinearLayout pointView = (LinearLayout) findViewById(R.id.ll_point);
			pointView.setVisibility(View.VISIBLE);
			final ImageView imgShare = (ImageView) pointView.findViewById(R.id.iv_share_point);
			imgShare.setVisibility(View.VISIBLE);
			final ImageView imgToday = (ImageView) pointView.findViewById(R.id.iv_today_point);
			pointView.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					if (!judge) {
						imgShare.setVisibility(View.INVISIBLE);
						imgToday.setVisibility(View.VISIBLE);
						judge = true;
						return;
					}
					if (judge) {
						pointView.setVisibility(View.INVISIBLE);
						SPHelper.setDetailMsg(MainActivity.this, "point", 1);
					}
				}
			});
		}
	}

	private void loadPurpers() {
		new BaseAsyncTask(Cons.SPORT_TARGET, new HashMap<String, Object>(),
				BaseAsyncTask.HttpType.Get, "", this) {
			@Override
			public void handler(String param) {
				// TODO Auto-generated method stub
				int total = 10000, fast = 8000, cal = 500;
				try {
					if (param != null && param.contains("status")) {
						JSONObject object = new JSONObject(param);
						int status = object.getInt("status");
						if (status == 0) {
							total = object.getInt("totalsteps");
							fast = object.getInt("faststeps");
							cal = object.getInt("calorie");
						}
					}
				} catch (Exception ex) {
				}
				SPHelper.setDetailMsg(MainActivity.this, "totalsteps", total);
				SPHelper.setDetailMsg(MainActivity.this, "faststeps", fast);
				SPHelper.setDetailMsg(MainActivity.this, "calorie", cal);
			}
		}.execute("");
	}

	void uploadData() {
		CommHelper.uploadMData(this);
	}

	String getVersion() {
		String version = "";
		try {
			PackageManager manager = this.getPackageManager();
			PackageInfo info = manager.getPackageInfo(this.getPackageName(), 0);
			version = info.versionName;

		} catch (Exception ex) {

		}
		return version;
	}

	void initHold() {
		String isfirst = SPHelper.getDetailMsg(this, "isfirst", "true");
		if (isfirst.equals("true")) {
			loadPurpers();
			SPHelper.setDetailMsg(this, "holdproc", true);
			SPHelper.setDetailMsg(this, "isrun", true);
			SPHelper.setDetailMsg(this, "isfirst", "false");
			SPHelper.setBaseMsg(this, "phonexh", android.os.Build.MODEL);
			SPHelper.setBaseMsg(this, "phonebb",android.os.Build.VERSION.RELEASE);
			Intent service = new Intent(this, MyService.class);
			service.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			startService(service);
		} else {
			SPHelper.setDetailMsg(this, "holdproc", true);
			boolean isrun = SPHelper.getDetailMsg(this, "isrun", true);
		}
		Intent service = new Intent(this, MyService.class);
		startService(service);

	}

	void sendStat() {
		String key = "pvkey";
		Date currentTime = new Date();
		SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");
		String dateString = formatter.format(currentTime);
		String datekey = SPHelper.getDetailMsg(this, key, "");
		if (!dateString.equals(datekey)) {
			SPHelper.setDetailMsg(this, key, dateString);
		}
	}

	void initData() {
		Date nowTime = new Date();
		SimpleDateFormat time = new SimpleDateFormat("yyyy-MM-dd");
		String key = time.format(nowTime);
		Set<String> res = SPHelper.getDetailMsg(this, key,new HashSet<String>());
		res = new HashSet<String>();
		List<FoodCal> list = FoodXmlHandler.getFoods(this);
		for (FoodCal item : list) {
			Random random = new Random();
			int ranval = random.nextInt(item.getFoods().size());
			res.add(String.valueOf(item.getFoods().get(ranval).getId()));
		}
		SPHelper.setDetailMsg(this, key, res);
		brocastMana=new BrocastReviverManager(this);
		conversationServiceHandler=new ConversationServiceHandler();
		mainpage = this;
		fragmentControl=new FragmentControl(this);
	}
	void loadConversations() {
		conversationServiceHandler.getConversationList(new Functional.IMCallback<List<SessionInfo>, Integer, String>() {
			@Override
			public void success(List<SessionInfo> arg0) {
				setNoReadNum(arg0);
			}
			@Override
			public void process(Integer arg0) {
			}

			@Override
			public void fail(String arg0) {
			}
		});
	}


	void setNoReadNum(List<SessionInfo> arg0){
		int total=0;
		for(SessionInfo item:arg0){
//			if(item.status()==ConversationStatus.NORMAL){
				total+=item.getNoreadnum();
//			}
		}
		noreadnum.setText(total+"");
		noreadnum.setVisibility(total==0?View.GONE:View.VISIBLE);
	}

	@Override
	protected void onResume() {
		super.onResume();
		initdata();
		new Handler().postDelayed(new Runnable() {
			@Override
			public void run() {
				// TODO Auto-generated method stub
				loadConversations();
			}
		}, 2000) ;
	}

	private void init() {
		mController = UMServiceFactory.getUMSocialService("com.umeng.share");
	}
	@SuppressLint("NewApi")
	private void initView() {
		main_tab_home = (RadioButton) findViewById(R.id.main_tab_home);
		main_tab_mine = (RadioButton) findViewById(R.id.main_tab_mine);
		main_tab_talk = (RadioButton) findViewById(R.id.main_tab_talk);
		main_tab_game = (RadioButton) findViewById(R.id.main_tab_game);
		main_tab_home.setOnClickListener(this);
		main_tab_mine.setOnClickListener(this);
		main_tab_talk.setOnClickListener(this);
		main_tab_game.setOnClickListener(this);
		homeFragment = new HomeFragment();
		mineFragment = new  MineFragment();
		mineFragment = new  MineFragment();
		talkFragment = new TalkFramgment();
		gameFragment = new GameFragment();

		fragmentControl.loadFragment(homeFragment, R.id.content_layout);
	}
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		switch (keyCode) {
			case KeyEvent.KEYCODE_BACK:
				if (event.getAction() == KeyEvent.ACTION_DOWN) {
					if (System.currentTimeMillis() - temptime > 2000) {
						Toast.makeText(this, R.string.next_exit,2000).show();
						temptime = System.currentTimeMillis();
					} else {
						finish();
					}
					return true;
				}
				break;
			default:
				break;
		}
		return false;
	}

	@SuppressLint("NewApi")
	void initdata() {
		String sex = SPHelper.getDetailMsg(this, Cons.APP_SEX,getString(R.string.appsex_man));
		main_tab_home.setCompoundDrawablesRelativeWithIntrinsicBounds(null,getResources().getDrawable(BGHelper.setHomeButton(this, sex)),null, null);
		main_tab_mine.setCompoundDrawablesRelativeWithIntrinsicBounds(null,getResources().getDrawable(BGHelper.setMineButton(this, sex)),null, null);
		main_tab_talk.setCompoundDrawablesRelativeWithIntrinsicBounds(null,getResources().getDrawable(sex.equals("M")?R.drawable.bt_talk_blue:R.drawable.bt_talk_red),null, null);
		main_tab_game.setCompoundDrawablesRelativeWithIntrinsicBounds(null,getResources().getDrawable(sex.equals("M")?R.drawable.bt_game_blue:R.drawable.bt_game_red),null, null);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		/** 使用SSO授权必须添加如下代码 */
		UMSsoHandler ssoHandler = mController.getConfig().getSsoHandler(
				requestCode);
		if (ssoHandler != null) {
			ssoHandler.authorizeCallBack(requestCode, resultCode, data);
		}
	}

	@Override
	protected void onDestroy() {
		new ImageCacheManger("stepic").clearBitmap();
		if (!SPHelper.getDetailMsg(this, "isrun", true)) {
			SPHelper.setDetailMsg(this, "holdproc", false);
			Intent intent = new Intent(this, SimpleStepService.class);
			if(intent!=null){
				stopService(intent);
			}
		}
		if(wakeLock!=null){
			wakeLock.release();
		}
		brocastMana.end();
		super.onDestroy();
	}
	@Override
	public void onClick(View arg0) {
		switch (arg0.getId()) {
			case R.id.main_tab_home:
				fragmentControl.loadFragment(homeFragment, R.id.content_layout);
				break;
			case R.id.main_tab_mine:
				fragmentControl.loadFragment(mineFragment, R.id.content_layout);
				break;
			case R.id.main_tab_talk:
				fragmentControl.loadFragment(talkFragment, R.id.content_layout);
				break;
			case R.id.main_tab_game:
				fragmentControl.loadFragment(gameFragment, R.id.content_layout);
				break;
			default:
				break;
		}
		initSelItem(arg0.getId());
	}
	void initSelItem(int pid){
		int [] ids={R.id.main_tab_home,R.id.main_tab_mine,R.id.main_tab_game,R.id.main_tab_talk};
		RadioButton[] radios={main_tab_home,main_tab_mine,main_tab_game,main_tab_talk};
		for(int i=0;i<ids.length;i++){
			if(ids[i]==pid){
				radios[i].setChecked(true);
			}else{
				radios[i].setChecked(false);
			}
		}
	}
}
