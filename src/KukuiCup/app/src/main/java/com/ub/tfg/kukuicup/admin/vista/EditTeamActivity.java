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

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
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
import cz.msebera.android.httpclient.message.BasicNameValuePair;

/**
 * Created by Carlos Cortes.
 */
public class EditTeamActivity extends Activity {
    EditText txtName;
//    EditText txtTeam;
    Button btnSave;
    Button btnDelete;
    Spinner tourSpinner;
    ArrayList<HashMap<String, String>> tournamentList;

    String id;
    String tournamentid;

    // Progress Dialog
    private ProgressDialog pDialog;

    // JSON parser class
    JSONParser jsonParser = new JSONParser();

    // teams JSONArray
    JSONArray tournaments = null;

    // single player url
    private static String url_team_details;
    private static String url_update_team;
    private static String url_delete_team;
    private static String url_all_tournaments;

    // JSON Node names
    private static final String TAG_SUCCESS = "success";
    private static final String TAG_TEAM = "team";
    private static final String TAG_ID = "id";
    private static final String TAG_NAME = "name";
    private static final String TAG_TOURNAMENT = "tournament_id";
    private static final String TAG_TOURNAMENTS = "tournaments";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        if(getResources().getBoolean(R.bool.tablet)){
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        }
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_team);

        Controller control = new Controller();
        String localhost = control.config.LOCALHOST;

        url_team_details = "http://"+localhost+"/get_team_details.php";
        url_update_team = "http://"+localhost+"/update_team_edit.php";
        url_delete_team = "http://"+localhost+"/delete_team.php";
        url_all_tournaments = "http://"+localhost+"/get_all_tournaments.php";

        // save button
        btnSave = (Button) findViewById(R.id.btnSave);
        btnDelete = (Button) findViewById(R.id.btnDelete);

        //Edit text
        txtName = (EditText)findViewById(R.id.inputName);
//        txtTeam = (EditText)findViewById(R.id.inputTeam);
        // Hashmap for Spinner
        tournamentList = new ArrayList<HashMap<String, String>>();
        tourSpinner = (Spinner)findViewById(R.id.tourSpinner);

        // Loading players in Background Thread
        new LoadAllTournaments().execute();

        // getting player details from intent
        Intent i = getIntent();

        // getting player id (pid) from intent
        id = i.getStringExtra(TAG_ID);

        // Getting complete player details in background thread
//        new GetPlayerDetails().execute();
        getTeamDetails();

        // save button click event
        btnSave.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                // starting background task to update player
                new SaveTeamDetails().execute();
//                savePlayerDetails();
            }
        });

        // Delete button click event
        btnDelete.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                AlertDialog.Builder altDialog = new AlertDialog.Builder(EditTeamActivity.this);
                altDialog.setMessage(getResources().getString(R.string.msgDeleteTeam)).setCancelable(false)
                        .setPositiveButton(getResources().getString(R.string.msgPosBtn), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                new Deleteteam().execute();
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

        tourSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                tournamentid = ((TextView) view.findViewById(R.id.pid)).getText()
                        .toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }

    private void getTeamDetails() {
        StringRequest stringRequest = new StringRequest(url_team_details+"?id="+id, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                showJSON(response);
                Log.i("response",response);
            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(EditTeamActivity.this,error.getMessage().toString(), Toast.LENGTH_LONG).show();
                    }
                });

        RequestQueue requestQueue = Volley.newRequestQueue(EditTeamActivity.this);
        requestQueue.add(stringRequest);
    }

    private void showJSON(String response){
        try {
            JSONObject jsonObject = new JSONObject(response);
            JSONArray result = jsonObject.getJSONArray(TAG_TEAM);
            JSONObject json = result.getJSONObject(0);
            txtName.setText(json.getString(TAG_NAME));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * Background Async Task to  Save player Details
     * */
    class SaveTeamDetails extends AsyncTask<String, String, String> {

        /**
         * Before starting background thread Show Progress Dialog
         * */
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(EditTeamActivity.this);
            pDialog.setMessage("Saving Team ...");
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
            String tournament = tournamentid;

            // Building Parameters
            List<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair(TAG_ID, id));
            params.add(new BasicNameValuePair(TAG_NAME, name));
            params.add(new BasicNameValuePair(TAG_TOURNAMENT, tournament));

            // sending modified data through http request
            // Notice that update player url accepts POST method
            JSONObject json = jsonParser.makeHttpRequest(url_update_team,
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
    class Deleteteam extends AsyncTask<String, String, String> {

        /**
         * Before starting background thread Show Progress Dialog
         * */
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(EditTeamActivity.this);
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
                        url_delete_team, "POST", params);

                // check your log for json response
                Log.d("Delete team", json.toString());

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
    class LoadAllTournaments extends AsyncTask<String, String, String> {

        /**
         * Before starting background thread Show Progress Dialog
         * */
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(EditTeamActivity.this);
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
                    SpinnerAdapter adapter = new SimpleAdapter(EditTeamActivity.this, tournamentList,
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
