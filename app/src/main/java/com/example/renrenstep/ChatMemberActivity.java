package com.example.renrenstep;

import android.os.Bundle;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.liuguangqiang.swipeback.SwipeBackActivity;
import com.liuguangqiang.swipeback.SwipeBackLayout;

import adapter.GroupMemAdapter;

/**
 * Created by admin on 2016/3/3.
 */
public class ChatMemberActivity extends SwipeBackActivity {
    private GridView grid_groupmem;
    private ImageView img_settop;
    private GroupMemAdapter groupAdapter;
    private TextView txt_groupnm;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       // requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.group_sliding_menu);
        grid_groupmem = (GridView) findViewById(R.id.grid_groupmem);
        img_settop = (ImageView) findViewById(R.id.img_settop);
        txt_groupnm = (TextView) findViewById(R.id.txt_groupnm);
        setDragEdge(SwipeBackLayout.DragEdge.LEFT);
    }
}
