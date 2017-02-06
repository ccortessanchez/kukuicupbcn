package com.ub.tfg.kukuicup.admin.vista;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListAdapter;
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
 * Created by Juanmi on 24/06/2016.
 */
public class NewPlayerActivity extends Activity {
    // Progress Dialog
    private ProgressDialog pDialog;

    JSONParser jsonParser = new JSONParser();
    EditText inputName;
    String pid;
    Spinner teamSpinner;
    ArrayList<HashMap<String, String>> teamList;

    // url to create new product
    private static String url_create_player;

    // url to get all team list
    private static String url_all_teams;

    // JSON Node names
    private static final String TAG_SUCCESS = "success";
    private static final String TAG_TEAMS = "teams";
    private static final String TAG_ID = "id";
    private static final String TAG_NAME = "name";

    // teams JSONArray
    JSONArray teams = null;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        if(getResources().getBoolean(R.bool.tablet)){
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        }
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_player);

        Controller control = new Controller();
        String localhost = control.config.LOCALHOST;

        url_create_player = "http://"+localhost+"/create_player.php";
        url_all_teams = "http://"+localhost+"/get_all_teams.php";

        // Hashmap for Spinner
        teamList = new ArrayList<HashMap<String, String>>();

        // Loading players in Background Thread
        new LoadAllTeams().execute();

        //Spinner
        teamSpinner = (Spinner)findViewById(R.id.teamSpinner);

        // Edit Text
        inputName = (EditText) findViewById(R.id.inputName);
//        inputTeam = (EditText) findViewById(R.id.inputTeam);

        // Create button
        Button btnCreatePlayer = (Button) findViewById(R.id.btnCreatePlayer);

        // button click event
        btnCreatePlayer.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                // creating new product in background thread
                new CreateNewPlayer().execute();
            }
        });

        teamSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
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
     * Background Async Task to Create new product
     * */
    class CreateNewPlayer extends AsyncTask<String,String,String> {

        /**
         * Before starting background thread Show Progress Dialog
         * */
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(NewPlayerActivity.this);
            pDialog.setMessage(getResources().getString(R.string.creatingPlayers));
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            //pDialog.show();
        }

        /**
         * Creating product
         * */
        protected String doInBackground(String... args) {
            String name = inputName.getText().toString();
            //String team = inputTeam.getText().toString();
            String team = pid;

            // Building Parameters
            List<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair("name", name));
            params.add(new BasicNameValuePair("team_id", team));

            // getting JSON Object
            // Note that create product url accepts POST method
            JSONObject json = jsonParser.makeHttpRequest(url_create_player,
                    "POST", params);

            // check log cat fro response
            Log.d("Create Response", json.toString());

            // check for success tag
            try {
                int success = json.getInt(TAG_SUCCESS);

                if (success == 1) {
                    // successfully created product
                    Intent i = new Intent(getApplicationContext(), AllPlayersActivity.class);
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

    /**
     * Background Async Task to Load all teams by making HTTP Request
     * */
    class LoadAllTeams extends AsyncTask<String, String, String> {

        /**
         * Before starting background thread Show Progress Dialog
         * */
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(NewPlayerActivity.this);
            pDialog.setMessage(getResources().getString(R.string.loadTeams));
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
            JSONObject json = jsonParser.makeHttpRequest(url_all_teams, "GET", params);

            // Check your log cat for JSON reponse
            Log.d("All Teams: ", json.toString());

            try {
                // Checking for SUCCESS TAG
                int success = json.getInt(TAG_SUCCESS);

                if (success == 1) {
                    // players found
                    // Getting Array of players
                    teams = json.getJSONArray(TAG_TEAMS);

                    // looping through All players
                    for (int i = 0; i < teams.length(); i++) {
                        JSONObject c = teams.getJSONObject(i);

                        // Storing each json item in variable
                        String id = c.getString(TAG_ID);
                        String name = c.getString(TAG_NAME);

                        // creating new HashMap
                        HashMap<String, String> map = new HashMap<String, String>();

                        // adding each child node to HashMap key => value
                        map.put(TAG_ID, id);
                        map.put(TAG_NAME, name);

                        // adding HashList to ArrayList
                        teamList.add(map);
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
                    SpinnerAdapter adapter = new SimpleAdapter(NewPlayerActivity.this, teamList,
                            R.layout.player_list_item, new String[] { TAG_ID,
                            TAG_NAME},
                            new int[] { R.id.pid, R.id.name });
                    // updating spinner

                    teamSpinner.setAdapter(adapter);
                }
            });

        }

    }
}
