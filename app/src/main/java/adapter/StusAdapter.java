package adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.renrenstep.R;

import java.util.List;

import bean.OptionsItem;

/**
 * Created by admin on 2016/3/23.
 */
public class StusAdapter extends BaseAdapter {
    private Context context;
    private List<OptionsItem> data;
    private OptionsItem item;

    public StusAdapter(Context context, List<OptionsItem> data){
        this.context = context;
        this.data = data;
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public Object getItem(int i) {
        return data.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHolder holder;
        if(view == null){
            view = LayoutInflater.from(context).inflate(R.layout.list_item_stus,null);
            holder = new ViewHolder();
            holder.tv_nickname = (TextView) view.findViewById(R.id.tv_nickname);
            holder.iv_avatar = (ImageView) view.findViewById(R.id.iv_avatar);
            view.setTag(holder);
        }else{
            holder = (ViewHolder) view.getTag();
        }

       item = data.get(i);
        holder.tv_nickname.setText(item.getTitle());
        holder.iv_avatar.setImageResource(item.getIconId());

        return view;
    }


    class ViewHolder{
       TextView tv_nickname;
        ImageView iv_avatar;

    }
}
