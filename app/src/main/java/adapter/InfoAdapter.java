package adapter;


import java.util.List;
 

import com.example.renrenstep.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

public class InfoAdapter extends BaseAdapter{
	public interface InfoCallback{
		void handler();
	}
	private boolean isClick = false; 
	private List<String> names;
	private List<String> infomation;
	private InfoCallback[] callbacks;
	private LayoutInflater inflater;
	public InfoAdapter(Context context) {
		super(); 
		inflater = LayoutInflater.from(context);
	}
	public void setNames( List<String> names){
		this.names = names; 
	}
	public void setInfomations( List<String> infomation){
		this.infomation = infomation;
	}
	
	public void setListener(InfoCallback[] callbacks){
		this.callbacks = callbacks;
	}
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return names!=null?names.size():0;
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return names.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}
	public void setOnClickable(boolean isClick){
		this.isClick = isClick;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		ViewHolder holder;
		if(convertView==null){
			convertView = inflater.inflate(R.layout.list_item_info, null);
			holder = new ViewHolder();
			holder.tv_name = (TextView)convertView.findViewById(R.id.tv_name);
			holder.tv_info = (TextView)convertView.findViewById(R.id.tv_info);
			holder.layout_item = (LinearLayout)convertView.findViewById(R.id.layout_item);
			convertView.setTag(holder);
		}else{
			holder = (ViewHolder) convertView.getTag();
		}
		final int index=position;
		holder.layout_item.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				if(callbacks!=null&&callbacks.length>index){
					callbacks[index].handler();
				}
			}
		});
		holder.layout_item.setTag(position);
		String name = names.get(position);
		String info = infomation.get(position);
		holder.tv_name.setText(name);
		holder.tv_info.setText(info);
		return convertView;
	}
	class ViewHolder{
		TextView tv_name;
		TextView tv_info;
		LinearLayout layout_item;
	}
//	@Override
//	public void onClick(View v) {
//		// TODO Auto-generated method stub
//		if(isClick){
//			int position = (Integer) v.getTag();
////			itemClickListener.OnItemClick(position, v);
//		}
//	}
//	public void refreshList(List<String> infomation){
//		this.infomation = infomation;
//		this.notifyDataSetChanged();
//	}
}



