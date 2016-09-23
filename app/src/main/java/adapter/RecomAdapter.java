package adapter;

import java.util.List;

import com.example.renrenstep.R;

import bean.Recom; 
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class RecomAdapter extends BaseAdapter {
	private List<Recom> list;
	private Context context;  
	public void setList(List<Recom> list) {
		this.list = list;
	}
	private LayoutInflater inflater;
	public RecomAdapter(Context context) {
		// TODO Auto-generated constructor stub
		this.context=context;
		inflater = LayoutInflater.from(context);
	}
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return list.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return list.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if(convertView==null){
			convertView = inflater.inflate(R.layout.list_item_recommed, null); 
		}
		Recom recommed=list.get(position);
		ImageView img=(ImageView) convertView.findViewById(R.id.weather_pic);
		TextView title=(TextView)convertView.findViewById(R.id.title);
		TextView cont=(TextView)convertView.findViewById(R.id.brief);
		img.setImageResource(recommed.getImgid());
		title.setText(recommed.getTitle());
		cont.setText(recommed.getBrief());
		return convertView;
	}
}
