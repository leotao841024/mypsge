package service;

import android.content.Context;
import android.graphics.Bitmap;

import com.alibaba.wukong.im.Message;
import com.alibaba.wukong.im.MessageContent;

import comm.CommHelper;
import helper.ImageLoadAsy;
import manager.MessageReceiver;

public class SingleTalking extends BaseTalking {
	public SingleTalking(Context context) {
		super(context);
	}
	// 是公共信息
	boolean iscommMsg(Message mitem) {
		String key = mitem.extension("flag");
		return key != null && key.equals("FRIEND_APPLY");
	}
	// 是别人发的特殊信息同意请求的信息
	boolean isSpecialMsg(String order, String sendid) {
		return order.equals(CommHelper.agreeMsg) && !mmid.equals(sendid);
	} 
	// 是自己发的特殊信息(暂时为请求同意)
	boolean isMineSendAgreeMsg(String order, String sendid) {
		return order.equals(CommHelper.agreeMsg) && mmid.equals(sendid);
	}

	boolean isMyNormalMsg(String order, String sendid) {
		return mmid.equals(sendid) && order.equals(CommHelper.normalMsg);
	}

	boolean isFriendNormalMsg(Message mitem) {
		return mmid.equals(mitem.conversation().getPeerId() + "");
	}

	boolean isDelMsg(String order, String senderid) {
		return order.equals(CommHelper.deleteMsg) && !mmid.equals(senderid);
	}

	boolean isMyDelMsg(String order, String senderid) {
		return order.equals(CommHelper.deleteMsg) && mmid.equals(senderid);
	}
	
	@Override
	public void hanlder(final Message mitem) {
		final String sendid = mitem.senderId() + "";
		if(mitem.messageContent().type()== MessageContent.MessageContentType.IMAGE){
			final String url= MessageReceiver.getMessageContent(mitem);
			String filenm=url.split("/")[url.split("/").length-1];
			ImageLoadAsy loadimageasy =new ImageLoadAsy(new ImageLoadAsy.ImageLodeCallback() {
				@Override
				public void handler(Bitmap map) {
					if (map != null) {
						sendBroacaste("group", mitem.conversation().conversationId() + "");
					}
				}
			},url,ImageLoadAsy.LoadType.FILTER,filenm);
			loadimageasy.setIsleft(!(mmid.equals(sendid)));
			loadimageasy.execute(filenm);
			if(!mmid.equals(sendid)){mitem.conversation().addUnreadCount(1);}
		}else {
			String content = MessageReceiver.getMessageContent(mitem);
			String order = getOrderMsg(content);
			if (iscommMsg(mitem)) {
				getOneFriend(mitem.extension("mid"), "new", new MessageCallback() {
					@Override
					public void handler() {
						mitem.conversation().addUnreadCount(1);
//					sendBroacaste("single", mitem.extension("mid"));
						sendBroacaste("single", mitem.conversation().conversationId() + "");
					}

					@Override
					public void failed() {
						// TODO Auto-generated method stub
					}
				});
			} else if (isSpecialMsg(order, sendid)) {
				mitem.conversation().resetUnreadCount();
				getOneFriend(sendid, "old", new MessageCallback() {
					@Override
					public void handler() {
						mitem.conversation().addUnreadCount(1);
						sendBroacaste("single", mitem.conversation().conversationId() + "");
					}
					@Override
					public void failed() {
						// TODO Auto-generated method stub
					}
				});
			} else if (isMineSendAgreeMsg(order, sendid)) {
				mitem.conversation().resetUnreadCount();
				mitem.conversation().addUnreadCount(1);
				sendBroacaste("single", mitem.conversation().conversationId() + "");
			} else if (isDelMsg(order, sendid)) {
				delFriend(mitem.senderId() + "", "old");
				clearConversation(mitem.conversation());
				sendBroacaste("single", mitem.conversation().conversationId() + "");
			} else if (isMyDelMsg(order, sendid)) {
				clearConversation(mitem.conversation());
			} else if (isMyNormalMsg(order, sendid)) {
				sendBroacaste("single", mitem.conversation().conversationId() + "");
			} else if (isOtherUserMsgChange(order, sendid)) {
				//mitem.conversation().addUnreadCount(1);
				downUserUpdateMsg(sendid);
			} else if (isMyMsgChange(order, sendid)) {
			} else {
				mitem.conversation().addUnreadCount(1);
				sendBroacaste("single", mitem.conversation().conversationId() + "");
			}
		}
	}
	
	void downUserUpdateMsg(final String pid){
		//String sql="delete from apm_sys_friend where mid="+pid+" and typ='old'";
		//db.query(sql);
		db.open();
		db.delete("apm_sys_friend", " mid=? and typ=?",new String[]{pid,"old"});
		db.close();
		getOneFriend(pid, "old", new MessageCallback() {
			@Override
			public void handler() {
				sendBroacaste("single", pid + "");
			}
			@Override
			public void failed() {
				// TODO Auto-generated method stub 
			}
		});
	}
}
