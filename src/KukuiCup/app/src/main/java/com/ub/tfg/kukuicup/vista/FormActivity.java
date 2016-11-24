package com.ub.tfg.kukuicup.vista;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;
import com.ub.tfg.kukuicup.R;

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

    private RadioGroup radioGr1, radioGr2, radioGr3;
    private RadioButton answer1btn, answer2btn, answer3btn;
    private RadioButton answer1a, answer1b, answer1c, answer2a, answer2b, answer2c, answer3a, answer3b, answer3c;

    private AlertDialog alertDialog;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.form);

        Bundle extras = getIntent().getExtras();

        levelId = extras.getInt("levelId");
        formId = extras.getInt("formId");
        pointsObt = extras.getInt("points");
        badgeObt = "none";

        formName = (TextView)findViewById(R.id.formName);
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
        alertDialog.setTitle("Congratulations!");

        // Setting OK Button
        alertDialog.setButton2("OK", new DialogInterface.OnClickListener() {
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

        getInfoByFormId(1, 0);

        primaryBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {

                if(answer1btn.isChecked()&&answer2btn.isChecked()&&answer3btn.isChecked()) {
                    //Correct//Show alert dialog
                    alertDialog.show();

                }else {
                    //Try again
                    Toast.makeText(FormActivity.this, "You can do it better!", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    public void getInfoByFormId(int levelId, int formId) {

        switch (levelId) {
            case 1:
                switch (formId) {
                    case 0:
                        formName.setText("Power and Energy");
                        reward.setText("+30");
                        badgeImg.setImageResource(R.mipmap.level1_badge);
                        badgeImg.setVisibility(View.VISIBLE);

                        answer1btn = (RadioButton)findViewById(R.id.answer1b);
                        answer2btn = (RadioButton)findViewById(R.id.answer2a);
                        answer3btn = (RadioButton)findViewById(R.id.answer3a);

                        jokeId = 1;

                        question1.setText("Work is being done only when a force is _____ an object.");
                        answer1a.setText("A: with");
                        answer1b.setText("B: moving");
                        answer1c.setText("C: next to");
                        question2.setText("When a moving object has the energy of motion it has this type of energy.");
                        answer2a.setText("A: kinetic energy");
                        answer2b.setText("B: high energy");
                        answer2c.setText("C: potential energy");
                        question3.setText("How can energy change?");
                        answer3a.setText("A: energy can be transferred from one object to another");
                        answer3b.setText("B: temperature changes the type of energy");
                        answer3c.setText("C: electrical energy can change light energy");

                        // Setting Dialog Message
                        alertDialog.setMessage("You have won the bagde of Level 1 and 30 points.");
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
                        formName.setText("Wind Power");
                        reward.setText("+30");
                        badgeImg.setImageResource(R.mipmap.level1_badge);
                        badgeImg.setVisibility(View.VISIBLE);

                        answer1btn = (RadioButton)findViewById(R.id.answer1b);
                        answer2btn = (RadioButton)findViewById(R.id.answer2a);
                        answer3btn = (RadioButton)findViewById(R.id.answer3a);

                        jokeId = 1;

                        question1.setText("Through _____ electricity is obtained using the wind as source of energy");
                        answer1a.setText("A: wind power plants");
                        answer1b.setText("B: nuclear power plants");
                        answer1c.setText("C: power plants");
                        question2.setText("Once the ideal place is found, which objects are placed to take advantage of the wind?");
                        answer2a.setText("A: none");
                        answer2b.setText("B: windmills");
                        answer2c.setText("C: water mills");
                        question3.setText("When wind moves the mill's helix, what kind of energy generates?");
                        answer3a.setText("A: kinetic energy");
                        answer3b.setText("B: mechanical energy");
                        answer3c.setText("C: electric power");

                        // Setting Dialog Message
                        alertDialog.setMessage("You have won the bagde of Level 2 and 30 points.");
                        // Setting Icon to Dialog
                        alertDialog.setIcon(R.mipmap.level1_badge);
                        pointsObt+=30;
                        badgeObt = "level2";
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
                switch (formId) {
                    case 0:
                        formName.setText("Clean Energy");
                        reward.setText("+30");
                        badgeImg.setImageResource(R.mipmap.level1_badge);
                        badgeImg.setVisibility(View.VISIBLE);

                        answer1btn = (RadioButton)findViewById(R.id.answer1b);
                        answer2btn = (RadioButton)findViewById(R.id.answer2a);
                        answer3btn = (RadioButton)findViewById(R.id.answer3a);

                        jokeId = 1;

                        question1.setText("What kind of materials are use during the setting of the 4 meter tower?");
                        answer1a.setText("A: pollutant materials");
                        answer1b.setText("B: recycled materials");
                        answer1c.setText("C: no materials are used");
                        question2.setText("What kind of panels are used?");
                        answer2a.setText("A: solar panels");
                        answer2b.setText("B: cardboard panels");
                        answer2c.setText("C: no panels are used");
                        question3.setText("How much voltage are the batteries used?");
                        answer3a.setText("A:15V");
                        answer3b.setText("B: no batteries are used");
                        answer3c.setText("C: 6V");

                        // Setting Dialog Message
                        alertDialog.setMessage("You have won the bagde of Level 3+ and 30 points.");
                        // Setting Icon to Dialog
                        alertDialog.setIcon(R.mipmap.level1_badge);
                        pointsObt+=30;
                        badgeObt = "level3+";
                        break;
                    case 1:
                        formName.setText("Solar Energy");
                        reward.setText("+30");
                        badgeImg.setImageResource(R.mipmap.level1_badge);
                        badgeImg.setVisibility(View.VISIBLE);

                        answer1btn = (RadioButton)findViewById(R.id.answer1b);
                        answer2btn = (RadioButton)findViewById(R.id.answer2a);
                        answer3btn = (RadioButton)findViewById(R.id.answer3a);

                        jokeId = 1;

                        question1.setText("Solar panels turn _____ into electric power");
                        answer1a.setText("A: wind force");
                        answer1b.setText("B: rain water");
                        answer1c.setText("C: solar radiation");
                        question2.setText("How many types of photovoltaic installations are there? ");
                        answer2a.setText("A: infinite");
                        answer2b.setText("B: two");
                        answer2c.setText("C: one");
                        question3.setText("If we have an installation connected to the electric network, what is done with the transformed solar energy?");
                        answer3a.setText("A: nothinh");
                        answer3b.setText("B: is sold to the electric company ");
                        answer3c.setText("C: owner of the installation keeps it for him");

                        // Setting Dialog Message
                        alertDialog.setMessage("You have won the bagde of Level 3 and 30 points.");
                        // Setting Icon to Dialog
                        alertDialog.setIcon(R.mipmap.level1_badge);
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
