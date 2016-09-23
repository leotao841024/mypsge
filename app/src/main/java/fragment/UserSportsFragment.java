package fragment;

import helper.BGHelper;
import helper.SPHelper;

import receiver.NotifyReceiver;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.renrenstep.R;
import com.example.renrenstep.RegisterActivity;

import constant.Cons;

public class UserSportsFragment extends Fragment implements OnClickListener {
	private View view;
	private Button bt_finish;
	private TextView tv_title;
	private RelativeLayout layout_actionbar;
	private UserAgeFragment.IOnPagerScrollListener onPagerScrollListener;
	private ImageView iv_person, iv_step, iv_fire;
	private TextView tv_step, tv_faststep, tv_cal;
	public UserSportsFragment(){

	}
	@SuppressLint("ValidFragment")
	public UserSportsFragment(UserAgeFragment.IOnPagerScrollListener onPagerScrollListener) {
		super();
		this.onPagerScrollListener = onPagerScrollListener;
	} 
	@SuppressLint("InflateParams")
	@Override
	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		view = inflater.inflate(R.layout.fragment_user_sports, null);

		initView();

		return view;
	}

	Handler handler = new Handler(){
		public void handleMessage(Message msg) { 
			int totalsteps = SPHelper.getDetailMsg(getActivity(), "totalsteps", 10000);
			int faststeps = SPHelper.getDetailMsg(getActivity(), "faststeps", 8000);
			int calorie = SPHelper.getDetailMsg(getActivity(), "calorie", 400);
			tv_step.setText(totalsteps+"");
			tv_faststep.setText(faststeps+"");
			tv_cal.setText(calorie+"");
		};
	};
	private NotifyReceiver receiver;
	private void initView() {
		receiver = new NotifyReceiver(){
			@Override
			public void onReceive(Context context, Intent intent) {
				// TODO Auto-generated method stub
				super.onReceive(context, intent);
				handler.sendMessage(new Message());
			}
		};
		IntentFilter filter = new IntentFilter();
		filter.addAction(Cons.STEPS_RECEIVER);
		getActivity().registerReceiver(receiver, filter);
		String sex = SPHelper.getDetailMsg(getActivity(), Cons.APP_SEX,
				getString(R.string.appsex_man));
		layout_actionbar = (RelativeLayout) view
				.findViewById(R.id.layout_actionbar);
		bt_finish = (Button) view.findViewById(R.id.bt_finish);
		bt_finish.setBackgroundResource(BGHelper.setSportButton(getActivity(),
				sex));
		bt_finish.setOnClickListener(this);
		tv_title = (TextView) view.findViewById(R.id.tv_title);
		int background = BGHelper.setBackground(getActivity(), SPHelper
				.getDetailMsg(getActivity(), Cons.APP_SEX, getResources()
						.getString(R.string.appsex_man)));
		tv_title.setTextColor(getResources().getColor(background));
		layout_actionbar.setBackgroundResource(background);

		iv_person = (ImageView) view.findViewById(R.id.iv_person);
		iv_person.setImageResource(BGHelper.setIconPerson(getActivity(), sex));
		iv_step = (ImageView) view.findViewById(R.id.iv_step);
		iv_step.setImageResource(BGHelper.setIconStep(getActivity(), sex));
		iv_fire = (ImageView) view.findViewById(R.id.iv_fire);
		iv_fire.setImageResource(BGHelper.setIconFire(getActivity(), sex));
		tv_step = (TextView) view.findViewById(R.id.tv_step);
		tv_faststep = (TextView) view.findViewById(R.id.tv_faststep);
		tv_cal = (TextView) view.findViewById(R.id.tv_cal); 
	}
	@Override
	public void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
	}


	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.bt_finish:
			onPagerScrollListener.OnPaterScroll(RegisterActivity.EXIT);
			break;

		default:
			break;
		}
	}
	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		getActivity().unregisterReceiver(receiver);
	}
}
