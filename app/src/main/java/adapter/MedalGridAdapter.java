package adapter;

import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.renrenstep.R;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import bean.HonorMedals;
import tools.AndroidTool;

/**
 * Created by admin on 2016/3/18.
 */
public class MedalGridAdapter extends BaseAdapter {
    private Context context;
    private List<HonorMedals.MedalData> datas;
    HonorMedals.MedalData data;
    public MedalGridAdapter(Context context,List<HonorMedals.MedalData> datas) {
        this.context = context;
        this.datas = datas;
    }

    @Override
    public int getCount() {
        return datas != null ?datas.size():0;
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
        ViewHolder viewHolder;
        if(view == null){
            view = LayoutInflater.from(context).inflate(R.layout.list_item_medal, null);
            viewHolder = new ViewHolder();
            viewHolder.iv_medal = (ImageView) view.findViewById(R.id.iv_medal);
            viewHolder.tv_medalNum = (TextView) view.findViewById(R.id.tv_medalNum);
            viewHolder.tv_medalName = (TextView) view.findViewById(R.id.tv_medalName);
            viewHolder.tv_medalType = (TextView) view.findViewById(R.id.tv_medalType);
            view.setTag(viewHolder);
        }else{
            viewHolder = (ViewHolder) view.getTag();
        }



        if (i == 0) viewHolder.tv_medalType.setVisibility(View.VISIBLE);
        if (i > 2) viewHolder.tv_medalType.setVisibility(View.INVISIBLE);

        data = datas.get(i);
        if(data.getCount() != 0){
           // viewHolder.iv_medal.setImageBitmap(getImageFromAssetsFile(data.getPeriodtyp() + ".png"));
            viewHolder.iv_medal.setImageResource(AndroidTool.getResourceId(context, data.getPeriodtyp()));
            viewHolder.tv_medalNum.setTextColor(Color.parseColor("#FF4638"));
        }
        else{
           // viewHolder.iv_medal.setImageBitmap(getImageFromAssetsFile(data.getTyp() + "_no.png"));
            viewHolder.iv_medal.setImageResource(AndroidTool.getResourceId(context, data.getTyp() + "_no"));
            viewHolder.tv_medalNum.setTextColor(Color.parseColor("#C9C9C9"));
        }

        viewHolder.tv_medalName.setText(data.getNm());
        viewHolder.tv_medalNum.setText("("+data.getCount()+"枚奖章)");
        viewHolder.tv_medalType.setText(data.getTypnm());

        return view;
    }


    public Bitmap getImageFromAssetsFile(String fileName)
    {
        Bitmap image = null;
        AssetManager am = context.getResources().getAssets();
        try
        {
            InputStream is = am.open(fileName);
            image = BitmapFactory.decodeStream(is);
            is.close();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }

        return image;

    }


    class ViewHolder{
        ImageView iv_medal;
        TextView tv_medalNum;
        TextView tv_medalName;
        TextView tv_medalType;

    }


}
