package tools;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;

import manager.MyLog;

public class FileUtils {
	private String SDPath;

	public FileUtils(String file) {
		SDPath = Environment.getExternalStorageDirectory().getAbsolutePath()
				+ "/" + file + "/";
	}

	public String getFilePath() {
		return SDPath;
	}

	public FileInputStream getFileStream(String filenm) {
		if (isFileExist(filenm)) {
			String path = SDPath + filenm;
			File file = new File(path);
			try {
				FileInputStream inputStream = new FileInputStream(file);
				return inputStream;
			} catch (Exception ex) {

			}
		}
		return null;
	}

	/**
	 * @param fileName
	 * @return
	 */
	public File createSDFile(String fileName) {
		File file = new File(SDPath + fileName);
		try {
			file.createNewFile();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return file;
	}

	/**
	 * @param dirName
	 * @return
	 */
	public File createSDDir(String dirName) {
		File file = new File(SDPath + dirName);
		file.mkdir();
		return file;
	}

	public boolean moveFile(String srcFileName, String destDirName) {
		File srcFile = new File(srcFileName);
		if (!srcFile.exists() || !srcFile.isFile())
			return false;
		File destDir = new File(destDirName);
		if (!destDir.exists())
			destDir.mkdirs();
		return srcFile.renameTo(new File(destDirName + File.separator
				+ srcFile.getName()));
	}

	public File createSDDir() {
		File file = new File(SDPath);
		file.mkdir();
		return file;
	}

	/**
	 * @param fileName
	 * @return
	 */
	public boolean isFileExist(String fileName) {
		File file = new File(SDPath + fileName);
		return file.exists();
	}

	/**
	 * @param path
	 * @param fileName
	 * @param inputStream
	 * @return
	 */
	public File writeToSDfromInput(String fileName, InputStream inputStream) {
		File file = createSDFile(fileName);
		OutputStream outStream = null;
		try {
			outStream = new FileOutputStream(file);
			byte[] buffer = new byte[4 * 1024];
			while (inputStream.read(buffer) != -1) {
				outStream.write(buffer);
			}
			outStream.flush();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				outStream.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return file;
	}

	public void saveMyBitmap(String filenm, Bitmap mBitmap) {
		if (!filenm.equals("") && mBitmap != null) {
			File f = new File(SDPath + filenm);
			try {
				f.createNewFile();
				FileOutputStream fOut = null;
				fOut = new FileOutputStream(f);
				mBitmap.compress(Bitmap.CompressFormat.PNG, 100, fOut);
				fOut.flush();
				fOut.close();
			} catch (Exception e) {
				MyLog.e("exception", e.getMessage());
			}
		}
	}
	
	public static void renameFile(String path, String oldname, String newname) {
		if (!oldname.equals(newname)) {// 新的文件名和以前文件名不同时,才有必要进行重命名
			File oldfile = new File(path + "/" + oldname);
			File newfile = new File(path + "/" + newname);
			if (!oldfile.exists()) {
				return;// 重命名文件不存在
			}
			if (newfile.exists())
				MyLog.i("file", "isexited"); 
			else {
				oldfile.renameTo(newfile);
			}
		} else { 
		}
	}
	
	public static String getRealPathFromURI(Uri contentUri, Context context) {
		String res = null;
		String[] proj = { MediaStore.Images.Media.DATA };
		Cursor cursor = context.getContentResolver().query(contentUri, proj,
				null, null, null);
		if (cursor.moveToFirst()) { 
			int column_index = cursor
					.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
			res = cursor.getString(column_index);
		}
		cursor.close();
		return res;
	}




}