package fragment;

import java.util.List;

import helper.SPHelper;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import bean.Permin;

import com.example.renrenstep.AnimActivity;
import com.example.renrenstep.AppActivity;
import com.example.renrenstep.HintActivity;
import com.example.renrenstep.MainActivity;
import com.example.renrenstep.R;

import comm.CommHelper;
import comm.PermissionHelper;

public class LeaderFragment2 extends Fragment{
	private View view;
	private Button bt_enter;
	@Override
	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		view = inflater.inflate(R.layout.fragment_leader2, null);
		bt_enter = (Button)view.findViewById(R.id.bt_enter);
		bt_enter.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				SPHelper.setBaseMsg(getActivity(), "login", 0);
				SPHelper.setBaseMsg(getActivity(),"ischecked",0);
				PermissionHelper per=new PermissionHelper(getActivity());
				List<Permin> list = per.getPermin();
				Intent intent=null;
				if(CommHelper.hasAllPermin(list, getActivity())){
					intent=new Intent(getActivity(),AppActivity.class);
				}else{
					intent=new Intent(getActivity(),HintActivity.class);
				}
				getActivity().startActivity(intent);
				getActivity().finish();
			}
		});
		return view;
	}
}
