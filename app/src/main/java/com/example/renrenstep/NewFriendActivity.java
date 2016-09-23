package com.example.renrenstep;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import tools.ToastManager;

import org.json.JSONObject;

import com.alibaba.wukong.Callback;
import com.alibaba.wukong.im.Conversation;
import com.alibaba.wukong.im.ConversationService;
import com.alibaba.wukong.im.IMEngine;
import com.alibaba.wukong.im.Message;
import com.alibaba.wukong.im.MessageBuilder;
import com.alibaba.wukong.im.Conversation.ConversationType;

import helper.BaseAsyncTask;
import comm.CommHelper;
import bean.Customer;
import helper.BGHelper;
import helper.SPHelper;
import constant.Cons;
import adapter.AcceptAdapter;
import android.app.Dialog;
import android.content.ContentValues;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Toast;

public class NewFriendActivity extends MyBaseActivity implements OnClickListener {
	private ListView mile_list;
	private RelativeLayout layouter;
	private List<Customer> mlist;
	private AcceptAdapter adapter;
	private Conversation conversation; 
	private String gender="";
	private Dialog dialoging;
	private String mid="";
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_new_friend);
		initView();
		initData();
		initEvent();
		initColor();
		initNewFriendsList();
	}
	
	void initData(){
		mid=SPHelper.getBaseMsg(NewFriendActivity.this, "mid", "0");
		mlist = new ArrayList<Customer>();
		adapter = new AcceptAdapter(this,new AddFriendEvent()); 
		adapter.setList(mlist);
		mile_list.setAdapter(adapter);
		unReadReset(getIntent().getStringExtra("commid"));
		gender=SPHelper.getDetailMsg(this, Cons.APP_SEX,getString(R.string.appsex_man));
		//db=new DBFriendHelper(this,new DatabaseHelper(this));
	}
	
	void initView(){
		mile_list=(ListView)findViewById(R.id.mile_list);
		layouter=(RelativeLayout)findViewById(R.id.layouter);
	}
	
	void initColor(){
		layouter.setBackgroundResource(BGHelper.setBackground(this, gender));
	} 
	void initEvent(){
		((LinearLayout)findViewById(R.id.covert_left_dao)).setOnClickListener(this); 
	}
	
	@Override
	public void onClick(View v) { 
		switch (v.getId()) {
		case R.id.covert_left_dao:
			finish();
			break; 
		default:
			break;
		}
	}
	
	void initNewFriendsList(){ 
		//String mid=SPHelper.getBaseMsg(NewFriendActivity.this, "mid", "0");
		String sql="select * from apm_sys_friend where idcd='"+mid+"' and typ='new'";
		db.open();
		Cursor cur=db.query(sql);
		while(cur.moveToNext()){
			Customer cus=new Customer();
			cus.setId(cur.getInt(cur.getColumnIndex("mid")));
			cus.setNc(cur.getString(cur.getColumnIndex("nc")));
			cus.setAvatar(cur.getString(cur.getColumnIndex("acvtor")));
			cus.setState(cur.getString(cur.getColumnIndex("state")));
			cus.setWords(cur.getString(cur.getColumnIndex("words")));
			mlist.add(cus);
		}
		cur.close();
		db.close();
		adapter.notifyDataSetChanged();
	}
	
	class AddFriendEvent implements AcceptAdapter.IAddFriendEvent{
		@Override
		public void handler(int pmid) {
			agreeApply(pmid);
		}
	}
	Customer getCus(int pid){
		for(Customer item:mlist){
			if(item.getId()==pid){
				return item;
			}
		}
		return null;
	}
	private int sssdpmid;
	private  Customer cus;
	void agreeApply(int pmid){
		dialoging=CommHelper.createLoadingDialog(this, "", gender); 
		dialoging.show();
		sssdpmid = pmid;
		Map<String,Object> maps=new HashMap<String, Object>();
		maps.put("id", pmid);
		cus= getCus(pmid);
		new BaseAsyncTask(Cons.AGREEAPPLY,maps, BaseAsyncTask.HttpType.Post,"",NewFriendActivity.this) {
			@Override
			public void handler(String param) {
				try
				{
					dialoging.dismiss();
					if(param==null||param.equals("")||!param.contains("status")){
						ToastManager.show(NewFriendActivity.this, getResources().getString(R.string.wangluoyic),2000);
						return;
					}
					JSONObject json=new JSONObject(param);
					int res=json.getInt("status");
					if(res==0){
						delListData(sssdpmid);
						delStore("AGREE",sssdpmid);
						insertCus(cus);
						adapter.notifyDataSetChanged();
						creareConversation(sssdpmid);
					}else if(res==1){
						ToastManager.show(NewFriendActivity.this, getResources().getString(R.string.adderror), 2000);
					}else if(res==2){
						ToastManager.show(NewFriendActivity.this, getResources().getString(R.string.isfriend), 2000);
					}
				}
				catch(Exception ex){
					ToastManager.show(NewFriendActivity.this, getResources().getString(R.string.wangluoyic),2000);
				}
			}
		}.execute("");
	}
	
	String filterStr(String pitem) {
		if (pitem == null) {
			return pitem;
		}
		return pitem;
	}
	
	void insertCus(Customer item){
		ContentValues contents= new ContentValues();  
			contents.put("mid", item.getId());
			contents.put("nc", filterStr(item.getNc()));
			contents.put("email",filterStr(item.getEmail()));
			contents.put("address", filterStr(item.getCity_alia()));
			contents.put("acvtor", filterStr(item.getAvatar()));
			contents.put("state", filterStr(item.getState()));
			contents.put("words", filterStr(item.getWords()));//remark,idcd,typ
			contents.put("remark", filterStr(item.getRemark()));
			contents.put("idcd", mid);
			contents.put("typ", "old");
			db.open();
			db.insert("apm_sys_friend", contents);
			db.close();
	}
	
	void delListData(int pid){
		int index=0;
		for(Customer item:mlist){
			if(item.getId()==pid){
				item.setState("AGREE");
			}
		}
	}
	
	void delStore(String pstate,int pid){
		String sql="update apm_sys_friend set state=? where mid=? and idcd=?";
		ContentValues values=new ContentValues();
		values.put("state",pstate);
		db.open();
		db.update("apm_sys_friend", values, " mid=? and idcd=?", new String[]{pid+"",mid});
		db.close();
	}
	
	void creareConversation(int pid) {
		Message alikongmsg = IMEngine.getIMService(MessageBuilder.class).buildTextMessage("helloworld"); // 系统消息
		IMEngine.getIMService(ConversationService.class).createConversation(
				new Callback<Conversation>() {
					@Override
					public void onSuccess(Conversation arg0) {
						// TODO Auto-generated method stub
						//Toast.makeText(NewFriendActivity.this,"成功1:" + arg0.conversationId(), 2000).show();
						conversation = arg0;
						sendMsgToFriend();
					}
					@Override
					public void onProgress(Conversation arg0, int arg1) {
						// TODO Auto-generated method stub
					}
					@Override
					public void onException(String arg0, String arg1) {
						// TODO Auto-generated method stub
					}
		}, Cons.ADDSINGAL, "", alikongmsg, ConversationType.CHAT,(long) pid);
	}
	
	void sendMsgToFriend() {
		Message message = IMEngine.getIMService(MessageBuilder.class).buildTextMessage(CommHelper.getCompleteStr(CommHelper.agreeMsg)+getResources().getString(R.string.helloworld));
		message.sendTo(conversation, new Callback<Message>() {
			@Override
			public void onSuccess(Message arg0) { 
				//Toast.makeText(NewFriendActivity.this, getResources().getString(R.string.success), 2000).show();
			}
			@Override
			public void onProgress(Message arg0, int arg1) { // TODO
				
			} 
			@Override
			public void onException(String arg0, String arg1) {
				Toast.makeText(NewFriendActivity.this, getResources().getString(R.string.senderror), 2000).show();
			}
		});
	}
	void unReadReset(String pconversationid){
		IMEngine.getIMService(ConversationService.class).getConversation(new Callback<Conversation>() { 
			@Override
			public void onSuccess(Conversation arg0) {
				// TODO Auto-generated method stub
				 arg0.resetUnreadCount();
			}
			
			@Override
			public void onProgress(Conversation arg0, int arg1) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onException(String arg0, String arg1) {
				// TODO Auto-generated method stub
			}
		}, pconversationid); 
	}
	@Override
	protected void onDestroy() {  
		super.onDestroy();
	}
	
}
