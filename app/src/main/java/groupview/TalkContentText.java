package groupview;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.renrenstep.R;

import bean.Talked;
import constant.Cons;
import helper.ImageLoadAsy;

/**
 * Created by jam on 2016/3/24.
 */
public class TalkContentText extends  TalkContentBase {

    public TalkContentText(Context context) {
        super(context);
    }
    @Override
    public View hanlder(Talked talked) {
        View convertView = getView();
        if(talked.getTyp().equals("sys")){
            convertView = initViewSys();
            ((TextView)convertView.findViewById(R.id.txt_sys_remind)).setText(talked.getCont());
            return convertView;
        }
        if(talked.getTyp().equals("to")){
            convertView = initViewTo();
        }
        else{
            convertView = initViewMe();
        }
        TextView mytimer=(TextView)convertView.findViewById(R.id.mytimer);
        final view.CircleImageView img_left=(view.CircleImageView)convertView.findViewById(R.id.img_left);
        TextView tv_content=(TextView)convertView.findViewById(R.id.tv_content);
        LinearLayout loser=(LinearLayout)convertView.findViewById(R.id.loser);
        TextView txt_usernm=(TextView)convertView.findViewById(R.id.txt_usernm);
        loser.setVisibility(talked.isIsgood()?View.GONE:View.VISIBLE);
        mytimer.setText(talked.getTimer());
        mytimer.setVisibility(isshow()?View.VISIBLE:View.GONE);
        tv_content.setText(talked.getCont());
        if(talked.getTyp().equals("sys")||talked.getTyp().equals("me")||talked.getChatTyp()==1){
            txt_usernm.setVisibility(View.GONE);
        }else{
            txt_usernm.setText(talked.getNc()+":");
            txt_usernm.setVisibility(View.VISIBLE);
        }
        String filenm=talked.getPicnm();
//        Glide.with(context).load(Cons.DONW_PIC +filenm).placeholder(R.drawable.regist_man).into(img_left);
        ImageLoadAsy imgloadasy = new ImageLoadAsy(new ImageLoadAsy.ImageLodeCallback() {
            @Override
            public void handler(Bitmap map) {
                if (map == null) return;
                img_left.setImageBitmap(map);
            }
        }, Cons.DONW_PIC + talked.getPicnm(), ImageLoadAsy.LoadType.NORMAL,talked.getPicnm());
        imgloadasy.execute(talked.getPicnm());
        return convertView;
    }
    
    View initViewMe(){
        View view= inflater.inflate(R.layout.chatting_item_msg_text_right, null);
        return view;
    }

    View initViewTo(){
        View view=inflater.inflate(R.layout.chatting_item_msg_text_left, null);
        return view;
    }

    View initViewSys(){
        View view=inflater.inflate(R.layout.chatting_item_msg_text_center,null);
        return view;
    }
}
