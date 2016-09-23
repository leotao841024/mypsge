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

public class UserWeightFragment extends Fragment implements OnClickListener {
	private View view;
	private TextView tv_title;
	private Button bt_last, bt_next;
	private RelativeLayout layout_actionbar;
	private UserAgeFragment.IOnPagerScrollListener onPagerScrollListener;
	private PickerView pv_age;
	private String weight;
	public UserWeightFragment(){}
	@SuppressLint("ValidFragment")
	public UserWeightFragment(UserAgeFragment.IOnPagerScrollListener onPagerScrollListener) {
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

	private void initView() {
		// TODO Auto-generated method stub
		layout_actionbar = (RelativeLayout) view
				.findViewById(R.id.layout_actionbar);
		tv_title = (TextView) view.findViewById(R.id.tv_title);
		tv_title.setText(getResources().getString(R.string.weight));
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
		int background = BGHelper.setBackground(getActivity(), SPHelper
				.getDetailMsg(getActivity(), Cons.APP_SEX, getResources()
						.getString(R.string.appsex_man)));
		tv_title.setTextColor(getResources().getColor(background));
		layout_actionbar.setBackgroundResource(background);
		pv_age = (PickerView) view.findViewById(R.id.pv_age);
		pv_age.setUnitText(getResources().getString(R.string.picker_weight));
		pv_age.setData(RegisterActivity.weights);
		weight = SPHelper.getDetailMsg(getActivity(), "weight", 0)+"";
		if(weight.equals("0")){
			if(sex.equals(getResources().getString(R.string.appsex_man))){
				pv_age.setSelected("70");
				weight = "70";
			}if(sex.equals(getResources().getString(R.string.appsex_women))){
				pv_age.setSelected("45");
				weight = "45";
			}
		}
		pv_age.setOnSelectListener(new onSelectListener() {

			@Override
			public void onSelect(String text) {
				// TODO Auto-generated method stub
				weight = text;
			}
		});
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.bt_last:
			onPagerScrollListener.OnPaterScroll(3);
			break;
		case R.id.bt_next:
			
			if(weight!=null) 
				SPHelper.setDetailMsg(getActivity(), "weight", Integer.parseInt(weight));
			else
				SPHelper.setDetailMsg(getActivity(), "weight", 65);
			CommHelper.insert_visit(getActivity(),"diseasepg"); 
			onPagerScrollListener.OnPaterScroll(5);
			break;

		default:
			break;
		}
	}
}
