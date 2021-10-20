package com.kimjjing1004.seoulapplication.main.user;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
//import com.google.android.youtube.player.YouTubeInitializationResult;
//import com.google.android.youtube.player.YouTubePlayer;
//import com.google.android.youtube.player.YouTubePlayerView;
import com.kimjjing1004.seoulapplication.R;
import com.kimjjing1004.seoulapplication.main.ui.UserFragment;


public class BadUserActivity extends AppCompatActivity {

//    YouTubePlayerView youtubeView;
//    Button button;
//    YouTubePlayer.OnInitializedListener listener;

    String english;
    String korea;
    String judgement;

    Intent secondIntent = getIntent();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_baduser);
//        button = (Button) findViewById((R.id.youtubeButton));
//        youtubeView = (YouTubePlayerView) findViewById(R.id.youtubeView);

        System.out.println(secondIntent);

        try {
            english = secondIntent.getStringExtra("English");
            korea = "";
            judgement="OK";
            System.out.println("영어왔냐");
        }
        catch (Exception ex){
            System.out.println("ERROR!!");
        }
        try {
            korea = secondIntent.getStringExtra("Korea");
            english = "";
            judgement = "OK";
            System.out.println("한국어왔냐");

        }
        catch (Exception ex){
            System.out.println("ERROR!!");
        }




//        listener = new YouTubePlayer.OnInitializedListener(){
//
//            @Override
//            public void onInitializationSuccess(YouTubePlayer.Provider provider, YouTubePlayer youTubePlayer, boolean b) {
//                // 비디오 아이디
//                youTubePlayer.loadVideo("dg4aOHYL75w");
//
//            }
//            @Override
//            public void onInitializationFailure(YouTubePlayer.Provider provider, YouTubeInitializationResult youTubeInitializationResult) {
//
//            }
//
//        };
//
//        button.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                // api 키 값
//                youtubeView.initialize("AIzaSyDpRyFn3f1W98-xahuC5_qLE7HKI4k30Qw", listener);
//            }
//
//        });
    }


}


