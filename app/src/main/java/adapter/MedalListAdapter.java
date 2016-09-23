package adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;

import com.example.renrenstep.R;

import java.util.List;

import bean.HonorMedals;
import view.NoScrollGridView;

/**
 * Created by admin on 2016/3/18.
 */
public class MedalListAdapter extends BaseAdapter {
    private List<List<HonorMedals.MedalData>> datas;
    private Context context;
    private MedalGridAdapter mAdapter;

    public MedalListAdapter(Context context,List<List<HonorMedals.MedalData>> datas) {
        this.context = context;
        this.datas = datas;

    }

    @Override
    public int getCount() {
        return datas != null ? datas.size():0;
    }

    @Override
    public Object getItem(int i) {
        return datas.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHolder holder;
        if(view == null){
            view = LayoutInflater.from(context).inflate(R.layout.list_item_honor_medal,null);
            holder = new ViewHolder();
            holder.gridView = (NoScrollGridView) view.findViewById(R.id.gv_medals);
            view.setTag(holder);
        }else{
            holder = (ViewHolder) view.getTag();
        }

        mAdapter = new MedalGridAdapter(context,datas.get(i));
        holder.gridView.setAdapter(mAdapter);

        return view;
    }


    class ViewHolder{
        NoScrollGridView gridView;
    }



}
