package co.kr.newpolice;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class DetailTypeActivity extends Activity {
	String Tag = "" , Type = "";
	private static final String LOGTAG = "SKY";

	TextView title;
	Button dele;
	private int temp = 0;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_detailtype);

		Tag = getIntent().getStringExtra("tag");
		Type = getIntent().getStringExtra("type");

		dele = (Button)findViewById(R.id.dele);
		title = (TextView)findViewById(R.id.title_dt);

		title.setText("" + Tag);
		if (Type.equals("휴지통")) {
			dele.setVisibility(View.VISIBLE);
		}


		findViewById(R.id.btn1).setOnClickListener(btnListener); 
		findViewById(R.id.btn2).setOnClickListener(btnListener); 
		findViewById(R.id.btn3).setOnClickListener(btnListener);
		findViewById(R.id.btn4).setOnClickListener(btnListener);
		findViewById(R.id.dele).setOnClickListener(btnListener);

	}


	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
	}
	//버튼 리스너 구현 부분 
	View.OnClickListener btnListener = new View.OnClickListener() {
		public void onClick(View v) {
			Intent it = new Intent(DetailTypeActivity.this, ResultActivity.class);
			it.putExtra("tag",Tag);
			it.putExtra("type",Type);
			switch (v.getId()) {
			case R.id.btn1:
				it.putExtra("level","1");
				break;
			case R.id.btn2:
				it.putExtra("level","2");
				break;
			case R.id.btn3:
				it.putExtra("level","3");
				break;
			case R.id.btn4:
				break;
			case R.id.dele:
				/*
				AlertDialog.Builder alt_bld = new AlertDialog.Builder(DetailTypeActivity.this , AlertDialog.THEME_HOLO_LIGHT);
				alt_bld.setMessage("정말 비우시겠습니까?").setCancelable(
						false).setPositiveButton("예",
								new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int id) {
								// Action for 'Yes' Button
								SELECT_Del();
							}
						}).setNegativeButton("아니오",
								new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int id) {
								// Action for 'NO' Button
								dialog.cancel();
							}
						});
				AlertDialog alert = alt_bld.create();
				// Title for AlertDialog
				alert.setTitle("알림");
				// Icon for AlertDialog
				alert.show();
				 */
				String tag = getIntent().getStringExtra("tag");;
			    String items[] = { tag+" 필수 1순위", tag+" 필수 2순위", tag+" 필수 3순위", tag+" 심화 문제" };
			    temp = 0;
			    AlertDialog.Builder ab = new AlertDialog.Builder(DetailTypeActivity.this, AlertDialog.THEME_HOLO_LIGHT);
			    ab.setTitle("비우실 문제 난이도를 선택해주세요");
			    ab.setSingleChoiceItems(items, 0,
			        new DialogInterface.OnClickListener() {
			    	
			        public void onClick(DialogInterface dialog, int whichButton) {
			            // 각 리스트를 선택했을때 
			        	Log.d("SKY", "선택 결과 : " + whichButton);
			        	temp = whichButton;
			        }
			        }).setPositiveButton("확인",
			        new DialogInterface.OnClickListener() {
			        	
			        public void onClick(DialogInterface dialog, int whichButton) {
			            // OK 버튼 클릭시 , 여기서 선택한 값을 메인 Activity 로 넘기면 된다.
			        	Log.d("SKY", "선택 결과 : " +getIntent().getStringExtra("tag") + " "+(temp+1)+"번째 버튼");
			        	SELECT_Del((temp+1)+"", getIntent().getStringExtra("tag"));
			        	
			        }
			        }).setNegativeButton("취소",
			        new DialogInterface.OnClickListener() {
			        public void onClick(DialogInterface dialog, int whichButton) {
			            // Cancel 버튼 클릭시
			        }
			        });
			    ab.show();
				return;
			}
			startActivity(it);

		}
	};
	private void SELECT_Del()
	{
		try{
			//  db파일 읽어오기
			SQLiteDatabase db = openOrCreateDatabase("mydb.db", Context.MODE_PRIVATE, null);
			// 쿼리로 db의 커서 획득
			String sql = "DELETE FROM garbege_table;";
			Log.e("SKY","sql  : "+ sql);
			db.execSQL(sql);
			db.close();

			SQLiteDatabase db_ = openOrCreateDatabase("police_db.db", Context.MODE_PRIVATE, null);
			// 쿼리로 db의 커서 획득
			String sql_ = "UPDATE data_table set flag = '0';";
			Log.e("SKY","sql  : "+ sql_);
			db_.execSQL(sql_);
			db_.close();
			Toast.makeText(getApplicationContext(), "휴지통을 모두 지웠습니다.", 0).show();
		}
		catch (SQLException se) {
			// TODO: handle exception
			Log.e("selectData()Error! : ",se.toString());
		}
	}
	
	private void SELECT_Del(String level, String type)
	{
		try{
			//  db파일 읽어오기
			SQLiteDatabase db = openOrCreateDatabase("mydb.db", Context.MODE_PRIVATE, null);
			// 쿼리로 db의 커서 획득
			String sql = "DELETE FROM garbege_table";
			sql += " WHERE level = '" + level
					+ "' and type = '" + type + "오엑스';";
			Log.d("SKY","sql  : "+ sql);
			db.execSQL(sql);
			db.close();

			SQLiteDatabase db_ = openOrCreateDatabase("police_db.db", Context.MODE_PRIVATE, null);
			// 쿼리로 db의 커서 획득
			String sql_ = "UPDATE data_table SET flag = '0'";
			sql_ += " WHERE level = '" + level
					+ "' and type = '" + type + "오엑스';";
			Log.d("SKY","sql  : "+ sql_);
			db_.execSQL(sql_);
			db_.close();
			Toast.makeText(getApplicationContext(), "해당 휴지통을 지웠습니다.", 0).show();
		}
		catch (SQLException se) {
			// TODO: handle exception
			Log.e("selectData()Error! : ",se.toString());
		}
	}
}
