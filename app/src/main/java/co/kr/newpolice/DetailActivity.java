package co.kr.newpolice;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import co.kr.newpolice.common.Check_Preferences;

public class DetailActivity extends Activity {
	String Tag = "";

	TextView title;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_detail);

		Tag = getIntent().getStringExtra("tag");

		title = (TextView)findViewById(R.id.title_dt);

		title.setText("" + Tag);

		findViewById(R.id.btn1).setOnClickListener(btnListener); 
		findViewById(R.id.btn2).setOnClickListener(btnListener); 
		findViewById(R.id.btn3).setOnClickListener(btnListener);
		findViewById(R.id.btn4).setOnClickListener(btnListener);
		findViewById(R.id.btn5).setOnClickListener(btnListener);

	}
	//버튼 리스너 구현 부분 
	View.OnClickListener btnListener = new View.OnClickListener() {
		public void onClick(View v) {
			Intent it = new Intent(DetailActivity.this, ResultActivity.class);
			it.putExtra("tag",Tag);
			switch (v.getId()) {
			case R.id.btn1:
				it.putExtra("type","익히기");
				startActivity(it);
				break;
			case R.id.btn2:
				it.putExtra("type","오엑스");
				startActivity(it);
				break;
			case R.id.btn3:
				it.putExtra("type","오답노트");
				startActivity(it);
				break;
			case R.id.btn4:
				it.putExtra("type","휴지통");
				startActivity(it);
				break;
			case R.id.btn5:
				//초기화
				Check_Preferences.setAppPreferences(DetailActivity.this, "position1", ""+0);
				Check_Preferences.setAppPreferences(DetailActivity.this, "position2", ""+0);
				Check_Preferences.setAppPreferences(DetailActivity.this, "position3", ""+0);
				Check_Preferences.setAppPreferences(DetailActivity.this, "position4", ""+0);
				
				Toast.makeText(getApplicationContext(), "모두 초기화 되었습니다.", 0).show();

				break;
			}
			

		}
	};

}
