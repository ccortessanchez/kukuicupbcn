package com.ub.tfg.kukuicup.admin.vista;

import android.app.Activity;
import android.app.ProgressDialog;
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
import com.ub.tfg.kukuicup.model.JSONParser;

import cz.msebera.android.httpclient.NameValuePair;
import cz.msebera.android.httpclient.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Juanmi on 27/06/2016.
 */
public class NewTeamActivity extends Activity {
    // Progress Dialog
    private ProgressDialog pDialog;

    JSONParser jsonParser = new JSONParser();
    EditText inputName;

    // url to create new team
    private static String url_create_team;

    // JSON Node names
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

        // Edit Text
        inputName = (EditText) findViewById(R.id.inputName);

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
            pDialog.show();
        }

        /**
         * Creating team
         * */
        protected String doInBackground(String... args) {
            String name = inputName.getText().toString();

            // Building Parameters
            List<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair("name", name));

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
}
