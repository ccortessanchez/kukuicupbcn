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
import com.ub.tfg.kukuicup.model.JSONParser;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import cz.msebera.android.httpclient.NameValuePair;

/**
 * Created by Carlos Cortes
 */
public class AllRegisterActivity extends ListActivity{
    // Progress Dialog
    private ProgressDialog pDialog;

    // Creating JSON Parser object
    JSONParser jParser = new JSONParser();

    ArrayList<HashMap<String, String>> registerList;

    // url to get all players list
    private String url_all_register_activity;


    // JSON Node names
    private static final String TAG_SUCCESS = "success";
    private static final String TAG_REGISTER = "activity_reg";
    private static final String TAG_PLAYER_NAME = "player_name";
    private static final String TAG_ACTIVITY_NAME = "activity_name";

    // players JSONArray
    JSONArray activity_reg = null;

    @Override
    public void onCreate(Bundle savedInstanceState) {

        if(getResources().getBoolean(R.bool.tablet)){
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        }
        super.onCreate(savedInstanceState);
        setContentView(R.layout.all_players);

        Controller control = new Controller();
        String localhost = control.config.LOCALHOST;
        url_all_register_activity = "http://"+localhost+"/get_all_activity_reg.php";

        // Hashmap for ListView
        registerList = new ArrayList<HashMap<String, String>>();

        // Loading players in Background Thread
        new LoadAllRegister().execute();

        // Get listview
        ListView lv = getListView();

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
    class LoadAllRegister extends AsyncTask<String, String, String> {

        /**
         * Before starting background thread Show Progress Dialog
         * */
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(AllRegisterActivity.this);
            pDialog.setMessage(getResources().getString(R.string.loadRegister));
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
            JSONObject json = jParser.makeHttpRequest(url_all_register_activity, "GET", params);

            // Check your log cat for JSON reponse
            Log.d("All Register: ", json.toString());

            try {
                // Checking for SUCCESS TAG
                int success = json.getInt(TAG_SUCCESS);

                if (success == 1) {
                    // players found
                    // Getting Array of players
                    activity_reg = json.getJSONArray(TAG_REGISTER);

                    // looping through All players
                    for (int i = 0; i < activity_reg.length(); i++) {
                        JSONObject c = activity_reg.getJSONObject(i);

                        // Storing each json item in variable
                        String player_name = c.getString(TAG_PLAYER_NAME);
                        String activity_name = c.getString(TAG_ACTIVITY_NAME);
                        String reg = player_name + " --> " + activity_name;

                        // creating new HashMap
                        HashMap<String, String> map = new HashMap<String, String>();

                        // adding each child node to HashMap key => value
                        map.put(TAG_PLAYER_NAME, player_name);
                        map.put(TAG_ACTIVITY_NAME, reg);

                        // adding HashList to ArrayList
                        registerList.add(map);
                    }
                } else {
                    // no register found
                    // Launch Add New player Activity
                    Intent i = new Intent(getApplicationContext(),
                            MenuAdminActivity.class);
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
                            AllRegisterActivity.this, registerList,
                            R.layout.register_list_item, new String[] { TAG_PLAYER_NAME,
                            TAG_ACTIVITY_NAME},
                            new int[] { R.id.playerName, R.id.activityName });
                    // updating listview
                    setListAdapter(adapter);
                }
            });

        }

    }
}
