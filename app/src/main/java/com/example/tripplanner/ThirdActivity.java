package com.example.tripplanner;

import android.app.ActivityOptions;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Pair;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.ScrollView;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;

import java.util.Locale;

public class ThirdActivity extends AppCompatActivity {
    ImageView down_arrow;
    ScrollView third_scrollview;
    Animation from_bottom;
    TextView third_title;
    TextView About_text;
    TextView Venue_type;
    TextView Venue_type_text;
    TextView Type_of_view;
    TextView Type_of_view_text;
    TextView third_rating_number;
    RatingBar third_ratingbar;
    Button ButtonListen,roadmap_button;
    TextToSpeech mTTS;
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_third);
        ButtonListen = findViewById(R.id.speak_button);
        roadmap_button = findViewById(R.id.roadmap_button);
        third_title = findViewById(R.id.third_title);
        third_rating_number = findViewById(R.id.third_rating_number);
        third_ratingbar = findViewById(R.id.third_ratingbar);
        About_text = findViewById(R.id.about_text);
        Venue_type = findViewById(R.id.venue_type);
        Venue_type_text = findViewById(R.id.venue_type_text);
        Type_of_view = findViewById(R.id.type_of_view);
        Type_of_view_text = findViewById(R.id.type_of_view_text);
        down_arrow = findViewById(R.id.down_arrow);
        third_scrollview = findViewById(R.id.third_scrillview);
        from_bottom = AnimationUtils.loadAnimation(this, R.anim.anim_from_bottom);
        down_arrow.setAnimation(from_bottom);
        third_scrollview.setAnimation(from_bottom);

        Intent intent = getIntent();
        int table = intent.getIntExtra("table_EXTRA",0);
        if(table == 1){
            third_title.setText(R.string.title_text);
            third_ratingbar.setRating(4.91f);
            third_rating_number.setText("4.91");
            About_text.setText(R.string.about_text);
            Venue_type_text.setText(R.string.venue_text);
            Type_of_view.setText(R.string.Good_text);
        }else if(table == 2){
            third_title.setText(R.string.title_text1);
            third_ratingbar.setRating(4.75f);
            third_rating_number.setText("4.75");
            About_text.setText(R.string.about_text1);
            Venue_type_text.setText(R.string.venue_text1);
            Type_of_view.setText(R.string.Good_text1);
        }else if(table == 3) {
            third_title.setText(R.string.title_text2);
            third_ratingbar.setRating(4.75f);
            third_rating_number.setText("4.75");
            About_text.setText(R.string.about_text2);
            Venue_type_text.setText(R.string.venue_text2);
            Type_of_view.setText(R.string.Good_text2);
        }

        roadmap_button.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                String store="";
                if(table == 1) {
                    store = "google.navigation:q=22.3125562,114.2245276&mode=l";
                }else if(table == 2){
                    store = "google.navigation:q=22.3001546,114.1738931&mode=l";
                }else if(table == 3){
                    store = "google.navigation:q=22.3163439,114.1710596&mode=l";
                }
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(store));
                intent.setPackage("com.google.android.apps.maps");

                if(intent.resolveActivity(getPackageManager())!=null){
                    startActivity(intent);
                }
            }
        });

        mTTS = new TextToSpeech(this, new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                String store = Locale.getDefault().getLanguage();
                int result = mTTS.setLanguage(Locale.ENGLISH);;
                if (status == TextToSpeech.SUCCESS) {
                    if(store == "en"){
                         result = mTTS.setLanguage(Locale.ENGLISH);
                    }else if(store == "zh"){
                         result = mTTS.setLanguage(Locale.CHINESE);
                    }
                    if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                        Log.e("TTS", "Language not supported");
                    } else {
                        ButtonListen.setEnabled(true);
                    }
                } else {
                    Log.e("TTS", "Initialization failed");
                }
            }
        });

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
        down_arrow.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onClick(View view) {
                if (mTTS != null) {
                    mTTS.stop();
                    mTTS.shutdown();
                }
                Intent intent = new Intent(ThirdActivity.this, SecondActivity.class);
                Pair[] pairs = new Pair[1];
                pairs[0] = new Pair<View, String>(down_arrow, "background_image_transition");
                ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(ThirdActivity.this, pairs);
                startActivity(intent, options.toBundle());
            }
        });
        ButtonListen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                speak();
            }
        });
    }

    private void speak() {
        String text = third_title.getText().toString() + About_text.getText().toString() + Venue_type.getText().toString() + Venue_type_text.getText().toString() + Type_of_view.getText().toString() + Type_of_view_text.getText().toString();
        mTTS.setPitch(0.8f);
        mTTS.setSpeechRate(0.6f);
        mTTS.speak(text, TextToSpeech.QUEUE_FLUSH, null);
    }
    @Override
    protected void onDestroy() {
        if (mTTS != null) {
            mTTS.stop();
            mTTS.shutdown();
        }
        super.onDestroy();
    }
}