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
 * Created by Carlos Cortes
 */
public class EditTournamentActivity extends Activity {
    EditText txtInitDate;
    EditText txtEndDate;
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
    private static String url_tournament_details;
    private static String url_update_tournament;
    private static String url_delete_tournament;
    private static String url_all_tournaments;

    // JSON Node names
    private static final String TAG_SUCCESS = "success";
    private static final String TAG_INIT_DATE = "init_date";
    private static final String TAG_END_DATE = "finish_date";
    private static final String TAG_ID = "id";
    private static final String TAG_NAME = "name";
    private static final String TAG_TOURNAMENT = "tournament";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        if(getResources().getBoolean(R.bool.tablet)){
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        }
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_tournament);

        Controller control = new Controller();
        String localhost = control.config.LOCALHOST;

        url_tournament_details = "http://"+localhost+"/get_tournament_details.php";
        url_update_tournament = "http://"+localhost+"/update_tournament_edit.php";
        url_delete_tournament = "http://"+localhost+"/delete_tournament.php";
        url_all_tournaments = "http://"+localhost+"/get_all_tournaments.php";

        // save button
        btnSave = (Button) findViewById(R.id.btnSave);
        btnDelete = (Button) findViewById(R.id.btnDelete);

        //Edit text
        txtInitDate = (EditText)findViewById(R.id.inputInitDate);
        txtEndDate = (EditText)findViewById(R.id.inputEndDate);
        txtName = (EditText)findViewById(R.id.inputTourName);
//        txtTeam = (EditText)findViewById(R.id.inputTeam);

        // getting player details from intent
        Intent i = getIntent();

        // getting player id (pid) from intent
        id = i.getStringExtra(TAG_ID);

        // Getting complete player details in background thread
//        new GetPlayerDetails().execute();
        getTournamentDetails();

        btnSave.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                // starting background task to update player
                new SaveTournamentDetails().execute();
//                savePlayerDetails();
            }
        });

        // Delete button click event
        btnDelete.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                AlertDialog.Builder altDialog = new AlertDialog.Builder(EditTournamentActivity.this);
                altDialog.setMessage(getResources().getString(R.string.msgDeleteTournament)).setCancelable(false)
                        .setPositiveButton(getResources().getString(R.string.msgPosBtn), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                new Deletetournament().execute();
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
    }

    private void getTournamentDetails() {
        StringRequest stringRequest = new StringRequest(url_tournament_details+"?id="+id, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                showJSON(response);
                Log.i("response",response);
            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(EditTournamentActivity.this,error.getMessage().toString(), Toast.LENGTH_LONG).show();
                    }
                });

        RequestQueue requestQueue = Volley.newRequestQueue(EditTournamentActivity.this);
        requestQueue.add(stringRequest);
    }

    private void showJSON(String response){
        try {
            JSONObject jsonObject = new JSONObject(response);
            JSONArray result = jsonObject.getJSONArray(TAG_TOURNAMENT);
            JSONObject json = result.getJSONObject(0);
            txtInitDate.setText(json.getString(TAG_INIT_DATE));
            txtEndDate.setText(json.getString(TAG_END_DATE));
            txtName.setText(json.getString(TAG_NAME));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    class SaveTournamentDetails extends AsyncTask<String, String, String> {

        /**
         * Before starting background thread Show Progress Dialog
         * */
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(EditTournamentActivity.this);
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
            String init_date = txtInitDate.getText().toString().trim();
            String finish_date = txtEndDate.getText().toString().trim();
            String name = txtName.getText().toString().trim();

            // Building Parameters
            List<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair(TAG_ID, id));
            params.add(new BasicNameValuePair(TAG_INIT_DATE, init_date));
            params.add(new BasicNameValuePair(TAG_END_DATE, finish_date));
            params.add(new BasicNameValuePair(TAG_NAME, name));

            // sending modified data through http request
            // Notice that update player url accepts POST method
            JSONObject json = jsonParser.makeHttpRequest(url_update_tournament,
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
    class Deletetournament extends AsyncTask<String, String, String> {

        /**
         * Before starting background thread Show Progress Dialog
         * */
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(EditTournamentActivity.this);
            pDialog.setMessage("Deleting Tournament...");
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
                        url_delete_tournament, "POST", params);

                // check your log for json response
                Log.d("Delete tournament", json.toString());

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

}
