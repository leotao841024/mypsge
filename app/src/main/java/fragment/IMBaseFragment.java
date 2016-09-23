package fragment;

import helper.SPHelper;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import com.alibaba.wukong.Callback;
import com.alibaba.wukong.auth.ALoginParam;
import com.alibaba.wukong.auth.AuthInfo;
import com.alibaba.wukong.auth.AuthService;
import com.alibaba.wukong.im.Conversation;
import com.alibaba.wukong.im.ConversationService;
import com.alibaba.wukong.im.IMEngine;
import com.alibaba.wukong.im.Message;
import com.alibaba.wukong.im.MessageBuilder;

import comm.CommHelper;
import db.DBhelper;
import manager.TalkAuthService;

import android.app.Dialog;
import android.os.Handler;
import android.app.Fragment;
import android.view.Display;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

public class IMBaseFragment extends Fragment {
	public DBhelper db = DBhelper.instance();

//	public void startTalkingService(){
//		Intent talkingservice = new Intent(getActivity(), TalkingMessageService.class);
//		getActivity().startService(talkingservice);
//	}

	protected  void initWkLogion(TalkAuthService.ILoginCallbak callback){
		TalkAuthService.initWkLogion(callback);
	}
	public void checkUserMsgChange(String key,String value,String pmsg){
		if(ischangedStr(key,value)){
			sendUsermsgChangeMsg(pmsg);
		}
	}
	
	public boolean ischangedStr(String key,String value){
		String old= SPHelper.getDetailMsg(getActivity(), key, "");
		return old.equals(value);
	}
	
	public void sendUsermsgChangeMsg(final String pmsg){
		IMEngine.getIMService(ConversationService.class).listConversations(new Callback<List<Conversation>>() {
			@Override
			public void onSuccess(List<Conversation> arg0) { 
				sendConversations(arg0,pmsg);
			} 
			@Override
			public void onProgress(List<Conversation> arg0, int arg1) { 
				
			}
			
			@Override
			public void onException(String arg0, String arg1) { 
				
			}
		},Integer.MAX_VALUE, Conversation.ConversationType.GROUP|Conversation.ConversationType.CHAT);
	}
	
	void sendConversations(List<Conversation> conversations,final String pmsg){
		for(final Conversation item:conversations){
			new Handler().postDelayed(new Runnable() {
				@Override
				public void run() {
					Message msg= IMEngine.getIMService(MessageBuilder.class).buildTextMessage(pmsg);
					msg.sendTo(item, new Callback<Message>() {
						@Override
						public void onSuccess(Message arg0) {
							
						}
						@Override
						public void onProgress(Message arg0, int arg1) { 
						}
						
						@Override
						public void onException(String arg0, String arg1) { 
						}
					});
				}
			}, 2000);
		}
	}
	public Dialog getDialog(View view,int styleid){
		Dialog alterDialog = new Dialog(getActivity(),styleid);
		alterDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		alterDialog.setCanceledOnTouchOutside(false);
		alterDialog.setContentView(view);
		WindowManager windowManager = getActivity().getWindowManager();
		Display display = windowManager.getDefaultDisplay();
		WindowManager.LayoutParams lp = alterDialog.getWindow().getAttributes();
		lp.width = display.getWidth()-70; // 设置宽度
		alterDialog.getWindow().setAttributes(lp);
		return alterDialog;
	}
}
