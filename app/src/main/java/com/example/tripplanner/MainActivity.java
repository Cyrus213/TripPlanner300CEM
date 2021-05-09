package com.example.tripplanner;

import android.annotation.TargetApi;
import android.app.SearchManager;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

public class MainActivity extends AppCompatActivity implements SensorEventListener,BiometricCallback{//homepage
    CardView cardView, cardView2, cardView3;
    Button plan_button;
    ImageView imageView;
    TextView textView, textView2, textView3, textView4, textView5 , textViewTem;
    SearchView searchView;
    Animation anim_from_button, anim_from_top, anim_from_left;
    SensorManager sensorManager;
    Sensor tempSensor;
    Boolean isTemperatureSsensoreAvilable;
    BiometricManager mBiometricManager;

    @TargetApi(Build.VERSION_CODES.KITKAT)
    @Override
    protected void onCreate(Bundle savedInstanceState) {//set animation of the homepage
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        cardView = findViewById(R.id.cardView);
        cardView2 = findViewById(R.id.cardView2);
        cardView3 = findViewById(R.id.cardView3);
        imageView = findViewById(R.id.imageView);
        textView = findViewById(R.id.firstText);
        textView2 = findViewById(R.id.textView);
        textView3 = findViewById(R.id.textView2);
        textView4 = findViewById(R.id.textView3);
        textView5 = findViewById(R.id.textView4);
        searchView = findViewById(R.id.searchView);
        textViewTem = findViewById(R.id.textViewTem);
        plan_button = findViewById(R.id.plan_button);
        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        //Load Animations
        anim_from_button = AnimationUtils.loadAnimation(this, R.anim.anim_from_bottom);
        anim_from_top = AnimationUtils.loadAnimation(this, R.anim.anim_from_top);
        anim_from_left = AnimationUtils.loadAnimation(this, R.anim.anim_from_left);
        //Set Animations
        cardView.setAnimation(anim_from_button);
        cardView2.setAnimation(anim_from_button);
        cardView3.setAnimation(anim_from_button);
        imageView.setAnimation(anim_from_top);
        textView.setAnimation(anim_from_top);
        textView2.setAnimation(anim_from_top);
        textView3.setAnimation(anim_from_top);
        textView4.setAnimation(anim_from_top);
        textView5.setAnimation(anim_from_top);
        searchView.setAnimation(anim_from_left);

        if(sensorManager.getDefaultSensor(Sensor.TYPE_AMBIENT_TEMPERATURE)!=null){//temperature sensor
            tempSensor = sensorManager.getDefaultSensor(Sensor.TYPE_AMBIENT_TEMPERATURE);
            isTemperatureSsensoreAvilable = true;
        }else{
            textViewTem.setText("Temperature Sensor is not Availible");
            isTemperatureSsensoreAvilable = false;
        }

        cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent secondActivity = new Intent(MainActivity.this, SecondActivity.class);
                secondActivity.putExtra("table_EXTRA", 1);
                startActivity(secondActivity);
            }
        });

        cardView2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent secondActivity = new Intent(MainActivity.this, SecondActivity.class);
                secondActivity.putExtra("table_EXTRA", 2);
                startActivity(secondActivity);
            }
        });

        cardView3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent secondActivity = new Intent(MainActivity.this, SecondActivity.class);
                secondActivity.putExtra("table_EXTRA", 3);
                startActivity(secondActivity);
            }
        });

        plan_button.setOnClickListener(new View.OnClickListener() {//go to trip planner
            @Override
            public void onClick(View view) {
                Intent planActivity = new Intent(MainActivity.this, Planner.class);
                startActivity(planActivity);
            }
        });

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {//search in internet
            @Override
            public boolean onQueryTextSubmit(String s) {
                String search = searchView.getQuery().toString();
                if(!search.equals("")){
                    searchNet(search);
                }
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                return false;
            }
        });

        imageView.setOnClickListener(new View.OnClickListener() {//go to the profile
            @Override
            public void onClick(View view2) {
                mBiometricManager = new BiometricManager.BiometricBuilder(MainActivity.this)
                        .setTitle(getString(R.string.login_btn))
                        .setSubtitle(getString(R.string.biometric_subtitle))
                        .setDescription(getString(R.string.biometric_description))
                        .setNegativeButtonText(getString(R.string.biometric_negative_button_text))
                        .build();
                //start authentication
                mBiometricManager.authenticate(MainActivity.this);
            }
        });
        //Hide status bar and navigation bar at the bottom
        getWindow().setFlags(
                WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
                WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS
        );
        this.getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
        );
    }

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        textViewTem.setText(sensorEvent.values[0]+"°C");
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {
    }

    public void searchNet(String words){//search method
        try{
            Intent intent = new Intent(Intent.ACTION_WEB_SEARCH);
            intent.putExtra(SearchManager.QUERY,words);
            startActivity(intent);
        } catch (ActivityNotFoundException e){
            e.printStackTrace();
            searchNetCompat(words);
        }
    }

    public void searchNetCompat(String words){
        try{
            Uri uri = Uri.parse("http://www.google.com/#q" + words);
            Intent intent = new Intent(Intent.ACTION_VIEW,uri);
            startActivity(intent);
        } catch (ActivityNotFoundException e){
            e.printStackTrace();
            Toast.makeText(this,"Error!",Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(isTemperatureSsensoreAvilable){
            sensorManager.registerListener(this,tempSensor,SensorManager.SENSOR_DELAY_NORMAL);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if(isTemperatureSsensoreAvilable){
            sensorManager.unregisterListener(this);
        }
    }
    //action of the fingerprint
    @Override
    public void onSdkVersionNotSupported() {
        Intent secondActivity = new Intent(MainActivity.this, Edit.class);
        startActivity(secondActivity);
    }
    @Override
    public void onBiometricAuthenticationNotSupported() {
        Intent secondActivity = new Intent(MainActivity.this, Edit.class);
        startActivity(secondActivity);
    }
    @Override
    public void onBiometricAuthenticationNotAvailable() {
        Intent secondActivity = new Intent(MainActivity.this, Edit.class);
        startActivity(secondActivity);
    }
    @Override
    public void onBiometricAuthenticationPermissionNotGranted() {
        Toast.makeText(getApplicationContext(),getString(R.string.biometric_error_permission_not_granted), Toast.LENGTH_LONG).show();
    }
    @Override
    public void onBiometricAuthenticationInternalError(String error) {
        Toast.makeText(getApplicationContext(), error, Toast.LENGTH_LONG).show();
    }
    @Override
    public void onAuthenticationFailed() {
        Toast.makeText(getApplicationContext(), getString(R.string.biometric_failure),Toast.LENGTH_LONG).show();
    }
    @Override
    public void onAuthenticationCancelled() {
        Toast.makeText(getApplicationContext(), getString(R.string.biometric_cancelled),
                Toast.LENGTH_LONG).show();
        mBiometricManager.cancelAuthentication();
    }
    @Override
    public void onAuthenticationSuccessful() {
        Intent secondActivity = new Intent(MainActivity.this, Edit.class);
        startActivity(secondActivity);
    }
    @Override
    public void onAuthenticationHelp(int helpCode, CharSequence helpString) {
        Toast.makeText(getApplicationContext(), helpString, Toast.LENGTH_LONG).show();
    }
    @Override
    public void onAuthenticationError(int errorCode, CharSequence errString) {
        Toast.makeText(getApplicationContext(), errString, Toast.LENGTH_LONG).show();
    }
}