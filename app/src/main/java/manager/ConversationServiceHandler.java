package manager;
import java.util.ArrayList;
import java.util.List;
import Interface.Functional;
import Interface.Functional.Action;
import Interface.Functional.Func;
import Interface.Functional.IMCallback;
import bean.MemberInfo;
import bean.SessionInfo;

import com.alibaba.wukong.Callback;
import com.alibaba.wukong.im.Conversation;
import com.alibaba.wukong.im.Conversation.ConversationStatus;
import com.alibaba.wukong.im.ConversationChangeListener;
import com.alibaba.wukong.im.ConversationService;
import com.alibaba.wukong.im.IMEngine;
import com.alibaba.wukong.im.Member;
import com.alibaba.wukong.im.Message;
import com.alibaba.wukong.im.MessageBuilder;

public class ConversationServiceHandler {

	private ConversationToSession conversation_to_session;
	class ConversationToSession implements Func<Conversation, SessionInfo>{
		public SessionInfo handler(Conversation from) {
			SessionInfo session=new SessionInfo();
			session.setConversationid(from.conversationId());
			session.setIcon(from.icon());
			session.setLastmsg(MessageReceiver.getMessageContent(from.latestMessage()));
			session.setPeerid(from.getPeerId());
			session.setTitle(from.title());
			session.setToplevel(from.getTop());
			session.setTotalmember(from.totalMembers());
			session.setTyp(from.type());
			session.setStatus(from.status().ordinal());
			session.setNoreadnum(from.unreadMessageCount());
			session.setConversation(new ConversationHandler(from));
			if (from.latestMessage()!=null)
				session.setMessageType(MessageReceiver.getMessageType(from.latestMessage().messageContent().type()));
			return session;
		}
	}
	class MemberToMemberInfo implements Func<Member,MemberInfo>{
		@Override
		public MemberInfo handler(Member from) {
			MemberInfo info=new MemberInfo();
			info.setOpenId(from.user().openId());
			return info;
		}
	}
	private ConversationService conversationService = IMEngine.getIMService(ConversationService.class);
	public ConversationServiceHandler(){
		conversation_to_session=new ConversationToSession();
	}
	public void getConversationList(final IMCallback<List<SessionInfo>,Integer,String> phandler){
		conversationService.listConversations(new Callback<List<Conversation>>() {
			@Override
			public void onSuccess(List<Conversation> arg0) {
				List<SessionInfo> list= Functional.each(filterNormalConversation(arg0),conversation_to_session);
				phandler.success(list);
			}
			@Override
			public void onProgress(List<Conversation> arg0, int arg1) {
				phandler.process(arg1);
			}
			@Override
			public void onException(String arg0, String arg1) {
				phandler.fail(arg0);
			}
		},Integer.MAX_VALUE, Conversation.ConversationType.GROUP|Conversation.ConversationType.CHAT);
	}
	private List<Conversation> filterNormalConversation(List<Conversation> arg0){
		List<Conversation>  mlist=new ArrayList<Conversation>();
		for(Conversation item:arg0){
			if(item.status()==ConversationStatus.NORMAL){
				mlist.add(item);
			}
		}
		return mlist;
	}
	public void listnerConversationStatusChange(final Action<List<SessionInfo>> callback){
		conversationService.addConversationChangeListener(new ConversationChangeListener() {
			@Override
			public void onStatusChanged(List<Conversation> list) {
				List<SessionInfo> mlist= Functional.each(list,conversation_to_session);
				callback.handler(mlist);
			}
		});
	}
	public void getSessionInfoById(String conversationId,final IMCallback<SessionInfo,Integer,String> callback){
		conversationService.getConversation(new Callback<Conversation>() {
			@Override
			public void onSuccess(Conversation arg0) {
				callback.success(conversation_to_session.handler(arg0));
			}
			@Override
			public void onProgress(Conversation arg0, int arg1) {
				callback.process(arg1);
			}
			@Override
			public void onException(String arg0, String arg1) {
				callback.fail(arg0);
			}
		},conversationId);
	}
	public void getMembers(String conversationId,int offset,int count,final Action<List<MemberInfo>> callback){
		conversationService.listMembers(new Callback<List<Member>>() {
			@Override
			public void onSuccess(List<Member> arg0) {
				callback.handler(Functional.each(arg0, new MemberToMemberInfo()));
			}
			@Override
			public void onProgress(List<Member> arg0, int arg1) {
			}
			@Override
			public void onException(String arg0, String arg1) {
				// TODO Auto-generated method stub

			}
		},conversationId,offset,count);
	}
	public void addGroupMemConversation(String msg,String conversationId,final IMCallback<List<Long>, List<Long>, String> callback, Long...uids){
		Message message = IMEngine.getIMService(MessageBuilder.class).buildTextMessage(msg);//message.atOpenIds()
		conversationService.addMembers(new Callback<List<Long>>() {
			@Override
			public void onProgress(List<Long> data, int progress) {
				callback.process(data);
			}
			@Override
			public void onSuccess(List<Long> data) {
				callback.success(data);
			}
			@Override
			public void onException(String code, String reason) {
				callback.fail(code);
			}
		}, conversationId, message, uids);
	}
	public void createConversation(String title,String iocn,String pmsg,int type,final IMCallback<SessionInfo,Integer, String> callback, Long... uids){
		Message message = IMEngine.getIMService(MessageBuilder.class).buildTextMessage(pmsg);
		conversationService.createConversation(new Callback<Conversation>() {
			@Override
			public void onSuccess(final Conversation arg0) {
				callback.success(conversation_to_session.handler(arg0));
			}
			@Override
			public void onProgress(Conversation arg0, int arg1) {
				callback.process(arg1);
			}
			@Override
			public void onException(String arg0, String arg1) {
				callback.fail(arg0);
			}
		}, title, iocn, message,type, uids);
	}
	public void removeMembers(String conversationId,String groupMsg,final IMCallback<List<Long>,List<Long>,String> callback,Long[] openIds){
		Message message = IMEngine.getIMService(MessageBuilder.class).buildTextMessage(groupMsg);
		conversationService.removeMembers(new Callback<List<Long>>() {
			@Override
			public void onSuccess(List<Long> arg0) {
				callback.success(arg0);
			}
			@Override
			public void onProgress(List<Long> arg0, int arg1) {
				callback.process(arg0);
			}
			@Override
			public void onException(String arg0, String arg1) {
				callback.fail(arg0);
			}
		},conversationId,message,openIds);
	}
}

