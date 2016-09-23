package fragment;
  
import java.util.ArrayList;   
import java.util.List;

import manager.TalkAuthService;
import tools.ToastManager;
import helper.BGHelper;  
import helper.SPHelper; 
import bean.Customer; 
 
import bean.SessionInfo;
import bean.Talking;      
import com.example.renrenstep.GrouMemChoseActivity;
import com.example.renrenstep.MailListActivity;
import com.example.renrenstep.NewFriendActivity;
import com.example.renrenstep.R;
import com.example.renrenstep.TalkSearchManActivity;
import com.example.renrenstep.TalkingActivity;       
import comm.CommHelper;   
import manager.ConversationHandler;
import manager.ConversationServiceHandler;
import constant.Cons;  
import Interface.Functional.Action;
import Interface.Functional.IMCallback;
import adapter.TalkListItemAdapter;

import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter; 
import android.database.Cursor; 
import android.os.Bundle; 
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener; 
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener; 
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
public class TalkFramgment extends IMBaseFragment implements OnClickListener,AdapterView.OnItemLongClickListener {
	private RelativeLayout layout;
	private View view;
	private ListView talk_list;
	private TalkListItemAdapter talkadapter;
	private List<Talking> mlist; 
	private String mmid;
	private ConversationServiceHandler conversationHandler;
	private LinearLayout linear_menu;
	private MyBroadcastReceiver receiver;
	private LinearLayout layout_selitembg;
	private String gender="";
	private AlertDialog dialog;
	@Override
	public void onActivityCreated(Bundle savedInstanceState) { 
		super.onActivityCreated(savedInstanceState);
	}

	@Override
	public void onStart() {
		autoLogion();
		initView();
		initData();
		initEvent();
		initBracaster();
		initColor();
		initListener();
		super.onStart();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
		view = inflater.inflate(R.layout.fragment_talk, null);
		return view;
	}

	void autoLogion(){ 
		initWkLogion(new TalkAuthService.ILoginCallbak() {
			@Override
			public void logionsuccess() {
			}

			@Override
			public void logionloser() {
				ToastManager.show(getActivity(), getResources().getString(R.string.wangluoyic), 2000);
			}
		});
	}
	
	void initBracaster(){
		IntentFilter filter = new IntentFilter();
        filter.addAction(Cons.RECIVE_MSG_ACTION);  
	    getActivity().registerReceiver(receiver, filter);
	}
	
	private String[] msgtyp={"iscommMsg","isSpecialMsg","isMineSendAgreeMsg","isNormalMsg"};




	class MyBroadcastReceiver extends BroadcastReceiver {
		@Override
		public void onReceive(Context context, Intent intent) {
			String typ=intent.getStringExtra("typ");
			String pid=intent.getStringExtra("pid");
			if(typ==null){return;}
			loadConversations();
		}
	} 
	void initListener(){  
		conversationHandler.listnerConversationStatusChange(new Action<List<SessionInfo>>() {
			@Override
			public void handler(List<SessionInfo> t) {
				loadConversations();
			}
		});
	}
	
	String filterMsg(String pmsg){
		String content= pmsg.length()>19?pmsg.substring(19,pmsg.length()):pmsg;
		return content.length()>19?content.substring(0, 19)+"......":content;
	}


	
	void updateConversation(SessionInfo item){
		Talking pitem= findExitTalk(item);
		if(pitem!=null){
			pitem.setLastWord(filterMsg(item.getLastmsg()));
			pitem.setNoreadnum(item.getNoreadnum());
			pitem.setMsgtyp(item.getMessageType());
			pitem.setCompleteLastwors(item.getLastmsg());
		}else{
			pitem=getConversation(item);
			if(pitem!=null){
				mlist.add(pitem);
			}
		}
	}
	
	Talking findExitTalk(SessionInfo item){
		for(Talking pitem:mlist){
			if(pitem.getConversationid().equals(item.getConversationid())){
				return pitem;
			}
		}
		return null;
	}
	
	void initConversations(List<SessionInfo> plist){
		for(SessionInfo item:plist){
			if(item.getTyp()==ConversationHandler.SINGLE){
				singleTalk(item);
			}else if(item.getTyp()==ConversationHandler.GROUP){
				groupTalk(item);
			}
		}
		talkadapter.notifyDataSetChanged();
	}
	
	void singleTalk(SessionInfo item){
		updateConversation(item);
	}
	
