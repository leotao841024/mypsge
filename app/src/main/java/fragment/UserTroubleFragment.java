package fragment;

import helper.BGHelper;
import helper.SPHelper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import tools.ToastManager;

import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.renrenstep.R;
import com.google.gson.Gson;

import helper.BaseAsyncTask;
import comm.CommHelper;
import constant.Cons;

public class UserTroubleFragment extends Fragment implements OnClickListener {
	private View view;
	private Button bt_last, bt_next;
	private TextView tv_title;
	private RelativeLayout layout_actionbar;
	private UserAgeFragment.IOnPagerScrollListener onPagerScrollListener;
	private CheckBox checkBox1, checkBox2, checkBox5;
	private String[] troubles;
	private Dialog dialog;
	public UserTroubleFragment(){}
	@SuppressLint("ValidFragment")
	public UserTroubleFragment(UserAgeFragment.IOnPagerScrollListener onPagerScrollListener) {
		super();
		this.onPagerScrollListener = onPagerScrollListener;
	}

	@Override
	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		view = inflater.inflate(R.layout.fragment_user_trouble, null); 
		initView();
		initData();
		return view;
	} 
	private void initView() {
		// TODO Auto-generated method stub
		String sex = SPHelper.getDetailMsg(getActivity(), Cons.APP_SEX,
				getString(R.string.appsex_man));
		layout_actionbar = (RelativeLayout) view
				.findViewById(R.id.layout_actionbar);
		tv_title = (TextView) view.findViewById(R.id.tv_title);
		bt_last = (Button) view.findViewById(R.id.bt_last);
		bt_last.setBackgroundDrawable(getResources().getDrawable(
				BGHelper.setbgButton(getActivity(), sex)));
		bt_last.setOnClickListener(this);
		bt_next = (Button) view.findViewById(R.id.bt_next);
		bt_next.setBackgroundDrawable(getResources().getDrawable(
				BGHelper.setbgButton(getActivity(), sex)));
		bt_next.setOnClickListener(this);
		int background = BGHelper.setBackground(getActivity(), SPHelper
				.getDetailMsg(getActivity(), Cons.APP_SEX, getResources()
						.getString(R.string.appsex_man)));
		tv_title.setTextColor(getResources().getColor(background));
		layout_actionbar.setBackgroundResource(background);

		checkBox1 = (CheckBox) view.findViewById(R.id.checkBox1);
		checkBox1.setTextColor(getResources().getColor(background));
		checkBox1.setButtonDrawable(BGHelper.setbgCheckBox(getActivity(), sex));
		checkBox1.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton buttonView,
					boolean isChecked) {
				// TODO Auto-generated method stub
				if (isChecked) {
					SPHelper.setDetailMsg(getActivity(), "GXB", isChecked);
					checkBox5.setChecked(false);
				}
			}
		});
		checkBox2 = (CheckBox) view.findViewById(R.id.checkBox2);
		checkBox2.setTextColor(getResources().getColor(background));
		checkBox2.setButtonDrawable(BGHelper.setbgCheckBox(getActivity(), sex));
		checkBox2.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton buttonView,
					boolean isChecked) {
				// TODO Auto-generated method stub
				if (isChecked) {
					SPHelper.setDetailMsg(getActivity(), "TNB", isChecked);
					checkBox5.setChecked(false);
				}
			}
		});
		checkBox5 = (CheckBox) view.findViewById(R.id.checkBox5);
		checkBox5.setTextColor(getResources().getColor(background));
		checkBox5.setButtonDrawable(BGHelper.setbgCheckBox(getActivity(), sex));
		checkBox5.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				// TODO Auto-generated method stub
				if(isChecked){
					checkBox1.setChecked(false);
					checkBox2.setChecked(false);
				}
			}
		});
		dialog=CommHelper.createLoadingDialog(getActivity(), "",SPHelper.getDetailMsg(getActivity(), Cons.APP_SEX,"M"));
	}
	
	void initData(){
		checkBox1.setChecked(SPHelper.getDetailMsg(getActivity(), "GXB", "").equals("")?false:true);
		checkBox2.setChecked(SPHelper.getDetailMsg(getActivity(), "TNB", "").equals("")?false:true); 
	}
	
	private void update(){
		new BaseAsyncTask(Cons.SPORT_TARGET, new HashMap<String, Object>(), BaseAsyncTask.HttpType.Post, "" , getActivity()) {
			
			@Override
			public void handler(String param) {
				Log.i("Cons.SPORT_TARGET", param);
				if(param!=null){
					try {
						JSONObject jsonObject = new JSONObject(param);
						int status = jsonObject.getInt("status");
						if(status==0){
							String totalsteps = jsonObject.getString("totalsteps");
							String faststeps = jsonObject.getString("faststeps");
							String calorie = jsonObject.getString("calorie"); 
							SPHelper.setDetailMsg(getActivity(), "totalsteps",Integer.parseInt(totalsteps));
							SPHelper.setDetailMsg(getActivity(), "faststeps", Integer.parseInt(faststeps));
							SPHelper.setDetailMsg(getActivity(), "calorie",Integer.parseInt(calorie));
							
							Intent intent = new Intent();
							intent.setAction(Cons.STEPS_RECEIVER);
							getActivity().sendBroadcast(intent);
							CommHelper.insert_visit(getActivity(),"reportgoalpg");
							onPagerScrollListener.OnPaterScroll(6);
						}
						else{
							ToastManager.show(getActivity(), getResources().getString(R.string.wangluoyic),1000);
						}
					} catch (JSONException e) {
						ToastManager.show(getActivity(), getResources().getString(R.string.wangluoyic),1000);
					}
				}
			}
		}.execute("");
	}
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.bt_last:
			onPagerScrollListener.OnPaterScroll(4);
			break;
		case R.id.bt_next:
			Map<String, Object> maps = new HashMap<String, Object>();
			String gender = SPHelper.getDetailMsg(getActivity(), Cons.APP_SEX,
					getString(R.string.appsex_man));
			int age = SPHelper.getDetailMsg(getActivity(), 
					"age", 25);
			int height = SPHelper.getDetailMsg(getActivity(),
					"height", 170);
			int weight = SPHelper.getDetailMsg(getActivity(),
					"weight", 60);
			List<String> list=new ArrayList<String>();
			if(checkBox1.isChecked()){
				list.add("GXB");
			}
			if(checkBox2.isChecked()){
				list.add("TNB") ;
			}
			Gson gson = new Gson();
			String[] para=list.toArray(new String[list.size()]);
			maps.put("gender", gender);
			maps.put("age", age);
			maps.put("height", height);
			maps.put("weight", weight);
			if(para.length!=0)
				maps.put("dis",gson.toJson(para));
			dialog.show();
			new BaseAsyncTask(Cons.UPDATE_INFO, maps, BaseAsyncTask.HttpType.Post, "",
					getActivity()) {
 
				@Override
				public void handler(String param) {
					dialog.dismiss(); 
					if (param != null && param.length() != 0) {
						try {
							JSONObject jsonObject = new JSONObject(param);
							int status = jsonObject.getInt("status");
							if(status==0){
								update();
							}
							else{
								Toast.makeText(getActivity(), getString(R.string.upfail), Toast.LENGTH_LONG).show();
							}
						} catch (JSONException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				}
			}.execute("");
			break;

		default:
			break;
		}
	}
}
