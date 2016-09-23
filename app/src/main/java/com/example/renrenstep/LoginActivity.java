package com.example.renrenstep;

import helper.SPHelper;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import manager.TalkAuthService;
import tools.ToastManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.google.gson.Gson;

import bean.Customer;
import bean.Customers;
import bean.Device;
import bean.DeviseData;
import bean.ServiceData;
import bean.ServiceReturnData;
import bean.ServiceStepData;
import helper.BaseAsyncTask;
import comm.CommHelper;
import helper.DatabaseHelper;
import db.DbSqlLite;
import comm.SqlLiteManager;
import constant.Cons;

import android.content.ContentValues;
import android.os.Bundle;

public class LoginActivity extends MyBaseActivity {
    private SqlLiteManager stepManger;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        stepManger = new SqlLiteManager(new DbSqlLite(LoginActivity.this, new DatabaseHelper(LoginActivity.this)));
    }

    void checkLoginMsg(String usernm, String password) {
        final String uid = usernm;
        final String pwd = password;
        Map<String, Object> maps = new HashMap<String, Object>();
        maps.put("uid", uid);
        maps.put("pwd", pwd);
        new BaseAsyncTask(Cons.LOGIN_URL, maps, BaseAsyncTask.HttpType.Get, "", LoginActivity.this) {
            @Override
            public void handler(String param) {
                if (param == null || !param.contains("status")) {
                    login_Exception();
                    return;
                }
                try {
                    final JSONObject pjsonObject = new JSONObject(param);
                    int status = pjsonObject.getInt("status");
                    if (status == 0) {
                        String mtoken = pjsonObject.getString("mtoken");
                        SPHelper.setBaseMsg(LoginActivity.this, "mtoken", mtoken);
                        String mid = pjsonObject.getString("mid");
                        SPHelper.setBaseMsg(LoginActivity.this, "mid", mid);
                        SPHelper.setBaseMsg(LoginActivity.this, "uid", uid);
                        SPHelper.setBaseMsg(LoginActivity.this, "pwd", pwd);
                        SPHelper.setDetailMsg(LoginActivity.this, "ischecked", "true");
                        down_mem_msg();
                    } else {
                        login_Fail();
                    }
                } catch (JSONException e) {
                    login_Exception();
                }
            }
        }.execute("");
    }

    void initSetMemData(String key, String val, boolean isStr) {
        if (!val.equals("null")) {
            if (isStr) {
                SPHelper.setDetailMsg(LoginActivity.this, key, val);
            } else {
                SPHelper.setDetailMsg(LoginActivity.this, key, (int) Double.parseDouble(val));
            }
        }
    }

    void down_mem_msg() {
        new BaseAsyncTask(Cons.GET_INFO, new HashMap<String, Object>(), BaseAsyncTask.HttpType.Get, "", LoginActivity.this) {
            @Override
            public void handler(String param) {
                try {
                    JSONObject jsonObject = new JSONObject(param);
                    int status = jsonObject.getInt("status");
                    if (status == 0) {
                        String gender = jsonObject.getString("gender");
                        String age = jsonObject.getString("age");
                        String height = jsonObject.getString("height");
                        String weight = jsonObject.getString("weight");
                        String mobile = jsonObject.getString("mobile_no");
                        String nc = jsonObject.getString("nc");
                        String email = jsonObject.getString("email");
                        String notifynum = jsonObject.getString("notifynum");
                        initSetMemData("age", age, false);
                        initSetMemData("height", height, false);
                        initSetMemData("weight", weight, false);
                        initSetMemData("nc", nc, true);
                        initSetMemData("email", email, true);
                        initSetMemData(Cons.APP_SEX, gender, true);
                        initSetMemData("mobile", mobile, true);
                        initSetMemData("newnotifynum", notifynum, false);
                        checkUserMsgChange("nc", nc, CommHelper.getCompleteStr(CommHelper.changeMemMsg));
                        initDisease(jsonObject.getString("dis"));
                        loadConversationData();
                    } else {
                        login_Success();
                    }
                } catch (JSONException e) {
                    login_Success();
                }
            }
        }.execute("");
    }

    void getNewFriend(final IDownPicHandler handler) {
        Map<String, Object> maps = new HashMap<String, Object>();
        new BaseAsyncTask(Cons.APPLYLIST, maps, BaseAsyncTask.HttpType.Get, "", LoginActivity.this) {
            @Override
            public void handler(String param) {
                JSONObject jsonobj;
                if (param == null || param.equals("") || !param.contains("status")) {
                    login_Success();
                    return;
                }
                try {
                    jsonobj = new JSONObject(param);
                    if (jsonobj.getInt("status") == 0) {
                        Customers customer = new Customers();
                        Gson gson = new Gson();
                        customer = gson.fromJson(param, customer.getClass());
                        insertDb(customer.getItems(), "new", handler);
                    } else {
                        login_Success();
                    }
                } catch (Exception ex) {
                    login_Success();
                }
            }
        }.execute("");
    }

    void loadConversationData() {
        getMailList(new IDownPicHandler() {
            @Override
            public void handler() {
                getNewFriend(new IDownPicHandler() {
                    @Override
                    public void handler() {
                        initWkLogion(new TalkAuthService.ILoginCallbak() {
                            @Override
                            public void logionsuccess() {
                                //startTalkingService();
                                checkDevice();
                            }

                            @Override
                            public void logionloser() {
                                login_Success();
                            }
                        });
                    }

                    @Override
                    public void failed() {
                        login_Success();
                    }
                });
            }

            @Override
            public void failed() {
                login_Success();
            }
        });
    }

    List<Device> getDevice(String res) {
        DeviseData devisedata = new DeviseData();
        Gson json = new Gson();
        devisedata = json.fromJson(res, devisedata.getClass());
        List<Device> mydevices = devisedata.getData();
        return mydevices;
    }

    Device getphoneDevice(List<Device> dlist, String device_cd) {
        for (Device item : dlist) {
            if (item.getDevicetype_cd().equals(device_cd)) {
                return item;
            }
        }
        return null;
    }

    private void checkDevice() {
        new BaseAsyncTask(Cons.GET_DEVICES, new HashMap<String, Object>(),
                BaseAsyncTask.HttpType.Get, "", LoginActivity.this) {
            @Override
            public void handler(String param) {
                try {
                    if (param == null || !param.contains("status")) {
                        ToastManager.show(LoginActivity.this, getResources().getString(R.string.check_dev_error), 2000);
                        return;
                    }
                    JSONObject jsonObject = new JSONObject(param);
                    int status = jsonObject.getInt("status");
                    if (status != 0) {
                        ToastManager.show(LoginActivity.this, getResources().getString(R.string.check_dev_error), 2000);
                        return;
                    }
                    List<Device> mlist = getDevice(param);
                    Device pitem = getphoneDevice(mlist, "3");
                    Device devitem = getphoneDevice(mlist, "1");
                    String devicekey = CommHelper.getAndroidSerial(LoginActivity.this) + SPHelper.getBaseMsg(LoginActivity.this, "mid", "");
                    if ((pitem != null && !devicekey.equals(pitem.getDevice_no())) || (devitem == null && pitem == null)) {
                        autoBindDevise(devicekey, "3");
                    } else {
                        Device minitem = pitem != null ? pitem : devitem != null ? devitem : null;
                        if (minitem != null) {
                            saveDev(minitem.getDevicetype_cd(), minitem.getDevice_no());
                        }
                        if (minitem != null && !minitem.getDevicetype_cd().equals("3")) {
                            down_data();
                        } else {
                            login_Success();
                        }
                    }
                } catch (JSONException e) {
                    ToastManager.show(LoginActivity.this, getResources().getString(R.string.check_dev_error), 2000);
                }
            }
        }.execute("");
    }

    void autoBindDevise(String code, String typ) {
        final String _code = code;
        final String _typ = typ;
        Map<String, Object> maps = new HashMap<String, Object>();
        maps.put("device_no", code);
        maps.put("devicetype_cd", typ);
        new BaseAsyncTask(Cons.BIND_DEVICE, maps, BaseAsyncTask.HttpType.Post, "",
                LoginActivity.this) {
            @Override
            public void handler(String param) {
                try {
                    if (param == null) {
                        ToastManager.show(LoginActivity.this, getResources().getString(R.string.bind_dev_error), 2000);
                        return;
                    }
                    JSONObject jsonObject = new JSONObject(param);
                    int status = jsonObject.getInt("status");
                    if (status == 0) {
                        saveDev(_typ, _code);
                    } else {
                        ToastManager.show(LoginActivity.this, getResources().getString(R.string.bind_dev_error), 2000);
                    }
                    login_Success();
                } catch (Exception ex) {
//					ToastManager.show(LoginActivity.this, getResources().getString(R.string.bind_dev_error), 2000);
                    login_Success();
                }
            }
        }.execute("");
    }

    String getDate() {
        GregorianCalendar gc = new GregorianCalendar();
        //gc.add(5, -7);
        return gc.get(Calendar.YEAR) + "-" + (gc.get(Calendar.MONTH) + 1) + "-" + gc.get(Calendar.DAY_OF_MONTH);
    }

    void storeData(List<ServiceData> pdatas) {
        for (ServiceData item : pdatas) {
            List<ServiceStepData> stepdatas = item.getSmds();
            for (ServiceStepData pitem : stepdatas) {
                insertServiceStepData(pitem);
            }
        }
    }

    void insertServiceStepData(ServiceStepData pdata) {
        Date date = new Date(pdata.getCollect_time() * 1000);
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        int myear = cal.get(Calendar.YEAR);
        int mth = cal.get(Calendar.MONTH) + 1;
        int mday = cal.get(Calendar.DAY_OF_MONTH);
        int mhour = cal.get(Calendar.HOUR_OF_DAY);
        int mins = cal.get(Calendar.MINUTE);
        stepManger.Insert(myear, mth, mday, mhour, mins, pdata.getSteps(), pdata.getCollect_time() * 1000, SPHelper.getBaseMsg(LoginActivity.this, "mid", ""));
    }

    void down_data() {
        try {
            String storeLastDay = SPHelper.getDetailMsg(LoginActivity.this, "downday", "");
            String formart_date = storeLastDay.equals("") ? getDate() : storeLastDay;
            final SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
            final long pointer = format.parse(formart_date).getTime();
            Map<String, Object> maps = new HashMap<String, Object>();
            maps.put("time", formart_date);
            new BaseAsyncTask(Cons.DOWN_DATA, maps, BaseAsyncTask.HttpType.Get, "",
                    LoginActivity.this) {
                @Override
                public void handler(String param) {
                    if (param != null && param.contains("status")) {
                        ServiceReturnData data = new ServiceReturnData();
                        Gson json = new Gson();
                        data = json.fromJson(param, data.getClass());
                        stepManger.DelStepHisData(pointer);
                        storeData(data.getData());
                        SPHelper.setDetailMsg(LoginActivity.this, "downday", format.format(new Date()));
                        long storeval = CommHelper.getMaxValue(data.getData());
                        SPHelper.getDetailMsg(LoginActivity.this, "lastval", storeval);
                    }
                    login_Success();
                }
            }.execute("");
        } catch (Exception ex) {

        }
    }

    void initDisease(String arg0) {
        JSONArray json;
        try {
            json = new JSONArray(arg0);
            SPHelper.setDetailMsg(LoginActivity.this, "GXB", json.getString(0));
            SPHelper.setDetailMsg(LoginActivity.this, "TNB", json.getString(1));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    void saveDev(String typcd, String scode) {
        SPHelper.setBaseMsg(LoginActivity.this, "bindkey", typcd);
        SPHelper.setBaseMsg(LoginActivity.this, "bindval", scode);
    }

    void getMailList(final IDownPicHandler handler) {
        Map<String, Object> maps = new HashMap<String, Object>();
        new BaseAsyncTask(Cons.APPMAILLIST, maps, BaseAsyncTask.HttpType.Get, "", LoginActivity.this) {
            @Override
            public void handler(String param) {
                // TODO Auto-generated method stub
                JSONObject jsonobj;
                if (param == null || param.equals("") || !param.contains("status")) {
                    ToastManager.show(LoginActivity.this, getResources().getString(R.string.wangluoyic), 2000);
                    login_Success();
                    return;
                }
                try {
                    jsonobj = new JSONObject(param);
                    if (jsonobj.getInt("status") == 0) {
                        Customers customer = new Customers();
                        Gson gson = new Gson();
                        customer = gson.fromJson(param, customer.getClass());
                        insertDb(customer.getItems(), "old", handler);
                    } else {
                        login_Success();
                    }
                } catch (Exception ex) {
                    login_Success();
                }
            }
        }.execute("");
    }

    void insertDb(List<Customer> plist, String typ, IDownPicHandler handler) {
        String tomid = SPHelper.getBaseMsg(LoginActivity.this, "mid", "0");
        db.open();
        db.delete("apm_sys_friend", " typ=? and idcd=? ", new String[]{typ, tomid});
        List<String> mlist = new ArrayList<String>();
        ContentValues contents = new ContentValues();
        for (Customer item : plist) {
            mlist.add(item.getAvatar());
            contents.clear();
            contents.put("mid", item.getId());
            contents.put("nc", filterStr(item.getNc()));
            contents.put("email", filterStr(item.getEmail()));
            contents.put("address", filterStr(item.getCity_alia()));
            contents.put("acvtor", filterStr(item.getAvatar()));
            contents.put("state", filterStr(item.getState()));
            contents.put("words", filterStr(item.getWords()));//remark,idcd,typ
            contents.put("remark", filterStr(item.getRemark()));
            contents.put("idcd", tomid);
            contents.put("typ", typ);
            db.insert("apm_sys_friend", contents);
        }
        db.close();
        handler.handler();
    }

    protected void login_Success() {
    }

    protected void login_Fail() {
    }

    protected void login_Exception() {
    }
}
