package adapter;

import java.io.File;
import java.util.List;

import com.example.renrenstep.R;

import bean.Customer;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class CusAdapter extends BaseAdapter {
	private List<Customer> mlist;
	private Context context;
	private LayoutInflater inflater;
	public CusAdapter(Context context) {
		// TODO Auto-generated constructor stub
		this.context=context;
		inflater=LayoutInflater.from(context);
	}
	
	public void setSource(List<Customer> mlist){
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
		ViewHolder view=new ViewHolder();
		if(convertView==null){
			convertView=inflater.inflate(R.layout.mile_list_item,null);
			view.iv_icon=(view.CircleImageView)convertView.findViewById(R.id.iv_icon);  
			view.nm=(TextView)convertView.findViewById(R.id.nm); 
			convertView.setTag(view);
		}else{
			
			view=(ViewHolder)convertView.getTag();
		}
		tools.FileUtils file=new tools.FileUtils("stepic");
		String dir = file.getFilePath();
		view.nm.setText(mlist.get(position).getNc());  
		File files=new File(dir+mlist.get(position).getAvatar());
		if(files.exists()){
			Bitmap bitmap=BitmapFactory.decodeFile(dir+mlist.get(position).getAvatar()); 
			view.iv_icon.setImageBitmap(bitmap); 
		}
		return convertView;
	}
	
	class ViewHolder
	{
		view.CircleImageView iv_icon;
		TextView nm; 
	}
	
}
