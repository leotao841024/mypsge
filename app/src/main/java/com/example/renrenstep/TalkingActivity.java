package com.example.renrenstep;

import android.app.Dialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.v4.widget.SwipeRefreshLayout;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.alibaba.wukong.im.Message;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.text.MessageFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import Interface.Functional.Action;
import Interface.Functional.IMCallback;
import adapter.GroupMemAdapter;
import adapter.MailAdapter;
import adapter.OptionsAdapter;
import adapter.TalkingAdapter;
import bean.Customer;
import bean.DayStep;
import bean.Food;
import bean.FoodCal;
import bean.MemberInfo;
import bean.MessageInfo;
import bean.MinuteStep;
import bean.OptionsItem;
import bean.SessionInfo;
import bean.StepHisData;
import bean.Talked;
import comm.CommHelper;
import manager.ConversationHandler;
import manager.ConversationServiceHandler;
import db.DBbase;
import helper.DatabaseHelper;
import db.DbSqlLite;
import comm.FoodXmlHandler;
import comm.GroupBitmap;
import manager.SlidingMenuManager;
import constant.Cons;
import groupview.GenderInfiViewBuilder;
import groupview.InfoViewBuilder;
import helper.BGHelper;
import helper.ResourceHelper;
import helper.SPHelper;
import manager.TalkAuthService;
import tools.ToastManager;
import tools.BitmapUtil;
import tools.FileUtils;
import tools.ImageTool;
import tools.PhotoTool;
import tools.StepTool;
import view.CircleBar;
import view.Emojicon;
import view.EmojiconEditText;
import view.EmojiconGridFragment;
import view.EmojiconsFragment;
import view.HomeColumnar;
import view.IconBar;

public class TalkingActivity extends MyBaseActivity implements OnClickListener, OnItemClickListener,OnScrollListener, EmojiconGridFragment.OnEmojiconClickedListener, EmojiconsFragment.OnEmojiconBackspaceClickedListener {
    private static final int ALBUM_RQUEST = 100;
    private static final int MAP_REQUEST = 101;
    private ListView mlist;
    private List<Talked> mtalks;
    private Button btn_send, loadMoreButton, btn_plus, btn_photo, btn_map, btn_emoji, btn_today_steps, btn_hist_steps;
    private TalkingAdapter adapter;
    private SessionInfo conversation;
    private EditText edit_groupnm;
    private ConversationServiceHandler conversationHandler;
    private String conversationId = "", gender = "";
    private int lastindex1;
    private View loadMoreView, talksettinView, mailView,view_today_steps;//
    private Handler handler = new Handler();
    private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    private GridView grid_groupmem;
    private ImageView img_settop,middlenm;
    private GroupMemAdapter groupAdapter;
    private SlidingMenu rightMenu, groupMenu;
    private List<Customer> maillist;//通讯录内的用户
    private MailAdapter mailAdapter;
    private List<Customer> talkmans = new ArrayList<Customer>();//当前会话内的用户
    private String userid;
    private ListView mailist;
    private TextView txt_groupnm, title,tv_distance,tv_cal,firstnm,lastnm;
    private Dialog groupnm_dialog, loading_dialog;
    private Bitmap bmp = null;
    private LinearLayout ll_options;
    private FrameLayout emojicons;
    private EmojiconEditText content;
    private InputMethodManager inputMethodManager;
    private FileUtils fileUtils;
    private CircleBar progressBar;
    IconBar cb_person;
    private DBbase base;
    private List<StepHisData> hisdatas;
    private DatabaseHelper database;
    private int pwidth;
    private List<MinuteStep> mSteplist,mflist;
    RelativeLayout linear;
    private GridView gv_options;
    private OptionsAdapter optionsAdapter;
    private List<OptionsItem> data;
    private SwipeRefreshLayout refreshLayout;
    private int pageIndex = 1;
    private Message msg;
    private TextView tv_hist;
    private LinearLayout linear_line;
    private String photo_filenm;


    //消息接收到后处理单聊
    boolean isSysMsg(String porder) {
        if (porder.equals("")) {
            return false;
        }
        for (String item : CommHelper.systemRemindStr) {
            if (item.equals(porder)) {
                return true;
            }
        }
        return false;
    }


    @Override
    public void receiveMsgHander(Intent intent) {
        conversation.getConversation().resetUnreadCount();
        String key = intent.getStringExtra("typ");
        String converid = intent.getStringExtra("mid");
        if (key.equals(CommHelper.UPDATEMEMMSG) && conversationId.equals(converid)) {
            handlerConversation();
        }else if(converid.equals(conversationId)) {
            loadHisData(true, 10);
        }
    }

