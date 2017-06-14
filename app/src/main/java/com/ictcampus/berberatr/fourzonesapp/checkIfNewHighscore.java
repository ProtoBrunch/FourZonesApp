package com.ictcampus.berberatr.fourzonesapp;

import android.os.AsyncTask;
import android.util.Log;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Iterator;

import static java.lang.Integer.parseInt;

/**
 * Created by berberatr on 14.06.2017.
 */

public class checkIfNewHighscore extends AsyncTask<String, Void ,Boolean>{
    int score;
    String scoreString;
    StringBuilder stringBuilder = new StringBuilder();
    ArrayList<String[]> scoreList;
    boolean newScore;
    public AsyncResponse delegate = null;

    public  checkIfNewHighscore(int score, AsyncResponse delegate) throws IOException{
        this.score = score;
        this.delegate = delegate;
    }

    public BufferedReader establishConnection()throws IOException{
        final String AUTHKEY = "Test123";

        String authkey = URLEncoder.encode("authkey" , "UTF-8")+ "=" + URLEncoder.encode(AUTHKEY, "UTF-8");
        URL url = new URL("https://fourzones.000webhostapp.com/get_highscore.php");
        URLConnection connection = url.openConnection();
        connection.setDoOutput(true);
        OutputStreamWriter outputStreamWriter = new OutputStreamWriter(connection.getOutputStream());
        outputStreamWriter.write(authkey);
        outputStreamWriter.flush();
        BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        return reader;
    }

    public void readString(BufferedReader reader) throws IOException{
        String dataString ;
        while ((dataString = reader.readLine()) != null){
            stringBuilder.append(dataString);
        }
        scoreString = stringBuilder.toString();
    }

    public boolean checkAndCompareScores(){
        Iterator it = scoreList.iterator();

        while(it.hasNext()){
            String[] temp = (String[])it.next();
            if((parseInt(temp[1]) < score)){
                return true;
            }
        }
        return false;
    }

    @Override
    protected Boolean doInBackground(String... params) {
        try {
            readString(establishConnection());
            turnStringIntoArrayList tsial = new turnStringIntoArrayList(scoreString);
            scoreList = tsial.getArrayList();
            newScore = checkAndCompareScores();
            return newScore;
        }catch(IOException e){
            e.printStackTrace();
            return null;
        }
    }

    @Override
    protected void onPostExecute(Boolean aBoolean) {
        super.onPostExecute(aBoolean);
        Log.d("NewScore?", Boolean.toString(aBoolean));
        delegate.processFinish(aBoolean);

    }
}
