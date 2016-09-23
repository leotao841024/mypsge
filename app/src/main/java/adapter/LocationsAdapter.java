package adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.baidu.mapapi.search.core.PoiInfo;
import com.example.renrenstep.R;

import java.util.List;

/**
 * Created by admin on 2016/3/8.
 */
public class LocationsAdapter extends BaseAdapter {
    private List<PoiInfo> mLocationList;
    private Context context;
    private PoiInfo poiInfo;
     ViewHolder viewHolder;
    public LocationsAdapter(Context context,List<PoiInfo> mLocationList) {
        this.context = context;
        this.mLocationList = mLocationList;
    }

    @Override
    public int getCount() {
        return mLocationList == null?0:mLocationList.size();
    }

    @Override
    public PoiInfo getItem(int i) {
        return mLocationList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {

        poiInfo = (PoiInfo) getItem(i);
        if(view ==null){
            view = LayoutInflater.from(context).inflate(R.layout.list_item_location,viewGroup,false);
            viewHolder = new ViewHolder();
            viewHolder.tv_address = (TextView) view.findViewById(R.id.tv_address);
            viewHolder.tv_name = (TextView) view.findViewById(R.id.tv_name);
            view.setTag(viewHolder);
        }else{
            viewHolder = (ViewHolder) view.getTag();
        }

        viewHolder.tv_address.setText(mLocationList.get(i).address);
        viewHolder.tv_name.setText(mLocationList.get(i).name);
        return view;
    }

    class ViewHolder{
        TextView tv_name;
        TextView tv_address;

    }
}
