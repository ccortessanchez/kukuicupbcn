package com.ub.tfg.kukuicup.vista;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.ub.tfg.kukuicup.R;

import android.app.Activity;
//import android.content.Context;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnChildClickListener;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

public class MenuActivity extends Activity {
	
	//private Context ctxt;
    private static int LEVEL1_POINTS = 50;
    private static int LEVEL2_POINTS = 180;
    private static int LEVEL3_POINTS = 467;
    ProgressBar levelBar;
	ExpandableListAdapter listAdapter;
    ExpandableListView expListView;
    List<String> listDataHeader;
    HashMap<String, List<String>> listDataChild;
    private Context ctxt;
    private int levelId;
    private int playerPoints;
    private TextView progressLabel;
    private TextView levelLabel;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.menu);
		
		ctxt = getApplicationContext();

        Bundle extras = getIntent().getExtras();

        levelId = extras.getInt("levelId");
        playerPoints = extras.getInt("points");
        levelLabel = (TextView)findViewById(R.id.levelLabel);
        levelBar = (ProgressBar)findViewById(R.id.LevelProgressBar);
        if (levelId == 1) {
            levelLabel.setText(getResources().getString(R.string.level1Label));

            levelBar.setMax(LEVEL1_POINTS);
            levelBar.setProgress(playerPoints);

            progressLabel = (TextView)findViewById(R.id.progressLabel);
            progressLabel.setText(playerPoints+"/"+LEVEL1_POINTS+" Points");
        }

        if (levelId == 2) {
            levelLabel.setText(getResources().getString(R.string.level2Label));

            levelBar.setMax(LEVEL2_POINTS);
            levelBar.setProgress(playerPoints);

            progressLabel = (TextView)findViewById(R.id.progressLabel);
            progressLabel.setText(playerPoints+"/"+LEVEL2_POINTS+" Points");
        }

        if (levelId == 3) {
            levelLabel.setText(getResources().getString(R.string.level3Label));

            levelBar.setMax(LEVEL3_POINTS);
            levelBar.setProgress(playerPoints);

            progressLabel = (TextView)findViewById(R.id.progressLabel);
            progressLabel.setText(playerPoints+"/"+LEVEL3_POINTS+" Points");
        }

        
		// get the listview
        expListView = (ExpandableListView) findViewById(R.id.lvExp);
 
        // preparing list data
        prepareListData();
 
        listAdapter = new ExpandableListAdapter(this, listDataHeader, listDataChild);
 
        // setting list adapter
        expListView.setAdapter(listAdapter);

        // Listview on child click listener
        expListView.setOnChildClickListener(new OnChildClickListener() {

            @Override
            public boolean onChildClick(ExpandableListView parent, View v,
                                        int groupPosition, int childPosition, long id) {
                // TODO Auto-generated method stub

                if (groupPosition==0) {
                    Intent intent = new Intent(ctxt, SavingActionActivity.class);
                    intent.putExtra("levelId",levelId);
                    intent.putExtra("actionId",childPosition);
                    startActivity(intent);
                }
                if (groupPosition==1) {
                    Intent intent = new Intent(ctxt, EnergyChallengeActivity.class);
                    intent.putExtra("levelId",levelId);
                    intent.putExtra("challengeId",childPosition);
                    startActivity(intent);
                }
                if (groupPosition==2) {
                    Intent intent = new Intent(ctxt, VideoActivity.class);
                    intent.putExtra("levelId",levelId);
                    intent.putExtra("videoId",childPosition);
                    startActivity(intent);
                }
                Toast.makeText(
                        getApplicationContext(),
                        listDataHeader.get(groupPosition)
                                + " : "
                                + listDataChild.get(
                                listDataHeader.get(groupPosition)).get(
                                childPosition), Toast.LENGTH_SHORT)
                        .show();
                return false;
            }
        });
	}
    /*
     * Preparing the list data
     */
    private void prepareListData() {
        listDataHeader = new ArrayList<String>();
        listDataChild = new HashMap<String, List<String>>();
 
        // Adding child data
        listDataHeader.add(getResources().getString(R.string.savAction));
        listDataHeader.add(getResources().getString(R.string.enrgChall));
        listDataHeader.add(getResources().getString(R.string.video));

        // Adding child data depending of level
        if(levelId == 1) {
            List<String> action = new ArrayList<String>();
            action.add(getResources().getString(R.string.savActL1_1));
            action.add(getResources().getString(R.string.savActL1_2));
            action.add(getResources().getString(R.string.savActL1_3));


            List<String> challenge = new ArrayList<String>();
            challenge.add(getResources().getString(R.string.enrgChalL1_1));


            List<String> video = new ArrayList<String>();
            video.add(getResources().getString(R.string.L1_video1));


            listDataChild.put(listDataHeader.get(0), action); // Header, Child data
            listDataChild.put(listDataHeader.get(1), challenge);
            listDataChild.put(listDataHeader.get(2), video);
        }

        if(levelId == 2) {
            List<String> action = new ArrayList<String>();
            action.add(getResources().getString(R.string.savActL2_1));
            action.add(getResources().getString(R.string.savActL2_2));
            action.add(getResources().getString(R.string.savActL2_3));
            action.add(getResources().getString(R.string.savActL2_4));


            List<String> challenge = new ArrayList<String>();
            challenge.add(getResources().getString(R.string.enrgChalL2_1));
            challenge.add(getResources().getString(R.string.enrgChalL2_2));
            challenge.add(getResources().getString(R.string.enrgChalL2_3));


            List<String> video = new ArrayList<String>();
            video.add(getResources().getString(R.string.L2_video1));
            video.add(getResources().getString(R.string.L2_video2));


            listDataChild.put(listDataHeader.get(0), action); // Header, Child data
            listDataChild.put(listDataHeader.get(1), challenge);
            listDataChild.put(listDataHeader.get(2), video);
        }

        if(levelId == 3) {
            List<String> action = new ArrayList<String>();
            action.add(getResources().getString(R.string.savActL3_1));
            action.add(getResources().getString(R.string.savActL3_2));


            List<String> challenge = new ArrayList<String>();
            challenge.add(getResources().getString(R.string.enrgChalL3_1));
            challenge.add(getResources().getString(R.string.enrgChalL3_2));


            List<String> video = new ArrayList<String>();
            video.add(getResources().getString(R.string.L3_video1));
            video.add(getResources().getString(R.string.L3_video2));


            listDataChild.put(listDataHeader.get(0), action); // Header, Child data
            listDataChild.put(listDataHeader.get(1), challenge);
            listDataChild.put(listDataHeader.get(2), video);
        }


    }
}
