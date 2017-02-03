package com.ub.tfg.kukuicup.admin.vista;

import com.ub.tfg.kukuicup.R;
import com.ub.tfg.kukuicup.controller.SQLiteHandler;
import com.ub.tfg.kukuicup.controller.SessionManager;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class MenuAdminActivity extends Activity {


	Button btnViewteams;
	Button btnViewplayers;
	Button btnViewregister;
	Button btnNewplayer;
	Button btnNewteam;
	//Button btnStart;

	SessionManager session;

	Calendar initData;
	Calendar endData;
	int dayCounter;
	TextView endDateText;
	TextView initDateText;

	private SQLiteHandler db;
	
	public void onCreate(Bundle savedInstanceState) {
		if (getResources().getBoolean(R.bool.tablet)) {
			setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
		}
		super.onCreate(savedInstanceState);
		setContentView(R.layout.menu_admin);

		session = new SessionManager(getApplicationContext());

		//SQLite database handler
		db = new SQLiteHandler(getApplicationContext());

		btnViewteams = (Button) findViewById(R.id.btnViewTeams);
		btnViewplayers = (Button) findViewById(R.id.btnViewPlayers);
		btnViewregister = (Button) findViewById(R.id.btnViewRegister);
		btnNewplayer = (Button) findViewById(R.id.btnCreatePlayer);
		btnNewteam = (Button) findViewById(R.id.btnCreateTeam);
		//btnStart = (Button) findViewById(R.id.startTournament);
		//initDateText = (TextView)findViewById(R.id.initDate);
		//endDateText = (TextView)findViewById(R.id.endDate);


		btnViewteams.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View view) {
				// Launching All players Activity
				Intent i = new Intent(getApplicationContext(), AllTeamsActivity.class);
				startActivity(i);

			}
		});
		// view players click event
		btnViewplayers.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View view) {
				// Launching All players Activity
				Intent i = new Intent(getApplicationContext(), AllPlayersActivity.class);
				startActivity(i);

			}
		});

		btnViewregister.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View view) {
				// Launching All players Activity
				Intent i = new Intent(getApplicationContext(), AllRegisterActivity.class);
				startActivity(i);

			}
		});

		// new players click event
		btnNewplayer.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View view) {
				// Launching create new player activity
				Intent i = new Intent(getApplicationContext(), NewPlayerActivity.class);
				startActivity(i);

			}
		});

		// new team click event
		btnNewteam.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View view) {
				// Launching create new player activity
				Intent i = new Intent(getApplicationContext(), NewTeamActivity.class);
				startActivity(i);

			}
		});
	}

	@Override
	public void onBackPressed() {
		Intent i = new Intent(getApplicationContext(), MenuTournamentActivity.class);
		i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		startActivity(i);
	}

}