	void groupTalk(SessionInfo item){  
			String lastwords = filterMsg(item.getLastmsg());
			Talking talk =  findExitTalk(item);
			if(talk!=null){
				talk.setMsgtyp(item.getMessageType());
				talk.setLastWord(lastwords);
				talk.setNoreadnum(item.getNoreadnum());
				talk.setCompleteLastwors(item.getLastmsg());
			}else{
				mlist.add(getTalking(item.getTitle(), CommHelper.getGroupConversationTitle(item.getTitle(), item.getTotalmember()), item.getConversationid(), item.getNoreadnum(), item.getPeerid(), lastwords, true, item.getMessageType(),item.getLastmsg()));
			} 
	}
	
	void loadConversations() { 
		conversationHandler.getConversationList(new IMCallback<List<SessionInfo>, Integer, String>() { 
			@Override
			public void success(List<SessionInfo> arg0) { 
				mlist.clear();
				initConversations(arg0);
				talkadapter.notifyDataSetChanged();
			} 
			@Override
			public void process(Integer arg0) {  
			}
			
			@Override
			public void fail(String arg0) {  
			}
		}); 
	} 
	Talking getConversation(SessionInfo pitem){
		long pid = pitem.getPeerid();  
		db.open();
		String sql="select * from apm_sys_friend where idcd='"+mmid+"' and typ='old' and mid=" + pid+"";
			Cursor cursor =db.query(sql);  //db.getFriend(mmid, "old", " and mid=" + pid);
			if (cursor.getCount() > 0) {
				while (cursor.moveToNext()) {
					String acvtor = cursor.getString(cursor.getColumnIndex("acvtor"));
					String nc = cursor.getString(cursor.getColumnIndex("nc"));
					cursor.close();
					Talking talk = getTalking(acvtor,nc,pitem.getConversationid(),pitem.getNoreadnum(),pid,filterMsg(pitem.getLastmsg()),false,pitem.getMessageType(),pitem.getLastmsg());
					return talk;
				}
				cursor.close();
			}else{
			for(String[] item:CommHelper.mids){
				if(item[0].equals(pid+"")){
					return getTalking(item[2],item[1],pitem.getConversationid(),pitem.getNoreadnum(),pid,filterMsg(pitem.getLastmsg()),false,pitem.getMessageType(),pitem.getLastmsg());
				}
			}
		}
		cursor.close();
		db.close();
		return null; 
	}
	Talking getTalking(String acvtor,String nc,String conversationid,int unreadCount,long mid,String text,boolean isgroup,String msgType,String comLastword){
		Talking talk = new Talking();
		talk.setAvatar(acvtor);
		talk.setNc(nc);
		talk.setConversationid(conversationid);
		talk.setNoreadnum(unreadCount);
		talk.setMid(mid);
		talk.setLastWord(text);
		talk.setIsgroup(isgroup);
		talk.setMsgtyp(msgType);
		talk.setCompleteLastwors(comLastword);
		return talk;
	}
	
	void initView() {
		layout = (RelativeLayout) view.findViewById(R.id.layouter);
		talk_list = (ListView) view.findViewById(R.id.talk_list);
		linear_menu = (LinearLayout) view.findViewById(R.id.linear_menu); 
		layout_selitembg=((LinearLayout) view.findViewById(R.id.layout_selitembg)); 
	}
	
//	Dialog del_Dialog;
//	void initDialog(){
//		Resources resource=getResources();
//		View view = DialogHelper.getConfrirmDialog(new DelDialogConfirm(),resource.getString(R.string.confirm),resource.getString(R.string.cancel), resource.getString(R.string.isdel), getActivity(), gender);
//		del_Dialog = getDialog(view);
//	}
//	
//	class DelDialogConfirm implements IDialog_event{
//		@Override
//		public void confirm() {
//			delConversation(delConversationid);
//		}
//		@Override
//		public void cancel() {
//			del_Dialog.dismiss();
//		} 
//	}
	
	void initColor() {
		layout.setBackgroundResource(BGHelper.setBackground(getActivity(),gender));
	}
	
