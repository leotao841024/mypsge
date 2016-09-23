package com.example.renrenstep;

import java.util.ArrayList; 
import java.util.List;
import android.app.Activity;
import android.app.ActionBar.LayoutParams;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.PixelFormat;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.NinePatchDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
public class Demo extends Activity{
//	private TextView text1;
//	private HandlerThread handlerthread1, handlerthread2;
//	private Handler handler1, handler2;
//	private Button btn_send;
	ListView mlist;
	List<String> sources=new ArrayList<String>();//group_mem_item


	Bitmap drawable2Bitmap(Drawable drawable) {
		if (drawable instanceof BitmapDrawable) {
			return ((BitmapDrawable) drawable).getBitmap();
		} else if (drawable instanceof NinePatchDrawable) {
			Bitmap bitmap = Bitmap
					.createBitmap(
							drawable.getIntrinsicWidth(),
							drawable.getIntrinsicHeight(),
							drawable.getOpacity() != PixelFormat.OPAQUE ? Bitmap.Config.ARGB_8888
									: Bitmap.Config.RGB_565);
			Canvas canvas = new Canvas(bitmap);
			drawable.setBounds(0, 0, drawable.getIntrinsicWidth(),
					drawable.getIntrinsicHeight());
			drawable.draw(canvas);
			return bitmap;
		} else {
			return null;
		}
	}
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_demo);
		ImageView img=null;
		img.setLayoutParams(new LayoutParams(100,1100));
//		Drawable mydemo= getResources().getDrawable(R.drawable.share_dialog_head);

//		((view.TalkingContentImageView)findViewById(R.id.image1)).setImageBitmap(drawable2Bitmap(mydemo));

//		final Button btn=((Button) findViewById(R.id.btn));
//		sources.add("");sources.add("");
//		mlist=(ListView)findViewById(R.id.mlist);
//		btn.setOnClickListener(new OnClickListener() {
//		@Override
//		public void onClick(View arg0) {
//			// TODO Auto-generated method stub
//			btn.setTextColor(0x7f070008);
//		}
//		});
//		DemoAdapter demoAdapter=new DemoAdapter();
//		mlist.setAdapter(demoAdapter);
//		mlist.setOnItemClickListener(new OnItemClickListener() {
//			@Override
//			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,long arg3) {
//				arg1.setSelected(true);
//			}
//		});
//					@Override
//					public void onClick(View arg0) {
//						// TODO Auto-generated method stub
//						// SysLog log=new SysLog();
//						// log.setKey("key");
//						// log.setTimer(12313L);
//						// log.setTyp(MyLog.LogLevel.DEBUG.ordinal());
//						// log.setValue("value");
//						// log.save();
//						try {
//						ClassEntity entity = new ClassEntity("MemAccount");
//						entity.addQueryCloumn("nc");
//						entity.addQueryCloumn("pwd");
//						QueryEntity query = new QueryEntity();
//						query.whereLike("nc", "z");
//						JSONObject jsonobj = SqlManager.getFormaterSql(entity, query,null, "id desc");
//						HttpTool.HttpRequestAsy(Cons.GETDATA, jsonobj, HttpType.JSON, "", Demo.this, new IHttpCallBackAsy() {
//								@Override
//								public void success(String param) {
//									String res="";
//									res+="";
//								} 
//								@Override
//								public void failed(String pmsg) {
//									// TODO Auto-generated method stub
//									String res="";
//									res+=""; 
//								}
//								@Override
//								public void exception(String pmsg) {
//									// TODO Auto-generated method stub
//									String res="";
//									res+=""; 
//								}
//							});
//						} catch (Exception e) {
//							e.printStackTrace();
//						} 
//					}
////				});
//		((Button) findViewById(R.id.update))
//				.setOnClickListener(new OnClickListener() {
//					@Override
//					public void onClick(View arg0) {
//						// TODO Auto-generated method stub
//						SysLog log = new SysLog();
//						log.setId(1L);
//						log.setKey("key1");
//						log.setTimer(123113L);
//						log.setTyp(MyLog.LogLevel.WARNING.ordinal());
//						log.setValue("value1");
//						log.save();
//					}
//				});
//		((Button) findViewById(R.id.get))
//				.setOnClickListener(new OnClickListener() {
//					@Override
//					public void onClick(View arg0) {
//						// TODO Auto-generated method stub
//						// SysLog log = new SysLog();
//						// SysLog log1 = log.findById(SysLog.class, 1);
//						List<SysLog> mlist = MyLog.getLogs(LogLevel.DEBUG);
//					}
//				}); 
//		setContentView(R.layout.activity_demo);
//		Countdown countdown=new Countdown(15, new CountdownBack(){ 
//			@Override
//			public void process(int timer) {
//				// TODO Auto-generated method stub
//				Log.i("process", timer+"");
//			}
//			@Override
//			public void finish() {
//				Log.i("finish","finish");
//			} 
//		}); 
//		countdown.begin();
//		((Button) findViewById(R.id.btn)).setOnClickListener(new OnClickListener() { 
//			@Override
//			public void onClick(View arg0) {
//				// TODO Auto-generated method stub
////				SysLog log=new SysLog();
////				log.setKey("key");
////				log.setTimer(12313L);
////				log.setTyp(MyLog.LogLevel.DEBUG.ordinal());
////				log.setValue("value");
////				log.save(); 
//				MemInfoViewBuilder age=new MemInfoViewBuilder(Demo.this,new ListnerInfo());
//				List<String> datas = new ArrayList<String>();
//				for (int i = 14; i <= 100; i++) {
//					datas.add(String.valueOf(i));
//				}
//				age.setView();
//				age.setColor(0xffff0000);
//				age.setDataSource(datas);
//				age.setSelectedVal("25");
//				age.setUnit("岁数");
////				age.popup(Gravity.BOTTOM, 0, 0);
//			}
//		});
//		((Button) findViewById(R.id.update)).setOnClickListener(new OnClickListener() { 
//			@Override
//			public void onClick(View arg0) {
//				// TODO Auto-generated method stub
//				SysLog log=new SysLog();
//				log.setId(1L);
//				log.setKey("key1");
//				log.setTimer(123113L);
//				log.setTyp(MyLog.LogLevel.WARNING.ordinal());
//				log.setValue("value1");
//				log.save();
//			}
//		});
//		
//		((Button) findViewById(R.id.get)).setOnClickListener(new OnClickListener() { 
//			@Override
//			public void onClick(View arg0) {
//				// TODO Auto-generated method stub
////				SysLog log = new SysLog();
////				SysLog log1 = log.findById(SysLog.class, 1);
//				List<SysLog> mlist = MyLog.getLogs(LogLevel.DEBUG);
//			}
//		});  
		// initView();
		// // initData();
		// // Intent intent=new Intent(Cons.RECIVE_MSG_ACTION);
		// // sendBroadcast(intent);
		// btn_send = (Button) findViewById(R.id.btn_send);
		// btn_send.setOnClickListener(this);
		// handlerthread1 = new HandlerThread("A");
		// handlerthread1.start();
		// handlerthread2 = new HandlerThread("B");
		// handlerthread2.start();
		// System.out.println("mainthread is " + Thread.currentThread().getId()+
		// "");
		// handler1 = new Handler(handlerthread1.getLooper()) {
		// @Override
		// public void handleMessage(Message msg) {
		// // TODO Auto-generated method stub
		// System.out.println("handler1 recevid msg is " + msg.arg1);
		// handler2.postDelayed(new Runnable() {
		// @Override
		// public void run() {
		// // TODO Auto-generated method stub
		// Message msg = new Message();
		// msg.arg1 = 2;
		// handler2.sendMessage(msg);
		// }
		// }, 5000);
		// super.handleMessage(msg);
		// }
		// }; 
		// handler2 = new Handler(handlerthread2.getLooper()) {
		// @Override
		// public void handleMessage(Message msg) {
		// System.out.println("handler2 recevid msg is " + msg.arg1);
		// handler1.postDelayed(new Runnable() {
		// @Override
		// public void run() {
		// // TODO Auto-generated method stub
		// Message msg = new Message();
		// msg.arg1 = 2;
		// handler1.sendMessage(msg);
		// }
		// }, 5000);
		// super.handleMessage(msg);
		// }
		// };
		// System.out.println("handlerthread1 is ="+ handlerthread1.getId() +
		// "");
		// System.out.println("handlerthread2 is ="+ handlerthread2.getId() +
		// "");
	} 
