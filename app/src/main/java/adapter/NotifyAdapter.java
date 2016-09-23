package adapter;

import java.util.List;

import com.example.renrenstep.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import bean.Notify;

public class NotifyAdapter extends BaseAdapter {
	private Context context;
	private List<Notify> lists;
	private LayoutInflater inflater;
	public NotifyAdapter() {
		super();
	}

	public NotifyAdapter(Context context, List<Notify> lists) {
		super();
		this.context = context;
		this.lists = lists;
		inflater = LayoutInflater.from(context);
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return lists==null?0:lists.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return lists.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		ViewHolder holder = null;
		if(convertView==null){
			convertView = inflater.inflate(R.layout.list_item_notify, null);
			holder = new ViewHolder();
			convertView.setTag(holder);
		}else{
			holder = (ViewHolder) convertView.getTag();
		}
		Notify notify = lists.get(position);
		holder.tv_title = (TextView)convertView.findViewById(R.id.tv_title);
		holder.tv_time = (TextView)convertView.findViewById(R.id.tv_time);
		String title = notify.getTitle();
		String time = notify.getTime();
		holder.tv_title.setText(title);
		holder.tv_time.setText(time);
		return convertView;
	}
	public void refresh(List<Notify> lists){
		this.lists = lists;
		notifyDataSetChanged();
	}
	private class ViewHolder{
		TextView tv_title;
		TextView tv_time;
	}
}




