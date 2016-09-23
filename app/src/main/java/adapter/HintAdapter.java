package adapter;

import java.util.List;

import com.example.renrenstep.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class HintAdapter extends BaseAdapter {
	private Context context;
	private List<String> data;
	public static final int RED = 0;
	public static final int GRAY = 1;
	private int color; 
	public HintAdapter(Context context, List<String> data, int color) {
		super();
		this.context = context;
		this.data = data;
		this.color = color;
	} 
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return data==null?0:data.size();
	} 
	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return data.get(position);
	} 
	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	} 
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder;
		if(convertView==null){
			convertView = LayoutInflater.from(context).inflate(R.layout.list_item_hint, null);
			holder = new ViewHolder();
			convertView.setTag(holder);
		}
		else{
			holder = (ViewHolder)convertView.getTag();
		}
		String str = data.get(position);
		holder.imageView = (ImageView)convertView.findViewById(R.id.imageView);
		holder.textView = (TextView)convertView.findViewById(R.id.textView);
		if(color==RED){
			holder.imageView.setImageResource(R.drawable.icon_point_red);
		}
		if(color==GRAY){
			holder.imageView.setImageResource(R.drawable.icon_point_normal);
		}
		holder.textView.setText(str); 
		return convertView;
	} 
	class ViewHolder{
		ImageView imageView;
		TextView textView;
	} 
}
