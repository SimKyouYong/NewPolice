package co.kr.newpolice;

import java.util.ArrayList;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.Configuration;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import co.kr.newpolice.common.Check_Preferences;
import co.kr.newpolice.obj.Tableobj;

public class ResultActivity extends Activity {
	String Tag = "", Type = "", Level = "";
	private static final String LOGTAG = "SKY";

	Boolean next_flag = false;
	LinearLayout view;
	TextView title, exam, exam_r;
	ArrayList<Tableobj> arr = new ArrayList<Tableobj>();

	Button btn_x, btn_o, btn_prev, btn_next, btn_garbege, btn_checkmarkon,
			btn_checkmarkload;
	LinearLayout oxonlybar;
	int position = 0;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_result);

		view = (LinearLayout) findViewById(R.id.view);
		Tag = getIntent().getStringExtra("tag");
		Type = getIntent().getStringExtra("type");
		Level = getIntent().getStringExtra("level");

		title = (TextView) findViewById(R.id.title_dt);
		exam = (TextView) findViewById(R.id.exam);
		exam_r = (TextView) findViewById(R.id.exam_r);

		btn_x = (Button) findViewById(R.id.btn_x);
		btn_o = (Button) findViewById(R.id.btn_o);
		btn_prev = (Button) findViewById(R.id.btn_prev);
		btn_next = (Button) findViewById(R.id.btn_next);

		btn_garbege = (Button) findViewById(R.id.btn_garbege);
		btn_checkmarkon = (Button) findViewById(R.id.btn_checkmarkon);
		btn_checkmarkload = (Button) findViewById(R.id.btn_checkmarkload);

		title.setText("" + Tag);

		findViewById(R.id.btn_x).setOnClickListener(btnListener);
		findViewById(R.id.btn_o).setOnClickListener(btnListener);
		findViewById(R.id.btn_prev).setOnClickListener(btnListener);
		findViewById(R.id.btn_next).setOnClickListener(btnListener);

		findViewById(R.id.btn_garbege).setOnClickListener(btnListener);
		findViewById(R.id.btn_checkmarkon).setOnClickListener(btnListener);
		findViewById(R.id.btn_checkmarkload).setOnClickListener(btnListener);

		setInit();

	}

	private void setInit() {

		
		if (Type.equals("익히기")) { // 익히기
			btn_x.setVisibility(View.INVISIBLE);
			btn_o.setVisibility(View.INVISIBLE);
			btn_prev.setVisibility(View.VISIBLE);
			view.setVisibility(View.GONE);
			int position_r = 0;
			if (Check_Preferences.getAppPreferences(ResultActivity.this, "position1").equals("")) {
				position_r = 0;
			}else{
				position_r = Integer.parseInt(Check_Preferences.getAppPreferences(ResultActivity.this, "position1"));
			}
			Study_1(position_r);
		} else if (Type.equals("오엑스")) { // 문제
			btn_x.setVisibility(View.VISIBLE);
			btn_o.setVisibility(View.VISIBLE);
			btn_next.setVisibility(View.INVISIBLE);
			int position_r = 0;
			if (Check_Preferences.getAppPreferences(ResultActivity.this, "position2").equals("")) {
				position_r = 0;
			}else{
				position_r = Integer.parseInt(Check_Preferences.getAppPreferences(ResultActivity.this, "position2"));
			}
			Study_2(position_r);
		} else if (Type.equals("오답노트")) { // 오답노트
			btn_x.setVisibility(View.VISIBLE);
			btn_o.setVisibility(View.VISIBLE);
			int position_r = 0;
			if (Check_Preferences.getAppPreferences(ResultActivity.this, "position3").equals("")) {
				position_r = 0;
			}else{
				position_r = Integer.parseInt(Check_Preferences.getAppPreferences(ResultActivity.this, "position3"));
			}
			Study_3(position_r);
		} else { // 휴지통
			btn_x.setVisibility(View.VISIBLE);
			btn_o.setVisibility(View.VISIBLE);
			btn_next.setVisibility(View.INVISIBLE);
			int position_r = 0;
			if (Check_Preferences.getAppPreferences(ResultActivity.this, "position4").equals("")) {
				position_r = 0;
			}else{
				position_r = Integer.parseInt(Check_Preferences.getAppPreferences(ResultActivity.this, "position4"));
			}
			Study_4(position_r);
		}
	}



	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
	}

	private void Study_1(int pp) {
		Log.d("SKY", "PP :: " + pp);
		String type = Tag + Type;
		Log.d("SKY", "type1 :: " + type);
		SELECT_DB(Level, type);
		btn_garbege.setVisibility(View.INVISIBLE);
		if (arr.size() == 0) {
			Toast.makeText(getApplicationContext(), "문제가 없습니다.",
					Toast.LENGTH_SHORT).show();
			finish();
			return;
		}
		exam.setText(Html.fromHtml("" + arr.get(pp).getProblem()));
		if (arr.get(pp).getSolution().equals("O")) {
			exam_r.setText("정답입니다.");
		} else {
			exam_r.setText("" + arr.get(pp).getDetail());
		}
	}

	private void Study_2(int pp) {
		Log.d("SKY", "PP :: " + pp);
		String type = Tag + Type;
		Log.d("SKY", "type :: " + type);
		SELECT_DB(Level, type);
		if (arr.size() == 0) {
			Toast.makeText(getApplicationContext(), "문제가 없습니다.",
					Toast.LENGTH_SHORT).show();
			finish();
			return;
		}
		// exam.setText(Html.fromHtml("<font color=\"red\">"+arr.get(pp).getProblem()+"</font>"));
		Log.i("SKY", "AAA ::" + arr.get(pp).getFlag() + "/"
				+ arr.get(pp).getProblem());
		exam.setText(Html.fromHtml("" + arr.get(pp).getProblem()));
		exam_r.setText("");
		/*
		 * if (arr.get(pp).getSolution().equals("O")) {
		 * exam_r.setText("정답입니다."); }else{ exam_r.setText("" +
		 * arr.get(pp).getDetail()); }
		 */
	}

	private void Study_3(int pp) {
		Log.d("SKY", "PP :: " + pp);
		Log.d("SKY", "Level :: " + Level);
		Log.d("SKY", "Tag :: " + Tag);
		// String type = Tag + Type;
		SELECT_Note_DB(Level, Tag);
		if (arr.size() == 0) {
			Toast.makeText(getApplicationContext(), "문제가 없습니다.",
					Toast.LENGTH_SHORT).show();
			finish();
			return;
		}
		exam.setText(Html.fromHtml("" + arr.get(pp).getProblem()));
		exam_r.setText("");
	}

	private void Study_4(int pp) {
		Log.d("SKY", "PP :: " + pp);
		Log.d("SKY", "Level :: " + Level);
		Log.d("SKY", "Tag :: " + Tag);
		SELECT_Garbege_DB(Level, Tag);
		btn_garbege.setVisibility(View.INVISIBLE);
		if (arr.size() == 0) {
			Toast.makeText(getApplicationContext(), "문제가 없습니다.",
					Toast.LENGTH_SHORT).show();
			finish();
			return;
		}
		exam.setText(Html.fromHtml("" + arr.get(pp).getProblem()));
		exam_r.setText("");
		/*
		 * if (arr.get(pp).getSolution().equals("O")) {
		 * exam_r.setText("정답입니다."); }else{ exam_r.setText("" +
		 * arr.get(pp).getDetail()); }
		 */
	}

	private void SELECT_Note_DB(String level, String type) // 디비 값 조회해서 저장하기
	{
		Log.e("SKY", "level :: " + level + "//type :: " + type);
		arr.clear();
		try {
			// db파일 읽어오기
			SQLiteDatabase db = openOrCreateDatabase("mydb.db",
					Context.MODE_PRIVATE, null);
			// 쿼리로 db의 커서 획득
			String sql = "SELECT * FROM note_table where type like '%" + type + "%';";
			Log.d("SKY", "sql  : " + sql);
			Cursor cur = db.rawQuery(sql, null);
			// 처음 레코드로 이동
			while (cur.moveToNext()) {
				// 읽은값 출력
				Log.i("SKY", cur.getString(0) + "/" + cur.getString(1) + "/"
						+ cur.getString(7));
				arr.add(new Tableobj(cur.getString(0), cur.getString(1), cur
						.getString(2), "<span style=\"color:red\">"
						+ cur.getString(3) + "</span>", cur.getString(4), cur
						.getString(5), cur.getString(6), cur.getString(7)));
			}
			cur.close();
			db.close();
		} catch (SQLException se) {
			// TODO: handle exception
			Log.e("selectData()Error! : ", se.toString());
		}
	}

	private void SELECT_Garbege_DB(String level, String type) // 디비 값 조회해서 저장하기
	{
		Log.e("SKY", "level :: " + level + "//type :: " + type);
		arr.clear();
		try {
			// db파일 읽어오기
			SQLiteDatabase db = openOrCreateDatabase("mydb.db",
					Context.MODE_PRIVATE, null);
			// 쿼리로 db의 커서 획득
			String sql = "SELECT * FROM garbege_table where type like '%" + type + "%';";
			Log.d("SKY", "sql  : " + sql);
			Cursor cur = db.rawQuery(sql, null);
			// 처음 레코드로 이동
			while (cur.moveToNext()) {
				// 읽은값 출력
				Log.i("SKY", cur.getString(0) + "/" + cur.getString(1) + "/"
						+ cur.getString(7));
				arr.add(new Tableobj(cur.getString(0), cur.getString(1), cur
						.getString(2), cur.getString(3), cur.getString(4), cur
						.getString(5), cur.getString(6), cur.getString(7)));
			}
			cur.close();
			db.close();
		} catch (SQLException se) {
			// TODO: handle exception
			Log.e("selectData()Error! : ", se.toString());
		}
	}

	private void SELECT_DB(String level, String type) // 디비 값 조회해서 저장하기
	{
		Log.e("SKY", "level :: " + level + "//type :: " + type);
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
			Garbage(arr.get(position));
			//문제 건너뜀 방지용 코드
			if (Type.equals("오엑스")) {
				position--;
			}
		}
		Flag_plus(flag_i, key_index);
	}

	private void update_X() {// 오답노트
		XData(arr.get(position));
		XNote(arr.get(position));
	}

	private void XData(Tableobj arr_) {
		try {
			// 인서트쿼리
			// Toast.makeText(One.this, "즐겨찾기 등록완료!", 0).show();
			String sql;
			String db_ = "";
			String table = "";
			if (Type.equals("오엑스")) {
				table = "data_table";
				db_ = "police_db.db";
			} else {
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

	private void Flag_plus(int flag_i, String key_index) {
		try {
			// 인서트쿼리
			// Toast.makeText(One.this, "즐겨찾기 등록완료!", 0).show();
			String sql;
			String db_ = "";
			String table = "";
			if (Type.equals("오엑스")) {
				table = "data_table";
				db_ = "police_db.db";
			} else {
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

	// 버튼 리스너 구현 부분
	View.OnClickListener btnListener = new View.OnClickListener() {
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.btn_x:
				if (!next_flag) {
					btn_next.setVisibility(View.VISIBLE);
					if (arr.get(position).getSolution().equals("X")) {
						// 정답입니다.
						String rr = "";
						if (arr.get(position).getDetail() == null) {
							rr = "";
						} else {
							rr = arr.get(position).getDetail();
						}
						if (rr == null) {
							rr = "";
						}
						exam_r.setText("정답입니다.\n" + rr + "\n");
						if (!Type.equals("휴지통"))
							update_O(arr.get(position).getKeyindex(),
									arr.get(position).getFlag());
						btn_o.setVisibility(View.INVISIBLE);
					} else {
						// 오답입니다.
						String rr = "";
						if (arr.get(position).getDetail() == null) {
							rr = "";
						} else {
							rr = arr.get(position).getDetail();
						}
						if (rr == null) {
							rr = "";
						}
						exam_r.setText("틀렸습니다. 정답은 O " + " 입니다.");
						btn_x.setVisibility(View.INVISIBLE);
						if (!Type.equals("휴지통"))
							update_X();
					}
					next_flag = true;

				}
				break;
			case R.id.btn_o:
				if (!next_flag) {
					btn_next.setVisibility(View.VISIBLE);
					if (arr.get(position).getSolution().equals("O")) {
						// 정답입니다.
						exam_r.setText("정답입니다.");
						if (!Type.equals("휴지통"))
							update_O(arr.get(position).getKeyindex(),
									arr.get(position).getFlag());
						btn_x.setVisibility(View.INVISIBLE);
					} else {
						// 오답입니다.
						String rr = "";
						if (arr.get(position).getDetail() == null) {
							rr = "";
						} else {
							rr = arr.get(position).getDetail();
						}
						if (rr == null) {
							rr = "";
						}
						exam_r.setText("틀렸습니다. 정답은 X 입니다.\n" + rr);
						btn_o.setVisibility(View.INVISIBLE);
						if (!Type.equals("휴지통"))
							update_X();
					}
					next_flag = true;
				}
				break;
			case R.id.btn_prev:
				if (position != 0) {
					// 이전 페이지로 이동
					position--;
					next_flag = false;
					setInit();
				} else {
					// 이전 페이지가 없음
					Toast.makeText(getApplicationContext(), "처음 문제입니다.",
							Toast.LENGTH_SHORT).show();
				}
				break;
			case R.id.btn_next:
				next_flag = true;
				if ((arr.size() - 1) == position) {
					// 마지막 페이지
					Toast.makeText(getApplicationContext(), "마지막 문제입니다.",
							Toast.LENGTH_SHORT).show();
				} else {
					// 다음 페이지로 이동
					/*
					 * if(Type.equals("오엑스") &
					 * Integer.parseInt(arr.get(position).getFlag()) < 4){
					 * 
					 * } else{
					 */
					position++;
					if (Type.equals("익히기")) { // 익히기
						Check_Preferences.setAppPreferences(ResultActivity.this, "position1", ""+position);
					} else if (Type.equals("오엑스")) { // 문제
						Check_Preferences.setAppPreferences(ResultActivity.this, "position2", ""+position);
					} else if (Type.equals("오답노트")) { // 오답노트
						Check_Preferences.setAppPreferences(ResultActivity.this, "position3", ""+position);
					} else { // 휴지통
						Check_Preferences.setAppPreferences(ResultActivity.this, "position4", ""+position);
					}

					// }
					next_flag = false;
					setInit();
				}
				break;
			case R.id.btn_garbege:
				AlertDialog.Builder alt_bld = new AlertDialog.Builder(
						ResultActivity.this, AlertDialog.THEME_HOLO_LIGHT);
				alt_bld.setMessage("정말 버리시겠습니까?")
						.setCancelable(false)
						.setPositiveButton("예",
								new DialogInterface.OnClickListener() {
									public void onClick(DialogInterface dialog,
											int id) {
										// Action for 'Yes' Button
										int i = 1;
										
										String key_index = arr.get(position)
												.getKeyindex();
										Log.d("SKY", "key_index :: "
												+ key_index);
										//6번 맞춘걸로 해서 강제 삭제
										update_O(arr.get(position)
												.getKeyindex(), "5");

										Toast.makeText(getApplicationContext(),
												"휴지통에 버렸습니다.",
												Toast.LENGTH_SHORT).show();
										;


										if ((arr.size() - 1) == position) {
											// 마지막 페이지
											Toast.makeText(
													getApplicationContext(),
													"마지막 문제였습니다.",
													Toast.LENGTH_SHORT).show();
											finish();
											return;
										} else {
											//문제 건너뜀 방지용 처리
											if(Type.equals("오엑스"))
												position++;
											
											next_flag = false;
											setInit();
											return;
										}

									}
								})
						.setNegativeButton("아니오",
								new DialogInterface.OnClickListener() {
									public void onClick(DialogInterface dialog,
											int id) {
										// Action for 'NO' Button

									}
								});
				AlertDialog alert = alt_bld.create();
				// Title for AlertDialog
				alert.setTitle("알림");
				// Icon for AlertDialog
				alert.show();

				break;
			case R.id.btn_checkmarkon:

				AlertDialog.Builder alt_bld2 = new AlertDialog.Builder(
						ResultActivity.this, AlertDialog.THEME_HOLO_LIGHT);
				alt_bld2.setMessage("다음에 이어할 수 있도록 현재 위치를 저장합니까?")
						.setCancelable(false)
						.setPositiveButton("예",
								new DialogInterface.OnClickListener() {
									public void onClick(DialogInterface dialog,
											int id) {
										// Action for 'Yes' Button

										check_point_save(arr.get(position));
										Toast.makeText(getApplicationContext(),
												"위치를 저장했습니다.",
												Toast.LENGTH_SHORT).show();
									}
								})
						.setNegativeButton("아니오",
								new DialogInterface.OnClickListener() {
									public void onClick(DialogInterface dialog,
											int id) {
										// Action for 'NO' Button

									}
								});
				AlertDialog alert2 = alt_bld2.create();
				// Title for AlertDialog
				alert2.setTitle("알림");
				// Icon for AlertDialog
				alert2.show();

				break;
			case R.id.btn_checkmarkload:

				AlertDialog.Builder alt_bld3 = new AlertDialog.Builder(
						ResultActivity.this, AlertDialog.THEME_HOLO_LIGHT);
				alt_bld3.setMessage("저장해둔 위치로 이동합니까?")
						.setCancelable(false)
						.setPositiveButton("예",
								new DialogInterface.OnClickListener() {
									public void onClick(DialogInterface dialog,
											int id) {
										// Action for 'Yes' Button

										int temp = position;
										int save_position = check_point_load(arr
												.get(position));
										if (save_position == -1) {
											Toast.makeText(
													getApplicationContext(),
													"기억한 문제가 없습니다.",
													Toast.LENGTH_SHORT).show();
										} else {
											for (position = 0; position <= (arr
													.size() - 1); position++) {
												next_flag = false;

												if (Integer
														.parseInt(arr.get(
																position)
																.getKeyindex()) == save_position) {
													setInit();
													Toast.makeText(
															getApplicationContext(),
															"기억한 문제로 이동했습니다.",
															Toast.LENGTH_SHORT)
															.show();
													return;
												}
												;
											}
											position = temp;
											Toast.makeText(
													getApplicationContext(),
													"저장해둔 문제가 삭제되어 이동할 수 없습니다.",
													Toast.LENGTH_SHORT).show();
										}

									}
								})
						.setNegativeButton("아니오",
								new DialogInterface.OnClickListener() {
									public void onClick(DialogInterface dialog,
											int id) {
										// Action for 'NO' Button

									}
								});
				AlertDialog alert3 = alt_bld3.create();
				// Title for AlertDialog
				alert3.setTitle("알림");
				// Icon for AlertDialog
				alert3.show();

				break;
			}

		}
	};

	private void check_point_save(Tableobj arr_) {
		try {
			// db파일 읽어오기
			SQLiteDatabase db = openOrCreateDatabase("mydb.db",
					Context.MODE_PRIVATE, null);
			// 테이블 없다면 테이블 생성
			String sql = "CREATE TABLE IF NOT EXISTS check_table ("
					+ "level TEXT  NOT NULL," + "type TEXT  NOT NULL,"
					+ "keyindex INTEGER," + "mmode TEXT" + ");";
			Log.i("SKY", sql);
			db.execSQL(sql);

			sql = "DELETE FROM check_table WHERE level='" + arr_.getLevel()
					+ "' AND type='" + arr_.getType() + "' AND mmode='" + Type
					+ "';";
			Log.d("SKY", "sql  : " + sql);
			db.execSQL(sql);

			sql = "INSERT INTO check_table values(";
			sql += "'" + arr_.getLevel() + "',";
			sql += "'" + arr_.getType() + "',";
			sql += arr_.getKeyindex() + ",";
			sql += "'" + Type + "')";
			Log.d("SKY", "sql  : " + sql);
			db.execSQL(sql);

			db.close();
		} catch (SQLException se) {
			// TODO: handle exception
			Log.e("selectData()Error! : ", se.toString());
		}
	}

	private int check_point_load(Tableobj arr_) {

		try {
			// db파일 읽어오기
			SQLiteDatabase db = openOrCreateDatabase("mydb.db",
					Context.MODE_PRIVATE, null);

			String sql = "SELECT * FROM check_table WHERE level='"
					+ arr_.getLevel() + "' AND type='" + arr_.getType()
					+ "' AND mmode='" + Type + "';";
			Log.d("SKY", "sql  : " + sql);
			Cursor cur = db.rawQuery(sql, null);
			// 처음 레코드로 이동
			while (cur.moveToNext()) {
				// 읽은값 출력
				Log.i("SKY", "읽어올 문제 번호 : " + cur.getInt(2) + "");
				return cur.getInt(2);
			}

			cur.close();
			db.close();
		} catch (SQLException se) {
			// TODO: handle exception
			Log.e("selectData()Error! : ", se.toString());
		}
		return -1;
	}
}
