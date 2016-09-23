package adapter;

import java.util.List;

import com.example.renrenstep.R;

import bean.Customer; 
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class GroupAdapter extends BaseAdapter {
	private List<Customer> mlist;
	private Context context;
	private LayoutInflater inflater;
	public GroupAdapter(Context context) {
		this.context=context;
		inflater=LayoutInflater.from(context);
	}
	
	public void setSource(List<Customer> mlist){
		this.mlist = mlist;
	}
	
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return mlist.size();
	}
	
	@Override
	public Object getItem(int arg0) {
		// TODO Auto-generated method stub
		return mlist.get(arg0);
	}
	
	@Override
	public long getItemId(int arg0) {
		// TODO Auto-generated method stub
		return 0;
	}
	
	@Override
	public View getView(int arg0, View arg1, ViewGroup arg2) {
		// TODO Auto-generated method stub
		arg1=inflater.inflate(R.layout.group_item, null);
		view.CircleImageView img_group = (view.CircleImageView)arg1.findViewById(R.id.img_group);
		TextView mem_title=(TextView)arg1.findViewById(R.id.mem_title);
		mem_title.setText(mlist.get(arg0).getNc());
		return arg1;
	}
	
}
