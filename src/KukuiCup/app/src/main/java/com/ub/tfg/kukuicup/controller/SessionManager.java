package com.ub.tfg.kukuicup.controller;

/**
 * Created by Juanmi on 27/06/2016.
 */
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.util.Log;

public class SessionManager {
    // LogCat tag
    private static String TAG = SessionManager.class.getSimpleName();

    // Shared Preferences
    SharedPreferences pref;

    Editor editor;
    Context _context;

    // Shared pref mode
    int PRIVATE_MODE = 0;

    // Shared preferences file name
    private static final String PREF_NAME = "KukuiCupLogin";

    private static final String KEY_IS_LOGGEDIN = "isLoggedIn";
    public static final String KEY_EASTEREGG_DAY = "easterEggDay";
    public static final String KEY_ALL_ACTIVITIES = "unlockedAll";
    public static final String KEY_NAME = "name";

    public static final String KEY_PLAYER = "player";
    public static final String KEY_TEAM = "team";

    public static final String KEY_CHALLENGE_ST = "challengeStatus";
    public static final String KEY_CHALLENGE_CNT = "challengeCnt";

    public static final String KEY_END_DATE = "endDate";


    public SessionManager(Context context) {
        this._context = context;
        pref = _context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = pref.edit();
    }

    public void setLogin(boolean isLoggedIn) {

        editor.putBoolean(KEY_IS_LOGGEDIN, isLoggedIn);

        // commit changes
        editor.commit();

        Log.d(TAG, "User login session modified!");
    }

    public boolean isLoggedIn(){
        return pref.getBoolean(KEY_IS_LOGGEDIN, false);
    }

    public void setChallengeStart(boolean isStarted) {
        editor.putBoolean(KEY_CHALLENGE_ST, isStarted);
        editor.commit();
    }

    public boolean isChallengeStarted() {return  pref.getBoolean(KEY_CHALLENGE_ST,false);}

    public void setChallengeCnt(int counter) {
        editor.putInt(KEY_CHALLENGE_CNT, counter);
        editor.commit();
    }

    public int getChallengeCnt() {return  pref.getInt(KEY_CHALLENGE_CNT,5);}

    public void setName(String name) {
        editor.putString(KEY_NAME, name);
        editor.commit();
    }

    public String getName() {return pref.getString(KEY_NAME, "null");}

    public void setTeam(String team) {
        editor.putString(KEY_TEAM, team);
        editor.commit();
    }

    public String getTeam() {return pref.getString(KEY_TEAM, "null");}

    public void setEasterEggDay(int day) {

        editor.putInt(KEY_EASTEREGG_DAY, day);
        editor.commit();
    }

    public int getEasterEggDay() {return pref.getInt(KEY_EASTEREGG_DAY, 0);}

    public void setEndDay(String endDate) {
        editor.putString(KEY_END_DATE, endDate);
        editor.commit();
    }

    public String getEndDate() {return pref.getString(KEY_END_DATE, "null");}

    public void setAllUnlocked(boolean isUnlocked) {
        editor.putBoolean(KEY_ALL_ACTIVITIES, isUnlocked);
        editor.commit();
    }

    public boolean isAllUnlocked() {return  pref.getBoolean(KEY_ALL_ACTIVITIES,false);}

}
