package com.ictcampus.berberatr.fourzonesapp.Activities;

import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.CompoundButton;
import android.widget.ToggleButton;

import com.ictcampus.berberatr.fourzonesapp.R;

public class Settings_Activity extends AppCompatActivity {

    ToggleButton colorBlindToggleButton, darkThemeToggleButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_settings_);

        SharedPreferences prefs = getApplicationContext().getSharedPreferences("MyPref", 0);

        colorBlindToggleButton = (ToggleButton) findViewById(R.id.colorBlindToggleButton);

        colorBlindToggleButton.setChecked(prefs.getBoolean("colorBlind", false));
        //TODO: add more settings (difficulty...)
        colorBlindToggleButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                SharedPreferences pref = getApplicationContext().getSharedPreferences("MyPref", MODE_PRIVATE);
                SharedPreferences.Editor editor = pref.edit();
                if (isChecked) {
                    editor.putBoolean("colorBlind", true);
                } else {
                    editor.putBoolean("colorBlind", false);
                }
                editor.apply();
            }
        });

        darkThemeToggleButton = (ToggleButton)findViewById(R.id.darkTHemeToggleButton);
        darkThemeToggleButton.setChecked(prefs.getBoolean("darkTheme", false));

        darkThemeToggleButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                SharedPreferences pref = getApplicationContext().getSharedPreferences("MyPref", MODE_PRIVATE);
                SharedPreferences.Editor editor = pref.edit();
                if (isChecked) {
                    editor.putBoolean("darkTheme", true);
                } else {
                    editor.putBoolean("darkTheme", false);
                }
                editor.apply();
            }
        });



    }
}
