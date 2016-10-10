package com.ub.tfg.kukuicup.vista;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.share.widget.LikeView;
import com.ub.tfg.kukuicup.R;
import com.ub.tfg.kukuicup.model.SavingAction;
import com.facebook.FacebookSdk;

/**
 * Created by Juanmi on 02/06/2016.
 */
public class SavingActionActivity extends Activity {

    int REQUEST_CODE = 1;

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

    private AlertDialog alertDialog;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.saving_action);

        FacebookSdk.sdkInitialize(getApplicationContext());

        Bundle extras = getIntent().getExtras();

        levelId = extras.getInt("levelId");
        actionId = extras.getInt("actionId");

        actionName = (TextView)findViewById(R.id.actionName);
        reward = (TextView)findViewById(R.id.actionReward);
        description = (TextView)findViewById(R.id.actionDescription);

        primaryBtn = (Button)findViewById(R.id.primaryBtn);
        secondaryBtn = (Button)findViewById(R.id.secondaryBtn);
        uploadBtn = (Button)findViewById(R.id.upload);
        likeView = (LikeView)findViewById(R.id.likeView);
        likeView.setLikeViewStyle(LikeView.Style.STANDARD);
        likeView.setAuxiliaryViewPosition(LikeView.AuxiliaryViewPosition.INLINE);
        likeView.setObjectIdAndType("https://www.facebook.com/kukuicup/?fref=ts",LikeView.ObjectType.OPEN_GRAPH);
        likeView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                primaryBtn.setEnabled(true);
            }
        });

        pointsImg = (ImageView)findViewById(R.id.pointsImg);
        badgeImg = (ImageView)findViewById(R.id.badgeImg);
        actionImg = (ImageView)findViewById(R.id.actionImg);

        alertDialog = new AlertDialog.Builder(SavingActionActivity.this).create();
        // Setting Dialog Title
        alertDialog.setTitle("Congratulations! Get your points!");
        // Setting Icon to Dialog
        alertDialog.setIcon(R.drawable.ic_launcher);
        // Setting OK Button
        alertDialog.setButton2("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                // Write your code here to execute after dialog closed
                Intent intent = new Intent(SavingActionActivity.this, MainActivity.class);
                intent.putExtra("points", pointsObt);
                startActivity(intent);
                primaryBtn.setEnabled(false);
            }
        });

        getInfoByActionId(levelId, actionId);

        secondaryBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                initTaskByActionId(levelId,actionId);
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
                Toast.makeText(getApplicationContext(),"Uploaded successfully", Toast.LENGTH_SHORT).show();
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
                break;
            case 3:
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
                        actionName.setText("Computer responsible");
                        reward.setText("+5");
                        description.setText("Configure your computer and any external monitor to sleep after 20 minutes of inactivity." +
                                "\n" +
                                "Once you have changed your settings, please take a photo showing the new settings for use in verification");

                        primaryBtn.setEnabled(false);
                        primaryBtn.setText("Done!");
                        secondaryBtn.setText("Take a photo");

                        // Setting Dialog Message
                        alertDialog.setMessage("You have won 5 points");
                        pointsObt = 5;
                        break;
                    case 1:
                        actionName.setText("Desk light");
                        reward.setText("+3");
                        description.setText("Commit to using task lighting (like a desk lamp) instead of overhead room lights when possible." +
                                "\n" +
                                "Press the 'Done! button when you make this saving action.");

                        primaryBtn.setText("Done!");
                        primaryBtn.setEnabled(true);
                        secondaryBtn.setVisibility(View.INVISIBLE);
                        secondaryBtn.setEnabled(false);

                        // Setting Dialog Message
                        alertDialog.setMessage("You have won 3 points");
                        pointsObt = 3;

                        break;
                    case 2:
                        actionName.setText("Like Kukui");
                        reward.setText("+5");
                        description.setText("Show support for the Kukui Cup by Liking the Kukui Cup page on Facebook. " +
                                "\n" +
                                "Follow the link, click on the 'Like' button, and then back on this page click the 'Done' button.");

                        primaryBtn.setText("Done!");
                        primaryBtn.setEnabled(true);
                        secondaryBtn.setVisibility(View.INVISIBLE);
                        likeView.setVisibility(View.VISIBLE);
                        likeView.setClickable(true);

                        // Setting Dialog Message
                        alertDialog.setMessage("You have won 5 points");
                        pointsObt = 5;
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