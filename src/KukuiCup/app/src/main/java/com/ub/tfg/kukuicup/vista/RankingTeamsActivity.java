package com.ub.tfg.kukuicup.vista;

import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.ub.tfg.kukuicup.R;
import com.ub.tfg.kukuicup.admin.vista.EditPlayerActivity;
import com.ub.tfg.kukuicup.admin.vista.NewPlayerActivity;
import com.ub.tfg.kukuicup.controller.Controller;
import com.ub.tfg.kukuicup.controller.SessionManager;
import com.ub.tfg.kukuicup.model.JSONParser;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import cz.msebera.android.httpclient.NameValuePair;

/**
 * Created by Carlos
 */
public class RankingTeamsActivity extends ListActivity{
    // Progress Dialog
    private ProgressDialog pDialog;
    private SessionManager session;

    String myteamID;

    // Creating JSON Parser object
    JSONParser jParser = new JSONParser();

    ArrayList<HashMap<String, String>> teamsList;

    // url to get all players list
    private String url_all_teams;


    // JSON Node names
    private static final String TAG_SUCCESS = "success";
    private static final String TAG_TEAMS = "teams";
    private static final String TAG_ID = "id";
    private static final String TAG_NAME = "name";
    private static final String TAG_POINTS = "points";

    // players JSONArray
    JSONArray teams = null;

    @Override
    public void onCreate(Bundle savedInstanceState) {

        if(getResources().getBoolean(R.bool.tablet)){
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        }
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ranking_teams);

        Controller control = new Controller();
        session = new SessionManager(getApplicationContext());
        myteamID = session.getTeam();
        String localhost = control.config.LOCALHOST;
        url_all_teams = "http://"+localhost+"/get_team_ranking.php";

        // Hashmap for ListView
        teamsList = new ArrayList<HashMap<String, String>>();

        // Loading players in Background Thread
        new LoadAllTeams().execute();

        // Get listview
        ListView lv = getListView();

    }


    /**
     * Background Async Task to Load all player by making HTTP Request
     * */
    class LoadAllTeams extends AsyncTask<String, String, String> {

        /**
         * Before starting background thread Show Progress Dialog
         * */
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(RankingTeamsActivity.this);
            pDialog.setMessage(getResources().getString(R.string.loadTeams));
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
            pDialog.show();
        }

        /**
         * getting All players from url
         * */
        protected String doInBackground(String... args) {
            // Building Parameters
            List<NameValuePair> params = new ArrayList<NameValuePair>();
            // getting JSON string from URL
            JSONObject json = jParser.makeHttpRequest(url_all_teams, "GET", params);

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
                        int rank = i + 1;
                        String id = c.getString(TAG_ID);
                        String name = c.getString(TAG_NAME);
                        String points = c.getString(TAG_POINTS);
                        String ranked = "";
                        if (id.equals(myteamID)) {
                            ranked = rank + " " + name + ": " + points + "  " + getResources().getString(R.string.yourTeamRkg);
                        }
                        else {
                            ranked = rank + " " + name + ": " + points;
                        }
                        // creating new HashMap
                        HashMap<String, String> map = new HashMap<String, String>();

                        // adding each child node to HashMap key => value
                        map.put(TAG_ID, id);
                        map.put(TAG_NAME, ranked);

                        // adding HashList to ArrayList
                        teamsList.add(map);
                    }
                } else {
                    // no players found
                    // Launch Add New player Activity
                    Intent i = new Intent(getApplicationContext(),
                            NewPlayerActivity.class);
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
                     * Updating parsed JSON data into ListView
                     * */
                    ListAdapter adapter = new SimpleAdapter(
                            RankingTeamsActivity.this, teamsList,
                            R.layout.team_list_item, new String[] { TAG_ID,
                            TAG_NAME},
                            new int[] { R.id.pid, R.id.name });
                    // updating listview
                    setListAdapter(adapter);
                }
            });

        }

    }
}
