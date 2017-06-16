package com.ictcampus.berberatr.fourzonesapp.Activities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.ictcampus.berberatr.fourzonesapp.R;

public class Main_Activity extends Activity {
    Button btn1, btn2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main_);
        btn1 = (Button) findViewById(R.id.btn1);
        btn2 = (Button) findViewById(R.id.btn2);
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo nwi = cm.getActiveNetworkInfo();
        if(nwi != null && nwi.isConnected()){

        }
        else{
            btn1.setEnabled(false);
            btn2.setEnabled(false);
        }
    }

    public void startGame(View view) {
        startActivity(new Intent(this, Game_Activity.class));
    }

    public void viewData(View view){
        startActivity(new Intent(this, Highscore_Activity.class));
    }

    public void viewSettings(View view){
        startActivity(new Intent(this, Settings_Activity.class));
    }

    public void viewAbout(View view){
        startActivity(new Intent(this, About_Activity.class));
    }
}
