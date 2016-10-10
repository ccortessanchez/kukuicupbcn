package com.ub.tfg.kukuicup.vista;

import android.app.Activity;
import android.app.AlertDialog;
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

import org.w3c.dom.Text;

/**
 * Created by Juanmi on 29/06/2016.
 */
public class EnergyChallengeResume extends Activity {

    public int levelId;
    public int challengeId;
    public int pointsObt;
    public int counter;
    public String badge;

    SessionManager session;
    private AlertDialog alertDialog;

    private TextView challengeName;
    private TextView reward;
    private TextView description;
    private TextView daysRemaining;

    private Button primaryBtn;

    private ImageView pointsImg;
    private ImageView badgeImg;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.energy_challenge_resume);

        Bundle extras = getIntent().getExtras();
        session = new SessionManager(getApplicationContext());

        levelId = extras.getInt("levelId");
        challengeId = extras.getInt("challengeId");
        counter = extras.getInt("counter");
        badge = "none";

        challengeName = (TextView)findViewById(R.id.challengeName);
        reward = (TextView)findViewById(R.id.challengeReward);
        description = (TextView)findViewById(R.id.challengeDescription);
        daysRemaining = (TextView)findViewById(R.id.daysRemaining);

        primaryBtn = (Button)findViewById(R.id.primaryBtn);

        pointsImg = (ImageView)findViewById(R.id.pointsImg);
        badgeImg = (ImageView)findViewById(R.id.badgeImg);

        getInfoBychallengeId(levelId, challengeId);

        alertDialog = new AlertDialog.Builder(EnergyChallengeResume.this).create();
        // Setting Dialog Title
        alertDialog.setTitle("Congratulations!");
        alertDialog.setIcon(R.mipmap.off_sleep_badge);
        alertDialog.setMessage("You have won the bagde Off Before Sleep.");

        alertDialog.setButton2("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                // Write your code here to execute after dialog closed
                Intent intent = new Intent(EnergyChallengeResume.this, MainActivity.class);
                intent.putExtra("points", pointsObt);
                intent.putExtra("badge", badge);
                startActivity(intent);
                primaryBtn.setEnabled(false);
            }
        });

        primaryBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Intent intent = new Intent(EnergyChallengeResume.this, MainActivity.class);
                intent.putExtra("points", pointsObt);
                session.setChallengeCnt(counter-1);
                primaryBtn.setEnabled(false);
                if(counter<=1){
                    session.setChallengeStart(false);
                    alertDialog.show();
                }else
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
                        description.setText("Turn off and unplug all electrical appliances in your room before going to sleep. ");

                        daysRemaining.setText(""+counter);

                        primaryBtn.setText("Done!");
                        pointsObt = 5;
                        badge = "offBeforeSleep";


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