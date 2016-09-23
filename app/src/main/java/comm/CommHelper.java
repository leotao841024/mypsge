package comm;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import org.apache.commons.codec.CharEncoding;

import helper.BaseAsyncTask;
import helper.DatabaseHelper;
import bean.Permin;
import bean.ServiceData;
import bean.ServiceStepData;
import bean.VisitRecord;

import com.example.renrenstep.R;
import com.google.gson.Gson;

import adapter.HintAdapter;
import android.net.NetworkInfo.State;
import constant.Cons;
import helper.BGHelper;
import helper.SPHelper;
import android.app.Dialog;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.ConnectivityManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;

import app.MyApplication;

public class CommHelper { 
	public static String app_version = "";
	public static String link_way = "";
	public static String[][] mids={{"174908","新朋友","1512011556547174908.png"}};
	public static String normalMsg="";
	public static String agreeMsg="AGREE_FRIEND";
	public static String deleteMsg="REMOVE_FRIEND";
	public static String createGroupMsg="CREATE_GROUP";
	public static String addMemGroup="ADD_MEM_GROUP";
	public static String removeMemGroup="REMOVE_MEM_GROUP";
	public static String quitMemGroup="QUIT_GROUP";
	public static String changeMemMsg="CHANGE_MEM_MSG";
	public static String changeGroupTitle="CHANGE_GROUP_TITLE";
	public static String UPDATEMEMMSG="group_mem_updatemsg";
	public static String MSG_CREATE="MSG_CREATE";
	public static String MSG_DELETE="MSG_DELETE";
	public static String MSG_ALTER="MSG_ALTER";
	
	public static String getCompleteStr(String msg){
		return String.format("%1$-20s", msg);
	}
	public static String[] systemRemindStr={createGroupMsg,addMemGroup,removeMemGroup,quitMemGroup,changeGroupTitle};
	public static boolean isCommFriend(String pid){
		for(String[] item : mids){
			if(item[0].equals(pid)){
				return true;
			}
		}
		return false;
	}
	
