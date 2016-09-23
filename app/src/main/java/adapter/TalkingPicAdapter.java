package adapter;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.wukong.im.Message;
import com.alibaba.wukong.im.MessageContent;
import com.alibaba.wukong.im.message.MessageContentImpl;
import com.example.renrenstep.R;

import java.text.SimpleDateFormat;
import java.util.List;

public class TalkingPicAdapter extends BaseAdapter {
	private List<Message> mlist;
	private Context context;
	public TalkingPicAdapter(Context context,List<Message> mlist) {
		 this.context = context;
		this.mlist = mlist;
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
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) { 
		Message message=mlist.get(position);
		convertView = LayoutInflater.from(context).inflate(R.layout.chatting_item_msg_pic_left, null);
//		ImageView pic_content=(ImageView)convertView.findViewById(R.id.pic_content);
		MessageContent.ImageContent msgContent = (MessageContent.ImageContent) message.messageContent();
		byte[] bitmap = msgContent.getData();
//		pic_content.setImageBitmap(BitmapFactory.decodeByteArray(bitmap, 0, bitmap.length));
		return convertView;
	}
	


}
