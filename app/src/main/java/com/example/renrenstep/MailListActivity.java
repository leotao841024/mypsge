package com.example.renrenstep;

import helper.BGHelper;
import helper.DialogHelper;
import helper.SPHelper; 

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import manager.TalkAuthService;
import tools.ToastManager;
import org.json.JSONObject;
import com.alibaba.wukong.Callback;
import com.alibaba.wukong.im.Conversation;
import com.alibaba.wukong.im.ConversationService;
import com.alibaba.wukong.im.IMEngine;
import com.alibaba.wukong.im.Message;
import com.alibaba.wukong.im.MessageBuilder; 
import com.alibaba.wukong.im.MessageService;  

import helper.BaseAsyncTask;
import comm.CommHelper;
import constant.Cons;
import bean.Customer;
import adapter.MailAdapter;
import android.app.Dialog;
import android.content.Intent;
import android.content.res.Resources;
import android.database.Cursor; 
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.TextView;

public class MailListActivity extends MyBaseActivity implements OnClickListener {
	private ListView mile_list;
	private List<Customer> mlist;
	private MailAdapter madapter;
	private RelativeLayout layouter;
	private TextView newfriendcount;
	private String currentMid;
	private String conversationid="";
	private Conversation commConversation;
	private MessageService msgService;
	private Dialog alterDialog;
	private String gender="";
	private int delindex = 0;
	private LinearLayout layout_selitembg;
	private ConversationService conversationService;
	private LinearLayout linear_menu;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_mail_list); 
		autoLogion();
		initView();
		initData();
		initEvent();
		initColor();
		initDialog();
	}
	
	void autoLogion(){
		db.open();
		initWkLogion(new TalkAuthService.ILoginCallbak() {
			@Override
			public void logionsuccess() {
				// TODO Auto-generated method stub
			}
			@Override
			public void logionloser() {
				// TODO Auto-generated method stub
				ToastManager.show(MailListActivity.this, getResources().getString(R.string.wangluoyic), 2000);
			}
		});
	}
	
	void initView(){
		mile_list=(ListView)findViewById(R.id.mile_list);
		layouter=(RelativeLayout)findViewById(R.id.layouter);
		newfriendcount=(TextView)findViewById(R.id.newfriendcount);
		linear_menu = (LinearLayout)findViewById(R.id.linear_menu);
		layout_selitembg=((LinearLayout)findViewById(R.id.layout_selitembg));
	}
	
	void initData(){
		mlist=new ArrayList<Customer>(); 
		currentMid=SPHelper.getBaseMsg(MailListActivity.this, "mid", "0");
		madapter=new MailAdapter(mlist, this);
		mile_list.setAdapter(madapter);
		Intent intent = getIntent();
		conversationid = intent.getStringExtra("commid");
		msgService=IMEngine.getIMService(MessageService.class);
		conversationService=IMEngine.getIMService(ConversationService.class);
		gender=SPHelper.getDetailMsg(this, Cons.APP_SEX,getString(R.string.appsex_man)); 
		linear_menu.setVisibility(View.GONE);
		layout_selitembg.setVisibility(View.GONE);
	} 
	void initEvent(){
		((LinearLayout)findViewById(R.id.covert_left_dao)).setOnClickListener(this);
		((LinearLayout)findViewById(R.id.covert_right_dao)).setOnClickListener(this);
		((LinearLayout)findViewById(R.id.addnewfriend)).setOnClickListener(this);
		((LinearLayout) findViewById(R.id.layout_menu_group)).setOnClickListener(this);
		((LinearLayout) findViewById(R.id.layout_menu_search)).setOnClickListener(this);
		mile_list.setOnItemClickListener(new OnItemClickListener() { 
			@Override
			public void onItemClick(AdapterView<?> parent, View view,int position, long id) {
				Intent intentitem=new Intent(MailListActivity.this,TalkingActivity.class);
				intentitem.putExtra("title", mlist.get(position).getNc());
				intentitem.putExtra("converid",getConversationId(mlist.get(position).getId()));
				MailListActivity.this.startActivity(intentitem);
			}
		}); 
		mile_list.setOnItemLongClickListener(new OnItemLongClickListener() {
			@Override
			public boolean onItemLongClick(AdapterView<?> parent, View view,int position, long id) {
				delindex=position;
				alterDialog.show();
				return true;
			}
		});
		((LinearLayout)findViewById(R.id.linear_talk)).setOnTouchListener(new OnTouchListener() {
			@Override
			public boolean onTouch(View arg0, MotionEvent arg1) {
				if(arg1.getAction()==MotionEvent.ACTION_DOWN){
					linear_menu.setVisibility(View.GONE);
					layout_selitembg.setVisibility(View.GONE);
				}
				return false;
			}
		});
		layout_selitembg.setOnClickListener(this);
		registBrocaster();
	}
	
	String getConversationId(int pid){
		int myid=Integer.parseInt(currentMid);
		return pid>myid?myid+":"+pid:pid+":"+myid;
	}
	
	void initColor(){
		layouter.setBackgroundResource(BGHelper.setBackground(this, SPHelper.getDetailMsg(this, Cons.APP_SEX,getString(R.string.appsex_man))));
	}
	
	void initDialog(){
		Resources resource=getResources();
		View view = DialogHelper.getConfrirmDialog(new IConfirmCls(),resource.getString(R.string.confirm),resource.getString(R.string.cancel), resource.getString(R.string.isdel), MailListActivity.this, gender);
		alterDialog =  getDialog(view);
	}
	
	class IConfirmCls implements DialogHelper.IDialog_event {
		@Override
		public void confirm() {
			delFriend();
		} 
		@Override
		public void cancel() {
			alterDialog.dismiss();
		} 
	}
	
	void delFriend(){
		Map<String,Object> maps=new HashMap<String, Object>();
		maps.put("id", mlist.get(delindex).getId()); 
		new BaseAsyncTask(Cons.DELFRIEND,maps, BaseAsyncTask.HttpType.Post,"",MailListActivity.this) {
			@Override
			public void handler(String param) {
				alterDialog.dismiss();
				try
				{
				if(param==null||param.equals("")||!param.contains("status")){
					ToastManager.show(MailListActivity.this, getResources().getString(R.string.wangluoyic),2000);
					return;
				}
				JSONObject json=new JSONObject(param);
				int res=json.getInt("status");
				if(res==0){
					getConversation(getConversationId(mlist.get(delindex).getId()+""));
				 }
				}
				catch(Exception ex){
					ToastManager.show(MailListActivity.this, getResources().getString(R.string.wangluoyic),2000);
				}
			}
		}.execute("");
	}
	String getConversationId(String pid){
		long lpid=Long.parseLong(pid);
		long mid=Long.parseLong(SPHelper.getBaseMsg(MailListActivity.this, "mid", "0"));
		return lpid>mid?mid+":"+lpid:lpid+":"+mid;
	} 
	
	void getConversation(String pconverid){
		conversationService.getConversation(new Callback<Conversation>() {	
			@Override
			public void onSuccess(Conversation arg0) {
				sendMsgToFriend(CommHelper.getCompleteStr(CommHelper.deleteMsg)+getResources().getString(R.string.deletefriend),arg0);
			}
			
			@Override
			public void onProgress(Conversation arg0, int arg1) {
				// TODO Auto-generated method stub
				
			} 
			@Override
			public void onException(String arg0, String arg1) {
				// TODO Auto-generated method stub
				
			}
		}, pconverid);
	}
	
	void sendMsgToFriend(String msg,final Conversation conversation){
		Message message = IMEngine.getIMService(MessageBuilder.class).buildTextMessage(msg);
		message.sendTo(conversation,new Callback<Message>() {
			@Override
			public void onSuccess(Message arg0) {
				// TODO Auto-generated method stub 
				delStore("old",mlist.get(delindex).getId());
				delListItem();
				madapter.notifyDataSetChanged();  
			}
			@Override
			public void onProgress(Message arg0, int arg1) {
				// TODO Auto-generated method stub 
			}
			
			@Override
			public void onException(String arg0, String arg1) { 
			}
		});
	}
	void delListItem(){
		mlist.remove(delindex);
	}
	
	void delStore(String typ,int pid){ 
		db.open();
		String sql="delete from apm_sys_friend where 1=1 and  typ=? and idcd=?";
		db.delete("apm_sys_friend", "  mid=? and  typ=? and idcd=?", new String[]{pid+"", typ,SPHelper.getBaseMsg(MailListActivity.this, "mid","0")});
		db.close();
	}
	
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.covert_left_dao:
			finish();
			break;
		case R.id.covert_right_dao: 
			initMenuView();
