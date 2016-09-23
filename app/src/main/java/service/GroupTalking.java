package service;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;

import com.alibaba.wukong.Callback;
import com.alibaba.wukong.im.ConversationService;
import com.alibaba.wukong.im.IMEngine;
import com.alibaba.wukong.im.Member;
import com.alibaba.wukong.im.Message;
import com.alibaba.wukong.im.MessageContent;

import java.util.ArrayList;
import java.util.List;

import comm.CommHelper;
import helper.ImageLoadAsy;
import manager.MessageReceiver;

public class GroupTalking extends BaseTalking {
	// 是否是创建群组的命令
	private Context context;

	boolean isCreateGroupMsg(String order) {
		return order.equals(CommHelper.createGroupMsg);
	}

	boolean isAddMemGroup(String order) {
		return order.equals(CommHelper.addMemGroup);
	}

	// 是否是我发的信息
	boolean isMyGroupMsg(String sendid) {
		return mmid.equals(sendid);
	}

	public GroupTalking(Context context) {
		super(context);
		this.context = context;
	}

	@Override
	public void hanlder(final Message mitem) {
		// TODO Auto-generated method stub
		String content = MessageReceiver.getMessageContent(mitem);//
		String order = getOrderMsg(content);
		final String sendid = mitem.senderId() + "";
		if(mitem.messageContent().type()== MessageContent.MessageContentType.IMAGE){
			final String url=MessageReceiver.getMessageContent(mitem);
			String filenm=url.split("/")[url.split("/").length-1];
			ImageLoadAsy loadimageasy = new ImageLoadAsy(new ImageLoadAsy.ImageLodeCallback() {
				@Override
				public void handler(Bitmap map) {
					if (map != null) {
						sendBroacaste("group", mitem.conversation().conversationId() + "");
 					}
				}
			},url,ImageLoadAsy.LoadType.FILTER,filenm);
			loadimageasy.setIsleft(!(mmid.equals(sendid)));
			if(!mmid.equals(sendid)){mitem.conversation().addUnreadCount(1);}
			return;
		}else {
			if (isCreateGroupMsg(order)) {// 获取用户openid并且下载用户信息 创建群的消息
				mitem.conversation().addUnreadCount(1);
				getGroupMem(mitem.conversation().conversationId());
			} else if (isAddMemGroup(order)) {// 增加会员的消息
				getGroupMem(mitem.conversation().conversationId());
			} else if (isMyMsgChange(order, sendid)) {
			} else if (isOtherUserMsgChange(order, sendid)) {
				downUserUpdateMsg(sendid, mitem.conversation().conversationId());
			} else if (isMyGroupMsg(sendid)) {// 我发的信息不加未读
				sendBroacaste("group_my_msg", mitem.conversation().conversationId() + "");
			} else {// 别人发的信息
				mitem.conversation().addUnreadCount(1);
				sendBroacaste("group_normal", mitem.conversation().conversationId() + "");
			}
		}
	}
	void downUserUpdateMsg(final String pid,final String conversationId){
		db.open();
		db.delete("apm_sys_friend", " mid=? and typ=?",new String[]{pid,"group"});
		db.close();
		getOneFriend(pid, "group", new MessageCallback() {
			@Override
			public void handler() {
				sendBroacaste(CommHelper.UPDATEMEMMSG, conversationId);
			}
			@Override
			public void failed() {

			}
		});
	}
	void getGroupMem(final String conversationId) {
		IMEngine.getIMService(ConversationService.class).listMembers(
				new Callback<List<Member>>() {
					@Override
					public void onProgress(List<Member> data, int progress) {

					}
					@Override
					public void onSuccess(List<Member> data) {
						List<Long> allMemId = getGroupMemId(data);
						final String uidstr = getMemIdStr(allMemId);
						List<Long> toMemId = getDbHasMem(uidstr);
						List<Long> downMemId = getChaMemId(allMemId, toMemId);

						if(downMemId.size()==0){
							sendBroacaste(CommHelper.UPDATEMEMMSG, conversationId);
						}else{
							downLoadMemMsg(downMemId,conversationId);
						}
					}
					@Override
					public void onException(String code, String reason) {
					}
				}, conversationId, 0, 100);
	}

	void downLoadMemMsg(List<Long> mids,final String conversationId){
		for (long item : mids) {
			getOneFriend(item + "", "group",new MessageCallback() {
				@Override
				public void handler() {
					sendBroacaste(CommHelper.UPDATEMEMMSG, conversationId);
				}
				@Override
				public void failed() {
				}
			});
		}
	}

	List<Long> getChaMemId(List<Long> all, List<Long> tolist) {
		List<Long> cha = new ArrayList<Long>();
		for (long item : all) {
			if (!tolist.contains(item)) {
				cha.add(item);
			}
		}
		return cha;
	}

	List<Long> getGroupMemId(List<Member> data) {
		List<Long> mlist = new ArrayList<Long>();
		for (Member item : data) {
			mlist.add(item.user().openId());
		}
		return mlist;
	}

	String getMemIdStr(List<Long> plist) {
		StringBuilder sb = new StringBuilder();
		for (long item : plist) {
			sb.append(item + ",");
		}
		return sb.substring(0, sb.length() - 1);
	}

	List<Long> getDbHasMem(String param) {
		List<Long> mlist = new ArrayList<Long>();
		db.open();
		String sql = "select mid from apm_sys_friend where mid in(" + param+ ") and typ='group'";
		Cursor cursor = db.query(sql);
		while (cursor.moveToNext()) {
			mlist.add(cursor.getLong(cursor.getColumnIndex("mid")));
		}
		db.close();
		return mlist;
	}
}
