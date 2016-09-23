package groupview;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import bean.Talked;

/**
 * Created by jam on 2016/3/24.
 */
public abstract class TalkContentBase {
    private View view;
    public LayoutInflater inflater;
    protected Context context;
    public TalkContentBase(Context context){
        inflater= LayoutInflater.from(context);
        this.context=context;
    }

    public static View dealTalk(int position,List<Talked> mlist,View view,boolean scroll,Context context){
        Talked talked=mlist.get(position);
        String[] talktyp={"txt","image"};
        TalkContentBase[] talkhandlers={new TalkContentText(context),new TalkContentImage(context)};
        for(int i=0;i<talktyp.length;i++){
            if(talktyp[i]==talked.getMsgtyp()){
                talkhandlers[i].setIsscroll(scroll);
                talkhandlers[i].setView(view);
                talkhandlers[i].setShowtime(isShow(position, mlist));
                return talkhandlers[i].hanlder(talked);
            }
        }
        return null;
    }

    static  boolean isShow(int position,List<Talked> mlist){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss " );
        if(position==0){
            return true;
        }
        Talked totalk=mlist.get(position-1);
        Talked mytalk=mlist.get(position);
        try{
            Date todate=sdf.parse(totalk.getTimer());
            Date mydate=sdf.parse(mytalk.getTimer());
            long totimer = todate.getTime();
            long mytimer = mydate.getTime();
            long chatimer = mytimer-totimer;
            return  chatimer>600000;
        }catch(Exception ex){
            return false;
        }
    }

    public  void setView(View view){
        this.view = view;
    }

    public boolean isscroll() {
        return isscroll;
    }

    public void setIsscroll(boolean isscroll) {
        this.isscroll = isscroll;
    }

    private boolean isscroll;

    public View getView(){
        return view;
    }

    public  abstract View hanlder(Talked talked);
    //显示规则
    protected boolean isshow(){
        return showtime;
    }

    public void setShowtime(boolean showtime) {
        this.showtime = showtime;
    }

    private boolean showtime;


}
