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
 * content of plus btn talk activity
 */
public class OptionsAdapter extends BaseAdapter {
    private Context context;
    private List<OptionsItem> data;
    private OptionsItem item;

    public OptionsAdapter(Context context, List<OptionsItem> data){
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
            view = LayoutInflater.from(context).inflate(R.layout.list_item_options,null);
            holder = new ViewHolder();
            holder.tv_option = (TextView) view.findViewById(R.id.tv_option);
            holder.iv_option = (ImageView) view.findViewById(R.id.iv_option);
            view.setTag(holder);
        }else{
            holder = (ViewHolder) view.getTag();
        }

       item = data.get(i);
        holder.tv_option.setText(item.getTitle());
        holder.iv_option.setImageResource(item.getIconId());

        return view;
    }


    class ViewHolder{
       TextView tv_option;
        ImageView iv_option;

    }
}
