package com.ictcampus.berberatr.fourzonesapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;

public class Game_Activity extends AppCompatActivity {
    private GameView view;
    Button buttonStart;
    Button buttonStop;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        view = new GameView(this);
        setContentView(view);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int action = event.getActionMasked();
        switch (action) {
            case MotionEvent.ACTION_DOWN:
                view.newRound((int)event.getX(),(int)event.getY() );
                if(!view.getThread().isAlive()){
                    view.getThread().start();
                }
                view.postInvalidate();
                break;
            case MotionEvent.ACTION_MOVE:
                view.setTouchY(event.getY());
                view.setTouchX(event.getX());
                break;
            case MotionEvent.ACTION_UP:
                view.setTouched(false);
                view.setHit(false);
                view.postInvalidate();
                setContentView(R.layout.activity_game_);
                buttonStart = (Button) findViewById(R.id.restartGameButton);
                buttonStop = (Button) findViewById(R.id.leaderBoardButton);
                setListener();
                break;
            case MotionEvent.ACTION_CANCEL:
                break;
        }
        return true;
    }

    private void setListener(){
        buttonStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                view = new GameView(v.getContext());
                setContentView(view);
            }
        });

        buttonStop.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), GetData.class);
                startActivity(intent);
            }
        });
    }

}