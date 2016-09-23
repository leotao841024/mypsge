package groupview;

import java.util.List;

import bean.Customer;

import com.example.renrenstep.R;
import groupview.InfoViewBuilder.EventListener;
import adapter.GroupMemAdapter;
import android.app.Activity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

public class TalkingSettingViewHolder extends SlidingMenuViewHolder {
	private GridView grid_groupmem;
	private ImageView img_settop;
	protected RelativeLayout layout_group;
	private Button btn_groupout;
	private GroupMemAdapter group_adapter;
	private List<Customer> source;
	private SettingCallback<String> setting_listener;
	private String userid="",groupnm="",gender="";
	private  LinearLayout head_layouer;
	private boolean istop;
	public interface SettingCallback<T> extends EventListener<T>{
		void delMem(long openid);
		void setTop();
	}
	public TalkingSettingViewHolder(Activity context, SettingCallback<String> listener) {
		super(context, listener); 
		setting_listener=listener;
	}
	@Override
	protected int getLayoutID() { 
		return R.layout.group_sliding_menu;
	}
	@Override
	protected int getCancelID() {
		return R.id.lay_outer;
	}
	public void setType(int typ){
		
	}
	public void setSource(List<Customer> source,String userid,String groupnm,Boolean istop,String gender){
		this.source = source;
		this.userid = userid;
		this.groupnm = groupnm;
		this.istop = istop;
		this.gender=gender;
	}
	public void initView(){
		group_adapter = new GroupMemAdapter(context,new GroupOpration());
		group_adapter.setSource(source);
		group_adapter.setRemoveOpration(false);
		group_adapter.setUserid(userid);
		grid_groupmem.setAdapter(group_adapter);
		int img_resourceid=istop?gender.equals("M")?R.drawable.toggle_blue:R.drawable.toggle_red:R.drawable.toggle_normal;
		img_settop.setImageDrawable(context.getResources().getDrawable(img_resourceid));
	}
	
	public void dataSourceChange(){
		group_adapter.notifyDataSetChanged();
	}
	public void setColor(int colval){
		head_layouer.setBackgroundColor(colval);
		btn_groupout.setBackgroundColor(colval);
	}
	@Override
	protected void findView(View view) {
		grid_groupmem = (GridView)view.findViewById(R.id.grid_groupmem);
		img_settop = (ImageView)view.findViewById(R.id.img_settop);
		layout_group = (RelativeLayout)view.findViewById(R.id.layout_group);
		btn_groupout = (Button)view.findViewById(R.id.btn_groupout);
		head_layouer=(LinearLayout)view.findViewById(R.id.head_layouer);
		grid_groupmem.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,long arg3) {
				 if(source.get(arg2).getId()==0){
					 listener.onConfirm("");
				 }
			}
		});
		img_settop.setOnClickListener(new  OnClickListener() {
			@Override
			public void onClick(View arg0) {
				
			}
		});
	}
	class GroupOpration implements GroupMemAdapter.IGroupOpration {
		@Override
		public void remove(int userid) {
			setting_listener.delMem((long)userid);
		}
		@Override
		public void beginOpration() {
			group_adapter.setRemoveOpration(true);
			group_adapter.notifyDataSetChanged();
		}
	}
}
