package com.ub.tfg.kukuicup.vista;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.ub.tfg.kukuicup.R;
import com.ub.tfg.kukuicup.controller.AppController;
import com.ub.tfg.kukuicup.controller.Controller;
import com.ub.tfg.kukuicup.controller.SQLiteHandler;
import com.ub.tfg.kukuicup.controller.SessionManager;
import com.ub.tfg.kukuicup.model.EnergyTournament;
import com.ub.tfg.kukuicup.model.JSONParser;
import com.ub.tfg.kukuicup.model.Player;
import com.ub.tfg.kukuicup.model.Team;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.view.View;
import android.view.View.OnClickListener;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class MainActivity extends Activity {
	
	private Context ctxt;

    private ProgressDialog pDialog;

    private ImageButton level1Btn;
	private ImageButton level2Btn;
	private ImageButton level3Btn;

    private AlertDialog alertDialog;
    private AlertDialog easterEgg;
	private SessionManager session;
	private SQLiteHandler db;
    JSONParser jsonParser = new JSONParser();

    private TextView playerName;
    private TextView teamName;
    private TextView playerPoints;
    private TextView teamPoints;
    private TextView playerRanking;
    private TextView teamRanking;
    private TextView endDate;

    private ImageView playerImg;
    private ImageView badgeLevel1;
    private ImageView badgeOffBeforeSleep;
    //...



	private EnergyTournament tournament;
	Player player = new Player();
    Team team = new Team();
    String username;
    String id;
    String points;

    JSONObject jsonplayer;
    JSONObject jsonteam;

    private Calendar today;
    private Calendar endDay;
    private int remainigDays;
    private int pointsObt;
    private String badgeObt;
    Bundle extras;

    boolean init;
    private boolean easterEggDay = false;
    private String teamId;
    private static String url_player_details;
    private static String url_team_details;
    private static String url_update_player_points;
    private static String url_update_team_points;

    String playername;
    String teamname;
    int playerpoints;
    int teampoints;

    public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

        Controller control = new Controller();
        String localhost = control.config.LOCALHOST;

        //player = new Player();
        //team = new Team();

        //Session manager
        session = new SessionManager(getApplicationContext());

        //Progress dialog
        pDialog = new ProgressDialog(this);
        pDialog.setCancelable(false);

        url_player_details = "http://"+localhost+"/kukuicupbcn/get_player_details_by_name.php";
        url_update_player_points = "http://"+localhost+"/kukuicupbcn/update_player_points.php";
        url_team_details = "http://"+localhost+"/kukuicupbcn/get_team_details.php";
        url_update_team_points = "http://"+localhost+"/kukuicupbcn/update_team_points.php";
		
		ctxt = getApplicationContext();
        pointsObt = 0;

        // SqLite database handler
        db = new SQLiteHandler(getApplicationContext());

        extras = getIntent().getExtras();

        if(session.getName().equals("null")) logoutUser();
        username = session.getName();

        today = Calendar.getInstance();
        int day = (today.get(Calendar.DAY_OF_MONTH));
        if((day%5==0)&&(day!=session.getEasterEggDay())) {
            easterEggDay = true;
            session.setEasterEggDay(today.get(Calendar.DAY_OF_MONTH));
        }

        playerName = (TextView)findViewById(R.id.usernameLabel);
        teamName = (TextView)findViewById(R.id.teamLabel);
        playerPoints = (TextView)findViewById(R.id.userPointsLabel);
        teamPoints = (TextView)findViewById(R.id.teamPointsLabel);
        playerRanking = (TextView)findViewById(R.id.userRkgLabel);
        teamRanking = (TextView)findViewById(R.id.teamRkgLabel);
        endDate = (TextView)findViewById(R.id.daysRemaining);
        endDate.setText(session.getEndDate());

        playerImg = (ImageView) findViewById(R.id.userImg);
        badgeLevel1 = (ImageView)findViewById(R.id.badgeLevel1);
        badgeOffBeforeSleep = (ImageView)findViewById(R.id.badgeOffBeforeSleep);

        level1Btn = (ImageButton) findViewById(R.id.level1Btn);
        level2Btn = (ImageButton) findViewById(R.id.level2Btn);
        level3Btn = (ImageButton) findViewById(R.id.level3Btn);
        playerImg.setClickable(true);


        alertDialog = new AlertDialog.Builder(MainActivity.this).create();
        easterEgg = new AlertDialog.Builder(MainActivity.this).create();

        // Setting Dialog Title
        alertDialog.setTitle("Level blocked");
        easterEgg.setTitle("It's your lucky day!");

        // Setting Dialog Message
        alertDialog.setMessage("You need to complete the previous level to continue");
        easterEgg.setMessage("It's a perfect day to give you 5 points. Enjoy it!");

        // Setting Icon to Dialog
        alertDialog.setIcon(R.drawable.ic_blocked_level);
        easterEgg.setIcon(R.drawable.ic_user);

        // Setting OK Button
        alertDialog.setButton2("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                // Write your code here to execute after dialog closed
                Toast.makeText(getApplicationContext(), "You can do it!", Toast.LENGTH_SHORT).show();
            }
        });
        easterEgg.setButton2("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                // Write your code here to execute after dialog closed

            }
        });

        if(easterEggDay){
            easterEgg.show();
        }

        loadPlayerData(username);


        playerImg.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(ctxt, UserProfileActivity.class);
