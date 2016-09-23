package view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.widget.ImageView;

/**
 * Created by jam on 2016/3/25.
 */
public class TalkingImageView extends ImageView {


    public TalkingImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        int w = getWidth(), h = getHeight();
        Drawable drawable = getDrawable();
        if (drawable == null) {
            return;
        }
        if (w == 0 || h == 0) {
            return;
        }
        Bitmap b = ((BitmapDrawable) drawable).getBitmap();
        if (null == b) {
            return;
        }
//        Paint p = new Paint();
//        Bitmap bitmap = b.copy(Bitmap.Config.ARGB_8888, true);
//        Canvas cavs = new Canvas(bitmap);
//        Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
//        cavs.drawBitmap(bitmap, rect, rect, p);
        Paint p = new Paint();
        p.setColor(Color.RED);// 设置红色

        canvas.drawBitmap(b, 0, 0, null);
        p.reset();
        p.setStyle(Paint.Style.FILL);
        p.setColor(Color.WHITE);
        Path path1=new Path();
        path1.moveTo(w, 0);
        path1.lineTo(w - 10, 0);
        path1.lineTo(w-10, 30);
        path1.lineTo(w, 40);
        path1.lineTo(w, 0);
        path1.close();//封闭
        canvas.drawPath(path1, p);
        path1.moveTo(w, h);
        path1.lineTo(w - 10, h);
        path1.lineTo(w-10, 50);
        path1.lineTo(w, 40);
        path1.lineTo(w, h);
        path1.close();//封闭
        canvas.drawPath(path1, p);
        Path path2=new Path();
        path2.moveTo(0, 50);//设置Path的起点
        path2.quadTo(20, 0, 0, 0); //设置贝塞尔曲线的控制点坐标和终点坐标
        canvas.drawPath(path2, p);//画出贝塞尔曲线
        Bitmap mask = Bitmap.createBitmap(200, 300, Bitmap.Config.ARGB_8888);

        Canvas can=new Canvas();
        RectF oval3 = new RectF(0, 0, 200, 300);// 设置个新的长方形
        can.drawRoundRect(oval3, 20, 15, p);//第二个参数是x半径，第三个参数是y半径
        can.setBitmap(mask);

        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_OUT));
        canvas.drawBitmap(mask, 0f, 0f, paint);

    }
//    Bitmap getMask(int width,int height){
//        Bitmap result = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
//
//    }
}
