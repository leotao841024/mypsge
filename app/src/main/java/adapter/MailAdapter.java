package adapter;

import helper.ImageLoadAsy;
import helper.ImageLoadAsy.ImageLodeCallback;

import java.util.List;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import bean.Customer;

import com.example.renrenstep.R;

import constant.Cons;

public class MailAdapter extends BaseAdapter {
	private List<Customer> mlist;
	private Context context;
	private LayoutInflater inflater;
	private List<Customer> groupMem;
//	private String basePath="";
//	private ImageCacheManger imagecached;
	public MailAdapter(List<Customer> mlist, Context context) {
		this.mlist = mlist;
		this.context = context;
		inflater = LayoutInflater.from(context);
//		utils.FileUtils file = new utils.FileUtils("stepic");
//		basePath = file.getFilePath();
//		imagecached=new ImageCacheManger("stepic");
	}
	public void setCompareList(List<Customer> groupMem) {
		this.groupMem = groupMem;
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
		if (convertView == null) {
			view = new ViewHolder();
			convertView = inflater.inflate(R.layout.mile_list_item, null);
			view.iv_icon = (view.CircleImageView) convertView.findViewById(R.id.iv_icon);
			view.nm = (TextView) convertView.findViewById(R.id.nm);
			view.layout_selmem = (RelativeLayout) convertView.findViewById(R.id.layout_selmem);
			view.img_sel=(ImageView)convertView.findViewById(R.id.img_sel);
			convertView.setTag(view);
		} else {
			view = (ViewHolder) convertView.getTag();
		}
		if(groupMem!=null){
			view.layout_selmem.setVisibility(View.VISIBLE);
			boolean isgroupman=isGroupMember(mlist.get(position).getId())||mlist.get(position).isIsgroupmem();
			view.img_sel.setImageResource(isgroupman?R.drawable.group_mem_sel:R.drawable.group_mem_unsel);
			mlist.get(position).setIsgroupmem(isgroupman);
		}
		view.nm.setText(mlist.get(position).getNc());//view.iv_icon
		String imgnm = mlist.get(position).getAvatar();
		if(imgnm!=null&&!imgnm.equals("")){
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
			ImageLoadAsy imgloadasy=new ImageLoadAsy(new ImageLodeCallback(){ 
				@Override
				public void handler(Bitmap map) { 
					if(map!=null)
						view.iv_icon.setImageBitmap(map); 
				}
			},Cons.DONW_PIC+imgnm, ImageLoadAsy.LoadType.NORMAL,imgnm);
			imgloadasy.execute(imgnm);
		}
		return convertView;
	}
	
	boolean isGroupMember(int memid){
		for(Customer item :groupMem){
			if(item.getId()==memid){
				return true;
			}
		}
		return false;
	}

	class ViewHolder {
		view.CircleImageView iv_icon;
		TextView nm;
		RelativeLayout layout_selmem;
		ImageView img_sel; 
	}
}
