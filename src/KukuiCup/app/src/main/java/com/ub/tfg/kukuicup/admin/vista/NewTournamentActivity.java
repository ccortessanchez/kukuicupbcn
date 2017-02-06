package com.ub.tfg.kukuicup.admin.vista;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.ub.tfg.kukuicup.R;
import com.ub.tfg.kukuicup.controller.Controller;
import com.ub.tfg.kukuicup.controller.SessionManager;
import com.ub.tfg.kukuicup.model.JSONParser;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import cz.msebera.android.httpclient.NameValuePair;
import cz.msebera.android.httpclient.message.BasicNameValuePair;

/**
 * Created by Juanmi on 27/06/2016.
 */
public class NewTournamentActivity extends Activity {
    // Progress Dialog
    private ProgressDialog pDialog;

    JSONParser jsonParser = new JSONParser();
    EditText inputInitDate;
    EditText inputEndDate;
    private AlertDialog alertDialog;

    int dayCounter;
    boolean max_tour = false;
    SessionManager session;

    // url to create new team
    private static String url_create_tournament;

    // JSON Node names
    private static final String TAG_SUCCESS = "success";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        if(getResources().getBoolean(R.bool.tablet)){
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        }
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_tournament);

        Controller control = new Controller();
        String localhost = control.config.LOCALHOST;
        session = new SessionManager(getApplicationContext());

        url_create_tournament = "http://"+localhost+"/create_tournament.php";

        // Edit Text
        inputInitDate = (EditText) findViewById(R.id.inputInitDate);
        inputEndDate = (EditText) findViewById(R.id.inputEndDate);


        // Create button
        Button btnCreateTournament = (Button) findViewById(R.id.btnCreateTournament);

        // button click event
        btnCreateTournament.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                // creating new team in background thread
                new CreateNewTournament().execute();
            }
        });
    }

    /**
     * Background Async Task to Create new team
     * */
    class CreateNewTournament extends AsyncTask<String,String,String> {

        /**
         * Before starting background thread Show Progress Dialog
         * */
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(NewTournamentActivity.this);
            pDialog.setMessage(getResources().getString(R.string.creatingTeam));
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            //pDialog.show();
        }

        /**
         * Creating team
         * */
        protected String doInBackground(String... args) {
            String init_date = inputInitDate.getText().toString();
            String finish_date = inputEndDate.getText().toString();

            // Building Parameters
            List<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair("init_date", init_date));
            params.add(new BasicNameValuePair("finish_date", finish_date));
            params.add(new BasicNameValuePair("duration", "21"));

            // getting JSON Object
            // Note that create team url accepts POST method
            JSONObject json = jsonParser.makeHttpRequest(url_create_tournament,
                    "POST", params);

            // check log cat fro response
            Log.d("Create Response", json.toString());

            // check for success tag
            try {
                int success = json.getInt(TAG_SUCCESS);

                if (success == 1) {
                    // successfully created team
                    session.setEndDay(finish_date);
                    dayCounter = 21;
                    Intent i = new Intent(getApplicationContext(), AllTournamentsActivity.class);
                    startActivity(i);

                    // closing this screen
                    finish();
                } else {
                    Intent i = new Intent(getApplicationContext(), MenuTournamentActivity.class);
                    startActivity(i);

                    // closing this screen
                    finish();
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
        Intent i = new Intent(getApplicationContext(), MenuTournamentActivity.class);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(i);
    }
}
