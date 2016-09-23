package com.example.renrenstep;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import adapter.MedalListAdapter;
import bean.HonorMedals;
import helper.BaseAsyncTask;
import constant.Cons;
import helper.BGHelper;
import helper.SPHelper;
import tools.GsonUtils;

/**
 * Created by admin on 2016/3/18.
 */
public class HonorHallActivity extends Activity implements View.OnClickListener {

    private ListView lv_honor_medals;
    private List<HonorMedals.MedalData> datas;
    private List<HonorMedals.MedalData> teamteamDatas;
    private List<HonorMedals.MedalData> gallopDatas;
    private List<HonorMedals.MedalData> awesomeDatas;
    private List<HonorMedals.MedalData> personteamDatas;
    private List<List<HonorMedals.MedalData>> medals;
    private MedalListAdapter medalListAdapter;
    private LinearLayout ll_back;
    private RelativeLayout layout_actionbar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_honor_hall);
        initView();
        initDatas();

    }

    private void initDatas() {

        String sex = SPHelper.getDetailMsg(this, Cons.APP_SEX, getString(R.string.appsex_man));
        layout_actionbar.setBackgroundResource(BGHelper.setBackground(this, sex));


        teamteamDatas = new ArrayList<>();
        gallopDatas = new ArrayList<>();
        awesomeDatas = new ArrayList<>();
        personteamDatas = new ArrayList<>();
        medals = new ArrayList<>();
      /*  String str = "{'status':0,'data':[" +
                "{'typ':'teamteam','nm':'团队霸主日勋章','periodtyp':'teamteam_day','typnm':'团队勋章','count':1}," +

                "{'typ':'gallop','nm':'健步如飞日勋章','periodtyp':'gallop_day','typnm':'飞奔勋章','count':0}," +
                "{'typ':'gallop','nm':'健步如飞年勋章','periodtyp':'gallop_day','typnm':'飞奔勋章','count':0}," +
                "{'typ':'gallop','nm':'健步如飞十年勋章','periodtyp':'gallop_day','typnm':'飞奔勋章','count':0}," +
                "{'typ':'awesome','nm':'给力日勋章','periodtyp':'awesome_day','typnm':'给力勋章','count':4}," +
                "{'typ':'awesome','nm':'给力年勋章','periodtyp':'awesome_day','typnm':'给力勋章','count':4}," +
                "{'typ':'personteam','nm':'金质最棒日勋章','periodtyp':'personteam_day','typnm':'金质勋章','count':0}," +
                "{'typ':'teamteam','nm':'团队霸主周勋章','periodtyp':'teamteam_week','typnm':'团队勋章','count':3}," +
                "{'typ':'gallop','nm':'健步如飞周勋章','periodtyp':'gallop_week','typnm':'飞奔勋章','count':0}," +
                "{'typ':'awesome','nm':'给力周勋章','periodtyp':'awesome_week','typnm':'给力勋章','count':0}," +
                "{'typ':'personteam','nm':'金质最棒周勋章','periodtyp':'personteam_week','typnm':'金质勋章','count':6}," +
                "{'typ':'personteam','nm':'金质最棒年勋章','periodtyp':'personteam_week','typnm':'金质勋章','count':6}," +

                "{'typ':'teamteam','nm':'团队霸主月勋章','periodtyp':'teamteam_month','typnm':'团队勋章','count':3}," +
                "{'typ':'gallop','nm':'健步如飞月勋章','periodtyp':'gallop_month','typnm':'飞奔勋章','count':0}," +
                "{'typ':'awesome','nm':'给力月勋章','periodtyp':'awesome_month','typnm':'给力勋章','count':0}," +
                "{'typ':'personteam','nm':'金质最棒月勋章','periodtyp':'personteam_month','typnm':'金质勋章','count':0}]}";
        HonorMedals honorMedals = GsonUtils.fromJson(str, HonorMedals.class);
        datas = honorMedals.getDatas();
        for (HonorMedals.MedalData data : datas) {

            if ("teamteam".equals(data.getTyp())) {
                teamteamDatas.add(data);
            } else if ("gallop".equals(data.getTyp())) {
                gallopDatas.add(data);
            } else if ("awesome".equals(data.getTyp())) {
                awesomeDatas.add(data);
            } else if ("personteam".equals(data.getTyp())) {
                personteamDatas.add(data);
            }
        }
        medals.add(teamteamDatas);
        medals.add(gallopDatas);
        medals.add(awesomeDatas);
        medals.add(personteamDatas);

        medalListAdapter = new MedalListAdapter(HonorHallActivity.this, medals);

        lv_honor_medals.setAdapter(medalListAdapter);*/


        Map<String, Object> maps = new HashMap<String, Object>();

        new BaseAsyncTask(Cons.GET_REWARD_END_HIS_LIST, maps, BaseAsyncTask.HttpType.Get, "", this) {

            @Override
            public void handler(String param) throws RuntimeException {
                Log.e("HonorHallActivity", param);
                JSONObject jsonObject = null;
                int res = -1;
                try {
                    jsonObject = new JSONObject(param);
                    res= jsonObject.getInt("status");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
               if (res != 0) return;

                HonorMedals honorMedals = GsonUtils.fromJson(param, HonorMedals.class);
                if (honorMedals!= null && honorMedals.getStatus() == 0) {
                    datas = honorMedals.getDatas();
                    for (HonorMedals.MedalData data:datas){
                        if ("teamteam".equals(data.getTyp())){
                            teamteamDatas.add(data);
                        }else if ("gallop".equals(data.getTyp())){
                            gallopDatas.add(data);
                        }else if ("awesome".equals(data.getTyp())){
                            awesomeDatas.add(data);
                        }else if ("personteam".equals(data.getTyp())){
                            personteamDatas.add(data);
                        }
                    }
                    medals.add(teamteamDatas);
                    medals.add(gallopDatas);
                    medals.add(awesomeDatas);
                    medals.add(personteamDatas);

                    medalListAdapter = new MedalListAdapter(HonorHallActivity.this,medals);

                    lv_honor_medals.setAdapter(medalListAdapter);
                }
            }

        }.execute("");
    }

    private void initView() {
        layout_actionbar = (RelativeLayout) findViewById(R.id.layout_actionbar);
        lv_honor_medals = (ListView) findViewById(R.id.lv_honor_medals);
        ll_back = (LinearLayout) findViewById(R.id.ll_back);
        ll_back.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ll_back:
                onBackPressed();
                break;

        }
    }
}
