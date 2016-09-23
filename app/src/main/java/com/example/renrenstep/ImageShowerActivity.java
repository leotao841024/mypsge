package com.example.renrenstep;

import android.app.Activity;
import android.os.Bundle;
import com.bumptech.glide.Glide;
import uk.co.senab.photoview.PhotoView;

/**
 * Created by admin on 2016/3/30.
 */
public class ImageShowerActivity extends Activity {
    private PhotoView iv_shower;
    //private PhotoViewAttacher mAttacher;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_shower);
        iv_shower = (PhotoView) findViewById(R.id.iv_shower);

        String url = getIntent().getStringExtra("imageShower_url");
        Glide.with(this).load(url).into(iv_shower);
      /*  Drawable bitmap = getResources().getDrawable(R.drawable.leader_12);
        iv_shower.setImageDrawable(bitmap);*/

    }
}
