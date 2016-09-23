package adapter;

import constant.Cons;
import helper.ImageLoadAsy;
import helper.ImageLoadAsy.ImageLodeCallback;
import helper.ResourceHelper;
import java.util.List;
import com.example.renrenstep.R;
import bean.Talking;
import view.EmojiconTextView;
import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
 

public class TalkListItemAdapter extends BaseAdapter {
	private List<Talking> mlist;
	private Context context;
	private LayoutInflater inflater;  
	public TalkListItemAdapter(Context context) {
		this.context = context;
		inflater=LayoutInflater.from(context); 
	}
	
	public void setSource(List<Talking> mlist){
		this.mlist=mlist;
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
		return 0;
	}
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		Talking talk=mlist.get(position);
		final ViewHolder view;
		if(convertView==null){
			view=new ViewHolder();
			convertView=inflater.inflate(R.layout.talk_list_item,null);
			view.iv_icon=(view.CircleImageView)convertView.findViewById(R.id.iv_icon); 
			view.noreadnum = (TextView) convertView.findViewById(R.id.noreadnum);
			view.nm=(TextView)convertView.findViewById(R.id.nm);
			view.resume= (EmojiconTextView) convertView.findViewById(R.id.resume);
			view.timer=(TextView) convertView.findViewById(R.id.timer);
//			view.normal_text = (TextView) convertView.findViewById(R.id.normal_text);
			convertView.setTag(view);
		}else{
			view=(ViewHolder)convertView.getTag();
		}
		String msg=mlist.get(position).getNc();
		view.nm.setText(msg.length()>10?msg.substring(0,10)+"...":msg);
		if(mlist.get(position).getNoreadnum()==0){
			view.noreadnum.setVisibility(View.INVISIBLE);
		}else{
			view.noreadnum.setVisibility(View.VISIBLE);
		}
		view.iv_icon.setImageResource(R.drawable.regist_man);
		String filenm=talk.isIsgroup()?talk.getConversationid()+".jpg" : talk.getAvatar(); 
		final boolean isgroup=talk.isIsgroup();
		ImageLoadAsy imgloadasy=new ImageLoadAsy(new ImageLodeCallback(){
			@Override
			public void handler(Bitmap map) {
				if(map!=null){
					view.iv_icon.setImageBitmap(map);
				}else{
					int imgresid=isgroup?R.drawable.qunliao:R.drawable.regist_man;
					view.iv_icon.setImageDrawable(ResourceHelper.getDrawable(imgresid));
				}
			}
		}, Cons.DONW_PIC+filenm, ImageLoadAsy.LoadType.NORMAL,filenm);
		imgloadasy.execute(filenm);

//		String clast = talk.getCompleteLastwors();
//		Log.e("talkItem",clast);
//		if (clast.endsWith(".png")||clast.endsWith(".jpg")||clast.endsWith(".jpeg")){
//			view.resume.setVisibility(View.GONE);
//			view.normal_text.setVisibility(View.VISIBLE);
//			view.normal_text.setText("[图片]");
//		}else {
//			view.resume.setVisibility(View.VISIBLE);
//			view.normal_text.setVisibility(View.GONE);
//			view.resume.setText(talk.getLastWord());
//		}

		view.noreadnum.setText(talk.getNoreadnum() + "");

		view.timer.setText(talk.getTimer());
		return convertView;
	}
	 
	class ViewHolder{
		view.CircleImageView iv_icon;
		TextView noreadnum,nm,timer,normal_text;
		view.EmojiconTextView resume;
	}
}
