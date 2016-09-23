package manager;

import java.util.List; 
import Interface.Functional.Action;
import Interface.Functional.Func; 
import Interface.Functional; 
import bean.MessageInfo;

import com.alibaba.wukong.im.IMEngine;
import com.alibaba.wukong.im.Message;
import com.alibaba.wukong.im.MessageListener;
import com.alibaba.wukong.im.MessageService; 
public class MessageHandler {
	private MessageService msgService=IMEngine.getIMService(MessageService.class);
	private MessageToMessageInfo messageinfo;
	private MessageListener listener;
	public MessageHandler() {
		messageinfo=new MessageToMessageInfo();
	}
	class MessageToMessageInfo implements Func<Message, MessageInfo>{
		@Override
		public MessageInfo handler(Message from) {
			MessageInfo info=new MessageInfo();
			info.setContent(MessageReceiver.getMessageContent(from));//((MessageContent.TextContent)from.messageContent()).text()
			info.setExtendmsg(from.extension("key"));
			info.setMsgConversationType(from.conversation().type());
			info.setMessageType(MessageReceiver.getMessageType(from.messageContent().type()));
			info.setMsgId(from.messageId());
			info.setSendId(from.senderId());
			info.setTimer(from.createdAt());
			info.setTyp(from.conversation().type());
			info.setConversationId(from.conversation().conversationId());
			info.setConversation(new ConversationHandler(from.conversation()));
			return info;
		}
	}
	public void addMessageListener(final Action<List<MessageInfo>> handler){
		listener=new MessageListener(){
			@Override
			public void onAdded(List<Message> arg0, DataType arg1) {
				handler.handler(Functional.each(arg0,messageinfo));
			}
			@Override
			public void onChanged(List<Message> arg0) {
			}

			@Override
			public void onRemoved(List<Message> arg0) {
			}
		};
		msgService.addMessageListener(listener);
	}
	public void unRegistListener(){
		if(listener!=null ){
			msgService.removeMessageListener(listener);
		}
	}

}