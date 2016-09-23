package com.example.renrenstep;

import android.app.Activity;
import android.os.Bundle;
import android.view.Window;
import android.widget.ListView;

/**
 * Created by admin on 2016/3/3.
 */
public class AddGroupChatMemberActivity extends Activity {

    private ListView mailist;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.group_mem_select);
        mailist=((ListView)findViewById(R.id.mlist));
    }
}
