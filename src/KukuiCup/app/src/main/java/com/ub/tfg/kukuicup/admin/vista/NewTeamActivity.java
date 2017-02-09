package com.ub.tfg.kukuicup.admin.vista;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SimpleAdapter;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TextView;

import com.ub.tfg.kukuicup.R;
import com.ub.tfg.kukuicup.controller.Controller;
import com.ub.tfg.kukuicup.model.JSONParser;

import cz.msebera.android.httpclient.NameValuePair;
import cz.msebera.android.httpclient.message.BasicNameValuePair;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Juanmi on 27/06/2016.
 */
public class NewTeamActivity extends Activity {
    // Progress Dialog
    private ProgressDialog pDialog;

    JSONParser jsonParser = new JSONParser();
    EditText inputName;
    Spinner tourSpinner;

    String pid;

    ArrayList<HashMap<String, String>> tournamentList;

    // url to create new team
    private static String url_create_team;
    private static String url_all_tournaments;

    JSONArray tournaments = null;

    private static final String TAG_TEAM = "team";
    private static final String TAG_ID = "id";
    private static final String TAG_NAME = "name";
    private static final String TAG_TOURNAMENT = "tournament_id";
    private static final String TAG_TOURNAMENTS = "tournaments";
    private static final String TAG_SUCCESS = "success";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        if(getResources().getBoolean(R.bool.tablet)){
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        }
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_team);

        Controller control = new Controller();
        String localhost = control.config.LOCALHOST;

        url_create_team = "http://"+localhost+"/create_team.php";
        url_all_tournaments = "http://"+localhost+"/get_all_tournaments.php";

        // Hashmap for Spinner
        tournamentList = new ArrayList<HashMap<String, String>>();

        // Loading players in Background Thread
        new LoadAllTournaments().execute();

        // Edit Text
        inputName = (EditText) findViewById(R.id.inputName);

        tourSpinner = (Spinner)findViewById(R.id.tourSpinner);

        // Create button
        Button btnCreateTeam = (Button) findViewById(R.id.btnCreateTeam);

        // button click event
        btnCreateTeam.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                // creating new team in background thread
                new CreateNewTeam().execute();
            }
        });

        tourSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                //((TextView) parent.getChildAt(0)).setTextColor(Color.BLACK);
                pid = ((TextView) view.findViewById(R.id.pid)).getText()
                        .toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    /**
     * Background Async Task to Create new team
     * */
    class CreateNewTeam extends AsyncTask<String,String,String> {

        /**
         * Before starting background thread Show Progress Dialog
         * */
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(NewTeamActivity.this);
            pDialog.setMessage(getResources().getString(R.string.creatingTeam));
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            //pDialog.show();
        }

        /**
         * Creating team
         * */
        protected String doInBackground(String... args) {
            String name = inputName.getText().toString();
            String tournament = pid;

            // Building Parameters
            List<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair("name", name));
            params.add(new BasicNameValuePair("tournament_id", tournament));

            // getting JSON Object
            // Note that create team url accepts POST method
            JSONObject json = jsonParser.makeHttpRequest(url_create_team,
                    "POST", params);

            // check log cat fro response
            Log.d("Create Response", json.toString());

            // check for success tag
            try {
                int success = json.getInt(TAG_SUCCESS);

                if (success == 1) {
                    // successfully created team
                    Intent i = new Intent(getApplicationContext(), MenuAdminActivity.class);
                    startActivity(i);

                    // closing this screen
                    finish();
                } else {
                    // failed to create team
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

    class LoadAllTournaments extends AsyncTask<String, String, String> {

        /**
         * Before starting background thread Show Progress Dialog
         * */
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(NewTeamActivity.this);
            pDialog.setMessage("Loading teams. Please wait...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
            //pDialog.show();
        }

        /**
         * getting All teams from url
         * */
        protected String doInBackground(String... args) {
            // Building Parameters
            List<NameValuePair> params = new ArrayList<NameValuePair>();
            // getting JSON string from URL
            JSONObject json = jsonParser.makeHttpRequest(url_all_tournaments, "GET", params);

            // Check your log cat for JSON reponse
            Log.d("All Tournaments: ", json.toString());

            try {
                // Checking for SUCCESS TAG
                int success = json.getInt(TAG_SUCCESS);

                if (success == 1) {
                    // players found
                    // Getting Array of players
                    tournaments = json.getJSONArray(TAG_TOURNAMENTS);

                    // looping through All players
                    for (int i = 0; i < tournaments.length(); i++) {
                        JSONObject c = tournaments.getJSONObject(i);

                        // Storing each json item in variable
                        String id = c.getString(TAG_ID);
                        String name = c.getString(TAG_NAME);

                        // creating new HashMap
                        HashMap<String, String> map = new HashMap<String, String>();

                        // adding each child node to HashMap key => value
                        map.put(TAG_ID, id);
                        map.put(TAG_NAME, name);

                        // adding HashList to ArrayList
                        tournamentList.add(map);
                    }
                } else {
                    // no teams found
                    // Launch Add New team Activity
                    Intent i = new Intent(getApplicationContext(),
                            NewTeamActivity.class);
                    // Closing all previous activities
                    i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(i);
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
            // dismiss the dialog after getting all players
            pDialog.dismiss();
            // updating UI from Background Thread
            runOnUiThread(new Runnable() {
                public void run() {
                    /**
                     * Updating parsed JSON data into Spinner
                     * */
                    SpinnerAdapter adapter = new SimpleAdapter(NewTeamActivity.this, tournamentList,
                            R.layout.player_list_item, new String[] { TAG_ID,
                            TAG_NAME},
                            new int[] { R.id.pid, R.id.name });
                    // updating spinner

//                    String[] names = teamList
//                    ArrayAdapter adapter = new ArrayAdapter<>(NewPlayerActivity.this, android.R.layout.simple_spinner_dropdown_item, )
                    tourSpinner.setAdapter(adapter);
                }
            });

        }

    }
}
