package helper;

import android.content.Context;

import com.example.renrenstep.InfoActivity;
import com.example.renrenstep.R;

public class BGHelper {
	public static int setBackground(Context context, String sex) {
		String man = context.getString(R.string.appsex_man);
		String women = context.getString(R.string.appsex_women);
		if (sex.equals(man)) {
			return R.color.appcolor_blue;
		}
		if(sex.equals(women)){
			return R.color.appcolor_red;
		}
		return R.color.appcolor_blue;
	}
	
	public static int setbgButton(Context context, String sex){
		String man = context.getString(R.string.appsex_man);
		String women = context.getString(R.string.appsex_women);
		if (sex.equals(man)) {
			return R.drawable.bg_bt_blue;
		}
		if(sex.equals(women)){
			return R.drawable.bg_bt_red;
		}
		return R.drawable.bg_bt_blue;
	}
	
	public static int setbgCheckBox(Context context, String sex){
		String man = context.getString(R.string.appsex_man);
		String women = context.getString(R.string.appsex_women);
		if (sex.equals(man)) {
			return R.drawable.bt_check_blue;
		}
		if(sex.equals(women)){
			return R.drawable.bt_check_red;
		}
		return R.drawable.bt_check_blue;
	}
	public static int setHomeButton(Context context, String sex){
		String man = context.getString(R.string.appsex_man);
		String women = context.getString(R.string.appsex_women);
		if (sex.equals(man)) {
			return R.drawable.bt_home_blue;
		}
		if(sex.equals(women)){
			return R.drawable.bt_home_red;
		}
		return R.drawable.bt_home_blue;
	}
	public static int getTalkButton(Context context, String sex){
		String man = context.getString(R.string.appsex_man);
		String women = context.getString(R.string.appsex_women);
		if (sex.equals(man)) {
			return R.drawable.bt_talk_blue;
		}
		if(sex.equals(women)){
			return R.drawable.bt_talk_red;
		}
		return R.drawable.bt_mine_blue;
	}
	
	public static int setMineButton(Context context, String sex){
		String man = context.getString(R.string.appsex_man);
		String women = context.getString(R.string.appsex_women);
		if (sex.equals(man)) {
			return R.drawable.bt_mine_blue;
		}
		if(sex.equals(women)){
			return R.drawable.bt_mine_red;
		}
		return R.drawable.bt_mine_blue;
	}
	public static int setButtonNotify(Context context, String sex){
		String man = context.getString(R.string.appsex_man);
		String women = context.getString(R.string.appsex_women);
		if (sex.equals(man)) {
			return R.drawable.icon_notify_man;
		}
		if(sex.equals(women)){
			return R.drawable.icon_notify_woman;
		}
		return R.drawable.icon_notify_man;
	}
	public static int setButtonReport(Context context, String sex){
		String man = context.getString(R.string.appsex_man);
		String women = context.getString(R.string.appsex_women);
		if (sex.equals(man)) {
			return R.drawable.icon_report_man;
		}
		if(sex.equals(women)){
			return R.drawable.icon_report_woman;
		}
		return R.drawable.icon_report_man;
	}
	public static int setButtonSetting(Context context, String sex){
		String man = context.getString(R.string.appsex_man);
		String women = context.getString(R.string.appsex_women);
		if (sex.equals(man)) {
			return R.drawable.icon_setting_man;
		}
		if(sex.equals(women)){
			return R.drawable.icon_setting_woman;
		}
		return R.drawable.icon_setting_man;
	}
	public static int setIconPerson(Context context, String sex){
		String man = context.getString(R.string.appsex_man);
		String women = context.getString(R.string.appsex_women);
		if (sex.equals(man)) {
			return R.drawable.icon_person_blue;
		}
		if(sex.equals(women)){
			return R.drawable.icon_person_red;
		}
		return R.drawable.icon_person_blue;
	}
	public static int setIconStep(Context context, String sex){
		String man = context.getString(R.string.appsex_man);
		String women = context.getString(R.string.appsex_women);
		if (sex.equals(man)) {
			return R.drawable.icon_step_blue;
		}
		if(sex.equals(women)){
			return R.drawable.icon_step_red;
		}
		return R.drawable.icon_step_blue;
	}
	public static int setIconFire(Context context, String sex){
		String man = context.getString(R.string.appsex_man);
		String women = context.getString(R.string.appsex_women);
		if (sex.equals(man)) {
			return R.drawable.icon_fire_blue;
		}
		if(sex.equals(women)){
			return R.drawable.icon_fire_red;
		}
		return R.drawable.icon_fire_blue;
	}
	public static int setSportButton(Context context, String sex){
		String man = context.getString(R.string.appsex_man);
		String women = context.getString(R.string.appsex_women);
		if (sex.equals(man)) {
			return R.drawable.icon_blue_sport;
		}
		if(sex.equals(women)){
			return R.drawable.icon_red_sport;
		}
		return R.drawable.icon_blue_sport;
	}
	public static int setNewSecret(Context context, String sex){
		String man = context.getString(R.string.appsex_man);
		String women = context.getString(R.string.appsex_women);
		if (sex.equals(man)) {
			return R.drawable.regist_pwd_blue;
		}
		if(sex.equals(women)){
			return R.drawable.regist_pwd_red;
		}
		return R.drawable.regist_pwd_blue;
	}
	public static int setShare(Context context, String sex){
		String man = context.getString(R.string.appsex_man);
		String women = context.getString(R.string.appsex_women);
		if (sex.equals(man)) {
			return R.drawable.bg_share_blue;
		}
		if(sex.equals(women)){
			return R.drawable.bg_share_red;
		}
		return R.drawable.bg_share_blue;
	}
	public static int setSmsCode(Context context, String sex){
		String man = context.getString(R.string.appsex_man);
		String women = context.getString(R.string.appsex_women);
		if (sex.equals(man)) {
			return R.drawable.sms_code_blue;
		}
		if(sex.equals(women)){
			return R.drawable.sms_code_red;
		}
		return R.drawable.sms_code_blue;
	}
	public static int setBgEdittext(Context context, String sex){
		String man = context.getString(R.string.appsex_man);
		String women = context.getString(R.string.appsex_women);
		if (sex.equals(man)) {
			return R.drawable.edittext_bg_blue;
		}
		if(sex.equals(women)){
			return R.drawable.edittext_bg_red;
		}
		return R.drawable.edittext_bg_blue;
	}
	 
	public static int getColor(Context context,String gender){
		return gender.equals("M")?0xff3D98FF:0xffff0000;
	}
}











