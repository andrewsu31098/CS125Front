package avsu.uci.edu.cs125front;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SharedPreferences prefs = getSharedPreferences("USER_DETAILS", MODE_PRIVATE);

        // This part makes the setup screen popup every time for testing
//        SharedPreferences.Editor editor = prefs.edit();
//        editor.clear();
//        editor.commit();

        //Testing if our app asks us everyday for wakeup time
//        SharedPreferences.Editor editor = prefs.edit();
//        editor.putString("user_date","End Of Time");
//        editor.commit();

        int age = prefs.getInt("user_age", 0);

        //Direct to Setup Screen
        if(age == 0){
            setContentView(R.layout.activity_setup);

            //Getting Gender
            final Spinner genderSpinner = findViewById(R.id.genderSpinner);
            ArrayAdapter<CharSequence> genAdapter = ArrayAdapter.createFromResource(MainActivity.this, R.array.gender_array, android.R.layout.simple_spinner_item);
            genAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            genderSpinner.setAdapter(genAdapter);


            //Setup Stuff
            Button finishSetupButton = findViewById(R.id.finishSetupButton);
            finishSetupButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    //Getting Age
                    EditText ageText = findViewById(R.id.AgeEditText);
                    int age = Integer.parseInt(ageText.getText().toString());
                    SharedPreferences.Editor user_editor = getSharedPreferences("USER_DETAILS",MODE_PRIVATE).edit();
                    user_editor.putInt("user_age", age);
                    user_editor.apply();
                    Log.d("SAVED_AGE", String.valueOf(age));

                    //Getting gender
                    String gender = genderSpinner.getSelectedItem().toString();
                    user_editor.putString("user_gender", gender);
                    user_editor.commit();
                    Log.d("SAVED_GENDER",gender);

                    //Catching Errors
                    if (age==0){
                        ageText.setText("");
                        ageText.setHint("Please enter valid age");
                    }
                    else {
                        chooseWakeupOrMainView();
                    }
                }
                //End of Search Button
            });

        }

        //Age exists, so user set up app before
        //Direct to wakeup or mainview
        else {
            chooseWakeupOrMainView();
        }

        //End of OnCreate
    }

    void chooseWakeupOrMainView(){
        // getting the actual date
        Date c = Calendar.getInstance().getTime();
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd, z");
        String actual_date = df.format(c);
        Log.d("SAVED_ACTUAL_DATE", actual_date);

        // getting the saved date
        SharedPreferences prefs = getSharedPreferences("USER_DETAILS", MODE_PRIVATE);
        String saved_date = prefs.getString("user_date","END OF TIME");
        Log.d("SAVED_CURRENT_DATE", saved_date);

        if (actual_date == saved_date) {
            goToMainView();
        }
        else{
            goToWakeupView();
        }
    }

    void goToWakeupView(){
        setContentView(R.layout.activity_wakeup);
        //Initialize spinner and adapter
        final Spinner wakeUpSpinner = findViewById(R.id.wakeUpSpinner);
        ArrayAdapter<CharSequence> wakeUpAdapter = ArrayAdapter.createFromResource(MainActivity.this, R.array.wakeUp_array, android.R.layout.simple_spinner_item);
        wakeUpAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        wakeUpSpinner.setAdapter(wakeUpAdapter);

        //Retrieving answer, saving to Shared prefs, and changing views once button is pressed
        Button wakeUpButton = findViewById(R.id.wakeUpButton);
        wakeUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                SharedPreferences.Editor user_editor = getSharedPreferences("USER_DETAILS",MODE_PRIVATE).edit();
                String wakeUpTime = wakeUpSpinner.getSelectedItem().toString();
                user_editor.putString("user_wakeUp", wakeUpTime);
                user_editor.commit();
                Log.d("SAVED_WAKE_TIME",wakeUpTime);

                // Finally go to main view once we get wakeUp time
                goToMainView();
            }
        });

    }

    void goToMainView(){
        setContentView(R.layout.activity_main);
        //Main Activity Stuff
        Date c = Calendar.getInstance().getTime();
        SimpleDateFormat df = new SimpleDateFormat("h:mm a, z");
        String formattedTimeString = df.format(c);

//                        String currentDateTimeString = DateFormat.getDateTimeInstance().format(new Date());
        TextView timeText = findViewById(R.id.Time_EditText);
        timeText.setText(formattedTimeString);
        Button localSearchButton = findViewById(R.id.LocalSearchButton);

        localSearchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent exerciseIntent = new Intent(getApplicationContext(), ExerciseListActivity.class);
                startActivity(exerciseIntent);
            }
        });
    }
    //End of Activity
}