    //初始化页面
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        setContentView(R.layout.activity_talking);
        autoLogion();
        initView();
        initData();
        initEvent();
        initColor();
    }


    void initView() {
        ll_options = (LinearLayout) findViewById(R.id.ll_options);
        emojicons = (FrameLayout) findViewById(R.id.emojicons);
        btn_emoji = (Button) findViewById(R.id.btn_emoji);
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.emojicons, EmojiconsFragment.newInstance(false))
                .commit();


        mlist = (ListView) findViewById(R.id.mlist);


        btn_send = (Button) findViewById(R.id.btn_send);
        btn_plus = (Button) findViewById(R.id.btn_plus);
       // btn_map = (Button) findViewById(R.id.btn_map);
      //  btn_photo = (Button) findViewById(R.id.btn_photo);
      //  btn_today_steps = (Button) findViewById(R.id.btn_today_steps);
       // btn_hist_steps = (Button) findViewById(R.id.btn_hist_steps);
        content = (EmojiconEditText) findViewById(R.id.content);
        view_today_steps = findViewById(R.id.view_today_steps);
        progressBar = (CircleBar)findViewById(R.id.cb_progress);
        progressBar.setMaxstepnumber(10000);
        cb_person = (IconBar) findViewById(R.id.cb_person);
        Typeface mFace = Typeface.createFromAsset(this.getAssets(), "HELVETICANEUELTPRO-CN.OTF");
        tv_distance = (TextView) findViewById(R.id.tv_distance);
        tv_distance.setTypeface(mFace);
        tv_cal = (TextView) findViewById(R.id.tv_cal);
        tv_cal.setTypeface(mFace);
        firstnm = (TextView) findViewById(R.id.firstnm);
        middlenm = (ImageView) findViewById(R.id.middlenm);
        lastnm = (TextView) findViewById(R.id.lastnm);

        linear = (RelativeLayout) findViewById(R.id.linear);
        linear_line = (LinearLayout) findViewById(R.id.linear_line);
        tv_hist = (TextView) findViewById(R.id.tv_hist);

        gv_options = (GridView) findViewById(R.id.gv_options);

        refreshLayout = (SwipeRefreshLayout) findViewById(R.id.refrshlayout);

        database=new DatabaseHelper(this);
        String mid=SPHelper.getBaseMsg(this, "mid","");
        base=new DbSqlLite(this,database);

        content.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                boolean bool = inputMethodManager.showSoftInput(view, InputMethodManager.SHOW_FORCED);
                if (bool) {
                    emojicons.setVisibility(View.GONE);
                    ll_options.setVisibility(View.GONE);
                }
                return false;
            }
        });

        content.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (content.getText().toString().length() == 0) {
                    btn_plus.setVisibility(View.VISIBLE);
                    btn_send.setVisibility(View.GONE);
                } else {
                    btn_plus.setVisibility(View.GONE);
                    btn_send.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        title = ((TextView) findViewById(R.id.title));
        WindowManager wm = this.getWindowManager();
        loadMoreView = getLayoutInflater().inflate(R.layout.load_more, null);
        loadMoreButton = (Button) loadMoreView.findViewById(R.id.loadMoreButton);
        mlist.setOnScrollListener(this);
        initGroupView();
        initMailView();
        initGroupNmDialogView();
    }


    void initGroupView() {
        talksettinView = LayoutInflater.from(this).inflate(R.layout.group_sliding_menu, null);
        grid_groupmem = (GridView) talksettinView.findViewById(R.id.grid_groupmem);
        img_settop = (ImageView) talksettinView.findViewById(R.id.img_settop);
        txt_groupnm = (TextView) talksettinView.findViewById(R.id.txt_groupnm);
    }

    void initGroupNmDialogView() {
        View groupdialogview = LayoutInflater.from(this).inflate(R.layout.change_groupnm_pg, null);
        edit_groupnm = (EditText) groupdialogview.findViewById(R.id.edit_groupnm);
        ((TextView) groupdialogview.findViewById(R.id.dialog_confirm)).setOnClickListener(this);
        ((TextView) groupdialogview.findViewById(R.id.dialog_cancel)).setOnClickListener(this);
        groupnm_dialog = getDialog(groupdialogview);
    }

    void initMailView() {
        mailView = LayoutInflater.from(this).inflate(R.layout.group_mem_select, null);
        mailist = ((ListView) mailView.findViewById(R.id.mlist));
    }

    void initData() {
        fileUtils = new FileUtils("stepic");
        conversationHandler = new ConversationServiceHandler();
        userid = SPHelper.getBaseMsg(TalkingActivity.this, "mid", "0");
        mtalks = new ArrayList<Talked>();
        Intent intent = this.getIntent();
        conversationId = intent.getStringExtra("converid");
        adapter = new TalkingAdapter(this);
        adapter.setSource(mtalks);
        mlist.setAdapter(adapter);
        rightMenu = SlidingMenuManager.getFullRightSlidingMenu(talksettinView, this);
        initGroupData();
        initMailData();
        initPlusOptionData();
        String talkingtitle = intent.getStringExtra("title");
        title.setText(talkingtitle);
        showData((DayStep) getObject("object.dat"));
        showCol();
    }

    private void initPlusOptionData() {
        OptionsItem picItem = new OptionsItem("照片", R.drawable.plus_photo);
        OptionsItem posItem = new OptionsItem("位置", R.drawable.plus_location);
        OptionsItem stepItem = new OptionsItem("今日步数", R.drawable.plus_steps);
        OptionsItem histItem = new OptionsItem("历史步数", R.drawable.plus_hist);
        data = new ArrayList<OptionsItem>();
        data.add(picItem);
        data.add(posItem);
        data.add(stepItem);
        data.add(histItem);
        optionsAdapter = new OptionsAdapter(this,data);
        gv_options.setAdapter(optionsAdapter);
    }

    void initGroupTitleView() {
        String groupnm = CommHelper.getGroupConversationTitle(conversation.getTitle(), conversation.getTotalmember());
        title.setText(groupnm);
        edit_groupnm.setText(conversation.getTitle().equals("") ? "" : groupnm);
        txt_groupnm.setText(conversation.getTitle().equals("") ? getResources().getString(R.string.no_name) : groupnm);
    }

    void initGroupData() {
        groupAdapter = new GroupMemAdapter(this, new GroupOpration());
        groupAdapter.setSource(talkmans);
        groupAdapter.setRemoveOpration(false);
        groupAdapter.setUserid(userid);
        grid_groupmem.setAdapter(groupAdapter);
    }

    String getDescFromTalkingMan(String[] parr, List<Customer> plist) {
        StringBuilder sb = new StringBuilder();
        for (String item : parr) {
            for (Customer pitem : plist) {
                if (item.equals(pitem.getId() + "")) {
                    sb.append(pitem.getNc() + " ");
                }
            }
        }
        return sb.toString();
    }

    @Override
    public void onEmojiconBackspaceClicked(View v) {
        EmojiconsFragment.backspace(content);
    }

    @Override
    public void onEmojiconClicked(Emojicon emojicon) {
        EmojiconsFragment.input(content, emojicon);
    }

    class GroupOpration implements GroupMemAdapter.IGroupOpration {
        @Override
        public void remove(int userid) {
            Long[] arr = new Long[]{(long) userid};
            removeGroupMem(CommHelper.getCompleteStr(CommHelper.removeMemGroup) + getDescFromTalkingMan(new String[]{userid + ""}, talkmans) + ResourceHelper.getStringValue(R.string.kitout), arr);
        }

        @Override
        public void beginOpration() {
            groupAdapter.setRemoveOpration(true);
            groupAdapter.notifyDataSetChanged();
        }
    }

    void initMailData() {
        maillist = new ArrayList<Customer>();
        mailAdapter = new MailAdapter(maillist, TalkingActivity.this);
        mailist.setAdapter(mailAdapter);
        groupMenu = SlidingMenuManager.getFullRightSlidingMenu(mailView, this);
        getMailList();
    }

    //开始获取会话内容
    void initEvent() {
        btn_send.setOnClickListener(this);
        ((LinearLayout) findViewById(R.id.covert_left_dao)).setOnClickListener(this);

        btn_plus.setOnClickListener(this);

        mlist.setOnScrollListener(this);
        ((LinearLayout) findViewById(R.id.covert_right_dao)).setOnClickListener(this);
        initGroupEvent();
        initMailEvent();
        if (!conversationId.equals("")) {
            getConversation();
        }
        btn_emoji.setOnClickListener(this);
        gv_options.setOnItemClickListener(this);

        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {

            @Override
            public void onRefresh() {
                if (mtalks.size() == 0) refreshLayout.setRefreshing(false);
                else LoadPrevious(true, mtalks.get(0).getMessage(), 10);
            }
        });
        registBrocaster();
    }

    void topImageDescEvent() {
        long toplevel = conversation.getToplevel();
        int img_resourceid = toplevel > 0 ? gender.equals("M") ? R.drawable.toggle_blue : R.drawable.toggle_red : R.drawable.toggle_normal;
        img_settop.setImageDrawable(getResources().getDrawable(img_resourceid));
    }

    //开始获取会话内容
    void getConversation() {

        conversationHandler.getSessionInfoById(conversationId, new IMCallback<SessionInfo, Integer, String>() {
            @Override
            public void success(SessionInfo arg0) {
                conversation = arg0;
                adapter.setConversationTyp(conversation.getTyp());
                resumeSettingView(arg0.getTyp());
                handlerConversation();
            }

            @Override
            public void process(Integer arg0) {
            }


            @Override
            public void fail(String arg0) {
            }
        });
    }

    //加载时根据会话的不同获取不同人的信息
    void handlerConversation() {
        if (conversation.getTyp() == ConversationHandler.SINGLE) {//单聊
            long peerid = conversation.getPeerid();
            initConversationMem(peerid + "", "old", userid);
            singleAddCurrentToConversationMem();
            loadHisData(true,10);
            addTalkMan();
            updatePageListnerAndMailData();
        } else if (conversation.getTyp() == ConversationHandler.GROUP) {//群聊
            getListMember();
        } else {
            ToastManager.show(TalkingActivity.this, getResources().getString(R.string.wangluoyic), 2000);
        }
        topImageDescEvent();
    }

    void singleAddCurrentToConversationMem() {
        Customer cus = new Customer();
        cus.setAvatar(getCurrentAvator());
        cus.setNc(SPHelper.getDetailMsg(this, "nc", ""));
        cus.setId(Integer.parseInt(SPHelper.getBaseMsg(this, "mid", "0")));
        talkmans.add(cus);
    }

    String getCurrentAvator() {
        String uri = SPHelper.getDetailMsg(this, "uri", "");
        String[] arr = uri.split("/");
        if (arr.length > 0) {
            return arr[arr.length - 1];
        }
        return "";
    }

    void resumeSettingView(int convertyp) {
        switch (convertyp) {
            case 2:
                ((RelativeLayout) talksettinView.findViewById(R.id.layout_group)).setVisibility(View.VISIBLE);
                break;
        }
    }

    //检查数据库中用户信息（通讯录的、组的）
    void initConversationMem(String param0, String typ, String userid) {
        talkmans.clear();
        db.open();
        Cursor cursor = getConversationMemMsg(param0, typ, userid);
        while (cursor.moveToNext()) {
            int id = cursor.getInt(cursor.getColumnIndex("mid"));
            if (!hasInTalkMan(id)) {
                Customer cus = new Customer();
                cus.setAvatar(cursor.getString(cursor.getColumnIndex("acvtor")));
                cus.setNc(cursor.getString(cursor.getColumnIndex("nc")));
                cus.setId(id);
                talkmans.add(cus);
            }
        }
        cursor.close();
        db.close();
    }

    Cursor getConversationMemMsg(String param0, String typ, String userid) {
        String sql = "select * from apm_sys_friend where 1=1";
        if (!param0.equals("")) {
            sql += " and mid in(" + param0 + ") ";
        }
        if (!userid.equals("")) {
            sql += " and idcd='" + userid + "'";
        }
        if (!typ.equals("")) {
            sql += " and typ='" + typ + "'";
        }
        Cursor cur = db.query(sql);
        return cur;
    }

    boolean hasInTalkMan(int id) {
        for (Customer item : talkmans) {
            if (item.getId() == id) {
                return true;
            }
        }
        return false;
    }

    //添加成员页面的增加按钮
    void addTalkMan() {
        Customer othercus = new Customer();
        //String res=conversation.getParentId();
        othercus.setNc("");
        othercus.setId(0);
        othercus.setIsgroupmem(false);
        talkmans.add(othercus);
    }

    //添加成员页面的删除按钮
    void removeTalkMan() {
        Customer othercus = new Customer();
        othercus.setNc("");
        othercus.setId(-1);
        othercus.setIsgroupmem(false);
        talkmans.add(othercus);
    }

    //根据用户id获取用户信息
    String getMemIdListStr(List<Long> param) {
        StringBuilder sb = new StringBuilder();
        for (long item : param) {
            sb.append(item + ",");
        }
        return sb.toString().substring(0, sb.length() - 1);
    }

    //消息监听注册以及群通讯录的列表更新
    void updatePageListnerAndMailData() {
        mailAdapter.setCompareList(talkmans);
        mailAdapter.notifyDataSetChanged();
        if (userid.equals(conversation.getIcon())) {
            removeTalkMan();
        }
    }

    //获取群聊会员信息
    void getListMember() {
        int offset = 0;
        int count = 100;
        conversationHandler.getMembers(conversationId, offset, count, new Action<List<MemberInfo>>() {
            @Override
            public void handler(List<MemberInfo> t) {
                List<Long> groupMemIds = new ArrayList<Long>();
                for (MemberInfo item : t) {
                    groupMemIds.add(item.getOpenId());
                }
                initConversationMem(getMemIdListStr(groupMemIds), "group", "");
                loadHisData(true,10);
                addTalkMan();
                GroupBitmap.createGroupBitmap(conversationId, groupMemIds);
                updatePageListnerAndMailData();
                initGroupTitleView();
                List<Long> chaMemIds = getChaMemId(groupMemIds, getDbHasMem(getMemIdStr(groupMemIds)));
                for (long item : chaMemIds) {
                    getOneFriend(item + "", "group", new IDownPicHandler() {
                        @Override
                        public void handler() {
                        }

                        @Override
                        public void failed() {

                        }
                    });
                }

            }
        });
    }

    List<Long> getChaMemId(List<Long> all, List<Long> tolist) {
        List<Long> cha = new ArrayList<Long>();
        for (long item : all) {
            if (!tolist.contains(item)) {
                cha.add(item);
            }
        }
        return cha;
    }

    String getMemIdStr(List<Long> plist) {
        StringBuilder sb = new StringBuilder();
        for (long item : plist) {
            sb.append(item + ",");
        }
        return sb.substring(0, sb.length() - 1);
    }

    List<Long> getDbHasMem(String param) {
        db.open();
        List<Long> mlist = new ArrayList<Long>();
        String sql = "select mid from apm_sys_friend where mid in(" + param + ")  and typ='group'";//and idcd="+userid+"
        Cursor cursor = db.query(sql);
        while (cursor.moveToNext()) {
            mlist.add(cursor.getLong(cursor.getColumnIndex("mid")));
        }
        cursor.close();
        db.close();
        return mlist;
    }

    //自动登录阿里悟空
    void autoLogion() {
        initWkLogion(new TalkAuthService.ILoginCallbak() {
            @Override
            public void logionsuccess() {
                // TODO Auto-generated method stub
            }

            @Override
            public void logionloser() {
                // TODO Auto-generated method stub
                ToastManager.show(TalkingActivity.this, getResources().getString(R.string.wangluoyic), 2000);
            }
        });
    }

    //加载历史数据

  /*  void loadHisData(final boolean isfirstload) {
        mtalks.clear();
        conversation.getConversation().listPreviousMessages(null, 10, new Action<List<MessageInfo>>() {*/

    void loadHisData(final boolean isfirstload,int pageSize) {
        mtalks.clear();
        conversation.getConversation().listPreviousMessages(null, pageSize, new Action<List<MessageInfo>>() {
            @Override
            public void handler(List<MessageInfo> t) {
                firstloadData(t);
            }
        });
        conversation.getConversation().resetUnreadCount();
    }

    void firstloadData(List<MessageInfo> data) {
        addMessages(data);
        adapter.notifyDataSetChanged();
        mlist.setSelection(mtalks.size() - 1);
    }

    void addMessages(List<MessageInfo> plist) {

        for (MessageInfo item : plist) {
            Talked talkd = getMessage(item);
            if (talkd != null) {
                mtalks.add(talkd);
            }
        }
    }

    Talked getMessage(MessageInfo pitem) {
        String msgcont = pitem.getContent();
        String order = msgcont.length() > 19 ? msgcont.substring(0, 20).trim() : msgcont;
        if (CommHelper.isCommandMsg(order)) {
            return null;
        }
        if(pitem.getMessageType().equals("txt")) {
            msgcont = msgcont.length() > 19 ? msgcont.substring(20, msgcont.length()) : msgcont;
        }
        if (msgcont.trim().equals("")) {
            return null;
        }
        long sendid = pitem.getSendId();
        long myid = Long.parseLong(userid);
        String isme = "to";
        if (isSysMsg(order)) {
            isme = "sys";
        } else if (sendid == myid) {
            isme = "me";
        }
        long timer = pitem.getTimer();
        Date date = new Date(timer);
        String timestr = sdf.format(date);
        return handlerTalkData(pitem.getSendId(), isme, msgcont, pitem.getMsgId(), timestr, pitem.getMessageType(), pitem.getConversationtype(),pitem.getMessage());
    }

    void hanlderMessage(MessageInfo item) {
        initAddPage(getMessage(item));
    }

    void initGroupEvent() {
        ((ImageView) talksettinView.findViewById(R.id.img_maillist)).setOnClickListener(this);
        ((LinearLayout) talksettinView.findViewById(R.id.lay_outer)).setOnClickListener(this);
        ((LinearLayout) talksettinView.findViewById(R.id.layout_groupnm)).setOnClickListener(this);
        ((Button) talksettinView.findViewById(R.id.btn_groupout)).setOnClickListener(this);
        img_settop.setOnClickListener(this);
        grid_groupmem.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                if (talkmans.get(arg2).getId() == 0) {
                    initMail();
                    mailAdapter.notifyDataSetChanged();
                    groupMenu.showSecondaryMenu();
                }
            }
        });
    }

    void initMail() {
        for (Customer item : maillist) {
            item.setIsgroupmem(false);
        }
    }

    void initMailEvent() {
        ((TextView) mailView.findViewById(R.id.txt_cancel)).setOnClickListener(this);
        ((TextView) mailView.findViewById(R.id.txt_confirm)).setOnClickListener(this);
        mailist.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                // TODO Auto-generated method stub
                maillist.get(arg2).setIsgroupmem(!maillist.get(arg2).isIsgroupmem());
                mailAdapter.notifyDataSetChanged();
            }
        });
    }


    void getMailList() {
        maillist.clear();
        String sql = "select * from apm_sys_friend where idcd=" + userid + " and typ='old'";
        db.open();
        Cursor cur = db.query(sql); //db.getFriend(userid, "old", "");
        while (cur.moveToNext()) {
            Customer cus = new Customer();
            cus.setAvatar(cur.getString(cur.getColumnIndex("acvtor")));
            cus.setNc(cur.getString(cur.getColumnIndex("nc")));
            cus.setId(cur.getInt(cur.getColumnIndex("mid")));
            maillist.add(cus);
        }
        cur.close();
        db.close();
    }

    void initAddPage(Talked talked) {
        if (talked != null) {
            mtalks.add(talked);
            adapter.notifyDataSetChanged();
        }
    }

    Talked handlerTalkData(long sendid, String typ, String cont, long msgid, String timer,MssageType pmsgtyp,int chattyp,Message msg) {
        Talked item = new Talked();
        item.setCont(cont);
        item.setTyp(typ);
        item.setTimer(timer);
        item.setMessageid(msgid);
        item.setIsgood(true);
        item.setPicnm(getCustomerAvator(sendid));
        item.setNc(getCustomerNm(sendid));
        item.setMsgtyp(pmsgtyp);
        item.setChatTyp(chattyp);
        item.setMessage(msg);
        return item;
    }

    String getCustomerNm(long sendid) {
        for (Customer item : talkmans) {
            if (item.getId() == sendid) {
                return item.getNc();
            }
        }
        return "";
    }

    String getCustomerAvator(long sendid) {
        for (Customer item : talkmans) {
            if (item.getId() == sendid) {
                return item.getAvatar();
            }
        }
        return "";
    }

    String getTimer() {
        Date nowTime = new Date();
        SimpleDateFormat time = new SimpleDateFormat("yyyy MM dd HH:mm:ss");
        return time.format(nowTime);
    }

    void initColor() {
        gender = SPHelper.getDetailMsg(this, Cons.APP_SEX, getString(R.string.appsex_man));
        ((RelativeLayout) findViewById(R.id.layouter)).setBackgroundResource(BGHelper.setBackground(this, gender));
        btn_send.setBackgroundResource(gender.equals("M") ? R.drawable.send_blue_btn : R.drawable.send_red_btn);
        ((LinearLayout) talksettinView.findViewById(R.id.head_layouer)).setBackgroundResource(BGHelper.setBackground(this, gender));
        ((RelativeLayout) mailView.findViewById(R.id.head_layouer)).setBackgroundResource(BGHelper.setBackground(this, gender));
        tv_hist.setBackgroundResource(BGHelper.setBackground(this, gender));
    }

    //发送消息d
    void sendMsgToUser(String msg, MessageInfo.MssageType type) {
        conversation.getConversation().sendMsg(msg, type, new IMCallback<MessageInfo, Integer, String>() {
            @Override
            public void success(MessageInfo arg0) {
                content.setText("");
                ll_options.setVisibility(View.GONE);
            }

            @Override
            public void process(Integer arg0) {
            }

            @Override
            public void fail(String arg0) {
                loading_dialog.dismiss();
                ll_options.setVisibility(View.GONE);
            }
        });
    }

    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub
        switch (v.getId()) {
            case R.id.btn_send:
                String cont = content.getText().toString();
                if (cont.equals("")) {
                    ToastManager.show(TalkingActivity.this, getResources().getString(R.string.sendmsgisempty), 2000);
                    return;
                }
                if (conversation != null) {
                    sendMsgToUser(CommHelper.getCompleteStr(CommHelper.normalMsg) + cont, MessageInfo.MssageType.TXT);
                } else {
                    ToastManager.show(this, getResources().getString(R.string.wangluoyic), 2000);
                }
                break;
            case R.id.covert_left_dao:
                finish();
                break;
            case R.id.covert_right_dao:
                orderTalkedMan();
                initGroupData();
                rightMenu.showSecondaryMenu();

                //add swipeback function
            /*Intent intent = new Intent(getApplicationContext(), ChatMemberActivity.class);
			startActivity(intent);*/
                break;
            case R.id.lay_outer:
                if (rightMenu.isSecondaryMenuShowing()) {
                    rightMenu.toggle();
//				handlerConversation();
                    initTalking();
                }
                break;
            case R.id.img_maillist:

                break;
            case R.id.txt_cancel:
                if (groupMenu.isSecondaryMenuShowing()) {
                    groupMenu.toggle();
                }
                break;
            case R.id.txt_confirm:
                loading_dialog = CommHelper.CreateLoading(this, "", gender, false);
                loading_dialog.show();
                if (conversation.getTyp() == ConversationHandler.SINGLE) {
                    final Long[] pids = getGroupMem(new ArrayList<Customer>());
                    String[] lmids = longArrToStringArray(pids);
                    String lmsg = CommHelper.getCompleteStr(CommHelper.createGroupMsg) + getDescFromTalkingMan(lmids, maillist) + ResourceHelper.getStringValue(R.string.meminqun);
                    createGroupConversation("", lmsg, pids);
                } else if (conversation.getTyp() == ConversationHandler.GROUP) {
                    Long[] pids = getGroupMem(talkmans);
                    if (pids.length > 0) {
                        String[] lmids = longArrToStringArray(pids);
                        addGroupMemConversation(CommHelper.getCompleteStr(CommHelper.addMemGroup) + getDescFromTalkingMan(lmids, maillist) + ResourceHelper.getStringValue(R.string.meminqun), pids);
                    } else {
                        loading_dialog.dismiss();
                        ToastManager.show(this, ResourceHelper.getStringValue(R.string.chosemem), 2000);
                    }
                } else {
                    loading_dialog.dismiss();
                }
                break;
            case R.id.layout_groupnm:
                //TODO 弹出框更改群名称
                groupnm_dialog.show();
                break;
            case R.id.btn_groupout:
                //TODO 退出群
                if (!userid.equals(conversation.getIcon())) {
                    quitGroup(CommHelper.getCompleteStr(CommHelper.quitMemGroup) + SPHelper.getDetailMsg(this, "nc", "") + ResourceHelper.getStringValue(R.string.quitgroup));
                } else {
                    disableGroup();
                }
                break;
            case R.id.dialog_confirm:
                //TODO 弹出框确认按钮事件
                String newTitle = edit_groupnm.getText().toString();
                if (newTitle.equals("")) {
                    ToastManager.show(this, ResourceHelper.getStringValue(R.string.noempty), 2000);
                    return;
                }
                if (newTitle.equals(txt_groupnm.getText().toString())) {
                    groupnm_dialog.dismiss();
                    return;
                }
                updateGroupTitle(newTitle, CommHelper.getCompleteStr(CommHelper.changeGroupTitle) + MessageFormat.format(ResourceHelper.getStringValue(R.string.changegrouptitle), SPHelper.getDetailMsg(TalkingActivity.this, "nc", ""), newTitle));
                break;
            case R.id.dialog_cancel:
                groupnm_dialog.dismiss();
                break;
            case R.id.img_settop:
                long toplevel = conversation.getToplevel();
                setConversationTop(!(toplevel > 0));
                break;

            case R.id.btn_plus:

                Boolean bool = inputMethodManager.hideSoftInputFromWindow(content.getWindowToken(), 0);
                if (!bool)
                    ll_options.setVisibility(ll_options.getVisibility() == View.GONE ? View.VISIBLE : View.GONE);
                emojicons.setVisibility(View.GONE);

                break;

          /*  case R.id.btn_map:
                Intent mapIntent = new Intent(TalkingActivity.this, MapDemoActivity.class);
                startActivityForResult(mapIntent, MAP_REQUEST);
                break;
            case R.id.btn_photo:

                Intent albumIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(albumIntent, ALBUM_RQUEST);
                break;*/
            case R.id.btn_emoji:
                ll_options.setVisibility(View.GONE);
                boolean b = inputMethodManager.hideSoftInputFromWindow(content.getWindowToken(), 0);

                if (!b) {
                    emojicons.setVisibility(View.GONE == emojicons.getVisibility() ? View.VISIBLE : View.GONE);
                }

                break;

           /* case R.id.btn_today_steps:
                Bitmap bitmap = BitmapUtil.createViewBitmap(view_today_steps);
                String filename = String.valueOf(System.currentTimeMillis())+".jpeg";
                try {
                    saveFile(bitmap,filename);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                sendMsgToUser(fileUtils.getFilePath()+filename, MessageInfo.MssageType.IMAGE);
                break;
            case R.id.btn_hist_steps:
                Bitmap histBitmao = BitmapUtil.createViewBitmap(linear);
                String histfile = String.valueOf(System.currentTimeMillis())+".jpeg";
                try {
                    saveFile(histBitmao,histfile);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                sendMsgToUser(fileUtils.getFilePath()+histfile, MessageInfo.MssageType.IMAGE);
                break;*/

            default:
                break;
        }
    }
    FileUtils tools=new FileUtils("stepic1");
    public void saveFile(Bitmap bm, String fileName) throws IOException {
        String path = tools.getFilePath();
        File dirFile = new File(path);
        if(!dirFile.exists()){
            dirFile.mkdir();
        }
        File myCaptureFile = new File(path + fileName);
        BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(myCaptureFile));
        bm.compress(Bitmap.CompressFormat.JPEG, 80, bos);
        bos.flush();
        bos.close();
    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && data != null &&requestCode == PhotoTool.OPEN_PHOTOLIST) {
            //选择图片
            Uri uri = geturi(data);
            String[] filePathColumn = {MediaStore.Images.Media.DATA};
            Cursor cursor = getContentResolver().query(uri, filePathColumn, null, null, null);
            cursor.moveToFirst();
            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            String imgUrl = cursor.getString(columnIndex);
            imgUrl = filterImage(imgUrl);
            cursor.close();
            File file=new File(imgUrl);
            if(file.exists()) {
                sendMsgToUser(imgUrl, MessageInfo.MssageType.IMAGE);
            }else{
                ToastManager.show(TalkingActivity.this,"不存在",3000);
            }
        }else if(resultCode == RESULT_OK && data != null &&requestCode == MAP_REQUEST){
            String  url = data.getStringExtra("map_snapshot_path");
            if (url != null){
                url= filterImage(url);
                File file=new File(url);
                if(file.exists()) {
                    sendMsgToUser(url, MessageInfo.MssageType.IMAGE);
                }else{
                    ToastManager.show(TalkingActivity.this,"不存在",3000);
                }
            }
        }else if(resultCode == RESULT_OK&& requestCode==PhotoTool.TAKE_PHOTO){
            if(data !=null){
                if(data.hasExtra("data")){
                    Bitmap thumbnail = data.getParcelableExtra("data");
                    Date date=new Date();
                    String filenm= String.valueOf(date.getTime())+".jpg";
                    FileUtils filetool=new FileUtils("stepic");
                    filetool.saveMyBitmap(filenm, thumbnail);
                    sendMsgToUser(filetool.getFilePath()+ filenm, MessageInfo.MssageType.IMAGE);
                }
            }else {
                String filename = fileUtils.getFilePath()+photo_filenm;
                String path =  filterImage(filename);
                sendMsgToUser(path, MessageInfo.MssageType.IMAGE);
            }

            }

        }



    String filterImage(String path){
        Date date=new Date();
        int angle =ImageTool.getBitmapDegree(path);
        String filenm= String.valueOf(date.getTime())+".jpg";
        Bitmap bitmap= ImageTool.getSmallBitmap(path,480,800);
        if(angle!=0){bitmap = ImageTool.changeImage(bitmap,angle);}
        FileUtils filetool=new FileUtils("stepic");
        filetool.saveMyBitmap(filenm, bitmap);
        return filetool.getFilePath()+ filenm;
    }

    /**
     * 解决小米手机上获取图片路径为null的情况
     * @param intent
     * @return
     */
    public Uri geturi(android.content.Intent intent) {
        Uri uri = intent.getData();
        String type = intent.getType();
        if (uri.getScheme().equals("file") && (type.contains("image/"))) {
            String path = uri.getEncodedPath();
            if (path != null) {
                path = Uri.decode(path);
                ContentResolver cr = this.getContentResolver();
                StringBuffer buff = new StringBuffer();
                buff.append("(").append(MediaStore.Images.ImageColumns.DATA).append("=")
                        .append("'" + path + "'").append(")");
                Cursor cur = cr.query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                        new String[] { MediaStore.Images.ImageColumns._ID },
                        buff.toString(), null, null);
                int index = 0;
                for (cur.moveToFirst(); !cur.isAfterLast(); cur.moveToNext()) {
                    index = cur.getColumnIndex(MediaStore.Images.ImageColumns._ID);
                    // set _id value
                    index = cur.getInt(index);
                }
                if (index == 0) {
                    // do nothing
                } else {
                    Uri uri_temp = Uri
                            .parse("content://media/external/images/media/"
                                    + index);
                    if (uri_temp != null) {
                        uri = uri_temp;
                    }
                }
            }
        }
        return uri;
    }

    void orderTalkedMan() {
        String ownerid = conversation.getIcon();
        Customer owner = null;
        for (Customer item : talkmans) {
            if (ownerid.equals(item.getId() + "")) {
                owner = item;
            }
        }
        if (owner != null) {
            talkmans.remove(owner);
            talkmans.add(0, owner);
        }
    }

    String[] longArrToStringArray(Long[] pids) {
        String[] lmids = new String[pids.length];
        for (int i = 0; i < pids.length; i++) {
            lmids[i] = pids[i] + "";
        }
        return lmids;
    }

    void updateGroupTitle(final String newTitle, String sendMsg) {
        conversation.getConversation().updateTitle(newTitle, sendMsg, new IMCallback<Void, Void, String>() {
            @Override
            public void success(Void arg0) {
                txt_groupnm.setText(newTitle);
                groupnm_dialog.dismiss();
            }

            @Override
            public void process(Void arg0) {
            }

            @Override
            public void fail(String arg0) {
                ToastManager.show(TalkingActivity.this, ResourceHelper.getStringValue(R.string.wangluoyic) + arg0, 2000);
            }
        });
    }

    boolean isExitedMan(List<Customer> plist, int pid) {
        for (Customer item : plist) {
            if (item.getId() == pid) {
                return true;
            }
        }
        return false;
    }

    Long[] getGroupMem(List<Customer> plist) {
        List<Long> list = new ArrayList<Long>();
        for (Customer item : maillist) {
            if (item.isIsgroupmem() && !isExitedMan(plist, item.getId())) {
                list.add((long) item.getId());
            }
        }
        return list.toArray(new Long[]{});
    }

    void addGroupMemConversation(String msg, Long... uids) {
        //添加成员成功后,推送的系统消息
        conversationHandler.addGroupMemConversation(msg, conversationId, new IMCallback<List<Long>, List<Long>, String>() {
            @Override
            public void success(List<Long> arg0) {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        loading_dialog.dismiss();
                        if (rightMenu.isSecondaryMenuShowing()) {
                            rightMenu.toggle();
                        }
                        if (groupMenu.isSecondaryMenuShowing()) {
                            groupMenu.toggle();
                        }
                    }
                }, 1000);
            }

            @Override
            public void process(List<Long> arg0) {
            }

            @Override
            public void fail(String arg0) {
                loading_dialog.dismiss();
            }
        }, uids);
    }

    void createGroupConversation(final String title, String pmsg, Long... uids) {
        conversationHandler.createConversation(title, userid, pmsg, ConversationHandler.GROUP, new IMCallback<SessionInfo, Integer, String>() {
            @Override
            public void success(final SessionInfo arg0) {
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        loading_dialog.hide();
                        Intent intentitem = new Intent(TalkingActivity.this, TalkingActivity.class);
                        intentitem.putExtra("title", title);
                        intentitem.putExtra("converid", arg0.getConversationid());
                        TalkingActivity.this.startActivity(intentitem);
                        TalkingActivity.this.finish();
                    }
                }, 2000);
            }

            @Override
            public void process(Integer arg0) {

            }

            @Override
            public void fail(String arg0) {
                loading_dialog.hide();
                ToastManager.show(TalkingActivity.this, ResourceHelper.getStringValue(R.string.wangluoyic), 2000);
            }
        }, uids);
    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
        lastindex1 = firstVisibleItem;
    }

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {
//        if (scrollState == OnScrollListener.SCROLL_STATE_IDLE && lastindex1 == 0) {
//            loadMoreButton.setText("loading..."); // 设置按钮文字loading
//            handler.postDelayed(new Runnable() {
//                @Override
//                public void run() {
////					loadHisData(false);
//                }
//            }, 2000);
//        }
//        switch (scrollState) {
//            case AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL:
//                adapter.setIsscroll(true);
//                break;
//            case AbsListView.OnScrollListener.SCROLL_STATE_IDLE://停止滚动
//                adapter.setIsscroll(false);
//                //adapter.notifyDataSetChanged();
//                break;
//        }
    }

    boolean cancelMailMenu(SlidingMenu menu) {
        boolean isshow = menu.isSecondaryMenuShowing();
        if (menu.isSecondaryMenuShowing()) {
            menu.toggle();
        }
        return isshow;
    }

    void removeGroupMem(String groupMsg, Long... openIds) {
        //删除成员成功后,推送的系统消息
        conversationHandler.removeMembers(conversationId, groupMsg, new IMCallback<List<Long>, List<Long>, String>() {
            @Override
            public void success(List<Long> arg0) {
                for (long item : arg0) {
                    removeMem(item);
                }
                groupAdapter.notifyDataSetChanged();
            }

            @Override
            public void process(List<Long> arg0) {
            }

            @Override
            public void fail(String arg0) {
                ToastManager.show(TalkingActivity.this, ResourceHelper.getStringValue(R.string.wangluoyic), 2000);
            }
        }, openIds);
    }

    void disableGroup() {
        conversation.getConversation().disband(new IMCallback<Void, Void, String>() {
            @Override
            public void success(Void arg0) {
                TalkingActivity.this.finish();
            }

            @Override
            public void process(Void arg0) {
            }

            @Override
            public void fail(String arg0) {
                ToastManager.show(TalkingActivity.this, ResourceHelper.getStringValue(R.string.wangluoyic), 2000);
            }
        });
    }

    void quitGroup(String pmsg) {
        conversation.getConversation().quit(pmsg, new IMCallback<Void, Void, String>() {
            @Override
            public void success(Void arg0) {
                TalkingActivity.this.finish();
            }

            @Override
            public void process(Void arg0) {
            }

            @Override
            public void fail(String arg0) {
                ToastManager.show(TalkingActivity.this, ResourceHelper.getStringValue(R.string.wangluoyic), 2000);
            }
        });
    }

    void setConversationTop(boolean istop) {
        conversation.getConversation().stayOnTop(istop, new Action<Long>() {
            @Override
            public void handler(Long t) {
                conversation.setToplevel(t);
                topImageDescEvent();
            }
        });
    }

    void removeMem(long item) {
        int index = getMemIndex(item);
        if (index >= 0) {
            talkmans.remove(index);
        }
    }

    int getMemIndex(long pid) {
        for (int i = 0; i < talkmans.size(); i++) {
            if (talkmans.get(i).getId() == pid) {
                return i;
            }
        }
        return -1;
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == event.KEYCODE_BACK) {
            if (cancelMailMenu(rightMenu)) {
//				handlerConversation();
                initTalking();
                return true;
            }
            if (cancelMailMenu(groupMenu)) {
                return true;
            }

            if (ll_options.getVisibility() != emojicons.getVisibility()){
                ll_options.setVisibility(View.GONE);
                emojicons.setVisibility(View.GONE);
                return true;
            }


        }
        return super.onKeyDown(keyCode, event);
    }

    void initTalking() {
        if (conversation.getTyp() == 2) {
            initGroupTitleView();
        }
    }

    @Override
    protected void onDestroy() {
        unregistBrocaster();
        super.onDestroy();
    }

    void showData(DayStep daystep) {
        int goal = SPHelper.getDetailMsg(this, "totalsteps", 10000);
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
        int height = SPHelper.getDetailMsg(this, "height", 170);
        int weight = SPHelper.getDetailMsg(this, "weight", 65);
        double dis = StepTool.calStepDistance(height, daystep.getTotal_step() - daystep.getFast_step(), daystep.getFast_step());
        double cal = StepTool.calc_calories(weight, daystep.getFast_step(), daystep.getTotal_step() - daystep.getFast_step());
        tv_distance.setText(Integer.toString(dealPoint(dis)));
        tv_cal.setText(Integer.toString(dealPoint(cal)));
        Food food = getFoodMsg(getCalFoodId(getCalorieIndex((int) cal)));
       /* stotal.setText(daystep.getTotal_step() + "");
        sfast.setText(daystep.getFast_step() + "");*/
        if (food != null) {
            double beishu = cal / 500;
            String hui = beishu < 2 ? "1" : String.valueOf((int) beishu);
            firstnm.setText(" ≈ " + hui + food.getUnit());
            lastnm.setText(food.getName());
            int resId = getResources().getIdentifier(food.getPicname(),
                    "drawable", "com.example.renrenstep");
            middlenm.setImageDrawable(getResources().getDrawable(resId));
        }
    }

    Food getFoodMsg(int id) {
        List<FoodCal> list = FoodXmlHandler.getFoods(this);
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
            Set<String> res = SPHelper.getDetailMsg(this, key, new HashSet<String>());
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
        double[][] arr = {{0.1, 7}, {7, 50}, {50, 70}, {70, 81},
                {81, 100}, {100, 108}, {108, 130}, {130, 140},
                {140, 156}, {156, 180}, {180, 202}, {202, 260},
                {260, 381}, {381, 424}, {424, 524}, {524, 540},
                {550, 50000}};
        for (int i = 0; i < arr.length; i++) {
            if (cal > arr[i][0] && cal <= arr[i][1]) {
                index = i;
                break;
            }
        }
        return index;



    }

    int dealPoint(double cal) {
        return new Double(cal).intValue();
    }


    private Object getObject(String name){
        FileInputStream fis = null;
        ObjectInputStream ois = null;
        try {
            fis = this.openFileInput(name);
            ois = new ObjectInputStream(fis);
            return ois.readObject();
        } catch (Exception e) {
            e.printStackTrace();
            //这里是读取文件产生异常
        } finally {
            if (fis != null){
                try {
                    fis.close();
                } catch (IOException e) {
                    //fis流关闭异常
                    e.printStackTrace();
                }
            }
            if (ois != null){
                try {
                    ois.close();
                } catch (IOException e) {
                    //ois流关闭异常
                    e.printStackTrace();
                }
            }
        }
        //读取产生异常，返回null
        return null;
    }



    public void showCol() {
        hisdatas=getDate();
        getHisData(hisdatas);
        WindowManager wm = (WindowManager) this.getSystemService(Context.WINDOW_SERVICE);
        pwidth= wm.getDefaultDisplay().getWidth()-20;
        HomeColumnar columnar = new HomeColumnar(TalkingActivity.this, hisdatas,pwidth);
        linear.addView(columnar);
        StepHisData data= hisdatas.get(hisdatas.size()-1);
        mSteplist =  getStepDataInDay(data.getYear(), data.getMonth(), data.getDay());

    }
    void getHisData(List<StepHisData> hisdatas){
        for(StepHisData data:hisdatas){
            DayStep step= base.GetOneDay(data.getYear(),data.getMonth() ,data.getDay());
            data.setSteps(step.getTotal_step());
            data.setFaststep(step.getFast_step());
        }
    }

    List<StepHisData> getDate(){
        GregorianCalendar gc=new GregorianCalendar();
        List<StepHisData> hisdates=new ArrayList<StepHisData>();
        for(int i=-7;i<0;i++){
            gc.setTime(new Date());
            gc.add(5, i);
            StepHisData hisdate=new StepHisData();
            hisdate.setYear(gc.get(Calendar.YEAR));
            hisdate.setMonth(gc.get(Calendar.MONTH)+1);
            hisdate.setDay(gc.get(Calendar.DAY_OF_MONTH));
            hisdates.add(hisdate);
        }
        return hisdates;
    }

    List<MinuteStep> getStepDataInDay(int year,int month,int day){
        return  base.GetOneDaySteps(year, month, day);
    }


    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        switch (i){
            case 0:
              /*  Intent albumIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(albumIntent, ALBUM_RQUEST);*/

                GenderInfiViewBuilder gender=new GenderInfiViewBuilder(TalkingActivity.this,new PhotoListner());
                gender.setView();
                gender.setColor(getSysColor());
                gender.setResource(getResources().getString(R.string.pop_head_item0), getResources().getString(R.string.pop_head_item1));
                gender.setResult(new String[]{PhotoTool.TAKE_PHOTO+"",PhotoTool.OPEN_PHOTOLIST+""});
                gender.popup(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT, Gravity.BOTTOM, 0, 0);

                break;
            case 1:
                Intent mapIntent = new Intent(TalkingActivity.this, MapDemoActivity.class);
                startActivityForResult(mapIntent, MAP_REQUEST);
                break;
            case 2:
                Bitmap bitmap = BitmapUtil.createViewBitmap(view_today_steps);
                String filename = String.valueOf(System.currentTimeMillis())+".jpg";
                try {
                    saveFile(bitmap, filename);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                sendMsgToUser(tools.getFilePath()+filename, MessageInfo.MssageType.IMAGE);

                break;

            case 3:
                Bitmap histBitmao = BitmapUtil.createViewBitmap(linear_line);
                String histfile = String.valueOf(System.currentTimeMillis())+".jpg";
                try {
                    saveFile(histBitmao,histfile);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                sendMsgToUser(tools.getFilePath()+histfile, MessageInfo.MssageType.IMAGE);
                break;
           /* case 4:
                ll_options.setVisibility(View.GONE);
                TalkInfiViewBuilder student=new TalkInfiViewBuilder(this,new StudentListener());
                student.setView();
                student.setColor(R.color.black_p50);
                student.setResource("成为此人健康学员");
                student.setResult(new String[]{"confirm"});
                student.popup(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT, Gravity.BOTTOM, 0, 0);
                break;
            case 5:

                ll_options.setVisibility(View.GONE);
                TalkInfiViewBuilder master=new TalkInfiViewBuilder(this,new MasterListener());
                master.setView();
                master.setColor(R.color.black_p50);
                master.setResource("成为此人健康导师");
                master.setResult(new String[]{"confirm"});
                master.popup(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT, Gravity.BOTTOM, 0, 0);
                break;*/


        }

    }



    //加载历史数据
    void LoadPrevious(final boolean isfirstload,Message message,int pageSize) {
        // mtalks.clear();
        mlist.setTranscriptMode(AbsListView.TRANSCRIPT_MODE_DISABLED);
        conversation.getConversation().listPreviousMessages(message, pageSize, new Action<List<MessageInfo>>() {
            @Override
            public void handler(List<MessageInfo> t) {
                loadPreData(t);
            }
        });
        conversation.getConversation().resetUnreadCount();
    }

    void loadPreData(List<MessageInfo> data) {
        if (data.size() != 0) {
            addPreMessages(data);
            adapter.notifyDataSetChanged();
        }
        refreshLayout.setRefreshing(false);
        mlist.setTranscriptMode(AbsListView.TRANSCRIPT_MODE_NORMAL);
    }

    void addPreMessages(List<MessageInfo> plist) {
        ArrayList<Talked> list = new ArrayList<>();

        for (MessageInfo item : plist) {
            Talked talkd = getMessage(item);
            if (talkd != null) {
                list.add(talkd);

            }
        }

        mtalks.addAll(0, list);
    }


    class PhotoListner implements InfoViewBuilder.EventListener<String> {
        @Override
        public void onConfirm(String result) {
            Intent intent=null;
            if(result.equals(PhotoTool.TAKE_PHOTO+"")){
                Date date=new Date();
                photo_filenm = String.valueOf(date.getTime())+".jpg";
                intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(new File(fileUtils.getFilePath()+photo_filenm)));
            }else if(result.equals(PhotoTool.OPEN_PHOTOLIST+"")){
                intent=PhotoTool.openPhotoListIntent();
            }
            int code =Integer.parseInt(result);
            if(intent!=null)
                startActivityForResult(intent,code);
        }
        @Override
        public void onCancel() {
        }
    }

    int getSysColor(){
        return SPHelper.getDetailMsg(TalkingActivity.this, "gender", "M").equals("M")?0xff3D98FF:0xffff0000;
    }

}
