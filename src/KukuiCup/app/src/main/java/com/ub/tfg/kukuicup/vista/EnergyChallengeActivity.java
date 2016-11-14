package com.ub.tfg.kukuicup.vista;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.ub.tfg.kukuicup.R;
import com.ub.tfg.kukuicup.controller.SessionManager;
import com.ub.tfg.kukuicup.model.EnergyChallenge;

/**
 * Created by Juanmi on 02/06/2016.
 */
public class EnergyChallengeActivity extends Activity {

    public int levelId;
    public int challengeId;
    public int counter;

    private TextView challengeName;
    private TextView reward;
    private TextView description;

    private Button primaryBtn;
    private Button secondaryBtn;

    private ImageView pointsImg;
    private ImageView badgeImg;

    private SessionManager session;
    
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.energy_challenge);

        session = new SessionManager(getApplicationContext());

        Bundle extras = getIntent().getExtras();

        levelId = extras.getInt("levelId");
        challengeId = extras.getInt("challengeId");
        counter = 5;

        challengeName = (TextView)findViewById(R.id.challengeName);
        reward = (TextView)findViewById(R.id.challengeReward);
        description = (TextView)findViewById(R.id.challengeDescription);

        primaryBtn = (Button)findViewById(R.id.primaryBtn);
        secondaryBtn = (Button)findViewById(R.id.secondaryBtn);

        pointsImg = (ImageView)findViewById(R.id.pointsImg);
        badgeImg = (ImageView)findViewById(R.id.badgeImg);

        getInfoBychallengeId(levelId, challengeId);

        primaryBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Intent intent = new Intent(EnergyChallengeActivity.this, EnergyChallengeResume.class);
                intent.putExtra("levelId",levelId);
                intent.putExtra("challengeId",challengeId);
                if(primaryBtn.getText().equals("Start challenge")) {
                    session.setChallengeStart(true);
                    session.setChallengeCnt(5);
                }else {counter = session.getChallengeCnt();}
                intent.putExtra("counter",counter);
                startActivity(intent);
            }
        });

    }

    public void getInfoBychallengeId(int levelId, int challengeId) {

        switch (levelId) {
            case 1:
                switch (challengeId) {
                    case 0:
                        challengeName.setText("Off before sleep");
                        reward.setText("+5/day");
                        badgeImg.setImageResource(R.mipmap.off_sleep_badge);
                        badgeImg.setVisibility(View.VISIBLE);
                        description.setText("Will you get the next challenge? " +
                                "\n" +
                                "Off and unplug all electrical appliances in your room before going to sleep. " +
                                "Perform this action for 5 consecutive days to win the challenge.");

                        //if(energyChallenge.isStarted)primaryBtn.setText("Continue challenge");
                        //else
                        if(session.isChallengeStarted()) primaryBtn.setText("Continue challenge");
                        else primaryBtn.setText("Start challenge");

                        break;
                    case 1:
                        break;
                    case 2:
                        break;
                    default:
                        break;
                }
                break;
            case 2:
                switch (challengeId) {
                    case 0:
                        challengeName.setText("Use stairs");
                        reward.setText("+1/floor");
                        //cambiar nombre medalla
                        badgeImg.setImageResource(R.mipmap.off_sleep_badge);
                        badgeImg.setVisibility(View.VISIBLE);
                        description.setText("Will you get the next challenge? " +
                                "\n" +
                                "Use the stairs instead of using the elevator to win the challenge");

                        if(session.isChallengeStarted()) primaryBtn.setText("Continue challenge");
                        else primaryBtn.setText("Start challenge");
                        break;
                    case 1:
                        challengeName.setText("Change for LED");
                        reward.setText("+30");
                        //cambiar nombre medalla
                        badgeImg.setImageResource(R.mipmap.off_sleep_badge);
                        badgeImg.setVisibility(View.VISIBLE);
                        description.setText("Will you get the next challenge? " +
                                "\n" +
                                "Change any lightbulb for a LED to win the challenge");

                        if(session.isChallengeStarted()) primaryBtn.setText("Continue challenge");
                        else primaryBtn.setText("Start challenge");
                        break;
                    case 2:
                        break;
                    default:
                        break;
                }
                break;
            case 3:
                switch (challengeId) {
                    case 0:
                        challengeName.setText("Team play");
                        reward.setText("+100/winner");
                        //cambiar nombre medalla
                        badgeImg.setImageResource(R.mipmap.off_sleep_badge);
                        badgeImg.setVisibility(View.VISIBLE);
                        description.setText("Will you get the next challenge? " +
                                "\n" +
                                "Team play! In this challenge, each team has to find two types of electronic devices:\n" +
                                "-The device with the higher consumption\n" +
                                "-The device with the lower consumption\n" +
                                "\n" +
                                "The first team to find them will be the challenge winner!" +
                                "50 point for the team in second place.");

                        if(session.isChallengeStarted()) primaryBtn.setText("Continue challenge");
                        else primaryBtn.setText("Start challenge");
                        break;
                    case 1:
                        challengeName.setText("Shirt design");
                        reward.setText("+20");
                        //cambiar nombre medalla
                        badgeImg.setImageResource(R.mipmap.off_sleep_badge);
                        badgeImg.setVisibility(View.VISIBLE);
                        description.setText("Will you get the next challenge? " +
                                "\n" +
                                "Create a design to put on a shirt for the upcoming KukuiCupBCN and take a photo of it to win the challenge");

                        //if(energyChallenge.isStarted)primaryBtn.setText("Continue challenge");
                        //else
                        if(session.isChallengeStarted()) primaryBtn.setText("Continue challenge");
                        else primaryBtn.setText("Start challenge");
                        break;
                    case 2:
                        break;
                    default:
                        break;
                }
                break;
            default:
                break;
        }

    }

}
