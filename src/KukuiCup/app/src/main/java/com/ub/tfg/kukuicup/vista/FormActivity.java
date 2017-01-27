package com.ub.tfg.kukuicup.vista;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;
import com.ub.tfg.kukuicup.R;
import com.ub.tfg.kukuicup.controller.Controller;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import cz.msebera.android.httpclient.NameValuePair;
import cz.msebera.android.httpclient.message.BasicNameValuePair;

/**
 * Created by Juanmi on 29/06/2016.
 */
public class FormActivity extends Activity {

    public int levelId;
    public int formId;
    public int pointsObt;
    public String badgeObt;

    private TextView formName;
    private TextView reward;
    private TextView question1;
    private TextView question2;
    private TextView question3;

    private Button primaryBtn;

    private ImageView pointsImg;
    private ImageView badgeImg;

    private String answer1, answer2, answer3;

    private int jokeId;
    private int form_attempt;

    private RadioGroup radioGr1, radioGr2, radioGr3;
    private RadioButton answer1btn, answer2btn, answer3btn;
    private RadioButton answer1a, answer1b, answer1c, answer2a, answer2b, answer2c, answer3a, answer3b, answer3c;

    private AlertDialog alertDialog;
    private AlertDialog alertFormAttempts;

    public void onCreate(Bundle savedInstanceState) {
        if(getResources().getBoolean(R.bool.tablet)){
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        }
        super.onCreate(savedInstanceState);
        setContentView(R.layout.form);

        Bundle extras = getIntent().getExtras();
        Controller control = new Controller();
        String localhost = control.config.LOCALHOST;

        levelId = extras.getInt("levelId");
        formId = extras.getInt("formId");
        pointsObt = extras.getInt("points");
        badgeObt = "none";

        formName = (TextView)findViewById(R.id.formName);
        form_attempt = 1;
        reward = (TextView)findViewById(R.id.formReward);
        question1 = (TextView)findViewById(R.id.quest1);
        question2 = (TextView)findViewById(R.id.quest2);
        question3 = (TextView)findViewById(R.id.quest3);

        primaryBtn = (Button)findViewById(R.id.primaryBtn);

        pointsImg = (ImageView)findViewById(R.id.pointsImg);
        badgeImg = (ImageView)findViewById(R.id.badgeImg);

        radioGr1 = (RadioGroup)findViewById(R.id.question1RG);
        radioGr2 = (RadioGroup)findViewById(R.id.question2RG);
        radioGr3 = (RadioGroup)findViewById(R.id.question3RG);
        answer1a = (RadioButton)findViewById(R.id.answer1a);
        answer1b = (RadioButton)findViewById(R.id.answer1b);
        answer1c = (RadioButton)findViewById(R.id.answer1c);
        answer2a = (RadioButton)findViewById(R.id.answer2a);
        answer2b = (RadioButton)findViewById(R.id.answer2b);
        answer2c = (RadioButton)findViewById(R.id.answer2c);
        answer3a = (RadioButton)findViewById(R.id.answer3a);
        answer3b = (RadioButton)findViewById(R.id.answer3b);
        answer3c = (RadioButton)findViewById(R.id.answer3c);

        alertDialog = new AlertDialog.Builder(FormActivity.this).create();
        // Setting Dialog Title
        alertDialog.setTitle(getResources().getString(R.string.msgCongratsAlt));

        // Setting OK Button
        alertDialog.setButton2(getResources().getString(R.string.btnOk), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                // Write your code here to execute after dialog closed
                Intent intent = new Intent(FormActivity.this, JokeActivity.class);
                intent.putExtra("levelId",levelId);
                intent.putExtra("formId",formId);
                intent.putExtra("points",pointsObt);
                intent.putExtra("badge",badgeObt);
                startActivity(intent);
            }
        });


        alertFormAttempts = new AlertDialog.Builder(FormActivity.this).create();
        // Setting Dialog Title
        alertFormAttempts.setTitle(getResources().getString(R.string.msgQuizAttemptTitle));

        alertFormAttempts.setMessage(getResources().getString(R.string.msgQuizAttempt));

        // Setting OK Button
        alertFormAttempts.setButton2(getResources().getString(R.string.btnOk), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                // Write your code here to execute after dialog closed
                Intent intent = new Intent(FormActivity.this, MainActivity.class);
                intent.putExtra("levelId",levelId);
                intent.putExtra("points",10);
                startActivity(intent);
            }
        });

        getInfoByFormId(levelId, formId);

        primaryBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {

                if(answer1btn.isChecked()&&answer2btn.isChecked()&&answer3btn.isChecked()) {
                    //Correct//Show alert dialog
                    alertDialog.show();

                }else {
                    //Try again
                    if (form_attempt == 0) {
                        alertFormAttempts.show();
                    }
                    else {
                        form_attempt = form_attempt - 1;
                        Toast.makeText(FormActivity.this, getResources().getString(R.string.msgToastQuiz), Toast.LENGTH_SHORT).show();
                    }

                }
            }
        });

    }

    public void getInfoByFormId(int levelId, int formId) {

        switch (levelId) {
            case 1:
                switch (formId) {
                    case 0:
                        formName.setText(getResources().getString(R.string.L1_video1));
                        reward.setText("+30");
                        badgeImg.setImageResource(R.mipmap.level1_badge);
                        badgeImg.setVisibility(View.VISIBLE);

                        answer1btn = (RadioButton)findViewById(R.id.answer1b);
                        answer2btn = (RadioButton)findViewById(R.id.answer2a);
                        answer3btn = (RadioButton)findViewById(R.id.answer3a);

                        jokeId = 1;

                        question1.setText(getResources().getString(R.string.quest1_L1_video1));
                        answer1a.setText(getResources().getString(R.string.L1V1_1A));
                        answer1b.setText(getResources().getString(R.string.L1V1_1B));
                        answer1c.setText(getResources().getString(R.string.L1V1_1C));
                        question2.setText(getResources().getString(R.string.quest2_L1_video1));
                        answer2a.setText(getResources().getString(R.string.L1V1_2A));
                        answer2b.setText(getResources().getString(R.string.L1V1_2B));
                        answer2c.setText(getResources().getString(R.string.L1V1_2C));
                        question3.setText(getResources().getString(R.string.quest3_L1_video1));
                        answer3a.setText(getResources().getString(R.string.L1V1_3A));
                        answer3b.setText(getResources().getString(R.string.L1V1_3B));
                        answer3c.setText(getResources().getString(R.string.L1V1_3C));

                        // Setting Dialog Message
                        alertDialog.setMessage(getResources().getString(R.string.msgQuiz1L1));
                        // Setting Icon to Dialog
                        alertDialog.setIcon(R.mipmap.level1_badge);
                        pointsObt+=30;
                        badgeObt = "level1";

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
                switch (formId) {
                    case 0:
                        formName.setText(getResources().getString(R.string.L2_video1));
                        reward.setText("+30");
                        badgeImg.setImageResource(R.mipmap.level2_badge);
                        badgeImg.setVisibility(View.VISIBLE);

                        answer1btn = (RadioButton)findViewById(R.id.answer1c);
                        answer2btn = (RadioButton)findViewById(R.id.answer2a);
                        answer3btn = (RadioButton)findViewById(R.id.answer3b);

                        jokeId = 1;

                        question1.setText(getResources().getString(R.string.quest1_L2_video1));
                        answer1a.setText(getResources().getString(R.string.L2V1_1A));
                        answer1b.setText(getResources().getString(R.string.L2V1_1B));
                        answer1c.setText(getResources().getString(R.string.L2V1_1C));
                        question2.setText(getResources().getString(R.string.quest2_L2_video1));
                        answer2a.setText(getResources().getString(R.string.L2V1_2A));
                        answer2b.setText(getResources().getString(R.string.L2V1_2B));
                        answer2c.setText(getResources().getString(R.string.L2V1_2C));
                        question3.setText(getResources().getString(R.string.quest3_L2_video1));
                        answer3a.setText(getResources().getString(R.string.L2V1_3A));
                        answer3b.setText(getResources().getString(R.string.L2V1_3B));
                        answer3c.setText(getResources().getString(R.string.L2V1_3C));

                        // Setting Dialog Message
                        alertDialog.setMessage(getResources().getString(R.string.msgQuiz1L2));
                        // Setting Icon to Dialog
                        alertDialog.setIcon(R.mipmap.level2_badge);
                        pointsObt+=30;
                        badgeObt = "level2";
                        break;

                    case 1:
                        formName.setText(getResources().getString(R.string.L2_video2));
                        reward.setText("+30");
                        badgeImg.setVisibility(View.INVISIBLE);

                        answer1btn = (RadioButton)findViewById(R.id.answer1c);
                        answer2btn = (RadioButton)findViewById(R.id.answer2a);
                        answer3btn = (RadioButton)findViewById(R.id.answer3b);

                        jokeId = 1;

                        question1.setText(getResources().getString(R.string.quest1_L2_video2));
                        answer1a.setText(getResources().getString(R.string.L2V2_1A));
                        answer1b.setText(getResources().getString(R.string.L2V2_1B));
                        answer1c.setText(getResources().getString(R.string.L2V2_1C));
                        question2.setText(getResources().getString(R.string.quest2_L2_video2));
                        answer2a.setText(getResources().getString(R.string.L2V2_2A));
                        answer2b.setText(getResources().getString(R.string.L2V2_2B));
                        answer2c.setText(getResources().getString(R.string.L2V2_2C));
                        question3.setText(getResources().getString(R.string.quest3_L2_video2));
                        answer3a.setText(getResources().getString(R.string.L2V2_3A));
                        answer3b.setText(getResources().getString(R.string.L2V2_3B));
                        answer3c.setText(getResources().getString(R.string.L2V2_3C));

                        // Setting Dialog Message
                        alertDialog.setMessage(getResources().getString(R.string.msgQuiz2L2));
                        pointsObt+=30;

                        break;
                    case 2:
                        break;
                    default:
                        break;
                }
                break;
            case 3:
                switch (formId) {
                    case 0:
                        formName.setText(getResources().getString(R.string.L3_video1));
                        reward.setText("+30");
                        badgeImg.setImageResource(R.mipmap.level1team_badge);
                        badgeImg.setVisibility(View.VISIBLE);

                        answer1btn = (RadioButton)findViewById(R.id.answer1c);
                        answer2btn = (RadioButton)findViewById(R.id.answer2b);
                        answer3btn = (RadioButton)findViewById(R.id.answer3a);

                        jokeId = 1;

                        question1.setText(getResources().getString(R.string.quest1_L3_video1));
                        answer1a.setText(getResources().getString(R.string.L3V1_1A));
                        answer1b.setText(getResources().getString(R.string.L3V1_1B));
                        answer1c.setText(getResources().getString(R.string.L3V1_1C));
                        question2.setText(getResources().getString(R.string.quest2_L3_video1));
                        answer2a.setText(getResources().getString(R.string.L3V1_2A));
                        answer2b.setText(getResources().getString(R.string.L3V1_2B));
                        answer2c.setText(getResources().getString(R.string.L3V1_2C));
                        question3.setText(getResources().getString(R.string.quest3_L3_video1));
                        answer3a.setText(getResources().getString(R.string.L3V1_3A));
                        answer3b.setText(getResources().getString(R.string.L3V1_3B));
                        answer3c.setText(getResources().getString(R.string.L3V1_3C));

                        // Setting Dialog Message
                        alertDialog.setMessage(getResources().getString(R.string.msgQuiz1L3));
                        // Setting Icon to Dialog
                        alertDialog.setIcon(R.mipmap.level1_badge);
                        pointsObt+=30;
                        badgeObt = "level3+";
                        break;

                    case 1:
                        formName.setText(getResources().getString(R.string.L3_video2));
                        reward.setText("+30");
                        badgeImg.setImageResource(R.mipmap.level3_badge);
                        badgeImg.setVisibility(View.VISIBLE);

                        answer1btn = (RadioButton)findViewById(R.id.answer1b);
                        answer2btn = (RadioButton)findViewById(R.id.answer2c);
                        answer3btn = (RadioButton)findViewById(R.id.answer3a);

                        jokeId = 1;
                        question1.setText(getResources().getString(R.string.quest3_L3_video2));
                        answer1a.setText(getResources().getString(R.string.L3V2_3A));
                        answer1b.setText(getResources().getString(R.string.L3V2_3B));
                        answer1c.setText(getResources().getString(R.string.L3V2_3C));
                        question2.setText(getResources().getString(R.string.quest2_L3_video2));
                        answer2a.setText(getResources().getString(R.string.L3V2_2A));
                        answer2b.setText(getResources().getString(R.string.L3V2_2B));
                        answer2c.setText(getResources().getString(R.string.L3V2_2C));
                        question3.setText(getResources().getString(R.string.quest3_L3_video2));
                        question3.setText(getResources().getString(R.string.quest1_L3_video2));
                        answer3a.setText(getResources().getString(R.string.L3V2_1A));
                        answer3b.setText(getResources().getString(R.string.L3V2_1B));
                        answer3c.setVisibility(View.INVISIBLE);


                        // Setting Dialog Message
                        alertDialog.setMessage(getResources().getString(R.string.msgQuiz2L3));
                        // Setting Icon to Dialog
                        alertDialog.setIcon(R.mipmap.level3_badge);
                        pointsObt+=30;
                        badgeObt = "level3";
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
