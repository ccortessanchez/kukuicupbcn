package com.ub.tfg.kukuicup.vista;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.ub.tfg.kukuicup.R;
import com.ub.tfg.kukuicup.controller.SessionManager;

import org.w3c.dom.Text;

/**
 * Created by Juanmi on 29/06/2016.
 */
public class EnergyChallengeResume extends Activity {

    int REQUEST_CODE = 1;

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
    private TextView getDaysRemainingLabel;

    private Button primaryBtn;
    private Button secondaryBtn;
    private Button uploadBtn;

    private ImageView pointsImg;
    private ImageView badgeImg;
    private ImageView actionImg;
    private ImageView challActionImg;

    public void onCreate(Bundle savedInstanceState) {
        if(getResources().getBoolean(R.bool.tablet)){
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        }
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
        getDaysRemainingLabel = (TextView)findViewById(R.id.daysRemainingLbl);

        primaryBtn = (Button)findViewById(R.id.primaryBtn);
        secondaryBtn = (Button)findViewById(R.id.secondaryBtn);
        uploadBtn = (Button)findViewById(R.id.upload);

        pointsImg = (ImageView)findViewById(R.id.pointsImg);
        badgeImg = (ImageView)findViewById(R.id.badgeImg);
        actionImg = (ImageView)findViewById(R.id.actionImg);
        challActionImg = (ImageView)findViewById(R.id.enrgChallImg);

        getInfoBychallengeId(levelId, challengeId);

        alertDialog = new AlertDialog.Builder(EnergyChallengeResume.this).create();
        // Setting Dialog Title

        if (badge.equals(getResources().getString(R.string.badgeL1_Chal1))){
            alertDialog.setTitle(getResources().getString(R.string.msgCongratsAlt));
            alertDialog.setIcon(R.mipmap.off_sleep_badge);
            alertDialog.setMessage(getResources().getString(R.string.msgBadge1));
        }

        if (badge.equals(getResources().getString(R.string.badgeL2_Chal1))){
            alertDialog.setTitle(getResources().getString(R.string.msgCongratsAlt));
            alertDialog.setIcon(R.mipmap.stairs_badge);
            alertDialog.setMessage(getResources().getString(R.string.msgBadge2));
        }

        if (badge.equals(getResources().getString(R.string.badgeL2_Chal2))){
            alertDialog.setTitle(getResources().getString(R.string.msgCongratsAlt));
            alertDialog.setIcon(R.mipmap.empty_room_badge);
            alertDialog.setMessage(getResources().getString(R.string.msgBadge3));
        }

        if (badge.equals(getResources().getString(R.string.badgeL3_Chal1))){
            alertDialog.setTitle(getResources().getString(R.string.msgCongratsAlt));
            alertDialog.setIcon(R.mipmap.more_less_team_badge);
            alertDialog.setMessage(getResources().getString(R.string.msgBadge4));
        }


        alertDialog.setButton2(getResources().getString(R.string.btnOk), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                // Write your code here to execute after dialog closed
                Intent intent = new Intent(EnergyChallengeResume.this, MainActivity.class);
                intent.putExtra("points", pointsObt);
                intent.putExtra("badge", badge);
                startActivity(intent);
                primaryBtn.setEnabled(false);
            }
        });

        secondaryBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                initTaskByChallengeId(levelId,challengeId);
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

        uploadBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                primaryBtn.setEnabled(true);
                actionImg.setVisibility(View.INVISIBLE);
                uploadBtn.setVisibility(View.INVISIBLE);
                Toast.makeText(getApplicationContext(),getResources().getString(R.string.msgToastUpload), Toast.LENGTH_SHORT).show();
            }
        });

    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        if(requestCode==REQUEST_CODE) {
            if(resultCode==RESULT_OK) {
                Bundle bundle = new Bundle();
                bundle = data.getExtras();
                Bitmap BMP;
                BMP = (Bitmap)bundle.get("data");
                actionImg.setImageBitmap(BMP);
                actionImg.setVisibility(View.VISIBLE);
                uploadBtn.setEnabled(true);
                uploadBtn.setVisibility(View.VISIBLE);
            }
        }

    }

    public void initTaskByChallengeId(int levelId, int challengeId) {
        switch (levelId) {
            case 1:
                switch (challengeId) {
                    case 0:
                        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                        if(intent.resolveActivity(getPackageManager())!=null) {
                            startActivityForResult(intent,REQUEST_CODE);
                        }
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
                        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                        if(intent.resolveActivity(getPackageManager())!=null) {
                            startActivityForResult(intent,REQUEST_CODE);
                        }
                        break;
                    case 1:
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
                        break;
                    case 1:
                        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                        if(intent.resolveActivity(getPackageManager())!=null) {
                            startActivityForResult(intent,REQUEST_CODE);
                        }
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
                        description.setText(getResources().getString(R.string.resumeL1_enrgChal1));

                        daysRemaining.setText(""+counter);

                        primaryBtn.setText(getResources().getString(R.string.btnDone));
                        pointsObt = 5;
                        badge = getResources().getString(R.string.badgeL1_Chal1);


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
                        badgeImg.setImageResource(R.mipmap.stairs_badge);
                        badgeImg.setVisibility(View.VISIBLE);
                        getDaysRemainingLabel.setVisibility(View.INVISIBLE);
                        description.setText(getResources().getString(R.string.resumeL2_enrgChal1));
                        primaryBtn.setText(getResources().getString(R.string.btnDone));
                        pointsObt = 15;
                        //cambiar nombre medalla
                        badge = getResources().getString(R.string.badgeL2_Chal1);
                        break;
                    case 1:
                        challActionImg.setImageResource(R.drawable.battery);
                        challActionImg.setVisibility(View.VISIBLE);
                        challengeName.setText(getResources().getString(R.string.enrgChalL2_2));
                        reward.setText("+30");
                        getDaysRemainingLabel.setVisibility(View.INVISIBLE);
                        badgeImg.setImageResource(R.mipmap.level1team_badge);
                        badgeImg.setVisibility(View.VISIBLE);
                        description.setText(getResources().getString(R.string.resumeL2_enrgChal2));
                        primaryBtn.setText(getResources().getString(R.string.btnDone));
                        pointsObt = 30;
                        //cambiar nombre medalla
                        badge = "level2Plus";
                        break;
                    case 2:
                        challActionImg.setImageResource(R.drawable.battery);
                        challActionImg.setVisibility(View.VISIBLE);
                        challengeName.setText(getResources().getString(R.string.enrgChalL2_3));
                        reward.setText("+5" + getResources().getString(R.string.resumeDay));
                        badgeImg.setImageResource(R.mipmap.empty_room_badge);
                        badgeImg.setVisibility(View.VISIBLE);
                        description.setText(getResources().getString(R.string.resumeL2_enrgChal3));

                        daysRemaining.setText(getResources().getString(R.string.challDays)+counter);

                        primaryBtn.setText(getResources().getString(R.string.btnDone));
                        pointsObt = 5;
                        //cambiar nombre medalla
                        badge = getResources().getString(R.string.badgeL2_Chal2);
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
                        getDaysRemainingLabel.setVisibility(View.INVISIBLE);
                        badgeImg.setImageResource(R.mipmap.more_less_badge);
                        badgeImg.setVisibility(View.VISIBLE);
                        description.setText(getResources().getString(R.string.resumeL3_enrgChal1));

                        primaryBtn.setText(getResources().getString(R.string.btnDone));
                        //comprobar primera posicion o segunda
                        pointsObt = 100;
                        //cambiar nombre medalla
                        badge = getResources().getString(R.string.badgeL3_Chal1);
                        break;
                    case 1:
                        challActionImg.setImageResource(R.drawable.battery);
                        challActionImg.setVisibility(View.VISIBLE);
                        challengeName.setText(getResources().getString(R.string.enrgChalL3_2));
                        reward.setText("+20");
                        getDaysRemainingLabel.setVisibility(View.INVISIBLE);
                        badgeImg.setImageResource(R.mipmap.logo_bcn);
                        badgeImg.setVisibility(View.VISIBLE);
                        description.setText(getResources().getString(R.string.resumeL3_enrgChal2));
                        primaryBtn.setEnabled(false);
                        primaryBtn.setText(getResources().getString(R.string.btnDone));
                        secondaryBtn.setVisibility(View.VISIBLE);
                        secondaryBtn.setText(getResources().getString(R.string.btnTakePhoto));
                        primaryBtn.setText(getResources().getString(R.string.btnDone));
                        pointsObt = 10;

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