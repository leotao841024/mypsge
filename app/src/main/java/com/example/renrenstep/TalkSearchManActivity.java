package com.example.renrenstep; 
import comm.HttpManager;
import helper.BGHelper;
import helper.FileHttp;
import helper.HttpCallback;
import helper.HttpHelper;
import helper.SPHelper;    

import java.io.File;
import java.io.InputStream;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import manager.ImageCacheManger;
import tools.ImageTool;
import tools.ToastManager;

import org.json.JSONException;
import org.json.JSONObject;  

import tools.FileUtils;

import com.google.gson.Gson;

import helper.BaseAsyncTask;
import comm.CommHelper;
import bean.Customer;
import bean.Customers;
import constant.Cons;
import adapter.CusAdapter; 
import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.TextView;

public class TalkSearchManActivity extends Activity implements OnClickListener {
	private ListView mile_list;
	private List<Customer> mlist;
	private CusAdapter madapter;
	private RelativeLayout layouter;
	private int currentPage=1;
	private int pagesize=20;
	private EditText txt_search;
	private Dialog dialoging;
	private String gender="";
	private TextView isempty; 
	private int visibleLastIndex = 0; 
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_talk_search_man);
		initView();
		initData();
		initEvent();
		initColor();
	}
	
	void initView() {
		layouter = (RelativeLayout) findViewById(R.id.layouter);
		mile_list = (ListView) findViewById(R.id.mile_list);
		txt_search= (EditText) findViewById(R.id.txt_search);
		isempty =(TextView)findViewById(R.id.isempty);
	}

	void initData() {
		madapter = new CusAdapter(this);
		mlist = new ArrayList<Customer>();
		madapter.setSource(mlist);
		mile_list.setAdapter(madapter);
		gender=SPHelper.getDetailMsg(this, Cons.APP_SEX,getString(R.string.appsex_man));
	}
	
	void initEvent() {
		((LinearLayout) findViewById(R.id.covert_left_dao)).setOnClickListener(this);
		((LinearLayout) findViewById(R.id.newfriendsearch)).setOnClickListener(this);
		mile_list.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
									int position, long id) {
				Intent intentitem = new Intent(TalkSearchManActivity.this, AddFriendActivity.class);
				intentitem.putExtra("key", mlist.get(position));
				TalkSearchManActivity.this.startActivity(intentitem);
			}
		});
		mile_list.setOnScrollListener(new OnScrollListener() {
			@Override
			public void onScrollStateChanged(AbsListView arg0, int arg1) {
				// TODO Auto-generated method stub
				int itemsLastIndex = mlist.size() - 1;
				int lastIndex = itemsLastIndex;
				if (arg1 == OnScrollListener.SCROLL_STATE_IDLE && visibleLastIndex == lastIndex) {
					currentPage++;
					searchEvent();
				}
			}

			@Override
			public void onScroll(AbsListView arg0, int arg1, int arg2, int arg3) {
				// TODO Auto-generated method stub  
				visibleLastIndex = arg1 + arg2 - 1;
			}
		});
	}

	
	void initColor() {
		layouter.setBackgroundResource(BGHelper.setBackground(this, SPHelper.getDetailMsg(this, Cons.APP_SEX, getString(R.string.appsex_man))));
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.covert_left_dao:
			finish();
			break;
		case R.id.newfriendsearch:
			mlist.clear();
			searchEvent();
			break;
		default:
			break;
		}
	}
	
	void searchEvent(){
		dialoging=CommHelper.createLoadingDialog(this, "", gender); 
		dialoging.show();
		String cont=txt_search.getText().toString().trim();
		if(cont.equals("")){
			ToastManager.show(this, getResources().getString(R.string.searchisnull),2000);
			dialoging.dismiss();
			return;
		}
		seachPerson(cont);
	}
	
	void seachPerson(String key) { 
		Map<String,Object> maps=new HashMap<String, Object>();
		maps.put("key",URLEncoder.encode(key.trim()));
		maps.put("page", currentPage + "");
		maps.put("pagesize", pagesize+"");
		new BaseAsyncTask(Cons.SERACHMAN, maps, BaseAsyncTask.HttpType.Get, "", this) {
			@Override
			public void handler(String param) {
				dialoging.dismiss();
				JSONObject jsonobj;
				if(param==null||param.equals("")||!param.contains("status")){
					ToastManager.show(TalkSearchManActivity.this, getResources().getString(R.string.wangluoyic), 2000);
					return;
				}
				try {
					jsonobj = new JSONObject(param);
					if(jsonobj.getInt("status")==0){
						Customers res=new Customers();
						Gson gson=new Gson();
						res = gson.fromJson(param, res.getClass());
						List<Customer> pitems=res.getItems();
						List<String> picnms=new ArrayList<String>();
						if(pitems.size()==0){mile_list.setVisibility(View.GONE);isempty.setVisibility(View.VISIBLE);return;}
						for(Customer item:pitems){
							mlist.add(item);
							picnms.add(item.getAvatar());
						}
						madapter.notifyDataSetChanged();
						loadPic(picnms);
					}
				} catch (JSONException e) {
					ToastManager.show(TalkSearchManActivity.this, getResources().getString(R.string.wangluoyic), 2000);
				} 
			}
		}.execute("");
	}

	void loadPic(final List<String> picnms) {
		for (String item : picnms) {
			new ImageCacheManger("stepic").downData(Cons.DONW_PIC + item, new ImageCacheManger.IdownImageCallback() {
				@Override
				public void finished(String path) {
					madapter.notifyDataSetChanged();
				}
			});
		}
	}

}