	public static String[] sysCommandArr={changeMemMsg};
	public static boolean isCommandMsg(String pmsg){
		for(String item:sysCommandArr){
			if(item.equals(pmsg)){
				return true;
			}
		}
		return false;
	}
	public static String getLinkWay(Context context) {
		ConnectivityManager manager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE); 
		if (manager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE) != null) {
			State gprs = manager
					.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState();
			State wifi = manager.getNetworkInfo(ConnectivityManager.TYPE_WIFI)
					.getState();
			if (gprs == State.CONNECTED || gprs == State.CONNECTING) {
				return "gprs";
			} 
			if (wifi == State.CONNECTED || wifi == State.CONNECTING) {
				return "wifi";
			}
		}
		return "";
	}

	static void initAppVersion(Context context) {
		if (app_version.equals("")) {
			app_version = getVersion(context);
		}
	} 
	public static void insert_visit(Context context, String pagenm) {
		if (context != null) {
			initAppVersion(context);
			insertLog(context, pagenm, "visitlog");
		}
	}

	public static void inert_error_log(Context context, String desc) {
		if (context != null) {
			desc = desc.substring(0,
					desc.length() > 3000 ? 2800 : desc.length());
			initAppVersion(context);
			insertLog(context, desc, "errorlog");
		}
	}

	static String getVersion(Context mContext) {
		String version = "";
		try {
			PackageManager manager = mContext.getPackageManager();
			PackageInfo info = manager.getPackageInfo(
					mContext.getPackageName(), 0);
			version = info.versionName;
		} catch (Exception ex) {

		}
		return version;
	}

	static void insertLog(Context context, String desc, String type) {
		String version = app_version;
		String linkway = getLinkWay(context);
		// String linkway="wifi";
		long getTime = new Date().getTime();
		String mid = SPHelper.getBaseMsg(context, "mid", "");
		String dev_version = SPHelper.getBaseMsg(context, "phonebb",
				android.os.Build.VERSION.RELEASE);
		String dev_xh = SPHelper.getBaseMsg(context, "phonexh",
				android.os.Build.MODEL);
		DatabaseHelper db = new DatabaseHelper(context);
		String sql = "insert into apm_sys_logs(mid,timer,version,dev_typ,dev_version,link_web_way,desc,logtyp) values('"
				+ mid
				+ "',"
				+ getTime
				+ ",'"
				+ version
				+ "','"
				+ dev_xh
				+ "','"
				+ dev_version
				+ "','"
				+ linkway
				+ "',?,'"
				+ type
				+ "');";
		SQLiteDatabase dbbase = db.getReadableDatabase();
		dbbase.execSQL(sql, new String[]{desc});
	}

	static List<VisitRecord> getHisRecord(Context context) {
		List<VisitRecord> list = new ArrayList<VisitRecord>();
		DatabaseHelper db = new DatabaseHelper(context);
		SQLiteDatabase dbbase = db.getReadableDatabase();
		String mid = SPHelper.getBaseMsg(context, "mid", "");
		String sql = "select * from apm_sys_logs where mid='" + mid
				+ "' order by timer desc";
		Cursor cusor = dbbase.rawQuery(sql, new String[] {});
		while (cusor.moveToNext()) {
			VisitRecord baseobj = new VisitRecord();
			baseobj.setApp_version(cusor.getString(cusor
					.getColumnIndex("version")));
			baseobj.setDev_typ(cusor.getString(cusor.getColumnIndex("dev_typ")));
			baseobj.setLink_web_way(cusor.getString(cusor
					.getColumnIndex("link_web_way")));
			baseobj.setDev_version(cusor.getString(cusor
					.getColumnIndex("dev_version")));
			baseobj.setMid(mid);
			baseobj.setDesc(cusor.getString(cusor.getColumnIndex("desc")));
			baseobj.setLogtyp(cusor.getString(cusor.getColumnIndex("logtyp")));
			// Date date=new Date(cusor.getLong(cusor.getColumnIndex("timer")));
			baseobj.setTimer(String.valueOf(cusor.getLong(cusor.getColumnIndex("timer"))));
			list.add(baseobj);
		}
		cusor.close();
		return list;
	}

	public static void del_visit(Context context, String mid, long getTime) {
		if (context != null) {
			DatabaseHelper db = new DatabaseHelper(context);
			String sql = "delete from apm_sys_logs where mid='" + mid
					+ "' and timer<=" + getTime + "";
			SQLiteDatabase dbbase = db.getReadableDatabase();
			dbbase.execSQL(sql);
		}
	}

	public static void uploadMData(Context context) {
		List<VisitRecord> records = getHisRecord(context);
		final Context lcontext = context;
		if (records.size() > 0) {
			final String mid = records.get(0).getMid();
			final long jiedian = Long.parseLong(records.get(0).getTimer());
			for (VisitRecord record : records) {
				Long ltimer = Long.parseLong(record.getTimer());
				Date ldate = new Date(ltimer);
				record.setTimer(ldate.toString());
			}
			HashMap<String, Object> maps = new HashMap<String, Object>();
			Gson gson = new Gson();
			String content = gson.toJson(records);
			maps.put("val", content);
			maps.put("typ", "android");
			new BaseAsyncTask(Cons.UPLOAD_MAI_URL, maps, BaseAsyncTask.HttpType.Post, "",
					context) {
				@Override
				public void handler(String param) {
					// TODO Auto-generated method stub
					if (param.contains("status") && param.contains("0")) {
						del_visit(lcontext, mid, jiedian);
					}
				}
			}.execute("");
		}
	}

	public static Dialog createLoadingDialog(Context context, String msg,String gender) {
		return 	 CreateLoading(context, msg,gender,true);
	}
	
	public static Dialog CreateLoading(Context context, String msg,String gender,boolean cancel){
		LayoutInflater inflater = LayoutInflater.from(context);
		View v = inflater.inflate(R.layout.loading_dialog, null);// 得到加载view
		LinearLayout layout = (LinearLayout) v.findViewById(R.id.dialog_view);// 加载布局
		layout.getBackground().setAlpha(00); 
		ImageView spaceshipImage = (ImageView) v.findViewById(R.id.img); 
		spaceshipImage.setImageResource(gender.equals("F") ? R.drawable.redloading: R.drawable.blueloading); 
		Animation hyperspaceJumpAnimation = AnimationUtils.loadAnimation(context, R.anim.loading_animation);// load_animation 
		spaceshipImage.startAnimation(hyperspaceJumpAnimation);
		Dialog loadingDialog = new Dialog(context, R.style.loading_dialog);// 创建自定义样式dialog
		loadingDialog.setCancelable(cancel);// 不可以用“返回键”取消
		loadingDialog.setContentView(layout, new LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT,
				LinearLayout.LayoutParams.FILL_PARENT));// 设置布局
		return loadingDialog;
	}

	private static String[] getLockedArr() {
		return new String[] { "huawei", "ale","mi","hm"};
	}

	public static boolean isLockedPhone() {
		String phone = android.os.Build.MODEL.toLowerCase().replace(" ", "");
		for (String item : getLockedArr()) {
			if (phone.contains(item)) {
				return true;
			}
		}
		return false;
	}

	public static boolean getPermission(String permission, Context context) {
		PackageManager pm = context.getPackageManager();
		return PackageManager.PERMISSION_GRANTED == pm.checkPermission(
				permission, "com.example.renrenstep");
	}

	public static boolean hasAllPermin(List<Permin> pers, Context context) {
		for (Permin item : pers) {
			if (item.getLevel().equals("must")) {
				if (!getPermission(item.getName(), context)) {
					return false;
				}
			}
		}
		return true;
	}
