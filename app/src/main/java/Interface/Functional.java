package Interface;
import com.alibaba.wukong.im.Message;

import java.util.ArrayList;
import java.util.List;

import bean.MessageInfo;

public class Functional {
	public interface Action<T> {
		void handler(T t);
	}
	public interface Func<TFrom,TTo> {
		TTo  handler(TFrom from);
	}
	public static<TTo,TFrom> List<TTo> each(List<TFrom> froms,Func<TFrom,TTo> func){
		List<TTo> mlist=new ArrayList<TTo>();
		for(TFrom item:froms){
			TTo obj= func.handler(item);
			if(obj!=null){
				mlist.add(obj);
			}
		}
		return mlist;
	}

	public interface IMCallback<T,T1,T2>{
		void success(T arg0);
		void process(T1 arg0);
		void fail(T2 arg0);
	}
	public interface IConversationHandler{
		void resetUnreadCount();
		void addUnreadCount(int num);
		void listPreviousMessages(Message msg,int pagesize, Action<List<MessageInfo>> callback);
		void sendMsg(String text, MessageInfo.MssageType type, IMCallback<MessageInfo, Integer, String> callback);
		void updateTitle(String newTitle, String sendMsg, IMCallback<Void, Void, String> callback);
		void disband(IMCallback<Void, Void, String> callback);
		void quit(String pmsg, IMCallback<Void, Void, String> callback);
		void stayOnTop(boolean istop, Action<Long> callback);

	}
}
