package service;
 
import java.util.List;  
import com.alibaba.wukong.im.Conversation.ConversationType;
import com.alibaba.wukong.im.IMEngine;
import com.alibaba.wukong.im.Message; 
import com.alibaba.wukong.im.MessageListener;
import com.alibaba.wukong.im.MessageService;

import android.app.Service;
import android.content.Intent; 
import android.os.IBinder; 

public class TalkingMessageService extends Service { 
	private MessageService messageService;
	private GroupTalking grouptalk; 
	private SingleTalking singletalk; 
	MessageListener messagelistener = new MessageListener() {
		@Override
		public void onAdded(List<Message> arg0, DataType arg1) {
			for (Message item : arg0) {
				switch (item.conversation().type()) {
					case ConversationType.CHAT:
						singletalk.hanlder(item);
					break;
					case ConversationType.GROUP:
						grouptalk.hanlder(item);
					break;
				}
			}
			// 消息分三种 1.添加好友消息 2.好友普通消息 3.同意请求消息 , 更新会话未读数 发出广播 去做相应的事 并且 更新ui
		}
		public void onRemoved(List<Message> list) {
			// 处理被删除的消息
		} 
		public void onChanged(List<Message> list) {
			// 通知ui更新消息
		}
	};

	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void onCreate() { 
		super.onCreate();
		messageService = IMEngine.getIMService(MessageService.class);
		messageService.addMessageListener(messagelistener);
		singletalk = new SingleTalking(this);
		grouptalk = new GroupTalking(this); 
		//notification = new NotificationHelper(R.drawable.ic_launcher, this);
	}
	 
	@Override
	public void onDestroy() { 
		messageService.removeMessageListener(messagelistener);
		super.onDestroy();
	}

}
