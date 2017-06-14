package com.ictcampus.berberatr.fourzonesapp;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;

public class AddData extends Activity {
    EditText etName, etEmail, etPhone;
    String strName, strEmail, strPhone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_data);
        etName = (EditText)findViewById(R.id.et_name);
        etEmail = (EditText)findViewById(R.id.et_email);
        etPhone = (EditText)findViewById(R.id.et_phone);
    }

    public void sendInfo(View view){
        strName = etName.getText().toString();
        strEmail = etEmail.getText().toString();
        strPhone = etPhone.getText().toString();
        BackgroundTask backgroundTask = new BackgroundTask();
        backgroundTask.execute(strName,strEmail,strPhone);
    }

    class BackgroundTask extends AsyncTask<String, Void, String> {
        String add_data_url;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            add_data_url = "https://fourzones.000webhostapp.com/add_info.php";
        }

        @Override
        protected String doInBackground(String... args) {
            String name, email, phone;
            name = args[0];
            email = args[1];
            phone = args[2];

            try {
                URL url = new URL(add_data_url);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);
                OutputStream out = httpURLConnection.getOutputStream();
                BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(out, "UTF-8"));

                String data_string =
                        URLEncoder.encode("name", "UTF-8") + "=" +  URLEncoder.encode(name, "UTF-8") + "&" +
                                URLEncoder.encode("email", "UTF-8") + "=" +  URLEncoder.encode(email, "UTF-8") + "&" +
                                URLEncoder.encode("phone", "UTF-8") + "=" +  URLEncoder.encode(phone, "UTF-8");

                writer.write(data_string);
                writer.flush();
                writer.close();
                out.close();
                InputStream in = httpURLConnection.getInputStream();
                in.close();
                httpURLConnection.disconnect();
                return "Data transmitted.";

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            Toast.makeText(getApplicationContext(), result, Toast.LENGTH_SHORT).show();
        }
    }
}

