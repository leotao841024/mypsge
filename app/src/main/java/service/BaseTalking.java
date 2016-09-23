package service;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;

import com.alibaba.wukong.Callback;
import com.alibaba.wukong.im.Conversation;
import com.alibaba.wukong.im.Message;
import com.example.renrenstep.MyBaseActivity;
import com.google.gson.Gson;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import bean.ApplayCustomer;
import bean.Customer;
import helper.BaseAsyncTask;
import comm.CommHelper;
import constant.Cons;
import db.DBhelper;
import helper.SPHelper;

public abstract class BaseTalking {
	interface  MessageCallback{
		void handler();
		void failed();
	}

	private Intent intent = new Intent(Cons.RECIVE_MSG_ACTION);
	private Context context;
	public DBhelper db;
	public String mmid;
	
	public BaseTalking(Context context){
		this.context = context;
		mmid=SPHelper.getBaseMsg(context, "mid", "");
		db=DBhelper.instance();
		db.open();
	}
	public Context currentContext(){
		return context;
	}
	public boolean isOtherUserMsgChange(String porder,String sendid){
		return porder.equals(CommHelper.changeMemMsg)&&!sendid.equals(mmid);
	}
	
	public boolean isMyMsgChange(String porder,String sendid){
		return porder.equals(CommHelper.changeMemMsg)&&sendid.equals(mmid);
	}
	
	public String getOrderMsg(String pmsg){
		return pmsg.length() > 19 ? pmsg.substring(0, 20).trim(): pmsg;
	}
	
	public String getContentMsg(String pmsg){
		return pmsg.length() > 19 ? pmsg.substring(20, pmsg.length()): pmsg;
	}
	
	public void sendBroacaste(String typ, String mid) { 
		intent.putExtra("typ", typ);
		intent.putExtra("mid", mid);
		context.sendBroadcast(intent);
	}

	public void getOneFriend(final String mid, final String typ,final MessageCallback handler) {
		Map<String, Object> maps = new HashMap<String, Object>();
		maps.put("id", mid);
		new BaseAsyncTask(Cons.GETAPPLAYMANURL, maps, BaseAsyncTask.HttpType.Get, "", context) {
			@Override
			public void handler(String param) {
				JSONObject jsonobj;
				if (param == null || param.equals("")|| !param.contains("status")) {
					return;
				}
				try {
					jsonobj = new JSONObject(param);
					if (jsonobj.getInt("status") == 0) {
						ApplayCustomer applycus = new ApplayCustomer();
						Gson gson = new Gson();
						applycus = gson.fromJson(param, applycus.getClass());
						Customer item = applycus.getItem();
						if (typ.equals("new")) {
							item.setState("UNREAD");
						}
						if(hasfirend(mid,typ)){
							ContentValues contents= new ContentValues(); 
								contents.put("mid", item.getId());
								contents.put("nc", filterStr(item.getNc()));
								contents.put("email",filterStr(item.getEmail()));
								contents.put("address", filterStr(item.getCity_alia()));
								contents.put("acvtor", filterStr(item.getAvatar()));
								contents.put("state", filterStr(item.getState()));
								contents.put("words", filterStr(item.getWords()));//remark,idcd,typ
								contents.put("remark", filterStr(item.getRemark()));
								contents.put("idcd", mmid);
								contents.put("typ", typ);
								db.open();
								db.insert("apm_sys_friend", contents);
								db.close();
						}
						String actor = item.getAvatar();
						List<String> pics = new ArrayList<String>();
						pics.add(actor);
						if(handler!=null){
							handler.handler();
						}
					}
				} catch (Exception ex) {
					handler.failed();
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

	boolean hasfirend(String mid,String typ) {
		String sql="select mid from apm_sys_friend where idcd='"+mmid+"' and typ='"+typ+"' and mid='"+mid+"'";
		db.open();
		Cursor cursor= db.query(sql);
		int cur = cursor.getCount();
		cursor.close();  
		db.close(); //db.getNewFriendCount(mmid, "new", "UNREAD", mid);
		return cur == 0;
	}
	
	public void delFriend(String mid,String typ) {
		db.open();
		db.delete("apm_sys_friend", " mid=? and typ=? and idcd=?",new String[]{mid,typ, mmid});
		db.close();
	}
	
	public void clearConversation(Conversation conversation) {
		conversation.clear(new Callback<Void>() {
			@Override
			public void onSuccess(Void arg0) {
				// TODO Auto-generated method stub 
			}
			
			@Override
			public void onProgress(Void arg0, int arg1) {
				// TODO Auto-generated method stub 
			}
			
			@Override
			public void onException(String arg0, String arg1) {
				// TODO Auto-generated method stub 
			}
		}); 
		conversation.remove();
	}
	public abstract void hanlder(Message mitem);
	
 
}
