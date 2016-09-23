package fragment;

import helper.BGHelper;
import helper.SPHelper;
import view.PickerView;
import view.PickerView.onSelectListener;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.renrenstep.R;
import com.example.renrenstep.RegisterActivity;

import comm.CommHelper;
import constant.Cons;
import fragment.UserSexFragment.OnRefreshListener;

public class UserAgeFragment extends Fragment implements OnClickListener,
		OnRefreshListener {
	private View view;
	private TextView tv_title;
	private Button bt_last, bt_next;
	private RelativeLayout layout_actionbar;
	private IOnPagerScrollListener onPagerScrollListener;
	private PickerView pv_age;
	private String age = "25";
	public UserAgeFragment(){

	}

	@SuppressLint("ValidFragment")
	public UserAgeFragment(IOnPagerScrollListener onPagerScrollListener) {
		super();
		this.onPagerScrollListener = onPagerScrollListener;
	}

	@Override
	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		view = inflater.inflate(R.layout.fragment_user_age, null);
		initView();
		return view;
	}

	@SuppressLint("NewApi")
	public void initView() {
		// TODO Auto-generated method stub
		layout_actionbar = (RelativeLayout) view
				.findViewById(R.id.layout_actionbar);
		tv_title = (TextView) view.findViewById(R.id.tv_title);
		bt_last = (Button) view.findViewById(R.id.bt_last);
		String sex = SPHelper.getDetailMsg(getActivity(), Cons.APP_SEX,
				getActivity().getString(R.string.appsex_man));
		bt_last.setBackgroundDrawable(getResources().getDrawable(
				BGHelper.setbgButton(getActivity(), sex)));
		bt_last.setOnClickListener(this);
		bt_next = (Button) view.findViewById(R.id.bt_next);
		bt_next.setBackgroundDrawable(getResources().getDrawable(
				BGHelper.setbgButton(getActivity(), sex)));
		bt_next.setOnClickListener(this);
		pv_age = (PickerView) view.findViewById(R.id.pv_age);
		pv_age.setUnitText(getResources().getString(R.string.picker_age));
		pv_age.setData(RegisterActivity.ages);
		age = SPHelper.getDetailMsg(getActivity(), "age", 25)+"";
		pv_age.setSelected(age);
		pv_age.setOnSelectListener(new onSelectListener() { 
			@Override
			public void onSelect(String text) {
				// TODO Auto-generated method stub
				age = text;
			}
		});
		setColor();
	}

	private void setColor() {
		int color = BGHelper.setBackground(getActivity(),
				SPHelper.getDetailMsg(getActivity(), "gender", "M")); 
		tv_title.setTextColor(getResources().getColor(color));
		layout_actionbar.setBackgroundResource(color);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.bt_last:
			onPagerScrollListener.OnPaterScroll(1);
			break;
		case R.id.bt_next:
			SPHelper.setDetailMsg(getActivity(), "age", Integer.parseInt(age));
			CommHelper.insert_visit(getActivity(),"heightpg"); 
			onPagerScrollListener.OnPaterScroll(3);

			break;
		default:
			break;
		}
	}

	@Override
	public void OnRefresh() {
		// TODO Auto-generated method stub
		initView();
	}
	public interface IOnPagerScrollListener {
		void OnPaterScroll(int position);
	}

}