//	public static View getCommAlterView(Context context,String title,String content,final ICommAlterCallback callback){
//		View view = LayoutInflater.from(context).inflate(R.layout.open_app, null);
//		Button bt_finish=(Button)view.findViewById(R.id.bt_finish);
//		TextView tv_content=(TextView)view.findViewById(R.id.tv_content);
//		TextView tv_title=(TextView)view.findViewById(R.id.tv_title);
//		tv_content.setText(content);
//		tv_title.setText(title);
//		tv_title.getPaint().setFakeBoldText(true);
//		bt_finish.setOnClickListener(new OnClickListener() {
//			@Override
//			public void onClick(View v) {
//				// TODO Auto-generated method stub
//				callback.callback();
//			}
//		});
//		bt_finish.setTextColor(SPHelper.getDetailMsg(context, Cons.APP_SEX, "M").equals("F")?0xffff0000:0xff3D98FF);
//		return view;
//	}
	public interface IAleterCallback {
		void aleterCallback();
	}
	public static View getAlterView(Context context,final IAleterCallback callback) {
		View view = LayoutInflater.from(context).inflate(R.layout.activity_hint, null);
		Button btn=(Button)view.findViewById(R.id.bt_next);
		btn.setOnClickListener(new OnClickListener() {
			  @Override
			  public void onClick(View v) {
				 // TODO Auto-generated method stub
				 callback.aleterCallback();
              }
		});
		String sex=SPHelper.getDetailMsg(context, Cons.APP_SEX,"F");
		RelativeLayout layout_actionbar=(RelativeLayout)view.findViewById(R.id.layout_actionbar);
		layout_actionbar.setBackgroundResource(BGHelper.setBackground(context,sex));
		btn.setBackgroundResource(BGHelper.setbgButton(context,sex));
		List<String> forceList = new ArrayList<String>();
		List<String> allowList = new ArrayList<String>();
		ListView lv_force = (ListView) view.findViewById(R.id.lv_force);
		ListView lv_allow = (ListView) view.findViewById(R.id.lv_allow);
		PermissionHelper per = new PermissionHelper(context);
		List<Permin> list = per.getPermin();
		String[] levels = { "must", "middlemust", "notmust" };
		for (String item : levels) {
			List<String> mystrs = getPerStr(list, item,context);
			if (mystrs.size() > 0) {
				if (item.equals("must")
						|| (item.equals("middlemust") && CommHelper
								.isLockedPhone())) {
					forceList.addAll(mystrs);
				} else {
					allowList.addAll(mystrs);
				}
			}
		}
		HintAdapter fAdapter = new HintAdapter(context, forceList,
				HintAdapter.RED);
		lv_force.setAdapter(fAdapter);
		HintAdapter lAdapter = new HintAdapter(context, allowList,
				HintAdapter.GRAY);
		lv_allow.setAdapter(lAdapter);
		return view;
	}

	static List<String> getPerStr(List<Permin> list, String level,Context context) {
		List<String> minlist = new ArrayList<String>();
		for (Permin item : list) {
			if (item.getLevel().equals(level)
					&& !CommHelper.getPermission(item.getName(), context)) {
				minlist.add(item.getDesc());
			}
		}
		return minlist;
	}
	
	public static String getAndroidSerial(Context context){
		return Installation.id(context);
	}
	
	public static long getMaxValue(List<ServiceData> pdatas){
		long total=0;
		for (ServiceData item : pdatas) {
			List<ServiceStepData> stepdatas = item.getSmds();
			for (ServiceStepData pitem : stepdatas) {
				total=pitem.getCollect_time()*1000>total?pitem.getCollect_time()*1000:total;
			}
		}
		return total;
	}
	
	public static String sha256Hex(String data) {
	        return encodeHexString(sha256(data));
	}
    public static String sha256Hex(byte[] data) {
        return encodeHexString(sha256(data));
    }
    
    protected static char[] encodeHex(byte[] data, char[] toDigits) {
        int l = data.length;
        char[] out = new char[l << 1];
        // two characters form the hex value.
        for (int i = 0, j = 0; i < l; i++) {
            out[j++] = toDigits[(0xF0 & data[i]) >>> 4];
            out[j++] = toDigits[0x0F & data[i]];
        }
        return out;
    }
    
    public static byte[] sha256(String data) {
        return sha256(getBytesUtf8(data));
    }
    
    public static byte[] sha256(byte[] data) {
        return getSha256Digest().digest(data);
    }
    
    private static MessageDigest getSha256Digest() {
        return getDigest("SHA-256");
    }
    
    private static byte[] getBytesUtf8(String data) {
        try {
			return data.getBytes(DEFAULT_CHARSET_NAME);
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			 throw new RuntimeException(e.getMessage());
		}
    }
    
    static MessageDigest getDigest(String algorithm) {
        try {
            return MessageDigest.getInstance(algorithm);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e.getMessage());
        }
    }
    
    public static String encodeHexString(byte[] data) {
        return new String(encodeHex(data));
    }
    
    public static char[] encodeHex(byte[] data) {
        return encodeHex(data, true);
    }
    
    static final String DEFAULT_CHARSET_NAME = CharEncoding.UTF_8; 
    /**
     * Used to build output as Hex
     */
    private static final char[] DIGITS_LOWER = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};

    /**
     * Used to build output as Hex
     */
    private static final char[] DIGITS_UPPER = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F'};
    static char[] encodeHex(byte[] data, boolean toLowerCase) {
        return encodeHex(data, toLowerCase ? DIGITS_LOWER : DIGITS_UPPER);
    }
    
