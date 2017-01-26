package com.ub.tfg.kukuicup.vista;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.ub.tfg.kukuicup.R;
import com.ub.tfg.kukuicup.admin.vista.MenuAdminActivity;

import java.util.Locale;
import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.util.DisplayMetrics;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.media.Image;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request.Method;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import com.ub.tfg.kukuicup.controller.AppController;
import com.ub.tfg.kukuicup.controller.Controller;
import com.ub.tfg.kukuicup.controller.SQLiteHandler;
import com.ub.tfg.kukuicup.controller.SessionManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class LoginActivity extends Activity {
    /** Called when the activity is first created. */
    private static final String TAG = LoginActivity.class.getSimpleName();
    private static final String TAG_NAME = "name";
    private static final String TAG_PASSWD = "passwd";
    private static final String TAG_SUCCESS = "success";
	private Context ctxt;
    private TextView welcomeLabel;

    Button enterBtn;
    EditText inputUsername;
    EditText inputPasswd;

    private ImageView english_flag;
    private ImageView spanish_flag;
    private ImageView catalan_flag;

    private ProgressDialog pDialog;
    private SessionManager session;
    private SQLiteHandler db;

    private static String url_login;

    public void onCreate(Bundle savedInstanceState) {
        if(getResources().getBoolean(R.bool.tablet)){
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        }
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);

        Controller control = new Controller();
        String localhost = control.config.LOCALHOST;
        url_login = "http://"+localhost+"/login.php";
        
        ctxt = getApplicationContext();

        welcomeLabel = (TextView)findViewById(R.id.welcomeLabel);
        enterBtn = (Button)findViewById(R.id.enterBtn);
        inputUsername = (EditText)findViewById(R.id.inputUsername);
        inputPasswd = (EditText)findViewById(R.id.inputPasswd);
        //english_flag = (ImageView)findViewById(R.id.en_lan);
        //spanish_flag = (ImageView)findViewById(R.id.es_lan);
        //catalan_flag = (ImageView)findViewById(R.id.ca_lan);

        welcomeLabel.setText(getResources().getString(R.string.welcomeMsg));
        inputUsername.setHint(getResources().getString(R.string.hintUser));
        //english_flag.setVisibility(View.VISIBLE);
        //spanish_flag.setVisibility(View.VISIBLE);
        //catalan_flag.setVisibility(View.VISIBLE);
        inputPasswd.setHint(getResources().getString(R.string.hintPass));

        //Progress dialog
        pDialog = new ProgressDialog(this);
        pDialog.setCancelable(false);

        //SQLite database handler
        db = new SQLiteHandler(getApplicationContext());

        //Session manager
        session = new SessionManager(getApplicationContext());

        //Check if user is already logged in or not
        if(session.isLoggedIn()){
            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        }

        /**
        english_flag.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), "You have selected English", Toast.LENGTH_SHORT).show();
                setLocale("en");

            }
        });

        spanish_flag.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), "Has seleccionado Castellano", Toast.LENGTH_SHORT).show();
                setLocale("es");

            }
        });

        catalan_flag.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), "Has seleccionat Català", Toast.LENGTH_SHORT).show();
                setLocale("ca_rES");

            }
        }); **/

        //Login button click event
        enterBtn.setOnClickListener(new OnClickListener() {
        	public void onClick(View v) {
                String username = inputUsername.getText().toString();
                String passwd = inputPasswd.getText().toString();

                // Check for empty data in the form
                if (!username.isEmpty() && !passwd.isEmpty()) {
                    if(username.equals("admin") && passwd.equals("1234")) {
                        Intent intent = new Intent(ctxt, MenuAdminActivity.class);
                        startActivity(intent);
                    } else {
                        if(session.getEndDate()!=null) {
                            // login user
                            checkLogin(username, passwd);
                        } else {
                            Toast.makeText(getApplicationContext(),
                                    getResources().getString(R.string.noTournament), Toast.LENGTH_LONG)
                                    .show();
                        }

                    }
                } else {
                    // Prompt user to enter credentials
                    Toast.makeText(getApplicationContext(),
                            getResources().getString(R.string.userEnter), Toast.LENGTH_LONG)
                            .show();
                }

        	}
        });
    }


    /**
    public void setLocale(String lang) {

        Resources res = getResources();
        DisplayMetrics dm = res.getDisplayMetrics();
        Configuration conf = res.getConfiguration();
        if (!conf.locale.getLanguage().equals(lang)) {
            conf.locale = new Locale(lang);
            res.updateConfiguration(conf, dm);
            Intent refresh = new Intent(this, LoginActivity.class);
            startActivity(refresh);
            finish();
        }
    } **/

    /**
     * function to verify login details in mysql db
     * */
    private void checkLogin(final String username, final String passwd) {
        // Tag used to cancel the request
        String tag_string_req = "req_login";

        pDialog.setMessage("Logging in ...");
        showDialog();

        StringRequest strReq = new StringRequest(Request.Method.POST,
                url_login, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d(TAG,"Login Response: " + response.toString());
                hideDialog();

                if(response.equalsIgnoreCase(TAG_SUCCESS)) {
                    //Create login session
                    session.setLogin(true);
                    session.setName(username);
                    // Launch main activity
                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                    intent.putExtra("name", username);
                    startActivity(intent);
                    finish();
                }else{
                    //If the server response is not success
                    //Displaying an error message on toast
                    Toast.makeText(LoginActivity.this, getResources().getString(R.string.invalidLogin), Toast.LENGTH_LONG).show();
                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("Login Error: ", error.getMessage());
                Toast.makeText(getApplicationContext(),
                        error.getMessage(), Toast.LENGTH_LONG).show();
                hideDialog();
            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                // Posting parameters to login url
                Map<String, String> params = new HashMap<String, String>();
                params.put(TAG_NAME, username);
                params.put(TAG_PASSWD, passwd);

                return params;
            }

        };

        // Adding request to request queue
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