package com.ub.tfg.kukuicup.vista;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.share.widget.LikeView;
import com.ub.tfg.kukuicup.R;
import com.ub.tfg.kukuicup.controller.Controller;
import com.ub.tfg.kukuicup.controller.SessionManager;
import com.ub.tfg.kukuicup.model.JSONParser;
import com.ub.tfg.kukuicup.model.SavingAction;
import com.facebook.FacebookSdk;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import cz.msebera.android.httpclient.NameValuePair;
import cz.msebera.android.httpclient.message.BasicNameValuePair;

/**
 * Created by Juanmi on 02/06/2016. Edited by Carlos Cortes.
 */
public class SavingActionActivity extends Activity {

    int REQUEST_CODE = 1;
    private SessionManager session;

    public int levelId;
    public int actionId;
    public int pointsObt;

    private TextView actionName;
    private TextView reward;
    private TextView description;

    private Button primaryBtn;
    private Button secondaryBtn;
    private Button uploadBtn;
    private LikeView likeView;

    private ImageView pointsImg;
    private ImageView badgeImg;
    private ImageView actionImg;
    private ImageView savActionImg;
    private String activityName;
    private String username;
    private static String url_activity_reg;
    JSONParser jsonParser = new JSONParser();

    private RadioGroup radioGr;
    private RadioButton answer1btn, answer2btn, answer3btn;
    private RadioButton answera, answerb, answerc;

    private AlertDialog alertDialog;
    private AlertDialog tip;

