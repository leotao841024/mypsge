package helper;

import com.example.renrenstep.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class DialogHelper {
	public interface IDialog_event {
		void confirm();
		void cancel();
	}
	public static View getConfrirmDialog(final IDialog_event event,
			String btn_confirm, String btn_cancel, String title,
			Context context, String gender) {
		View view = LayoutInflater.from(context).inflate(
				R.layout.dialog_finish, null);
		Button btn = (Button) view.findViewById(R.id.bt_testagain);
		btn.setText(btn_confirm);
		Button cancel = (Button) view.findViewById(R.id.bt_exitplan);
		cancel.setText(btn_cancel);
		TextView tv_title = (TextView) view.findViewById(R.id.tv_title);
		tv_title.setText(title);
		btn.setBackgroundResource(gender.equals("M") ? R.drawable.btn_add_friend_blue
				: R.drawable.btn_add_friend_red);
		btn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				event.confirm();
			}
		});
		cancel.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				event.cancel();
			}
		});
		return view;
	}
}
