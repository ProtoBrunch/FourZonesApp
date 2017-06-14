package com.ictcampus.berberatr.fourzonesapp;

import android.os.AsyncTask;
import android.widget.Toast;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

/**
 * Created by berberatr on 14.06.2017.
 */

public class SendData extends AsyncTask<String, Void, String> implements AsyncResponse{
    private String playerName, add_data_url;
    private int playerScore;
    public AsyncResponse delegate;

    public SendData(String name, int score,AsyncResponse delegate){
        this.playerName = name;
        this.playerScore = score;
        this.delegate = delegate;
    }

    @Override
    protected String doInBackground(String... params) {
        try {
            URL url = new URL(add_data_url);
            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.setRequestMethod("POST");
            httpURLConnection.setDoOutput(true);
            OutputStream out = httpURLConnection.getOutputStream();
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(out, "UTF-8"));

            String data_string =
                    URLEncoder.encode("name", "UTF-8") + "=" +  URLEncoder.encode(playerName, "UTF-8") + "&" +
                            URLEncoder.encode("score", "UTF-8") + "=" +  URLEncoder.encode(Integer.toString(playerScore), "UTF-8");

            writer.write(data_string);
            writer.flush();
            writer.close();
            out.close();
            InputStream in = httpURLConnection.getInputStream();
            in.close();
            httpURLConnection.disconnect();
            return "Data transmitted.";

        }catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        add_data_url = "https://fourzones.000webhostapp.com/add_highscore.php";
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
        delegate.processFinish(true);
    }

    @Override
    public void processFinish(Boolean output) {

    }
}
