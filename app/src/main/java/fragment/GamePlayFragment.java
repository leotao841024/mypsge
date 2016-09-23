package fragment;

import com.example.renrenstep.R;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;

public class GamePlayFragment extends Fragment {
	ListView list_group_day;
	ImageView img_group_day;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) { 
		View view = inflater.inflate(R.layout.fragment_game_play, null);
		list_group_day=(ListView)view.findViewById(R.id.list_group_day);
		img_group_day=(ImageView)view.findViewById(R.id.img_group_day);
		return view;
	}
}
