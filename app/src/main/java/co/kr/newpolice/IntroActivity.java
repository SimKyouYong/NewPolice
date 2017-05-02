package co.kr.newpolice;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.v4.app.FragmentActivity;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import co.kr.newpolice.common.Check_Preferences;
import co.kr.newpolice.common.CommonUtil;
import co.kr.newpolice.common.MySQLiteOpenHelper;
import co.kr.newpolice.obj.Tableobj;
import co.kr.sky.AccumThread;

public class IntroActivity extends FragmentActivity {
	DisplayMetrics displayMetrics = new DisplayMetrics();
	CommonUtil dataSet = CommonUtil.getInstance();
	float local_Ver, Server_Ver;

	private static final int DELAY_TIME = 3000;
	AccumThread mThread;
	MySQLiteOpenHelper vc;
	Map<String, String> map = new HashMap<String, String>();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_intro);
		
		
		vc = new MySQLiteOpenHelper(this);

		
		
//		//파일 유무 체크 
//		File file = new File(dataSet.Local_Path  + filename + ".mp3");
//		if(file.exists()==true) {
//			//파일이 있을시
//			Log.e("SKY" , "파일 있음 패스!!");
//		}
		
		
		
		map.put("url", dataSet.SERVER + "Version.php");
		// 스레드 생성
		mThread = new AccumThread(this, mAfterAccum, map, 0, 0, null);
		mThread.start(); // 스레드 시작!!
		
		
	}
	private void MainMove(){
		Handler handler = new Handler(Looper.getMainLooper());
		handler.postDelayed(new Runnable() {
			@Override
			public void run() {
				//Intent intent = new Intent(IntroActivity.this, MainActivity.class);
				Intent intent = new Intent(IntroActivity.this, SlideNotActivity.class);
				startActivity(intent);
				finish();
			}
		}, DELAY_TIME);
	}
	Handler mAfterAccum = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			if (msg.arg1 == 0) {
				String res = (String) msg.obj;
				Log.e("CHECK", "RESULT  -> " + res);
				String ver = Check_Preferences.getAppPreferences(getApplicationContext(), "version");
				Log.e("SKY", "ver  -> " + ver);
				if (!ver.equals("")) {
					local_Ver = Float.parseFloat(ver);
					Server_Ver = Float.parseFloat(res);
					Log.e("SKY", "local_Ver :: " + local_Ver);
					Log.e("SKY", "Server_Ver :: " + Server_Ver);
					if (local_Ver < Server_Ver) {
						// 다운로드
						new DownloadFileFullAsync(IntroActivity.this).execute(dataSet.SERVER + "police_db.db");
					} else {
						MainMove();
					}
				} else {
					// 최초버전.. 무조건 다운로드
					new DownloadFileFullAsync(IntroActivity.this).execute(dataSet.SERVER + "police_db.db");
				}
			}
		}
	};
	public class DownloadFileFullAsync extends
	AsyncTask<String, String, String> {

		private ProgressDialog mDlg;
		private Context mContext;

		public DownloadFileFullAsync(Context context) {
			mContext = context;
		}

		@Override
		protected void onPreExecute() {
			mDlg = new ProgressDialog(mContext, AlertDialog.THEME_HOLO_LIGHT);
			mDlg.setProgressStyle(ProgressDialog.STYLE_SPINNER);
			mDlg.setMessage("로딩중");
			mDlg.show();

			super.onPreExecute();
		}

		@Override
		protected String doInBackground(String... params) {

			int count = 0;
			try {
				String str = dataSet.SERVER + "police_db.db";
				String DEFAULT_FILE_PATH = dataSet.Local_Path + "/police_db.db";
				Log.e("SKY", "STR :: " + str);
				
				URL url = new URL(str);
				URLConnection conexion = url.openConnection();
				conexion.setRequestProperty("Accept-Encoding", "identity"); // <--- Add this line

				conexion.connect();

				int lenghtOfFile = conexion.getContentLength();
				Log.e("SKY", "Lenght of file: " + lenghtOfFile);

				File file = new File(dataSet.Local_Path);
				if (!file.exists()) { // 원하는 경로에 폴더가 있는지 확인
					Log.e("SKY", "폴더 생성");
					file.mkdirs();
				}

				
				InputStream input = new BufferedInputStream(url.openStream());
				OutputStream output = new FileOutputStream(DEFAULT_FILE_PATH);

				byte data[] = new byte[1024];
				long total = 0;
				while ((count = input.read(data)) != -1) {
					Log.e("SKY", "total :: " + total);
					total += count;
					publishProgress("" + (int) ((total * 100) / lenghtOfFile));
					output.write(data, 0, count);
				}

				output.flush();
				output.close();
				input.close();


			} catch (IOException e) {
				e.printStackTrace();
			}

			return null;
		}

		@Override
		protected void onProgressUpdate(String... progress) {
			// Log.e("SKY" , "progress size :: " + progress.length);
			// Log.e("SKY" , "progress 0 :: " + progress[0]);
			mDlg.setProgress(Integer.parseInt(progress[0]));
		}

		@SuppressWarnings("deprecation")
		@Override
		protected void onPostExecute(String unused) {
			mDlg.dismiss();
			Check_Preferences.setAppPreferences(IntroActivity.this, "version","" + Server_Ver);
			MainMove();
		}
	}
}
