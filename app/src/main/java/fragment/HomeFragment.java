package fragment;
import app.MyApplication;
import helper.BGHelper;
import helper.HttpTool;
import helper.HttpTool.IHttpCallBackAsy;
import helper.SPHelper;

import java.lang.ref.WeakReference;
import java.text.SimpleDateFormat;
import tools.ToastManager;
import org.json.JSONObject;
import service.SimpleStepService;
import tools.BitmapUtil;
import tools.StepTool;
import view.CircleBar;
import view.IconBar;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.*;
import bean.DayStep;
import bean.Food;
import bean.FoodCal;
import bean.Permin;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.example.renrenstep.LogInfoActivity;
import com.example.renrenstep.MainActivity;
import com.example.renrenstep.R;
import com.example.renrenstep.ReportActivity;
import com.example.renrenstep.StepDataActivity;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.bean.SocializeEntity;
import com.umeng.socialize.controller.UMSocialService;
import com.umeng.socialize.controller.listener.SocializeListeners.SnsPostListener;
import com.umeng.socialize.media.UMImage;
import com.umeng.socialize.sso.SinaSsoHandler;
import com.umeng.socialize.weixin.controller.UMWXHandler;
import com.umeng.socialize.weixin.media.CircleShareContent;
import com.umeng.socialize.weixin.media.WeiXinShareContent;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import helper.BaseAsyncTask;
import comm.CommHelper;
import comm.FoodXmlHandler;
import comm.PermissionHelper;
import constant.Cons;
public class HomeFragment extends IMBaseFragment implements OnClickListener {
    private ImageView iv_notify, iv_share, imageView1,iv_sina, iv_wechat, iv_wxcircle;
    private CircleBar progressBar;
    private IconBar cb_person;
    private View view;
    private RelativeLayout layout_actionbar, share_actionbar;
    private LinearLayout share_body, layouterweima;
    private SlidingMenu menu, shareMenu;
    private View  shareView;
    private LinearLayout ll_exit_share;
    private view.CircleImageView simg;
    private TextView tv_distance, tv_cal, tv_city,
            tv_max_temperature, tv_weather, tv_pm, tv_sport, stotal, sfast,
            sname, stimer, tv_title, tv_share;
    private boolean  isShareMenu;
    private TextView firstnm, lastnm,txt_noreadnotify;
    private ImageView middlenm;
    private SimpleStepService simpleStep;
    private Dialog dialog;
    private String gender ="";
    private boolean isbind = false;
    public HomeFragment() {
        super();
    }
    ServiceConnection myconn = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            simpleStep = ((SimpleStepService.StepBinder) service).getStepBinder();
            if(SPHelper.getBaseMsg(getActivity(), "bindkey", "3").equals("3")){
                simpleStep.initDevice();
            }else{
                simpleStep.stop();
            }
            simpleStep.initData();
            showData(simpleStep.getNowTotalStep());
        }
        @Override
        public void onServiceDisconnected(ComponentName name) {
        }
    };

    void delStepData() {
        SimpleDateFormat date = new SimpleDateFormat("yyyyMMddhhmmss");
        Date mydate = new Date();
        String filenm = date.format(mydate);
        if (!SPHelper.getBaseMsg(getActivity(), "delval", "").equals(filenm)) {
            long initval = SPHelper.getDetailMsg(getActivity(), "lastval",new Long(12));
            GregorianCalendar gc = new GregorianCalendar();
            gc.setTime(new Date());
            gc.add(5, -8);
            long bval = gc.getTime().getTime();
            initval = initval < bval ? initval : bval;
            simpleStep.delData(initval);
            SPHelper.setBaseMsg(getActivity(), "delval", filenm);
        }
    }

    private void wxcircle() {
        hide();
        Context context = getActivity();
        if (context != null) {
            dialog = CommHelper.createLoadingDialog(getActivity(), "", SPHelper.getDetailMsg(getActivity(), Cons.APP_SEX, "M"));
            MainActivity activity = (MainActivity) context;
            // 添加微信平台
            UMWXHandler wxCircleHandler = new UMWXHandler(getActivity(), Cons.WEIXINGONGZHONGHAO,Cons.WEIXINSCRECT);
            wxCircleHandler.showCompressToast(false);
            wxCircleHandler.setToCircle(true);
            wxCircleHandler.addToSocialSDK();
            final UMSocialService mController = activity.mController;
            CircleShareContent circleShareContent = new CircleShareContent();
            circleShareContent.setShareImage(new UMImage(getActivity(),BitmapUtil.createViewBitmap(shareView)));
            mController.setShareMedia(circleShareContent);
            mController.getConfig().closeToast();
            CommHelper.insert_visit(context, "friendpg");
            postshare(mController, SHARE_MEDIA.WEIXIN_CIRCLE);
        }
        show();
    }

    void postshare(UMSocialService mController, SHARE_MEDIA media) {
        mController.postShare(getActivity(), media, new SnsPostListener() {
            @Override
            public void onStart() {
            }
            @Override
            public void onComplete(SHARE_MEDIA platform, int eCode,SocializeEntity entity) {
                if (eCode == 200) {
                    ToastManager.show(getActivity(), getResources().getString(R.string.sharesuceess), 2000);
                }
            }
        });
    }

    void showData(DayStep daystep) {
        int goal = SPHelper.getDetailMsg(getActivity(), "totalsteps", 10000);
        progressBar.setMaxstepnumber(goal);
        progressBar.update(daystep.getTotal_step(), daystep.getFast_step());
        HashMap<String, Float> map = progressBar.getPoint();
        float totalangle = progressBar.getTotalAngle();
        float x = map.get("pointx");
        float y = map.get("pointy");
        int pwidth = progressBar.getwidth();
        int pheight = progressBar.getheight();
        int bwidth = cb_person.getwidth();
        int bheight = cb_person.getheight();
        int chax = (bwidth - pwidth) / 2;
        int chay = (bheight - pheight) / 2;
        cb_person.update(x + chax, y + chay, totalangle);
        int  height = SPHelper.getDetailMsg(getActivity(), "height", 170);
        int weight = SPHelper.getDetailMsg(getActivity(), "weight", 65);
        double dis = StepTool.calStepDistance(height, daystep.getTotal_step()- daystep.getFast_step(), daystep.getFast_step());
        double cal = StepTool.calc_calories(weight, daystep.getFast_step(),daystep.getTotal_step() - daystep.getFast_step());
        tv_distance.setText(Integer.toString(dealPoint(dis)));
        tv_cal.setText(Integer.toString(dealPoint(cal)));
        Food food = getFoodMsg(getCalFoodId(getCalorieIndex((int) cal)));
        stotal.setText(daystep.getTotal_step() + "");
        sfast.setText(daystep.getFast_step() + "");
        if (food != null) {
            double beishu = cal / 500;
            String hui = beishu < 2 ? "1" : String.valueOf((int) beishu);
            firstnm.setText(" ≈ " + hui + food.getUnit());
            lastnm.setText(food.getName());
            int resId = getResources().getIdentifier(food.getPicname(),"drawable", "com.example.renrenstep");
            middlenm.setImageDrawable(getResources().getDrawable(resId));
        }
        saveObject("object.dat", daystep);
    }

    private void saveObject(String name,Object obj){
        FileOutputStream fos = null;
        ObjectOutputStream oos = null;
        try {
            fos = getActivity().openFileOutput(name, getActivity().MODE_PRIVATE);
            oos = new ObjectOutputStream(fos);
            oos.writeObject(obj);
        } catch (Exception e) {
            e.printStackTrace();
            //这里是保存文件产生异常
        } finally {
            if (fos != null){
                try {
                    fos.close();
                } catch (IOException e) {
                    //fos流关闭异常
                    e.printStackTrace();
                }
            }
            if (oos != null){
                try {
                    oos.close();
                } catch (IOException e) {
                    //oos流关闭异常
                    e.printStackTrace();
                }
            }
        }
    }

    int dealPoint(double cal) {
        return new Double(cal).intValue();
    }

    private void initLocation() {
        // TODO Auto-generated method stub
        mLocationClient = new LocationClient(getActivity()); // 声明LocationClient类
        mLocationClient.start();
        mLocationClient.registerLocationListener(myListener); // 注册监听函数

        if (mLocationClient != null && mLocationClient.isStarted()) {
            mLocationClient.requestLocation();
        }
    }

    public LocationClient mLocationClient = null;
    public BDLocationListener myListener = new MyLocationListener();

    public class MyLocationListener implements BDLocationListener {
        @Override
        public void onReceiveLocation(BDLocation location) {
            if (location == null) {
                return;
            }
            double latitude = location.getLatitude();
            double longitude = location.getLongitude();
            getWeather(longitude, latitude);
            mLocationClient.stop();
        }
    }

    Food getFoodMsg(int id) {
        List<FoodCal> list = FoodXmlHandler.getFoods(getActivity());
        for (FoodCal cal : list) {
            for (Food food : cal.getFoods()) {
                if (food.getId() == id) {
                    return food;
                }
            }
        }
        return null;
    }
    int getCalFoodId(int index) {
        try {
            Date nowTime = new Date();
            SimpleDateFormat time = new SimpleDateFormat("yyyy-MM-dd");
            String key = time.format(nowTime);
            Set<String> res = SPHelper.getDetailMsg(getActivity(), key,new HashSet<String>());
            String[] foodrec = (String[]) res.toArray(new String[res.size()]);
            int[] iarr = StringToInt(foodrec);
            if (index < 0) {
                return -1;
            } else if (res.size() <= index) {
                return iarr[iarr.length - 1];
            }
            return iarr[index];
        } catch (Exception ex) {
            return 0;
        }
    }

    int[] StringToInt(String[] arr) {
        int[] iarr = new int[arr.length];
        for (int i = 0; i < arr.length; i++) {
            iarr[i] = Integer.parseInt(arr[i]);
        }
        Arrays.sort(iarr);
        return iarr;
    }

    int getCalorieIndex(double cal) {
        int index = -1;
        double[][] arr = { { 0.1, 7 }, { 7, 50 }, { 50, 70 }, { 70, 81 },
                { 81, 100 }, { 100, 108 }, { 108, 130 }, { 130, 140 },
                { 140, 156 }, { 156, 180 }, { 180, 202 }, { 202, 260 },
                { 260, 381 }, { 381, 424 }, { 424, 524 }, { 524, 540 },
                { 550, 50000 } };
        for (int i = 0; i < arr.length; i++) {
            if (cal > arr[i][0] && cal <= arr[i][1]) {
                index = i;
                break;
            }
        }
        return index;
    }
    class StepHanlder extends  Handler{
        private WeakReference<HomeFragment> home;
        public  StepHanlder(HomeFragment home){
            this.home=new WeakReference<HomeFragment>(home);
        }
        @Override
        public void handleMessage(Message msg) {
            HomeFragment homefragment= this.home.get();
            if(homefragment!=null){
                DayStep ds = (DayStep) msg.obj;
                homefragment.showData(ds);
            }
            super.handleMessage(msg);
        }
    }

    private StepHanlder stephandler=new StepHanlder(this);
    public void registerBoradcastReceiver() {
        IntentFilter myIntentFilter = new IntentFilter(Cons.PROGRESS_MUSIC_ACTION);
        getActivity().registerReceiver(reciver, myIntentFilter);
    }

    private BroadcastReceiver reciver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            // TODO Auto-generated method stub
            DayStep step = (DayStep) intent.getExtras().getSerializable("key");
            Message msg = new Message();
            msg.obj = step;
            stephandler.sendMessage(msg);
        }
    };
    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container,  Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_home, null);
        gender = SPHelper.getDetailMsg(getActivity(), Cons.APP_SEX, "M");
        shareView = inflater.inflate(R.layout.sliding_share, null);
        tv_title = (TextView) shareView.findViewById(R.id.tv_title);
        tv_share = (TextView) shareView.findViewById(R.id.tv_share);
        iv_sina = (ImageView) shareView.findViewById(R.id.iv_sina);
        iv_wechat = (ImageView) shareView.findViewById(R.id.iv_wechat);
        layouterweima = (LinearLayout) shareView.findViewById(R.id.worldperson);
        iv_wxcircle = (ImageView) shareView.findViewById(R.id.iv_wxcircle);
        iv_sina.setOnClickListener(this);
        iv_wechat.setOnClickListener(this);
        iv_wxcircle.setOnClickListener(this);
        initView();
        CommHelper.insert_visit(getActivity(), "mianpg");
        PermissionHelper per=new PermissionHelper(getActivity());
        List<Permin> list = per.getPermin();
        if(!CommHelper.hasAllPermin(list, getActivity())){
            initPerminssion();
        }
        if(SPHelper.getBaseMsg(getActivity(),"isfrist", "0").equals("0")&&CommHelper.isLockedPhone()&&SPHelper.getBaseMsg(getActivity(), "bindkey", "3").equals("3")){
            specialAlter();
            SPHelper.setBaseMsg(getActivity(),"isfrist", "1");
        }
        if(SPHelper.getDetailMsg(getActivity(), "nc", "").equals("")){
            getNcDialog();
        }
        ((LinearLayout)view.findViewById(R.id.linear_log)).setOnClickListener(this);
        getGameDialog();
        return view;
    }
    private Dialog speicalDialog;
    private void specialAlter(){
        speicalDialog=new Dialog(getActivity());
        String title= getResources().getString(R.string.hint);
        String content=getResources().getString(R.string.huawei);
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.open_app, null);
        Button bt_finish=(Button)view.findViewById(R.id.bt_finish);
        TextView tv_content=(TextView)view.findViewById(R.id.tv_content);
        TextView tv_title = (TextView)view.findViewById(R.id.tv_title);
        tv_content.setText(content);
        tv_title.setText(title);
        tv_title.getPaint().setFakeBoldText(true);
        bt_finish.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                speicalDialog.dismiss();
            }
        });
        bt_finish.setTextColor(gender.equals("F")?0xffff0000:0xff3D98FF);
        speicalDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        speicalDialog.setCanceledOnTouchOutside(false);
        speicalDialog.setContentView(view);
        speicalDialog.show();
    }
    Dialog gameDialog;
    void getGameDialog(){
        String walkid = SPHelper.getDetailMsg(getActivity(),"walkid","");
        if(!walkid.equals("")){
            View view = LayoutInflater.from(getActivity()).inflate(R.layout.dialog_game, null);
            TextView txt_perioids=(TextView)view.findViewById(R.id.txt_perioids);
            TextView txt_saizhi=(TextView)view.findViewById(R.id.txt_saizhi);
            TextView txt_inverst_title=(TextView)view.findViewById(R.id.txt_inverst_title);
            TextView txt_game_title=(TextView)view.findViewById(R.id.txt_game_title);
            txt_perioids.setText( SPHelper.getDetailMsg(getActivity(),"periods","")+"周期");
            txt_saizhi.setText(SPHelper.getDetailMsg(getActivity(),"walktyp",""));
            txt_inverst_title.setText(SPHelper.getDetailMsg(getActivity(),"fromid","")+"邀请您参加比赛");
            txt_game_title.setText(SPHelper.getDetailMsg(getActivity(),"walknm",""));
            Button btn_confirm=(Button)view.findViewById(R.id.btn_confirm);
            Button btn_cancel=(Button)view.findViewById(R.id.btn_cancel);
            final Map<String,Object> map=new HashMap<String, Object>();
            map.put("gid", walkid);
            btn_confirm.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View arg0) {
                    sendHttp(Cons.GAMEAGREE,map,new dialogCallback(){
                        @Override
                        public void handler() {
                            gameDialog.dismiss();
                            SPHelper.setDetailMsg(getActivity(),"walkid","");
                        }
                    });
                }
            });
            btn_cancel.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View arg0) {
                    sendHttp(Cons.GAMEREFUSE,map,new dialogCallback(){
                        @Override
                        public void handler() {
                            gameDialog.dismiss();
                            SPHelper.setDetailMsg(getActivity(),"walkid","");
                        }
                    });
                }
            });
            gameDialog=getDialog(view, R.style.dialog1);
            gameDialog.show();
        }
    }
    interface dialogCallback{
        void handler();
    }
    void sendHttp(String url,Map<String,Object> params,final dialogCallback callback){
        HttpTool.HttpRequestAsy(url, params, BaseAsyncTask.HttpType.Post,"", getActivity(), new IHttpCallBackAsy() {
            @Override
            public void success(String param) {
                //gameDialog.dismiss();
                callback.handler();
            }
            @Override
            public void failed(String pmsg) {
                ToastManager.show(getActivity(),pmsg, 2000);
            }
            @Override
            public void exception(String pmsg) {
                ToastManager.show(getActivity(), getResources().getString(R.string.wangluoyic), 2000);
            }
        });
    }
    void getNcDialog(){
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.nc_dialog, null);
        final EditText edit_nc=(EditText)view.findViewById(R.id.edit_nc);
        final Dialog ncDialog = getDialog(view,R.style.dialog1);
        Button btn_confirm=(Button)view.findViewById(R.id.btn_confirm);
        btn_confirm.setBackgroundResource(gender.equals("M")?R.drawable.nc_btn_blue:R.drawable.nc_btn_red);
        btn_confirm.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View arg0) {
                final String nm= edit_nc.getText().toString();
                if(nm.equals("")){
                    ToastManager.show(getActivity(), getResources().getString(R.string.not_name), 2000);
                    return;
                }
                Map<String,Object> maps=new HashMap<String,Object>();
                maps.put("nc", nm);
                HttpTool.HttpRequestAsy(Cons.UPDATE_INFO, maps, BaseAsyncTask.HttpType.Post,"", getActivity(), new IHttpCallBackAsy() {
                    @Override
                    public void success(String param) {
                        ncDialog.dismiss();
                        SPHelper.setDetailMsg(getActivity(),"nc", nm);
                    }
                    @Override
                    public void failed(String pmsg) {
                        ToastManager.show(getActivity(),pmsg, 2000);
                    }
                    @Override
                    public void exception(String pmsg) {
                        ToastManager.show(getActivity(), getResources().getString(R.string.wangluoyic), 2000);
                    }
                });
            }
        });
        ncDialog.show();
    }

    void initPerminssion(){
        try {
            Date date2 = new Date();
            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
            String predate = SPHelper.getBaseMsg(getActivity(), "predate", "");
            if (!predate.equals("")) {
                Date date = df.parse(predate);
                long diff = date2.getTime() - date.getTime();
                long days = diff / (1000 * 60 * 60 * 24);
                if(days>=7){
                    alertPerminssion();
                    SPHelper.setBaseMsg(getActivity(), "predate", df.format(date2));
                }
            } else {
                alertPerminssion();
                SPHelper.setBaseMsg(getActivity(), "predate", df.format(date2));
            }
        } catch (Exception ex) {
        }
    }
    Dialog manager=null;
    void alertPerminssion() {
        manager = getDialog(CommHelper.getAlterView(getActivity(), new CommHelper.IAleterCallback() {
            @Override
            public void aleterCallback() {
                manager.dismiss();
            }
        }),R.style.dialog1);
        manager.show();
    }

    private void initView() {
        share_body = (LinearLayout) shareView.findViewById(R.id.layout_body);
        share_actionbar = (RelativeLayout) shareView
                .findViewById(R.id.layout_actionbar);
        stotal = (TextView) shareView.findViewById(R.id.stotalstep);
        sfast = (TextView) shareView.findViewById(R.id.sfaststep);
        sname = (TextView) shareView.findViewById(R.id.tv_name);
        stimer = (TextView) shareView.findViewById(R.id.tv_time);
        simg = (view.CircleImageView) shareView.findViewById(R.id.icon);
        Date nowTime = new Date();
        SimpleDateFormat time = new SimpleDateFormat("yyyy-MM-dd");
        stimer.setText(time.format(nowTime));
        iv_share = (ImageView) view.findViewById(R.id.iv_share);
        iv_share.setOnClickListener(this);
        ll_exit_share = (LinearLayout) shareView.findViewById(R.id.ll_exit_share);
        ll_exit_share.setOnClickListener(this);
        iv_notify = (ImageView) view.findViewById(R.id.iv_notify);
        iv_notify.setOnClickListener(this);
        layout_actionbar = (RelativeLayout) view.findViewById(R.id.layout_actionbar);
        tv_distance = (TextView) view.findViewById(R.id.tv_distance);
        Typeface mFace = Typeface.createFromAsset(getActivity().getAssets(),"HELVETICANEUELTPRO-CN.OTF");
        tv_distance.setTypeface(mFace);
        tv_cal = (TextView) view.findViewById(R.id.tv_cal);
        tv_cal.setTypeface(mFace);
        tv_city = (TextView) view.findViewById(R.id.tv_city);
        tv_max_temperature = (TextView) view.findViewById(R.id.tv_max_temperature);
        tv_weather = (TextView) view.findViewById(R.id.tv_weather);
        tv_pm = (TextView) view.findViewById(R.id.tv_pm);
        tv_sport = (TextView) view.findViewById(R.id.tv_sport);
        progressBar = (CircleBar) view.findViewById(R.id.cb_progress);
        progressBar.setMaxstepnumber(10000);
        progressBar.setOnClickListener(this);
        cb_person = (IconBar) view.findViewById(R.id.cb_person);
        menu = new SlidingMenu(getActivity());
        registerBoradcastReceiver();
        Intent service = new Intent(getActivity(), SimpleStepService.class);
        getActivity().startService(service);
        isbind = getActivity().bindService(service, myconn, Context.BIND_AUTO_CREATE);

        firstnm = (TextView) view.findViewById(R.id.firstnm);
        middlenm = (ImageView) view.findViewById(R.id.middlenm);
        lastnm = (TextView) view.findViewById(R.id.lastnm);
        imageView1 = (ImageView) view.findViewById(R.id.imageView1);
        txt_noreadnotify = (TextView)view.findViewById(R.id.txt_noreadnotify);
        initLocation();
    }

    void initStepDevice(){
        if(SPHelper.getBaseMsg(getActivity(), "bindkey", "").equals("1")){
            ((LinearLayout)view.findViewById(R.id. layouter_devise)).setVisibility(View.VISIBLE);
        }else{
            ((LinearLayout)view.findViewById(R.id. layouter_devise)).setVisibility(View.GONE);
        }
    }

    void initdata() {
        gender = SPHelper.getDetailMsg(getActivity(), Cons.APP_SEX,getString(R.string.appsex_man));
        layout_actionbar.setBackgroundResource(BGHelper.setBackground(getActivity(), gender));
        share_actionbar.setBackgroundResource(BGHelper.setBackground(getActivity(), gender));
        share_body.setBackgroundResource(BGHelper.setShare(getActivity(), gender));
        int newnotify = SPHelper.getDetailMsg(MyApplication.getInstance(), "newnotifynum", 0);
        int oldnotify = SPHelper.getDetailMsg(MyApplication.getInstance(), "oldnotifynum", 0);
        int cha_num = newnotify - oldnotify;
        txt_noreadnotify.setVisibility(cha_num>0?View.VISIBLE:View.GONE);
        txt_noreadnotify.setText(cha_num+"");
    }
    @Override
    public void onResume() {
        // TODO Auto-generated method stub
        super.onResume();
        initdata();
        if (simpleStep != null) {
            showData(simpleStep.getNowTotalStep());
        }
        if (dialog != null) {
            dialog.dismiss();
        }
        myResume();
    }

    public void myResume(){
        initStepDevice();
        if(simpleStep==null){
            return;
        }
        String binddev=SPHelper.getBaseMsg(getActivity(), "bindkey", "");
        if(binddev.equals("3")){
            simpleStep.initDevice();
        }else{
            simpleStep.stop();
            simpleStep.initData();
            showData(simpleStep.getNowTotalStep());
        }
    }

    public void demo_Click(View view) {
        String res = "";
    }

    public boolean isSlidingShow() {

        if (menu != null && menu.isSecondaryMenuShowing()) {
            menu.showContent();
            return true;
        }
        if (shareMenu != null && shareMenu.isSecondaryMenuShowing()) {
            shareMenu.showContent();
            return true;
        }
        return false;
    }

    private void getWeather(double longitude, double latitude) {
        final double lon = longitude;
        final double lat = latitude;
        Map<String, Object> maps = new HashMap<String, Object>();
        maps.put("lon", longitude);
        maps.put("lat", latitude);
        new BaseAsyncTask(Cons.WEATHER_URL, maps, BaseAsyncTask.HttpType.Get, "",getActivity()) {
            @Override
            public void handler(String param) {
                if (param != null && param.contains("status")) {
                    try {
                        JSONObject jsonObject = new JSONObject(param);
                        int status = jsonObject.getInt("status");
                        if (status == 0) {
                            JSONObject obj = jsonObject.getJSONObject("data");
                            String city = obj.getString("city");
                            String min_temperature = obj.getString("min_temperature");
                            String max_temperature = obj.getString("max_temperature");
                            String weather = obj.getString("weather");
                            String pm25 = obj.getString("pm25");
                            String out_sports = obj.getString("out_sports");
                            String daypic = obj.getString("dayurl");
                            String nightpic = obj.getString("nighturl");
                            SPHelper.setBaseMsg(getActivity(), "city", city);
                            SPHelper.setBaseMsg(getActivity(),"min_temperature", min_temperature);
                            SPHelper.setBaseMsg(getActivity(),"max_temperature", max_temperature);
                            SPHelper.setBaseMsg(getActivity(), "weather",weather);
                            SPHelper.setBaseMsg(getActivity(), "pm25", pm25);
                            SPHelper.setBaseMsg(getActivity(), "out_sports",out_sports);
                            SPHelper.setBaseMsg(getActivity(), "daypic", daypic);
                            SPHelper.setBaseMsg(getActivity(), "nightpic",nightpic);
                            SPHelper.setBaseMsg(getActivity(), "lon",String.valueOf(lon));
                            SPHelper.setBaseMsg(getActivity(), "lat",String.valueOf(lat));
                            loadNativeWeather();
                        } else {
                            loadNativeWeather();
                        }
                    } catch (Exception e) {
                        loadNativeWeather();
                    }
                } else {
                    loadNativeWeather();
                }
            }
        }.execute("");
    }

    void getweatherError() {
        String lon = SPHelper.getBaseMsg(getActivity(), "lon", "116.305145");
        String lat = SPHelper.getBaseMsg(getActivity(), "lat", "39.982368");
        getWeather(Double.parseDouble(lon), Double.parseDouble(lat));
    }

    protected void loadNativeWeather() {
        String city = SPHelper.getBaseMsg(getActivity(), "city", "");
        if (!city.equals("")) {
            String min_temperature = SPHelper.getBaseMsg(getActivity(),"min_temperature", "");
            String max_temperature = SPHelper.getBaseMsg(getActivity(),"max_temperature", "");
            String weather = SPHelper.getBaseMsg(getActivity(), "weather", "");
            String pm25 = SPHelper.getBaseMsg(getActivity(), "pm25", "");
            String out_sports = SPHelper.getBaseMsg(getActivity(),"out_sports", "");
            String daypic = SPHelper.getBaseMsg(getActivity(), "daypic", "");
            String nightpic = SPHelper.getBaseMsg(getActivity(), "nightpic", "");
            tv_city.setText(city);
            tv_city.setText(city);
            tv_max_temperature.setText(min_temperature + "°~" + max_temperature+ "°");
            tv_weather.setText(weather);
            tv_pm.setText("PM2.5：" + pm25);
            tv_sport.setText(out_sports+ getResources().getString(R.string.sportsweather));
            Calendar candler = Calendar.getInstance();
            int hour = candler.get(Calendar.HOUR_OF_DAY);
            int resid = 0;
            if (hour < 18) {
                resid = getResources().getIdentifier(daypic, "drawable","com.example.renrenstep");
            } else {
                resid = getResources().getIdentifier(nightpic, "drawable","com.example.renrenstep");
            }
            imageView1.setImageResource(resid);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_notify:
                CommHelper.insert_visit(getActivity(), "notifypg");
                Intent mintent = new Intent(getActivity(), ReportActivity.class);
                mintent.putExtra("pagenm",getResources().getString(R.string.notifycenter));
                mintent.putExtra("gender",SPHelper.getDetailMsg(getActivity(), Cons.APP_SEX, "M"));
                mintent.putExtra("url",String.format(Cons.NOTIFYTEXT,"notify","step",SPHelper.getBaseMsg(getActivity(), "mid", "0")));
                getActivity().startActivity(mintent);
                break;
            case R.id.cb_progress:
                Intent intent = new Intent(getActivity(), StepDataActivity.class);
                startActivity(intent);
                break;
            case R.id.iv_share:
                if (!isShareMenu) {
                    String uri = SPHelper.getDetailMsg(getActivity(), "uri", "");
                    Options options = new Options();
                    options.inSampleSize = 2;
                    Bitmap bitmap = BitmapFactory.decodeFile(uri, options);
                    if (bitmap != null) {
                        simg.setImageBitmap(bitmap);
                    }
                    sname.setText(SPHelper.getDetailMsg(getActivity(), "nc", ""));
                    isShareMenu = true;
                    shareMenu = new SlidingMenu(getActivity());
                    setMenu(shareMenu, shareView);
                }
                CommHelper.insert_visit(getActivity(), "sharepg");
                sname.setText(SPHelper.getDetailMsg(getActivity(), "nc", ""));
                sname.setTextColor(SPHelper.getDetailMsg(getActivity(),Cons.APP_SEX, "M").equals("M") ? 0xffff0000 : 0xff3D98FF);
                shareMenu.showSecondaryMenu();
                break;
            case R.id.ll_exit_share:
                if (shareMenu.isSecondaryMenuShowing())
                    shareMenu.showContent();
                break;
            case R.id.iv_sina:
                sina();
                break;
            case R.id.iv_wechat:
                wechat();
                break;
            case R.id.iv_wxcircle:
                wxcircle();
                break;
            case R.id.linear_log:
                Intent logintent=new Intent(getActivity(),LogInfoActivity.class);
                startActivity(logintent);
                break;
            default:
                break;
        }
    }

    void hide() {
        ll_exit_share.setVisibility(View.GONE);
        tv_title.setVisibility(View.GONE);
        tv_share.setVisibility(View.GONE);
        iv_wechat.setVisibility(View.GONE);
        iv_sina.setVisibility(View.GONE);
        iv_wxcircle.setVisibility(View.GONE);
        layouterweima.setVisibility(View.VISIBLE);
    }

    void show() {
        ll_exit_share.setVisibility(View.VISIBLE);
        tv_title.setVisibility(View.VISIBLE);
        tv_share.setVisibility(View.VISIBLE);
        iv_wechat.setVisibility(View.VISIBLE);
        iv_sina.setVisibility(View.VISIBLE);
        iv_wxcircle.setVisibility(View.VISIBLE);
        layouterweima.setVisibility(View.INVISIBLE);
    }

    public void wechat() {
        hide();
        Context context = getActivity();
        if (context != null) {
            dialog = CommHelper.createLoadingDialog(getActivity(), "",SPHelper.getDetailMsg(context, Cons.APP_SEX, "M"));
            dialog.show();
            MainActivity activity = (MainActivity) context;
            String appID = Cons.WEIXINGONGZHONGHAO;
            String appSecret = Cons.WEIXINSCRECT;//"8889448ea02418ee2817a11ea02d398b";// 添加微信平台
            UMWXHandler wxHandler = new UMWXHandler(getActivity(), appID,
                    appSecret);
            wxHandler.addToSocialSDK();
            final UMSocialService mController = activity.mController;
            WeiXinShareContent weixinContent = new WeiXinShareContent();
            weixinContent.setShareImage(new UMImage(getActivity(), BitmapUtil.createViewBitmap(shareView)));
            mController.setShareMedia(weixinContent);
            mController.getConfig().closeToast();//设置分享图片, 参数2为图片的url地址
            mController.postShare(getActivity(), SHARE_MEDIA.WEIXIN,
                    new SnsPostListener() {
                        @Override
                        public void onStart() {

                        }
                        @Override
                        public void onComplete(SHARE_MEDIA platform, int eCode,SocializeEntity entity) {
                            Context mContext = getActivity();
                            if (eCode == 200 && mContext != null && isAdded()) {
                                CommHelper.insert_visit(mContext, "weixinpg");
                                Toast.makeText(mContext,mContext.getResources().getString(R.string.share_success), 2000).show();
                            }
                        }
                    });
        }
        show();
    }

    public void sina() {
        hide();
        Context context = getActivity();
        MainActivity activity = (MainActivity) context;
        dialog = CommHelper.createLoadingDialog(getActivity(), "",SPHelper.getDetailMsg(getActivity(), Cons.APP_SEX, "M"));
        Bitmap bitmap = BitmapUtil.createViewBitmap(shareView);
        final UMSocialService mController = activity.mController;
        // 设置分享内容
        mController.setShareImage(new UMImage(getActivity(), bitmap));
        mController.getConfig().closeToast();
        mController.getConfig().setSinaCallbackUrl("http://sns.whalecloud.com/sina2/callback");
        mController.getConfig().setSsoHandler(new SinaSsoHandler());
        postshare(mController, SHARE_MEDIA.SINA);
        CommHelper.insert_visit(context, "weibopg");
        show();
    }

    private void setMenu(SlidingMenu menu, View slidingView) {
        menu.setMode(SlidingMenu.RIGHT);
        // 设置触摸屏幕的模式
        menu.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);
        menu.attachToActivity(getActivity(), SlidingMenu.SLIDING_CONTENT);
        menu.setBehindWidth(getActivity().getWindowManager().getDefaultDisplay().getWidth());
        // 为侧滑菜单设置布局
        menu.setMenu(slidingView);
        menu.setTouchModeAbove(SlidingMenu.TOUCHMODE_NONE);
    }
    @Override
    public void onDestroy() {
        // TODO Auto-generated method stub
        if (simpleStep != null) {
            simpleStep.SaveStepData();
        }
        if (getActivity() != null && myconn != null && reciver != null && isbind) {
            getActivity().unbindService(myconn);
            getActivity().unregisterReceiver(reciver);
        }
        super.onDestroy();
    }

}
