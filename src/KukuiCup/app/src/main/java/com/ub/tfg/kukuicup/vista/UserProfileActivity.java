package com.ub.tfg.kukuicup.vista;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.ub.tfg.kukuicup.R;
import com.ub.tfg.kukuicup.controller.AppController;
import com.ub.tfg.kukuicup.controller.Controller;
import com.ub.tfg.kukuicup.controller.SQLiteHandler;
import com.ub.tfg.kukuicup.controller.SessionManager;
import com.ub.tfg.kukuicup.model.JSONParser;

import cz.msebera.android.httpclient.NameValuePair;
import cz.msebera.android.httpclient.message.BasicNameValuePair;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Juanmi on 01/07/2016.
 */
public class UserProfileActivity extends Activity {

    private static final String TAG = UserProfileActivity.class.getSimpleName();
    private EditText passwd;
    private EditText newPasswd;
    private EditText confirmPasswd;
    private Button done;
    private TextView username;
    private Button logOutBtn;
    private SessionManager session;
    private SQLiteHandler db;
    private ProgressDialog pDialog;

    private String confirmpass;
    private String newpass;
    JSONParser jsonParser = new JSONParser();
    private static String url_update_player_passwd;
    private static String url_login;


    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_profile);

        Controller control = new Controller();
        String localhost = control.config.LOCALHOST;

        url_update_player_passwd = "http://" + localhost + "/kukuicupbcn/update_player_passwd.php";
        url_login = "http://" + localhost + "/kukuicupbcn/login.php";

        session = new SessionManager(getApplicationContext());
        if (!session.isLoggedIn()) {
            logoutUser();
        }

        // SqLite database handler
        db = new SQLiteHandler(getApplicationContext());

        Bundle extras = getIntent().getExtras();

        //Progress dialog
        pDialog = new ProgressDialog(this);
        pDialog.setCancelable(false);

        passwd = (EditText) findViewById(R.id.inputPswd);
        newPasswd = (EditText) findViewById(R.id.inputNewPswd);
        confirmPasswd = (EditText) findViewById(R.id.inputConfirmPswd);
        done = (Button) findViewById(R.id.doneBtn);
        username = (TextView) findViewById(R.id.usernameLabel);
        logOutBtn = (Button) findViewById(R.id.logOutBtn);

        logOutBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                logoutUser();
            }
        });

        done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String pass = passwd.getText().toString().trim();
                newpass = newPasswd.getText().toString().trim();
                confirmpass = confirmPasswd.getText().toString().trim();

                if (!pass.isEmpty() && !newpass.isEmpty() && !confirmpass.isEmpty()) {
                    checkLogin(session.getName(),pass,newpass, confirmpass);

                } else {
                    Toast.makeText(getApplicationContext(), "You must fill all the gaps", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    /**
     * Logging out the user. Will set isLoggedIn flag to false in shared
     * preferences Clears the user data from sqlite users table
     */
    private void logoutUser() {
        session.setLogin(false);

        db.deletePlayers();

        // Launching the login activity
        Intent intent = new Intent(UserProfileActivity.this, LoginActivity.class);
        startActivity(intent);
        finish();
    }

    /**
     * Background Async Task to  Save player passwd
     */
    class SavePlayerPasswd extends AsyncTask<String, String, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        /**
         * Saving player passwd
         */
        protected String doInBackground(String... args) {
            String name = session.getName();
            String pass = confirmPasswd.getText().toString().trim();
            // Building Parameters
            List<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair("name", name));
            params.add(new BasicNameValuePair("passwd", pass));

            // sending modified data through http request
            // Notice that update player url accepts POST method
            JSONObject json = jsonParser.makeHttpRequest(url_update_player_passwd, "POST", params);
            Log.d("player: ", json.toString());

            return null;
        }

        protected void onPostExecute(String file_url) {
        }
    }

    /**
     * function to verify login details in mysql db
     */
    private void checkLogin(final String username, final String passwd, final String newpass, final String confirmpass) {
        // Tag used to cancel the request
        String tag_string_req = "req_login";

        pDialog.setMessage("Identification ...");
        showDialog();

        StringRequest strReq = new StringRequest(Request.Method.POST,
                url_login, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d(TAG, "Login Response: " + response.toString());
                hideDialog();

                if (response.equalsIgnoreCase("success")) {
                    if (newpass.equals(confirmpass)) {
                        Toast.makeText(getApplicationContext(), "Change successfully", Toast.LENGTH_SHORT).show();
                        new SavePlayerPasswd().execute();
                        Intent intent = new Intent(UserProfileActivity.this, MainActivity.class);
                        startActivity(intent);
                    } else {
                        Toast.makeText(getApplicationContext(), "New password and confirmation must be the same", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    //If the server response is not success
                    //Displaying an error message on toast
                    Toast.makeText(UserProfileActivity.this, "Invalid password", Toast.LENGTH_LONG).show();
                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("Passwd Error: ", error.getMessage());
                Toast.makeText(getApplicationContext(),
                        error.getMessage(), Toast.LENGTH_LONG).show();
                hideDialog();
            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                // Posting parameters to login url
                Map<String, String> params = new HashMap<String, String>();
                params.put("name", username);
                params.put("passwd", passwd);

                return params;
            }

        };
        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
    }

    private void showDialog() {
        if (!pDialog.isShowing())
            pDialog.show();
    }

    private void hideDialog() {
        if (pDialog.isShowing())
            pDialog.dismiss();
    }
}
