package com.example.anthony.instantpick;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;


public class SigninActivity extends AppCompatActivity {
    String e1;
    EditText emailfield, passwordfield;
    String TempEmail, TempPassword;
    ToggleButton togSt, togHr;
    TextView tester;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.signin);
        passwordfield = (EditText) findViewById(R.id.password);
        emailfield = (EditText) findViewById(R.id.email);
        togSt = (ToggleButton) findViewById(R.id.togSt);
        togHr = (ToggleButton) findViewById(R.id.togHr);
        tester = (TextView) findViewById(R.id.tester);
        Intent i=getIntent();
        e1=i.getStringExtra("1");
        tester.setText(e1);

    }


    public void onLogin(View V) {
        TempEmail = emailfield.getText().toString();
        TempPassword = passwordfield.getText().toString();
        if(tester.getText()==e1) {
            new BackGroundLogin().execute(TempEmail, TempPassword);
        }else{
            new BackGroundLogin2().execute(TempEmail, TempPassword);

        }
    }

    public class BackGroundLogin extends AsyncTask<String, String, String> {
        public static final int CONNECTION_TIMEOUT = 10000;
        public static final int READ_TIMEOUT = 15000;

        ProgressDialog pdLoading = new ProgressDialog(SigninActivity.this);
        URL url = null;
        HttpURLConnection conn;

        @Override
        protected String doInBackground(String... params) {

            try {
                url = new URL("http://"+LoginActivity.ip.c+"/instantpick/login.php");
            } catch (MalformedURLException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
                return "exception";
            }
            try {
                conn = (HttpURLConnection) url.openConnection();
                conn.setReadTimeout(READ_TIMEOUT);
                conn.setConnectTimeout(CONNECTION_TIMEOUT);
                conn.setRequestMethod("POST");
                conn.setDoInput(true);
                conn.setDoOutput(true);
                Uri.Builder builder = new Uri.Builder()
                        .appendQueryParameter("emailfield", params[0])
                        .appendQueryParameter("passwordfield", params[1]);
                String query = builder.build().getEncodedQuery();
                OutputStream os = conn.getOutputStream();
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(os, "UTF-8"));
                bufferedWriter.write(query);
                bufferedWriter.flush();
                bufferedWriter.close();
                os.close();
            } catch (IOException e1) {
                e1.printStackTrace();
                return "exception";
            }
            try {
                int response_code = conn.getResponseCode();
                if (response_code == HttpURLConnection.HTTP_OK) {
                    InputStream input = conn.getInputStream();
                    BufferedReader reader = new BufferedReader(new InputStreamReader(input));
                    StringBuilder result = new StringBuilder();
                    String line;
                    while ((line = reader.readLine()) != null) {
                        result.append(line);
                    }
                    return (result.toString());
                } else {
                    return ("unsuccessful");
                }
            } catch (IOException e) {
                e.printStackTrace();
                return "exception";
            } finally {
                conn.disconnect();
            }
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pdLoading.setMessage("\tLoading...");
            pdLoading.setCancelable(false);
            pdLoading.show();
        }

        @Override
        protected void onPostExecute(String result) {
            if (result.equalsIgnoreCase("true")) {
                Intent intent = new Intent(SigninActivity.this, StudentActivity.class);
                intent.putExtra("emailfield", true);
                startActivity(intent);
                SigninActivity.this.finish();
            } else if (result.equalsIgnoreCase("false")) {
                // If username and password does not match display a error message
                pdLoading.cancel();
                Toast.makeText(SigninActivity.this, "Invalid email or password", Toast.LENGTH_LONG).show();
            } else if (result.equalsIgnoreCase("exception") || result.equalsIgnoreCase("unsuccessful")) {
                pdLoading.cancel();
                Toast.makeText(SigninActivity.this, "OOPs! Something went wrong. Connection Problem.", Toast.LENGTH_LONG).show();
            }
        }
    }








    public class BackGroundLogin2 extends AsyncTask<String, String, String> {
        public static final int CONNECTION_TIMEOUT = 10000;
        public static final int READ_TIMEOUT = 15000;

        ProgressDialog pdLoading = new ProgressDialog(SigninActivity.this);
        URL url = null;
        HttpURLConnection conn;

        @Override
        protected String doInBackground(String... params) {

            try {
                url = new URL("http://"+LoginActivity.ip.c+"/instantpick/loginhr.php");
            } catch (MalformedURLException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
                return "exception";
            }
            try {
                conn = (HttpURLConnection) url.openConnection();
                conn.setReadTimeout(READ_TIMEOUT);
                conn.setConnectTimeout(CONNECTION_TIMEOUT);
                conn.setRequestMethod("POST");
                conn.setDoInput(true);
                conn.setDoOutput(true);
                Uri.Builder builder = new Uri.Builder()
                        .appendQueryParameter("emailfield", params[0])
                        .appendQueryParameter("passwordfield", params[1]);
                String query = builder.build().getEncodedQuery();
                OutputStream os = conn.getOutputStream();
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(os, "UTF-8"));
                bufferedWriter.write(query);
                bufferedWriter.flush();
                bufferedWriter.close();
                os.close();
            } catch (IOException e1) {
                e1.printStackTrace();
                return "exception";
            }
            try {
                int response_code = conn.getResponseCode();
                if (response_code == HttpURLConnection.HTTP_OK) {
                    InputStream input = conn.getInputStream();
                    BufferedReader reader = new BufferedReader(new InputStreamReader(input));
                    StringBuilder result = new StringBuilder();
                    String line;
                    while ((line = reader.readLine()) != null) {
                        result.append(line);
                    }
                    return (result.toString());
                } else {
                    return ("unsuccessful");
                }
            } catch (IOException e) {
                e.printStackTrace();
                return "exception";
            } finally {
                conn.disconnect();
            }
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pdLoading.setMessage("\tLoading...");
            pdLoading.setCancelable(false);
            pdLoading.show();
        }

        @Override
        protected void onPostExecute(String result) {
            if (result.equalsIgnoreCase("true")) {
                Intent intent = new Intent(SigninActivity.this, RecruiterActivity.class);
                intent.putExtra("emailfield", true);
                startActivity(intent);
                SigninActivity.this.finish();

            } else if (result.equalsIgnoreCase("false")) {
                // If username and password does not match display a error message
                pdLoading.cancel();
                Toast.makeText(SigninActivity.this, "Invalid email or password", Toast.LENGTH_LONG).show();
            } else if (result.equalsIgnoreCase("exception") || result.equalsIgnoreCase("unsuccessful")) {
                pdLoading.cancel();
                Toast.makeText(SigninActivity.this, "OOPs! Something went wrong. Connection Problem.", Toast.LENGTH_LONG).show();
            }
        }
    }

}
