package groupview;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import android.view.View;
import android.widget.AbsListView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.renrenstep.ImageShowerActivity;
import com.example.renrenstep.R;

import java.io.File;

import bean.Talked;
import constant.Cons;
import helper.ImageLoadAsy;
import manager.ImageCacheManger;
import tools.FileUtils;


/**
 * Created by jam on 2016/3/24.
 */
public class TalkContentImage extends TalkContentBase {
    private ImageCacheManger imageCached;

    public TalkContentImage(Context context) {
        super(context);
        imageCached=new ImageCacheManger("stepic");
    }

    @Override
    public View hanlder(final Talked talked) {
        View convertView = getView();
        final boolean isother = talked.getTyp().equals("to");
        if (isother) {
            convertView = initViewTo();
        } else {
            convertView = initViewMe();
        }
        TextView mytimer = (TextView) convertView.findViewById(R.id.mytimer);
        final view.CircleImageView img_left = (view.CircleImageView) convertView.findViewById(R.id.img_left);
        final ImageView pic_content = (ImageView) convertView.findViewById(R.id.pic_content);
        LinearLayout loser = (LinearLayout) convertView.findViewById(R.id.loser);
        TextView txt_usernm = (TextView) convertView.findViewById(R.id.txt_usernm);
        loser.setVisibility(talked.isIsgood() ? View.INVISIBLE : View.VISIBLE);
        mytimer.setText(talked.getTimer());
        mytimer.setVisibility(isshow() ? View.VISIBLE : View.GONE);
        if (talked.getTyp().equals("me") || talked.getChatTyp() == 1) {
            txt_usernm.setVisibility(View.GONE);
        } else {
            txt_usernm.setText(talked.getNc() + ":");
            txt_usernm.setVisibility(View.VISIBLE);
        }
        String[] filenms = talked.getCont().split("/");
        final String filenm = filenms[filenms.length - 1];

        FileUtils filetools = new FileUtils("stepic");
        String path=filetools.getFilePath() + filenm;
        File file=new File(path);
        if(file.exists()) {
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inJustDecodeBounds = true;
            BitmapFactory.decodeFile(filetools.getFilePath() + filenm, options);
            AbsListView.LayoutParams lp = new AbsListView.LayoutParams(AbsListView.LayoutParams.WRAP_CONTENT,options.outHeight);
            convertView.setLayoutParams(lp);
        }
        Bitmap bit = imageCached.getCachedBitmap(filenm);
        if (bit == null) {
            ImageLoadAsy imgloadasy = new ImageLoadAsy(new ImageLoadAsy.ImageLodeCallback() {
                @Override
                public void handler(Bitmap map) {
                    if(map!=null) {
                        pic_content.setLayoutParams(new LinearLayout.LayoutParams(map.getWidth(), map.getHeight()));
                        pic_content.setImageBitmap(map);
                    }
                }
            }, talked.getCont(), ImageLoadAsy.LoadType.FILTER,filenm);
            imgloadasy.setIsleft(isother);
            imgloadasy.execute(filenm);
        } else {
            pic_content.setLayoutParams(new LinearLayout.LayoutParams(bit.getWidth(), bit.getHeight()));
            pic_content.setImageBitmap(bit);
        }
        ImageLoadAsy imgloadasy1 = new ImageLoadAsy(new ImageLoadAsy.ImageLodeCallback() {
            @Override
            public void handler(Bitmap map) {
                if (map == null) return;
                img_left.setImageBitmap(map);
            }
        }, Cons.DONW_PIC + talked.getPicnm(), ImageLoadAsy.LoadType.NORMAL,talked.getPicnm());
        imgloadasy1.execute(talked.getPicnm());
        pic_content.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(context, ImageShowerActivity.class);
                i.putExtra("imageShower_url",talked.getCont());
                context.startActivity(i);
            }
        });
        return  convertView;
    }


    View initViewMe() {
        View view = inflater.inflate(R.layout.chatting_item_msg_pic_right, null);
        return view;
    }

    View initViewTo() {
        View view = inflater.inflate(R.layout.chatting_item_msg_pic_left, null);
        return view;
    }

}
