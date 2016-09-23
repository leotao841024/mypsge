package com.example.renrenstep;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.GridView;
import android.widget.LinearLayout;
import java.util.ArrayList;
import java.util.List;
import adapter.StusAdapter;
import bean.OptionsItem;

/**
 * Created by admin on 2016/3/29.
 */
public class StudentsActivity extends Activity implements View.OnClickListener {
    private LinearLayout ll_back;
    private GridView gridView_stus;
    private List<OptionsItem> stus;
    private StusAdapter stusAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_students_list);
        initview();
        initEvent();
        initData();

    }

    private void initview() {
        ll_back  = (LinearLayout) findViewById(R.id.ll_back);
        gridView_stus = (GridView) findViewById(R.id.gridview_stus);

    }

    private void initEvent() {
        ll_back.setOnClickListener(this);
    }

    private void initData() {
        stus = new ArrayList<>();
        stusAdapter = new StusAdapter(this,stus);
        gridView_stus.setAdapter(stusAdapter);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.ll_back:
                onBackPressed();
                break;
        }
    }
}
