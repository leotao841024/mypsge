package constant;

public class Cons {
	public static final int TAB_HOME = 0;
	public static final int TAB_MINE = 1;
	public static final boolean isdev = false;
	public static final String ADDSINGAL="add";
	public static final String USER_INFO = "appConf";
	public static final String APP_SEX = "gender";
	public static final String NORIFY_RECEIVER = "android.action.NOTIFYRECEIVER";
	public static final String STEPS_RECEIVER = "android.action.STEPSRECEIVER";
	public static final int HISDAY = 8;
	public static final String BASE_URL = isdev?"http://192.168.0.153:1110":"http://www.renjk.com";
	//下载头像
	public static final String DONW_PIC =isdev?BASE_URL+"/upload/mem/":"http://assets.renjk.com/mem/";
	//上传数据url
	public static final String UPLOAD_STEPDATA_URL=isdev?"http://192.168.0.84:811/ReceiveService.asmx/Receive":"http://receive.renjk.com/ReceiveService.asmx/Receive";

	public static final String RECOMMEN_URL=BASE_URL+"/mem/sportfree";

	public static final String DOWN_NEW_EDITION=BASE_URL+"/app_down/RenrenStep.apk";
	//登陆
	public static final String LOGIN_URL = BASE_URL+"/api/mem/login_by_phone";
	//更新info
	public static final String UPDATE_INFO = BASE_URL+"/api/mem/up_mem_info";
	//获取运动目标
	public static final String SPORT_TARGET = BASE_URL+"/api/mem/get_mem_sport_goal";
	//注册
	public static final String REGIST_URL = BASE_URL+"/api/mem/register_by_phone";
	//找回密码sendcode
	public static final String SEND_CODE = BASE_URL+"/api/mem/send_code";
	//第二次邮箱发送验证码
	public static final String NEXT_CODE = BASE_URL+"/api/mem/sendcode_by_bindemail";
	//第二次提交邮箱
	public static final String NEXT_EMAIL = BASE_URL+"/api/mem/bind_email";
	//找回密码
	public static final String CHECK_CODE = BASE_URL+"/api/mem/validate_code";
	//修改密码
	public static final String CHANGE_CODE = BASE_URL+"/api/mem/update_mem_pwd";
	//获取天气
	public static final String WEATHER_URL = BASE_URL+"/api/phone/get_weather";
	//绑定手机
	public static final String BIND_PHONE = BASE_URL+"/api/mem/sendcode_by_bindphone";

	public static final String PROGRESS_MUSIC_ACTION = "com.ct.dynamicui.PROGRESS_MUSIC_ACTION";

	public static final String RECIVE_MSG_ACTION="com.example.renrenstep.RECIVE_MSG_ACTION";
	//绑定手机 checkcode
	public static final String CHECK_PHONE = BASE_URL+"/api/mem/bind_phone";
	//修改密码
	public static final String CONF_SECRET = BASE_URL+"/api/mem/alter_mem_pwd";
	//通知中心
	public static final String NOTIFY_URL = BASE_URL+"/api/mem/get_noticy_msg";
	//上传头像
	public static final String UPLOAD_HEAD = BASE_URL+"/api/upload/save_mem_pic";
	//获取头像
	public static final String DONWLOAD_HEAD = BASE_URL+ "/api/mem/get_mem_pic";
	//获取info
	public static final String GET_INFO = BASE_URL+"/api/mem/get_mems_info";
	//发送更改邮箱验证码
	public static final String CHANGE_EMAIL_CODE = BASE_URL+"/api/mem/sendcode_by_bindemail";
	//验证绑定邮箱
	public static final String CHANGE_EMAILNO = BASE_URL+"/api/mem/check_uid_code";

	//获取版本号 
	public static final String GET_VERSION = BASE_URL+"/api/mem/get_app_version";
	//检查设备
	public static final String INSPECT_DEVICE = BASE_URL+"/api/mem/mem_has_devise";
	//接触绑定设备
	public static final String UPDATE_DEVISE = BASE_URL+"/api/mem/update_devise_invalid";
	//报告
	public static final String REPORT_URL= BASE_URL+"/app/walkdata/report?platform=phone&mid=";
	//用户协议
	public static final String PROTOCOL=BASE_URL+"/mem/protocol";
	//PV统计
	public static final String PVCALURL=BASE_URL+"/api/mem/app_pv_total";
	//验证验证码
	public static final String VALID_CODE=BASE_URL+"/api/mem/check_uid_code";
	//首页
	public static final String FIRSTPG="firstpg";
	//首页
	public static final String TODAYPG="todaypg";
	//首页
	public static final String HISPG="hispg";
	//首页
	public static final String PERINFOPG="perinfopg";
	//首页
	public static final String NOTIFYPG="notifypg";
	//首页
	public static final String SHAREPG="sharepg";
	//首页
	public static final String REPORTPG="reportpg";
	//上传埋点信息
	public static final String UPLOAD_MAI_URL=BASE_URL+"/api/mem/app_pv_total";

	public static final String WEIXINGONGZHONGHAO="wxfe18eb8b90d17328";

	public static final String WEIXINSCRECT="8889448ea02418ee2817a11ea02d398b";//8889448ea02418ee2817a11ea02d398b

	public static final int FIRSTSTEP=100;

	public static final String WEIBO="http://weibo.com/selfdoctor";
	//获取绑定设备
	public static final String GET_DEVICES=BASE_URL+"/api/device/get_devices";
	//绑定设备
	public static final String BIND_DEVICE=BASE_URL+"/api/device/bind_device";
	//下载计步器数据
	public static final String DOWN_DATA=BASE_URL+"/api/device/download_step_data";
	//解绑设备
	public static final String UNBIND_DEVICE=BASE_URL+"/api/device/unbind_device";

	public static final String NOTIFYTEXT=BASE_URL+"/mem/apptextshow?typ=%s&nm=%s&mid=%s";

	public static final String SERACHMAN=BASE_URL+"/api/mem/friend/search_mem";

	public static final String DOWN_ANIMATE =  BASE_URL + (isdev ?"/upload/mem/":"/api/phone/down_sport_animate?filename=");

	public static final String APPLYURL =  BASE_URL +"/api/mem/friend/apply";

	public static final String APPLYLIST= BASE_URL +"/api/mem/friend/apply_list";

	public static final String AGREEAPPLY = BASE_URL +"/api/mem/friend/agree";

	public static final String APPMAILLIST=BASE_URL+"/api/mem/friend/list";

	//http://www.renjk.com/api/mem/friend/apply_list
	public static final String GETAPPLAYMANURL=BASE_URL+"/api/mem/friend/index";

	public static final String DELFRIEND = BASE_URL+"/api/mem/friend/destroy";

	public static final String GETDATA = BASE_URL+"/api/data";

	public static final String GAMEAGREE= BASE_URL+"/api/game/player/agree";

	public static final String GAMEREFUSE= BASE_URL+"/api/game/player/refuse";

	public static final String GAMEURL=BASE_URL+"/app/zouku/index?mid=";

	public static final String GET_SHARE_PERSON_RANK=BASE_URL+ "/api/game/get_share_person_rank";

	//renjk-mobile://renjk.com/action?id=1&b=2
	public static final String GET_REWARD_END_HIS_LIST=BASE_URL+ "/api/game/get_reward_end_his_list";
}



