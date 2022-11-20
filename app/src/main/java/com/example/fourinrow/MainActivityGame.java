package com.example.fourinrow;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.Date;

public class MainActivityGame extends AppCompatActivity {

    JSONArray array = new JSONArray();
    private int turn = 0; //0 is yellow, 1 is red
    private int boardId[][] = new int[6][7];
    private int board[][] = new int[6][7]; //1 is yellow, 2 is red
    private int yellow_score = 0;
    private int red_score = 0;
    private boolean endgame = false;

    //Four in a row game
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_game);
        loadData();
        playGame();

        //hide action bar
        getSupportActionBar().hide();

        //onclick back_but to go back to main activity
        findViewById(R.id.back_but).setOnClickListener(v -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Warning");
            builder.setMessage("Are you sure you want to exit?");
            builder.setPositiveButton("Yes", (dialog, which) -> {
                finish();
            });
            builder.setNegativeButton("No", (dialog, which) -> {
                dialog.dismiss();
            });
            builder.show();
//            Intent intent = new Intent(this, MainActivity.class);
//            startActivity(intent);
//            finish();
        });
    }

    //handle press back button show warning dialog
    @Override
    public void onBackPressed() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Warning");
        builder.setMessage("Are you sure you want to exit?");
        builder.setPositiveButton("Yes", (dialog, which) -> {
            finish();
        });
        builder.setNegativeButton("No", (dialog, which) -> {
            dialog.dismiss();
        });
        builder.show();
    }

    private void playGame(){
        //set default board
        checkTurn();
        showScore();
        loadDefaultBoard();
    }

    private void loadDefaultBoard() {
        yellow_score = 0;
        red_score = 0;
        showScore();

        //set all imageview to def
        for (int i = 1; i < 43; i++) {
            String imgID = "imageView" + i;
            int resID = getResources().getIdentifier(imgID, "id", getPackageName());
            ImageView img = findViewById(resID);
            img.setImageResource(R.drawable.def);
        }
        for (int i = 2; i < 7; i++) {
            String imgID2 = "b" + i;
            int resID2 = getResources().getIdentifier(imgID2, "id", getPackageName());
            ImageView img2 = findViewById(resID2);
            img2.setImageResource(R.drawable.base);

            String imgID3 = "t" + i;
            int resID3 = getResources().getIdentifier(imgID3, "id", getPackageName());
            ImageView img3 = findViewById(resID3);
            img3.setImageResource(R.drawable.top);
        }
        ImageView bL = findViewById(R.id.b1);
        bL.setImageResource(R.drawable.baseleft);
        ImageView bR = findViewById(R.id.b7);
        bR.setImageResource(R.drawable.baseright);

        ImageView tL = findViewById(R.id.t1);
        tL.setImageResource(R.drawable.topleft);
        ImageView tR = findViewById(R.id.t7);
        tR.setImageResource(R.drawable.topright);
        setZeroBoard();
    }

    private void setZeroBoard(){
        int id = 1;
        for(int i = 0; i < 6; i++){
            for(int j = 0; j < 7; j++){
                board[i][j] = 0;
                boardId[i][j] = id;
                id++;
            }
        }
    }

    private void checkDraw(){
        int count = 0;
        for(int i = 0; i < 6; i++){
            for(int j = 0; j < 7; j++){
                if(board[i][j] != 0){
                    count++;
                }
            }
        }
        if(count == 42){
            showActivityWin(3);
        }
    }

    @SuppressLint("SetTextI18n")
    public void showActivityWin(int status){
        //floating layout win
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View view = getLayoutInflater().inflate(R.layout.show_wins, null);
        builder.setView(view);


        //set image win
        ImageView imgWin = view.findViewById(R.id.win_coin);
        ImageView txtWin = view.findViewById(R.id.win_text);
        if(status == 1){
            imgWin.setImageResource(R.drawable.yellow);
            txtWin.setImageResource(R.drawable.yellow_win);
        }else if (status == 2){
            imgWin.setImageResource(R.drawable.red);
            txtWin.setImageResource(R.drawable.red_win);
        }else if (status == 3){
            imgWin.setImageResource(R.drawable.draw);
            txtWin.setImageResource(R.drawable.draw_win);
            //hide edit text
            view.findViewById(R.id.editTextTextPersonName3).setVisibility(View.GONE);

        }

        //set show score
        if (status == 1){
            TextView txtScore = view.findViewById(R.id.your_score);
            txtScore.setText("Number of move: " + yellow_score + "\nEnter your name:");
        } else if (status == 2){
            TextView txtScore = view.findViewById(R.id.your_score);
            txtScore.setText("Number of move: " + red_score + "\nEnter your name:");
        } else if (status == 3){
            TextView txtScore = view.findViewById(R.id.your_score);
            txtScore.setText("Draw!!");
        }




        AlertDialog dialog = builder.create();
        if (endgame == false){
            dialog.show();
            endgame = true;
        }

        //click back to main.
        view.findViewById(R.id.button_back).setOnClickListener(v -> {


            //get name
            EditText name = view.findViewById(R.id.editTextTextPersonName3);
            String nameStr = name.getText().toString();
            if (nameStr.equals("")){
                nameStr = "Unknown";
            }

            if (status == 1){
                writeData(nameStr, yellow_score);
            } else if (status == 2){
                writeData(nameStr, red_score);
            }
            dialog.dismiss();
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);

            //close activity
            finish();
        });



        //handle click space not close dialog
        dialog.setCanceledOnTouchOutside(false);
    }

    public void checkWin(){
        //check horizontal
        for(int i = 0; i < 6; i++){
            for(int j = 0; j < 4; j++){
                if(board[i][j] == board[i][j+1] && board[i][j] == board[i][j+2] && board[i][j] == board[i][j+3] && board[i][j] != 0){
                    if(board[i][j] == 1){
                        showActivityWin(1);
                    }else if (board[i][j] == 2){
                        showActivityWin(2);
                    }
                }
            }
        }
        //check vertical
        for(int i = 0; i < 3; i++){
            for(int j = 0; j < 7; j++){
                if(board[i][j] == board[i+1][j] && board[i][j] == board[i+2][j] && board[i][j] == board[i+3][j] && board[i][j] != 0){
                    if(board[i][j] == 1){
                        showActivityWin(1);
                    }else if (board[i][j] == 2){
                        showActivityWin(2);
                    }
                }
            }
        }
        //check diagonal
        for(int i = 0; i < 3; i++){
            for(int j = 0; j < 4; j++){
                if(board[i][j] == board[i+1][j+1] && board[i][j] == board[i+2][j+2] && board[i][j] == board[i+3][j+3] && board[i][j] != 0){
                    if(board[i][j] == 1){
                        //alertShowWinner(1);
                        showActivityWin(1);
                    }else if (board[i][j] == 2){
                        //alertShowWinner(2);
                        showActivityWin(2);
                    }
                }
            }
        }
        for(int i = 0; i < 3; i++) {
            for (int j = 3; j < 7; j++) {
                if (board[i][j] == board[i + 1][j - 1] && board[i][j] == board[i + 2][j - 2] && board[i][j] == board[i + 3][j - 3] && board[i][j] != 0) {
                    if (board[i][j] == 1) {
                        //alertShowWinner(1);
                        showActivityWin(1);
                    } else if (board[i][j] == 2) {
                        //alertShowWinner(2);
                        showActivityWin(2);
                    }
                }
            }
        }
    }

    public void alertShowWinner(int winner){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Winner");
        if(winner == 1){
            builder.setMessage("Yellow wins");
        }else if(winner == 2){
            builder.setMessage("Red wins");
        }
        builder.setPositiveButton("NewGame", (dialog, which) -> {
            playGame();
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    public int checkTurn(){
        if(turn == 0){
            TextView txtTurn = findViewById(R.id.red_turn);
            txtTurn.setVisibility(View.VISIBLE);
            TextView txtTurn2 = findViewById(R.id.yellow_turn);
            txtTurn2.setVisibility(View.INVISIBLE);
            turn = 1;
            return 0;
        }else{
            TextView txtTurn = findViewById(R.id.red_turn);
            txtTurn.setVisibility(View.INVISIBLE);
            TextView txtTurn2 = findViewById(R.id.yellow_turn);
            txtTurn2.setVisibility(View.VISIBLE);
            turn = 0;
            return 1;
        }
    }


    public void runRow(int column){
        for(int i = 5; i >= 0; i--){
            if(board[i][column] == 0){
                if (turn == 0){
                    board[i][column] = 1;
                }
                else {
                    board[i][column] = 2;
                }
                String imgID = "imageView" + boardId[i][column];
                int resID = getResources().getIdentifier(imgID, "id", getPackageName());
                if (checkTurn() == 0) {
                    setAnimation(i, column, 0);
                } else {
                    setAnimation(i, column, 1);
                }
                break;
            }
        }
    }

    private void showScore(){
        TextView txtScore = findViewById(R.id.yellow_score);
        txtScore.setText(String.valueOf(yellow_score));
        TextView txtScore2 = findViewById(R.id.red_score);
        txtScore2.setText(String.valueOf(red_score));
    }

    private void changeScore(int status){
        if (status == 0){
            yellow_score++;
            showScore();
        } else if(status == 1){
            red_score++;
            showScore();
        }
    }
    public void setAnimation(int input, int column, int status){
        int i = 0;

        changeScore(status);
        String imgID = "imageView" + boardId[i][column];
        int resID = getResources().getIdentifier(imgID, "id", getPackageName());
        ImageView img1 = findViewById(resID);

        Handler handler = new Handler();
        if (i >= input){
            imgID = "imageView" + boardId[i][column];
            resID = getResources().getIdentifier(imgID, "id", getPackageName());
            ImageView done = findViewById(resID);
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    if (status == 0)
                        done.setImageResource(R.drawable.y1);
                    else
                        done.setImageResource(R.drawable.r1);
                }
            }, 0);
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    if (status == 0)
                        done.setImageResource(R.drawable.y2);
                    else
                        done.setImageResource(R.drawable.r2);
                }
            }, 25);
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    if (status == 0)
                        done.setImageResource(R.drawable.y3);
                    else
                        done.setImageResource(R.drawable.r3);
                }
            }, 50);
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    if (status == 0)
                        done.setImageResource(R.drawable.y4);
                    else
                        done.setImageResource(R.drawable.r4);
                }
            }, 75);
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    checkDraw();
                    checkWin();
                }
            }, 200);
            return;
        }
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (status == 0)
                    img1.setImageResource(R.drawable.y1);
                else
                    img1.setImageResource(R.drawable.r1);
            }
        }, 0);
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (status == 0)
                    img1.setImageResource(R.drawable.y2);
                else
                    img1.setImageResource(R.drawable.r2);
            }
        }, 25);
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (status == 0)
                    img1.setImageResource(R.drawable.y3);
                else
                    img1.setImageResource(R.drawable.r3);
            }
        }, 50);
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (status == 0)
                    img1.setImageResource(R.drawable.y4);
                else
                    img1.setImageResource(R.drawable.r4);
            }
        }, 75);
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (status == 0)
                    img1.setImageResource(R.drawable.y5);
                else
                    img1.setImageResource(R.drawable.r5);
            }
        }, 100);
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (status == 0)
                    img1.setImageResource(R.drawable.y6);
                else
                    img1.setImageResource(R.drawable.r6);
            }
        }, 125);
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (status == 0)
                    img1.setImageResource(R.drawable.y7);
                else
                    img1.setImageResource(R.drawable.r7);
            }
        }, 150);
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                img1.setImageResource(R.drawable.def);
            }
        }, 175);
        i++;
        if (i >= input){
            imgID = "imageView" + boardId[i][column];
            resID = getResources().getIdentifier(imgID, "id", getPackageName());
            ImageView done = findViewById(resID);
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    if (status == 0)
                        done.setImageResource(R.drawable.y1);
                    else
                        done.setImageResource(R.drawable.r1);
                }
            }, 150);
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    if (status == 0)
                        done.setImageResource(R.drawable.y2);
                    else
                        done.setImageResource(R.drawable.r2);
                }
            }, 175);
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    if (status == 0)
                        done.setImageResource(R.drawable.y3);
                    else
                        done.setImageResource(R.drawable.r3);
                }
            }, 200);
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    if (status == 0)
                        done.setImageResource(R.drawable.y4);
                    else
                        done.setImageResource(R.drawable.r4);
                }
            }, 225);
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    checkDraw();
                    checkWin();
                }
            }, 250);
            return;
        }
        imgID = "imageView" + boardId[i][column];
        resID = getResources().getIdentifier(imgID, "id", getPackageName());
        ImageView img2 = findViewById(resID);
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (status == 0)
                    img2.setImageResource(R.drawable.y1);
                else
                    img2.setImageResource(R.drawable.r1);
            }
        }, 150);
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (status == 0)
                    img2.setImageResource(R.drawable.y2);
                else
                    img2.setImageResource(R.drawable.r2);
            }
        }, 175);
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (status == 0)
                    img2.setImageResource(R.drawable.y3);
                else
                    img2.setImageResource(R.drawable.r3);
            }
        }, 200);
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (status == 0)
                    img2.setImageResource(R.drawable.y4);
                else
                    img2.setImageResource(R.drawable.r4);
            }
        }, 225);
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (status == 0)
                    img2.setImageResource(R.drawable.y5);
                else
                    img2.setImageResource(R.drawable.r5);
            }
        }, 250);
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (status == 0)
                    img2.setImageResource(R.drawable.y6);
                else
                    img2.setImageResource(R.drawable.r6);
            }
        }, 275);
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (status == 0)
                    img2.setImageResource(R.drawable.y7);
                else
                    img2.setImageResource(R.drawable.r7);
            }
        },  300);
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                img2.setImageResource(R.drawable.def);
            }
        }, 325);
        i++;
        if (i >= input){
            imgID = "imageView" + boardId[i][column];
            resID = getResources().getIdentifier(imgID, "id", getPackageName());
            ImageView done = findViewById(resID);
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    if (status == 0)
                        done.setImageResource(R.drawable.y1);
                    else
                        done.setImageResource(R.drawable.r1);
                }
            }, 300);
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    if (status == 0)
                        done.setImageResource(R.drawable.y2);
                    else
                        done.setImageResource(R.drawable.r2);
                }
            }, 325);
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    if (status == 0)
                        done.setImageResource(R.drawable.y3);
                    else
                        done.setImageResource(R.drawable.r3);
                }
            }, 350);
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    if (status == 0)
                        done.setImageResource(R.drawable.y4);
                    else
                        done.setImageResource(R.drawable.r4);
                }
            }, 375);
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    checkDraw();
                    checkWin();
                }
            }, 400);
            return;
        }
        imgID = "imageView" + boardId[i][column];
        resID = getResources().getIdentifier(imgID, "id", getPackageName());
        ImageView img3 = findViewById(resID);
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (status == 0)
                    img3.setImageResource(R.drawable.y1);
                else
                    img3.setImageResource(R.drawable.r1);
            }
        }, 300);
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (status == 0)
                    img3.setImageResource(R.drawable.y2);
                else
                    img3.setImageResource(R.drawable.r2);
            }
        }, 325);
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (status == 0)
                    img3.setImageResource(R.drawable.y3);
                else
                    img3.setImageResource(R.drawable.r3);
            }
        }, 350);
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (status == 0)
                    img3.setImageResource(R.drawable.y4);
                else
                    img3.setImageResource(R.drawable.r4);
            }
        }, 375);
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (status == 0)
                    img3.setImageResource(R.drawable.y5);
                else
                    img3.setImageResource(R.drawable.r5);
            }
        }, 400);
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (status == 0)
                    img3.setImageResource(R.drawable.y6);
                else
                    img3.setImageResource(R.drawable.r6);
            }
        }, 425);
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (status == 0)
                    img3.setImageResource(R.drawable.y7);
                else
                    img3.setImageResource(R.drawable.r7);
            }
        }, 450);
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                img3.setImageResource(R.drawable.def);
            }
        }, 475);
        i++;
        if (i >= input){
            imgID = "imageView" + boardId[i][column];
            resID = getResources().getIdentifier(imgID, "id", getPackageName());
            ImageView done = findViewById(resID);
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    if (status == 0)
                        done.setImageResource(R.drawable.y1);
                    else
                        done.setImageResource(R.drawable.r1);
                }
            }, 450);
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    if (status == 0)
                        done.setImageResource(R.drawable.y2);
                    else
                        done.setImageResource(R.drawable.r2);
                }
            }, 475);
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    if (status == 0)
                        done.setImageResource(R.drawable.y3);
                    else
                        done.setImageResource(R.drawable.r3);
                }
            }, 500);
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    if (status == 0)
                        done.setImageResource(R.drawable.y4);
                    else
                        done.setImageResource(R.drawable.r4);
                }
            }, 525);
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    checkDraw();
                    checkWin();
                }
            }, 550);
            return;
        }
        imgID = "imageView" + boardId[i][column];
        resID = getResources().getIdentifier(imgID, "id", getPackageName());
        ImageView img4 = findViewById(resID);
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (status == 0)
                    img4.setImageResource(R.drawable.y1);
                else
                    img4.setImageResource(R.drawable.r1);
            }
        }, 450);
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (status == 0)
                    img4.setImageResource(R.drawable.y2);
                else
                    img4.setImageResource(R.drawable.r2);
            }
        }, 475);
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (status == 0)
                    img4.setImageResource(R.drawable.y3);
                else
                    img4.setImageResource(R.drawable.r3);
            }
        }, 500);
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (status == 0)
                    img4.setImageResource(R.drawable.y4);
                else
                    img4.setImageResource(R.drawable.r4);
            }
        }, 525);
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (status == 0)
                    img4.setImageResource(R.drawable.y5);
                else
                    img4.setImageResource(R.drawable.r5);
            }
        }, 550);
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (status == 0)
                    img4.setImageResource(R.drawable.y6);
                else
                    img4.setImageResource(R.drawable.r6);
            }
        }, 575);
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (status == 0)
                    img4.setImageResource(R.drawable.y7);
                else
                    img4.setImageResource(R.drawable.r7);
            }
        }, 600);
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                img4.setImageResource(R.drawable.def);
            }
        }, 625);
        i++;
        if (i >= input){
            imgID = "imageView" + boardId[i][column];
            resID = getResources().getIdentifier(imgID, "id", getPackageName());
            ImageView done = findViewById(resID);
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    if (status == 0)
                        done.setImageResource(R.drawable.y1);
                    else
                        done.setImageResource(R.drawable.r1);
                }
            }, 600);
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    if (status == 0)
                        done.setImageResource(R.drawable.y2);
                    else
                        done.setImageResource(R.drawable.r2);
                }
            }, 625);
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    if (status == 0)
                        done.setImageResource(R.drawable.y3);
                    else
                        done.setImageResource(R.drawable.r3);
                }
            }, 650);
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    if (status == 0)
                        done.setImageResource(R.drawable.y4);
                    else
                        done.setImageResource(R.drawable.r4);
                }
            }, 675);
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    checkDraw();
                    checkWin();
                }
            }, 675);
            return;
        }
        imgID = "imageView" + boardId[i][column];
        resID = getResources().getIdentifier(imgID, "id", getPackageName());
        ImageView img5 = findViewById(resID);
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (status == 0)
                    img5.setImageResource(R.drawable.y1);
                else
                    img5.setImageResource(R.drawable.r1);
            }
        }, 650);
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (status == 0)
                    img5.setImageResource(R.drawable.y2);
                else
                    img5.setImageResource(R.drawable.r2);
            }
        }, 675);
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (status == 0)
                    img5.setImageResource(R.drawable.y3);
                else
                    img5.setImageResource(R.drawable.r3);
            }
        }, 700);
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (status == 0)
                    img5.setImageResource(R.drawable.y4);
                else
                    img5.setImageResource(R.drawable.r4);
            }
        }, 725);
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (status == 0)
                    img5.setImageResource(R.drawable.y5);
                else
                    img5.setImageResource(R.drawable.r5);
            }
        }, 750);
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (status == 0)
                    img5.setImageResource(R.drawable.y6);
                else
                    img5.setImageResource(R.drawable.r6);
            }
        }, 775);
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (status == 0)
                    img5.setImageResource(R.drawable.y7);
                else
                    img5.setImageResource(R.drawable.r7);
            }
        }, 800);
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                img5.setImageResource(R.drawable.def);
            }
        }, 825);
        i++;
        if (i >= input){
            imgID = "imageView" + boardId[i][column];
            resID = getResources().getIdentifier(imgID, "id", getPackageName());
            ImageView done = findViewById(resID);
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    if (status == 0)
                        done.setImageResource(R.drawable.y1);
                    else
                        done.setImageResource(R.drawable.r1);
                }
            }, 800);
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    if (status == 0)
                        done.setImageResource(R.drawable.y2);
                    else
                        done.setImageResource(R.drawable.r2);
                }
            }, 825);
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    if (status == 0)
                        done.setImageResource(R.drawable.y3);
                    else
                        done.setImageResource(R.drawable.r3);
                }
            }, 850);
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    if (status == 0)
                        done.setImageResource(R.drawable.y4);
                    else
                        done.setImageResource(R.drawable.r4);
                }
            }, 875);
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    checkDraw();
                    checkWin();
                }
            }, 900);
            return;
        }
    }

    public void onClickRow1(View view) {
        runRow(0);
    }

    public void onClickRow2(View view) {
        runRow(1);
    }

    public void onClickRow3(View view) {
        runRow(2);
    }

    public void onClickRow4(View view) {
        runRow(3);
    }

    public void onClickRow5(View view) {
        runRow(4);
    }

    public void onClickRow6(View view) {
        runRow(5);
    }

    public void onClickRow7(View view) {
        runRow(6);
    }

    public void writeData(String name, int score){
        JSONObject obj = new JSONObject();
        try {
            obj.put("name", name);
            obj.put("score", score+"");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        array.put(obj);

        String filename = "FourARow.txt";
        FileOutputStream outputStream;
        //String storageState = Environment.getExternalStorageState();
        //store in internal storage
        try {
            outputStream = openFileOutput(filename, Context.MODE_PRIVATE);
            outputStream.write(array.toString().getBytes());
            outputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
//        if (storageState.equals(Environment.MEDIA_MOUNTED)) {
//            try {
//                File file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), filename);
//                outputStream = new FileOutputStream(file);
//                outputStream.write(array.toString().getBytes());
//                outputStream.close();
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//        }
    }

    public void loadData(){
        String filename = "FourARow.txt";
        String inputString;
        //String storageState = Environment.getExternalStorageState();
        //load from internal storage
        try {
            FileInputStream inputStream = openFileInput(filename);
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
            StringBuilder stringBuilder = new StringBuilder();
            while ((inputString = bufferedReader.readLine()) != null) {
                stringBuilder.append(inputString).append("\n");
            }

            JSONArray infile_array = new JSONArray(stringBuilder.toString());
            for (int i = 0; i < infile_array.length(); i++) {
                array.put(infile_array.getJSONObject(i));
            }
            bufferedReader.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
//        if (storageState.equals(Environment.MEDIA_MOUNTED)) {
//            try {
//                File file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), filename);
//                BufferedReader inputReader = new BufferedReader(new InputStreamReader(
//                        new FileInputStream(file)));
//                StringBuilder stringBuffer = new StringBuilder();
//                while ((inputString = inputReader.readLine()) != null) {
//                    stringBuffer.append(inputString).append("\n");
//                }
//
//                JSONArray infile_array = new JSONArray(stringBuffer.toString());
//                for (int i = 0; i < infile_array.length(); i++) {
//                    array.put(infile_array.getJSONObject(i));
//                }
//                inputReader.close();
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//        }//end if
    }
}