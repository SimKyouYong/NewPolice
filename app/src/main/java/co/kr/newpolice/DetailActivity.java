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

import com.kakao.adfit.publisher.AdView;
import com.kakao.adfit.publisher.AdView.AnimationType;
import com.kakao.adfit.publisher.AdView.OnAdClickedListener;
import com.kakao.adfit.publisher.AdView.OnAdClosedListener;
import com.kakao.adfit.publisher.AdView.OnAdFailedListener;
import com.kakao.adfit.publisher.AdView.OnAdLoadedListener;
import com.kakao.adfit.publisher.AdView.OnAdWillLoadListener;
import com.kakao.adfit.publisher.impl.AdError;

import co.kr.newpolice.common.Check_Preferences;

import static com.google.ads.AdRequest.LOGTAG;

public class DetailActivity extends Activity {
	String Tag = "";
	private AdView adView = null;
	private LinearLayout adWrapper = null;

	TextView title;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_detail);

		Tag = getIntent().getStringExtra("tag");
		adWrapper = (LinearLayout) findViewById(R.id.adWrapper);

		title = (TextView)findViewById(R.id.title_dt);

		title.setText("" + Tag);

		findViewById(R.id.btn1).setOnClickListener(btnListener); 
		findViewById(R.id.btn2).setOnClickListener(btnListener); 
		findViewById(R.id.btn3).setOnClickListener(btnListener);
		findViewById(R.id.btn4).setOnClickListener(btnListener);
		findViewById(R.id.btn5).setOnClickListener(btnListener);
		initAdam();
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
	private void initAdam() {
		// AdFit sdk 초기화 시작
		adView = (AdView) findViewById(R.id.adview);
		adView.setRequestInterval(5);

		// 광고 클릭시 실행할 리스너
		adView.setOnAdClickedListener(new OnAdClickedListener() {
			public void OnAdClicked() {
				Log.e(LOGTAG, "광고를 클릭했습니다.");
			}
		});

		// 광고 내려받기 실패했을 경우에 실행할 리스너
		adView.setOnAdFailedListener(new OnAdFailedListener() {
			public void OnAdFailed(AdError arg0, String arg1) {
				adWrapper.setVisibility(View.GONE);
				Log.e(LOGTAG, arg1);
			}
		});

		// 광고를 정상적으로 내려받았을 경우에 실행할 리스너
		adView.setOnAdLoadedListener(new OnAdLoadedListener() {
			public void OnAdLoaded() {
				adWrapper.setVisibility(View.VISIBLE);
				Log.e(LOGTAG, "광고가 정상적으로 로딩되었습니다.");
			}
		});

		// 광고를 불러올때 실행할 리스너
		adView.setOnAdWillLoadListener(new OnAdWillLoadListener() {
			public void OnAdWillLoad(String arg1) {
				Log.e(LOGTAG, "광고를 불러옵니다. : " + arg1);
			}
		});

		// 광고를 닫았을때 실행할 리스너
		adView.setOnAdClosedListener(new OnAdClosedListener() {
			public void OnAdClosed() {
				Log.e(LOGTAG, "광고를 닫았습니다.");
			}
		});

		// 할당 받은 clientId 설정
		adView.setClientId("DAN-1ib0yvnhuimur");
		adView.setRequestInterval(12);
		// Animation 효과 : 기본 값은 AnimationType.NONE
		adView.setAnimationType(AnimationType.FLIP_HORIZONTAL);
		adView.setVisibility(View.VISIBLE);
	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
	}
}
