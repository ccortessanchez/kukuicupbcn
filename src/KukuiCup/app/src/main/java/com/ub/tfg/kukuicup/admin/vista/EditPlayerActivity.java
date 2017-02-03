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
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SimpleAdapter;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.ub.tfg.kukuicup.R;
import com.ub.tfg.kukuicup.controller.AppController;
import com.ub.tfg.kukuicup.controller.Controller;
import com.ub.tfg.kukuicup.controller.CustomRequest;
import com.ub.tfg.kukuicup.model.JSONParser;
import com.ub.tfg.kukuicup.vista.MainActivity;
import com.ub.tfg.kukuicup.vista.UserProfileActivity;

import cz.msebera.android.httpclient.NameValuePair;
import cz.msebera.android.httpclient.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Juanmi on 24/06/2016.
 */
public class EditPlayerActivity extends Activity {
    EditText txtName;
//    EditText txtTeam;
    Button btnSave;
    Button btnDelete;
    Spinner teamSpinner;
    ArrayList<HashMap<String, String>> teamList;

    String id;
    String teamid;

    // Progress Dialog
    private ProgressDialog pDialog;

    // JSON parser class
    JSONParser jsonParser = new JSONParser();

    // teams JSONArray
    JSONArray teams = null;

    // single player url
    private static String url_player_details;

    // url to update player
    private static String url_update_player;

    // url to delete player
    private static String url_delete_player;

    // url to get all team list
    private static String url_all_teams;

    // JSON Node names
    private static final String TAG_SUCCESS = "success";
    private static final String TAG_PLAYER = "player";
    private static final String TAG_ID = "id";
    private static final String TAG_NAME = "name";
    private static final String TAG_TEAM = "team_id";
    private static final String TAG_TEAMS = "teams";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        if(getResources().getBoolean(R.bool.tablet)){
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        }
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_player);

        Controller control = new Controller();
        String localhost = control.config.LOCALHOST;

        url_player_details = "http://"+localhost+"/get_player_details.php";
        url_update_player = "http://"+localhost+"/update_player_edit.php";
        url_delete_player = "http://"+localhost+"/delete_player.php";
        url_all_teams = "http://"+localhost+"/get_all_teams.php";

        // save button
        btnSave = (Button) findViewById(R.id.btnSave);
        btnDelete = (Button) findViewById(R.id.btnDelete);

        //Edit text
        txtName = (EditText)findViewById(R.id.inputName);
//        txtTeam = (EditText)findViewById(R.id.inputTeam);
        // Hashmap for Spinner
        teamList = new ArrayList<HashMap<String, String>>();
        teamSpinner = (Spinner)findViewById(R.id.teamSpinner);

        // Loading players in Background Thread
        new LoadAllTeams().execute();

        // getting player details from intent
        Intent i = getIntent();

        // getting player id (pid) from intent
        id = i.getStringExtra(TAG_ID);

        // Getting complete player details in background thread
//        new GetPlayerDetails().execute();
        getPlayerDetails();

        // save button click event
        btnSave.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                // starting background task to update player
                new SavePlayerDetails().execute();
//                savePlayerDetails();
            }
        });

        // Delete button click event
        btnDelete.setOnClickListener(new View.OnClickListener() {
                // deleting player in background thread
                @Override
                public void onClick(View v) {
                    AlertDialog.Builder altDialog = new AlertDialog.Builder(EditPlayerActivity.this);
                    altDialog.setMessage(getResources().getString(R.string.msgDeletePlayer)).setCancelable(false)
                            .setPositiveButton(getResources().getString(R.string.msgPosBtn), new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    new Deleteplayer().execute();
                                }
                            })
                            .setNegativeButton(getResources().getString(R.string.msgNegBtn), new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    dialogInterface.cancel();
                                }
                            });
                    AlertDialog alert = altDialog.create();
                    alert.setTitle(getResources().getString(R.string.btnDelete));
                    alert.show();

                }
        });

        teamSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                teamid = ((TextView) view.findViewById(R.id.pid)).getText()
                        .toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }

    private void getPlayerDetails() {
        StringRequest stringRequest = new StringRequest(url_player_details+"?id="+id, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                showJSON(response);
                Log.i("response",response);
            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(EditPlayerActivity.this,error.getMessage().toString(), Toast.LENGTH_LONG).show();
                    }
                });

        RequestQueue requestQueue = Volley.newRequestQueue(EditPlayerActivity.this);
        requestQueue.add(stringRequest);
    }

    private void showJSON(String response){
        try {
            JSONObject jsonObject = new JSONObject(response);
            JSONArray result = jsonObject.getJSONArray(TAG_PLAYER);
            JSONObject json = result.getJSONObject(0);
            txtName.setText(json.getString(TAG_NAME));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * Background Async Task to  Save player Details
     * */
    class SavePlayerDetails extends AsyncTask<String, String, String> {

        /**
         * Before starting background thread Show Progress Dialog
         * */
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(EditPlayerActivity.this);
            pDialog.setMessage("Saving Player ...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            //pDialog.show();
        }

        /**
         * Saving player
         * */
        protected String doInBackground(String... args) {

            // getting updated data from EditTexts
            String name = txtName.getText().toString().trim();
            String team = teamid;

            // Building Parameters
            List<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair(TAG_ID, id));
            params.add(new BasicNameValuePair(TAG_NAME, name));
            params.add(new BasicNameValuePair(TAG_TEAM, team));

            // sending modified data through http request
            // Notice that update player url accepts POST method
            JSONObject json = jsonParser.makeHttpRequest(url_update_player,
                    "POST", params);

            // check json success tag
            try {
                int success = json.getInt(TAG_SUCCESS);

                if (success == 1) {
                    // successfully updated
                    Intent i = getIntent();
                    // send result code 100 to notify about player update
                    setResult(100, i);
                    finish();
                } else {
                    // failed to update player
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
            // dismiss the dialog once player uupdated
            pDialog.dismiss();
        }
    }

    /*****************************************************************
     * Background Async Task to Delete player
     * */
    class Deleteplayer extends AsyncTask<String, String, String> {

        /**
         * Before starting background thread Show Progress Dialog
         * */
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(EditPlayerActivity.this);
            pDialog.setMessage("Deleting Player...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            //pDialog.show();
        }

        /**
         * Deleting player
         * */
        protected String doInBackground(String... args) {

            // Check for success tag
            int success;
            try {
                // Building Parameters
                List<NameValuePair> params = new ArrayList<NameValuePair>();
                params.add(new BasicNameValuePair("id", id));

                // getting player details by making HTTP request
                JSONObject json = jsonParser.makeHttpRequest(
                        url_delete_player, "POST", params);

                // check your log for json response
                Log.d("Delete player", json.toString());

                // json success tag
                success = json.getInt(TAG_SUCCESS);
                if (success == 1) {
                    // player successfully deleted
                    // notify previous activity by sending code 100
                    Intent i = getIntent();
                    // send result code 100 to notify about player deletion
                    setResult(100, i);
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
            // dismiss the dialog once player deleted
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
            pDialog = new ProgressDialog(EditPlayerActivity.this);
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
                    SpinnerAdapter adapter = new SimpleAdapter(EditPlayerActivity.this, teamList,
                            R.layout.player_list_item, new String[] { TAG_ID,
                            TAG_NAME},
                            new int[] { R.id.pid, R.id.name });
                    // updating spinner

//                    String[] names = teamList
//                    ArrayAdapter adapter = new ArrayAdapter<>(NewPlayerActivity.this, android.R.layout.simple_spinner_dropdown_item, )
                    teamSpinner.setAdapter(adapter);
                }
            });

        }

    }
}
