package fragment;

import comm.HttpManager;
import helper.BGHelper;
import helper.HttpCallback;
import helper.HttpHelper;
import helper.SPHelper;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import manager.ImageCacheManger;
import tools.FileUtils;
import view.CircleImageView;
import adapter.MineAdapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.renrenstep.HonorHallActivity;
import com.example.renrenstep.InfoActivity;
import com.example.renrenstep.R;
import com.example.renrenstep.ReportActivity;
import com.example.renrenstep.SettingActivity;

import helper.BaseAsyncTask;
import comm.CommHelper;
import constant.Cons;

@SuppressLint("InflateParams")
public class MineFragment extends IMBaseFragment implements OnClickListener,
        MineAdapter.IOnMyItemClickListener {
    private static final int NOFITY_CENTER = 1;
    private static final int REPORT = 2;
    private static final int QUES = 3;
    private static final int SETTING = 4;
    private static final int HONOR_HALL = 0;
    private RelativeLayout layout_actionbar;
    private LinearLayout layout_info;
    private View view;
    private ListView lv_mine;
    private MineAdapter adapter;
    private List<String> contents = new ArrayList<String>();
    private List<Integer> images = new ArrayList<Integer>();
    private CircleImageView iv_icon;
    private TextView tv_email, tv_name;
    public MineFragment() {
        super();
        downPictrue();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        downPictrue();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_mine, null);
        initView();
        initData();
        initCoinsData();
        initViewData();
        CommHelper.insert_visit(getActivity(), "mypg");
        return view;
    }

    private void initCoinsData() {
        SPHelper.setBaseMsg(getActivity(), "currentCoins", 0);
        getMyCurrentCoins();
    }

    @Override
    public void onResume() {
        // TODO Auto-generated method stub
        super.onResume();
        initViewData();
        initdata();
    }

    private void initData() {
        // TODO Auto-generated method stub
        contents.clear();
        images.clear();
        contents.add("荣誉殿堂");
        contents.add(getResources().getString(R.string.mine_item1));
        contents.add(getResources().getString(R.string.mine_item2));
        contents.add(getResources().getString(R.string.mine_item4));
        contents.add(getResources().getString(R.string.mine_item3));

        String sex = SPHelper.getDetailMsg(getActivity(), Cons.USER_INFO, getString(R.string.appsex_man));
        images.add(BGHelper.setButtonSetting(getActivity(), sex));
        images.add(BGHelper.setButtonNotify(getActivity(), sex));
        images.add(BGHelper.setButtonReport(getActivity(), sex));
        images.add(sex.equals("M") ? R.drawable.ioc_mine_que_blue : R.drawable.ioc_mine_que_red);
        images.add(BGHelper.setButtonSetting(getActivity(), sex));


        adapter = new MineAdapter(getActivity(), contents, images, this);
        lv_mine.setAdapter(adapter);
    }

    private void getMyCurrentCoins() {
//        new BaseAsyncTask(Cons.GET_CURRENT_WALK_COIN, new HashMap<String, Object>(), HttpType.Get, "", getActivity()) {
//            @Override
//            public void handler(String param) {
//
//                Log.e("MineFragment","getMyCurrentCoins:"+param);
//
//                if (param != null) {
//                    try {
//                        JSONObject jsonObject = new JSONObject(param);
//                        int status = jsonObject.getInt("status");
//                        if (status == 0) {
//                             currentCoins = jsonObject.getInt("data");
//                            SPHelper.setBaseMsg(getActivity(),"currentCoins",currentCoins);
//                            adapter.notifyDataSetChanged();
//
//                        }else{
//                            SPHelper.setBaseMsg(getActivity(),"currentCoins",0);
//                        }
//                    } catch (JSONException e) {
//                        e.printStackTrace();
//                    }
//                }
//            }
//        }.execute("");
    }

    private void downPictrue() {
        new BaseAsyncTask(Cons.DONWLOAD_HEAD, new HashMap<String, Object>(), BaseAsyncTask.HttpType.Get, "", getActivity()) {
            @Override
            public void handler(String param) {
                if (param != null) {
                    try {
                        JSONObject jsonObject = new JSONObject(param);
                        int status = jsonObject.getInt("status");
                        if (status == 0) {
                            String pic = jsonObject.getString("data");
                            Context context = getActivity();
                            if (context != null) {
                                String oldnm = SPHelper.getDetailMsg(context, "uri", "");
                                String[] names = oldnm.split("/");
                                if (oldnm == "" || names.length == 0 || !pic.equals(names[names.length - 1])) {
                                    sendUsermsgChangeMsg(CommHelper.getCompleteStr(CommHelper.changeMemMsg));
                                    uploadPic(pic);
                                }
                            }
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        }.execute("");
    }

    private void uploadPic(String pic) {
        final String filenm = pic;
        new ImageCacheManger("stepic").downData(Cons.DONW_PIC + pic, new ImageCacheManger.IdownImageCallback() {
            @Override
            public void finished(String path) {
                SPHelper.setDetailMsg(getActivity(), "uri",path);
                Options options = new Options();
                options.inSampleSize = 3;
                Bitmap bitmap = BitmapFactory.decodeFile(path, options);
                if (bitmap != null && iv_icon != null) {
                    iv_icon.setImageBitmap(bitmap);
                }
            }
        });
    }

    private void initView() {
        iv_icon = (CircleImageView) view.findViewById(R.id.iv_icon);
        tv_name = (TextView) view.findViewById(R.id.tv_name);
        tv_name.setText(SPHelper.getDetailMsg(getActivity(), "nc", ""));
        lv_mine = (ListView) view.findViewById(R.id.lv_mine);
        tv_email = (TextView) view.findViewById(R.id.tv_email);

        tv_email.setText(SPHelper.getDetailMsg(getActivity(), "email", ""));
        layout_actionbar = (RelativeLayout) view.findViewById(R.id.layout_actionbar);
        layout_info = (LinearLayout) view.findViewById(R.id.layout_info);
        layout_info.setOnClickListener(this);
    }

    void initdata() {
        String sex = SPHelper.getDetailMsg(getActivity(), Cons.APP_SEX, getString(R.string.appsex_man));
        layout_actionbar.setBackgroundResource(BGHelper.setBackground(getActivity(), sex));
        images.clear();
        images.add(BGHelper.setButtonSetting(getActivity(), sex));
        images.add(BGHelper.setButtonNotify(getActivity(), sex));
        images.add(BGHelper.setButtonReport(getActivity(), sex));
        images.add(sex.equals("M") ? R.drawable.ioc_mine_que_blue : R.drawable.ioc_mine_que_red);
        images.add(BGHelper.setButtonSetting(getActivity(), sex));
        adapter.setNoReadNum(getPageReminds());
        adapter.refreshList(images);
    }

    List<Integer> getPageReminds() {
        List<Integer> mitems = new ArrayList<Integer>();
        mitems.add(0);
        mitems.add(0);
        mitems.add(0);
        mitems.add(0);
        mitems.add(0);
        return mitems;
    }

    void initViewData() {
        String bemail = SPHelper.getDetailMsg(getActivity(), "email", "");
        tv_email.setText(bemail.equals("") ? getResources().getString(R.string.nobind) : bemail);
        tv_name.setText(SPHelper.getDetailMsg(getActivity(), "nc", ""));
        String uri = SPHelper.getDetailMsg(getActivity(), "uri", "");
        Options options = new Options();
        options.inSampleSize = 2;
        Bitmap bitmap = BitmapFactory.decodeFile(uri, options);
        if (bitmap != null) {
            iv_icon.setImageBitmap(bitmap);
        }
    }

    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub
        switch (v.getId()) {
            case R.id.layout_info:
                Intent intent = new Intent(getActivity(), InfoActivity.class);
                startActivity(intent);
                break;
            default:
                break;
        }
    }

    @Override
    public void OnItemClick(int position, View view) {
        // TODO Auto-generated method stub
        switch (position) {
            case NOFITY_CENTER:
                CommHelper.insert_visit(getActivity(), "notifypg");
                Intent mintent = new Intent(getActivity(), ReportActivity.class);
                mintent.putExtra("pagenm", getResources().getString(R.string.notifycenter));
                mintent.putExtra("gender", SPHelper.getDetailMsg(getActivity(), Cons.APP_SEX, "M"));
                mintent.putExtra("url", String.format(Cons.NOTIFYTEXT, "notify", "step", SPHelper.getBaseMsg(getActivity(), "mid", "0")));
                getActivity().startActivity(mintent);
                break;
            case REPORT:
                CommHelper.insert_visit(getActivity(), "reportpg");
                Intent rintent = getIntent(getResources().getString(R.string.report_title), SPHelper.getDetailMsg(getActivity(), Cons.APP_SEX, "M"), Cons.REPORT_URL + SPHelper.getBaseMsg(getActivity(), "mid", "00000"));
                getActivity().startActivity(rintent);
                break;
            case SETTING:
                CommHelper.insert_visit(getActivity(), "settingpg");
                Intent intent = new Intent(getActivity(), SettingActivity.class);
                getActivity().startActivity(intent);
               /* Intent iintentB = new Intent(getActivity(), TalkingPicsBActivity.class);
                getActivity().startActivity(iintentB);*/
                break;

            case QUES:
                Intent qintent = getIntent(getResources().getString(R.string.mine_item4), SPHelper.getDetailMsg(getActivity(), Cons.APP_SEX, "M"), String.format(Cons.NOTIFYTEXT, "question", "step", SPHelper.getBaseMsg(getActivity(), "mid", "123")));
                getActivity().startActivity(qintent);
                break;
            case HONOR_HALL:
                Intent iintent = new Intent(getActivity(), HonorHallActivity.class);
                getActivity().startActivity(iintent);
                /* Intent iintentA = new Intent(getActivity(), TalkingPicsActivity.class);
                getActivity().startActivity(iintentA);*/
                break;
            default:
                break;
        }
    }

    Intent getIntent(String nm, String gender, String url) {
        Intent lintent = new Intent(getActivity(), ReportActivity.class);
        lintent.putExtra("pagenm", nm);
        lintent.putExtra("gender", gender);
        lintent.putExtra("url", url);
        return lintent;
    }

    @Override
    public void onDestroy() {
        // TODO Auto-generated method stub
        super.onDestroy();
    }
}