//			Intent rintent = new Intent(this,TalkSearchManActivity.class);
//			startActivity(rintent);
			break;
		case R.id.addnewfriend:
			Intent mintent=new Intent(this,NewFriendActivity.class);
			mintent.putExtra("commid", conversationid);
			startActivity(mintent);
			break;
		case R.id.layout_menu_group: 
			initMenuView();
			Intent intent = new Intent(this,GrouMemChoseActivity.class);
			startActivity(intent);
			break;
		case R.id.layout_menu_search:
			initMenuView();
			Intent rintent = new Intent(this,TalkSearchManActivity.class);
			startActivity(rintent);
			break;
		case R.id.layout_selitembg:
			initMenuView();
			break;
		default:
			break;
		}
	}
	void initMenuView(){
		boolean ishow=View.VISIBLE==linear_menu.getVisibility();
		linear_menu.setVisibility(ishow?View.GONE:View.VISIBLE);
		layout_selitembg.setVisibility(ishow?View.GONE:View.VISIBLE);
	}
 
	@Override
	protected void onResume() {
		initCommConversation(new handlerCommCountCallback() {
			@Override
			public void hanlder(int count) {
				// TODO Auto-generated method stub
				if(count!=0){
					newfriendcount.setText(count+"");
					newfriendcount.setVisibility(View.VISIBLE);
				}else{
					newfriendcount.setVisibility(View.INVISIBLE);
				}
			}
		}); 
		getMailList(); 
		super.onResume();
	}
	@Override
	public void receiveMsgHander(Intent intent) {
		String typ=intent.getStringExtra("typ");
		String pid=intent.getStringExtra("pid");
		if(typ==null){return;} 
		getMailList(); 
	};
	
	void getMailList(){
		mlist.clear();
		String sql="select * from apm_sys_friend where idcd='"+currentMid+"' and typ='old'";
		db.open();
		Cursor cur=db.query(sql);//db.getFriend(currentMid, "old","");
		while(cur.moveToNext()){
			Customer cus=new Customer();
			cus.setAvatar(cur.getString(cur.getColumnIndex("acvtor")));
			cus.setNc(cur.getString(cur.getColumnIndex("nc")));
			cus.setId(cur.getInt(cur.getColumnIndex("mid")));
			mlist.add(cus);
		}
		cur.close();
		db.close();
		madapter.notifyDataSetChanged();
	}
	
	 interface handlerCommCountCallback{
		 void hanlder(int count);
	 }
	void initCommConversation(final handlerCommCountCallback phandler){
		if(!conversationid.equals("")){
			IMEngine.getIMService(ConversationService.class).getConversation(new Callback<Conversation>() {
				@Override
				public void onSuccess(Conversation arg0) {
					// TODO Auto-generated method stub
					phandler.hanlder(arg0.unreadMessageCount()); 
				} 
				@Override
				public void onProgress(Conversation arg0, int arg1) {
					// TODO Auto-generated method stub
					
				}
				
				@Override
				public void onException(String arg0, String arg1) {
					// TODO Auto-generated method stub
					
				}
			}, conversationid);
		}
	}
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub 
 		unregistBrocaster();
		super.onDestroy();
	}
	
}
