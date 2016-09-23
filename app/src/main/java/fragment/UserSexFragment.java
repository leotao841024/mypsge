package fragment;

import helper.BGHelper;
import helper.SPHelper;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.renrenstep.R;
import com.example.renrenstep.RegisterActivity;

import comm.CommHelper;
import constant.Cons;

public class UserSexFragment extends Fragment implements OnClickListener{
	interface OnRefreshListener {
		void OnRefresh();
	};
	private OnRefreshListener onRefreshListener;
	private View view;
	private Button bt_next;
	private TextView tv_title;
	private ImageView iv_man, iv_woman;
	private UserAgeFragment.IOnPagerScrollListener onPagerScrollListener;
	RegisterActivity registerActivity;
	private String sex="M";
	public UserSexFragment(){}
	@SuppressLint("ValidFragment")
	public UserSexFragment(UserAgeFragment.IOnPagerScrollListener onPagerScrollListener, OnRefreshListener onRefreshListener,RegisterActivity registerActivity) {
		super();
		this.onPagerScrollListener = onPagerScrollListener;
		this.onRefreshListener = onRefreshListener;
		this.registerActivity = registerActivity;
	}

	@Override
	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		view = inflater.inflate(R.layout.fragment_user_sex, null);
		init(); 
		return view;
	}

	private void init() {
		// TODO Auto-generated method stub
		bt_next = (Button)view.findViewById(R.id.bt_next);
		bt_next.setOnClickListener(this);
		tv_title = (TextView)view.findViewById(R.id.tv_title);
		iv_man = (ImageView)view.findViewById(R.id.iv_man);
		iv_man.setOnClickListener(this);
		iv_woman = (ImageView)view.findViewById(R.id.iv_woman);
		iv_woman.setOnClickListener(this);
		tv_title.setTextColor(getResources().getColor(R.color.white));
		//SPHelper.setDetailMsg(getActivity(), Cons.APP_SEX, getString(R.string.appsex_man));
		setColor("M");
	}
	
	private void setColor(String gender){
		//String sex = SPHelper.getDetailMsg(getActivity(), Cons.APP_SEX, "M");
		String sex=gender;
		int background = BGHelper.setBackground(getActivity(), sex);
		LinearLayout layout_sex = (LinearLayout)view.findViewById(R.id.layout_sex);
		layout_sex.setBackgroundColor(getResources().getColor(background));
		bt_next.setTextColor(getResources().getColor(background));
	}
	
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.bt_next:
			String gender= SPHelper.getDetailMsg(getActivity(), Cons.APP_SEX,"");
			if(gender.equals("")||gender.equals("null")){
				 SPHelper.setDetailMsg(getActivity(), Cons.APP_SEX,getResources().getString(R.string.appsex_man));
			}
			CommHelper.insert_visit(getActivity(),"agepg"); 
			onPagerScrollListener.OnPaterScroll(2);
			onRefreshListener.OnRefresh();
			registerActivity.loadPager(2);
			break;
		case R.id.iv_man:
			SPHelper.setDetailMsg(getActivity(), Cons.APP_SEX, getResources().getString(R.string.appsex_man));
			//MyApplication.getInstance().setBackground(R.color.appcolor_blue);
			setColor("M");
			break;
		case R.id.iv_woman:
			SPHelper.setDetailMsg(getActivity(), Cons.APP_SEX, getResources().getString(R.string.appsex_women));
			//MyApplication.getInstance().setBackground(R.color.appcolor_red);
			setColor("F");
			break; 
		default:
			break;
		}
	}
	
}
