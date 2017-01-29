package com.ub.tfg.kukuicup.admin.vista;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import cz.msebera.android.httpclient.NameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

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
import com.ub.tfg.kukuicup.model.JSONParser;

/**
 * Created by Juanmi on 07/06/2016.
 */
public class AllPlayersActivity extends ListActivity{
    // Progress Dialog
    private ProgressDialog pDialog;

    // Creating JSON Parser object
    JSONParser jParser = new JSONParser();

    ArrayList<HashMap<String, String>> playersList;

    // url to get all players list
    private String url_all_players;


    // JSON Node names
    private static final String TAG_SUCCESS = "success";
    private static final String TAG_PLAYERS = "players";
    private static final String TAG_ID = "id";
    private static final String TAG_NAME = "name";

    // players JSONArray
    JSONArray players = null;

    @Override
    public void onCreate(Bundle savedInstanceState) {

        if(getResources().getBoolean(R.bool.tablet)){
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        }
        super.onCreate(savedInstanceState);
        setContentView(R.layout.all_players);

        Controller control = new Controller();
        String localhost = control.config.LOCALHOST;
        url_all_players = "http://"+localhost+"/get_all_players.php";

        // Hashmap for ListView
        playersList = new ArrayList<HashMap<String, String>>();

        // Loading players in Background Thread
        new LoadAllPlayers().execute();

        // Get listview
        ListView lv = getListView();

        // on seleting single player
        // launching Edit player Screen
        lv.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                // getting values from selected ListItem
                String pid = ((TextView) view.findViewById(R.id.pid)).getText()
                        .toString();

                // Starting new intent
                Intent in = new Intent(AllPlayersActivity.this,
                        EditPlayerActivity.class);
                // sending pid to next activity
                in.putExtra(TAG_ID, pid);

                // starting new activity and expecting some response back
                startActivityForResult(in, 100);
            }
        });

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
            pDialog = new ProgressDialog(AllPlayersActivity.this);
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
            JSONObject json = jParser.makeHttpRequest(url_all_players, "GET", params);

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
                    for (int i = 0; i < players.length(); i++) {
                        JSONObject c = players.getJSONObject(i);

                        // Storing each json item in variable
                        String id = c.getString(TAG_ID);
                        String name = c.getString(TAG_NAME);

                        // creating new HashMap
                        HashMap<String, String> map = new HashMap<String, String>();

                        // adding each child node to HashMap key => value
                        map.put(TAG_ID, id);
                        map.put(TAG_NAME, name);

                        // adding HashList to ArrayList
                        playersList.add(map);
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
                            AllPlayersActivity.this, playersList,
                            R.layout.player_list_item, new String[] { TAG_ID,
                            TAG_NAME},
                            new int[] { R.id.pid, R.id.name });
                    // updating listview
                    setListAdapter(adapter);
                }
            });

        }

    }
}
