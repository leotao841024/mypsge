package com.example.renrenstep;

import helper.HttpTool;
import helper.SPHelper;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONObject;

import tools.BitmapUtil;
import tools.FileUtils;
import comm.CommHelper;
import constant.Cons;

import android.content.ContentValues;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory.Options;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class PhotoActivity extends MyBaseActivity implements OnClickListener {
	private ImageView iv_pic;
	private TextView tv_reset, tv_conf;
	private ByteArrayOutputStream outStream;
	private String uri;
	private Bitmap saveBitmap; 
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_photo); 
		try {
			initView();
		}
		catch (Exception e) { 
			e.printStackTrace();
		} 
	} 
	private void initView() throws Exception { 
		Intent intent = getIntent();
		uri = intent.getStringExtra("uri");
		byte[] buffer = new byte[1024];
		int len = -1;
		outStream = new ByteArrayOutputStream();
		InputStream inStream = getContentResolver().openInputStream(Uri.parse(uri));
		while ((len = inStream.read(buffer)) != -1) {
			outStream.write(buffer, 0, len);
		}
		byte[] data = outStream.toByteArray();
		inStream.close();
		
		Options options = new Options();
		options.inSampleSize = 4;
		Bitmap bitmap = BitmapUtil.getPicFromBytes(data, options);
		iv_pic = (ImageView) findViewById(R.id.iv_pic); 
		if(bitmap!=null){
			Matrix matrix=new Matrix();
			matrix.setRotate(90);
			saveBitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
			iv_pic.setImageBitmap(saveBitmap);
		}
		tv_reset = (TextView) findViewById(R.id.tv_reset);
		tv_conf = (TextView) findViewById(R.id.tv_conf);
		tv_reset.setOnClickListener(this);
		tv_conf.setOnClickListener(this);
	}
	@Override
	public void onClick(View v) {
		Intent intent;
		switch (v.getId()) {
		case R.id.tv_reset:
			intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
			startActivityForResult(intent, 1); 
			//this.finish();
			break;
		case R.id.tv_conf: 
			final String path = FileUtils.getRealPathFromURI(Uri.parse(uri), this);
			new AsyncTask<String, Void, String>() { 
				@Override
				protected String doInBackground(String... params) {
					try
					{
						Map<String,String> maps=new HashMap<String,String>();
						maps.put("mtoken", SPHelper.getBaseMsg(PhotoActivity.this, "mtoken", "mtoken"));
						String response= HttpTool.uploadFile(params[0], path, "filedata",maps);
						JSONObject jsonObject = new JSONObject(response);
						int res= jsonObject.getInt("status");
						if(res==0){
							String filenm=jsonObject.getString("filename"); 
							File file=new File(path);
							FileUtils filetool=new FileUtils("stepic");
							File saveFile= filetool.createSDDir(); 
							filetool.saveMyBitmap(filenm, saveBitmap);
							SPHelper.setDetailMsg(PhotoActivity.this, "uri", saveFile.getAbsolutePath()+"/"+filenm);
							updateUserPic(filenm);
							sendUsermsgChangeMsg(CommHelper.getCompleteStr(CommHelper.changeMemMsg));
						}
						Intent photo = new Intent();
						photo.setAction(Cons.NORIFY_RECEIVER);
						photo.putExtra("pic", "confirm");
						sendBroadcast(photo); 
						return response; 
					}catch(Exception ex){ 
						return "";
					}
				}
			}.execute(Cons.UPLOAD_HEAD);
			try {
				outStream.close();
				PhotoActivity.this.finish();
			} catch (IOException e) {
				e.printStackTrace();
			}
			break;

		default:
			break;
		}
	}
	
	void updateUserPic(String picnm){ 
		ContentValues values=new ContentValues();
		values.put("acvtor", picnm);
		db.open();
		db.update("apm_sys_friend",values, " mid=?", new String[]{SPHelper.getBaseMsg(PhotoActivity.this, "mid", "0")});
		db.close();
	}
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (data != null) {
			switch (requestCode) {
			case 1:
				//Uri uri = data.getData();
				//SPHelper.setDetailMsg(this, "uri", uri.toString());
				if (resultCode == RESULT_OK) {
					if(data !=null&& data.getData()!=null){ //可能尚未指定intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
						//返回有缩略图
						if(data.hasExtra("data")){
							String cUri = data.getData().toString();
							Intent photo = new Intent(this, PhotoActivity.class);
							photo.putExtra("uri", cUri); 
							startActivity(photo);
							finish();
						}
					}else{
						Toast.makeText(this,getResources().getString(R.string.nofindpic), 2000).show();
					}
				}
				break;
			default:
				break;
			}
		}
	}
	 @Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		if(keyCode==KeyEvent.KEYCODE_BACK){ 
				Intent photo = new Intent();
				photo.setAction(Cons.NORIFY_RECEIVER);
				photo.putExtra("pic", "cancel");
				sendBroadcast(photo); 
		}
		return super.onKeyDown(keyCode, event);
	}

}
