package fragment;
 
 

import com.example.renrenstep.R;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class DemoFragment extends Fragment {
	private View view;
	public DemoFragment(){
		super();
	}
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		view = inflater.inflate(R.layout.fragment_demo, null);
		//View view =inflater.inflate(R.layout.fragment_demo, null);
		return view;
	}
}
