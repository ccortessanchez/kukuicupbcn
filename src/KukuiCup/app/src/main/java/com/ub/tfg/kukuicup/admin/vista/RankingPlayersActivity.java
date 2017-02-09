package com.ub.tfg.kukuicup.admin.vista;

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
import com.ub.tfg.kukuicup.controller.Controller;
import com.ub.tfg.kukuicup.controller.SessionManager;
import com.ub.tfg.kukuicup.model.JSONParser;
import com.ub.tfg.kukuicup.vista.*;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import cz.msebera.android.httpclient.NameValuePair;

/**
 * Created by Juanmi on 07/06/2016. Edited by Carlos Cortes.
 */
public class RankingPlayersActivity extends ListActivity{
    // Progress Dialog
    private ProgressDialog pDialog;

    // Creating JSON Parser object
    JSONParser jParser = new JSONParser();

    private SessionManager session;

    String myteamID;


    ArrayList<HashMap<String, String>> playersList;

    // url to get all players list
    private String url_ranking_players;


    // JSON Node names
    private static final String TAG_SUCCESS = "success";
    private static final String TAG_PLAYERS = "players";
    private static final String TAG_ID = "id";
    private static final String TAG_TEAM = "team_id";
    private static final String TAG_NAME = "name";
    private static final String TAG_POINTS = "points";

    // players JSONArray
    JSONArray players = null;

    @Override
    public void onCreate(Bundle savedInstanceState) {

        if(getResources().getBoolean(R.bool.tablet)){
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        }
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ranking_players);

        Controller control = new Controller();
        String localhost = control.config.LOCALHOST;
        session = new SessionManager(getApplicationContext());
        url_ranking_players = "http://"+localhost+"/get_player_ranking.php";

        myteamID = session.getTeam();
        // Hashmap for ListView
        playersList = new ArrayList<HashMap<String, String>>();

        // Loading players in Background Thread
        new LoadAllPlayers().execute();

    }

    // Response from Edit player Activity
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // if result code 100
        if (resultCode == 100) {
            // if result code 100 is received
            // means user edited/deleted player
            // reload this screen again
            Intent intent = getIntent();
            finish();
            startActivity(intent);
        }

    }

    /**
     * Background Async Task to Load all player by making HTTP Request
     * */
    class LoadAllPlayers extends AsyncTask<String, String, String> {

        /**
         * Before starting background thread Show Progress Dialog
         * */
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(RankingPlayersActivity.this);
            pDialog.setMessage(getResources().getString(R.string.loadPlayers));
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
            //pDialog.show();
        }

        /**
         * getting All players from url
         * */
        protected String doInBackground(String... args) {
            // Building Parameters
            List<NameValuePair> params = new ArrayList<NameValuePair>();
            // getting JSON string from URL
            JSONObject json = jParser.makeHttpRequest(url_ranking_players, "GET", params);

            // Check your log cat for JSON reponse
            Log.d("All Players: ", json.toString());

            try {
                // Checking for SUCCESS TAG
                int success = json.getInt(TAG_SUCCESS);

                if (success == 1) {
                    // players found
                    // Getting Array of players
                    players = json.getJSONArray(TAG_PLAYERS);

                    // looping through All players
                    int counter = 1;
                    HashMap<String, String> map = new HashMap<String, String>();
                    for (int i = 0; i < players.length(); i++) {
                        JSONObject c = players.getJSONObject(i);

                        // Storing each json item in variable
                        String id = c.getString(TAG_ID);
                        String name = c.getString(TAG_NAME);
                        String team_id = c.getString(TAG_TEAM);
                        String points = c.getString(TAG_POINTS);
                        if (team_id.equals(myteamID)) {
                            name = counter + " " + name + " , " + points + " " +getResources().getString(R.string.pointsLabel);
                            counter++;
                            map.put(TAG_ID, id);
                            map.put(TAG_NAME, name);

                            // adding HashList to ArrayList
                            playersList.add(map);
                        }
                    }
                } else {
                    // no players found
                    // Launch Add New player Activity
                    Intent i = new Intent(getApplicationContext(),
                            MenuTournamentActivity.class);
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
                            RankingPlayersActivity.this, playersList,
                            R.layout.player_list_item, new String[] { TAG_ID,
                            TAG_NAME},
                            new int[] { R.id.pid, R.id.name });
                    // updating listview
                    setListAdapter(adapter);
                }
            });

        }

    }

    @Override
    public void onBackPressed() {
        Intent i = new Intent(getApplicationContext(), RankingTeamsActivity.class);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(i);
    }
}