	void initEvent() { 
		((LinearLayout) view.findViewById(R.id.covert_right_dao)).setOnClickListener(this);
		((LinearLayout) view.findViewById(R.id.covert_left_dao)).setOnClickListener(this);
		((LinearLayout) view.findViewById(R.id.convert_search)).setOnClickListener(this); 
		view.setOnClickListener(this);
		((LinearLayout) view.findViewById(R.id.layout_menu_group)).setOnClickListener(this);
		((LinearLayout) view.findViewById(R.id.layout_menu_search)).setOnClickListener(this);
		((LinearLayout) view.findViewById(R.id.linear_talk)).setOnTouchListener(new OnTouchListener() {
			@Override
			public boolean onTouch(View arg0, MotionEvent arg1) {
				if(arg1.getAction()==MotionEvent.ACTION_DOWN){
					linear_menu.setVisibility(View.GONE);
					layout_selitembg.setVisibility(View.GONE);
				}
				return false;
			}
		});
		talk_list.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,int position, long id) {
				long mmid=mlist.get(position).getMid();
				if(CommHelper.isCommFriend(mmid+"")){ 
					Intent intent=new Intent(getActivity(),NewFriendActivity.class);
					intent.putExtra("commid", mlist.get(position).getConversationid());
					startActivity(intent);
				}else{
					Intent intentitem = new Intent(getActivity(),TalkingActivity.class);
					intentitem.putExtra("title", mlist.get(position).getNc());
					intentitem.putExtra("converid", mlist.get(position).getConversationid());
					getActivity().startActivity(intentitem);	
				}
			}
		});
		talk_list.setOnItemLongClickListener(this);
		//layout_selitembg.setOnClickListener(this);
	}
	private String delConversationid="";
	Customer getCustomer(Talking pitem) {
		Customer cus = new Customer();
		cus.setNc(pitem.getNc());
		cus.setId((int) pitem.getMid());
		cus.setAvatar(pitem.getAvatar());
		return cus;
	}
	
	void initData() { 
		mlist = new ArrayList<Talking>();
		talkadapter = new TalkListItemAdapter(getActivity());
		talkadapter.setSource(mlist);
		talk_list.setAdapter(talkadapter); 
		mmid = SPHelper.getBaseMsg(getActivity(), "mid", "0");
		receiver=new MyBroadcastReceiver(); 
		linear_menu.setVisibility(View.GONE);
		layout_selitembg.setVisibility(View.GONE);
		gender=SPHelper.getDetailMsg(getActivity(), Cons.APP_SEX,getString(R.string.appsex_man));
		conversationHandler=new ConversationServiceHandler();
	}
	
	String getCommConversationId(){
		for(Talking item:mlist){
			for(String[] items:CommHelper.mids){
				if(items[0].equals(item.getMid()+"")){
					return item.getConversationid();
				}
			}
		}
		return "";
	}
	 
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.covert_right_dao:
			initMenuView(); 
			break;
		case R.id.covert_left_dao:
			Intent lintent = new Intent(getActivity(), MailListActivity.class);
			lintent.putExtra("commid", getCommConversationId());
			startActivity(lintent);
			break;
		case R.id.convert_search:
			break;
		case R.id.layout_menu_group: 
			initMenuView();
			Intent intent = new Intent(getActivity(),GrouMemChoseActivity.class);
			startActivity(intent);
			break;
		case R.id.layout_menu_search:
			initMenuView();
			Intent rintent = new Intent(getActivity(),TalkSearchManActivity.class);
			startActivity(rintent);
			break;
		case R.layout.fragment_talk:
			linear_menu.setVisibility(View.GONE);
			break; 
		case R.id.layout_selitembg:
			initMenuView();
			break;
		default:
			break;
		}
	}

	@Override
	public boolean onItemLongClick(final AdapterView<?> adapterView, View view, int i, long l) {

		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
		builder.setTitle("删除该会话？");
		builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialogInterface, int i) {


			}
		});

		builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialogInterface, int i) {
				dialog.dismiss();
			}
		});

		dialog = builder.create();
		dialog.show();

		return true;
	}

	
	void initMenuView(){
		boolean ishow=View.VISIBLE==linear_menu.getVisibility();
		linear_menu.setVisibility(ishow?View.GONE:View.VISIBLE);
		layout_selitembg.setVisibility(ishow?View.GONE:View.VISIBLE);
	}
	
	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		loadConversations();
		linear_menu.setVisibility(View.GONE);
		super.onResume();
	}
	
	void clectionResource(){
//		converService.removeConversationChangeListener(conversationChange); 
	}
	
	@Override
	public void onDestroy() {
		clectionResource(); 
		if(receiver!=null){
			getActivity().unregisterReceiver(receiver);
		}
		super.onDestroy();
	}
//	void delConversation(String conversationid){
//		IMEngine.getIMService(ConversationService.class).getConversation(new Callback<Conversation>() {
//			@Override
//			public void onSuccess(Conversation arg0) {
//				arg0.removeAndClearMessage();
//				del_Dialog.dismiss();
//				loadConversations();
//			}
//			@Override
//			public void onProgress(Conversation arg0, int arg1) {
//				
//			}
//			
//			@Override
//			public void onException(String arg0, String arg1) {
//				Toast.makeText(getActivity(), getResources().getString(R.string.wangluoyic),2000).show();
//			}
//		}, conversationid);
//	}
}