//	class ListnerInfo implements EventListener<String>{
//		@Override
//		public void onConfirm(String result) {
//			// TODO Auto-generated method stub
//			manager.Toast.makeText(Demo.this, result, 2000).show();
//		}
//		@Override
//		public void onCancel() {
//			// TODO Auto-generated method stub
//		}
//	} 
//	Handler mhandler = new Handler() {
//		@Override
//		public void handleMessage(Message msg) {
//			// TODO Auto-generated method stub
//			text1.setText(text1.getText().toString() + msg.arg1);
//		}
//	};
//
//	void initView() {
//		// text1 = (TextView) findViewById(R.id.text1);
//	}
//
//	HandlerThread handlerthread = new HandlerThread("") {
//		@Override
//		public Looper getLooper() {
//			// TODO Auto-generated method stub
//			return super.getLooper();
//		}
//
//		@Override
//		public void run() {
//			// TODO Auto-generated method stub
//			super.run();
//		}
//	};
//
//	void initData() {
//		new Thread() {
//			@Override
//			public void run() {
//				// TODO Auto-generaed method stub
//				while (true) {
//					Message msg = new Message();
//					msg.arg1 = 1;
//					mhandler.sendMessage(msg);
//					try {
//						Thread.sleep(5000);
//					} catch (InterruptedException e) {
//						// TODO Auto-generated catch block
//						e.printStackTrace();
//					}
//				}
//			}
//		}.start();
//	}
//
//	@Override
//	public void onClick(View arg0) {
//		// // TODO Auto-generated method stub
//		// Message msg = new Message();
//		// msg.arg1 = 2;
//		// handler1.sendMessage(msg);
//	}

	class DemoAdapter extends BaseAdapter{
		LayoutInflater flater;
		public DemoAdapter() {
			 flater=LayoutInflater.from(Demo.this);
		}
		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return sources.size();
		} 
		@Override
		public Object getItem(int arg0) {
			// TODO Auto-generated method stub
			return sources.get(arg0);
		} 
		@Override
		public long getItemId(int arg0) {
			// TODO Auto-generated method stub
			return 0;
		} 
		@Override
		public View getView(int arg0, View arg1, ViewGroup arg2) {
			arg1= flater.inflate(R.layout.group_mem_item, null);
			return arg1;
		} 
	}
}
