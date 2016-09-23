package adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;
import com.example.renrenstep.R;
import java.util.List;
import bean.Customer;
import constant.Cons;
import helper.ImageLoadAsy;
import helper.ImageLoadAsy.ImageLodeCallback;

public class AcceptAdapter extends BaseAdapter {
	public interface IAddFriendEvent {
		void handler(int cus);
	}
	private List<Customer> mlist;
	private Context context;
	private LayoutInflater inflater;
	private IAddFriendEvent addfriend; 
	public AcceptAdapter(Context context,IAddFriendEvent addfriend) { 
		this.context = context;
		this.inflater=LayoutInflater.from(context);
		this.addfriend=addfriend; 
	} 
	public void setList(List<Customer> mlist){
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
		final ViewHolder view;
		if(convertView==null){
			view = new ViewHolder();
			convertView=inflater.inflate(R.layout.new_friend_item,null);
			view.iv_icon=(view.CircleImageView)convertView.findViewById(R.id.iv_icon);  
			view.nm=(TextView)convertView.findViewById(R.id.nm); 
			view.resume=(TextView)convertView.findViewById(R.id.resume);
			view.btn_accept=(Button)convertView.findViewById(R.id.btn_accept);
			view.hasagree=(TextView)convertView.findViewById(R.id.hasagree);
			convertView.setTag(view);
		}else{
			view=(ViewHolder)convertView.getTag();
		}
		view.nm.setText(mlist.get(position).getNc()); 
		view.resume.setText(mlist.get(position).getWords());
		String imgnm = mlist.get(position).getAvatar();
		ImageLoadAsy imgloadasy=new ImageLoadAsy(new ImageLodeCallback(){ 
			@Override
			public void handler(Bitmap map) { 
				if(map!=null)
					view.iv_icon.setImageBitmap(map); 
			}
		}, Cons.DONW_PIC+imgnm,ImageLoadAsy.LoadType.NORMAL,imgnm );
		imgloadasy.execute(imgnm);
//		if(imgnm!=null&&!imgnm.equals("")){
//			Bitmap bitmap =imagecached.getCachedBitmap(imgnm);
//			if(bitmap==null){
//				imagecached.downData(Cons.DONW_PIC+imgnm, new IdownImageCallback() {
//					@Override
//					public void finished(Bitmap map) {
//						if(map!=null){
//							view.iv_icon.setImageBitmap(map);
//						}
//					}
//				});
//			}else{
//				view.iv_icon.setImageBitmap(bitmap);
//			}
//		}
		final int mid=mlist.get(position).getId();
		view.btn_accept.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) { 
				addfriend.handler(mid);
			}
		}); 
		String typ=mlist.get(position).getState();
		if(typ!=null&&typ.equals("AGREE")){ 
			view.hasagree.setVisibility(View.VISIBLE);
			view.btn_accept.setVisibility(View.GONE);
		}
		return convertView;
	}
	
	class ViewHolder
	{
		view.CircleImageView iv_icon;
		TextView nm; 
		TextView resume;
		Button btn_accept;
		TextView hasagree;
	}

}
