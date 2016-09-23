package adapter;


import java.util.List;
 


import com.example.renrenstep.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class MineAdapter extends BaseAdapter implements OnClickListener{

	public interface IOnMyItemClickListener {
		void OnItemClick(int position, View view);
	}

	private IOnMyItemClickListener itemClickListener;
	private Context context;
	private List<String> contents;
	private List<Integer> images;
	private LayoutInflater inflater;
	private List<Integer> expands;
	public MineAdapter(Context context, List<String> contents,
			List<Integer> images, IOnMyItemClickListener itemClickListener) {
		super();
		this.context = context;
		this.contents = contents;
		this.images = images;
		this.itemClickListener = itemClickListener;
		inflater = LayoutInflater.from(context);
	}
	
	public void setNoReadNum(List<Integer> expands){
		this.expands = expands;
	}
	
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return contents!=null?contents.size():0;
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return contents.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		ViewHolder holder;
		if(convertView==null){
			holder = new ViewHolder();
			convertView = inflater.inflate(R.layout.list_item_mine, null);
			holder.layout_mine = (LinearLayout)convertView.findViewById(R.id.layout_mine);
			holder.iv_icon = (ImageView)convertView.findViewById(R.id.iv_icon);
			holder.tv_content = (TextView)convertView.findViewById(R.id.tv_content);
			holder.iv_next = (ImageView)convertView.findViewById(R.id.iv_next);
			holder.txt_noreadnotify = (TextView)convertView.findViewById(R.id.txt_noreadnotify);
//			holder.tv_right_content = (TextView) convertView.findViewById(R.id.tv_right_content);
			convertView.setTag(holder);
		}else{
			holder = (ViewHolder) convertView.getTag();
		}
		if(expands==null){
			holder.txt_noreadnotify.setVisibility(View.GONE);
		}
		else{
			int nonum = expands.get(position);
			holder.txt_noreadnotify.setVisibility(nonum==0?View.GONE:View.VISIBLE);
			holder.txt_noreadnotify.setText(nonum+"");
//			holder.txt_noreadnotify.setBackgroundResource(SPHelper.getDetailMsg(context, "gender", "M").equals("M")?R.drawable.remind_icon:R.drawable.remind_blue_icon);
		}


//		if(position == 0){
//			int currentCoins = SPHelper.getBaseMsg(context,"currentCoins",0);
//			if (currentCoins != 0){
//				holder.tv_right_content.setText("比赛金币："+currentCoins);
//				holder.tv_right_content.setVisibility(View.VISIBLE);
//			}
//		}

		holder.layout_mine.setTag(position);
		holder.layout_mine.setOnClickListener(this);
		String content = contents.get(position);
		Integer image = images.get(position);
		holder.iv_icon.setImageResource(image);
		holder.tv_content.setText(content);

		return convertView;
	}
	class ViewHolder{
		LinearLayout layout_mine;
		ImageView iv_icon;
		TextView tv_content,txt_noreadnotify,tv_right_content;
		ImageView iv_next;

	}
	
	@Override
	public void onClick(View v) {
		int position = (Integer)v.getTag();
		itemClickListener.OnItemClick(position, v);
	}
	
	public void refreshList(List<Integer> images){
		this.images = images;
		this.notifyDataSetChanged();
	}

}







