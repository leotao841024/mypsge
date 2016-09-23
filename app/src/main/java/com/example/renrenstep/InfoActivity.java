package com.example.renrenstep;
import groupview.CommunicationViewBuilder;
import groupview.Email1ViewHolder;
import groupview.Email2ViewHolder;
import groupview.GenderInfiViewBuilder;
import groupview.MemInfoViewBuilder;
import groupview.InfoViewBuilder.EventListener;
import groupview.NameViewBuilder;
import groupview.PassWordViewHolder;
import groupview.Phone1ViewHolder;
import groupview.Phone2ViewHolder;
import helper.BaseAsyncTask;
import helper.HttpTool.*;
import helper.HttpTool;
import helper.HttpTool.imageUploadCallback;
import helper.SPHelper; 

import java.io.File;
import java.util.*;

import tools.ToastManager;

import org.json.JSONException;
import org.json.JSONObject;   

import receiver.NotifyReceiver;
import tools.AndroidTool;
import tools.FileUtils;
import tools.ImageTool;
import manager.MyLog;
import tools.PhotoTool;
import tools.TimeFormat;
import view.CircleImageView; 
import adapter.InfoAdapter;
import adapter.InfoAdapter.InfoCallback;
import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.*;
import android.graphics.*; 
import android.net.Uri; 
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;   
import android.widget.*;  
import comm.CommHelper;
import constant.Cons;
@SuppressLint("ResourceAsColor")
public class InfoActivity extends MyBaseActivity { 
	private RelativeLayout layout_actionbar;  
	private ListView lv_info, lv_bind;
	private InfoAdapter infoAdapter, bindAdapter; 
	private List<String> infomation,bindInfomation;
	private Dialog dialogwin;  
	private CircleImageView iv_pic;
	private CommunicationViewBuilder commViewHolder;
	private int x,y;
	private NotifyReceiver notifyReceiver = new NotifyReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) { 
			String action = intent.getAction();
			String res = intent.getStringExtra("pic");
			Message msg = new Message();
			if (action.equals(Cons.NORIFY_RECEIVER)&& res.equals("confirm")) {
				msg.what = -1;
			} else if (res.equals("cancel")) {
				msg.what = -2;
			}
			handler.sendMessage(msg);
		}
	};
	private Handler handler = new Handler() {
		@SuppressLint("ResourceAsColor")
		public void handleMessage(Message msg) {
			switch (msg.what) { 
			case -1:// 更新头像
				loadPicture();
				cancelDialog();
				break;
			case -2:// 更新头像
				cancelDialog();
				break; 
			default:
				break;
			}
		}
	}; 
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE); 
		setContentView(R.layout.activity_info);  
		IntentFilter intentFilter = new IntentFilter(Cons.NORIFY_RECEIVER);
		registerReceiver(notifyReceiver, intentFilter);
		initView();  
		initData(); 
		initColor();
		CommHelper.insert_visit(this, "profilepg");
	}
	private void initData() { 
		bindInfomation =new ArrayList<String>();
		infomation = new ArrayList<String>();
		initInfoItems();
		initBinditems();
	}
	void initInfoItems(){
		List<String> names = new ArrayList<String>();
		names.add(getResources().getString(R.string.info_item1));
		names.add(getResources().getString(R.string.info_item2));
		names.add(getResources().getString(R.string.info_item3));
		names.add(getResources().getString(R.string.info_item4));
		names.add(getResources().getString(R.string.info_item5)); 
		infoAdapter=new InfoAdapter(this);
		infoAdapter.setNames(names);
		logicInfo(); 
		infoAdapter.setInfomations(infomation);
		InfoCallback[] callbacks=new InfoCallback[]{ncCallback,genderCallback,ageCallback,heightCallback,weightCallback};
		infoAdapter.setListener(callbacks);
		lv_info.setAdapter(infoAdapter);
	}
	void initBinditems(){
		List<String> bindNames = new ArrayList<String>();
		bindNames.add(getResources().getString(R.string.phone));
		bindNames.add(getResources().getString(R.string.bind_item_email));
		bindNames.add(getResources().getString(R.string.mima));
		logicBind();
		bindAdapter=new InfoAdapter(this);
		bindAdapter.setNames(bindNames);
		bindAdapter.setInfomations(bindInfomation);
		InfoCallback[] callbacks=new InfoCallback[]{phoneCallback,emailCallback,pwdCallback};
		bindAdapter.setListener(callbacks);
		lv_bind.setAdapter(bindAdapter);
	}
	private void logicBind() {
		bindInfomation.clear();
		String mob = SPHelper.getDetailMsg(this, "mobile", "");
		String emailno = SPHelper.getDetailMsg(this, "email", "");
		int mobindid= mob.equals("null") || mob.equals("") ? R.string.nobind : R.string.hasbind;
		int emailbindid=emailno.equals("null") || emailno.equals("") ?R.string.nobind:R.string.hasbind;
		bindInfomation.add(getResources().getString(mobindid));
		bindInfomation.add(getResources().getString(emailbindid));
		bindInfomation.add("");
	}
	InfoCallback ncCallback=new InfoCallback(){
		@Override
		public void handler(){
			NameViewBuilder name=new NameViewBuilder(InfoActivity.this,new MemListner("nc"));
			name.setView();
			name.setColor(getSysColor());
			name.setSource(SPHelper.getDetailMsg(InfoActivity.this, "nc", ""));
			int statusBarH = AndroidTool.getStatusBarHeight(InfoActivity.this);
			int screenH = AndroidTool.getScreenHeight(InfoActivity.this);
			name.popup(LayoutParams.MATCH_PARENT,screenH-statusBarH, Gravity.TOP, 0,statusBarH);

		}
	}; 
	InfoCallback genderCallback=new InfoCallback(){
		@Override
		public void handler() {
			GenderInfiViewBuilder gender=new GenderInfiViewBuilder(InfoActivity.this,new MemListner("gender"));
			gender.setView();
			gender.setColor(getSysColor());
			gender.setResource(getResources().getString(R.string.man), getResources().getString(R.string.woman));
			gender.setResult(new String[]{"M","F"});
			gender.popup(LayoutParams.MATCH_PARENT,LayoutParams.WRAP_CONTENT,Gravity.BOTTOM, 0,0);
		}
	};
	InfoCallback ageCallback=new InfoCallback(){
		@Override
		public void handler() {
			initMemData("age",getSelRange(14,100),SPHelper.getDetailMsg(InfoActivity.this, "age", 25)+"",getResources().getString(R.string.picker_age));
		}
	};
	InfoCallback heightCallback=new InfoCallback(){
		@Override
		public void handler() {
			initMemData("height",getSelRange(130,230),SPHelper.getDetailMsg(InfoActivity.this, "height", 170)+"",getResources().getString(R.string.picker_height));
		}
	};
	InfoCallback weightCallback=new InfoCallback(){
		@Override
		public void handler() {
			initMemData("weight",getSelRange(20,150),SPHelper.getDetailMsg(InfoActivity.this, "weight", 70)+"",getResources().getString(R.string.picker_weight));
		}
	};
	InfoCallback phoneCallback=new InfoCallback(){
		@Override
		public void handler() { 
			initPhone();
		}
	};
	InfoCallback emailCallback=new InfoCallback(){
		@Override
		public void handler() { 
			initEmail();
		}
	};
	private PassWordViewHolder passWord;
	InfoCallback pwdCallback=new InfoCallback(){
		@Override
		public void handler() {
			 passWord= new PassWordViewHolder(InfoActivity.this,new EventListener<String>() { 
				@Override
				public void onConfirm(String result) {
					SPHelper.setBaseMsg(InfoActivity.this, "pwd", result);
				}
				@Override
				public void onCancel() {
					passWord = null;
				}
			});
			passWord.setView();
			passWord.setColor(getSysColor());
			passWord.popup();
		}
	};
	void initEmail(){
		String email=SPHelper.getDetailMsg(InfoActivity.this, "email", ""); 
		if(email.equals("")){
			initEmail2();
		}else{
			 initEmail1(email);
		} 
	}
	void initEmail1(String pemail){
		commViewHolder=new Email1ViewHolder(InfoActivity.this, new EventListener<String>() { 
			@Override
			public void onConfirm(String result) {
				initEmail2();
			}
			@Override
			public void onCancel() {
				commViewHolder=null;
			} 
		});
		popSlidingMenu(commViewHolder,getResources().getString(R.string.regist_user),"", pemail);
	}
	void initEmail2(){
		commViewHolder=new Email2ViewHolder(InfoActivity.this, new EventListener<String>() { 
			@Override
			public void onConfirm(String result) {
				saveResult("email", result);
			}
			@Override
			public void onCancel() {
				commViewHolder = null;
			} 
		});
		popSlidingMenu(commViewHolder,getResources().getString(R.string.regist_user), getResources().getString(R.string.new_email) , "");
	}
	void initPhone(){
		String phone=SPHelper.getDetailMsg(InfoActivity.this, "mobile", ""); 
		if(phone.equals("")){
			initPhone2();
		}else{
			 initPhone1(phone);
		}
	}
	void initPhone1(String phone){
		commViewHolder=new Phone1ViewHolder(InfoActivity.this, new EventListener<String>() { 
			@Override
			public void onConfirm(String result) {
				initPhone2();
			}
			@Override
			public void onCancel() {
				commViewHolder=null;
			} 
		}); 
		popSlidingMenu(commViewHolder,getResources().getString(R.string.bind_phone),"", phone);
	}
	void initPhone2(){
		commViewHolder=new Phone2ViewHolder(InfoActivity.this, new EventListener<String>() { 
			@Override
			public void onConfirm(String result) {
				saveResult("phone", result);
			}
			@Override
			public void onCancel() {
				commViewHolder=null;
			} 
		});
		popSlidingMenu(commViewHolder,getResources().getString(R.string.yanzheng_phone), getResources().getString(R.string.yanzheng_phone), "");
	}
	void popSlidingMenu(CommunicationViewBuilder commView,String title,String bghit,String val){
		commView.setView();
		commView.setPageDesc(title,bghit,val);
		commView.setColor(getSysColor());
		commView.popup();
	}
	private void logicInfo() {
		infomation.clear();
		String mname = SPHelper.getDetailMsg(this, "nc",getResources().getString(R.string.nosetting));
		infomation.add(mname);
		String sex = SPHelper.getDetailMsg(this, "gender",getString(R.string.appsex_man));
		String gender = sex.equals("M") ? getResources().getString(R.string.man) : getResources().getString(R.string.woman);
		String mAge = SPHelper.getDetailMsg(this, "age", 25) + "";
		String mHeight = SPHelper.getDetailMsg(this, "height", 170) + "";
		String mweight = SPHelper.getDetailMsg(this, "weight", 65) + "";
		infomation.add(gender);
		infomation.add(mAge);
		infomation.add(mHeight+ getResources().getString(R.string.picker_height));
		infomation.add(mweight+ getResources().getString(R.string.picker_weight));
	}
	void take_photo(Intent data){
		String cUri="";
		Intent photo = new Intent(this, PhotoActivity.class);
		if (data != null&&data.getData()!=null) { // 可能尚未指定intent.putExtra(MediaStore.EXTRA_OUTPUT,  
			cUri = data.getData().toString(); 
		}
		if(!cUri.equals("")){
			photo.putExtra("uri", cUri);
			startActivity(photo);
		}else{
			ToastManager.show(this, getResources().getString(R.string.filemiss), 1000);
		}
		cancelDialog();
	} 
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) { 
		if(data==null||resultCode != RESULT_OK){ 
			return;
		}
		showDialog();
		if(requestCode==PhotoTool.TAKE_PHOTO){
			take_photo(data);
		}else if(requestCode==PhotoTool.OPEN_PHOTOLIST){
			String pUri = data.getData().toString();
			String path = FileUtils.getRealPathFromURI(Uri.parse(pUri), this);
			if(!PhotoTool.isvalidPic(path)){
				cancelDialog();
				ToastManager.show(InfoActivity.this, getResources().getString(R.string.errorpic), 2000);
				return;
			}
			File file = new File(path);
			if(!file.exists()){
				cancelDialog();
				ToastManager.show(InfoActivity.this, getResources().getString(R.string.filemiss), 2000);
				return;
			}
			upLoadImage(path);
		}else{
			cancelDialog();
		} 
	}
	void upLoadImage(String pfilepath){
		final String filenm=TimeFormat.DateToString(new Date(), "yyyyMMddHHmmss")+".jpg";
		Map<String,String> maps=new HashMap<String,String>();
		maps.put("mtoken", SPHelper.getBaseMsg(InfoActivity.this, "mtoken", "mtoken"));
		HttpTool.uploadLocalImage(pfilepath,maps,new imageUploadCallback(){
			@Override
			public String handler(String filepath) {
				int angle =ImageTool.getBitmapDegree(filepath); 
				FileUtils filetool = new FileUtils("stepic");
				filetool.createSDDir(); 
				Bitmap mp = ImageTool.changeImage(ImageTool.compressImage(filepath, 2),angle);
				filetool.saveMyBitmap(filenm, mp);
				return filetool.getFilePath()+filenm;
			}
			@Override
			public void excute(String result) {
				if(!result.equals("")){
					JSONObject jsonObject;
					try {
						jsonObject = new JSONObject(result);
						int res = jsonObject.getInt("status");
						if (res == 0) {
							String newfilenm = jsonObject.getString("filename");
							FileUtils filetool = new FileUtils("stepic");
							FileUtils.renameFile(filetool.getFilePath(),filenm, newfilenm);
							updateUserPic(filenm);
							sendUsermsgChangeMsg(CommHelper.getCompleteStr(CommHelper.changeMemMsg));
							SPHelper.setDetailMsg(InfoActivity.this,"uri",filetool.getFilePath()+newfilenm); 
							loadPicture();
						}
					}
					catch (JSONException e) {
						MyLog.e("photolist", e.getMessage());	 
					}
					cancelDialog();
				}
			}
		});
	} 
	void updateUserPic(String picnm){ 
		ContentValues values=new ContentValues();
		values.put("acvtor", picnm);
		db.open();
		db.update("apm_sys_friend",values, " mid=?", new String[]{SPHelper.getBaseMsg(InfoActivity.this, "mid", "0")});
		db.close();
	} 
	void cancelDialog() {
		if (dialogwin != null) {
			dialogwin.dismiss();
		}
	}  
	void showDialog() {
		dialogwin = CommHelper.createLoadingDialog(InfoActivity.this, "",SPHelper.getDetailMsg(InfoActivity.this, Cons.APP_SEX, "M"));
		dialogwin.show();
	} 
	private void initView() {
		iv_pic = (CircleImageView) findViewById(R.id.iv_pic); 
		loadPicture();
		((LinearLayout) findViewById(R.id.layout_head)).setOnClickListener(new OnClickListener() {// 更换头像
			@Override
			public void onClick(View v) { 
				GenderInfiViewBuilder gender=new GenderInfiViewBuilder(InfoActivity.this,new PhotoListner());
				gender.setView();
				gender.setColor(getSysColor());
				gender.setResource(getResources().getString(R.string.pop_head_item0), getResources().getString(R.string.pop_head_item1)); 
				gender.setResult(new String[]{PhotoTool.TAKE_PHOTO+"",PhotoTool.OPEN_PHOTOLIST+""});
				gender.popup(LayoutParams.MATCH_PARENT,LayoutParams.WRAP_CONTENT,Gravity.BOTTOM, 0, 0);
			}
		});
		layout_actionbar = (RelativeLayout) findViewById(R.id.layout_actionbar);
		((LinearLayout)findViewById(R.id.ll_exit)).setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				InfoActivity.this.finish();	
			}
		}); 
		lv_info = (ListView) findViewById(R.id.lv_info);
		lv_bind = (ListView) findViewById(R.id.lv_bind); 
	} 
	private void loadPicture() { 
		try {
			String uri = SPHelper.getDetailMsg(this, "uri", "");
			Bitmap bitmap = BitmapFactory.decodeFile(uri);
			if (bitmap != null) {
				iv_pic.setImageBitmap(bitmap);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	};
	public void initColor() {  
		layout_actionbar.setBackgroundColor(getSysColor());  
	}
	List<String> getSelRange(int begin,int end){
		List<String> datas = new ArrayList<String>();
		for (int i = begin; i <= end; i++) {
			datas.add(String.valueOf(i));
		}
		return datas;
	}
	void initMemData(String typ,List<String> source,String setVal,String until){
		MemInfoViewBuilder mem=new MemInfoViewBuilder(InfoActivity.this,new MemListner(typ));
		mem.setView();
		mem.setColor(getSysColor());
		mem.setDataSource(source);
		mem.setSelectedVal(setVal);
		mem.setUnit(until);
		mem.popup(LayoutParams.MATCH_PARENT,LayoutParams.WRAP_CONTENT,Gravity.BOTTOM, 0, 0);
	}
	class PhotoListner implements EventListener<String>{ 
		@Override
		public void onConfirm(String result) {
			Intent intent=null;
			if(result.equals(PhotoTool.TAKE_PHOTO+"")){
				intent=PhotoTool.takePhotoIntent(InfoActivity.this);
			}else if(result.equals(PhotoTool.OPEN_PHOTOLIST+"")){
				intent=PhotoTool.openPhotoListIntent();
			}
			int code =Integer.parseInt(result);
			if(intent!=null)
				startActivityForResult(intent,code);
		}
		@Override
		public void onCancel() { 
		}
	}
	
	class MemListner implements EventListener<String>{ 
		private String typ="";
		public MemListner(String typ) {
			this.typ = typ; 
		}
		@Override
		public void onConfirm(String result) {
			sendMsgToServer(typ,result);
		}
		@Override
		public void onCancel() {	
		}
	}
	void sendMsgToServer(final String key,final Object val){
		Map<String, Object> maps = new HashMap<String, Object>();
		maps.put(key, val);
		showDialog();

//		HttpTool.HttpRequestAsy(Cons.UPDATE_INFO, maps, BaseAsyncTask.HttpType.Post, "", InfoActivity.this,new IHttpCallBackAsy() {
//			@Override
//			public void success(String param) {
//				cancelDialog();
//				saveResult(key,val);
//			}
//			@Override
//			public void failed(String pmsg) {
//				cancelDialog();
//				ToastManager.show(InfoActivity.this,pmsg,2000);
//			}
//			@Override
//			public void exception(String pmsg) {
//				cancelDialog();
//				ToastManager.show(InfoActivity.this,getResources().getString(R.string.wangluoyic),2000);
//			}
//		});
	}
	interface InfoHandler{
		void handler(String key, Object value);
	}
	class StringHanlder implements InfoHandler{ 
		@Override
		public void handler(String key, Object value) {
			SPHelper.setDetailMsg(InfoActivity.this, key,(String)value);
			sourceChange();
		} 
	}
	class IntegerHanlder implements InfoHandler{ 
		@Override
		public void handler(String key, Object value) { 
			SPHelper.setDetailMsg(InfoActivity.this, key,Integer.parseInt(value.toString()));
			sourceChange();
		} 
	}
	void sourceChange(){
		initColor();
		logicInfo(); 
		logicBind();
		infoAdapter.notifyDataSetChanged();
		bindAdapter.notifyDataSetChanged(); 
	}
	
	void saveResult(String key, Object val){
		InfoHandler infohandler= getMaps().get(key);
		if(infohandler!=null){
			infohandler.handler(key, val);
		}
	}
	Map<String,InfoHandler> getMaps(){
		Map<String,InfoHandler> maps=new HashMap<String,InfoHandler>(); 
		maps.put("nc",new StringHanlder());
		maps.put("height",new IntegerHanlder());
		maps.put("weight",new IntegerHanlder());
		maps.put("gender",new StringHanlder());
		maps.put("age",new IntegerHanlder());
		maps.put("phone",new StringHanlder());
		maps.put("email",new StringHanlder());
		maps.put("pwd",new StringHanlder());
		return maps;
	}
	int getSysColor(){
		return SPHelper.getDetailMsg(InfoActivity.this, "gender", "M").equals("M")?0xff3D98FF:0xffff0000;
	}
	@Override
	protected void onDestroy() {  
		unregisterReceiver(notifyReceiver);
		super.onDestroy();
	}
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if(keyCode==KeyEvent.KEYCODE_BACK){
			if(commViewHolder!=null){
				commViewHolder.hidden();
				commViewHolder = null;
			}
			else if(passWord!=null){
				passWord.hidden();
				passWord= null;
			}
		}
		return true;
	}

}
