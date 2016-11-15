package com.ub.tfg.kukuicup.vista;

import com.google.android.youtube.player.YouTubeBaseActivity;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayer.PlayerStyle;
import com.google.android.youtube.player.YouTubePlayerView;
import com.ub.tfg.kukuicup.R;
import com.ub.tfg.kukuicup.controller.Config;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

public class VideoActivity extends YouTubeBaseActivity implements
		YouTubePlayer.OnInitializedListener {

//public class VideoActivity extends YouTubeBaseActivity {
//public class VideoActivity extends Activity {

	private static final int RECOVERY_DIALOG_REQUEST = 1;

	// YouTube player view
	private YouTubePlayerView youTubeView;
	
	//private YouTubePlayer.OnInitializedListener onInitializedListener;

	private ImageButton playBtn;
	private Button toQuiz;
	private String address;
	private TextView videoname;
	private TextView reward;

	public int levelId;
	public int videoId;
	public int pointsObt;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);

		setContentView(R.layout.video);

		Bundle extras = getIntent().getExtras();

		levelId = extras.getInt("levelId");
		videoId = extras.getInt("videoId");
		pointsObt = 0;

		address = "https://www.youtube.com/watch?v=PD7a1EWjsTc";
		toQuiz = (Button)findViewById(R.id.primaryBtn);
		playBtn = (ImageButton)findViewById(R.id.playBtn);
		reward = (TextView)findViewById(R.id.reward);
		videoname = (TextView)findViewById(R.id.videoName);

		if(levelId==1 && videoId == 0){
			videoname.setText("Power and Energy");
			reward.setText("+10");
		}

		if(levelId==2 && videoId == 1){
            //energia eolica
			videoname.setText("");
			reward.setText("+10");
		}

        if(levelId==2 && videoId == 2){
            //beneficios LED
            videoname.setText("");
            reward.setText("+10");
        }

        if(levelId==3 && videoId == 3){
            //energia limpia
            videoname.setText("");
            reward.setText("+10");
        }

        if(levelId==3 && videoId == 4){
            //energia solar
            videoname.setText("");
            reward.setText("+10");
        }

		playBtn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				toQuiz.setEnabled(true);
				goToLink(address);
				pointsObt+=10;

			}
		});

		toQuiz.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(VideoActivity.this, FormActivity.class);
				intent.putExtra("levelId", levelId);
				intent.putExtra("formId", videoId);
				intent.putExtra("points", pointsObt);
				startActivity(intent);
			}
		});



//		youTubeView = (YouTubePlayerView) findViewById(R.id.youtube_view);

		// Initializing video player with developer key
//		youTubeView.initialize(Config.DEVELOPER_KEY, this);
		//onInitializedListener = new YouTubePlayer.OnInitializedListener() {
			
//			public void onInitializationSuccess(Provider arg0, YouTubePlayer arg1,
//					boolean arg2) {
//				arg1.loadVideo("a4NT5iBFuZs");
//			}
//
//			public void onInitializationFailure(Provider arg0,
//					YouTubeInitializationResult arg1) {
//
//			}
//		};

	}

	public void onInitializationFailure(YouTubePlayer.Provider provider,
			YouTubeInitializationResult errorReason) {
		if (errorReason.isUserRecoverableError()) {
			errorReason.getErrorDialog(this, RECOVERY_DIALOG_REQUEST).show();
		} else {
			String errorMessage = String.format(
					getString(R.string.error_player), errorReason.toString());
			Toast.makeText(this, errorMessage, Toast.LENGTH_LONG).show();
		}
	}

	public void onInitializationSuccess(YouTubePlayer.Provider provider,
			YouTubePlayer player, boolean wasRestored) {
		if (!wasRestored) {

			// loadVideo() will auto play video
			// Use cueVideo() method, if you don't want to play it automatically
			player.loadVideo(Config.YOUTUBE_VIDEO_CODE);

			// Hiding player controls
			player.setPlayerStyle(PlayerStyle.CHROMELESS);
		}
	}

	public void goToLink(String link) {
		Uri uri = Uri.parse(link);
		Intent intentNav = new Intent(Intent.ACTION_VIEW,uri);
		startActivity(intentNav);
	}
//
//	@Override
//	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//		if (requestCode == RECOVERY_DIALOG_REQUEST) {
//			//Retry initialization if user performed a recovery action
//			getYouTubePlayerProvider().initialize(Config.DEVELOPER_KEY, this);
//		}
//	}

//	private YouTubePlayer.Provider getYouTubePlayerProvider() {
//		return (YouTubePlayerView) findViewById(R.id.youtube_view);
//	}

}