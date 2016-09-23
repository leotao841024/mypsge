package fragment;
import java.lang.reflect.Method;
import java.net.URI;
import java.text.MessageFormat;
import java.util.HashMap;
import java.util.Map;

import tools.ToastManager;

import org.json.JSONObject;

import tools.BitmapUtil;
import tools.FileUtils;
import view.CircleImageView;
import helper.BGHelper;
import helper.SPHelper;

import com.example.renrenstep.MainActivity;
import com.example.renrenstep.R;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.bean.SocializeEntity;
import com.umeng.socialize.controller.UMSocialService;
import com.umeng.socialize.controller.listener.SocializeListeners;
import com.umeng.socialize.media.UMImage;
import com.umeng.socialize.sso.SinaSsoHandler;
import com.umeng.socialize.weixin.controller.UMWXHandler;
import com.umeng.socialize.weixin.media.CircleShareContent;
import com.umeng.socialize.weixin.media.WeiXinShareContent;

import helper.BaseAsyncTask;
import comm.CommHelper;
import constant.Cons;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class GameFragment extends IMBaseFragment implements OnClickListener {
    WebView webview;
    LinearLayout linear_noweb;
    private  Dialog dialog;
    private View layoutShareView,view;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        view= inflater.inflate(R.layout.fragment_game, null);
        return view;
    }

    void initView(){
        LinearLayout ll_share = (LinearLayout) view.findViewById(R.id.ll_share);
        layoutShareView =  view.findViewById(R.id.layoutShareView);
        webview=(WebView)view.findViewById(R.id.webview);
        linear_noweb=(LinearLayout)view.findViewById(R.id.linear_noweb);
        ll_share.setOnClickListener(this);
    }

    void initColor(){
        ((RelativeLayout)view.findViewById(R.id.layout_actionbar)).setBackgroundResource(BGHelper.setBackground(getActivity(),SPHelper.getDetailMsg(getActivity(), Cons.APP_SEX, "M")));
    }

    @Override
    public void onResume() {
        initView();
        initColor();
        initData();
        super.onResume();
    }

    void initData(){
        webview.getSettings().setJavaScriptEnabled(true);
        webview.loadUrl(Cons.GAMEURL+SPHelper.getBaseMsg(getActivity(), "mid", "0"));
        webview.setWebViewClient(new ClientWebView());
    }

    class ClientWebView extends WebViewClient{
        @Override
        public void onReceivedError(WebView view, int errorCode,String description, String failingUrl) {
            linear_noweb.setVisibility(View.VISIBLE);
            webview.setVisibility(View.GONE);
        }

//		@Override
//		public void onPageStarted(WebView view, String url, Bitmap favicon) {
//			// TODO Auto-generated method stub
//			ToastManager.show(getActivity(), "123123", 2000);
//			super.onPageStarted(view, url, favicon);
//		}

		@Override
		public boolean shouldOverrideUrlLoading(WebView view, String url) {
            // TODO Auto-generated method stub
			//ToastManager.show(getActivity(), "123123", 2000);
            //view.loadUrl(url);
            //view.stopLoading();
//            renjk-mobile://renjk.com/action?a=1&b=2
            URI uri = null;
            try {
                uri = new URI(url);
                Class cls=Class.forName("fragment.GameFragment");
                Object obj=cls.newInstance();
                Class[] param=new Class[1];
                param[0]=String.class;
                Method med=cls.getMethod( uri.getPath(),param);
                med.invoke(obj,uri.getQuery());
            } catch (Exception e) {
                e.printStackTrace();
            }
//            System.out.println(uri.getScheme());  //renjk-mobile
//            System.out.println(uri.getHost());   //renjk.com
//            System.out.println(uri.getPath());  //action
//            System.out.println(uri.getQuery());//a=1&b=2


            return false;
      }


//		@Override
//		public boolean shouldOverrideKeyEvent(WebView view, KeyEvent event) {
//			// TODO Auto-generated method stub
//			ToastManager.show(getActivity(), "1231231", 2000);
//			return super.shouldOverrideKeyEvent(view, event);
//		}
    }

    //	@Override
