package com.example.renrenstep;

import android.app.Dialog;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.wukong.Callback;
import com.alibaba.wukong.im.Conversation;
import com.alibaba.wukong.im.ConversationService;
import com.alibaba.wukong.im.IMEngine;
import com.alibaba.wukong.im.Message;
import com.alibaba.wukong.im.MessageBuilder;

import java.util.ArrayList;
import java.util.List;

import adapter.MailAdapter;
import bean.Customer;
import comm.CommHelper;
import helper.BGHelper;
import helper.ResourceHelper;
import helper.SPHelper;
import tools.ToastManager;

public class GrouMemChoseActivity extends MyBaseActivity implements OnClickListener {
	private ListView mailist;
	private List<Customer> maillist;
	private MailAdapter mailAdapter; 
	private String userid;
	private String gender="";
	private Dialog loading_dialog;
	private ConversationService conversationService;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.group_mem_select);
		initView();
		initData();
		initEvent();
		initColor();
	}
	
	void initView(){
		mailist=((ListView)findViewById(R.id.mlist));
	}
	
	void initData() {
		gender = SPHelper.getDetailMsg(this, "gender", "M");
		conversationService=IMEngine.getIMService(ConversationService.class);
		userid=SPHelper.getBaseMsg(this, "mid", "0");
		maillist = new ArrayList<Customer>(); 
		mailAdapter = new MailAdapter(maillist,this);
		mailAdapter.setCompareList(new ArrayList<Customer>());
		mailist.setAdapter(mailAdapter);
		getMailList();
	}
	void initEvent(){
		((TextView)findViewById(R.id.txt_cancel)).setOnClickListener(this);
		((TextView)findViewById(R.id.txt_confirm)).setOnClickListener(this);
		mailist.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,long arg3) {
				maillist.get(arg2).setIsgroupmem(!maillist.get(arg2).isIsgroupmem());
				mailAdapter.notifyDataSetChanged();
			}
		});
	} 
	void initColor(){
		((RelativeLayout) findViewById(R.id.head_layouer)).setBackgroundResource(BGHelper.setBackground(this, gender));  
	}
	void getMailList() {
		maillist.clear();
		String sql="select * from apm_sys_friend where idcd="+userid+" and typ='old'"; 
		db.open();
		Cursor cur =db.query(sql); //db.getFriend(userid, "old", "");
		while (cur.moveToNext()) {
			Customer cus = new Customer();
			cus.setAvatar(cur.getString(cur.getColumnIndex("acvtor")));
			cus.setNc(cur.getString(cur.getColumnIndex("nc")));
			cus.setId(cur.getInt(cur.getColumnIndex("mid")));
			maillist.add(cus);
		}
		cur.close();
		db.close();
	}
	
	@Override
	public void onClick(View arg0) {
		switch (arg0.getId()) {
		case  R.id.txt_cancel:
			finish();
			break;
		case  R.id.txt_confirm:
			loading_dialog=CommHelper.CreateLoading(this, "",gender,false);
			loading_dialog.show();
			List<Long> mlist = getCusId();
			if(mlist.size()!=0){
				Long[] larr=mlist.toArray(new Long[]{});
				String lmsg = CommHelper.getCompleteStr(CommHelper.createGroupMsg)+getDescFromTalkingMan(mlist,maillist)+ResourceHelper.getStringValue(R.string.meminqun);
				createGroupConversation("",lmsg,larr);
			}else{
				loading_dialog.dismiss();
			}
			break;
		default:
			break;
		};
	}
	//获取组的名称
	String getGroupTitle(Long[] params){
			StringBuilder sb=new StringBuilder();
			for(long pitem:params){
				for(Customer item:maillist){
					if(item.getId()==pitem){
						sb.append(item.getNc()+" ");
					}
				}
			}
			sb.append(SPHelper.getDetailMsg(GrouMemChoseActivity.this, "nc", ""));
			return sb.toString();
	}
	String getDescFromTalkingMan(List<Long> parr,List<Customer> plist){
		StringBuilder sb=new StringBuilder();
		for(long item:parr){
			for(Customer pitem:plist){
				if(item==pitem.getId()){
					sb.append(pitem.getNc()+" ");
				}
			}
		}
		return sb.toString();
	} 
	void createGroupConversation(final String title,String pmsg, Long... uids){
		Message message = IMEngine.getIMService(MessageBuilder.class).buildTextMessage(pmsg);
		conversationService.createConversation(new Callback<Conversation>() {
			@Override
			public void onSuccess(final Conversation arg0) { 
				new Handler().postDelayed(new Runnable() {
					@Override
					public void run() {
						loading_dialog.dismiss();
						Intent intentitem = new Intent(GrouMemChoseActivity.this,TalkingActivity.class); 
						intentitem.putExtra("title", title);
						intentitem.putExtra("converid", arg0.conversationId());
						GrouMemChoseActivity.this.startActivity(intentitem);	
						GrouMemChoseActivity.this.finish();
					}
				}, 2000);
			}
			@Override
			public void onProgress(Conversation arg0, int arg1) {
				
			}
			@Override
			public void onException(String arg0, String arg1) {
				loading_dialog.dismiss();
				ToastManager.show(GrouMemChoseActivity.this, ResourceHelper.getStringValue(R.string.wangluoyic), 2000);
			}
		}, title, userid, message,Conversation.ConversationType.GROUP, uids);
	}
	List<Long> getCusId(){
		List<Long> mlist=new ArrayList<Long>();
		for(Customer item:maillist){
			if(item.isIsgroupmem()){
				mlist.add((long)item.getId());
			}
		}
		return mlist;
	}
}
