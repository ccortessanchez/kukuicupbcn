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
import android.content.pm.ActivityInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.view.View;
import android.view.View.OnClickListener;

import cz.msebera.android.httpclient.NameValuePair;
import cz.msebera.android.httpclient.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.HashMap;


public class MainActivity extends Activity {
	
	private Context ctxt;

    private ProgressDialog pDialog;

    private ImageButton level1Btn;
	private ImageButton level2Btn;
	private ImageButton level3Btn;

    private AlertDialog alertDialog;
    private AlertDialog easterEgg;
    private AlertDialog unlockedActivities;
	private SessionManager session;
	private SQLiteHandler db;

    JSONParser jsonParser = new JSONParser();
    ArrayList<HashMap<String, String>> badgesList;

    private TextView playerName;
    private TextView teamName;
    private TextView playerPoints;
    private TextView teamPoints;
    private TextView playerRanking;
    private TextView teamRanking;
    private TextView endDate;

    private ImageView playerImg;
    private ImageView teamImg;
    private ImageView badgeLevel1;
    private ImageView badgeLevel2;
    private ImageView badgeLevel3;
    private ImageView badgeOffBeforeSleep;
    private ImageView badgeEmptyRoom;
    private ImageView badgeUseStairs;
    private ImageView badgeMoreLess;
    //...



	private EnergyTournament tournament;
	Player player = new Player();
    Team team = new Team();
    String username;
    String id;
    String badge_id;
    String points_player;
    String points_team;

    JSONObject jsonplayer;
    JSONObject jsonteam;

    // JSON Node names
    private static final String TAG_SUCCESS = "success";
    private static final String TAG_BADGES = "players_badges";
    private static final String TAG_ID_PLAYER = "player_id";
    private static final String TAG_ID_BADGE = "badge_id";

    JSONArray badges = null;

    private Calendar today;
    private Calendar endDay;
    private int remainigDays;
    private int pointsObt;
    private String badgeObt;
    Bundle extras;

    boolean init;
    private boolean easterEggDay = false;
    private boolean allActivities = false;
    private String teamId;
    private static String url_player_details;
    private static String url_team_details;
    private static String url_update_player_points;
    private static String url_update_team_points;
    private static String url_create_player_badges;
    private static String url_get_player_badges;
    private static String url_get_player_ranking;
    private static String url_get_team_ranking;

    String playername;
    String teamname;
    int playerpoints;
    int teampoints;

