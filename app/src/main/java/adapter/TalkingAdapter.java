package adapter;

import java.text.SimpleDateFormat;
import java.util.List;

import bean.Talked;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import groupview.TalkContentBase;

public class TalkingAdapter extends BaseAdapter {
	private List<Talked> mlist;
	private LayoutInflater flater;
	private  SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss " );
	private int conversationTyp = 0;
	private Context context;
	public TalkingAdapter(Context context) {
		this.context= context;
		this.flater=LayoutInflater.from(context); 
	} 
	public void setConversationTyp(int conversationTyp){
		this.conversationTyp=conversationTyp;
	}
	public void setSource(List<Talked> mlist){
		this.mlist=mlist;
	}
	
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return mlist.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return mlist.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	public boolean isscroll() {
		return isscroll;
	}

	public void setIsscroll(boolean isscroll) {
		this.isscroll = isscroll;
	}

	private boolean isscroll;

	@Override
	public View getView(int position, View convertView, ViewGroup parent) { 
//		Talked mobj=mlist.get(position);
//		if(mobj.getTyp().equals("sys")){
//			convertView = initViewSys();
//			((TextView)convertView.findViewById(R.id.txt_sys_remind)).setText(mobj.getCont());
//			return convertView;
//		}
//		if(mobj.getTyp().equals("to")){
//			convertView = initViewTo();
//		}
//		else{
//			convertView = initViewMe();
//		}
//		TextView mytimer=(TextView)convertView.findViewById(R.id.mytimer);
//		final view.CircleImageView img_left=(view.CircleImageView)convertView.findViewById(R.id.img_left);
//		TextView tv_content=(TextView)convertView.findViewById(R.id.tv_content);
//		LinearLayout loser=(LinearLayout)convertView.findViewById(R.id.loser);
//		TextView txt_usernm=(TextView)convertView.findViewById(R.id.txt_usernm);
//		loser.setVisibility(mobj.isIsgood()?View.GONE:View.VISIBLE);
//		mytimer.setText(mobj.getTimer());
//		mytimer.setVisibility(isshow(position)?View.VISIBLE:View.GONE);
//		tv_content.setText(mobj.getCont());
//		if(mobj.getTyp().equals("sys")||mobj.getTyp().equals("me")||conversationTyp==Conversation.ConversationType.CHAT){
//			txt_usernm.setVisibility(View.GONE);
//		}else{
//			txt_usernm.setText(mobj.getNc()+":");
//			txt_usernm.setVisibility(View.VISIBLE);
//		}
//		String filenm=mobj.getPicnm();
//		Glide.with(context).load(Cons.DONW_PIC +filenm).placeholder(R.drawable.regist_man).into(img_left);
		convertView = TalkContentBase.dealTalk(position,mlist,convertView,isscroll(),context);
		return convertView;
	}
	
//	String getActorPath(String pactor) {
//		utils.FileUtils file = new utils.FileUtils("stepic");
//		String dir = file.getFilePath();
//		String topic = dir + pactor;
//		return topic;
//	}
//
//	//显示规则
//	boolean isshow(int position){
//		if(position==0){
//			return true;
//		}
//		Talked totalk=mlist.get(position-1);
//		Talked mytalk=mlist.get(position);
//		try{
//			Date todate=sdf.parse(totalk.getTimer());
//			Date mydate=sdf.parse(mytalk.getTimer());
//			long totimer = todate.getTime();
//			long mytimer = mydate.getTime();
//			long chatimer = mytimer-totimer;
//			return  chatimer>600000;
//		}catch(Exception ex){
//			return false;
//		}
//	}
//
//
//	View initViewMe(){
//		View view=flater.inflate(R.layout.chatting_item_msg_text_right,null);
//		return view;
//	}
//
//	View initViewTo(){
//		View view=flater.inflate(R.layout.chatting_item_msg_text_left,null);
//		return view;
//	}
//
//	View initViewSys(){
//		View view=flater.inflate(R.layout.chatting_item_msg_text_center,null);
//		return view;
//	}
//
//	public List<Talked> getDataList(){
//		return mlist;
//	}
}
