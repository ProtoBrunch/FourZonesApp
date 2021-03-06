package com.ictcampus.berberatr.fourzonesapp.Activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.ictcampus.berberatr.fourzonesapp.Custom.AsyncResponse;
import com.ictcampus.berberatr.fourzonesapp.Views.GameView;
import com.ictcampus.berberatr.fourzonesapp.R;
import com.ictcampus.berberatr.fourzonesapp.Custom.SendData;
import com.ictcampus.berberatr.fourzonesapp.Custom.checkIfNewHighscore;

import java.io.IOException;

public class Game_Activity extends AppCompatActivity{
    private GameView view;
    Button buttonStart, buttonStop, buttonSubmit;
    TextView tvScore, tvSample;
    EditText etName;
    boolean something;
    boolean clickedNewGame;
    ProgressDialog pG;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setupPG();
        clickedNewGame = true;
        view = new GameView(this);
        setContentView(view);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int action = event.getActionMasked();
        if(clickedNewGame) {
            switch (action) {
                case MotionEvent.ACTION_DOWN:
                    view.newRound((int) event.getX(), (int) event.getY());
                    if (!view.getThread().isAlive()) {
                        view.getThread().start();
                    }
                    view.postInvalidate();
                    break;
                case MotionEvent.ACTION_MOVE:
                    view.setTouchY(event.getY());
                    view.setTouchX(event.getX());
                    break;
                case MotionEvent.ACTION_UP:
                    pG.show();

                    setContentView(R.layout.activity_game_);

                    buttonSubmit = (Button) findViewById(R.id.buttonSubmit);
                    etName = (EditText) findViewById(R.id.etName);
                    try {
                        new checkIfNewHighscore(view.getCounter(), new AsyncResponse() {
                            @Override
                            public void processFinish(Boolean output) {
                                something = output;
                                pG.dismiss();
                                tvSample = (TextView) findViewById(R.id.tvNewHighscore);
                                if (output) {
                                    tvSample.setText("You've reached a new Highscore!");
                                    buttonSubmit.setVisibility(View.VISIBLE);
                                    buttonSubmit.setEnabled(true);
                                    etName.setVisibility(View.VISIBLE);
                                    etName.setEnabled(true);
                                } else {
                                    if((int) (Math.random() * 10)+1== 10) {
                                        tvSample.setText("Git gud");
                                    }else{
                                        tvSample.setText("No new Highscore");
                                    }
                                    buttonSubmit.setVisibility(View.INVISIBLE);
                                    buttonSubmit.setEnabled(false);
                                    etName.setVisibility(View.INVISIBLE);
                                    etName.setEnabled(false);
                                }
                            }
                        }).execute();

                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    buttonStart = (Button) findViewById(R.id.restartGameButton);
                    buttonStop = (Button) findViewById(R.id.leaderBoardButton);
                    tvScore = (TextView) findViewById(R.id.tvScore);
                    tvScore.setText(Integer.toString(view.getCounter()));

                    setListener();
                    clickedNewGame = false;
                    break;
                case MotionEvent.ACTION_CANCEL:
                    break;
            }
        }
        return true;
    }

    private void setListener() {
        buttonStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickedNewGame = true;
                view = new GameView(v.getContext());
                setContentView(view);
            }
        });

        buttonStop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), Highscore_Activity.class);
                startActivity(intent);
            }
        });

        buttonSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new SendData(etName.getText().toString(), view.getCounter(), new AsyncResponse() {
                    @Override
                    public void processFinish(Boolean output) {
                        Toast.makeText(getApplicationContext(), "Score Submitted", Toast.LENGTH_SHORT).show();
                    }
                }).execute();
                buttonSubmit.setEnabled(false);
            }
        });
    }


    private void setupPG(){
        pG = new ProgressDialog(this);
        pG.setTitle("Checking for Highscore");
        pG.setMessage("Please Stand By.");
        pG.setCancelable(false);
    }
}