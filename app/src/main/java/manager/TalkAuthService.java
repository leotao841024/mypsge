package manager;

import com.alibaba.wukong.Callback;
import com.alibaba.wukong.auth.ALoginParam;
import com.alibaba.wukong.auth.AuthInfo;
import com.alibaba.wukong.auth.AuthService;
import com.alibaba.wukong.im.IMEngine;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import app.MyApplication;
import comm.CommHelper;
import helper.SPHelper;

/**
 * Created by jam on 2016/4/14.
 */
public class TalkAuthService {
    public interface ILoginCallbak {
        void logionsuccess();
        void logionloser();
    }
    public static void initWkLogion(final ILoginCallbak plogion) {
        long oldopenId = AuthService.getInstance().latestAuthInfo().getOpenId();
        if (oldopenId != 0) {
            IMEngine.getIMService(AuthService.class).autoLogin(oldopenId);
            plogion.logionsuccess();
        } else {
            AuthService authService = IMEngine.getIMService(AuthService.class);
            Random random = new Random();
            String nonce = random.nextInt(6) + "";// 随机数
            long timestamp = System.currentTimeMillis() / 1000;// 时间戳
            String mid = SPHelper.getBaseMsg(MyApplication.getInstance(), "mid", "");
            long openId = mid.equals("") ? 0 : Long.parseLong(mid);// 用户标识
            String appSecret = "SrmZ15rCH9d5CcqoxACQhVX35P3K3gO0";
            String appToken = "SAjSeMlnXXlA2SiPGCfOCGmfo6hDsNpN";
            String signature = getSignature(appToken, appSecret, openId, nonce,timestamp);
            ALoginParam params = buildLoginParam(openId, nonce, timestamp,signature);
            authService.login(params, new Callback<AuthInfo>() {
                @Override
                public void onSuccess(AuthInfo arg0) {
                    plogion.logionsuccess();
                }
                @Override
                public void onProgress(AuthInfo arg0, int arg1) {

                }
                @Override
                public void onException(String arg0, String arg1) {
                    plogion.logionloser();
                }
            });
        }
    }
    static ALoginParam buildLoginParam(long openId, String nonce, long timestamp,
                                String signature) {
        ALoginParam param = new ALoginParam();
        param.domain = "ZouKu";
        param.openId = openId;// 用户id
        param.nonce = nonce;// 随机数
        param.timestamp = timestamp;// 时间戳 System.currentTimeMillis() / 1000
        param.signature = signature;
        return param;
    }
    static String getSignature(String appToken, String appSecret, long openId,String nonce, long timestamp) {
        List<String> slist = new ArrayList<String>();
        slist.add(appToken);
        slist.add(appSecret);
        slist.add("" + openId);
        slist.add(nonce);
        slist.add("" + timestamp);
        Collections.sort(slist);
        StringBuilder builder = new StringBuilder();
        for (String item : slist) {
            builder.append(item);
        }
        return new String(CommHelper.sha256Hex(builder.toString()));
    }
}
