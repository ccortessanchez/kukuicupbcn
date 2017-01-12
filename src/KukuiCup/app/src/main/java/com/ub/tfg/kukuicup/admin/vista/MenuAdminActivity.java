package com.ub.tfg.kukuicup.admin.vista;

import com.ub.tfg.kukuicup.R;
import com.ub.tfg.kukuicup.controller.SQLiteHandler;
import com.ub.tfg.kukuicup.controller.SessionManager;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class MenuAdminActivity extends Activity {
	

	Button btnViewplayers;
	Button btnNewplayer;
	Button btnNewteam;
	Button btnStart;

	SessionManager session;

	Calendar initData;
	Calendar endData;
	int dayCounter;
	TextView endDateText;
	TextView initDateText;

	private SQLiteHandler db;
	
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.menu_admin);

		session = new SessionManager(getApplicationContext());

		//SQLite database handler
		db = new SQLiteHandler(getApplicationContext());

		btnViewplayers = (Button) findViewById(R.id.btnViewPlayers);
		btnNewplayer = (Button) findViewById(R.id.btnCreatePlayer);
		btnNewteam = (Button) findViewById(R.id.btnCreateTeam);
		btnStart = (Button) findViewById(R.id.startTournament);
		initDateText = (TextView)findViewById(R.id.initDate);
		endDateText = (TextView)findViewById(R.id.endDate);

		// view players click event
		btnViewplayers.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View view) {
				// Launching All players Activity
				Intent i = new Intent(getApplicationContext(), AllPlayersActivity.class);
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

		btnStart.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View view) {
				SimpleDateFormat format1 = new SimpleDateFormat("dd-MM-yyyy");
				initData = Calendar.getInstance();
				endData = Calendar.getInstance();

				initData.add(Calendar.DATE,1);
				endData.add(Calendar.DAY_OF_YEAR, 21);
				endData.add(Calendar.DATE,1);

				dayCounter = 21;

				String formattedInit = format1.format(initData.getTime());
				String formattedEnd = format1.format(endData.getTime());
				initDateText.setText(formattedInit);
				initDateText.setTextColor(Color.BLACK);
				endDateText.setText(formattedEnd);
				endDateText.setTextColor(Color.BLACK);

				session.setEndDay(formattedEnd);
			}
		});
	}

}
