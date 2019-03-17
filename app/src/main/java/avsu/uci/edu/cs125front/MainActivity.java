package avsu.uci.edu.cs125front;


import android.Manifest;
import android.app.AlertDialog;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;

import android.location.Location;

import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;


import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class MainActivity extends AppCompatActivity {

    private static final int MY_PERMISSIONS_REQUEST_ACCESS_COARSE_LOCATION = 1;
    private FusedLocationProviderClient fusedLocationClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SharedPreferences prefs = getSharedPreferences("USER_DETAILS", MODE_PRIVATE);

        // Add this code to test Setup Screen and WakeUp Screen
//        SharedPreferences.Editor editor = prefs.edit();
//        editor.clear();
//        editor.commit();

        //Add this code to test WakeUp Screen
//        SharedPreferences.Editor editor = prefs.edit();
//        editor.putString("user_date","End Of Time");
//        editor.commit();


        // Instantiate the object we use to get last location later.
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        // User age is indicator to whether or not Setup Screen is needed
        int age = prefs.getInt("user_age", 0);

        //Direct to Setup Screen
        if(age == 0 ){
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
            Log.d("AGE != 0", "We skipped the setup Screen");
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

//        String actual_date = saved_date;

        if (actual_date.equals(saved_date)) {
            Log.d("ACTUAL == SAVED DATE", "IT WAS EQUIVALENT");
            goToMainView();
        }
        // Set the wake up screen to get the users wakeup time
        // Also set the saved date as the actual date.
        else{
            Log.d("ACTUAL != SAVED DATE", "IT WASN'T EQUIVALENT");
            SharedPreferences.Editor user_editor = prefs.edit();
            user_editor.putString("user_date",actual_date);
            user_editor.commit();
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
                fetchLocation();
                Log.d("goToMainView Context", getApplicationContext().toString());
            }
        });
    }

    private void fetchLocation() {
        // Here, thisActivity is the current activity
        if (ContextCompat.checkSelfPermission(MainActivity.this,
                Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            // Permission is not granted
            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this,
                    Manifest.permission.ACCESS_COARSE_LOCATION)) {
                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.

                new AlertDialog.Builder(MainActivity.this)
                        .setTitle("Required Location Permissions")
                        .setMessage("You have to give this permission to access the feature")
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                ActivityCompat.requestPermissions(MainActivity.this,
                                        new String[]{Manifest.permission.ACCESS_COARSE_LOCATION},
                                        MY_PERMISSIONS_REQUEST_ACCESS_COARSE_LOCATION);
                            }
                        })
                        .setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        })
                        .create()
                        .show();


            } else {
                // No explanation needed; request the permission
                ActivityCompat.requestPermissions(MainActivity.this,
                        new String[]{Manifest.permission.ACCESS_COARSE_LOCATION},
                        MY_PERMISSIONS_REQUEST_ACCESS_COARSE_LOCATION);

                // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                // app-defined int constant. The callback method gets the
                // result of the request.
            }
        } else {
            // Permission has already been granted


            Log.d("Permission Granted", "We reached the permission block");
            fusedLocationClient.getLastLocation()
                    .addOnSuccessListener(this, new OnSuccessListener<Location>() {
                        @Override
                        public void onSuccess(Location location) {
                            // Got last known location. In some rare situations this can be null.
                            if (location != null) {
                                // Logic to handle location object

                                Double latitude = location.getLatitude();
                                Double longitude = location.getLongitude();

                                Log.d("LOCATION_DATA",latitude.toString());
                                SharedPreferences.Editor user_editor = getSharedPreferences("USER_DETAILS",MODE_PRIVATE).edit();
                                user_editor.putString("user_lat",latitude.toString());
                                user_editor.putString("user_long",longitude.toString());

                                Log.d("fetchLocation Context", getApplicationContext().toString());
                                Intent exerciseIntent = new Intent(getApplicationContext(), ExerciseListActivity.class);
                                startActivity(exerciseIntent);

                            }
                            else{
                                Log.d("LOCATION RETURNED?","LOCATION WAS NULL");


                            }
                        }
                    });
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if(requestCode == MY_PERMISSIONS_REQUEST_ACCESS_COARSE_LOCATION){
            if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
            }
            else{
                Log.d("SAVED_ERROR","This block shouldn't be reached");
            }
        }
    }



    //End of Activity
}

