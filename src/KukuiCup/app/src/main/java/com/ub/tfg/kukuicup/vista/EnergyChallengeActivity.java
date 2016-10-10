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
                break;
            case 3:
                break;
            default:
                break;
        }

    }

}
