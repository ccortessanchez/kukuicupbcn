package com.ub.tfg.kukuicup.admin.vista;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.ub.tfg.kukuicup.R;
import com.ub.tfg.kukuicup.controller.Controller;
import com.ub.tfg.kukuicup.controller.SQLiteHandler;
import com.ub.tfg.kukuicup.controller.SessionManager;
import com.ub.tfg.kukuicup.model.JSONParser;
import com.ub.tfg.kukuicup.vista.*;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

import cz.msebera.android.httpclient.NameValuePair;
import cz.msebera.android.httpclient.message.BasicNameValuePair;

public class MenuTournamentActivity extends Activity {


	private ProgressDialog pDialog;

	Button btnAdminTour;
	Button btnViewtournaments;
	Button btnStart;
	Button btnRankingTeams;

	JSONParser jsonParser = new JSONParser();

	SessionManager session;

	boolean tournament_exist = false;

	private static String url_create_tournament;
	private String url_all_tournaments;

	ArrayList<HashMap<String, String>> tournamentsList;


	// JSON Node names
	private static final String TAG_SUCCESS = "success";
	private static final String TAG_TOURNAMENTS = "tournaments";
	private static final String TAG_ID = "id";
	private static final String TAG_INIT_DATE = "init_date";
	private static final String TAG_FINISH_DATE = "finish_date";
	private static final String TAG_NAME = "name";

	Calendar initData;
	Calendar endData;
	int dayCounter;
	TextView endDateText;
	TextView initDateText;

	private SQLiteHandler db;
	
	public void onCreate(Bundle savedInstanceState) {
		if(getResources().getBoolean(R.bool.tablet)){
			setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
		}
		super.onCreate(savedInstanceState);
		setContentView(R.layout.menu_tournament);

		Controller control = new Controller();
		String localhost = control.config.LOCALHOST;
		session = new SessionManager(getApplicationContext());

		if (session.getEndDate().equals(null)) {
			tournament_exist = false;
		}
		else {
			tournament_exist = true;
		}


		//SQLite database handler
		db = new SQLiteHandler(getApplicationContext());


		url_create_tournament = "http://"+localhost+"/create_tournament.php";
		url_all_tournaments = "http://"+localhost+"/get_all_tournaments.php";

		tournamentsList = new ArrayList<HashMap<String, String>>();

		// Loading players in Background Thread

		btnAdminTour = (Button)findViewById(R.id.btnAdminTour);
		btnViewtournaments = (Button) findViewById(R.id.btnViewTours);
		btnStart = (Button) findViewById(R.id.startTournament);
		btnRankingTeams = (Button) findViewById(R.id.btnRankingTeams);

		if(!tournament_exist) {
			btnAdminTour.setVisibility(View.INVISIBLE);
			btnViewtournaments.setVisibility(View.INVISIBLE);
			btnRankingTeams.setVisibility(View.INVISIBLE);
		}
		//initDateText = (TextView)findViewById(R.id.initDate);
		//endDateText = (TextView)findViewById(R.id.endDate);


		btnAdminTour.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View view) {
				// Launching All players Activity
				Intent i = new Intent(getApplicationContext(), MenuAdminActivity.class);
				startActivity(i);

			}
		});
		// view players click event
		btnViewtournaments.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View view) {
				// Launching All players Activity
				Intent i = new Intent(getApplicationContext(), AllTournamentsActivity.class);
				startActivity(i);

			}
		});


		btnStart.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View view) {

				Intent i = new Intent(getApplicationContext(), NewTournamentActivity.class);
				startActivity(i);


				//initDateText.setText(formattedInit);
				//endDateText.setText(formattedEnd);


			}
		});

		btnRankingTeams.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View view) {

				Intent i = new Intent(getApplicationContext(),RankingTeamsActivity.class);
				startActivity(i);


				//initDateText.setText(formattedInit);
				//endDateText.setText(formattedEnd);


			}
		});
	}

	class CreateNewTournament extends AsyncTask<String,String,String> {

		/**
		 * Before starting background thread Show Progress Dialog
		 * */
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			List<NameValuePair> params = new ArrayList<NameValuePair>();
			// getting JSON string from URL
			JSONObject json = jsonParser.makeHttpRequest(url_all_tournaments, "GET", params);

			// Check your log cat for JSON reponse
			Log.d("All Tournaments: ", json.toString());

			try {
				// Checking for SUCCESS TAG
				int success = json.getInt(TAG_SUCCESS);

				if (success == 1) {
					tournament_exist = true;
				} else {
					tournament_exist = false;
				}
			} catch (JSONException e) {
				e.printStackTrace();
			}
			//pDialog.show();
		}

		/**
		 * Creating product
		 * */
		protected String doInBackground(String... args) {
			//String team = inputTeam.getText().toString();
			SimpleDateFormat format1 = new SimpleDateFormat("yyyy-MM-dd");
			initData = Calendar.getInstance();
			endData = Calendar.getInstance();

			initData.add(Calendar.DATE,1);
			endData.add(Calendar.DAY_OF_YEAR, 21);
			endData.add(Calendar.DATE,1);

			dayCounter = 21;

			String formattedInit = format1.format(initData.getTime()).toString();
			//System.out.println(initData);
			String formattedEnd = format1.format(endData.getTime()).toString();
			//System.out.println(endData);
			//String formattedInit = "2017-02-5";
			//String formattedEnd = "2017-02-25";
			//initDateText.setText(formattedInit);
			//endDateText.setText(formattedEnd);

			session.setEndDay(formattedEnd);

			// Building Parameters
			List<NameValuePair> params = new ArrayList<NameValuePair>();
			params.add(new BasicNameValuePair("init_date", formattedInit));
			params.add(new BasicNameValuePair("finish_date", formattedEnd));
			params.add(new BasicNameValuePair("duration", "21"));

			// getting JSON Object
			// Note that create product url accepts POST method
			JSONObject json = jsonParser.makeHttpRequest(url_create_tournament,
					"POST", params);

			// check log cat fro response
			Log.d("Create Response", json.toString());

			// check for success tag
			try {
				int success = json.getInt(TAG_SUCCESS);

				if (success == 1) {
					// successfully created product
					Intent i = new Intent(getApplicationContext(), AllTournamentsActivity.class);
					startActivity(i);

					// closing this screen
					finish();
				} else {
					// failed to create product
				}
			} catch (JSONException e) {
				e.printStackTrace();
			}

			return null;
		}

		/**
		 * After completing background task Dismiss the progress dialog
		 * **/
		protected void onPostExecute(String file_url) {
			// dismiss the dialog once done
			pDialog.dismiss();
		}

	}



		@Override
	public void onBackPressed() {
		Intent i = new Intent(getApplicationContext(), LoginActivity.class);
		i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		startActivity(i);
	}

}