//	public void onClick(View arg0) {
//		switch (arg0.getId()) {
//		case R.id.linear_week_dao:
//			Intent intent=new Intent(getActivity(),WeekGameActivity.class);
//			startActivity(intent);
//			break;
//		default:
//			break;
//		}
//	}
//	
    @Override
    public void onClick(View arg0) {
        switch (arg0.getId()) {
            case R.id.ll_share:
                doShare();
                break;
            case R.id.iv_weixin_share:
                wechat();
                break;
            case R.id.iv_weibo_share:
                sina();
                break;
            case R.id.iv_wechat_circle:
                wxcircle();
                break;
            default:
                break;
        }
    }
    private void wxcircle() {
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
            circleShareContent.setShareImage(new UMImage(getActivity(),BitmapUtil.createViewBitmap(layoutShareView)));
            mController.setShareMedia(circleShareContent);
            mController.getConfig().closeToast();
            CommHelper.insert_visit(context, "friendpg");
            postshare(mController, SHARE_MEDIA.WEIXIN_CIRCLE);
        }
    }
    private void doShare() {
        //layoutShareView.setVisibility(View.VISIBLE);
        Map<String, Object> maps = new HashMap<String, Object>();
        new BaseAsyncTask(Cons.GET_SHARE_PERSON_RANK,maps, BaseAsyncTask.HttpType.Get,"", getActivity()){
            @Override
            public void handler(String param) throws RuntimeException {
                String avatar = SPHelper.getDetailMsg(getActivity(), "uri", "");
                String name= SPHelper.getDetailMsg(getActivity(), "nc", "");
                try {
                    JSONObject jsonObject = new JSONObject(param);
                    int status = jsonObject.getInt("status");
                    String description = jsonObject.getString("description");
                    SPHelper.setDetailMsg(getActivity(), "description", description);
                    if (status == 0) {
                        JSONObject obj = jsonObject.getJSONObject("data");
                        String typ = jsonObject.getString("typ");
                        String rank = obj.getString("rank");
                        String steps = obj.getString("steps");
                        String quicksteps = obj.getString("quicksteps");
                        String walknm = obj.getString("name");
                        initShareView(typ.equals("steps")?steps:quicksteps,walknm,rank,name,avatar,"");
                        setSuccessDialogData(typ.equals("steps")?steps:quicksteps,walknm,rank,name,avatar,"");
                    }else{
                        initShareView("","","",name,avatar,description);
                        setSuccessDialogData("","","",name,avatar,description);
                    }
                } catch (Exception e) {
                    initShareView("","","",name,avatar,e.getMessage());
                    setSuccessDialogData("","","",name,avatar,e.getMessage());
                }
            }
        }.execute("");
    }


    void initShareView(String psteps,String pwalknm,String prank,String pusernm,String pavator,String pexceptionmsg){
        TextView  txt_game_title = (TextView)view.findViewById(R.id.txt_team);
        TextView  txt_rank = (TextView)view.findViewById(R.id.txt_rank);
        TextView  txt_rankdata_title = (TextView)view.findViewById(R.id.txt_rankdata_title);

        RelativeLayout layout_share_data = (RelativeLayout)view.findViewById(R.id.layout_share_data);
        TextView  txt_share_data = (TextView)view.findViewById(R.id.txt_share_data);
        view.CircleImageView img_avatar=(view.CircleImageView)view.findViewById(R.id.img_avatar);
        TextView  tv_username = (TextView)view.findViewById(R.id.tv_username);
        if(pexceptionmsg.equals("")){
            txt_game_title.setText(MessageFormat.format(getResources().getString(R.string.share_item1), pwalknm));
            txt_rank.setText(MessageFormat.format(getResources().getString(R.string.share_item4), prank));
            txt_share_data.setText(MessageFormat.format(getResources().getString(R.string.share_item2), psteps));
            txt_rankdata_title.setText(getResources().getString(R.string.share_view_data_title));
        }else{
            txt_game_title.setText(pexceptionmsg);
        }
        tv_username.setText(MessageFormat.format(getResources().getString(R.string.share_item3), pusernm));
        Bitmap bitmap = BitmapFactory.decodeFile(pavator);
        if (bitmap != null) {
            img_avatar.setImageBitmap(bitmap);
        }
    }

    private void setSuccessDialogData(String psteps,String pwalknm,String prank,String pusernm,String pavator,String pexceptionmsg) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        View dialogView =LayoutInflater.from(getActivity()).inflate(R.layout.dialog_share_game_result,null);
        dialogView.findViewById(R.id.iv_weixin_share).setOnClickListener(this);
        dialogView.findViewById(R.id.iv_weibo_share).setOnClickListener(this);
        dialogView.findViewById(R.id.iv_wechat_circle).setOnClickListener(this);
        view.CircleImageView user_avatar = (CircleImageView) dialogView.findViewById(R.id.user_avatar);
        TextView tv_name = (TextView) dialogView.findViewById(R.id.tv_name);
        TextView tv_todaySteps = (TextView) dialogView.findViewById(R.id.tv_todaySteps);
        TextView tv_rank = (TextView) dialogView.findViewById(R.id.tv_rank);
        builder.setView(dialogView);
        builder.setCancelable(true);
        Bitmap bitmap = BitmapFactory.decodeFile(pavator);
        if (bitmap != null) {
            user_avatar.setImageBitmap(bitmap);
        }
        tv_name.setText(pusernm);
        if(pexceptionmsg.equals("")){
            tv_todaySteps.setText(MessageFormat.format(getResources().getString(R.string.share_step_data), psteps));
            tv_rank.setText(MessageFormat.format(getResources().getString(R.string.share_walk_nm), pwalknm,prank));
        }else{
            tv_todaySteps.setText(pexceptionmsg);
        }
        dialog = builder.create();
        dialog.show();
    }
    public void wechat() {
        if(dialog != null) dialog.dismiss();
        Context context = getActivity();
        if (context != null) {
            //layoutShareView.setVisibility(View.VISIBLE);
            MainActivity activity = (MainActivity) context;
            String appID = Cons.WEIXINGONGZHONGHAO;
            String appSecret = Cons.WEIXINSCRECT;//"8889448ea02418ee2817a11ea02d398b";// 添加微信平台
            UMWXHandler wxHandler = new UMWXHandler(getActivity(), appID,
                    appSecret);
            wxHandler.addToSocialSDK();
            final UMSocialService mController = activity.mController;
            WeiXinShareContent weixinContent = new WeiXinShareContent();
            Bitmap bitmap = BitmapUtil.createViewBitmap(layoutShareView);


            FileUtils fileUtils = new FileUtils("renrenstep");
            fileUtils.saveMyBitmap(System.currentTimeMillis()+".png",bitmap);

            weixinContent.setShareImage(new UMImage(getActivity(),bitmap));
            mController.setShareMedia(weixinContent);
            mController.getConfig().closeToast();//设置分享图片, 参数2为图片的url地址
            mController.postShare(getActivity(), SHARE_MEDIA.WEIXIN,
                    new SocializeListeners.SnsPostListener() {
                        @Override
                        public void onStart() {

                        }
                        @Override
                        public void onComplete(SHARE_MEDIA arg0, int arg1,
                                               SocializeEntity arg2) {
                            Context mContext = getActivity();
                            if (arg1 == 200 && mContext != null && isAdded()) {
                                CommHelper.insert_visit(mContext, "weixinpg");
                                Toast.makeText(mContext, mContext.getResources().getString(R.string.share_success), 2000).show();
                            }
                        }
                    });
        }

    }
    public void sina() {
        Context context = getActivity();
        //	layoutShareView.setVisibility(View.VISIBLE);
        MainActivity activity = (MainActivity) context;
        Bitmap bitmap = BitmapUtil.createViewBitmap(layoutShareView);

        FileUtils fileUtils = new FileUtils("renrenstep");
        fileUtils.saveMyBitmap(System.currentTimeMillis()+".png",bitmap);
        final UMSocialService mController = activity.mController;
        // 设置分享内容
        mController.setShareImage(new UMImage(getActivity(), bitmap));
        mController.getConfig().closeToast();
        mController.getConfig().setSinaCallbackUrl("http://sns.whalecloud.com/sina2/callback");
        mController.getConfig().setSsoHandler(new SinaSsoHandler());
        postshare(mController, SHARE_MEDIA.SINA);
        CommHelper.insert_visit(context, "weibopg");
    }

    void postshare(UMSocialService mController, SHARE_MEDIA media) {

        mController.postShare(getActivity(), media, new SocializeListeners.SnsPostListener() {
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


}