    public void onCreate(Bundle savedInstanceState) {
        if(getResources().getBoolean(R.bool.tablet)){
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        }
        super.onCreate(savedInstanceState);
        setContentView(R.layout.saving_action);

        FacebookSdk.sdkInitialize(getApplicationContext());

        Bundle extras = getIntent().getExtras();

        levelId = extras.getInt("levelId");
        actionId = extras.getInt("actionId");

        Controller control = new Controller();
        session = new SessionManager(getApplicationContext());
        username = session.getName();
        String localhost = control.config.LOCALHOST;
        url_activity_reg = "http://"+localhost+"/create_reg_activity.php";

        actionName = (TextView) findViewById(R.id.actionName);
        reward = (TextView) findViewById(R.id.actionReward);
        description = (TextView) findViewById(R.id.actionDescription);

        primaryBtn = (Button) findViewById(R.id.primaryBtn);
        secondaryBtn = (Button) findViewById(R.id.secondaryBtn);
        uploadBtn = (Button) findViewById(R.id.upload);
        likeView = (LikeView) findViewById(R.id.likeView);
        likeView.setLikeViewStyle(LikeView.Style.STANDARD);
        likeView.setAuxiliaryViewPosition(LikeView.AuxiliaryViewPosition.INLINE);
        likeView.setObjectIdAndType("https://www.facebook.com/kukuicup/?fref=ts", LikeView.ObjectType.OPEN_GRAPH);
        likeView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                primaryBtn.setEnabled(true);
            }
        });

        pointsImg = (ImageView) findViewById(R.id.pointsImg);
        badgeImg = (ImageView) findViewById(R.id.badgeImg);
        actionImg = (ImageView) findViewById(R.id.actionImg);
        savActionImg = (ImageView) findViewById(R.id.savActionImg);

        radioGr = (RadioGroup) findViewById(R.id.RGActlvl3);
        answera = (RadioButton) findViewById(R.id.answera);
        answerb = (RadioButton) findViewById(R.id.answerb);
        answerc = (RadioButton) findViewById(R.id.answerc);

        alertDialog = new AlertDialog.Builder(SavingActionActivity.this).create();
        // Setting Dialog Title
        alertDialog.setTitle(getResources().getString(R.string.msgCongrats));
        // Setting Icon to Dialog
        alertDialog.setIcon(R.drawable.ic_launcher);
        // Setting OK Button
        alertDialog.setButton2(getResources().getString(R.string.btnOk), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                // Write your code here to execute after dialog closed
                new SavePlayerActivity().execute();
                Intent intent = new Intent(SavingActionActivity.this, MainActivity.class);
                intent.putExtra("points", pointsObt);
                startActivity(intent);
                primaryBtn.setEnabled(false);
            }
        });


        tip = new AlertDialog.Builder(SavingActionActivity.this).create();
        tip.setTitle(getResources().getString(R.string.savTip));
        tip.setIcon(R.drawable.piggybank);


        tip.setButton2(getResources().getString(R.string.btnOk), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                // Write your code here to execute after dialog closed
            }
        });

        getInfoByActionId(levelId, actionId);

        secondaryBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                initTaskByActionId(levelId, actionId);
            }
        });

        primaryBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                alertDialog.show();
            }
        });

        uploadBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                primaryBtn.setEnabled(true);
                actionImg.setVisibility(View.INVISIBLE);
                uploadBtn.setVisibility(View.INVISIBLE);
                Toast.makeText(getApplicationContext(), getResources().getString(R.string.msgToastUpload), Toast.LENGTH_SHORT).show();
            }
        });

        answera.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (actionId == 0) {
                    tip.setMessage(getResources().getString(R.string.descL3_saveAct1_tip1));
                    tip.show();
                    //Toast.makeText(getApplicationContext(), getResources().getString(R.string.descL3_saveAct1_tip1), Toast.LENGTH_LONG).show();
                }
                if (actionId == 1) {
                    tip.setMessage(getResources().getString(R.string.descL3_saveAct2_tip1));
                    tip.show();
                    //Toast.makeText(getApplicationContext(), getResources().getString(R.string.descL3_saveAct2_tip1), Toast.LENGTH_LONG).show();
                }
            }
        });

        answerb.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (actionId == 0) {
                    tip.setMessage(getResources().getString(R.string.descL3_saveAct1_tip2));
                    tip.show();
                    //Toast.makeText(getApplicationContext(), getResources().getString(R.string.descL3_saveAct1_tip2), Toast.LENGTH_LONG).show();

                }
                if (actionId == 1) {
                    tip.setMessage(getResources().getString(R.string.descL3_saveAct2_tip2));
                    tip.show();
                    //Toast.makeText(getApplicationContext(), getResources().getString(R.string.descL3_saveAct2_tip2), Toast.LENGTH_LONG).show();
                }
            }
        });

        answerc.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (actionId == 0) {
                    tip.setMessage(getResources().getString(R.string.descL3_saveAct1_tip3));
                    tip.show();
                    //Toast.makeText(getApplicationContext(), getResources().getString(R.string.descL3_saveAct1_tip3), Toast.LENGTH_LONG).show();

                }
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

    public void initTaskByActionId(int levelId, int actionId) {
        switch (levelId) {
            case 1:
                switch (actionId) {
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
                switch (actionId) {
                    case 0:
                        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                        if(intent.resolveActivity(getPackageManager())!=null) {
                            startActivityForResult(intent,REQUEST_CODE);
                        }
                        break;
                    case 1:
                        break;
                    case 2:
                        Intent intent2 = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                        if(intent2.resolveActivity(getPackageManager())!=null) {
                            startActivityForResult(intent2,REQUEST_CODE);
                        }
                        break;
                    case 3:
                        Intent intent3 = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                        if(intent3.resolveActivity(getPackageManager())!=null) {
                            startActivityForResult(intent3,REQUEST_CODE);
                        }
                        break;
                    default:
                        break;
                }
                break;
            case 3:
                switch (actionId) {
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
            default:
                break;
        }
    }

    public void getInfoByActionId(int levelId, int actionId) {

        switch (levelId) {
            case 1:
                switch (actionId) {
                    case 0:
                        savActionImg.setImageResource(R.drawable.piggybank);
                        savActionImg.setVisibility(View.VISIBLE);
                        actionName.setText(getResources().getString(R.string.savActL1_1));
                        reward.setText("+5");
                        description.setText(getResources().getString(R.string.descL1_saveAct1));

                        primaryBtn.setEnabled(false);
                        primaryBtn.setText(getResources().getString(R.string.btnDone));
                        secondaryBtn.setText(getResources().getString(R.string.btnTakePhoto));

                        // Setting Dialog Message
                        alertDialog.setMessage(getResources().getString(R.string.msgReward) + " 5 " +getResources().getString(R.string.msgRewardPoint));
                        pointsObt = 5;
                        activityName = "SA: Comp. Responsible";
                        break;
                    case 1:
                        savActionImg.setImageResource(R.drawable.piggybank);
                        savActionImg.setVisibility(View.VISIBLE);
                        actionName.setText(getResources().getString(R.string.savActL1_2));
                        reward.setText("+3");
                        description.setText(getResources().getString(R.string.descL1_saveAct2));

                        primaryBtn.setText(getResources().getString(R.string.btnDone));
                        primaryBtn.setEnabled(true);
                        secondaryBtn.setVisibility(View.INVISIBLE);
                        secondaryBtn.setEnabled(false);

                        // Setting Dialog Message
                        alertDialog.setMessage(getResources().getString(R.string.msgReward) + " 3 " +getResources().getString(R.string.msgRewardPoint));
                        pointsObt = 3;
                        activityName = "SA: Desk Light";

                        break;
                    case 2:
                        savActionImg.setImageResource(R.drawable.piggybank);
                        savActionImg.setVisibility(View.VISIBLE);
                        actionName.setText(getResources().getString(R.string.savActL1_3));
                        reward.setText("+5");
                        description.setText(getResources().getString(R.string.descL1_saveAct3));

                        primaryBtn.setText(getResources().getString(R.string.btnDone));
                        primaryBtn.setEnabled(false);
                        secondaryBtn.setVisibility(View.INVISIBLE);
                        likeView.setVisibility(View.VISIBLE);
                        likeView.setClickable(true);

                        // Setting Dialog Message
                        alertDialog.setMessage(getResources().getString(R.string.msgReward) + " 5 " +getResources().getString(R.string.msgRewardPoint));
                        pointsObt = 5;
                        activityName = "SA: Like Kukui";
                        break;
                    default:
                        break;
                }
                break;
            case 2:
                switch (actionId) {
                    case 0:
                        savActionImg.setImageResource(R.drawable.piggybank);
                        savActionImg.setVisibility(View.VISIBLE);
                        actionName.setText(getResources().getString(R.string.savActL2_1));
                        reward.setText("+20");
                        description.setText(getResources().getString(R.string.descL2_saveAct1));

                        primaryBtn.setEnabled(false);
                        primaryBtn.setText(getResources().getString(R.string.btnDone));
                        secondaryBtn.setText(getResources().getString(R.string.btnTakePhoto));

                        // Setting Dialog Message
                        alertDialog.setMessage(getResources().getString(R.string.msgReward) + " 20 " +getResources().getString(R.string.msgRewardPoint));
                        pointsObt = 20;
                        activityName = "SA: Room Consump.";
                        break;
                    case 1:
                        savActionImg.setImageResource(R.drawable.piggybank);
                        savActionImg.setVisibility(View.VISIBLE);
                        actionName.setText(getResources().getString(R.string.savActL2_2));
                        reward.setText("+3");
                        description.setText(getResources().getString(R.string.descL2_saveAct2));

                        primaryBtn.setText(getResources().getString(R.string.btnDone));
                        primaryBtn.setEnabled(true);
                        secondaryBtn.setVisibility(View.INVISIBLE);
                        secondaryBtn.setEnabled(false);

                        // Setting Dialog Message
                        alertDialog.setMessage(getResources().getString(R.string.msgReward) + " 3 " +getResources().getString(R.string.msgRewardPoint));
                        pointsObt = 3;
                        activityName = "SA: Use Daylight";
                        break;
                    case 2:
                        savActionImg.setImageResource(R.drawable.piggybank);
                        savActionImg.setVisibility(View.VISIBLE);
                        actionName.setText(getResources().getString(R.string.savActL2_3));
                        reward.setText("+20");
                        description.setText(getResources().getString(R.string.descL2_saveAct3));

                        primaryBtn.setEnabled(false);
                        primaryBtn.setText(getResources().getString(R.string.btnDone));
                        secondaryBtn.setText(getResources().getString(R.string.btnTakePhoto));

                        // Setting Dialog Message
                        alertDialog.setMessage(getResources().getString(R.string.msgReward) + " 20 " +getResources().getString(R.string.msgRewardPoint));
                        pointsObt = 20;
                        activityName = "SA: Consump. POST-IT";
                        break;
                    case 3:
                        savActionImg.setImageResource(R.drawable.piggybank);
                        savActionImg.setVisibility(View.VISIBLE);
                        actionName.setText(getResources().getString(R.string.savActL2_4));
                        reward.setText("+15");
                        description.setText(getResources().getString(R.string.descL2_saveAct4));

                        primaryBtn.setEnabled(false);
                        primaryBtn.setText(getResources().getString(R.string.btnDone));
                        secondaryBtn.setText(getResources().getString(R.string.btnTakePhoto));

                        // Setting Dialog Message
                        alertDialog.setMessage(getResources().getString(R.string.msgReward) + " 15 " +getResources().getString(R.string.msgRewardPoint));
                        pointsObt = 15;
                        activityName = "SA: Search LED";
                        break;
                    default:
                        break;
                }
                break;
            case 3:
                switch (actionId) {
                    case 0:
                        savActionImg.setImageResource(R.drawable.piggybank);
                        savActionImg.setVisibility(View.VISIBLE);
                        actionName.setText(getResources().getString(R.string.savActL3_1));
                        reward.setText("+15");
                        description.setText(getResources().getString(R.string.descL3_saveAct1));
                        answera.setVisibility(View.VISIBLE);
                        answerb.setVisibility(View.VISIBLE);
                        answerc.setVisibility(View.VISIBLE);
                        answera.setText(getResources().getString(R.string.descL3_saveAct1_opt1));
                        answerb.setText(getResources().getString(R.string.descL3_saveAct1_opt2));
                        answerc.setText(getResources().getString(R.string.descL3_saveAct1_opt3));
                        primaryBtn.setText(getResources().getString(R.string.btnDone));
                        primaryBtn.setEnabled(true);
                        secondaryBtn.setVisibility(View.INVISIBLE);
                        secondaryBtn.setEnabled(false);

                        // Setting Dialog Message
                        alertDialog.setMessage(getResources().getString(R.string.msgReward) + " 15 " +getResources().getString(R.string.msgRewardPoint));
                        pointsObt = 15;
                        activityName = "SA: Wash M. Time";
                        break;

                    case 1:
                        savActionImg.setImageResource(R.drawable.piggybank);
                        savActionImg.setVisibility(View.VISIBLE);
                        actionName.setText(getResources().getString(R.string.savActL3_2));
                        reward.setText("+15");
                        description.setText(getResources().getString(R.string.descL3_saveAct2));
                        answera.setVisibility(View.VISIBLE);
                        answerb.setVisibility(View.VISIBLE);
                        answera.setText(getResources().getString(R.string.descL3_saveAct2_opt1));
                        answerb.setText(getResources().getString(R.string.descL3_saveAct2_opt2));
                        primaryBtn.setText(getResources().getString(R.string.btnDone));
                        primaryBtn.setEnabled(true);
                        secondaryBtn.setVisibility(View.INVISIBLE);
                        secondaryBtn.setEnabled(false);

                        // Setting Dialog Message
                        alertDialog.setMessage(getResources().getString(R.string.msgReward) + " 15 " +getResources().getString(R.string.msgRewardPoint));
                        pointsObt = 15;
                        activityName = "SA: Wash M. Charge";
                        break;
                    default:
                        break;
                }
                break;
            default:
                break;
        }

    }

    class SavePlayerActivity extends AsyncTask<String, String, String> {

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
            params.add(new BasicNameValuePair("player_name", username));
            params.add(new BasicNameValuePair("activity_name", activityName));

            // sending modified data through http request
            // Notice that update player url accepts POST method
            JSONObject json = jsonParser.makeHttpRequest(url_activity_reg,
                    "POST", params);
            Log.d("activity_reg: ", json.toString());

            return null;
        }
        protected void onPostExecute(String file_url) {
        }
    }
}
