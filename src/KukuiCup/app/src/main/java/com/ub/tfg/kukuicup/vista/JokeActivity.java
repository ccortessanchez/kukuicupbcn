package com.ub.tfg.kukuicup.vista;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.Toast;

import com.ub.tfg.kukuicup.R;

/**
 * Created by Juanmi on 30/06/2016.
 */
public class JokeActivity extends Activity {

    private int levelId;
    private int formId;
    private int jokeId;
    private int pointsObt;
    private String badgeObt;

    private Button continueBtn;
    private ImageView jokeImg;

    public void onCreate(Bundle savedInstanceState) {
        if(getResources().getBoolean(R.bool.tablet)){
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        }
        super.onCreate(savedInstanceState);
        setContentView(R.layout.joke);

        Bundle extras = getIntent().getExtras();

        levelId = extras.getInt("levelId");
        formId = extras.getInt("formId");
        jokeId = extras.getInt("jokeId");
        pointsObt = extras.getInt("points");
        badgeObt = extras.getString("badge");

        continueBtn = (Button)findViewById(R.id.continueBtn);
        jokeImg = (ImageView)findViewById(R.id.jokeImg);

        getInfoByFormId(levelId, formId);

        continueBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Intent intent = new Intent(JokeActivity.this, MainActivity.class);
                intent.putExtra("points",pointsObt);
                intent.putExtra("badge",badgeObt);
                startActivity(intent);
            }
        });
    }

    public void getInfoByFormId(int levelId, int formId) {

        switch (levelId) {
            case 1:
                switch (formId) {
                    case 0:
                        jokeImg.setImageResource(R.mipmap.joke1_img);
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
                        jokeImg.setImageResource(R.mipmap.joke2_img);
                        break;
                    case 1:
                        jokeImg.setImageResource(R.mipmap.joke3_img);
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
                        jokeImg.setImageResource(R.mipmap.joke4_img);
                        break;
                    case 1:
                        jokeImg.setImageResource(R.mipmap.joke5_img);
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
