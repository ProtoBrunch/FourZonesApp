package com.ictcampus.berberatr.fourzonesapp.Activities;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.ictcampus.berberatr.fourzonesapp.R;
import com.ictcampus.berberatr.fourzonesapp.Custom.turnStringIntoArrayList;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Iterator;


public class Highscore_Activity extends Activity {
    ProgressDialog progress;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initProgressDialog();
        progress.show();
        setContentView(R.layout.activity_get_data);
        new BackgroundTask().execute();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    private class BackgroundTask extends AsyncTask<String, String, View> {
        private static final String AUTHKEY = "Test123";

        @Override
        protected View doInBackground(String... params) {
            try{
                String authkey = URLEncoder.encode("authkey" , "UTF-8")+ "=" + URLEncoder.encode(AUTHKEY, "UTF-8");
                URL url = new URL("https://fourzones.000webhostapp.com/get_highscore.php");
                URLConnection connection = url.openConnection();
                connection.setDoOutput(true);
                OutputStreamWriter outputStreamWriter = new OutputStreamWriter(connection.getOutputStream());
                outputStreamWriter.write(authkey);
                outputStreamWriter.flush();
                BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                StringBuilder stringBuilder = new StringBuilder();
                String dataString ;
                while ((dataString = reader.readLine()) != null){
                    stringBuilder.append(dataString);
                }
                Log.d("Check", "gotdata");
                turnStringIntoArrayList dad = new turnStringIntoArrayList(stringBuilder.toString());
                ArrayList<String[]> scores = dad.getArrayList();
                Log.d("Check", "about to return");
                return  addRowsToTable(scores);
            } catch (UnsupportedEncodingException e) {
                Log.e("Exception", "UnsupportedEncoding");
            } catch (MalformedURLException e) {
                Log.e("Exception", "MalformedURL");
                e.printStackTrace();
            } catch (IOException e) {
                Log.e("Exception", "IO");
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(View v) {
            super.onPostExecute(v);
            progress.dismiss();
            setContentView(v);
            Log.d("Check" , "Set content view");
        }
    }

    private View addRowsToTable(ArrayList<String[]> scores){
        int rank = 1;
        LayoutInflater inflater = (LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.activity_get_data, null);

        //setup Layout_Params
        TableRow.LayoutParams param = new TableRow.LayoutParams(
                TableLayout.LayoutParams.MATCH_PARENT,
                TableLayout.LayoutParams.WRAP_CONTENT,
                1.0f);

        // Find TableLayout by id
        TableLayout tableLayout = (TableLayout)view.findViewById(R.id.highscoretable);

        Iterator it = scores.iterator();

        while(it.hasNext()) {
            String[] temp = (String[])it.next();
            float textSize = 25;
            // Create a new TableRow
            TableRow tableRow = new TableRow(this);

            //Create the first Textview
            TextView textViewOne = new TextView(this);
            textViewOne.setText(String.valueOf(rank));
            textViewOne.setGravity(Gravity.START);
            textViewOne.setLayoutParams(param);
            textViewOne.setTextSize(TypedValue.COMPLEX_UNIT_SP, textSize);

            //Create the second Textview
            TextView textViewTwo = new TextView(this);
            textViewTwo.setText(temp[0]);
            textViewTwo.setGravity(Gravity.CENTER);
            textViewTwo.setLayoutParams(param);
            textViewTwo.setTextSize(TypedValue.COMPLEX_UNIT_SP, textSize);

            //Create the third Textview
            TextView textViewThree = new TextView(this);
            textViewThree.setText(temp[1]);
            textViewThree.setGravity(Gravity.END);
            textViewThree.setLayoutParams(param);
            textViewThree.setTextSize(TypedValue.COMPLEX_UNIT_SP, textSize);

            // add TextViews to the Tablerow
            tableRow.addView(textViewOne);
            tableRow.addView(textViewTwo);
            tableRow.addView(textViewThree);

            // Add the TableRow to the TableLayout
            tableLayout.addView(tableRow);

            // Increment the rank
            rank++;
        }

        // Display the View
        rank = 1;
        return view;
    }

    private void initProgressDialog(){
        progress = new ProgressDialog(this);
        progress.setTitle("Loading Highscore-Data");
        progress.setMessage("Please Stand By.");
        progress.setCancelable(false);
    }
}