    public void onCreate(Bundle savedInstanceState) {
        if(getResources().getBoolean(R.bool.tablet)){
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        }
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

        url_player_details = "http://"+localhost+"/get_player_details_by_name.php";
        url_update_player_points = "http://"+localhost+"/update_player_points.php";
        url_team_details = "http://"+localhost+"/get_team_details.php";
        url_update_team_points = "http://"+localhost+"/update_team_points.php";
        url_get_player_ranking = "http://"+localhost+"/get_player_ranking.php";
        url_get_team_ranking = "http://"+localhost+"/get_team_ranking.php";
        url_create_player_badges = "http://"+localhost+"/create_badge.php";
        url_get_player_badges = "http://"+localhost+"/get_all_badges.php";
		
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
        endDate = (TextView)findViewById(R.id.daysRemaining);
        if (session.getEndDate().equals("null")) {
            endDate.setText(" " + getResources().getString(R.string.noEndDate));
        }
        else {
            endDate.setText(" " + session.getEndDate());
        }


        playerImg = (ImageView) findViewById(R.id.userImg);
        teamImg = (ImageView) findViewById(R.id.teamImg);
        badgeLevel1 = (ImageView)findViewById(R.id.badgeLevel1);
        badgeLevel2 = (ImageView)findViewById(R.id.badgeLevel2);
        badgeLevel3 = (ImageView)findViewById(R.id.badgeLevel3);
        badgeOffBeforeSleep = (ImageView)findViewById(R.id.badgeOffBeforeSleep);
        badgeEmptyRoom = (ImageView)findViewById(R.id.badgeTurnOff);
        badgeUseStairs = (ImageView)findViewById(R.id.badgeUpStairs);
        badgeMoreLess = (ImageView)findViewById(R.id.badgeMoreLess);

        level1Btn = (ImageButton) findViewById(R.id.level1Btn);
        level2Btn = (ImageButton) findViewById(R.id.level2Btn);
        level3Btn = (ImageButton) findViewById(R.id.level3Btn);
        playerImg.setClickable(true);
        teamImg.setClickable(true);


        alertDialog = new AlertDialog.Builder(MainActivity.this).create();
        easterEgg = new AlertDialog.Builder(MainActivity.this).create();
        unlockedActivities = new AlertDialog.Builder(MainActivity.this).create();

        // Setting Dialog Title
        alertDialog.setTitle(getResources().getString(R.string.titleLevelBlocked));
        easterEgg.setTitle(getResources().getString(R.string.titleEasterEgg));
        unlockedActivities.setTitle(getResources().getString(R.string.titleMinPoints));

        // Setting Dialog Message
        alertDialog.setMessage(getResources().getString(R.string.msgLevelBlocked));
        easterEgg.setMessage(getResources().getString(R.string.msgEasterEgg));
        unlockedActivities.setMessage(getResources().getString(R.string.minPoints));

        // Setting Icon to Dialog
        alertDialog.setIcon(R.drawable.ic_blocked_level);
        easterEgg.setIcon(R.drawable.ic_user);
        unlockedActivities.setIcon(R.drawable.ic_launcher);

        // Setting OK Button
        alertDialog.setButton2(getResources().getString(R.string.btnOk), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                // Write your code here to execute after dialog closed
                if(getResources().getBoolean(R.bool.tablet)){
                    Toast toast = Toast.makeText(getApplicationContext(), getResources().getString(R.string.msgToastLevel), Toast.LENGTH_SHORT);
                    LinearLayout toastLayout = (LinearLayout) toast.getView();
                    TextView toastTV = (TextView) toastLayout.getChildAt(0);
                    toastTV.setTextSize(30);
                    toast.show();
                }
                else {
                    Toast.makeText(getApplicationContext(), getResources().getString(R.string.msgToastLevel), Toast.LENGTH_SHORT).show();
                }

            }
        });
        easterEgg.setButton2(getResources().getString(R.string.btnOk), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                // Write your code here to execute after dialog closed

            }
        });

        unlockedActivities.setButton2(getResources().getString(R.string.btnOk), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                // Write your code here to execute after dialog closed

            }
        });

        if(easterEggDay){
            easterEgg.show();
        }

        if(allActivities){
            unlockedActivities.show();
        }

        loadPlayerData(username);
        //loadPlayerBadges(id);
        //updateDashBoard();


        playerImg.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(ctxt, UserProfileActivity.class);
//                intent.putExtra("name", player.getName());
//                intent.putExtra("passwd", player.getPasswd());
                startActivity(intent);
            }
        });

        teamImg.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(ctxt, RankingTeamsActivity.class);
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
                if (player.getPoints() <= 50) {
                    alertDialog.show();
                }
                else {
                    Intent intent = new Intent(ctxt, MenuActivity.class);
                    int level = 2;
                    intent.putExtra("levelId", level);
                    intent.putExtra("points", player.getPoints());
                    startActivity(intent);
                }
//				Intent intent = new Intent(ctxt, MenuActivity.class);
				//intent.putExtra("levelId",2);
                //intent.putExtra("points", player.getPoints());
