package com.ictcampus.berberatr.fourzonesapp;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;

public class Game_Activity extends AppCompatActivity {
    private CircleView view;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_FULLSCREEN);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        view = new CircleView(this);
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
                /*Intent intent = new Intent(getApplicationContext(), Main_Activity.class);
                startActivity(intent);*/
                //setContentView(new GameOverView(this));
                break;
            case MotionEvent.ACTION_CANCEL:
                break;
        }
        return true;
    }

    class GameOverView extends View {

        private Button restartGameButton, leaderBoardButton;

        public GameOverView(final Context context){
            super(context);
            LinearLayout layout = (LinearLayout) findViewById(R.id.layoutGame);
            restartGameButton = new Button(context);
            restartGameButton.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    setContentView(view);
                }
            });
            leaderBoardButton = new Button(context);
            leaderBoardButton.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    Intent intent = new Intent(getApplicationContext(), Main_Activity.class);
                    startActivity(intent);
                }
            });
            layout.addView(restartGameButton);
            layout.addView(leaderBoardButton);
        }


    }
}