//                intent.putExtra("name", player.getName());
//                intent.putExtra("passwd", player.getPasswd());
                startActivity(intent);
            }
        });

		level1Btn.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				Intent intent = new Intent(ctxt, MenuActivity.class);
				int level = 1;
				intent.putExtra("levelId", level);
                intent.putExtra("points", player.getPoints());
				startActivity(intent);
			}
		});
		
		level2Btn.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				alertDialog.show();
//				Intent intent = new Intent(ctxt, MenuActivity.class);
				//intent.putExtra("levelId",2);
                //intent.putExtra("points", player.getPoints());
//				startActivity(intent);
			}
		});
		
		level3Btn.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				alertDialog.show();
//				Intent intent = new Intent(ctxt, MenuActivity.class);
				//intent.putExtra("levelId",3);
//                intent.putExtra("points", player.getPoints());
//				startActivity(intent);
			}
		});


	}

    /**
     * Background Async Task to  Save player Details
     * */
    class SavePlayerPoints extends AsyncTask<String, String, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        /**
         * Saving player
         * */
        protected String doInBackground(String... args) {

            // Building Parameters
            List<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair("name", username));
            params.add(new BasicNameValuePair("points", points));

            // sending modified data through http request
            // Notice that update player url accepts POST method
            JSONObject json = jsonParser.makeHttpRequest(url_update_player_points,
                    "POST", params);
            Log.d("player: ", json.toString());

            return null;
        }
        protected void onPostExecute(String file_url) {
        }
    }

    /**
     * Background Async Task to  Save team Details
     * */
    class SaveTeamPoints extends AsyncTask<String, String, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        /**
         * Saving player
         * */
        protected String doInBackground(String... args) {

            // Building Parameters
            List<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair("name", team.getName()));
            params.add(new BasicNameValuePair("points", points));

            // sending modified data through http request
            // Notice that update player url accepts POST method
            JSONObject json = jsonParser.makeHttpRequest(url_update_team_points,
                    "POST", params);
            Log.d("player: ", json.toString());

            return null;
        }
        protected void onPostExecute(String file_url) {
        }
    }

    public void loadPlayerData(String username) {
        String tag_string_req = "req_loadplayer";
        pDialog.setMessage("Loading ...");
        showDialog();
        StringRequest stringRequest = new StringRequest(url_player_details + "?name='" + username+"'", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                jsonToPlayer(response);
                Log.i("response", response);
                hideDialog();
            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(MainActivity.this, error.getMessage().toString(), Toast.LENGTH_LONG).show();
                        hideDialog();
                    }
                });

        AppController.getInstance().addToRequestQueue(stringRequest, tag_string_req);
    }
    private void jsonToPlayer(String response){
        String teamid="";
        try {
            JSONObject jsonObject = new JSONObject(response);
            JSONArray result = jsonObject.getJSONArray("player");
            JSONObject json = result.getJSONObject(0);
            id = json.getString("id");
            playerName.setText(json.getString("name"));
            playerPoints.setText(json.getString("points"));
            this.player.setName(json.getString("name"));
            this.player.setPoints(Integer.parseInt(json.getString("points")));
            teamid = json.getString("team_id");

        } catch (JSONException e) {
            e.printStackTrace();
        }
        loadTeamData(teamid);
    }

    public void loadTeamData(String teamId) {
        String tag_string_req = "req_loadteam";
        pDialog.setMessage("Loading ...");
        showDialog();
        StringRequest stringRequest = new StringRequest(url_team_details + "?id='" + teamId+"'", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                jsonToTeam(response);
                Log.i("response", response);
                hideDialog();
            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(MainActivity.this, error.getMessage().toString(), Toast.LENGTH_LONG).show();
                        hideDialog();
                    }
                });

        AppController.getInstance().addToRequestQueue(stringRequest, tag_string_req);
    }
    private void jsonToTeam(String response){
        try {
            JSONObject jsonObject = new JSONObject(response);
            JSONArray result = jsonObject.getJSONArray("team");
            JSONObject json = result.getJSONObject(0);
            this.team.setName(json.getString("name"));
            this.team.setPoints(json.getInt("points"));
            teamName.setText(json.getString("name"));
            teamPoints.setText(json.getString("points"));

        } catch (JSONException e) {
            e.printStackTrace();
        }
        updateDashBoard();
    }


    public void updateDashBoard(){

        if(extras!=null){
            if(extras.containsKey("points")){
                player.setPoints(player.getPoints()+extras.getInt("points"));
                points = String.valueOf(player.getPoints());
                // starting background task to update player
                new SavePlayerPoints().execute();
                // starting background task to update team
                team.setPoints(team.getPoints()+extras.getInt("points"));
                points = String.valueOf(team.getPoints());
                new SaveTeamPoints().execute();
//                loadPlayerData(username);
            }
            if(extras.containsKey("badge")){
                if(extras.getString("badge").equals("level1")){
                    badgeLevel1.setImageResource(R.mipmap.level1_badge);
                }
                if(extras.getString("badge").equals("offBeforeSleep")){
                    badgeOffBeforeSleep.setImageResource(R.mipmap.off_sleep_badge);
                }
            }
            if(easterEggDay){
                easterEggDay=false;
                player.setPoints(player.getPoints()+5);
                points = String.valueOf(player.getPoints());
                // starting background task to update player
                new SavePlayerPoints().execute();
                // starting background task to update team
                team.setPoints(team.getPoints()+5);
                points = String.valueOf(team.getPoints());
                new SaveTeamPoints().execute();
            }
//                playerRanking.setText(extras.getInt("points"));
        }

        playerName.setText(player.getName());
        teamName.setText(team.getName());
        playerPoints.setText(""+player.getPoints());
        teamPoints.setText(""+team.getPoints());
//        playerRanking.setText("Ranking: "+player.getRanking());
//        teamRanking.setText("Ranking: "+team.getClassification());

        if(team.getBadgeByName("Level1")!=null)
            badgeLevel1.setImageResource(R.mipmap.level1team_badge);
        else {
            if(player.getBadgeByName("Level1")!=null)
                badgeLevel1.setImageResource(R.mipmap.level1_badge);
        }

        if(team.getBadgeByName("OffBeforeSleep")!=null)
            badgeOffBeforeSleep.setImageResource(R.mipmap.off_sleep_team_badge);
        else {
            if(player.getBadgeByName("OffBeforeSleep")!=null)
                badgeOffBeforeSleep.setImageResource(R.mipmap.off_sleep_badge);
        }

    }

    /**
     * Logging out the user. Will set isLoggedIn flag to false in shared
     * preferences Clears the user data from sqlite users table
     */
    private void logoutUser() {
        session.setLogin(false);

        db.deletePlayers();

        // Launching the login activity
        Intent intent = new Intent(MainActivity.this, LoginActivity.class);
        startActivity(intent);
        finish();
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