//    public static void loadPic(final List<String> picnms,final Context context,final IDownPicHandler callback) {
//		new AsyncTask<String, Void, Integer>() {
//			@Override
//			protected void onPostExecute(Integer result) {
//				if (result == 0&&callback!=null){
//					callback.handler();
//					//madapter.notifyDataSetChanged();
//				}
//			};
//			@Override
//			protected Integer doInBackground(String... params) {
//				try {
//					FileUtils filetools=new FileUtils("stepic");
//					filetools.createSDDir();
//					String basepath=filetools.getFilePath();
//					for (String item : picnms) {
//						if (item.equals("")) {
//							continue;
//						}
//						File file=new File(basepath+item);
//						if(file.exists()){
//							continue;
//						}
//						Bitmap bitmap = loadPic(item,context);
//						if (bitmap != null) {
//							savePic(bitmap, item);
//						}
//					}
//
//				} catch (Exception ex) {
//					return 1;
//				}
//				return 0;
//			}
//		}.execute("");
//	}
	
//	private static Bitmap loadPic(String picnm,Context context) {
//		try {
//			String mtoken = SPHelper.getBaseMsg(context,"mtoken", "mtoken");
//			Map<String, String> maps = new HashMap<String, String>();
//			maps.put("mtoken", mtoken);
//			Bitmap bitmaps = HttpHelper.donwload(Cons.DONW_PIC + picnm,context);
//			return bitmaps;
//		} catch (Exception ex) {
//			return null;
//		}
//	}
//
//	private static	void savePic(Bitmap bitmaps, String name) {
//		FileUtils utils = new FileUtils("stepic");
//		utils.createSDDir();
//		utils.saveMyBitmap(name, bitmaps);
//	}
//
	
//	public static Map<String,Map<String,String>> publicDic(){
//		Map<String,Map<String,String>>  maps=new HashMap<String, Map<String,String>>();
//		Map<String,String> map = new HashMap<String, String>();
//		map.put("nc","新朋友");
//		map.put("avatar", "1512011556547174908.png");
//		maps.put("174908", map);
//		return maps;
//	}
//
	public static String getGroupConversationTitle(String title,int pnum){
		if(!title.equals("")){
			return title;
		}
		return MessageFormat.format(MyApplication.getInstance().getResources().getString(R.string.group_default_nm),pnum+"");
	}
//
//	public static void setListViewHeightBasedOnChildren(ListView listView) {
//		ListAdapter listAdapter = listView.getAdapter();
//		if (listAdapter == null) {
//			// pre-condition
//			return;
//		}
//
//		int totalHeight = 0;
//		for (int i = 0; i < listAdapter.getCount(); i++) {
//			View listItem = listAdapter.getView(i, null, listView);
//			listItem.measure(0, 0);
//			totalHeight += listItem.getMeasuredHeight();
//		}
//
//		ViewGroup.LayoutParams params = listView.getLayoutParams();
//		params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
//		listView.setLayoutParams(params);
//	}
	
}
