package com.example.fourinrow;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageButton;

public class MainActivity extends AppCompatActivity {

    boolean buttonOn1 = false;
    boolean buttonOn2 = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);



        ImageButton start = findViewById(R.id.start);
        start.setImageResource(R.drawable.newgamebut);
        if (buttonOn1 == true){
            start.setImageResource(R.drawable.newgamebut);
        }

        start.setOnClickListener(v -> {
            //set active image
            if (buttonOn1 == true) {
                start.setImageResource(R.drawable.newgamebut);
                buttonOn1 = false;
            } else {
                start.setImageResource(R.drawable.newgamebut_ac);
                buttonOn1 = true;
            }
            openGame();
        });

        ImageButton score = findViewById(R.id.score);
        score.setImageResource(R.drawable.scorebut);
        if (buttonOn2 == true) {
            score.setImageResource(R.drawable.scorebut);
            buttonOn2 = false;
        }
        score.setOnClickListener(v -> {
            //set active image
            if (buttonOn2 == true) {
                score.setImageResource(R.drawable.scorebut);
                buttonOn2 = false;
            } else {
                score.setImageResource(R.drawable.scorebut_ac);
                buttonOn2 = true;
            }
            openHistory();
        });

        //hide the action bar
        getSupportActionBar().hide();


    }

    @Override
    protected void onResume() {
        super.onResume();
        ImageButton start = findViewById(R.id.start);
        start.setImageResource(R.drawable.newgamebut);
        ImageButton score = findViewById(R.id.score);
        score.setImageResource(R.drawable.scorebut);
    }

    @Override
    protected void onPause() {
        super.onPause();
        ImageButton start = findViewById(R.id.start);
        start.setImageResource(R.drawable.newgamebut);
        ImageButton score = findViewById(R.id.score);
        score.setImageResource(R.drawable.scorebut);
    }

    private void openHistory(){
        Intent intent = new Intent(this, History.class);
        startActivity(intent);
    }

    private void openGame() {
        Intent intent = new Intent(this, MainActivityGame.class);
        startActivity(intent);
    }
}