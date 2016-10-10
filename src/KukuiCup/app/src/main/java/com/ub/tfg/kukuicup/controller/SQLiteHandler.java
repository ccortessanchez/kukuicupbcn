package com.ub.tfg.kukuicup.controller;

/**
 * Created by Juanmi on 27/06/2016.
 */
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.HashMap;

public class SQLiteHandler extends SQLiteOpenHelper {

    private static final String TAG = SQLiteHandler.class.getSimpleName();

    // All Static variables
    // Database Version
    private static final int DATABASE_VERSION = 1;

    // Database Name
    private static final String DATABASE_NAME = "kukuicupbcn";

    // Login table name
    private static final String TABLE_PLAYER = "player";

    // Login Table Columns names
    private static final String KEY_ID = "id";
    private static final String KEY_NAME = "name";
    private static final String KEY_POINTS = "points";
    private static final String KEY_RANKING = "ranking";

    // Tournament table name
    private static final String TABLE_TOURNAMENT = "tournament";

    // Tournament Table Columns names
    private static final String KEY_TID = "tid";
    private static final String KEY_INITDATE = "initdate";
    private static final String KEY_ENDDATE = "enddate";

    public SQLiteHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    // Creating Tables
    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_LOGIN_TABLE = "CREATE TABLE " + TABLE_PLAYER + "("
                + KEY_ID + " INTEGER PRIMARY KEY," + KEY_NAME + KEY_POINTS + KEY_RANKING+")";
        db.execSQL(CREATE_LOGIN_TABLE);

        String CREATE_TOURNAMENT_TABLE = "CREATE TABLE " + TABLE_TOURNAMENT + "("
                + KEY_TID + " INTEGER PRIMARY KEY," + KEY_INITDATE + KEY_ENDDATE+")";
        db.execSQL(CREATE_TOURNAMENT_TABLE);

        Log.d(TAG, "Database tables created");
    }

    // Upgrading database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PLAYER);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_TOURNAMENT);

        // Create tables again
        onCreate(db);
    }

    /**
     * Storing user details in database
     * */
    public void addPlayer(String name) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_NAME, name); // Name

        // Inserting Row
        long id = db.insert(TABLE_PLAYER, null, values);
        db.close(); // Closing database connection

        Log.d(TAG, "New player inserted into sqlite: " + id);
    }

    public void addTournament(String initdate, String enddate) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_INITDATE, initdate);
        values.put(KEY_ENDDATE, enddate);

        // Inserting Row
        long tid = db.insert(TABLE_TOURNAMENT, null, values);
        db.close(); // Closing database connection

        Log.d(TAG, "Tournament inserted into sqlite: " + tid);
    }

    /**
     * Getting user data from database
     * */
    public HashMap<String, String> getPlayerDetails() {
        HashMap<String, String> player = new HashMap<String, String>();
        String selectQuery = "SELECT  * FROM " + TABLE_PLAYER;

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        // Move to first row
        cursor.moveToFirst();
        if (cursor.getCount() > 0) {
            player.put("name", cursor.getString(1));
//            user.put("points", cursor.getString(2));
//            user.put("team_id", cursor.getString(3));
        }
        cursor.close();
        db.close();
        // return user
        Log.d(TAG, "Fetching player from Sqlite: " + player.toString());

        return player;
    }

    public HashMap<String, String> getTournamentDetails() {
        HashMap<String, String> user = new HashMap<String, String>();
        String selectQuery = "SELECT  * FROM " + TABLE_TOURNAMENT;

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        // Move to first row
        cursor.moveToFirst();
        if (cursor.getCount() > 0) {
            user.put("initdate", cursor.getString(1));
            user.put("enddate", cursor.getString(2));
        }
        cursor.close();
        db.close();
        // return user
        Log.d(TAG, "Fetching player from Sqlite: " + user.toString());

        return user;
    }

    /**
     * Re crate database Delete all tables and create them again
     * */
    public void deletePlayers() {
        SQLiteDatabase db = this.getWritableDatabase();
        // Delete All Rows
        db.delete(TABLE_PLAYER, null, null);
        db.close();

        Log.d(TAG, "Deleted all player info from sqlite");
    }

}
