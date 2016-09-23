package receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;

/**
 * Created by jam on 2016/4/6.
 */
public class BrocastReviverManager  extends BroadcastReceiver {
    private IntentFilter intent;
    private Context context;
    private ReciverCallback callback;
    public BrocastReviverManager(Context context){
        this.intent=new IntentFilter();
        this.context=context;
    }
    public void setCallback(ReciverCallback callback){
        this.callback = callback;
    }
    public void addFilter(String filter){
        intent.addAction(filter);
    }
    public  void begin(){
        context.registerReceiver(this,intent);
    }

    public void end(){
        context.unregisterReceiver(this);
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        if(callback==null) return;
        callback.handler(intent);
    }

    public interface  ReciverCallback{
        void handler(Intent intent);
    }
}

