package co.kr.newpolice;

import android.app.Activity;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Configuration;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import com.kakao.adfit.publisher.AdView;
import com.kakao.adfit.publisher.AdView.AnimationType;
import com.kakao.adfit.publisher.AdView.OnAdClickedListener;
import com.kakao.adfit.publisher.AdView.OnAdClosedListener;
import com.kakao.adfit.publisher.AdView.OnAdFailedListener;
import com.kakao.adfit.publisher.AdView.OnAdLoadedListener;
import com.kakao.adfit.publisher.AdView.OnAdWillLoadListener;
import com.kakao.adfit.publisher.impl.AdError;

import co.kr.newpolice.common.Check_Preferences;

public class SlideNotActivity extends Activity {
	private LinearLayout adWrapper = null;
	Button screenlock;
	private static final String LOGTAG = "SKY";
	private AdView adView = null;

	private ScreenReceiver restartService;

	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_slidenot);

		adWrapper = (LinearLayout) findViewById(R.id.adWrapper);
		screenlock = (Button) findViewById(R.id.btn5);
		initAdam();

		if (Check_Preferences.getAppPreferencesboolean(SlideNotActivity.this, "ScreenLock")) {
			//on
			screenlock.setBackgroundResource(R.mipmap.policedream_mainbtn_06_0n);
			Intent intent = new Intent(SlideNotActivity.this, ScreenService.class);

			SlideNotActivity.this.startService(intent);	
			//리스타트 서비스 생성
			restartService = new ScreenReceiver();
			intent = new Intent(SlideNotActivity.this, ScreenReceiver.class);


			IntentFilter intentFilter = new IntentFilter("co.kr.newpolice.ScreenService");
			//브로드 캐스트에 등록
			registerReceiver(restartService,intentFilter);
			// 서비스 시작
			startService(intent);
		}else{
			//off
			screenlock.setBackgroundResource(R.mipmap.policedream_mainbtn_06_0ff);
		}
		findViewById(R.id.btn1).setOnClickListener(btnListener); 
		findViewById(R.id.btn2).setOnClickListener(btnListener); 
		findViewById(R.id.btn3).setOnClickListener(btnListener); 
		findViewById(R.id.btn4).setOnClickListener(btnListener); 
		findViewById(R.id.btn5).setOnClickListener(btnListener); 
	}
	@Override
	public void onDestroy() {
		super.onDestroy();

		if ( adView != null ) {
			adView.destroy();
			adView = null;
		}
	}
	//버튼 리스너 구현 부분 
	View.OnClickListener btnListener = new View.OnClickListener() {
		@SuppressWarnings("deprecation")
		public void onClick(View v) {
			Intent it = new Intent(SlideNotActivity.this, DetailActivity.class);
			switch (v.getId()) {
			case R.id.btn1:
				it.putExtra("tag", "형법");
				startActivity(it);
				break;
			case R.id.btn2:
				it.putExtra("tag", "형소법");
				startActivity(it);
				break;
			case R.id.btn3:
				it.putExtra("tag", "경찰학개론");
				startActivity(it);
				break;
			case R.id.btn4:
				/*
				Intent it3 = new Intent(Intent.ACTION_SEND);
				it.setType("plain/text");
				// 수신인 주소 - tos배열의 값을 늘릴 경우 다수의 수신자에게 발송됨
				String[] tos = { "pongjjun@gmail.com" };
				it3.putExtra(Intent.EXTRA_EMAIL, tos);
				it3.putExtra(Intent.EXTRA_SUBJECT, "[문제추가 요청]" + "문제 추가 요청 드립니다.");
				it3.putExtra(Intent.EXTRA_TEXT, "파일을 첨부해주세요.(Execel 형식으로 정리해서 주시면 감사합니다.");
				startActivity(Intent.createChooser(it3, "Choose Email Client"));
				*/
				break;
			case R.id.btn5:
				Log.e("SKY", "--btn5--");
				//스크린락 
				Intent intent = null;

				if (!Check_Preferences.getAppPreferencesboolean(SlideNotActivity.this, "ScreenLock")) {
					//on
					Log.e("SKY", "--on--");
					screenlock.setBackgroundResource(R.mipmap.policedream_mainbtn_06_0n);
					//리스타트 서비스 생성
					intent = new Intent(SlideNotActivity.this, ScreenService.class);
					startService(intent);
					Check_Preferences.setAppPreferences(SlideNotActivity.this, "ScreenLock", true);
				}else{
					//off
					Log.e("SKY", "--off--");
					intent = new Intent(SlideNotActivity.this, ScreenReceiver.class);
					screenlock.setBackgroundResource(R.mipmap.policedream_mainbtn_06_0ff);
					//stopService(intent);

					intent = new Intent(SlideNotActivity.this, ScreenService.class);
					stopService(intent);
					Check_Preferences.setAppPreferences(SlideNotActivity.this, "ScreenLock", false);
				}
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
