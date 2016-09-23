package adapter;

import helper.ImageLoadAsy;
import helper.ImageLoadAsy.ImageLodeCallback;
import java.util.List;
import bean.Customer;
import com.example.renrenstep.R;
import constant.Cons;
import android.content.Context; 
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter; 
import android.widget.ImageView;

public class GroupMemAdapter extends BaseAdapter {
	public interface IGroupOpration {
		void remove(int userid);
		void beginOpration();
	}
	private List<Customer> mlist;
	private LayoutInflater flater;
	private boolean isRemoveOpration;
	private IGroupOpration opration; 
	public GroupMemAdapter(Context context,IGroupOpration opration) {
		this.flater = LayoutInflater.from(context); 
		this.opration = opration; 
	} 
	private String userid="";
	public void setSource(List<Customer> mlist){
		this.mlist=mlist;
	}
	public void setRemoveOpration(boolean param){
		this.isRemoveOpration = param;
	}
	public void setUserid(String userid){
		this.userid=userid;
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
		//TODO 删除好友
		final ViewHolder holder;
		Customer mobj=mlist.get(position);
		if(convertView==null){
			convertView = flater.inflate(R.layout.group_item, null);
			holder=new ViewHolder();
			holder.img=(view.CircleImageView)convertView.findViewById(R.id.img_group);
			holder.removeiocn=(ImageView)convertView.findViewById(R.id.img_remove_mem);
			convertView.setTag(holder);
		}else{
			holder=(ViewHolder)convertView.getTag();
		}
		final int pid=mobj.getId();
		String imgnm=mlist.get(position).getAvatar();
		if(imgnm!=null&&!imgnm.equals("")){
			ImageLoadAsy imgloadasy=new ImageLoadAsy(new ImageLodeCallback(){ 
				@Override
				public void handler(Bitmap map) { 
					if(map!=null)
						holder.img.setImageBitmap(map); 
				} 
			},Cons.DONW_PIC+imgnm, ImageLoadAsy.LoadType.NORMAL,imgnm);
			imgloadasy.execute(imgnm);
		}else if(pid==0){
			holder.img.setImageResource(R.drawable.add_group_mem);
		}else if(pid==-1){
			holder.img.setImageResource(R.drawable.remove_group_mem);
			holder.img.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View arg0) {
					opration.beginOpration();
				}
			});
		}
		if(isRemoveOpration){
			if(pid==0||pid==-1||(pid+"").equals(userid)){
				holder.img.setVisibility(View.INVISIBLE);
				holder.removeiocn.setVisibility(View.INVISIBLE);
			}else{
				holder.img.setVisibility(View.VISIBLE);
				holder.removeiocn.setVisibility(View.VISIBLE);
				holder.removeiocn.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View arg0) {
						opration.remove(pid);
					}
				});
			}
		}else{
			holder.img.setVisibility(View.VISIBLE);
			holder.removeiocn.setVisibility(View.GONE);
		}
		return convertView;
	}
	
	void removeDesc(int pid){
		
	} 
	class ViewHolder{
		view.CircleImageView img;
		ImageView removeiocn;
	}
}