//				startActivity(intent);
			}
		});
		
		level3Btn.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
                if (player.getPoints() <= 180) {
                    alertDialog.show();
                }
                else {
                    Intent intent = new Intent(ctxt, MenuActivity.class);
                    int level = 3;
                    intent.putExtra("levelId", level);
                    intent.putExtra("points", player.getPoints());
                    startActivity(intent);
                }
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
            params.add(new BasicNameValuePair("points", points_player));

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

    class SavePlayerBadges extends AsyncTask<String, String, String> {

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
            params.add(new BasicNameValuePair("player_id", id));
            params.add(new BasicNameValuePair("badge_id", badge_id));

            // sending modified data through http request
            // Notice that update player url accepts POST method
            JSONObject json = jsonParser.makeHttpRequest(url_create_player_badges,
                    "POST", params);
            Log.d("badge: ", json.toString());

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
            params.add(new BasicNameValuePair("points", points_team));

            // sending modified data through http request
            // Notice that update player url accepts POST method
            JSONObject json = jsonParser.makeHttpRequest(url_update_team_points,
                    "POST", params);
            Log.d("team: ", json.toString());

            return null;
        }
        protected void onPostExecute(String file_url) {
        }
    }


    public String loadBadges(String id) {
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        // getting JSON string from URL
        JSONObject json = jsonParser.makeHttpRequest(url_get_player_badges, "GET", params);

        // Check your log cat for JSON reponse
        Log.d("All Badges: ", json.toString());

        try {
            // Checking for SUCCESS TAG
            int success = json.getInt(TAG_SUCCESS);

            if (success == 1) {
                // players found
                // Getting Array of players
                badges = json.getJSONArray(TAG_BADGES);

                // looping through All players
                for (int i = 0; i < badges.length(); i++) {
                    JSONObject c = badges.getJSONObject(i);

                    // Storing each json item in variable
                    String player_id = c.getString(TAG_ID_PLAYER);
                    String badge_id = c.getString(TAG_ID_BADGE);

                    // creating new HashMap
                    HashMap<String, String> map = new HashMap<String, String>();
                    if (player_id.equals(id)) {
                        map.put(TAG_ID_PLAYER, player_id);
                        map.put(TAG_ID_BADGE, badge_id);
                        badgesList.add(map);
                    }
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return null;
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

    public void loadPlayerBadges(String id) {
        String tag_string_req = "req_loadbadges";
        pDialog.setMessage("Loading ...");
        showDialog();
        StringRequest stringRequest = new StringRequest(url_get_player_badges, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                jsonToBadges(response);
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

    private void jsonToBadges(String response) {
        try {
            JSONObject jsonObject = new JSONObject(response);
            JSONArray result = jsonObject.getJSONArray("players_badges");
            for (int i = 0; i < result.length(); i++) {
                JSONObject json = result.getJSONObject(i);
                String player_id = json.getString("player_id");
                String badge_id = json.getString("badge_id");
                if (player_id.equals(id)) {
                    if (badge_id.equals("1")) {
                        badgeLevel1.setImageResource(R.mipmap.level1_badge);
                    }
                    if (badge_id.equals("2")) {
                        badgeLevel2.setImageResource(R.mipmap.level2_badge);
                    }
                    if (badge_id.equals("3")) {
                        badgeLevel3.setImageResource(R.mipmap.level3_badge);
                    }
                    if (badge_id.equals("4")) {
                        badgeOffBeforeSleep.setImageResource(R.mipmap.off_sleep_badge);
                    }
                    if (badge_id.equals("5")) {
                        badgeUseStairs.setImageResource(R.mipmap.stairs_badge);
                    }
                    if (badge_id.equals("6")) {
                        badgeEmptyRoom.setImageResource(R.mipmap.empty_room_badge);
                    }
                    if (badge_id.equals("8")) {
                        badgeMoreLess.setImageResource(R.mipmap.more_less_badge);
                    }
                }


            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void jsonToPlayer(String response){
        String teamid="";
        try {
            JSONObject jsonObject = new JSONObject(response);
            JSONArray result = jsonObject.getJSONArray("player");
            JSONObject json = result.getJSONObject(0);
            id = json.getString("id");
            loadPlayerBadges(id);
            playerName.setText(json.getString("name"));
            playerPoints.setText(json.getString("points"));
            this.player.setName(json.getString("name"));
            this.player.setPoints(Integer.parseInt(json.getString("points")));
            teamid = json.getString("team_id");
            if(player.getPoints() >= 50) {
                level2Btn.setImageResource(R.drawable.ic_level);
            }
            if(player.getPoints() >= 180) {
                level3Btn.setImageResource(R.drawable.ic_level);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
        loadTeamData(teamid);
    }

    public void loadTeamData(String teamId) {
        String tag_string_req = "req_loadteam";
        session.setTeam(teamId);
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
                points_player = String.valueOf(player.getPoints());
                // starting background task to update player
                new SavePlayerPoints().execute();
                // starting background task to update team
                team.setPoints(team.getPoints()+extras.getInt("points"));
                points_team = String.valueOf(team.getPoints());
                new SaveTeamPoints().execute();
//                loadPlayerData(username);
            }
            if(extras.containsKey("badge")){
                if(extras.getString("badge").equals("level1")){
                    badge_id = "1";
                    badgeLevel1.setImageResource(R.mipmap.level1_badge);
                    new SavePlayerBadges().execute();
                }

                if(extras.getString("badge").equals("level2")){
                    badge_id = "2";
                    badgeLevel2.setImageResource(R.mipmap.level2_badge);
                    new SavePlayerBadges().execute();
                }

                if(extras.getString("badge").equals("level3")){
                    badge_id = "3";
                    badgeLevel3.setImageResource(R.mipmap.level3_badge);
                    new SavePlayerBadges().execute();
                }

                if(extras.getString("badge").equals("offBeforeSleep")){
                    badge_id = "4";
                    badgeOffBeforeSleep.setImageResource(R.mipmap.off_sleep_badge);
                    new SavePlayerBadges().execute();

                }

                if(extras.getString("badge").equals("emptyRoom")){
                    badge_id = "6";
                    badgeEmptyRoom.setImageResource(R.mipmap.empty_room_badge);
                    new SavePlayerBadges().execute();
                }
                if(extras.getString("badge").equals("useStairs")){
                    badge_id = "5";
                    badgeUseStairs.setImageResource(R.mipmap.stairs_badge);
                    new SavePlayerBadges().execute();
                }
                if(extras.getString("badge").equals("teamPlay")){
                    badge_id = "8";
                    badgeMoreLess.setImageResource(R.mipmap.more_less_badge);
                    new SavePlayerBadges().execute();
                }
            }
            if(easterEggDay){
                easterEggDay=false;
                player.setPoints(player.getPoints()+5);
                points_player = String.valueOf(player.getPoints());
                // starting background task to update player
                new SavePlayerPoints().execute();
                // starting background task to update team
                team.setPoints(team.getPoints()+5);
                points_team = String.valueOf(team.getPoints());
                new SaveTeamPoints().execute();
            }
            if(player.getPoints() >= 50) {
                level2Btn.setImageResource(R.drawable.ic_level);

            }

            if(player.getPoints() >= 180) {
                level3Btn.setImageResource(R.drawable.ic_level);
            }
//                playerRanking.setText(extras.getInt("points"));
        }

        playerName.setText(player.getName());
        teamName.setText(team.getName());
        playerPoints.setText(" "+player.getPoints());
        teamPoints.setText(" "+team.getPoints());
        //playerRanking.setText(""+player.getRanking());
        //teamRanking.setText(""+team.getClassification());

        if(team.getBadgeByName("Level1")!=null)
            badgeLevel1.setImageResource(R.mipmap.level1team_badge);
        else {
            if(player.getBadgeByName("Level1")!=null)
                badgeLevel1.setImageResource(R.mipmap.level1_badge);
        }

        if(team.getBadgeByName("Level2")!=null)
            badgeLevel2.setImageResource(R.mipmap.level2_badge);
        else {
            if(player.getBadgeByName("Level2")!=null)
                badgeLevel2.setImageResource(R.mipmap.level2_badge);
        }

        if(team.getBadgeByName("Level3")!=null)
            badgeLevel1.setImageResource(R.mipmap.level3_badge);
        else {
            if(player.getBadgeByName("Level3")!=null)
                badgeLevel1.setImageResource(R.mipmap.level3_badge);
        }

        if(team.getBadgeByName("OffBeforeSleep")!=null)
            badgeOffBeforeSleep.setImageResource(R.mipmap.off_sleep_team_badge);
        else {
            if(player.getBadgeByName("OffBeforeSleep")!=null)
                badgeOffBeforeSleep.setImageResource(R.mipmap.off_sleep_badge);
        }

        if(team.getBadgeByName("EmptyRoom")!=null)
            badgeEmptyRoom.setImageResource(R.mipmap.empty_room_team_badge);
        else {
            if(player.getBadgeByName("EmptyRoom")!=null)
                badgeEmptyRoom.setImageResource(R.mipmap.empty_room_badge);
        }

        if(team.getBadgeByName("UseStairs")!=null)
            badgeUseStairs.setImageResource(R.mipmap.stairs_badge_team);
        else {
            if(player.getBadgeByName("UseStairs")!=null)
                badgeUseStairs.setImageResource(R.mipmap.stairs_badge);
        }

        if(team.getBadgeByName("TeamPlay")!=null)
            badgeMoreLess.setImageResource(R.mipmap.more_less_team_badge);
        else {
            if(player.getBadgeByName("TeamPlay")!=null)
                badgeMoreLess.setImageResource(R.mipmap.more_less_team_badge);
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

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }
}
