package co.kr.newpolice;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

import co.kr.newpolice.obj.Tableobj;

public class LockScreenActivity extends Activity{

	TextView title, exam, exam_r,title_sub;
	ArrayList<Tableobj> arr = new ArrayList<Tableobj>();

	Button btn_x, btn_o, btn_prev, btn_next, btn_garbege, btn_checkmarkon,
	btn_checkmarkload;
	LinearLayout oxonlybar;
	LinearLayout view;
	int random_index;
	private Typeface ttf;

	@Override
	protected void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_lockscreen);
		ttf = Typeface.createFromAsset(getAssets(), "HANYGO230.TTF");

		getWindow().addFlags(WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED 
				| WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD);


		view = (LinearLayout) findViewById(R.id.view);
		title = (TextView) findViewById(R.id.title_dt);
		exam = (TextView) findViewById(R.id.exam);
		exam_r = (TextView) findViewById(R.id.exam_r);
		title_sub = (TextView) findViewById(R.id.title_sub);
		btn_x = (Button) findViewById(R.id.btn_x);
		btn_o = (Button) findViewById(R.id.btn_o);
		btn_prev = (Button) findViewById(R.id.btn_prev);
		btn_next = (Button) findViewById(R.id.btn_next);
		btn_garbege = (Button) findViewById(R.id.btn_garbege);
		btn_checkmarkon = (Button) findViewById(R.id.btn_checkmarkon);
		btn_checkmarkload = (Button) findViewById(R.id.btn_checkmarkload);
		
		title.setTypeface(ttf);
		title_sub.setTypeface(ttf);
		exam.setTypeface(ttf);
		

		findViewById(R.id.btn_x).setOnClickListener(btnListener);
		findViewById(R.id.btn_o).setOnClickListener(btnListener);

		setSetting();
	}
	// 버튼 리스너 구현 부분
	View.OnClickListener btnListener = new View.OnClickListener() {
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.btn_x:
				if (arr.get(random_index).getSolution().equals("X")) {
					// 정답입니다.
					String rr = "";
					if (arr.get(random_index).getDetail() == null) {
						rr = "";
					} else {
						rr = arr.get(random_index).getDetail();
					}
					if (rr == null) {
						rr = "";
					}
					//exam_r.setText("정답입니다.\n" + rr + "\n");
					update_O(arr.get(random_index).getKeyindex(),arr.get(random_index).getFlag());
					btn_o.setVisibility(View.GONE);
					btn_x.setVisibility(View.GONE);
					confirmDialog("정답입니다.\n" + rr);
				} else {
					// 오답입니다.
					String rr = "";
					if (arr.get(random_index).getDetail() == null) {
						rr = "";
					} else {
						rr = arr.get(random_index).getDetail();
					}
					if (rr == null) {
						rr = "";
					}
					//exam_r.setText("틀렸습니다. 정답은 O " + " 입니다.");
					btn_o.setVisibility(View.GONE);
					btn_x.setVisibility(View.GONE);
					update_X();
					confirmDialog("정답은 X 입니다.");
				}

				break;
			case R.id.btn_o:
				btn_next.setVisibility(View.GONE);
				if (arr.get(random_index).getSolution().equals("O")) {
					// 정답입니다.
					update_O(arr.get(random_index).getKeyindex(),arr.get(random_index).getFlag());
					btn_x.setVisibility(View.GONE);
					btn_o.setVisibility(View.GONE);
					confirmDialog("정답입니다.");
					//exam_r.setText("정답입니다.");
					
				} else {
					// 오답입니다.
					String rr = "";
					if (arr.get(random_index).getDetail() == null) {
						rr = "";
					} else {
						rr = arr.get(random_index).getDetail();
					}
					if (rr == null) {
						rr = "";
					}
					btn_o.setVisibility(View.GONE);
					btn_x.setVisibility(View.GONE);
					update_X();
					confirmDialog("정답은 X 입니다\n" + rr);
					//exam_r.setText("틀렸습니다. 정답은 X 입니다.\n" + rr);

				}
				break;
			}

		}
	};
	private void setSetting() {
		int random = (int) (Math.random() * 3);
		SELECT_DB(random);
	}
	private void SELECT_DB(int random) // 디비 값 조회해서 저장하기
	{
		Log.e("SKY", "//random :: " + random);
		String type = "경찰학개론오엑스";
		switch (random) {
		case 0:
			type = "형법오엑스";
			break;
		case 1:
			type = "형소법오엑스";
			break;
		case 2:
			type = "경찰학개론오엑스";
			break;
		}
		title.setText("락스크린");
		title_sub.setText(type.replace("오엑스", ""));
		arr.clear();
		try {
			// db파일 읽어오기
			SQLiteDatabase db = openOrCreateDatabase("police_db.db",
					Context.MODE_PRIVATE, null);
			// 쿼리로 db의 커서 획득
			String sql = "SELECT * FROM data_table where type = '" + type + "';";
			Log.d("SKY", "sql  : " + sql);

			Cursor cur = db.rawQuery(sql, null);
			// 처음 레코드로 이동
			while (cur.moveToNext()) {
				// 읽은값 출력
				Log.d("SKY",
						cur.getString(0) + "/" + cur.getString(1) + "/"
								+ cur.getString(7) + "/"
								+ cur.getString(3).substring(0, 5));
				if (cur.getString(7) == null) {// 맞춘 카운트 없음
					arr.add(new Tableobj(cur.getString(0), cur.getString(1),
							cur.getString(2), cur.getString(3), cur
							.getString(4), cur.getString(5), cur
							.getString(6), cur.getString(7)));
				} else if ((Integer.parseInt(cur.getString(7)) < 4)) { // 4번이상
					// 맟춘
					// 문제는
					// 안씀
					arr.add(new Tableobj(cur.getString(0), cur.getString(1),
							cur.getString(2), cur.getString(3), cur
							.getString(4), cur.getString(5), cur
							.getString(6), cur.getString(7)));
				}

			}
			cur.close();
			db.close();

			btn_garbege.setVisibility(View.INVISIBLE);
			random_index = (int) (Math.random() * arr.size());
			Log.e("SKY", "random_index :: " + random_index);
			exam.setText(Html.fromHtml("" + arr.get(random_index).getProblem()));

		} catch (SQLException se) {
			// TODO: handle exception
			Log.e("selectData()Error! : ", se.toString());
		}
	}
	private void update_O(String key_index, String flag) {
		Log.e("SKY", "flag :: " + flag);
		Log.e("SKY", "key_index :: " + key_index);
		int flag_i = 0;
		if (flag != null) {
			if (!flag.equals("null")) {
				flag_i = Integer.parseInt(flag);
			}
		}
		Log.d("SKY", "flag_i :: " + flag_i);
		flag_i++;
		if (flag_i >= 4) {
			// 휴지통 이동
			Garbage(arr.get(random_index));
		}
		Flag_plus(flag_i, key_index);
	}
	private void Flag_plus(int flag_i, String key_index) {
		try {
			// 인서트쿼리
			// Toast.makeText(One.this, "즐겨찾기 등록완료!", 0).show();
			String sql;
			String db_ = "";
			String table = "";
			table = "note_table";
			db_ = "mydb.db";
			SQLiteDatabase db1 = openOrCreateDatabase("police_db.db",
					Context.MODE_PRIVATE, null);
			try {
				sql = "UPDATE " + "data_table" + " SET flag = '" + flag_i
						+ "' WHERE keyindex = " + key_index + ";";
				Log.d("SKY", "sql  : " + sql);
				db1.execSQL(sql);
			} catch (Exception e) {
				db1.close();
				Log.e("MiniApp", "sql error : " + e.toString());
			}
			SQLiteDatabase db = openOrCreateDatabase(db_, Context.MODE_PRIVATE,
					null);

			try {
				sql = "UPDATE " + table + " SET flag = '" + flag_i
						+ "' WHERE keyindex = " + key_index + ";";
				Log.d("SKY", "sql  : " + sql);
				db.execSQL(sql);
			} catch (Exception e) {
				db.close();
				Log.e("MiniApp", "sql error : " + e.toString());
			}
			db.close();
		} catch (Exception e) {
			Log.e("MiniApp", "onPostExecute error : " + e.toString());
		}
	}
	private void Garbage(Tableobj arr_) {
		if (Select_Search(arr_)) {
			Log.e("SKY", "이미 같은게 등록되어있음!!");
			return;
		}
		try {
			// 인서트쿼리
			// Toast.makeText(One.this, "즐겨찾기 등록완료!", 0).show();
			SQLiteDatabase db = openOrCreateDatabase("mydb.db",
					Context.MODE_PRIVATE, null);
			String sql;
			try {
				sql = "INSERT INTO garbege_table values(";
				sql += arr_.getKeyindex();
				sql += ",'" + arr_.getPart() + "',";
				sql += "'" + arr_.getLevel() + "',";
				sql += "'" + (arr_.getProblem() + "").replaceAll("'", "''")
						+ "',";
				sql += "'" + arr_.getSolution() + "',";
				sql += "'" + (arr_.getDetail() + "").replaceAll("'", "''")
						+ "',";
				sql += "'" + arr_.getType() + "',";
				sql += "'" + arr_.getFlag() + "')";

				Log.d("SKY", "sql  : " + sql);
				db.execSQL(sql);

			} catch (Exception e) {
				db.close();
				Log.e("MiniApp", "sql error : " + e.toString());
			}
			db.close();
		} catch (Exception e) {
			Log.e("MiniApp", "onPostExecute error : " + e.toString());
		}
		// 오답노트에 있으면 삭제 할수 있도록!! 개발
		try {
			// 인서트쿼리
			// Toast.makeText(One.this, "즐겨찾기 등록완료!", 0).show();
			SQLiteDatabase db = openOrCreateDatabase("mydb.db",
					Context.MODE_PRIVATE, null);
			String sql;
			try {
				sql = "DELETE FROM note_table where keyindex ='";
				sql += arr_.getKeyindex() + "';";

				Log.d("SKY", "sql  : " + sql);
				db.execSQL(sql);
			} catch (Exception e) {
				db.close();
				Log.e("MiniApp", "sql error : " + e.toString());
			}
			db.close();
		} catch (Exception e) {
			Log.e("MiniApp", "onPostExecute error : " + e.toString());
		}
	}
	private Boolean Select_Search(Tableobj arr_) {
		Boolean f = false;
		try {
			// db파일 읽어오기
			SQLiteDatabase db = openOrCreateDatabase("mydb.db",
					Context.MODE_PRIVATE, null);
			// 쿼리로 db의 커서 획득
			String sql = "SELECT * FROM garbege_table where keyindex = '"
					+ arr_.getKeyindex() + "';";
			Log.d("SKY", "sql  : " + sql);

			Cursor cur = db.rawQuery(sql, null);
			// 처음 레코드로 이동
			while (cur.moveToNext()) {
				// 읽은값 출력
				f = true;
				Log.d("SKY",
						cur.getString(0) + "/" + cur.getString(1) + "/"
								+ cur.getString(2) + "/" + cur.getString(3)
								+ "/" + cur.getString(4) + "/"
								+ cur.getString(5) + "/" + cur.getString(6)
								+ "/" + cur.getString(7));

			}
			cur.close();
			db.close();
		} catch (SQLException se) {
			// TODO: handle exception
			Log.e("selectData()Error! : ", se.toString());
		}
		return f;
	}
	private void update_X() {// 오답노트
		XData(arr.get(random_index));
		XNote(arr.get(random_index));
	}
	private void XData(Tableobj arr_) {
		try {
			// 인서트쿼리
			// Toast.makeText(One.this, "즐겨찾기 등록완료!", 0).show();
			String sql;
			String db_ = "";
			String table = "";
			table = "note_table";
			db_ = "mydb.db";
			SQLiteDatabase db1 = openOrCreateDatabase("police_db.db",
					Context.MODE_PRIVATE, null);
			try {
				sql = "UPDATE " + "data_table" + " SET flag = '" + 0
						+ "' WHERE keyindex = " + arr_.getKeyindex() + ";";
				Log.d("SKY", "sql  : " + sql);
				db1.execSQL(sql);
			} catch (Exception e) {
				db1.close();
				Log.e("MiniApp", "sql error : " + e.toString());
			}
			SQLiteDatabase db = openOrCreateDatabase(db_, Context.MODE_PRIVATE,
					null);

			try {
				sql = "UPDATE " + table + " SET flag = '" + 0
						+ "' WHERE keyindex = " + arr_.getKeyindex() + ";";
				Log.d("SKY", "sql  : " + sql);
				db.execSQL(sql);
			} catch (Exception e) {
				db.close();
				Log.e("MiniApp", "sql error : " + e.toString());
			}
			db.close();
		} catch (Exception e) {
			Log.e("MiniApp", "onPostExecute error : " + e.toString());
		}
	}

	private void XNote(Tableobj arr_) {
		if (Select_note_Search(arr_)) {
			Log.e("SKY", "이미 같은게 XNote 등록되어있음!!");
			return;
		}
		try {
			// 인서트쿼리
			// Toast.makeText(One.this, "즐겨찾기 등록완료!", 0).show();
			SQLiteDatabase db = openOrCreateDatabase("mydb.db",
					Context.MODE_PRIVATE, null);
			String sql;
			try {
				sql = "INSERT INTO note_table values(";
				sql += arr_.getKeyindex();
				sql += ",'" + arr_.getPart() + "',";
				sql += "'" + arr_.getLevel() + "',";
				sql += "'" + (arr_.getProblem() + "").replaceAll("'", "''")
						+ "',";
				sql += "'" + arr_.getSolution() + "',";
				sql += "'" + (arr_.getDetail() + "").replaceAll("'", "''")
						+ "',";
				sql += "'" + arr_.getType() + "',";
				sql += "'" + arr_.getFlag() + "')";

				Log.d("SKY", "sql  : " + sql);
				db.execSQL(sql);
			} catch (Exception e) {
				db.close();
				Log.e("MiniApp", "sql error : " + e.toString());
			}
			db.close();
		} catch (Exception e) {
			Log.e("MiniApp", "onPostExecute error : " + e.toString());
		}
	}
	private Boolean Select_note_Search(Tableobj arr_) {
		Boolean f = false;
		try {
			// db파일 읽어오기
			SQLiteDatabase db = openOrCreateDatabase("mydb.db",
					Context.MODE_PRIVATE, null);
			// 쿼리로 db의 커서 획득
			String sql = "SELECT * FROM note_table where keyindex = '"
					+ arr_.getKeyindex() + "';";
			Log.d("SKY", "sql  : " + sql);

			Cursor cur = db.rawQuery(sql, null);
			// 처음 레코드로 이동
			while (cur.moveToNext()) {
				// 읽은값 출력
				f = true;
				Log.e("SKY", cur.getString(0) + "/" + cur.getString(1) + "/"
						+ cur.getString(7));

			}
			cur.close();
			db.close();
		} catch (SQLException se) {
			// TODO: handle exception
			Log.e("selectData()Error! : ", se.toString());
		}
		return f;
	}
	public void confirmDialog(String message) {
		AlertDialog.Builder ab = new AlertDialog.Builder(this , AlertDialog.THEME_HOLO_LIGHT);
		//		.setTitle("부적결제 후 전화상담 서비스로 연결 되며 12시간 동안 재연결 무료 입니다.\n(운수대톡 )")
		ab.setMessage(message);
		ab.setPositiveButton("확인", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int whichButton) {
				finish();
				return;
			}
		})
		.show();
	}
}
