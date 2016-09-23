package fragment;

import helper.SPHelper;

import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import tools.ToastManager;

import org.json.JSONException;
import org.json.JSONObject;

import com.example.renrenstep.R;
import com.example.renrenstep.ReportActivity;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
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
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import helper.BaseAsyncTask;
import comm.CommHelper;
import constant.Cons;

public class UserInfoFragment extends Fragment implements OnClickListener {
	private View view;
	private Button bt_regist, bt_send;
	private EditText et_mob, et_mid, et_pwd, et_name, et_code;
	private ImageView exit_act;
	private int times;
	private Timer timer;
	private TimerTask task;
	public static final int SEND_CODE = 0;
	public static final int STOP_CODE = 1;
	private TextView tv_protocol;
	private CheckBox cb_protocol;
	private Dialog dialog;
	UserAgeFragment.IOnPagerScrollListener onPagerScrollListener;
	public UserInfoFragment(){

	}
	@SuppressLint("ValidFragment")
	public UserInfoFragment(UserAgeFragment.IOnPagerScrollListener onPagerScrollListener) {
		super();
		this.onPagerScrollListener = onPagerScrollListener;
	} 
	
	@Override
	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		view = inflater.inflate(R.layout.fragment_user, null);
		init();
		return view;
	}

	private Handler handler = new Handler() {

		public void handleMessage(Message msg) {
			switch (msg.what) {
			case SEND_CODE:
				if (bt_send != null)
					bt_send.setText(String.valueOf(times)+ getResources().getString(R.string.send_time));
				break;
			case STOP_CODE:
				timer.cancel();
				bt_send.setClickable(true);
				bt_send.setText(getResources().getString(R.string.send_code));
				break;
			default:
				break;
			} 
		};
	};

	private void init() {
		// TODO Auto-generated method stub
		cb_protocol = (CheckBox) view.findViewById(R.id.cb_protocol);
		tv_protocol = (TextView) view.findViewById(R.id.tv_protocol);
		tv_protocol.setOnClickListener(this);
		exit_act = (ImageView) view.findViewById(R.id.exit_act);
		exit_act.setOnClickListener(this);
		bt_send = (Button) view.findViewById(R.id.bt_send);
		bt_send.setOnClickListener(this);
		et_code = (EditText) view.findViewById(R.id.et_code);
		et_name = (EditText) view.findViewById(R.id.et_name);
		et_mid = (EditText) view.findViewById(R.id.et_mid);
		et_pwd = (EditText) view.findViewById(R.id.et_pwd);
		bt_regist = (Button) view.findViewById(R.id.bt_regist);
		bt_regist.setOnClickListener(this);
		et_mob = (EditText) view.findViewById(R.id.et_mob);
		dialog = CommHelper.createLoadingDialog(getActivity(), "", "F");
		// bt_regist.getBackground().setAlpha(100);
		// bt_regist.setClickable(false);
		bt_regist.setEnabled(true);
	}

	private void regist() {
		String mid = et_mob.getText().toString().trim();
		String nc = et_name.getText().toString().trim();
		String pwd = et_pwd.getText().toString().trim();
		if (!cb_protocol.isChecked()) {
			ToastManager.show(getActivity(), R.string.read_protocol, 1000);
			return;
		}
		if (nc.equals("") || nc == null) {
			ToastManager.show(getActivity(), R.string.not_name, 1000);
			return;
		}

		if (pwd.equals("") || pwd == null) {
			ToastManager.show(getActivity(), R.string.not_secret, 1000);
			return;
		}
		if (pwd.length() < 6) {
			ToastManager.show(getActivity(), R.string.limit_mlength, 1000);
			return;
		}
		if (mid.equals("")) {
			ToastManager.show(getActivity(), R.string.mismid, 1000);
			return;
		}
		Map<String, Object> maps = new HashMap<String, Object>();
		maps.put("nc", nc);
		boolean isemail = mid.contains("@");
		if (isemail) {
			maps.put("email", mid);
		} else {
			Pattern p = Pattern.compile("[0-9]*");
			Matcher m = p.matcher(mid);
			if (!m.matches()) {
				ToastManager.show(getActivity(), R.string.phoneerror, 1000);
				return;
			}
			if (mid.length() != 11) {
				ToastManager.show(getActivity(), R.string.phoneerror, 1000);
				return;
			}
			String code = et_code.getText().toString().trim();
			if (code.length() != 6) {
				ToastManager.show(getActivity(), R.string.error_code, 1000);
				return;
			}
			maps.put("mobile", mid);
			maps.put("code", code);
		}
		maps.put("pwd", pwd);
		dialog = CommHelper.createLoadingDialog(getActivity(), "", "F");
		postUrl(maps, isemail);
	}

	void postUrl(Map<String, Object> maps, boolean isemail) {
		new BaseAsyncTask(Cons.REGIST_URL, maps, BaseAsyncTask.HttpType.Post, "",getActivity()) {
			@Override
			public void handler(String param) {
				// {"status":3,"description":"邮箱不能为空！","mtoken":null,"mid":null}
				dialog.dismiss();
				if (param != null) {
					try {
						JSONObject jsonObject = new JSONObject(param);
						int status = jsonObject.getInt("status");
						String description = jsonObject
								.getString("description");
						if (status == 0) {
							// 注册
							String uid = et_mob.getText().toString().trim();
							boolean key = uid.contains("@");
							SPHelper.setBaseMsg(getActivity(), "mtoken",
									jsonObject.getString("mtoken"));
							SPHelper.setBaseMsg(getActivity(), "mid",
									jsonObject.getString("mid"));
							SPHelper.setBaseMsg(getActivity(), "uid", et_mob
									.getText().toString().trim());
							SPHelper.setBaseMsg(getActivity(), "pwd", et_pwd
									.getText().toString().trim());
							SPHelper.setDetailMsg(getActivity(), "nc", et_name
									.getText().toString().trim());
							SPHelper.setDetailMsg(getActivity(), (key ? "email": "mobile"), et_mob.getText().toString().trim());
							SPHelper.setDetailMsg(getActivity(), "newnotifynum",jsonObject.getInt("notifynum"));
							CommHelper.insert_visit(getActivity(), "genderpg");
							onPagerScrollListener.OnPaterScroll(1);
						} else {
							Toast.makeText(getActivity(), description, 1).show();
						}
					} catch (JSONException e) {
						Toast.makeText(getActivity(),getResources().getString(R.string.wangluoyic),1000).show();
					}
				}
			}
		}.execute("");
	} 
 
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.bt_regist:

			regist();
			break;
		case R.id.bt_send:
			String mob = et_mob.getText().toString().trim();
			if (mob != null && mob.length() != 0&& mob.length() == 11) {
				String uid = et_mob.getText().toString().trim();
				Pattern p = Pattern.compile("[0-9]*");
				Matcher m = p.matcher(uid);
				if (!m.matches()) {
					ToastManager.show(getActivity(), R.string.phoneerror, 1000);
					return;
				}
				Map<String, Object> map = new HashMap<String, Object>();
				map.put("uid", uid);
				dialog.show();
				new BaseAsyncTask(Cons.BIND_PHONE, map, BaseAsyncTask.HttpType.Post, "",
						getActivity()) {
					@Override
					public void handler(String param) {
						dialog.dismiss();
						if (param != null) {
							try {
								JSONObject jsonObject = new JSONObject(param);
								int status = jsonObject.getInt("status");
								if (status == 0) {
									sendCode();
								} else {
									String res = jsonObject
											.getString("description");
									if (res != null && !res.equals("")) {
										Toast.makeText(getActivity(), res, 1)
												.show();
									}
								}
							} catch (JSONException e) {
								e.printStackTrace();
							}
						}
					}
				}.execute("");
			} else {
				Toast.makeText(getActivity(),
						getResources().getString(R.string.phoneerror), 1)
						.show();
			}
			break;
		case R.id.exit_act:
			if (timer != null) {
				timer.cancel();
			}
			getActivity().finish();
			break;
		case R.id.tv_protocol:
			 Intent intent = new Intent(getActivity(), ReportActivity.class);
			 intent.putExtra("pagenm",
			 getResources().getString(R.string.app_name));
			 intent.putExtra("gender", "F");
			 intent.putExtra("url", Cons.PROTOCOL);
			 startActivity(intent);
			break;
		default:
			break;
		}
	}
	
	private void sendCode() {
		times = 60;
		bt_send.setClickable(false);
		timer = new Timer();
		task = new TimerTask() {
			@Override
			public void run() {
				// TODO Auto-generated method stub
				--times;
				if (times > 0) {
					Message msg = new Message();
					msg.what = SEND_CODE;
					msg.arg1 = times;
					handler.sendMessage(msg);
				} else {
					Message msg = new Message();
					msg.what = STOP_CODE;
					handler.sendMessage(msg);
				}
			}
		};
		timer.schedule(task, 200, 1000);
	}

	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		if (timer != null) {
			timer.cancel();
		}
		super.onDestroy();
	}
}
