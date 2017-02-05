package com.ub.tfg.kukuicup.vista;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
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
 * Created by Juanmi on 02/06/2016. Edited by Carlos Cortes.
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
    private ImageView challActionImg;

    private SessionManager session;
    
    public void onCreate(Bundle savedInstanceState) {
        if(getResources().getBoolean(R.bool.tablet)){
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        }
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
        challActionImg = (ImageView)findViewById(R.id.enrgChallImg);

        getInfoBychallengeId(levelId, challengeId);

        primaryBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Intent intent = new Intent(EnergyChallengeActivity.this, EnergyChallengeResume.class);
                intent.putExtra("levelId",levelId);
                intent.putExtra("challengeId",challengeId);
                if(primaryBtn.getText().equals(getResources().getString(R.string.btnStart))) {
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
                        challActionImg.setImageResource(R.drawable.battery);
                        challActionImg.setVisibility(View.VISIBLE);
                        challengeName.setText(getResources().getString(R.string.enrgChalL1_1));
                        reward.setText("+5" + getResources().getString(R.string.resumeDay));
                        badgeImg.setImageResource(R.mipmap.off_sleep_badge);
                        badgeImg.setVisibility(View.VISIBLE);
                        description.setText(getResources().getString(R.string.descL1_enrgChal1));

                        //if(energyChallenge.isStarted)primaryBtn.setText("Continue challenge");
                        //else
                        if(session.isChallengeStarted()) primaryBtn.setText(getResources().getString(R.string.btnContinue));
                        else primaryBtn.setText(getResources().getString(R.string.btnStart));

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
                        challActionImg.setImageResource(R.drawable.battery);
                        challActionImg.setVisibility(View.VISIBLE);
                        challengeName.setText(getResources().getString(R.string.enrgChalL2_1));
                        reward.setText("+15");
                        //cambiar nombre medalla
                        badgeImg.setImageResource(R.mipmap.stairs_badge);
                        badgeImg.setVisibility(View.VISIBLE);
                        description.setText(getResources().getString(R.string.descL2_enrgChal1));
                        if(session.isChallengeStarted()) primaryBtn.setText(getResources().getString(R.string.btnContinue));
                        else primaryBtn.setText(getResources().getString(R.string.btnStart));
                        break;

                    case 1:
                        challActionImg.setImageResource(R.drawable.battery);
                        challActionImg.setVisibility(View.VISIBLE);
                        challengeName.setText(getResources().getString(R.string.enrgChalL2_2));
                        reward.setText("+30");
                        //cambiar nombre medalla
                        badgeImg.setImageResource(R.mipmap.level1team_badge);
                        badgeImg.setVisibility(View.VISIBLE);
                        description.setText(getResources().getString(R.string.descL2_enrgChal2));

                        if(session.isChallengeStarted()) primaryBtn.setText(getResources().getString(R.string.btnContinue));
                        else primaryBtn.setText(getResources().getString(R.string.btnStart));
                        break;
                    case 2:
                        challActionImg.setImageResource(R.drawable.battery);
                        challActionImg.setVisibility(View.VISIBLE);
                        challengeName.setText(getResources().getString(R.string.enrgChalL2_3));
                        reward.setText("+5" + getResources().getString(R.string.resumeDay));
                        //cambiar nombre medalla
                        badgeImg.setImageResource(R.mipmap.empty_room_badge);
                        badgeImg.setVisibility(View.VISIBLE);
                        description.setText(getResources().getString(R.string.descL2_enrgChal3));

                        if(session.isChallengeStarted()) primaryBtn.setText(getResources().getString(R.string.btnContinue));
                        else primaryBtn.setText(getResources().getString(R.string.btnStart));

                        break;
                    default:
                        break;
                }
                break;
            case 3:
                switch (challengeId) {
                    case 0:
                        challActionImg.setImageResource(R.drawable.battery);
                        challActionImg.setVisibility(View.VISIBLE);
                        challengeName.setText(getResources().getString(R.string.enrgChalL3_1));
                        reward.setText("+100" + getResources().getString(R.string.resumeTeam));
                        //cambiar nombre medalla
                        badgeImg.setImageResource(R.mipmap.more_less_badge);
                        badgeImg.setVisibility(View.VISIBLE);
                        description.setText(getResources().getString(R.string.descL3_enrgChal1));

                        if(session.isChallengeStarted()) primaryBtn.setText(getResources().getString(R.string.btnContinue));
                        else primaryBtn.setText(getResources().getString(R.string.btnStart));
                        break;
                    case 1:
                        challActionImg.setImageResource(R.drawable.battery);
                        challActionImg.setVisibility(View.VISIBLE);
                        challengeName.setText(getResources().getString(R.string.enrgChalL3_2));
                        reward.setText("+20");
                        //cambiar nombre medalla
                        badgeImg.setImageResource(R.mipmap.logo_bcn);
                        badgeImg.setVisibility(View.VISIBLE);
                        description.setText(getResources().getString(R.string.descL3_enrgChal2));

                        //if(energyChallenge.isStarted)primaryBtn.setText("Continue challenge");
                        //else
                        if(session.isChallengeStarted()) primaryBtn.setText(getResources().getString(R.string.btnContinue));
                        else primaryBtn.setText(getResources().getString(R.string.btnStart));
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
