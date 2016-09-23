package manager;
import java.io.File;
import java.util.HashMap;
import java.util.List;
import Interface.Functional;
import Interface.Functional.Action;
import Interface.Functional.Func;
import Interface.Functional.IConversationHandler;
import Interface.Functional.IMCallback;
import bean.MessageInfo;
import bean.MessageInfo.MssageType;

import com.alibaba.wukong.Callback;
import com.alibaba.wukong.im.Conversation;
import com.alibaba.wukong.im.IMEngine;
import com.alibaba.wukong.im.Message;
import com.alibaba.wukong.im.MessageBuilder;

public class ConversationHandler implements IConversationHandler {
	public static final int SINGLE=1;
	public static final int GROUP=2;

	private Conversation conversation;
	public ConversationHandler (Conversation conversation){
		this.conversation=conversation;
	}
	@Override
	public void resetUnreadCount() {
		conversation.resetUnreadCount();
	}

	@Override
	public void addUnreadCount(int num) {
		conversation.addUnreadCount(num);
	}
	@Override
	public void listPreviousMessages(Message msg, int pagesize,final Action<List<MessageInfo>> callback) {
		conversation.listPreviousMessages(msg, pagesize, new Callback<List<Message>>() {
			@Override
			public void onSuccess(List<Message> arg0) {
				callback.handler(Functional.each(arg0, new MessageToMessageInfo()));
			}

			@Override
			public void onProgress(List<Message> arg0, int arg1) {
			}

			@Override
			public void onException(String arg0, String arg1) {

			}
		});
	}
	@Override
	public void sendMsg(String text, MssageType type,final IMCallback<MessageInfo, Integer, String> callback) {
		Message message = buildMessage(text,type);
		message.sendTo(conversation, new Callback<Message>() {
			@Override
			public void onSuccess(Message arg0) {
				callback.success(new MessageToMessageInfo().handler(arg0));
			}
			@Override
			public void onProgress(Message arg0, int arg1) {

			}

			@Override
			public void onException(String arg0, String arg1) {
				callback.fail(arg0);
			}
		});
	}

	Message buildMessage(String text,MssageType type){
		Message msg = null;
		switch (type){
			case TXT:
				msg=IMEngine.getIMService(MessageBuilder.class).buildTextMessage(text);
				break;
			case IMAGE:
				File file=new File(text);
				if(file.exists()){
					msg=IMEngine.getIMService(MessageBuilder.class).buildImageMessage(file.getPath(), file.length(), 0);
				}
				break;
		}
		return msg;
	}

	class MessageToMessageInfo implements Func<Message, MessageInfo>{
		@Override
		public MessageInfo handler(Message from) {
			MessageInfo info=new MessageInfo();
			info.setContent(MessageReceiver.getMessageContent(from));
			info.setExtendmsg(from.extension("key"));
			info.setMsgConversationType(from.conversation().type());
			info.setMsgId(from.messageId());
			info.setSendId(from.senderId());
			info.setTimer(from.createdAt());
			info.setConversationtype(from.conversation().type());
			info.setConversationId(from.conversation().conversationId());
			info.setMessageType(MessageReceiver.getMessageType(from.messageContent().type()));
			info.setConversation(new ConversationHandler(from.conversation()));
			info.setMessage(from);
			return info;
		}
	}
	@Override
	public void updateTitle(String newTitle, String sendMsg,final IMCallback<Void, Void, String> callback) {
		Message message = IMEngine.getIMService(MessageBuilder.class).buildTextMessage(sendMsg);
		conversation.updateTitle(newTitle,message,new Callback<Void>() {	//调用接口更改标题
			@Override
			public void onProgress(Void data, int progress) {
				callback.process(data);
			}
			@Override
			public void onSuccess(Void data) {//更新成功后，可以在这里通知UI变更
				callback.success(data);
			}
			@Override
			public void onException(String code, String reason) {
				callback.fail(reason);
			}
		});
	}
	@Override
	public void quit(String pmsg,final IMCallback<Void, Void, String> callback) {
		Message message = IMEngine.getIMService(MessageBuilder.class).buildTextMessage(pmsg);
		conversation.quit(message,new Callback<Void>() {
			@Override
			public void onSuccess(Void arg0) {
				callback.success(arg0);
			}
			@Override
			public void onProgress(Void arg0, int arg1) {
			}
			@Override
			public void onException(String arg0, String arg1) {
				callback.fail(arg0);
			}
		});
	}
	@Override
	public void stayOnTop(boolean istop,final Action<Long> callback) {
		conversation.stayOnTop(istop, new Callback<Long>() {
			@Override
			public void onSuccess(Long arg0) {
				callback.handler(arg0);
			}
			@Override
			public void onProgress(Long arg0, int arg1) {

			}
			@Override
			public void onException(String arg0, String arg1) {

			}
		});
	}
	@Override
	public void disband(final IMCallback<Void, Void, String> callback) {
		// TODO Auto-generated method stub
		conversation.disband(new Callback<Void>() {
			@Override
			public void onSuccess(Void arg0) {
				// TODO Auto-generated method stub
				callback.success(arg0);
			}
			@Override
			public void onProgress(Void arg0, int arg1) {
				callback.process(arg0);
			}
			@Override
			public void onException(String arg0, String arg1) {
				callback.fail(arg0);
			}
		});
	}

}
